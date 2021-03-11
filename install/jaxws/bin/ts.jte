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

#########################################################################
#                                                                   	#
#   JavaTest Environment file for JAXWS Compatibility Test Suite        #
#                                                                       #
#   Environment specific properties in this file will likely	 	#
#   have to be modified prior to running the JAXWS TCK. Instructions	#
#   for modifying these properties are contained in this file.	 	#
#                                                                       #
#########################################################################

####################################################################
#The following properties must be set prior to running the JAXWS
#TCK tests.
#
## These properties must specify the host and port of the web server,
## in which the JAXWS TCK test servlets are deployed.
#
# @webServerHost  hostname for the Vendor's Web Server
# @webServerPort  port number of the Vendor's Web Server
# @webServerHost.2  hostname for the RI Web Server
# @webServerPort.2  port number of the RI Web Server
####################################################################
webServerHost=localhost
webServerPort=8080
webServerHost.2=
webServerPort.2=

#################################################################################
# @jaxws.home  should be set to the location of where the implementation under
#              test is installed (VI implementation). This is usually installed
#	       within a web container (glassfish or tomcat) so we default this
#	       value to @webcontainer.home. See this property below.
# @jaxws.home.ri  should be set to the location of where the RI is installed.
#		  This is usually installed within a web container (glassfish 
#		  or tomcat) so we default this value to @webcontainer.home.ri.
#		  See this property below.
# @endorsed.dirs  If using JavaSE 6 or above, verify that the property 
#                 endorsed.dirs is set to the location of the VI api jars 
#                 for those technologies you wish to overrride. JavaSE 6 
#                 contains an implementation of JAXWS 2.x which will conflict
#                 with JAXWS 2.2, therefore this property must be set so that 
#                 JAXWS 2.2 will be used during the building of tests and 
#                 during test execution. For details regarding how the 
#                 Endorsed Standards Override Mechanism works, see 
#                 http://java.sun.com/javase/6/docs/technotes/guides/standards
# @endorsed.dirs.ri  If using JavaSE 6 or above, verify that the property 
#                    endorsed.dirs is set to the location of the RI api jars 
#                    for those technologies you wish to overrride. JavaSE 6 
#                    contains an implementation of JAXWS 2.x which will conflict
#                    with JAXWS 2.2, therefore this property must be set so that 
#                    JAXWS 2.2 will be used during the building of tests and 
#                    during test execution. For details regarding how the 
#                    Endorsed Standards Override Mechanism works, see 
#                    http://java.sun.com/javase/6/docs/technotes/guides/standards
#################################################################################
jaxws.home=${webcontainer.home}
jaxws.home.ri=${webcontainer.home.ri}
jaxws.lib=${jaxws.home}/modules
jaxws.lib.ri=${jaxws.home.ri}/modules
endorsed.dirs=${jaxws.home}/modules/endorsed
endorsed.dirs.ri=${jaxws.home.ri}/modules/endorsed


###########################################################################
# @jaxws.classes  should contain all Vendor specific jars/classes
###########################################################################
jaxws.classes=${jaxws.lib}/webservices-api-osgi.jar${pathsep}${jaxws.lib}/jaxb-api-osgi.jar${pathsep}${jaxws.lib}/webservices-osgi.jar${pathsep}${jaxws.lib}/jaxb-osgi.jar${pathsep}${jaxws.lib}/gmbal.jar${pathsep}${jaxws.lib}/management-api.jar${pathsep}${jaxws.lib}/mimepull.jar${pathsep}${jaxws.lib}/ha-api.jar${pathsep}${jaxws.lib}/webservices-api.jar${pathsep}${jaxws.lib}/jakarta.xml.bind-api.jar${pathsep}${jaxws.lib}/jakarta.activation.jar

###########################################################################
# @jaxws.classes.ri  should contain all RI specific jars/classes
###########################################################################
jaxws.classes.ri=${jaxws.lib.ri}/webservices-api-osgi.jar${pathsep}${jaxws.lib.ri}/jaxb-api-osgi.jar${pathsep}${jaxws.lib.ri}/webservices-osgi.jar${pathsep}${jaxws.lib.ri}/jaxb-osgi.jar${pathsep}${jaxws.lib.ri}/gmbal.jar${pathsep}${jaxws.lib.ri}/management-api.jar${pathsep}${jaxws.lib.ri}/mimepull.jar${pathsep}${jaxws.lib.ri}/ha-api.jar${pathsep}${jaxws.lib.ri}/webservices-api.jar${pathsep}${jaxws.lib.ri}/jakarta.xml.bind-api.jar${pathsep}${jaxws.lib.ri}/jakarta.activation.jar

