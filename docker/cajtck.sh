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
echo "TCK_HOME in cajtck.sh $TCK_HOME"
echo "ANT_HOME in cajtck.sh $ANT_HOME"
echo "PATH in cajtck.sh $PATH"
echo "ANT_OPTS in cajtck.sh $ANT_OPTS"

cd $TCK_HOME


if [ -f "${WORKSPACE}/standalone-bundles/cajtck-1.3_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi
unzip ${WORKSPACE}/standalone-bundles/cajtck-1.3_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/cajtck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^build.level=.*#build.level=2#g" ts.jte
sed -i "s#^endorsed.dirs=.*#endorsed.dirs=$TCK_HOME/glassfish5/glassfish/modules/endorsed#g" ts.jte
sed -i "s#^local.classes=.*#local.classes=$TCK_HOME/glassfish5/glassfish/modules/endorsed/jakarta.annotation-api.jar#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/cajtckreport/cajtck#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/cajtckwork/cajtck#g" ts.jte

mkdir -p $TCK_HOME/cajtckreport/cajtck
mkdir -p $TCK_HOME/cajtckwork/cajtck

cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

cd $TS_HOME/src/com/sun/ts/tests/signaturetest/caj
ant runclient

TCK_NAME=cajtck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/

