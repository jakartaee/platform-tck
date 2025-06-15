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
##
##   JavaTest Environment file for Java Authorization Contract for Containers
##   Standalone Test Suite (JACC - JSR 115)
##
##   Environment specific properties in this file will likely
##   have to be modified prior to running the JACC TCK. 
##   Instructions for modifying these properties are contained in this
##   file.
##
##   This file is processed by an external tool that helps generate the
##   TCK documents.  Therefore this file has a standard format that must
##   be followed.  This file is a standard Java Properties file with
##   very specific comment formatting.  Users can write property specific
##   comments by using the property name and an ampersand (@).  As an
##   example the following comment applies to the foo.bar property:
##       # @foo.bar  - This is a comment pertaining to foo.bar
##       #             that spans multiple lines.
##   This comment must be preceded by a single hash (#) charater and
##   the property name must be prepended with an ampersand (@).  The
##   comment can appear anywhere in the ts.jte file.  If users have
##   comments that belong in ts.jte but DO NOT pertain to a particular
##   property the user must start the comment with at least 2 hash (#)
##   characters.  The following is a valid non-property comment:
##     ## A valid non-property comment
##     ## that spans multiple lines.
#########################################################################

########################################################################
## Javatest batch mode work directory and report directory, and policy for 
## handling existing work and report directories.  These properties affects
## runclient and report targets, but not gui target.
## To disable generating test report, unset report.dir, or set it to "none" 
## either here or from command line, as in the following command:
##               ant runclient -Dreport.dir="none"
##
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
#                    if in TCK workspace
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

## Settings for Vendor JACC Implementation
jacc.home=/sun/glassfish4/glassfish
jacc.host=localhost

########################################################################
# @orb.port    The port number the vendor implementation is listening
#              to for service requests.
########################################################################
orb.port=3699


###########################################################################
# @endorsed.dirs  If using JavaSE 6 or above and you provide newer versions
#                 of technologies than those contained in Java SE 6, verify
#                 that the property endorsed.dirs is set to the location of
#                 the VI api jars for those technologies you wish to
#                 overrride.  For example, JavaSE 6 contains an
#                 implementation of JAXWS 2.0 which will conflict with
#                 JAXWS 2.1, therefore this property would need to be set
#                 so that JAXWS 2.1 would be used during the building of
#                 tests and during test execution.
#
###########################################################################
endorsed.dirs=${jacc.home}/modules/endorsed

########################################################################
# The following properties are implementation specific and are needed
# to set information specific to your jacc provider.
########################################################################
s1as.admin.user=admin
s1as.admin.passwd=
s1as.admin.host=${jacc.host}
s1as.admin.port=4848
s1as.admin=${jacc.home}/bin/asadmin
s1as.server=server
s1as.domain.dir=${jacc.home}/domains
s1as.domain.name=domain1
s1as.domain=${s1as.domain.dir}/${s1as.domain.name}
s1as.asenv.loc=${jacc.home}/config
s1as.imqbin.loc=${jacc.home}/imq/bin
s1as.lib=${jacc.home}/lib
s1as.imq.share.lib=${jacc.home}/imq/lib
s1as.truststore=${s1as.domain}/config/cacerts.jks
s1as.jvm.options=-Doracle.jdbc.J2EE13Compliant=true
s1as.java.endorsed.dirs=${endorsed.dirs}
s1as.applicationRoot=c:

###############################################################
# @extension.dir - The extension directory for the app server under test.
#
# Note: App server vendors will need to set this to their
#       app server's extension directory.  The config.vi
#       target will copy the library jars(jacctck.jar,tsharness.jar) 
#        to this location.
###############################################################
extension.dir=${s1as.domain}/lib

##############################################################################
# @sjsas.master.password  -- Used as default password for keystore &  truststore
#                            Defaults to changeit. 
##############################################################################
sjsas.master.password=changeit

###############################################################
# @instance.listenerName - Default value for the iiop listener
#                          for your instance.  Users will
#                          most likely not need to change this.
###############################################################
instance.listenerName=orb-listener-1


