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

### JWSDP Setup
cd $JWSDP_HOME
echo "Install JDK 1.5 ..."
tar -xvf jdk1.5.0_22.tar

echo "Install Java Web Services Developer Pack 1.3 ..."
tar -xvf jwsdp-1.3.tar

cd $JWSDP_HOME/jwsdp-1.3/jwsdp-shared/bin
sed -i "s#^JAVA_HOME=.*#JAVA_HOME=\"$JWSDP_HOME/jdk1.5.0_22\"#g" setenv.sh
sed -i "s#^JWSDP_HOME=.*#JWSDP_HOME=\"$JWSDP_HOME/jwsdp-1.3\"#g" setenv.sh

echo "Starting Java Web Services Developer Pack ..."
cd $JWSDP_HOME/jwsdp-1.3/bin
./startup.sh &
echo "Java Web Services Developer Pack started ..."

cd $TCK_HOME

echo "Download and install JAX-R TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jaxrtck-1.0_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jaxrtck-1.0_Latest.zip
fi
unzip jaxrtck-1.0_Latest.zip

TS_HOME=$TCK_HOME/jaxrtck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^registryURL =.*#registryURL=http://localhost:8080/RegistryServer/#g" ts.jte
sed -i "s#^queryManagerURL =.*#queryManagerURL=http://localhost:8080/RegistryServer/#g" ts.jte
sed -i "s#^jaxrUser=.*#jaxrUser=testuser#g" ts.jte
sed -i "s#^jaxrPassword=.*#jaxrPassword=testuser#g" ts.jte
sed -i "s#^jaxrUser2=.*#jaxrUser2=jaxr-sqe#g" ts.jte
sed -i "s#^jaxrPassword2=.*#jaxrPassword2=jaxrsqe#g" ts.jte
sed -i "s#^ts_home=.*#ts_home=$TS_HOME/#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jaxrtckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jaxrtckwork#g" ts.jte

sed -i "s#^jwsdp\.home=.*#jwsdp.home=$TCK_HOME/glassfish5/glassfish/#g" $TS_HOME/bin/build.properties
echo "ts.home=$TS_HOME" >>$TS_HOME/bin/build.properties

GF_HOME=$TCK_HOME/glassfish5/glassfish/

mkdir $TCK_HOME/jaxrtckreport
mkdir $TCK_HOME/jaxrtckwork
mkdir $TCK_HOME/jaxrtck-sig

cd $TS_HOME/src/com/sun/ts/tests/jaxr/
ant -Dts.home=$TS_HOME -Djwsdp.home=$TCK_HOME/glassfish5/glassfish -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/jaxrtckreport -Dwork.dir=$TCK_HOME/jaxrtckwork runclient
cd $TS_HOME/src/com/sun/ts/tests/signaturetest/jaxr/
ant -Dts.home=$TS_HOME -Djwsdp.home=$TCK_HOME/glassfish5/glassfish -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/jaxrtck-sig -Dwork.dir=$TCK_HOME/jaxrtck-sig runclient
echo "Test run complete"
