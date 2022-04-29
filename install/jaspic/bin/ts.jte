#
# Copyright (c) 2006, 2022 Oracle and/or its affiliates. All rights reserved.
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
##   JavaTest Environment file for Jakarta EE Platform TCK
##
##   Environment specific properties in this file will likely
##   have to be modified prior to running the Jakarta EE Platform TCK. 
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

########################################################################
## Settings for Vendor JASPIC Implementation
# @jaspic.home The location of the vendor's Jakarta EE platform
#              implementation.
# @orb.host    Hostname of the machine running the vendor's
#              implementation.
# @orb.port    The port number the vendor implementation is listening
#              to for service requests.
########################################################################
jaspic.home=/sun/glassfish4/glassfish
orb.host=localhost
orb.port=3699

########################################################################
## Settings for Eclipse GlassFish Jakarta EE Implementation
# @jaspic.home.ri The location of the RI.
# @orb.host       Hostname of the machine running the RI.
# @orb.port       The port number the RI is listening to for service
#                 requests.
########################################################################
jaspic.home.ri=${jaspic.home}
orb.host.ri=${orb.host}
orb.port.ri=${orb.port}

###################################################################
###################################################################
###################################################################
##  RI SPECIFIC PROPERTIES LIVE BELOW
###################################################################
###################################################################
###################################################################

###################################################################
## When installing CTS/RI on Windows, users must install CTS and
## the RI on the same drive.  Also note that you should never
## specify drive letters in any path defined in this properties
## file.
##
# @pathsep Users must set this property when running on Windows.
#          The appropriate value on windows is a semi-colon (;).
#          If you are not running on Windows leave this property
#          set to its default value of colon (:).
###################################################################
pathsep=:


###############################################################
# @ts.display -- location to display CTS output on Unix
###############################################################
ts.display=:0.0


###############################################################
## Config params needed for Sun Java System Application Server
## (SJSAS) asadmin. You must change these values as needed
## only if you are testing against SJSAS app server
## (jaspic.home is pointing to SJSAS).
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
# @s1as.lib            -- Library directory for other Jakarta EE RI
#                         jars
# @s1as.imq.share.lib  -- Shared library directory for imq 
# @s1as.jvm.options    -- Java options needed by SJSAS
# @s1as.java.endorsed.dirs -- endorsed directory used by SJSAS
# @s1as.applicationRoot-- Location of application repository
#                         Only needed when running on windows
###############################################################
s1as.admin.user=admin
s1as.admin.passwd=
s1as.admin.host=${orb.host}
s1as.admin.port=4848
s1as.admin=${jaspic.home}/bin/asadmin
s1as.server=server
s1as.domain.dir=${jaspic.home}/domains
s1as.domain.name=domain1
s1as.domain=${s1as.domain.dir}/${s1as.domain.name}
s1as.asenv.loc=${jaspic.home}/config
s1as.imqbin.loc=${jaspic.home}/imq/bin
s1as.lib=${jaspic.home}/lib
s1as.imq.share.lib=${jaspic.home}/imq/lib
s1as.jvm.options=-Dj2eelogin.name=${user}:-Dj2eelogin.password=${password}
s1as.java.endorsed.dirs=${endorsed.dirs}
s1as.applicationRoot=c:

sjsas.master.password=changeit
sjsas.instance.config.dir=config


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
# @endorsed.dirs.ri  If using JavaSE 6 or above and you provide newer versions
#                 of technologies than those contained in Java SE 6, verify
#                 that the property endorsed.dirs is set to the location of
#                 the RI api jars for those technologies you wish to
#                 overrride.  For example, JavaSE 6 contains an
#                 implementation of JAXWS 2.0 which will conflict with
#                 JAXWS 2.1, therefore this property would need to be set
#                 so that JAXWS 2.1 would be used during the building of
#                 tests and during test execution.
###########################################################################
endorsed.dirs=${jaspic.home}/modules/endorsed
endorsed.dirs.ri=${endorsed.dirs}

s1as.targets=${s1as.server}


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
# @tz - your local timezone. For valid values, consult your
#       Operating System documentation.
###############################################################
tz=US/Eastern


