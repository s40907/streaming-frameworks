#!/bin/bash
./s4 deploy -s4r=/opt/s4/protopubS4IntegerConsumer-1.0f.jar -c=ci2 -appName=pi2 -appClass=lan.s40907.protopubS4.IntegerInputAdapter -p=s4.adapter.output.stream=words
./s4 node -c=ci2 >measureLowS4Static.dat
