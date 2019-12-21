---
title: Loom - Part 3 - Asynchronous code
date: 2019-12-19T10:19:35+01:00
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

We've covered a lot of ground in the previous entries and we've come to the conclusion that blocking code _"is bad" ©_.  
But what can we do about it?

When I was researching, I've read a lot of blog posts talking about `asynchronous` code and _asynchronous programming_
as a solution. Thinking about it, it easily makes sense: our kernels use preemptive scheduling, so when the scheduler
time-slices a long-running thread there's not much we can do about it, but we **can** maximize efficiency by _being
careful_ to avoid blocking calls and use asynchronous APIs!

What we are going to do is write code that, instead of blocking while waiting for a result, will start a task with a
call from an API which will return the control of execution immediately (so we can go on and execute other instructions)
but will notify us when the result is ready.

Let's take a look at the second implementation I've made of the proxy service I've presented in the [previous entry][part-2]:

## Async API

Changing an API from synchronous to asynchronous seems pretty simple at first.  
Starting from the inner out, we create a new function, `asyncRequest`, as an asynchronous alternative to
`blockingRequest`.

```java
public void asyncRequest(ExecutorService, String, String, CompletionHandler)
```

Because _asyncRequest_ has to return immediately in order to not block the calling thread, we give it an
`ExecutorService` just to be explicit about which pool is going to be used, a _String_ for the URL, another for the
headers, and also a `CompletionHandler`.  
_CompletionHandler_ is an interface one has to implement, that is going to be called by the code inside _asyncRequest_
once the result of the request is available: one callback in case of success, another in case of error.

```java
public interface CompletionHandler<V> {
    void completed(V result);
    void failed(Throwable t);
}
```

I'll save you the details of actually passing a URL and parameters hidden inside the intermediary
`CoordinatorService#requestConnection(String, CompletionHandler, ExecutorService)` and get directly to the new version
of `getConnection` that we've talked about in the previous entry:

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

The signatures are similar to the synchronous ones, except for the extra `CompletionHandler<Connection.Available>`
handler.  
The difference here is that instead of making the connection request directly we submit (`schedule`) it to this
`boundedServiceExecutor`.

_boundedServiceExecutor_ is an instance of `ScheduledThreadPoolExecutor`.

```java
boundedRequestsExecutor =
    Executors.newScheduledThreadPool(10, new PrefixedThreadFactory("requests"));
```

In fact, _boundedServiceExecutor_ is not the only thread pool used in this second implementation of the proxy service.  
Just to make things more explicit regarding where each task runs, I've create three dedicated executors:

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
> because only heartbeat requests (using `boundedPulseExecutor`) need to be delayed.  
> But both being fixed-sized pools, the result is the same in this case.

