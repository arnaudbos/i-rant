#!/bin/bash

rm -rf img/
(cd ..; docker run -it --rm --volume=$(pwd):/srv --volume=$(pwd)/../hugo-sustain:/srv/themes/hugo-sustain registry.gitlab.com/pages/hugo:latest hugo -D -s srv -d public/)
rm -rf code/
git add --all
git commit -m 'Update and publish'
git push origin master
