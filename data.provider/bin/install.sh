#!/bin/bash

sudo apt-get -y install python-pip
sudo pip install cql
sudo pip install pytz

git clone https://github.com/mumrah/kafka-python
sudo pip install ./kafka-python

sudo pip install flask
sudo pip install jsonschema
