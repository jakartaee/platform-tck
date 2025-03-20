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

#
# @(#)ts.jte	1.201 02/06/28
#
 
#########################################################################
#                                                                   	#
#   JavaTest Environment file for J2EE Compatibility Test Suite         #
#                                                                       #
#   Environment specific properties in this file will likely	 	    #
#   have to be modified prior to running the J2EE-CTS.                  #
#   Instructions for modifying these properties are contained in this 	#
#   file.								                                #
#                                                                       #
#########################################################################

# TestFinder macros
JCKTestFinder=com.sun.javatest.lib.JCKTestFinder
TSTestFinder=com.sun.ts.lib.harness.TSTestFinder

# Script macros
JCKScript=com.sun.javatest.lib.JCKScript
TSScript=com.sun.ts.lib.harness.TSScript

# Command Macros
ExecJCKTestSameJVMCmd=com.sun.javatest.lib.ExecJCKTestSameJVMCmd
ExecJCKTestOtherJVMCmd=com.sun.javatest.lib.ExecJCKTestOtherJVMCmd

 
########################################################################
# Environment ts_unix                                              #
########################################################################
env.ts_unix.description=\
        Run the runtime tests in a separate JVM (process) on Solaris
#env.ts_unix.finder=$TSTestFinder
#env.ts_unix.script=$TSScript -runtime
env.ts_unix.menu=true
env.ts_unix.testsuite=com.sun.ts.lib.harness.TS
env.ts_unix.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${J2EE_HOME}/lib/j2ee.jar:${J2EE_HOME}/lib/classes:${TS_HOME}/lib/tsharness.jar:${TS_HOME}/lib/cts.jar:${TS_HOME}/lib/javatest.jar:${TS_HOME}/lib/commons-httpclient.jar:${TS_HOME}/classes:${LOCAL_CLASSES} \
        DISPLAY=:0.0 \
        HOME=${user.home} \
        LD_LIBRARY_PATH=${J2EE_HOME}/nativelib \
        ${JAVA_HOME}/bin/java \
        --module-path ${J2EE_HOME}/lib/bootstrap --add-modules ALL-MODULE-PATH \
        ${JAVA_OPTIONS} -Dorg.omg.CORBA.ORBInitialHost=${ORB_HOST} \
    -Dcts.tmp=$harness.temp.directory \
	-Djava.security.auth.login.config=${TS_HOME}/bin/login.config \
	-Djava.security.auth.policy=${TS_HOME}/bin/java.security.auth.policy \
	-Dcom.sun.enterprise.home=${J2EE_HOME} \
	-Dorg.omg.CORBA.ORBInitialPort=${ORB_PORT} -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs
env.ts_unix.command.testExecuteAppClient=com.sun.ts.lib.harness.ExecTSTestCmd DISPLAY=:0.0 HOME=${user.home} \
        LD_LIBRARY_PATH=${J2EE_HOME}/nativelib \
	TZ=${TZ} \
        ${JAVA_HOME}/bin/java \
        --module-path ${J2EE_HOME}/lib/bootstrap --add-modules ALL-MODULE-PATH \
        -Dorg.omg.CORBA.ORBInitialHost=${ORB_HOST} \
		${JAVA_OPTIONS} -Djava.security.policy=${TS_HOME}/bin/java.policy -Djava.util.logging.config.file=${TS_HOME}/bin/client-logging.properties \
    -Dcts.tmp=$harness.temp.directory \
         -Dorg.xml.sax.driver=org.apache.crimson.parser.XMLReaderImpl \
         -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter \
	 -Dorg.omg.CORBA.ORBInitialPort=${ORB_PORT} -Djms.home=${J2EE_HOME} -Djms\
.properties=${J2EE_HOME}/config/jms_client.properties \
	-Djava.security.auth.login.config=${TS_HOME}/bin/login.config \
	-Djava.security.auth.policy=${TS_HOME}/bin/java.security.auth.policy \
	-Dcom.sun.enterprise.home=${J2EE_HOME} \
	-Djavax.net.ssl.trustStore=${J2EE_HOME}/lib/security/cacerts.jks \
    -Djava.endorsed.dirs=${java.endorsed.dirs} \
	-Dstartup.login=false -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Ddeliverable.class=${deliverable.class} -classpath ${J2EE_HOME}/lib/j2ee.jar:${J2EE_HOME}/lib/schema2beans.jar:${J2EE_HOME}/lib/toolclasses:${J2EE_HOME}/lib/management/MEJBServerClient.jar:${TS_HOME}/lib/tsharness.jar:${TS_HOME}/lib/cts.jar:${TS_HOME}/lib/javatest.jar:${J2EE_HOME}/lib/locale:${LOCAL_CLASSES}  com.sun.enterprise.appclient.Main $testExecuteArgs