###############################################################
# @alt.dtd.dir DTD location for Jakarta EE and RI xml files.  Used
#              for xml validation when building tests. If 
#              jaspic.home.ri is set, <jaspic.home.ri>/lib/dtds
#              will be used and alt.dtd.dir is ignored.
# @alt.schema.dir schema location for Jakarta EE and RI xml files.
#                 Used for xml validation when building tests. 
#                 If jaspic.home.ri is set,
#                 <jaspic.home.ri>/lib/schemas will be used and
#                 alt.schema.dir is ignored.
###############################################################
alt.dtd.dir=${ts.home}/lib/dtds
alt.schema.dir=${ts.home}/lib/schemas


###############################################################
## Classpath properties required by CTS:
# @ts.run.classpath     --  Classpath required by the vendor
#                           appclient container.
# @ts.harness.classpath --  Classes required by javatest
# @ts.classpath         --  Classes used to build the CTS tests
# @ts.lib.classpath     --  Classes used to build jaspic.jar 
###############################################################

jaspic.classes=${jaspic.home}/modules/war-util.jar:\
               ${jaspic.home}/modules/jaspic.provider.framework.jar:\
               ${jaspic.home}/modules/security.jar:\
               ${jaspic.home}/modules/security-ee.jar:\
               ${jaspic.home}/modules/kernel.jar:\
               ${jaspic.home}/modules/admin-cli.jar:\
               ${jaspic.home}/modules/annotation-framework.jar:\
               ${jaspic.home}/modules/common-util.jar:\
               ${jaspic.home}/modules/config-api.jar:\
               ${jaspic.home}/modules/connectors-runtime.jar:\
               ${jaspic.home}/modules/container-common.jar:\
               ${jaspic.home}/modules/dol.jar:\
               ${jaspic.home}/modules/gf-connectors-connector.jar:\
               ${jaspic.home}/modules/glassfish.jar:\
               ${jaspic.home}/modules/glassfish-naming.jar:\
               ${jaspic.home}/modules/internal-api.jar:\
               ${jaspic.home}/modules/security.jar:\
               ${jaspic.home}/modules/jakarta.servlet-api.jar:\
               ${jaspic.home}/modules/war-util.jar:\
               ${jaspic.home}/modules/jakarta.mail.jar:\
               ${jaspic.home}/modules/jakarta.enterprise.deploy-api.jar:\
               ${jaspic.home}/modules/webservices-osgi.jar:\
               ${jaspic.home}/modules/jaxb-osgi.jar:\
               ${jaspic.home}/modules/jakarta.jms-api.jar:\
               ${jaspic.home}/modules/jakarta.annotation-api.jar:\
               ${jaspic.home}/modules/webservices-api-osgi.jar:\
               ${jaspic.home}/modules/deployment-client.jar:\
	       ${jaspic.home}/modules/jakarta.authentication-api.jar

ts.run.classpath=${jaspic.classes}:${ts.home}/lib/tsharness.jar:${ts.home}/lib/jaspic.jar:${ts.home}/lib/tssv.jar

ts.harness.classpath=${ts.home}/lib/jaxb-api.jar:${ts.home}/lib/jaxb-impl.jar:${ts.home}/lib/jaxb-xjc.jar:${ts.home}/lib/tsharness.jar:${ts.home}/lib/jaspic.jar:${ts.home}/lib/sigtest.jar:${ts.run.classpath}:${ts.home}/lib/javatest.jar:${ts.home}/lib/jdom-1.1.3.jar:${ant.home}/lib/ant.jar

#classpath used for building CTS tests only (DO NOT MODIFY)
ts.classpath=${ts.home}/classes:${tools.jar}:${ts.home}/lib/tsharness.jar:${ts.home}/lib/jaspic.jar:${ts.home}/lib/sigtest.jar:${ts.run.classpath}:${ts.home}/lib/javatest.jar:${ts.home}/lib/jdom-1.1.3.jar:${ant.home}/lib/ant.jar

#classpath used for building jaspic.jar (DO NOT MODIFY)
ts.lib.classpath=${tools.jar}:${ts.harness.classpath}


