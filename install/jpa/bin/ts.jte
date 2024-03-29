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

######################################################################
## When installing TCK/RI on Windows, users must install the TCK and
## the RI on the same drive.
######################################################################

###############################################################
## Settings for Vendor Java Persistence API Implementation
###############################################################
jpa.classes=

####################################################################
# @jdbc.driver.classes - This property points to where the JDBC
# drivers live.
####################################################################
jdbc.driver.classes=

###############################################################
##
## DataBase configuration information
##
###############################################################

###############################################################
# @jdbc.db - The name of the currently configured DB.
# 	Defines which database will be used for certification.
#	This property will be used to determine the corresponding sql
#   to initialize and can be one of the following:
#
# 	- derby
#	- mysql
#	- sybase
#	- db2
#	- mssqlserver
#	- oracle
#	- postgresql
#
# The sql for the tables and stored procedures are contained in:
#
#  $TS_HOME/[jdbc.db]/sql/[jdbc.db].ddl.persistence.sql
#  $TS_HOME/[jdbc.db]/sql/[jdbc.db].ddl.persistence.sprocs.sql
#
# If using a database other than above, you need to create
# your own DDL files but can use these files for reference of what
# tables are required.
#

###############################################################
jdbc.db=

###############################################################
##
## JPA generic database properties. These properties must be set
## regardless of which database is being used for testing. The
## initdb.xml file uses them to create the jpa-provider.properties file.
##
###############################################################
jakarta.persistence.provider=org.eclipse.persistence.jpa.PersistenceProvider
jakarta.persistence.jdbc.driver=
jakarta.persistence.jdbc.url=
jakarta.persistence.jdbc.user=
jakarta.persistence.jdbc.password=

###############################################################
# @jpa.provider.implementation.specific.properties
#
#     - This property is used to allow vendor specific properties to be added
#       to the jpa-provider.properties file which is created during the
#       init.database process.
#
#       The format is:
#          name\=value:name\=value:etc
###############################################################
jpa.provider.implementation.specific.properties=eclipselink.logging.level\=OFF

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

###################################################################
###################################################################
###################################################################
##  PROPERTIES USERS MAY HAVE TO SET
##
###################################################################
###################################################################
###################################################################

#########################################################################
# @jimage.dir:       This property specifies the directory where Java 11+
#                    modules will be expanded by the jimage tool for use
#                    in sigTestClasspath
# @sigTestClasspath: This property must be set when running signature
#                    tests.  This property should be set to a list of
#                    jar files and/or directories which contain your
#                    Java Persistence and Java SE classes.
#########################################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${jpa.classes}${pathsep}\
                 ${jimage.dir}/java.base${pathsep}\
                 ${jimage.dir}/java.rmi${pathsep}\
                 ${jimage.dir}/java.sql${pathsep}\
                 ${jimage.dir}/java.naming

###############################################################
## Users must set this property when running on Windows.  The
## appropriate value on Windows is a semi-colon (;).  If you are
## not running on Windows leave this property set to its default
## value of colon (:) for other .
###############################################################
pathsep=:

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
work.dir=${ts.home}/tmp/JTwork
report.dir=${ts.home}/tmp/JTreport
if.existing.work.report.dirs=auto

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
harness.log.traceflag=false
harness.executeMode=0
harness.log.delayseconds=1
harness.socket.retry.count=10

########################################################################
## Common environment for both ts_unix and ts_win32
#
# @command.testExecute - This command is used to execute any test
#                        clients which are not run inside an
#                        application client container.
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
        -Dcts.tmp=$harness.temp.directory \
        -Dlog.file.location=$log.file.location \
        -Djava.security.policy=${bin.dir}/harness.policy \
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

###############################################################
## These properties must be set to tell the Test harness the
## class names of your porting class implementations.
#
# @porting.ts.url.class.1  VI of
#    com.sun.ts.lib.porting.TSURLInterface
###############################################################
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL

