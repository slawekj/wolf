#!/usr/bin/python

import os
import time, datetime
import cql
import struct

# connect to database
cassandra_host = os.getenv('CQLSH_HOST', 'localhost')
cassandra_port = os.getenv('CQLSH_PORT', '9042')
broker_string  = os.getenv('BROKER_STRING', 'janusz_forex_rt_demo')

con = cql.connect(cassandra_host, cassandra_port, broker_string, cql_version='3.1.1')

cursor = con.cursor()

t = dict(lim='10')
cursor.execute("select issued_at, ask from ticks limit :lim", {"lim": 10})
r = cursor.fetchall()
print str(datetime.datetime.fromtimestamp(  struct.unpack('!Q',r[0][0])[0]/ 1e3  ))

cursor.close()
con.close()
