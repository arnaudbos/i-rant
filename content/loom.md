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

> WARNING: Long article, because I like to read and write long blog posts. Sue me.
>
> If you're interested in an introduction and some context continue reading,  
> otherwise skip to the [TOC](#table-of-contents).

{{< img src="/img/loom/loom.jpg" alt="Loom" width="100%" title="foo" caption="Stephencdickson" attr="CC BY-SA 4.0" attrlink="https://creativecommons.org/licenses/by-sa/4.0" >}}

-----

I've been interested with parallel and concurrent programming for the past few years because everybody says it's
complicated and stuff.  
Finding good introduction level articles on the matter is not easy because every post assumes some prior
knowledge. And you can quickly find yourself into dark corners of the Web where people talk about lock-free concurrency
and I don't know you, I was really not ready for it!

A few months ago we've had an interesting use-case at work, where we had to implement as scheduler for a scarce
resource in our system.  
We've been using many concurrency "techniques" for the past two years, from simple threads to various types of
executor services and also a lot of "reactive" (Rx) programming.  
But when the time came to implement this specific piece of algorithm, I'm not ashamed to say that I didn't feel the
nerves to implement it using any of those techniques. And because the problem lended itself well into an actor model,
we looked around to see which library would help us in this endeavor. This is where we took a look at [Quasar].

My first introduction to [Quasar] was a few years ago around 2014. At the time I was writing [Clojure] professionally
and Clojure's [core.async] library just came out. I didn't understand every aspect of the value proposition
at first, but naively started using it.  
Because I was using `core.async`, I stumbled upon [Pulsar] (not Apache Pulsar!) which is built upon Quasar.

Yet again, I didn't understand the value proposition of Pulsar more than Quasar at that time, but I was interested
in this "Actor Model" along with all the Parallel Universe stack, and longed for the day I would have a use case for
this.  
Fast forward a couple of years and with more experience in the field, Quasar and it's actor model in particular seemed
to be a great fit to solve our problem and I fell into this rabbit hole, trying to understand the underlying concepts
and "how it works".

I gave a couple of presentations at conferences on this topic this year, so I've had to research a lot.  
This blog post is about what I've learnt and what I would have liked to read when I begun my research on
concurrent programming and lightweight concurrency. I'm basically writing for the past me.  
For people already familiar, I'm not promising you'll learn anything new, but hopefully you'll be interested in
another point of view.

<!-- toc -->

## About Concurrency, About Scheduling

### There's only one hard problem

The first problem there is, I think, with concurrency and with most problems in computer science and programming
really, is that we're extremely bad at naming things. We sometimes use different words to describe one and only thing,
or the same word to describe several distinct concepts.

In concurrent programming specifically, people like to use terms from the lexical field of weaving or the weaving
industry:

* [ ] Thread
* [ ] Kernel Thread
* [ ] Lightweight Thread
* [ ] User Thread
* [ ] Green Thread
* [ ] Fiber
* [ ] Knot

Quick questions:

1. Can you spot the mistake?
2. Do you know what concept each of those terms refer to?
3. If yes, are you sure you know them well enough to explain their meaning to somebody?

If you've answered yes three times, stop reading and go do something else.

-----

Still with me? Alright, let's try to untangle this and look back into history first.

### It's all about Scheduling

[DRAFTING]
* Serial execution (Multiple users)
(First computers and their operators)
** Operators competing for access
** Computers idle
* Time-sharing & Threads
(Next gen and I/O)
** Multi-processir machines
** Parallel control stacks
** Parking/Unparking
** Preemption
> There are two types of code on the JVM. The ones that block and the ones that don't. But they come in different API flavors.
[/DRAFTING]

Did you know that programmers haven't always existed?

The question sounds stupid: **of course** you know. It's not like a Chicken and Egg problem,
programmers couldn't exist before computers.  
But did you know that the word "computers" existed before machines were even built?

I'm making a short digression here, bear with me. If you search about the history of computing for a bit, and
especially about the history of **women in computing**, thanks to the work of passionate and driven researchers, it
won't be long until you find some really interesting stuff.

For example, you could learn that during World War II women
[were hired as "computers" to calculate ballistics trajectories by hand][computers]. Or you could
also read this great story about Katherine Johnson: [She Was a Computer When Computers Wore Skirts][Johnson]).

