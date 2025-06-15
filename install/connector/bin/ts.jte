#
# Copyright (c) 2009, 2024 Oracle and/or its affiliates. All rights reserved.
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
##
##   JavaTest Environment file for Java EE Connector Architecture (JCA) 
##
##   Environment specific properties in this file will likely
##   have to be modified prior to running the JCA TCK.
##   Instructions for modifying these properties are contained in this
##   file.
##
##   This file is processed by an external tool that helps generate the
##   CTS documents.  Therefore this file has a standard format that must
##   be followed.  This file is a standard Java Properties file with
##   very specific comment formattting.  Users can write property specific
##   comments by using the property name and an ampersand (@).  As an
##   example the following comment applies to the foo.bar property:
##       # @foo.bar  - This is a comment pertaining to foo.bar
##       #             that spans multiple lines.
##   This comment must be preceeded by a single hash (#) charater and
##   the property name must be prepended with an ampersand (@).  The
##   comment can appear anywhere in the ts.jte file.  If users have
##   comments that belong in ts.jte but DO NOT pertain to a particular
##   property the user must start the comment with at least 2 hash (#)
##   characters.  The following is a valid non-property comment:
##     ## A valid non-property comment
##     ## that spance multiple lines.
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
#               report.dir will be handled, and it must be one of the 
#               following values:
#      overwrite     overwrites all content in work.dir and report.dir
#      backup        moves all content in work.dir and report.dir to
#                    work.dir_time_day_bak and report.dir_time_day_bak, 
#                    respectively 
#      append        reuses and preserves the existing work.dir and report.dir
#      auto          lets the build files decide which mode to use
#                    (overwrite, backup or append).  the value is determined
#                    like this:
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

########################################################################
## Settings for Vendor Connector Implementation
# These properties must be set prior to running the TCK for Connector
#
# @connector.home The location of the vendor's connector
#              implementation.
#
# @orb.host    Hostname of the machine running the vendor's
#              implementation.
#
# @orb.port    The port number the vendor implementation is listening
#              to for service requests.
#
########################################################################
connector.home=/sun/glassfish4/glassfish
server.home=${connector.home}
orb.host=localhost
orb.port=3700


###############################################################
# @ts.display -- location to display CTS output on Unix
###############################################################
ts.display=:0.0

###############################################################
## Config params needed for conector Impl asadmin
## You must change these values as needed
##
# @connector.admin.user     -- The Java EE RI asadmin user id
# @connector.admin.passwd   -- The Java EE RI asadmin user password
# @connector.admin.host     -- The Java EE RI host
# @connector.admin.port     -- The Java EE RI port
# @connector.admin          -- The Java EE RI admin command 
# @connector.server         -- The Java EE RI server instance being used
# @connector.domain.dir     -- Points to where your domains are installed.
# @connector.domain.name    -- The Java EE RI domain being used
# @connector.domain         -- The Java EE RI domain path being used
# @ri.asenv.loc      -- location of asenv.conf or asenv.bat
# @ri.imqbin.loc     -- location of the IMQ bin directory
# @ri.lib            -- Library directory for other Java EE RI
#                       jars
# @ri.imq.share.lib  -- Shared library directory for imq 
# @ri.jvm.options    -- Java options needed by the Java EE RI
#                       note, the second option is not needed
#                       but is required to work around asadmin
#                       command line parsing issues.
# @ri.java.endorsed.dirs -- endorsed directory used by RI
# @ri.applicationRoot-- Location of application repository
#                       Only needed when running on windows
###############################################################
connector.admin.user=admin
connector.admin.passwd=
connector.admin.host=${orb.host}
connector.admin.port=4848
connector.admin=${connector.home}/bin/asadmin
connector.server=server
connector.domain.dir=${connector.home}/domains
connector.domain.name=domain1
connector.domain=${connector.domain.dir}/${connector.domain.name}
ri.asenv.loc=${connector.home}/config
ri.imqbin.loc=${connector.home}/imq/bin
ri.lib=${connector.home}/modules
ri.imq.share.lib=${connector.home}/imq/lib
ri.jvm.options=-Doracle.jdbc.J2EE13Compliant=true:-Dj2eelogin.name=${user}:-Dj2eelogin.password=${password}:-Deislogin.name=${user1}:-Deislogin.password=${password1}
ri.java.endorsed.dirs=${endorsed.dirs}
ri.applicationRoot=c:


