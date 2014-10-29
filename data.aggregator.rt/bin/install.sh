#!/bin/bash -x -e

sudo cat >/etc/apt/sources.list.d/cassandra.list <<EOF
deb http://www.apache.org/dist/cassandra/debian 21x main
deb-src http://www.apache.org/dist/cassandra/debian 21x main
EOF

for key in 4BD736A82B5C1B00 ; do
    gpg --keyserver pgp.mit.edu --recv-keys $key
    gpg --export --armor $key | sudo apt-key add -
done

sudo apt-get install -y cassandra

cat data.aggregator.rt/src/1.create.table.cql | cqlsh

