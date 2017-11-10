---
title: Just use Clojure already!
date: 2017-11-10T13:41:21+01:00
description: The beginning of a series of article about Clojure and Kotlin
parent: blog
categories: ["clojure"]
tags: ["clojure", "kotlin", "idris"]
draft: false
seoimage: img/use-clojure-already/lisp_cycles.png
highlight: true
---

{{< img src="/img/use-clojure-already/lisp_cycles.png" title="Lisp as a secret weapon" alt="xkcd joke" width="100%">}}

-----

I hear a lot of talking going on about [Kotlin][kotlin] around me lately.

There's no doubt that since Kotlin v1.0 has been released last year, a lot
has happened around it from the Google announcement that
Kotlin would benefit from
[first class support on Android][kotlin-first-class-android], up to the
hiring of [Jake Wharton][jakewharton] (a figure in the world of Android
developers).

I've never been very enthusiastic about Kotlin, but my colleagues at
[MonkeyPatch][monkeypatch] seem to be following the hype (muahaha...) and are
becoming more and more addicted to it.  

[Igor][igor] offered to make a live demo in order to show me what it is they
(he) like about Kotlin.

{{< tweet 893066399794556928 >}}

During his presentation I've noticed a few points that I disliked, but before
jumping to conclusion I decided to dig a bit further, reading
[Kotlin in Action][kotlin-in-action] during vacations two weeks later.

I also asked on our private Slack team:
***"What is Kotlin's value proposition?"***

Amongst other small things, the consensus begun to build: *"It's a better
Java than Java"*...  
Exactly what I feared.

{{< tweet 898808015146627072 >}}

To fulfill the promise made in this tweet, I took it as a game to code the
same code example Igor showed me in [Clojure][clojure] in order to return
the favor to my colleagues.

And I've decided to start a series of blog posts about Clojure's features and
Kotlin's features in order to express why ***I*** don't see the later being that
much of an improvement, despite its growth in popularity.

But using Clojure is not like riding on unicorns all day long either.  
Although I fail to see if some other new languages solve real problems or just
add more puzzles to solve to our daily share or incidental complexity, I will
address some pain points I've encountered too in Clojure.

-----

This post was actually in draft form since September 15th (I screwed up my
publication planning) and in the mean time I've had many occasions to express
my *"obsessive enthusiasm"* of Clojure.

And the static typing and type systems aficionados around me (who seem to be
legion, interestingly) have come to discover and appreciate [Idris][idris] and
its [dependent types](https://en.wikipedia.org/wiki/Dependent_type):

{{< tweet 912183360285544448 >}}

I remember having heard about Idris around summer 2016 in this
[podcast](https://www.functionalgeekery.com/episode-54-edwin-brady/).  
I even added the episode to my favorites: which means I wanted to get back to
it and listen again later.

It's nice to have people around ready to talk about these stuff.  
This tweet made me realize it would be a good way to also inject a bit of
Idris dependent types and interactive editing features in the mix and see how
Clojure compares on the matter (Kotlin being out of the equation here).

I hope this post will be the first of a series where [Igor][igor] and I (and
maybe [Frederic][frederic] on Idris?) might go back and forth, answering
each other. It's up to them.

In the first part I will solely talk about the *programming style* I've
used to arrive at my solution, and in the following parts I will explore
other features of the language such as *data structures*, *polymorphism*,
*interoperability*, *concurrency* and of course *static/dynamic/dependent
typing*.

[kotlin]: https://kotlinlang.org/
[kotlin-first-class-android]: https://blog.jetbrains.com/kotlin/2017/05/kotlin-on-android-now-official/
[jakewharton]: https://twitter.com/JakeWharton
[monkeypatch]: http://www.monkeypatch.io/
[igor]: https://twitter.com/ilaborie
[kotlin-in-action]: https://www.manning.com/books/kotlin-in-action
[clojure]: https://clojure.org/
[idris]: https://www.idris-lang.org/
[frederic]: https://twitter.com/fcabestre