s1as.admin.user=admin
s1as.admin.passwd=
s1as.admin.host=${orb.host}
s1as.admin.port=4848
s1as.admin=${connector.home}/bin/asadmin
s1as.server=server
s1as.domain.dir=${connector.home}/domains
s1as.domain.name=domain1
s1as.domain=${s1as.domain.dir}/${s1as.domain.name}
s1as.asenv.loc=${connector.home}/config
s1as.imqbin.loc=${connector.home}/imq/bin
s1as.imq.share.lib=${connector.home}/imq/lib
s1as.truststore=${s1as.domain}/config/cacerts.jks
s1as.jvm.options=-Doracle.jdbc.J2EE13Compliant=true:-Dj2eelogin.name=${user}:-Dj2eelogin.password=${password}:-Deislogin.name=${user1}:-Deislogin.password=${password1}
s1as.java.endorsed.dirs=${endorsed.dirs}
s1as.applicationRoot=c:



###########################################################################
# @endorsed.dirs  using JavaSE 6 or above and you provide newer versions
#                 of technologies than those contained in Java SE 6, verify
#                 that the property endorsed.dirs is set to the location of
#                 the VI api jars for those technologies you wish to
#                 overrride.  For example, JavaSE 6 contains an
#                 implementation of JAXWS 2.0 which will conflict with
#                 JAXWS 2.1, therefore this property would need to be set
#                 so that JAXWS 2.1 would be used during the building of
#                 tests and during test execution.
###########################################################################
endorsed.dirs=${connector.home}/lib/endorsed


###############################################################
# @sjsas.master.password           -- Used to create a node agent only
#                                     applicable to EE.  Defaults to changeit. This
#                                     can be changed at EE install time.
# @sjsas.instance.config.dir       -- config directory used by the intsance
#                                     being tested.
#                                     For PE/DAS = config
#                                     For remote instance = config
# @s1as.targets                    -- Instance(s) to deploy tests
#                                     Supports multiple instances, For example:
#                                     s1as.targets=server server-1
###############################################################
sjsas.master.password=changeit
sjsas.instance.config.dir=config
s1as.targets=${s1as.server}


###############################################################
# @s1as.pe.jmsServer      -- name of the JMS server the RI/PE
# @s1as.se.jmsServer      -- name of the JMS server for SE/EE
###############################################################
s1as.pe.jmsServer=imqbroker
s1as.se.jmsServer=imqbroker


###############################################################
# @extension.dir - The extension directory for the app
#                  server under test.  This does not apply
#                  to the RI.
#
# Note: App server vendors will need to set this to their
#       app server's extension directory.  The CTS config.vi
#       target will copy the CTS library jars to this location.
###############################################################
extension.dir=${s1as.domain}/lib/ext

###############################################################
# @instance.listenerName - Default value for the iiop listener
#                          for your instance.  Users will
#                          most likely not need to change this.
###############################################################
instance.listenerName=orb-listener-1


###############################################################
# @tz - your local timezone. For valid values, consult your
#       Operating System documentation.
###############################################################
tz=US/Eastern

###############################################################
# @jdbc.lib.class.path - This property is used by the
#                        database.classes properties to point to
#                        where the JDBC drivers live.  
###############################################################
jdbc.lib.class.path=${ts.home}/internal/lib


###############################################################
# @alt.dtd.dir DTD location for Java EE and RI xml files.  Used
#              for xml validation when building tests. If 
#              connector.home is set, <connector.home>/lib/dtds
#              will be used and alt.dtd.dir is ignored.
# @alt.schema.dir schema location for Java EE and RI xml files.
#                 Used for xml validation when building tests. 
#                 If connector.home is set,
#                 <connector.home>/lib/schemas will be used and
#                 alt.schema.dir is ignored.
###############################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas


###############################################################
## Classpath properties required by CTS:
# @connector.classes    --  Classes required by Connector RI
# @ts.run.classpath     --  Classpath required by the vendor
#                           appclient container.
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the Connector tests
# @ts.lib.classpath     --  Classes used to build connector.jar 
###############################################################
connector.classes.1=${ri.lib}/glassfish.jar${pathsep}${ri.lib}/jakarta.security.auth.message-api.jar${pathsep}${ri.lib}/web-core.jar${pathsep}${ri.lib}/webservices.security.jar${pathsep}${pathsep}${ri.lib}/connectors-admin.jar${pathsep}${ri.lib}/connectors-inbound-runtime.jar${pathsep}${ri.lib}/orb-connector.jar${pathsep}${ri.lib}/jakarta.transaction-api.jar${pathsep}${ri.lib}/jakarta.servlet-api.jar${pathsep}${ri.lib}/jakarta.resource-api.jar${pathsep}${ri.lib}/jakarta.authorization-api.jar${pathsep}${ri.lib}/jakarta.ejb-api.jar${pathsep}${ri.lib}/jakarta.enterprise.deploy-api.jar

