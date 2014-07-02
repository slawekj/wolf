#!/bin/bash -x -e

sudo apt-get update
sudo apt-get -y install default-jre

wget http://mirror.cogentco.com/pub/apache/kafka/0.8.1.1/kafka_2.9.2-0.8.1.1.tgz
tar xvzf kafka_2.9.2-0.8.1.1.tgz

