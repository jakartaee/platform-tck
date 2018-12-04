#!/bin/bash -x
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

RI_MODULES=$CTS_HOME/ri/glassfish5/glassfish/modules
TS_JTE=$CTS_HOME/javaeetck/bin/ts.jte

javax_prefixed_jars = ( "javax.batch-api.jar" "javax.el-api.jar" "javax.jms-api.jar" "javax.json-api.jar" "javax.xml.registry-api.jar" "javax.security.auth.message-api.jar" "javax.security.jacc-api.jar" "javax.servlet.jsp-api.jar" "javax.servlet.jsp.jstl.jar" "javax.xml.rpc-api.jar" "javax.ws.rs-api.jar" )

for javax_prefix_file in "${javax_prefixed_jars[@]}"
do
  jakarta_prefix_file=`echo $javax_prefix_file | sed 's/javax/jakarta/g'`
  if [[ -f "$RI_MODULES/$jakarta_prefix_file" ]]; then
     echo "Replacing javax prefix with jakarta for jar file $javax_prefix_file in classpath of ts.jte"
     sed -i "s/$javax_prefix_file/$jakarta_prefix_file/g" $TS_JTE
  fi 
done
