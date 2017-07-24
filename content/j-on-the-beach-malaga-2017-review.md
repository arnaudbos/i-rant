---
title: J On The Beach 2017 — Review
date: 2017-06-26T23:01:54+01:00
description:
parent: blog
categories: ["conference"]
tags: ["conference", "review"]
draft: true
seoimage: img/j-on-the-beach-malaga-2017-review/hall.jpg
gallery: true
highlight: true
---

{{< img src="/img/j-on-the-beach-malaga-2017-review/panoramic.jpg" title="Panoramic view of the seaside of Malaga — Picture taken while hiking, the day after the conference" alt="Picture taken while hiking, the day after the conference" width="100%">}}

-----

[J On The Beach](https://jonthebeach.com/) (JOTB) is *"A Big Data Conference On The Beach"* happening in Malaga (Spain) and this year (2017) was its second edition.

# tl;dr

* The staff was great, the speakers awesome and the talks interesting.  

* I felt very comfortable and almost at home during the three days. The
atmosphere felt like being at a local conference but with famous and
renowned speakers from around the world.  

* Everyone I met - speakers/attendees/staff - was accessible and affable.  

* I will detail the lunches and dinner in the rest of the article to make
pauses between talks reviews and technical explanations, but it's fair to say
that the food was ***great*** and that ***the organizers made sure we wouldn't
forget our stay***.  

* The party at the end of the event was ***fantastic***, I've never seen so
many geeks dance {{< emoji content=":astonished:" >}}

-----
I've discovered JOTB by accident last year, during the revelation of the
[Panama Papers](https://panamapapers.icij.org/) when I was searching for
technical details about how the [ICIJ](https://www.icij.org/about) managed to
analyse this massive leak of data.  
I stumbled-upon Mar Cabra's
[*"The tech behind the bigger journalism leak in history"*](https://www.youtube.com/watch?v=rjqHC5dYdfE)
, and after watching a few other talks I decided it was worth keeping an
eye on it for the next edition.

Soon enough, the 2017 line-up was released, followed by the
[schedule](https://jonthebeach.com/schedule) and when
I saw the subjects and the speakers I managed to get one of the first round of
tickets

{{< tweet 816912096948613120 >}}

# Tabla de contenido

<div id="toc" class="well col-md-12">
  <!-- toc -->
</div>

# Overview

I'll start by giving my experience of the conference just to try to
make you feel what it was like to be there, I hope it'll give context for
reading my reviews of the talks.

{{< gallery >}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/venue.jpg"
  size="4032x3024" caption="La Termica" width="100%" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/hall.jpg"
  size="4032x3024" caption="Campero Hall" width="505" height="390" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/corridor.jpg"
  size="3024x4032" caption="Corridor" width="285" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/jotb-logo.jpg"
  size="3024x2873" caption="JOTB Logo" width="203" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/termica.jpg"
  size="1200x800" caption="La Termica" width="291" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/workshop.png"
  size="1200x800" caption="Jepsen Workshop" width="291" %}}
{{< /gallery >}}

* The title of the conference *"A Big Data Conference On The Beach"* is not a
scam: the beach is right there, 200 meters away from the conference venue
[La Termica](http://www.latermicamalaga.com/).

* The location was easy to find because it is a cultural center. It is not in
the city center, more halfway between the airport and the center and can
be reached in 40 minutes walk by the beach. There are hotels around and a
bus stop 50m from the entrance, as well as city bikes spots.

* The place in itself is nice, the hallways and rooms are large and bright.

* The staff was really welcoming every days, everyone speaking English and
willing to assist the speakers and attendees.

* The registration process took forever on the first day but someone was
available to guide me to my workshop room so I didn't have to search
(thank you!).

* The necklace with printed program schedule and plan was a really great idea,
but on the other hand the names of the attendees were not that legible.
Also the bracelet that's *"your pass for the three days"* that you can't
remove at night or to shower was annoying to me.

* During the workshop and also during the talks, one could find several power
strips along the chairs.

* The conference WiFi was great during the two days of conference, but was
really awful during the workshop and we even experienced an outage during
20 to 30 minutes (which is unfortunate when you're `SSH`-ing onto cluster
nodes to run Jepsen tests).

* Breakfast, coffee/tea and milk was catered every morning and bottles of
(cold) water were available all day long. There were also bottles of waters
ready for each attendee in the workshop room which was a nice treat.

* Speaking of treats, the swag bag was OK, but please bear in mind that *I'm
not much fan of goodies* in general:

  * The t-shirt was cool, plus there were women sizes
  {{< emoji content=":raising_hand:" >}}

  * The aluminum water bottle was also a good idea

  * Only one sticker?

  * A frisbee, because it's on the beach, you know
  {{< emoji content=":sunglasses:" >}}

* The rest I can't remember and didn't bring back home... ¯\\_(ツ)_/¯

# Day1: Jepsen Workshop

I wrote about this workshop [in a separate blog entry](../j-on-the-beach-jepsen-workshop)
to save some space and not mix topics.

This other entry is just scratching the surface of what Jepsen is, what it does
and how to does it.

I'm planning to write more about it in the future and I'll
make sure to put links here when it's done.

# Day 2: Talks

## Eric Ladizinsky —  Evolving Quantum Computers

This is the kind of opening that really stick with me.  
If you don't know about Quantum Computers,
[D-Wave Systems](https://www.dwavesys.com) and Eric Ladizinsky,
don't feel bad, I didn't know either (if you knew, good for you!).

So the conference started with a man going up on stage really naturally,
presenting himself not as a programmer nor computer scientist but as a
physicist.

He started talking about how, given our current tools and all computing power,
we are still unable to solve the real problems we are facing a civilization:
poverty, injustice, ...others!  
Which, put that way, is frankly quite depressing...

He took a step back and gave us his analyze on how, through evolution,
our species ([Homo genus](https://en.wikipedia.org/wiki/Homo))
evolved step by step by first ***discovering***, ***using*** and then
***mastering*** new tools (think {{< emoji content=":fire:" >}},
{{< emoji content=":tractor:" >}}, {{< emoji content=":factory:" >}}),
and how *quantum physics* might be another step forward.

He drew the parallel with computer science through our limited ability to find
the most advanced models for a task in data science: we manage to find good
results through the use of lots of training data and clever algorithms.  
But when some algorithm can be successful by throwing more computing power at
them, some problem remain [intractable](https://en.wikipedia.org/wiki/Computational_complexity_theory#Intractability).

Quantum computers leverage the principles of quantum physics
like `untanglement` in order to evaluate all possible models and find the
most efficient solution.

Eric gave us a great analogy with forgery: consider the molecules of metal
forming a sword.  
The best sword ever can only be achieved by find the best shape, the best
alignment possible of its molecules.

So, of all the possible combinations, which one is the best?

Quantum physics can help us explore all the possible solutions, and find the
most efficient models whereas classical computers would require centuries of
computation.

Considering quantum computers like that really blew my mind. Of course
realizing this kind of processors is hard and expansive, but they already exist!

D-Wave are making quantum computers, and each new evolution is faster than the
previous one at a much higher rate than our current CPUs.

At the moment it seems like only the NASA and Google can afford to buy some
of them and try them on, but we, as programmers,
[we can already program them](https://www.dwavesys.com/software)
with our current tools (Python, C++) in cloud based environments.

I'm short on words and my knowledge of quantum physics, quantum computers and
the programming model to use them is limited, but if you are interested,
here are some links:

* [WTF Is Quantum Computing? A 5-minute Primer](https://www.cbinsights.com/blog/quantum-computing-explainer/)
* [What will NASA be doing with its new quantum computer?](http://io9.gizmodo.com/what-will-nasa-be-doing-with-its-new-quantum-computer-1468333514)

## Inés Sombra — The trouble with distribution

[Inés Sombra](https://twitter.com/randommood) is an engineer at
[Fastly](https://www.fastly.com) and in this session she talked about
**tradeoffs** in building a distributed system. And OMG she speaks really fast.

If I could summarize the entire talk in a single word, it would be
***"tradeoffs"***.

Taking as an example the implementation of Faslty's
[Image optimizer](https://docs.fastly.com/api/imageopto), Inés presented
several stages of the evolution of the Image optimizer and the pros and cons
of each solution.

To give a little bit of context and grossly describe this service, it is and
*image processing and caching* solution.  
You give it the location of your original quality content images and a set
of ***V****arnish* ***C****onfiguration* ***L****anguage* rules, and each
time one of your clients requires an image it points to this high quality
image and gives query parameters about the expected size, ratio, quality, etc,
of the result it needs.  
Then Fastly's Image optimizer will search for a cached version of the image
result, optimize it on the fly in case of a cache miss, and eventually return
the expected result after caching it.  
Of course a lots of details are missing but this is the big picture...

Throughout the talk, Inés weights the costs and benefits of:

* *pre-processing all the catalog then caching* VS *on-the-fly transforming
on a per-request basis then caching*, or
* *being centralized* VS *being distributed*, or
* *being consistent* VS *being available* (or being *"fast enough"*), or
* *stripping metadata* VS *keeping metadata*, etc.

All in all, I think the talk had both too much and too little content. Let me
explain:

It is true that Inés speaks really fast, but she still had to rush through
the last couple of slides in order to finish on time.  
On the other hand, the talk was really high level on technical details and I
think that was the goal, but the tradeoffs slides with only pros and cons
about the "current" version of the service, with not a lot of details
about how this "current" version was designed made it difficult to really
realize what/how choices were made.

It's worth re-watching and take time to rewind now that the video is available:

{{< youtube m8CyenESI1c >}}

## Kyle Kingsbury — Jepsen Talk
Distributed systems behave badly
Hard to test
Don't trust vendors documentation, they're not evil but have physical/budget limits to testing
Do tests
Hope this will lead to better software

Once again, [aphyr](https://aphyr.com/) strikes with his presentation skills
and humor.

To paraphrase him: ***"our databases are on fire"***, and what we do as
developers is try to hide this instability to our users through nicely
designed and shiny APIs.

But if we do our best for our users to continue riding around on unicorns
all day long, we are sitting on top of systems of which we don't really know
the inner workings.

Since 2014, Kyle has been using [Jepsen](http://jepsen.io/) to test databases,
queues and other distributed systems on the basis of what he suspected was
wrong or what the README and documentation of those softwares were stating.

He found numerous "bugs" and "inconsistencies", published them in a series
of blog posts and worked with some of the teams to help analyze problems
and improve stability.

What he learnt was that we should all test the software we use and we depend
on.  
We should carefully read the claims and documentations of such systems,
but not take anything for granted, because all of these systems are developed
under physical and budget constraints, because formal testing is hard and
simulation testing is hard.

This talk is also, in my opinion, a call to acknowledge that implementations
of such distributed systems might not be perfect, and that some kinds of
errors, loss or corruptions might be acceptable from a domain point
of view, but that problem should not be hidden and assumptions must be
validated.

{{< youtube tpbNTEYE9NQ >}}

In the same series, I find this previous talk about Jepsen to be more
informative for someone interested in ***learning*** about distributed systems, and
it also might be the funniest talk I've ever watched:
[Jepsen II: Linearizable Boogaloo](https://www.youtube.com/watch?v=QdkS6ZjeR7Q).

## Dharma Shukla — Lessons learnt from building a globally distributed database service from the ground up

For the first few seconds I was a bit bored by the tone of the presentation.
Boy I was wrong!

During this talk, I've been introduced to
[CosmosDB](https://azure.microsoft.com/en-us/services/cosmos-db/), formerly
known as DocumentDB.

Honestly, I didn't even know this was a thing, and yet it is a really
interesting product developed during the last 7 years by Microsoft:
a *schema-less*, *multi-model*, *globally distributed* database in Azure Cloud.

There are articles that will better describe Cosmos DB than I could do, but
what I will keep from Dharma's talk is the amount of work the teams at
Microsoft seem to have invested in ensuring strong and comprehensible SLAs
for latency and availability, but also for consistency and throughput,
which is another level of quality.

Cosmos DB is fully ***schema agnostic*** and has its own way of structuring data
so that you don't have to worry about it (RDF under the hood?), and it
also "indexes all the data".

Cosmos DB is ***multi-model*** in the sense that it supports *document*,
*graph* of *key-value* data types and uses a *SQL-like* query dialect as well
as MongoDB or graph APIs.

Another important feature of Cosmos DB is it's **5 different levels of
consistency**, from *"strong consistency"* to *"eventual consistency"*,
Microsoft has implemented three other consistency models based on academic
researches.

Cosmos DB is available as a service on *Microsoft Azure Cloud*. There you can
choose and *elastically control with API calls* the amount of storage and
*scale the throughput* of your application and pay as you go down to
a *granularity of a second*.

All those features are really impressive, they even brought
[Leslie Lamport](https://fr.wikipedia.org/wiki/Leslie_Lamport) (inventor of
the [Lamport clock](https://en.wikipedia.org/wiki/Lamport_timestamps) and
the [Paxos consensus protocol](https://en.wikipedia.org/wiki/Paxos_(computer_science))
among [other things](https://en.wikipedia.org/wiki/LaTeX)) on board and
used it's [TLA+](https://en.wikipedia.org/wiki/TLA%2B) formal language to
design and verify for correctness. And they also use Jepsen...

I wasn't expecting to be amazed by this talk but honestly if you're not
afraid of vendor lock-in or are already using Microsoft Azure, it looks amazing.

{{< youtube 3Sq9AJzgxAg >}}

## Lunch: Wraps+beers

The first day's lunch consisted of wraps of different flavors, chicken, ham
or veggie.  
They were good enough, and open beer bar was present just like the day before
so one just had to manage to eat and drink wisely in order to be fully ready
for the talks of the afternoon.

The lunch took place in the backyard just like on Wednesday, on the grass,
under a beautiful sunny sky.

During the meal I reconnected with some of my fellow Jepsen workshop-ers
[Andrei Dan](https://twitter.com/rocketarium) and
[Sebastian Utz](https://twitter.com/einsmu) from [CrateDB](https://crate.io/),
[Max Neunhöffer](https://twitter.com/neunhoef) from
[ArangoDB](https://www.arangodb.com/) and Sayat Satybaldiev from
[Zalando](https://jobs.zalando.com/tech/blog/) to share our experiences about
the morning talks.

## Anjana Vakil — Custom Query Languages: Why? How?
Design CQL like a UX with user/usability in mind
Powerful abstractions
Parser + AST

{{< youtube y5wMNehHYBY >}}

## Roland Kuhn — Taming distribution: formal protocols for Akka Typed
Typed actors

{{< youtube kpvTd49YJpQ >}}

## Christopher Meiklejohn — Just-Right Consistency: Closing The CAP Gap
OMG WAT?
Antidote db

{{< youtube Vd2I9v3pYpA >}}

## Santiago Ortiz — Dynamic data visualization

Santiago Ortiz has a really interesting way of looking at things and
explaining how he thinks data visualization is meant to become a more
democratized way at looking at data and just something reserved to data
scientists as it is today.

Referring to the mental palace technique for remembering complex numbers, he
draw the following parallel:

> Machines use numbers for encoding of information including stories.
>
> And we need stories to store numbers.

The rest of the talk was a portfolio of data visualizations which was nice
to see but not really informative unless it is your thing.

On the plus side, one of the examples Santiago gave was about a book :
[Rayuela](https://en.wikipedia.org/wiki/Hopscotch_(Cort%C3%A1zar_novel) by
Julio Cortázar.  
This book as he explained, has been written in such a was that it can be read
linearly or in an unordered way following a pattern suggested by the author to
jump from chapter to chapter.  
The visualization consisted in circles that allowed the viewer to screen
and read from a chapter to another.

{{< youtube 7t0ob7nGjN8 >}}

## Dinner: Pizza+beers+other attendees+meetups I didn't go because sleep

# Day 3: Talks
## Duarte Nunes — ScyllaDB: NoSQL at Ludicrous Speed

I was interested in this talk because of its summary highlighting the work
done on performance on [ScyllaDB](http://www.scylladb.com/).

So ScyllaDB is a distributed databased based on the same model as
[Cassandra](http://cassandra.apache.org/): dynamo-based. Its characteristics are
**high write availability**, **eventual consistency** and inability to do join
queries.

ScyllaDB is *fully compatible* with Cassandra's API and file format so you can
drop replace it without a single code line change.

Where ScyllaDB delivers is on the performance aspects. According to the
benchmarks (we all love a good benchmark don't we), ***a 3 nodes ScyllaDB
cluster is able to sustain the same load as a 30 nodes Cassandra cluster***.  
This is basically Cassandra on steroids

 So how did the team manage to do that?

The answer is lots of time and efforts applying ***good algorithms*** and
***mechanical sympathy*** principles in order to squeeze every bit from CPUs
and memory.

ScyllaDB uses a ***thread-per-core*** model to avoid *context-switching* between
threads and allocates each core a **private memory pool**. So blocking calls are
forbidden and ScyllaDB adopts an *asynchronous* programming style with *futures
and promises* for networking and file I/O and use message passing between CPUs
to hide latency from accessing memory.

On the memory side, ScyllaDB is very cautious about its memory space allocation
and manages to ***compact*** memory to avoid fragmentation.

ScyllaDB also features an interesting and tunable *self-monitoring* tool
(benchmarking itself) able to analyze the stress under which the database is
and make decisions on which process to prioritize (queuing requests) and how
to deal with background tasks such as *compaction*.

If you have a use case for Cassandra and are critical about your resources
usage, I think it's really worth taking a look at ScyllaDB which is also open
source by the way.

{{< youtube Qsj6KkbjMGI >}}

## Danielle Ashley — Uniting Church and State: FP and OO Together

When preparing my schedule, I selected this talk
because it "*talked about FP and OO together*" and I really like the idea
that a lot of OO problems could be solved by using more FP
concepts and good practices.
Or use OO techniques in order to gain performance in our FP code, for
instance.

So I just *completely ignored* the first part of the talk and didn't
understand the "Church and State" pun until later, so yeah, I'm stupid.

**After** the conference I did my homework and looked at this church
encoding thing and I realized it is really interesting as an exercise.
At first, I didn't see where it'd be useful unless you're trying to implement
a new language. But Danielle talked about the performance side of things, and
it started to click.

Church encoding is a way of representing data and operators using *nothing
more than recursion and anonymous functions*. In this talk, Danielle is not
showing us how to implement **Church Numerals** but how, by thoroughly
designing out programs, we can eventually make it simpler to *reason about*
(keep state in isolation), *invert the flow of control* (push-pull *FRP* style
instead of downstream method calls) and *improve performance* (less object
instantiation).

This will deserve another blog post in the future to explore this topic.

{{< youtube Nn0eb9Tl1Bo >}}

## Justo Ruiz Ferrer — Adressing the elephant in the room: what a post-Hadoop era looks like

The title mislead to expect a talk giving insight about *where* the big data
processing techniques could go, but in the end it didn't deliver.  
The talk was mainly just an explanation of the *"my big data is bigger than
your big data"* saying and a quick demo of [Valo](https://valo.io/)'s streaming
platform.

The speaker (and CTO of the company hosting the event) is clearly comfortable
talking to an audience and fills the room with his speaking, and
I expected some insights or takeaway thoughts about the direction in which
our industry is going.  
But I ended up with sort of a statement about our architectures moving from
batch processing to streaming processing, with the lambda architecture
(he didn't mention it) and micro-batch processing architectures and
techniques in between.

No video available for this one.

## Lunch: Paella!!!

The last day's meal was an authentic
[Valencian Paella](https://en.wikipedia.org/wiki/Paella), and I can tell you
they nailed it. It was fantastic.

Once again in the backyard under a sunny sky I've had the occasion to share
with other conference attendees about the previous talks and speak about
programming languages and other geeky stuff. Fun time.

## Caitie McCaffrey — Distributed Sagas: A Protocol for Coordinating Microservices

Super interesting talk about how to orchestrate multiple *microservices* in
order to achieve *consistency* of a single business action.

**Distributed Sagas** is a protocol with no standard or open source
implementation (yet) designed to create a layer of *coordination* on top of
several (micro)services without having to write *yet another service*.

The example given in this talk is really simple and powerful: you have a
*flight booking* service to which was added a *hotel booking* service as well
as a *car rental* service, and you want to offer your customers the ability
to *book an entire trip* including those three services.  
How do you ensure *business/domain level consistency* without creating a
new service?

This is not just an exercise: with the rise of microservices, we have moved
away from the consistency guarantees offered by our databases and must
maintain application-level consistency, or *feral consistency*.  
And for each new service that is deployed and available we could
develop `n` new combinations of *coordination* services in order to deliver new
value to customers...

Distributed Sagas introduces the concept of a **SEC**
(***S****aga* ***E****xecution* ***C****oordinator*) along with
*action requests* and *compensating requests*.

Based on a persistent *log*, the stateless **SEC** will come and write the
actions of other services and operations results to the log ***after
acknowledgement*** of the success of the action.  
The **SEC** is then responsible to manage the consistency of the operations from
a domain perspective and *replay* or *rollback* operations in case of failure.

The log in this case is like a *stack*, where each operation performed by a
service is *idempotent* (in order to be replayed in case of delay or timeout for
instance) and has a corresponding *backward idempotent action* (*compensating
requests*) that can be performed: a semantic *undo*.

Now this is interesting: because an *action request* from a service may be
delayed or just fail without returning (and we can't make a difference), it is
important that it be *idempotent* in order to be replay.  
This way, we avoid the complexity of having to send a *compensating request*
to undo an *action request* that may or may not have succeeded.  
By replaying, we make sure that we get a result and then we "cancel" if need be.

For this protocol to be applicable, the business/domain model must be
compatible with eventual consistency because each service being exposed
individually has its state exposed, but the whole saga can not be made
*isolated* nor *atomic* under such constraints without introducing a lot
of latency and single point of failures (see the part of the talk about
*two phase commit*).  
Indeed, the **SEC** itself might look like a SPOF, but it is actually stateless
so just spawning a new instance of it and replay the log is sufficient to
continue the saga.

From what I understood both Uber and Twitter are currently
experimenting this technique, so we will have to wait and see what comes out.

{{< youtube 0UTOLRTwOX0 >}}

## Martin Thompson — High Performance Managed Languages

Famous for his work on high performance coding,
Martin Thompson is a renowned expert and speaker.

At JOTB17, he gave us a talk about managed languages: languages that run on a virtual machine which provides garbage-collected memory management (Java, C#, etc.).

I've already listened to Martin in various podcasts
and one thing that he mentions quite often is that:

> Our CPUs Arent't Getting Any Faster.

But most of our applications aren't really CPU intensive,
they are intensive in their memory access.  
Hardware constructors are adding cores in order to increase speed, and they are doing wonders, but ourlittle understanding of how CPUs and memory access works prevents us from gaining all of the benefits.

Just like there was a debate between Assembler vs Compiled Languages, there's an argument between managed languages vs native languages and this talk is just about that.

Managed languages benefit from JIT optimizations that a sole static compiler cannot provide.  
These are predictions made on real, measured data, and if it happens that the bets are wrong, the JIT can correct itself, revoke some decisions and make new optimizations.

**Code branching**

If a static compiler armed with a good programmer branch prediction hint/information can be a solid optimization, a managed language is able to use real data instead of static decisions and adjust over time.

**Methods inlining**

Methods/Functions can be inlined in case of hotness in order to avoid function call overhead.

**Loop unrolling**

The optimizer can also measure which loops are hotspots
and which are not, and decide to unroll some rather than check and iterate.

**Intrinsics**

The runtime can also replace programmer instructions by architecture specific instructions (intrinsics) in order to benefit from a the particular implementation.  
Something impossible for static compiler unless compiling for a specific architecture.

**Polymorphism**

Managed runtimes can avoid virtual dispatch or going through jump tables by directly calling the '*right*' method of an object or inlining it.  
By checking the number of subtypes we can decide whether or not it's worth to just calling the method directly,
using a simple `if` statement, go polymorphic or cache the code for a particular type if it is hotter than the others.

**Garbage Collector**

The performance of a Garbage Collector depends upon its implementation, but also on how we, as programmers, deal with our objects.

Martin outlines the fact that it is not allocation that is costly, but reclamation...  
The longer and object lives, the more operations the GC needs to do.

In the end, THE most important thing to performance is TIME: how much time and effort are we willing to put into implementing good algorithms and doing right choices.

Some takeaways:
- Avoiding cache misses
- Amortising expensive operations
- Mechanical sympathy
- Algo & Data-structures

Fun facts:

Martin is the author of Aeron, a high throughput, low latency messaging system (used by Onyx, BTW).

The Java implementation of Aeron is faster than native implementation of other fast messaging systems: because of the time and efforts put into implementing good algorithms.

Aeron has been ported to C++, then Go, then C#: and in the end the fastest implementation is in C#.  
C++ could be but would take much, much more time.

All comes down to {{< emoji content=":hourglass_flowing_sand:" >}}...

{{< youtube Pz-4co8IaI8 >}}

## Dinner + Party

After dinner (Camperos + beer), the organizers invited a
flamenco dancer, singer and guitarist.  

And after the local culture and art, the party started.

# Conclusion

I think I've said it in the first part of the article but the conference felt very comfortable and almost like a local conference, despite the number of people from abroad and the awesome speakers.  
It was then really easy to speak to people, even speakers.

So thanks a lot to the organizers, speakers and other attendees for the lovely event.

And thanks a lot to MonkeyPatch for sending me in Malaga!



Bonus track "The Computer Science behind a modern distributed data store" by Max Neunhoef from ArangoDB.

I've talked multiple times with Max during the three days of the conference and didn't even know he was a speaker until the last day.

I've watched his talk later on Youtube and I've found it really interesting to get a sense of some major problems one stumble upon while learning about or working on distributed systems.

It goes fast so if you are not "familiar" with distributed systems problems such as consensus, log structured merge trees, clocks and distributed ACID transactions you will find a lot of usefull information in order to learn more about these topics.

{{< youtube m9xYejDUdus >}}
