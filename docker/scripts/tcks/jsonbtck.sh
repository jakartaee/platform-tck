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
echo "Download and install JSON-B TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jsonbtck-1.0_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jsonbtck-1.0_Latest.zip
fi
unzip jsonbtck-1.0_Latest.zip

TS_HOME=$TCK_HOME/jsonbtck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jsonbtckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jsonbtckwork#g" ts.jte
sed -i "s#^jsonb\.classes=.*#jsonb.classes=$TCK_HOME/glassfish5/glassfish/modules/javax.json.bind-api.jar:$TCK_HOME/glassfish5/glassfish/modules/javax.json.jar:$TCK_HOME/glassfish5/glassfish/modules/javax.inject.jar:$TCK_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$TCK_HOME/glassfish5/glassfish/modules/yasson.jar#" ts.jte

mkdir $TCK_HOME/jsonbtckreport
mkdir $TCK_HOME/jsonbtckwork

cd $TS_HOME/src/com/sun/ts/tests/
ant run.all
