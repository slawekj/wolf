#!/bin/bash -e

# remove all previous java
sudo apt-get purge openjdk-\* icedtea-\* icedtea6-\*

# install java 1.7
sudo apt-get install openjdk-7-jdk

sudo apt-get install zookeeper
# zookeeper conf is in /etc/zookeeper/conf/zoo.cfg

# we will need storm
sudo apt-get install unzip
wget http://mirror.symnds.com/software/Apache/incubator/storm/apache-storm-0.9.2-incubating/apache-storm-0.9.2-incubating.zip
unzip apache-storm-0.9.2-incubating.zip

# we will also need storm-kafka integration jars in mvn repository
# this step might be done on dev machine, not production server
# this step will install storm-core 0.9.3 and storm-kafka 0.9.3 and kafka_2.9.2 0.8.1.1 to local mvn repository
sudo apt-get install maven2 
git clone https://github.com/apache/incubator-storm.git
cd incubator-storm && mvn clean install -DskipTests=true && cd
# once this is done we can install rule engine
cd wolf/rule.engine && mvn clean install

# run nimbus
#./apache-storm-0.9.2-incubating/bin/storm nimbus &
#sleep 10
# run storm ui
#./apache-storm-0.9.2-incubating/bin/storm ui &
#sleep 10
# run storm supervisor
#./apache-storm-0.9.2-incubating/bin/storm supervisor &
#sleep 10
# now build topology


