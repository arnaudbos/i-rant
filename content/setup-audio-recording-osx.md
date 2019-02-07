---
title: Setup audio recording on OSX
date: 2019-02-07T02:59:53+01:00
description: Note to self — setup audio recording for the Toulouse JUG.
parent: blog
categories: ["jug"]
tags: ["jug", "osx", "capture"]
draft: false
seoimage: /img/setup-audio-recording-osx/garageband.png
---

{{< gallery title="The goal: something like that" >}}
  {{% galleryimage file="/img/setup-audio-recording-osx/garageband.png"
  size="3360×2054" width="100%" height="100%" %}}
{{< /gallery >}}

-----

* **Created**: February 7th, 2019
* **Object**: As I was preparing the next evening of the [ToulouseJUG] I found myself
  lacking a second mic for our next duet. This post is a note to myself about the setup
  for recording audio of one or two speakers with the pieces of equipment I have at my
  disposal, with pictures, so I can come back later and relearn how to do this.

-----


# Table of contents

<div id="toc" class="well col-md-12">
  <!-- toc -->
</div>

## One speaker

### Hardware setup

For one speaker we will use the Toulouse JUG's own audio recording hardware which is
composed of a Sennheiser FM transmitter and receiver.

The FM receiver is plugged to my MacBook Pro via an XLR `->` 3.5mm jack.

But the MacBook Pro does not recognize the microphone as is by just plugging the XLR
cable in.

{{< img src="/img/setup-audio-recording-osx/sound-builtin-only.png" title="OSX does not recognize the microphone" alt="Screen capture of OSX sound settings" width="100%">}}

I've followed [this video](https://www.youtube.com/watch?v=9k1MfRs8DlI) and bought a
"TRRS Cable" to plug the XLR cable:

{{< img src="/img/setup-audio-recording-osx/receiver-jug.jpg" title="The Sennheiser FM receiver" alt="Picture of the Sennheiser FM receiver" width="100%">}}

> Note: the headphones are there to monitor the audio input while recording

And then it worked:

{{< img src="/img/setup-audio-recording-osx/sound-with-external.png" title="OSX now shows the external microphone" alt="Screen capture of OSX sound settings" width="100%">}}

> Note: Ignore the "Two Mics" thing for the moment.

### Recording from Final Cut Pro X

We can record the audio track of the talk directly from Final Cut Pro X (the software
I use for post-processing and final video export).

Simply start a new "Voiceover recording":

{{< gallery >}}
  {{% galleryimage file="/img/setup-audio-recording-osx/fcpx-record-voiceover.png"
  size="612×868" width="40%" height="40%" %}}
{{< /gallery >}}

Select the external microphone as the `Input` and heck the "Monitor `On`" feature if you
want to monitor the incoming audio feed.

## Two speakers

There are ~~99~~ two problems when doubling the number of speakers:

1. We need a second microphone
2. Final Cut Pro X doesn't support multiple voiceover recordings simultaneously

### Hardware setup

So we need another microphone, but the Toulouse JUG only owns one Sennheiser set
(headset mic + transmitter + receiver)...

Fortunately, a member of the JUG is able to lend us more audio and video recording
hardware. One such piece of equipment is another audio recording set (lavalier mic +
transmitter + receiver + some kind of decoder/amplifier, IDK) which is plugged to the
receiving computer via a USB-A cable (in this case, my MacBook Pro via a USB-C hub).

{{< img src="/img/setup-audio-recording-osx/receiver-eclipse.jpg" title="The second audio recording set with USB-C adapter" alt="Picture of the second audio recording set with cables and USB-C adapter" width="100%">}}

First, repeat the process above to setup the first microphone, and read on to record
both simultaneously.

### Audio settings

When plugged to the MacBook Pro, the new microphone is automatically detected:

{{< img src="/img/setup-audio-recording-osx/sound-with-usb.png" title="OSX shows the USB microphone" alt="Screen capture of OSX sound settings" width="100%">}}

Great but FCPX still doesn't support multiple simultaneous recordings.  
... Well, remember the "Two Mics" thingy? Now's the time to talk about it.

