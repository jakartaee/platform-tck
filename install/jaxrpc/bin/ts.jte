#
# Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.
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

#########################################################################
#                                                                   	#
#   JavaTest Environment file for JAXRPC Compatibility Test Suite       #
#                                                                       #
#   Environment specific properties in this file will likely	 	    #
#   have to be modified prior to running the JAXRPC TCK. Instructions	#
#   for modifying these properties are contained in this file.	 	    #
#                                                                       #
#########################################################################

# Command Macros
ExecJCKTestSameJVMCmd=com.sun.javatest.lib.ExecJCKTestSameJVMCmd
ExecJCKTestOtherJVMCmd=com.sun.javatest.lib.ExecJCKTestOtherJVMCmd

 
########################################################################
# Environment ts_unix                                              #
########################################################################
env.ts_unix.description=\
        Run the runtime tests in a separate JVM (process) on Solaris
env.ts_unix.menu=true
env.ts_unix.testsuite=com.sun.ts.lib.harness.TS
env.ts_unix.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${TS_HOME}/lib/tsharness.jar:${TS_HOME}/lib/apiCheck.jar:${TS_HOME}/lib/jaxrpctck.jar:${TS_HOME}/lib/javatest.jar:${TS_HOME}/classes:${JAVA_HOME}/jre/lib/rt.jar:${LOCAL_CLASSES} \
        DISPLAY=:0.0 \
        HOME=${user.home} \
        ${JAVA_HOME}/bin/java ${JAVA_OPTIONS} -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs

########################################################################
# Environment ts_win32						       #
########################################################################
env.ts_win32.description=\
        Run the runtime tests in a separate JVM (process) on Win32
env.ts_win32.menu=true
env.ts_win32.testsuite=com.sun.ts.lib.harness.TS
env.ts_win32.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${TS_HOME}\\lib\\tsharness.jar;${TS_HOME}\\lib\\apiCheck.jar;${TS_HOME}\\lib\\jaxrpctck.jar;${TS_HOME}\\lib\\javatest.jar;${TS_HOME}\\classes;${JAVA_HOME}\\jre\\lib\\rt.jar;${LOCAL_CLASSES} \
	windir=${windir} \
	SYSTEMROOT=${SYSTEMROOT} \
        ${JAVA_HOME}\\bin\\java ${JAVA_OPTIONS} -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs

########################################################################
## These properties are used by the harness.  "harness.log.port"
## specifies the port that server components use to send logging
## output back to JavaTest.  If the default port # is not available
## on the machine running JavaTest, then you can set it here.
##
## "harness.log.traceflag" is used to turn on/off verbose debugging output
## for the tests.
##
## "harness.executeMode" is used to run the harness in the following modes
## of execution:
##    0 - default (deploy, run, undeploy)
##    1 - deploy only
##    2 - run only
##    3 - undeploy only
##    4 - deploy and run only
##
#  @harness.socket.retry.count - denotes the number of time we should
#           attempt to create a server socket when intilizing a test
#           client.  The socket is used for logging purposes.
########################################################################
harness.temp.directory=${ts.home}
harness.log.port=2000
harness.log.traceflag=true
harness.log.delayseconds=1
harness.executeMode=2
harness.socket.retry.count=10

###############################################################
#These properties must be set to tell the Javatest harness the
#class names of your porting class implementations. By default
#the property settings below point to Sun RI specific classes.  
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.jaxrpc.class.1=com.sun.ts.lib.implementation.sun.jaxrpc.SunRIJAXRPC


####################################################################
#The following properties must be set prior to running the JAXRPC
#TCK tests.
#
#These web server properties must specify the host and port of
#the web server in which the JAXRPC TCK test servlets are deployed.
####################################################################
webServerHost=localhost
webServerPort=8080
secureWebServerPort=8443

#################################################################
#The following properties must be set prior to running the JAXRPC
#TCK tests. These properties are used for the JAXRPC security tests
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
ts_home=${ts.home}

#######################################################################
#jaxrpc.mode is used by jaxrpc tests to know whether to use the
#J2EE Programming Model for service and port access or the
#JAX-RPC Programming model.
#
# jaxrpc.mode=jaxrpc	JAX-RPC Programming Model
# jaxrpc.mode=j2ee	J2EE Programming Model
#######################################################################
jaxrpc.mode=jaxrpc

sigTestClasspath=${local.classes}:${JAVA_HOME}/lib/rt.jar
platform.mode=standalone


########################################################################
## @javatest.timeout.factor This property specifies the scale factor used by
## Javatest to adjust the time JavaTest will wait for a given test to
## complete before returning failure.  For instance if the default test timeout
## is 5 minutes, this value will be multiplied by 5 minutes to determine
## the total timeout delay.  Note: this value only works with Javatest's
## batch mode (runclient).  When using the Javatest GUI users must change
## this timeout factor in the GUI. Configure -> Edit Configuration -> View
## -> choose Standard Values -> select tab Execution ->  set Time Factor.
#########################################################################
javatest.timeout.factor=2.0

webServerHost=localhost
webServerPort=8080
webServerHome=/vi/glassfish5/glassfish
impl.vi=glassfish
impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy
impl.deploy.timeout.multiplier=20

########################################################################
## Javatest batch mode work directory and report directory, and policy for
## handling existing work and report directories.  These properties affects
## runclient and report targets, but not gui target.
## To disable generating test report, unset report.dir, or set it to "none"
## either here or from command line, as in the following command:
##               ant runclient -Dreport.dir="none"
##
## if.existing.work.report.dirs specifies how existing work.dir and
## report.dir will be handled, and it must be one of the following values:
## overwrite     overwrites all content in work.dir and report.dir
## backup        moves all content in work.dir and report.dir to
##               work.dir_time_day_bak and report.dir_time_day_bak,
##               respectively
## append        reuses and preserves the existing work.dir and report.dir
#########################################################################
work.dir=/tmp/JTwork
report.dir=/tmp/JTreport
if.existing.work.report.dirs=overwrite

########################################################################
## Environment ts_unix
#########################################################################
env.ts_unix.menu=true

#########################################################################
## Environment ts_win32
#########################################################################
env.ts_win32.menu=true

##########################################################################
## Set your local timezone. For valid values, consult your Operating System
## documentation.
###########################################################################
tz=US/Eastern

#########################################################################
## This property specifies the type of archive that is generated.
## DO NOT EDIT
#########################################################################
build.level=2

###########################################################################
## The alt.schema.dir and alt.dtd.dir properties specifu the location of
## the Schemas used to package the TCK tests (web, Java EE, etc).  These
## properties need not be set if only running the tests.
## DO NOT EDIT
###########################################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas

