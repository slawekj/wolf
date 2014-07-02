#!/bin/bash

if [ $# -ne 1 ]
then 
	echo "usage: $(basename $0) SYMBOL"
	exit 1
fi

CAT=/bin/cat
PYTHON=/usr/bin/python

PATH_TO_CSV=/home/ubuntu/histdata.com/data/csv
PATH_TO_SRC=/home/ubuntu/data.provider/src
PATH_TO_LOG=/home/ubuntu/data.provider/log

CURR_MONTH=$(TZ="EST" date +"%m")
PREV_MONTH=$(TZ="EST" date +"%m" --date="1 month ago")

CURR_DAY=$(TZ="EST" date +"%d")
NEXT_DAY=$(TZ="EST" date +"%d" --date="next day")

CURR_HOUR=$(TZ="EST" date +"%H")

CURR_YEAR=$(TZ="EST" date +"%Y")

SYMBOL=$1

${CAT} ${PATH_TO_CSV}/DAT_ASCII_${SYMBOL}_T_${CURR_YEAR}${PREV_MONTH}.csv | grep ^${CURR_YEAR}${PREV_MONTH}${CURR_DAY} | $PYTHON -u $PATH_TO_SRC/2.provider.py $SYMBOL $CURR_MONTH $CURR_DAY &>> $PATH_TO_LOG/$SYMBOL.log &

