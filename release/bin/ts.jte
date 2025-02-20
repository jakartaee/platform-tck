#
# Copyright (c) 2006, 2025 Oracle and/or its affiliates and others.
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
##
##   JavaTest Environment file for Jakarta EE Compatibility Test Suite
##
##   Environment specific properties in this file will likely
##   have to be modified prior to running the Jakarta EE TCK. 
##   Instructions for modifying these properties are contained in this
##   file.
##
##   This file is a standard Java Properties file with
##   very specific comment formatting.  Users can write property specific
##   comments by using the property name and an ampersand (@).  As an
##   example the following comment applies to the foo.bar property:
##       # @foo.bar  - This is a comment pertaining to foo.bar
##       #             that spans multiple lines.
#########################################################################

#########################################################################
## As of Jakarta EE 11, this file is only used to provide these properties
## used by the legacy vehicle framework:
# s1as
# s1as.modules
# Driver
# authpassword
# authuser
# binarySize
# cofSize
# cofTypeSize
# db.dml.file
# db.supports.sequence
# db1
# db2
# DriverManager
# ftable
# generateSQL
# harness.log.port
# harness.log.traceflag
# harness.socket.retry.count
# harness.temp.directory
# imap.port
# iofile
# java.naming.factory.initial
# javamail.mailbox
# javamail.password
# javamail.protocol
# javamail.root.path
# javamail.server
# javamail.username
# jdbc.db
# jms_timeout
# jstl.db.user
# jstl.db.password
# log.file.location
# logical.hostname.servlet
# longvarbinarySize
# mailuser1
# org.omg.CORBA.ORBClass
# password
# password1
# platform.mode
# porting.ts.HttpsURLConnection.class.1
# porting.ts.HttpsURLConnection.class.2
# porting.ts.login.class.1
# porting.ts.login.class.2
# porting.ts.url.class.1
# porting.ts.url.class.2
# porting.ts.jms.class.1
# porting.ts.jms.class.2
# ptable
# rapassword1
# rapassword2
# rauser1
# rauser2
# securedWebServicePort
# sigTestClasspath
# smtp.port
# transport_protocol
# ts_home
# user
# user1
# varbinarySize
# variable.mapper
# vehicle_ear_name
# webServerHost
# webServerPort
# whitebox-anno_no_md
# whitebox-mdcomplete
# whitebox-mixedmode
# whitebox-multianno
# whitebox-notx
# whitebox-notx-param
# whitebox-permissiondd
# whitebox-tx
# whitebox-tx-param
# whitebox-xa
# whitebox-xa-param
# work.dir
# ws_wait
#########################################################################

#########################################################################
## @jte.version  This version denotes the bundle this JTE was
##               originally included with.  The version matches the ID
##               in the bundle name.  It can be used to identify a
##               mismatched JTE file.  This value is filled in during
##               the build process as part of sanitizing the jte file.
#########################################################################
jte.version=@JTE_VERSION@

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
work.dir=/tmp/JTWork
report.dir=/tmp/JTReport
if.existing.work.report.dirs=auto


########################################################################
## Settings for Vendor Java EE Implementation
# @javaee.home The location of the vendor's Java EE platform
#              implementation.
# @orb.host    Hostname of the machine running the vendor's
#              implementation.
# @orb.port    The port number the vendor implementation is listening
#              to for service requests.
########################################################################
javaee.home=
orb.host=localhost
orb.port=3699


########################################################################
## Settings for Sun RI Java EE Implementation
# @javaee.home.ri The location of the RI.
# @orb.host       Hostname of the machine running the RI.
# @orb.port       The port number the RI is listening to for service
#                 requests.
########################################################################
javaee.home.ri=
orb.host.ri=localhost
orb.port.ri=3700

###################################################################
###################################################################
###################################################################
##  RI SPECIFIC PROPERTIES LIVE BELOW
###################################################################
###################################################################
###################################################################

###############################################################
## Config params needed for Java EE RI asadmin
## You must change these values as needed
##
# @ri.admin.user     -- The Java EE RI asadmin user id
# @ri.admin.passwd   -- The Java EE RI asadmin user password
# @ri.admin.host     -- The Java EE RI host
# @ri.admin.port     -- The Java EE RI port
# @ri.admin          -- The Java EE RI admin command 
# @ri.server         -- The Java EE RI server instance being used
# @ri.domain.dir     -- Points to where your domains are installed.
# @ri.domain.name    -- The Java EE RI domain being used
# @ri.domain         -- The Java EE RI domain path being used
# @ri.asenv.loc      -- location of asenv.conf or asenv.bat
# @ri.imqbin.loc     -- location of the IMQ bin directory
# @ri.lib            -- Library directory for other Java EE RI
#                       jars
# @ri.imq.share.lib  -- Shared library directory for imq 
# @ri.jvm.options    -- Java options needed by the Java EE RI
#                       note, the second option is not needed
#                       but is required to work around asadmin
#                       command line parsing issues.  The
#                       xxxlogin and xxxpassword are used
#                       to set known server side creds for use
#                       with connector tests.
# @ri.applicationRoot-- Location of application repository
#                       Only needed when running on windows
# @ri.and.vi.run.on.same.host  - set to true if interop tests are run 
#                       with both RI and VI on same machine.  set to
#                       false if they are run on different machines.
#                       This is used to work around an orb issue
#                       specific to running VI and RI on same box.
#
# @ri.orb.iiop.orbserverid - This is used to set a similarly
#                       named jvm option in the RI.  It is only used
#                       when we are running interop where remote EJBs 
#                       try to access target EJB's on the same host with
#                       zero port configuration for ssl.
#                       This only gets used when ri.and.vi.run.on.same.host=true.
#                       The value is to be an ORB server id.
###############################################################
ri.admin.user=admin
ri.admin.passwd=
ri.admin.host=${orb.host.ri}
ri.admin.port=4848
ri.admin=${javaee.home.ri}/bin/asadmin
ri.server=server
ri.domain.dir=${javaee.home.ri}/domains
ri.domain.name=domain1
ri.domain=${ri.domain.dir}/${ri.domain.name}
ri.asenv.loc=${javaee.home.ri}/config
ri.imqbin.loc=${javaee.home.ri}/../mq/bin
ri.lib=${javaee.home.ri}/lib
ri.log.file.location=${ri.domain}/logs
ri.modules=${javaee.home.ri}/modules
ri.imq.share.lib=${javaee.home.ri}/../mq/lib
ri.jvm.options=-Doracle.jdbc.J2EE13Compliant=true:-Xmx4096m:-Dj2eelogin.name=${user}:-Dj2eelogin.password=${password}:-Deislogin.name=${user1}:-Deislogin.password=${password1}:-Dtest.ejb.stateful.timeout.wait.seconds=${test.ejb.stateful.timeout.wait.seconds}:-DwebServerPort.2=${webServerPort.2}:-DwebServerHost.2=${webServerHost.2}:-Dcsiv2.save.log.file=${harness.log.traceflag}:-Djavax.xml.accessExternalStylesheet=all:-Djavax.xml.accessExternalDTD=file,http
ri.jvm.options.remove=-Xmx512m:${ri.jvm.options}
ri.java.endorsed.dirs=${endorsed.dirs.ri}
ri.applicationRoot=c:
ri.and.vi.run.on.same.host=true
ri.orb.iiop.orbserverid=200

