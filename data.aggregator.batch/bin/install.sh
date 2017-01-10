#!/bin/bash

PROJECT_DIR=$HOME/projects
PWD=`pwd`

cd $PROJECT_DIR

if [ ! -d gobblin ]; then
   git clone git@github.com:linkedin/gobblin.git

   cd gobblin

   # latest release is set by a tag
   git checkout tags/gobblin_0.7.0

   # ./gradlew uses the wrapper which picks its own gradle version
   # in this case, a rather old one.  but, it works for them.
   ./gradlew clean build -x test

   cd ..
fi

if [ ! -d /usr/local/hadoop-2.6.0 ]; then
   sudo addgroup hadoop

   sudo adduser --ingroup hadoop hduser

   cd /usr/local

   sudo wget http://mirrors.sonic.net/apache/hadoop/common/hadoop-2.6.0/hadoop-2.6.0.tar.gz

   sudo gunzip hadoop-2.6.0.tar.gz

   sudo tar xvf hadoop-2.6.0.tar

   sudo chown -R hduser:hadoop hadoop-2.6.0
fi

cd $PWD

source hadoop-env.sh

