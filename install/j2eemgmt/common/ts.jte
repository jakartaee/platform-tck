#
# Copyright (c) 2002, 2018 Oracle and/or its affiliates. All rights reserved.
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
 
#########################################################################
#                                                                   	#
#   JavaTest Environment file for J2EE Compatibility Test Suite         #
#                                                                       #
#   Environment specific properties in this file will likely	 	#
#   have to be modified prior to running the J2EE-TS.                   #
#   Instructions for modifying these properties are contained in this 	#
#   file.								#
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
        CLASSPATH=${TS_HOME}/lib/harness.jar:${TS_HOME}/lib/javatest.jar:${TS_HOME}/classes:${JAVA_HOME}/jre/lib/rt.jar:${LOCAL_CLASSES} \
        DISPLAY=:0.0 \
        HOME=${user.home} \
        ${JAVA_HOME}/bin/java ${JAVA_OPTIONS} $testExecuteClass $testExecuteArgs

########################################################################
# Environment ts_win32						       #
########################################################################
env.ts_win32.description=\
        Run the runtime tests in a separate JVM (process) on Win32
env.ts_win32.menu=true
env.ts_win32.testsuite=com.sun.ts.lib.harness.TS
env.ts_win32.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${TS_HOME}\\lib\\harness.jar;${TS_HOME}\\lib\\javatest.jar;${TS_HOME}\\classes;${JAVA_HOME}\\jre\\lib\\rt.jar;${LOCAL_CLASSES} \
	windir=${windir} \
	SYSTEMROOT=${SYSTEMROOT} \
        ${JAVA_HOME}\\bin\\java ${JAVA_OPTIONS} $testExecuteClass $testExecuteArgs

########################################################################
#These properties are used by the Javatest harness. "harness.log.port"
#specifies the port that server components use to send logging
#output back to JavaTest.  If the default port # is not available
#on the machine running JavaTest, then you can set it here. 
#
#"harness.log.traceflag" is used to turn on/off verbose debugging output
#for the tests.
#
########################################################################
harness.temp.directory=${TS_HOME}
harness.log.port=2000
harness.log.traceflag=false
harness.log.mode=silent
harness.log.delayseconds=1 

###############################################################
#These properties must be set to tell the Javatest harness the
#class names of your porting class implementations. By default
#the property settings below point to Sun RI specific classes.  
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL

#################################################################
#The following properties must be set prior to running the J2EEMGMT
#TCK tests.
#
#These web server properties must specify the host and port of
#the web server in which the J2EEMGMT TCK test servlets are deployed.
#
#################################################################
webServerHost=localhost
webServerPort=8080

#################################################################
#The following properties must be set prior to running the J2EEMGMT
#TCK tests. These properties are used for the J2EEMGMT security tests
#which test HTTP Basic Authentication.
#
#################################################################
user=j2ee
password=j2ee
authuser=javajoe
authpassword=javajoe

###############################################################
#Test suite base directory
###############################################################
ts_home=${TS_HOME}

###############################################################
#These properties are needed for signaturetest.
###############################################################
sigTestClasspath=${J2EE_HOME}$/lib$/j2ee.jar$:${JAVA_HOME}$/jre$/lib$/rt.jar
