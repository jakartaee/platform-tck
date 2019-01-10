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
echo "Download and install JACC TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jacctck-1.5_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jacctck-1.5_Latest.zip
fi
unzip jacctck-1.5_Latest.zip

TS_HOME=$TCK_HOME/jacctck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin

sed -i 's#orb\.port=.*#orb.port=3699#g' ts.jte
sed -i 's#javaee\.level=.*#javaee.level=full#g' ts.jte
sed -i "s#jacc\.home=.*#jacc.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i 's#jacc\.host=.*#jacc.host=localhost#g' ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jacctckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jacctckwork#g" ts.jte

CONTENT='<property name="all.test.dir" value="com/sun/ts/tests/jacc/,com/sun/ts/tests/signaturetest/jacc,com/sun/ts/tests/common/vehicle/" />'
C=$(echo $CONTENT | sed 's/\//\\\//g')
sed -i "/<\/project>/ s/.*/${C}\n&/" $TS_HOME/bin/build.xml

mkdir $TCK_HOME/jacctckreport
mkdir $TCK_HOME/jacctckwork

cd $TCK_HOME/glassfish5/glassfish/bin
./asadmin start-domain

cd $TS_HOME/bin
ant config.vi

cd $TS_HOME/bin
ant enable.jacc

cd $TS_HOME/src/com/sun/ts/tests/jacc/ejb
ant deploy runclient
cd $TS_HOME/src/com/sun/ts/tests/jacc/web
ant deploy runclient
