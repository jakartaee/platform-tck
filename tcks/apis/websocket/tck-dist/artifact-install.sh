#!/usr/bin/env bash
#
# Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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

##script to install the artifact directory contents into a local maven repository

if [[ $1 =~ ^[0-9]+\.[0-9]+\.[0-9]+.*$ ]]; then
  VERSION="$1"
else
  VERSION="2.2.0"
fi

JAKARTAEE_VERSION="11.0.0-M1"


# websocket-tck pom
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file  \
-Dfile=websocket-tck-$VERSION.pom -DgroupId=jakarta.tck -DartifactId=websocket-tck \
-Dversion=$VERSION -Dpackaging=pom

# websocket-tck-common pom
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file  \
-Dfile=websocket-tck-common-$VERSION.pom -DgroupId=jakarta.tck -DartifactId=websocket-tck-common \
-Dversion=$VERSION -Dpackaging=pom

# websocket-tck-common jar
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file  \
-Dfile=websocket-tck-common-$VERSION.jar -DgroupId=jakarta.tck -DartifactId=websocket-tck-common \
-Dversion=$VERSION -Dpackaging=jar

# websocket-tck-spec-tests jar
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file  \
-Dfile=websocket-tck-spec-tests-$VERSION.jar -DgroupId=jakarta.tck -DartifactId=websocket-tck-spec-tests \
-Dversion=$VERSION -Dpackaging=jar

# websocket-tck-spec-tests pom
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file  \
-Dfile=websocket-tck-spec-tests-$VERSION.pom -DgroupId=jakarta.tck -DartifactId=websocket-tck-spec-tests \
-Dversion=$VERSION -Dpackaging=pom
