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
echo "Download and install CAJ TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/cajtck-1.3_latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O cajtck-1.3_latest.zip
fi
unzip cajtck-1.3_latest.zip

TS_HOME=$TCK_HOME/cajtck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin

sed -i "s#^build.level=.*#build.level=2#g" ts.jte
sed -i "s#^endorsed.dirs=.*#endorsed.dirs=$TCK_HOME/glassfish5/glassfish/modules/endorsed#g" ts.jte
sed -i "s#^local.classes=.*#local.classes=$TCK_HOME/glassfish5/glassfish/modules/endorsed/javax.annotation-api.jar#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/cajtckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/cajtckwork#g" ts.jte

mkdir $TCK_HOME/cajtckreport
mkdir $TCK_HOME/cajtckwork

cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

cd $TS_HOME/src/com/sun/ts/tests/signaturetest/caj
ant runclient
