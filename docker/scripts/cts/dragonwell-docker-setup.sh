#!/bin/bash -ex
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

cd $CTS_HOME

echo "Download and install JDK8 ..."
rm -rf /opt/jdk1.8.0_171
rm -rf /opt/jdk1.8.0_191
cd /opt/
wget http://ci.dragonwell-jdk.io/job/build-scripts/job/openjdk8-pipeline/51/artifact/target/linux/x64/dragonwell/OpenJDK8U-jdk_x64_linux_dragonwell_dragonwell-8.4.4_jdk8u262-b11.tar.gz
tar xf OpenJDK8U-jdk_x64_linux_dragonwell_dragonwell-8.4.4_jdk8u262-b11.tar.gz
cd -
cp -r /opt/jdk8u262-b11/ ./jdk1.8.0_191
cp -r /opt/jdk8u262-b11/ ./jdk1.8.0_171
cp -r /opt/jdk8u262-b11/ /opt/jdk1.8.0_171
cp -r /opt/jdk8u262-b11/ /opt/jdk1.8.0_191
