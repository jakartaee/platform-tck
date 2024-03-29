#
# Copyright (c) 2008, 2021 Oracle and/or its affiliates. All rights reserved.
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
#   JavaTest Environment file for JTA Test Compatibility Kit (TCK)       
#                                                                       
#   Environment specific properties in this file will likely	 	
#   have to be modified prior to running the JTA TCK. Instructions
#   for modifying these properties are contained in this file.	
#                                                                       
#########################################################################

#########################################################################
# The following properties MUST be set prior to running the JTA TCK:
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
# jta.classes - Specifies the JTA API and implementation classes that
#               are under test
#
# For an example of property settings one might use, assuming we are using
# Glassfish (and assuming Glassfish is installed into the /sun/glassfish6/glassfish 
# directory);  one might have the following property settings:
#     webServerHost=localhost
#     webServerPort=8001
#     webServerHome=/sun/glassfish6/glassfish
#     web.deployment.handler=glassfish.xml
#     jta.classes=${webServerHome}/modules/jakarta.transaction.jar${pathsep}${webServerHome}/modules/jakarta.servlet-api.jar
#     
#########################################################################
webServerHost=localhost
webServerPort=8080
webServerHome=

########################################################################
#
# @jtaJarClasspath: This property must be set when running signature
#                    tests.  This property should be set to the Path
#                    for the JTA API jar.
#
########################################################################
jtaJarClasspath=${webServerHome}/modules/jakarta.transaction-api.jar

jta.classes=${jtaJarClasspath}${pathsep}\
            ${webServerHome}/modules/jakarta.interceptor-api.jar${pathsep}\
            ${webServerHome}/modules/jts.jar${pathsep}\
            ${webServerHome}/modules/weld-osgi-bundle.jar${pathsep}\
            ${webServerHome}/modules/glassfish-naming.jar${pathsep}\
            ${webServerHome}/modules/jakarta.servlet-api.jar${pathsep}\
            ${webServerHome}/modules/bean-validator.jar${pathsep}\
            ${webServerHome}/modules/jakarta.enterprise.cdi-api.jar

tck.classes=${ts.home}/lib/tsharness.jar${pathsep}\
            ${ts.home}/lib/jtatck.jar${pathsep}\
            ${ts.home}/lib/javatest.jar${pathsep}\
            ${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}\
            ${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}\
            ${ts.home}/classes${pathsep}\
            ${ts.home}/lib/sigtest.jar

###############################################################
# @extension.dir - The extension directory for the
#                  server under test (if applicable).  
###############################################################
extension.dir=${webServerHome}/domains/domain1/lib

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

sigTestClasspath=${jta.classes}${pathsep}\
                 ${jimage.dir}/java.base${pathsep}\
                 ${jimage.dir}/java.rmi${pathsep}\
                 ${jimage.dir}/java.sql${pathsep}\
                 ${jimage.dir}/java.naming

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
                    CLASSPATH="${JAVA_HOME}/lib/rt.jar${pathsep}${ts.classpath}" \
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
# Final classpath
# DO NOT EDIT 
##########################################################################
ts.classpath=${jta.classes}${pathsep}${tck.classes}${pathsep}${ts.home}/classes${pathsep}${ant.home}/lib/ant.jar

#classpath used for building the jtatck.jar (DO NOT MODIFY)
#ts.lib.classpath=${ts.classpath}

ts.harness.classpath=${ts.classpath}
javatest.timeout.factor=1.0

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
harness.temp.directory=${TS_HOME}
harness.log.port=2000
harness.log.mode=silent
harness.log.traceflag=true
harness.executeMode=2
harness.log.delayseconds=1 
harness.socket.retry.count=10



####################################################################
# Implementation Property Settings for Vendor and RI.  These properties
# are used when either porting.ts.deploy.class.1 or porting.ts.deploy.class.2
# are set to com.sun.ts.lib.implementation.sun.javaee.glassfish.AutoDeployment.
# Note:  They are are also used when using the "ant deploy" and "ant undeploy"
# targets outside of running tests.
#
# The CTS provides the glassfish implementation out of the box.
#
# Here are the common properties that need to be defined for the common
# implementation functionality:
#
# @impl.vi                    This property must be set to the Vendor implementation
#                                 under test.
#
# @impl.vi.deploy.dir This property must be set to the deploy directory for
#                                 the Vendor implementation.
#
# @impl.deploy.timeout.multiplier  The time it will wait for deployment to
#                                   succeed or fail
#
####################################################################
impl.vi=glassfish
impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy
impl.deploy.timeout.multiplier=240


####################################################################
# The following properties are used as part of the restart.server
# ant target which is invoked at the end of config.vi.  
#
# @s1as.admin.user          - The SJSAS asadmin user id
# @s1as.admin.passwd        - The SJSAS asadmin user password
# @s1as.admin               - The SJSAS admin command
# @s1as.asenv.loc           - location of asenv.conf or asenv.bat
# @s1as.domain.dir          - Points to where your domains are installed.
# @s1as.domain.name         - The SJSAS domain being used
# @s1as.domain              - The SJSAS domain path being used
# @sjsas.master.password    - Used to create a node agent only
#                             applicable to EE.  Defaults to changeit. This
#                             can be changed at EE install time.
####################################################################
s1as.admin.user=admin
s1as.admin.passwd=
s1as.admin=${webServerHome}/bin/asadmin
s1as.asenv.loc=${webServerHome}/config
s1as.domain.dir=${webServerHome}/domains
s1as.domain.name=domain1
s1as.domain=${s1as.domain.dir}/${s1as.domain.name}
sjsas.master.password=changeit


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

##########################################################################
## Deliverables wanting ts.* packaging tasks to add extension list
## attributes to the manifest files must set this property to true.
##########################################################################
#create.manifest.extension.entries=

##########################################################################
## Deliverables must set this property to the name of the deliverable
## specific library jar file (iff create.manifest.extension.entries is set  
## to true)
##########################################################################
tslib.name=jtatck
