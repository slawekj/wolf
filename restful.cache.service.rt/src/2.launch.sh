#!/bin/bash

PATH_TO_SRC=/home/ubuntu/restful.cache.service/src
PATH_TO_LOG=/home/ubuntu/restful.cache.service/log

PYTHON=/usr/bin/python

${PYTHON} ${PATH_TO_SRC}/server.py &>> ${PATH_TO_LOG}/server.log &
#${PYTHON} ${PATH_TO_SRC}/server.py 
