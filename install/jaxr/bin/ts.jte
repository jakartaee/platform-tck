#
# Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.
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
# @(#)ts.jte	1.215 02/08/26
#
 
#########################################################################
#                                                                   	#
#   JavaTest Environment file for JAXR Compatibility Test Suite         #
#                                                                       #
#   Environment specific properties in this file will likely            #
#   have to be modified prior to running the JAXR TCK.                  #
#   Instructions for modifying these properties are contained in this   #
#   file.                                                               #
#                                                                       #
#########################################################################

# Command Macros
ExecJCKTestSameJVMCmd=com.sun.javatest.lib.ExecJCKTestSameJVMCmd
ExecJCKTestOtherJVMCmd=com.sun.javatest.lib.ExecJCKTestOtherJVMCmd

########################################################################
# Environment ts_unix                                                  #
########################################################################

env.ts_unix.description=\
        Run the runtime tests in a separate JVM (process) on Solaris
env.ts_unix.menu=true
env.ts_unix.testsuite=com.sun.ts.lib.harness.TS
env.ts_unix.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${TS_HOME}/lib/tsharness.jar:${TS_HOME}/lib/apiCheck.jar:${TS_HOME}/lib/jaxrtck.jar:${TS_HOME}/lib/javatest.jar:${TS_HOME}/classes:${JAVA_HOME}/jre/lib/rt.jar:${LOCAL_CLASSES} \
        DISPLAY=:0.0 \
        HOME=${user.home} \
        ${JAVA_HOME}/bin/java \
${JAVA_OPTIONS} -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs

########################################################################
# Environment ts_win32                                                 #
########################################################################

env.ts_win32.description=\
        Run the runtime tests in a separate JVM (process) on Win32
env.ts_win32.menu=true
env.ts_win32.testsuite=com.sun.ts.lib.harness.TS
env.ts_win32.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${TS_HOME}\\lib\\tsharness.jar;${TS_HOME}\\lib\\apiCheck.jar;${TS_HOME}\\lib\\jaxrtck.jar;${TS_HOME}\\lib\\javatest.jar;${TS_HOME}\\classes;${JAVA_HOME}\\jre\\lib\\rt.jar;${LOCAL_CLASSES} \
        windir=${windir} \
        SYSTEMROOT=${SYSTEMROOT} \
        ${JAVA_HOME}\\bin\\java ${JAVA_OPTIONS} -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs

########################################################################
#These properties are used by the harness.  "harness.log.port"
#specifies the port that server components use to send logging
#output back to JavaTest.  If the default port # is not available
#on the machine running JavaTest, then you can set it here.
#
#"harness.log.traceflag" is used to turn on/off verbose debugging output
#for the tests.
#
# "harness.executeMode" is used to run the harness in the following modes
# of execution:
#    0 - default (deploy, run, undeploy)
#    1 - deploy only
#    2 - run only
#    3 - undeploy only
#    4 - deploy and run only
#
#  @harness.socket.retry.count - denotes the number of time we should
#           attempt to create a server socket when intilizing a test
#           client.  The socket is used for logging purposes.
########################################################################
harness.temp.directory=${ts.home}
harness.log.port=2000
harness.log.traceflag=false
harness.log.delayseconds=1
harness.executeMode=2
harness.socket.retry.count=10


#####################################################################
## The following properties must be set prior to running the Servlet
## or JSP tests.
##
## These properties must specify the host and port of the web server,
## in which the servlets and JSPs are deployed.
##
## "webServerHost" and "webServerPort" are the settings for the Vendor
## J2EE Web Server 
##
#####################################################################
webServerHost=localhost
webServerPort=8080

deliverable.class=com.sun.ts.lib.deliverable.jaxr.JAXRDeliverable
#################################################################
#
#The following properties must be set prior to running the JAXR
#TCK tests.
# ------------------------------------------------------------------
# The following are Standard Properties for Registry URLs
# ------------------------------------------------------------------
#
# registryURL - standard connection property for publishing.
#               for RegistryServer it is:
#               registryURL = http://localhost:8080/RegistryServer/
#               for ebxml it is:
#               registryURL = http://localhost:8080/omar/registry/soap
# queryManagerURL - standard connection property for querying.
#               for RegistryServer it is:
#               queryManagerURL = http://localhost:8080/RegistryServer/
#               for ebxml it is:
#               queryManagerURL = http://localhost:8080/omar/registry/soap
# ------------------------------------------------------------------
# Depending on whether you are using uddi or ebxml, you need to
# setup up one of the following sets of user accounts.
# UDDI - username/password combination 
# For the RegistryServer you can use the 
# predefined username and passwords. ( or you can create your own).
# The predefined are as follows:
#
#                jaxrUser=testuser
#                jaxrPassword=testuser
#                jaxrUser2=jaxr-sqe
#                jaxrPassword2=jaxrsqe
#
# ebxml -  digital credentials
# if using digital certificates, else set to = "".
# jaxrAlias   identifies the entry in the keystore for this user.
# jaxrAlias2  identifies the entry in the keystore for the second user
# jaxrAliasPassword   identifies the password in the keystore for this user.
# jaxrAlias2Password  identifies the password in the keystore for the second user
# also must set keystore file name and keystore password on command line if using digital 
# certificates
# You must create 2 accounts with the ebxml registry
# And then set the following 
#
#                jaxrAlias=yourAccountUser
#                jaxrAliasPassword=yourAccountUsersPassword
#                jaxrAlias=yourAccountUser-secondAccount
#                jaxrAlias2Password=yourAccountUsersPassword-secondAccount
#
# ---------------------------------------------------------------
#
# jaxrConnectionFactoryLookup  The preferred way for a client to lookup
#                              a JAXR ConnectionFactory is to
#                              use JNDI.  An alternate method to lookup
#                              a JAXR ConnectionFactory is to use the
#                              newInstance static method on the ConnectionFactory
#
#                              0 = use JNDI lookup
#                              1 = use newInstance method.
#
# ---------------------------------------------------------------
#
# jaxrJNDIResource   JAXR ConnectionFactoryReference if JNDI lookup is
#                    being used.  For example:
#                    java:comp/env/eis/JAXR
#
#                    If not using JNDI lookup set to ""
#
# ---------------------------------------------------------------
#
# providerCapabilityLevel the provider must set this to
#              the supported capability level 0 or 1
#
# ---------------------------------------------------------------
# authenticationMethod - the provider supported authentication method
#
#
##############################################################
#  = use newInstance for standalone client environment 
jaxrConnectionFactoryLookup = 1 
# 0 = uddi username/password
# 1 = ebxml credentials
jaxrSecurityCredentialType = 0
#
jaxrWebContext = "web_content"

jaxrJNDIResource = "java:comp/env/eis/JAXR"
# set jaxrAlias if using digital certificates for authorization
jaxrAlias=""
jaxrAlias2=""
jaxrAliasPassword=""
jaxrAlias2Password=""

# UDDI level 0
providerCapability=0

registryURL = 
queryManagerURL = 
jaxrUser=""
jaxrPassword=""

jaxrUser2=""
jaxrPassword2=""

ts_home=${ts.home}

# This is for UDDI
authenticationMethod = "UDDI_GET_AUTHTOKEN"

sigTestClasspath=${local.classes}:${JAVA_HOME}/lib/rt.jar
