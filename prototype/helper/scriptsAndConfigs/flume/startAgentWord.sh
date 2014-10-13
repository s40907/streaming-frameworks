#!/bin/bash
bin/flume-ng agent -Xmx512m -Xms512m --conf conf --conf-file conf/flume-conf-WordCount.properties --name agent -Dlog4j.configuration=file://$PWD/conf/log4j.properties
