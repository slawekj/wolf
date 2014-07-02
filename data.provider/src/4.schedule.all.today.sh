#!/bin/bash

for p in USDJPY EURUSD AUDUSD GBPUSD USDCAD USDCHF NZDUSD
do
	echo "scheduling $p"
	/home/ubuntu/data.provider/src/3.schedule.today.sh $p
	sleep 5
done
