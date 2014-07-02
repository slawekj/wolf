#!/usr/bin/python

from flask import Flask, json, jsonify, request
import socket, time
from jsonschema import validate, ValidationError
from kafka.client import KafkaClient
from kafka.producer import SimpleProducer
from flask import make_response, request, current_app
from datetime import timedelta
from functools import update_wrapper

def crossdomain(origin=None, methods=None, headers=None,
                max_age=21600, attach_to_all=True,
                automatic_options=True):
    if methods is not None:
        methods = ', '.join(sorted(x.upper() for x in methods))
    if headers is not None and not isinstance(headers, basestring):
        headers = ', '.join(x.upper() for x in headers)
    if not isinstance(origin, basestring):
        origin = ', '.join(origin)
    if isinstance(max_age, timedelta):
        max_age = max_age.total_seconds()

    def get_methods():
        if methods is not None:
            return methods

        options_resp = current_app.make_default_options_response()
        return options_resp.headers['allow']

    def decorator(f):
        def wrapped_function(*args, **kwargs):
            if automatic_options and request.method == 'OPTIONS':
                resp = current_app.make_default_options_response()
            else:
                resp = make_response(f(*args, **kwargs))
            if not attach_to_all and request.method != 'OPTIONS':
                return resp

            h = resp.headers

            h['Access-Control-Allow-Origin'] = origin
            h['Access-Control-Allow-Methods'] = get_methods()
            h['Access-Control-Max-Age'] = str(max_age)
            if headers is not None:
                h['Access-Control-Allow-Headers'] = headers
            return resp

        f.provide_automatic_options = False
        return update_wrapper(wrapped_function, f)
    return decorator


# connect to kafka
kafka = KafkaClient("ec2-54-183-118-187.us-west-1.compute.amazonaws.com:9092")
producer = SimpleProducer(kafka)

topic = 'rules'

schema = {
	"type" : "object",
	"properties" : {
		"symbol" : {"type" : "string", "pattern" : "(AUDUSD)|(EURUSD)|(GBPUSD)|(NZDUSD)|(USDCAD)|(USDCHF)|(USDJPY)"},
		"modifier" : {"type" : "string", "pattern" : "(ASK)|(BID)"},
		"comparator" : {"type" : "string", "pattern" : "<|>|="},
		"threshold" : {"type" : "number"},
		"url" : {"type" : "string", "pattern" : "http:\/\/[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:\/~+#-]*[\w@?^=%&amp;\/~+#-])?"},
	},
	"required": ["symbol", "modifier", "comparator", "threshold", "url"]
}

app = Flask(__name__)

@app.route('/', methods = ['POST'])
@crossdomain(origin='*')
def api_message():
	status = ""
	valid = True
	rule = json.loads(request.form['data'])
	try:
		validate(rule,schema)
	except ValidationError:
		valid = False
		
	if valid == True:
		utc = str(int(round(time.time() * 1000)))
		event = str(rule["symbol"]) + " " + "1" + " " + utc + " "
		event = event + utc + " " + str(rule["modifier"]) + " "  
		event = event + str(rule["comparator"]) + " " + str(rule["threshold"]) + " "
		event = event + str(rule["url"])
		print event
		producer.send_messages(topic,event)
		status = "Rule has been queued"
	else:
		status = "Rule is invalid"
	
	resp = jsonify({'status':status})
	resp.status_code = 200
	return resp


if __name__ == '__main__':
	#app.debug = True
        app.run(host=socket.gethostbyname(socket.gethostname()), port=5002)

