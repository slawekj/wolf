#!/bin/bash

echo "Starting data.provider.hist..."

rm *.log

java -jar ./build/libs/data.provider.hist-all.jar >data_provider_hist.log &


