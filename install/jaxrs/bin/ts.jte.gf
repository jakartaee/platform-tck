#
# Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
#   JavaTest Environment file for Jakarta Restful Web Services          #
#   Compatibility Test Suite                                            #
#                                                                       #
#   Environment specific properties in this file will likely	 	    #
#   have to be modified prior to running the Jakarta Restful Web        #
#   Services TCK. Instructions	                                        #
#   for modifying these properties are contained in this file.	 	    #
#                                                                       #
#########################################################################

########################################################################
## Javatest batch mode work directory and report directory, and policy for
## handling existing work and report directories.  These properties affects
## runclient and report targets, but not gui target.
## To disable generating test report, unset report.dir, or set it to "none"
## either here or from command line, as in the following command:
##               ant runclient -Dreport.dir="none"
##
# @work.dir     The directory used to store Javatest test results and test
#               information.
# @report.dir   The directory used to store Javatest summary reports of
#               test results.
# @if.existing.work.report.dirs specifies how existing work.dir and
# report.dir will be handled, and it must be one of the following values:
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


###################################################################
###################################################################
###################################################################
##  RI SPECIFIC PROPERTIES LIVE BELOW
###################################################################
###################################################################
###################################################################

###################################################################
## When installing Jakarta Restful Web Services TCK/RI on Windows,
## users must install Jakarta Restful Web Services-TCK and
## the RI on the same drive.  Also note that you should never
## specify drive letters in any path defined in this properties
## file.
##
# @ts.display -- location to display Jakarta Restful Web Services 
#   TCK output on Unix
###############################################################
ts.display=:0.0


###############################################################
# @tz - your local timezone. For valid values, consult your
#       Operating System documentation.
###############################################################
tz=US/Eastern


##########################################################################
# @alt.dtd.dir
# @alt.schema.dir specify the location of the Schemas used to package
#                 the TCK tests (web, j2ee, etc).  These properties
#                 need not be set if only running the tests.
# DO NOT EDIT
##########################################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas

###############################################################
## Classpath properties required by Jakarta Restful Web Services TCK:
# @ts.run.classpath     --  Classpath required by Jakarta Restful Web Services impl
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the Jakarta Restful Web Services tests
###############################################################
ts.run.classpath=${jaxrs_impl.classes}${pathsep}${jaxrs.classes}${pathsep}${servlet.classes}

