#!/bin/bash -x
#
# Copyright (c) 2018, 2021 Oracle and/or its affiliates. All rights reserved.
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
echo "Download and install JSTL TCK Bundle ..."
if [ -z "$TCK_BUNDLE_URL" ]; then
  wget http://<host>/jstltck-1.2_Latest.zip
else
  echo "Downloading the TCK bundle from $TCK_BUNDLE_URL"  
  wget $TCK_BUNDLE_URL -O jstltck-1.2_Latest.zip
fi
unzip jstltck-1.2_Latest.zip

TS_HOME=$TCK_HOME/jstltck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin
sed -i.bak "s#^webServerHome=.*#webServerHome=$TCK_HOME/glassfish7/glassfish#g" ts.jte
sed -i.bak 's#^webServerPort=.*#webServerPort=8080#g' ts.jte
sed -i.bak 's#^impl\.vi=.*#impl.vi=glassfish#g' ts.jte
sed -i.bak "s#^impl\.vi\.deploy\.dir=.*#impl.vi.deploy.dir=$TCK_HOME/glassfish7/glassfish/domains/domain1/autodeploy#g" ts.jte
sed -i.bak 's#^impl\.deploy\.timeout\.multiplier=.*#impl.deploy.timeout.multiplier=20#g' ts.jte
sed -i.bak 's#^jdbc.classes=.*#jdbc.classes=\$\{webServerHome\}/../javadb/lib/derbyclient.jar#g' ts.jte
sed -i.bak 's#^webServerHost=.*#webServerHost=localhost#g' ts.jte
sed -i.bak 's#^jstl\.db\.name=.*#jstl.db.name=derbyDB#g' ts.jte
sed -i.bak 's#^jstl\.db\.server=.*#jstl.db.server=localhost#g' ts.jte
sed -i.bak 's#^jstl\.db\.port=.*#jstl.db.port=1527#g' ts.jte
sed -i.bak 's#^jstl\.db\.url=.*#jstl.db.url=jdbc:derby://\$\{jstl.db.server\}:\$\{jstl.db.port\}/\$\{jstl.db.name\};create=true#g' ts.jte
sed -i.bak 's#^jstl\.db\.driver=.*#jstl.db.driver=org.apache.derby.jdbc.ClientDriver#g' ts.jte
sed -i.bak 's#^jstl\.db\.user=.*#jstl.db.user=cts1#g' ts.jte
sed -i.bak 's#^jstl\.db\.password=.*#jstl.db.password=cts1#g' ts.jte
sed -i.bak 's#^sigTestClasspath=.*#sigTestClasspath=\$\{ts.home\}/classes:\$\{jstl.classes\}:\$\{jspservlet.classes\}:\$\{JAVA_HOME\}/lib/rt.jar#g' ts.jte
sed -i.bak 's#^jspservlet.classes=.*#jspservlet.classes=\$\{webServerHome\}/modules/javax.servlet-api.jar:\$\{webServerHome\}/modules/javax.servlet.jsp.jar:\$\{webServerHome\}/modules/javax.servlet.jsp-api.jar:\$\{webServerHome\}/modules/javax.el.jar#g' ts.jte
sed -i.bak 's#^jstl.classes=.*#jstl.classes=\$\{webServerHome\}/modules/javax.servlet.jsp.jstl.jar\$\{pathsep\}\$\{webServerHome\}/modules/javax.servlet.jsp.jstl-api.jar#g' ts.jte
sed -i.bak "s#^report.dir=.*#report.dir=$TCK_HOME/jstltckreport#g" ts.jte
sed -i.bak "s#^work.dir=.*#work.dir=$TCK_HOME/jstltckwork#g" ts.jte

mkdir $TCK_HOME/jstltckreport
mkdir $TCK_HOME/jstltckwork

cd $TCK_HOME/glassfish7/glassfish/bin
./asadmin start-database
./asadmin start-domain

cd $TS_HOME/bin
ant init.javadb
ant deploy.all
ant run.all
