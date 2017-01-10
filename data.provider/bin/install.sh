#!/bin/bash

sudo apt-get -y install python-pip

# the following packages were required on a fresh Amazon AWS Ubuntu 14 image - start
sudo apt-get -y install python-dev
sudo apt-get -y install binutils-doc debian-keyring g++-multilib g++-4.8-multilib gcc-4.8-doc
sudo apt-get -y install libstdc++6-4.8-dbg gcc-multilib autoconf automake1.9 libtool flex bison gdb
sudo apt-get -y install gcc-doc gcc-4.8-multilib gcc-4.8-locales libgcc1-dbg libgomp1-dbg
sudo apt-get -y install libitm1-dbg libatomic1-dbg libasan0-dbg libtsan0-dbg libquadmath0-dbg
sudo apt-get -y install glibc-doc libstdc++-4.8-doc make-doc python-genshi python-lxml
sudo apt-get -y install python3-setuptools
# - end

sudo pip install cql
sudo pip install pytz

git clone https://github.com/mumrah/kafka-python
sudo pip install ./kafka-python

sudo pip install flask
sudo pip install jsonschema
