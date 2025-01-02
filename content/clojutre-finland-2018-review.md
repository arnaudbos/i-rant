---
title: ClojuTRE & SmallFP 2018 — Review
date: 2018-09-25T22:56:42+01:00
description:
parent: blog
categories: ["conference"]
tags: ["conference", "review"]
draft: false
seoimage: img/clojutre-finland-2018-review/boats.jpg
gallery: true
---

{{< img src="/img/clojutre-finland-2018-review/boats.jpg" title="Helsinki" alt="Picture of Helsinki boats" width="100%">}}

-----

[ClojuTRE](https://clojutre.com) is a Clojure conference happening in Finland
since 2012.

I was super eager to attend a Clojure conference this year, so the addition of
SmallFP just before ClojuTRE finally amortized the travel cost.

# tl;dr

* SmallFP did deliver a great experience by allowing an audience of
  Clojure developers to hear about other functional programming languages and
  concepts.

* The topics were a nice mixture of industry practices, libraries and research.

* The venue was **a Tram museum**(!)

* I bought bear salami in Helsinki's Old Market Hall... **BEAR SALAMI!**

-----

# Preamble

This year I was super eager to attend a Clojure conference and was
looking forward to EuroClojure. But after the realization that it would not
hapen, I switched my focus to ClojuTRE as it was one of the most
popular one in Europe.

For the second time, ClojuTRE has been colocated with SmallFP, also a one day
conference, focusing on other functional programming languages.

The addition of SmallFP is a very good initiative, it amortizes the cost of
travelling and it also encourages cross-polinization and open-minded/honest
discussions in the functional programming community.

<!-- toc -->

# A few pictures

{{< gallery >}}
  {{% galleryimage file="/img/clojutre-finland-2018-review/cathedral.jpg"
  size="5158x3167" caption="Helsinki Cathedral" width="100%" %}}
  {{% galleryimage file="/img/clojutre-finland-2018-review/island.jpg"
  size="5184x3888" caption="Some island" width="395" %}}
  {{% galleryimage file="/img/clojutre-finland-2018-review/kings-gate.jpg"
  size="5184x3888" caption="King's Gate" width="395" %}}
  {{% galleryimage file="/img/clojutre-finland-2018-review/salmon.jpg"
  size="5184x3888" caption="Cold-smoked salmon" width="395" %}}
  {{% galleryimage file="/img/clojutre-finland-2018-review/monkeys.jpg"
  size="5184x3888" caption="Polar Monkeys" width="395" %}}
{{< /gallery >}}

# SmallFP

## OCaml, REborn: fullstack applications with ReasonML (António Monteiro)

[António Monteiro](@anmonteiro90) has been a ClojureScript developer, speaker
and contributor for a few years, and although doing Clojure all day for work,
he recently switched focus onto ReasonML on his spare time, and he explains why
in this talk.

Coming from ClojureScript, it's not a surprise that António values data-driven
applications, and ReasonML seems to suits well this kind of applications too.  
ReasonML is a modern new dialect of OCaml aimed at JavaScript developers and
provides a toolchain for fullstack applications.

It provides a statically checked type system which enables developers to share
objects between backend and frontend and catch errors at compile time.  
Unlike some statically typed programming languages, like Haskell for instance,
ReasonML is not a pure language, as the type system will not check and prevent
the developer to do side effects, among other things.  
As António puts it, the type system is still 100% sound, but more "practical".

Its type system seems to align well with GraphQL types and schemas, and
provides type-checked queries ensuring consistency between the client and server
implementation, when their code is co-located. I'm wondering what the story is
when they are not though...

At MonkeyPatch, we've recently interviewed a candidate who implemented his
coding test at home in ReasonML (& Reason React).

Having a candidate handing us this nice piece of frontend code brightened
up my day. I look forward to see the future of ReasonML.

[Abstract, slides and video available here](https://clojutre.org/2018/#anmonteiro).

## Interactive, Functional, GPU Accelerated Programming in Clojure (Dragan Djuric)

The recent surge of machine learning, deep learning, AI and whatnot has made the
ecosystem of libraries and tools to "do data science" to grow quickly.

In the quest of performance, data scientists value every processing unit
cycle and typically use low-level programming languages such as C or C++ rather
than high-level ones. But even then, the performances are dramatically better
when leveraging the power of GPU grids with special versions of these languages.

On the other hand, programmers typically counterbalance the rigidity,
low-level aspects and scaling issues of these languages by using high-level
programming languages such as R, Python or Java as wrappers.

[Dragan Djuric](https://twitter.com/draganrocks) is know in the Clojure
community for his work on libraries allowing developers to leverage the power
of GPU kernels, from the comfort of the Clojure REPL.

The numbers showed in his slides comparing runtime speed for basic "number
crunching" tasks are telling, and he presents a style of programming which
is functional and interactive as another approach to notebook based programming,
which seems to be problematic.

I have to admit I'm far (oh so far) from being an expert in this field (I've
just tried some basic introductory exercises a few times), but this presentation
really resonated with me, and a lot of the pain points outlined in this talk by
Joel Grus could, perhaps disappear with the use of a proper REPL.

I think Clojure's style for interactive development can provide a good story
for agile and iterative data science projects.

[Abstract and video available here](https://clojutre.org/2018/#dragandjuric).

## I used Elm in production and it cost me my job (Annaia Berry)

[Annaia Berry](https://twitter.com/ann_arcana) delivered an accurate, yet oh so
funny talk on how Elm has provided her and her team with a really productive
and stable environment. New feature development was so quick, and maintenance
tasks so infrequent, that it wasn't necessary for the company hiring her as
a consultant to keep her.

This is a story about how functional programming is a comptetitive advantage
but also a double edged sword for programmers, who need to live and pay the bills
in this world where productivity is not necessarily accurately measured nor
valued.

And then the talks takes an unexpected turn [to narcissistic design](https://www.youtube.com/watch?v=LEZv-kQUSi4).
Describing all the things we can still do to keep our jobs by complecting some
aspects of our work so much as to be the only ones able to maintain it.

It was a very entertaining talk, I encourage you to [watch it here](https://clojutre.org/2018/#jarcane).

## Meetings With Remarkable Trees (Bodil Stokke)

As a good clojurist, I'm pretty familiar with Bagwell Tries (aka "Hash array
mapped tries") and Hickey Tries (aka "Persistent Bit-partitioned Vector Tries"
or "Bitmapped Vector Tries") but I'm less familiar with Finger Trees.

If you don't know what those data structures are or what they provide then good
news, [Bodil Stokke](https://twitter.com/bodil) just gave a quick reminder of
their Big O properties and quickly presented the one data structure to rule them
all: RRB-Trees (aka Relaxed Radix Balanced Trees).

In this presentation, Hickey Tries are explored at length and reimplemented in
JavaScript. Sadly, Finger Trees are not studied as much and are just presented
for Big O comparison. Finally, RRB-Trees are introduced in the end to outline
their awesome properties.

In summary: review your Big O, study awesome data structures and worship Phil Bagwell.

[Abstract, slides and video available here](https://clojutre.org/2018/#bodil).

# ClojuTRE

## Documenting the Clojure/Script Ecosystem (Martin Klepsch)

If it wasn't clear already I'm a huge fan of Clojure, and as a user for more
than five years, I'm pretty familiar with the language and its ecosystem.  
But regularly, through polls or random blog posts on the Web, we hear about
beginners having a hard time getting started setting up their environment,
choose libraries, understand how to use libraries or simply grok the docstring
of some core functions.

To be honest it was my first impression too, although I remember being amazed by
reading the core functions docstrings. They **seem** difficult to grasp sometimes,
yes, but they are concise and accurate.

After this barrier to entry, the Clojure programmers tend to quickly skim through
the documentation of a library, and if the documentation is not clear enough,
jump straight into the code, see what data goes in and what goes out,
fire a REPL to start experimenting.

However from a beginner's point of view it is a bit rude, and we could all
benefit from better documentation.

Rust has [docs.rs](docs.rs), and now, Clojure has [cljdoc!](https://cljdoc.xyz/)

Cljdoc is an initiative by [Martin Klepsch](https://twitter.com/martinklepsch) to
encourage library authors to write better documentation without the hassle
of building it.

The project is young but very promising, and the roadmap includes
interesting features such as attaching code examples to vars or integrating
`clojure.spec` to navigate from function to function inside or accross library
boundaries.

There is no video available for this talk as Martin has asked the conference
organizers to defer its release for after he's back from a sailing trip, so he
can answer questions.

You can still [read the abstract here](https://clojutre.org/2018/#martinklepsch).

## Carp: A Language for the 21st Century (Veit Heller)

Veit Heller gave us a gentle introduction to the language he is a core
contributor to. Carp is designed for realtime applications: games and graphical
libraries.

It is not ready for production yet, but it will be interesting to see how it
evolves and how fast brave people will adopt it.

You can find [the abstract here](https://clojutre.org/2018/#hellerve) and the
video by following the link in the tweet above.

## Native Clojure with GraalVM (Jan Stępień)

It is impossible to not hear about GraalVM these days, and the Clojure
community seems to also be interested in it.

In this presentation [Jan Stępień](https://twitter.com/janstepien) shows interesting
numbers comparing the binary sizes, startup times and memory usages of different
packaging/runtime/targets.

He shows that thanks to GraalVM, Clojure can actually become a good candidate to
command line utilities too, giving us the power of Clojure for data manipulation
to transform incoming data and chain small utilities following the Unix
philosophy.

He goes on demonstrating a simple key/value store implementation and its
packaging as a native binary inside a Docker image resulting in a **13MB** (!)
image size.

[Abstract, slides and video available here](https://clojutre.org/2018/#janstepien).

On a side note, if you haven't read this paper by Oracle Labs people about
Truffle, I encourage you to do so now!

https://labs.oracle.com/pls/apex/f?p=94065:10:103995161328214:5354

# Conclusion

Obviously I haven't covered all of the talks in this review, I've tried to stick
with the ones that I knew I could summarize properly, but they are not necessarily
the only ones I've found interesting.  
If you are eager for more I encourage you to watch the other talks on youtube.
The organizers have done an amazing work at publishing all the videos in under
a week, and the 25 minutes long format is ideal for "later watching".

Finally, I'd like to address a giant thank you to [MonkeyPatch](http://www.monkeypatch.io/)
(my employer) for allowing the budget to attend **yet another conference**!