{{< gallery title="Women holding parts of computers (from Wikipedia)." >}}
  {{% galleryimage file="/img/loom/computers.jpg"
  size="1024x812" caption="Women holding parts of the first four Army computers."
  width="100%" %}}
{{< /gallery >}}

Later, what we commonly call _computers_ started to get built and to handle tasks. At that time, computer execution
was sequential.

#### Sequential execution

The first computers were capable of running a set of instructions sequentially, a program, and would sit idle the rest
of the time. Up until another program is executed. Kind of like a sewing machine, or a loom.

The diagram below shows the execution of two distinct programs on such a computing machine.

{{< gallery title="Sequential execution" >}}
  {{% galleryimage file="/img/loom/sequential.png"
  size="2198x994" caption="Schema of sequential execution, also detailed in text in the following paragraphs."
  width="100%" %}}
{{< /gallery >}}

> "Operators" were the names of the first computers, who used _computers_.

Here we have two operators. The first one starts "writing a program", which actually consists in wiring cables and
turning switches (among other things) to make the computer calculate the solution to a problem.

Once the computer is working, Operator1 must wait the end of the program to get the result and only then Operator2 can
access the computer to write her program and make it run. There is no ability for Operator1 and Operator2 to parallelize
their work, their programs are executed sequentially.

This worked well for people to see the value added, and the Red Queen's race started...

Sequential execution became a problem!  
Because the bottleneck to solve problems faster shifted, from computers to humans being too slow: even with new ways to
write programs (punch cards, magnetic tapes) the time taken by each programmer to "insert" its program into the machine
became unacceptable—i.e., too expensive.

In this situation, a good scheduler has to do something. Right?

#### Batch processing
  
In this case, the schedulers were computer scientists and manufacturers. They came up with this really sensible idea
that, to amortize the cost of running many jobs sequentially, one could ["batch"][batch] them.

Not only let programmers write program instructions on their own, away from the machine, but let them "offer" their
programs to a waiting queue of jobs to be performed.

{{< gallery title="Batch processing" >}}
  {{% galleryimage file="/img/loom/batch.png"
  size="1984x978" caption="Schema of batch processing, also detailed in text in the following paragraphs."
  width="100%" %}}
{{< /gallery >}}

In the diagram above, Program1 and Program2 are submitted one after the other, so Program1 runs first and Program2 will
run after.

But having to add your program into a "giant" batch of jobs for the program to compute, and then wait for the computer
to return all the results of all those jobs in order to get yours, was a massive pain in the butt!  
Imagine debugging your programs **with a 24 hours feedback loop...**

It was not only painful for programmers though. It was also painful for _users_ who would have to run the programs!

{{< img src="/img/loom/kropotchev.png" title="\"Batch processing\" spoof movie — Stanford" alt="Silent spoof movie relating the adventures of a programmer waiting on batch processing." width="100%" link="https://www.computerhistory.org/revolution/punched-cards/2/211/2253">}}

It's a common problem in scheduling. Optimising for a use case/parameter (less computer idle time) ends up
making it worse from another point of view (more programmer idle time).

Computers kept getting faster and faster for sure, and although programmers tried to make the best of the computing
power they could get, any single user wouldn't make efficient use of a computer by herself!

Indeed, processes, to do something useful, have to be fed with inputs and produce any kind of output, otherwise they'd
be just heating rooms.  
Consuming inputs today can be reading from a keyboard, hard drive or network card. But in the sixties it mostly meant
reading from magnetic tape or a teletypewriter. Similarly, producing output meant writing to magnetic tape, printing
to a teletypewriter or a "high-speed" Xerox printer.

And, generally, all the time during which a processor is waiting for I/O (polling, [busy waiting]) rather than
"crunching numbers" is wasted.

{{< gallery title="Cycles wasted on I/O" >}}
  {{% galleryimage file="/img/loom/io.png"
  size="1934x664" caption="Schema of wasted cycles because of I/O."
  width="100%" %}}
{{< /gallery >}}

In the above diagram, we can see Program1 starting and finishing before Program2, but there are _gaps_ between bursts
of execution. Same for Program2.

Schedulers to the rescue!

#### Time-sharing

One of the major advance in computer systems.

{{< youtube Q07PhW5sCEk >}}
<br />

The video above is a gem.  
In it, you can see Turing Award winner ~~Buddy Holly~~ [Fernando Corbató] explain the promises and premises of
[time-sharing][time-sharing] as it was being designed.

