#!/bin/bash

echo "Starting data.provider.oanda..."

java -jar ./build/libs/data.provider.oanda-all.jar 1>data-provider-oanda.out 2>data-provider-oanda.err &