env.ts_unix.command.testExecuteAppClient2=com.sun.ts.lib.harness.ExecTSTestCmd DISPLAY=:0.0 HOME=${user.home} \
        LD_LIBRARY_PATH=${J2EE_HOME_RI}/nativelib \
	TZ=${TZ} \
        ${JAVA_HOME}/bin/java \
        --module-path ${J2EE_HOME_RI}/lib/bootstrap --add-modules ALL-MODULE-PATH \
        -Dorg.omg.CORBA.ORBInitialHost=${ORB_HOST_RI} \
		${JAVA_OPTIONS} -Djava.security.policy=${TS_HOME}/bin/java.policy -Djava.util.logging.config.file=${TS_HOME}/bin/client-logging.properties \
    -Dcts.tmp=$harness.temp.directory \
         -Dorg.xml.sax.driver=org.apache.crimson.parser.XMLReaderImpl \
         -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter \
	 -Dorg.omg.CORBA.ORBInitialPort=${ORB_PORT_RI} -Djms.home=${J2EE_HOME_RI} -Djms\
.properties=${J2EE_HOME_RI}/config/jms_client.properties \
	-Djava.security.auth.login.config=${TS_HOME}/bin/login.config \
	-Djava.security.auth.policy=${TS_HOME}/bin/java.security.auth.policy \
	-Dcom.sun.enterprise.home=${J2EE_HOME_RI} \
	-Djavax.net.ssl.trustStore=${J2EE_HOME_RI}/lib/security/cacerts.jks \
    -Djava.endorsed.dirs=${java.endorsed.dirs} \
	-Dstartup.login=false -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Ddeliverable.class=${deliverable.class} -classpath ${J2EE_HOME_RI}/lib/j2ee.jar:${J2EE_HOME_RI}/lib/schema2beans.jar:${J2EE_HOME_RI}/lib/toolclasses:${J2EE_HOME_RI}/lib/management/MEJBServerClient.jar:${J2EE_HOME_RI}/lib/system/riinterceptors.jar:${TS_HOME}/lib/tsharness.jar:${TS_HOME}/lib/cts.jar:${TS_HOME}/lib/javatest.jar:${J2EE_HOME_RI}/lib/locale:${LOCAL_CLASSES_RI}  com.sun.enterprise.appclient.Main $testExecuteArgs

########################################################################
# Environment ts_win32						       #
########################################################################
env.ts_win32.description=\
        Run the runtime tests in a separate JVM (process) on Win32
#env.ts_win32.finder=$TSTestFinder
#env.ts_win32.script=$TSScript -runtime
env.ts_win32.menu=true
env.ts_win32.testsuite=com.sun.ts.lib.harness.TS
env.ts_win32.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${J2EE_HOME}\\lib\\j2ee.jar;${TS_HOME}\\lib\\tsharness.jar;${TS_HOME}\\lib\\cts.jar;${J2EE_HOME}\\lib\\classes;${TS_HOME}\\lib\\javatest.jar;${TS_HOME}\\lib\\commons-httpclient.jar;${TS_HOME}\\classes;${LOCAL_CLASSES} \
	windir=${windir} \
	windir=${windir} \
	SYSTEMROOT=${SYSTEMROOT} \
	PATH=${J2EE_HOME}\\nativelib \
        ${JAVA_HOME}\\bin\\java \
        --module-path ${J2EE_HOME}\\lib\\bootstrap --add-modules ALL-MODULE-PATH \
        ${JAVA_OPTIONS} -Dorg.omg.CORBA.ORBInitialHost=${ORB_HOST} \
    -Dcts.tmp=$harness.temp.directory \
	-Djava.security.auth.login.config=${TS_HOME}\\bin\\login.config \
	-Djava.security.auth.policy=${TS_HOME}\\bin\\java.security.auth.policy \
	-Dorg.omg.CORBA.ORBInitialPort=${ORB_PORT} -Djava.security.policy=${TS_HOME}\\bin\\java.policy -Ddeliverable.class=${deliverable.class} $testExecuteClass $testExecuteArgs

