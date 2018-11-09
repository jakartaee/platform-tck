#!/bin/bash -xe

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

# Hudson log file location for archiving later.
LOGFILE=/log

export BASEDIR=${WORKSPACE}
if [ -z "${GF_HOME}" ]; then
  export GF_HOME=${WORKSPACE}
fi

which ant
ant -version

which java
java -version

if [ "false" == "${buildStandaloneTCKFlag}" ]; then
  echo "Not building standalone TCK"
  exit 0
fi

echo "########## Trunk.Install.V5 Config ##########"
# Install GF 5.0 - not latest //TODO update with latest gf link
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip -o latest-glassfish.zip

export ANT_OPTS="-Xmx2G -Djava.endorsed.dirs=${BASEDIR}/glassfish5/glassfish/modules/endorsed \
-Djavax.xml.accessExternalStylesheet=all \
-Djavax.xml.accessExternalSchema=all \
-Djavax.xml.accessExternalDTD=file,http,https"

export TCK_NAME="$@"
if [ -z "$TCK_NAME" ]; then
  echo "ERROR: Please provide atleast one TCK name or 'All' as argument to the script"
  exit 1
fi
echo "The option selected to build is $TCK_NAME TCK"

if [ "All" == "$TCK_NAME" ];then
  TCK_LIST=( jaxr jsonp jsonb jaxrs websocket el concurrency connector jacc jaspic caj jms jsp jstl jaxws saaj servlet jsf securityapi jaxrpc jpa jta )
else 
  TCK_LIST=( ${TCK_NAME} )
fi


################################################
### STANDALONE TCK BUNDLE BUILD - START
################################################
################################################
### STANDALONE TCK BUNDLE BUILD - START
################################################

DOC_SPECIFIC_PROPS=" "