###############################################################
# @ts.display -- location to display TCK output on Unix
###############################################################
ts.display=:0.0

###############################################################
# @tz - your local timezone. For valid values, consult your
#       Operating System documentation.
###############################################################
tz=US/Eastern

###################################################################
###################################################################
###################################################################
##  PROPERTIES USERS WILL NOT HAVE TO SET
##
##  DO NOT CHANGE ANY PROPERTIES BELOW THIS POINT
###################################################################
###################################################################
###################################################################

###############################################################
##build level
##1: compile only
##2: compile and build component archives (e.g., jar's, war's)
##3: compile and build component and application archives
##default is set to 3
build.level=2

###############################################################
## JPA Deliverable Class
## DO NOT CHANGE THIS PROPERTY
###############################################################
deliverable.class=com.sun.ts.lib.deliverable.jpa.JPADeliverable

#######################################################################
## platform.mode is used to tell the enviroment we are in standalone
## mode
## DO NOT CHANGE THIS PROPERTY
######################################################################
platform.mode=standalone

###############################################################
## The following property defines a file which contains name/value pairs of
# properties intended to be passed to the Persistence Provider during the
# creation of the EntityManagerFactory. These properties correspond to the
# persistence.xml properties and may contain provider-specific properties
# including database connection properties.
# This file is automatically generated when the database is initialized
# therefore edits to this file could be lost.
## DO NOT CHANGE THIS PROPERTY
###############################################################
persistence.properties.file=${bin.dir}/jpa-provider.properties

#######################################################################
## the name for the persistence unit used in this TCK. Its value is the
## same as the name attribute in persistence.xml
## persistence.unit.name.2 is used to create second persistence unit
## DO NOT CHANGE THIS PROPERTY
######################################################################
persistence.unit.name=JPATCK
persistence.unit.name.2=JPATCK2

#######################################################################
## Location of ts_home
## DO NOT CHANGE THIS PROPERTY
######################################################################
ts_home=${ts.home}

###############################################################
## The directory separator for the platform.  User should not change
## this property.
## DO NOT CHANGE THIS PROPERTY
###############################################################
dirsep=/

###############################################################
## Schema locations for Persistence xml files.
## Used for xml validation when building tests.
## DO NOT CHANGE THIS PROPERTY
###############################################################
alt.schema.dir=${ts.home}/lib/schemas

#######################################################################
## the location where the pluggability log will be written to an read from
## DO NOT CHANGE THIS PROPERTY
######################################################################
log.file.location=${ts.home}/tmp

###############################################################
## Classpath properties required by the JPA TCK:
## @ts.run.classpath 	--  Classes needed for test run
## @ts.harness.classpath --  Classes required by javatest
##DO NOT CHANGE THIS PROPERTY
###############################################################
ts.run.classpath=${ts.home}/classes${pathsep}\
                 ${jdbc.driver.classes}${pathsep}\
                 ${jpa.classes}${pathsep}\
                 ${ts.home}/lib/jpatck.jar${pathsep}\
                 ${ts.home}/dist/com/sun/ts/tests/jpa/common/pluggability/altprovider/jpa_alternate_provider.jar
ts.harness.classpath=${ts.home}/lib/tsharness.jar${pathsep}\
                     ${ts.home}/lib/sigtest.jar${pathsep}\
                     ${ts.run.classpath}${pathsep}\
                     ${ts.home}/lib/javatest.jar${pathsep}
                     

###############################################################
## @ts.classpath   --  Used to build the Persistence tests
## DO NOT CHANGE THIS PROPERTY
###############################################################
ts.classpath=${ts.harness.classpath}${pathsep}\
             ${ts.home}/lib/jpatck.jar${pathsep}\
             ${jpa.classes}

######################################################################
## Deliverables must set this property to the name of the deliverable
## specific library jar file (if create.manifest.extension.entries
## is set to true)
## DO NOT CHANGE THIS PROPERTY
######################################################################
tslib.name=jpatck
