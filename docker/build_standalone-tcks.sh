#!/bin/bash -xe

# Copyright (c) 2018, 2023 Oracle and/or its affiliates. All rights reserved.
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

if [ -z "$WORKSPACE" ]; then
  export WORKSPACE=`pwd`
fi

export BASEDIR=${WORKSPACE}
if [ -z "${GF_HOME}" ]; then
  export GF_HOME=${WORKSPACE}
fi

if [ -z "$JAKARTA_JARS" ]; then
  export JAKARTA_JARS=$BASEDIR
fi


cd $WORKSPACE

wget https://download.java.net/java/early_access/jdk21/15/GPL/openjdk-21-ea+15_linux-x64_bin.tar.gz -O jdk-21.tar.gz
tar -xvf jdk-21.tar.gz
export JAVA_HOME=$WORKSPACE/jdk-21

export PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH


which ant
ant -version

which java
java -version

if [ "false" == "${buildStandaloneTCKFlag}" ]; then
  echo "Not building standalone TCK"
  exit 0
fi

if [ -z "$GF_TOPLEVEL_DIR" ]; then
  export GF_TOPLEVEL_DIR=glassfish7
fi

if [ ! -d "$GF_HOME/$GF_TOPLEVEL_DIR" ]; then
  echo "########## Trunk.Install.V5 Config ##########"
  wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
  unzip -q -o latest-glassfish.zip
fi

if [ ! -z "$GF_VERSION_URL" ]; then
  wget --progress=bar:force --no-cache $GF_VERSION_URL -O glassfish.version
  cat glassfish.version
fi
export ANT_OPTS="-Xmx2G -Djavax.xml.accessExternalStylesheet=all \
-Djavax.xml.accessExternalSchema=all \
-DenableExternalEntityProcessing=true \
-Djavax.xml.accessExternalDTD=file,http,https"

export TCK_NAME="$@"
if [ -z "$TCK_NAME" ]; then
  echo "ERROR: Please provide atleast one TCK name or 'All' as argument to the script"
  exit 1
fi
echo "The option selected to build is $TCK_NAME TCK"

if [ "All" == "$TCK_NAME" ];then
  TCK_LIST=( websocket el connector jacc caj jms jsp jstl jaxws saaj servlet jpa jta )
else 
  TCK_LIST=( ${TCK_NAME} )
fi

cd $BASEDIR

mkdir -p $JAKARTA_JARS/modules

mvn -f $BASEDIR/docker/pom.xml -Pstaging dependency:copy-dependencies -DoutputDirectory="${JAKARTA_JARS}/modules" -Dmdep.stripVersion=true

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

RMI_CLASSES="-Drmi.classes=$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar"