env.ts_win32.command.testExecuteAppClient=com.sun.ts.lib.harness.ExecTSTestCmd \
			windir=${windir} SYSTEMROOT=${SYSTEMROOT} \
	PATH=${J2EE_HOME}\\nativelib \
        ${JAVA_HOME}\\bin\\java \
        --module-path ${J2EE_HOME}\\lib\\bootstrap --add-modules ALL-MODULE-PATH \
        ${JAVA_OPTIONS} -Dorg.omg.CORBA.ORBInitialHost=${ORB_HOST} \
    -Dcts.tmp=$harness.temp.directory \
		-Djava.security.policy=${TS_HOME}\\bin\\java.policy -Djava.util.logging.config.file=${TS_HOME}/bin/client-logging.properties \
        -Dorg.xml.sax.driver=org.apache.crimson.parser.XMLReaderImpl \
        -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter \
	-Dorg.omg.CORBA.ORBInitialPort=${ORB_PORT} -Djms.home=${J2EE_HOME} -Djms\
.properties=${J2EE_HOME}\\config\\jms_client.properties \
	-Djava.security.auth.login.config=${TS_HOME}\\bin\\login.config \
	-Djava.security.auth.policy=${TS_HOME}\\bin\\java.security.auth.policy \
	-Dcom.sun.enterprise.home=${J2EE_HOME} \
	-Djavax.net.ssl.trustStore=${J2EE_HOME}\\lib\\security\\cacerts.jks \
    -Djava.endorsed.dirs=${java.endorsed.dirs} \
	-Dstartup.login=false -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Ddeliverable.class=${deliverable.class} -classpath ${J2EE_HOME}\\lib\\j2ee.jar;${J2EE_HOME}\\lib\\schema2beans.jar;${J2EE_HOME}\\lib\\toolclasses;${J2EE_HOME}\\lib\\management\\MEJBServerClient.jar;${TS_HOME}\\lib\\tsharness.jar;${TS_HOME}\\lib\\cts.jar;${TS_HOME}\\lib\\javatest.jar;${J2EE_HOME}\\lib\\locale;${LOCAL_CLASSES}  com.sun.enterprise.appclient.Main $testExecuteArgs

env.ts_win32.command.testExecuteAppClient2=com.sun.ts.lib.harness.ExecTSTestCmd \
			windir=${windir} SYSTEMROOT=${SYSTEMROOT} \
	PATH=${J2EE_HOME_RI}\\nativelib \
        ${JAVA_HOME}\\bin\\java \
        --module-path ${J2EE_HOME_RI}\\lib\\bootstrap --add-modules ALL-MODULE-PATH \
        ${JAVA_OPTIONS} -Dorg.omg.CORBA.ORBInitialHost=${ORB_HOST_RI} \
    -Dcts.tmp=$harness.temp.directory \
		-Djava.security.policy=${TS_HOME}\\bin\\java.policy -Djava.util.logging.config.file=${TS_HOME}/bin/client-logging.properties \
        -Dorg.xml.sax.driver=org.apache.crimson.parser.XMLReaderImpl \
        -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter \
	-Dorg.omg.CORBA.ORBInitialPort=${ORB_PORT_RI} -Djms.home=${J2EE_HOME_RI} -Djms\
.properties=${J2EE_HOME_RI}\\config\\jms_client.properties \
	-Djava.security.auth.login.config=${TS_HOME}\\bin\\login.config \
	-Djava.security.auth.policy=${TS_HOME}\\bin\\java.security.auth.policy \
	-Dcom.sun.enterprise.home=${J2EE_HOME_RI} \
	-Djavax.net.ssl.trustStore=${J2EE_HOME_RI}\\lib\\security\\cacerts.jks \
    -Djava.endorsed.dirs=${java.endorsed.dirs} \
	-Dstartup.login=false -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl -Dorg.xml.sax.parser=org.xml.sax.helpers.XMLReaderAdapter -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Ddeliverable.class=${deliverable.class} -classpath ${J2EE_HOME_RI}\\lib\\j2ee.jar;${J2EE_HOME_RI}\\lib\\schema2beans.jar;${J2EE_HOME_RI}\\lib\\toolclasses;${J2EE_HOME_RI}\\lib\\management\\MEJBServerClient.jar;${J2EE_HOME_RI}\\lib\\system\\riinterceptors.jar;${TS_HOME}\\lib\\tsharness.jar;${TS_HOME}\\lib\\cts.jar;${TS_HOME}\\lib\\javatest.jar;${J2EE_HOME_RI}\\lib\\locale;${LOCAL_CLASSES_RI}  com.sun.enterprise.appclient.Main $testExecuteArgs

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
# of execution:
#    0 - default (deploy, run, undeploy)
#    1 - deploy only
#    2 - run only
#    3 - undeploy only
#    4 - deploy and run only
########################################################################
harness.temp.directory=${TS_HOME}
harness.log.port=2000
harness.log.traceflag=false
harness.log.delayseconds=1
ts_home=${TS_HOME}
harness.executeMode=0


