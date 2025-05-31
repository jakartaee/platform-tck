#
# Copyright (c) 2013, 2021 Oracle and/or its affiliates. All rights reserved.
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
#########################################################################
#                                                                      #
#   JavaTest Environment file for Websocket Compatibility Test Suite    #
#                                                                       #
#   Environment specific properties in this file will likely           #
#   have to be modified prior to running the Websocket TCK. Instructions#
#   for modifying these properties are contained in this file.         #
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
work.dir=/files/JTWork
report.dir=/files/JTReport
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

########################################################################
## Settings for Vendor WebSocket Implementation
#  @web.home The location of the vendor's WebSocket Implementation
########################################################################
web.home=

########################################################################
## Common environment for both ts_unix and ts_win32
#
# @command.testExecute - This command is used to execute any test 
#                        clients which are not run inside an
#                        application client container.  For example,
#                        any URL clients or standalone java clients
#                        would be executed with this command.  Some
#                        test directories which make use of this command
#                        are websocket and servlet.
########################################################################
command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
                    CLASSPATH=${JAVA_HOME}/lib/rt.jar${pathsep}\
                    ${LOCAL_CLASSES} \
                    HOME="${user.home}" \
                    windir=${windir} \
                    SYSTEMROOT=${SYSTEMROOT} \
                    ${JAVA_HOME}/bin/java \
                    -Dbytecheck=true \
                    -Djavax.net.ssl.keyStorePassword=changeit \
                    -Djavax.net.ssl.keyStore=${bin.dir}/certificates/clientcert.jks \
                    -Djavax.net.ssl.trustStore=${s1as.truststore} \
                    -Djavax.net.ssl.trustStorePassword=changeit \
                    ${JAVA_OPTIONS} $testExecuteClass $testExecuteArgs

########################################################################
# Environment ts_unix                                              
########################################################################
env.ts_unix.menu=true

########################################################################
# Environment ts_win32						       
########################################################################
env.ts_win32.menu=true

########################################################################
#These properties are used by the Javatest harness. "harness.log.port"
#specifies the port that server components use to send logging
#output back to JavaTest.  If the default port # is not available
#on the machine running JavaTest, then you can set it here. 
#
#harness.socket.retry.count denotes the number of time we should attempt
#to create a server socket when intilizing a test  client.  The socket
#is used for logging purposes.
#
# "harness.log.traceflag" is used to turn on/off verbose debugging output
# for the tests.
#
# "harness.executeMode" is used to run the harness in the following modes
# of execution:
#    0 - default (deploy, run, undeploy)
#    1 - deploy only
#    2 - run only
#    3 - undeploy only
#    4 - deploy and run only
########################################################################
harness.temp.directory=${TS_HOME}
harness.log.port=2000
harness.log.mode=silent
harness.log.delayseconds=1 
harness.socket.retry.count=10
harness.log.traceflag=false
harness.executeMode=2

ts_home=${ts.home}

##########################################################################
#build level
#2: compile and build component archives (war's)
##########################################################################
build.level=2


###############################################################
## These properties must be set to tell the Test harness the
## class names of your porting class implementations.  By default
## both property sets below point to Oracle RI specific classes.  To
## run interoperability tests, the ".2" set of properties should
## always point to Oracle RI classes.  The ".1" set should point to
## implementations that work in your specific Java EE environment.
#
# @porting.ts.url.class.1  VI of
#                          com.sun.ts.lib.porting.TSURLInterface
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL

#####################################################################
#The following properties must be set prior to running the Websocket API tests 
#
## These properties must specify the host and port of the web server,
## in which the servlets and JSPs are deployed.
#
# @webServerHost  hostname for the Vendor's Java EE Web Server
# @webServerPort  port number of the Vendor's Java EE Web Server
#
##########################################################################
webServerHost=
webServerPort=

###############################################################
# @securedWebServicePort must be set to run ssl tests. 
#   Set this property with your application server's secured webservice port.
###############################################################
securedWebServicePort=8181

##########################################################################
# The ClientThreads property configures the number of threads
# used by the client. 
# If the container implementation supports pooling, 
# set the value of ClientThreads to twice the value
# of the default server instance pool size.  If the container
# implementation only maintains a single instance,
# leave the default value of 2.
##########################################################################
websocket.client.threads=2
ClientThreads=2

##########################################################################
## Classpath properties required by Web Socket TCK:
#
# @websocket.api        -- classes for the Web Socket api 
# @websocket.classes    -- classes for the Web Socket implementation 
# @ts.harness.classpath -- Classes required by javatest
# @ts.classpath         -- Classes used to build the  Web Socket TCK tests
# @ts.run.classpath     -- Classpath required to run Web Socket TCK tests
##########################################################################
websocket.api=${web.home}/modules/jakarta.websocket-api.jar${pathsep}${web.home}/modules/jakarta.websocket-client-api.jar${pathsep}${web.home}/modules/jakarta.servlet-api.jar${pathsep}${web.home}/modules/jakarta.enterprise.cdi-api.jar
websocket.classes=${web.home}/modules/tyrus-websocket-core.jar${pathsep}${web.home}/modules/tyrus-client.jar${pathsep}${web.home}/modules/tyrus-core.jar${pathsep}${web.home}/modules/tyrus-container-grizzly.jar${pathsep}${web.home}/modules/glassfish-grizzly-extra-all.jar${pathsep}${web.home}/modules/nucleus-grizzly-all.jar${pathsep}${web.home}/modules/tyrus-server.jar${pathsep}${web.home}/modules/tyrus-container-servlet.jar${pathsep}${web.home}/modules/tyrus-container-grizzly-client.jar${pathsep}${web.home}/modules/tyrus-spi.jar

