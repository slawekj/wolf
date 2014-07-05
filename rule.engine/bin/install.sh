#!/bin/bash

# remove all previous java
sudo apt-get purge openjdk-\* icedtea-\* icedtea6-\*

# install java 1.7
sudo apt-get install openjdk-7-jdk


sudo apt-get install zookeeper
# zookeeper conf is in /etc/zookeeper/conf/zoo.cfg

# do I really need autopurge?
#sudo echo autopurge.purgeInterval=24 >> /etc/zookeeper/conf/zoo.cfg
#sudo echo autopurge.snapRetainCount=5 >> /etc/zookeeper/conf/zoo.cfg
sudo /usr/share/zookeeper/bin/zkServer.sh start

# check if zookeeper is running
echo stat | nc localhost 2181

# do I really need ZeroMQ? I hope not
# ignore ZeroMQ installation


sudo apt-get install unzip
wget http://mirror.symnds.com/software/Apache/incubator/storm/apache-storm-0.9.2-incubating/apache-storm-0.9.2-incubating.zip
unzip apache-storm-0.9.2-incubating.zip

# run nimbus
./apache-storm-0.9.2-incubating/bin/storm nimbus &
sleep 10
# run storm ui
./apache-storm-0.9.2-incubating/bin/storm ui &
sleep 10
# run storm supervisor
./apache-storm-0.9.2-incubating/bin/storm supervisor &
sleep 10
# now build topology