#######################################################################
#These properties are specific for deployment to Vendor J2EE Server #1
#and Sun RI J2EE Server #2 respectively.
#######################################################################
deployment_host.1=${ORB_HOST}
deployment_host.2=${ORB_HOST_RI}

#######################################################################
#This is the port that is used by the standalone RMI server used
#by SunRIDeploymentSeparateVM.  If you change this port, you must also
#change the port that the startSunRIPortingServer script uses by 
#passing -port <port #>. 
#######################################################################
sun.porting_server.port=2002

###############################################################
#These properties must be set to tell the Test harness the
#class names of your porting class implementations.  By default
#both property sets below point to Sun RI specific classes.  To
#run interoperability tests, the ".2" set of properties should
#always point to Sun RI classes.  The ".1" set should point to
#implementations that work in your specific J2EE environment.
###############################################################

#Porting class names for Vendor J2EE Implementation #1
porting.ts.deploy.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIDeployment
porting.ts.login.class.1=com.sun.ts.lib.implementation.sun.javaee.GlassFishLoginContext
porting.ts.url.class.1=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.jms.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIJMSAdmin
porting.ts.ctsHttpsURLConnection.class.1=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection


#Porting class names for Sun RI J2EE Implementation #2 (must be Sun's RI)
porting.ts.deploy.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIDeploymentSeparateVM
porting.ts.login.class.2=com.sun.ts.lib.implementation.sun.javaee.GlassFishLoginContext
porting.ts.url.class.2=com.sun.ts.lib.implementation.sun.common.SunRIURL
porting.ts.jms.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIJMSAdmin
porting.ts.ctsHttpsURLConnection.class.2=com.sun.ts.lib.implementation.sun.javaee.SunRIHttpsURLConnection

#Naming Service hosts and ports for Vendor J2EE Naming Service #1 and 
#Sun RI J2EE Naming Service #2
namingServiceHost1=${ORB_HOST}
namingServicePort1=${ORB_PORT}
namingServiceHost2=${ORB_HOST_RI}
namingServicePort2=${ORB_PORT_RI}

##############################################################
# Useralias for certificate based login
# This property is used in mutual authentication to pickup the
# certificate based on the useralias
##############################################################
certLoginUserAlias=cts


#####################################################################
#The following properties must be set prior to running the Servlet
#or JSP API tests and interoperability tests.
#
#These properties must specify the host and port of the web server,
#in which the servlets and JSPs are deployed.  
#
#"webServerHost" and "webServerPort" are the settings for the Vendor
#J2EE Web Server #1 and "webServerHost2" and "webServerPort2" are the
#settings for Sun RI J2EE Web Server #2.
# 
#The ServletClientThreads property configures the number of threads
#used by the client for the SingleThreadModel servlet test.
#If the container implementation supports pooling of SingleThreadModel
#servlets, set the value of ServletClientThreads to twice the value
# of the default servlet instance pool size.  If the container 
#implementation only maintains a single instance of a SingleTheadModel 
#servlet, leave the default value of 2.
#####################################################################
webServerHost=
webServerPort=
webServerHost.2=
webServerPort.2=
ServletClientThreads=2

####################################################################
#Transaction interoperability settings for Vendor J2EE EJB Server #1
#and Sun RI J2EE EJB Server #2.
####################################################################
EJBServer1TxInteropEnabled=true
EJBServer2TxInteropEnabled=true

###############################################################
# These properties are used for the Connector specific tests.
#
# You must be able to deploy the following resource adapters
# on your J2EE server.
###############################################################

# JNDI name bindings.  You should not change these.  The TSDeploymentInterface
# will pass these values as a properties object.  See the SunRIDeployment.java
# file for usage patterns.

whitebox-tx=java:comp/env/eis/whitebox-tx
whitebox-notx=java:comp/env/eis/whitebox-notx
whitebox-xa=java:comp/env/eis/whitebox-xa
whitebox-tx-param=java:comp/env/eis/whitebox-tx-param
whitebox-notx-param=java:comp/env/eis/whitebox-notx-param
whitebox-xa-param=java:comp/env/eis/whitebox-xa-param