for tck in ${TCK_LIST[@]}; do
  if [ "jta" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djta.classes=$JAKARTA_JARS/modules/jakarta.transaction-api.jar:$JAKARTA_JARS/modules/jakarta.servlet-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "websocket" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dwebsocket.classes=$JAKARTA_JARS/modules/jakarta.websocket-client-api.jar:$JAKARTA_JARS/modules/jakarta.websocket-api.jar:$JAKARTA_JARS/modules/jakarta.servlet-api.jar:$JAKARTA_JARS/modules/jakarta.inject-api.jar:$JAKARTA_JARS/modules/jakarta.enterprise.cdi-api.jar:$JAKARTA_JARS/modules/jakarta.activation-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar:$JAKARTA_JARS/modules/jakarta.xml.bind-api.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "el" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Del.classes=$JAKARTA_JARS/modules/jakarta.el-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "connector" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dconnector.home=$GF_HOME/$GF_TOPLEVEL_DIR/glassfish/"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jacc" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djacc.home=$GF_HOME/$GF_TOPLEVEL_DIR/glassfish/ -Djacc.classes=$JAKARTA_JARS/modules/jakarta.jms-api.jar:$JAKARTA_JARS/modules/jakarta.annotation-api.jar:$JAKARTA_JARS/modules/security.jar:$JAKARTA_JARS/modules/jakarta.servlet-api.jar:$JAKARTA_JARS/modules/jakarta.authorization-api.jar:$JAKARTA_JARS/modules/jakarta.ejb-api.jar:$JAKARTA_JARS/modules/jakarta.persistence-api.jar:$JAKARTA_JARS/modules/jakarta.interceptor-api.jar:$JAKARTA_JARS/modules/jakarta.mail-api.jar:$JAKARTA_JARS/modules/jakarta.transaction-api.jar:$JAKARTA_JARS/modules/jakarta.servlet.jsp-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "caj" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dlocal.classes=$JAKARTA_JARS/modules/jakarta.annotation-api.jar:$JAKARTA_JARS/modules/jakarta.transaction-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jms" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djms.classes=$JAKARTA_JARS/modules/jakarta.jms-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jsp" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-DwebServerHome=$GF_HOME/$GF_TOPLEVEL_DIR/glassfish/"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jstl" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-DwebServerHome=$GF_HOME/$GF_TOPLEVEL_DIR/glassfish/"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "jaxws" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dwebcontainer.home=$BASEDIR/$GF_TOPLEVEL_DIR/glassfish -Dwebcontainer.home.ri=$BASEDIR/$GF_TOPLEVEL_DIR/glassfish -Ddeliverable.version=3.0"
    JAXWS_SPECIFIC_PROPS="-Dwebcontainer.home=$BASEDIR/$GF_TOPLEVEL_DIR/glassfish -Dwebcontainer.home.ri=$BASEDIR/$GF_TOPLEVEL_DIR/glassfish -Ddeliverable.version=3.0"
    DOC_SPECIFIC_PROPS=""
  elif [ "jpa" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Djpa.classes=$JAKARTA_JARS/modules/jakarta.persistence-api.jar:$JAKARTA_JARS/modules/jakarta.annotation-api.jar:$JAKARTA_JARS/modules/jakarta.transaction-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "saaj" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dlocal.classes=$JAKARTA_JARS/modules/webservices-osgi.jar:$JAKARTA_JARS/modules/webservices-api-osgi.jar:$JAKARTA_JARS/modules/jakarta.activation-api.jar:$JAKARTA_JARS/modules/jakarta.servlet-api.jar:$JAKARTA_JARS/modules/jakarta.ejb-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar -Dwebcontainer.home=$GF_HOME/$GF_TOPLEVEL_DIR/glassfish"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  elif [ "servlet" == "$tck" ]
  then
    TCK_SPECIFIC_PROPS="-Dservlet.classes=$JAKARTA_JARS/modules/jakarta.annotation-api.jar:$JAKARTA_JARS/modules/jakarta.servlet-api.jar:$JAKARTA_JARS/modules/jakarta.activation-api.jar:$JAKARTA_JARS/modules/glassfish-corba-omgapi.jar"
    DOC_SPECIFIC_PROPS=""
    JAXWS_SPECIFIC_PROPS=""
  else
    echo "TCK is not in the list"
  fi

  echo "########## Trunk.$tck Started##########"
  ant -f $BASEDIR/install/$tck/bin/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $RMI_CLASSES $TCK_SPECIFIC_PROPS $JAXWS_SPECIFIC_PROPS clean.all build.all.jars 

  ant -f $BASEDIR/install/$tck/bin/build.xml -Ddeliverabledir=$tck -Dbasedir=$BASEDIR/install/$tck/bin $RMI_CLASSES $TCK_SPECIFIC_PROPS $JAXWS_SPECIFIC_PROPS build.all 

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
      echo "copying ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry to ${WORKSPACE}/standalone-bundles/jakarta-${strippedEntry}.zip"
      cp ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry ${WORKSPACE}/standalone-bundles/jakarta-${strippedEntry}.zip
    else
      echo "copying ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry to ${WORKSPACE}/standalone-bundles/${strippedEntry}.zip"
      cp ${WORKSPACE}/release/${UPPER_TCK}_BUILD/latest/$entry ${WORKSPACE}/standalone-bundles/${strippedEntry}.zip
    fi
    cp ${WORKSPACE}/version.info ${WORKSPACE}/${strippedEntry}.version
  done
done


