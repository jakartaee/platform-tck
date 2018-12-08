#!/bin/bash -xe

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
echo "TCK_HOME in eltck.sh $TCK_HOME"
echo "ANT_HOME in eltck.sh $ANT_HOME"
echo "PATH in eltck.sh $PATH"
echo "ANT_OPTS in eltck.sh $ANT_OPTS"

cd $TCK_HOME

if [ -f "${WORKSPACE}/standalone-bundles/eltck-3.0_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi
unzip ${WORKSPACE}/standalone-bundles/eltck-3.0_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/eltck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin

sed -i "s#^el\.classes=.*#el.classes=$TS_HOME/lib/javatest.jar:$TCK_HOME/glassfish5/glassfish/modules/jakarta.el.jar#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/eltckreport/eltck#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/eltckwork/eltck#g" ts.jte

mkdir -p $TCK_HOME/eltckreport/eltck
mkdir -p $TCK_HOME/eltckwork/eltck

KEYWORDS=all
cd $TS_HOME/bin
ant -Dkeywords=$KEYWORDS deploy.all
ant -Dkeywords=$KEYWORDS run.all
echo "Test run complete"

TCK_NAME=eltck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
