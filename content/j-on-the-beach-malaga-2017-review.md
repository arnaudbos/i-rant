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

After a warm welcome and a handshake, [Kyle Kingsbury](https://aphyr.com/)
started introducing [Clojure](https://clojure.org/) for a brief moment:
the syntax, data structures literals and the immutability part,
just enough to be able to follow the hands-on part of the workshop.

{{< tweet 864748353489428481 >}}

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

## How does it work?

[Jepsen](https://github.com/jepsen-io/jepsen) is designed to run as a
**cluster**: the number of nodes is parametrizable but it seems 3 to 5 nodes
are usually enough to reproduce and/or detect *catastrophic errors*
([source](https://www.usenix.org/conference/osdi14/technical-sessions/presentation/yuan)).

One of the nodes is the **control node**, which is responsible for logging
into the other nodes via `SSH` and *execute* the tests.  
The other nodes will ship the software at test and execute the operations.

As said in the github's page:

> Once the system is running, the control node spins up a set of logically single-threaded processes, each with its own client for the distributed system. A generator generates new operations for each process to perform. Processes then apply those operations to the system using their clients. The start and end of each operation is recorded in a history. While performing operations, a special nemesis process introduces faults into the system--also scheduled by the generator.

The point of having single-threaded processes is to avoid unnecessary
complexity on the testing side, in order to make the work of the *checker*,
which will analyze the *history*, more manageable.

### Operation

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

### Client

A client is an implementation of the types of function invocations you want to
perform onto the system.

Given the three *operations* above, one must implement a `clojure protocol`
(think Java interface, but better) in order to actually perform the operation,
for instance implementing a write of an integer to the database you are
willing to test, using your SDK/library of choice.

### Generator

Generators are more sophisticated beast from my little understanding. A
generator is responsible for 

### Nemesis

### Pizza + beers + chit-chat

### History

### Checker

### Model

### Q/As

How do you find a bug? how long does it take? overview of Jepsen's source code and libraries (knossos?)

## Wrap-up

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
Plus he wears capes ^^

I'm also planning to write a series of follow-up articles on topics related to
JOTB talks I've been to, so keep in touch.

Interested in distributed systems testing? FoundationDB talk/TLA+ talks/Lamport guides/Caitie McCaffrey?/etc...

# Day 2: Taks
## Eric Ladizinsky —  Evolving Quantum Computers
This is the kind of opening that really stick with me. If you don't know about Quantum Computers, DWave sys and Eric Ladizinsky, don't feel bad, I didn't know either (if you knew, good for you!). So the conference started with this man going up on stage really naturally, presenting himself as not a programmer nor computer scientist but as a physicist.
He starts talking about how, given our current tools and all computing power, we are still unable to solve the real problems we are facing a civilization: poverty, injustice, ...others! which is frankly quite depressing.
He takes a step back and analyses how, through evolution, our species (from the Homo genus) evolve step by step by first discovering, then using and then mastering new tools (think fire, agriculture, industry) and how quantum physics might be another step forward.
He draws the parallel with computer science through our limited ability to find the most advanced models in data science: we manage to find good results through the use of lots of training data and clever algorithms. But when some algorithm can be successful by throwing more computing power at them, some problem remain untractable.
Quantum computers on the other hand leverage the principles of quantum physics like STUFF1, STUFF2 and untanglement in order to evaluate literally all possible models and find the most efficient solution. [Find the valleys graphs] Eric gave us a great analogy with forgery: consider the molecules of metal forming a sword. The best sword ever can only be achieved by find the best shape, the best alignment possible of its molecules. So, of all the possible combinations, which one is the best?
Quantum physics can help us explore all the possible solutions, and find the most efficient models whereas classical computers would require centuries of computation.
Considering quantum computers like that really blew my mind. Of course realizing this kind of processors is hard and expansive, but they already exist!
DWave are making quantum computers, and each new evolution is faster than the previous one at a much higher rate than our current CPUs.
At the moment it seems like only the NASA and Google can afford to buy some of them and try them on, but we, as programmers, can already program them with our current tools (Python, C++) in cloud based environments.
I'm short on words and my knowledge of quantum physics, quantum computers and the programming model to use them is limited, but if you are interested, here are some links I've found on the subject.

## Inés Sombra — The trouble with distribution
Inés Sombra (@randommood) is an engineer at Fastly and in this session she talked about tradeoffs in building a distributed system, and OMG she speaks so fast.
If I could summarize the entire talk in a single word, it would be "tradeoffs".
Taking as an example the implementation of Faslty's Image Optimizer, Inés presented several stages of the evolution of the Image Optimizer and the pros and cons of each solution.
To give a little bit of context and grossly describe this service, Fastly's Image Optimizer is and image processing and caching solution. You give it the location of you original quality content images and a set of Varnish Configuration Language rules, and each tim one of you clients requires an image, it points to this high quality image and gives query parameters about the expected size, ratio, quality, etc, of the result it needs. Then Fastly's Image Optimizer will search for a cached version of the image result, optimize it on the fly in case of a cache miss, and eventually return the expected result after caching it. A lots of details are missing but this is the big picture.
Throughout the talk, she weights in the costs and benefits of pre-processing all the catalog then caching vs on-the-fly transforming on a per-request basis then caching, or being centralized vs being distributed, being consistent vs being available or being "fast enough", stripping metadata vs keeping metadata, etc.
All in all, I think the talk had both too much and too little content, let me explain. It is true that Inés speaks really fast, but she had to rush through the last couple of slides in order to finish on time, but on the other hand, the talk was really high level on technical details and I think that was the goal, but the tradeoffs slides with only pros and cons about the "current" version of the service, with not a lot of details (or passed to fast) about what this current version was designed made it difficult to really realize what/how choices were made.

## Kyle Kingsbury — Jepsen Talk
Distributed systems behave badly
Hard to test
Don't trust vendors documentation, they're not evil but have physical/budget limits to testing
Do tests
Hope this will lead to better software

Once again, aphyr strikes with his presentation skills and humor. To paraphrase him: "our databases are on fire", and what we do as developers is try to hide this instability to our users through nicely designed and shiny APIs. But if we do our best for our users to continue riding around on unicorns all day long, we are sitting on top of systems of which we don't really know the solidity.
Since 2014, Kyle has been using Jepsen to test databases, queues and other distributed systems on the basis of what he suspected was wrong or what the README and documentation of those softwares were stating. He found numerous "bugs" and "inconsistencies", published them in a series of blog posts and worked with some of the teams to help analyze problems and improve stability.
What he learnt was that we should all test the software we use and we depend on. We should carefully read the claims and documentations of such systems, but don't take anything for granted, because all of these systems are developed under physical and budget constraints, because formal testing is hard and simulation testing is hard.
This talk is also, in my opinion, a call to acknowledge that implementations of such distributed systems might not be perfect, and that some kinds of errors, loss or corruptions might be acceptable from a domain problem point of view, but that problem should not be hidden and assumptions must be validated.
In the end, I think Kyle is a on a journey to help teams develop better software.

## Dharma Shukla — Lessons learnt from building a globally distributed database service from the ground up
For the first few seconds I was a bit bored about the tone of the presentation. Boy I was wrong.
During this talk, I've been introduced to CosmosDB, formerly known as DocumentDB.
Honestly, I didn't even know this was a thing, and yet it is a really interesting product developed during the last 7 years by Microsoft: a schema-less, multi-model, globally distributed database in Azure Cloud.
There are articles that will better describe Cosmos DB than I could do, but what I will keep from Dharma's talk is the amount of work the teams at Microsoft seem to have invested in ensuring strong and comprehensible SLAs for latency and availability, but also for consistency and throughput, which is another level of quality.
Cosmos DB is fully schema agnostic and has its own was of structuring data so that you don't have to worry about it, and it also "indexes all the data". Cosmos DB is multi-model in the sense that it supports document, graph of key-value data types and uses a SQL-like query dialect as well as MongoDB or graph APIs.
Another important feature of Cosmos DB is it's 5 different levels of consistency models, from strong consistency to eventual consistency, Microsoft has implemented three other consistency models based on academic researches.
Cosmos DB is available as a service on Microsoft Azure Cloud. There you can choose and elastically control with API calls the amount of storage and scale the throughput of your application and pay as you go down to a granularity of a second.
All those features are really impressive, they even brought Leslie Lamport on board and used it's TLA+ formal language to test for correctness. They also use Jepsen.
I wasn't expecting to be amazed by this talk but honestly if you're not afraid of vendor lock-in or are already locked-in Microsoft Azure, just give it a try, it looks amazing.

## Lunch: Wraps+beers
The first day's lunch consisted of wraps of different flavors, chicken, ham or veggie. They were good enough, and open beer bar was present just like the day before so one just had to manage to eat and drink wisely in order to be fully ready for the talks of the afternoon.
The lunch took place in the garden just like on wednesday, on the grass under a beautiful sunny sky.
During the meal I reconnected with some of my fellow Jepsen workshop-ers from CrateDB, Dupont&Dupond and with Max and Kazakman to share our experiences about the morning talks.

## Anjana Vakil — Custom Query Languages: Why? How?
Design CQL like a UX with user/usability in mind
Powerful abstractions
Parser + AST

## Roland Kuhn — Taming distribution: formal protocols for Akka Typed
Typed actors

## Christopher Meiklejohn — Just-Right Consistency: Closing The CAP Gap
OMG WAT?
Antidote db

## Santiago Ortiz —Dynamic data visualization
Speaker has a really interesting way of looking at things and explaining how he thinks data visualization is meant to become a more democratized way at looking at data and just something reserved to data scientists as it is today.
The rest of the talk was a portfolio of data visualizations which was nice to see but not really informative unless it is your thing (talk about the Lost example). On the plus side, one of the examples Santiago gave was about a book by (???). This book as he explained, has been written in such a was that it can be read in any order to produce a completely different story. The end result is a set of X chapters that can be assembled into XX completely different books. The visualization consisted in circles that allowed the viewer to screen and read from a chapter to another.

## Dinner: Pizza+beers+other attendees+meetups I didn't go because sleep

# Day 3: Talks
## Duarte Nunes — ScyllaDB: NoSQL at Ludicrous Speed
I was interested in this talk because of its summary highlighting the work done on performance on ScyllaDB. Performance is a topic I began to be interested in recently so I decided to go to learn things.
So ScyllaDB is a distributed databased based on the same model as Cassandra: the dynamo db paper. Its caracteristics are high write availability, eventual consistency and inability to do join queries.
ScyllaDB is fully compatible with Cassandra's API so you can drop replace it without a single code line change. Where ScyllaDB delivers is on the performance aspects. According to the benchmarks, a 3 nodes ScyllaDB cluster is able to sustain the same load as a 30 nodes Cassandra cluster. This is basically Cassandra on steroids. So how did the team manage to do that?
The answer is lors of time and efforts applying good algorithms and mechanical sympathy principles in order to squizze every bit of power from CPUs and memory.
ScyllaDB uses a thread-per-core model to avoid ...? Talk about the controller thing.
On the memory side, ScyllaDB is very cautious about its memory space allocation and manages to compact memory to avoid fragmentation and achieve better data locality. I think this is similar to how MongoDB compacts free space on disk, but ScyllaDB does it on live memory.
Data: live is memory, then persist sstables on the background
ScyllaDB also features an interesting and tunnable self-monitoring tool able to analyse the stress under which the database is going and make decisions on which process to prioritize and how to deal with background tasks.
ScyllaDB is open source and supported by a community (and company?)
If you have a use case for Cassandra and are critical about your resources usage, I think it's really worth taking a look at ScyllaDB.

## Danielle Ashley — Uniting Church and State: FP and OO Together
ZZZzzzZZZzzz but church encoding is interesting
Problem: scala and its fucking type system.
OMG people if you want this kind of semantics just use Haskell already and save from scala's awful syntax.
I don't think Church and State should ever be reunited, but pun aside, this presentation made me realized that although I was a lisp convert, I knew nothing about lambda calculus.
Before the conference, when preparing my schedule, I selected this talk because it talked about "FP and OO Together" and I really like the idea that a lot of OO problems could be solved by introducing more FP concepts and good practice into "our day to day OO programming". So I just ignored completely ignored the first part of the talk and didn't understand the "Church" pun until later, so yeah, I'm stupid.
The presentation in itself I really disliked. The speaker wasn't entertaining at all and really just seemed exhausted, jetlagged maybe? Also all the examples are in Scala... I really try to tell myself that I shouldn't be disgusted by a language just because I'm not used to its syntax, but this Scala think really makes me "frissonner".
So not a geat feeling. But after the conference I did my homeworks and looked at this church encoding think, and through a few number of articles, I realized I really miss something in my lambda calculus education, and this church encoding is really interesting as an exercise.
Honestly, I don't see where it'd be useful unless you're trying to implement a new language, but I'm really new to it, nonetheless it's something I'm happy to have looked at.

## Francesco Vivoli — From concept to adoption - the maze of organizational readiness for Big Data solutions
Well this was an overview of the decision process a CIO/CTO (?) should pass in order to decide what technologies to use or not in an organisation. Key insight: choosing not to use a certain technology is as important as choosing one.

## Justo Ruiz Ferrer — Adressing the elephant in the room: what a post-Hadoop era looks like
Bullshit
The title lead to thinking about a talk giving insight about where the big data processing techniques could go, but in the end it didn't deliver. The talk was mainly just an explaination about the "my big data is bigger than your big data" saying and meaningless/short demo of Valo's streaming platform. Anyways the speaker (and CTO of the company hosting the event, so I probably shouldn't say what I'm currently saying) is clearly comfortable talking to an audience and fills the room with his speaking, but in the end I expected some insights or take away thoughts about the direction in which our industry is going, and I ended up with sort of a statement about our architectures moving from batch processing to streaming processing, with the lambda architecture (he didn't mention it) and micro-batch processing architectures and techniques in between. Well, this was nothing new.

## Lunch: Paella!!!+beers+other attendees

## Caitie McCaffrey — Distributed Sagas: A Protocol for Coordinating Microservices
Great! Super interesting.
No SPOF: SEC (controller)
Write to log
Idempotency
Replay when in doubt
Then pop from stack the backward idempotent actions

## Martin Thompson — High Performance Managed Languages
In a word: What the GC gives you.
Possible to have highly optimized non-GCed code but at the expense of efforts and time
Allocation is not the problem, the problem is reclaiming memory
"Pledge" for Better algorithms

## Clemens Szyperski — Declare Victory with Big Data
Boring...

## Dinner + Party: Camperos+beers+Flamenco+The Coovers+Los Vinagres+other attendees and Anjana Vakil


# Conclusion
Shut up and take my money!
