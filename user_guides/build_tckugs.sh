#!/bin/bash -xe

# Copyright (c) 2019, 2022 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License v. 2.0, which is available at
# http://www.eclipse.org/legal/epl-2.0.
#
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the
# Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
# version 2 with the GNU Classpath Exception, which is available at
# https://www.gnu.org/software/classpath/license.html.
#
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0


if [ -z "$M2_HOME" ]; then
  export M2_HOME=/usr/share/apache-maven/
fi

if [ -z "$JAVA_HOME" ]; then
  export JAVA_HOME=/opt/jdk-11.0.10
fi

export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH
cd ${WORKSPACE}
export BASEDIR=`pwd`
export USERGUIDEDIR=$BASEDIR/user_guides
alltcks="caj el jacc jaspic jaxws jca jms jpa jsp jstl jta saaj servlet websocket"

cd $USERGUIDEDIR
rm -rf $USERGUIDEDIR/tmp
rm -rf $USERGUIDEDIR/userguides.zip
mkdir -p $USERGUIDEDIR/tmp/userguides

if [ -z "$1" ]; then
 tck="all"
 echo *************Building All TCK Userguides************
 for j in $alltcks
 do
   echo *************Building $j TCK Userguide************
   cd $USERGUIDEDIR/$j;
   mvn -B -Dmaven.repo.local=$HOME/.m2/repository
   cp -r $USERGUIDEDIR/$j/target/staging $USERGUIDEDIR/tmp/userguides/$j
 done
 bundlename="userguides_alltcks.zip"
else
 for i in "$@"
 do
   echo *************Building $i TCK Userguide************
   cd $USERGUIDEDIR/$i;
   mvn -B -Dmaven.repo.local=$HOME/.m2/repository
   cp -r $USERGUIDEDIR/$i/target/staging $USERGUIDEDIR/tmp/userguides/$i
 done
 bundlename="userguides.zip"
fi

cd $USERGUIDEDIR/tmp
chmod -R 777 userguides
zip -r $USERGUIDEDIR/$bundlename userguides/
