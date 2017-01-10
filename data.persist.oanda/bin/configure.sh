#!/bin/bash

echo 'This script stops and starts Cassandra and initially creates the market_data keyspace.'

sudo service cassandra stop

sleep 20

sudo service cassandra start

sleep 20

cqlsh -u cassandra -f ../src/main/resources/1.create.table.cql

