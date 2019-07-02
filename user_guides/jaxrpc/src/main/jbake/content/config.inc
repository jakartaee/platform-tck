///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
The following sections should be customized for the technology.
This text was originally from the JAX-RS TCK.  Most references
to JAX-RS have been parameterized to serve as a simple starting
point for customization.  There are still many details that will
need to be changed or removed.  The major sections 4.1, 4.2, and
4.3 should be preserved.  If their titles are changed, the links
at the top of config.adoc will need to be changed as well as well
as toc.adoc.
///////////////////////////////////////////////////////////////////////

[[GBFVU]][[configuring-your-environment-to-run-the-tck-against-the-reference-implementation]]

4.1 Configuring Your Environment to Run the TCK Against the Reference Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

After configuring your environment as described in this section,
continue with the instructions in link:#GBFUY[Section 4.3, "Using the
JavaTest Harness Software."]


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<TS_HOME>` becomes `$TS_HOME` on
Solaris/Linux and `%TS_HOME%` on Windows. In addition, the forward
slashes (`/`) used in all of the examples need to be replaced with
backslashes (`\`) for Windows. Finally, be sure to use the appropriate
separator for your operating system when specifying multiple path
entries (`;` on Windows, `:` on UNIX/Linux).

On Windows, you must escape any backslashes with an extra backslash in
path separators used in any of the following properties, or use forward
slashes as a path separator instead.

=======================================================================


1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  `JWSDP_HOME` to the directory in which the {TechnologyShortName}
  {TechnologyVersion} RI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<TS_HOME>/bin/build.properties` file and set the following
environment variables:
  a.  Set the `webserver.home` property to the directory in which the
  Java Web Services Developers Pack 1.3 software is installed. +
  By default this is `${env.JWSDP_HOME}`.
  b.  Set the `webserver.host` property to the host on
  which the Web server is running. +
  By default this is `localhost`.
  c.  Set the `webserver.port` property to the port on which the web Server is
  running.
  d.  Set the `j2ee.home.ri` property to the directory in which the J2SE software
  is installed, for reference to the packager tool used by the build infrastructure.
  You only need to set this property if you want to build the JAX-RPC TCK software.
  e.  Set the `jaxrpc.home` property to the path for Java Web Services Developers
  Pack, which contains the JAX-RPC mapping tool, is installed. +
  f.  Set the `jaxrpc.tool` property to the path where the {TechnologyShortName}
  {TechnologyVersion} `JavaToWSDL` and `WSDLToJava` mapping tool exists. +
  By default this is `<JWSDP_HOME>/jaxrpc/bin/wscompile.sh`.
  g.  Set the local_classes property to point to the {TechnologyShortName} RI
  classes/jars and the TCK classes/jars (tsharness.jar, javatest.jar, jaxrpctck.jar).
  For the Java Web Services Developers Pack (JWSDP 1.3) your `local_classes`
  property setting is:
+
[source,oac_no_warn]
----
local_classes=${env.JWSDP_HOME}/jaxp/lib/jaxp-api.jar:
${env.JWSDP_HOME}/jaxp/lib/endorsed/xercesImpl.jar:
${env.JWSDP_HOME}/jaxp/lib/endorsed/xsltc.jar:
${env.JWSDP_HOME}/jaxp/lib/endorsed/xalan.jar:
${env.JWSDP_HOME}/jaxp/lib/endorsed/sax.jar:
${env.JWSDP_HOME}/jaxp/lib/endorsed/dom.jar:
${env.JWSDP_HOME}/saaj/lib/saaj-impl.jar:
${env.JWSDP_HOME}/saaj/lib/saaj-api.jar:
${env.JWSDP_HOME}/jaxrpc/lib/jaxrpc-impl.jar:
${env.JWSDP_HOME}/jaxrpc/lib/jaxrpc-api.jar:
${env.JWSDP_HOME}/jaxrpc/lib/jaxrpc-spi.jar:
${env.JWSDP_HOME}/jwsdp-shared/lib/mail.jar:
${env.JWSDP_HOME}/jwsdp-shared/lib/activation.jar:
${env.JWSDP_HOME}/jwsdp-shared/lib/commons-logging.jar:
${env.JWSDP_HOME}/jwsdp-shared/lib/jax-qname.jar:
${env.JWSDP_HOME}/jwsdp-shared/lib/relaxngDatatype.jar:
${env.JWSDP_HOME}/jwsdp-shared/lib/xsdlib.jar:
${env.JWSDP_HOME}/jwsdp-shared/lib/jaas.jar:
${env.TS_HOME}/lib/javatest.jar:${env.TS_HOME}/lib/
tsharness.jar:
${env.TS_HOME}/lib/jaxrpctck.jar:${env.JWSDP_HOME}/common/
lib/servlet-api.jar
----
3.  Provide your own implementation of the porting package interface
provided with the {TechnologyShortName} TCK. +
  * The porting package interface, `TSURLInterface.java`, obtains URL
  strings for web resources in an implementation-specific manner.
  * `TSJAXRPCInterface.java` – Obtains RPC service endpoint URLs and WSDL
  service file location URLs in a vendor-specific manner using standard properties
  that map to the URL values supplied by the vendor and to obtain Service
  Implementation Classes in a vendor-specific manner. +
  API documentation for the `TSURLInterface.java` porting package interface is
  available in the {TechnologyShortName} TCK documentation bundle.
