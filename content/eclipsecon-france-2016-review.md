---
title: EclipseCon France 2016 — Review
date: 2016-07-13T12:55:28+01:00
description: This ear, I've been given the opportunity to attend the French edition of the EclipseCon. It's been the first time I could attend an event of this nature with Keynotes, workshops, talks and demos on various topics, informal talks with other attendees and speakers.
parent: blog
categories: ["conference"]
tags: ["conference", "review"]
draft: false
seoimage: /img/eclipsecon-france-2016-review/eclipsecon.jpg
---

{{< img src="/img/eclipsecon-france-2016-review/eclipsecon.jpg" title="Keynote" alt="Picture taken during the Keynote" width="100%">}}

-----

This ear, I've been given the opportunity to attend the French edition of the EclipseCon. It's been the first time I could attend an event of this nature with Keynotes, workshops, talks and demos on various topics, informal talks with other attendees and speakers.

Bellow is a feedback on **my** experience of the EclipseCon France 2016.

Schedule: https://www.eclipsecon.org/france2016/conference/schedule/session/2016-06-07

-----

## Connecting low power IoT devices with LoRa, MQTT, and The Things Network

{{< img src="/img/eclipsecon-france-2016-review/iot.png" alt="Eclipse IoT logo" width="280px" center="true">}}

<br />
From my point of view, [IoT][iot] really was an important topic this year in Toulouse, most notably because of the presence of [The Things Network][ttn], who's been invited by the Eclipse Foundation to lead a workshop and give the first Keynote of the conference.

This team, from Amsterdam, wants to federate people and communities anywhere in the world around their worldwide network dedicated to connected devices, based on the LoRa technology.

So, what is LoRa? And what is The Things Network?

-----

### LoRa

{{< img src="/img/eclipsecon-france-2016-review/lora.png" alt="LoRaWan logo" width="280px" center="true">}}

Taken from the [LoRa Alliance][lora-alliance] :

    LoRa is a diminutive for LoRaWAN™: Low Power Wide Area Network (LPWAM).

Without going into too much details (that I don't master anyways), here's what I can say:

* *LoRa is a free and open wireless specification based on ISM radios bands (https://en.wikipedia.org/wiki/ISM_band).*

This technology particularly fits in as a network layer for communication of small, connected devices because it allows for localization and mobility of devices, low consumption, does not need a big or existing previous installation, and communication can go both ways (bi-directional).

With a single antenna on top of a building in an urban area, or in fields, LoRa allows the connection of thousands of devices without loss, far more than a classic wireless gateway (WiFi, Bluetooth) and energy consumption is kept low and is less costly that a 3G gateway.

A few examples:

* Power networks: predict consumption and adapt to real needs
* Logistics: accurate delivery and moving targets
* Transportation: emergency calls
* Health: embedded measuring devices
* And a lot more... all that wirelessly.

LoRa competes with the [Sigfox][sigfox] technology, that you're obviously aware of if you're working/living in Toulouse like I do. Nonetheless, the two have different approaches, as Sigfox's technology is proprietary and implies license costs, whereas the LoRa specification is free and open.

Some measures made by The Things Network, as well as a few tech characteristics:

* Dense urban environment: 500m to 3km
* Rural environment: 10-50km (up to 92km over lands)
* Up to 10.000 devices per gateway
* Up to 3 years batteries
* Very low consumption (and no "handshake")
* Free, open license, in both emit and receive
* No previous installation required
* Multi-coverage (multiple gateways can relay the information)

Devices connecting to a LoRa network can be sorted into three categories:

    A) Uplink only, device initiates the communication and server can answer;
    B) Device and network are in sync on a shooting window the data exchange;
    C) Device constantly listening for updates.

Of course consumption depends on device category.

-----

### The Things Network

{{< img src="/img/eclipsecon-france-2016-review/ttn.png" alt="The Things Network logo" width="280px" center="true">}}

[The Things Network][ttn] is a project born in Amsterdam, the goal is to build a worldwide, open distributed network for IoT devices.

Following a crowdfunding campaign, the team has started to create a Web platform in order to allow connection of devices via brokers.

All the source code of The Things Network is [open source and available on Github][ttn-github], according to their commitment to allow a vast adoption of these technologies.

In parallel, their business entity sells [Starter kits][ttn-starter] for education purposes as well as gateways, and participates in workshops and trainings to allow people to equip their homes/neighborhoods/towns and initiate a global coverage movement.

There already are communities around the world, mostly in Europe at this time. Those communities were sometimes initiated by the team members of The Things Network, who travel a lot to advocate for their project and the LoRa technology, and sometimes communities are spontaneously created by local people.

-----

## What every Java developer should know about AngularJS

{{< img src="/img/eclipsecon-france-2016-review/angular.png" alt="AngularJS logo" width="280px" center="true">}}

Everything's in the title.

This workshop was intended to developers who are more familiar with backend technologies and wanted to have an introduction at the most famous front-end framework of the moment: [AngularJS][angular].

The was articulated about an introduction to controllers, scopes, services and directives, based on a tiny project example.

As a full-stack developer I think this workshop was well adapted to its audience, with an iterative process in order to introduce new concepts in turn on the tiny project.