###############################################################
## When installing TCK/RI on Windows, users must install the TCK and
## the RI on the same drive.  Also note that you should never
## specify drive letters in any path defined in this properties
## file.
###############################################################
## Users must set this property when running on Windows.  The
## appropriate value on Windows is a semi-colon (;).  If you are
## not running on Windows leave this property set to its default
## value of colon (:) for other .
###############################################################
pathsep=:

###############################################################
## The directory separator for the platform.  User should not change
## this property.
###############################################################
dirsep=/

###############################################################
# @ts.display -- location to display TCK output on Unix
###############################################################
ts.display=:0.0

###############################################################
# @tz - your local timezone. For valid values, consult your
#       Operating System documentation.
###############################################################
tz=US/Eastern


###############################################################
## Configure the behavior of whether tables will be created when
## ant init.database is invoked.
#
# NOTE: The JACC Specification permits
# DDL generation to be supported by an implementation but
# it is not required.
# 
# @create.cmp.tables
#	-  When set to false, the persistence provider
# 	   is responsible for creating tables
#
# 	-  When set to true, init.datbaseName will
# 	   create the tables used by the persistence provider
#
###############################################################
create.cmp.tables=true

###############################################################
#
# The sql for the tables are contained in:
#
#  $TS_HOME/[databaseName]/sql/[databaseName].ddl.persistence.sql
#
# @databaseName
# 	Defines which database will be used for certification.
#	This property will be used to determine the corresponding sql
#       to intialize and can be one of the following:
#
# 	- derby
#	- mysql
#	- pointbase
#	- sybase
#	- db2
#	- mssqlserver
#	- oracle
#	- postgresql
#
# If using a database other than above, you need to create
# your own DDL files but can use these files for reference of what
# tables are required.
#
###############################################################
databaseName=derby

###############################################################
# @jdbc.lib.class.path - This property is used by the
#                        database.classes properties to point to
#                        where the JDBC drivers live.  
###############################################################
jdbc.lib.class.path=${jacc.home}/javadb/lib
#
###############################################################
##
## Info to be Used for DataBase Initialization
##
###############################################################
database.dbName=derbyDB
database.server=${jacc.host}
database.port=1527
database.user=cts1
database.passwd=cts1
database.url=jdbc:derby://${database.server}:${database.port}/${database.dbName};create=true
database.driver=org.apache.derby.jdbc.ClientDriver
database.classes=${jdbc.lib.class.path}/derbyclient.jar${pathsep}${jdbc.lib.class.path}/derbyshared.jar${pathsep}${jdbc.lib.class.path}/derbytools.jar
database.dataSource=org.apache.derby.jdbc.ClientDataSource
database.properties=DatabaseName\=\"${database.dbName}\":user\=${database.user}:password\=${database.passwd}:serverName\=${database.server}:portNumber=${database.port}

###############################################################
## Schema locations for Persistence xml files.
## Used for xml validation when building tests.
###############################################################
alt.schema.dir=${lib.dir}/schemas

###############################################################
## Classpath properties required by the JACC TCK:
# @jacc.classes          --  Classes required by RI 
# @ts.run.classpath 	--  Classes needed for test run
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the Persistence tests
# @ts.lib.classpath     --  Classes used to build cts.jar 
###############################################################

ri.lib=${jacc.home}/modules
lib.dir=${ts.home}/lib

jacc.classes=${ri.lib}/jakarta.authorization-api.jar:${ri.lib}/jakarta.security.auth.message-api.jar:${ri.lib}/common-util.jar:${ri.lib}/container-common.jar:${ri.lib}/jakarta.annotation-api.jar:${ri.lib}/jakarta.resource-api.jar:${ri.lib}/jmxremote_optional-repackaged.jar:${ri.lib}/ldapbp-repackaged.jar:${ri.lib}/security.jar:${ri.lib}/web-core.jar:${ri.lib}/web-glue.jar:${ri.lib}/websecurity.jar:${ri.lib}/jakarta.servlet-api.jar:${ri.lib}/jakarta.ejb-api.jar:${ri.lib}/jakarta.persistence.jar:${ri.lib}/jakarta.transaction-api.jar:${ri.lib}/jakarta.jms-api.jar:${ri.lib}/jakarta.servlet.jsp-api.jar