4.  Edit your `<TS_HOME>/bin/ts.jte` file and modify the following
environment settings:
  a.  Set the `webServerHost` property to the name of the host on which
  Java EE 8 RI is running. +
  The default setting is `localhost`.
  b.  Set the `webServerPort` property to the port number of the host on
  which Java EE 8 RI is running. +
  The default setting is `8080`.
  c.  Set the `secureWebServerPort` property to the secure SSL HTTP/1.1 port
  number of the host on which the web server is running.+
  The default setting is `8443`.
  d.  Set the `user` and `password` properties to the user and password used for
  the basic authentication tests. +
  The default setting is `j2ee` for both.
  e.  Set the `authuser` and `authpassword` properties to the user and password
  used for the basic authentication tests. +
  The default setting is `javajoe` for both.
  f.  Set the `porting.ts.url.class.1` property to your porting implementation
  class that is used for obtaining URLs. +
  The default setting for the Sun RI porting implementation is: +
  `com.sun.ts.lib.implementation.sun.common.SunRIURL`.
  g.  Set the porting.ts.jaxrpc.class.1 property to your porting
  implementation class that is used for obtaining service implementation
  classes, port information, and URLs for service endpoints and WSDL
  locations.+
  The default setting for the Sun RI porting implementation is: +
  `com.sun.ts.lib.implementation.sun.jaxrpc.SunRIJAXRPC`


