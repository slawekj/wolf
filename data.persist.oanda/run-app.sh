#!/bin/bash

echo "Starting data.persist.oanda..."

echo "Remember, data.provider.oanda must also be running."

rm -rf *.log

java -jar ./build/libs/data.persist.oanda-all.jar 1>data-persist-oanda.out 2>data-persist-oanda.err &