ts.run.classpath=${ts.home}/classes:${database.classes}:${jacc.classes}

ts.harness.classpath=${lib.dir}/tsharness.jar:${lib.dir}/sigtest.jar:${lib.dir}/commons-logging-1.1.3.jar:${lib.dir}/commons-httpclient-3.1.jar:${ts.run.classpath}:${lib.dir}/javatest.jar:${ant.home}/lib/ant.jar

#classpath used for building JACC tests only (DO NOT MODIFY)
ts.classpath=${ts.harness.classpath}:${jacc.classes}

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
#
########################################################################
command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${ts.harness.classpath}:\
                  ${JAVA_HOME}/../lib/tools.jar \
        DISPLAY=${ts.display} \
        HOME="${user.home}" \
        windir=${windir} \
        SYSTEMROOT=${SYSTEMROOT} \
        ${JAVA_HOME}/bin/java \
        --module-path ${jacc.home}/lib/bootstrap --add-modules ALL-MODULE-PATH \
        -Dcts.tmp=$harness.temp.directory \
        -Dlog.file.location=${log.file.location} \
        -Djava.security.policy=${bin.dir}/harness.policy \
        -Dcom.sun.aas.installRoot=${jacc.home} \
        -Djava.protocol.handler.pkgs=javax.net.ssl \
        -Djavax.net.ssl.keyStore=${bin.dir}/certificates/clientcert.jks \
        -Djavax.net.ssl.keyStorePassword=changeit \
        -Djavax.net.ssl.trustStore=${s1as.truststore} \
        -Djavax.net.ssl.trustStorePassword=changeit \
        -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs


#########################################################################
## Environment for ts_unix
## test execution command inherit from common environment
## defined above:  testExecute.
## If you need to override them, uncomment them in the
## following section.
#########################################################################
env.ts_unix.menu=true
##env.ts_unix.command.testExecute=

########################################################################
## Environment for ts_win32
## test execution commands inherit from common environment
## defined above: testExecute.
## If you need to override them, uncomment them in the
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
#                    Java Persistence and Java SE classes. Paths must be
#                    separated by the appropriate path separator
#                    (';' windows, ':' Unixes).
#########################################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${jacc.home}/modules/jakarta.authorization-api.jar:${jacc.home}/modules/jakarta.security.auth.message-api.jar${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming


########################################################################
## These properties are used by the harness.  "harness.log.port"
## specifies the port that server components use to send logging
## output back to JavaTest.  If the default port # is not available
## on the machine running JavaTest, then you can set it here.
##
## "harness.log.traceflag" is used to turn on/off verbose debugging output
##  for the tests.
##
## "harness.executeMode" is used to run the harness in the following modes
##  of execution:
##    0 - default (deploy, run, undeploy)
##    1 - deploy only
##    2 - run only
##    3 - undeploy only
##    4 - deploy and run only
##
##
##  @harness.socket.retry.count - denotes the number of time we should
##    attempt to create a server socket when intilizing a test
##    client.  The socket is used for logging purposes.
##
########################################################################
harness.temp.directory=${ts.home}/tmp
harness.log.port=2000
harness.log.traceflag=true
harness.executeMode=0
harness.log.delayseconds=1
harness.socket.retry.count=10

###############################################################
## These properties must be set to tell the Test harness the
## class names of your porting class implementations.
#
# @porting.ts.url.class.1  VI of
#    com.sun.ts.lib.porting.TSURLInterface
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL

###################################################################
# @log.file.location  This property is used by JACC tests to create
#   and analyze provider logs. Specify a log directory
#   
###################################################################
log.file.location=${jacc.home}/domains/domain1/logs

