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
echo "TCK_HOME in cajtck.sh $TCK_HOME"
echo "ANT_HOME in cajtck.sh $ANT_HOME"
echo "PATH in cajtck.sh $PATH"
echo "ANT_OPTS in cajtck.sh $ANT_OPTS"

HOST=`hostname`

cd $TCK_HOME

if ls ${WORKSPACE}/standalone-bundles/*jaxwstck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jaxwstck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*jaxwstck*.zip -d ${TCK_HOME}
  TCK_NAME=jaxwstck
elif ls ${WORKSPACE}/standalone-bundles/*xml-ws-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for xml-ws-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*xml-ws-tck*.zip -d ${TCK_HOME}
  TCK_NAME=xml-ws-tck
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
mkdir $TCK_HOME/ri

sed -i "s#^webcontainer\.home=.*#webcontainer.home=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#webcontainer\.home\.ri=.*#webcontainer.home.ri=$TCK_HOME/ri/glassfish5/glassfish#g" ts.jte
sed -i 's#webServerHost\.2=.*#webServerHost.2=localhost#g' ts.jte
sed -i 's#webServerPort\.2=.*#webServerPort.2=9080#g' ts.jte
sed -i 's#wsgen.ant.classname=.*#wsgen.ant.classname=com.sun.tools.ws.ant.WsGen#g' ts.jte
sed -i 's#wsimport.ant.classname=.*#wsimport.ant.classname=com.sun.tools.ws.ant.WsImport#g' ts.jte
sed -i "s#glassfish.admin.port.ri=.*#glassfish.admin.port.ri=5858#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

export ANT_OPTS="-Djava.endorsed.dirs=$TCK_HOME/glassfish5/glassfish/modules/endorsed -Djavax.xml.accessExternalStylesheet=all -Djavax.xml.accessExternalSchema=all -Djavax.xml.accessExternalDTD=file,http,https"

cd $TCK_HOME/vi/glassfish5/glassfish/bin
./asadmin start-domain
cd $TS_HOME/bin
ant config.vi

RI_DOMAIN_CONFIG_FILE=$TCK_HOME/ri/glassfish5/glassfish/domains/domain1/config/domain.xml
rm -rf $TCK_HOME/ri/*

# TODO : Web Profile 
# cp $TCK_HOME/latest-glassfish-$PROFILE.zip $BASEDIR/ri/latest-glassfish.zip
cp $TCK_HOME/latest-glassfish.zip $TCK_HOME/ri/latest-glassfish.zip
cd $TCK_HOME/ri
unzip -q latest-glassfish.zip

sed -i 's/4848/5858/g' $RI_DOMAIN_CONFIG_FILE
sed -i 's/8080/9080/g' $RI_DOMAIN_CONFIG_FILE
sed -i 's/8181/9181/g' $RI_DOMAIN_CONFIG_FILE

cd $TCK_HOME/ri/glassfish5/glassfish/bin
./asadmin start-domain
cd $TS_HOME/bin
ant config.ri

cd $TCK_HOME/vi/glassfish5/glassfish/bin
./asadmin stop-domain
./asadmin start-domain

cd $TCK_HOME/ri/glassfish5/glassfish/bin
./asadmin stop-domain
./asadmin start-domain

cd $TS_HOME/src/com/sun/ts/tests/jaxws
ant -Dkeywords=all -Dbuild.vi=true build
cd $TS_HOME/bin
ant -Dkeywords=all deploy.all
ant -Dkeywords=all run.all 

#run sigtest
#cd $TS_HOME/src/com/sun/ts/tests/signaturetest
#ant -Dreport.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}-sig -Dwork.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}-sig runclient


JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${WORKSPACE}/*.log ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
