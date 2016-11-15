---
title: MR. P — Project outline
date: 2016-06-05T00:22:34+01:00
categories: ["iot"]
tags: ["iot", "nabaztag"]
draft: false
---

{{< img src="/img/mr-p-outline/nabaztags.png" title="Nabaztag/tags" alt="Photo of several Nabaztag rabbits" width="100%">}}

-----

* **Updated**: June 15th, 2016
* **Created**: June 5th, 2016
* **Object**: Kick-off of "Mr. P" project for [Nabaztag/tag][nabaztag] rabbit resurection with a [Raspberry Pi][raspberrypi].
* **Naming**
	* Nabaztag means "rabbit" ;
	* There's a rabbit whose name is "Mr. Rabbit" in [Fantastic Mr. Fox] film, which I love ;
	* There's also a "Mr. Rabbit" in the [Utopia] TV series, which I'm an absolute fan of ;
	* I'll use a [Raspberry Pi][raspberrypi].

-----

## Nabaztag

{{< img src="/img/mr-p-outline/Nabaztag.jpg" title="Nabaztag/tag" alt="Photo of a dismantled Nabaztag/tag" width="100%">}}

Here's a really great introduction of what's a Nabaztag(/tag), found at [RabbitPi]:

    The original Nabaztag "first smart rabbit" was released in 2005, billed
    as an ambient home assistant (sound familiar Amazon & Google?) - arguably
    it was the first "Internet of Things" thing and was in many ways way
    ahead of its time, I bought one straightway. It sat on our mantelpiece
    reading out daily weather forecasts and occasional notifications but
    never had a lot of capability, relying on a WEP wi-fi connection and
    proprietary software and servers to provide its text-to-speech (TTS)
    services. It's hard to imagine now but at the time there wasn't that much
    it could connect to, social media was barely a thing, Nokia ruled the
    smartphone world and LED lightbulbs were an expensive novelty.

    In coming years there followed two further versions, the Nabaztag:Tag and
    the Karotz, both offered improved functionality but neither found its
    niche in the marketplace, ultimately let down by hardware and software
    limitations. The shame was that as soon as the supporting servers were
    switched off the previously smart rabbits became little more than
    ornaments. Several community projects tried to replace the services of
    the "official" servers, and we did use "OpenKarotz" for a while, but this
    too seemed to die off a year or two ago, leaving my rabbits silent and
    immobile atop my speakers.

    Anyway history lesson over! The upshot is that we fondly remember the
    presence of the Nabaztag in our living room, and I wanted it back, but as
    a proper modern IoT device.

Mine is a [Nabaztag/tag][nabaztag-tag], the second version whose most noteworthy additions are a microphone and an RFID reader.

Without going into too much details, here are the capabilities and internals of this rabbit by itself:

* Top button ;
* Moving ear motor and ear position sensor (x2 ears) ;
* Microphone ;
* Sound speaker ;
* 3.5 jack female output ;
* Volume wheel ;
* LEDs (x5) ;
* RFID sensor ;
* WiFi card ;
* Power adapter.

## Raspberry Pi

Mine is a [Model B Generation 1 revision 1.2][model-b]:

{{< img src="/img/mr-p-outline/Pi-model-B.png" title="Nabaztag/tag" alt="Schema of a Raspberry Pi board">}}

I am planning to go for a [Raspberry Pi Zero][pi-zero] to reduce the real estate footprint inside the rabbit case eventually:

{{< img src="/img/mr-p-outline/Pi-zero.png" title="Nabaztag/tag" alt="Design of a Raspberry Pi Zero">}}

But in the mean time, a full sized Model B will be alright, especially for learning purposes.

The Pi runs [Raspbian Jessie][raspbian-jessie] version 4.4 (2016-05-27), downloaded from [here][raspbian-dl].

## IoT skills

At this very moment, I know almost nothing about IoT, electronics or robotics, and the only goal of this project is to learn and eventually play with [Amazon Alexa] service.

I also want to make something useful with my Nabaztag, which has became nothing more than a paperweight since almost ten years: I used it a little at the beginning but the use was very limited, and then the service was discontinued.

My girlfriend at that time (now my wife), who offered it to me, often jokes about how I'm "not using this thing she bought me". So now is time to prove her wrong and make something not useless out of it, not just some geeky gadget.

So I am planning to use this project as a long running learning and hacking project for home automation.

## Links

* [Rabbity-Pi]
* [Hack the Nabaztag]
* [RabbitPi]
* [Nabaztagtag (Nabaztag v2) Dissection]
* http://louterrailloune.com/category/nabaztag/

[nabaztag]: https://en.wikipedia.org/wiki/Nabaztag
[nabaztag-tag]: https://en.wikipedia.org/wiki/Nabaztag#Nabaztag.2Ftag
[raspberrypi]: https://www.raspberrypi.org/
[Fantastic Mr. Fox]: https://en.wikipedia.org/wiki/Fantastic_Mr._Fox_(film)
[Utopia]: https://en.wikipedia.org/wiki/Utopia_(UK_TV_series)
[Rabbity-Pi]: https://github.com/Oripy/Rabbity-Pi
[Nabaztagtag (Nabaztag v2) Dissection]: http://petertyser.com/nabaztag-nabaztagtag-dissection/
[Hack the Nabaztag]: http://www.instructables.com/id/Hack-the-Nabaztag/?ALLSTEPS
[RabbitPi]: http://www.instructables.com/id/RabbitPi-the-Alexa-Enabled-IFTTT-Connected-Ear-Wig/?ALLSTEPS
[model-b]: https://en.wikipedia.org/wiki/Raspberry_Pi#Specifications
[pi-zero]: https://www.raspberrypi.org/products/pi-zero/
[raspbian-jessie]: https://en.wikipedia.org/wiki/Raspbian
[raspbian-dl]: https://www.raspberrypi.org/downloads/raspbian/
[Amazon Alexa]: https://developer.amazon.com/alexa
