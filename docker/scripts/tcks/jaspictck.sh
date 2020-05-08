#!/bin/bash -xe
#
# Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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
echo "Download and install JASPIC TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jaspictck-1.1_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jaspictck-1.1_Latest.zip
fi
unzip jaspictck-1.1_Latest.zip

TS_HOME=$TCK_HOME/jaspictck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin
sed -i 's#orb\.port=.*#orb.port=3700#g' ts.jte
sed -i "s#jaspic\.home=.*#jaspic.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i 's#platform\.mode=.*#platform.mode=javaEE#g' ts.jte
sed -i 's#^deliverable\.class=.*#deliverable.class=com.sun.ts.lib.deliverable.cts.CTSDeliverable#g' ts.jte
sed -i 's#wsgen\.ant\.classname=.*#wsgen.ant.classname=com.sun.tools.ws.ant.WsGen#g' ts.jte
sed -i 's#wsimport\.ant\.classname=.*#wsimport.ant.classname=com.sun.tools.ws.ant.WsImport#g' ts.jte
sed -i 's#wsimport\.jvmargs=.*#wsimport.jvmargs=-Dhttp.proxyHost=<host> -Dhttp.proxyPort=<port> -Dhttp.nonProxyHosts=<host>#g' ts.jte
sed -i 's#ri\.wsimport\.jvmargs=.*#ri.wsimport.jvmargs=-Dhttp.proxyHost=<host> -Dhttp.proxyPort=<port> -Dhttp.nonProxyHosts=<host>#g' ts.jte
sed -i 's#harness\.log\.traceflag=false.*#harness.log.traceflag=true#g' ts.jte
sed -i "s#tools\.jar=.*#tools.jar=$JAVA_HOME/lib/tools.jar#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jaspictckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jaspictckwork#g" ts.jte

echo "persistence.unit.name.2=JPATCK2" >> ts.jte
echo "persistence.unit.name=CTS-EM" >> ts.jte
echo "jakarta.persistence.provider=org.eclipse.persistence.jpa.PersistenceProvider" >> ts.jte
echo "jakarta.persistence.jdbc.driver=org.apache.derby.jdbc.ClientDriver" >> ts.jte
echo "jakarta.persistence.jdbc.url=jdbc:derby://localhost:1527/derbyDB;create=true" >> ts.jte
echo "jakarta.persistence.jdbc.user=cts1" >> ts.jte
echo "jakarta.persistence.jdbc.password=cts1" >> ts.jte
echo "jpa.provider.implementation.specific.properties=eclipselink.logging.level\=OFF" >> ts.jte
echo "persistence.second.level.caching.supported=true" >> ts.jte

mkdir $TCK_HOME/jaspictckreport
mkdir $TCK_HOME/jaspictckwork

cd $TCK_HOME/glassfish5/bin
./asadmin start-domain
./asadmin stop-domain

mkdir $TS_HOME/tmp/deploy_only_workdir
cd $TS_HOME/bin
ant config.vi

cd $TS_HOME/bin
ant enable.jaspic

cd $TCK_HOME/glassfish5/bin
./asadmin stop-domain
./asadmin start-domain

cd $TS_HOME/src/com/sun/ts/tests/jaspic
ant runclient
