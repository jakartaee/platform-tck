#
# Copyright (c) 2013, 2022 Oracle and/or its affiliates and others.
# All rights reserved.
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

########################################################
#                                                                   	
#   JavaTest Environment file for EL Compatibility Test Suite       
#                                                                       
#   Environment specific properties in this file will likely	 	
#   have to be modified prior to running the EL TCK. Instructions
#   for modifying these properties are contained in this file.	
#                                                                       
########################################################

########################################################
# The following properties MUST be set prior to running the
# TCK for Expression Language.
# ---------------------------------
# el.classes - Specifies the EL API and implementation jars(They do not need to be 
#					inside a container.
#
#   Example:(No Container)
#		el.classes=/el-impl.jar${pathsep}/el-api.jar 
#
#   Example:(Glassfish 4)
#       el.classes=/ri/glassfish3/glassfish/modules/jakarta.el.jar
#
#
########################################################
el.classes=

########################################################
#     This property is used to point to the location of the 
#     implementation of VariableMapper. 
#
#     variable.mapper=com.sun.el.lang.VariableMapperImpl
#
########################################################
variable.mapper=org.glassfish.expressly.lang.VariableMapperImpl

########################################################
#
# @jimage.dir:       This property specifies the directory where Java 11+
#                    modules will be expanded by the jimage tool for use
#                    in sigTestClasspath
#
# @sigTestClasspath: This property must be set when running signature 
#                    tests.  This property should be set to a list of
#                    jar files and/or directories which contain your 
#                    Java EE and Java SE classes. 
#
#   Examples:
#       Solaris/Linux/Windows Java 8:
#       sigTestClasspath=${el.classes}${pathsep}${java.home}/lib/rt.jar
# 
#       MacOS Java 8:
#       sigTestClasspath=${el.classes}${pathsep}${java.home}/jre/lib/rt.jar
# 
#       Solaris/Linux/Windows/MacOs Java 9:
#       sigTestClasspath=${el.classes}${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming
#
########################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${el.classes}${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming


########################################################
# These properties must be set to tell the Javatest harness the
# class names of your porting class implementations. By default
# the property settings below point to Sun RI specific classes.  
########################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL


########################################################
# Javatest batch mode work directory and report directory, and policy for 
# handling existing work and report directories.  These properties affect
# runclient and report targets, but not gui target.
# To disable generating test report, unset report.dir, or set it to "none" 
# either here or from command line, as in the following command:
#               ant runclient -Dreport.dir="none"
#
# if.existing.work.report.dirs specifies how existing work.dir and
# report.dir will be handled, and it must be one of the following values:
# overwrite     overwrites all content in work.dir and report.dir
# backup        moves all content in work.dir and report.dir to
#               work.dir_time_day_bak and report.dir_time_day_bak, 
#               respectively 
# append        reuses and preserves the existing work.dir and report.dir
######################################################## 
work.dir=/tmp/JTwork_EL
report.dir=/tmp/JTreport_EL
if.existing.work.report.dirs="append"

########################################################
## Common environment for both ts_unix and ts_win32
#
# @command.testExecute - This command is used to execute any test 
#                        	clients which are not run inside an application client container.  
#						  	For example,
#                        	any URL clients or standalone java clients would be executed with this 
#							command.  Some test directories which make use of this command
#                        	are servlet and jsp.
########################################################
command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
                    CLASSPATH=${LOCAL_CLASSES} \
                    HOME="${user.home}" \
                    windir=${windir} \
                    SYSTEMROOT=${SYSTEMROOT} \
                    ${JAVA_HOME}/bin/java \
                    ${JAVA_OPTIONS} $testExecuteClass $testExecuteArgs


########################################################
# Environment ts_unix                                              
########################################################
env.ts_unix.menu=true


########################################################
# Environment ts_win32						       
########################################################
env.ts_win32.menu=true


########################################################
# Include the TS harness libs - tsharness.jar,
# javatest.jar - the test classes and any tck specific jars.
# DO NOT EDIT
########################################################
local.classes=${ts.home}/lib/tsharness.jar${pathsep}\
              ${ts.home}/lib/eltck.jar${pathsep}\
              ${el.classes}${pathsep}\
              ${ts.home}/lib/sigtest.jar${pathsep}\
              ${ts.home}/lib/javatest.jar${pathsep}\
              ${ts.home}/classes

########################################################
# Final classpath
# DO NOT EDIT 
########################################################
ts.classpath=${local.classes}${pathsep}${ant.home}/lib/ant.jar
ts.harness.classpath=${ts.classpath}${pathsep}\
                     ${ts.home}/lib/jaxb-api.jar${pathsep}\
                     ${ts.home}/lib/jaxb-impl.jar${pathsep}\
                     ${ts.home}/lib/jaxb-xjc.jar${pathsep}\
                     ${ts.home}/lib/jdom-1.1.3.jar${pathsep}

javatest.timeout.factor=1.0

########################################################
# These properties are used by the harness.  "harness.log.port"
# specifies the port that server components use to send logging
# output back to JavaTest.  If the default port # is not available
# on the machine running JavaTest, then you can set it here.
# 
# "harness.log.traceflag" is used to turn on/off verbose debugging output
# for the tests.
#
# "harness.executeMode" is used to run the harness in the following modes
# of execution (DO NOT EDIT):
#    2 - run only
########################################################
harness.temp.directory=${TS_HOME}
harness.log.port=2000
harness.log.mode=silent
harness.log.traceflag=false
harness.executeMode=2
harness.log.delayseconds=1 
harness.socket.retry.count=10
ts_home=${TS_HOME}

########################################################
# Set your local timezone. For valid values, consult your Operating System
# documentation.
########################################################
tz=US/Eastern

########################################################
# This property specifies the type of archive that is generated.
# DO NOT EDIT
########################################################
build.level=2

########################################################
# Needed for building/packaging
########################################################
deliverable.class=com.sun.ts.lib.deliverable.el.ELDeliverable

