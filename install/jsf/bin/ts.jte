#
# Copyright (c) 2006, 2021 Oracle and/or its affiliates. All rights reserved.
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
#                                                                   	
#   JavaTest Environment file for JSF Compatibility Test Suite       
#                                                                       
#   Environment specific properties in this file will likely	 	
#   have to be modified prior to running the JSF TCK. Instructions
#   for modifying these properties are contained in this file.	
#                                                                       
#########################################################################

#########################################################################
## @jte.version  This version denotes the bundle this JTE was
##               originally included with.  The version matches the ID
##               in the bundle name.  It can be used to identify a
##               mismatched JTE file.  This value is filled in during
##               the build process as part of sanitizing the jte file.
#########################################################################
jte.version=@JTE_VERSION@

#########################################################################
# The following properties MUST be set prior to running the
# TCK for JavaServer Faces.
# ---------------------------------
# webServerHost - Specifies the host of the target container
# webServerPort - Specifies the port the container is bound
# webServerHome - Specifies the installation directory of the container
# 
# The web.deployment.handler property must refer to an ANT file containing
# at least a single target named 'deploy'.  When 'ant deploy' is invoked,
# this target will be invoked to handle the deployment of the web
# applications to the target server.
#
# If using a custom handler, copy the handler to TS_HOME/bin/deploy and
# provide only the filename as the property value.
#
# The JSF TCK includes 3 handlers:
#    - none (no deployment occurs)
#    - glassfish (deployment to SJSAS/Glassfish)
#    - tomcat (Tomcat)
#
# jsf.classes - Specifies the JSF API and implementation classes that
#               are under test
#
# jspservlet.classes - Specifies the path to the Servlet & JSP API classes.
#
#
# Here are some example configurations.
#
#  Glassfish V4 Example:
#     webServerHost=localhost
#     webServerPort=8080
#     webServerHome=/sun/glassfish3/glassfish
#     impl.vi=glassfish
#     impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy
#     impl.deploy.timeout.multiplier=20
#     jsf.classes=${webServerHome}/modules/jakarta.faces.jar${pathsep} \
#				  ${webServerHome}/modules/weld-osgi-bundle.jar
#     jspservlet.classes=${webServerHome}/modules/jakarta.servlet.jsp.jar${pathsep} \
#				${webServerHome}/modules/jakarta.servlet.jsp.jstl-api.jar${pathsep} \
#				${webServerHome}/modules/jakarta.servlet.jsp.jstl.jar${pathsep} \
#				${webServerHome}/modules/jakarta.servlet-api.jar${pathsep} \
#				${webServerHome}/modules/jakarta.el.jar${pathsep} \
#				${webServerHome}/modules/jakarta.servlet.jsp-api.jar
#
#########################################################################
webServerHost=localhost
webServerPort=8080
webServerHome=/ri/glassfish3/glassfish
impl.vi=glassfish
impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy
impl.deploy.timeout.multiplier=20
jsf.classes=${webServerHome}/modules/jakarta.faces.jar${pathsep}${webServerHome}/modules/weld-osgi-bundle.jar
jspservlet.classes=${webServerHome}/modules/jakarta.servlet.jsp.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp.jstl.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp.jstl-api.jar${pathsep}${webServerHome}/modules/jakarta.servlet-api.jar${pathsep}${webServerHome}/modules/jakarta.el.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp-api.jar

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
javatest.timeout.factor=2.0

########################################################################
#
# @jimage.dir:       This property specifies the directory where Java 11+
#                    modules will be expanded by the jimage tool for use
#                    in sigTestClasspath
#
# @sigTestClasspath: This property must be set when running signature 
#                    tests.  This property should be set to a list of
#                    jar files and/or directories which contain your 
#                    Java EE and Java SE classes. Paths must be 
#                    separated by the appropriate path separator 
#                    (';' windows, ':' Unixes).
#
########################################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${jsf.classes}${pathsep}${jspservlet.classes}${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming

########################################################################
#These properties must be set to tell the Javatest harness the
#class names of your porting class implementations. By default
#the property settings below point to Sun RI specific classes.  
########################################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL


########################################################################
# Javatest batch mode work directory and report directory, and policy for 
# handling existing work and report directories.  These properties affects
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
######################################################################## 
work.dir=/tmp/JTwork
report.dir=/tmp/JTreport
if.existing.work.report.dirs=overwrite
 