ts.classpath=${websocket.api}${pathsep}${ts.harness.classpath}

ts.harness.classpath=${ts.home}/lib/jaxb-xjc.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/websockettck.jar${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/classes${pathsep}${websocket.classes}${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}${ts.home}/lib/commons-codec-1.9.jar

ts.run.classpath=${websocket.api}${pathsep}${websocket.classes}${pathsep}${ts.harness.classpath}

local.classes=${ts.home}/classes${pathsep}${websocket.api}${pathsep}${websocket.classes}${pathsep}${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}${ts.home}/lib/commons-codec-1.9.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.home}/lib/javatest.jar:${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/websockettck.jar

##########################################################################
# Default client used for tests
##########################################################################
test.client=WSClient.java

##########################################################################
# Set your local timezone. For valid values, consult your Operating System
# documentation.
##########################################################################
tz=US/Eastern

deliverable.class=com.sun.ts.lib.deliverable.tck.TCKDeliverable

##########################################################################
# The alt.schema.dir and alt.dtd.dir properties specify the location of
# the Schemas used to package the TCK tests (web, j2ee, etc).  These
# properties need not be set if only running the tests.
# DO NOT EDIT
##########################################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas

##########################################################################
# @jimage.dir:       This property specifies the directory where Java 11+
#                    modules will be expanded by the jimage tool for use
#                    in sigTestClasspath
# @sigTestClasspath: This property specifies the location of websocket
#                    implementation classes used for the signature test
##########################################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${websocket.classes}${pathsep}${websocket.api}${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming

##########################################################################
## Deliverables wanting ts.* packaging tasks to add extension list
## attributes to the manifest files must set this property to true.
##########################################################################
#create.manifest.extension.entries=

##########################################################################
## Deliverables must set this property to the name of the deliverable
## specific library jar file (if create.manifest.extension.entries is set  
## to true)
##########################################################################
tslib.name=websockettck

####################################################################
# Implementation Property Settings for Vendor and RI.  
#
# They are are used when using the "ant deploy" and "ant undeploy"
# targets outside of running tests.
#
# The TCK provides to run tests against the glassfish implementation iby default
#
# Here are the common properties that need to be defined for the common
# implementation functionality:
#
# @impl.vi            This property must be set to the Vendor implementation
#                     under test.
# @impl.vi.deploy.dir This property must be set to the deploy directory for
#                     the Vendor implementation.
####################################################################
impl.vi=
impl.vi.deploy.dir=${web.home}/domains/domain1/autodeploy

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
#The following properties must be set prior to running the JAXRS
#TCK tests. These properties are used for the JAXRS security tests
#which test HTTP Authentication.
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
# Waiting time in seconds for socket to send/receive a message
##########################################################################
ws_wait=5

#################################################################
# To Configure config.vi with security manager
#################################################################
javaee.home=${web.home}
###############################################################
## Config params needed for Sun Java System Application Server
## (SJSAS) asadmin. You must change these values as needed
## only if you are testing against SJSAS app server
## (javaee.home is pointing to SJSAS).
#
# @s1as.admin.user     -- The SJSAS asadmin user id
# @s1as.admin.passwd   -- The SJSAS asadmin user password
# @s1as.admin.host     -- The SJSAS host
# @s1as.admin.port     -- The SJSAS port
# @s1as.admin          -- The SJSAS admin command
# @s1as.server         -- The SJSAS server instance being used
# @s1as.domain.dir     -- Points to where your domains are installed.
# @s1as.domain.name    -- The SJSAS domain being used
# @s1as.domain         -- The SJSAS domain path being used
# @s1as.asenv.loc      -- location of asenv.conf or asenv.bat
# @s1as.imqbin.loc     -- location of the IMQ bin directory
# @s1as.lib            -- Library directory for other Java EE RI
#                         jars
# @s1as.imq.share.lib  -- Shared library directory for imq 
# @s1as.jvm.options    -- Java options needed by SJSAS
#                         The xxxlogin and xxxpassword are used
#                         to set known server side creds for use
#                         with connector tests.
# @s1as.java.endorsed.dirs -- endorsed directory used by SJSAS
# @s1as.applicationRoot-- Location of application repository
#                         Only needed when running on windows
###############################################################
s1as.admin=${javaee.home}/bin/asadmin
s1as.domain.dir=${javaee.home}/domains
s1as.domain.name=domain1
s1as.domain=${s1as.domain.dir}/${s1as.domain.name}
s1as.admin.host=${glassfish.admin.host}
s1as.admin.port=${glassfish.admin.port}
s1as.truststore=${s1as.domain}/config/cacerts.jks

###############################################################
# @extension.dir - The extension directory of RI. 
# The CTS config.vi target will copy the CTS library 
# jars to this location, used only for RI.
###############################################################
extension.dir=${web.home}/domains/domain1/lib/