###############################################################
# @s1as.classpathsuffix - The classpath suffix of
#     the RI when being used as the app server under test.
###############################################################
s1as.classpathsuffix=

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
        CLASSPATH=${jaspic.home.ri}/modules/webservices-osgi.jar:\
                  ${jaspic.home.ri}/modules/webservices-security.jar:\
                  ${jaspic.home.ri}/modules/ha-api.jar:\
                  ${ts.home}/classes:${ts.harness.classpath}:\
                  ${ts.home}/lib/jaxen-1.1.6.jar:\
        DISPLAY=${ts.display} \
        HOME="${user.home}" \
        windir=${windir} \
        SYSTEMROOT=${SYSTEMROOT} \
        PATH="${jaspic.home}/nativelib" \
        ${JAVA_HOME}/bin/java \
        -Dcts.tmp=$harness.temp.directory \
        -Djava.protocol.handler.pkgs=javax.net.ssl \
        -Djavax.net.ssl.keyStore=${ts.home}/bin/certificates/clientcert.jks \
        -Djavax.net.ssl.keyStorePassword=changeit \
        -Djavax.net.ssl.trustStore=${s1as.domain}/${sjsas.instance.config.dir}/cacerts.jks \
        -Dcom.sun.aas.installRoot=${jaspic.home} \
        -Dcom.sun.aas.configRoot=${jaspic.home}/config \
        -Dlog.file.location=${log.file.location} \
        -Djava.util.logging.config.file=${ts.home}/bin/logging.properties \
        -Dservlet.is.jsr115.compatible=${servlet.is.jsr115.compatible} \
        -Dsoap.is.jsr115.compatible=${soap.is.jsr115.compatible} \
        -Dprovider.configuration.file=${provider.configuration.file} \
        -Dlogical.hostname.servlet=${logical.hostname.servlet} \
        -Djava.security.properties=${s1as.domain}/config/ts.java.security \
        -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs

########################################################################
# -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5000 
## Appclient Command line for the App Server under test
## Licensees modify this command
#
# @command.testExecuteAppClient - This command is used to execute
#                                 the application client container for
#                                 the vendor implementation (vi).  
#                                 Please note that $TS_HOME/classes
#                                 should not be in the classpath for
#                                 this command since all client classes
#                                 are self contained in the application
#                                 archive (or referenced via the manifest).
########################################################################
command.testExecuteAppClient= \
        com.sun.ts.lib.harness.ExecTSTestCmd DISPLAY=${ts.display} HOME="${user.home}" \
        LD_LIBRARY_PATH=${jaspic.home}/lib \
        TMP=${TMP} \
        windir=${windir} \
        SYSTEMROOT=${SYSTEMROOT} \
        PATH="${jaspic.home}/nativelib" \
        APPCPATH=${pathsep}${ts.home}/lib/tsharness.jar${pathsep}${ts.home}/lib/jaspic.jar${pathsep}${jaspic.home}/lib/tssv.jar \
        TZ=${tz} \
        ${JAVA_HOME}/bin/java \
        -Dlog.file.location=${log.file.location} \
        -Djava.util.logging.config.file=${jaspic.home}/domains/domain1/config/logging.properties \
        -Dservlet.is.jsr115.compatible=${servlet.is.jsr115.compatible} \
        -Dsoap.is.jsr115.compatible=${soap.is.jsr115.compatible} \
        -Dprovider.configuration.file=${provider.configuration.file} \
        -Dlogical.hostname.servlet=${logical.hostname.servlet} \
        -Djava.security.properties=${jaspic.home}/domains/domain1/config/ts.java.security \
        -Djava.system.class.loader=org.glassfish.appclient.client.acc.agent.ACCAgentClassLoader  \
        -Djava.security.policy=${jaspic.home}/lib/appclient/client.policy \
        -Dcts.tmp=$harness.temp.directory \
        -Djava.security.auth.login.config=${jaspic.home}/lib/appclient/appclientlogin.conf \
        -Djava.protocol.handler.pkgs=javax.net.ssl \
        -Dcom.sun.enterprise.home=${jaspic.home} \
        -Djavax.net.ssl.keyStore=${bin.dir}/certificates/clientcert.jks \
        -Djavax.net.ssl.keyStorePassword=changeit \
        -Djavax.net.ssl.trustStore=${s1as.domain}/${sjsas.instance.config.dir}/cacerts.jks \
        -Dcom.sun.aas.installRoot=${jaspic.home} \
        -Dcom.sun.aas.imqLib=${jaspic.home}/imq/lib \
        -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl \
        -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl \
        -Djavax.xml.transform.TransformerFactory=com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl \
        -Dorg.xml.sax.driver=com.sun.org.apache.xerces.internal.parsers.SAXParser \
        -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter \
        -Dstartup.login=false \
        -Dauth.gui=false \
        -Dcom.sun.aas.configRoot=${jaspic.home}/config \
        -Ddeliverable.class=${deliverable.class} \
        -Dcom.sun.xml.ws.transport.http.client.HttpTransportPipe.dump=true \
        -Dcom.sun.xml.ws.transport.http.HttpAdapter.dump=true \
        -javaagent:${jaspic.home}/lib/gf-client.jar=arg=-configxml,arg=${ts.home}/tmp/appclient/jaspic.sun-acc.xml,client=jar=$testExecuteArgs




