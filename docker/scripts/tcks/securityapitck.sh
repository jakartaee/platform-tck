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
echo "Download and install Security API TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/securityapitck-1.0_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O securityapitck-1.0_Latest.zip
fi
unzip securityapitck-1.0_Latest.zip

TS_HOME=$TCK_HOME/securityapitck
echo "TS_HOME $TS_HOME"

rm -f $TS_HOME/lib/js-1.6R1.jar
rm -f $TS_HOME/lib/jax-qname.jar

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^web\.home=.*#web.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/securityapitckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/securityapitckwork#g" ts.jte

mkdir -p $TCK_HOME/securityapitckreport
mkdir -p $TCK_HOME/securityapitckwork

cd $TCK_HOME/glassfish5/bin
./asadmin start-database

cd $TS_HOME/bin
ant config.vi
ant init.ldap
ant deploy.all
ant run.all
