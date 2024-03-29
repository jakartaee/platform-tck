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

########################################################
#                                                                   																
#   JavaTest Environment file for JSTL Compatibility Test Suite 
#
#   Environment specific properties in this file will likely
#   have to be modified prior to running the JSTL TCK. Instruction 
#   for modifying these properties are contained in this file.
#
########################################################

########################################################
# The following properties MUST be set prior to running the
# TCK for JSTL.
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
# Example: Glassfish V3 Settings would be
#
#      webServerHome=/ri/glassfishv3/glassfish
#      webServerHost=localhost
#      webServerPort=8080
#		impl.vi=glassfish.xml
#		impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy
#		impl.deploy.timeout.multiplier=20
#
########################################################
webServerHome=/ri/glassfish3/glassfish
webServerHost=localhost
webServerPort=8001
impl.vi=glassfish
impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy
impl.deploy.timeout.multiplier=20

########################################################
# Database specific properties
#
#  jstl.db.name - Name of the database (optional)
#  jstl.db.server - The server on which the database is running (optional)
#  jstl.db.port - The port on which the database is accepting connections (optional)
#  jstl.db.driver - JDBC driver implementation class
#  jstl.db.url - JDBC URL specification to allow access to the target backend
#  jstl.db.user - A valid user for the target backend
#  jstl.db.password - The password for the specified user
#
# As one example of some possible property settings; assuming we are
# running against a Glassfish install, one might use the following property settings:
#
#    jstl.db.name=derbyDB
#    jstl.db.server=localhost
#    jstl.db.port=1527
#    jstl.db.url=jdbc:derby://${jstl.db.server}:${jstl.db.port}/${jstl.db.name};create=true
#    jstl.db.driver=org.apache.derby.jdbc.ClientDriver
#    jstl.db.user=cts1
#    jstl.db.password=cts1
#
########################################################
jstl.db.name=derbyDB
jstl.db.server=localhost
jstl.db.port=1527
jstl.db.url=jdbc:derby://${jstl.db.server}:${jstl.db.port}/${jstl.db.name};create=true
jstl.db.driver=org.apache.derby.jdbc.ClientDriver
jstl.db.user=cts1
jstl.db.password=cts1


########################################################
# JSTL API/Implementation classes, JDBC, and JSP/Servlet classes
# JSP/Servlet classes must reference the Servlet 2.5/JSP 2.1 API classes
#
# Example:(Glassfish V3)
#
#    jdbc.classes=${webServerHome}/../javadb/lib/derbyclient.jar
#
#    jspservlet.classes=${webServerHome}/modules/jakarta.servlet-api.jar${pathsep} \
#                        ${webServerHome}/modules/jakarta.servlet.jsp.jar${pathsep} \
#						  ${webServerHome}/modules/jakarta.servlet.jsp-api.jar${pathsep} \
#						  ${webServerHome}/modules/jakarta.el-api.jar${pathsep} \
#						  ${webServerHome}/modules/jakarta.el.jar
#
#    jstl.classes=${webServerHome}/modules/jakarta.servlet.jsp.jstl-api.jar${pathsep}\
#    				${webServerHome}/modules/jakarta.servlet.jsp.jstl.jar
#
########################################################
jdbc.classes=${webServerHome}/../javadb/lib/derbyclient.jar${pathsep}${webServerHome}/../javadb/lib/derbyshared.jar.jar${pathsep}${webServerHome}/../javadb/lib/derbytools.jar
jspservlet.classes=${webServerHome}/modules/jakarta.servlet-api.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp-api.jar${pathsep}${webServerHome}/modules/jakarta.el-api.jar${pathsep}${webServerHome}/modules/jakarta.el.jar
jstl.classes=${webServerHome}/modules/jakarta.servlet.jsp.jstl.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp.jstl-api.jar


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


########################################################
# Includes:
# - tsharness.jar
# - javatest.jar
# - TCK specific JARs
# - Test classes
# - Any additional classes that may be needed to run the TCK
########################################################
local.classes = ${ts.home}/lib/tsharness.jar\
${pathsep}${ts.home}/lib/jstltck.jar\
${pathsep}${ts.home}/lib/javatest.jar\
${pathsep}${ts.home}/classes\
${pathsep}${jstl.classes}\
${pathsep}${jspservlet.classes}\
${pathsep}${jdbc.classes}${pathsep}\
${ts.home}/lib/commons-httpclient-3.1.jar\
${pathsep}${ts.home}/lib/commons-codec-1.9.jar\
${pathsep}${ts.home}/lib/commons-logging-1.1.3.jar\
${pathsep}${ts.home}/lib/sigtest.jar

########################################################
# The JAVA EE server jar, the TS harness libs -
# tsharness.jar, parser.jar and javatest.jar - the test classes and any tck
# specific jars.
########################################################
ts.classpath=${local.classes}${pathsep}${ant.home}/lib/ant.jar
ts.harness.classpath=${ts.classpath}
javatest.timeout.factor=1.0

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
########################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${ts.home}/classes${pathsep}${jstl.classes}${pathsep}${jspservlet.classes}${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming

########################################################
#  Common environment for both ts_unix and ts_win32
#
# @command.testExecute - This command is used to execute any test
#                        clients which are not run inside an
#                        application client container.  For example,
#                        any URL clients or standalone java clients
#                        would be executed with this command.  Some
#                        test directories which make use of this command
#                        are servlet and jsp.
########################################################
command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
                    CLASSPATH=${JAVA_HOME}/lib/rt.jar${pathsep}\
                    ${LOCAL_CLASSES} \
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
#These properties are used by the Javatest harness. "harness.log.port"
#specifies the port that server components use to send logging
#output back to JavaTest.  If the default port # is not available
#on the machine running JavaTest, then you can set it here. 
#
#"harness.log.traceflag" is used to turn on/off verbose debugging output
#for the tests.
#
########################################################
harness.temp.directory=${ts.home}/tmp
harness.log.port=2000
harness.log.mode=silent
harness.log.traceflag=true
harness.executeMode=2
harness.log.delayseconds=1
harness.socket.retry.count=10
ts_home=${TS_HOME}

########################################################
#These properties must be set to tell the Javatest harness the
#class names of your porting class implementations. By default
#the property settings below point to Sun RI specific classes.  
########################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL

########################################################
#build level
#2: compile and build component archives (war's)
########################################################
build.level=2

########################################################
# Default client used for tests
########################################################
test.client=JSTLClient.java

########################################################
# Set your local timezone. For valid values, consult your Operating System
# documentation.
########################################################
tz=US/Eastern

########################################################
# Needed for building/packaging
########################################################
deliverable.class=com.sun.ts.lib.deliverable.tck.TCKDeliverable

########################################################
#These properties are used by the harness.
#
# "harness.executeMode" is used to run the harness.(Do not Modify)
########################################################
harness.executeMode=2

########################################################
# The alt.schema.dir and alt.dtd.dir properties specifu the location of
# the Schemas used to package the TCK tests (web, javaee, etc).
# DO NOT EDIT
########################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas
