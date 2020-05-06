#!/bin/bash -x

#
# Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
# Copyright (c) 2019 Payara Foundation and/or its affiliates. All rights reserved.
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
export TS_HOME=${CTS_HOME}/jakartaeetck/


if [ -z "${RI_JAVA_HOME}" ]; then
  export RI_JAVA_HOME=$JAVA_HOME
fi

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



printf  "
******************************************************
* Shutting down running Glassfish instances          *
******************************************************

"



echo "[killJava.sh] uname: LINUX"
echo "Pending process to be killed:"
ps -eaf | grep "com.sun.enterprise.admin.cli.AdminMain" | grep -v "grep" | grep -v "nohup" 
for i in `ps -eaf | grep "com.sun.enterprise.admin.cli.AdminMain" | grep -v "grep" | grep -v "nohup" | tr -s " " | cut -d" " -f2`
do
  echo "[killJava.sh] kill $i"
  kill $i
done


##### installCTS.sh starts here #####
cat ${TS_HOME}/bin/ts.jte | sed "s/-Doracle.jdbc.mapDateToTimestamp/-Doracle.jdbc.mapDateToTimestamp -Djava.security.manager/"  > ts.save
cp ts.save $TS_HOME/bin/ts.jte
##### installCTS.sh ends here #####



printf  "
******************************************************
* Installing CI/RI (Glassfish 5.1)                   *
******************************************************

"


##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  if [ -z "$DEFAULT_GF_BUNDLE_URL" ]; then
    echo "[ERROR] GF_BUNDLE_URL not set"
    exit 1
  else 
    echo "Using default url for GF bundle: $DEFAULT_GF_BUNDLE_URL"
    export GF_BUNDLE_URL=$DEFAULT_GF_BUNDLE_URL
  fi
fi
if [ -z "${OLD_GF_BUNDLE_URL}" ]; then
  export OLD_GF_BUNDLE_URL=$GF_BUNDLE_URL
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O ${CTS_HOME}/latest-glassfish.zip
if [[ "interop" == ${test_suite} ]]; then
  wget --progress=bar:force --no-cache $OLD_GF_BUNDLE_URL -O ${CTS_HOME}/glassfish-5.0.zip
fi
rm -Rf ${CTS_HOME}/ri
mkdir -p ${CTS_HOME}/ri
if [[ "interop" == ${test_suite} ]]; then
  unzip -q ${CTS_HOME}/glassfish-5.0.zip -d ${CTS_HOME}/ri
else
  unzip -q ${CTS_HOME}/latest-glassfish.zip -d ${CTS_HOME}/ri
fi
chmod -R 777 ${CTS_HOME}/ri

if [ ! -z "${RI_JAVA_HOME}" ]; then
 echo "AS_JAVA=${RI_JAVA_HOME}" >> ${CTS_HOME}/ri/glassfish5/glassfish/config/asenv.conf
fi

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
ps -eaf | grep "$RI_JAVA_HOME/bin/java" | grep -v "grep" | grep -v "nohup" 
for i in `ps -eaf | grep "$RI_JAVA_HOME/bin/java" | grep -v "grep" | grep -v "nohup" | tr -s " " | cut -d" " -f2`
do
  echo "[killJava.sh] kill -9 $i"
  kill -9 $i
done
##### installRI.sh ends here #####




printf  "
******************************************************
* Installing VI (Glassfish)                          *
******************************************************

"


##### installVI.sh starts here #####

if [ -z "${GF_VI_BUNDLE_URL}" ]; then
    echo "Using GF_BUNDLE_URL for GF VI bundle: $GF_BUNDLE_URL"
    export GF_VI_BUNDLE_URL=$GF_BUNDLE_URL
fi

if [ -z "${GF_VI_BUNDLE_URL}" ]; then
    echo "Using GF_BUNDLE_URL for GF VI bundle: $GF_BUNDLE_URL"
    export GF_VI_BUNDLE_URL=$GF_BUNDLE_URL
fi

if [ -z "${GF_VI_TOPLEVEL_DIR}" ]; then
    echo "Using glassfish5 for GF_VI_TOPLEVEL_DIR"
    export GF_VI_TOPLEVEL_DIR=glassfish5
fi

wget --progress=bar:force --no-cache $GF_VI_BUNDLE_URL -O ${CTS_HOME}/latest-glassfish-vi.zip