########################################################################
## Command line for standalone clients running against the 
## Jakarta EE RI (Do not modify)
########################################################################
command.testExecute2=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${ts.home}/classes:${ts.harness.classpath}:\
                  ${ts.home}/lib/jaxen-1.1.6.jar:\
                  ${ts.home}/lib/saxpath.jar:\
        DISPLAY=${ts.display} \
        HOME="${user.home}" \
        windir=${windir} \
        SYSTEMROOT=${SYSTEMROOT} \
        PATH="${jaspic.home}/nativelib" \
        ${JAVA_HOME}/bin/java \
        -Dcts.tmp=$harness.temp.directory \
        -Djava.security.policy=${ts.home}/bin/harness.policy \
        -Djava.security.manager \
        -Djava.protocol.handler.pkgs=javax.net.ssl \
        -Djavax.net.ssl.keyStore=${ts.home}/bin/certificates/clientcert.jks \
        -Djavax.net.ssl.keyStorePassword=changeit \
        -Djavax.net.ssl.trustStore=${ri.domain}/${ri.instance.config.dir}/cacerts.jks \
        -Dlog.file.location=${log.file.location} \
        -Dprovider.configuration.file=${provider.configuration.file} \
        -Djava.security.properties=${ri.domain}/config/ts.java.security \
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

#########################################################################
# @jimage.dir:       This property specifies the directory where Java 11+
#                    modules will be expanded by the jimage tool for use
#                    in sigTestClasspath
# @sigTestClasspath: This property must be set when running signature 
#                    tests.  This property should be set to a list of
#                    jar files and/or directories which contain your 
#                    Jakarta EE and Java SE classes. Paths must be 
#                    separated by the appropriate path separator 
#                    (';' windows, ':' Unixes).
#                    Note that on mac, classes.jar is used instead of rt.jar.
#########################################################################
jimage.dir=${ts.home}/tmp/jdk-bundles

sigTestClasspath=${jaspic.home}/modules/jakarta.authentication-api.jar:${jaspic.home}/modules/security.jar:${jimage.dir}/java.base${pathsep}${jimage.dir}/java.rmi${pathsep}${jimage.dir}/java.sql${pathsep}${jimage.dir}/java.naming

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
harness.executeMode=0
harness.socket.retry.count=10
harness.log.delayseconds=1


####################################################################
# @impl.vi                    This property must be set to the implementation
#                             under test.
# @impl.vi.deploy.dir         This property must be set to the deploy dir for
#                             the implementation such that copying archives to
#                             this dir will initiate deployment of that archive.
# @impl.deploy.timeout.multiplier  The time it will wait for deployment to
#                                   succeed or fail
####################################################################
impl.vi=glassfish
impl.vi.deploy.dir=${s1as.domain}/autodeploy
impl.deploy.timeout.multiplier=1200


#########################################################################
# @wsdlRepository1 Location to publish final wsdl files when using
#                  file URL publishing for Vendor Jakarta EE implementation.
# @wsdlRepository2 Location to publish final wsdl files when using
#                  file URL publishing for Sun RI.
#########################################################################
wsdlRepository1=${harness.temp.directory}/wsdlRepository1
wsdlRepository2=${harness.temp.directory}/wsdlRepository2

