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

if ls ${WORKSPACE}/standalone-bundles/*eltck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for eltck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*eltck*.zip -d ${TCK_HOME}
  TCK_NAME=eltck
elif ls ${WORKSPACE}/standalone-bundles/*expression-language-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for expression-language-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*expression-language-tck*.zip -d ${TCK_HOME}
  TCK_NAME=expression-language-tck
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

TS_HOME=$TCK_HOME/$TCK_NAME
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin

sed -i "s#^el\.classes=.*#el.classes=$TS_HOME/lib/javatest.jar:$TCK_HOME/glassfish5/glassfish/modules/jakarta.el.jar#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte
if [ ! -z "$TCK_BUNDLE_BASE_URL" ]; then
  sed -i 's#sigTestClasspath=.*#sigTestClasspath=\$\{el.classes\}\$\{pathsep\}\$\{JAVA_HOME\}/lib/rt.jar#g' ts.jte
fi

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

KEYWORDS=all
cd $TS_HOME/bin
ant -Dkeywords=$KEYWORDS run.all
echo "Test run complete"

JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
