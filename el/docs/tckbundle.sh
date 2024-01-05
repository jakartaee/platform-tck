#!/bin/bash -xe

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

if [ -z "$WORKSPACE" ]; then
  export WORKSPACE="$(dirname "$(dirname "$(pwd)")")"
fi

if [ -z "$EL_HOME" ]; then
  export EL_HOME="$(dirname "$(pwd)")"
fi

# export EL_HOME=$WORKSPACE/el
export VERSION="$2"

if [ -z "$VERSION" ]; then
  export VERSION="6.0.0"
fi

cd $EL_HOME

if [[ "$1" == "epl" || "$1" == "EPL" ]]; then
    #to build EPL Licensed TCK bundle of name expression-language-tck*
    sed "s/jakarta-expression-language-tck/expression-language-tck/g" $EL_HOME/pom.xml > $EL_HOME/pom.epl.xml
    mvn clean install -f pom.epl.xml
else
    #to build EFTL Licensed TCK bundle of name jakarta-expression-language-tck*
    mvn clean install
fi

cd $EL_HOME/docs/userguide
mvn

rm -rf $EL_HOME/bundle

mkdir -p $EL_HOME/bundle
mkdir -p $EL_HOME/bundle/docs
mkdir -p $EL_HOME/bundle/docs/html-usersguide
mkdir -p $EL_HOME/bundle/docs/pdf-usersguide
cp $EL_HOME/docs/userguide/target/generated-docs/*.pdf $EL_HOME/bundle/docs/pdf-usersguide/
cp $EL_HOME/docs/userguide/target/staging/*.html $EL_HOME/bundle/docs/html-usersguide/
cp -r $EL_HOME/docs/userguide/target/staging/css $EL_HOME/bundle/docs/html-usersguide/
cp -r $EL_HOME/docs/userguide/target/staging/img $EL_HOME/bundle/docs/html-usersguide/
cp $EL_HOME/docs/*.html $EL_HOME/bundle/docs/
cp $EL_HOME/docs/*.txt $EL_HOME/bundle/docs/
cp -r $EL_HOME/docs/assertions $EL_HOME/bundle/docs/

cp $EL_HOME/target/*.jar $EL_HOME/bundle/

cd $EL_HOME/bundle
mkdir -p $EL_HOME/bundle/META-INF


if [[ "$1" == "epl" || "$1" == "EPL" ]]; then
    #copy EPL LICENSE file 
    cp $WORKSPACE/LICENSE.md $EL_HOME/bundle/LICENSE.md
    cp $WORKSPACE/LICENSE.md $EL_HOME/bundle/META-INF/LICENSE.md
    cp $EL_HOME/pom.epl.xml $EL_HOME/bundle/expression-language-tck-"$VERSION".pom
    #include a copy of LICENSE file in tck jar
    jar -uvf expression-language-tck-"$VERSION".jar META-INF/LICENSE.md
    rm -rf $WORKSPACE/bundle/META-INF
    zip -r expression-language-tck-"$VERSION".zip *
else
    #copy EFTL LICENSE file 
    cp $EL_HOME/docs/LICENSE_EFTL.md $EL_HOME/bundle/LICENSE.md
    cp $EL_HOME/docs/LICENSE_EFTL.md $EL_HOME/bundle/META-INF/LICENSE.md
    cp $EL_HOME/pom.xml $EL_HOME/bundle/jakarta-expression-language-tck-"$VERSION".pom
    #include a copy of LICENSE file in tck jar
    jar -uvf jakarta-expression-language-tck-"$VERSION".jar META-INF/LICENSE.md
    rm -rf $WORKSPACE/bundle/META-INF
    zip -r jakarta-expression-language-tck-"$VERSION".zip * 
fi