With it, processes wouldn't spin on I/O but rather be _parked aside_, while another process would be allocated
CPU time. Time-sharing allows **concurrency**: the **illusion of parallelism** and **efficient use of physical
resources**.

Let's see an example:

{{< gallery title="Time-sharing" >}}
  {{% galleryimage file="/img/loom/time-sharing.png"
  size="1830x1194" caption="Schema of time-sharing, also detailed in text in the following paragraphs."
  width="100%" %}}
{{< /gallery >}}

In this diagram, Program1 starts first and is parked when it starts a **blocking** I/O call.  
Program2 then gets a chance to run but is quickly parked too as it also makes a **blocking** I/O call.  
Some other program, Program3, runs while 1 and 2 are **parked**.  
Program2's I/O returns first, so on the next _shift_, Program2 is **unparked** and _continues_ to run while
Program1's I/O returns.  
Program2 ends.
Program1 then picks up the result from its I/O call and finishes running too.

I'll leave the "Context switch" legend unexplained for now, we'll come to it later.

#### Threads

In the previous sections I've considered all programs equals, but as always, _"It depends"_.

`P1`, `P2`, `P3` could very well have different subroutines lengths but also very different characteristics with regard
to the kind of work they do.

_CPU-bound_ tasks that don't require much (if any) communication could be tackled by multiple processes (instances
of a program communicating thought IPC).

But tasks requiring communication, such as coupled cooperative processes, for which IPC may be too expensive, or tasks
requiring many I/O operations (_I/O bound_), the time to _switch_ from `P1` to `P2` and back to `P1` (for instance)
may hamper it fast realization and/or drain physical resources...

I've mostly talked about processes in the previous sections (`P1`, `P2`, etc), but nowadays the most widespread
fundamental unit of concurrency is not the process: it's the **thread**.

{{< gallery title="Threads in a Process" >}}
  {{% galleryimage file="/img/loom/threads.png"
  size="1882x1314" caption="Threads in a process, sharing memory but with distinct call stacks."
  width="100%" %}}
{{< /gallery >}}

Conceptually, a thread is like a process: it embodies the execution of a set of instructions and, at runtime, gets
its own [control stack] filled with stack frames. But what's really remarkable about threads is their ability (for
better or worse) to share the memory of their parent process.

> In some papers, they are referred as ["lightweight processes"][lightweight-processes].

And because each thread has its own call stack, it's possible for multiple threads, while in a single process, to
execute **different sets of instructions** running **in parallel** on distinct processors and cores.

The JVM, for instance, takes advantage of this, while other languages (such as Python because of its
[Global Interpreter Lock][GIL]) would require multiple processes. Others languages, such as Go, despite being based on
the same principles, introduced another layer of concurrency into their runtime.

But before diving into this, let's talk about time-sharing a bit more.

#### Multitasking
 
Time-sharing gained traction after the realization that computers had become powerful enough to be under-utilized
by their users and that connecting more users to one would amortize its cost.

In order to do that, time-sharing employed both

* multiprogramming: multiple programs running concurrently; and
* multitasking: programs would run one after the other, in **short** bursts (to avoid greedy programs to monopolize the CPU)

techniques.

There are several techniques that can lead to effective multitasking and fair (or not) sharing of CPU cycles, but they
can grouped in two main categories: `Preemptive multitasking` and `Cooperative multitasking`; and their properties are
enforced by scheduler policies.

##### Preemptive scheduling

A preemptive scheduler may suspend a running thread when it blocks on I/O or waits on a condition, so the processor
can be assigned another thread to work on. It may also prevent spin-locking or CPU intensive tasks to
_hog the CPU_ by allowing threads for a finite amount of time before parking it, in order to let
other tasks a "fair" chance to run.
 
> To be _fair_ (pun intended), "fair" scheduling isn't a solved problem, and I didn't dive into the details of the
> algorithms, but these two resources may be good entry-points for more research [Completely Fair Scheduler] and
> [Brain Fuck Scheduler].

{{< gallery title="Preemptive scheduling illustrations" >}}
  {{% galleryimage file="/img/loom/preempt-io.png"
  size="2118x1552" caption="Thread preemption triggered by a blocking call. The first thread is parked and unparked some time later."
  width="395px" %}}
  {{% galleryimage file="/img/loom/preempt-quantum.png"
  size="2116x1554" caption="Thread preemption triggered by time slicing. The first thread consumes is quantum so is parked and unparked some time later."
  width="395px" %}}
{{< /gallery >}}

