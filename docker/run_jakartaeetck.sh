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
if [[ $1 = *'_'* ]]; then
  test_suite=`echo "$1" | cut -f1 -d_`
  vehicle_name=`echo "$1" | cut -f2 -d_`
  vehicle="${vehicle_name}_vehicle"
else
  test_suite="$1"
  vehicle=""
fi

echo "TEST_SUITE:${test_suite}."
echo "VEHICLE:${vehicle}."

if [ -z "${test_suite}" ]; then
  echo "Please supply a valid test_suite as argument"
  exit 1
fi

if [ -z "${CTS_HOME}" ]; then
  export CTS_HOME="${WORKSPACE}"
fi
export TS_HOME=${CTS_HOME}/javaeetck/
# Run CTS related steps
echo "JAVA_HOME ${JAVA_HOME}"
echo "ANT_HOME ${ANT_HOME}"
echo "CTS_HOME ${CTS_HOME}"
echo "TS_HOME ${TS_HOME}"
echo "PATH ${PATH}"
echo "Test suite to run ${test_suite}"

#Set default mailserver related env variables
if [ -z "$MAIL_HOST" ]; then
  export MAIL_HOST="localhost"
fi
if [ -z "$MAIL_USER" ]; then
  export MAIL_USER="user01@james.local"
fi
if [ -z "$MAIL_FROM" ]; then
  export MAIL_FROM="user01@james.local"
fi
if [ -z "$MAIL_PASSWORD" ]; then
  export MAIL_PASSWORD="1234"
fi
if [ -z "$SMTP_PORT" ]; then
  export SMTP_PORT="1025"
fi
if [ -z "$IMAP_PORT" ]; then
  export IMAP_PORT="1143"
fi
##################################################

# Set JWSDP install dir and UDDI Registry server url 
# required for JAXR tests if not set.
if [ -z "$UDDI_REGISTRY_URL" ]; then
  export UDDI_REGISTRY_URL="http://localhost:8080/RegistryServer/"
fi

if [ -z "$JWSDP_HOME" ]; then
  export  JWSDP_HOME="/opt/jwsdp-1.3"
fi

##################################################

##### installCTS.sh starts here #####
cat ${TS_HOME}/bin/ts.jte | sed "s/-Doracle.jdbc.mapDateToTimestamp/-Doracle.jdbc.mapDateToTimestamp -Djava.security.manager/"  > ts.save
cp ts.save $TS_HOME/bin/ts.jte
##### installCTS.sh ends here #####

##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O ${CTS_HOME}/latest-glassfish.zip
rm -Rf ${CTS_HOME}/ri
mkdir -p ${CTS_HOME}/ri
unzip ${CTS_HOME}/latest-glassfish.zip -d ${CTS_HOME}/ri
chmod -R 777 ${CTS_HOME}/ri

export ADMIN_PASSWORD_FILE="${CTS_HOME}/admin-password.txt"
echo "AS_ADMIN_PASSWORD=adminadmin" > ${ADMIN_PASSWORD_FILE}

echo "AS_ADMIN_PASSWORD=" > ${CTS_HOME}/change-admin-password.txt
echo "AS_ADMIN_NEWPASSWORD=adminadmin" >> ${CTS_HOME}/change-admin-password.txt

echo "" >> ${CTS_HOME}/change-admin-password.txt

${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${CTS_HOME}/change-admin-password.txt change-admin-password
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} start-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} stop-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} start-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} enable-secure-admin
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} version
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} stop-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} start-domain

# Change default ports for RI
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --interactive=false  --user admin --passwordfile ${ADMIN_PASSWORD_FILE} delete-jvm-options -Dosgi.shell.telnet.port=6666
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} create-jvm-options -Dosgi.shell.telnet.port=6667
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.jms-service.jms-host.default_JMS_host.port=7776
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${CTS_HOME}/change-admin-password.txt change-admin-password
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} start-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} stop-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} start-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} enable-secure-admin
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} version
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} stop-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} start-domain