To bypass this limitation of FCPX I use GarageBand. But in order to use it, as I've
found in [this video](https://www.youtube.com/watch?v=SeBAyanpeBU), we must first create
a new "Aggregate Device".

Open Spotlight and type "midi":

{{< img src="/img/setup-audio-recording-osx/open-midi.png" title="Command + Space then search for \"Audio MIDI Setup\"" alt="Screen capture of Spotlight" width="100%">}}

Click on the `+` sign in the bottom-left corner:

{{< gallery >}}
  {{% galleryimage file="/img/setup-audio-recording-osx/two-mics.png"
  size="1838×1026" width="100%" height="100%" %}}
{{< /gallery >}}

And select "Create Aggregate Device":

{{< img src="/img/setup-audio-recording-osx/create-aggregate-device.png" title="Create Aggregate Device" alt="Screen capture of the \"Create Aggregate Device\" popup" width="30%">}}

Then make sure both the "external microphone" (the one we plugged in when we had only
one speaker) and "USB Audio Codec" (the one we plugged in via USB just above) have their
inputs selected.

I have also selected the external headphones (the ones plugged at the other end of the
TRRS cable) and the built-in speakers for good measure. I honestly don't know if it's
useful or not, but at least it does no harm.

Then give this "Aggregate Device" a name, such as "Two Mics" for instance, and you
should have the same config as we saw above:

{{< gallery >}}
  {{% galleryimage file="/img/setup-audio-recording-osx/two-mics.png"
  size="1838×1026" width="100%" height="100%" %}}
{{< /gallery >}}

Now go to GarageBand.

### Recording from GarageBand

From GarageBand you will have to open the settings and switch the default devices
to your newly created "Two Mics":

{{< img src="/img/setup-audio-recording-osx/garageband-settings.png" title="Set GarageBand devices to \"Two Mics\"" alt="Screen capture of Audio MIDI Setup window showing the new Two Mics device" width="100%">}}

Now add a new track that will record the first microphone, select "Input 1":

{{< gallery >}}
  {{% galleryimage file="/img/setup-audio-recording-osx/input-1-two-mics.png"
  size="3360×2054" width="100%" height="100%" %}}
{{< /gallery >}}

And then add a new track that will record the second microphone, select "Input 3:

{{< gallery >}}
  {{% galleryimage file="/img/setup-audio-recording-osx/input-3-two-mics.png"
  size="3360×2054" width="100%" height="100%" %}}
{{< /gallery >}}

Why "Input 1" and "Input 3"? I have no idea, but it works {{< emoji content=":thumbsup:" >}}

Give your tracks names, it's more pleasant.  
And finally, with your two tracks in place, go to the  `Track`(s?) top menu and select
"Configure Track Header..." to display the little record control to each of your tracks:

{{< img src="/img/setup-audio-recording-osx/configure-track-header.png" title="Select \"Configure Track Header...\"" alt="Screen capture of the \"Configure Track Header...\" popup" width="40%">}}

Selecting the menu should show this popup where you can select the "Record Enable"
checkbox:

{{< gallery >}}
  {{% galleryimage file="/img/setup-audio-recording-osx/garageband.png"
  size="3360×2054" width="100%" height="100%" %}}
{{< /gallery >}}

If you want your recording to get both input simultaneously, Shift + Select both tracks'
little red record buttons.

And you're good to go!

## Last thought

1. Don't forget to bring a power strip!
2. Don't forget to bring spare batteries for the microphones transmitters!

The Duracell Powercheck are actually pretty good for that matter:

{{< img src="/img/setup-audio-recording-osx/batteries.jpg" title="Duracell Powercheck" alt="Picture of a Duracell Powercheck" width="40%">}}

As their name suggest they have this little "Power check" which kinda looks like a
colored bar chart that lights up when you touch the right spot, and tells you how much
is left before it's empty.

{{< img src="/img/setup-audio-recording-osx/powercheck.jpg" title="Powercheck demo" alt="Picture of the Powercheck feature" width="30%">}}

Until we change our equipment to something with a Lithium-ion batteries... ?

That's it!


[ToulouseJUG]: http://www.toulousejug.org/
