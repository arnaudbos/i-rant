---
title: [TODO]
date: 2019-11-04T12:23:00+01:00
description: [TODO]
parent: blog
categories: ["java"]
tags: ["java", "concurrency"]
draft: true
seoimage: img/[TODO]/[TODO].png
highlight: true
---

{{< img src="/img/[TODO]/[TODO].png" title="Lisp as a secret weapon" alt="xkcd joke" width="100%">}}

-----

I've been quiet on this blog for the past few months and it has to do with my involvement in our local [Java User Group]
but mainly because I've been diving into this really interesting topic called "lightweight concurrency".

> WARNING: Long article, because I like to read and write long blog posts. Sue me.
>
> If you're interested in an introduction and some context continue reading, otherwise skip to the TOC.

You see, I've always been interested with parallel and concurrent programming because everybody says it's
complicated and stuff.  
Finding good introduction level articles on the matter is not easy because every post assumes some prior
knowledge about how computers work and you can quickly find yourself into dark corners of the Web where people
talk about lock-free concurrency and you were really not ready for it.

A few months ago, sometime after my last post, we've had an interesting use-case at
work where we had to implement basically some sort of scheduler for a scarce resource in our system.  
We've been using several concurrency techniques for the past two years from simple threads to various types of
executor services and also a lot of so called "reactive" programming.  
But when the time came to implement this specific piece of algorithm, I'm not ashamed to say that I didn't feel the
nerves to implement it using any of those techniques. And because the problem lended itself well into an actor model,
we looked around to see which library would help us in this endeavor. This is where we took a look at [Quasar].

My first introduction to [Quasar] was a few years ago around 2014. At the time I was writing [Clojure] professionally
and Clojure's [core.async] library just came out. I didn't understand every aspect of the value proposition
at first, but grew fond of the concept and started using it.  
Because I was using `core.async` I stumbled upon [Pulsar] (not Apache Pulsar!) which is built upon Quasar.

Yet again, I didn't understand the value proposition of Pulsar and especially Quasar at this time, but I was interested
in this "Actor Model" and all the Parallel Universe stack, and longed for the day I would have a use case for this.  
Fast forward a couple of years and with more experience in the field, Quasar and it's actor model in particular seemed
to be a great fit to solve our problem and I fell into this rabbit hole, trying to understand the underlying concepts
and "how it works".

I gave a couple of presentations at conferences on this topic this year so I've had to research a lot.  
This blog post is about what I've learnt and what I would have liked to read when I begun my research on
concurrent programming and lightweight concurrency.  
For people already familiar I'm not promising you'll learn anything new, but hopefully you'll be interested in hearing
another point of view.

## Table of contents

<div id="toc" class="well col-md-12">
  <!-- toc -->
</div>

## About Concurrency, About Scheduling

### There's only one hard problem

The first problem there is, I think, with concurrency, and with most problems in computer science and programming
really, it that we're extremely bad at naming things. We sometimes use different words to describe one and only thing,
or several distinct words to mean or represent the same thing.

[TODO]: Insert "one/two hard problems" here.

In concurrent programming specifically, people like to use terms from the lexical field of weaving or the weaving
industry:

* [ ] Process
* [ ] Lightweight Process
* [ ] Thread
* [ ] Lightweight Thread
* [ ] Green Thread
* [ ] Fiber
* [ ] Knot
* [ ] Coroutine

I have a couple of questions for you:

1. Can you spot the mistake?
2. Do you know what concept all of those terms refer to?
3. If yes, are you sure you know them well enough to explain their meaning to somebody?

If you've answered yes three times, stop reading.

-----

Still with me? Alright, let's try to untangle this and look back into history first.

### It's all about Scheduling

[DRAFTING]
# Serial execution (Multiple users)
(First computers and their operators)
## Operators competing for access
## Computers idle
# Time-sharing & Threads
(Next gen and I/O)
## Multi-processir machines
## Parallel control stacks
## Parking/Unparking
## Preemption
> There are two types of code on the JVM. The ones that block and the ones that don't. But they come in different API flavors.
[/DRAFTING]

#### Serial execution

If you've never heard about Flynn's or Duncan's "Taxonomy of computer architecture" I encourage you to read
[Flynn's wikipedia entry][Flynn] or [Duncan's paper][Duncan], it's fascinating: the history of programming is **all
about scheduling**!

