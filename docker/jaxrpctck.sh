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

if [ -f "${WORKSPACE}/standalone-bundles/jaxrpctck-1.1_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
else
  echo "Download and install Servlet TCK Bundle ..."
  mkdir -p ${WORKSPACE}/standalone-bundles
  wget http://blr00akv.in.oracle.com/tck-builds/links/builds/tcks/javaee_cts/8.1/nightly/jaxrpctck-1.1_Latest.zip -O ${WORKSPACE}/standalone-bundles/jaxrpctck-1.1_latest.zip
fi
unzip ${WORKSPACE}/standalone-bundles/jaxrpctck-1.1_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  export GF_BUNDLE_URL="http://download.oracle.com/glassfish/5.0.1/nightly/latest-glassfish.zip"
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

JAVA_OPTIONS="-Djavax.net.ssl.keyStore=$TCK_HOME/glassfish5/glassfish/domains/domain1/config/keystore.jks \
-Djavax.net.ssl.keyStorePassword=changeit \
-Djavax.net.ssl.trustStore=$TCK_HOME/glassfish5/glassfish/domains/domain1/config/cacerts.jks \
-Djava.naming.factory.url=iiop://localhost:3700 -Djava.naming.factory.initial=com.sun.enterprise.naming.impl.SerialInitContextFactory"

TS_HOME=$TCK_HOME/jaxrpctck
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

sed -i 's#local.classes=.*#local.classes=\$\{webserver.home\}/modules/webservices-osgi.jar:\$\{webserver.home\}/modules/javax.xml.rpc-api.jar:\${webserver.home\}/modules/javax.servlet-api.jar:\$\{webserver.home\}/modules/javax.mail.jar:\$\{webserver.home\}/modules/jaxb-osgi.jar:\$\{webserver.home\}/modules/javax.ejb-api.jar:\$\{webserver.home\}/modules/glassfish-naming.jar:\$\{webserver.home\}/modules/bean-validator.jar#g' build.properties
sed -i 's#secureWebServerPort=.*#secureWebServerPort=8181#g' ts.jte
sed -i "s#webServerHome=.*#webServerHome=$TCK_HOME/glassfish5/glassfish#g" ts.jte

echo "webServerHost.1=localhost" >>  ts.jte
echo "webHost1=localhost" >>  ts.jte
echo "webServerPort.1=8080" >>  ts.jte
echo "webServerHost.1=localhost" >>  build.properties
echo "webHost1=localhost" >>  build.properties
echo "webServerPort.1=8080" >>  build.properties
echo "ts.home=$TS_HOME" >>  build.properties

mkdir -p $TCK_HOME/jaxrpctckreport/jaxrpctck
mkdir -p $TCK_HOME/jaxrpctckwork/jaxrpctck

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

cd $TS_HOME/src/com/sun/ts/tests/jaxrpc/
ant  -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/jaxrpctckreport/jaxrpctck -Dwork.dir=$TCK_HOME/jaxrpctckwork/jaxrpctck deploy.all 

ant  -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/jaxrpctckreport/jaxrpctck -Dwork.dir=$TCK_HOME/jaxrpctckwork/jaxrpctck runclient
cd $TS_HOME/bin
ant -propertyfile build.properties -Dts.harness.classpath=$TS_HOME/lib/javatest.jar:$TS_HOME/lib/tsharness.jar:$TS_HOME/lib/jaxrpctck.jar:$TS_HOME/classes -Dwork.dir=$TCK_HOME/jaxrpctckwork/jaxrpctck -Dreport.dir=$TCK_HOME/jaxrpctckreport/jaxrpctck report
echo "Test run complete"

TCK_NAME=jaxrpctck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/
