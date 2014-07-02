#!/bin/bash

QFILE=/tmp/$RANDOM.sql
OFILE=/tmp/$RANDOM.txt
LOG=/home/ubuntu/data.aggregator/log/server.log
AGGREGATE=/home/ubuntu/data.aggregator/src/2.aggregate.py
COLLECT=/home/ubuntu/data.aggregator/src/1.collect.sh
HIVE=hive
PYTHON=python
BASH=bash
CAT=cat
RM=rm

LOC=$(date -u +"/forex/forexJ/hourly/%Y/%m/%d/%H")

MIN=$(date -u +"%Y-%m-%d %H:00:00" --date "1 hour ago")

MAX=$(date -u +"%Y-%m-%d %H:00:00" --date "next hour")

# passing arguments to hive is very limited
# so hive query is created dynamically from bash

echo "DROP TABLE IF EXISTS ticks;"                                                            >  $QFILE
echo "CREATE  EXTERNAL TABLE ticks (value STRING ) LOCATION  '$LOC';"                         >> $QFILE
echo "SELECT internal.symbol, internal.issued, avg(internal.bid), avg(internal.ask) FROM ("   >> $QFILE
echo "  SELECT flatten.symbol AS symbol,"                                                     >> $QFILE
echo "    floor(flatten.issued_at/60)*60 AS issued,"                                          >> $QFILE 
echo "    flatten.ask AS ask,"                                                                >> $QFILE 
echo "    flatten.bid AS bid"                                                                 >> $QFILE
echo "  FROM ticks LATERAL VIEW json_tuple(ticks.value, 'symbol', 'ask', 'bid', 'issued_at')" >> $QFILE 
echo "   flatten AS symbol, ask, bid, issued_at"                                              >> $QFILE
echo "  WHERE floor(flatten.issued_at) > unix_timestamp('$MIN')"                              >> $QFILE
echo "    AND floor(flatten.issued_at) < unix_timestamp('$MAX')"                              >> $QFILE
echo ") internal"                                                                             >> $QFILE
echo "GROUP BY internal.symbol, internal.issued;"                                             >> $QFILE
echo "DROP TABLE ticks;"                                                                      >> $QFILE

$BASH $COLLECT
$HIVE -f $QFILE > $OFILE
$CAT $OFILE | $PYTHON $AGGREGATE &>> $LOG
$RM $QFILE
$RM $OFILE


