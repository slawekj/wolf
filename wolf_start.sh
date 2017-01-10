#!/bin/bash

WOLF_PWD=`pwd`
git pull

cd data.router/bin
source ./run.sh

cd $WOLF_PWD

cd data.provider.hist
gradle shadowJar
source ./run-app.sh

cd $WOLF_PWD
cd data.persist.hist
gradle shadowJar
source ./run-app.sh

