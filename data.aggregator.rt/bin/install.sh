#!/bin/bash

sudo cp java.sh /etc/profile.d/
sudo chmod +x /etc/profile.d/java.sh
source /etc/profile.d/java.sh

echo "deb http://debian.datastax.com/datastax-ddc 3.2 main" | sudo tee -a /etc/apt/sources.list.d/cassandra.sources.list
curl -L https://debian.datastax.com/debian/repo_key | sudo apt-key add -

sudo apt-get update

sudo apt-get install datastax-ddc

echo ' '

echo 'Now, you need to edit /etc/cassandra/conf/cassandra.yaml'

echo 'with your hostnames, etc.  Read the README.md for instructions.'

echo ' '

echo 'When done, run.sh will start cassandra for you.'

echo ' '


