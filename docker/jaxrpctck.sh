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
echo "TCK_HOME in jaxrpctck.sh $TCK_HOME"
echo "ANT_HOME in jaxrpctck.sh $ANT_HOME"
echo "PATH in jaxrpctck.sh $PATH"
echo "ANT_OPTS in jaxrpctck.sh $ANT_OPTS"

cd $TCK_HOME

if ls ${WORKSPACE}/standalone-bundles/*jaxrpctck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jaxrpctck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*jaxrpctck*.zip -d ${TCK_HOME}
  TCK_NAME=jaxrpctck
elif ls ${WORKSPACE}/standalone-bundles/*xml-rpc-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for xml-rpc-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*xml-rpc-tck*.zip -d ${TCK_HOME}
  TCK_NAME=xml-rpc-tck
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

JAVA_OPTIONS="-Djavax.net.ssl.keyStore=$TCK_HOME/glassfish5/glassfish/domains/domain1/config/keystore.jks \
-Djavax.net.ssl.keyStorePassword=changeit \
-Djavax.net.ssl.trustStore=$TCK_HOME/glassfish5/glassfish/domains/domain1/config/cacerts.jks \
-Djava.naming.factory.url=iiop://localhost:3700 -Djava.naming.factory.initial=com.sun.enterprise.naming.impl.SerialInitContextFactory"

TS_HOME=$TCK_HOME/$TCK_NAME
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin

sed -i "s#\${ts.home}#$TS_HOME#g" build.properties
sed -i "s#\${ant.home}#$ANT_HOME#g" build.properties
sed -i "s#webserver.home=.*#webserver.home=$TCK_HOME/glassfish5/glassfish#g" build.properties
sed -i 's#webserver.host=.*#webserver.host=localhost#g' build.properties
sed -i 's#webserver.port=.*#webserver.port=8080#g' build.properties
sed -i "s#webapp.dir=.*#webapp.dir=$TCK_HOME/glassfish5/glassfish/domains/domain1/autodeploy#g" build.properties
sed -i 's#jaxrpc\.home=.*#jaxrpc.home=\$\{webserver.home\}#g' build.properties
sed -i 's#j2ee\.home\.ri=.*#j2ee.home.ri=\$\{webserver.home\}#g' build.properties
sed -i 's#jaxrpc\.tool=.*#jaxrpc.tool=\$\{jaxrpc.home\}/bin/wscompile#g' build.properties
sed -i 's#jaxrpc.deploy.tool=.*#jaxrpc.deploy.tool=\$\{jaxrpc.home\}/bin/wsdeploy#g' build.properties
sed -i 's#\${class.dir}#/ts/jaxrpctck/classes#g' build.properties

sed -i 's#local.classes=.*#local.classes=\$\{webserver.home\}/modules/webservices-osgi.jar:\$\{webserver.home\}/modules/jakarta.xml.rpc-api.jar:\${webserver.home\}/modules/jakarta.servlet-api.jar:\$\{webserver.home\}/modules/jakarta.mail.jar:\$\{webserver.home\}/modules/jaxb-osgi.jar:\$\{webserver.home\}/modules/jakarta.ejb-api.jar:\$\{webserver.home\}/modules/glassfish-naming.jar:\$\{webserver.home\}/modules/bean-validator.jar#g' build.properties
sed -i 's#secureWebServerPort=.*#secureWebServerPort=8181#g' ts.jte
sed -i "s#webServerHome=.*#webServerHome=$TCK_HOME/glassfish5/glassfish#g" ts.jte

echo "webServerHost.1=localhost" >>  ts.jte
echo "webHost1=localhost" >>  ts.jte
echo "webServerPort.1=8080" >>  ts.jte
echo "webServerHost.1=localhost" >>  build.properties
echo "webHost1=localhost" >>  build.properties
echo "webServerPort.1=8080" >>  build.properties
echo "ts.home=$TS_HOME" >>  build.properties

sed -i "s#ts.home=.*#ts.home=$TS_HOME#g" ts.jte
sed -i "s#webserver.home=.*#webserver.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i 's#local.classes=.*#local.classes=\$\{webserver.home\}/modules/webservices-osgi.jar:\$\{webserver.home\}/modules/jakarta.xml.rpc-api.jar:\${webserver.home\}/modules/jakarta.servlet-api.jar:\$\{webserver.home\}/modules/jakarta.mail.jar:\$\{webserver.home\}/modules/jaxb-osgi.jar:\$\{webserver.home\}/modules/jakarta.ejb-api.jar:\$\{webserver.home\}/modules/glassfish-naming.jar:\$\{webserver.home\}/modules/bean-validator.jar#g' ts.jte

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

cp $TS_HOME/lib/tsharness.jar $TCK_HOME/glassfish5/glassfish/lib
cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

echo "AS_ADMIN_MASTERPASSWORD=changeit" > $TS_HOME/bin/password.txt
echo "AS_ADMIN_PASSWORD=" >> $TS_HOME/bin/password.txt
echo "AS_ADMIN_USERPASSWORD=j2ee" >> $TS_HOME/bin/password.txt

./asadmin create-file-user --user admin --passwordfile $TS_HOME/bin/password.txt --groups Administrator:Employee j2ee

sed -i 's/AS_ADMIN_USERPASSWORD=.*/AS_ADMIN_USERPASSWORD=javajoe/g' $TS_HOME/bin/password.txt
sed -i 's/javax\.xml\.namespace=1\.1//g' $TS_HOME/bin/sig-test.map
sed -i 's/javax\.xml\.namespace//g' $TS_HOME/bin/sig-test-pkg-list.txt

./asadmin create-file-user --user admin --passwordfile $TS_HOME/bin/password.txt --groups Manager:Employee:Guest javajoe
./asadmin stop-domain
./asadmin start-domain

cd $TS_HOME/src/com/sun/ts/tests/
ant  -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME} -Dwork.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME} deploy.all 

ant  -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME} -Dwork.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME} runclient
cd $TS_HOME/bin
ant -propertyfile build.properties -Dts.harness.classpath=$TS_HOME/lib/javatest.jar:$TS_HOME/lib/tsharness.jar:$TS_HOME/lib/jaxrpctck.jar:$TS_HOME/classes -Dwork.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME} -Dreport.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME} report
echo "Test run complete"

JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
