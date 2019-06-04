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
  c.  Set the `webServerHome` property to the installation directory of Java EE
  8 RI.
  d.  `web.deployment.handler` to an Ant file that contains at least a
  single target named `deploy`. +
  When `ant deploy` is invoked, this target will be invoked to handle the
  deployment of the web applications to the target server. To use a custom
  handler, copy the handler to the `<TS_HOME>/bin/deploy` directory and
  provide only the filename as the property value. +
  The JSP TCK includes the following handlers:
  * `noop`: No deployment; default setting
  * `glassfish`: Deploy to the Java EE 8 server
  * `jwsdp`: Deploy to a server running the Java Web Services Developers
  Pack or to a Tomcat server +
  See link:#GCRVD[Section 4.5, "Custom Deployment Handlers,"] for
  information about creating your own deployment handler and setting the
  `web.deployment.handler` property to use that handler.
  e.  `jspservlet.classes` to the path to the Servlet and JSP API classes
  that are used for building. +
  The `ts.jte` file includes examples that show how to set the classes
  properties for GlassFish 4 servers.
  f.  `jstl.classes` to the path to the JavaServer Pages Standard Tag
  Library (JSTL) JAR files.
  g.  `report.dir` to the default directory in which JavaTest creates a
  test report for the most recent test run. +
  This property is a required property for the TCK Ant targets; it must be
  set. To disable reporting, set the `report.dir` property to `"none"`.
  h.  `work.dir` to the default directory in which JavaTest writes
  temporary files that are created during test execution. +
  This property is required for the TCK Ant targets.
  i.  When the signature tests are run, the `sigTestClasspath` property
  must be set to a list of JAR files and directories which contain the
  Java EE and Java SE classes. +
  Paths must be separated by the appropriate path separator ("`;`" on
  Windows, "`:`" on Solaris/Linux).
  j. `porting.ts.url.class.1` to the class names of your porting class
  implementations. +
  By default, the property settings point to the JSP TCK RI-specific
  classes.
+
3.  Provide your own implementation of the porting package interface
provided with the JSP TCK. +
The porting package interface, `TSURLInterface.java`, obtains URL
strings for web resources in an implementation-specific manner. API
documentation for the `TSURLInterface.java` porting package interface is
available in the JSP TCK documentation bundle.


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


[[GCFQK]][[preparing-the-war-files-for-deployment]]

4.3 Preparing the WAR Files for Deployment
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Complete this step only if you intend to package a JSP implementation in
the WAR files, thereby overriding the default JSP implementation for the
application server under test. This procedure adds to and rebuilds the
JSP TCK WAR files.

1.  Copy one or more of the JAR files required by the JSP implementation
that will replace the application server's default JSP implementation to
the `<TS_HOME>/weblib` directory. +
This JAR file (or files) must contain all of the TLDs (located in the
`META-INF` directory of the archive) for the tag libraries, as defined
by the JSP specification and any additional support JAR files upon which
the JSP implementation is dependent.
2.  Change to the `<TS_HOME>/src/com/sun/ts/tests` directory and run
`ant build` to package the web archives. +
This adds one or more JAR files from the `<TS_HOME>/weblib` directory to
each of the WAR files.