###############################################################
## Config params needed for Java EE VI asadmin
## You must change these values as needed
##
# @vi.admin.user     -- The Java EE VI asadmin user id
# @vi.admin.passwd   -- The Java EE VI asadmin user password
# @vi.admin.host     -- The Java EE VI host
# @vi.admin.port     -- The Java EE VI port
# @vi.admin          -- The Java EE VI admin command 
# @vi.server         -- The Java EE VI server instance being used
# @vi.domain.dir     -- Points to where your domains are installed.
# @vi.domain.name    -- The Java EE VI domain being used
# @vi.domain         -- The Java EE VI domain path being used
# @vi.asenv.loc      -- location of asenv.conf or asenv.bat
# @vi.imqbin.loc     -- location of the IMQ bin directory
# @vi.lib            -- Library directory for other Java EE VI
#                       jars
# @vi.imq.share.lib  -- Shared library directory for imq 
# @vi.jvm.options    -- Java options needed by the Java EE VI
#                       note, the second option is not needed
#                       but is required to work around asadmin
#                       command line parsing issues.  The
#                       xxxlogin and xxxpassword are used
#                       to set known server side creds for use
#                       with connector tests.
# @vi.applicationRoot-- Location of application repository
#                       Only needed when running on windows
###############################################################
vi.admin.user=admin
vi.admin.passwd=
vi.admin.host=${orb.host}
vi.admin.port=4848
vi.admin=${javaee.home}/bin/asadmin
vi.server=server
vi.domain.dir=${javaee.home}/domains
vi.domain.name=domain1
vi.domain=${vi.domain.dir}/${vi.domain.name}
vi.asenv.loc=${javaee.home}/config
vi.imqbin.loc=${javaee.home}/../mq/bin
vi.lib=${javaee.home}/server/lib
vi.log.file.location=${vi.domain}/logs
vi.modules=${javaee.home}/modules
vi.imq.share.lib=${javaee.home}/../mq/lib
vi.jvm.options=-Doracle.jdbc.J2EE13Compliant=true:-Xmx4096m:-Dj2eelogin.name=${user}:-Dj2eelogin.password=${password}:-Deislogin.name=${user1}:-Deislogin.password=${password1}:-Dtest.ejb.stateful.timeout.wait.seconds=${test.ejb.stateful.timeout.wait.seconds}:-DwebServerPort.2=${webServerPort.2}:-DwebServerHost.2=${webServerHost.2}:-Dcsiv2.save.log.file=${harness.log.traceflag}:-Djavax.xml.accessExternalStylesheet=all:-Djavax.xml.accessExternalDTD=file,http
vi.jvm.options.remove=-Xmx512m:${vi.jvm.options}
vi.java.endorsed.dirs=${endorsed.dirs}
vi.applicationRoot=c:

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
# @s1as.applicationRoot-- Location of application repository
#                         Only needed when running on windows
###############################################################
s1as.admin.user=admin
s1as.admin.passwd=
s1as.admin.host=${orb.host}
s1as.admin.port=4848
s1as.admin=${javaee.home}/bin/asadmin
s1as.server=server
s1as.domain.dir=${javaee.home}/domains
s1as.domain.name=domain1
s1as.domain=${s1as.domain.dir}/${s1as.domain.name}
s1as.asenv.loc=${javaee.home}/config
s1as.imqbin.loc=${javaee.home}/../mq/bin
s1as.lib=${javaee.home}/lib
s1as.modules=${javaee.home}/modules
s1as.imq.share.lib=${javaee.home}/../mq/lib
s1as.jvm.options=-Doracle.jdbc.J2EE13Compliant=true:-Xmx4096m:-Dj2eelogin.name=${user}:-Dj2eelogin.password=${password}:-Deislogin.name=${user1}:-Deislogin.password=${password1}:-Dtest.ejb.stateful.timeout.wait.seconds=${test.ejb.stateful.timeout.wait.seconds}:-DwebServerPort.2=${webServerPort.2}:-DwebServerHost.2=${webServerHost.2}
s1as.jvm.options.remove=-Xmx512m:${s1as.jvm.options}
s1as.java.endorsed.dirs=${endorsed.dirs}
s1as.applicationRoot=c:

###############################################################
# @sjsas.das.orb.port              -- ORB port number for the DAS
# @sjsas.das.orb.host              -- ORB host name for the DAS
# @sjsas.das.webServerPort         -- HTTP listener port for the DAS
# @sjsas.das.securedWebServicePort -- HTTPS listener port for the DAS
# @sjsas.nodeagent.name            -- Name of node agent used by
#                                     the remote instance.
# @sjsas.nodeinstance.name         -- Name of the remote instance 
# @sjsas.master.password           -- Used to create a node agent only
#                                     applicable to EE.  Defaults to changeit. This
#                                     can be changed at EE install time.
# @sjsas.instance.config.dir       -- config directory used by the intsance
#                                     being tested.
#                                     For PE/DAS = config
#                                     For remote instance = config
# @sjsas.cts.timer.resource        -- Backend to use when we are using java2db with
#                                     the CMP tests for the jdbc/DBTimer resource
# @sjsas.cmp.backend               -- Backend to use when we are using java2db with
#                                     the CMP tests
# @sjsas.node.agent.dir.name       -- The name of the node agent directory to use.
#                                     This value will be used on Windows only and
#                                     ignored on non-Windows platforms.  The default
#                                     is 'n' meaning the create-node-agent command
#                                     will pass the --agentdir argument with a value
#                                     of ${s1as.applicationRoot}\${sjsas.node.agent.dir.name}.
# @sjsas.env.type                  -- CTS test configuration.  Possible values are:
#                                     das     (for PE or DAS)
#                                     remote  (for remote intance)
#                                     cluster (for cluster config not yet supported)
# @s1as.targets                    -- Instance(s) to deploy tests
#                                     Supports multiple instances, For example:
#                                     s1as.targets=server server-1
###############################################################
sjsas.das.orb.port=3700
sjsas.das.orb.host=${orb.host}
sjsas.das.webServerPort=8000
sjsas.das.securedWebServicePort=1043
sjsas.nodeagent.name=node-agent-1
sjsas.nodeinstance.name=server-1
sjsas.master.password=changeit
sjsas.instance.config.dir=config
sjsas.cts.timer.resource=derby
sjsas.cmp.backend=derby
sjsas.node.agent.dir.name=n

sjsas.env.type=das
#sjsas.env.type=remote

s1as.targets=${s1as.server}
#s1as.targets=${sjsas.nodeinstance.name}

###############################################################
# @s1as.pe.jmsServer      -- name of the JMS server the RI/PE
# @s1as.se.jmsServer      -- name of the JMS server for SE/EE
###############################################################
s1as.pe.jmsServer=imqbroker
s1as.se.jmsServer=imqbroker
#s1as.se.jmsServer=${s1as.domain.name}_${s1as.server}

###############################################################
# @extension.dir - The extension directory for the app
#                  server under test.  This does not apply
#                  to the RI.
#
# Note: App server vendors will need to set this to their
#       app server's extension directory.  The CTS config.vi
#       target will copy the CTS library jars to this location.
###############################################################
extension.dir=${s1as.domain}/lib

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
## The following section is for CTS Database configuration
## For each database that you will test, configure the following:
##   Here is an example using derby:  Users will need to
##   replace derby with the appropriate DB name.
# @derby.dbName       -- Database Name
# @derby.server       -- Database Server
# @derby.port         -- Database Server port
# @derby.dml.file     -- DML file used for CTS test cases
# @derby.user         -- User Id configured
# @derby.passwd       -- User password configured
# @derby.url          -- URL to the cts database
# @derby.driver       -- DriverManager driver
# @derby.classes      -- CLASSPATH to JDBC driver classes
# @derby.poolName     -- Name of pool configured in the Java EE
#                          RI (do not change!)
# @derby.dataSource   -- DataSource driver
# @derby.properties   -- Any additional JDBC driver required
#                          properties
# @derby.startup.delay-- delay to wait for DB to start

###############################################################

##
## Info for Cloudscape 10/Derby
##
derby.dbName=derbyDB
derby.server=${orb.host}
derby.port=1527
derby.port.ri=1527
derby.dml.file=derby/derby.dml.sql
derby.user=cts1
derby.passwd=cts1
derby.url=jdbc:derby://${derby.server}:${derby.port}/${derby.dbName};create=true
derby.driver=org.apache.derby.jdbc.ClientDriver
derby.home=${javaee.home}/../javadb
derby.system.home=${derby.home}/databases
derby.classpath=${ts.home}/lib/dbprocedures.jar${pathsep}${derby.home}/lib/derbynet.jar${pathsep}${derby.home}/lib/derbyshared.jar${pathsep}${derby.home}/lib/derbytools.jar
derby.classes=${derby.home}/lib/derbyclient.jar${pathsep}${derby.home}/lib/derbyshared.jar${pathsep}${derby.home}/lib/derbytools.jar
derby.poolName=cts-derby-pool
derby.dataSource=org.apache.derby.jdbc.ClientDataSource
derby.properties=DatabaseName\=\"${derby.dbName}\":user\=${derby.user}:password\=${derby.passwd}:serverName\=${derby.server}:portNumber=${derby.port}
derby.startup.delay=5

