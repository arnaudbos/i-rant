---
title: Loom - Part 3 - Asynchronous code
date: 2019-12-23T18:56:06+01:00
description: Loom
parent: blog
categories: ["java"]
tags: ["java", "concurrency"]
draft: false
seoimage: img/loom/handweaving.jpg
highlight: true
gallery: true
---
---

> Part 3 in a series of articles about Project Loom.  
> In this part we re-implement our proxy service with an asynchronous API.
>
> If you'd like you could head over to  
> [Part 0 - Writing for the past me][part-0]  
> [Part 1 - It's all about Scheduling][part-1]  
> [Part 2 - Blocking code][part-2]  

{{< img center="true" src="/img/loom/handweaving.jpg" alt="Seamstresses in a shop" width="100%" title="Atelier de couture" caption="J. Trayer 1854" attr="CC0 1.0" attrlink="https://creativecommons.org/publicdomain/zero/1.0/deed.en" link="https://commons.wikimedia.org/wiki/File:Jean_Baptiste_Jules_Trayer_Bretonische_Schneiderinnen_1854.jpg">}}

-----

<!-- toc -->

We've covered a lot of ground in the previous entries and we concluded that blocking code _"is bad" ©_.  
But what can we do about it?

While researching, I've read many blog posts presenting `asynchronous` programming
as a solution. It makes sense: our kernels use preemptive scheduling. So there's nothing to do when the scheduler
time-slices a long-running thread. But we **can** maximize efficiency! By _being
careful_ to avoid blocking calls and use asynchronous APIs.

We will write code that, instead of blocking, will call an async API. This call will return the control of execution
immediately (so we can execute other instructions) and only notify us when the result is ready.

Let's take a look at the second implementation I've made of the proxy service I've presented in the [previous entry][part-2]:

## Async API

Changing an API from synchronous to asynchronous seems simple at first.  
From the inner out, we create a new function, `asyncRequest`, as an asynchronous alternative to
`blockingRequest`.

```java
public void asyncRequest(ExecutorService, String, String, CompletionHandler)
```

_asyncRequest_ has to return immediately to not block the calling thread, so we give it an
`ExecutorService`. This also benefits us to explicit about which pool is used. It also takes a _String_ for the URL, another for the
headers, and also a `CompletionHandler`.  
_CompletionHandler_ is an interface one has to implement. Its methods are called by _asyncRequest_
once the result of the request is available: one callback in case of success, another in case of error.

```java
public interface CompletionHandler<V> {
    void completed(V result);
    void failed(Throwable t);
}
```

I'll spare you the request details hidden inside the intermediary
`CoordinatorService#requestConnection(String, CompletionHandler, ExecutorService)`. Instead, let's get to the new version
of `getConnection` we've talked about in the previous entry:

```java
private void getConnection(CompletionHandler<Connection.Available> handler) {
    getConnection(0, 0, null, handler);
}

private void getConnection(long eta,
                           long wait,
                           String token,
                           CompletionHandler<Connection.Available> handler)
{
    if (eta > MAX_ETA_MS) {
        if (handler!=null) handler.failed(new EtaExceededException());
    }

    boundedServiceExecutor.schedule(() -> {
        println("Retrying download after " + wait + "ms wait.");

        coordinator.requestConnection(
            token,
            new CompletionHandler<>() {
                // ... We'll see this later
            },
            boundedServiceExecutor);
    }, wait, TimeUnit.MILLISECONDS);
}
```

The signatures are like the synchronous ones, except for the extra `CompletionHandler<Connection.Available>`
handler.  
But instead of making the request we `schedule` it to the `boundedServiceExecutor`.

_boundedServiceExecutor_ is an instance of `ScheduledThreadPoolExecutor`.

```java
boundedRequestsExecutor =
    Executors.newScheduledThreadPool(10, new PrefixedThreadFactory("requests"));
```

In fact, _boundedServiceExecutor_ is not the only thread pool used in this implementation.  
To make things explicit about where each task runs, I've create 3 dedicated executors:

```java
// One from which service methods are called and coordinator requests are sent
boundedServiceExecutor =
    Executors.newScheduledThreadPool(10, new PrefixedThreadFactory("service"));
// One from which gateway (download) requests are sent
boundedRequestsExecutor =
    Executors.newScheduledThreadPool(10, new PrefixedThreadFactory("requests"));
// One from which heartbeat resquests are scheduled
boundedPulseExecutor =
    Executors.newScheduledThreadPool(10, new PrefixedThreadFactory("pulse"));
```

> Note that both `boundedServiceExecutor` and `boundedRequestsExecutor` could have been instances of
> `ThreadPoolExecutor` (using `Executors#newFixedThreadPool(int, ThreadFactory)`)  
> rather than `ScheduledThreadPoolExecutor`;  
> because only heartbeat requests (using `boundedPulseExecutor`) must be delayed.  
> But both being fix-sized pools, the result is the same in this case.

Like the synchronous example, `getConnection` deals with the retry logic. But
`CoordinatorService#requestConnection` is asynchronous and takes a _CompletionHandler_, because it calls to
`asyncRequest`. So we have to implement both success and error callback methods.

```java
private void getConnection(long eta,
                           long wait,
                           String token,
                           CompletionHandler<Connection.Available> handler)
    if (eta > MAX_ETA_MS) {
        if (handler!=null) handler.failed(new EtaExceededException());
    }

    boundedServiceExecutor.schedule(() -> {
        println("Retrying download after " + wait + "ms wait.");

        coordinator.requestConnection( //(1)
            token,
            new CompletionHandler<>() {
                @Override
                public void completed(Connection c) { //(2)
                    if (c instanceof Connection.Available) {
                        if (handler!=null)
                            handler.completed((Connection.Available) c); //(3)
                    } else {
                        Connection.Unavailable unavail =
                            (Connection.Unavailable) c; //(4)
                        getConnection(
                            unavail.getEta(),
                            unavail.getWait(),
                            unavail.getToken(),
                            handler);
                    }
                }

                @Override
                public void failed(Throwable t) { //(5)
                    if (handler!=null) handler.failed(t);
                }
            },
            boundedServiceExecutor);
    }, wait, TimeUnit.MILLISECONDS);
}
```

1. In case of success of the underlying _requestConnection > asyncRequest_ call chain.
2. The `completed` method will be called with the response from the coordinator. The answer could be positive or negative.
3. In case of `Available`, we're done; we can complete the completion _handler_ passed to `getConnection`. Its caller can
   be notified of the success and proceed (with the download).
4. In case of `Unavailable`, we hide the retry logic and _reschedule_ the call to _requestConnection_ to the executor
   by recursively calling `getConnection` with updated parameters.
5. In case of failure we simply propagate the error to `getConnection`'s caller.

The callbacks already make this logic cluttered enough; but we're not done! We must now implement `getThingy`, our
service method which calls to `getConnection` and then start the download request.

```java
private void getThingy(int i, CompletionHandler<Void> handler) {
    println("Start getThingy.");

    getConnection(new CompletionHandler<>() { //(1)
        @Override
        public void completed(Connection.Available conn) { //(2)
            println("Got token, " + conn.getToken());

            CompletableFuture<Void> downloadFut = new CompletableFuture<>();
            gateway.downloadThingy(new CompletionHandler<>() { //(3)
                @Override
                public void completed(InputStream content) { //(4)
                    // Download started
                }

                @Override
                public void failed(Throwable t) { //(6)
                    if (t instanceof EtaExceededException) {
                        err("Couldn't getThingy because ETA exceeded: " + t);
                    } else {
                        err("Couldn't getThingy because something failed: " + t);
                    }
                    if (handler!=null) handler.failed(t);
                }
            }, boundedServiceExecutor); //(5)

        }

        @Override
        public void failed(Throwable t) { //(6)
            err("Task failed.");
            if (handler!=null) handler.failed(t);
        }
    });
}
```

1. We've seen above that `getConnection` takes a completion handler.
2. Its `complete` method will be called when we successfully get a download authorization from the coordinator.
3. On _successful_ completion, the download starts, which materializes calling `gateway.downloadThingy`.
  `downloadThingy` is, itself, asynchronous because it also calls down to `asyncRequest`, so we must give it a new
  _CompletionHandler_.
4. This completion handler is passed an `InputStream` once we connect to the data source. We
   can then read the content and forward it to this service's client. The consumption of the _InputStream_ and
   forwarding to the client is omitted for now and replaced by the "Download started" comment.
5. `downloadThingy`'s last parameter is an optional executor, used to specify which pool is used to run the completion
   handler. If omitted, it is the calling thread, but in this case, we specify that we want the content
   consumption/forwarding to happen on the `boundedServiceExecutor`.
6. In case of failure from `getConnection` or `downloadThingy`, we complete `getThingy`'s completion handler with a
   failure.

Now that we have the structure, we can handle the content, but also start the periodic heartbeat requests!

```java
Runnable pulse = new PulseRunnable(i, downloadFut, conn); //(2)
int total = 0;
try(content) { //(1)
    println(i + " :: Starting pulse ");
    boundedPulseExecutor.schedule(pulse, 2_000L, TimeUnit.MILLISECONDS); //(2)

    // Get read=-1 quickly and not all content
    // because of HTTP 1.1 but really don't care
    byte[] buffer = new byte[8192];
    while(true) { //(3)
        int read = content.read(buffer);
        // drop it
        if (read==-1 || (total+=read)>=MAX_SIZE) break;
    }

    println("Download finished");

    if (handler!=null)
        handler.completed(null); //(4)
} catch (IOException e) {
    err("Download failed.");
    if (handler!=null)
        handler.failed(e); //(4)
} finally {
    downloadFut.complete(null); //(5)
}
```

Remember this code executes inside `downloadThingy`'s completion handler, so when this code runs, the connection
to the data source is established.

1. We encapsulate the logic inside a `try-with-resource` block around `content` (the _InputStream_) because it can
   fail at any moment.
2. We've initialized a `PulseRunnable`, which we can now schedule to send heartbeat requests. It will reschedule itself
   upon completion. The reference to the _Future_ `downloadFut` allows to stop sending heartbeats
   when the download ends. We'll see `PulseRunnable` later.
3. In the mean time, we consume the _InputStream_ and just ignore the content once again (not important).
4. Finally, when the download stops, on success or on error, we _complete_ `getThingy`'s handler accordingly.
5. We don't forget to complete `downloadFut` either, so the heartbeats stop.

Almost done. We now look at `PulseRunnable`:

```java
class PulseRunnable implements Runnable {
    private int i;
    private Future<Void> download;
    private Connection.Available conn;

    PulseRunnable(int i, Future<Void> download, Connection.Available conn) {
        this.i = i;
        this.download = download;
        this.conn = conn;
    }

    @Override
    public void run() {
        if (!download.isDone()) {
            println(i + " :: Pulse!");
            coordinator.heartbeat( //(1)
                conn.getToken(),
                new CompletionHandler<>() {
                    @Override
                    public void completed(Connection result) { //(2)
                        if (!download.isDone()) { //(3)
                            boundedPulseExecutor
                                .schedule(PulseRunnable.this, 2_000L, TimeUnit.MILLISECONDS);
                        }
                    }

                    @Override
                    public void failed(Throwable t) { //(2)
                        if (!download.isDone()) { //(3)
                            boundedPulseExecutor
                                .schedule(PulseRunnable.this, 2_000L, TimeUnit.MILLISECONDS);
                        }
                    }
                },
                boundedPulseExecutor
            );
        } else {
            println(i + " :: Pulse stopped.");
        }
    }
}
```

Nothing too fancy.

1. When started, the runnable calls the asynchronous `CoordinatorService#heartbeat` method with the token,
   _completion handler_ and executor. The executor is responsible to run the handler's methods (`boundedServiceExecutor` in
   this case, like when calling `GatewayService#downloadThingy` above).
