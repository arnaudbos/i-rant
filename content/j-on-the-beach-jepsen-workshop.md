---
title: J On The Beach 2017 — Jepsen Workshop
date: 2017-08-07T16:23:04+01:00
description:
parent: blog
categories: ["conference"]
tags: ["conference", "review", "jepsen"]
draft: false
gallery: true
highlight: true
---

{{< gallery title="Photos shamelessly stolen from Kyle Kinsburry's website" >}}
  {{% galleryimage file="/img/j-on-the-beach-jepsen-workshop/-012.jpg"
  size="900x675" caption="Network's Laggy" width="395" %}}
  {{% galleryimage file="/img/j-on-the-beach-jepsen-workshop/-013.jpg"
  size="900x675" caption="Store it maybe" width="395" %}}
{{< /gallery >}}

-----

The first book I've read about distributed systems was
[Distributed Systems: Principles and Paradigms](https://www.amazon.fr/Distributed-Systems-Principles-Andrew-Tanenbaum/dp/0132392275)
by Andrew S. Tanenbaum and Maarten van Steen.  
I was reading at the library on my last year of study and I remember being
confused very often, and to be honest I didn't even finish it...

By the end of 2013 or beginning of 2014, I progressively started to regain
interest in distributed systems after having been distracted by programming
on mobile devices for a while.

At that time, something appeared on my radar, it was named
[Jepsen](http://jepsen.io/), and this year I've had the chance to finally give
it a try by myself by attending [J On The Beach](../j-on-the-beach-malaga-2017-review)
and its Jepsen workshop animated by no one else than the author:
[Kyle Kingsbury](https://aphyr.com/).

<!-- toc -->

# J On The Beach Day 1: Jepsen Workshop

After a warm welcome and a handshake, [Kyle Kingsbury](https://aphyr.com/)
started introducing [Clojure](https://clojure.org/) for a brief moment:
the syntax, data structures literals and the immutability part,
just enough to be able to follow the hands-on part of the workshop.

It's true that Clojure might be disturbing at first glance, like any new
programming language's syntax.  
If you're interested in learning Clojure (you should), Kyle's blogpost series
[Clojure from the ground up](https://aphyr.com/tags/Clojure-from-the-ground-up)
and Daniel Higginbotham's [Clojure for the Brave and True](http://www.braveclojure.com/clojure-for-the-brave-and-true/)
are really great resources before you go into more in-depths topics with
[The Joy of Clojure](http://www.joyofclojure.com/) for instance.

After this quick *aperitivo*, Kyle gave us an overview of [Jepsen's design and core concepts](https://github.com/jepsen-io/jepsen#design-overview), namely:
Operations, Generators, Clients, Nemesis, History and Checkers (and Models)
which, combined together and executed, form a Jepsen Test.
So what is [Jepsen](http://jepsen.io/)?

I think Kyle started from an observation that lots of people agree about:
Testing distributed systems is hard. So after an argument about a consistency
issue, he went and implemented a tool to introduce failure in systems and
check the outcome.

Starting from READMEs and gut feelings, he begun to create a battery of tests
for some of the best know distributed databases and key-value stores "we" use,
and found [disturbances in the force](https://aphyr.com/tags/jepsen):

> He found complex errors lying into the heart of some of the softwares we use.

If you talk with Kyle, he makes it really clear that he's not blaming vendors
for introducing bugs and is not implying that they are intentionally hiding
faults in their systems, but they can omit details or make assumptions or
simply not test every corner case.

Jepsen is meant to do ***simulation testing*** (see other
[system testing methods](http://queue.acm.org/detail.cfm?id=2889274)) on
distributed systems such as databases, distributed caches, etc., finding
real/production errors, not theoretical ones.

Starting to think about
[Netflix's Chaos Monkey/Simian Army](https://github.com/Netflix/SimianArmy)?  
Close enough.  
The [Simian Army](https://github.com/Netflix/SimianArmy) is a suite of
***fault-injection*** tools meant to introduce *catastrophic errors* by
terminating nodes/regions or introducing *network instability*.

While Jepsen can also inject this kind of faults, it is more concerned about
***correctness*** than ***availability*** or ***latency***.

# How does it work?

[Jepsen](https://github.com/jepsen-io/jepsen) is designed to run as a
**cluster**: the number of nodes is parametrizable but it seems 3 to 5 nodes
are usually enough to reproduce and/or detect *catastrophic errors*
([source](https://www.usenix.org/conference/osdi14/technical-sessions/presentation/yuan)).

One of the nodes is the **control node**, which is responsible for logging
into the other nodes via `SSH` and *execute* the tests.  
The other nodes will ship the software at test and execute the operations.

As said in the github's page:

> Once the system is running, the control node spins up a set of logically
single-threaded processes, each with its own client for the distributed system.
A generator generates new operations for each process to perform. Processes
then apply those operations to the system using their clients. The start and
end of each operation is recorded in a history. While performing operations,
a special nemesis process introduces faults into the system--also scheduled
by the generator.

The point of having single-threaded processes is to avoid unnecessary
complexity on the testing side, in order to make the work of the *checker*,
which will analyze the *history*, more manageable.

## Operation

An operation is an abstract representation, a common language
(a data structure) to express a function invocation onto the system at test.

Stripping the details of the `clojure` implementation, here are examples of
operations (think JSON documents, JavaScript Objects, Java HashMaps,
Python Dictionaries, whatever seems familiar to you):

```clojure
;; a read: we don't know what value we'll read from the system yes so value is 'nil
{:type :invoke, :f :read, :value nil}

;; a write of an integer that will be performed on the system
{:type :invoke, :f :write, :value (rand-int 5)}

;; a compare-and-set of an old integer and a new integer value
{:type :invoke, :f :cas, :value [(rand-int 5) (rand-int 5)]}
```

These operations are *`invocation operations`*, they just describe a bunch of
types of operations for the *clients* to perform.

It is the *generator*'s job to actually generate a few operations and then let
the *clients* performs the operations and return *`completion operations`*
(with the read value specified for instance).

## Client

A client is an implementation of the types of function invocations you want to
perform onto the system.

Given the three *operations* above, one must implement a `clojure protocol`
(think Java interface, but better) in order to actually perform the operation,
for instance implementing a write of an integer to the database you are
willing to test, using your SDK/library of choice.

## Generator

Generators are more sophisticated beast from my little understanding. While an
*invocation operation* is a kind of template of an operation to be performed,
a generator is responsible for creating more of them. Without giving you all
the options available, I can tell that a generator can be configured to
construct new *`invocation operations`* for a certain type (read for instance)
or a mix (a mix of read and writes, or a mix of read, writes and
compare-and-set).  
Other parameters include the ability to stagger operations by a certain amount
of time, impose a time limit for performing operations, etc.

## Nemesis

A nemesis is our chaos monkey: it will introduce failures, such as network
partitions, and heal them back.

Nemesis operations are also constructed by the generator, to which we can
specify an order of execution, such as a cycling: sleep operation, start
operation (introduce a failure), sleep operation (hoping some client operation
will fail) and then stop operation (end the failure).

## Pizza + beers + chit-chat

That was the content of the workshop for the morning, and it went *fast*.

For lunch, the organizers at JOTB had planned on-demand pizzas and beer. It was
also the occasion to discover the backyard of the venue under a bright sky.

I sat at a table with Kyle and other trainees and chitchatted about
[Jepsen analyses](http://jepsen.io/analyses) and systems that other attendees
were interested in testing.  
We also talked about Clojure and languages but mostly the discussion focused on
Kyle's work/studies, how he funded his researches and how he approached (or
was being approached by) databases or other softwares vendors in order to
improve their product/communicate about its robustness/bugfixes.

And then back to work.

## History

> With our generator and clients performing operations, we've got a history
> to analyze for correctness.
[Jepsen etcdemo - checker](https://github.com/jepsen-io/jepsen/blob/master/doc/checker.md)

The history is not directly exposed to you as a test designer, but it is
necessary to verify the results of the operations.  
Under the hood I suppose (I haven't checked yet) that Jepsen stores the history
in some way as a collection of every *completed operations*.

This history is then used by the *checker* in order to verify correctness
between what the systems said it did and what it actually did.

Jepsen also uses the history to generate a set of graphs.

## Checker

Now, this history is not going to analyze itself. This is the role of the
checker, whose job it is to reorder the calls/results from all the clients
and confront the history to a model.

When a client writes a value and gets back an acknowledgement of the write, the
system's log is checked to see if the database actually performed the write.

## Model

A model is an abstraction on top of the
[***linearizability***](https://en.wikipedia.org/wiki/Linearizability) concept.

It's not easy to defined linearizability, I've had to read and watch so many
resources to try to understand that I can't count them, and in the end I'm not
sure I understand it well.

Linearizability is a type of consistency among others (eventual consistency,
causality linearizability, strong linearizability, etc).

In short, and probably not fully exact: when a new value for a certain key
has been written (successfully), no previous value for this key should be read
ever, and every subsequent read should return the same value unless another
successful write is performed.

Now, Jepsen uses [Knossos](https://github.com/jepsen-io/knossos) under the hood
to build a singlethreaded model that is holding the history of operations and
results, and by reducing (I guess, but it's an implementation detail) over the
operations, analyses the state to find illegal state transitions.

## Q/A

**How do you find a bug?**

As Kyle explained, he generally started by reading the READMEs and
documentations of the systems he wanted to evaluate.

Then it's a mix of gut feeling and experience that leads him toward testing
a feature or another, and apply different types of failures to the system.

In the end what is important is that Jepsen is a tool that enables anyone to
test the softwares it uses and make informed choices.

**How long does it take?**

I honestly can't remember the answer to this question and I did not write it
in my notes. So maybe someone (a fellow trainee, a reviewer) can fill-in this
gap?

# Wrap-up

This workshop is usually done in two days so keeping up with the fast coding
pace and trying to fit all the concepts and information in my head in a
single day was hard.  
And by "hard" I don't mean *painful*, but *exhausting*. It was actually
pleasant, and Kyle is a ***fantastic*** teacher: he is funny and he really
cares.
Nonetheless, I'm pretty sure I wasn't the only one in the room feeling really
tired at the end of the day, so I took a long walk
(by the beach {{< emoji content=":sunglasses:" >}}) in order to clear my mind.

If you want to know more about Jepsen, and learn more about distributed systems
in general (which is my case), you can follow the etcdemo guide by yourself
at home, but if you have the possibility to attend one of Kyle's workshop,
don't hesitate, he really *knows his stuff* and he is a *fantastic person*.  