for tck in ${TCK_LIST[@]}; do
  if [ "jaxr" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djwsdp.home=$GF_HOME/glassfish5/glassfish/ -Dts.classpath=$GF_HOME/glassfish5/glassfish/modules/endorsed/jaxb-api.jar:$GF_HOME/glassfish5/glassfish/modules/endorsed/webservices-api-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/javax.xml.registry-api.jar:$GF_HOME/glassfish5/glassfish/modules/webservices-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jaxb-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$BASEDIR/lib/javatest.jar:$BASEDIR/lib/tsharness.jar -Dall.test.dir=com/sun/ts/tests/jaxr,com/sun/ts/tests/signaturetest/jaxr -Dbuild.level=2"
  elif [ "jaxrpc" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dendorsed.dirs=$GF_HOME/glassfish5/glassfish/modules/endorsed -Dall.test.dir=com/sun/ts/tests/jaxrpc,com/sun/ts/tests/signaturetest/jaxrpc -Dbuild.level=2 -Dlocal.classes=$GF_HOME/glassfish5/glassfish/modules/webservices-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/javax.xml.rpc-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.mail.jar:$GF_HOME/glassfish5/glassfish/modules/jaxb-osgi.jar:$GF_HOME/modules/javax.ejb-api.jar"
    DOC_SPECIFIC_PROPS="-propertyfile $BASEDIR/install/jaxrpc/bin/build.properties -Dts.home=$BASEDIR -Ddeliverable.version=1.1 -Ddeliverable.class=com.sun.ts.lib.deliverable.jaxrpc.JAXRPCDeliverable"
  elif [ "jta" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djta.classes=$GF_HOME/glassfish5/glassfish/modules/javax.transaction-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar"
  elif [ "jsf" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djsf.classes=$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet.jsp.jstl-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.inject.jar:$GF_HOME/glassfish5/glassfish/modules/javax.faces.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet.jsp-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.el.jar"
  elif [ "jsonp" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djsonp.classes=$GF_HOME/glassfish5/glassfish/modules/javax.json-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.json.jar"
  elif [ "jsonb" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djsonb.classes=$GF_HOME/glassfish5/glassfish/modules/javax.json-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.json.jar:$GF_HOME/glassfish5/glassfish/modules/javax.json.jar:$GF_HOME/glassfish5/glassfish/modules/javax.inject.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar"
  elif [ "jaxrs" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djaxrs.classes=$GF_HOME/glassfish5/glassfish/modules/javax.json-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.json.jar:$GF_HOME/glassfish5/glassfish/modules/jsonp-jaxrs.jar:$GF_HOME/glassfish5/glassfish/modules/javax.ws.rs-api.jar:$GF_HOME/glassfish5/glassfish/modules/jsonp-jaxrs.jar:$GF_HOME/glassfish5/glassfish/modules/endorsed/javax.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.json.jar:$GF_HOME/glassfish5/glassfish/modules/javax.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.interceptor-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.inject.jar:$GF_HOME/glassfish5/glassfish/modules/validation-api.jar:$GF_HOME/glassfish5/glassfish/modules/bean-validator.jar"
  elif [ "websocket" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dwebsocket.classes=$GF_HOME/glassfish5/glassfish/modules/javax.websocket-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.inject.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar"
  elif [ "securityapi" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dsecurityapi.classes=$GF_HOME/glassfish5/glassfish/modules/endorsed/javax.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.inject.jar:$GF_HOME/glassfish5/glassfish/modules/javax.security.enterprise-api.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.faces.jar:$GF_HOME/glassfish5/glassfish/modules/javax.security.auth.message-api.jar:$BASEDIR/glassfish5/glassfish/modules/javax.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.interceptor-api.jar"
  elif [ "el" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Del.classes=$GF_HOME/glassfish5/glassfish/modules/javax.el.jar"
  elif [ "concurrency" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dconcurrency.classes=$GF_HOME/glassfish5/glassfish/modules/javax.enterprise.concurrent-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jta.jar:$GF_HOME/glassfish5/glassfish/modules/javax.enterprise.deploy-api.jar"
  elif [ "connector" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dconnector.home=$GF_HOME/glassfish5/glassfish/"
  elif [ "jacc" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djacc.home=$GF_HOME/glassfish5/glassfish/"
  elif [ "jaspic" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djaspic.home=$GF_HOME/glassfish5/glassfish/"
  elif [ "caj" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS=""
  elif [ "jms" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djms.classes=$GF_HOME/glassfish5/glassfish/modules/javax.jms-api.jar"
  elif [ "jsp" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-DwebServerHome=$GF_HOME/glassfish5/glassfish/"
  elif [ "jstl" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-DwebServerHome=$GF_HOME/glassfish5/glassfish/"
  elif [ "jaxws" == "$tck" ]
  then
     PROXY_HOST=`echo ${http_proxy} | cut -d: -f2 | sed -e 's/\/\///g'`
     PROXY_PORT=`echo ${http_proxy} | cut -d: -f3`
     WSIMPORT_JVMARG="-Djavax.xml.accessExternalSchema=all -Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT"
    sed -i "s/wsimport.jvmargs=.*/wsimport.jvmargs=$WSIMPORT_JVMARG/g" $BASEDIR/install/jaxws/bin/ts.jte
    sed -i "s/ri.wsimport.jvmargs=.*/ri.wsimport.jvmargs=$WSIMPORT_JVMARG/g" $BASEDIR/install/jaxws/bin/ts.jte
    cat $BASEDIR/install/jaxws/bin/ts.jte | grep wsimport.jvmargs
    TCK_SPECIFIC_PROPS="-Dwebcontainer.home=$BASEDIR/glassfish5/glassfish -Dwebcontainer.home.ri=$BASEDIR/glassfish5/glassfish -Ddeliverable.version=2.3"
    JAXWS_SPECIFIC_PROPS="-Dwebcontainer.home=$BASEDIR/glassfish5/glassfish -Dwebcontainer.home.ri=$BASEDIR/glassfish5/glassfish -Ddeliverable.version=2.3"
  elif [ "jpa" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djpa.classes=$GF_HOME/glassfish5/glassfish/modules/javax.persistence.jar"
  elif [ "saaj" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dlocal.classes=$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.ejb-api.jar -Dwebcontainer.home=$GF_HOME/glassfish5/glassfish -Dendorsed.dirs=$GF_HOME/glassfish5/glassfish/modules/endorsed"
  elif [ "servlet" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dservlet.classes=$GF_HOME/glassfish5/glassfish/modules/endorsed/javax.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar"
  else
    echo "TCK is not in the list"
  fi

  echo "########## Trunk.$tck Started##########"
  ant -f $BASEDIR/install/$tck/bin/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $JAXWS_SPECIFIC_PROPS clean.all build.all.jars 

  if [ "jaxrpc" == "$tck" ]
  then
    echo "Generating JAXRPC specific classes using wscompile"
    cp $BASEDIR/lib/ant-props.jar $ANT_HOME/lib
    sed -i "s#webserver\.home=.*#webserver.home=$GF_HOME/glassfish5/glassfish#g" $BASEDIR/install/$tck/bin/build.properties
    sed -i "s#jaxrpc\.tool=.*#jaxrpc.tool=$GF_HOME/glassfish5/glassfish/bin/wscompile#g" $BASEDIR/install/$tck/bin/build.properties
    cat $BASEDIR/install/$tck/bin/build.properties
    ant -f $BASEDIR/install/$tck/bin/build.jaxrpc.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $TCK_SPECIFIC_PROPS -Djava.endorsed.dirs=$BASEDIR/glassfish5/glassfish/modules/endorsed buildall
  else 
    ant -f $BASEDIR/install/$tck/bin/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $TCK_SPECIFIC_PROPS -Djava.endorsed.dirs=$BASEDIR/glassfish5/glassfish/modules/endorsed build.all 
  fi


  mkdir -p $BASEDIR/internal/docs/$tck
  cp $BASEDIR/internal/docs/dtd/*.dtd $BASEDIR/internal/docs/$tck/
  ant -f $BASEDIR/release/tools/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/release/tools $DOC_SPECIFIC_PROPS -Dskip.createbom="true" -Dskip.build="true" $TCK_SPECIFIC_PROPS $tck 
  echo "########## Trunk.$tck Completed ##########"

  # Copy build to archive path
  mkdir -p ${WORKSPACE}/standalone-bundles/
  UPPER_TCK=`echo "${tck}" | tr '[:lower:]' '[:upper:]'`
  cd ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/
  for entry in `ls *.zip`; do
    date=`echo "$entry" | cut -d_ -f2`
    strippedEntry=`echo "$entry" | cut -d_ -f1`
    echo "copying ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry to ${WORKSPACE}/standalone-bundles/${strippedEntry}_latest.zip"
    cp ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry ${WORKSPACE}/standalone-bundles/${strippedEntry}_latest.zip
  done
done
