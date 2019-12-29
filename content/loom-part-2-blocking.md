---
title: Loom - Part 2 - Blocking code
date: 2019-12-18T12:11:16+01:00
description: Part 2 on a series of articles about OpenJDK's Project Loom
parent: blog
categories: ["java"]
tags: ["java", "concurrency"]
draft: false
seoimage: img/loom/loom.jpg
highlight: true
gallery: true
---

> Part 2 in a series of articles about Project Loom.  
> In this part we implement a proxy service, the easiest way possible.
>
> The companion code repository is at [arnaudbos/untangled](https://github.com/arnaudbos/untangled)
>
> If you'd like you could head over to  
> [Part 0 - Rationale][part-0]  
> [Part 1 - It's all about Scheduling][part-1]  
> [Part 3 - Asynchronous code][part-3]  

{{< img center="true" src="/img/loom/loom.jpg" alt="Loom" width="100%" title="Weaving" caption="Max Pixel" attr="CC0 1.0" attrlink="https://creativecommons.org/publicdomain/zero/1.0/deed.en" link="https://www.maxpixels.net/Post-Impressionist-Post-Impressionism-Fine-Art-Dutch-1428139">}}

-----

<!-- toc -->

## A simple use case

One of the biggest pain points I had learning about concurrent programming was the emphasis put on
`Blocking`, `Non-blocking`, `Synchronous` and `Asynchronous` code.    
We'll touch on all four in the next parts of this series.

After reading a lot I figured that implementing a simple use case would comfort my understandings and help me convey
what I wanted to explain. So here's a not totally made up, simple use case:

{{< gallery title="Use case: A proxy server" >}}
  {{% galleryimage file="/img/loom/demo.png"
  size="2530x1180" caption="Sequence diagram, also detailed in text in the following paragraphs."
  width="100%" %}}
{{< /gallery >}}

Clients need access to some restricted resources. A proxying service will rate-limit the clients access, based on a
token policy.

1. A client addresses a download request to the proxy service.
2. The proxy service registers the request to a coordinator.
3. The coordinator is responsible for the scheduling logic using a priority queue. Its logic is irrelevant.
4. The coordinator responds either "Unavailable" or "Available".
5. "Unavailable" means that the client can't download the resource just yet (responses' payload contains a token).
6. The payload also contains an "ETA" so the service can decide to abort or to retry, using the token to keep its place
   in the coordinator's queue.
7. The payload also contains a "wait" time to rate-limit the retry requests to the coordinator.
8. The proxy may have to retry a couple of times, on behalf of the client, but will eventually receive an "Available"
   response.
9. With an "Available" response and the token contained in its payload, the proxy service can initiate a download
   request to the data source.
10. The proxy service streams the content of the download request back to its client;
11. While at the same time sending periodic "heartbeat" requests to the coordinator to ensure its token is not revoked.

We are going to use this scenario in the next parts of the series. We will implement this proxy service in
various ways, simulate a few (200) clients connecting simultaneously and see the pros and cons of each implementation.

> Bear in mind that this is not a benchmark, it's just an experiment.  
> 200 clients is a really low number but is enough to observe a few interesting things. 

## First implementation

I am now going to show you the easiest way I came up with to implement this proxy service.

You can find the complete source code for this sample [here][chapter-1].

`getThingy()` is the download endpoint in this example. Each client "connecting" to the proxy hits this method:

```java
private void getThingy() throws EtaExceededException, IOException {
    println("Start getThingy.");

    try {
/*1*/   Connection.Available conn = getConnection();
        println("Got token, " + conn.getToken());

        Thread pulse = makePulse(conn);
/*2*/   try (InputStream content = gateway.downloadThingy()) {
/*3*/       pulse.start();

/*4*/       ignoreContent(content);
        } catch (IOException e) {
            err("Download failed.");
            throw e;
        }
        finally {
/*5*/       pulse.interrupt();
        }
    } catch (InterruptedException e) {
        // D'oh!
        err("Task interrupted.");
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
    }
    println("Download finished")
}
```

1. We retrieve an "Available" connection `conn` from the coordinator
2. We initiate the download request to the data source (called `gateway` in this example).
3. Once the download has begun we start the heartbeat requests (_pulse_ thread).
4. While at the same time consuming the content (`InputStream`, here we simply drop it, but in a real scenario we
   would forward to the client).  
5. Once the download ends (successfully or not) we stop sending heartbeat requests and the call ends.

Maybe you were hoping to see some kind of framework here, some _@Controller_ or _@GET_ annotation perhaps?

In this series, I'm not going to bother with a framework. Because the number of clients is so small and this is not a
benchmark, I am just simulating the client calls from within the same JVM.

This way, I am able to use the kind of `ExecutorService` I want for each implementation, in order to outline a few
things. This executor and its thread pool will simulate the Web Server thread pool that could be found inside any Web
Framework. In the end it is more illustrative to have it directly at hand.

Let's simulate a few clients, shall we?

```java
for(int i=0; i<MAX_CLIENTS; i++) {
    elasticServiceExecutor.submit(() -> {
        try {
            getThingy();
        } catch (EtaExceededException e) {
            err("Couldn't getThingy because ETA exceeded: " + e);
        } catch (Exception e) {
            err("Couldn't getThingy because something failed: " + e);
        }
    });
}
```

In this implementation, `elasticServiceExecutor` is a "cached" `ThreadPoolExecutor`, and I will explain why in a
little bit.

```java
elasticServiceExecutor =
    Executors.newCachedThreadPool(new PrefixedThreadFactory("service"));
```

We have a "Web Server", clients and our download controller.

There are a few things to unpack from this controller.  
Let's start with the least interesting bit first, `makePulse`:

```java
private Thread makePulse(Connection.Available conn) {
    return new Thread(() -> {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                // Periodic heartbeat
                Thread.sleep(2_000L);

                println("Pulse!");
                coordinator.heartbeat(conn.getToken());
            } catch (InterruptedException e) {
                // D'oh!
                Thread.currentThread().interrupt();
            }
        }
    });
}
```

Nothing fancy here: sleep for a while, send a request, sleep for a while, send a request, ...

You may have noticed that _getThingy_ uses the `gateway` service to talk to the data source and that _makePulse_ uses
the `coordinator` service to talk to the coordinator.  
So what is this `getConnection` method and why does _getThingy_ not use `coordinator` directly?

Because of the retry logic we've talked about before!  
`getConnection` is actually a helper to handle
the _Unavailable_ responses from the coordinator and only return when an _Available_ response has been received. Here's
the code:

```java
private Connection.Available getConnection()
    throws EtaExceededException, InterruptedException
{
/*1*/return getConnection(0, 0, null);
}

private Connection.Available getConnection(long eta, long wait, String token)
    throws EtaExceededException, InterruptedException
{
    for(;;) {
        if (eta > MAX_ETA_MS) {
            throw new EtaExceededException();
        }

        if (wait > 0) {
            Thread.sleep(wait);
        }

        println("Retrying download after " + wait + "ms wait.");

/*2*/   Connection c = coordinator.requestConnection(token);
        if (c instanceof Connection.Available) {
/*4*/       return (Connection.Available) c;
        }
/*3*/   Connection.Unavailable unavail = (Connection.Unavailable) c;
        eta = unavail.getEta();
        wait = unavail.getWait();
        token = unavail.getToken();
    }
}
```

1. We start with initial `eta` and `wait` times at zero and no token (`null`).
2. We try to get a grant from the coordinator.
3. If the coordinator rejects us with an `Unavailable` response, we update the _eta_, _wait_ and _token_ and loop;
4. Otherwise we can return the `Available` response.

As I said: the **easiest** implementation I could come up with.

**Any Java developer from junior to expert can understand this code!**  
It is _classic_, _old_, _boring_, _imperative_ Java code which ***does the job***.

And _easy_ is important, right? I'm 100% confident that all of you know what the service does and how it does it. We
can now build from here with all the subsequent implementations, using different paradigms and APIs.

Before that, let's now see what this code _actually_ does.

## Profiling

The first tool I turned to is [VisualVM]. In the absence of metrics from the code, defaulting to VisualVM gives a basic
understanding of the behaviour of a JVM application regarding its thread and objects allocation, CPU utilization, GC
pressure, etc.

### CPU usage

{{< gallery title="CPU utilization is low" >}}
  {{% galleryimage file="/img/loom/impl1-cpu.png"
  size="3124x1730"
  width="100%" %}}
{{< /gallery >}}

CPU usage is really limited in this example (at a guess, I'd say the 95 percentile is less than 3% with an outlier at
about 8% for a very short time during startup). Which makes sense, right?

Indeed, this use case is designed to be _I/O bound_: it's not like we're computing any math. Instead
we send a bunch or requests, wait a little in between, then some more requests to forward content from
buffers and... Done.

None of this requires a lot of CPU power so this screenshot should not be surprising. In fact, it will be the same
for all the other implementations we will see in the next parts so I am not going to embed it again.

Looking at the threads is much more interesting.

### Threads

{{< gallery title="We're creating a lot of threads" >}}
  {{% galleryimage file="/img/loom/impl1-threads.png"
  size="3110x1830"
  width="100%" %}}
{{< /gallery >}}

We can see in the figure above that when the application starts, meaning "when our clients connect", it is going to
create a first batch of about 200 threads. And then progressively start 200 more over a period of about 5 seconds.

The last 200 are pretty obvious given the implementation of `makePulse`: once the proxy begins to receive `Available`
responses from the coordinator, it starts the threads instantiated by the calls to `makePulse`. This is just an
implementation detail. A wrong one for sure, but a minor detail.

What should be more intriguing are the first 200 (the 10 additional ones are created by the JVM itself, the net
addition from our application is of 200). Why are 200 clients creating 200 threads?

{{< gallery title="200+ threads \"running\"" >}}
  {{% galleryimage file="/img/loom/impl1-threads-running.png"
  size="2820x1786"
  width="100%" %}}
{{< /gallery >}}

They all seem pretty busy (green means "running" in VisualVM), which is weird. We've seen that CPU usage is really low,
so our cores don't actually do much in practice!

We must take a closer look at what those threads actually do. Let's get a _thread dump_.

{{< gallery title="Typical thread from this application" >}}
  {{% galleryimage file="/img/loom/impl1-thread-dump.png"
  size="2898x976"
  width="100%" %}}
{{< /gallery >}}

This screenshot shows only one of the many threads described in the thread dump because they all look alike.

The typical thread in this application seems to be running this `SocketDispatch#read0` native method. And they aren't
"just" running this method but in fact spending **most of their time** running it.

{{< gallery title="Flame Graph" >}}
  {{% galleryimage file="/img/loom/impl1-flame.png"
  size="2158x1548"
  width="100%" %}}
{{< /gallery >}}

This [flame graph] was acquired using [async-profiler] and shows that time spent running `SocketDispatcher#read0`'s
underlying `read` system call dominates our application.

If we track its call stack to find its origin, we stumble upon `lambda$run$1`. Which, in fact, is the call to the
astutely named `blockingRequest` method inside of the _gateway_ service:

```java
class SyncGatewayService {
    InputStream downloadThingy() throws IOException {
        return blockingRequest(
            "http://localhost:7000",
            String.format(HEADERS_TEMPLATE,
                          "GET",
                          "download",
                          "text/*",
                          String.valueOf(0))
        );
    }
}
```

Without further suspense, here's its code:

```java
public static InputStream blockingRequest(String url, String headers)
    throws IOException
{
    println("Starting request to " + url);
    URL uri = new URL(url);
    SocketAddress serverAddress =
        new InetSocketAddress(uri.getHost(), uri.getPort());
    SocketChannel channel = SocketChannel.open(serverAddress);
    ByteBuffer buffer = ByteBuffer.wrap(
        (headers + "Host: " + uri.getHost() + "\r\n\r\n").getBytes()
    );
    do {
        channel.write(buffer);
    } while(buffer.hasRemaining());

    return channel.socket().getInputStream();
}
```

You can see in the call chain that `read0` originates from calling `InputStream#read`. The _InputStream_ itself is
obtained from the `SocketChannel`. And this, dear reader, is the ugly detail that makes this application not
efficient and is the reason why we end up with as many threads as clients.

Because this socket channel is written to and read from in **blocking mode**.

## Context switches

### What they are

What's a blocking call and why is it bad? Let's talk about what happens when one of our threads encounters a thread
blocking call.

For the sake of simplicity, I am going to assume here that our CPUs run two kinds of instructions.

{{< gallery title="Legend" >}}
  {{% galleryimage file="/img/loom/engine.png"
  size="1928x770"
  width="100%" %}}
{{< /gallery >}}

Instructions coming from what we'll call our "user code" represented by the triangle, hexagon, square and round shapes.  
And instructions coming from the kernel whose goals are to enforce scheduling policies represented by the circle and
cross shapes.  
The CPU will be represented by a [Wankel engine].

{{< gallery title="Kernel threads" >}}
  {{% galleryimage file="/img/loom/context-switch-1.png"
  size="3080x1046"
  width="100%" %}}
{{< /gallery >}}

On the JVM, the threads we manipulate are actually kernel threads. The threads which are instantiated and managed by
the various flavors of `ExecutorService` available via the helper `java.util.concurrent.Executors` are an abstraction
over native threads with additional thread pool logic, tasks queues management and scheduling mechanisms.

As said earlier, the executor I've used in this implementation is a "cached" `ThreadPoolExecutor`.

```java
elasticServiceExecutor =
    Executors.newCachedThreadPool(new PrefixedThreadFactory("service"));
```

This executor handles an initial pool of threads as well as a task queue (`SynchronousQueue`).  
It also has a reference to a `ThreadFactory`, because this executor will try to match each `Runnable` that is submitted
to it via its `submit` method to a runnable thread in its pool. If no thread is available to run the next _Runnable_ in
the queue, it will use the _ThreadFactory_ to create a new thread and hand the _Runnable_ object to it.

The threads thus created are managed by the kernel, which itself manages its own priority queue and acts according to
its scheduling policy (we've talked about this in the [part 1 of this series][part-1]). The priority queue shown in the
illustration above is the kernel's.

So, in a nutshell, when a thread is scheduled to run, its instructions are executed one after the other by the CPU.  
Up until it finishes or a blocking call is made.

{{< gallery title="Close Encounters of the Blocking Kind" >}}
  {{% galleryimage file="/img/loom/context-switch-2.png"
  size="3038x1014"
  width="100%" %}}
{{< /gallery >}}

In the illustration above, the "round" instructions come from the currently scheduled thread. We can see that the
current instruction is in fact a blocking OS call (syscall, such as `read`).

What will actually happen here, is a **context switch**.  
Because the thread is currently trying to execute an action _outside_ its current [protection ring].

JVM applications run in _user space_ (ring 3) to ensure memory and hardware protection.  
The kernel runs in _kernel space_ (ring 0) and is responsible to ensure computer security and that processes behave,
basically.

When executing syscalls such as blocking `read`s, _kernel space_ access level is required. Kernel instructions will be
run on behalf of the "user code" and will, for instance, ensure that this thread does not hold onto the CPU while
waiting for its call to return and that another thread has a chance to run in the mean time, hence ensuring compliance
with the scheduling policies.

For the sake of simplicity, I'm representing a context switch as a 2 step process.

{{< gallery title="A context switch" >}}
  {{% galleryimage file="/img/loom/context-switch-3.png"
  size="3016x1036"
  width="100%" %}}
{{< /gallery >}}

During the first step, the kernel is going to _suspend_ the execution of the current thread. In order to do this,
it is going to save a few things such as the current instruction or process counter (on which instruction did the
thread pause), the thread's current call stack, the state of CPU registers it was accessing, etc.

{{< gallery title="1/2: save execution state" >}}
  {{% galleryimage file="/img/loom/context-switch-4.png"
  size="2990x1024"
  width="100%" %}}
{{< /gallery >}}

The kernel is going to save all this in a data structure (see [PCB], for Process Control Block), and put the thread
back into the priority queue.

> Also for the sake of simplicity, I am representing the "not ready" state of a thread as if flagged and put back into
> a priority queue. But the actual logic may be more complicated, including several distinct queues for different
> "waiting" purposes or any arbitrary logic as kernel developers see fit.

{{< gallery title="2/2: schedule next" >}}
  {{% galleryimage file="/img/loom/context-switch-5.png"
  size="2996x1014"
  width="100%" %}}
{{< /gallery >}}

In the second step, the kernel decides which thread should be scheduled next according to its policies, and this thread
is allocated to the CPU. If this thread had been scheduled before, its state would have to be restored first, of
course.

This thread itself may contain instructions pointing at a blocking syscall, which would trigger a new context switch,
and so on and so forth until eventually the result of the blocking syscall made by the round instruction above is
available and this thread is scheduled again.

{{< gallery >}}
  {{% galleryimage file="/img/loom/context-switch-6.png"
  size="3036x1052"
  width="395px" %}}
  {{% galleryimage file="/img/loom/context-switch-7.png"
  size="3014x1002"
  width="395px" %}}
  {{% galleryimage file="/img/loom/context-switch-8.png"
  size="3046x1016"
  width="396px" %}}
  {{% galleryimage file="/img/loom/context-switch-9.png"
  size="3066x1034"
  width="390px" %}}
{{< /gallery >}}

Now it's time to connect the dots and understand why blocking calls deserve such hatred.

### Why they are bad

One of the blog posts I like the most to explain this issue is
_"Little's Law, Scalability and Fault Tolerance: The OS is your bottleneck (and what you can do about it)"_ by 
{{< twitter pressron >}}.

{{< img center="true" src="/img/loom/little.png" alt="Little's Law, Scalability and Fault Tolerance: The OS is your bottleneck (and what you can do about it)" width="100%" link="https://blog.paralleluniverse.co/2014/02/04/littles-law/">}}

I'm trying to do half as good in this series, so I strongly suggest that you take a look at it and read _at least_ the
first 3 parts of the article: _"Our Little Service"_, _"Little’s Law"_ and _"What Dominates the Capacity"_.

It explains that the number of connections our services can handle when executing blocking code is
not limited by the number of network connections our OS can keep open, but by the number of threads we create.  
Each kernel thread stack takes memory space and thread scheduling (context switches explained above) wastes CPU cycles
and adds latency to requests.

> Allowing the software to spawn thread willy-nilly may bring our application to its knees, so we usually set a hard
> limit on the number of threads we let the application spawn.  
> — R. Pressler

So we know that we can't let the number of threads grow too much.  
But why is this code creating one thread-per-connection (not to mention the additional "pulsing" thread)?

The answer is: because of the cached `ThreadPoolExecutor`!  
As I said:

> If no thread is available to run the next _Runnable_ in the queue, it will use the _ThreadFactory_ to create a new
> thread and hand the _Runnable_ object to it.

In this implementation, each request issues blocking writes and reads to and from the `SocketChannel`.  
Each of these calls lead to context switches during which the current thread will be paused.  
So connection requests added to the `ThreadPoolExecutor` waiting queue will quickly drain the number of threads
cached in the pool, because they are paused!  
This triggers the creation of more threads by the executor and {{< emoji content=":boom:" >}}

We could use a different executor, such as `Executors.newFixedThreadPool(int, ThreadFactory)` in order to _"limit the
number of threads we let the application spawn"_. By doing so we explicitly limit the number of connections our service
can handle.

> We could, of course, buy more servers, but those cost money and incur many other hidden costs. We might be
> particularly reluctant to buy extra servers when we realize that software is the problem, and those servers we
> already have are under-utilized.  
> — R. Pressler

## Conclusion

This part of the series presented a use case from which we can build upon and
experiment to try various solutions to the problem of blocking calls and scalability.

I hope you understand a little more about blocking calls and context switches after reading this.

In the [next part][part-3], we will take a look at asynchronous calls.

[part-0]: ../loom-part-0-rationale
[part-1]: ../loom-part-1-scheduling
[part-3]: ../loom-part-3-async
[part-4]: ../loom-part-4-nio
[VisualVM]: https://visualvm.github.io/
[flame graph]: http://www.brendangregg.com/flamegraphs.html
[async-profiler]: https://github.com/jvm-profiling-tools/async-profiler
[PCB]: https://www.tutorialspoint.com/what-is-process-control-block-pcb
[protection ring]: https://en.wikipedia.org/wiki/Protection_ring
[Wankel engine]: https://en.wikipedia.org/wiki/Wankel_engine
[chapter-1]: https://github.com/arnaudbos/untangled/blob/master/hawaii/src/main/java/io/monkeypatch/untangled/Chapter01_SyncBlocking.java