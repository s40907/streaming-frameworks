#!/bin/bash
./s4 deploy -s4r=/opt/s4/protopubS4IntegerProducer-1.0f.jar -c=ci1 -appName=pi1 -appClass=lan.s40907.protopubS4.IntegerApp -p=s4.adapter.output.stream=words
./s4 node -c=ci1
