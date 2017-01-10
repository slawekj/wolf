data.router
===========

This is the wolf data.router module.  It reads data from histdata.com.

To install:
```
PROJECT_DIR=<your project dir>
cd $PROJECT_DIR
cd wolf
cd data.router
cd bin

nano install.sh and make sure you are Ok with it.  For example, I changed the hash bang
to just "#!/bin/bash"

./install.sh and let the installation run.  Watch for any errors.
```

To configure:
```
nano run.sh and make sure you are Ok with it.  Before I ran it, I first went to edit the
zookeeper settings and the kafka server settings.  I changed localhost to correct hostnames
of the Amazon AWS Ubuntu 14 server I had launched for Wolf.  Specifically,
in server.properties, I changed host.name=ip-xxx-xxx-xxx-xxx.ec2.internal, the internal
hostname which matches what is returned by the hostname command.
Also in server.properties, changed zookeeper.connect=ip-xxx-xxx-xxx-xxx.ec2.internal:2181
Also in server.properties, changed advertised.host.name=ec2-xxx-xxx-xxx-xxx.compute-1.amazonaws.com,
the external public elastic IP address for the server.

The default zookeeper and kafka server settings files use ports 2191 and 9092.  Edit
your Amazon AWS security group to make sure these are opened up to your client IPs.

Also check all of the other configuration files for usage of localhost and 127.0.0.1.
For Amazon AWS, change these to the public elastic IP address of your server(s). 
Specifically, in consumer.properties, I changed zookeeper.connect=xxx.xxx.xxx.xxx:2181,
using the external IP address.  In producer.properties, I changed metadata.broker.list=xxx.xxx.xxx.xxx:9092,
using the external IP address.

I also edited the run.sh and changed /dev/null to zookeeper.log and kafka.log so I could see what
was going on.
```

To run:
```
./run.sh and let it run.  Check ps -ef to see the processes running.  Check log files
for errors.
```

To be helpful:
```
Fork the github repository to your repository.
Clone your repository to your development machine.
Use git to manage your changes.
When you have something good to contribute back, create a pull request.  (See github for instructions.)

