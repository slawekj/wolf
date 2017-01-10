#!/bin/bash

sudo apt-get install php5-cli

sudo wget http://www.flotcharts.org/downloads/flot-0.8.3.zip
unzip flot-0.8.3.zip
mv flot ../src/js/flot
rm -rf flot-0.8.3.zip