The first computers were capable of running a set of instructions sequentially and would sit idle the rest of the time
before or after its execution. 

{{< img src="/img/loom/sequential.png" title="Sequential execution" alt="Schema of sequential execution, also detailed in text in the following paragraphs." width="100%">}}

By the way, did you know that programmers haven't always existed?

Of course asked like that this quiestion sounds stupid: **of course** you know. It's not like a Chicken and Egg problem,
programmers couldn't exist before computers.  
But did you know that the word "computers" existed before those machines were even built?

I'm making a short digression here but bear with me. If you search about the history of computing for a bit, and
especially about the history of **women in computing**, thanks to the work of passionate and driven researchers it
won't be long until you find some really interesting stuff. For example, you could learn that during World War II,
women in particular [were hired as "computers" to calculate ballistics trajectories by hand][computers], or you could
also read this great story about Katherine Johnson: [She Was a Computer When Computers Wore Skirts][Johnson]).

The diagram above show the execution of two distinct programs on such a computing machine. "Operators" were the names
of the first computers who used computers.  
Here we have two operators, the first one starts "writing a program", which actually consists in wiring cables and
turning switches (among other things) to make the computer calculate the solution to a problem.

Once the computer is working, Operator1 must wait the end of the program to get the result and only then Operator2 can
access the computer to write her program and make it run. There is no ability for Operator1 and Operator2 to parallelize
their work, their programs are executed sequentially.

Of course you know that computers only got more powerful since then.  
Sequential execution became a problem because the bottleneck to solve problems faster shifted from computers to humans
being too slow: even with new ways to write programs (punch cards, magnetic tapes) the time taken by each programmer to
"insert" its program into the machine became unacceptable—i.e., too expensive.

#### Batch processing & Time-sharing

In this situation, a good scheduler has to do something. Right?  
In this case, the schedulers wre computer scientists and manufacturers, and they came up with this really sensible idea
that to amortize the cost of running many jobs sequentially one could ["batch"][batch] them.

Not only let programmers write program instructions on their own, away from the machine, but let them "offer" their
programs to a waiting queue of jobs to be performed.

{{< img src="/img/loom/batch.png" title="Batch processing" alt="Schema of batch processing, also detailed in text in the following paragraphs." width="100%">}}

In the diagram above, Program1 and Program2 are submitted one after the other, so Program1 runs first and Program2 will
run after.

This model worked well and long enough to have people joke about how inneficient it was for the programmers. Why?
Because having to add your program into a "giant" batch of jobs for the program to compute and wait for the computer to
return all the results of all those jobs in order to get yours was a massive pain in the ass

{{< img src="/img/loom/kropotchev.jpg" title="Batch processing" alt="Silent spoof movie relating the adventures of a programmer waiting on batch processing." width="100%" link="https://www.computerhistory.org/revolution/punched-cards/2/211/2253">}}

It's a common problem in scheduling, when optimising for a use case/parameter (less computer idle time) you end up
making it worse from another point of view (more programmer idle time). So where did the bottleneck go after that?  
You guessed it, humanz!

Computers kept getting faster and faster for sure, and although programmers tried to make the best of the computing power
they could get, any single user wouldn't make efficient use of a computer by herself!

The processes, to do something useful, have to be fed with inputs (whether it'd be

Q07PhW5sCEk

[Toulouse JUG]: http://www.toulousejug.org/
[Quasar]: https://docs.paralleluniverse.co/quasar/
[Clojure]: https://clojure.org/
[core.async]: https://clojure.org/news/2013/06/28/clojure-clore-async-channels
[Pulsar]: https://docs.paralleluniverse.co/pulsar/
[Flynn]: https://en.wikipedia.org/wiki/Flynn%27s_taxonomy
[Duncan]: https://www.icloud.com/iclouddrive/0rYdyArRSYYupsRrEXSJ_cLgw#Duncan_-_1990_-_A_Survey_of_Parallel_Computer_Architectures
[computers]: https://www.history.com/news/coding-used-to-be-a-womans-job-so-it-was-paid-less-and-undervalued
[Johnson]: https://www.nasa.gov/centers/langley/news/researchernews/rn_kjohnson.html
[batch]: https://en.wikipedia.org/wiki/Batch_processing