# Change default ports for RI
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --interactive=false  --user admin --passwordfile ${ADMIN_PASSWORD_FILE} delete-jvm-options -Dosgi.shell.telnet.port=6666
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} create-jvm-options -Dosgi.shell.telnet.port=6667
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.jms-service.jms-host.default_JMS_host.port=7776
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.iiop-service.iiop-listener.orb-listener-1.port=3701
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.iiop-service.iiop-listener.SSL.port=4820
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.iiop-service.iiop-listener.SSL_MUTUALAUTH.port=4920
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.admin-service.jmx-connector.system.port=9696
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.network-config.network-listeners.network-listener.http-listener-1.port=8002
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.network-config.network-listeners.network-listener.http-listener-2.port=1045
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} set server-config.network-config.network-listeners.network-listener.admin-listener.port=5858
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} create-jvm-options -Dorg.glassfish.orb.iiop.orbserverid=200

sleep 5
echo "Stopping RI domain"
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} stop-domain
sleep 5
echo "Killing any RI java processes that were not stopped gracefully"
echo "Pending process to be killed:"
ps -eaf | grep "$JAVA_HOME/bin/java" | grep -v "grep" | grep -v "nohup" 
for i in `ps -eaf | grep "$JAVA_HOME/bin/java" | grep -v "grep" | grep -v "nohup" | tr -s " " | cut -d" " -f2`
do
  echo "[killJava.sh] kill -9 $i"
  kill -9 $i
done
##### installRI.sh ends here #####

##### installGlassFish.sh starts here #####

mkdir -p ${CTS_HOME}/vi
unzip ${CTS_HOME}/latest-glassfish.zip -d ${CTS_HOME}/vi
chmod -R 777 ${CTS_HOME}/vi

${CTS_HOME}/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${CTS_HOME}/change-admin-password.txt change-admin-password
${CTS_HOME}/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} start-domain
${CTS_HOME}/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} version
${CTS_HOME}/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} create-jvm-options -Djava.security.manager
${CTS_HOME}/vi/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} stop-domain
sleep 5
echo "[killJava.sh] uname: LINUX"
echo "Pending process to be killed:"
ps -eaf | grep "$JAVA_HOME/bin/java" | grep -v "grep" | grep -v "nohup" 
for i in `ps -eaf | grep "$JAVA_HOME/bin/java" | grep -v "grep" | grep -v "nohup" | tr -s " " | cut -d" " -f2`
do
  echo "[killJava.sh] kill -9 $i"
  kill -9 $i
done
##### installGlassFish.sh ends here #####

##### configVI.sh starts here #####

export CTS_ANT_OPTS="-Djava.endorsed.dirs=${CTS_HOME}/vi/glassfish5/glassfish/modules/endorsed \
-Djavax.xml.accessExternalStylesheet=all \
-Djavax.xml.accessExternalSchema=all \
-Djavax.xml.accessExternalDTD=file,http"

if [ "$PROFILE" == "web" ];then
  KEYWORDS="javaee_web_profile|jacc_web_profile|jaspic_web_profile|javamail_web_profile"
fi

if [ -z "${vehicle}" ];then
  echo "Vehicle not set. Running all vehichles"
else
  echo "Vehicle set. Running in vehicle: ${vehicle}"
  if [ -z "${KEYWORDS}" ]; then
    KEYWORDS=${vehicle}
  else
    KEYWORDS="${KEYWORDS}|${vehicle}"
  fi
fi

if [ ! -z "$KEYWORDS" ];then
  CTS_ANT_OPTS="${CTS_ANT_OPTS} -Dkeywords=\"${KEYWORDS}\""
fi
echo "CTS_ANT_OPTS:${CTS_ANT_OPTS}"

export JT_REPORT_DIR=${CTS_HOME}/jakartaeetck-report
export JT_WORK_DIR=${CTS_HOME}/jakartaeetck-work

### Update ts.jte for CTS run
cd ${TS_HOME}/bin
sed -i "s#^report.dir=.*#report.dir=${JT_REPORT_DIR}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=${JT_WORK_DIR}#g" ts.jte

sed -i "s/^mailHost=.*/mailHost=${MAIL_HOST}/g" ts.jte
sed -i "s/^mailuser1=.*/mailuser1=${MAIL_USER}/g" ts.jte
sed -i "s/^mailFrom=.*/mailFrom=${MAIL_FROM}/g" ts.jte
sed -i "s/^javamail.password=.*/javamail.password=${MAIL_PASSWORD}/g" ts.jte
sed -i "s/^smtp.port=.*/smtp.port=${SMTP_PORT}/g" ts.jte
sed -i "s/^imap.port=.*/imap.port=${IMAP_PORT}/g" ts.jte

