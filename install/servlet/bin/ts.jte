#
# Copyright (c) 2009, 2020 Oracle and/or its affiliates and others.
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

#########################################################################
#                                                                   	#
#   JavaTest Environment file for SERVLET Compatibility Test Suite      #
#                                                                       #
#   Environment specific properties in this file will likely	 	    #
#   have to be modified prior to running the SERVLET TCK. Instructions	#
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
## Settings for Vendor Servlet Implementation
#  @web.home The location of the vendor's Servlet implementation.
########################################################################
web.home=

###############################################################
# The following property are added for setting up security tests.
# All values are default to running with Servlet RI.
#
# All above environmental property are
# used for method permission testing; as well as security related API testing.
#
#
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
#########################################################################
javaee.home=${web.home}
s1as.admin.user=admin
s1as.admin.passwd=
s1as.admin.host=${webServerHost}
s1as.admin.port=4848
s1as.admin=${javaee.home}/bin/asadmin
s1as.domain.dir=${web.home}/domains
s1as.domain.name=domain1
s1as.domain=${web.home}/domains/domain1
s1as.asenv.loc=${javaee.home}/config

###############################################################
# @tz - your local timezone. For valid values, consult your
#       Operating System documentation.
###############################################################
tz=US/Eastern

###############################################################
# @alt.dtd.dir DTD location for Servlet specification and RI xml files.
#              Used for xml validation when building tests.
# @alt.schema.dir schema location for Servlet specification and RI xml files.
#                 Used for xml validation when building tests.
###############################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas


##########################################################################
## Classpath properties required by Servlet TCK:
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the Servlet TCK tests
# @servlet.classes      --  Servlet API classes, default value using Servlet RI
##########################################################################
local.classes=${ts.home}/classes${pathsep}${servlet.classes}${pathsep}${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}${ts.home}/lib/commons-codec-1.9.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/servlettck.jar${pathsep}${ts.home}/lib/http.jar

ts.classpath=${local.classes}${pathsep}${ts.harness.classpath}

ts.harness.classpath=${ts.home}/lib/jaxb-xjc.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/servlettck.jar${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/lib/http.jar${pathsep}${servlet.classes}${pathsep}${ts.home}/lib/jaxb-impl.jar${pathsep}${ts.home}/lib/jaxb-api.jar

servlet.classes=${web.home}/modules/jakarta.servlet-api.jar${pathsep}${web.home}/modules/jakarta.annotation-api.jar

########################################################################
# @endorsedlib.dir - Location to be used for java.endorsed.dirs as some
#                    tests require some addition classes in the
#                    java.util.concurrent package that are not present
#                    in Java 8 as they were added in Java 9.
########################################################################
endorsedlib.dir=${ts.home}/endorsedlib

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
                    -Djava.endorsed.dirs=${endorsedlib.dir} \
                    -Dbytecheck=true \
                    -Djava.protocol.handler.pkgs=javax.net.ssl \
                    -Djavax.net.ssl.keyStore=${ts.home}/bin/certificates/clientcert.jks \
                    -Djavax.net.ssl.keyStorePassword=changeit \
                    -Djavax.net.ssl.trustStore=${web.home}/domains/domain1/config/cacerts.jks \
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
# The sigTestClasspath specify the location of servlet implementation 
# classes that used for signature test
##########################################################################
sigTestClasspath=${java.home}/lib/rt.jar${pathsep}${java.home}/lib/modules${pathsep}${servlet.classes}

########################################################################
## These properties are used by the harness.  "harness.log.port"
## specifies the port that server components use to send logging
## output back to JavaTest.  If the default port # is not available
## on the machine running JavaTest, then you can set it here.
##
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
#
########################################################################
harness.temp.directory=${TS_HOME}
harness.log.port=2000
harness.log.traceflag=true
harness.executeMode=2
harness.socket.retry.count=10
harness.log.delayseconds=1 
harness.log.mode=silent

##########################################################################
# Added for convenience to use CTS utilities for deploy in Servlet RI
##########################################################################
impl.vi=
impl.vi.deploy.dir=${web.home}/domains/domain1/autodeploy
 
###############################################################
# These properties must be set to tell the Javatest harness the
# class names of your porting class implementations. By default
# the property settings below point to Sun RI specific classes.  
# @porting.ts.url.class.1  VI of
#    com.sun.ts.lib.porting.TSURLInterface
# @porting.ts.HttpsURLConnection.class.1 - VI of
#    com.sun.ts.lib.porting.TSHttpsURLConnectionInterface 
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.HttpsURLConnection.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection

##############################################################
# @certLoginUserAlias  User alias for certificate based login.
# This property is used in mutual authentication to pickup the
# certificate based on the user alias.
##############################################################
certLoginUserAlias=cts

#########################################################################
## These properties must specify the host and port of the web server,
## in which the servlets are deployed.
#
# @webServerHost  hostname for the Vendor's Java EE Web Server
# @webServerPort  port number of the Vendor's Java EE Web Server
# @ServletClientThreads  The ServletClientThreads property configures
#                        the number of threads used by the client for
#                        the SingleThreadModel servlet test. If the
#                        container implementation supports pooling of
#                        SingleThreadModel servlets, set the value of
#                        ServletClientThreads to twice the value of
#                        the default servlet instance pool size.  If
#                        the container implementation only maintains
#                        a single instance of a SingleTheadModel
#                        servlet, leave the default value of 2.
#####################################################################
webServerHost=
webServerPort=
ServletClientThreads=2

#########################################################################
## The following properties must be set before running any security
## related tests.  The properties user, password, authuser, authpassword,
## and nobodyuser must be set.
##
## The value for user, password, authuser, and authpassword need to be set
## exactly as they are set in the container/server.
#
# @user           User defined to exercise rolemapping feature
# @password       Associated password for the user
# @authuser       User defined to exercise rolemapping feature
# @authpassword   Associated password for the authuser
#########################################################################
user=j2ee
password=j2ee
authuser=javajoe
authpassword=javajoe

###############################################################
# @securedWebServicePort must be set to run secbasicssl test.
#                        Set this property with your application
#                        server's secured webservice port.
###############################################################
securedWebServicePort=

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

##build level
##2: compile and build component archives (e.g., jar's, war's)
##default is set to 3
build.level=2

##########################################################################
# Needed for building/packaging
##########################################################################
deliverable.class=com.sun.ts.lib.deliverable.servlet.ServletDeliverable

######################################################################
## Deliverables must set this property to the name of the deliverable
## specific library jar file (iff create.manifest.extension.entries
## is set to true)
######################################################################
tslib.name=servlettck

ts_home=${ts.home}

org.omg.CORBA.ORBClass=foo

##########################################################################
# @servlet_waittime: Time in seconds to wait after HttpSession expires 
#                    to access the session
# @servlet_async_wait: Time in seconds to wait between sending asynchronous messages 
# @logical.hostname.servlet
#              This used to identify the the name of a logical host that
#              processes Servlet requests. Servlet requests may be directed to
#              a logical host using various physical or virtual host names or
#              addresses, and a message processing runtime may be composed of
#              multiple logical hosts (This is required to properly identify
#              the servlet profiles  AppContextId hostname.)
#              If a logical.hostname.servlet does not exist, you can set this
#              to the default hostname (eg webServerHost).
##########################################################################
servlet_waittime=10
servlet_async_wait=4
logical.hostname.servlet=server


##########################################################################
# Default client used for tests
##########################################################################
test.client=URLClient.java

