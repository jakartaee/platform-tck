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
echo "TCK_HOME in jstltck.sh $TCK_HOME"
echo "ANT_HOME in jstltck.sh $ANT_HOME"
echo "PATH in jstltck.sh $PATH"
echo "ANT_OPTS in jstltck.sh $ANT_OPTS"

cd $TCK_HOME

if ls ${WORKSPACE}/standalone-bundles/*jstltck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jstltck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*jstltck*.zip -d ${TCK_HOME}
  TCK_NAME=jstltck
elif ls ${WORKSPACE}/standalone-bundles/*tags-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for tags-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*tags-tck*.zip -d ${TCK_HOME}
  TCK_NAME=tags-tck
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

sed -i "s#webServerHome=.*#webServerHome=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i 's#webServerPort=.*#webServerPort=8080#g' ts.jte
sed -i 's#impl\.vi=.*#impl.vi=glassfish#g' ts.jte
sed -i "s#impl\.vi\.deploy\.dir=.*#impl.vi.deploy.dir=$TCK_HOME/glassfish5/glassfish/domains/domain1/autodeploy#g" ts.jte
sed -i 's#impl\.deploy\.timeout\.multiplier=.*#impl.deploy.timeout.multiplier=20#g' ts.jte
sed -i 's#jdbc.classes=.*#jdbc.classes=\$\{webServerHome\}/../javadb/lib/derbyclient.jar#g' ts.jte
sed -i 's#webServerHost=.*#webServerHost=localhost#g' ts.jte
sed -i 's#jstl\.db\.name=.*#jstl.db.name=derbyDB#g' ts.jte
sed -i 's#jstl\.db\.server=.*#jstl.db.server=localhost#g' ts.jte
sed -i 's#jstl\.db\.port=.*#jstl.db.port=1527#g' ts.jte
sed -i 's#jstl\.db\.url=.*#jstl.db.url=jdbc:derby://\$\{jstl.db.server\}:\$\{jstl.db.port\}/\$\{jstl.db.name\};create=true#g' ts.jte
sed -i 's#jstl\.db\.driver=.*#jstl.db.driver=org.apache.derby.jdbc.ClientDriver#g' ts.jte
sed -i 's#jstl\.db\.user=.*#jstl.db.user=cts1#g' ts.jte
sed -i 's#jstl\.db\.password=.*#jstl.db.password=cts1#g' ts.jte
sed -i 's#sigTestClasspath=.*#sigTestClasspath=\$\{ts.home\}/classes:\$\{jstl.classes\}:\$\{jspservlet.classes\}:\$\{JAVA_HOME\}/lib/rt.jar#g' ts.jte
sed -i 's#jspservlet.classes=.*#jspservlet.classes=\$\{webServerHome\}/modules/jakarta.servlet-api.jar:\$\{webServerHome\}/modules/javax.servlet.jsp.jar:\$\{webServerHome\}/modules/jakarta.servlet.jsp-api.jar:\$\{webServerHome\}/modules/jakarta.el.jar#g' ts.jte
sed -i 's#jstl.classes=.*#jstl.classes=\$\{webServerHome\}/modules/javax.servlet.jsp.jstl.jar\$\{pathsep\}\$\{webServerHome\}/modules/jakarta.servlet.jsp.jstl-api.jar#g' ts.jte

sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

export ANT_OPTS="${ANT_OPTS} -Djavax.xml.accessExternalStylesheet=all -Djavax.xml.accessExternalSchema=all -Djavax.xml.accessExternalDTD=file,http"
cd $TCK_HOME/glassfish5/glassfish/bin
./asadmin start-database
./asadmin start-domain
./asadmin create-jvm-options -Djavax.xml.accessExternalStylesheet=all
./asadmin create-jvm-options -Djavax.xml.accessExternalSchema=all
./asadmin create-jvm-options -Djavax.xml.accessExternalDTD=file,http
./asadmin stop-domain
./asadmin start-domain

cd $TS_HOME/bin
ant init.javadb
ant deploy.all
ant run.all
echo "Test run complete"

JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
