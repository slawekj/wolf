#!/bin/bash

PROJECT_DIR=$HOME/projects
# old JAR=/home/ubuntu/camus/camus-example/target/camus-example-0.1.0-SNAPSHOT-shaded.jar
# new
JAR=$PROJECT_DIR/gobblin/build/gobblin-example/libs/gobblin-example-0.7.0.jar
CLASS=gobblin.example.simplejson.SimpleJsonExtractor
# old PROPERTIES=$PROJECT_DIR/gobblin/gobblin-example/src/main/resources/wikipedia.pull
# new
PROPERTIES=$PROJECT_DIR/gobblin/gobblin-example/src/main/resources/simplejson.pull
HADOOP=hadoop

$HADOOP jar $JAR $CLASS -P $PROPERTIES
