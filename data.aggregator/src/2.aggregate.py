#!/usr/bin/python

import os, cql, sys
from time import strftime
from datetime import datetime

# connect to database
con = cql.connect('ec2-54-187-166-118.us-west-2.compute.amazonaws.com',
                '9160', 'janusz_forex_rt_demo', cql_version='3.1.1')
cursor = con.cursor()

for t in sys.stdin:
	f          = t.split('\t')
	pair       = f[0].strip()
	issued_at  = f[1].strip()
	bid        = f[2].strip()
	ask        = f[3].strip()

	utc_day    = datetime.fromtimestamp(float(issued_at)).strftime('%Y-%m-%d')
	utc_hour   = datetime.fromtimestamp(float(issued_at)).strftime('%Y-%m-%d %H:%M:%S+0000')

        q = "INSERT INTO ticks_avg_s (pair_day,issued_at,bid,ask) VALUES ("
        q = q + "'" + pair + ":" + utc_day + "',"
        q = q + "'" + utc_hour + "',"
        q = q + "" + bid + ","
        q = q + "" + ask + ") USING TTL 10800"

        m = "pushing " + pair + " "
        m = m + utc_hour + " "
        m = m + "to key " + pair + ":" + utc_day 

	print m
	cursor.execute(q)


cursor.close()
con.close()


