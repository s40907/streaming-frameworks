#!/bin/bash
./s4 newCluster -c ci1 -nbTasks=1 -flp=2001
./s4 newCluster -c ci2 -nbTasks=1 -flp=2002
./s4 newCluster -c cwc1 -nbTasks=1 -flp=3001
./s4 newCluster -c cwc2 -nbTasks=1 -flp=3002