#
# Cloudscape 10 /Derby embedded driver
#
derbyEmbedded.dbName=/tmp/DerbyDB
derbyEmbedded.server=${orb.host}
derbyEmbedded.port=1527
derbyEmbedded.dml.file=derby/derby.dml.sql
derbyEmbedded.user=cts1
derbyEmbedded.passwd=cts1
derbyEmbedded.url=jdbc:derby:${derbyEmbedded.dbName};create=true
derbyEmbedded.driver=org.apache.derby.jdbc.EmbeddedDriver
derbyEmbedded.classes=${javaee.home}/../javadb/lib/derby.jar${pathsep}${javaee.home}/../javadb/lib/derbyshared.jar${pathsep}${javaee.home}/../javadb/lib/derbytools.jar${pathsep}${ts.home}/lib/dbprocedures.jar
derbyEmbedded.poolName=cts-derbyEmbedded-pool
derbyEmbedded.dataSource=org.apache.derby.jdbc.EmbeddedDataSource
derbyEmbedded.pool.url='jdbc\\:derby\\:${derbyEmbedded.dbName}\\;create=true'
derbyEmbedded.properties=DatabaseName\=\"${derbyEmbedded.dbName}\":user\=${derbyEmbedded.user}:password\=${derbyEmbedded.passwd}



###############################################################
## The following database configuration section is for the CTS
## interop tests which require a database that is used by the
## reference implementation. These entries should not be changed.
# @derby.dbName.ri        -- Database Name
# @derby.server.ri        -- Database Server
# @derby.port.ri          -- Database Server port
# @derby.dml.file.ri      -- DML file used for CTS test cases
# @derby.user.ri          -- User Id configured
# @derby.passwd.ri        -- User password configured
# @derby.url.ri           -- URL to the cts database
# @derby.driver.ri        -- DriverManager driver
# @derby.classes.ri       -- CLASSPATH to JDBC driver classes
# @derby.poolName.ri      -- Name of pool configured in the Java EE
#                          RI (do not change!)
# @derby.dataSource.ri    -- DataSource driver
# @derby.properties.ri    -- Any additional JDBC driver required
#                          properties
# @derby.startup.delay.ri -- delay to wait for DB to start
###############################################################
derby.dbName.ri=derbyDBri
derby.server.ri=${orb.host.ri}
derby.port.ri=1527
derby.dml.file.ri=derby/derby.dml.sql
derby.user.ri=cts1
derby.passwd.ri=cts1
derby.url.ri=jdbc:derby://${derby.server.ri}:${derby.port.ri}/${derby.dbName.ri};create=true
derby.driver.ri=org.apache.derby.jdbc.ClientDriver
derby.home.ri=${javaee.home.ri}/../javadb
derby.system.home.ri=${derby.home.ri}/databases
derby.classpath.ri=${ts.home}/lib/dbprocedures.jar${pathsep}${derby.home.ri}/lib/derbynet.jar${pathsep}${derby.home.ri}/lib/derbyshared.jar${pathsep}${derby.home.ri}/lib/derbytools.jar
derby.classes.ri=${derby.home.ri}/lib/derbyclient.jar{pathsep}${derby.home.ri}/lib/derbyshared.jar${pathsep}${derby.home.ri}/lib/derbytools.jar
derby.poolName.ri=cts-derby-pool
derby.dataSource.ri=org.apache.derby.jdbc.ClientDataSource
derby.properties.ri=DatabaseName\=\"${derby.dbName.ri}\":user\=${derby.user.ri}:password\=${derby.passwd.ri}:serverName\=${derby.server.ri}:portNumber=${derby.port.ri}
derby.startup.delay.ri=5
###############################################################
# @alt.dtd.dir DTD location for Java EE and RI xml files.  Used
#              for xml validation when building tests. If 
#              javaee.home.ri is set, <javaee.home.ri>/lib/dtds
#              will be used and alt.dtd.dir is ignored.
# @alt.schema.dir schema location for Java EE and RI xml files.
#                 Used for xml validation when building tests. 
#                 If javaee.home.ri is set,
#                 <javaee.home.ri>/lib/schemas will be used and
#                 alt.schema.dir is ignored.
###############################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas

###############################################################
## Configure the behavior of which tables CTS will create when
## ant init.[datbaseName] is invoked.
# 
# @create.cmp.tables       - When set to false, the appserver is
#                            responsible for creating cmp tables
#                            at deployment of the ejb/ear
#                            When set to true, init.[datbaseName]
#                            will create the tables used by CMP
#                            EJBs.  The sql for the CMP tables are
#                            contained in:
#                            $TS_HOME/[datbaseName]/sql/[databaseName].ddl.cmp.sql
#                            $TS_HOME/[datbaseName]/sql/[databaseName].ddl.interop.sql
#
###############################################################
create.cmp.tables=true

###############################################################
# @jdbc.poolName - Configure the connection pool that will be
#                  tested in this cts test run.
#
# @jdbc.maxpoolsize - This property defines the max pool size
#                     when creating JDBC connection pools.
#
# @jdbc.steadypoolsize - This property defines the steady pool size
#                     when creating JDBC connection pools.
###############################################################
jdbc.poolName=${derby.poolName}
jdbc.maxpoolsize=64
jdbc.steadypoolsize=32

###############################################################
## These properties are use for the CTS
## interop tests which require a database that is used by the
## reference implementation.
#
# @jdbc.poolName.ri - Configure the connection pool that will be
#                     use when configuring the JDBC connection
#                     pools for the reference implemetation.
#
# @jdbc.maxpoolsize.ri - This property defines the max pool size
#                        when creating JDBC connection pools.
#
# @jdbc.steadypoolsize.ri - This property defines the steady pool size
#                     when creating JDBC connection pools.
###############################################################
jdbc.poolName.ri=${derby.poolName.ri}
jdbc.maxpoolsize.ri=96
jdbc.steadypoolsize.ri=32

###############################################################
# @jdbc.datasource.props - Used to to specify Vendor specific
#    properties and less commonly used DataSource properties.
#    Its value is a comma-separated array of name-value pairs.
#    Each property pair follows the format of "name=value",
#    including the surrounding double quotes.
#
#    The value of this property must not contain any extra spaces.
#
#    In most cases, this property is not needed and therefore
#    commented out.
#
###############################################################
#jdbc.datasource.props="driverType=thin","name2=value2"

###############################################################
# @jdbc.db - The name of the currently configured DB.  This
#            value is the prefix of the DB properties currently
#            being used.  Some valid values are; derby and
#            derbyEmbedded. See the other DB property
#            names for other valid values.
###############################################################
jdbc.db=derby
jdbc.db.classes=${derby.classes}

###############################################################
## These properties are configurable and must specify valid
## usernames and passwords to establish JDBC connections to
## backend RDBMS.
##
# @user1     - Set this to the user for the jdbc/DB1 resource
# @password1 - Set this to the password for the jdbc/DB1 resource 
# @user2     - Set this to the user for the jdbc/DB2 resource
# @password2 - Set this to the password for the jdbc/DB2 resource
# @user3     - Set this to the user for the jdbc/DBTimer resource
# @password3 - Set this to the password for the jdbc/DBTimer resource
###############################################################
user1=${derby.user}
password1=${derby.passwd}
user2=${derby.user}
password2=${derby.passwd}
user3=${derby.user}
password3=${derby.passwd}

###############################################################
## Configure the dml file to use
# @db.dml.file    - dml file for VI
# @db.dml.file.ri - dml file for RI
###############################################################
db.dml.file=${derby.dml.file}
db.dml.file.ri=${derby.dml.file}

###############################################################
## Configure the DB specific information needed by JSTL
# @jstl.db.driver   - JDBC driver 
# @jstl.db.url      - DB URL
###############################################################
jstl.db.driver=${derby.driver}
jstl.db.url=${derby.url}

########################################################################
#
# @jtaJarClasspath: This property must be set when running signature
#                    tests.  This property should be set to the Path
#                    for the JTA API jar.
#
########################################################################
jtaJarClasspath=${s1as.modules}/jakarta.transaction-api.jar

