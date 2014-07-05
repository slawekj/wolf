#!/bin/bash -e

# do I really need autopurge?
#sudo echo autopurge.purgeInterval=24 >> /etc/zookeeper/conf/zoo.cfg
#sudo echo autopurge.snapRetainCount=5 >> /etc/zookeeper/conf/zoo.cfg
sudo /usr/share/zookeeper/bin/zkServer.sh start

# check if zookeeper is running
echo stat | nc localhost 2181

# run nimbus
./apache-storm-0.9.2-incubating/bin/storm nimbus &
sleep 10
# run storm ui
./apache-storm-0.9.2-incubating/bin/storm ui &
sleep 10
# run storm supervisor
./apache-storm-0.9.2-incubating/bin/storm supervisor &
sleep 10
# now build topology
./apache-storm-0.9.2-incubating/bin storm jar ./wolf/rule.engine/target/rule.engine-0.0.1-SNAPSHOT-jar-with-dependencies.jar rule.engine.RuleEngineTopology RuleEngine