The speakers chose to have [TypeScript][typescript] at the basis of their example, in order to keep their audience, more used to classical object architectures than ECMAScript, in their comfort zone. My co-attendees had the occasion to have their feet wet in a project architecture modeled around interfaces and implementations, with generic types and inheritance. On the other hand, they've had to deal with the poor front-end development tooling of the Eclipse IDE.

Let's talk about tooling.

-----

## Tooling

This year we (the attendees) had a lot of choice regarding sessions about the Eclipse tooling. Here are is a feedback on the tools I've been presented.

### JSDT 2.0

This talk was dedicated to the new version of the JavaScript Development Tools (JSDT), currently under development.

The objectives of JSDT 2.0 are to support the tools and methods of the current state of the art of modern JavaScript development.

At the moment, JSDT 2.0 benefits from a new parser, more powerful and robust than the previous one, which is able to handle the ECMAScript 6 specification.

The other objectives are centered around the integration of packets managers (npm / bower), task builders (grunt, gulp), support of Node.js, and additional tools for debugging and browsers integrations (Chrome).

### The State of Docker and Vagrant Tooling in Eclipse

It's been a few month I got myself interested in [Vagrant][vagrant] and [Docker][docker] and started to mess with them, especially for development and integration environments. The idea to setup and share with teams/contributors an immutable infrastructure and repeatable deployment processes is very exciting.

In this talk, I've been showcased two Eclipse plugins, one for [Docker][docker] integration and the other one for [Vagrant][vagrant] integration.

At the moment, the two plugins provide new "perspectives" to the Eclipse IDE, allowing to do everything (I mean, almost everything) you can do on the command line:

* Create and manage Vagrant boxes
* Configure your Vagrantfile
* Create and manage virtual machines
* Create and manage Docker images
* List and manage Docker containers
* Edit Dockerfile

I'm a little bit disappointed, even though I personally don't have an affinity for IDE integrations of command lines tools (I like my git separated from my IDE for instance).

Anyway it's worth mentioning that all the developments made on these plugins are the work of developers who are doing it for free, there are not a lot of them, and like everybody else they have to mow their lawn and fix their home on weekends.
So thanks guys, and keep up the good work.

## Continuous Delivery: Pipeline As Code With Jenkins

{{< img src="/img/eclipsecon-france-2016-review/jenkins.png" alt="Jenkins CI logo" width="280px" center="true">}}

I was very enthusiast to attend this talk. I'm very interested in the opportunity to manage build jobs in a pipeline-shaped way. I'm also interested in "Continuous Delivery" and in "Continuous Deployment" for that matter.

So what was this talk about ? Mainly what I could describe as the ability of orchestration, interruption and resilience of build jobs. Nothing less...

Like said in [the slides](http://batmat.github.io/presentations/jenkins-pipeline-as-code/prez.html), what happens when you have fairly complicated build jobs, requiring operator inputs and possibly the ability to run in parallel?

Apart from creating multiple individual jobs, that you can link or chain later on, leave alone fail-fast and parallelism, there is no idiomatic way to do.

This is the kind of problems "Jenkins Pipeline Plugins", which is in reality, a set of plugins, is try to solve. At the core of it is a DSL, the "Pipeline DSL", allowing to chain builds, as steps, and attach to each a set of configuration options, like parallelism for instance.

It becomes possible, for example, to configure a few dozens look-alike (small variation) jobs shaping the basis (the dependencies of a cascading build job) and trigger the execution of all these builds in parallel, before the execution of the next build job which depends upon them. All of this while specifying that the **complete** build sequence should stop in case of a failure of any of the base builds (fail-fast).

For the record, the speaker showcased this example exactly, on a [Docker Swarm][docker-swarm] build cluster provided by a cloud provider:

* 336 CPUs
* 1.032 TiB RAM

Don't we all have one like that in our basement?

Anyways, I was really curious about the choice of a DSL, instead of a fully declarative description of the build pipeline thanks to configuration file(s).
It's easy to envision how to describe via simple data structures like maps and collections, the orchestration of jobs and the description of each step.

I did not get a clear answer except that: most of the contributors being Java developers, a DSL (which really looks like Java by the way) seemed a natural choice.

## Conclusion

I've fully enjoyed my first time at a conference. The organization was perfect and the quality of the speakers was very satisfying.

I will be please to come over next year at the EclipseCon France 2017, and I recommend to any developer having the opportunity to attend it, to do so without hesitation.

All of the Keynotes and talks have been video recorded and are available on the Eclipse Foundation's [Youtube channel](https://www.youtube.com/playlist?list=PLy7t4z5SYNaRJff0KBMbubOaj8gevvML4).

[sigfox]: https://sigfox.com/
[iot]: http://iot.eclipse.org/
[ttn]: https://www.thethingsnetwork.org/
[ttn-github]: https://github.com/TheThingsNetwork/
[ttn-starter]: https://shop.thethingsnetwork.com/
[lora-alliance]: https://www.lora-alliance.org/
[angular]: https://angularjs.org/
[vagrant]: https://www.vagrantup.com/
[docker]: https://www.docker.com/
[typescript]: https://www.typescriptlang.org/
[docker-swarm]: https://www.docker.com/products/docker-swarm/