###############################################################
## Classpath properties required by CTS:
# @javaee.classes.ri    --  Classes required by Java EE RI
# @ts.run.classpath.ri  --  Classpath required by Java EE RI
#                           appclient container.
# @ts.run.classpath     --  Classpath required by the vendor
#                           appclient container.
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the CTS tests
# @ts.lib.classpath     --  Classes used to build cts.jar 
###############################################################
implementation.classes.ri=${ri.modules}/orb-connector.jar${pathsep}${ri.modules}/deployment-client.jar${pathsep}${ri.modules}/security-ee.jar${pathsep}${ri.modules}/security.jar${pathsep}${ri.modules}/common-util.jar${pathsep}${ri.modules}/glassfish-corba-omgapi.jar${pathsep}${ri.modules}/glassfish-corba-orb.jar${pathsep}${ri.modules}/internal-api.jar${pathsep}${ri.modules}/deployment-common.jar${pathsep}${ri.modules}/gmbal.jar${pathsep}${ri.modules}/bean-validator.jar${pathsep}${ri.modules}/jersey-client.jar${pathsep}${ri.modules}/jersey-common.jar${pathsep}${ri.modules}/jersey-hk2.jar${pathsep}${ri.modules}/jersey-media-jaxb.jar${pathsep}${ri.modules}/jersey-media-sse.jar${pathsep}${ri.modules}/jersey-media-json-processing.jar${pathsep}${ri.modules}/jsonp-jaxrs.jar${pathsep}${ri.modules}/jersey-media-json-binding.jar${pathsep}${ri.modules}/jersey-server.jar${pathsep}${ri.modules}/jersey-container-servlet.jar${pathsep}${ri.modules}/jersey-container-servlet-core.jar${pathsep}${ri.modules}/guava.jar${pathsep}${ri.modules}/jakarta.el.jar${pathsep}${ri.modules}/jakarta.el-api.jar${pathsep}${ri.modules}/tyrus-websocket-core.jar${pathsep}${ri.modules}/tyrus-client.jar${pathsep}${ri.modules}/tyrus-container-grizzly-client.jar${pathsep}${ri.modules}/tyrus-core.jar${pathsep}${ri.modules}/tyrus-container-grizzly.jar${pathsep}${ri.modules}/tyrus-container-grizzly-client.jar${pathsep}${ri.modules}/glassfish-grizzly-extra-all.jar${pathsep}${ri.modules}/nucleus-grizzly-all.jar${pathsep}${ri.modules}/tyrus-server.jar${pathsep}${ri.modules}/tyrus-container-servlet.jar${pathsep}${ri.modules}/tyrus-spi.jar${pathsep}${ri.modules}/yasson.jar${pathsep}${ri.modules}/jakarta.websocket-client-api.jar${pathsep}${ri.modules}/parsson.jar${pathsep}${ri.modules}/parsson-media.jar${pathsep}${ri.modules}/angus-activation.jar${pathsep}${ri.modules}/angus-mail.jar${pathsep}${ri.modules}/expressly.jar

implementation.classes=${s1as.modules}/deployment-client.jar${pathsep}${s1as.modules}/security.jar${pathsep}${s1as.modules}/common-util.jar${pathsep}${s1as.modules}/glassfish-corba-omgapi.jar${pathsep}${s1as.modules}/deployment-common.jar${pathsep}${s1as.modules}/gmbal.jar${pathsep}${s1as.modules}/bean-validator.jar${pathsep}${s1as.modules}/jakarta.ws.rs-api.jar${pathsep}${s1as.modules}/jersey-client.jar${pathsep}${s1as.modules}/jersey-common.jar${pathsep}${s1as.modules}/jersey-hk2.jar${pathsep}${s1as.modules}/jersey-media-jaxb.jar${pathsep}${s1as.modules}/jersey-media-sse.jar${pathsep}${s1as.modules}/jersey-media-json-processing.jar${pathsep}${s1as.modules}/jsonp-jaxrs.jar${pathsep}${s1as.modules}/jersey-media-json-binding.jar${pathsep}${s1as.modules}/jersey-server.jar${pathsep}${s1as.modules}/jersey-container-servlet.jar${pathsep}${s1as.modules}/jersey-container-servlet-core.jar${pathsep}${s1as.modules}/guava.jar${pathsep}${s1as.modules}/jakarta.el.jar${pathsep}${s1as.modules}/jakarta.el-api.jar${pathsep}${s1as.modules}/tyrus-websocket-core.jar${pathsep}${s1as.modules}/tyrus-client.jar${pathsep}${s1as.modules}/tyrus-core.jar${pathsep}${s1as.modules}/tyrus-container-grizzly.jar${pathsep}${s1as.modules}/tyrus-container-grizzly-client.jar${pathsep}${s1as.modules}/glassfish-grizzly-extra-all.jar${pathsep}${s1as.modules}/nucleus-grizzly-all.jar${pathsep}${s1as.modules}/tyrus-server.jar${pathsep}${s1as.modules}/tyrus-container-servlet.jar${pathsep}${s1as.modules}/tyrus-spi.jar${pathsep}${s1as.modules}/yasson.jar${pathsep}${s1as.modules}/jakarta.websocket-client-api.jar${pathsep}${s1as.modules}/parsson.jar${pathsep}${s1as.modules}/parsson-media.jar${pathsep}${s1as.modules}/angus-activation.jar${pathsep}${s1as.modules}/angus-mail.jar${pathsep}${s1as.modules}/expressly.jar

javaee.classes.ri=${ri.modules}/jakarta.jms-api.jar${pathsep}${ri.modules}/jakarta.json.jar${pathsep}${ri.modules}/jakarta.json-api.jar${pathsep}${ri.modules}/jakarta.json.bind-api.jar${pathsep}${ri.modules}/jakarta.ejb-api.jar${pathsep}${ri.modules}/jakarta.annotation-api.jar${pathsep}${ri.modules}/jakarta.enterprise.deploy-api.jar${pathsep}${ri.modules}/jakarta.mail.jar${pathsep}${ri.modules}/jakarta.mail-api.jar${pathsep}${ri.modules}/jakarta.persistence.jar${pathsep}${ri.modules}/jakarta.persistence-api.jar${pathsep}${ri.modules}/jakarta.resource-api.jar${pathsep}${ri.modules}/jakarta.security.auth.message-api.jar${pathsep}${ri.modules}/jakarta.authentication-api.jar${pathsep}${ri.modules}/jakarta.security.jacc-api.jar${pathsep}${ri.modules}/jakarta.authorization-api.jar${pathsep}${ri.modules}/jakarta.interceptor-api.jar${pathsep}${ri.modules}/jakarta.servlet-api.jar${pathsep}${ri.modules}/jakarta.servlet.jsp-api.jar${pathsep}${ri.modules}/jakarta.transaction-api.jar${pathsep}${ri.modules}/jakarta.xml.bind-api.jar${pathsep}${ri.modules}/jaxb-osgi.jar${pathsep}${ri.modules}/jmxremote_optional-repackaged.jar${pathsep}${ri.modules}/jakarta.faces.jar${pathsep}${ri.modules}/jakarta.faces-api.jar${pathsep}${ri.modules}/jakarta.servlet.jsp.jstl.jar${pathsep}${ri.modules}/jakarta.servlet.jsp.jstl-api.jar${pathsep}${ri.modules}/webservices-osgi.jar${pathsep}${ri.modules}/webservices-api-osgi.jar${pathsep}${ri.modules}/ejb.security.jar${pathsep}${ri.modules}/glassfish-corba-csiv2-idl.jar${pathsep}${ri.modules}/weld-osgi-bundle.jar${pathsep}${implementation.classes.ri}${pathsep}${ri.modules}/javamail-connector.jar${pathsep}${ri.modules}/javamail-runtime.jar${pathsep}${ri.modules}/jakarta.websocket-api.jar${pathsep}${ri.modules}/jakarta.enterprise.concurrent-api.jar${pathsep}${ri.modules}/jakarta.enterprise.cdi-api.jar${pathsep}${ri.modules}/jakarta.security.enterprise-api.jar${pathsep}${ri.modules}/resolver.jar${pathsep}${ri.modules}/jakarta.websocket-client-api.jar${pathsep}${ri.modules}/parsson.jar${pathsep}${s1as.modules}/parsson-media.jar${pathsep}${ri.modules}/angus-activation.jar${pathsep}${s1as.modules}/angus-mail.jar${pathsep}${s1as.modules}/expressly.jar${pathsep}${ri.modules}/webservices-extra-jdk-packages.jar${pathsep}${ri.modules}/webservices-connector.jar${pathsep}${ri.modules}/webservices-extra-xmlsec.jar${pathsep}${ri.modules}/webservices.security.jar

