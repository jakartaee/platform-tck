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

if [ -z "$ANT_HOME" ]; then
  export ANT_HOME=/usr/share/ant/
fi

if [ -z "$JAVA_HOME" ]; then
  export JAVA_HOME=/opt/jdk1.8.0_171
fi

export PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH

cd $WORKSPACE
export BASEDIR=`pwd`

if [ -z "$GF_HOME" ]; then
  export GF_HOME=$BASEDIR
fi

if [ ! -z "$TCK_BUNDLE_BASE_URL" ]; then
   echo "Skipping build and using pre-build binary jakartaeetck bundle: $TCK_BUNDLE_BASE_URL/$TCK_BUNDLE_FILE_NAME"
   mkdir -p ${WORKSPACE}/jakartaeetck-bundles
   wget  --progress=bar:force --no-cache ${TCK_BUNDLE_BASE_URL}/$TCK_BUNDLE_FILE_NAME -O ${WORKSPACE}/jakartaeetck-bundles/jakartaeetck.zip
   # Check if the bundle is from Oracle Java EE CTS project. If so add scripts required for execution.
   unzip -l ${WORKSPACE}/jakartaeetck-bundles/jakartaeetck.zip | grep -q jakartaeetck/docker/run_jakartaeetck.sh;
   if [ "$?" == "0" ]; then
     echo "Bundle contains required scripts to run the tests"
   else
     #renaming folder javaeetck to jakartaeetck in the Oracle Bundle
     cd ${WORKSPACE}/jakartaeetck-bundles
     unzip -q -o jakartaeetck.zip
     mv javaeetck jakartaeetck
     rm -rf jakartaeetck.zip
     zip -r jakartaeetck.zip jakartaeetck
     rm -rf jakartaeetck
     cd $WORKSPACE
     mkdir jakartaeetck
     cp -R docker jakartaeetck
     zip -u ${WORKSPACE}/jakartaeetck-bundles/jakartaeetck.zip jakartaeetck/docker/run_jakartaeetck.sh
     zip -u ${WORKSPACE}/jakartaeetck-bundles/jakartaeetck.zip jakartaeetck/docker/build_jakartaeetck.sh
     zip -u ${WORKSPACE}/jakartaeetck-bundles/jakartaeetck.zip jakartaeetck/docker/fix_classpaths.sh
     zip -u ${WORKSPACE}/jakartaeetck-bundles/jakartaeetck.zip jakartaeetck/docker/JTReportParser/JTReportParser.jar
     rm -rf jakartaeetck
   fi

   if [ ! -z "$GF_VERSION_URL" ]; then
       wget --progress=bar:force --no-cache $GF_VERSION_URL -O glassfish.version
       cat glassfish.version
   fi
   exit 0
fi

which ant
ant -version

which java
java -version

export ANT_OPTS="-Xmx2G -Djava.endorsed.dirs=${GF_HOME}/glassfish5/glassfish/modules/endorsed \
                 -Djavax.xml.accessExternalStylesheet=all \
                 -Djavax.xml.accessExternalSchema=all \
                 -Djavax.xml.accessExternalDTD=file,http"

echo ########## Remove hard-coded paths from install/jakartaee/bin/ts.jte ##########"
sed -e "s#^javaee.home=.*#javaee.home=$GF_HOME/glassfish5/glassfish#g" \
    -e "s#^javaee.home.ri=.*#javaee.home.ri=$GF_HOME/glassfish5/glassfish#g" \
    -e "s#^report.dir=.*#report.dir=$BASEDIR/JTReport#g" \
    -e "s#^work.dir=.*#work.dir=$BASEDIR/JTWork#g" $BASEDIR/install/jakartaee/bin/ts.jte > $BASEDIR/install/jakartaee/bin/ts.jte.new
mv $BASEDIR/install/jakartaee/bin/ts.jte.new $BASEDIR/install/jakartaee/bin/ts.jte
echo "Contents of modified TS.JTE file"
cat $BASEDIR/install/jakartaee/bin/ts.jte

echo "########## Trunk.Install.V5 Config ##########"
cd $BASEDIR
if [ -z "$GF_BUNDLE_URL" ]; then
  echo "Using default url for GF bundle: $DEFAULT_GF_BUNDLE_URL"
  export GF_BUNDLE_URL=$DEFAULT_GF_BUNDLE_URL
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip -q -o latest-glassfish.zip
ls -l $GF_HOME/glassfish5/glassfish/

# temporary fix to get build passing until we have glassfish with new api jars
wget --progress=bar:force --no-cache \
     https://repo1.maven.org/maven2/jakarta/platform/jakarta.jakartaee-api/9.0.0-RC1/jakarta.jakartaee-api-9.0.0-RC1.jar \
     -O $GF_HOME/glassfish5/glassfish/modules/jakartaee-api.jar
wget --progress=bar:force --no-cache \
     https://repo1.maven.org/maven2/jakarta/xml/ws/jakarta.xml.ws-api/3.0.0-RC1/jakarta.xml.ws-api-3.0.0-RC1.jar \
     -O $GF_HOME/glassfish5/glassfish/modules/jakarta.xml.ws-api.jar