2. We ignore heartbeat results, whether successes or failures,
3. And schedule a new heartbeat request as long as the download _Future_ is not "done".

Finally, the last piece of the puzzle, the clients calling the service:

```java
CompletableFuture<Void>[] futures = new CompletableFuture[MAX_CLIENTS];
for(int i=0; i<MAX_CLIENTS; i++) {
    int finalI = i;
    futures[i] = new CompletableFuture<>();
    getThingy(finalI, new CompletionHandler<>() {
        @Override
        public void completed(Void result) {
            futures[finalI].complete(result);
        }

        @Override
        public void failed(Throwable t) {
            futures[finalI].completeExceptionally(t);
        }
    });
}
```

Phew...

That certainly wasn't easy code. Not like the synchronous code we've seen in the [previous entry][part-2]! The problem
is still simple though, so it tells a lot about asynchronous programming: it's a **pain in the butt**.

The logic is all over the place! Asynchronous APIs forces us to split our logic into pieces, but not the pieces we'd
like. A perfectly self-contained function in synchronous programming would have to be split into two to three (if not more)
callbacks and suddenly it's not easy to reason about the code anymore.  
This problem has a name: **[Callback Hell]** (and no, it's not just JavaScript, I mean just look at the code above).

## Profiling

If async is the answer to write efficient services that make the most out of server resources, maybe it's worth the
pain!

Profiling this code revealed that CPU usage was still low, more or less like in the previous implementation.
Threads are more interesting:

{{< gallery title="Not too many threads" >}}
  {{% galleryimage file="/img/loom/impl2-threads.png"
  size="2522x1496"
  width="100%" %}}
{{< /gallery >}}

200 clients, 30 threads, everything is proceeding as I have foreseen. But wait... this is very slow!  
This screenshot shows only the first minute of runtime and then the rest of the chart looks the same.

The previous implementation created a bunch of threads but at least finished quickly, less than a minute.  
Why is it so slow?

[VisualVM] to the rescue:

{{< gallery title="Three thread pools" >}}
  {{% galleryimage file="/img/loom/impl2-threads-running.png"
  size="2602x1730"
  width="100%" %}}
{{< /gallery >}}

Let's refer to the implementation to find what threads, from each thread pool (`request`, `pulse` and `service`)
is doing what.

### Request threads

Threads whose names begin with `request` come from `boundedRequestsExecutor`. The only place where this _Executor_ is
used is inside `downloadThingy`, which I had omitted before. Here it is:

```java
class GatewayService {
    void downloadThingy(CompletionHandler<InputStream> handler,
                        ExecutorService handlerExecutor)
{
    asyncRequest(
        boundedRequestsExecutor,
        "http://localhost:7000",
        String.format(HEADERS_TEMPLATE, "GET", "download", "text/*", String.valueOf(0)),
        new CompletionHandler<>() {
            @Override
            public void completed(InputStream result) {
                if (handler != null)
                    if (handlerExecutor!=null) {
                        handlerExecutor.submit(
                          () -> handler.completed(result)
                        );
                    } else {
                        handler.completed(result);
                    }
            }

            @Override
            public void failed(Throwable t) {
                if (handler != null)
                    if (handlerExecutor!=null) {
                        handlerExecutor.submit(
                            () -> handler.failed(t)
                        );
                    } else {
                        handler.failed(t);
                    }
            }
        });
    }
}
```

The code of `asyncRequest` is secret sauce for the moment, suffice to say that the executor it is given
(`boundedRequestsExecutor` in this case) serves only to call its `submit(Runnable)` method.

So the `request-*` threads in the VisualVM screenshot above are all doing the same thing: execute an `asyncRequest`
and pass the result to whoever the caller is.

### Pulse threads

Threads whose names begin with `pulse` come from `boundedPulseExecutor`. The only places where this _Executor_ is
used is when scheduling the heartbeat requests from within `getThingy` and from the "pulse" _Runnable_ itself:

`boundedPulseExecutor.schedule(pulse, 2_000L, TimeUnit.MILLISECONDS);`

It is also given as the last parameter to `CoordinatorService#heartbeat` to execute its _completion
handler_:

```java
@Override
public void completed(InputStream is) {
    Runnable r = () -> {
        if (handler != null)
            handler.completed(parseToken(() -> is));
    };
    if (handlerExecutor!=null) {
        handlerExecutor.submit(r);
    } else {
        r.run();
    }
}
```

So the `pulse-*` threads in the VisualVM screenshot above are doing small things too: execute a heartbeat
`asyncRequest` and a bit of `parseToken`, which consists in decoding a few bytes.

### Service threads

Threads whose names begin with `service` come from `boundedServiceExecutor` and are used in several places.

* in `getConnection`, to submit the call to `CoordinatorService#requestConnection`
* to execute the _completion handler_ of `CoordinatorService#requestConnection`
* to execute the _completion handler_ of `GatewayService#downloadThingy`
* to execute the calls to `asyncRequest` inside `CoordinatorService#requestConnection` and `CoordinatorService#heartbeat`

So the `service-*` threads in the VisualVM screenshot above are doing a few asynchronous calls and also a bit of
callback execution.

### A word about ScheduledThreadPoolExecutor

It's time to introduce the `ScheduledThreadPoolExecutor`.

Contrary to cached `ThreadPoolExecutor`, it doesn't spawn new threads to keep up with the number of _Runnable_.
Instead, it stays with the number of threads it is passed when created.

{{< gallery title="Async all the things!" >}}
  {{% galleryimage file="/img/loom/async-all-the-things.png"
  size="2990x1288"
  width="100%" %}}
{{< /gallery >}}

Each thread runs an infinite loop, executing _Runnable_ after _Runnable_.  
When using this kind of executor, the goal is to execute non-blocking calls. Blocking calls suspend the
thread until the result is available, thus preventing the following runnables from executing!

As seen in [Part 2][part-2], we want to avoid blocking calls. Especially with this kind of executor, because
it becomes possible to bring the whole application to a halt!

Fortunately, our whole implementation is asynchronous!  
Every method of our service submits tasks to an executor and there are callbacks all over the place. 

## Beware of the lurking blocking call

We now have a bit more context on what each thread does. Let's take another look at the VisualVM screenshot:

{{< gallery title="\"Service\" thread pool's threads are busy" >}}
  {{% galleryimage file="/img/loom/impl2-threads-running.png"
  size="2602x1730"
  width="100%" %}}
{{< /gallery >}}

The `request-*` and `pulse-*` threads spend most of their time `parked`.

Each thread in those pools looks for tasks to execute (_runnables_) coming down the executor's queue.
But because the only tasks submitted to them are asynchronous, their execution are quick! Soon, the
queue is drained, and when there's nothing to do, the executor will park the threads so they don't
[busy wait][busy-waiting].

The `service-*` threads, however, spend all their time running. And it is as weird as in the previous entry:
we've supposed to execute only asynchronous methods and compute nothing. I'll spare you the thread dump, let's
jump to the [flame graph]:

{{< gallery title="Bummer!" >}}
  {{% galleryimage file="/img/loom/impl2-flame.png"
  size="2910x1520"
  width="100%" %}}
{{< /gallery >}}

**There's a blocking call lurking in a dark corner of our "beautiful" asynchronous API!**

And if you didn't see it coming, maybe you understand why I didn't show the code of `asyncRequest` before.
Let's look:

```java
public static void asyncRequest(ExecutorService executor,
                                String url,
                                String headers,
                                CompletionHandler<InputStream> handler)
{
    executor.submit(() -> {
        try {
            InputStream is = blockingRequest(url, headers);
            if (handler!=null)
                handler.completed(is);
        } catch (Exception e) {
            if (handler!=null)
                handler.failed(e);
        }
    });
}
```

Yup, _"Mischief managed"_.  
Under the hood, `asyncRequest` calls down to `blockingRequest` and its blocking `SocketChannel`/`InputStream`!

The reason why the `service-*` threads look busy is that they are actually blocked. And this explains the question I
was asking before: "Why is it so slow?". Which is, because those threads are from a pool managed by a fixed-size
`ScheduledThreadPoolExecutor`, so no more than 10 simultaneous requests can be running. But the threads are being
blocked, preventing other tasks submitted to the executor from running.

{{< gallery title="Still thread-blocking" >}}
  {{% galleryimage file="/img/loom/still-thread-blocking.png"
  size="3086x1558"
  width="100%" %}}
{{< /gallery >}}

Thus, the net effect of changing this API from synchronous to asynchronous has been a latency increase. Because the
jobs keep piling up in the executor's queue!

## Conclusion

Some blog posts present asynchronous programming as a solution to the problem of scaling a service. This bothers me.

They often use `asynchronous` in opposition to `blocking`. But the opposite to `blocking` is `non-blocking`.
However, using _non-blocking_ alone conflates not blocking the ***current thread*** with not blocking ***any thread***.
`Non-blocking` is not synonymous to `non-thread-blocking`!

As seen above, using an asynchronous API doesn't mean anything about its thread-blocking properties.
An asynchronous call submitted to an executor doesn't block the current thread, for sure, but it may very well block
the thread it is running on!  
This is usually where you read comments such as "use a dedicated thread pool". Indeed, you
want to dedicate a thread pool for those blocking calls to avoid impacting the rest of your application. But you're left
with two choices. Either use an unbounded thread pool, risking unpredictable behaviour, or use a bounded one, risking
increased latency for those tasks.

I prefer to think about _blocking_ and _non-blocking_ as runtime properties.  
Likewise, I much prefer using _asynchronous_ in opposition to _synchronous_ when talking about an API or _programming
style_. **Conflating sync/async and blocking/non-blocking is a source of confusion.** In fact, I argue that both are
orthogonal.  

But there's a reason why both are often mixed-up.  
Today, the only—i.e. built-in— way to execute `non-thread-blocking` code *on the JVM* is to use `asynchronous` API.

In the next part, we'll re-implement `asyncRequest` to be truly non-blocking.

## Post Scriptum

Writing asynchronous code is hard.

{{< gallery title="It's over 800!" >}}
  {{% galleryimage file="/img/loom/vegeta.png"
  size="3016x1822"
  width="100%" %}}
{{< /gallery >}}

On the left a peak to 122 live threads when I did a small mistake.  
On the right a peak to more that 800 live threads when I messed up on purpose to see the result (I swear!).

[part-0]: ../loom-part-0-rationale
[part-1]: ../loom-part-1-scheduling
[part-2]: ../loom-part-2-blocking
[VisualVM]: https://visualvm.github.io/
[flame graph]: http://www.brendangregg.com/flamegraphs.html
[async-profiler]: https://github.com/jvm-profiling-tools/async-profiler
[Callback Hell]: http://callbackhell.com/
[busy-waiting]: https://en.wikipedia.org/wiki/Busy_waiting