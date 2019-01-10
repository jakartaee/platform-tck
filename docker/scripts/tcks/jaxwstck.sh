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

echo "Download and install JAX-WS TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jaxwstck-2.3_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jaxwstck-2.3_Latest.zip
fi
unzip jaxwstck-2.3_Latest.zip

TS_HOME=$TCK_HOME/jaxwstck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin
mkdir $TCK_HOME/ri
sed -i "s#^webcontainer\.home=.*#webcontainer.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^webcontainer\.home\.ri=.*#webcontainer.home.ri=$TCK_HOME/ri/glassfish5/glassfish#g" ts.jte
sed -i 's#^webServerHost\.2=.*#webServerHost.2=localhost#g' ts.jte
sed -i 's#^webServerPort\.2=.*#webServerPort.2=9080#g' ts.jte
sed -i 's#^wsgen.ant.classname=.*#wsgen.ant.classname=com.sun.tools.ws.ant.WsGen#g' ts.jte
sed -i 's#^wsimport.ant.classname=.*#wsimport.ant.classname=com.sun.tools.ws.ant.WsImport#g' ts.jte
PROXY_HOST=`echo ${http_proxy} | cut -d: -f2 | sed -e 's/\/\///g'`
PROXY_PORT=`echo ${http_proxy} | cut -d: -f3`
sed -i "s#^wsimport.jvmargs=.*#wsimport.jvmargs=-Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttp.nonProxyHosts=localhost#g" ts.jte
sed -i "s#^ri.wsimport.jvmargs=.*#ri.wsimport.jvmargs=-Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttp.nonProxyHosts=localhost#g" ts.jte
sed -i "s#^glassfish.admin.port.ri=.*#glassfish.admin.port.ri=5858#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jaxwstckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jaxwstckwork#g" ts.jte

mkdir $TCK_HOME/jaxwstckreport
mkdir $TCK_HOME/jaxwstckwork

export ANT_OPTS="-Djava.endorsed.dirs=$TCK_HOME/glassfish5/glassfish/modules/endorsed -Djavax.xml.accessExternalStylesheet=all -Djavax.xml.accessExternalSchema=all -Djavax.xml.accessExternalDTD=file,http,https"

cd $TS_HOME/bin
ant config.vi

RI_DOMAIN_CONFIG_FILE=$TCK_HOME/ri/glassfish5/glassfish/domains/domain1/config/domain.xml
rm -rf $TCK_HOME/ri/*

# TODO : Web Profile 
cp $TCK_HOME/latest-glassfish.zip $TCK_HOME/ri/latest-glassfish.zip
cd $TCK_HOME/ri
unzip latest-glassfish.zip

sed -i 's/4848/5858/g' $RI_DOMAIN_CONFIG_FILE
sed -i 's/8080/9080/g' $RI_DOMAIN_CONFIG_FILE
sed -i 's/8181/9181/g' $RI_DOMAIN_CONFIG_FILE

cd $TS_HOME/bin
cd $TCK_HOME/ri/glassfish5/glassfish/bin
./asadmin start-domain
cd $TS_HOME/bin
ant config.ri
cd $TS_HOME/src/com/sun/ts/tests/jaxws
ant -Dkeywords=all -Dbuild.vi=true clean build package

ant -Dkeywords=forward -Dreport.dir=$TCK_HOME/jaxwstckreport -Dwork.dir=$TCK_HOME/jaxwstckwork deploy.all runclient 
cd $TS_HOME/src/com/sun/ts/tests/signaturetest
ant -Dreport.dir=$TCK_HOME/jaxwstckreport/jaxwstck-sig -Dwork.dir=$TCK_HOME/jaxwstckwork/jaxwstck-sig runclient
