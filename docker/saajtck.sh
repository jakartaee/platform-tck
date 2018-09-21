#!/bin/bash -x

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


export TCK_HOME=${WORKSPACE}
echo "TCK_HOME in saajtck.sh $TCK_HOME"
echo "ANT_HOME in saajtck.sh $ANT_HOME"
echo "PATH in saajtck.sh $PATH"
echo "ANT_OPTS in saajtck.sh $ANT_OPTS"

cd $TCK_HOME

if [ -f "${WORKSPACE}/standalone-bundles/saajtck-1.4_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi
unzip ${WORKSPACE}/standalone-bundles/saajtck-1.4_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/saajtck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin
PROXY_HOST=`echo ${http_proxy} | cut -d: -f2 | sed -e 's/\/\///g'`
PROXY_PORT=`echo ${http_proxy} | cut -d: -f3`
sed -i "s#webcontainer\.home=.*#webcontainer.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#webcontainer\.home.ri=.*#webcontainer.home.ri=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i 's#wsgen.ant.classname=.*#wsgen.ant.classname=com.sun.tools.ws.ant.WsGen#g' ts.jte
sed -i 's#wsimport.ant.classname=.*#wsimport.ant.classname=com.sun.tools.ws.ant.WsImport#g' ts.jte
sed -i "s#wsimport.jvmargs=.*#wsimport.jvmargs=-Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttp.nonProxyHosts=localhost#g" ts.jte
sed -i "s#ri.wsimport.jvmargs=.*#ri.wsimport.jvmargs=-Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttp.nonProxyHosts=localhost#g" ts.jte
sed -i "s#glassfish.admin.port.ri=.*#glassfish.admin.port.ri=5858#g" ts.jte
sed -i "s#local.classes=.*#local.classes=$TCK_HOME/glassfish5/glassfish/modules/endorsed/webservices-api-osgi.jar#g" ts.jte
sed -i "s#^endorsed.dirs=.*#endorsed.dirs=$TCK_HOME/glassfish5/glassfish/modules/endorsed#g" ts.jte

#if [ "web" == "$PROFILE" ]; then
#  sed -i "s#1\.4#1.3#g" $TS_HOME/bin/sig-test_se8.map
#fi

sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/saajtckreport/saajtck#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/saajtckwork/saajtck#g" ts.jte

mkdir -p $TCK_HOME/saajtckreport/saajtck
mkdir -p $TCK_HOME/saajtckwork/saajtck

cd $TS_HOME/bin
ant config.vi
# ant add.interop.certs

### ctsStartStandardDeploymentServer.sh starts here #####

cd $TS_HOME/bin;
echo "ant start.auto.deployment.server > /tmp/deploy.out 2>&1 & "
ant start.auto.deployment.server > /tmp/deploy.out 2>&1 &
### ctsStartStandardDeploymentServer.sh ends here #####

cd $TS_HOME/bin
ant deploy.all
ant run.all
echo "Test run complete"

TCK_NAME=saajtck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/
