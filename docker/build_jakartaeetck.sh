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
BUILD_WORKSPACE=$WORKSPACE
LOGFILE=$WORKSPACE/build.jakartaeetck.log

export ANT_HOME=/usr/share/ant/
export JAVA_HOME=/opt/jdk1.8.0_171
export PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH

cd $BUILD_WORKSPACE
export BASEDIR=`pwd`

# Replace dummy.domain.com to a hosting server  where the Assertion DTDs and XSDs are located.
echo "Files modified for removing dummy.domain.com:"
grep -rl dummy.domain.com . | cut -d: -f1 | xargs sed -i "s#dummy\.domain\.com#${SCHEMA_HOSTING_SERVER}#g"

which ant
ant -version

which java
java -version

export ANT_OPTS="-Djava.endorsed.dirs=${BASEDIR}/glassfish5/glassfish/modules/endorsed \
                 -Djavax.xml.accessExternalStylesheet=all \
                 -Djavax.xml.accessExternalSchema=all \
                 -Djavax.xml.accessExternalDTD=file,http"

echo ########## Remove hard-coded paths from install/j2ee/bin/ts.jte ##########"
sed -e "s#^javaee.home=.*#javaee.home=$BASEDIR/glassfish5/glassfish#g" \
    -e "s#^javaee.home.ri=.*#javaee.home.ri=$BASEDIR/glassfish5/glassfish#g" \
    -e "s#^report.dir=.*#report.dir=$BASEDIR/JTReport#g" \
    -e "s#^work.dir=.*#work.dir=$BASEDIR/JTWork#g" $BASEDIR/install/j2ee/bin/ts.jte > $BASEDIR/install/j2ee/bin/ts.jte.new
mv $BASEDIR/install/j2ee/bin/ts.jte.new $BASEDIR/install/j2ee/bin/ts.jte
echo "Contents of modified TS.JTE file"
cat $BASEDIR/install/j2ee/bin/ts.jte

echo "########## Trunk.Install.V5 Config ##########"
cd $BASEDIR
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip -o latest-glassfish.zip
ls -l $BASEDIR/glassfish5/glassfish/

echo "########## Trunk.Clean.Build.Libs ##########"
ant -f $BASEDIR/install/j2ee/bin/build.xml -Ddeliverabledir=j2ee -Dbasedir=$BASEDIR/install/j2ee/bin clean.all build.all.jars

echo "########## Trunk.Build ##########"
# Builds the CTS Deliverable
ant -f $BASEDIR/install/j2ee/bin/build.xml -Ddeliverabledir=j2ee -Dbasedir=$BASEDIR/install/j2ee/bin  modify.jstl.db.resources

# Full workspace build.
ant -f $BASEDIR/install/j2ee/bin/build.xml -Ddeliverabledir=j2ee -Dbasedir=$BASEDIR/install/j2ee/bin -Djava.endorsed.dirs=$BASEDIR/glassfish5/glassfish/modules/endorsed build.all


echo "########## Trunk.Build.Log.Scraper ##########"
# Checks for "BUILD FAILED" in log.
ant -f $BASEDIR/release/tools/build-utils.xml -Ddeliverabledir=j2ee -Dhudson.build.log.copy.dest=$DESTDIR -Dhudson.build.log=$LOGFILE -Dproprietary.check.enabled=true -Dhudson.build.log.2=$LOGFILE -Dbasedir=$BASEDIR/release/tools -lib $BASEDIR/tools/ant-opt-libs scrape.cts.build.log


echo "########## Trunk.Sanitize.JTE ##########"
# Sanitize the ts.jte file based on the values in release/tools/jte.props.sanitize
ant -f $BASEDIR/release/tools/build-utils.xml -Ddeliverabledir=j2ee -Dbasedir=$BASEDIR/release/tools -Dts.jte.prop.file=$BASEDIR/release/tools/jte.props.sanitize


echo "########## Trunk.Clean.Builds ##########"
# Cleans all bundles under TS_HOME/release except tools.
ant -f $BASEDIR/release/tools/build-utils.xml -Ddeliverabledir=j2ee -Dbasedir=$BASEDIR/release/tools remove.bundles


echo "########## Trunk.CTS ##########"
ant -f $BASEDIR/release/tools/build.xml -Ddeliverabledir=j2ee -Ddeliverable.version=8.0 -Dskip.createbom="true" -Dskip.build="true" -Dbasedir=$BASEDIR/release/tools j2ee

mkdir -p ${WORKSPACE}/jakartaeetck-bundles
cd ${WORKSPACE}/jakartaeetck-bundles
cp ${WORKSPACE}/release/JAVAEE_BUILD/latest/javaeetck*.zip ${WORKSPACE}/jakartaeetck-bundles/javaeetck.zip
