///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
The following sections should be customized for the technology.
This text was originally from the Jakarta RESTful Web Services TCK.  Most references
to Jakarta RESTful Web Services have been parameterized to serve as a simple starting
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
continue with the instructions in link:#GBFUY[Section 4.6, "Using the
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

[[GBFWN]][[TCAUT00012]][[to-configure-your-environment-for-the-jaspic-tck]]

4.1.2 To Configure Your Environment for the Jakarta Authentication TCK
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This section describes how to configure your environment to run the
{TechnologyShortName} TCK tests.

Deploy the {TechnologyShortName} TCK tests in the manner that your implementation
requires, based on the type of profile.

If your implementation is Jakart EE based, set the `platform.mode`
property in the `ts.jte` file to `jakartaEE`.

If your implementation is not Jakarta EE based, set the `platform.mode`
property in the `ts.jte` file to `standalone`.


1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `ANT_HOME/bin`
2.  Copy <TS_HOME>/bin/ts.jte.jdk11 as <TS_HOME>/bin/ts.jte if JAVA_HOME is Java SE 11.
Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  `pathsep` to the type of path separator used by your operating
  system +
  The default is `:` for Solaris/Linux. Windows users should change this
  value to `;`.
  b.  Set the `jaspic.home` property to the root directory of
  implementation under test.
  c.  Set the `orb.host` property to the name of the machine on which you
  are running the {TechnologyShortName} TCK tests.
  d.  Set the `orb.port` property to the port number of the machine on
  which you are running the {TechnologyShortName} TCK tests.
  e.  Set the `sigTestClasspath` property to point to the implementation
  classes that are to be validated for signature compliance. This
  classpath must also include any other classes that are referenced,
  implemented, or extended by your implementation .
  f.  Set the `servlet.is.jsr115.compatible` property based on whether or
  not you are running the Servlet profile in a Jakarta Authorization 2.0 compatible
  container.
  g.  Set the `soap.is.jsr115.compatible` property based on whether or not
  you are running the SOAP profile in a Jakarta Authorization 2.0 compatible container.
  h.  Set the `log.file.location` property to the location where your
  implementation's log files and the {TechnologyShortName} log file will be written.
  i.  Set the `logical.hostname.servlet` property to the logical host that
  will process Servlet requests. +
  Servlet requests may be directed to a logical host using various
  physical or virtual host names or addresses. A message processing
  runtime may be composed of multiple logical hosts. This setting is
  required to properly identify the Servlet profile's application context
  identifier hostname. If the logical host that will process Servlet
  requests does not exist, you can set this to the default hostname of
  your implementation's Web server.
  j. Set the `logical.hostname.soap` property to the name of the logical
  host that will process SOAP requests. +
  This hostname is used in the implementation runtime's application
  context identifier in the SOAP profile.
  k. Set the `vendor.authconfig.factory` property to specify your
  `AuthConfigFactory` class. +
  This property setting will be used by the {TechnologyShortName} tests to register the
  test suite's provider in your `AuthConfigFactory`.
3.  Run `ant config.vi`. +
This task configures the implementation under test to run the {TechnologyShortName} TCK
tests by doing the following:
  a.  Copies `jaspic.jar` and `tsharness.jar` to the lib extension
  directory (for example, `/glassfish/domains/domain1/lib/ext`)
  b.  Set up users and passwords for your implementation. +
  For the purpose of running the CTS test suite, these should be set as
  follows:
