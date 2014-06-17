from flask import Flask, jsonify
from thread import start_new_thread
from datetime import timedelta
import time, datetime, struct
import cql
import pytz
from datetime import timedelta
from flask import make_response, request, current_app
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

# connect to database
con = cql.connect('ec2-54-187-166-118.us-west-2.compute.amazonaws.com',
                '9160', 'janusz_forex_rt_demo', cql_version='3.1.1')
cursor = con.cursor()

#tasks = [{'NZDUSD':0}]
tasks = {'NZDUSD':0}

def foo(x):
	while True:
		date = datetime.datetime.now()

		today = str(date.year) + "-" + str(date.month).zfill(2) + "-" + str(date.day).zfill(2)

		t = ('NZDUSD',)

		q = "select issued_at, ask, bid from ticks where "
		q = q + "pair_day = '" + "NZDUSD" + ":" + today + "'"
		q = q + " and "
		q = q + " issued_at > '" + today + " " + str(date.hour).zfill(2)
		q = q +  ":" + str(date.minute - 1).zfill(2) + ":" + str(date.second).zfill(2) + "'"
		#q = q + " limit 10"

		#print q

		#cursor.execute(q,t)
		cursor.execute(q)
		
		#workaround
		data = cursor.fetchall()
		for d in data:
			#d[0] = struct.unpack('!Q', d[0] )[0]/ 1e3 
			#d[0] = struct.unpack('!Q', d[0] )[0] 
			d[0] = str(datetime.datetime.fromtimestamp(
				struct.unpack('!Q', d[0])[0]/1e3 )
				+ timedelta(hours=7)
			)
			#d[0] = str(datetime.datetime.fromtimestamp(
			#	struct.unpack('!Q', d[0])[0]/1e3 ,
			#	#pytz.timezone('US/Pacific'))
			#	pytz.timezone('UTC'))
			#)

		#export data

		#tasks[0]['NZDUSD'] = data
		tasks['NZDUSD'] = data

		time.sleep(2)

start_new_thread(foo,(tasks,))

app = Flask(__name__)

#app.config["CACHE_TYPE"] = "null"
#print app.config['CACHE_TYPE']

@app.route('/', methods = ['GET'])
@crossdomain(origin='*')
def get_tasks():
    return jsonify( { 'ticks': tasks } )

if __name__ == '__main__':
	app.run(debug = True)
	cursor.close()
	con.close()
