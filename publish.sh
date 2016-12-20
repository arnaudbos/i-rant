#!/bin/bash

(cd ..; hugo --buildDrafts)
rm -rf code/
git add --all
git commit -m 'Update and publish'
git push origin master
