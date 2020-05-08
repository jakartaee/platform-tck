#!/bin/bash -xe

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

if [ ! -d "$GF_HOME/glassfish5" ]; then
  echo "########## Trunk.Install.V5 Config ##########"
  wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
  unzip -q -o latest-glassfish.zip
fi

if [ ! -z "$GF_VERSION_URL" ]; then
  wget --progress=bar:force --no-cache $GF_VERSION_URL -O glassfish.version
  cat glassfish.version
fi

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

DOC_SPECIFIC_PROPS=""
JAXWS_SPECIFIC_PROPS=""

#Generate Version file
GIT_HASH=`git rev-parse HEAD`
GIT_BRANCH=`git branch | awk '{print $2}'`
BUILD_DATE=`date`
echo "Git Revision: ${GIT_HASH}" >> ${WORKSPACE}/version.info
echo "Git Branch: ${GIT_BRANCH}" >> ${WORKSPACE}/version.info
echo "Build Date: ${BUILD_DATE}" >> ${WORKSPACE}/version.info

#temporary fix to get build passing until we have glassfish with new api jars
wget --progress=bar:force --no-cache \
     https://repo1.maven.org/maven2/jakarta/platform/jakarta.jakartaee-api/9.0.0-RC1/jakarta.jakartaee-api-9.0.0-RC1.jar \
     -O $GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar

if [ ! -z "$TCK_BUNDLE_BASE_URL" ]; then
  mkdir -p ${WORKSPACE}/standalone-bundles/
  IFS=' ' # space is set as delimiter
  read -ra FILESLIST <<< "$STANDALONE_TCK_BUNDLES_FILE_NAME_LIST" 
  for i in "${FILESLIST[@]}"; do
    filename="$(echo -e "${i}" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
    echo "Skipping build and using pre-build binary TCK bundle: ${TCK_BUNDLE_BASE_URL}/${filename}"
    wget  --progress=bar:force --no-cache ${TCK_BUNDLE_BASE_URL}/$filename -O ${WORKSPACE}/standalone-bundles/$filename
  done
  #for tck in ${TCK_LIST[@]}; do
  #  echo "Skipping build and using pre-build binary TCK bundle: ${TCK_BUNDLE_BASE_URL}/${tck}tck"
  #  wget  --progress=bar:force --no-cache ${TCK_BUNDLE_BASE_URL} -P ${WORKSPACE}/standalone-bundles/ -A "${tck}tck*_latest.zip"
  #done
  exit 0
fi

