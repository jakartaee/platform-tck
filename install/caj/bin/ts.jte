#
# Copyright (c) 2009, 2021 Oracle and/or its affiliates. All rights reserved.
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

###################################################################################
#                                                                   	          #
#   JavaTest Environment file for Common Annotations Compatibility Test Suite     #
#                                                                                 #
#   Environment specific properties in this file will likely	 	              #
#   have to be modified prior to running the Common Annotations TCK. Instructions #
#   for modifying these properties are contained in this file.	 	              #
#                                                                                 #
###################################################################################


####################################################################
## When installing TCK/VI on Windows, users must install TCK and
## the VI on the same drive.  Also note that you should never
## specify drive letters in any path defined in this properties
## file and all paths should be specified in the unix format only
## (ie. the "/" should be used instead of "\".
####################################################################


###########################################################################
# @local.classes  should contain all Vendor specific jars/classes
###########################################################################
local.classes=

####################################################################
# The jars/classes for the TS harness/javatest and the test classes
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the CTS tests
# @ts.run.classpath     --  Classpath required by the vendor
#                           appclient container.
####################################################################
ts.harness.classpath=${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/cajtck.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/lib/jaxb-api.jar${pathsep}${ts.home}/lib/jaxb-xjc.jar${pathsep}${ts.home}/lib/jaxb-libs.jar${pathsep}${ts.home}/lib/jaxb-impl.jar

ts.classpath=${local.classes}${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/cajtck.jar

ts.run.classpath=${local.classes}${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.home}/lib/cajtck.jar${pathsep}${ts.home}/classes

########################################################################
## Javatest batch mode work directory and report directory, and policy for
## handling existing work and report directories.  These properties affects
## runclient and report targets, but not gui target.
## To disable generating test report, unset report.dir, or set it to "none"
## either here or from command line, as in the following command:
##               ant runclient -Dreport.dir="none"
##
# @if.existing.work.report.dirs specifies how existing work.dir and
# @report.dir will be handled, and it must be one of the following values:
# overwrite     overwrites all content in work.dir and report.dir
# backup        moves all content in work.dir and report.dir to
#               work.dir_time_day_bak and report.dir_time_day_bak,
#               respectively
# append        reuses and preserves the existing work.dir and report.dir
# auto          lets the build files decide which mode to use
#               (overwrite, backup or append).  the value is determined
#               like this:
#                  if.existing.work.report.dirs == auto
#                    if in CTS workspace
#                      if.existing.work.report.dirs = overwrite
#                    else we are in a distribution bundle
#                      if.existing.work.report.dirs = append
#                    end if
#                  else
#                    if.existing.work.report.dirs = value in this file
#                  end if
########################################################################
work.dir=/tmp/JTwork
report.dir=/tmp/JTreport
if.existing.work.report.dirs=auto

###############################################################
#These properties must be set to tell the Javatest harness the
#class names of your porting class implementations. By default
#the property settings below point to Sun RI specific classes.  
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL

#######################################################################
# default deliverable class
#######################################################################
deliverable.class=com.sun.ts.lib.deliverable.standalone.StandaloneDeliverable

########################################################################
## These properties are used by the harness.  "harness.log.port"
## specifies the port that server components use to send logging
## output back to JavaTest.  If the default port # is not available
## on the machine running JavaTest, then you can set it here.
##
# @harness.temp.directory directory location used by the
#                         harness to store temporary files
#
# @harness.log.port  the port the harness listens on for log mesages
#                    from remote clients
#
# @harness.log.traceflag  used to turn on/off verbose debugging output
#                         for the tests.
#
# @harness.log.delayseconds  Number of seconds to delay to allow
#                            reporting from remote clients to finish.
#
# @harness.executeMode  used to run the harness in the following modes
#                       of execution:  2 - run only
#
#  @harness.socket.retry.count - denotes the number of time we should
#           attempt to create a server socket when intilizing a test
#           client.  The socket is used for logging purposes.
########################################################################
harness.temp.directory=${ts.home}/tmp
harness.log.port=2000
harness.log.traceflag=false
harness.log.delayseconds=1
harness.executeMode=2
harness.socket.retry.count=10

###############################################################
# @tz - your local timezone. For valid values, consult your
#       Operating System documentation.
###############################################################
tz=US/Eastern

########################################################################
# @javatest.timeout.factor This property specifies the scale factor used by
# Javatest to adjust the time JavaTest will wait for a given test to
# complete before returning failure.  For instance if the default test timeout
# is 5 minutes, this value will be multiplied by 5 minutes to determine
# the total timeout delay.  Note: this value only works with Javatest's
# batch mode (runclient).  When using the Javatest GUI users must change
# this timeout factor in the GUI. Configure -> Edit Configuration -> View
# -> choose Standard Values -> select tab Execution ->  set Time Factor.
########################################################################
javatest.timeout.factor=1.0

#######################################################################
#build level
#######################################################################
#1: compile only
#2: compile and build component archives (e.g., jar's, war's)
build.level=1

###############################################################
# @ts.display -- location to display CTS output on Unix
###############################################################
ts.display=:0.0

########################################################################
## Common environment for both ts_unix and ts_win32
########################################################################
# This execute line runs against the VI implementation
command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${ts.run.classpath} \
        DISPLAY="${ts.display}" \
		HOME="${user.home}"  \
		windir=${windir} \
		SYSTEMROOT=${SYSTEMROOT} \
        ${JAVA_HOME}/bin/java \
        --module-path ${J2EE_HOME}/lib/bootstrap --add-modules ALL-MODULE-PATH \
		-Djava.security.policy="${bin.dir}/harness.policy" \
		-Djava.security.manager \
		-Ddeliverable.class=${deliverable.class} \
        $testExecuteClass $testExecuteArgs

#########################################################################
## Environment for ts_unix
## The test execution commands inherit from common environment
## defined above: testExecute.
## If you need to override it, uncomment it in the following section.
#########################################################################
env.ts_unix.menu=true
##env.ts_unix.command.testExecute=

########################################################################
## Environment for ts_win32
## The test execution commands inherit from common environment
## defined above: testExecute.
## If you need to override it, uncomment it in the following section.
########################################################################
env.ts_win32.menu=true
##env.ts_win32.command.testExecute=

#########################################################################
# @jimage.dir:       This property specifies the directory where Java 11+
#                    modules will be expanded by the jimage tool for use
#                    in sigTestClasspath
# @sigTestClasspath: This property must be set when running signature 
#                    tests.  This property should be set to a list of
#                    jar files and/or directories which contain your 
#                    Java EE and Java SE classes. Paths must be 
#                    separated by the appropriate path separator 
#                    (';' windows, ':' Unixes).
#########################################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${local.classes}${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming

###############################################################
## This command allows the test clients to be run in
## the same JVM as JavaTest in this case.  This mode can be
## enabled by passing "-Dsame.jvm=true" to the runclient or gui
## targets.  NOTE: This option is only to be used for sanity
## checking and not when running the TCK for compatibility.
###############################################################
command.testExecuteSameJVM=com.sun.ts.lib.harness.ExecuteTSTestSameJVMCmd \
        $testExecuteClass $testExecuteArgs

###############################################################
## This allows the testing of the various common annotations that
## are supported by a TCK. 
## For those annotations that are not supported, the value
## should be set to false.
###############################################################
#jakarta.annotation.Generated
ca.sig.generated=true

#jakarta.annotation.ManagedBean
ca.sig.managedbean=true

#jakarta.annotation.PostConstruct
ca.sig.postconstruct=true

#jakarta.annotation.PreDestroy
ca.sig.predestroy=true

#jakarta.annotation.Priority
ca.sig.priority=true

#jakarta.annotation.Resource
ca.sig.resource=true

#jakarta.annotation.Resources
ca.sig.resources=true

#jakarta.annotation.security.DeclareRoles
ca.sig.securitydeclareroles=true

#jakarta.annotation.security.DenyAll
ca.sig.securitydenyall=true

#jakarta.annotation.security.PermitAll
ca.sig.securitypermitall=true

#jakarta.annotation.security.RolesAllowed
ca.sig.securityrolesallowed=true

#jakarta.annotation.security.RunAs
ca.sig.securityrunas=true

#jakarta.annotation.sql.DataSourceDefinition
ca.sig.sqldatasourcedefinition=true

#jakarta.annotation.sql.DataSourceDefinitions
ca.sig.sqldatasourcedefinitions=true
