#!/bin/bash

(cd ..; hugo --buildDrafts)
git add --all
git commit -m 'Update and publish'
git push -f origin master
