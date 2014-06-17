#!/bin/bash

if [ $# -ne 3 ]; then exit 1; fi

PATH_TO_CSV=$1
MONTH=$2
PAIR=$3

cat $PATH_TO_CSV/DAT_ASCII_${PAIR}_T_2014${MONTH}.csv | python -u 2.provider.py ${PAIR} 06 &> output.txt &