#######################################################################
# @deployment_host.1  name of machine running the JSR 88 deployment
#                     process for the vendor's Jakarta EE implementation.
# @deployment_port.1  deployment port (if applicable) for the vendor's
#                     Jakarta EE implementation.
#######################################################################
deployment_host.1=${orb.host}
deployment_port.1=${s1as.admin.port}

########################################################################
# @deploy.delay.in.minutes is no longer used, it was for pruned Jakarta Deployment
########################################################################

########################################################################
## Following are no longer used, were settings for pruned Jakarta Deployment
########################################################################
#deployManagerJarFile.2
#deployManageruri.2
#deployManageruname.2
#deployManagerpasswd.2
#
# @porting.ts.deploy2.class.1
# @porting.ts.deploy2.class.2
# porting.ts.deploy2.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIDeployment2
# porting.ts.deploy2.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIDeployment2


###############################################################
## These properties must be set to tell the Test harness the
## class names of your porting class implementations.  By default
## both property sets below point to Sun RI specific classes.  To
## run interoperability tests, the ".2" set of properties should
## always point to Sun RI classes.  The ".1" set should point to
## implementations that work in your specific Jakarta EE environment.
#
# @porting.ts.login.class.1  VI of
#    com.sun.ts.lib.porting.TSLoginContextInterface
# @porting.ts.url.class.1  VI of
#    com.sun.ts.lib.porting.TSURLInterface
# @porting.ts.jms.class.1  VI of
#    com.sun.ts.lib.porting.TSJMSAdminInterface
# @porting.ts.HttpsURLConnection.class.1  VI of
#    com.sun.ts.lib.porting.TSHttpsURLConnectionInterface
###############################################################
porting.ts.login.class.1=com.sun.ts.lib.implementation.sun.javaee.GlassFishLoginContext
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.jms.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIJMSAdmin
porting.ts.HttpsURLConnection.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection


###############################################################
# The following properties should not be needed unless trying
# to execute the SOAP profile tests within a JavaEE container
# which implies platform.mode is set to jakartaEE
# Even then, these properties should only be needed to
# satisfy some requirements for properly lookups.
###############################################################
porting.ts.deploy2.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIDeployment2
porting.ts.login.class.2=com.sun.ts.lib.implementation.sun.javaee.GlassFishLoginContext
porting.ts.url.class.2=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.jms.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIJMSAdmin
porting.ts.HttpsURLConnection.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection
namingServiceHost1=${orb.host}
namingServicePort1=${orb.port}
namingServiceHost2=${orb.host}
namingServicePort2=${orb.port}
variable.mapper=org.glassfish.expressly.lang.VariableMapperImpl
# The following Jakarta Deployment properties are not supported
#deployManagerJarFile.2=${jaspic.home}/lib/deployment/sun-as-jsr88-dm.jar
#deployManageruri.2=deployer:Sun:AppServer::${deployment_host.2}:${s1as.admin.port}
#deployManageruname.2=${s1as.admin.user}
#deployManagerpasswd.2=${s1as.admin.passwd}


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
# @webServerHost  hostname for the Vendor's Jakarta EE Web Server
# @webServerPort  port number of the Vendor's Jakarta EE Web Server
# @webServerHost.2  hostname for the Jakarta EE RI Web Server
# @webServerPort.2  port number of the Jakarta EE RI Web Server
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
webServerHost.2=${orb.host}
webServerPort.2=8002
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
# @nobodyuser     This value must be the same value returned by a call to 
#                 getCallerPrincipal().getName() from the ejb-tier when
#                 an unauthenticated caller in the web tier invokes an
#                 ejb method.
#########################################################################
user         = j2ee
password     = j2ee
authuser     = javajoe
authpassword = javajoe
nobodyuser   = guest
user_vi=j2ee_vi
password_vi=j2ee_vi
user_ri=j2ee_ri
password_ri=j2ee_ri
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
##This property is needed for jms tests.  
#
# @jms_timeout - this is the amount of time in milliseconds that synchronous
#                receives will wait for a message
##############################################################################
jms_timeout=5000


###################################################################
# @log.file.location  This property is used by Jakarta Authorization tests and Jakarta Authentication tests
#   to create and analyze provider logs. Specify the log directory in which
#   your appserver generates logs.
#  
###################################################################
log.file.location=${s1as.domain}/logs


