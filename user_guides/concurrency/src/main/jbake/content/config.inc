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

[[GBFVU]][[configuring-your-environment-to-run-tck-with-the-reference-implementation]]

4.1 Configuring Your Environment to Run the TCK With a Compatible Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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


1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `webServerHost` property to the name of the host on which
  Jakarta EE 9 CI is running. +
  The default setting is `localhost`.
  b.  Set the `webServerPort` property to the port number of the host on
  which Jakarta EE 9 CI is running. +
  The default setting is `8080`.
  c.  Set the `concurrency.classes` property to point to the {TechnologyRI} CI
  classes/JAR files. 
  If you are using the Jakarta EE 9 CI, {TechnologyRI}, set this property to the following: +
  `concurrency.classes=${glassfish.ri.classes}`
  d.  Set the `webcontainer.home` property to the installation directory of Jakarta EE
  9 CI.

  e. Set the `impl.vi`, `impl.vi.deploy.dir`, `impl.vi.host`, and `impl.vi.port`
  properties to the supported web container, deploy directory, host and port for
  the {TechnologyShortName} CI.
  The supported web container for the {TechnologyShortName} CI is the Jakarta EE 9 CI. The
  default settings for these properties are as follows: +
+
[source,oac_no_warn]
----
impl.vi.deploy.dir=${webcontainer.home}/domains/domain1/autodeploy
impl.vi=glassfish
impl.vi.host=${webServerHost}
impl.vi.port=${webServerPort}
----

  f. Set the `porting.ts.url.class.1` property to your porting
  implementation class that is used for obtaining URLs. +
  The default setting for this property is
  `com.sun.ts.lib.implementation.sun.common.SunRIURL`.

3.  Run the `ant config.vi` target to configure the Vendor
Implementation that is defined in the `impl.vi` property.
+
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant config.vi
----
+
This target performs the following tasks:
* Stops the application server running the {TechnologyShortName} {TechnologyVersion} CI
* Copies the TCK-dependent files `${tslib.name}.jar` and `tsharness.jar`
into the application server's external library folder
* Starts the application server
* Creates users and the appropriate roles
* Enables HTTP trace requests

4. Deploy the {TechnologyShortName} TCK tests to the Web server:
  * Change to the `<TS_HOME>/bin` directory.
  * Execute the ant `deploy.all` command. +
    This target invokes the `<TS_HOME>/bin/xml/impl/glassfish/deploy.xml`
    script, which deploys the {TechnologyShortName} TCK {TechnologyVersion} WAR files to the
    implementation server under test.
    Follow the instructions in link:using.html#GBFWO[5 Executing Tests] to run
    the tests you just deployed.

[[GCLHU]][[configuring-your-environment-to-repackage-and-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Run the TCK With Your Concurreny Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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


[[sthref9]]

Before You Begin

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

1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  +{TechnologyHomeEnv}+ to the directory in which the
  {TechnologyShortName} {TechnologyVersion} VI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `webServerHost` property to the name of the host on which
  your Web server is running that is configured with the Vendor
  Implementation. +
  The default setting is `localhost`.
  b.  Set the `webServerPort` property to the port number of the host on
  which the Web server is running that is configured with the Vendor
  {TechnologyShortName} {TechnologyVersion} Vendor Implementation. +
  The default setting is `8080`.
  c.  Set the `webcontainer.home` property to the installation directory of the Web
  server.
  d.  Set the `concurrency.classes` property to point to the classes or JAR file
  for the {TechnologyShortName} {TechnologyVersion} API. +
  e.  Set the `report.dir` to the default directory in which JavaTest creates a
  test report for the most recent test run.
  This property is a required property for the TCK Ant targets; it must be set. To
  disable reporting, set the `report.dir` property to `none`.
  f.  `work.dir` to the default directory in which JavaTest writes temporary files that
  are created during test execution.
  This property is required for the TCK Ant targets.
  g.  Set the `impl.vi` property to the vendor implementation.
  This property will be used for the directory name in which the `deploy.xml`
  and the `config.vi.xml` files will reside. See link:config.html#GHGDG[4.3 "Custom Configuration
  Handlers]," and Section 4.4.1, "To Create a Custom Deployment Handler," for
  more information.
  h.  Set the `impl.vi.deploy.dir` property to point to the `autodeploy`
  directory for the Web server.
+
3.  Provide your own implementation of the porting package interface
provided with the {TechnologyShortName} TCK. +
The porting package interface, `TSURLInterface.java`, obtains URL
strings for web resources in an implementation-specific manner. API
documentation for the `TSURLInterface.java` porting package interface is
available in the {TechnologyShortName} TCK documentation bundle.
4.  Edit your `<TS_HOME>/bin/ts.jte` file and set the porting.ts.url.class.1
property to point to your porting implementation class used for obtaining URLs.
The default setting for the CI porting implementation is
`com.sun.ts.lib.implementation.sun.common.SunRIURL.`
5.  Execute the `config.vi` command to configure your implementation for the
Concurrency TCK:
  * Change to the `<TS_HOME>/bin` directory.
  * Execute the `ant config.vi` command. +
This target invokes the 
`<TS_HOME>/bin/xml/impl/<your-implementation>/config.vi.xml` script, 
which configures the {TechnologyShortName} {TechnologyVersion} implementation server under test (the 
server that was designated by the `impl.vi` setting in the `ts.jte` file). If you are using the 
Jakarta EE 9 CI, {TechnologyRI} with the `impl.vi` property set to `glassfish`, this target copies 
required TCK JAR files to the server and then starts the server. +
 +
You can configure your implementation server manually or automatically. If 
you choose to do this automatically, see link:config.html#GHGDG[4.3 "Custom Configuration 
Handlers"] to create your own configuration handler.
6.  Deploy the Concurrency TCK tests to the Web server:
    * Change to the `<TS_HOME>/bin` directory.
    * Execute the `ant deploy.all` command. +
This target invokes the
`<TS_HOME>/bin/xml/impl/<your-implementation>/deploy.xml` script,
which deploys the {TechnologyShortName} {TechnologyVersion} WAR files to the implementation server
under test.

You can deploy the TCK WAR files manually or automatically. If you choose
to do this automatically, see Section 4.4.1, "To Create a Custom Deployment
Handler," to create your own custom deployment handler.

Follow the instructions in link:using.html#GBFWO[5 Executing Tests]
to run the tests you just deployed.

[[GHGDG]][[publishing-the-test-applications]]

4.3 Custom Configuration Handlers
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Configuration handlers are used to configure and unconfigure a {TechnologyShortName}
{TechnologyVersion} implementation during the certification process.
These are similar to deployment handlers but used for configuration.
A configuration handler is an Ant build file that contains at least the required targets
listed below:
  * `config.vi` - to configure the vendor implementation
  * `clean.vi` - to unconfigure the vendor implementation

These targets are called from the `<TS_HOME>/bin/build.xml` file and call down into the
implementation-specific configuration handlers.

To provide your own configuration handler, create a config.vi.xml file with the
necessary configuration steps for your implementation and place the file under the
`<TS_HOME>/bin/xml/impl/<your_impl>` directory.

For more information, see `<TS_HOME>/bin/xml/impl/glassfish/config.vi.xml`, the
configuration file for a Jakarta EE 9 CI, {TechnologyRI}.
