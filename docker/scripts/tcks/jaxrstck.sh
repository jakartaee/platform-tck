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

echo "Download and install JAX-RS TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jaxrstck-2.1_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jaxrstck-2.1_Latest.zip
fi
unzip jaxrstck-2.1_Latest.zip

TS_HOME=$TCK_HOME/jaxrstck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^web.home=.*#web.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jaxrstckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jaxrstckwork#g" ts.jte
sed -i "s#^impl.vi=.*#impl.vi=glassfish#g" ts.jte
sed -i 's#jaxrs_impl\.classes=.*#jaxrs_impl.classes=${web.home}/modules/jersey-client.jar:${web.home}/modules/jersey-common.jar:${web.home}/modules/jersey-server.jar:${web.home}/modules/jersey-container-servlet.jar:${web.home}/modules/jersey-container-servlet-core.jar:${web.home}/modules/jersey-media-jaxb.jar:${web.home}/modules/jersey-media-sse.jar:${web.home}/modules/jersey-hk2.jar:${web.home}/modules/osgi-resource-ocator.jar:${web.home}/modules/javax.inject.jar:${web.home}/modules/guava.jar:${web.home}/modules/hk2-api.jar:${web.home}/modules/hk2-locator.jar:${web.home}/modules/hk2-utils.jar:${web.home}/modules/cglib.jar:${web.home}/modules/asm-all-repackaged.jar:${web.home}/modules/bean-validator.jar:${web.home}/modules/endorsed/javax.annotation-api.jar#g' ts.jte
sed -i 's#jaxrs_impl_lib=.*#jaxrs_impl_lib=${web.home}/modules/jersey-container-servlet-core.jar#g' ts.jte
sed -i 's#jaxrs\.classes=.*#jaxrs.classes=${web.home}/modules/javax.ws.rs-api.jar#g' ts.jte
sed -i 's#servlet_adaptor=.*#servlet_adaptor=org\/glassfish\/jersey\/servlet\/ServletContainer.class#g' ts.jte
sed -i 's#servlet.classes=.*#servlet.classes=${web.home}/modules/javax.servlet-api.jar#g' ts.jte
sed -i 's#jaxrs_impl_name=.*#jaxrs_impl_name=jersey#g' ts.jte
sed -i 's#webServerHost=.*#webServerHost=localhost#g' ts.jte
sed -i 's#webServerPort=.*#webServerPort=8080#g' ts.jte
sed -i 's#impl\.vi=.*#impl.vi=glassfish#g' ts.jte
sed -i 's#impl\.vi\.deploy\.dir=.*#impl.vi.deploy.dir=${web.home}/domains/domain1/autodeploy#g' ts.jte

mkdir $TCK_HOME/jaxrstckreport
mkdir $TCK_HOME/jaxrstckwork

cd $TS_HOME/bin
ant config.vi
cd $TS_HOME/src/com/sun/ts/tests/
ant clean build package
cd $TS_HOME/bin
ant update.jaxrs.wars
ant deploy.all

# TODO : Fix web profile run
#if [ $PROFILE == "web" ];then
#  export ANT_OPTS="-Dkeywords=javaee_web_profile $ANT_OPTS"
#fi

cd $TS_HOME/src/com/sun/ts/tests/
ant runclient
echo "Test run complete"

