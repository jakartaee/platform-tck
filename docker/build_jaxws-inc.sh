# Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
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

# Temporary fix to get jaxws build work until we have glassfish with new api jars
#  - to be included into main build scripts

echo '*** Temporary build fix'

if [ -z "${METRO_BUNDLE_URL}" ]; then
  METRO_BUNDLE_URL='https://ci.eclipse.org/metro/job/wsit-master-build/lastSuccessfulBuild/artifact/wsit/bundles/metro-standalone/target/metro.zip'
fi

if [ ! -d metro ]; then
  wget --progress=bar:force --no-cache "${METRO_BUNDLE_URL}" -O ${WORKSPACE}/metro.zip \
  && unzip -q ${WORKSPACE}/metro.zip
  rm -v ${WORKSPACE}/metro.zip
fi

(cd metro && ant -f metro-on-glassfish.xml -Das.home="$GF_HOME/glassfish5/glassfish" install)
cp -v metro/lib/webservices-tools.jar $GF_HOME/glassfish5/glassfish/modules/webservices-tools.jar
cp -v metro/lib/webservices-api.jar $GF_HOME/glassfish5/glassfish/modules/webservices-api.jar
cp -v metro/osgi/jakarta.xml.bind-api.jar $GF_HOME/glassfish5/glassfish/modules/endorsed

sed -i -e 's#tools\.jar=.*#tools.jar='${GF_HOME//\//\\\/}'\/glassfish5\/glassfish\/modules\/webservices-tools.jar:'${GF_HOME//\//\\\/}'\/glassfish5\/glassfish\/modules\/webservices-api.jar#g' $BASEDIR/install/$tck/bin/ts.jte
