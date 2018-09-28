!#/bin/bash

git add .
git commit
git reset eclipse/*metadata/*
git reset eclipse/*reco*/*
git push
git pull

