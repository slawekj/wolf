# provider.py usdchf 02

# argument 1 is a custom forex pair name
# argument 2 is a custom month number

import sys, sched, time, cql, calendar
from pytz import timezone
from datetime import datetime
from time import mktime, strftime
from kafka.client import KafkaClient
from kafka.producer import SimpleProducer
from flask import json, jsonify
from jsonschema import validate, ValidationError

schema = {
        "properties" : {
                "symbol" : {"type" : "string", "pattern" : "(AUDUSD)|(EURUSD)|(GBPUSD)|(NZDUSD)|(USDCAD)|(USDCHF)|(USDJPY)"},
                "issued_at" : {"type" : "number" },
                "bid" : {"type" : "number"},
                "ask" : {"type" : "number"},
        },
        "required": ["symbol", "issued_at", "bid", "ask"]
}

# connect to kafka
kafka = KafkaClient("ec2-54-183-118-187.us-west-1.compute.amazonaws.com:9092")
producer = SimpleProducer(kafka)

# connect to database
con = cql.connect('ec2-54-187-166-118.us-west-2.compute.amazonaws.com',
		'9160', 'janusz_forex_rt_demo', cql_version='3.1.1')
cursor = con.cursor()

# time conversion and stuff
custom_month = -1 
custom_day   = -1 
est          = timezone('EST')

topic        = "forex"
topicJ       = "forexJ"

forex_pair   = "unknown"
if len(sys.argv) > 3:
	forex_pair = sys.argv[1]
	custom_month = sys.argv[2]
	custom_day = sys.argv[3]

# load a file to memory
print "Loading data to memory..."
lines = [line.strip() for line in sys.stdin]
print "Loading data to memory done!"

# initialize scheduler	
s = sched.scheduler(time.time, time.sleep)

def upload(q,m,l,j):
	print m
	#print l
	#print str(j)
	#print q
	producer.send_messages(topic,l)
	producer.send_messages(topicJ,j)
	cursor.execute(q)

print "Loading data to scheduler..."
for line in lines:
	cols = line.split(",")
	date = cols[0].split(" ")

	year = date[0][0:4]
	month = date[0][4:6]
	if custom_month>0:
		month = custom_month
	day = date[0][6:8]
	if custom_day>0:
		day = custom_day

	hour = date[1][0:2]
	minute = date[1][2:4]
	second = date[1][4:6]
	milisec = date[1][6:9]
	
	bid = cols[1]
	ask = cols[2]

	timestring = year + month + day + hour + minute + second + milisec + "00"	

	timezone_blind = datetime.strptime(timestring,"%Y%m%d%H%M%S%f")
	timezone_aware = est.localize(timezone_blind)
	utc_ts         = datetime.utctimetuple(timezone_aware)
	#utc_t          = mktime(utc_ts) + 1.0 * int(milisec) / 1000
	utc_t          = calendar.timegm(utc_ts) + 1.0 * int(milisec) / 1000
	utc_s1         = strftime('%Y-%m-%d %H:%M:%S.', utc_ts)
	utc_s1         = utc_s1 + milisec + "+0000"
	utc_k          = forex_pair + ":" + strftime('%Y-%m-%d', utc_ts)

	#print utc_ts
	#print utc_k

	q = "INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ("
	q = q + "'" + utc_k + "',"
	q = q + "'" + utc_s1 + "',"
	q = q + "" + bid + ","
	q = q + "" + ask + ") USING TTL 10800;"

	m = "pushing " + forex_pair + " " 
	m = m + utc_s1 + " "
	m = m + "to key " + utc_k + " "
	m = m + "with timestamp " + str(utc_t)

	tick  = forex_pair + " 0 " + '%d' % (utc_t * 1000) + " " + '%d' % (utc_t * 1000) + " " + str(bid) + " " + str(ask) 
	tickJ = json.dumps({'symbol':forex_pair,'issued_at':utc_t,'bid':bid,'ask':ask}) 
        #validate(tickJ,schema)

	s.enterabs(utc_t,1,upload,argument=(q,m,tick,tickJ))
	
print "Loading data to scheduler done!"

# run the scheduler

print "Running scheduler..."
s.run()
print "Running scheduler done!"

# cleanup when scheduler is done
cursor.close()
con.close()

