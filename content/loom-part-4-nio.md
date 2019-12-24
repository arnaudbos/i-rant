---
title: Loom - Part 4 - Non-blocking Asynchronous I/O
date: 2019-12-24T14:21:35+01:00
description: Part 4 on a series of articles about OpenJDK's Project Loom
parent: blog
categories: ["java"]
tags: ["java", "concurrency"]
draft: true
seoimage: img/loom/japaneseweavera.jpg
---

> Part 4 in a series of articles about Project Loom.  
> In this part we re-implement our proxy service with non-thread-blocking asynchronous java NIO.
>
> If you'd like you could head over to  
> [Part 0 - Rationale][part-0]  
> [Part 1 - It's all about Scheduling][part-1]  
> [Part 2 - Blocking code][part-2]  
> [Part 3 - Asynchronous code][part-3]  

{{< img center="true" src="/img/loom/japaneseweavera.jpg" alt="Japanese loom" width="100%" title="機織り" caption="Yanagawa Shigenobu" attr="CC0 1.0" attrlink="https://creativecommons.org/publicdomain/zero/1.0/deed.en" link="https://commons.wikimedia.org/wiki/File:Japaneseweavera.jpg">}}

-----

<!-- toc -->

So asynchronous thread blocking APIs are nice because they're blocking other threads, but the problem of memory
footprint of kernel threads and context switches still remains.

## Java NIO
## Conclusion

[part-0]: ../loom-part-0-rationale
[part-1]: ../loom-part-1-scheduling
[part-2]: ../loom-part-2-blocking
[part-3]: ../loom-part-3-async
[VisualVM]: https://visualvm.github.io/
[flame graph]: http://www.brendangregg.com/flamegraphs.html
[async-profiler]: https://github.com/jvm-profiling-tools/async-profiler