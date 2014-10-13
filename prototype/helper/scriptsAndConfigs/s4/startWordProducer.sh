#!/bin/bash
./s4 deploy -s4r=/opt/s4/protopubS4WordCountProducer-1.0f.jar -c=cwc1 -appName=pwc1 -appClass=lan.s40907.protopubS4.WordCountApp -p=s4.adapter.output.stream=words
./s4 node -c=cwc1