The first figure, the one on the left, illustrates a thread/task being preempted by the kernel, using a _context-switch_
when a blocking call occurs, in order to satisfy the scheduling policy.  
The first thread is parked while the next thread is executed. Eventually (hopefully), the first thread is going to be
unparked and resume its execution.

The second figure, on the right, illustrates a thread being preempted because its execution duration is longer than the
time it is authorized to run on the CPU, also named its _quantum_.  

Preemptive multitasking is what we get from our modern operating systems, or kernels. And in practice, the preemption
is specified inside the kernel scheduler's algorithm and could use any number of techniques or policies to determine
when the current task could/should be preempted and which task gets to run next.

##### Cooperative scheduling

> Cooperative multitasking, also known as non-preemptive multitasking, is a style of computer multitasking in which
> **the operating system never initiates a context switch** from a running process to another process. Instead,
> **processes voluntarily yield control periodically or when idle or logically blocked** in order to enable multiple
> applications to be run concurrently. This type of multitasking is called "cooperative" because **all programs must
> cooperate for the entire scheduling scheme to work**.  
> — From https://en.wikipedia.org/wiki/Cooperative_multitasking (emphasis mine)

{{< gallery title="Cooperative scheduling" >}}
  {{% galleryimage file="/img/loom/cooperative.png"
  size="2096x920" caption="Tasks cooperatively yield when they do not need system resources, allowing other tasks to run."
  width="100%" %}}
{{< /gallery >}}

In the diagram above, we can see two threads, Thread 1 and Thread 2, both containing interleaved "random" instructions
(that is, any code that this thread is supposed to run) and instructions containing a `yield` statement.

With cooperative scheduling, there is no intervention from the kernel to pause a thread and schedule the next one, this
is all done by each task deliberately relinquishing CPU time when it is not using computing resources so that another
task can run.

As you see, with this scheduling policy it requires **ALL** programs to cooperate and fairness is not ensured, which
explains why most operating systems nowadays implement preemptive scheduling.

#### Back to the present

I think we've covered most of the ground work necessary to understand where we're at from a scheduling
point of view regarding modern computers.

I obviously took a great detour just explain some fundamentals.  
On the other hand, there's been so much innovation in computer systems in the last 60 years that I had to cut corners
and feel like I didn't do it any justice. If it picked your curiosity, start with the links I've provided and follow
those threads (pun intended).

Alternatively, I've just stumbled upon this Twitter thread which cover about the same period but with more details on
the dates and pre-historic computer names. Thought you might find it interesting:

{{< tweet 1201956309941116928 >}}

Now let's get back on track!









On the JVM, the threads we manipulate are actually kernel threads (TODO: rework, in my presentations I used to mention the kernel
threads when talking about the CachedThreadPoolExecutor, maybe this is right here and should be a remainider aka "*"
when talking about the code later.).

"Pausing" (TODO: make sure of the terms for suspending a thread and their states (ready, runnable, etc.)) a thread from
the kernel's point of view is a task requiring to get out of user-mode (TODO talk about rings?) into privileged mode
to execute reserved instructions in order to save the execution state of the thread, schedule another one, load its
state and go back to user mode (TODO: maybe introduce "wankel" diagrams here).  
This is also known as context switching and is detrimental to performance (TODO add link, see pocket) because the cycles
spent on "book keeping" are not spent running your tasks.

## Two types of code

In this part

Today, in our tool belts, we have processors much (much!) more powerful than in the sixties or seventies. We have
**multiprocessor** computers and even **multi-core** processors. What do we have on the software side?

When the scheduler time-slices a long-running thread there's not much we can do about it (TODO: open to unikernels?).
But we can maximize efficiency by being careful to avoid blocking calls and use async APIs.

[TODO] if this wasn't too obvious at this point, here I really get back in track with my recent presentation at the
moment where I introduce async programming. In the talk a talk just a bit more about how sync is great while here I
don't talk about programming styles/APIs at all, this must be fixed.

The thing with async APIs is that it's not obvious to differentiate async from non-blocking, and vice-versa.  
An asynchronous call can still be thread-blocking. (TODO: yes, this should be illustrated with content from my talks).
It may of may not block the current thread but run the distinct control stack on another thread, managed in a library
specific thread pool.

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