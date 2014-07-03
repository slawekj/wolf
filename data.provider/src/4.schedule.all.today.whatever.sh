#!/bin/bash 

if [ -z "$WOLF_DATA_PROVIDER_HOME" ]; then
	echo variable WOLF_DATA_PROVIDER_HOME undefined
	exit 1
fi

for p in USDJPY EURUSD AUDUSD GBPUSD USDCAD USDCHF NZDUSD
do
	echo "scheduling $p"
	$WOLF_DATA_PROVIDER_HOME/src/3.schedule.today.whatever.sh $p
	sleep 5
done
