#!/bin/bash -x -e

./kafka_2.9.2-0.8.1.1/bin/zookeeper-server-start.sh ./kafka_2.9.2-0.8.1.1/config/zookeeper.properties &> /dev/null &
./kafka_2.9.2-0.8.1.1/bin/kafka-server-start.sh ./kafka_2.9.2-0.8.1.1/config/server.properties &> /dev/null &
sleep 10

./kafka_2.9.2-0.8.1.1/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic rules
./kafka_2.9.2-0.8.1.1/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic forex
./kafka_2.9.2-0.8.1.1/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic forexJ

