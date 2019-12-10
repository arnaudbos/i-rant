---
title: J On The Beach 2018 — Review
date: 2018-07-05T23:12:11+01:00
description:
parent: blog
categories: ["conference"]
tags: ["conference", "review"]
draft: false
seoimage: img/j-on-the-beach-malaga-2018-review/plaza_del_obispo.jpg
gallery: true
highlight: true
math: true
---

{{< img src="/img/j-on-the-beach-malaga-2018-review/plaza_del_obispo.jpg" title="360 Photo — Plaza del Obispo" alt="360° picture of Plaza del Obispo" width="100%">}}

-----

[J On The Beach](https://jonthebeach.com) (JOTB) is *"A Big Data
Conference On The Beach"* happening in Malaga (Spain) and this year (2018)
was its third edition.

# tl;dr

* The staff was great, the speakers awesome and the talks interesting.  

* I felt very comfortable and almost at home during the three days. The
  atmosphere felt like being at a local conference or meetup but with famous and
  renowned speakers from around the world.  

* Everyone I met - speakers/attendees/staff - was accessible and really, really
  affable.  

* YES, I totally did copy/paste the three points above from
  [last year's review](../j-on-the-beach-malaga-2017-review).

* Less great than last year: no diner included, "just" breakfast and lunch so
  I had to eat outside, like, in tapas bars or restaurants by the beach, yeah
  poor me...

* Awesome line-up, great diversity, good job there {{< emoji content=":thumbsup:" >}}

* Met a few people from last year and a few new people, cool!

* Unfortunately I couldn't attend the party but I'm sure it was as great as last
  year's.

-----

# Preamble

I wasn't sure I would attend JOTB 2018 because I had more or less decided to
attend only one big conference this year, like [Strange Loop](https://www.thestrangeloop.com/)
or the [Clojure/conj](http://clojure-conj.org/).  
And then I saw [the speakers](https://jonthebeach.com/speakers) announcements
on Twitter, one after the other, and couldn't resist.  
So here's my review of J On The Beach 2018!

<small>Hopefully shorter than last year's</small>

<!-- toc -->

# Overview

{{< gallery >}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2018-review/termica_landscape.jpg"
  size="4032x3024" caption="La Termica" width="100%" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2018-review/jungle_landscape.jpg"
  size="4032x3024" caption="Parque de Málaga" width="395" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2018-review/joeerl.jpg"
  size="4032x3024" caption="Joe Armstrong Interview" width="395" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2018-review/venue_panoramic.jpg"
  size="6306x1902" caption="Garden" width="100%" %}}
{{< /gallery >}}

* The title of the conference *"A Big Data Conference On The Beach"* is still not a
scam: the beach is right there, 200 meters away from the conference venue
[La Termica](http://www.latermicamalaga.com/).

* The location was easy to find because it is a cultural center. It is not in
the city center, more halfway between the airport and the center and can
be reached in 40 minutes walk by the beach. There are hotels around and a
bus stop 50m from the entrance, as well as city bikes spots.

* The place in itself is nice, the hallways and rooms are large and bright.

* The staff was really welcoming, everyone speaking English and
willing to assist the speakers and attendees.

* YES, I totally did copy/paste the four points above from
  [last year's review](../j-on-the-beach-malaga-2017-review).

* The registration was sooo smooth. Compared to last year, they've split the
  stream of incoming attendees into two groups by alphabetical order which was
  a good move.

* I'll skip the goodies part for this year, they were OK: couple of notepads,
  stickers, sunglasses, sunscreen ^^

# Day1: Erlang Workshop

New year, new workshop!  
And this year I've had the chance to get a taste of [erlang](https://www.erlang.org/)
instructed by no one else than: [Joe Armstrong](https://twitter.com/joeerl)!

{{< img src="/img/j-on-the-beach-malaga-2018-review/erlang.png" alt="erlang: let it crash" width="50%" center="">}}

The workshop started with a crash course on Erlang's syntax and 101 concepts:
Joe Armstrong made us all **open a REPL and experiment**.

We discovered Erlang's advanced pattern matching, data structures, destructuring,
case statements, function definitions, actors, etc.  
A very good introduction but as Joe remarked, all of this
we could learn by ourselves, so after 1 or 2 hours of this he pointed us
toward [this one day course](https://github.com/joearms/courses/tree/master/1_day)
and to other resources such as his own book or this [Learn You Some Erlang for Great Good](http://learnyousomeerlang.com/content) book.

The rest of the workshop was pieces of wisdom and stories told by a man who's
earned his gray hair and seen a lot of things in his career, it was fascinating.

Of course we heard many anecdotes about Erlang and Ericsson but we also
learned a lot about "the 4 erlangs":

* Sequential Erlang
* Concurrent Erlang
* Fault-tolerant Erlang
* Distributed Erlang

I especially like the presentation of Erlang as layers and how they stack up
to create a language that is really suited to today's massively parallel and
distributed environments.

Let me plagiarize the [chapter 15 of the PDF course](https://github.com/joearms/courses/blob/master/1_day/one_day_erlang.pdf)
I liked above:

Sequential Erlang:

• Simple functional language  
• Lists, tuples, atoms, bignums, floats, ...  
• Function selection is by pattern matching  
• Data selection is by pattern matching • Variables are immutable

Concurrent Erlang:

• Adds spawn, send and receive to sequential Erlang.  
• register [unregister] can be used to associate a name with a process

Fault-tolerant Erlang:

• catch .. throw and try ... catch ... end added to sequential Erlang  
• link, process_flag(trap_exit, true) added to concurrent Erlang

Distributed Erlang:

• Add +spawn(Node, Mod, Func, Args) to concurrent Erlang  
• Or use explicit term passing over sockets

Et Voilà! You get a "multi-core ready, fault-tolerant distributed programming"
language with a proven track record of successful projects for 30 years and
counting.

# Day 2: Talks

## Vlad Mihalcea — Transactions and Concurrency Control Patterns

If you've read [my review from last year's Jepsen workshop](../j-on-the-beach-jepsen-workshop),
you've probably noticed that I like to understand things that fail.

Even if we don't go as far as Kyle Kingsbury, pushing the boundaries (and
[writing fantastic analyses](http://jepsen.io/analyses)), we all implicitly
know that distributed systems are complicated pieces of software.  
We tend to code for the happy path, hope everything works well and in case it
doesn't, well, what could go wrong?

After all, we've used relational databases for years, and ORMs, and we all know
that when we use transactions, everything is fine and `ACID`: our tool and SQL
will deal with the complexity...

Of course this isn't true, and [Vlad Mihalcea](https://twitter.com/vlad_mihalcea)
is here to raise awareness: we **have** to understand those concepts.

Different relational databases have different concurrency control mechanism
available with different guarantees, each with their own tweaks and tradeoffs.
And we must understand those technical details in order to choose a database
and write correct software.

Let's mention a few concepts:

* Serializability
* Linearizability
* Two-Phase-Locking
* MVCC
* Snapshot isolation
* Dirty read
* Stale read
* Phantom read
* Read skew
* Write skew
* Lost update

I really really make a parallel between his talk and Kyle Kingsbury's [Jepsen series of talks](http://jepsen.io/talks), because they deal with similar
concepts but a different scale (my personal favorite: [Jepsen II: Linearizable Boogaloo](https://youtu.be/QdkS6ZjeR7Q)).

I've talk at length with Vlad during the conference, my feeling was that this
talk shouldn't be rated as "intermediate" but as "expert" because of the
concepts involved, but he made me change my mind.

This level is indeed "intermediate" given the topic: transactions and
concurrency. Every developer with more than 5 years of experience and working
with databases should know and understand those concepts.

Of course this is not the case, and this is why talks like Vlad's are important.

[The video is now available on Youtube.](https://youtu.be/4qiV_QhPyIk)

## Venkat Subramaniam — Exploring Java 9

One does not simply "Explore Java 9" with [Venkat Subramaniam](https://twitter.com/venkat_s).

With Java 10 around the corner you might wonder why bothering with Java 9?

Well, Java 9 brings a few changes and additions to Java that are worth knowing
to keep up with the language so it's better not to skip it.

Again, this is not new stuff so I won't enumerate the updates here but I can
mention the new try-with-resources properties, a couple of new operators on
`Stream`, `IntStream` and `Optional`, the new immutable collections factories
`List.of("One", "Two", "Three");`, etc.

Most important in my opinion are the Java modules, and also the feature that
will probably be the most ignored feature, but my personal favorite: `jshell`.

Of course Venkat Subramaniam is an awesome speaker and because these days a
talk on Java wouldn't be a good talk on Java without making fun of Java (?),
so go have fun watching him.

[The video is now available on Youtube.](https://youtu.be/OWOkB80p2DY)

## Martin Kelppmann - Automerge: Making servers optional for real-time collaboration

I really enjoy learning and reading about distributed systems, database in
particular, I find them fascinating.

In this field, [Martin Kelppmann](https://twitter.com/martinkl)'s book,
[Designing Data-Intensive Applications](https://dataintensive.net/), is a really
valuable resource to learn about reliability, consistency and scalability issues
and find more specialized resources and papers to study the field later on.

In this talk, Martin presents the difference between **consensus** and **convergence**
and, using really simple words, analogies and examples, slowly makes its way
to a very interesting topic: [*Conflict-Free Replicated Data Type* (CRDTs)](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type).

[Automerge](https://github.com/automerge/automerge) is a work in progress and
the result of studies on CRDTs, their representation and algorithms to
automatically resolve concurrent modifications without losing updates and
progressing toward replicas state *convergence*.

Another great aspect is that Automerge does not presuppose of a specific
network protocol, it is network-agnostic.

What's even more interesting, is that [Automerge](https://github.com/automerge/automerge),
although a research topic, already has a practical implementation and a library
available as an npm package, and it also has a few example applications available
on the github page of the project using different network protocols.

I'm very eager to see what kind of application I could build using CRDTs in the
future.

Link: [A Conflict-Free Replicated JSON Datatype](https://arxiv.org/abs/1608.03960)

[The video is now available on Youtube.](https://youtu.be/PHz17gwiOc8)

## Sergey Bykov — Distributed Transactions are dead, long live distributed transaction!

Last year I've been really impressed by [Dharma Shukla's talk about CosmosDB](http://i-rant.arnaudbos.com/j-on-the-beach-malaga-2017-review/#dharma-shukla-lessons-learnt-from-building-a-globally-distributed-database-service-from-the-ground-up) and the
engineering effort put by Microsoft.

In this talk, [Sergey Bykov](https://twitter.com/sergeybykov) gives an
introductory presentation of [Microsoft Orleans](https://dotnet.github.io/orleans/).

It's really interesting to see big companies, move back from full eventual
consistency models and tackle the problem of distributed transactions.

In a nutshell, Orleans is an actor model (encapsulated state, message passing, ...)
based framework for the .NET ecosystem with support for transactions (beta).

And this is really exciting even if you're not a .NET aficionado,
because it has extensive documentation for you to study if you like
to read about distributed systems.

Links:

* [Life Beyond Distributed Transactions: An Apostate’s Opinion – Pat Helland, 2007](http://adrianmarriott.net/logosroot/papers/LifeBeyondTxns.pdf)
* [The Morning Paper on the above publication](https://blog.acolyer.org/2014/11/20/life-beyond-distributed-transactions/)

[The video is now available on Youtube.](https://youtu.be/2ylIl_QToq4)

## Jonas Boner — Designing Event-First Microservices

"Designing Event-First Microservices", is a talk about event sourcing by
[Mr Jonas Bonér](https://twitter.com/jboner).

[Event sourcing](https://martinfowler.com/eaaDev/EventSourcing.html) is one very
interesting answer to the kind of problems encountered in microservices,
illustrated by the [Death Star diagram](https://www.google.fr/search?q=death+star+microservices&tbm=isch).

By emitting events, services can be more naturally decoupled and made reactive
to their environment. And we know being [reactive](https://www.reactivemanifesto.org/)
is a concept dear to the heart of Jonas Bonér.

In this talk we also get to hear about [Event storming](http://ziobrando.blogspot.com/2013/11/introducing-event-storming.html)
and [Domain Driven Design (DDD)](http://dddcommunity.org/), and a few
illustrations on how to handle state and side-effects.

If you're already familiar with this topic or these concepts, or you've already
watched a couple of talks by [Greg Young](https://twitter.com/gregyoung) or
read a couple of articles by [Martin Fowler](https://twitter.com/martinfowler),
you probably won't learn a lot by watching this talk, but if you don't, by all
means please watch this and reconsider your way of building services, it's
mind blowing.

[The video is now available on Youtube.](https://youtu.be/iDey1GoAJy0)

# Day 3: Talks

## Joe Armstrong — Good ideas that we forgot.

It is hard for me to write a summary of a talk by
[Joe Armstrong](https://twitter.com/joeerl).  
When he's telling, you're listening.

In this keynote he talked about our relatively young industry's fascinating
ability to forget about stuff that was invented just a few decades ago.

A few days ago, [Andrei Dan](https://twitter.com/_andreidan) (whom I met last
year [at JOTB 2017](../j-on-the-beach-malaga-2017-review)) and fellow "craties"
wrote a review of a bunch of talks they attended at JOTB18, among which is a
review of this keynote.

I encourage you to read [their review](https://crate.io/a/craties-go-to-j-on-the-beach/).  
They also cover talks I didn't attend so you'll find other interesting
feedbacks.

Anyways, go watch any of his talk, you'll learn something or listen to a good story,
or both.

[The video is now available on Youtube.](https://youtu.be/YuBi7Qs555U)

## Don Syme — The F# Path to Relaxation

This is the story of building a language to bridge the gap between
functional programming purism and pragmatism, leveraging OO and .NET benefits,
and gaining momentum and building a community.

If I were into .NET I'd sure as heck would give it a try.

[Don Syme](https://twitter.com/dsyme) goods a really detailed explanation
of the probles/disputes Microsoft was trying to solve by building a language
that would be the synthesis of FP and OO, here are some examples:

* Functional VS .NET interoperability
* Functional VS Object Oriented
* Functional VS Practical
* Code VS Data
* Pattern Matching VS Abstraction

I think the tradeoffs are really sound in what they adopted, what they
deprecated and what they kept and decided "tolerate".

I was particularly interested in the community aspect and adoption of the
language and what they used to achieve it: openness (remember, Microsoft...),
keep it neutral, accepting contributions, etc.

If I were to be snarky however, I'd say that between the plethora of functional
languages available on the JVM today and C# kinda sorta functional abilities,
developers on the .NET platform didn't have much choice but to embrace this new
language for functional programming.

Please don't let this last remark get to you, have a look at the talk, it's
really interesting from a language geek point of view.

[The video is now available on Youtube.](https://youtu.be/CLuHokcr63k)

## Martin Thompson — Cluster Consensus: when Aeron met Raft

This time again, I encourage you to read [CrateDB people's review](https://crate.io/a/craties-go-to-j-on-the-beach/)
of this talk by the one and only
[Martin Thompson](https://twitter.com/mjpt777) rather than reading what I could
have wrote about it.

They give very interesting links but I really want to point you to the very
first paper Martin did talk about in his talk:

* [Simple testing can prevent most critical failures: an analysis of production failures in distributed data-intensive systems Yuan et al. OSDI 2014](https://www.usenix.org/system/files/conference/osdi14/osdi14-paper-yuan.pdf)
* [The Morning Paper on the above publication](https://blog.acolyer.org/2016/10/06/simple-testing-can-prevent-most-critical-failures/)

[The video is now available on Youtube.](https://youtu.be/8Q1qbgAcOv8)

## Shagufta Gurmukhdas — Real-time object detection coz YOLO!

Machine learning and deep learning have been really hot topics for the past
couple of years. Take projects such as [AlphaGo](https://en.wikipedia.org/wiki/AlphaGo)
for instance: The program's victory over human the world's best Go players and
its the next versions (AlphaGo Zero and AlphaZero) achievements are pretty darn
impressive.

The future is bright for computerized human assistance and engineers willing to
get their feet wet can now find industry-grate libraries and tons of learning
resources online.

I know near nothing about deep learning and neural networks myself but I'm
currently working at a clients which is the world's largest supplier of Earth
observation systems so even if I'm not working on image analysis per say I'm one
way on another into this atmosphere (no pun intended).

Anyways, I was really interested to go to this talk by
[Shagufta Gurmukhdas](https://twitter.com/shaguftamethwan) to discover
[YOLO (You Only Look Once)](https://pjreddie.com/darknet/yolo/), a "real-time
object detection system" by [Joe Redmon](https://twitter.com/pjreddie).

{{< img src="/img/j-on-the-beach-malaga-2018-review/yolo.png" alt="YOLO" width="100%">}}

Shagufta gave a really nice talk composed of a lightweight introduction to
[neural nets](https://en.wikipedia.org/wiki/Artificial_neural_network), a
presentation of YOLO and then a very entertaining demo of an object detection
application first from an image source, then from a video recording and finally
from a live webcam stream using Python, [TensorFlow](https://www.tensorflow.org/)
and [OpenCV](https://opencv.org/).

**Kudos for the live-demo, this was great!**

I've played with motion detection using OpenCV last summer, for my
client, where I was building a stream processing prototype using
[Apache Storm](http://storm.apache.org/) and needed a dummy algorithm to put
in there for the algorithmic part.

I didn't put a lot of thoughts into it but adding object detection would be an
fun use-case and this presentation definitely made it more interesting.  
I've used the latest version at the time which was 3.3 and support for YOLO
has now been added in version
[3.3.1](https://github.com/opencv/opencv/wiki/ChangeLog#version331):

> The partial Darknet parser, enough to parse YOLO models, as well as the
layers to support a few variations of YOLO object detection networks have been
integrated.

I'll definitely try to write something about this in the future.

[The code is available on GitHub](https://github.com/ShaguftaMethwani/yolo-jotb)

[The video is now available on Youtube.](https://youtu.be/ej9wFtHZu4c)

## Mario Fusco — Lazy Java

Lazy lazy lazy. Kids are lazy when it comes to chores and homework, or not!  
According to [Mario Fusco](https://twitter.com/mariofusco) they just do
optimizations to minimize the effort they must provide, and this is what we
should make our programs do.

Java is not a lazy language by default, it evaluates right away. It has
short-circuit evaluation to simulate this in the case of `||` and `&&` and other
operators but it is fundamentally eagerly evaluated.

Then comes the `Stream` API in Java 8 which provides a specification for data
manipulation instructions but which is not fully lazy.

Mario presents several examples of problems (primes, big palindromes) that can
be made efficient with laziness and how to implement them using Java with
recursion, which leads to stack overflows and from tail call optimization (or
lack thereof in Java) to trampolines.

Fantastic speaker, of course.

[The video is now available on Youtube.](https://youtu.be/iIEd12Q_umo)

## Hadi Hariri — Asynchronous Programming with Kotlin

It won't come as a surprise to people who know me to read that I have strongly
mixed feelings about Kotlin itself, but I wanted to hear about Kotlin coroutines
from the source (JetBrains).

[Hadi Hariri](https://twitter.com/hhariri) is a local from Malaga and as he
joked himself: he might have been the most affordable speaker for the
conference organizers this year.

His talk about Kotlin coroutines was really well put with a lot of code examples
for various scenarios and running code samples.

If you've never heard of the concept of green threads, fibers or
coroutines (minus the implementation details) in other languages, or
if you are interested in to use this feature in Kotlin, don't miss this talk.  
Hadi Hariri is a great speaker.

The video is not yet available on Youtube, so I've found
[this recent recording](https://youtu.be/krfGMLuhB8M) from a few months ago.

# Conclusion

Much like the previous year, J On The Beach 2018 has been a really interesting
conference and joyful experience.

**Thanks a lot to the organizers, speakers and other attendees for the great
event.**

Who knows, I might even come back next year for the third time in a row ¯\\_(ツ)_/¯

* Virtual handshake to [Rosa Castillo](https://twitter.com/rosacastilloPhD) and
  [Vlad Mihalcea](https://twitter.com/vlad_mihalcea) for the Hallway Track and
  fun time during the breaks.
* Also to [Andrei Dan](https://twitter.com/rocketarium) and
  [Max Novelli](https://jonthebeach.com/speakers/40/Max+Novelli), it was great
  to see you again after JOTB17
* Final thank you note to [MonkeyPatch](http://www.monkeypatch.io/) (my
  employer) for allowing the budget to attend a conference in Malaga
  **once again**!
