#!/bin/bash

PATH_TO_SRC=/home/ubuntu/restful.rule.submission/src
PATH_TO_LOG=/home/ubuntu/restful.rule.submission/log

PYTHON=/usr/bin/python

${PYTHON} ${PATH_TO_SRC}/server.py &>> ${PATH_TO_LOG}/server.log &
#${PYTHON} ${PATH_TO_SRC}/server.py 

