#!/bin/bash

rm -rf img/
(cd ..; docker run -it --rm --workdir /srv --volume=$(pwd):/srv --volume=$(pwd)/../hugo-sustain:/srv/themes/hugo-sustain registry.gitlab.com/pages/hugo@sha256:017b3e0d3620cf4a7d782656237b7aa8793909339d927c15dcd5bdd26180a849 hugo -D -s /srv -d public/)
rm -rf code/
git add --all
git commit -m 'Update and publish'
git push origin master