connector.classes=${ts.home}/classes${pathsep}${connector.classes.1}

ts.run.classpath=${connector.classes}${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/connector.jar${pathsep}${ts.home}/lib/cts.jar${pathsep}${ts.home}/lib/glassfishporting.jar

ts.harness.classpath=${ts.home}/classes${pathsep}${ts.home}/lib/jaxb-api.jar${pathsep}${ts.home}/lib/jaxb-impl.jar${pathsep}${ts.home}/lib/jaxb-xjc.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/connector.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.run.classpath}${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/jdom-1.1.3.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/lib/cts.jar${pathsep}${ts.home}/lib/glassfishporting.jar

#classpath used for building CTS tests only (DO NOT MODIFY)
ts.classpath=${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/connector.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.run.classpath}${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/jdom-1.1.3.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/lib/commons-httpclient-3.0.jar${pathsep}${ts.home}/lib/commons-logging-1.0.4.jar${pathsep}${ts.home}/lib/htmlunit-1.7.jar${pathsep}${ts.home}/lib/cts.jar${pathsep}${ts.home}/lib/glassfishporting.jar

#classpath used for building connector.jar (DO NOT MODIFY)
ts.lib.classpath=${ts.harness.classpath}


###############################################################
# @s1as.classpathsuffix - The classpath suffix of
#     the RI when being used as the app server under test.
###############################################################
s1as.classpathsuffix=${connector.home}/lib/tsprovider.jar

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
        CLASSPATH=${ts.harness.classpath}${pathsep}\
                  ${ts.home}/classes${pathsep}\
                  ${JAVA_HOME}/../lib/tools.jar${pathsep}\
                  ${ts.home}/lib/commons-codec-1.9.jar${pathsep}\
                  ${connector.home}/lib/appserv-rt.jar \
        DISPLAY=${ts.display} \
        HOME="${user.home}" \
        windir=${windir} \
        SYSTEMROOT=${SYSTEMROOT} \
        PATH="${connector.home}/nativelib" \
        ${JAVA_HOME}/bin/java \
        --module-path ${J2EE_HOME}/lib/bootstrap --add-modules ALL-MODULE-PATH \
        -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9008 \
        -Dcts.tmp=$harness.temp.directory \
        -DnoSecurityManager=true \
        -Djava.protocol.handler.pkgs=javax.net.ssl \
        -Djavax.net.ssl.keyStore=${bin.dir}/certificates/clientcert.jks \
        -Djavax.net.ssl.keyStorePassword=changeit \
        -Djavax.net.ssl.trustStore=${s1as.truststore} \
        -Dcom.sun.aas.installRoot=${connector.home} \
        -Dcom.sun.aas.configRoot=${connector.home}/config \
        -Djava.util.logging.config.file=${TS_HOME}/bin/client-logging.properties \
        -Dlogical.hostname.servlet=${logical.hostname.servlet} \
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


########################################################################
## Environment for ts_win32
## 3 test execution commands inherit from common environment
## defined above: testExecuteAppClient2, testExecuteAppClient, and
## testExecute. If you need to override them, uncomment them in the
## following section.
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

sigTestClasspath=${ri.lib}/jakarta.annotation-api.jar${pathsep}${ri.lib}/jakarta.resource-api.jar${pathsep}${ri.lib}/jakarta.transaction-api.jar${pathsep}${ri.lib}/jakarta.security.auth.message-api.jar${pathsep}${ri.lib}/jakarta.security.jacc-api.jar${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming


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
########################################################################
harness.temp.directory=${ts.home}/tmp
harness.log.port=2000
harness.log.traceflag=true
harness.executeMode=2
harness.socket.retry.count=10
harness.log.delayseconds=1


#######################################################################
# @impl.vi
#              This property must refer to an ANT dir containing config
#              info in descriptor files.  There will be config (xml) files
#              in this directory that will assist with the configuration
#              and deployment tasks.
#              There following exists for Suns RI:  ts_home/bin/xml/impl/glassfish
#              Different vendors will want to implement their own config
#              files in their own dir.
#
# @impl.vi.deploy.dir
#              This points to the autodeploy dir.
#
# @impl.deploy.timeout.multiplier
#              This is a timeout value to be used if deployment doesn't return.
#
#######################################################################
impl.vi=glassfish
impl.vi.deploy.dir=${connector.home}/domains/domain1/autodeploy
impl.deploy.timeout.multiplier=10


###############################################################
## These properties must be set to tell the Test harness the
## class names of your porting class implementations.  By default
## both property sets below point to Sun RI specific classes.  To
## run interoperability tests, the ".2" set of properties should
## always point to Sun RI classes.  The ".1" set should point to
## implementations that work in your specific Java EE environment.
#
# @porting.ts.login.class.1  VI of
#    com.sun.ts.lib.porting.TSLoginContextInterface
# @porting.ts.url.class.1  VI of
#    com.sun.ts.lib.porting.TSURLInterface
# @porting.ts.HttpsURLConnection.class.1  VI of
#    com.sun.ts.lib.porting.TSHttpsURLConnectionInterface
###############################################################
porting.ts.login.class.1=com.sun.ts.lib.implementation.sun.javaee.GlassFishLoginContext
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.HttpsURLConnection.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection


## Porting pkg class that identifies the component class that the
## client will be using to invoke the client-test code.
porting.ts.component.class=SunRILocator


##############################################################
# @namingServiceHost1  Naming Service host name for the
#     Vendor's Implementation (VI)
# @namingServicePort1  Naming Service port for the VI
# @namingServiceHost2  Naming Service host name for the
#     Reference Implementation (RI)
# @ namingServicePort2  Naming Service port for the RI
##############################################################
namingServiceHost1=${orb.host}
namingServicePort1=${orb.port}
namingServiceHost2=${orb.host}
namingServicePort2=${orb.port}

##############################################################
# @certLoginUserAlias  User alias for certificate based login.
# This property is used in mutual authentication to pickup the
# certificate based on the user alias.
##############################################################
certLoginUserAlias=cts

#####################################################################
## The following properties must be set prior to running the Servlet
## or JSP API tests and interoperability tests.
##
## These properties must specify the host and port of the web server,
## in which the servlets and JSPs are deployed.  
#
# @webServerHost  hostname for the Vendor's Java EE Web Server
# @webServerPort  port number of the Vendor's Java EE Web Server
#####################################################################
webServerHost=${orb.host}
webServerPort=8001


#########################################################################
# You must be able to deploy the resource adapters (ie .rar files) which
# are contained in <top>/dist/com/sun/ts/tests/common/connector/whitebox/**
# into your connector server.  
# Each .rar file will have a corresponding JNDI name.
#
# @whitebox-embed   This is the JNDI name that will be used to
#                   establish connection to the embedded whitebox
#                   which is contained in ejb_Deployment.ear which
#                   lives under src/com/.../connnector/deployment
#                   This is only applicable for full JavaEE profile.
#
# @embeddedWhiteboxRarName  This is the name of the rar file that
#                   is deployed as part of ejb_Deployment.ear (above)
#                   The name used here will likely be impl specific and
#                   must be set on a per impl basis so that proper
#                   connector-pools and connection-resources can be 
#                   created during configuration. 
#                   This is only applicable for full JavaEE profile.
#                   
#########################################################################

# Embedded resource adapter JNDI name for a .rar thats embedded in an 
# ear file named ejb_Deployment.ear
whitebox-embed=java:comp/env/eis/ejb_Deployment_whitebox-tx.rar

# These are the JNDI names that will be used to create pools/resources
# during connector config.  These names correspond to similarly named
# connector .rar files. 
whitebox-tx=java:comp/env/eis/whitebox-tx
whitebox-tx-param=java:comp/env/eis/whitebox-tx-param
whitebox-notx=java:comp/env/eis/whitebox-notx
whitebox-xa=java:comp/env/eis/whitebox-xa
whitebox-notx-param=java:comp/env/eis/whitebox-notx-param
whitebox-xa-param=java:comp/env/eis/whitebox-xa-param
whitebox-anno_no_md=java:comp/env/eis/whitebox-anno_no_md
whitebox-ibanno_no_md=java:comp/env/eis/whitebox-ibanno_no_md
whitebox-mixedmode=java:comp/env/eis/whitebox-mixedmode
whitebox-multianno=java:comp/env/eis/whitebox-multianno
whitebox-mdcomplete=java:comp/env/eis/whitebox-mdcomplete
whitebox-permissiondd=java:comp/env/eis/whitebox-permissiondd

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
# @nobodyuser     This value must be the same value returned by a call to 
#                 getCallerPrincipal().getName() from the ejb-tier when
#                 an unauthenticated caller in the web tier invokes an
#                 ejb method.
#########################################################################
user=j2ee
password=j2ee
authuser=javajoe
authpassword=javajoe
nobodyuser=guest

###############################################################
# @securedWebServicePort must be set to run secbasicssl and
#   csiv2 tests. Set this property with your application
#   server's secured webservice port.
# @securedWebServicePort.2 points to the secured webservice
#   port in Sun's Reference Implementation(RI).
###############################################################
securedWebServicePort=8181
securedWebServicePort.2=8181

###############################################################
# @login This property must be set to run appclient security
#        tests
###############################################################
login=default


###################################################################
###################################################################
###################################################################
##  PROPERTIES USERS TYPICALLY WILL NOT HAVE TO SET LIVE BELOW
###################################################################
###################################################################
###################################################################

##
## The directory seperator for the platform.  User should not change
## this property.
##
dirsep=/

##build level
##1: compile only
##2: compile and build component archives (e.g., jar's, war's)
##3: compile and build component and application archives
##default is set to 3
build.level=2

## Deliverables wanting ts.* packaging tasks to add extension list
## attributes to the manifest files must set this property to true.
create.manifest.extension.entries=true

## Deliverables must set this property to the name of the deliverable
## specific library jar file (iff create.manifest.extension.entries is set to 
## true)
tslib.name=connector


###############################################################
## These properties are used by connector tests and are non configurable 
## and must specify valid username,password to establish TSeis connection. 
## user1 = backend DB user entry
## password1 = backend DB user entry
## rauser1 = resource adapter user1 entry
## rapassword1 = resource adapter password1 entry
## rauser2 = resource adapter user2 entry
## rapassword2 = resource adapter password2 entry
## 
###############################################################
user1=cts1
password1=cts1
rauser1=cts1
rapassword1=cts1
rauser2=cts2
rapassword2=cts2


###############################################################
## This command is only used when quickly checking any servlet
## or jsp related tests.  The test clients will be run in
## the same JVM as JavaTest in this case.  This mode can be
## enabled by passing "-Dsame.jvm=true" to the runclient or gui
## targets.  NOTE: This option is only to be used for sanity
## checking and not when running CTS for compatibility.
###############################################################
command.testExecuteSameJVM=com.sun.ts.lib.harness.ExecuteTSTestSameJVMCmd \
        $testExecuteClass $testExecuteArgs

ts_home=${ts.home}



###################################################################
#
#  Web Profile TCK properties
#
# @logical.hostname.servlet
#              This used to identify the the name of a logical host that
#              processes Servlet requests. Servlet requests may be directed to
#              a logical host using various physical or virtual host names or
#              addresses, and a message processing runtime may be composed of
#              multiple logical hosts (This is required to properly identify
#              the servlet profiles  AppContextId hostname.)
#              If a logical.hostname.servlet does not exist, you can set this
#              to the default hostname (eg webServerHost).
$
# @logical.hostname.soap
#              This property is used to identify the name of the logical host
#              that processes soap requests. This hostname is used in server
#              side Application context Identifier in soap profile.
#
# @platform.mode
#              This property is used to to know whether to use the Java EE
#              Programming Model for service and port access or the JAXRPC/JAXWS/Connector
#              Programming model based on the runtime execution platform.
#                  platform.mode=standalone    JAXRPC/JAXWS/Connector Programming Model
#
#              Note: When you switch platform.mode make sure you change the property
#              deliverable.class as well(this is appilcable only for Connector tests)
#
# @deliverable.class
#              This property is used to identify the deliverable mode based on
#              the platform.mode setting.  This means that the deliverable
#              class setting must correspond to the platform mode setting.
#              Valid deliverable class types are:
#                  com.sun.ts.lib.deliverable.connector.StandaloneDeliverable
#
###################################################################
logical.hostname.servlet=server
logical.hostname.soap=localhost
platform.mode=standalone
deliverable.class=com.sun.ts.lib.deliverable.connector.StandaloneDeliverable


###########################################################################
# tools.jar should be set to the location of the tools.jar from the installed
# jdk
###########################################################################
tools.jar=${jdk.home}/lib/tools.jar

###############################################################
# @extension.dir - The extension directory of RI. 
# The CTS config.vi target will copy the CTS library 
# jars to this location, used only for RI.
###############################################################
extension.dir=${connector.domain}/lib
