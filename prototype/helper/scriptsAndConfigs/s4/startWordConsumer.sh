#!/bin/bash
./s4 deploy -s4r=/opt/s4/protopubS4WordCountConsumer-1.0f.jar -c=cwc2 -appName=pwc2 -appClass=lan.s40907.protopubS4.WordCountInputAdapter -p=s4.adapter.output.stream=words 
./s4 node -c=cwc2 > measureLowS4Dynamic.dat
