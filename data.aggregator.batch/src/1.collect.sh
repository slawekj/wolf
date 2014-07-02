#!/bin/bash

JAR=/home/ubuntu/camus/camus-example/target/camus-example-0.1.0-SNAPSHOT-shaded.jar
CLASS=com.linkedin.camus.etl.kafka.CamusJob
PROPERTIES=/home/ubuntu/camus/camus-example/src/main/resources/camus.properties
HADOOP=hadoop

$HADOOP jar $JAR $CLASS -P $PROPERTIES