for tck in ${TCK_LIST[@]}; do
  if [ "jaxr" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djwsdp.home=$GF_HOME/glassfish5/glassfish/ -Dts.classpath=$GF_HOME/glassfish5/glassfish/modules/endorsed/jakarta.xml.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/endorsed/webservices-api-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.xml.registry-api.jar:$GF_HOME/glassfish5/glassfish/modules/webservices-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jaxb-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$BASEDIR/lib/javatest.jar:$BASEDIR/lib/tsharness.jar -Dall.test.dir=com/sun/ts/tests/jaxr,com/sun/ts/tests/signaturetest/jaxr -Dbuild.level=2"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jaxrpc" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dendorsed.dirs=$GF_HOME/glassfish5/glassfish/modules/endorsed -Dall.test.dir=com/sun/ts/tests/jaxrpc,com/sun/ts/tests/signaturetest/jaxrpc -Dbuild.level=2 -Dlocal.classes=$GF_HOME/glassfish5/glassfish/modules/webservices-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.xml.rpc-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.mail.jar:$GF_HOME/glassfish5/glassfish/modules/jaxb-osgi.jar:$GF_HOME/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS="-propertyfile $BASEDIR/install/jaxrpc/bin/build.properties -Dts.home=$BASEDIR -Ddeliverable.version=1.1 -Ddeliverable.class=com.sun.ts.lib.deliverable.jaxrpc.JAXRPCDeliverable"
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jta" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djta.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.transaction-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jsf" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djsf.classes=$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet.jsp.jstl-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.faces.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet.jsp-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.el.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jsonp" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djsonp.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jsonb" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djsonb.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jaxrs" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djaxrs.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jsonp-jaxrs.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ws.rs-api.jar:$GF_HOME/glassfish5/glassfish/modules/jsonp-jaxrs.jar:$GF_HOME/glassfish5/glassfish/modules/endorsed/jakarta.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.interceptor-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/validation-api.jar:$GF_HOME/glassfish5/glassfish/modules/bean-validator.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    sed -i "s#impl\.vi\.deploy\.dir=.*#impl.vi.deploy.dir=$GF_HOME/glassfish5/glassfish/domains/domain1/autodeploy#g" $BASEDIR/install/$tck/bin/ts.jte
    sed -i "s#impl\.vi=.*#impl.vi=glassfish#g" $BASEDIR/install/$tck/bin/ts.jte
    sed -i "s#jaxrs_impl_lib=.*#jaxrs_impl_lib=$GF_HOME/glassfish5/glassfish/modules/jersey-container-servlet-core.jar#g" $BASEDIR/install/$tck/bin/ts.jte
    sed -i "s#jaxrs_impl_name=.*#jaxrs_impl_name=jersey#g" $BASEDIR/install/$tck/bin/ts.jte
    sed -i 's#servlet_adaptor=.*#servlet_adaptor=org\/glassfish\/jersey\/servlet\/ServletContainer.class#g' $BASEDIR/install/$tck/bin/ts.jte
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "websocket" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dwebsocket.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.websocket-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "securityapi" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dsecurityapi.classes=$GF_HOME/glassfish5/glassfish/modules/endorsed/jakarta.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.security.enterprise-api.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.faces.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.security.auth.message-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.interceptor-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "el" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Del.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.el.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "concurrency" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dconcurrency.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.enterprise.concurrent-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jta.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.enterprise.deploy-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "connector" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dconnector.home=$GF_HOME/glassfish5/glassfish/"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jacc" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djacc.home=$GF_HOME/glassfish5/glassfish/ -Djacc.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.jms-api.jar:$GF_HOME/glassfish5/glassfish/modules/security.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.security.jacc-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.persistence.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.interceptor-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.mail.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.transaction-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet.jsp-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jaspic" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djaspic.home=$GF_HOME/glassfish5/glassfish/"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "caj" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS=""
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jms" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djms.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.jms-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jsp" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-DwebServerHome=$GF_HOME/glassfish5/glassfish/"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jstl" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-DwebServerHome=$GF_HOME/glassfish5/glassfish/"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jaxws" == "$tck" ]
  then
    # temporary fix to get jaxws build passing until we have glassfish with new api jars
    . ${WORKSPACE}/docker/build_jaxws-inc.sh

    TCK_SPECIFIC_PROPS="-Dwebcontainer.home=$BASEDIR/glassfish5/glassfish -Dwebcontainer.home.ri=$BASEDIR/glassfish5/glassfish -Ddeliverable.version=2.3"
    JAXWS_SPECIFIC_PROPS="-Dwebcontainer.home=$BASEDIR/glassfish5/glassfish -Dwebcontainer.home.ri=$BASEDIR/glassfish5/glassfish -Ddeliverable.version=2.3"
    DOC_SPECIFIC_PROPS=""
  elif [ "jpa" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djpa.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.persistence.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "saaj" == "$tck" ]
  then
    wget --progress=bar:force --no-cache https://repo1.maven.org/maven2/jakarta/xml/soap/jakarta.xml.soap-api/2.0.0-RC3/jakarta.xml.soap-api-2.0.0-RC3.jar -O ${GF_HOME}/glassfish5/glassfish/modules/jakarta.xml.soap-api.jar
    TCK_SPECIFIC_PROPS="-Dwebcontainer.home=$GF_HOME/glassfish5/glassfish -Dendorsed.dirs=$GF_HOME/glassfish5/glassfish/modules/endorsed"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "servlet" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dservlet.classes=$GF_HOME/glassfish5/glassfish/modules/endorsed/jakarta.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  else
    echo "TCK is not in the list"
  fi

  echo "########## Trunk.$tck Started##########"
  ant -f $BASEDIR/install/$tck/bin/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $TCK_SPECIFIC_PROPS $JAXWS_SPECIFIC_PROPS clean.all build.all.jars 

  if [ "jaxrpc" == "$tck" ]
  then
    echo "Generating JAXRPC specific classes using wscompile"
    # Copy additional ant library required for nested property handling in jaxrpc build
    if [[ -f $BASEDIR/lib/ant-props.jar && ! -f $ANT_HOME/lib/ant-props.jar ]]; then
      cp $BASEDIR/lib/ant-props.jar $ANT_HOME/lib
    fi
    sed -i "s#webserver\.home=.*#webserver.home=$GF_HOME/glassfish5/glassfish#g" $BASEDIR/install/$tck/bin/build.properties
    sed -i "s#jaxrpc\.tool=.*#jaxrpc.tool=$GF_HOME/glassfish5/glassfish/bin/wscompile#g" $BASEDIR/install/$tck/bin/build.properties
    cat $BASEDIR/install/$tck/bin/build.properties
    ant -f $BASEDIR/install/$tck/bin/build.jaxrpc.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $TCK_SPECIFIC_PROPS -Djava.endorsed.dirs=$BASEDIR/glassfish5/glassfish/modules/endorsed buildall
  else 
    ant -f $BASEDIR/install/$tck/bin/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $TCK_SPECIFIC_PROPS $JAXWS_SPECIFIC_PROPS -Djava.endorsed.dirs=$BASEDIR/glassfish5/glassfish/modules/endorsed build.all 
  fi

  if [ "jaxrs" == "$tck" ]; then
    ant -f $BASEDIR/install/$tck/bin/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $TCK_SPECIFIC_PROPS update.jaxrs.wars
  fi

  mkdir -p $BASEDIR/internal/docs/$tck
  cp $BASEDIR/internal/docs/dtd/*.dtd $BASEDIR/internal/docs/$tck/
  if [[ "$LICENSE" == "EFTL" || "$LICENSE" == "eftl" ]]; then
    ant -f $BASEDIR/release/tools/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/release/tools $DOC_SPECIFIC_PROPS -Dskip.createbom="true" -Dskip.build="true" $TCK_SPECIFIC_PROPS -DuseEFTLicensefile="true" $tck
  else
    ant -f $BASEDIR/release/tools/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/release/tools $DOC_SPECIFIC_PROPS -Dskip.createbom="true" -Dskip.build="true" $TCK_SPECIFIC_PROPS $tck
  fi 
  echo "########## Trunk.$tck Completed ##########"

  # Copy build to archive path
  mkdir -p ${WORKSPACE}/standalone-bundles/
  UPPER_TCK=`echo "${tck}" | tr '[:lower:]' '[:upper:]'`
  cd ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/
  for entry in `ls *.zip`; do
    date=`echo "$entry" | cut -d_ -f2`
    strippedEntry=`echo "$entry" | cut -d_ -f1`
    if [ "$strippedEntry" == "excludelist" ]; then
        strippedEntry=${strippedEntry}_`echo "$entry" | cut -d_ -f2`
    fi
    echo "copying ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry to ${WORKSPACE}/standalone-bundles/${strippedEntry}_latest.zip"
    if [[ "$LICENSE" == "EFTL" || "$LICENSE" == "eftl" ]]; then
      echo "copying ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry to ${WORKSPACE}/standalone-bundles/eclipse-${strippedEntry}.zip"
      cp ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry ${WORKSPACE}/standalone-bundles/eclipse-${strippedEntry}.zip
    else
      echo "copying ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry to ${WORKSPACE}/standalone-bundles/${strippedEntry}.zip"
      cp ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry ${WORKSPACE}/standalone-bundles/${strippedEntry}.zip
    fi
    cp ${WORKSPACE}/version.info ${WORKSPACE}/${strippedEntry}.version
  done
done