rm -Rf ${CTS_HOME}/vi
mkdir -p ${CTS_HOME}/vi
unzip -q ${CTS_HOME}/latest-glassfish-vi.zip -d ${CTS_HOME}/vi
chmod -R 777 ${CTS_HOME}/vi

if [ ! -d "${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR" ]; then
  echo "VI toplevel directory ${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR does not exists or is not a directory"
  exit 1
fi

if [[ $test_suite == ejb30/lite* ]] || [[ "ejb30" == $test_suite ]] ; then
  echo "Using higher JVM memory for EJB Lite suites to avoid OOM errors"
  sed -i 's/-Xmx512m/-Xmx4096m/g' ${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/domains/domain1/config/domain.xml
  sed -i 's/-Xmx1024m/-Xmx4096m/g' ${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/domains/domain1/config/domain.xml
  sed -i 's/-Xmx512m/-Xmx2048m/g' ${CTS_HOME}/ri/glassfish5/glassfish/domains/domain1/config/domain.xml
  sed -i 's/-Xmx1024m/-Xmx2048m/g' ${CTS_HOME}/ri/glassfish5/glassfish/domains/domain1/config/domain.xml
 
  # Change the memory setting in ts.jte as well.
  sed -i 's/-Xmx1024m/-Xmx4096m/g' ${TS_HOME}/bin/ts.jte
fi 

${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/bin/asadmin --user admin --passwordfile ${CTS_HOME}/change-admin-password.txt change-admin-password
${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} start-domain
${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} version
${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} create-jvm-options -Djava.security.manager
${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/bin/asadmin --user admin --passwordfile ${ADMIN_PASSWORD_FILE} stop-domain

sleep 5
echo "[killJava.sh] uname: LINUX"
echo "Pending process to be killed:"
ps -eaf | grep "$JAVA_HOME/bin/java" | grep -v "grep" | grep -v "nohup" 
for i in `ps -eaf | grep "$JAVA_HOME/bin/java" | grep -v "grep" | grep -v "nohup" | tr -s " " | cut -d" " -f2`
do
  echo "[killJava.sh] kill -9 $i"
  kill -9 $i
done
##### installVI.sh ends here #####

##### configVI.sh starts here #####

export CTS_ANT_OPTS="-Djava.endorsed.dirs=${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/modules/endorsed \
-Djavax.xml.accessExternalStylesheet=all \
-Djavax.xml.accessExternalSchema=all \
-Djavax.xml.accessExternalDTD=file,http"

if [[ "$PROFILE" == "web" || "$PROFILE" == "WEB" ]];then
  KEYWORDS="javaee_web_profile|jacc_web_profile|jaspic_web_profile|javamail_web_profile|connector_web_profile"
fi

if [ -z "${vehicle}" ];then
  echo "Vehicle not set. Running all vehichles"
else
  echo "Vehicle set. Running in vehicle: ${vehicle}"
  if [ -z "${KEYWORDS}" ]; then
    KEYWORDS=${vehicle}
  else
    KEYWORDS="(${KEYWORDS} & ${vehicle})"
  fi
fi

if [ ! -z "$KEYWORDS" ];then
  if [ ! -z "$USER_KEYWORDS" ]; then
    KEYWORDS="${KEYWORDS}${USER_KEYWORDS}"
  fi
else
  if [ ! -z "$USER_KEYWORDS" ]; then
    KEYWORDS="${USER_KEYWORDS}"
  fi
fi

if [ ! -z "${KEYWORDS}" ]; then
  CTS_ANT_OPTS="${CTS_ANT_OPTS} -Dkeywords=\"${KEYWORDS}\""
fi

echo "CTS_ANT_OPTS:${CTS_ANT_OPTS}"
echo "KEYWORDS:${KEYWORDS}"
		
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

sed -i "s#^javaee.home=.*#javaee.home=${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish#g" ts.jte
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

if [[ "$PROFILE" == "web" || "$PROFILE" == "WEB" ]]; then
  sed -i "s/^javaee.level=.*/javaee.level=web connector jaxws jaxb javamail javaeemgmt javaeedeploy jacc jaspic wsmd/g" ts.jte
  sed -i "s/^optional.tech.packages.to.ignore=.*/optional.tech.packages.to.ignore=javax.xml.rpc.handler/g" ts.jte
fi

sed -i 's/^impl.deploy.timeout.multiplier=.*/impl.deploy.timeout.multiplier=240/g' ts.jte
sed -i 's/^javatest.timeout.factor=.*/javatest.timeout.factor=2.0/g' ts.jte
sed -i 's/^test.ejb.stateful.timeout.wait.seconds=.*/test.ejb.stateful.timeout.wait.seconds=180/g' ts.jte
sed -i 's/^harness.log.traceflag=.*/harness.log.traceflag=false/g' ts.jte
sed -i 's/^impl\.deploy\.timeout\.multiplier=240/impl\.deploy\.timeout\.multiplier=480/g' ts.jte

if [ "servlet" == "${test_suite}" ]; then
  sed -i 's/s1as\.java\.endorsed\.dirs=.*/s1as.java.endorsed.dirs=\$\{endorsed.dirs\}\$\{pathsep\}\$\{ts.home\}\/endorsedlib/g' ts.jte
fi

if [ ! -z "${DATABASE}" ];then
  if [ "JavaDB" == "${DATABASE}" ]; then
    echo "Using the bundled JavaDB in GlassFish. No change in ts.jte required."
  else
    echo "Modifying DB related properties in ts.jte"
    ${TS_HOME}/docker/process_db_config.sh ${DATABASE} ${TS_HOME}
  fi
fi

VI_SERVER_POLICY_FILE=${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/domains/domain1/config/server.policy
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
ant config.vi.javadb
##### configVI.sh ends here #####

### populateMailbox for suites using mail server - Start ###
if [[ $test_suite == "javamail" || $test_suite == "samples" || $test_suite == "servlet" ]]; then
  ESCAPED_MAIL_USER=`echo ${MAIL_USER} | sed -e 's/@/%40/g'`
  cd  ${TS_HOME}/bin
  ant -DdestinationURL="imap://${ESCAPED_MAIL_USER}:${MAIL_PASSWORD}@${MAIL_HOST}:${IMAP_PORT}" populateMailbox
fi
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
if [[ "jaxr" == ${test_suite} ]]; then
  if [ -f $JWSDP_HOME/bin/startup.sh ]; then
    $JWSDP_HOME/bin/startup.sh
    sleep 10
    echo "Java Web Services Developer Pack started ..."
  fi
fi
### Registry server initialization ends here

if [[ "securityapi" == ${test_suite} ]]; then
  cd $TS_HOME/bin;
  ant init.ldap
  echo "LDAP initilized for securityapi"
fi

### ctsStartStandardDeploymentServer.sh starts here #####
cd $TS_HOME/bin;
echo "ant start.auto.deployment.server > /tmp/deploy.out 2>&1 & "
ant start.auto.deployment.server > /tmp/deploy.out 2>&1 &
### ctsStartStandardDeploymentServer.sh ends here #####

cd $TS_HOME/bin;
if [ -z "$KEYWORDS" ]; then
  if [[ "jbatch" == ${test_suite} ]]; then
    cd $TS_HOME/src/com/ibm/jbatch/tck;
    ant runclient -Dwork.dir=${JT_WORK_DIR}/jbatch -Dreport.dir=${JT_REPORT_DIR}/jbatch;
  else
    ant -f xml/impl/glassfish/s1as.xml run.cts -Dant.opts="${CTS_ANT_OPTS} ${ANT_OPTS}" -Dtest.areas="${test_suite}"
  fi
else
  if [[ "jbatch" == ${test_suite} ]]; then
    cd $TS_HOME/src/com/ibm/jbatch/tck;
    ant runclient -Dkeywords=\"${KEYWORDS}\" -Dwork.dir=${JT_WORK_DIR}/jbatch -Dreport.dir=${JT_REPORT_DIR}/jbatch;
  else
    ant -f xml/impl/glassfish/s1as.xml run.cts -Dkeywords=\"${KEYWORDS}\" -Dant.opts="${CTS_ANT_OPTS} ${ANT_OPTS}" -Dtest.areas="${test_suite}"
  fi
fi


cd $TS_HOME/bin;
# Check if there are any failures in the test. If so, re-run those tests.
FAILED_COUNT=0
ERROR_COUNT=0
TEST_SUITE=`echo "${test_suite}" | tr '/' '_'`
FAILED_COUNT=`cat ${JT_REPORT_DIR}/${TEST_SUITE}/text/summary.txt | grep 'Failed.' | wc -l`
ERROR_COUNT=`cat ${JT_REPORT_DIR}/${TEST_SUITE}/text/summary.txt | grep 'Error.' | wc -l`
if [[ $FAILED_COUNT -gt 0 || $ERROR_COUNT -gt 0 ]]; then
  echo "One or more tests failed. Failure count:$FAILED_COUNT/Error count:$ERROR_COUNT"
  echo "Re-running only the failed, error tests"
if [ -z "$KEYWORDS" ]; then
  if [[ "jbatch" == ${test_suite} ]]; then
    cd $TS_HOME/src/com/ibm/jbatch/tck;
    ant runclient -DpriorStatus=fail -Dwork.dir=${JT_WORK_DIR}/jbatch -Dreport.dir=${JT_REPORT_DIR}/jbatch
  else
    ant -f xml/impl/glassfish/s1as.xml run.cts -Dant.opts="${CTS_ANT_OPTS} ${ANT_OPTS}" -Drun.client.args="-DpriorStatus=fail,error"  -DbuildJwsJaxws=false -Dtest.areas="${test_suite}"
  fi
else
  if [[ "jbatch" == ${test_suite} ]]; then
    cd $TS_HOME/src/com/ibm/jbatch/tck;
    ant runclient -DpriorStatus=fail -Dkeywords=\"${KEYWORDS}\" -Dwork.dir=${JT_WORK_DIR}/jbatch -Dreport.dir=${JT_REPORT_DIR}/jbatch;
  else
    ant -f xml/impl/glassfish/s1as.xml run.cts -Dkeywords=\"${KEYWORDS}\" -Dant.opts="${CTS_ANT_OPTS} ${ANT_OPTS}" -Drun.client.args="-DpriorStatus=fail,error"  -DbuildJwsJaxws=false -Dtest.areas="${test_suite}"
  fi
fi
  # Generate combined report for both the runs.
if [[ "jbatch" == ${test_suite} ]]; then
  ant -Dreport.for=com/ibm/jbatch/tck -Dwork.dir=${JT_WORK_DIR}/jbatch -Dreport.dir=${JT_REPORT_DIR}/jbatch report
else  
  ant -Dreport.for=com/sun/ts/tests/$test_suite -Dreport.dir=${JT_REPORT_DIR}/${TEST_SUITE} -Dwork.dir=${JT_WORK_DIR}/${TEST_SUITE} report
fi

fi

export HOST=`hostname -f`
echo "1 ${TEST_SUITE} ${HOST}" > ${CTS_HOME}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${TS_HOME}/docker/JTReportParser/JTReportParser.jar ${CTS_HOME}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/
rm -f ${CTS_HOME}/args.txt

if [ -z ${vehicle} ];then
  RESULT_FILE_NAME=${TEST_SUITE}-results.tar.gz
else
  RESULT_FILE_NAME=${TEST_SUITE}_${vehicle_name}-results.tar.gz
  sed -i "s/name=\"${TEST_SUITE}\"/name=\"${TEST_SUITE}_${vehicle_name}\"/g" ${WORKSPACE}/results/junitreports/${TEST_SUITE}-junit-report.xml
  mv ${WORKSPACE}/results/junitreports/${TEST_SUITE}-junit-report.xml  ${WORKSPACE}/results/junitreports/${TEST_SUITE}_${vehicle_name}-junit-report.xml
fi
tar zcvf ${WORKSPACE}/${RESULT_FILE_NAME} ${CTS_HOME}/*.log ${JT_REPORT_DIR} ${JT_WORK_DIR} ${WORKSPACE}/results/junitreports/ ${CTS_HOME}/jakartaeetck/bin/ts.* ${CTS_HOME}/vi/$GF_VI_TOPLEVEL_DIR/glassfish/domains/domain1/ ${CTS_HOME}/ri/$GF_VI_TOPLEVEL_DIR/glassfish/domains/domain1/

if [ -z ${vehicle} ];then
  JUNIT_REPORT_FILE_NAME=${TEST_SUITE}-junitreports.tar.gz
else
  JUNIT_REPORT_FILE_NAME=${TEST_SUITE}_${vehicle_name}-junitreports.tar.gz
fi
tar zcvf ${WORKSPACE}/${JUNIT_REPORT_FILE_NAME} ${WORKSPACE}/results/junitreports/
