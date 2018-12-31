#!/bin/bash -xe
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
echo "Download and install Concurrency TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/concurrencytck-1.0_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O concurrencytck-1.0_Latest.zip
fi
unzip concurrencytck-1.0_Latest.zip

TS_HOME=$TCK_HOME/concurrencytck
echo "TS_HOME $TS_HOME"
chmod -R 777 $TS_HOME

# Workaround, ideally should not be built at all
rm -f $TS_HOME/dist/com/sun/ts/tests/concurrency/spec/ContextService/contextPropagate/ContextPropagate_web.war

cd $TS_HOME/bin
sed -i "s#^webcontainer\.home=.*#webcontainer.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i 's#^concurrency\.classes=.*#concurrency.classes=${webcontainer.home}/modules/javax.enterprise.concurrent-api.jar${pathsep}${webcontainer.home}/modules/javax.servlet-api.jar${pathsep}${webcontainer.home}/modules/javax.ejb-api.jar${pathsep}${webcontainer.home}/modules/jta.jar${pathsep}${webcontainer.home}/modules/javax.enterprise.deploy-api.jar#g' ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/concurrencytckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/concurrencytckwork#g" ts.jte

mkdir $TCK_HOME/concurrencytckreport
mkdir $TCK_HOME/concurrencytckwork

cd $TS_HOME/bin
ant config.vi
cd $TS_HOME/src/com/sun/ts/tests/
ant deploy.all
ant run.all
