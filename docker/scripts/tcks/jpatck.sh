#!/bin/bash -x
#
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
#

cd $TCK_HOME
echo "Download and install JPA TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jpatck-1.1_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jpatck-1.1_Latest.zip
fi
unzip jpatck-1.1_Latest.zip

TS_HOME=$TCK_HOME/jpatck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin
sed -i "s#^jpa.classes=.*#jpa.classes=$TCK_HOME/glassfish5/glassfish/modules/jakarta.persistence.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.jpa.jar:$TCK_HOME/glassfish5/glassfish/modules/javax.servlet-api.jar:$TCK_HOME/glassfish5/glassfish/modules/javax.transaction-api.jar:$TCK_HOME/glassfish5/glassfish/modules/javax.ejb-api.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.antlr.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.asm.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.asm.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.core.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.dbws.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.jpa.jpql.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.jpa.modelgen.processor.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.moxy.jar:$TCK_HOME/glassfish5/glassfish/modules/org.eclipse.persistence.oracle.jar#g" ts.jte
sed -i "s#^jdbc\.driver\.classes=.*#jdbc.driver.classes=$TCK_HOME/glassfish5/javadb/lib/derbyclient.jar:$TS_HOME/lib/dbprocedures.jar#g" ts.jte
sed -i "s#^jdbc\.db=.*#jdbc.db=derby#g" ts.jte
sed -i "s#^jakarta.persistence.provider=.*#jakarta.persistence.provider=org.eclipse.persistence.jpa.PersistenceProvider#g" ts.jte
sed -i "s#^jakarta.persistence.jdbc.driver=.*#jakarta.persistence.jdbc.driver=org.apache.derby.jdbc.ClientDriver#g" ts.jte
sed -i 's#^jakarta.persistence.jdbc.url=.*#jakarta.persistence.jdbc.url=jdbc:derby:\/\/localhost:1527\/derbyDB;create=true#g' ts.jte
sed -i "s#^jakarta.persistence.jdbc.user=.*#jakarta.persistence.jdbc.user=cts1#g" ts.jte
sed -i "s#^jakarta.persistence.jdbc.password=.*#jakarta.persistence.jdbc.password=cts1#g" ts.jte
echo "impl.vi=glassfish" >> ts.jte
#sed -i "s#harness.log.traceflag=false.*#harness.log.traceflag=true#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jpatckreport#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jpatckwork#g" ts.jte

mkdir $TCK_HOME/jpatckreport
mkdir $TCK_HOME/jpatckwork

cd $TCK_HOME/glassfish5/glassfish/bin
./asadmin start-database --dbhome $TCK_HOME/glassfish5/glassfish/databases --dbport 1527 --jvmoptions "-Djava.security.manager -Djava.security.policy=$TS_HOME/bin/security.policy"

cd $TS_HOME/bin
ant -f initdb.xml
ant -f xml/ts.top.import.xml deploy.all
ant run.all
