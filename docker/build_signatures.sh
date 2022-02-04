#!/bin/bash -xe

# Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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
# Usage:  run docker/build_signatures.sh from root of Jakarta EE Platform TCK source project to generate signature map files with Java SE 11.
#          bash -x docker/build_signatures.sh  2>&1 | tee /tmp/sign.log
# 

if [ -z "$WORKSPACE" ]; then
  export WORKSPACE=`pwd`
fi

cd $WORKSPACE
export BASEDIR=`pwd`
export TS_HOME=$BASEDIR

if [ -z "$JAKARTA_JARS" ]; then
  export JAKARTA_JARS=$BASEDIR/modules
fi

echo "JAKARTA_JARS = $JAKARTA_JARS"
export TS_HOME=$BASEDIR

which java
java -version

cd $BASEDIR

mkdir -p $JAKARTA_JARS

cd $JAKARTA_JARS

mvn -f $BASEDIR/docker/pom.xml -Pstaging dependency:copy-dependencies -DoutputDirectory="${JAKARTA_JARS}" -Dmdep.stripVersion=true

# if we ever need jimage support, uncomment the following 3 lines and reopen https://github.com/jtulach/netbeans-apitest/pull/6
# rm -rf /tmp/.jimage
# mkdir -p /tmp/.jimage
# jimage extract --dir=/tmp/.jimage $JAVA_HOME/lib/modules

echo "generate TCK signature map files"

export sigtest_classes="${TS_HOME}/lib/sigtest.jar"
export pathsep=:
export deliverabledir=signature-repository
mkdir -p $BASEDIR/install/$deliverabledir
export tckname=jakartaee

export INCLUDES="jakarta.activation:jakarta.annotation:javax.annotation.processing:jakarta.annotation.security:jakarta.annotation.sql:jakarta.batch.api:jakarta.batch.api.chunk:jakarta.batch.api.chunk.listener:jakarta.batch.api.listener:jakarta.batch.api.partition:jakarta.batch.operations:jakarta.batch.runtime:jakarta.batch.runtime.context:jakarta.decorator:jakarta.ejb:jakarta.ejb.embeddable:jakarta.ejb.spi:jakarta.el:jakarta.enterprise.concurrent:jakarta.enterprise.context:jakarta.enterprise.context.control:jakarta.enterprise.context.spi:jakarta.enterprise.event:jakarta.enterprise.inject:jakarta.enterprise.inject.literal:jakarta.enterprise.inject.spi:jakarta.enterprise.inject.spi.configurator:jakarta.enterprise.util:jakarta.faces:jakarta.faces.application:jakarta.faces.bean:jakarta.faces.component:jakarta.faces.component.behavior:jakarta.faces.component.html:jakarta.faces.component.search:jakarta.faces.component.visit:jakarta.faces.context:jakarta.faces.convert:jakarta.faces.el:jakarta.faces.event:jakarta.faces.flow:jakarta.faces.flow.builder:jakarta.faces.lifecycle:jakarta.faces.model:jakarta.faces.push:jakarta.faces.render:jakarta.faces.validator:jakarta.faces.view:jakarta.faces.view.facelets:jakarta.faces.webapp:jakarta.inject:jakarta.interceptor:jakarta.jms:jakarta.json:jakarta.json.bind:jakarta.json.bind.adapter:jakarta.json.bind.annotation:jakarta.json.bind.config:jakarta.json.bind.serializer:jakarta.json.bind.spi:jakarta.json.spi:jakarta.json.stream:jakarta.jws:jakarta.jws.soap:jakarta.mail:jakarta.mail.event:jakarta.mail.internet:jakarta.mail.search:jakarta.mail.util:javax.naming:jakarta.persistence:jakarta.persistence.criteria:jakarta.persistence.metamodel:jakarta.persistence.spi:jakarta.resource:jakarta.resource.cci:jakarta.resource.spi:jakarta.resource.spi.endpoint:jakarta.resource.spi.security:jakarta.resource.spi.work:javax.security.auth.callback:javax.security.auth.login:jakarta.security.auth.message:jakarta.security.auth.message.callback:jakarta.security.auth.message.config:jakarta.security.auth.message.module:jakarta.security.enterprise:jakarta.security.enterprise.authentication.mechanism.http:jakarta.security.enterprise.credential:jakarta.security.enterprise.identitystore:jakarta.security.jacc:jakarta.servlet:jakarta.servlet.annotation:jakarta.servlet.descriptor:jakarta.servlet.http:jakarta.servlet.jsp:jakarta.servlet.jsp.el:jakarta.servlet.jsp.jstl.core:jakarta.servlet.jsp.jstl.fmt:jakarta.servlet.jsp.jstl.sql:jakarta.servlet.jsp.jstl.tlv:jakarta.servlet.jsp.tagext:jakarta.transaction:javax.transaction.xa:jakarta.validation:jakarta.validation.Configuration<{jakarta.validation:jakarta.validation.Configuration<{jakarta.validation.bootstrap:jakarta.validation.Configuration<{jakarta.validation.spi:jakarta.validation.bootstrap:jakarta.validation.constraints:jakarta.validation.constraintvalidation:jakarta.validation.executable:jakarta.validation.groups:jakarta.validation.metadata:jakarta.validation.spi:jakarta.validation.valueextraction:jakarta.websocket:jakarta.websocket.server:jakarta.ws.rs:jakarta.ws.rs.client:jakarta.ws.rs.container:jakarta.ws.rs.core:jakarta.ws.rs.ext:jakarta.ws.rs.sse:jakarta.xml.bind:jakarta.xml.bind.annotation:jakarta.xml.bind.annotation.adapters:jakarta.xml.bind.attachment:jakarta.xml.bind.helpers:jakarta.xml.bind.util:jakarta.xml.soap:javax.xml.transform:javax.xml.transform.dom:javax.xml.transform.sax:jakarta.xml.ws:jakarta.xml.ws.handler:jakarta.xml.ws.handler.soap:jakarta.xml.ws.http:jakarta.xml.ws.soap:jakarta.xml.ws.spi:jakarta.xml.ws.spi.http:jakarta.xml.ws.wsaddressing:org.w3c.dom:org.xml.sax"


