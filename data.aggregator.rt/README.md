data.aggregator.rt
==================

This is the wolf data.aggregator.rt module.

To install:
```
PROJECT_DIR=<your project dir>
cd $PROJECT_DIR
cd wolf
cd data.aggregator.rt
cd bin

nano java.sh and make sure it points to your Java 8 JDK home.

nano install.sh and make sure you are Ok with it.

./install.sh and let the installation run.  Watch for any errors.

Notably, current versions of Cassandra Community version now require
Java 8.  data.aggregator.rt and all other modules have been updated
accordingly.

install.sh will copy a version of java.sh to /etc/profile.d/java.sh so
that the java environment gets set correctly with every boot.  you
may want to reboot your node after ./install.sh completes.

```

To configure:
```
cd /etc/cassandra
sudo /bin/bash
yes, sudo /bin/bash, because if you don't you will forget to
 sudo nano cassandra.yaml and you won't be able to save your changes.
since there are a lot of changes, this can be extremely demoralizing
for you. so, once again, sudo /bin/bash

cd /etc/cassandra
sudo nano cassandra.yaml

here are the cassandra.yaml changes for an Amazon AWS Ubuntu 14 server:

    cluster_name to 'cluster-x' or some other letter
    authenticator to PasswordAuthenticator
    authorizer to CassandraAuthorizer
    seed to your Amazon AWS external, public, elastic IP address.  just the digits and dots
    listen_address to your Amazon AWS internal IP address.  just the digits and dots.
    broadcast_address to the external IP
    rpc_address to 0.0.0.0
    broadcast_rpc_address to the external IP
    endpoint_snitch to Ec2MultiRegionSnitch

nano run.sh and make sure you are Ok with it.

Also check all of the other configuration files for usage of localhost and 127.0.0.1.

```

To run:
```
./run.sh and let it run.  Check ps -ef to see the processes running.  Check log files
for errors.

You can check the status of cassandra by using the command: nodetool status

You can also check the status of the cassandra service by using the command: sudo service cassandra status

Cassandra log files are in /var/log/cassandra

```

To stop:
```
```

To be helpful:
```
Fork the github repository to your repository.
Clone your repository to your development machine.
Use git to manage your changes.
When you have something good to contribute back, create a pull request.  (See github for instructions.)
```