[[GCLHU]][[configuring-your-environment-to-repackage-and-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Repackage and Run the TCK Against the Vendor Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

After configuring your environment as described in this section,
continue with the instructions in link:#GBFUY[Section 4.3, "Using the
JavaTest Harness Software."]


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<TS_HOME>` becomes `$TS_HOME` on
Solaris/Linux and `%TS_HOME%` on Windows. In addition, the forward
slashes (`/`) used in all of the examples need to be replaced with
backslashes (`\`) for Windows. Finally, be sure to use the appropriate
separator for your operating system when specifying multiple path
entries (`;` on Windows, `:` on UNIX/Linux).

On Windows, you must escape any backslashes with an extra backslash in
path separators used in any of the following properties, or use forward
slashes as a path separator instead.

=======================================================================


[[GCLJK]][[running-the-jaxrpc-tck-on-vendor-implementation]]

4.2.1 Running the {TechnologyShortName} TCK on a Vendor's Implementation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The {TechnologyShortName} {TechnologyVersion} specification does not
specify any normative model for packaging and deployment of service
endpoints on a servlet container-based {TechnologyShortName} runtime
system. A {TechnologyShortName} {TechnologyVersion} implementation is
allowed to have a vendorspecific deployment and packaging model for
servlet-based service endpoints.  For this reason, the
{TechnologyShortName} TCK provides two distribution directories, /dist
and /dist-vi, for the web service endpoint tests.

The /dist directory contains all the WAR files for the
{TechnologyShortName} TCK service endpoint tests that have been
compiled and packaged for deployment on a Servlet-compliant web
container using the standard Web Archive (WAR) format. These WAR files
are tailored for use with the {TechnologyShortName} {TechnologyVersion}
Reference Implementation. The web.xml file contains the deployment
information that the web container needs to link {TechnologyShortName}
service endpoints to its associated servlet class. One generic servlet
class, com.sun.xml.rpc.server.http.JAXRPCServlet , is used for all
{TechnologyShortName} service endpoints. These WAR files enable you to
run, without any additional setup or modification, the
{TechnologyShortName} TCK tests and to test the various features and
functionality of the {TechnologyShortName} {TechnologyVersion}
Reference Implementation.

The /dist-vi directory contains generic JAR files for all the TCK
service endpoint tests that you must use to package and deploy on your
{TechnologyShortName} {TechnologyVersion} implementation in a
vendor-specific way. For each of these web applications, you must use
your deployment tool to perform the appropriate Java-to-WSDL or
WSDL-to-Java mapping, based on whether the service endpoint is defined
by a Java Service Endpoint Interface or a WSDL service document. Next,
you must package all the generated artifacts needed along with the
service implementation classes for the service endpoints and deploy
them on your container-based implementation.

The following README files and TABLE show test breakdown and provide
information that should help you with this process

* README.JAVA-TO-WSDL
* README.WSDL-TO-JAVA
* Test Breakdown and Mapping Requirements

These README files contain the manual instructions that you need to
perform to implementation. The files also specify the mapping
(Java-to-WSDL or WSDL-to- deploy and package the service endpoint tests
on your {TechnologyShortName} {TechnologyVersion} Java) that each test
must perform. + For a brief example of how the {TechnologyShortName}
{TechnologyVersion} Reference Implementation does this, look at the
J2WMT.war web application in the the /dist directory.  This test
marshalls/unmarshalls all the {TechnologyShortName}-supported Java data
types as outlined in the {TechnologyShortName} {TechnologyVersion}
Specification using the Java-to-WSDL mapping. The web.xml file that is
packaged in this web application is as follows:
----
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://
java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
<display-name>J2WMarshallTestApp</display-name>
<listener>
<listenerclass>
com.sun.ts.tests.jaxrpc.common.JAXRPCContextListener</
listener-class>
</listener>
<servlet>
<servlet-name>XmlServletName</servlet-name>
<servlet-class>com.sun.ts.tests.jaxrpc.common.JAXRPCServlet</
servlet-class>
<load-on-startup>0</load-on-startup>
</servlet>
<servlet-mapping>
<servlet-name>XmlServletName</servlet-name>
<url-pattern>/jaxrpc/*</url-pattern>
</servlet-mapping>
<session-config>
<session-timeout>54</session-timeout>
</session-config>
</web-app>
----
As you can see, the {TechnologyShortName} service endpoint is linked to
a generic servlet in the {TechnologyShortName} RI
(com.sun.xml.rpc.server.http.JAXRPCServlet). This servlet is abstracted
out in {TechnologyShortName} TCK under common servlet
(com.sun.ts.tests.jaxrpc.common.JAXRPCServlet) which simply extends it.
This common servlet is used for processing, handling and dispatching to
all JAX-RPC service endpoints.  The {TechnologyShortName} TCK Service
Endpoint WAR files use a consistent naming convention for accessing all
Service Endpoint URLs and WSDL URLs: as follows.

* For Service Endpoint URLs, the URL mapping follows this convention:
http://webServerHost:webServerPort/ContextRoot/URLAlias/PortName +
The URL Alias used in all JAX-RPC TCK tests is jaxrpc.
* For WSDL URL locations, the URL mapping follows this convention:
http://webServerHost:webServerPort/ContextRoot/URLAlias?WSDL

The example that follows is one of the JAX-RPC TCK tests that shows this
mapping.

The following implementation-specific runtime configuration file,
jaxrpc-riruntime.  xml , is supplied as a web archive initialization
file to this servlet. This runtime configuration file is automatically
generated by the wsdeploy deployment tool, which gives information on
all service endpoints containing their interface, implementation and
tie classes, their service, port and WSDL document and their URL
pattern for accessing the endpoint.
----
<?xml version="1.0" encoding="UTF-8"?>
<endpoints xmlns=’http://java.sun.com/xml/ns/jax-rpc/ri/runtime’ version=’1.0’>
<endpoint
  name=’MarshallTest’
  interface=’com.sun.ts.tests.jaxrpc.ee.j2w.marshalltest.Marshall Test’
  implementation=’com.sun.ts.tests.jaxrpc.ee.j2w.marshalltest.MarshallTestImpl’
  tie=’com.sun.ts.tests.jaxrpc.ee.j2w.marshalltest.MarshallTest_Tie’
  wsdl=’/WEB-INF/MarshallTestService.wsdl’
  service=’{http://marshalltestservice.org/wsdl}MarshallTestService’
  port=’{http://marshalltestservice.org/wsdl}MarshallTestPort’
  urlpattern=’/jaxrpc/MarshallTest’/>
</endpoints>
----
The RPC service endpoint has one port defined in its runtime
configuration file.  The test client code accesses this
{TechnologyShortName} service endpoint and port at the following URL
location: +
http://webServerHost:webServerPort/J2WMT/jaxrpc/MarshallTest

where MarshallTest is the service port to access.

The above RPC service endpoint WSDL file describing this service is also
published. The test client code accesses this WSDL file based on the above
configuration via the following URL location: +
http://webServerHost:webServerPort/J2WMT/jaxrpc/MarshallTest?WSDL


[[GCLII]][[to-deploy-a-single-test-directory]]

4.2.2 Instructions for Interoperability Test Scenarios
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To test for interoperability between your implementation and the
{TechnologyShortName} Reference Implementation, we recommend the
following two test scenarios:

* J2WMT
* W2JMarshallTest

Complete the following steps to test interoperability between
{TechnologyShortName} implementations in both directions:

1.  Deploy and run the J2WMT and W2JMarshallTest TCK tests on your own
{TechnologyShortName} {TechnologyVersion} implementation and perform
the appropriate mappings (WSDL-to-Java) and (Java-to-WSDL)
2.  Deploy the {TechnologyShortName} RI WAR files (J2WMT.war) and
(W2JMarshallTest.war) on a remote host system using the
{TechnologyShortName} {TechnologyVersion} Reference Implementation.
3.  On your local host system that is running your own
{TechnologyShortName} implementation, edit the ts.jte file to change
the Web server host and port to point to this remote system running the
{TechnologyShortName} RI with the J2WMT.war and W2JMarshallTest.war WAR
files deployed.
4.  Run the client code for these tests against the remote system to
test the marshalling/unmarshalling of data from your
{TechnologyShortName} implementaton to the {TechnologyShortName} RI
implementation system and back.
5.  Perform the same procedure going in the opposite direction, running
the client on the {TechnologyShortName} RI and accessing the Service
Endpoint deployed on your {TechnologyShortName} implementation.
