---
title: J On The Beach 2017 — Review
date: 2017-06-26T23:01:54+01:00
description:
parent: blog
categories: ["conference"]
tags: ["conference", "review"]
draft: false
seoimage: img/j-on-the-beach-malaga-2017-review/hall.jpg
gallery: true
highlight: true
math: true
---

{{< img src="/img/j-on-the-beach-malaga-2017-review/panoramic.jpg" title="Panoramic view — Seaside of Malaga — Hiking the day after the conference" alt="Picture taken while hiking, the day after the conference" width="100%">}}

-----

[J On The Beach](https://jonthebeach.com/) (JOTB) is *"A Big Data
Conference On The Beach"* happening in Malaga (Spain) and this year (2017)
was its second edition.

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

* I came for distributed systems topics, and I've been served.  

* The party at the end of the event was ***fantastic***, I've never seen so
many geeks dance {{< emoji content=":astonished:" >}}

-----
I've discovered JOTB by accident in 2016, during the revelation of the
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

I didn't know how to cut this article, maybe in half? or maybe an entry by
talk was more appropriate?  
So I just kept everything in a single page and here is the table of contents
for you to hop around and read about the topics that you are interested in.

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
[D-Wave Systems](https://www.dwavesys.com) or Eric Ladizinsky,
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
like [`entanglement`](https://en.wikipedia.org/wiki/Quantum_entanglement)
in order to evaluate all possible models and find the most efficient solution.

Eric gave us a great analogy with forgery: consider the molecules of metal
forming a sword.  
The best sword ever can only be achieved by finding the best shape, the best
arrangement possible of its molecules.

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
with our current tools (Python, C++) thanks to cloud based environments and
several layers of abstraction.

I'm short on words and my knowledge of quantum physics, quantum computers and
the programming model to use them is limited, but if you are interested,
here are some links:

* [WTF Is Quantum Computing? A 5-minute Primer](https://www.cbinsights.com/blog/quantum-computing-explainer/)
* [What will NASA be doing with its new quantum computer?](http://io9.gizmodo.com/what-will-nasa-be-doing-with-its-new-quantum-computer-1468333514)

## Inés Sombra — The trouble with distribution

[Inés Sombra](https://twitter.com/randommood) is an engineer at
[Fastly](https://www.fastly.com) and in this session she talked about
**tradeoffs** in building a distributed system. And *OMG she speaks really
fast* {{< emoji content=":race_car:" >}}

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

Once again, [aphyr](https://aphyr.com/) strikes with his presentation skills
and humor.

To paraphrase him: ***"our databases are on fire"***, and what we do as
developers is try to hide this instability to our users through nicely
designed and shiny APIs {{< emoji content=":rainbow:" >}}

But if we do our best for our users to continue riding around on unicorns
all day long, we are sitting on top of systems of which we don't really know
the inner workings.

Since 2014, Kyle has been using [Jepsen](http://jepsen.io/) to test databases,
queues and other distributed systems on the basis of what he suspected was
wrong or what the README and documentation of those softwares were stating.

He found numerous "bugs" and "inconsistencies", published them in a
[series of blog posts](http://jepsen.io/analyses)
and worked with some of the teams to help analyze problems and improve
stability.

What he shares with us is that ***we should test the software we use and
we depend on***.  
We should *carefully read* the claims and documentations of such systems,
but not take anything for granted, because all of these systems are developed
under physical and budget constraints, because *formal testing is hard* and
*simulation testing is hard too*.

Implementations of distributed systems might not be perfect, and some kinds of
*errors*, *loss* or *inconsistencies* might be acceptable **from a domain point
of view**, but that problem should not be hidden and assumptions must be
validated.

{{< youtube tpbNTEYE9NQ >}}

<h5>More on Jepsen and simulation testing:</h5>

In the same series, I find this previous talk about Jepsen to be more
informative for someone interested in ***learning*** about distributed systems,
and it also might be the funniest talk I've ever watched:
[Jepsen II: Linearizable Boogaloo](https://www.youtube.com/watch?v=QdkS6ZjeR7Q).

I've also came across this
[talk about simulation testing](https://www.youtube.com/watch?v=4fFDFbi3toc)
at FoundationDB. [FoundationDB](https://en.wikipedia.org/wiki/FoundationDB)
was a closed source *"multi-model NoSQL database"* that has been acquired
by Apple since this talk.

## Dharma Shukla — Lessons learnt from building a globally distributed database service from the ground up

For the first few seconds I was a bit bored by the tone of the presentation.
Boy I was wrong!

During this talk, [Dharma Shukla](https://twitter.com/dharmashukla) introduces
us with
[CosmosDB](https://azure.microsoft.com/en-us/services/cosmos-db/), formerly
known as DocumentDB.

Honestly, I didn't even know this was a thing, and yet it is a really
interesting product developed during the last 7 years by Microsoft:
a *schema-less*, *multi-model*, *globally distributed* database in Azure Cloud
{{< emoji content=":earth_africa:" >}}

There are articles that will better describe Cosmos DB than I could do, but
what I will keep from Dharma's talk is the amount of work the teams at
Microsoft seem to have invested in ensuring strong and comprehensible SLAs
for latency and availability, but also for consistency and throughput,
which is another level of quality.

CosmosDB is fully ***schema agnostic*** and has its own way of structuring
data so that you don't have to worry about it (RDF under the hood?), and it
also "indexes all the data".

CosmosDB is ***multi-model*** in the sense that it supports *document*,
*graph* of *key-value* data types and uses a *SQL-like* query dialect as well
as MongoDB or graph APIs.

Another important feature of CosmosDB is it's **5 different levels of
consistency**, from *"strong consistency"* to *"eventual consistency"*,
Microsoft has implemented three other consistency models based on academic
researches.

CosmosDB is available as a service on *Microsoft Azure Cloud*. There you can
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
or veggie {{< emoji content=":burrito:" >}}  
They were good enough, and open beer bar was present just like the day before
so one just had to manage to eat and drink wisely in order to be fully ready
for the talks of the afternoon.

The lunch took place in the backyard just like on Wednesday, on the grass,
under a beautiful sunny sky {{< emoji content=":sunny:" >}}

During the meal I reconnected with some of my fellow Jepsen workshop-ers to
share our experiences about the morning talks:

* [Andrei Dan](https://twitter.com/rocketarium) and
  [Sebastian Utz](https://twitter.com/einsmu) from [CrateDB](https://crate.io/);
* [Max Neunhöffer](https://twitter.com/neunhoef) from
  [ArangoDB](https://www.arangodb.com/);
* Sayat Satybaldiev from [Zalando](https://jobs.zalando.com/tech/blog/) and
* [Donnchadh Ó Donnabháin](https://twitter.com/donnchadho) from
  [Poppulo](https://www.poppulo.com/)

## Anjana Vakil — Custom Query Languages: Why? How?

In this talk, [Anjana Vakil](https://twitter.com/anjanavakil) explains what is the
difference between a ***DSL*** and a ***CQL*** and gives us some insights on
how to design such languages with the goal of providing powerful abstractions
while keeping usability in mind.

{{< youtube y5wMNehHYBY >}}

## Roland Kuhn — Taming distribution: formal protocols for Akka Typed

I'm interested in actors but not familiar enough with Scala, Akka and the
specific implementation that is presented here to provide a good review of
this talk.

{{< youtube kpvTd49YJpQ >}}

## Christopher Meiklejohn — Just-Right Consistency: Closing The CAP Gap

[Christopher Meiklejohn](https://twitter.com/cmeik) is a former software
developer in industry turned researcher after having worked at
[Riak](https://en.wikipedia.org/wiki/Riak) at [Basho](http://basho.com/).  
In this talk he talks about "***Just-Right Consistency***" or how to "*exploit
availability whenever possible and only synchronize exactly when needed to
enforce application invariance*".

Topics of the talk:

* Invariants
* **CAP** theorem
* **AP** and **CP** systems
* Consensus
* Causality
* Relative order
* Joint updates
* **CRDT**
* ...

And this is me watching the talk:

{{< img src="/img/j-on-the-beach-malaga-2017-review/wat.jpg" title="What is this, I don't even." alt="wat" width="100%">}}

This one gave me headaches, seriously. I'm not going to be able to summarize it
in a short paragraph so I'll go for the long one.

The state of cloud databases that has been for many years is that we
traditionally put out data in databases in datacenter(s). We have clients
from all over the world accessing a *primary* copy and we *geo-replicate* in
order to ensure *fault-tolerance* and provide *high-availability* and
*low latency*.

And in the event that one or more of the copy of the database cannot
communicate with one another we have to make a choice, once and for all:

We either choose a
*Consistent-under-Partition* (**CP**) or an
*Available-under-Partition* (**AP**) approach.

Characteristics of a **CP system**:

* All the operations are *synchronized* (write or read) on order to "*treat all
  the copies as one logical unit*". A *consensus* protocol must be used in order
  to ensure a single system image across replicas.
* This is [***serializability***](https://en.wikipedia.org/wiki/Serializability)
  (e.g., Google Cloud Spanner): provide *strong consistency* an minimize the
  amount of time you have to wait, but *still slower than an AP system*.
* Often *over-conservative* but very popular because easier to program! On the
  programmer side, *the application is written without thinking about
  distribution*: if I write something then I will read this something, this is
  a guarantee of
  [***linearizability***](https://en.wikipedia.org/wiki/Linearizability).

Characteristics of an **AP system**:

* Operations are *executed against local copies* and then the result of those
  operations are then *propagated asynchronously* which may take time
* Reads and writes happen *extremely fast*, so we may have ***stale reads***
  and ***write conflicts*** between two updates on two different copies of
  the database since they haven't *synchronized*: they need to reconcile.
* The system can keep operating in the even of failure: *Available but
  difficult to program*.

Acknowledging the fact that there is no such thing as a *"one-size-fits-all"*
consistency model for applications, Christopher presents us a better approach:

Express our application invariants (properties of the system that must always
hold true), and provide a way for the system to automatically infer from
the application and tailor the consistency choices (a.k.a. feature by feature)
based on those invariants to guarantee that no violation occur,
this is ***Just-Right Consistency***.

Characteristics of **Just-Right Consistency**:

* Write *sequential programs* that enforce *application level invariants*
  and preserve this application behavior *when deployed under*
  ***concurrency*** *and* ***distribution***.
* We will have **AP** compatible invariants: under an **AP** system we can
  guarantee these invariants without synchronization.
* And **CAP**-sensitive invariants: *one way* VS *two ways* communication
  invariants:
  *  One way: an operation that may or may not happen *atomically* or in a
     particular order, but we *don't need a response* so it works without
     synchronization
  * Two way: two operations that are *related* or *dependent* will require
    *coordination*
* Provides tools for *analysis and verification* (e.g., at the IDE level): tell
  whether or not the application *will be safe when it's deployed*, bringing
  the application code and the database closer together.

In the talk we follow the case study of a lifecycle management tool for
prescriptions and drug delivery to demonstrate how some application invariants
may be **AP** compatible while other being **CAP**-sensitive.

Our invariants will be:

* ***Relative order*** (*referential integrity*): Create a prescription record
  and reference it by a patient
* ***Joint updates*** (*atomicity*): Create prescription, then update doctor,
  then patient, then pharmacy
* ***Precondition check***: Deliver medication only the intended number of times

The first two invariants are **AP** compatible, and the third one is
**CAP**-sensitive as we shall see.

Characteristics of an **AP** compatible invariant:

* No synchronization: can operate locally without blocking. Its updates are
  applied locally and asynchronously propagated.
* Updates are fast and exploit *concurrency*.
* Updates must *commute*: *non-commutative* updates require **CP**.


What [***commutativity***](https://en.wikipedia.org/wiki/Commutative_property)
is:

Imagine we have two replicas of a register that are in agreement.

If two concurrent operations set different values (e.g., 2 and 3) for a
specific key on the two replicas and we don't synchronize, we will have a
divergence.  
Replication without coordination will not produce a single outcome, the system
cannot by itself decide which value should win unless being told so.

These two operations are said to be *non-commutative*.

Can we make non-commutative updates commutative?  Yes:

* Using deterministic conflict resolution: pick a value that wins.  
  Use a timestamp-based algorithm (Cassandra's ***L****ast* ***W****rite*
  ***W****ins*), or an application level merge/resolution (CouchDB, OrientDB,
  Aerospike, Riak).
* using
  [***CRDT***](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type)s:
  **CRDT**s are extended sequential data types that encapsulate (built-in)
  deterministic merge functions. I'm not very familiar so I will try to get into
  more details in future blog posts, but there are many different designs of
  CRTDs for different uses.

So what happens when we assume that the system we're building has this property?  
All operations will commute.

Let's examine *relative order* and *joint updates* first.

**Relative order** invariant:

* We do something, then other actions, and ensure that the updates
  are seen in the proper order, this is relative order.
* If an operation P implies Q than the program makes sure that Q is written
  before P. As long as it's in order on the target replica and that changes are
  replicated in the proper order, the invariant is never violated.

So we need a system that ensures
[***causal consitency***](https://en.wikipedia.org/wiki/Causal_consistency):

> Updates that are causaly related to other updates (that influence other
> updates) that happen in an order should be delivered in the exact same order

**Joint updates** invariant:

* Given *relative order*, two replicas with updates are causally consistent,
  so far so good
* But still, inconsistent state of the database can be read by
  clients/replicas *between* updates
* *Joint updates* relate to
  [***atomicity***](https://en.wikipedia.org/wiki/Atomicity_(database_systems)):
  all-or-nothing

This is solved by grouping updates atomically in batches, taking snapshots
along the way so clients (or other replicas) read consistent snapshots.

By ensuring these invariants on the database side based on the sequential
program on the client side, at this point we are designing a system that has
*relative order* plus *joint updates*, that is:

***Transactional Causal Consistency***: the strongest **AP** compatible
consistency model.

Now for our last invariant: **Preconditions check**:

* Preventing getting the same prescription twice
* This is basically a counter with addition and subtraction operation

What happens when these operations happen concurently?

Addition for instance: Take a counter and apply an addition on a replica
and another addition on a second replica. We assumed earlier that operations
on our system will commute, so for instance adding 2 and then 3 is the
same thing as adding 3 and then 2.

Addition is then stable under concurrency and we don't have to synchronize.

Subtraction on the other hand is not stable under concurrency. If we
decrement our counter on a replica and do the same thing on another
replicat without synchronizing, we could break the invariant that the
counter must always be greater than zero.

This is **CAP**-sensitive.

We have two solutions do deal with this issue:

* First: **¯\\_(ツ)_/¯**
  We can be fine with this tradeoff and acknowledge that our invariant
  *wasn't really an invariant*, e.g.: ensure that a pacient gets its
  prediction rather than being too conservative.
* Second: Forbid concurrency, a.k.a *synchronization*.

Wow, that was a long read!

This model is interesting because we can choose *for each operation*
what consistency model suits best. And when I say "we", I say the
*system*. Because it's hard to reason about these problems and we need
*new tools*.

Need tools to analyse and allow or not operations to proceed by analysing
*where invariants could be violated* and tell us where we're fine and
where we need *synchronization*.

This is a research topic with lots of movement.  
Cristopher presents one model: the
[CISE Analysis](https://syncfree.lip6.fr/index.php/2-uncategorised/51-cise)
([paper](http://software.imdea.org/~gotsman/papers/cise-tool.pdf)),
but I'm not going to go into
[details](https://github.com/SyncFree/CISE) at this point.

So we have [AntidoteDB](http://syncfree.github.io/antidote/):
[Open-source](https://github.com/SyncFree/antidote), implemented in Erlang,
built on top of Riak core, providing ***Transactional Causal Consistency***
and is in alpha release.

Antidote respects ***causality*** by program order through clients and
provide an *operations API* as well as a *transaction API*.

A company is materializing around it and is in the process of raising money.
The goal is to prodive the database open-source and for free and support
and tools commercially.

Really interesting topic and talk, and lots of content!

{{< youtube Vd2I9v3pYpA >}}

## Santiago Ortiz — Dynamic data visualization

[Santiago Ortiz](https://twitter.com/moebio) has a really interesting way
of looking at things and explaining how he thinks data visualization is meant
to become a more democratized way at analyzing data and not just something
reserved to data scientists.

Referring to the [memory palace](https://en.wikipedia.org/wiki/Method_of_loci)
technique for remembering complex numbers, he draw the following parallel:

> Machines use numbers for encoding of information including stories.
>
> And we need stories to store numbers.

The rest of the talk was a portfolio of data visualizations which was nice
to see but not really informative unless it is your thing.

On the plus side, one of the examples Santiago gave was about a book :
[Rayuela](https://en.wikipedia.org/wiki/Hopscotch_(Cort%C3%A1zar_novel) by
Julio Cortázar.  
This book, he explained, has been written in such a was that it can be read
linearly or in an *unordered* way following a pattern suggested by the author
to jump from chapter to chapter.  
The visualization consisted in circles that allowed the viewer to screen
and read from a chapter to another.

{{< youtube 7t0ob7nGjN8 >}}

## Dinner

The dinner was served in the backyard once again. We had fresh our of the oven
pizzas and beer as well as soft drinks.

I ate with other attendees and left shortly after so I missed the meetups
that had been organized specially for the event.

# Day 3: Talks
## Duarte Nunes — ScyllaDB: NoSQL at Ludicrous Speed

I was interested in this talk because of its summary highlighting the work
done on performance on [ScyllaDB](http://www.scylladb.com/).

ScyllaDB is a distributed databased based on the same model as
[Cassandra](http://cassandra.apache.org/): dynamo-based. Its characteristics are
**high write availability**, **eventual consistency** and inability to do
*join* queries.

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
showing us how to implement
[Church Numerals](https://en.wikipedia.org/wiki/Church_encoding#Church_numerals)
but how, by thoroughly designing out programs, we can eventually make it
simpler to ***reason about*** (keep state in isolation),
***invert the flow of control*** (push-pull *FRP style* instead of downstream
method calls) and ***improve performance*** (less object instantiation,
and avoiding `Optional` overhead in this example).

This will deserve another blog post in the future to explore this topic.

{{< youtube Nn0eb9Tl1Bo >}}

## Justo Ruiz Ferrer — Adressing the elephant in the room: what a post-Hadoop era looks like

The title mislead to expect a talk giving insight about *where* the big data
processing techniques could go, but in the end for me it didn't
deliver {{< emoji content=":elephant:" >}}  
The talk was mainly just an explanation of the *"my big data is bigger than
your big data"* saying and a quick demo of [Valo](https://valo.io/)'s streaming
platform.

The speaker (and CTO of the company hosting the event) is clearly comfortable
talking to an audience and fills the room with his speaking, and
I expected some insights or takeaway thoughts about the direction in which
our industry is going.  
But I ended up with sort of a statement about our architectures moving from
***batch processing*** to ***streaming processing***, with the ***lambda
architecture***, ***micro-batch*** processing and the ***dataflow model***
which he didn't mention.

No video available for this one.

## Lunch: Paella!!!

The last day's meal was an authentic
[Valencian Paella](https://en.wikipedia.org/wiki/Paella), and I can tell you
they nailed it. It was fantastic.

{{< gallery title="Valencian Paella for 300 person" >}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/paella.jpg"
  size="4032x3024" width="372" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/paella2.jpg"
  size="4032x3024" width="418" %}}
{{< /gallery >}}

Once again in the backyard under a sunny sky {{< emoji content=":sunny:" >}}
I've had the occasion to share with other conference attendees about the
previous talks and speak about programming languages and other geeky stuff.  
Fun time {{< emoji content=":nerd:" >}}

## Caitie McCaffrey — Distributed Sagas: A Protocol for Coordinating Microservices

Super interesting talk by
[Caitie McCaffrey](https://twitter.com/caitie) about how to orchestrate
multiple *microservices* in order to achieve *consistency* of a single
business action.

**Distributed Sagas** is a protocol with no standard or open source
implementation (yet) designed to create a layer of *coordination* on top of
several (micro)services without having to write *yet another service*.

The example given in this talk is really simple and powerful: you have a
*flight booking* service to which was added a *hotel booking* service as well
as a *car rental* service, and you want to offer your customers the ability
to *book an entire trip* including those three services.  
How do you ensure *business/domain level* ***consistency*** without creating a
new service?

This is not just an exercise: with the rise of microservices, we have moved
away from the consistency *guarantees* offered by our databases and must
maintain *application-level consistency*, or ***feral consistency***.  
And for each new service that is deployed and available we could
develop $n$ new combinations of *coordination* services in order to deliver new
value to customers...

Distributed Sagas introduces the concept of a **SEC**
(***S****aga* ***E****xecution* ***C****oordinator*) along with
***action requests*** and ***compensating requests***.

Based on a persistent *log*, the stateless **SEC** will come and write the
actions of other services and operations results to the log ***after
acknowledgement*** of the success of the action.  
The **SEC** is then responsible to manage the consistency of the operations from
a domain perspective and *replay* or *rollback* operations in case of failure.

The log in this case is like a *stack*, where each operation performed by a
service is *idempotent* (in order to be replayed in case of delay or timeout for
instance) and has a corresponding *backward idempotent action* (*compensating
requests*) that can be performed: a semantic *undo*.

Now this is interesting: because an ***action request*** from a service may be
delayed or just fail without returning (and we can't make a difference), it is
important that it be ***idempotent*** in order to be *replayed*.  
This way, we avoid the complexity of having to send a ***compensating request***
to undo an *action request* that may or may not have succeeded.  
***By replaying, we make sure that we get a result and then we "cancel" if
need be.***

For this protocol to be applicable, the business/domain model must be
compatible with *eventual consistency* because each service being exposed
individually *exposes its state*, and the whole saga cannot be made
***isolated*** nor ***atomic*** under such constraints without introducing a lot
of *latency* and *single point of failures* (see the part of the talk about
*two phase commit*).  
Indeed, the ***SEC*** itself might look like a SPOF, but it is actually
stateless so just spawning a new instance of it and replay the log is
sufficient to *continue the saga* {{< emoji content=":traffic_light:" >}}

From what I understood both Uber and Twitter are currently
experimenting this technique, so we will have to wait and see what comes out.

{{< youtube 0UTOLRTwOX0 >}}

## Martin Thompson — High Performance Managed Languages

Famous for his work on high performance coding,
[Martin Thompson](https://twitter.com/mjpt777) is a renowned expert and speaker.

At JOTB17, he gave us a talk about managed languages: languages that run on a
virtual machine which provides garbage-collected memory management
(Java, C#, etc.).

I've already listened to Martin in various
[podcasts](http://www.se-radio.net/2014/02/episode-201-martin-thompson-on-mechanical-sympathy/)
and one thing that he mentions quite often is that:

> Our CPUs Arent't Getting Any Faster.

But most of our applications aren't really *CPU intensive*, they are intensive
in their *memory access*.  
Hardware constructors are adding cores in order to increase speed, and they
are doing wonders, but our little understanding of how CPUs and memory access
work prevents us from gaining all of the benefits.

Just like there was a debate between *Assembler* VS *Compiled Languages*,
there's an argument between *managed languages* VS *native languages* and this
talk is just about that.

Managed languages benefit from ***JIT optimizations*** that a sole static
compiler cannot provide.  
These are ***predictions*** made on *real, measured data*, and if it happens
that the bets are wrong, ***the JIT can correct itself***, *revoke* some
decisions and *make new optimizations*.

**Code branching**

If a static compiler armed with a good programmer branch prediction
hint/information can be a solid optimization, a managed language is
*able to use real data instead of static decisions and adjust over time*.

**Methods inlining**

Methods/Functions *can be inlined in case of hotness* in order to avoid
*function call overhead*.

**Loop unrolling**

The optimizer can also *measure which loops are hotspots*
and which are not, and decide to *unroll* some rather than check and iterate.

**Intrinsics**

The runtime can also *replace programmer instructions by architecture specific
instructions* (***intrinsics***) in order to benefit from a the particular
implementation.  
Something impossible for static compiler unless *compiling for a specific
architecture*.

**Polymorphism**

Managed runtimes can *avoid virtual dispatch* or going through jump tables
by directly calling the '*right*' method of an object or *inlining* it.  
By checking the number of subtypes we can *decide* whether or not it's worth
to just *calling the method directly*, using a simple `if` statement,
go *polymorphic* or *cache the code for a particular type* if it is hotter
than the others.

**Garbage Collector**

The performance of a ***Garbage Collector*** depends upon its implementation,
but also on how we, as programmers, deal with our objects.

Martin outlines the fact that ***it is not allocation that is costly, but
reclamation***...  
*The longer and object lives, the more operations the GC needs to do.*

In the end, ***THE*** most important thing to performance is **TIME**:

*How much time and effort are we willing to put into implementing* ***good
algorithms*** *and doing right choices™*.

Some takeaways:

* Avoiding cache misses
* Amortising expensive operations
* Mechanical sympathy
* Algo & Data-structures

<h5>Fun facts:</h5>

Martin is the author of [Aeron](https://github.com/real-logic/aeron),
a *high throughput*, *low latency* messaging system (it is used by
[Onyx](http://www.onyxplatform.org/), BTW).

The **Java implementation of Aeron is faster than native implementation of
other fast messaging systems**: because of the ***time*** and ***efforts*** put
into implementing ***good algorithms***.

Aeron has been ported to C++, then Go, then C#: and in the end ***the fastest
implementation is in C#***.  
C++ could be but would take much, much more time
{{< emoji content=":hourglass_flowing_sand:" >}}...

{{< youtube Pz-4co8IaI8 >}}

## Dinner + Party

After dinner (Camperos {{< emoji content=":taco:" >}} +
{{< emoji content=":beers:" >}}), the organizers invited a
flamenco dancer {{< emoji content=":dancer_tone1:" >}},
singer {{< emoji content=":microphone2:" >}} and
guitarist {{< emoji content=":guitar:" >}}.  

And after the local culture and art, the party started with a rock band doing
cover songs as well as a group called
[Los Vinagres](https://g.co/kgs/zVDQ3a) coming from the
Canary Islands {{< emoji content=":island:" >}}.

{{< gallery title="Flamenco and Party" >}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/flamenco.jpg"
  size="4032x3024" caption="Flamenco group" width="395" %}}
  {{% galleryimage file="/img/j-on-the-beach-malaga-2017-review/group.jpg"
  size="4032x3024" caption="Cover songs" width="395" %}}
{{< /gallery >}}

I left with a few other attendees around midnight, going on a quest to find
[Tapas](https://en.wikipedia.org/wiki/Tapas) {{< emoji content=":fried_shrimp:" >}}

# Conclusion

I think I've said it in the first part of the article but the conference
*felt very comfortable* and ***almost like a local conference***, despite the
number of people from abroad and the *awesome speakers*.  
It was then really easy to speak to people.


**Thanks a lot to the organizers, speakers and other attendees for the great
event.**

**And thanks a lot to [MonkeyPatch](http://www.monkeypatch.io/)** (the company
I work for) **for paying and allowing me to attend a conference in Malaga!**

On a side note, I'd like to apologize for the length of this article.  
It took me ***way too long*** to write this review and if you made it thus far,
you're a hero.

I really wanted to highlight the content of each talk and it took me researches
and learning to give it back.  
I hope you enjoyed nonetheless. I don't know if or how I will split my next
conference review because I'm still new in this field, but there will be more
content on most of the topics covered here in future blog posts.

Thanks for reading, see you {{< emoji content=":wave:" >}}

{{< img src="/img/j-on-the-beach-malaga-2017-review/venue2.jpg" title="Leaving" alt="Leaving the conference" width="100%">}}

## Bonus track: The Computer Science behind a modern distributed data store

By [Max Neunhöffer](https://twitter.com/neunhoef) from
[ArangoDB](https://www.arangodb.com/).

I've talked *multiple times* with Max during the three days of the conference
and *didn't even know he was a speaker until the last day*.

I've watched his talk later on Youtube and I've found it really interesting
to get a sense of some *major problems* one can experience while learning about
or working on *distributed systems*.

It goes fast so if you are not familiar with distributed systems problems
such as
[*consensus*](https://en.wikipedia.org/wiki/Consensus_(computer_science)),
[*log structured merge trees*](https://en.wikipedia.org/wiki/Log-structured_merge-tree),
[*clocks*](https://en.wikipedia.org/wiki/Clock_synchronization) and
[*distributed ACID transactions*](https://en.wikipedia.org/wiki/Distributed_transaction)
you will find a **lot** of useful information in order to learn more about
these topics.

{{< youtube m9xYejDUdus >}}
