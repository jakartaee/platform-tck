#!/bin/sh
#
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
#

PATHSEP=:
WEBCONTAINER_HOME=$CATALINA_HOME
JAXWS_CLASSES=${JAXWS_HOME}/lib/FastInfoset.jar${PATHSEP}${JAXWS_HOME}/lib/activation.jar${PATHSEP}${JAXWS_HOME}/lib/gmbal-api-only.jar${PATHSEP}${JAXWS_HOME}/lib/http.jar${PATHSEP}${JAXWS_HOME}/lib/jaxb-api.jar${PATHSEP}${JAXWS_HOME}/lib/jaxb-impl.jar${PATHSEP}${JAXWS_HOME}/lib/jaxb-xjc.jar${PATHSEP}${JAXWS_HOME}/lib/jaxws-api.jar${PATHSEP}${JAXWS_HOME}/lib/jaxws-rt.jar${PATHSEP}${JAXWS_HOME}/lib/jaxws-tools.jar${PATHSEP}${JAXWS_HOME}/lib/jsr173_api.jar${PATHSEP}${JAXWS_HOME}/lib/jsr181-api.jar${PATHSEP}${JAXWS_HOME}/lib/jsr250-api.jar${PATHSEP}${JAXWS_HOME}/lib/management-api.jar${PATHSEP}${JAXWS_HOME}/lib/mimepull.jar${PATHSEP}${JAXWS_HOME}/lib/policy.jar${PATHSEP}${JAXWS_HOME}/lib/resolver.jar${PATHSEP}${JAXWS_HOME}/lib/saaj-api.jar${PATHSEP}${JAXWS_HOME}/lib/saaj-impl.jar${PATHSEP}${JAXWS_HOME}/lib/woodstox.jar${PATHSEP}${JAXWS_HOME}/lib/stax-ex.jar${PATHSEP}${JAXWS_HOME}/lib/streambuffer.jar${PATHSEP}${WEBCONTAINER_HOME}/common/lib/servlet-api.jar
cd ../signature-repository
ant -f ../record-build.xml -Drecorder.type=sigtest -Dsig.source=${JAXWS_CLASSES}:$JAVA_HOME/jre/lib/rt.jar \
       -Dmap.file=$TS_HOME/install/jaxws/bin/sig-test.map record.sig.batch
cd ../jaxws