sed -i 's/^s1as.admin.passwd=.*/s1as.admin.passwd=adminadmin/g' ts.jte
sed -i 's/^ri.admin.passwd=.*/ri.admin.passwd=adminadmin/g' ts.jte

sed -i 's/^jdbc.maxpoolsize=.*/jdbc.maxpoolsize=30/g' ts.jte
sed -i 's/^jdbc.steadypoolsize=.*/jdbc.steadypoolsize=5/g' ts.jte

sed -i "s#^javaee.home=.*#javaee.home=${CTS_HOME}/vi/glassfish5/glassfish#g" ts.jte
sed -i 's/^orb.host=.*/orb.host=localhost/g' ts.jte

sed -i "s#^javaee.home.ri=.*#javaee.home.ri=${CTS_HOME}/ri/glassfish5/glassfish#g" ts.jte
sed -i 's/^orb.host.ri=.*/orb.host.ri=localhost/g' ts.jte

sed -i 's/^ri.admin.port=.*/ri.admin.port=5858/g' ts.jte
sed -i 's/^orb.port.ri=.*/orb.port.ri=3701/g' ts.jte

sed -i "s#^registryURL=.*#registryURL=${UDDI_REGISTRY_URL}#g" ts.jte
sed -i "s#^queryManagerURL=.*#queryManagerURL=${UDDI_REGISTRY_URL}#g" ts.jte
sed -i 's/^jaxrUser=.*/jaxrUser=testuser/g' ts.jte
sed -i 's/^jaxrPassword=.*/jaxrPassword=testuser/g' ts.jte
sed -i 's/^jaxrUser2=.*/jaxrUser2=jaxr-sqe/g' ts.jte
sed -i 's/^jaxrPassword2=.*/jaxrPassword2=jaxrsqe/g' ts.jte

sed -i "s/^wsgen.ant.classname=.*/wsgen.ant.classname=$\{ri.wsgen.ant.classname\}/g" ts.jte
sed -i "s/^wsimport.ant.classname=.*/wsimport.ant.classname=$\{ri.wsimport.ant.classname\}/g" ts.jte
if [ ! -z "$http_proxy" ]; then
  PROXY_HOST=`echo ${http_proxy} | cut -d: -f2 | sed -e 's/\/\///g'`
  PROXY_PORT=`echo ${http_proxy} | cut -d: -f3`
  sed -i "s#^wsimport.jvmargs=.*#wsimport.jvmargs=-Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttp.nonProxyHosts=localhost#g" ts.jte
  sed -i "s#^ri.wsimport.jvmargs=.*#ri.wsimport.jvmargs=-Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT -Dhttp.nonProxyHosts=localhost#g" ts.jte
fi
sed -i 's/^impl.deploy.timeout.multiplier=.*/impl.deploy.timeout.multiplier=240/g' ts.jte
sed -i 's/^javatest.timeout.factor=.*/javatest.timeout.factor=2.0/g' ts.jte
sed -i 's/^test.ejb.stateful.timeout.wait.seconds=.*/test.ejb.stateful.timeout.wait.seconds=180/g' ts.jte
sed -i 's/^harness.log.traceflag=.*/harness.log.traceflag=false/g' ts.jte
sed -i 's/^impl\.deploy\.timeout\.multiplier=240/impl\.deploy\.timeout\.multiplier=480/g' ts.jte

if [ "servlet" == "${test_suite}" ]; then
  sed -i 's/s1as\.java\.endorsed\.dirs=.*/s1as.java.endorsed.dirs=\$\{endorsed.dirs\}\$\{pathsep\}\$\{ts.home\}\/endorsedlib/g' ts.jte
fi

if [[ $test_suite == ejb30/lite* ]] || [[ "ejb30" == $test_suite ]] ; then
  echo "Using higher JVM memory for EJB Lite suites to avoid OOM errors"
  sed -i 's/-Xmx512m/-Xmx2048m/g' ${CTS_HOME}/vi/glassfish5/glassfish/domains/domain1/config/domain.xml
  sed -i 's/-Xmx1024m/-Xmx2048m/g' ${CTS_HOME}/vi/glassfish5/glassfish/domains/domain1/config/domain.xml
  sed -i 's/-Xmx512m/-Xmx2048m/g' ${CTS_HOME}/ri/glassfish5/glassfish/domains/domain1/config/domain.xml
  sed -i 's/-Xmx1024m/-Xmx2048m/g' ${CTS_HOME}/ri/glassfish5/glassfish/domains/domain1/config/domain.xml