ts.harness.classpath=${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/lib/jaxrstck.jar${pathsep}${ts.home}/lib/jaxb-xjc.jar

apache.commons.classes=${pathsep}${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}${ts.home}/lib/commons-codec-1.9.jar
local.classes=${ts.harness.classpath}${pathsep}${ts.home}/lib/jaxrstck.jar${pathsep}${ts.home}/classes${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.home}/lib/javaee-api-6.0.jar:${apache.commons.classes}

ts.classpath=${local.classes}${pathsep}${ts.run.classpath}

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
        CLASSPATH=${ts.harness.classpath}:${ts.home}/classes:\
                  ${JAVA_HOME}/../lib/tools.jar:\
                  ${ts.home}/lib/commons-httpclient-3.1.jar:\
                  ${ts.home}/lib/commons-logging-1.1.3.jar:\
                  ${ts.home}/lib/commons-codec-1.9.jar:\
                  ${ts.home}/lib/sigtest.jar:\
                  ${jaxrs.classes}:\
                  ${jaxrs_impl.classes} \
        DISPLAY=${ts.display} \
        HOME="${user.home}" \
        windir=${windir} \
        SYSTEMROOT=${SYSTEMROOT} \
        PATH="${javaee.home}/nativelib" \
        ${JAVA_HOME}/bin/java \
        -Dcts.tmp=$harness.temp.directory \
        -Djava.protocol.handler.pkgs=javax.net.ssl \
        -Djavax.net.ssl.keyStore=${bin.dir}/certificates/clientcert.jks \
        -Djavax.net.ssl.keyStorePassword=changeit \
        -Djavax.net.ssl.trustStore=${javaee.home}/domains/domain1/config/cacerts.jks \
        -Djava.endorsed.dirs=${s1as.java.endorsed.dirs} \
        -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs


#########################################################################
## Environment for ts_unix
## 3 test execution commands inherit from common environment
## defined above: testExecuteAppClient2, testExecuteAppClient, and
## testExecute. If you need to override them, uncomment them in the
## following section.
#########################################################################
env.ts_unix.menu=true
##env.ts_unix.command.testExecute=
##env.ts_unix.command.testExecuteAppClient=
##env.ts_unix.command.testExecuteAppClient2=


########################################################################
## Environment for ts_win32
## 3 test execution commands inherit from common environment
## defined above: testExecuteAppClient2, testExecuteAppClient, and
## testExecute. If you need to override them, uncomment them in the
## following section.
########################################################################
env.ts_win32.menu=true
##env.ts_win32.command.testExecute=
##env.ts_win32.command.testExecuteAppClient=
##env.ts_win32.command.testExecuteAppClient2=

##########################################################################
# @sigTestClasspath: This property must be set when running signature tests.
#
#                    The sigTestClasspath specifies a list of classes/jar files
#                    which contains Jakarta Restful Web Services implementation 
#                    and Java SE classes.
##########################################################################
sigTestClasspath=${java.home}/lib/rt.jar${pathsep}${jaxrs.classes}


########################################################################
## These properties are used by the Javatest harness.
#
# @harness.log.port   specifies the port that server components use
#                     to send logging output back to JavaTest.
#                     If the default port # is not available on the machine
#                     running JavaTest, then you can set it here.
#
# @harness.temp.directory directory location used by the
#                         harness to store temporary files
#
# @harness.log.port  the port the harness listens on for log mesages
#                    from remote clients
#
# @harness.log.traceflag  used to turn on/off verbose debugging output
#                         for the tests.
#
# @harness.executeMode  used to run the harness in the following modes
#                       of execution:
#    0 - default (deploy, run, undeploy)
#    1 - deploy only
#    2 - run only
#    3 - undeploy only
#    4 - deploy and run only
#
# @harness.socket.retry.count - denotes the number of time we should
#          attempt to create a server socket when intilizing a test
#          client.  The socket is used for logging purposes.
#
# @harness.log.delayseconds  Number of seconds to delay to allow
#                            reporting from remote clients to finish.
########################################################################
harness.temp.directory=${ts.home}/tmp
harness.log.port=2000
harness.log.traceflag=false
harness.executeMode=0
harness.socket.retry.count=10
harness.log.delayseconds=1

ts_home=${TS_HOME}

########################################################################
# @deploy.delay.in.minutes This property can be used to specify the
# amount of time in minutes that the test harness will wait for the JSR-88
# ProgressObject to return either failed or completed from a DeploymentManager
# API call.  After the time has elapsed, the harness will report failure for
# the given action.
########################################################################
deploy.delay.in.minutes=5


###############################################################
## These properties must be set to tell the Test harness the
## class names of your porting class implementations.  By default
## both property sets below point to Sun RI specific classes.
#
# @porting.ts.url.class.1  VI of
#    com.sun.ts.lib.porting.TSURLInterface
# @porting.ts.HttpsURLConnection.class.1  VI of
#    com.sun.ts.lib.porting.TSHttpsURLConnectionInterface
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.HttpsURLConnection.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection


#####################################################################
## The following properties must be set prior to running the Jakarta Restful Web Services
## tests.
##
## These properties must specify the host and port of the web server,
## in which the Jakarta Restful Web Services application deployed on.
#
# @webServerHost  hostname for the Vendor's Java EE Web Server
# @webServerPort  port number of the Vendor's Java EE Web Server
#
#####################################################################
webServerHost=
webServerPort=

###################################################################
###################################################################
###################################################################
##  PROPERTIES USERS WILL NOT HAVE TO SET LIVE BELOW
###################################################################
###################################################################
###################################################################

##
## The directory seperator for the platform.  User should not change
## this property.
##
dirsep=/

##########################################################################
# build level
#  1: compile only
#  2: compile and build component archives (e.g., jar's, war's)
#  3: compile and build component and application archives
#  default is set to 2
##########################################################################
build.level=2


##########################################################################
# Needed for building/packaging
##########################################################################
deliverable.class=com.sun.ts.lib.deliverable.tck.TCKDeliverable

###########################################################################
# tools.jar should be set to the location of the tools.jar from the installed
# jdk
###########################################################################
tools.jar=${jdk.home}/lib/tools.jar


##########################################################################
# Default client used for tests
##########################################################################
test.client=JAXRSClient.java

####################################################################
# For the webcontainer home properties defined above, if glassfish
# is selected these are the various additional properties that should
# be set for use with configuration setup.
####################################################################
glassfish.instance.home=${web.home}/domains/domain1
glassfish.admin.host=localhost
glassfish.admin.port=4848
glassfish.admin.user=admin
glassfish.admin.pass=
glassfish.master.pass=changeit
glassfish.server.instance=server
glassfish.domain.name=domain1

#################################################################
# The following properties must be set prior to running the 
# Jakarta Restful Web Services TCK tests. These properties are 
# used for the Jakarta Restful Web Services security tests
# which test HTTP Authentication.
#
# @user           User defined to exercise rolemapping feature
# @password       Associated password for the user
# @authuser       User defined to exercise rolemapping feature
# @authpassword   Associated password for the authuser
#################################################################
user=j2ee
password=j2ee
authuser=javajoe
authpassword=javajoe

##########################################################################
# 
# @web.home                -- JavaEE 6 implementation installation home
#
# @jaxrs_impl.classes      -- Used for running tests. 
# @jaxrs_impl_lib          -- Used for repackaging test application 
#                             Jakarta Restful Web Services implementation classes by vendor
#                             Default to ${web.home}/modules/jersey-gf-servlet.jar
#
# @jaxrs.classes           -- The classes for the Jakarta Restful Web Services api's
#                             Default to ${web.home}/modules/jsr311-api.jar
#
# @servlet.classes         -- The classes for the Servlet api's
#                             Default to ${web.home}/modules/jakarta.servlet.jar
#
# @servlet_adaptor         -- servlet adaptor class provided by vendor, 
#                             used to package resource in .war file and
#                             deploy resource classes in servlet container
#                             Default to com/sun/jersey/spi/container/servlet/ServletContainer.class
#
# @impl.vi                 -- Name of JakartaEE implementation.  
#                             All relevant porting files are located under
#                             $TS_HOME/bin/xml/impl/${impl.vi}/
#                             Default to glassfish
#
# @impl.vi.deploy.dir      -- For deployment.  When web server supports autodeployment,
#                             this points to the autodeployment directory
#                             Default to ${web.home}/domains/domain1/autodpeloy
#
# @jaxrs_impl_name         -- Name of the Jakarta Restful Web Services implementation to be tested.  
#                             A file bearing the name will be created under 
#                             $TS_HOME/bin/xml/impl/${impl.vi}/${jaxrs_impl_name}.xml 
#                             with instructions on how to package and deploy
#                             Default to jersey
##########################################################################
web.home=
jaxrs_impl.classes=
jaxrs.classes=

jaxrs_impl_lib=
servlet_adaptor=

jaxrs_impl_name=

servlet.classes=

impl.vi=
impl.vi.deploy.dir=

tslib.name=jaxrstck
javatest.timeout.factor=1
impl.vi=glassfish
impl.vi.deploy.dir=${javaee.home}/domains/domain1/autodeploy
jaxrs_impl_name=jersey
harness.log.traceflag=true
webServerHost=localhost
webServerPort=8080
web.home=${javaee.home}
web.modules=${web.home}/modules
jaxrs_impl.classes=${web.modules}/jersey-client.jar:${web.modules}/jersey-common.jar:${web.modules}/jersey-server.jar:${web.modules}/jersey-container-servlet.jar:${web.modules}/jersey-container-servlet-core.jar:${web.modules}/osgi-resource-locator.jar:${web.modules}/jakarta.inject.jar:${web.modules}/guava.jar:${web.modules}/hk2-api.jar:${web.modules}/hk2-locator.jar:${web.modules}/hk2-utils.jar:${web.modules}/cglib.jar:${web.modules}/asm-all-repackaged.jar:${web.modules}/bean-validator.jar:${web.modules}/endorsed/jakarta.annotation-api.jar:${web.modules}/javax.json.jar
jaxrs.classes=${web.modules}/jakarta.ws.rs-api.jar
jersey.home=D:/CTS/CTS_JAXRS/jaxrstck/jersey
jaxrs_impl_lib=${web.modules}/jersey-container-servlet-core.jar
servlet_adaptor=org/glassfish/jersey/servlet/ServletContainer.class
