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

cd $JWSDP_HOME
echo "Proxy $http_proxy"
echo "proxy=http://$http_proxy" >> /etc/yum.conf

echo "Download Java Web Services Developer Pack 1.3 and JDK 1.5 required for that ..."

if [ "$JWSDP_BUNDLE" == "dummy" ];then
  wget http://<host>/jwsdp-1.3.tar
else
  wget $JWSDP_BUNDLE -O jwsdp-1.3.tar
fi
tar -xvf jwsdp-1.3.tar

if [ "$JDK_FOR_JWSDP" == "dummy" ];then
  wget http://<host>/jdk1.5.0_22.tar
else
  wget $JDK_FOR_JWSDP -O jdk1.5.0_22.tar
fi
tar -xvf jdk1.5.0_22.tar

echo "Install Java Web Services Developer Pack 1.3 ..."
cd $JWSDP_HOME/jwsdp-1.3/jwsdp-shared/bin

sed -i "s#^JAVA_HOME=.*#JAVA_HOME=\"$JWSDP_HOME/jdk1.5.0_22\"#g" setenv.sh
sed -i "s#^JWSDP_HOME=.*#JWSDP_HOME=\"$JWSDP_HOME/jwsdp-1.3\"#g" setenv.sh

cd $CTS_HOME

