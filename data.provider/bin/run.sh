#!/bin/bash

export WOLF_DATA_PROVIDER_HOME=$HOME/wolf/data.provider
export WOLF_HISTDATA_HOME=$HOME/wolf/histdata.com

$WOLF_DATA_PROVIDER_HOME/src/4.schedule.all.today.sh

cat $WOLF_DATA_PROVIDER_HOME/src/5.crontab.txt | crontab -
