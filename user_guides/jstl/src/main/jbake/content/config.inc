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


1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  +{TechnologyHomeEnv}+ to the directory in which the {TechnologyShortName}
  {TechnologyVersion} RI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `webServerHost` property to the name of the host on which
  Java EE 8 RI is running. +
  The default setting is `localhost`.
  b.  Set the `webServerPort` property to the port number of the host on
  which Java EE 8 RI is running. +
  The default setting is `8080`.
  c.  `impl.vi` to the Vendor implementation.
  d.  `impl.vi.deploy.dir` to the Vendor implementation deployment
  directory. +
  The JSTL TCK includes the following handlers:
  * `none`: No deployment; default setting
  * `glassfish`: Deploy to the Java EE 8 server
  * `tomcat`: Deploy to the Tomcat server +
  See link:#GCRVD[Section 4.4, "Custom Deployment Handlers,"] for
  information about creating your own deployment handler and setting the
  `impl.vi.deploy.dir` property to use that handler.
  e.  `jsf.classes` to the Standard Tag Library for JavaServer Pages API
  and implementation classes that are under test
  f.  `jspservlet.classes` to the path to the Servlet and JSP API classes. +
  The `ts.jte` file includes examples that show how to set the classes
  properties for both GlassFish v2 and GlassFish v3 servers.
  g.  `report.dir` to the default directory in which JavaTest creates a
  test report for the most recent test run. +
  This property is a required property for the TCK Ant targets; it must be
  set. To disable reporting, set the `report.dir` property to `"none"`.
  h.  `work.dir` to the default directory in which JavaTest writes
  temporary files that are created during test execution. +
  This property is required for the TCK Ant targets.
+
3.  Provide your own implementation of the porting package interface
provided with the {TechnologyShortName} TCK. +
The porting package interface, `TSURLInterface.java`, obtains URL
strings for web resources in an implementation-specific manner. API
documentation for the `TSURLInterface.java` porting package interface is
available in the {TechnologyShortName} TCK documentation bundle.

4.1.1 Initializing Database and Packaging the War Files for Deployment
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The Ant target `init.<dbname>` configures the database for running the
test. The target can be run from `<TS_HOME>/bin`.

For example, change to the <TS_HOME>/bin directory

<TS_HOME>/tools/ant/bin/ant init.javadb

This adds the database users and run db statements that are necessary
for the tests to run successfully.

In addition to database configuration `init.<dbname>` builds all the
test classes and packages the war files to `<TS_HOME>/dist/*` which is
deployed later.

4.1.2 Preparing the WAR Files for Deployment
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Complete this step only if you intend to package a JSTL implementation
in the WAR files, thereby overriding the default JSTL implementation for
the application server under test. This procedure adds to and rebuilds
the JSTL TCK WAR files.

1.  Copy the JAR file or files required by the JSTL implementation that
will replace the application server's default JSTL implementation to the
`<TS_HOME>/weblib` directory. +
This JAR file (or files) must contain all of the TLDs (located in the
`META-INF` directory of the archive) for the tag libraries, as defined
by the JSTL specification and any additional support JAR files upon
which the JSTL implementation is dependent.
2.  Change to the `<TS_HOME>/src/com/sun/ts/tests` directory and run
`<TS_HOME>/tools/ant/bin/ant build` to package the web archives. +
This adds the JAR file or files from the `<TS_HOME>/weblib` directory to
each of the WAR files.


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


[[sthref9]]

Before You Begin

Decide against which {TechnologyShortName} implementation the tests
will be run and determine to which Servlet–compliant Web server the
{TechnologyShortName} TCK applications will be published.

Adapt the instructions above for the vendor implementation.


[[GCRWX]][[deploying-the-jstl-tck-tests]]

4.3 Deploying the JSTL TCK Tests
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To deploy the JSTL TCK tests to the Java EE 7 platform, perform the
following steps.

1.  Make sure that the Web server to which you will deploy the JSTL TCK
tests is running.
  a.  Start the database.
  b.  Start the web server domain. +
  For example, we can use the `asadmin` to start/stop database and domain. +
  ./asadmin start-database +
  ./asadmin start-domain
  c.  In addition, with Java SE 8 security changes, it is necessary to set
  the following JVM properties on the domain in order to ensure all tests
  pass. +
  -Djavax.xml.accessExternalStylesheet=all +
  -Djavax.xml.accessExternalSchema=all +
  -Djavax.xml.accessExternalDTD=file,http +
  These properties need to exist and be set within the appserver domain if
  security manager is enabled. For example, we can use `asadmin` to
  set/unset jvm options followed by domain restart. +
  ./asadmin create-jvm-options -Djavax.xml.accessExternalStylesheet=all +
  ./asadmin create-jvm-options -Djavax.xml.accessExternalSchema=all +
  ./asadmin create-jvm-options -Djavax.xml.accessExternalDTD=file,http +
  ./asadmin restart-domain
+
2.  Change to the `<TS_HOME>/bin` directory.
3.  Execute the `<TS_HOME>/tools/ant/bin/ant deploy.all` command. +
All of the WAR files containing the JSTL TCK tests have been deployed.
Follow the instructions in link:using.html#GBFUZ[Section 5.1, "Using the
GUI for TCK Test Execution,"] or link:using.html#GJCIW[Section 5.2,
"Using the Command Line for TCK Test Execution,"] to run the tests you
just deployed.
