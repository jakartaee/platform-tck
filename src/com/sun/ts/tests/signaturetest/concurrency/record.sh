#!/bin/sh
#
# Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

#
# $Id$
#

PATHSEP=:
CONCURRENCY_HOME=/sun/glassfish4/glassfish
CONCURRENCY_LIB=${CONCURRENCY_HOME}/modules
CONCURRENCY_CLASSES=$CONCURRENCY_LIB/javax.enterprise.concurrent-api.jar
cd ../signature-repository
ant -f ../record-build.xml -Drecorder.type=sigtest -Dsig.source=${CONCURRENCY_CLASSES}:$JAVA_HOME/jre/lib/rt.jar \
       -Dmap.file=$TS_HOME/install/concurrency/bin/sig-test.map record.sig.batch
cd ../concurrency
