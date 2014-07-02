#!/bin/bash

for p in USDJPY EURUSD AUDUSD GBPUSD USDCAD USDCHF NZDUSD
do
	echo "scheduling $p"
	/home/ubuntu/data.provider/src/3.schedule.today.whatever.sh $p
	sleep 5
done
