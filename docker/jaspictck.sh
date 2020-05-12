#!/bin/bash -xe

# Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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
echo "TCK_HOME in jaspictck.sh $TCK_HOME"
echo "ANT_HOME in jaspictck.sh $ANT_HOME"
echo "PATH in jaspictck.sh $PATH"
echo "ANT_OPTS in jaspictck.sh $ANT_OPTS"

cd $TCK_HOME
if ls ${WORKSPACE}/standalone-bundles/*jaspictck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jaspictck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*jaspictck*.zip -d ${TCK_HOME}
  TCK_NAME=jaspictck
elif ls ${WORKSPACE}/standalone-bundles/*authentication-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for authentication-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*authentication-tck*.zip -d ${TCK_HOME}
  TCK_NAME=authentication-tck
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi


if [[ "$PROFILE" == "web" || "$PROFILE" == "WEB" ]];then
  KEYWORDS="jaspic_web_profile"
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
sed -i 's#orb\.port=.*#orb.port=3700#g' ts.jte
sed -i "s#jaspic\.home=.*#jaspic.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i 's#platform\.mode=.*#platform.mode=javaEE#g' ts.jte
sed -i 's#^deliverable\.class=.*#deliverable.class=com.sun.ts.lib.deliverable.cts.CTSDeliverable#g' ts.jte
sed -i 's#wsgen\.ant\.classname=.*#wsgen.ant.classname=com.sun.tools.ws.ant.WsGen#g' ts.jte
sed -i 's#wsimport\.ant\.classname=.*#wsimport.ant.classname=com.sun.tools.ws.ant.WsImport#g' ts.jte
sed -i 's#harness\.log\.traceflag=false.*#harness.log.traceflag=true#g' ts.jte
sed -i "s#tools\.jar=.*#tools.jar=$JAVA_HOME/lib/tools.jar#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte

echo "persistence.unit.name.2=JPATCK2" >> ts.jte
echo "persistence.unit.name=CTS-EM" >> ts.jte
echo "jakarta.persistence.provider=org.eclipse.persistence.jpa.PersistenceProvider" >> ts.jte
echo "jakarta.persistence.jdbc.driver=org.apache.derby.jdbc.ClientDriver" >> ts.jte
echo "jakarta.persistence.jdbc.url=jdbc:derby://localhost:1527/derbyDB;create=true" >> ts.jte
echo "jakarta.persistence.jdbc.user=cts1" >> ts.jte
echo "jakarta.persistence.jdbc.password=cts1" >> ts.jte
echo "jpa.provider.implementation.specific.properties=eclipselink.logging.level\=OFF" >> ts.jte
echo "persistence.second.level.caching.supported=true" >> ts.jte

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

cd $TCK_HOME/glassfish5/bin
./asadmin start-domain
./asadmin stop-domain

mkdir $TS_HOME/tmp/deploy_only_workdir
cd $TS_HOME/bin
ant config.vi

cd $TS_HOME/bin
ant enable.jaspic

cd $TCK_HOME/glassfish5/bin
./asadmin stop-domain
./asadmin start-domain

#cd $TS_HOME/src/com/sun/ts/tests/jaspic
cd $TS_HOME/bin
if [ -z "$KEYWORDS" ]; then
  ant run.all
else
  ant run.all -Dkeywords=\"${KEYWORDS}\" ;
fi

echo "Test run complete"

JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