Similarly to the synchronous example, `getConnection` deals with the retry logic. But because
`CoordinatorService#requestConnection` is asynchronous and takes a _CompletionHandler_ (because it itself calls to
`asyncRequest`), we have to implement both success and error callback methods.

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

        coordinator.requestConnection(
            token,
            new CompletionHandler<>() {
                @Override
                public void completed(Connection c) {
                    if (c instanceof Connection.Available) {
                        if (handler!=null)
                            handler.completed((Connection.Available) c);
                    } else {
                        Connection.Unavailable unavail = (Connection.Unavailable) c;
                        getConnection(
                            unavail.getEta(),
                            unavail.getWait(),
                            unavail.getToken(),
                            handler);
                    }
                }

                @Override
                public void failed(Throwable t) {
                    if (handler!=null) handler.failed(t);
                }
            },
            boundedServiceExecutor);
    }, wait, TimeUnit.MILLISECONDS);
}
```

In case of success of the underlying _requestConnection > asyncRequest_ call chain, the `completed` method will be
called with the response from the coordinator. The answer could be positive or negative.  
In case of `Available`, we're done; so we can complete the completion _handler_ which was given to `getConnection` so
its caller can be notified of the success and proceed (with the download).  
In case of `Unavailable`, we hide the retry logic and _reschedule_ the call to _requestConnection_ to the executor
by recursively calling `getConnection` with updated parameters.

In case of failure we simply propagate the error to `getConnection`'s caller.

The callbacks already make this logic cluttered enough; but we're not done! We must now implement `getThingy`, our
service method which calls to `getConnection` and then start the download request.

```java
private void getThingy(int i, CompletionHandler<Void> handler) {
    println("Start getThingy.");

    getConnection(new CompletionHandler<>() {
        @Override
        public void completed(Connection.Available conn) {
            println("Got token, " + conn.getToken());

            CompletableFuture<Void> downloadFut = new CompletableFuture<>();
            gateway.downloadThingy(new CompletionHandler<>() {
                @Override
                public void completed(InputStream content) {
                    // Download started
                }

                @Override
                public void failed(Throwable t) {
                    if (t instanceof EtaExceededException) {
                        err("Couldn't getThingy because ETA exceeded: " + t);
                    } else {
                        err("Couldn't getThingy because something failed: " + t);
                    }
                    if (handler!=null) handler.failed(t);
                }
            }, boundedServiceExecutor);

        }

        @Override
        public void failed(Throwable t) {
            err("Task failed.");
            if (handler!=null) handler.failed(t);
        }
    });
}
```

We've seen above that `getConnection` takes a completion handler which will be called when we successfully get a
download authorization from the coordinator or when an error happens, so `getThingy` implements an ad-hoc handler to
handle the logic that should ensue.

On _successful_ completion, we are authorized to start the download, which is done by calling `gateway.downloadThingy`.  
`downloadThingy` is, itself, asynchronous because it also calls down to `asyncRequest`, so we must give it a new
_CompletionHandler_.  
This completion handler will be given an `InputStream` once the connection to the data source is established, so we
can read the content and forward it to this service's client. The consumption of the _InputStream_ and forwarding to
the client is omitted for now and replaced by the "Download started" comment.  
`downloadThingy`'s last parameter is an optional executor, used to specify on which pool will the completion handler
be executed. If omitted, it is the calling thread, but in this case we specify that we want the content
consumption/forwarding to happen on the `boundedServiceExecutor`.

In case of failure from `getConnection` or `downloadThingy`, we complete `getThingy`'s completion handler with a
failure.

Now that we have the structure, we can handle the content, but also start the periodic heartbeat requests!

```java
Runnable pulse = new PulseRunnable(i, downloadFut, conn);
int total = 0;
try(content) {
    println(i + " :: Starting pulse ");
    boundedPulseExecutor.schedule(pulse, 2_000L, TimeUnit.MILLISECONDS);

    // Get read=-1 quickly and not all content
    // because of HTTP 1.1 but really don't care
    byte[] buffer = new byte[8192];
    while(true) {
        int read = content.read(buffer);
        // drop it
        if (read==-1 || (total+=read)>=MAX_SIZE) break;
    }

    println("Download finished");

    if (handler!=null)
        handler.completed(null);
} catch (IOException e) {
    err("Download failed.");
    if (handler!=null)
        handler.failed(e);
} finally {
    downloadFut.complete(null);
}
```

Remember this code executes inside `downloadThingy`'s completion handler, so when this code runs, the connection
to the data source has been established.  
We encapsulate the logic inside a `try-with-resource` block around `content` (the _InputStream_) because it can
fail at any moment.

We've initialized a `PulseRunnable` which we can now schedule to send heartbeat requests (it will reschedule itself
upon completion). The reason why we give it a reference to the _Future_ `downloadFut` it to stop sending heartbeats
when the download ends, but we'll see `PulseRunnable` later.

In the mean time, we consume the _InputStream_ and just ignore the content once again (not important).

Finally, when the download is finished, successfully or on error, we _complete_ `getThingy`'s handler accordingly and don't
forget to complete `downloadFut` so that the heartbeats stop.

Almost done. We now take a look at `PulseRunnable`:

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
            coordinator.heartbeat(
                conn.getToken(),
                new CompletionHandler<>() {
                    @Override
                    public void completed(Connection result) {
                        if (!download.isDone()) {
                            boundedPulseExecutor
                                .schedule(PulseRunnable.this, 2_000L, TimeUnit.MILLISECONDS);
                        }
                    }

                    @Override
                    public void failed(Throwable t) {
                        if (!download.isDone()) {
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

Nothing too fancy. When started, the runnable calls the asynchronous `CoordinatorService#heartbeat` method with the
token, a _completion handler_ and an executor responsible to run the handler's methods (`boundedServiceExecutor` in
this case, just like when calling `GatewayService#downloadThingy` above).

Heartbeat results, whether successes or failures, are simply ignored, and a new heartbeat request is scheduled, as
long as the download _Future_ is not done.

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

Profiling this code revealed that CPU usage was still really low, more or less like in the previous implementation.
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

{{< gallery title="\"Service\" thread pool's threads are busy" >}}
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
(`boundedRequestsExecutor` in this case) is used only to call its `submit(Runnable)` method.

So the `request-*` threads in the VisualVM screenshot above are all doing the same thing: execute an `asyncRequest`.

### Pulse threads

Threads whose names begin with `pulse` come from `boundedPulseExecutor`. The only places where this _Executor_ is
used is when scheduling the heartbeat requests from within `getThingy` and from the "pulse" _Runnable_ itself:

`boundedPulseExecutor.schedule(pulse, 2_000L, TimeUnit.MILLISECONDS);`

And it is also given as the last parameter to `CoordinatorService#heartbeat` in order to execute its _completion
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
`asyncRequest` and sometimes doing a bit of `parseToken` which consists in decoding a few bytes.

### Service threads

Threads whose names begin with `service` come from `boundedServiceExecutor` and are used in a couple of places.

* in `getConnection` in order to submit the call to `CoordinatorService#requestConnection`
* to execute the _completion handler_ of `CoordinatorService#requestConnection`
* to execute the _completion handler_ of `GatewayService#downloadThingy`
* to execute the calls to `asyncRequest` inside `CoordinatorService#requestConnection` and `CoordinatorService#heartbeat`

So the `service-*` threads in the VisualVM screenshot above are doing a few asynchronous calls and also a bit of
callback execution.

### A word about ScheduledThreadPoolExecutor

I think it's time to introduce the `ScheduledThreadPoolExecutor` properly.

Contrary to cached `ThreadPoolExecutor`, this one doesn't doesn't spawn new threads to match the number of _Runnable_
it is submitted. Instead it stays with the number or threads it is passed when create.

{{< gallery title="Async all the things!" >}}
  {{% galleryimage file="/img/loom/async-all-the-things.png"
  size="2990x1288"
  width="100%" %}}
{{< /gallery >}}

When using this kind of executor, the goal is to execute non blocking calls





## Beware of the lurking blocking call


## Conclusion


[part-0]: ../loom-part-0-rationale
[part-1]: ../loom-part-1-scheduling
[part-2]: ../loom-part-2-blocking
[VisualVM]: https://visualvm.github.io/
[flame graph]: http://www.brendangregg.com/flamegraphs.html
[async-profiler]: https://github.com/jvm-profiling-tools/async-profiler
[Callback Hell]: http://callbackhell.com/