+
--
[width="100%",cols="33%,33%,34%",options="header",]
|=========================================
|User |Password |Groups
|`j2ee_vi` |`j2ee_vi` |`staff`
|`javajoe` |`javajoe` |`guest`
|`j2ee` |`j2ee` |`staff`, `mgr`, `asadmin`
|=========================================
Also make sure the principal to role-mappings that are specified in the
runtime XML files are properly mapped in your environment. Note that the
principal-to-role mappings may vary for each application.
--
+
  c.  Install the client-side certificate in the `trustStore` in your
  implementation. +
  Certificates are located `<TS_HOME>/bin/certificates`. +
  Use the certificate that suits your environment:
  * `cts_cert` - For importing the CTS client certificate into a
  `truststore`
  * `clientcert.jks` - Used by the J2SE runtime to identify the CTS
  client's identity
  * `clientcert.p12` – Contains CTS client certificate in `pkcs12` format
  d.  Append the file `<TS_HOME>/bin/server_policy.append` to the Java
  policy file or files on your implementation. +
  This file contains the grant statements used by the test harness,
  signature tests, and API tests.
  e.  Appends the file `<TS_HOME>/bin/client_policy.append` to the
  application client's Java policy file, which is referenced in the
  `TestExecuteAppClient` section of the `ts.jte` file.
  f.  Creates a JVM option that increases the MaxPermSize for your
  implementation.
+
4.  Run `ant enable.jaspic`. +
This task performs the configuration necessary for adding the test
suite's `SPI Verifier(TSSV)` to your implementation. Specifically,
`ant enable.jaspic` performs the following operations:
  a.  Sets the `jvm` option `-Dlog.file.location` in your implementation. +
  This is the location of the log file where the Test Suite SPI Verifier
  (TSSV) creates log messages, which will be used by the {TechnologyShortName} TCK tests,
  to identify the test status.
  b.  Sets the `jvm` option `-Dprovider.configuration.file` in your
  implementation. +
  This option is used to identify the provider configuration file that
  will be used by `TSAuthConfigFactory` to load the providers required by
  the {TechnologyShortName} TCK tests.
  c.  Sets the `jvm` option
  `-Dschema.file.location=${schema.file.location}` in your implementation. +
  This option is used to identify the location of the schema file that is
  used by the `Provider-Configuration.xml` file.
  d.  Sets up your implementation to use the test suite's
  `AuthConfigFactory`. +
  This can be done in one of the following ways:
  * Copy `<TS_HOME>/bin/ts.java.security` to the location in your
  implementation where the security configuration files reside. For
  example, the Eclipse GlassFish Server security configuration files are in the
  `<JAVAEE_HOME>glassfish/domains/domain1/config` directory. After the
  file has been copied, use the `-Djava.security.properties` JVM option to
  direct your implementation to use this security property file.  For
  example, to direct Eclipse GlassFish Server to use the `ts.java.security` file,
  you would use this JVM option:
+
[source,oac_no_warn]
----
-Djava.security.properties=glassfish/domains/domain1/config/ts.java.security
----
+
  * Add the following lines as a single line to the
  `JAVA_HOME/jre/lib/security/java.security` file:
+
--
[source,oac_no_warn]
----
authconfigprovider.factory=
com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory
----
Adding this property to the `java.security` file forces your
implementation to load the test suite's `AuthConfigFactory`.
--
+
  e.  Copies the `TS_HOME/lib/tssv.jar` file to your implementation
  instance library directory. +
  The `tssv.jar` file includes the class files necessary to load
  `TSAuthConfigFactory` and related classes.
  f.  Copies the TSSV configuration files (`ProviderConfiguration.xml`,
  `provider-configuration.xsd`) to your implementation instance library
  directory.
  g.  Deploys the Jakarta Authentication file processor,
  `com/sun/ts/tests/jaspic/util/jaspic_util_web.war`.
+
5.  If necessary, provide your own implementations of the porting
package interface provided with the Jakarta Authentication TCK. +
`TSURLInterface.java` obtains URL strings for web resources in an
implementation-specific manner. API documentation for the
`TSURLInterface.java` porting package interface is available in the
documentation bundle in the `docs/api` directory.


