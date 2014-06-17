#!/usr/bin/python

import time, datetime
import cql
import struct

# connect to database
con = cql.connect('ec2-54-187-166-118.us-west-2.compute.amazonaws.com',
                '9160', 'janusz_forex_rt_demo', cql_version='3.1.1')
cursor = con.cursor()

t = dict(lim='10')
cursor.execute("select issued_at, ask from ticks limit :lim", {"lim": 10})
r = cursor.fetchall()
print str(datetime.datetime.fromtimestamp(  struct.unpack('!Q',r[0][0])[0]/ 1e3  ))

cursor.close()
con.close()