###################################################################
# @servlet.is.jsr115.compatible  This property is used by Jakarta Authentication tests 
#   to determine if the servlet container is a Jakarta Authorization compatible 
#   container.  (true = compatible to Jakarta Authorization, false = not compatible.)
# @soap.is.jsr115.compatible  This is also used by Jakarta Authentication tests to
#   determine if the SOAP container is  Jakarta Authorization compatible.
###################################################################
servlet.is.jsr115.compatible=true
soap.is.jsr115.compatible=false


###################################################################
## The following properties should be set before running any security
## related Jakarta Authentication tests.
#
# @provider.configuration.file
#             This property is used by Jakarta Authentication tests to configure TestSuite's
#             AuthConfig Provider and points at an xml file which is used
#             to populate the Jakarta Authentication provider information into the active
#             ACF.  This file contaiins known/expected test provider info.
#
# @schema.file.location
#              This points to the directory that the provider-configuration.xsd 
#              file will live.  (The provider-configuration.xsd is used to
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
$
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
#              logs the Jakarta Authentication calls made in appclient container. 
#
#              using this property we enable the appclient container's 
#              logging level to INFO
#
# @vendor.authconfig.factory
#             This property specifies vendor's authconfig factory class
#             this will be used by Jakarta Authentication tests to register TestSuite's 
#             provider in Vendor's AuthConfig Factory.
#
#              For example for SJSAS RI this value is
#
#               vendor.authconfig.factory=
#                   com.sun.enterprise.security.jmac.config.GFAuthConfigFactory
#
#
###################################################################
provider.configuration.file=${javaee.home}/domains/domain1/config/ProviderConfiguration.xml
schema.file.location=${javaee.home}/lib/schemas
logical.hostname.servlet=server
logical.hostname.soap=localhost
appclient.log.output=true
vendor.authconfig.factory=com.sun.enterprise.security.jmac.config.GFAuthConfigFactory


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
##1: compile only
##2: compile and build component archives (e.g., jar's, war's)
##3: compile and build component and application archives
##default is set to 3
build.level=3

## Deliverables wanting ts.* packaging tasks to add extension list
## attributes to the manifest files must set this property to true.
create.manifest.extension.entries=true

## Deliverables must set this property to the name of the deliverable
## specific library jar file (iff create.manifest.extension.entries is set to 
## true)
tslib.name=jaspic

###############################################################
## Use JaspicDeliverable class when the platform.mode is in standalone mode
## Use CTSDeliverable class when the platform.mode is in JakartaEE mode
###############################################################
#deliverable.class=com.sun.ts.lib.deliverable.jaspic.JaspicDeliverable
#deliverable.class=com.sun.ts.lib.deliverable.jaspic.JaspicJakartaEEDeliverable
deliverable.class=com.sun.ts.lib.deliverable.cts.CTSDeliverable


###############################################################
## These properties are used by connector tests and are non configurable 
## and must specify valid username,password to establish TSeis connection. 
###############################################################
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

#######################################################################
## platform.mode is used by jaxws tests to know whether to use
## the Jakarta EE Programming Model for service and port access or the
## JAXWS/JASPIC Programming model based on the runtime execution 
## platform.
##
## platform.mode=standalone	JAXWS/JASPIC Programming Model
## platform.mode=jakartaEE		Jakarta EE Programming Model
##
## Note: When you switch platform.mode make sure you change the property
##       deliverable.class as well(this is appilcable only for JASPIC tests)
######################################################################
platform.mode=jakartaEE


ts_home=${ts.home}

###########################################################################
# tools.jar should be set to the location of the tools.jar from the installed
# jdk
###########################################################################
jaxws.ri.lib=/export/home/sun/jaxws211/jaxws-ri/lib
tools.jar=${jaxws.ri.lib}/jaxws-tools.jar

###########################################################################
# various flags used by the generation tools
###########################################################################
wsgen.ant.classname=
wsgen.classpath=${tools.jar}:${jaspic.classes}
wsgen.verbose=true
wsgen.debug=false
wsimport.ant.classname=
wsimport.classpath=${jaspic.classes}:${tools.jar}
wsimport.verbose=true
wsimport.debug=false
wsimport.jvmargs=

