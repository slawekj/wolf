#!/bin/bash

PWDDIR=`pwd`

PROJECT_DIR=$HOME/projects
export PROJECT_DIR=$PROJECT_DIR

WOLF_DATA_PROVIDER_HOME=$PROJECT_DIR/wolf/data.provider
export WOLF_DATA_PROVIDER_HOME=$WOLF_DATA_PROVIDER_HOME

if [ ! -d $WOLF_DATA_PROVIDER_HOME/log ]; then
   cd $WOLF_DATA_PROVIDER_HOME
   mkdir log
   cd $PWDDIR
fi

WOLF_HISTDATA_HOME=$PROJECT_DIR/wolf/histdata.com
export WOLF_HISTDATA_HOME=$WOLF_HISTDATA_HOME

if [ ! -d $WOLF_HISTDATA_HOME/data ]; then
   cd $WOLF_HISTDATA_HOME
   mkdir data
   cd data
   mkdir csv
   cd $PWDDIR
fi

cd $PWDDIR

KAFKA_HOST=`cat .kafkahost.properties`
export KAFKA_HOST=$KAFKA_HOST

KAFKA_PORT=9092
export KAFKA_PORT=$KAFKA_PORT

CQLSH_HOST=`cat .cqlhost.properties`
export CQLSH_HOST=$CQLSH_HOST

CQLSH_PORT=9042
export CQLSH_PORT=$CQLSH_PORT

BROKER_STRING="janusz"
export BROKER_STRING=$BROKER_STRING

$WOLF_DATA_PROVIDER_HOME/src/4.schedule.all.today.sh


echo $WOLF_DATA_PROVIDER_HOME >tmp.txt
sed -i -e 's/\//\\\//g' tmp.txt
REPLACE_STRING=`cat tmp.txt`
sed -e "s/\/home\/ubuntu\/data.provider/$REPLACE_STRING/g" $WOLF_DATA_PROVIDER_HOME/src/5.crontab.txt > $WOLF_DATA_PROVIDER_HOME/src/5.crontab.tmp
unset REPLACE_STRING
rm -rf tmp.txt

if [ -f $WOLF_DATA_PROVIDER_HOME/../data.aggregator.batch/src/crontab.tmp ]; then
   cat $WOLF_DATA_PROVIDER_HOME/src/5.crontab.tmp $WOLF_DATA_PROVIDER_HOME/../data.aggregator.batch/src/crontab.tmp | crontab -
else
   cat $WOLF_DATA_PROVIDER_HOME/src/5.crontab.tmp | crontab -
fi
