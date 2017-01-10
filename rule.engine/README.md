rule.engine
===========

This is the wolf rule.engine module.

To install:
```
PROJECT_DIR=<your project dir>
cd $PROJECT_DIR
cd wolf
cd rule.engine
cd bin

nano install.sh and make sure you are Ok with it.

./install.sh and let the installation run.  Watch for any errors.
```

To configure:
```
nano run.sh and make sure you are Ok with it.

Also check all of the other configuration files for usage of localhost and 127.0.0.1.

```

To run:
```
./run.sh and let it run.  Check ps -ef to see the processes running.  Check log files
for errors.
```

To stop:
```
ps -ef
(Note the process ID numbers of the storm and zookeeper processes.)
kill pid      for each process ID
sudo /usr/share/zookeeper/bin/zkServer.sh stop    (to stop ZooKeeper)
```

To be helpful:
```
Fork the github repository to your repository.
Clone your repository to your development machine.
Use git to manage your changes.
When you have something good to contribute back, create a pull request.  (See github for instructions.)
```
