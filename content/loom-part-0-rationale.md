---
title: Loom - Part 0 - Writing for the past me
date: 2019-12-10T14:27:43+01:00
description: Part 0 on a series of articles about OpenJDK's Project Loom
parent: blog
categories: ["java"]
tags: ["java", "concurrency"]
draft: true
seoimage: img/loom/jacquard_loom.jpg
---

> Part 0 in a series of articles about Project Loom.  
> In this part I just talk about me, me, me, me and why I wrote this.
>
> If you'd like you could head over to  
> [Part 1 - It's all about Scheduling][part-1]

Finding good introduction level articles on parallel or concurrent programming is not easy because every post assumes
some prior knowledge. You can quickly find yourself into dark corners of the Web where people talk about lock-free
concurrency while you're _absolutely not_ ready for this!

Luckily, a few months ago, we've had an interesting use-case at work, where we had to implement a scheduler for a scarce
resource in our system.

We've been using many concurrency "techniques" for the past two years, from simple `threads` and `executor services`
to `reactive` (Rx) programming. And we were doing fine.

But when the time came to implement this specific piece of algorithm, I'm not ashamed to say that I didn't feel the
nerves to implement it using any of those techniques. And because the problem lended itself well into an actor model,
we looked around to see which library would help us in this endeavor. This was when we took a look at [Quasar].

My first introduction to [Quasar] was a few years ago, around 2014. At the time I was writing [Clojure] professionally
and Clojure's [core.async] library just came out.  
I didn't understand every aspect of the value proposition at first, but naively started using it.
And because I was using `core.async`, I stumbled upon [Pulsar] (not Apache Pulsar!) which is itself built upon Quasar.  
Yet again, I didn't understand the value proposition of Pulsar more than Quasar at that time, but I was interested
in this "lightweight concurrency" and "actor model" along with all the [Parallel Universe][puniverse] stack, and longed
for the day I would have a use case for this.

Fast forward a couple of years and with more experience in the field, Quasar and its actor model in particular seemed
to be a great fit to solve our problem so we used it to great effect. And that's when I fell into this rabbit hole,
trying to understand the underlying concepts and "how it works".

I gave a couple of presentations [at conferences][talks] on this topic this year, so I've had to research a lot.  
This is by no mean a guide on all concurrent things as I'm still struggling a lot to grok lock-free concurrency and
low-level/advanced techniques such as `Unsafe`, `VarHandle` or `Conditions` for instance.  
This series is about what I've learnt and what I would have liked to read when I begun my research on concurrent
programming, Reactive Streams, lightweight concurrency and Project Loom specifically.

I'm basically writing for the past me.

For people already familiar, I'm not promising you'll learn anything new, but hopefully you'll be interested in
another point of view.

In the [next part][part-1], we talk history!

[part-1]: ../loom-part-1-scheduling
[Quasar]: https://docs.paralleluniverse.co/quasar/
[Clojure]: https://clojure.org/
[core.async]: https://clojure.org/news/2013/06/28/clojure-clore-async-channels
[Pulsar]: https://docs.paralleluniverse.co/pulsar/
[puniverse]: http://www.paralleluniverse.co/
[talks]: http://talks.arnaudbos.com/