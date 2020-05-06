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

if ls ${WORKSPACE}/standalone-bundles/*saajtck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for saajtck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*saajtck*.zip -d ${TCK_HOME}
  TCK_NAME=saajtck
elif ls ${WORKSPACE}/standalone-bundles/*soap-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for soap-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*soap-tck*.zip -d ${TCK_HOME}
  TCK_NAME=soap-tck
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi

##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip -q ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

#temp fix to use latest RC jar for soap api
wget --progress=bar:force --no-cache https://repo1.maven.org/maven2/jakarta/xml/soap/jakarta.xml.soap-api/2.0.0-RC3/jakarta.xml.soap-api-2.0.0-RC3.jar -O ${TCK_HOME}/glassfish5/glassfish/modules/jakarta.xml.soap-api.jar
wget --progress=bar:force --no-cache https://repo1.maven.org/maven2/jakarta/activation/jakarta.activation-api/2.0.0-RC3/jakarta.activation-api-2.0.0-RC3.jar -O ${TCK_HOME}/glassfish5/glassfish/modules/jakarta.activation-api.jar

TS_HOME=$TCK_HOME/$TCK_NAME
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin
sed -i "s#webcontainer\.home=.*#webcontainer.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#webcontainer\.home.ri=.*#webcontainer.home.ri=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i 's#wsgen.ant.classname=.*#wsgen.ant.classname=com.sun.tools.ws.ant.WsGen#g' ts.jte
sed -i 's#wsimport.ant.classname=.*#wsimport.ant.classname=com.sun.tools.ws.ant.WsImport#g' ts.jte
sed -i "s#glassfish.admin.port.ri=.*#glassfish.admin.port.ri=5858#g" ts.jte
sed -i "s#local.classes=.*#local.classes=$TCK_HOME/glassfish5/glassfish/modules/endorsed/webservices-api-osgi.jar:$TCK_HOME/glassfish5/glassfish/modules/jakarta.xml.soap-api.jar:$TCK_HOME/glassfish5/glassfish/modules/jakarta.activation-api.jar#g" ts.jte
sed -i "s#^endorsed.dirs=.*#endorsed.dirs=$TCK_HOME/glassfish5/glassfish/modules/endorsed#g" ts.jte

#if [ "web" == "$PROFILE" ]; then
#  sed -i "s#1\.4#1.3#g" $TS_HOME/bin/sig-test_se8.map
#fi

sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

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

JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
