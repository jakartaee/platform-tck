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
echo "Download and install JSF TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jsftck-2.3_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jsftck-2.3_Latest.zip
fi
unzip jsftck-2.3_Latest.zip

cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

TS_HOME=$TCK_HOME/jsftck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^webServerHome=.*#webServerHome=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^webServerHost=.*#webServerHost=localhost#g" ts.jte
sed -i "s#^webServerPort=.*#webServerPort=8080#g" ts.jte
sed -i "s#^impl.vi=.*#impl.vi=glassfish#g" ts.jte
sed -i "s#^impl.vi.deploy.dir=.*#impl.vi.deploy.dir=$TCK_HOME/glassfish5/glassfish/domains/domain1/autodeploy#g" ts.jte
sed -i "s#^jsf.classes=.*#jsf.classes=${webServerHome}/modules/cdi-api.jar;${webServerHome}/modules/javax.servlet.jsp.jstl-api.jar;${webServerHome}/modules/javax.inject.jar;${webServerHome}/modules/jakarta.faces.jar;${webServerHome}/modules/javax.servlet.jsp-api.jar;${webServerHome}/modules/javax.servlet-api.jar;${webServerHome}/modules/javax.el.jar#g" ts.jte
sed -i 's/^impl\.deploy\.timeout\.multiplier=.*/impl\.deploy\.timeout\.multiplier=960/g' ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jsftckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jsftckwork#g" ts.jte

mkdir $TCK_HOME/jsftckreport
mkdir $TCK_HOME/jsftckwork

cd $TS_HOME/src/com/sun/ts/tests/jsf
ant -Dutil.dir=$TS_HOME deploy.all
ant -Dutil.dir=$TS_HOME runclient