[[GCLHU]][[configuring-your-environment-to-repackage-and-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Repackage and Run the TCK Against the Vendor Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

After configuring your environment as described in this section,
continue with the instructions in link:#GBFUY[Section 4.4, "Using the
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

With the {TechnologyShortName} TCK, vendors can specify the level of {TechnologyShortName} support
with which they comply. For example, a vendor may be compliant with the
Servlet Profile, the SOAP Profile, or another (possibly unknown)
profile. If a vendor chooses not to pursue compliance with any profile,
they have an option of meeting something called baseline compliance.
This is the level of compliance that exists regardless of which profile
is being tested.

When a vendor is vying for compliance against no profile and is trying
to get baseline compliance certification only, they have to implement a
porting package (for example, a customvehicle) and pass the baseline
tests that are in the `TS_HOME/src/com/sun/ts/tests/jaspic/spi/baseline`
directory.

The sections that follow explain how to create a custom vehicle and how
to replace the default vehicle with a custom vehicle.

[[CBDCAIEE]][[TCAUT117]][[to-create-a-custom-vehicle]]

4.2.1 To Create a Custom Vehicle
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

A custom vehicle must be created and used when {TechnologyShortName} profile tests are
run in an environment that does not contain a Web server. If your {TechnologyShortName}
profile implementation includes a Web server, you do not need to
implement your own custom vehicle.

The custom vehicle exists, in stubbed out form, and must be implemented
in a way that provides a wrapper in which {TechnologyShortName} tests can execute. The
default `jaspicservlet` vehicle is an example of a vehicle that wraps
and executes tests in a Servlet container. The `jaspicservlet` vehicle
source can be used a reference to help you implement your own custom
vehicle. The `jaspicservlet` vehicle is in the
`src/com/sun/ts/tests/common/vehicle/jaspicservlet` directory.

1.  Use the stubbed-out `customvehicle` in the
`src/com/sun/ts/tests/common/vehicle/customvehicle` directory as your
starting point.
2.  Modify the `CustomVehicleRunner` class, using other vehicles as
references. +
The `bin/xml/ts.vehicles.xml` file includes a stubbed-out section for
the `customvehicle`, which you can modify to build you own
`customvehicle`.
3.  Build the `customvehicle` you created.
4.  Modify the `src/vehicle.properties` file so that it refers to
`customvehicle` instead of `jaspicservlet`. +
The `vehicle.properties` file is used during runtime to indicate in
which vehicle the tests should be executed.
5.  Remove or rename the `src/testsuite.jtd` file. +
This allows the test harness to identify tests to be run in your
`customvehicle`.

[[TCAUT118]][[sthref10]]


[[to-replace-the-default-vehicle-with-a-custom-vehicle]]
4.2.2 To Replace the Default Vehicle With a Custom Vehicle
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If your {TechnologyShortName} server does not have web support, you will need to create
your own vehicle. A vehicle is a wrapper that supports running tests in
different server-side containers, such as servlet, JSP, and so on. The
{TechnologyShortName} TCK provides a default vehicle, `jaspicservlet`, which supports
running the TCK tests in a {TechnologyShortName} runtime that has a Servlet container.
To support running tests in an environment other than a Servlet
container, you need to implement your own vehicle, effectively replacing
the default vehicle, `jaspicservlet`.

This TCK was designed so you could use `jaspicservlet` as a template for
creating your own vehicle. The `jaspicservlet` vehicle is used to
contain and execute your client-side tests in the connector runtime.

The `jaspicservlet` vehicle is located in the
`<TS_HOME>/src/com/sun/ts/tests/common/vehicle/jaspicservlet` directory.

To run the tests in a vehicle other than `jaspicservlet`, you need to
create a custom vehicle named `customvehicle`. See
link:#CBDCAIEE[Section 4.2.1, "To Create a Custom Vehicle,"] for more
information on this topic.



[[GHGDG]][[publishing-the-test-applications]]

4.3 Publishing the Test Applications
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Not needed for the {TechnologyShortName} TCK.
