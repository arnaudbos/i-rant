---
title: Loom - Part 5 - Reactive Streams
date: 2020-05-11T20:02:21+02:00
description: Part 5 on a series of articles about OpenJDK's Project Loom
parent: blog
categories: ["java"]
tags: ["java", "concurrency"]
draft: false
seoimage: img/loom/sergio-gonzalez-YFXUumP4m5U-unsplash.jpg
highlight: true
gallery: true
---

> Part 5 in a series of articles about Project Loom.  
> In this part we re-implement our proxy service using a Reactive Streams API.
>
> The companion code repository is at [arnaudbos/untangled](https://github.com/arnaudbos/untangled)
>
> If you'd like you could head over to  
> [Part 0 - Rationale][part-0]  
> [Part 1 - It's all about Scheduling][part-1]  
> [Part 2 - Blocking code][part-2]  
> [Part 3 - Asynchronous code][part-3]  
> [Part 4 - Non-thread-blocking async I/O][part-4]  
> [Part 5 - Reactive Streams][part-5] (this page)  

{{< img center="true" src="/img/loom/sergio-gonzalez-YFXUumP4m5U-unsplash.jpg" alt="Loom" width="100%" caption="Photo by Sergio Gonzalez" attr="on Unsplash" attrlink="https://unsplash.com/photos/YFXUumP4m5U">}}

-----

<!-- toc -->

Full disclosure: I have a love-hate relationship with the Reactive Streams specification. More
specifically with its implementations.

Let's see what they have to offer, and what are the tradeoffs.

## Reactive Streams

Let me begin with a quote:

> Reactive Streams is an initiative to provide a standard for asynchronous stream processing
> with non-blocking back pressure. This encompasses efforts aimed at runtime environments
> (JVM and JavaScript) as well as network protocols.
>
> — [Reactive Streams]

Two sentences, but a lot to unpack here. Reactive Streams, the concept and the ensuing
specification, is about unifying efforts, across platforms, to provide foundations for
building scalable systems with the following characteristics (which I'll let you
read the definition of on the [Reactive Manifesto]): `responsive`, `resilient`, `elastic`
and `message driven`.

The key takeaways are **asynchronous** stream processing and **non-blocking** back pressure.

We'll dive into these, but first let me present an implementation of this specification
with which I've played a lot, and then I'll tell you about this love-hate relationship.

The library I'm going to present briefly is Pivotal's [Project Reactor].

> Reactor is a fourth-generation reactive library, based on the Reactive Streams
> specification, for building non-blocking applications on the JVM.

### The honeymoon

As I said in the [previous installment][part-4] of this series, Reactor is one such
library which implements the Reactive Streams specification and provides a
"*composable, functional, declarative and lazy API*". 

So let's see how it can help us turn the gobbledygook of callback nesting,
that was born out of Java NIO, into a simple program. We'll begin with the
`makePulse` function.

(makePulse is the function responsible for periodically sending the heartbeat requests
in the [use-case][use-case] we've already seen throughout this series)

```java
private Mono<Void> makePulse(Connection.Available conn) {
①  return Flux.interval(Duration.ofSeconds(2L))
②      .flatMap(l -> coordinator.heartbeat(conn.getToken()))
③      .doOnNext(c -> println("Pulse!"))
④      .then()
⑤      .doOnCancel(() -> println("Pulse cancelled"))
⑥      .doOnTerminate(() -> println("Pulse terminated"));
}
```

First, take a second to appreciate how having a functional programming background helps
a lot to find such library really appealing.  
The current trend on the JVM is to go more functional, which started progressively with
lambdas and other functional constructs such as `Optional` and `Stream`, so this API
should look kinda familiar.

1. Every 2 seconds
2. Send a heartbeat request to the coordinator
3. "log" that a heart beat has happened
4. when this stream of operations comes to an end, just turn it into a single "completion signal"
5. "log" when this `Flux` has been cancelled (somehow)
6. "log" that the heart is no longer pulsing, whatever may be the cause

If you're familiar with the `Optional` or `Stream` API or better, with the [Vavr] library,
the only thing which may surprise you is this `then()` function call.  
Other than that, we're infinitely flatMap—ing over a stream of ever-increasing longs.

It's nice and all, especially compared to the [40+ lines][pulse-nio] of the Java NIO
implementation, and even compared to the [10-ish lines][pulse-blocking] of the
straightforward blocking implementation.

Sadly, there is a "but". Two actually.

### The learning curve of FP

If programming is a kind of roller coaster then functional programming is definitely
one of the sections that's got a few hills. I'm not going to include any code example
in this section but bear with me.

Not everyone is keen on learning "functional programming" and its many concepts. Those
who do usually come to like the way it makes managing state more conceivable, helps
isolate side-effects, promotes composability, increases conciseness, boosts
expressiveness and all the yada yada of safety.

I agree with all the above. Unfortunately, in the FP advocates community there are
also the ones who can't help but flog about category theory to death. Have you ever
heard the joke that "A monad is just ..."?  
Fasten your seatbelt now, 'cause that was so 2018.  
Apparently 2020 wasn't bad enough to some, so now it's all about "A Kleisi is just ...",
"An Arrow is just ...", and the latest: "It's always traverse".

Some will tell you that you don't have to worry about all this theoretical knowledge,
that you're already using monads and such without even realizing it. On the other hand
it's like each time a concept becomes somewhat approachable (due to a sheer amount of SO
questions and blog posts) and even "mainstream" after a year or two, some folks decide to
dive deeper in search of more infantilizing meme material. Jeez!

{{< img center="true" src="/img/loom/haskell_pyramid.png" alt="The Haskell Pyramid" width="100%" caption="\"The Haskell Pyramid\" by" attr="Patrick Mylund Nielsen" attrlink="https://patrickmn.com/software/the-haskell-pyramid/" link="https://patrickmn.com/software/the-haskell-pyramid/">}}

{{% fold id="explore" title="Disclaimer on the use of the previous image" %}}
The image above was taken from a very heart-warming blog post.

It didn't seem fair to me to include it without a note on how this image, out of context,
could give an impression opposite to the indent of its author's full article. Which is,
in a nutshell: "It’s perfectly possible to be productive in Haskell without understanding
anything about monads!".

I have included this image here as it illustrates what I was saying about the ever-increasing
pyramid height, because even though it is out of its context, it isn't false either.
{{% /fold %}}

Tackling the cognitive load of asynchrony by hiding it behind functional abstractions
is one approach to solving the problem (not the only one as we'll see), and if you start
using a "reactive" library, you're in for a serious ride. It may be worth it, maybe not.

### The learning curve of Rx

As if "mastering" FP wasn't enough, the learning curve of Rx itself is no sinecure either.

It took me a good year and half to know what I was doing with [Project Reactor] and some
of its features still feel impenetrable. The vicious thing, I think, is the false impression
of simplicity, the warm cosy feeling given by code samples like the one above.

What I didn't realize, at first, is that the learning curve is not steep as people are
used to write or say. It's actually steep

### The debugging


## Profiling
## Backpressure (real quick)
## Conclusion

[part-0]: ../loom-part-0-rationale
[part-1]: ../loom-part-1-scheduling
[part-2]: ../loom-part-2-blocking
[part-3]: ../loom-part-3-async
[part-4]: ../loom-part-4-nio
[part-5]: ../loom-part-5-rx
[Reactive Streams]: https://www.reactive-streams.org/
[Reactive Manifesto]: https://www.reactivemanifesto.org/
[Project Reactor]: https://projectreactor.io/
[use-case]: ..//loom-part-2-blocking/#a-simple-use-case
[Vavr]: https://www.vavr.io/
[pulse-nio]: https://github.com/arnaudbos/untangled/blob/master/hawaii/src/main/java/io/monkeypatch/untangled/Chapter03_AsyncNonBlocking.java#L150
[pulse-blocking]: https://github.com/arnaudbos/untangled/blob/master/hawaii/src/main/java/io/monkeypatch/untangled/Chapter01_SyncBlocking.java#L89
[haskell-pyramid]: https://patrickmn.com/software/the-haskell-pyramid/

[generations]: https://akarnokd.blogspot.com/2016/03/operator-fusion-part-1.html

[VisualVM]: https://visualvm.github.io/
[flame graph]: http://www.brendangregg.com/flamegraphs.html
[chapter-2]: https://github.com/arnaudbos/untangled/blob/master/hawaii/src/main/java/io/monkeypatch/untangled/Chapter02bis_ScheduledFully.java
[chapter-3]: https://github.com/arnaudbos/untangled/blob/master/hawaii/src/main/java/io/monkeypatch/untangled/Chapter03_AsyncNonBlocking.java
[file descriptor]: https://en.wikipedia.org/wiki/File_descriptor
[callback hell]: http://callbackhell.com/
[note-on-AsynchronousSocketChannel]: #note-on-AsynchronousSocketChannel
[note-on-asyncTower]: #note-on-asyncTower
[userspace]: https://en.wikipedia.org/wiki/User_space#Overview
[Protection ring]: https://en.wikipedia.org/wiki/Protection_ring
[selector]: https://github.com/arnaudbos/untangled/blob/master/hawaii/src/main/java/io/monkeypatch/untangled/FiberEchoServer.java#L197
[netty]: https://netty.io/
[netty-proxy-example]: https://netty.io/4.1/xref/io/netty/example/proxy/HexDumpProxyFrontendHandler.html
[netty-io_uring]: https://github.com/netty/netty/wiki/Google-Summer-of-Code-Ideas-2020#add-io_uring-based-transport
[rx-spec]: https://github.com/reactive-streams/reactive-streams-jvm