# Resource adapter configuration values. You must modify these values
# to plug your JDBC driver into the TS whitebox resource adapter.
#
# When you run the XA tests (src/tests/xa), you must configure this
# resource adapter to use a database other than jdbc/DB1.
#
# connectionURL:  	The URL for your JDBC driver.  Should be the
#			same as the one used for the rest of the TS.
#
# serverName:		Potentially used for TSXADataSource implementation.
#
# portNumber:		Potentially used for TSXADataSource implementation
#
# xaDataSource:		The implementation class of TSXADataSource.  This class
#			is responsible for returning an XADataSource object, and
#			configuring it for use in the TS whitebox resource adapter.
#

# Cloudscape values.  serverName and portNumber are not needed by
# Cloudscape, but are needed by the JdbcManagedConnectionFactory
# so there is no runtime error.

connector_connectionURL=jdbc:cloudscape:rmi:CloudscapeDB;create=true
connector_serverName=brownie
connector_portNumber=19996
porting.ts.connector.xaDataSource.class=com.sun.ts.lib.implementation.cloudscape.CloudXADataSource

# Merant values

#connector_connectionURL=jdbc:sequelink://brownie:19996
#connector_serverName=brownie
#connector_portNumber=19996
#porting.ts.connector.xaDataSource.class=com.sun.ts.lib.implementation.merant.MerantXADataSource

###############################################################
#These properties are configurable and must specify valid usernames 
#and passwords to establish JDBC connections to a backend RDBMS. 
#Make sure that DBUSER and DBPASSWD in ts_env or ts_env.bat
#are also specified accordingly. Please refer to TS User Guide
#Appendix E3 for more information.
###############################################################
user1=cts1
password1=cts1
user2=cts2
password2=cts2

###############################################################
# These properties are used for the JDBC specific tests,
# they do not need to be changed.
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

#########################################################################
#The following properties must be set before running any security 
#related tests.  The properties user, password, authuser, authpassword, and
#nobodyuser must be set. 
#
#The value for user, password, authuser, and authpassword need to be set 
#exactly as they are set in the container/server.  
#
#The value for nobodyuser must be the same value returned by a call to 
#getCallerPrincipal().getName() from the ejb-tier when an unauthenticated 
#caller in the web tier invokes an ejb method.
#
#All above environmental property are 
#used for method permission testing; as well as security related API testing.
#########################################################################
user         = j2ee
password     = j2ee
authuser     = javajoe
authpassword = javajoe
user_vi      = j2ee_vi
password_vi  = j2ee_vi
user_ri      = j2ee_ri
password_ri  = j2ee_ri
nobodyuser   = guest

###############################################################
#securedWebServicePort must be set to run secbasicssl and csiv2 tests.
#Set this property with your Application server's secured webservice port
#
#securedWebServicePort.2 points to the secured webservice port in
#Sun's Reference Implementation(RI) Application Server.
#This port number is defined in ${J2EE_HOME}/config/web.properties
###############################################################
securedWebServicePort=
securedWebServicePort.2=7000

###############################################################
#This property must be set to run appclient security test
###############################################################
login=default

###############################################################
#These properties are needed for javamail tests.  
#mailuser1 must be set to a valid email address where test mails 
#will be sent to. 
#transport_protocol must be set to a valid protocol, i.e, smtp.
###############################################################
mailuser1=
transport_protocol=smtp
mailFrom=
mailHost=

###############################################################
#These properties are needed for signaturetest.
###############################################################
sigTestClasspath=${J2EE_HOME}$/lib$/j2ee.jar$:${JAVA_HOME}$/lib$/rt.jar
sigTestUrl=file:${TS_HOME}/src/com/sun/ts/tests/signaturetest/SigTestTS.html#SigTestTS
sigTestSetupUrl=file:${TS_HOME}/src/com/sun/ts/tests/signaturetest/StaticSigTSTest.html#StaticSigTSTest

###############################################################################
#This property is needed for jms tests.  
#
#jms_timeout - this is the amount of time in milliseconds that synchronous
#              receives will wait for a message
#
##############################################################################
jms_timeout=5000

###############################################################################
#This property is needed for EJB timer tests.
#
#ejb_timeout - this is the amount of time in milliseconds that duration will
#              be set for an ejbtimeout callback method
#
#ejb_wait - this is the amount of time in milliseconds that the client will
#              wait for ejbtimeout callback method results
#
##############################################################################
ejb_timeout=1000
ejb_wait=30000

###############################################################################
#These should be deprecated. DO NOT MODIFY THE FOLLOWING PROPERTIES. DOING SO 
#WILL CAUSE THE TESTS TO FAIL.
###############################################################################
java.naming.factory.initial=foo
org.omg.CORBA.ORBClass=foo
undeploy_redeploy_apps=${UNDEPLOY_REDEPLOY_FLAG}
