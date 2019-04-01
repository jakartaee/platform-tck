#!/bin/bash
#
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
#

# Run CTS related steps

echo "JAVA_HOME $JAVA_HOME"
echo "ANT_HOME $ANT_HOME"
echo "TS_HOME $TS_HOME"
echo "PATH $PATH"
echo "Test suite to run $test_suite"

##### installCTS.sh starts here #####
echo "Download CTS Bundle ..."
if [ -z "$CTS_BUNDLE_URL" ]; then
  wget http://<host>/jakartaeetck_latest.zip
else
  echo "Downloading the CTS bundle from $CTS_BUNDLE_URL"  
  wget $CTS_BUNDLE_URL -O jakartaeetck_latest.zip
fi
unzip jakartaeetck_latest.zip

echo "Copy the latest CTS Internal Bundle ..."
wget http://<host>/cts-internal_latest.zip
mv cts-internal_latest.zip $TS_HOME/cts-internal.zip
cd $TS_HOME
unzip -o cts-internal.zip

cd $CTS_HOME
# Do we really need this ?
chmod -R 777 $TS_HOME

cat $TS_HOME/bin/ts.jte | sed "s/-Doracle.jdbc.mapDateToTimestamp/-Doracle.jdbc.mapDateToTimestamp -Djava.security.manager/"  > ts.save
cp ts.save $TS_HOME/bin/ts.jte
##### installCTS.sh ends here #####

##### installRI.sh starts here #####
# TODO : Take care of Web Profile
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "$GLASSFISH_BUNDLE_URL" ]; then
  wget http://<host>/latest-glassfish.zip
else
  echo "Downloading the GlassFish bundle from $GLASSFISH_BUNDLE_URL"  
  wget $GLASSFISH_BUNDLE_URL -O latest-glassfish.zip
fi

mkdir ri
cp latest-glassfish.zip ri
cd ri
unzip latest-glassfish.zip

cd $CTS_HOME
# Do we really need this ?
chmod -R 777 $CTS_HOME/ri/glassfish5

$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile change-admin-password.txt change-admin-password
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt start-domain
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt stop-domain
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt start-domain
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt enable-secure-admin
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt version
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt stop-domain
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt start-domain

# Will these port work ?
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --interactive=false  --user admin --passwordfile admin-password.txt delete-jvm-options -Dosgi.shell.telnet.port=6666
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt create-jvm-options -Dosgi.shell.telnet.port=6667
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt set server-config.jms-service.jms-host.default_JMS_host.port=7776
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt set server-config.iiop-service.iiop-listener.orb-listener-1.port=3701
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt set server-config.iiop-service.iiop-listener.SSL.port=4820
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt set server-config.iiop-service.iiop-listener.SSL_MUTUALAUTH.port=4920
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt set server-config.admin-service.jmx-connector.system.port=9696
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt set server-config.network-config.network-listeners.network-listener.http-listener-1.port=8002
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt set server-config.network-config.network-listeners.network-listener.http-listener-2.port=1045
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt set server-config.network-config.network-listeners.network-listener.admin-listener.port=5858
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt create-jvm-options -Dorg.glassfish.orb.iiop.orbserverid=200

sleep 5
##### installRI.sh ends here #####

##### installGlassFish.sh starts here #####

cd $CTS_HOME
./killJava.sh

mkdir vi
cp latest-glassfish.zip vi
cd vi
unzip latest-glassfish.zip

cd $CTS_HOME
echo "$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt start-domain"
$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile change-admin-password.txt change-admin-password

echo "$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt start-domain"
$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt start-domain

echo "$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt version"
$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt version

echo "$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt create-jvm-options -Djava.security.manager"
$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt create-jvm-options -Djava.security.manager

echo "$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt stop-domain"
$CTS_HOME/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt stop-domain

./killJava.sh
##### installGlassFish.sh ends here #####

##### configVI.sh starts here #####

export ANT_OPTS="-Djava.endorsed.dirs=$CT_HOME/vi/glassfish5/glassfish/modules/endorsed \
                 -Djavax.xml.accessExternalStylesheet=all \
                 -Djavax.xml.accessExternalSchema=all \
                 -Djavax.xml.accessExternalDTD=file,http"

# TODO : Call tsJte 
cd $TS_HOME/bin

sed -i "s#report.dir=.*#report.dir=$CTS_HOME/ctsreport#g" ts.jte
sed -i "s#work.dir=.*#work.dir=$CTS_HOME/ctswork#g" ts.jte

# sed -i 's/mailHost=.*/mailHost=localhost/g' ts.jte
sed -i 's#mailHost=.*#mailHost=172.17.0.2#g' ts.jte

sed -i 's/mailuser1=.*/mailuser1=user01@james.local/g' ts.jte
sed -i 's/mailFrom=.*/mailFrom=user01@james.local/g' ts.jte
sed -i 's/javamail.password=.*/javamail.password=1234/g' ts.jte

