#!/bin/bash -x

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
echo "TCK_HOME in jpatck.sh $TCK_HOME"
echo "ANT_HOME in jpatck.sh $ANT_HOME"
echo "PATH in jpatck.sh $PATH"
echo "ANT_OPTS in jpatck.sh $ANT_OPTS"

cd $TCK_HOME

if ls ${WORKSPACE}/standalone-bundles/*jpatck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jpatck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*jpatck*.zip -d ${TCK_HOME}
  TCK_NAME=jpatck
elif ls ${WORKSPACE}/standalone-bundles/*persistence-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for persistence-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*persistence-tck*.zip -d ${TCK_HOME}
  TCK_NAME=persistence-tck
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

sed -i "s#^jpa.classes=.*#jpa.classes=$TCK_HOME/glassfish5/glassfish/modules/jakarta.persistence.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.jpa.jar:$TCK_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$TCK_HOME/glassfish5/glassfish/modules/jakarta.transaction-api.jar:$TCK_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.antlr.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.asm.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.asm.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.core.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.dbws.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.jpa.jpql.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.jpa.modelgen.processor.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.moxy.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.oracle.jar#g" ts.jte
sed -i "s#jdbc\.driver\.classes=.*#jdbc.driver.classes=$TCK_HOME/glassfish5/javadb/lib/derbyclient.jar:$TS_HOME/lib/dbprocedures.jar#g" ts.jte
sed -i "s#jdbc\.db=.*#jdbc.db=derby#g" ts.jte
sed -i "s#jakarta.persistence.provider=.*#jakarta.persistence.provider=org.eclipse.persistence.jpa.PersistenceProvider#g" ts.jte
sed -i "s#jakarta.persistence.jdbc.driver=.*#jakarta.persistence.jdbc.driver=org.apache.derby.jdbc.ClientDriver#g" ts.jte
sed -i 's#jakarta.persistence.jdbc.url=.*#jakarta.persistence.jdbc.url=jdbc:derby:\/\/localhost:1527\/derbyDB;create=true#g' ts.jte
sed -i "s#jakarta.persistence.jdbc.user=.*#jakarta.persistence.jdbc.user=cts1#g" ts.jte
sed -i "s#jakarta.persistence.jdbc.password=.*#jakarta.persistence.jdbc.password=cts1#g" ts.jte
echo "impl.vi=glassfish" >> ts.jte
#sed -i "s#harness.log.traceflag=false.*#harness.log.traceflag=true#g" ts.jte

sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

cd $TCK_HOME/glassfish5/glassfish/bin
./asadmin start-database --dbhome $TCK_HOME/glassfish5/glassfish/databases --dbport 1527 --jvmoptions "-Djava.security.manager -Djava.security.policy=$TS_HOME/bin/security.policy"

cd $TS_HOME/bin
ant -f initdb.xml
ant -f xml/ts.top.import.xml deploy.all
ant run.all
echo "Test run complete"

JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