###########################################################################
# @catalog.path  Path to catalog file for use with catalog tests
###########################################################################
catalog.path=${src.dir}/com/sun/ts/tests/jaxws/common/xml/catalog

####################################################################
# The jars/classes for the TS harness/javatest and the test classes
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the JAXWS tests
# @ts.run.classpath     --  Classpath required by the vendor
#                           appclient container.
# @ts.run.classpath.ri  --  Classpath required by Java EE RI
#                           appclient container.
####################################################################
ts.harness.classpath=${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/jaxwstck.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/lib/jaxb-api.jar${pathsep}${ts.home}/lib/jaxb-xjc.jar${pathsep}${ts.home}/lib/jaxb-libs.jar${pathsep}${ts.home}/lib/jaxb-impl.jar

ts.classpath=${jaxws.classes}${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/jaxwstck.jar

ts.run.classpath=${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.home}/lib/jaxwstck.jar${pathsep}${ts.home}/classes${pathsep}${jaxws.classes}${pathsep}${catalog.path}

ts.run.classpath.ri=${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.home}/lib/jaxwstck.jar${pathsep}${ts.home}/classes${pathsep}${jaxws.classes.ri}${pathsep}${catalog.path}

###########################################################################
# @tools.jar  should be set to the location of the tools.jar from the installed
# jdk
###########################################################################
tools.jar=${jdk.home}/lib/tools.jar

###########################################################################
# various flags used by the Vendors generation tools
# @wsgen.ant.classname  Complete package name of the Vendors wsgen tool
# @wsgen.classpath      Classpath to the Vendors jars that contain the wsgen tool
# @wsgen.verbose        Verbose flag
# @wsgen.debug          Debug flag
# @wsimport.ant.classname  Complete package name of the Vendors wsimport tool
# @wsimport.classpath      Classpath to the Vendors jars that contain the wsimport tool
# @wsimport.verbose        Verbose flag
# @wsimport.debug          Debug flag
# @wsimport.jvmargs        JVM args to pass to Vendors wsimport tool
###########################################################################
wsgen.ant.classname=
wsgen.classpath=${jaxws.classes}${pathsep}${tools.jar}
wsgen.verbose=true
wsgen.debug=false
wsimport.ant.classname=
wsimport.classpath=${jaxws.classes}${pathsep}${tools.jar}
wsimport.verbose=true
wsimport.debug=false
wsimport.jvmargs=

###########################################################################
# various flags used by Suns generation tools
# DO NOT EDIT
# @ri.wsgen.ant.classname  Complete package name of the RI wsgen tool
# @ri.wsgen.classpath      Classpath to the RI jars that contain the wsgen tool
# @ri.wsgen.verbose        Verbose flag
# @ri.wsgen.debug          Debug flag
# @ri.wsimport.ant.classname  Complete package name of the RI wsimport tool
# @ri.wsimport.classpath      Classpath to the RI jars that contain the wsimport tool
# @ri.wsimport.verbose        Verbose flag
# @ri.wsimport.debug          Debug flag
# @ri.wsimport.jvmargs        JVM args to pass to RI wsimport tool
###########################################################################
ri.wsgen.ant.classname=com.sun.tools.ws.ant.WsGen
ri.wsgen.classpath=${jaxws.classes.ri}${pathsep}${tools.jar}
ri.wsgen.verbose=true
ri.wsgen.debug=false
ri.wsimport.ant.classname=com.sun.tools.ws.ant.WsImport
ri.wsimport.classpath=${jaxws.classes.ri}${pathsep}${tools.jar}
ri.wsimport.verbose=true
ri.wsimport.debug=false
ri.wsimport.jvmargs=

#######################################################################################
# Implementation Property Settings for Vendor and RI
#
# The TCK uses the following implementations:
#    - glassfish	(JavaEE Web Container)
#    - tomcat		(Tomcat Web Container)
#
# Here are the common properties that need to be defined for the common
# implementation functionality:
#
# @impl.vi		    This property must be set to the Vendor implementation
#			    under test.
# @impl.vi.deploy.dir	    This property must be set to the deploy directory for
#			    the Vendor implementation.
# @impl.vi.host		    This property must be set to the webserver host where
#			    the Vendor implementation is running.
# @impl.vi.port		    This property must be set to the webserver port where
#			    the Vendor implementation is running.
# @impl.ri		    This property must be set to the RI implementation
#			    under test.
# @impl.ri.deploy.dir	    This property must be set to the deploy directory for
#			    the RI implementation.
# @impl.ri.host		    This property must be set to the webserver host where
#			    the RI implementation is running.
# @impl.ri.port		    This property must be set to the webserver port where
#			    the RI implementation is running.
#
# These additional properties are required to be defined for use with configuration
# functionality used for Vendor and RI specific configuration:
#
# @webcontainer.home	    This property must refer to the location of where the
#			    Vendor implementation is installed within the webcontainer
#			    under test (glassfish or tomcat). 
# @webcontainer.home.ri	    This property must refer to the location of where the 
#			    RI implementation is installed within the webcontainer
#                           under test (glassfish or tomcat). 
# @impl.deploy.timeout.multiplier  The time it will wait for deployment to succeed
#			    or fail
#######################################################################################
webcontainer.home=
webcontainer.home.ri=

impl.vi=glassfish
impl.vi.deploy.dir=${webcontainer.home}/domains/domain1/autodeploy
impl.vi.host=${webServerHost}
impl.vi.port=${webServerPort}

impl.ri=glassfish
impl.ri.deploy.dir=${webcontainer.home.ri}/domains/domain1/autodeploy
impl.ri.host=${webServerHost.2}
impl.ri.port=${webServerPort.2}

impl.deploy.timeout.multiplier=120

####################################################################
# For the webcontainer home properties defined above, if glassfish
# is selected these are the various additional properties that should
# be set for use with configuration setup.
####################################################################
# For glassfish as Vendor set the following:
glassfish.instance.home=${webcontainer.home}/domains/domain1
glassfish.admin.host=localhost
glassfish.admin.port=4848
glassfish.admin.user=admin
glassfish.admin.pass=
glassfish.master.pass=changeit
glassfish.server.instance=server
glassfish.domain.name=domain1

# For glassfish as RI set the following:
glassfish.instance.home.ri=${webcontainer.home.ri}/domains/domain1
glassfish.admin.host.ri=localhost
glassfish.admin.port.ri=4848
glassfish.admin.user.ri=admin
glassfish.admin.pass.ri=
glassfish.master.pass.ri=changeit
glassfish.server.instance.ri=server
glassfish.domain.name.ri=domain1

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
#                    if in JAXWS workspace
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
## These properties must be set to tell the Test harness the
## class names of your porting class implementations.  By default
## both property sets below point to Sun RI specific classes.  To
## run against both implementations automatically, the ".2" 
## property should always point to Sun RI classes.  The ".1"
## property should point to implementations that work in your specific
## Java EE environment.
# @porting.ts.url.class.1  VI of com.sun.ts.lib.porting.TSURLInterface
# @porting.ts.url.class.2  RI of com.sun.ts.lib.porting.TSURLInterface
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.url.class.2=com.sun.ts.lib.implementation.sun.common.SunRIURL

#################################################################
#The following properties must be set prior to running the JAXWS
#TCK tests. These properties are used for the JAXWS security tests
#which test HTTP Basic Authentication.
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
build.level=2

###############################################################
# @ts.display -- location to display JAXWS output on Unix
###############################################################
ts.display=:0.0

#######################################################################
# default deliverable class
#######################################################################
deliverable.class=com.sun.ts.lib.deliverable.jaxws.JAXWSDeliverable

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
		-Djava.security.policy="${bin.dir}/harness.policy" \
		-Djava.security.manager \
		-Ddeliverable.class=${deliverable.class} \
                -Djava.endorsed.dirs=${endorsed.dirs} \
        $testExecuteClass $testExecuteArgs

# This execute line runs against the RI implementation
command.testExecute2=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${ts.run.classpath.ri} \
        DISPLAY="${ts.display}" \
		HOME="${user.home}"  \
		windir=${windir} \
		SYSTEMROOT=${SYSTEMROOT} \
        ${JAVA_HOME}/bin/java \
		-Djava.security.policy="${bin.dir}/harness.policy" \
		-Djava.security.manager \
		-Ddeliverable.class=${deliverable.class} \
		-Djava.endorsed.dirs=${endorsed.dirs.ri} \
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
# @sigTestClasspath: This property must be set when running signature 
#                    tests.  This property should be set to a list of
#                    jar files and/or directories which contain your 
#                    Java EE and Java SE classes. Paths must be 
#                    separated by the appropriate path separator 
#                    (';' windows, ':' Unixes).
#########################################################################
sigTestClasspath=${jaxws.classes}${pathsep}${JAVA_HOME}/lib/rt.jar

###############################################################
## This command allows the test clients to be run in
## the same JVM as JavaTest in this case.  This mode can be
## enabled by passing "-Dsame.jvm=true" to the runclient or gui
## targets.  NOTE: This option is only to be used for sanity
## checking and not when running the TCK for compatibility.
###############################################################
command.testExecuteSameJVM=com.sun.ts.lib.harness.ExecuteTSTestSameJVMCmd \
        $testExecuteClass $testExecuteArgs

#######################################################################
# platform.mode is used by JAXWS tests to know whether to use the
# J2EE Programming Model for service and port access or the
# JAXWS Programming model based on the runtime execution platform.
#
# platform.mode=standalone	JAXWS Programming Model
# platform.mode=jakartaEE		Java EE Programming Model
#######################################################################
platform.mode=standalone

################################################################
# @http.server.supports.endpoint.publish   Does HTTP server-side support endpoint publish API's.
# @http.server.supports.endpoint.publish.2 Does the RI HTTP server-side support endpoint publish API's.
# http.server.supports.endpoint.publish=(true|false)
################################################################
http.server.supports.endpoint.publish=true
http.server.supports.endpoint.publish.2=true

#######################################################################
# Endpoint API test
#
# Port info is obtained dynamically for Endpoint API test.
# Uncomment port range min/max if you want to specify port range
# for Endpoint publish test.
# Server endpoints will be published on ports within this range.
# If you do not want to dynamically obtain the port but wish to publish
# to a fixed static port than make the values below both equal to the
# specific port.
#######################################################################
port.range.min=1025
port.range.max=10000

###########################################################################
# various flags used by the Vendors Endpoint Publish tool
# @endpoint.publish.classname  Complete package name of the Vendors Endpoint Publish tool
###########################################################################
endpoint.publish.classname=com.sun.ts.tests.jaxws.util.PublishEndpoint

###########################################################################
# various flags used by Suns generation tools
# DO NOT EDIT
# @ri.endpoint.publish.classname  Complete package name of the RI Endpoint Publish tool
###########################################################################
ri.endpoint.publish.classname=com.sun.ts.tests.jaxws.util.PublishEndpoint


###################################################################
## Deliverables wanting ts.* packaging tasks to add extension list
## attributes to the manifest files must set this property to true.
###################################################################
#create.manifest.extension.entries=false

######################################################################
## Deliverables must set this property to the name of the deliverable
## specific library jar file (iff create.manifest.extension.entries
## is set to true)
######################################################################
tslib.name=jaxwstck

###########################################################################
# This property is used by some test clients that need a delay between
# method invocations to server endpoints. The value specified is the amount
# of time the client will delay in seconds.
###########################################################################
client.delay=1

#################################################################
# To Configure config.vi with security manager
#################################################################
javaee.home=${webcontainer.home}
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
# @s1as.db.ext.dirs    -- The extension directory for DB Type 2
#                         drivers.  Currently, this only needs
#                         to be set when using DB2 else it
#                         should be empty.
############################################################### 
s1as.admin=${javaee.home}/bin/asadmin
s1as.domain.dir=${javaee.home}/domains
s1as.domain.name=${glassfish.domain.name}
s1as.domain=${s1as.domain.dir}/${s1as.domain.name}
s1as.admin.host=${glassfish.admin.host}
s1as.admin.port=${glassfish.admin.port}

###############################################################
# @extension.dir - The extension directory of RI. 
# The CTS config.vi target will copy the CTS library 
# jars to this location, used only for RI.
###############################################################
extension.dir=${webcontainer.home}/domains/domain1/lib/ext
extension.dir.ri=${webcontainer.home.ri}/domains/domain1/lib/ext


#################################################################
# To Configure config.vi with security manager
#################################################################
javaee.home=${webcontainer.home}
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
# @s1as.db.ext.dirs    -- The extension directory for DB Type 2
#                         drivers.  Currently, this only needs
#                         to be set when using DB2 else it
#                         should be empty.
############################################################### 
s1as.admin=${javaee.home}/bin/asadmin
s1as.domain.dir=${javaee.home}/domains
s1as.domain.name=${glassfish.domain.name}
s1as.domain=${s1as.domain.dir}/${s1as.domain.name}
s1as.admin.host=${glassfish.admin.host}
s1as.admin.port=${glassfish.admin.port}
