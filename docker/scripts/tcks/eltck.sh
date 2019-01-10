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
echo "Download and install EL TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/eltck-3.0_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O eltck-3.0_Latest.zip
fi
unzip eltck-3.0_Latest.zip

TS_HOME=$TCK_HOME/eltck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin

sed -i 's#^javax\.el=3\.0_se6.*#javax.el=3.0_se8#g' sig-test.map
sed -i "s#^el\.classes=.*#el.classes=$TS_HOME/lib/javatest.jar:$TCK_HOME/glassfish5/glassfish/modules/javax.el.jar#g" ts.jte
sed -i "s#^sigTestClasspath=.*#sigTestClasspath=\$\{el.classes\}\$\{pathsep\}\$\{JAVA_HOME\}/lib/rt.jar#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/eltckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/eltckwork#g" ts.jte

mkdir $TCK_HOME/eltckreport
mkdir $TCK_HOME/eltckwork

KEYWORDS=all
#if [ "web" == "$PROFILE" ]; then
#  KEYWORDS=javaee_web_profile
#fi

cd $TS_HOME/bin
ant -Dkeywords=$KEYWORDS deploy.all
ant -Dkeywords=$KEYWORDS run.all