javaee.classes=${s1as.modules}/jakarta.jms-api.jar${pathsep}${s1as.modules}/jakarta.json.jar${pathsep}${s1as.modules}/jakarta.json-api.jar${pathsep}${s1as.modules}/jakarta.json.bind-api.jar${pathsep}${s1as.modules}/jakarta.ejb-api.jar${pathsep}${s1as.modules}/jakarta.annotation-api.jar${pathsep}${s1as.modules}/jakarta.enterprise.deploy-api.jar${pathsep}${s1as.modules}/jakarta.mail.jar${pathsep}${s1as.modules}/jakarta.mail-api.jar${pathsep}${s1as.modules}/jakarta.persistence.jar${pathsep}${s1as.modules}/jakarta.persistence-api.jar${pathsep}${s1as.modules}/jakarta.resource-api.jar${pathsep}${s1as.modules}/jakarta.security.auth.message-api.jar${pathsep}${s1as.modules}/jakarta.security.jacc-api.jar${pathsep}${s1as.modules}/jakarta.authentication-api.jar${pathsep}${s1as.modules}/jakarta.authorization-api.jar${pathsep}${s1as.modules}/jakarta.servlet-api.jar${pathsep}${s1as.modules}/el-impl.jar${pathsep}${s1as.modules}/jakarta.servlet.jsp-api.jar${pathsep}${jtaJarClasspath}${pathsep}${s1as.modules}/jakarta.xml.bind-api.jar${pathsep}${s1as.modules}/jaxb-osgi.jar${pathsep}${s1as.modules}/jmxremote_optional-repackaged.jar${pathsep}${s1as.modules}/jakarta.faces.jar${pathsep}${s1as.modules}/jakarta.faces-api.jar${pathsep}${s1as.modules}/jakarta.servlet.jsp.jstl.jar${pathsep}${ri.modules}/jakarta.servlet.jsp.jstl-api.jar${pathsep}${s1as.modules}/webservices-osgi.jar${pathsep}${s1as.modules}/webservices-api-osgi.jar${pathsep}${s1as.modules}/jakarta.management.j2ee-api.jar${pathsep}${s1as.modules}/ejb.security.jar${pathsep}${s1as.modules}/glassfish-corba-csiv2-idl.jar${pathsep}${s1as.modules}/weld-osgi-bundle.jar${pathsep}${implementation.classes}${pathsep}${s1as.modules}/javamail-connector.jar${pathsep}${s1as.modules}/javamail-runtime.jar${pathsep}${s1as.modules}/jakarta.websocket-api.jar${pathsep}${s1as.modules}/jakarta.enterprise.concurrent-api.jar${pathsep}${s1as.modules}/jakarta.enterprise.cdi-api.jar$${pathsep}${s1as.modules}/jakarta.xml.ws-api.jar${pathsep}${s1as.modules}/jakarta.xml.bind-api.jar${pathsep}${s1as.modules}/jakarta.security.enterprise-api.jar${pathsep}${s1as.modules}/jakarta.inject-api.jar${pathsep}${s1as.modules}/jakarta.activation-api.jar${pathsep}${s1as.modules}/jakarta.validation-api.jar${pathsep}${s1as.modules}/resolver.jar${pathsep}${s1as.modules}/jakarta.websocket-client-api.jar${pathsep}${s1as.modules}/parsson.jar${pathsep}${s1as.modules}/parsson-media.jar${pathsep}${s1as.modules}/angus-activation.jar${pathsep}${s1as.modules}/angus-mail.jar${pathsep}${s1as.modules}/expressly.jar${pathsep}${s1as.modules}/webservices-extra-jdk-packages.jar${pathsep}${s1as.modules}/webservices-connector.jar${pathsep}${s1as.modules}/webservices-extra-xmlsec.jar${pathsep}${s1as.modules}/webservices.security.jar