sed -i 's/s1as.admin.passwd=.*/s1as.admin.passwd=adminadmin/g' ts.jte
sed -i 's/ri.admin.passwd=.*/ri.admin.passwd=adminadmin/g' ts.jte

sed -i 's/jdbc.maxpoolsize=.*/jdbc.maxpoolsize=30/g' ts.jte
sed -i 's/jdbc.steadypoolsize=.*/jdbc.steadypoolsize=5/g' ts.jte

sed -i "s#javaee.home=.*#javaee.home=$CTS_HOME/vi/glassfish5/glassfish#g" ts.jte
sed -i 's/orb.host=.*/orb.host=localhost/g' ts.jte

sed -i "s#javaee.home.ri=.*#javaee.home.ri=$CTS_HOME/ri/glassfish5/glassfish#g" ts.jte
sed -i 's/orb.host.ri=.*/orb.host.ri=localhost/g' ts.jte

sed -i 's/ri.admin.port=.*/ri.admin.port=5858/g' ts.jte
sed -i 's/orb.port.ri=.*/orb.port.ri=3701/g' ts.jte

sed -i "s#registryURL=.*#registryURL=http://localhost:8080/RegistryServer/#g" ts.jte
sed -i "s#queryManagerURL=.*#queryManagerURL=http://localhost:8080/RegistryServer/#g" ts.jte
sed -i 's/jaxrUser=.*/jaxrUser=testuser/g' ts.jte
sed -i 's/jaxrPassword=.*/jaxrPassword=testuser/g' ts.jte
sed -i 's/jaxrUser2=.*/jaxrUser2=jaxr-sqe/g' ts.jte
sed -i 's/jaxrPassword2=.*/jaxrPassword2=jaxrsqe/g' ts.jte

sed -i "s/^wsgen.ant.classname=.*/wsgen.ant.classname=$\{ri.wsgen.ant.classname\}/g" ts.jte
sed -i "s/^wsimport.ant.classname=.*/wsimport.ant.classname=$\{ri.wsimport.ant.classname\}/g" ts.jte
PROXY_HOST=`echo ${http_proxy} | cut -d: -f2 | sed -e 's/\/\///g'`
PROXY_PORT=`echo ${http_proxy} | cut -d: -f3`
sed -i "s#^wsimport.jvmargs=.*#wsimport.jvmargs=-Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttp.nonProxyHosts=localhost#g" ts.jte
sed -i "s#^ri.wsimport.jvmargs=.*#ri.wsimport.jvmargs=-Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttp.nonProxyHosts=localhost#g" ts.jte
sed -i 's/^impl.deploy.timeout.multiplier=.*/impl.deploy.timeout.multiplier=240/g' ts.jte
sed -i 's/^javatest.timeout.factor=.*/javatest.timeout.factor=2.0/g' ts.jte
sed -i 's/^test.ejb.stateful.timeout.wait.seconds=.*/test.ejb.stateful.timeout.wait.seconds=180/g' ts.jte
sed -i 's/^harness.log.traceflag=.*/harness.log.traceflag=false/g' ts.jte

mkdir $CTS_HOME/ctsreport
mkdir $CTS_HOME/ctswork

REPORT=`cat $TS_HOME/bin/ts.jte |grep ^report.dir=/ | awk -F= '{print $2}'`
echo "report.dir=${REPORT}"
mkdir -p $REPORT
chmod -R 777 $REPORT

export JAVA_VERSION=`java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}'`
echo $JAVA_VERSION > $REPORT/.jdk_version

cd  $TS_HOME/bin
ant config.vi
ant -f xml/impl/glassfish/s1as.xml start.javadb
ant init.javadb
##### configVI.sh ends here #####

##### configRI.sh ends here #####
cd  $TS_HOME/bin
ant config.ri
ant enable.csiv2
##### configRI.sh ends here #####


##### addInteropCerts.sh starts here #####
cd $TS_HOME/bin
ant add.interop.certs
##### addInteropCerts.sh ends here #####

### restartRI.sh starts here #####
cd $CTS_HOME
export PORT=5858
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt -p $PORT stop-domain
$CTS_HOME/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile admin-password.txt -p $PORT start-domain
### restartRI.sh ends here #####

echo "Starting Java Web Services Developer Pack ..."
cd $JWSDP_HOME/jwsdp-1.3/bin
./startup.sh &
echo "Java Web Services Developer Pack started ..."

### ctsStartStandardDeploymentServer.sh starts here #####

cd $TS_HOME/bin;
echo "ant start.auto.deployment.server > /tmp/deploy.out 2>&1 & "
ant start.auto.deployment.server > /tmp/deploy.out 2>&1 &
### ctsStartStandardDeploymentServer.sh ends here #####

cd $TS_HOME/bin
ant -f xml/impl/glassfish/s1as.xml run.cts -Dant.opts="$ANT_OPTS" -Dtest.areas="$test_suite"
