#!/bin/bash

# save the current directory in WOLF_PWD
WOLF_PWD=`pwd`

# send the process info relevant to our wolf processes to wolf_process_info.txt
ps -ef | grep "data." | grep "java -jar " >wolf_process_info.txt

# read each line of wolf_process_info.txt
while read line
do
  # for each line, get just the process id number in variable proc_no
  proc_no=$(echo $line | cut -d " " -f2)
  # display the process number proc_no
  echo $proc_no
  # kill the process identified by proc_no
  kill $proc_no
done < "wolf_process_info.txt"