ts.run.classpath.ri=${javaee.classes.ri}${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/cts.jar${pathsep}${ts.home}/lib/glassfishporting.jar

ts.run.classpath=${pathsep}${javaee.classes}${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/cts.jar${pathsep}${ts.home}/lib/glassfishporting.jar${pathsep}${jdbc.db.classes}

ts.harness.classpath=${ts.home}/lib/jaxb-api.jar${pathsep}${ts.home}/lib/jaxb-core.jar${pathsep}${ts.home}/lib/jaxb-impl.jar${pathsep}${ts.home}/lib/jaxb-xjc.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/cts.jar${pathsep}${ts.home}/lib/glassfishporting.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.run.classpath}${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/jdom-1.1.3.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ri.modules}/admin-cli.jar

#classpath used for building CTS tests only (DO NOT MODIFY)
ts.classpath=${ts.home}/lib/commons-lang3-3.3.2.jar${pathsep}${javaee.home.ri}/javadb/lib/derby.jar${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/cts.jar${pathsep}${ts.home}/lib/glassfishporting.jar${pathsep}${ts.home}/lib/sigtest.jar${pathsep}${ts.run.classpath}${pathsep}${ts.run.classpath.ri}${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/jdom-1.1.3.jar${pathsep}${ant.home}/lib/ant.jar${pathsep}${ts.home}/lib/commons-httpclient-3.1.jar${pathsep}${ts.home}/lib/commons-logging-1.1.3.jar${pathsep}${ts.home}/lib/htmlunit-2.15.jar${pathsep}${ts.home}/lib/htmlunit-core-js-2.15.jar${pathsep}${ts.home}/lib/unboundid-ldapsdk.jar${pathsep}${ts.home}/lib/commons-codec-1.9.jar${pathsep}${ts.home}/lib/xml-apis-1.4.01.jar${pathsep}${ts.home}/lib/jaxb-api.jar
#classpath used for building cts.jar (DO NOT MODIFY)
ts.lib.classpath=${javaee.home.ri}/javadb/lib/derby.jar${pathsep}${ts.harness.classpath}

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

sigTestClasspath=${s1as.modules}/glassfish-corba-omgapi.jar${pathsep}${s1as.modules}/glassfish-corba-orb.jar${pathsep}${s1as.modules}/jakarta.enterprise.cdi-api.jar${pathsep}${s1as.modules}/jakarta.json-api.jar${pathsep}${s1as.modules}/jakarta.json.bind-api.jar${pathsep}${s1as.modules}/jakarta.batch-api.jar${pathsep}${s1as.modules}/jakarta.interceptor-api.jar${pathsep}${s1as.modules}/stax2-api.jar${pathsep}${s1as.modules}/jakarta.enterprise.concurrent-api.jar${pathsep}${s1as.modules}/jakarta.websocket-api.jar${pathsep}${s1as.modules}/jakarta.websocket-client-api.jar${pathsep}${s1as.modules}/jakarta.jms-api.jar${pathsep}${s1as.modules}/jakarta.faces.jar${pathsep}${s1as.modules}/jakarta.validation-api.jar${pathsep}${s1as.modules}/jakarta.annotation-api.jar${pathsep}${s1as.modules}/jakarta.xml.bind-api.jar${pathsep}${s1as.modules}/webservices-api-osgi.jar${pathsep}${pathsep}${s1as.modules}/jakarta.ws.rs-api.jar${pathsep}${s1as.modules}/weld-osgi-bundle.jar${pathsep}${s1as.modules}/jakarta.ejb-api.jar${pathsep}${s1as.modules}/jakarta.mail-api.jar${pathsep}${s1as.modules}/jakarta.persistence-api.jar${pathsep}${s1as.modules}/jakarta.resource-api.jar${pathsep}${s1as.modules}/jakarta.authorization-api.jar${pathsep}${s1as.modules}/jakarta.authentication-api.jar${pathsep}${s1as.modules}/jakarta.servlet-api.jar${pathsep}${s1as.modules}/jakarta.inject-api.jar${pathsep}${s1as.modules}/jakarta.el-api.jar${pathsep}${s1as.modules}/jakarta.servlet.jsp-api.jar${pathsep}${s1as.modules}/jakarta.servlet.jsp.jstl-api.jar${pathsep}${jtaJarClasspath}${pathsep}${s1as.modules}/jakarta.security.enterprise-api.jar${pathsep}${s1as.modules}/jakarta.activation-api.jar${pathsep}${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming

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
# @porting.ts.jms.class.1  VI of
#    com.sun.ts.lib.porting.TSJMSAdminInterface
# @porting.ts.HttpsURLConnection.class.1  VI of
#    com.sun.ts.lib.porting.TSHttpsURLConnectionInterface
# @porting.ts.login.class.2  RI of
#    com.sun.ts.lib.porting.TSLoginContextInterface
# @porting.ts.url.class.2  RI of
#    com.sun.ts.lib.porting.TSURLInterface
# @porting.ts.jms.class.2  RI of
#    com.sun.ts.lib.porting.TSJMSAdminInterface
# @porting.ts.HttpsURLConnection.class.2  RI of
#    com.sun.ts.lib.porting.TSHttpsURLConnectionInterface
###############################################################
porting.ts.login.class.1=com.sun.ts.lib.implementation.sun.javaee.GlassFishLoginContext
porting.ts.url.class.1=com.sun.ts.lib.porting.implementation.SunRIURL
porting.ts.jms.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIJMSAdmin
porting.ts.HttpsURLConnection.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection

##Porting class names for Sun RI Java EE Implementation #2 (must be Sun's RI)
porting.ts.login.class.2=com.sun.ts.lib.implementation.sun.javaee.GlassFishLoginContext
porting.ts.url.class.2=com.sun.ts.lib.porting.implementation.SunRIURL
porting.ts.jms.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIJMSAdmin
porting.ts.HttpsURLConnection.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection

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
namingServiceHost2=${orb.host.ri}
namingServicePort2=${orb.port.ri}

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
# @webServerHost.2  hostname for the Java EE RI Web Server
# @webServerPort.2  port number of the Java EE RI Web Server
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
webServerHost=${orb.host}
webServerPort=8080
webServerHost.2=${orb.host.ri}
webServerPort.2=8002
ServletClientThreads=2


###############################################################
## These properties are used for the Connector specific tests.
##
## You must be able to deploy the following resource adapters
## on your Java EE server.
###############################################################

## JNDI name bindings.  You should not change these.  The TSDeploymentInterface
## will pass these values as a properties object.  See the SunRIDeployment.java
## file for usage patterns.
whitebox-tx=java:comp/env/eis/whitebox-tx
whitebox-notx=java:comp/env/eis/whitebox-notx
whitebox-xa=java:comp/env/eis/whitebox-xa
whitebox-tx-param=java:comp/env/eis/whitebox-tx-param
whitebox-notx-param=java:comp/env/eis/whitebox-notx-param
whitebox-xa-param=java:comp/env/eis/whitebox-xa-param
whitebox-anno_no_md=java:comp/env/eis/whitebox-anno_no_md
whitebox-ibanno_no_md=java:comp/env/eis/whitebox-ibanno_no_md
whitebox-mixedmode=java:comp/env/eis/whitebox-mixedmode
whitebox-multianno=java:comp/env/eis/whitebox-multianno
whitebox-mdcomplete=java:comp/env/eis/whitebox-mdcomplete
whitebox-permissiondd=java:comp/env/eis/whitebox-permissiondd

## Embedded resource adapter property 
whitebox-embed=java:comp/env/eis/ejb_Deployment_whitebox-tx.rar

whitebox-embed-xa="__SYSTEM/resource/ejb_Tsr#whitebox-xa#com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory"
tsrJndiName=java:comp/TransactionSynchronizationRegistry
tsrHomeJndiName="java:global/ejb_Tsr/ejb_Tsr_ejb/Tsr"

## JNDI name bindings for JDBC Connector.
JDBCwhitebox-tx=java:comp/env/eis/JDBCwhitebox-tx
JDBCwhitebox-notx=java:comp/env/eis/JDBCwhitebox-notx
JDBCwhitebox-xa=java:comp/env/eis/JDBCwhitebox-xa
JDBCwhitebox-tx-param=java:comp/env/eis/JDBCwhitebox-tx-param
JDBCwhitebox-notx-param=java:comp/env/eis/JDBCwhitebox-notx-param
JDBCwhitebox-xa-param=java:comp/env/eis/JDBCwhitebox-xa-param

#########################################################################
## Resource adapter configuration values. You must modify these values
## to plug your JDBC driver into the TS JDBCwhitebox resource adapter.
##
## When you run the XA tests (src/com/sun/ts/tests/xa), you must
## configure this resource adapter to use a database.
##
# @xa.properties: These are the properties required by the XA driver.  The
#                 managed connection factory will set these properties via
#                 reflection on the class specified by the 
#                 xa.xadatasource.class property.
#
#    Note: The xa.properties value is the set of properties that will be set on
#          your XA JDBC driver (the class denoted by the xa.xadatasource.class property).
#          See section 9.4.1 of the JDBC 3.0 specification for more details.
#
#          When specifying the xa.properties property please escape the appropriate
#          characters.  The xa.properties value needs to be treated as a single property
#          even though it is made up of many properties.  The properties need to be
#          separated by :'s and the names and values to be separated by ='s.
#          We also need any values that contain colons or equals that do not
#          need to be interrpretted to be inside single quotes.  Anything in
#          single quotes will be treated as a string literal.
#
#          For example: The following xa.properties property:
#
#		 xa.properties=user=admin:password=some-password:DatabaseName='jdbc:pointbase:server://localhost:9092/cts,new'
#
#          Would result in the following setters being called on the supplied XA JDBC driver denoted
#          by the xa.xadatasource.class property.
# 
#             XADataSource.setUser("admin");
#             XADataSource.setPassword("some-password");
#             XADataSource.setDatabaseName("jdbc:pointbase:server://localhost:9092/cts,new");
#
#          Please map the property xa.properties to the property name XAProps and map the
#          xa.xadatasource.class property to the property name XADataSourceName.  This will
#          ensure the appropriate setters will be called to make these properties
#          available to the managed connection factory.  See section 17.4.2 of the Connector 
#          specification version 1.5.
#
# @xa.xadatasource.class: The implementation of the XADataSource interface.
#                         This class is your XA JDBC driver.
#                         Please note: this class and any dependent classes
#                         must be accessible by the CTS libraries in the app
#                         server's extension directory.
#
# @connector_connectionURL: The URL for your JDBC driver.  Should be the
#			same as the one used for the rest of the TS.
#			
#########################################################################

### Cloudscape/Derby properties for XA
xa.properties="user\\\=${derbyEmbedded.user}\\\:password\\\=${derbyEmbedded.passwd}\\\:DatabaseName\\\=${derbyEmbedded.dbName}"
xa.xadatasource.class=org.apache.derby.jdbc.EmbeddedXADataSource
connector_connectionURL="jdbc\\:derby\\:${derbyEmbedded.dbName}\\;create\\=true"

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
# @user_vi        Username for the vendor implementation used in
#                 interop tests
# @password_vi    Associated password for the VI user
# @user_ri        Username for the reference implementation used in
#                 interop tests
# @password_ri    Associated password for the RI user
# @nobodyuser     This value must be the same value returned by a call to 
#                 getCallerPrincipal().getName() from the ejb-tier when
#                 an unauthenticated caller in the web tier invokes an
#                 ejb method.
#########################################################################
user=j2ee
password=j2ee
authuser=javajoe
authpassword=javajoe
user_vi=j2ee_vi
password_vi=j2ee_vi
user_ri=j2ee_ri
password_ri=j2ee_ri
nobodyuser=guest

###############################################################
# @securedWebServicePort must be set to run secbasicssl and
#   csiv2 tests. Set this property with your application
#   server's secured webservice port.
# @securedWebServicePort.2 points to the secured webservice
#   port in Sun's Reference Implementation(RI).
###############################################################
securedWebServicePort=1044
securedWebServicePort.2=1045

###############################################################################
# @client.cert.test.jdk.tls.client.protocols JDK 11 in TLSv1.3 does not support
#                        Post-Handshake Authentication, so TLSv1.2 must be used
#                        for client-cert authentication to work.
###############################################################################
client.cert.test.jdk.tls.client.protocols=TLSv1.2

###############################################################
# @login This property must be set to run appclient security
#        tests
###############################################################
login=default

################################################################################
##These properties are needed for jms tests.  
#
# @jms_timeout - this is the amount of time in milliseconds that synchronous
#                receives will wait for a message
###############################################################################
jms_timeout=10000

#########################################################################

###############################################################################
## These properties are needed for the EJB timer tests.
#
# @ejb_timeout - this is the amount of time in milliseconds that duration will
#                be set for an ejbtimeout callback method
#
# @ejb_wait - this is the amount of time in milliseconds that the client will
#             wait for ejbtimeout callback method results
##############################################################################
ejb_timeout=30000
ejb_wait=60000

###############################################################################
## This property is needed for ejb30 stateful timeout tests.
# @test.ejb.stateful.timeout.wait.seconds - the minimum amount of time in seconds 
#       the test client waits before verifying the status of the target stateful
#       bean.  Its value must be an integer number.  Its default value in ts.jte
#       file is 480 seconds.  It may be set to a smaller number (e.g., 240 seconds)
#       to speed up testing, depending on the stateful timeout implementation
#       strategy in the target server.
#     
#       For these stateful timeout tests, the property javatest.timeout.factor
#       must be set to a value such that the JavaTest harness does not timeout
#       before the test completes.  Usually setting javatest.timeout.factor to
#       2.0 or greater should suffice.
###############################################################################
test.ejb.stateful.timeout.wait.seconds=480

###################################################################
# @log.file.location  This property is used by JACC tests to create
#   and analyze provider logs. Specify the log directory in which
#   your appserver generates logs.
###################################################################
log.file.location=${vi.log.file.location}


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


###############################################################
## These properties are used by connector tests and are non configurable 
## and must specify valid username,password to establish TSeis connection. 
###############################################################
rauser1=cts1
rapassword1=cts1
rauser2=cts2
rapassword2=cts2

###############################################################
## These properties are for JSTL 
###############################################################
jstl.db.user=${user1}
jstl.db.password=${password1}


###############################################################
## These properties are used for the JDBC specific tests,
## they do not need to be changed.
###############################################################
DriverManager=no
Driver=DriverOnlyUsedIfDriverManagerIsYes
db1=db1OnlyUsedIfDriverManagerIsYes
db2=db1OnlyUsedIfDriverManagerIsYes
ptable=ctstable1
ftable=ctstable2
cofSize=5
cofTypeSize=5
binarySize=24
varbinarySize=48
longvarbinarySize=50

ts.home=dummy
ts_home=${ts.home}

#######################################################################
## platform.mode is used by jpa tests to know whether to use
## the Java EE Programming Model for service and port access.
## platform.mode=jakartaEE		Java EE Programming Model
######################################################################
platform.mode=jakartaEE



###############################################################################
## DO NOT MODIFY ANY PROPERTIES BELOW THIS LINE.
###############################################################################

#classpath used by standard deployment server to deploy to the RI in a separate 
#VM (DO NOT MODIFY)
ts.standard.deployment.server.classpath=${ant.jars}:${pathsep}${ts.run.classpath.ri}${pathsep}${ts.home}/lib/javatest.jar${pathsep}${ts.home}/lib/jdom-1.1.3.jar${pathsep}${ri.modules}/admin-cli.jar

harness.deployment.server.port=2002

java.naming.factory.initial=foo
org.omg.CORBA.ORBClass=foo
undeploy_redeploy_apps=${UNDEPLOY_REDEPLOY_FLAG}

###############################################################################
# The following properties are aliases for the old J2EE properties.  These
# properties are now named javaee instead of j2ee and will eventually be phased
# out of the CTS.  These props are necessary until the props are renamed 
# through out all CTS.
###############################################################################
j2ee.home=${javaee.home}
j2ee.home.ri=${javaee.home.ri}

####################################################################
##    This propertry is passed to any test
##    that needs to programatically login from with the appclient
##    container and propogate that login context to test code running
##    within the EJB container.  The value is a no-op.  The fact that
##    the test includes the property is sufficient to cause the 
##    EJBVehicleRunner to programatically login.  To have a test
##    include this property simply add it to the javatest props list
##    of the test.
####################################################################
secured.ejb.vehicle.client=true

####################################################################
#     This property is used to point to the location of the 
#     implementation of VariableMapper. The value for sjsas 9.x is
#
#     variable.mapper=org.glassfish.expressly.lang.VariableMapperImpl
#
####################################################################
variable.mapper=org.glassfish.expressly.lang.VariableMapperImpl

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

################################################################
# @http.server.supports.endpoint.publish 
#     Does HTTP server-side support endpoint publish API's.
# @http.server.supports.endpoint.publish.2
#    Does the RI HTTP server-side support endpoint publish API's.
#
# http.server.supports.endpoint.publish=(true|false)
# DO NOT TOUCH - value MUST be false for managed environments like Java EE
################################################################
http.server.supports.endpoint.publish=false
http.server.supports.endpoint.publish.2=false

###########################################################################
# This property is used by some test clients that need a delay between
# method invocations to server endpoints. The value specified is the amount
# of time the client will delay in seconds.
###########################################################################
client.delay=1

#######################################################################
## This persistence unit name is used by jpa/ee/pluggability tests. Its value is the
## same as the name attribute in persistence.xml
######################################################################
persistence.unit.name=CTS-EM

###############################################################
# @db.supports.sequence
#
#     - This property is used to determine if a database supports
#       the use of SEQUENCE. If it does not, this property
#       should be set to false so the test is not run. The
#       default value is true.
#       This value should be set to false for the following databases:
#       sybase, mysql, and mssqlserver.
###############################################################
db.supports.sequence=true

#######################################################################
## @persistence.second.level.caching.supported
##     - This property is used to determine if the persistence provider supports
##        second level caching.
######################################################################
persistence.second.level.caching.supported=true

##########################################################################
## This section contains all properties that are specific to JAX-RS Tests.
## All default values are specific to GlassFish and Jersey.
##
## servlet_adaptor       - implementation specific servlet for JAX-RS
## jaxrs_impl_class.dir  - Directory that servlet_adaptor locates
## jaxrs_impl_name       - used to tag intermediate file names
##########################################################################
servlet_adaptor=org/glassfish/jersey/servlet/ServletContainer.class
jaxrs_impl_lib=${javaee.home}/modules/jakarta.ws.rs-api.jar
jaxrs_impl_name=jersey


##########################################################################
# This section contains all properties that are specific to JSR-196 Tests.
# All default values are specific to GlassFish.
#
# @schema.file.location
#              This points to the directory that the provider-configuration.xsd 
#              file will live.  The provider-configuration.xsd is used to
#              describe the provider.configuration.file (above). 
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
#
# @logical.hostname.soap
#              This property is used to identify the name of the logical host
#              that processes soap requests. This hostname is used in server
#              side Application context Identifier in soap profile.
#
# @appclient.log.output
#              The client logging level for appclient container depends on the
#              log level specified in sun-acc.xml
#
#              This log level directly affects the output of TSLogger which
#              logs the JSR196 SPI calls made in appclient container.
#
#              using this property we enable the appclient container's
#              logging level to INFO
#
##########################################################################
schema.file.location=${javaee.home}/lib/schemas
logical.hostname.servlet=server
logical.hostname.soap=localhost
appclient.log.output=true

##########################################################################
# @servlet_waittime:   Time in seconds to wait after HttpSession expires 
#                      to access the session
# @servlet_async_wait: Time in seconds to wait between sending asynchronous messages 
##########################################################################
servlet_waittime=10
servlet_async_wait=4

##########################################################################################
# The following properties are ONLY used if testing a Standalone JMS Resource Adapter. 
# Since support for a JMS Resource Adapter is OPTIONAL by the JMS 2.0 Specification
# this funtionality is USE AT YOUR OWN RISK. We WILL NOT support it. Its here in the
# case that a vendor wants to test a Standalone JMS Resource Adapter which is OPTIONAL.
#
# test.sa.jmsra		// Set to true ONLY if testing a standalone JMS Resource Adapter
# jmsra.rarfile		// Location of the standalone JMS Resource Adapter RAR file 
# jmsra.jarfile		// Location of the standalone JMS Resource Adapter JAR file 
#
# A standalone JMS Resource Adapter requires deployment/configuration to the Vendor 
# Implementation under test (Example: Java EE 8 RI). If not testing a standalone JMS
# Resource Adapter then these properties MUST NOT be used and the value of the property
# (test.sa.jmsra) MUST BE SET to false (which is the DEFAULT setting). If you set the
# property (test.sa.jmsra=true) then you are testing a standalone JMS Resource Adapter
# and the (jmsra.rarfile and jmsra.jarfile) properties must point to the location of
# the standalone JMS Resource Adapter RAR and JAR files. During CTS configuration the 
# (config.vi) ant task will call another script that will deploy the standalone JMS 
# Resource Adapter, configure the standalone JMS Resource Adapter, create the JMS
# connector connection pools, create the JMS connector resources, and finally create 
# the JMS administration objects. The ant scripts that handles all of these steps for
# the (Java EE 8 RI) are the following:
#
# $TS_HOME/bin/xml/impl/glassfish/jmsra.xml
# $TS_HOME/bin/xml/impl/glassfish/templates/create.jmsra.template
#
# These scripts work for the Standalone Generic JMS Resource Adapter (GenericJMSRA)
# located at: (http://genericjmsra.java.net/). If testing another standalone JMS 
# Resource Adapter then these scripts will need to be rewritten/modified for that 
# JMS Resource Adapter.
#
# Here is the summary of steps of what the (jmsra.xml) script does. It configures the JMS 
# resource adapter by doing a resource-adapter-config, it deploys the JMS resource adapter 
# RAR file specified in the "jmsra.rarfile" property to the (Java EE 8 RI), it copies the 
# JMS Resource Adapter JAR file specified in the "jmsra.jarfile" property to the (Java EE
# 8 RI) lib directory under $JAVAEE_HOME/lib. It copies some of the JMS client library jars
# to the $JAVAEE_HOME/lib directory. it then creates the JMS connector connection pools, 
# the JMS connector resources, and the JMS administration objects that are needed by CTS. 
# When the (config.vi) ant task completes the Java EE 8 environment will be ready to run 
# the JMS CTS tests. One final step is needed to the JMS MDB CTS tests which is to modify
# the sun-ejb-jar runtime xml files by adding the <mdb-resource-adapter> deployment info.
# 
# Here are the manual steps for the Standalone Generic JMS Resource Adapter (GenericJMSRA)
# after the (config.vi) ant task completes.
#
# (1) Edit the ts.jte file and add the following:
#	  Add "-Dgenericra.inAppClientContainer=true" to the "command.testExecuteAppClient"
#	  property and then add "${pathsep}${jmsra.jarfile}" to the end of APPCPATH variable
#         in the same "command.testExecuteAppClient" property. You can refer to the Generic
#	  JMS Resource Adapter (GenericJMSRA) user guide for more information on this.
# (2) Modify the sun-ejb-jar runtime xml files for the JMS MDB tests by adding the 
#     <mdb-resource-adapter> deployment info.
#	  cd $TS_HOME/bin
#	  ant -f xml/impl/glassfish/jmsra.xml modify-jmsmdbejbxml
# (3) Now you can run the JMS CTS tests. After you are done running the JMS CTS tests ypu
#     can unconfigure the Java EE 8 RI and restore the original sun-ejb-jar runtime xml 
#     files.
#	  cd $TS_HOME/bin
#	  ant -f xml/impl/glassfish/jmsra.xml restore-jmsmdbejbxml
#	  ant clean.vi
# (4) Finally reset the properties back to there defaults. Set (test.sa.jmsra=false) and unset
#     (jmsra.rarfile and jmsra.jarfile). Remove the "-Dgenericra.inAppClientContainer=true"
#     and "${pathsep}${jmsra.jarfile}" from the ts.jte file from step 1.
#
# If using a different Java EE environment from the Java EE RI than a vendor will need to 
# provide his own JMS Resource Adapter support scripts needed for that Java EE environment. 
# It can reuse/modify the (Java EE 8 RI) scripts above or simply write their own scripts. A
# vendor can use the Java EE 8 RI or any Java EE 8 implementation of its choice as the Java 
# EE 8 environment for testing its Standalone JMS Resource Adapter. The Standalone JMS Resource 
# Adapter MUST pass all of the JMS CTS tests under: (TS_HOME/src/com/sun/ts/tests/jms) in order
# to be declared fully JMS 2.0 compliant. 
#
# See the JMS 2.0 Specification and the CTS 8 User's Guide for more information.
##########################################################################################
test.sa.jmsra=false
jmsra.rarfile=
jmsra.jarfile=

##########################################################################################
# The following property specifies the name of the JMS Resource Adapter under test. The
# Java EE 8 RI contains its own builtin JMS Resource Adapter (JMSRA). Its name is "jmsra".
# So we set (jmsra.name=jmsra) as the default value for the Java EE 8 RI. 
#
# Change the value of this property for the JMS Resource Adapter that is under test. This
# hardcoded value is specific to the Java EE 8 RI.
#
# jmsra.name		// Name of JMS Resource Adapter for the implementation under test
##########################################################################################
jmsra.name=jmsra


##########################################################################
# The following property is for WebSocket tests. 
#
# @ws_wait:  waiting time in seconds for socket to send/receive a message
##########################################################################
ws_wait=5

############################################################################################
# These following properties are needed for Javamail tests.
# 
#  @mailHost  hostname of the mail server 
#  @mailuser1 must be set to a valid email address where test mails will be sent to. 
#  @mailFrom  reply to address set in the email messages generated by the javamail tests
#  @javamail.username user account name on server
#  @javamail.password password for the mailuser1
#
############################################################################################

mailHost=jsepc04.us.oracle.com
mailuser1=cts1@jsepc04.us.oracle.com
mailFrom=cts1@jsepc04.us.oracle.com
javamail.password=cts1
smtp.port=25
imap.port=143

### User need not change the following javamail properties

javamail.username=${mailuser1}
transport_protocol=smtp

# mail server used to run tests against.
javamail.server=${mailHost}

# protocol used to run the Store tests
javamail.protocol=imap

# mail server used to run tests against.
javamail.transport.server=$javamail.server

# rootpath of test mailboxes
javamail.root.path=""

# note the mailbox 'test1'
javamail.mailbox=test1

# search pattern used by list tests in Folder
javamail.listpattern=test

# your own login account name
smtp.username=$javamail.username

# smtp domain name ; i.e. oracle.com
smtp.domain=$javamail.server

# smtp From address
smtp.from=$smtp.username

# smtp To address
smtp.to=$smtp.username

# io file
iofile=MailIOFile.txt

#####################################################################################################
#### Properties used by ldap installation
# @ldap.server                        -- The type of ldap server.  unboundid is supported by now
# @ldap.install.server                -- Install ldap server by CTS script or not
# @ldap.ldif.file                     -- The ldif file containing data seeded in ldap server
#
# @unboundid.installation.package     -- The package for installing Unboundid ldap server
########################################################################################################
ldap.server=unboundid
ldap.install.server=true
ldap.ldif.file=${ts.home}/bin/ldap.ldif

unboundid.installation.package=${ts.home}/lib/unboundid-ldapsdk.jar

#####################################################################################################
# Properties used for JPA 2.2 Tests
########################################################################################################
persistencs.properties.file=${bin.dir}/jpa-provider.properties
persistence.unit.name.2=JPATCK2

jpa.provider.implementation.specific.properties=eclipselink.logging.level\=OFF
jakarta.persistence.provider=org.eclipse.persistence.jpa.PersistenceProvider
jakarta.persistence.jdbc.driver=${derby.driver}
jakarta.persistence.jdbc.url=${derby.url}
jakarta.persistence.jdbc.user=${derby.user}
jakarta.persistence.jdbc.password=${derby.passwd}