###########################################################################
# various flags used by Suns generation tools
# DO NOT EDIT
###########################################################################
ri.wsgen.ant.classname=com.sun.tools.ws.ant.WsGen
ri.wsgen.classpath=${jaspic.classes}:${tools.jar}
ri.wsgen.verbose=true
ri.wsgen.debug=false
ri.wsimport.ant.classname=com.sun.tools.ws.ant.WsImport
ri.wsimport.classpath=${jaspic.classes}:${tools.jar}
ri.wsimport.verbose=true
ri.wsimport.debug=false
ri.wsimport.jvmargs=


###############################################################################
## These should be deprecated.  There is no need to modify the following
## properties...
###############################################################################


###############################################################
## These properties are implementations of the pre-1.4 Deployment
## porting interface.  The impls defined below use those APIs.
## The TSDeploymentInterface2 interface
## is now the location for deployment porting code for functionality
## not addressed by JSR-88.
#
# @porting.ts.deploy.class.1 This property must point to an implementation
# of com.sun.ts.lib.porting.TSDeploymentInterface.  CTS 5 shipped
# with this property set to a standard JSR-88 impl which
# should be able to be used when running against an appserver that supports
# JSR 88.  Web Profile impls may not support JSR 88.  In CTS 6, the default
# value fo this property points to an impl that deploys via copying/deleting
# to/from the RI autodeploy directory.  You have the following choices for
# setting these properties in CTS 6:
#
# 1.  Set this property to the standard JSR 88 impl....
#
#     porting.ts.deploy.class.1=com.sun.ts.lib.deliverable.cts.deploy.StandardDeployment14
#
#     If you use the above impl, then you must also set the
#     porting.ts.deploy2.class.1 property to a valid impl of
#     com.sun.ts.lib.porting.TSDeploymentInterface2.  In the case of the RI,
#     the porting.ts.deploy2.class.2 property would be set to the
#     RI impl of com.sun.ts.lib.implementation.sun.javaee.SunRIDeployment2.
#
# 2.  Or, you can set this property to deploy/undeploy to/from your impl in
#     a custom way.  In the case of the RI, it would be set to the following.
#     This is the default setting for CTS 6.
#
#     porting.ts.deploy.class.1=com.sun.ts.lib.implementation.sun.javaee.glassfish.AutoDeployment
#
# @porting.ts.deploy.class.2 This property must point to an implementation
# of com.sun.ts.lib.porting.TSDeploymentInterface.  CTS 5 shipped
# with this property set to a standard JSR-88 impl which
# should be able to be used when running against an appserver that supports
# JSR 88.  Web Profile impls may not support JSR 88.  In CTS 6, the default
# value fo this property points to an impl that deploys via copying/deleting
# to/from the RI autodeploy directory.  This should be left unchanged...
#
#     porting.ts.deploy.class.2=com.sun.ts.lib.implementation.sun.javaee.glassfish.AutoDeployment
#
###############################################################
porting.ts.deploy.class.1=com.sun.ts.lib.implementation.sun.javaee.glassfish.AutoDeployment
porting.ts.deploy.class.2=com.sun.ts.lib.implementation.sun.javaee.glassfish.AutoDeploymentSeparateVM


#classpath used by standard deployment server to deploy to the RI in a separate 
#VM (DO NOT MODIFY)
ts.standard.deployment.server.classpath=${ts.home}/lib/jakarta.xml.bind-api.jar:${ts.home}/lib/jaxb-impl.jar:${ts.home}/lib/jaxb-xjc.jar:${ts.home}/lib/tsharness.jar:${ts.home}/lib/jaspic.jar:${ts.home}/lib/sigtest.jar:${ts.run.classpath}:${ts.home}/lib/javatest.jar:${ts.home}/lib/jdom-1.1.3.jar:${ant.home}/lib/ant.jar:${ts.home}/lib/commons-codec-1.9.jar

###############################################################################
# The following properties are aliases for the old J2EE properties.  These
# properties are now named javaee instead of j2ee and will eventually be phased
# out of the CTS.  These props are necessary until the props are renamed 
# through out all CTS.
###############################################################################
j2ee.home=${jaspic.home}
j2ee.home.ri=${jaspic.home.ri}
javaee.home=${jaspic.home}
javaee.home.ri=${jaspic.home}

