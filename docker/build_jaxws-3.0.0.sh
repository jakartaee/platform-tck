#!/bin/bash -xe

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

echo '-[ Temporary build fix ]-----------------------------------------'

if [ -z "${JAXWS_RI_SNAPSHOT_URL}" ]; then
  JAXWS_RI_SNAPSHOT_URL='https://jakarta.oss.sonatype.org/content/repositories/snapshots/com/sun/xml/ws/jaxws-ri/3.0.0-SNAPSHOT/jaxws-ri-3.0.0-20200910.184222-21.zip'
fi

if [ -z "${JAXB_RI_URL}" ]; then
  JAXB_RI_URL='https://jakarta.oss.sonatype.org/content/groups/staging/com/sun/xml/bind/jaxb-osgi/3.0.0-M5/jaxb-osgi-3.0.0-M5.jar'
fi

JAXWS_RI_DIR=${WORKSPACE}/jaxws_3.0.0/jaxws-ri/lib

if [ ! -d ${WORKSPACE}/jaxws_3.0.0 ]; then
  mkdir -p ${WORKSPACE}/jaxws_3.0.0
  cd ${WORKSPACE}/jaxws_3.0.0
  wget --progress=bar:force --no-cache "${JAXWS_RI_SNAPSHOT_URL}" -O jaxws-ri.zip && unzip -q jaxws-ri.zip
  wget --progress=bar:force --no-cache "${JAXB_RI_URL}" -O jaxb-osgi.jar
fi

cd ${JAXWS_RI_DIR}

echo '-[ Extracting and merging downloaded RI ]--------------------------------------'
mkdir -vp ${JAXWS_RI_DIR}/ws-ri-gf
cd ${JAXWS_RI_DIR}/ws-ri-gf
jar xf ${GF_HOME}/glassfish6/glassfish/modules/webservices-osgi.jar
cd ${JAXWS_RI_DIR}

mkdir -vp ${JAXWS_RI_DIR}/ws-ri-dl
cd ${JAXWS_RI_DIR}/ws-ri-dl
jar xf ${JAXWS_RI_DIR}/jaxws-rt.jar
jar xf ${JAXWS_RI_DIR}/jaxws-tools.jar
mv -v META-INF/MANIFEST.MF META-INF/MANIFEST.MF-jaxws
mv -v module-info.class module-info.class-jaxws
rm -rfv META-INF/MANIFEST.MF module-info.class &&
  find ./ | cpio -pudm ${JAXWS_RI_DIR}/ws-ri-gf
  ls -l

rm -v ${GF_HOME}/glassfish6/glassfish/modules/webservices-osgi.jar
cd ${JAXWS_RI_DIR}/ws-ri-gf
jar cfm ${GF_HOME}/glassfish6/glassfish/modules/webservices-osgi.jar META-INF/MANIFEST.MF .

cp -a ${WORKSPACE}/jaxws_3.0.0/jaxb-osgi.jar ${GF_HOME}/glassfish6/glassfish/modules/jaxb-osgi.jar