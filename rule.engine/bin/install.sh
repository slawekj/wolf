#!/bin/bash -e

# remove all previous java
sudo apt-get purge openjdk-\* icedtea-\* icedtea6-\*

# install java 1.8
#sudo apt-get install openjdk-8-jdk  - works with Ubuntu 14.10 and higher
sudo add-apt-repository ppa:webupd8team/java -y
sudo apt-get update
sudo apt-get install oracle-java8-installer

sudo apt-get install zookeeper
# zookeeper conf is in /etc/zookeeper/conf/zoo.cfg

# we will need storm

### TODO - fcn - it looks like you are just grabbing the jars here.  I set up gradle to do it for us.
sudo apt-get install unzip
###wget http://mirror.symnds.com/software/Apache/incubator/storm/apache-storm-0.9.2-incubating/apache-storm-0.9.2-incubating.zip
###unzip apache-storm-0.9.2-incubating.zip
wget http://apache.mirrors.hoobly.com/storm/apache-storm-1.0.1/apache-storm-1.0.1.tar.gz
gunzip apache-storm-1.0.1.tar.gz
tar xvf apache-storm-1.0.1.tar

# we will also need storm-kafka integration jars in mvn repository
# this step might be done on dev machine, not production server
# this step will install storm-core 0.9.3 and storm-kafka 0.9.3 and kafka_2.9.2 0.8.1.1 to local mvn repository
###sudo apt-get purge maven\*
###sudo apt-get install maven
###git clone https://github.com/apache/storm.git
###cd storm
###git checkout tags/v1.0.1
###mvn clean install -DskipTests=true
# once this is done we can install rule engine
###cd ..
cd ..
# this installs a current version of gradle (better than maven)
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt-get install gradle

gradle build
gradle shadowJar

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
