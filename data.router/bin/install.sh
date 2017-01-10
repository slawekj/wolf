#!/bin/bash

# if oracle java 8 is not installed...
if [ ! -d /usr/lib/jvm/java-8-oracle ]; then
   # purge all previously installed versions of java
   sudo apt-get purge openjdk-\* icedtea-\* icedtea6-\*

   # install a fresh oracle java 8
   sudo add-apt-repository ppa:webupd8team/java -y
   sudo apt-get update
   sudo apt-get install oracle-java8-installer

   # copy a file so that JAVA_HOME will always be
   # set to the oracle java 8 jdk
   sudo cp java.sh /etc/profile.d/
   source /etc/profile.d/java.sh
fi

# if the old kafka version is installed...
if [ -d kafka_2.9.2-0.8.1.1 ]; then
  # ...then remove it
  rm -rf kafka_2.9.2-0.8.1.1
fi

# new
# if the new kafka version is not installed...
#if [ ! -d kafka_2.11-0.10.0.0 ]; then
#   # ...then install it
#   wget http://apache.mirrors.hoobly.com/kafka/0.10.0.0/kafka_2.11-0.10.0.0.tgz
#   tar xvzf kafka_2.11-0.10.0.0.tgz
#   rm -rf *.tgz
#fi

# new
# if a snapshot build of Kafka is available, install that
if [ ! -d kafka ]; then
   cp $HOME/kafka_2.10-0.10.1.0-SNAPSHOT.tgz .
   gunzip kafka_2.10-0.10.1.0-SNAPSHOT.tgz
   tar xvf kafka_2.10-0.10.1.0-SNAPSHOT.tar
   mv kafka_2.10-0.10.1.0-SNAPSHOT kafka
   rm *.tar
fi

