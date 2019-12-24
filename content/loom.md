---
title: Loom
date: 2019-11-04T12:23:00+01:00
description: Loom
parent: blog
categories: ["java"]
tags: ["java", "concurrency"]
draft: true
seoimage: img/loom/loom.png
highlight: true
gallery: true
---


So asynchronous thread blocking APIs are nice because they're blocking other threads, but the problem of memory
footprint of kernel threads and context switches still remains.

Truly non-blocking asynchronous APIs rely on event loops and fine tuned implementation techniques such as work
stealing and drain loops in order to maximize CPU utilisation.  
But they rely on callbacks too, which leads to the infamous callback-hell that.

TODO: Yup, rework this part, heavily.

But we're in 2019 so we have libraries now to lift this problem off of our shoulders, namely Reactive Streams libraries
such as RxJava or Pivotal's Project Reactor.

Reactive Streams APIs are a great solution to the inefficient use of CPU but they come as a whole new programming style.
A new paradigm on a platform that may not be suited for it.

And becauseit's 2019, we're stating to get feedback on these libraries, hinting they're not the best for all use-cases,
most notably because of debugging and profiling and interoperability with existing (blocking) code.

Even after almost two years I sometimes have to think hard about which Reactor operator to use, am I messing up
ordering, am I blocking here, am I lazy there, am I stack safe?

So it's interesting to ask, is there another way? Can we write non-blocking, efficient code without Reactive Streams
APIs while still avoiding async APIs and callback hell?  
Project Loom is one possible answer.

Again, you can see this scheduling story has been running for quite some time now. (TODO link to beginning, possible
distinct article).

TODO: Note: unrelated to this specific location: synchronized -> doog lea -> reactive -> loom

The lightweight concurrency mechanisms that Loom aims to bring to the JVM are also known as user-threads.  
TODO: Checkboxes again.

Contrary to Kernel threads, which are managed by the kernel, user threads are manager by the language or runtime and
are named this way because they un in user space.

We'll see why they're interesting and how they work, but I think it's time to check a few boxes now:

* lightweight threads
* user threads
* fibers
* coroutnies
* erlang processes
* goroutines
* async/await
are all, basically, user thread mechanisms and they differ in their implementation details or whether they're
first-class citizens or language syntax.

All those details matter from a language implementer's point of view and are really interesting, but if you're not that
much of a language geek, hopefully I can save you some research time with this statement.

The only bow I'm not goiing to tick here is green-threads because they have a specific meaning in the Java world, so
I'm leaving this as an exercise to the reader.

Bak to user-threads.  
The conceptual model for user-threads is often referred to in the literature (TODO link) as the M:N model, where M
represents the number of user-threads running on top of N kernel threads.

Just to give a little bit more details, we can add the letter P in M:N:P to explicit the fact that M user-threads run
on N kernel threads on top of P processor cores. Where N would be as close to P as possible, ideally.

In Project Loom, user-threads are called lightweight/virtual threads, so I'll stick with this name from now on.

The reason why fibers are interesting is because the cost of creating, suspending and resuming those fibers is
massively reduced by the scheduler running in user-space, which means no extra kernel/user mode context-switches.

How does it work...  
Implementations vary: Kotlin transforms suspendable functions bytecode during compilation, Clojure transforms code at
macro-expansion time, etc.

It turns out that Java in particular, and Kotlin too actually, even before it had coroutines, Fibers have exsited for
quite some time. Since 2013 at least, thanks to a library named [Quasar].

Quasar has been developed by a company named [Parallel Universe][puniverse] who, sadly, no longer exists, but whose
CTO and core developer is now Project Loom's lead developer: Ron Pressler.  
Quasar implements fibers by instrumenting the Java bytecode and scans methods annotated as "suspendable" and turns them
into continuations that run on a Fiber scheduler. Those continuations have the ability to `yield`, or relinquish the
thread they are currently running on when encountering a blocking call.

On my last project, we implemented a service whose responsibility was to proxy requests to remote backends through
a limited number of connections. The goal of this scheduler was to maximize the use of connections, or in other words,
minimize the time during which a given connection would sit idle, unused.  
I'm not ashamed to say that even after almost two years of using Reactor daily, I didn't feel the nerves to do it with
a Reactive Streams implementation.

My colleague Guillaume started the Spring Boot 2 project, implemented the first REST endpoints and other background
tasks necessary for the service, but when it was my turn to implement the logic of the scheduler and states and
transitions, I went for Quasar and fiber-blocking, imperative code, and an actor model, but it's another story.

I'm not saying Reactor wasn't a good fit for this problem or that it couldn't be done. In fact I'm sure it could be,
just not by me, and I still think it was a good idea to find another way.

So when you've followed the work of Ron Pressler, it's not surprising that the way he proposed to approach implementing
Fibers on the JVM is through the use of continuations.  
Now, what's a continuation?

Conceptually, a continuation represents the rest of the execution of a process from a certain point in time.  
More practically, when looking at the following code:

TODO: insert code sample with "suspension point".

Who's  already heard about continuations?  
Most programming languages today do not expose continuations and ways to create them as first-class citizens. Pretty
much like many languages did not expose functions as first-class until the resurgence of functional programming.  
Continuations are very fundamental and extremely powerful, and after preparing this talk I now find them more interesting
by themselves then their use in the context of fibers. But anyways.

The way continuations allow fibers implementation is via the yielding mechanism.  
As soon as a running continuation encounters a blocking call, it calls this yield instruction.

The runtime will then know that this continuation's stack can be saved, put aside for when its execution can be
restarted, and will pick another continuation, which is ready to be restarted, if any, restore its control stack and
assign it to the CPU.

So Fibers in Loom are the association of continuations, and a scheduler.



[Toulouse JUG]: http://www.toulousejug.org/
[puniverse]: http://www.paralleluniverse.co/
[Quasar]: https://docs.paralleluniverse.co/quasar/
[Clojure]: https://clojure.org/
[core.async]: https://clojure.org/news/2013/06/28/clojure-clore-async-channels
[Pulsar]: https://docs.paralleluniverse.co/pulsar/
[Flynn]: https://en.wikipedia.org/wiki/Flynn%27s_taxonomy
[Duncan]: https://www.icloud.com/iclouddrive/0rYdyArRSYYupsRrEXSJ_cLgw#Duncan_-_1990_-_A_Survey_of_Parallel_Computer_Architectures
[computers]: https://www.history.com/news/coding-used-to-be-a-womans-job-so-it-was-paid-less-and-undervalued
[Johnson]: https://www.nasa.gov/centers/langley/news/researchernews/rn_kjohnson.html
[batch]: https://en.wikipedia.org/wiki/Batch_processing
[time-sharing]: https://en.wikipedia.org/wiki/Compatible_Time-Sharing_System
[virtual-memory]: https://en.wikipedia.org/wiki/Virtual_memory
[lightweight-processes]: [TODO]
[Fernando Corbató]: https://en.wikipedia.org/wiki/Fernando_J._Corbat%C3%B3
[control stack]: https://en.wikipedia.org/wiki/Call_stack
[GIL]: https://wiki.python.org/moin/GlobalInterpreterLock
[busy waiting]: https://en.wikipedia.org/wiki/Busy_waiting
[Completely Fair Scheduler]: https://en.wikipedia.org/wiki/Completely_Fair_Scheduler
[Brain Fuck Scheduler]: https://en.wikipedia.org/wiki/Brain_Fuck_Scheduler