fi 

VI_SERVER_POLICY_FILE=${CTS_HOME}/vi/glassfish5/glassfish/domains/domain1/config/server.policy
echo 'grant {' >> ${VI_SERVER_POLICY_FILE}
echo 'permission java.io.FilePermission "${com.sun.aas.instanceRoot}${/}generated${/}policy${/}-", "read,write,execute,delete";' >> ${VI_SERVER_POLICY_FILE}
echo '};' >> ${VI_SERVER_POLICY_FILE}


echo "Contents of ts.jte"
cat ${TS_HOME}/bin/ts.jte

mkdir -p ${JT_REPORT_DIR}
mkdir -p ${JT_WORK_DIR}

export JAVA_VERSION=`java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}'`
echo $JAVA_VERSION > ${JT_REPORT_DIR}/.jdk_version

cd  ${TS_HOME}/bin
ant config.vi
ant -f xml/impl/glassfish/s1as.xml start.javadb
ant init.javadb
##### configVI.sh ends here #####

### populateMailbox for javamail suite - Start ###
ESCAPED_MAIL_USER=`echo ${MAIL_USER} | sed -e 's/@/%40/g'`
cd  ${TS_HOME}/bin
ant -DdestinationURL="imap://${ESCAPED_MAIL_USER}:${MAIL_PASSWORD}@${MAIL_HOST}:${IMAP_PORT}" populateMailbox
### populateMailbox for javamail suite - End ###

##### configRI.sh ends here #####
cd  ${TS_HOME}/bin
ant config.ri
ant enable.csiv2
##### configRI.sh ends here #####

##### addInteropCerts.sh starts here #####
cd ${TS_HOME}/bin
ant add.interop.certs
##### addInteropCerts.sh ends here #####

### restartRI.sh starts here #####
cd ${CTS_HOME}
export PORT=5858
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} -p ${PORT} stop-domain
${CTS_HOME}/ri/glassfish5/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} -p ${PORT} start-domain
### restartRI.sh ends here #####

### Registry server initialization starts here
if [ "jaxr" == ${test_suite} ];then
  if [ -f $JWSDP_HOME/bin/startup.sh ]; then
    $JWSDP_HOME/bin/startup.sh
    sleep 10
    echo "Java Web Services Developer Pack started ..."
  fi
fi
### Registry server initialization ends here

if [ "securityapi" == ${test_suite} ]; then
  cd $TS_HOME/bin;
  ant init.ldap
  echo "LDAP initilized for securityapi"
fi

### ctsStartStandardDeploymentServer.sh starts here #####
cd $TS_HOME/bin;
echo "ant start.auto.deployment.server > /tmp/deploy.out 2>&1 & "
ant start.auto.deployment.server > /tmp/deploy.out 2>&1 &
### ctsStartStandardDeploymentServer.sh ends here #####
 
cd $TS_HOME/bin
ant -f xml/impl/glassfish/s1as.xml run.cts -Dant.opts="${CTS_ANT_OPTS} ${ANT_OPTS}" -Dtest.areas="${test_suite}"

export HOST=`hostname -f`
TEST_SUITE=`echo "${test_suite}" | tr '/' '_'`
echo "1 ${TEST_SUITE} ${HOST}" > ${CTS_HOME}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${TS_HOME}/docker/JTReportParser/JTReportParser.jar ${CTS_HOME}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/
rm -f ${CTS_HOME}/args.txt

if [ -z ${vehicle} ];then
  RESULT_FILE_NAME=${TEST_SUITE}-results.tar.gz
else
  RESULT_FILE_NAME=${TEST_SUITE}_${vehicle_name}-results.tar.gz
fi
tar zcvf ${WORKSPACE}/${RESULT_FILE_NAME} ${JT_REPORT_DIR} ${JT_WORK_DIR} ${WORKSPACE}/results/junitreports/ ${CTS_HOME}/javaeetck/bin/ts.* ${CTS_HOME}/vi/glassfish5/glassfish/domains/domain1/logs/ ${CTS_HOME}/vi/glassfish5/glassfish/domains/domain1/config
