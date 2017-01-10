data.provider.oanda
===================

This is the wolf data.provider.oanda module.  It gets real-time tick stream from OANDA
and immediately writes the information to the ticks topic for subsequent persistence.

Prerequisites:
```
A local gradle installation on your PATH.  (See gradle.apache.org)


To install:
```
PROJECT_DIR=<your project dir>
cd $PROJECT_DIR
cd wolf
cd data.provider.oanda
gradle shadowJar


To configure:
```
To use this module, you must have an FX account with OANDA. (www.oanda.com)

Somewhere secure, set your OANDA_API_KEY and OANDA_API_ID as environment variables.

Also, set ZK_HOST to your ZooKeeper host name, and KAFKA_HOST to your Kafka broker host
name as environment variables.

```

To run:
```
./run-app.sh and let it run.  Check ps -ef to see the processes running.  Check log files
for errors.  (If you want logging, edit run-app.sh and change /dev/null to a filename.log.)
```

To be helpful:
```
Fork the github repository to your repository.
Clone your repository to your development machine.
Use git to manage your changes.
When you have something good to contribute back, create a pull request.  (See github for instructions.)

