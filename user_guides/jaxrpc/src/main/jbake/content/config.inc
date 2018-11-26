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


[[sthref9]]

Before You Begin
~~~~~~~~~~~~~~~~~

Decide against which {TechnologyShortName} implementation the tests
will be run and determine to which Servlet–compliant Web server the
{TechnologyShortName} TCK applications will be published.

Package the {TechnologyShortName} test applications for that
{TechnologyShortName} implementation and Servlet–compliant Web
server.

See link:rebuild.html#GCLIZ[Appendix B, "Packaging the Test
Applications in Servlet-Compliant WAR Files With VI-Specific
Information,"] for information about repackaging the
{TechnologyShortName} test application.

Deploying the {TechnologyShortName} TCK Tests
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1.  Change to the `<TS_HOME>/dist` directory.
2.  For the Java Web Services Developers Pack, copy all of the WAR files to the
`<JWSDP_HOME>/webapps` directory: +
    `cp *.war <JWSDP_HOME>/webapps`

Complete the following two procedures to configure your Web server environment
for Java Web Services Developers Pack.

Configuring the Java Web Server
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  `JWSDP_HOME` to the directory in which the {TechnologyShortName}
  {TechnologyVersion} RI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<JWSDP_HOME>/conf/tomcat-users.xml` file and add the following
  users:
  `<user name="j2ee" password="j2ee"`
  `roles="Administrator,Employee"/>`
  `<user name="javajoe" password="javajoe"`
  `roles="Manager,Employee"/>`
3.  Configure the Web server for use with SSL and HTTPS. +
Some tests in TCK do secure testing using SSL over HTTPS. See the `README.SSLHTTPS-
SETUP` file for configuration and setup instructions.
4. Copy the `<TS_HOME>/lib/tsharness.jar` file to the `<JWSDP_HOME>/common/
lib` directory.

Starting the Java Web Server
~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Execute the `<JWSDP_HOME>/bin/startup.sh` script or the `<JWSDP_HOME>/
bin/startup.bat` batch file. +
Wait for the web server to come up. Look at the file `<JWSDP_HOME>/logs/
launcher.server.log` and wait until the messages stop printing to the file.
After a series of startup and initialization messages, You will see the following
message when the web server has completed its startup: +
`[INFO] Http11Protocol - -Starting Coyote HTTP/1.1 on port 8080`
`[INFO] Catalina - -Server startup in 126511 ms`