export sigTestClasspath="$JAKARTA_JARS/glassfish-corba-omgapi.jar${pathsep}\
$JAKARTA_JARS/jakarta.activation-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.activation.jar${pathsep}\
$JAKARTA_JARS/jakarta.annotation-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.authentication-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.authorization-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.batch-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.ejb-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.el-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.enterprise.cdi-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.enterprise.concurrent-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.faces-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.inject-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.interceptor-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.jms-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.json-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.json.bind-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.mail-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.persistence-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.resource-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.security.enterprise-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.servlet-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.servlet.jsp-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.servlet.jsp.jstl-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.transaction-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.validation-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.websocket-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.websocket-client-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.ws.rs-api.jar${pathsep}\
$JAKARTA_JARS/jakarta.xml.bind-api.jar${pathsep}\
$JAKARTA_JARS/webservices-api.jar${pathsep}\
$JAKARTA_JARS/webservices-api-osgi.jar${pathsep}"

#export OPTIONS="-static -debug -verbose"
export OPTIONS="-static -BootCp 11"
#export CLASSNAME="-agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=y com.sun.tdk.signaturetest.Main"
export CLASSNAME="com.sun.tdk.signaturetest.Main"
export CLASSPATH=$CLASSPATH:${TS_HOME}/lib/sigtest.jar
export SIGFILEPATH=$TS_HOME/src/com/sun/ts/tests/signaturetest/signature-repository