#####################################################################
## These properties must specify the host and port of the web server,
## in which the servlets and JSPs are deployed.
#
# @webServerHost  hostname for the Vendor's  Web Server
# @webServerPort  port number of the Vendor's  Web Server
#####################################################################
webServerHost=${jacc.host}
webServerPort=8080

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
# @securedWebServicePort must be set to run ssl tests 
#   server's secured webservice port.
###############################################################
securedWebServicePort=8181

###################################################################
###################################################################
###################################################################
##  PROPERTIES USERS WILL NOT HAVE TO SET ARE BELOW
###################################################################
###################################################################
###################################################################

##build level
##1: compile only
##2: compile and build component archives (e.g., jar's, war's)
##3: compile and build component and application archives
##default is set to 3
build.level=2

###############################################################
## These properties must be set to tell the Test harness the
## class names of your porting class implementations.  By default
## both property sets below point to Sun RI specific classes.  To## run interoperability tests, the ".2" set of properties should
## always point to Sun RI classes.  The ".1" set should point to## implementations that work in your specific Java EE environment.
#
# @porting.ts.url.class.1  VI of
#    com.sun.ts.lib.porting.TSURLInterface
# @porting.ts.HttpsURLConnection.class.1  VI of
#    com.sun.ts.lib.porting.TSHttpsURLConnectionInterface
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.HttpsURLConnection.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection

##Porting class names for Sun RI Java EE Implementation #2 (must be Sun's RI)
porting.ts.url.class.2=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.HttpsURLConnection.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection


###############################################################
## JPA Deliverable Class
## DO NOT CHANGE THIS PROPERTY
############################################################### 
deliverable.class=com.sun.ts.lib.deliverable.jacc.JACCDeliverable

#######################################################################
## platform.mode is used to tell the enviroment we are in standalone
## mode
## DO NOT CHANGE THIS PROPERTY
######################################################################
platform.mode=standalone

#######################################################################
## Location of ts_home
## DO NOT CHANGE THIS PROPERTY
######################################################################

ts_home=${ts.home}

###################################################################
## Deliverables wanting ts.* packaging tasks to add extension list
## attributes to the manifest files must set this property to true.
###################################################################
#create.manifest.extension.entries=

######################################################################
## Deliverables must set this property to the name of the deliverable
## specific library jar file (iff create.manifest.extension.entries 
## is set to true)
######################################################################
tslib.name=jacctck

########################################################################
## Level of Vendor Java EE Implementation
# @javaee.level The level of Java EE support for the implementation
#               test.  This level is used to determine Java EE support
#               when deploying archives - whether ears are supported,
#               etc.  Currently 2 values are supported:
#
#               full - for full Java EE Platform support
#               web  - for Java EE Web Profile [plus optional tech.]
########################################################################
javaee.level=web


####################################################################
# Implementation Property Settings for Vendor and RI
#
# The jacctck provides support for glassfish implementation out of the box.
#
# Here are the common properties that need to be defined for the common
# implementation functionality:
#
# @impl.vi          This property must be set to the Vendor implementation
#                                 under test.
# @impl.vi.deploy.dir This property must be set to the deploy directory for
#                                 the Vendor implementation.
# @impl.vi.host     This property must be set to the webserver host where
#                                the Vendor implementation is running.
# @impl.vi.port     This property must be set to the webserver port where
#                                 the Vendor implementation is running.
# @impl.ri          This property must be set to the RI implementation
#                                 under test.
# @impl.ri.deploy.dir This property must be set to the deploy directory for
#                                 the RI implementation.
# @impl.ri.host    This property must be set to the webserver host where
#                                 the RI implementation is running.
# @impl.ri.port    This property must be set to the webserver port where
#                                 the RI implementation is running.
#
# @impl.deploy.timeout.multiplier  The time it will wait for deployment to
#                                   succeed or fail
####################################################################
impl.vi=glassfish
impl.vi.deploy.dir=${s1as.domain}/autodeploy
impl.vi.host=${webServerHost}
impl.vi.port=${webServerPort}

impl.ri=glassfish
impl.ri.deploy.dir=${s1as.domain}/autodeploy
impl.ri.host=${webServerHost}
impl.ri.port=${webServerPort}

impl.deploy.timeout.multiplier=30


