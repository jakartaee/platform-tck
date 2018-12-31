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

echo "Proxy $http_proxy"

echo "proxy=$http_proxy" >> /etc/yum.conf

echo "Install wget ...."
yum install -y wget

echo "Install unzip ..."
yum install -y unzip

yum install -y  libXext.x86_64 libXrender.x86_64 libXtst.x86_64

echo "Download and install JDK8 ..."
if [ "$JDK_BUNDLE_URL" == "dummy" ];then
  wget http://<jdkhost>/jdk-8u171-linux-x64.tar.gz
else
  echo "Downloading the JDK8 bundle from $JDK_BUNDLE_URL"  
  wget $JDK_BUNDLE_URL -O jdk-8u171-linux-x64.tar.gz
fi
tar -zxf jdk-8u171-linux-x64.tar.gz

echo "Download and install Apache Ant v 1.9.7 ..."
wget https://<apachehost>/apache-ant-1.9.7-bin.tar.gz
tar -zxf apache-ant-1.9.7-bin.tar.gz
