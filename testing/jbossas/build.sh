#!/bin/sh

# build current JBoss AS master from source
cd build

if [ -d "as" ]
then
 echo "reuse existing AS build source tree"
else
 echo "git clone AS build source tree"
 git clone git://github.com/jbossas/jboss-as.git as
fi

cd as
./build.sh clean install
if [ $? = 0 ]; then
 echo "JBoss AS build success"
else
 echo "JBoss AS build failed"
 exit $?
fi

# merge modules into AS build