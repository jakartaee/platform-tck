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
echo "TCK_HOME in websockettck.sh $TCK_HOME"
echo "ANT_HOME in websockettck.sh $ANT_HOME"
echo "PATH in websockettck.sh $PATH"
echo "ANT_OPTS in websockettck.sh $ANT_OPTS"

cd $TCK_HOME

if [ -f "${WORKSPACE}/standalone-bundles/websockettck-1.1_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
else
  echo "Download and install Servlet TCK Bundle ..."
  mkdir -p ${WORKSPACE}/standalone-bundles
  wget http://blr00akv.in.oracle.com/tck-builds/links/builds/tcks/javaee_cts/8.1/nightly/websockettck-1.1_Latest.zip -O ${WORKSPACE}/standalone-bundles/websockettck-1.1_latest.zip
fi
unzip ${WORKSPACE}/standalone-bundles/websockettck-1.1_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  export GF_BUNDLE_URL="http://download.oracle.com/glassfish/5.0.1/nightly/latest-glassfish.zip"
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/websockettck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin

sed -i "s#^web.home=.*#web.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^webServerPort=.*#webServerPort=8080#g" ts.jte
sed -i "s#^webServerHost=.*#webServerHost=localhost#g" ts.jte
sed -i "s#^impl.vi=.*#impl.vi=glassfish#g" ts.jte

sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/websockettckreport/websockettck/#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/websockettckwork/websockettck/#g" ts.jte

mkdir -p $TCK_HOME/websockettckreport/websockettck
mkdir -p $TCK_HOME/websockettckwork/websockettck

cat $TS_HOME/bin/server_policy.append >> $TCK_HOME/glassfish5/glassfish/domains/domain1/config/server.policy
cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

cd $TS_HOME/bin
ant deploy.all
ant run.all

TCK_NAME=websockettck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/