wget --progress=bar:force --no-cache \
     https://repo1.maven.org/maven2/jakarta/xml/bind/jakarta.xml.bind-api/3.0.0-RC2/jakarta.xml.bind-api-3.0.0-RC2.jar \
      -O $GF_HOME/glassfish5/glassfish/modules/jakarta.xml.bind-api.jar
# temporary fix to get jaxws build passing until we have glassfish with new api jars
. ${WORKSPACE}/docker/build_jaxws-inc.sh

if [ ! -z "$GF_VERSION_URL" ]; then
  wget --progress=bar:force --no-cache $GF_VERSION_URL -O glassfish.version
  cat glassfish.version
fi

echo "########## Trunk.Clean.Build.Libs ##########"
ant -f $BASEDIR/install/jakartaee/bin/build.xml -Ddeliverabledir=jakartaee -Dbasedir=$BASEDIR/install/jakartaee/bin clean.all build.all.jars

echo "########## Trunk.Build ##########"
# Builds the CTS Deliverable
ant -f $BASEDIR/install/jakartaee/bin/build.xml -Ddeliverabledir=jakartaee -Dbasedir=$BASEDIR/install/jakartaee/bin  modify.jstl.db.resources

# Full workspace build.
ant -f $BASEDIR/install/jakartaee/bin/build.xml -Ddeliverabledir=jakartaee -Dbasedir=$BASEDIR/install/jakartaee/bin -Djava.endorsed.dirs=$GF_HOME/glassfish5/glassfish/modules/endorsed build.all


echo "########## Trunk.Sanitize.JTE ##########"
# Sanitize the ts.jte file based on the values in release/tools/jte.props.sanitize
ant -f $BASEDIR/release/tools/build-utils.xml -Ddeliverabledir=jakartaee -Dbasedir=$BASEDIR/release/tools -Dts.jte.prop.file=$BASEDIR/release/tools/jte.props.sanitize


echo "########## Trunk.Clean.Builds ##########"
# Cleans all bundles under TS_HOME/release except tools.
ant -f $BASEDIR/release/tools/build-utils.xml -Ddeliverabledir=jakartaee -Dbasedir=$BASEDIR/release/tools remove.bundles


echo "########## Trunk.CTS ##########"
mkdir -p $BASEDIR/internal/docs/jakartaee/
cp $BASEDIR/internal/docs/dtd/*.dtd $BASEDIR/internal/docs/jakartaee/
if [[ "$LICENSE" == "EFTL" || "$LICENSE" == "eftl" ]]; then
  ant -f $BASEDIR/release/tools/build.xml -Ddeliverabledir=jakartaee -Ddeliverable.version=8.0 -Dskip.createbom="true" -Dskip.build="true" -Dbasedir=$BASEDIR/release/tools -DuseEFTLicensefile="true" jakartaee
else
  ant -f $BASEDIR/release/tools/build.xml -Ddeliverabledir=jakartaee -Ddeliverable.version=8.0 -Dskip.createbom="true" -Dskip.build="true" -Dbasedir=$BASEDIR/release/tools jakartaee
fi

ant -f $BASEDIR/release/tools/build.xml -Ddeliverabledir=jakartaee -Ddeliverable.version=8.0 -Dskip.createbom="true" -Dskip.build="true" -Dbasedir=$BASEDIR/release/tools smoke

mkdir -p ${WORKSPACE}/jakartaeetck-bundles
cd ${WORKSPACE}/jakartaeetck-bundles

cp ${WORKSPACE}/release/JAKARTAEE-SMOKE_BUILD/latest/jakartaee-smoke*.zip ${WORKSPACE}/jakartaeetck-bundles/
cp ${WORKSPACE}/release/JAKARTAEE_BUILD/latest/jakartaeetck*.zip ${WORKSPACE}/jakartaeetck-bundles/

for entry in `ls *.zip`; do
  date=`echo "$entry" | cut -d_ -f2`
  strippedEntry=`echo "$entry" | cut -d_ -f1`
  if [[ "$LICENSE" == "EFTL" || "$LICENSE" == "eftl" ]]; then
    mv ${WORKSPACE}/jakartaeetck-bundles/$entry ${WORKSPACE}/jakartaeetck-bundles/eclipse-${strippedEntry}.zip
  else
    mv ${WORKSPACE}/jakartaeetck-bundles/$entry ${WORKSPACE}/jakartaeetck-bundles/${strippedEntry}.zip
  fi
done

#Generate Version file
GIT_HASH=`git rev-parse HEAD`
GIT_BRANCH=`git branch | awk '{print $2}'`
BUILD_DATE=`date`
rm -f ${WORKSPACE}/jakartaeetck.version
touch ${WORKSPACE}/jakartaeetck.version
echo "Git Revision: ${GIT_HASH}" >> ${WORKSPACE}/jakartaeetck.version
echo "Git Branch: ${GIT_BRANCH}" >> ${WORKSPACE}/jakartaeetck.version
echo "Build Date: ${BUILD_DATE}" >> ${WORKSPACE}/jakartaeetck.version