########################################################################
## Common environment for both ts_unix and ts_win32
#
# @command.testExecute - This command is used to execute any test 
#                        clients which are not run inside an
#                        application client container.  For example,
#                        any URL clients or standalone java clients
#                        would be executed with this command.  Some
#                        test directories which make use of this command
#                        are servlet and jsp.
########################################################################
command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
                    CLASSPATH=${JAVA_HOME}/lib/rt.jar${pathsep}\
                    ${LOCAL_CLASSES} \
                    HOME="${user.home}" \
                    windir=${windir} \
                    SYSTEMROOT=${SYSTEMROOT} \
                    ${JAVA_HOME}/bin/java \
                    ${JAVA_OPTIONS} $testExecuteClass $testExecuteArgs

########################################################################
# Environment ts_unix                                              
########################################################################
env.ts_unix.menu=true

########################################################################
# Environment ts_win32						       
########################################################################
env.ts_win32.menu=true

##########################################################################
# Include the TS harness libs - tsharness.jar,
# javatest.jar - the test classes and any tck specific jars.
# DO NOT EDIT
##########################################################################
local.classes=${ts.home}/lib/tsharness.jar${pathsep}\
${ts.home}/lib/jsftck.jar${pathsep}\
${ts.home}/lib/sigtest.jar${pathsep}\
${ts.home}/lib/javatest.jar${pathsep}\
${ts.home}/classes${pathsep}\
${jsf.classes}${pathsep}\
${jspservlet.classes}${pathsep}\
${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}\
${ts.home}/lib/commons-io-2.4.jar${pathsep}\
${ts.home}/lib/commons-lang3-3.3.2.jar${pathsep}\
${ts.home}/lib/commons-codec-1.9.jar${pathsep}\
${ts.home}/lib/commons-collections-3.2.1.jar${pathsep}\
${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}\
${ts.home}/lib/cssparser-0.9.25.jar${pathsep}\
${ts.home}/lib/htmlunit-2.15.jar${pathsep}\
${ts.home}/lib/htmlunit-core-js-2.15.jar${pathsep}\
${ts.home}/lib/httpcore-4.4.9.jar${pathsep}\
${ts.home}/lib/httpclient-4.5.5.jar${pathsep}\
${ts.home}/lib/httpmime-4.5.5.jar${pathsep}\
${ts.home}/lib/nekohtml-1.9.21.jar${pathsep}\
${ts.home}/lib/sac-1.3.jar${pathsep}\
${ts.home}/lib/saxpath.jar${pathsep}\
${ts.home}/lib/jaxen-1.1.6.jar${pathsep}\
${ts.home}/lib/jaxen-core-1.0.jar${pathsep}\
${ts.home}/lib/jdom-1.1.3.jar${pathsep}\
${ts.home}/lib/xercesImpl-2.11.0.jar${pathsep}\
${ts.home}/lib/xalan-2.7.2.jar${pathsep}\
${ts.home}/lib/serializer-2.7.2.jar${pathsep}\
${ts.home}/lib/xml-apis-1.4.01.jar${pathsep}\

##########################################################################
# Final classpath
# DO NOT EDIT 
##########################################################################
ts.classpath=${local.classes}${pathsep}${ant.home}/lib/ant.jar
ts.harness.classpath=${ts.classpath}

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
# of execution (DO NOT EDIT):
#    2 - run only
########################################################################
harness.temp.directory=${ts.home}/tmp
harness.log.port=2000
harness.log.mode=silent
harness.log.traceflag=true
harness.executeMode=2
harness.log.delayseconds=1 
harness.socket.retry.count=10
ts_home=${TS_HOME}

##########################################################################
# Set your local timezone. For valid values, consult your Operating System
# documentation.
##########################################################################
tz=US/Eastern

########################################################################
# This property specifies the type of archive that is generated.
# DO NOT EDIT
########################################################################
build.level=2

##########################################################################
# The alt.schema.dir and alt.dtd.dir properties specifu the location of
# the Schemas used to package the TCK tests (web, Java EE, etc).  These
# properties need not be set if only running the tests.
# DO NOT EDIT
##########################################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas

##########################################################################
# Needed for building/packaging
##########################################################################
deliverable.class=com.sun.ts.lib.deliverable.tck.TCKDeliverable
