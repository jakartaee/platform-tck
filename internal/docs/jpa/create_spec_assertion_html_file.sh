#!/bin/sh +x
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

DOC_DIR=${TS_HOME}/internal/docs/jpa
TMP_DIR=${DOC_DIR}/tmp
rm -rf ${TMP_DIR}
mkdir -p ${TMP_DIR}
echo "Processing file:${DOC_DIR}/PersistenceSpecAssertions.xml"
cd $CTS_TOOLS/tools/xsl-transformer/scripts
./run ${DOC_DIR}/PersistenceSpecAssertions.xml ../../../docs/xsl/assertions/spec_assertions.xsl ${TMP_DIR}/PersistenceSpecAssertions.html
echo "Output html file:${TMP_DIR}/PersistenceSpecAssertions.html"

