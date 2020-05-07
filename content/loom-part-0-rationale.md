---
title: Loom - Part 0 - Rationale
date: 2019-12-14T15:38:21+01:00
description: Part 0 on a series of articles about OpenJDK's Project Loom
parent: blog
categories: ["java"]
tags: ["java", "concurrency"]
draft: false
seoimage: img/loom/jacquard_loom.jpg
---

> Part 0 in a series of articles about Project Loom.  
> In this part I just talk about me, me, me, me and why I wrote this.
>
> The companion code repository is at [arnaudbos/untangled](https://github.com/arnaudbos/untangled)
>
> If you'd like you could head over to  
> [Part 0 - Rationale][part-0] (this page)  
> [Part 1 - It's all about Scheduling][part-1]  
> [Part 2 - Blocking code][part-2]  
> [Part 3 - Asynchronous code][part-3]  
> [Part 4 - Non-thread-blocking async I/O][part-4]  

Finding good introduction level articles on concurrent programming is difficult because every post assumes
some prior knowledge. You can quickly find yourself into dark corners of the Web where people talk about lock-free
concurrency while you're _absolutely not_ ready for this!

Luckily, a few months ago, we've had an interesting use-case at work. We had to implement a scheduler for a scarce
resource in our system.

We've been using a good deal of concurrency "techniques" for the past two years: simple `threads`,
`executor services`, `reactive` (Rx) programming; we were doing fine.

However, for this service, I wasn't convinced by any of those techniques. The problem lent itself well into an actor
model, so we searched for a library to help us and found [Quasar].  
My first introduction to [Quasar] was around 2014. At the time Clojure's [core.async] library just came out.

I didn't understand every aspect of the value proposition, but naively started using it.  
And because I was using `core.async`, I stumbled upon [Pulsar] (not Apache Pulsar!) which is itself built upon Quasar.  
I missed the value proposition of Pulsar, like I did with Quasar, but I was interested
in this concept of "lightweight concurrency" and the "actor model".  
I longed for the day I would have a use case for this.

Fast forward five years and more experience in the field, Quasar and its actor model were
a great fit to solve our problem and we used it to great effect. That's when I fell into the rabbit hole: trying to
understand the underlying concepts and "how it works", I stumbling upon Project Loom.

Unfortunately, the [OpenJDK wiki][wiki] and the many videos by [Ron Pressler] take for granted a great deal of
knowledge and start from there. That's when I decided to gain this fundamental knowledge in order to grok
Project Loom, and share with others so they, too, can connect the dots. I ended up giving a couple of [conference talks][talks]
on this topic in 2019, for which I've had to research a lot.

This series is about what I've learnt and what I would have liked to read when I began my research on concurrent
programming, Reactive Streams, lightweight concurrency and Project Loom: I'm writing for the past me.

For people already familiar, I'm not promising you'll learn anything new, but hopefully you'll be interested in
another point of view.

In the [next part][part-1], we talk history!

[part-0]: ../loom-part-0-rationale
[part-1]: ../loom-part-1-scheduling
[part-2]: ../loom-part-2-blocking
[part-3]: ../loom-part-3-async
[part-4]: ../loom-part-4-nio
[Quasar]: https://docs.paralleluniverse.co/quasar/
[Clojure]: https://clojure.org/
[core.async]: https://clojure.org/news/2013/06/28/clojure-clore-async-channels
[Pulsar]: https://docs.paralleluniverse.co/pulsar/
[puniverse]: http://www.paralleluniverse.co/
[talks]: https://talks.arnaudbos.com/
[Ron Pressler]: https://twitter.com/pressron
[wiki]: https://wiki.openjdk.java.net/display/loom/Main