# jakarta.annotation
java $jdk9options $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0 -package jakarta.annotation -FileName ${SIGFILEPATH}/jakarta.annotation.sig_2.1_se11
# jakarta.security.jacc
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.security.jacc -FileName ${SIGFILEPATH}/jakarta.security.jacc.sig_3.0_se11
# jakarta.batch
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.batch -FileName ${SIGFILEPATH}/jakarta.batch.sig_2.1_se11
# jakarta.decorator
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.decorator -FileName ${SIGFILEPATH}/jakarta.decorator.sig_4.0_se11
# jakarta.ejb
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.ejb -FileName ${SIGFILEPATH}/jakarta.ejb.sig_4.0_se11
# jakarta.el
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.el -FileName ${SIGFILEPATH}/jakarta.el.sig_5.0_se11
# jakarta.enterprise
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -Exclude jakarta.enterprise.concurrent -package jakarta.enterprise -FileName ${SIGFILEPATH}/jakarta.enterprise.sig_4.0_se11
# jakarta.enterprise.concurrent
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.enterprise.concurrent -FileName ${SIGFILEPATH}/jakarta.enterprise.concurrent.sig_3.0_se11
# jakarta.faces
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.faces -FileName ${SIGFILEPATH}/jakarta.faces.sig_4.0_se11
# jakarta.inject
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.inject -FileName ${SIGFILEPATH}/jakarta.inject.sig_2.0_se11
# jakarta.interceptor
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.interceptor -FileName ${SIGFILEPATH}/jakarta.interceptor.sig_2.0_se11
# jakarta.jms
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.jms -FileName ${SIGFILEPATH}/jakarta.jms.sig_3.1_se11
# jakarta.json
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -Exclude jakarta.json.bind -package jakarta.json -FileName ${SIGFILEPATH}/jakarta.json.sig_2.1_se11
# jakarta.json.bind
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 3.0  -package jakarta.json.bind -FileName ${SIGFILEPATH}/jakarta.json.bind.sig_3.0_se11
# jakarta.mail
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.mail -FileName ${SIGFILEPATH}/jakarta.mail.sig_2.1_se11
# jakarta.persistence
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.persistence -FileName ${SIGFILEPATH}/jakarta.persistence.sig_3.1_se11
# jakarta.resource
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.resource -FileName ${SIGFILEPATH}/jakarta.resource.sig_2.1_se11
# jakarta.security.enterprise
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.security.enterprise -FileName ${SIGFILEPATH}/jakarta.security.enterprise.sig_3.0_se11
# jakarta.security.auth.message
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.security.auth.message -FileName ${SIGFILEPATH}/jakarta.security.auth.message.sig_3.0_se11
# jakarta.servlet (exclude jakarta.servlet.jsp)
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -Exclude jakarta.servlet.jsp -package jakarta.servlet -FileName ${SIGFILEPATH}/jakarta.servlet.sig_6.0_se11
# jakarta.servlet.jsp (exclude jakarta.servlet.jsp.jstl)
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -Exclude jakarta.servlet.jsp.jstl -package jakarta.servlet.jsp -FileName ${SIGFILEPATH}/jakarta.servlet.jsp.sig_3.1_se11
# jakarta.servlet.jsp.jstl
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.servlet.jsp.jstl -FileName ${SIGFILEPATH}/jakarta.servlet.jsp.jstl.sig_3.0_se11
# jakarta.transaction
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.transaction -FileName ${SIGFILEPATH}/jakarta.transaction.sig_2.0_se11
# jakarta.validation
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.validation -FileName ${SIGFILEPATH}/jakarta.validation.sig_3.0_se11
# jakarta.websocket
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.websocket -FileName ${SIGFILEPATH}/jakarta.websocket.sig_2.1_se11
# jakarta.ws.rs
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.ws.rs -FileName ${SIGFILEPATH}/jakarta.ws.rs.sig_3.1_se11
# jakarta.xml.ws
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.xml.ws -FileName ${SIGFILEPATH}/jakarta.xml.ws.sig_4.0_se11
# jakarta.xml.soap
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0  -package jakarta.xml.soap -FileName ${SIGFILEPATH}/jakarta.xml.soap.sig_3.0_se11
# javax.rmi (only check on JDK11+)
java $CLASSNAME Setup ${OPTIONS} -classpath ${sigTestClasspath} -apiVersion 1.0 -package javax.rmi  -FileName ${SIGFILEPATH}/javax.rmi.sig_1.0_se11


