#!/bin/bash
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

if [ -z "$TCK_NAME" ]
then
  echo "Please supply a valid TCK_NAME using --env"
  exit 1
fi

# Run Standalone TCK with GlassFish
cd $TCK_HOME

### Common part starts here ###
echo "JAVA_HOME $JAVA_HOME"
echo "ANT_HOME $ANT_HOME"
echo "PATH $PATH"
echo "TCK_HOME $TCK_HOME"

echo "Download and install GlassFish 5.0.1 ..."
if [ -z "$GLASSFISH_BUNDLE_URL" ]; then
  wget http://<host>/latest-glassfish.zip
else
  echo "Downloading the TCK bundle from $GLASSFISH_BUNDLE_URL"  
  wget $GLASSFISH_BUNDLE_URL -O latest-glassfish.zip
fi
unzip latest-glassfish.zip

# Do we really need this ?
chmod -R 777 $TCK_HOME/glassfish5

export ANT_OPTS="-Djava.endorsed.dirs=$TCK_HOME/glassfish5/glassfish/modules/endorsed \
                 -Djavax.xml.accessExternalStylesheet=all \
                 -Djavax.xml.accessExternalSchema=all \
                 -Djavax.xml.accessExternalDTD=file,http $ANT_OPTS"

### Common part ends here ###

if [ "$TCK_NAME" == "servlet" ];then
  source servlettck.sh
  exit 0
fi

if [ "$TCK_NAME" == "jsonb" ];then
  source jsonbtck.sh
  exit 0
fi

if [ "$TCK_NAME" == "jsf" ];then
  source jsftck.sh
  exit 0
fi

if [ "$TCK_NAME" == "securityapi" ];then
  source securityapitck.sh
  exit 0
fi

if [ "$TCK_NAME" == "jsonp" ];then
  source jsonptck.sh
  exit 0
fi

if [ "$TCK_NAME" == "websocket" ];then
  source websockettck.sh
  exit 0
fi

if [ "$TCK_NAME" == "caj" ];then
  source cajtck.sh
  exit 0
fi

if [ "$TCK_NAME" == "jta" ];then
  source jtatck.sh
  exit 0
fi

if [ "$TCK_NAME" == "jaxws" ];then
  source jaxwstck.sh
  exit 0
fi

if [ "$TCK_NAME" == "jaxrs" ];then
  source jaxrstck.sh
  exit 0
fi

if [ "$TCK_NAME" ==  "jpa" ];then
  source jpatck.sh
  exit 0
fi

if [ "$TCK_NAME" ==  "jaspic" ];then
  source jaspictck.sh
  exit 0
fi

if [ "$TCK_NAME" ==  "jacc" ];then
  source jacctck.sh
  exit 0
fi

if [ "$TCK_NAME" ==  "jms" ];then
  source jmstck.sh
  exit 0
fi

if [ "$TCK_NAME" ==  "jstl" ];then
   source jstltck.sh
   exit 0
fi

if [ "$TCK_NAME" ==  "concurrency" ];then
   source concurrencytck.sh
   exit 0
fi

if [ "$TCK_NAME" ==  "jaxr" ];then
   source jaxrtck.sh
   exit 0
fi

if [ "$TCK_NAME" ==  "connector" ];then
   source connectortck.sh
   exit 0
fi

if [ "$TCK_NAME" ==  "el" ];then
   source eltck.sh
   exit 0
fi

if [ "$TCK_NAME" ==  "jsp" ];then
   source jsptck.sh
   exit 0
fi

if [ "$TCK_NAME" ==  "saaj" ];then
   source saajtck.sh
   exit 0
fi

if [ "$TCK_NAME" ==  "jaxrpc" ];then
   source jaxrpctck.sh
   exit 0
fi
