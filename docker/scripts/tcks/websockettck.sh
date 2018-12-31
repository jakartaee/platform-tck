#!/bin/bash -x
#
# Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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
#

cd $TCK_HOME
echo "Download and install WebSocket TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/websockettck-1.1_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O websockettck-1.1_Latest.zip
fi
unzip websockettck-1.1_Latest.zip

TS_HOME=$TCK_HOME/websockettck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin
sed -i "s#^web.home=.*#web.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^webServerPort=.*#webServerPort=8080#g" ts.jte
sed -i "s#^webServerHost=.*#webServerHost=localhost#g" ts.jte
sed -i "s#^impl.vi=.*#impl.vi=glassfish#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/websockettckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/websockettckwork#g" ts.jte

mkdir $TCK_HOME/websockettckreport
mkdir $TCK_HOME/websockettckwork

cat $TS_HOME/bin/server_policy.append >> $TCK_HOME/glassfish5/glassfish/domains/domain1/config/server.policy
cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

cd $TS_HOME/bin
ant deploy.all
ant run.all
