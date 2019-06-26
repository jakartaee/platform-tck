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
echo "TCK_HOME in jaxrtck.sh $TCK_HOME"
echo "ANT_HOME in jaxrtck.sh $ANT_HOME"
echo "PATH in jaxrtck.sh $PATH"
echo "ANT_OPTS in jaxrtck.sh $ANT_OPTS"

### Registry server initialization starts here
cd /opt/jwsdp-1.3/bin
./startup.sh
sleep 10
echo "Java Web Services Developer Pack started ..."
### Registry server initialization ends here


cd $TCK_HOME
if [ -f "${WORKSPACE}/standalone-bundles/jaxrtck-1.0_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
elif [ -f "${WORKSPACE}/standalone-bundles/eclipse-jaxrtck-1.0_latest.zip" ];then
  echo "Using stashed eclipse bundle created during the build phase"
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi
unzip ${WORKSPACE}/standalone-bundles/*jaxrtck-1.0_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/jaxrtck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^registryURL =.*#registryURL=http://localhost:8080/RegistryServer/#g" ts.jte
sed -i "s#^queryManagerURL =.*#queryManagerURL=http://localhost:8080/RegistryServer/#g" ts.jte
sed -i "s#^jaxrUser=.*#jaxrUser=testuser#g" ts.jte
sed -i "s#^jaxrPassword=.*#jaxrPassword=testuser#g" ts.jte
sed -i "s#^jaxrUser2=.*#jaxrUser2=jaxr-sqe#g" ts.jte
sed -i "s#^jaxrPassword2=.*#jaxrPassword2=jaxrsqe#g" ts.jte
sed -i "s#^ts_home=.*#ts_home=$TS_HOME/#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jaxrtckreport/jaxrtck#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jaxrtckwork/jaxrtck#g" ts.jte

sed -i "s#^jwsdp\.home=.*#jwsdp.home=$TCK_HOME/glassfish5/glassfish/#g" $TS_HOME/bin/build.properties
if [ ! -z "$TCK_BUNDLE_BASE_URL" ]; then
  sed -i 's/javax.xml.registry-api.jar/jakarta.xml.registry-api.jar/g' $TS_HOME/bin/build.properties
  sed -i 's/javax.xml.bind-api.jar/jakarta.xml.bind-api.jar/g' $TS_HOME/bin/build.properties
fi

echo "ts.home=$TS_HOME" >>$TS_HOME/bin/build.properties

GF_HOME=$TCK_HOME/glassfish5/glassfish/

mkdir -p $TCK_HOME/jaxrtckreport/jaxrtck
mkdir -p $TCK_HOME/jaxrtckwork/jaxrtck-sig
mkdir -p $TCK_HOME/jaxrtckreport/jaxrtck-sig
mkdir -p $TCK_HOME/jaxrtckwork/jaxrtck-sig

cd $TS_HOME/src/com/sun/ts/tests/jaxr/
ant -Dts.home=$TS_HOME -Djwsdp.home=$TCK_HOME/glassfish5/glassfish -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/jaxrtckreport/jaxrtck -Dwork.dir=$TCK_HOME/jaxrtckwork/jaxrtck runclient
cd $TS_HOME/src/com/sun/ts/tests/signaturetest/jaxr/
ant -Dts.home=$TS_HOME -Djwsdp.home=$TCK_HOME/glassfish5/glassfish -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/jaxrtckreport/jaxrtck-sig -Dwork.dir=$TCK_HOME/jaxrtckwork/jaxrtck-sig runclient
echo "Test run complete"

TCK_NAME=jaxrtck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
echo "2 ${TCK_NAME}-sig ${HOST}" >> ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
