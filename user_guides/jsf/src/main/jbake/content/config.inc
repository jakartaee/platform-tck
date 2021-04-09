[[GBFVU]][[configuring-your-environment-to-run-the-tck-against-the-compatible-implementation]]

4.1 Configuring Your Environment to Run the TCK Against the Compatible Implementation (CI)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

After configuring your environment as described in this section,
continue with the instructions in link:#GBFUY[Section 4.6, "Using the
JavaTest Harness Software."]


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<TS_HOME>` becomes `$TS_HOME` on
UNIX/Linux and `%TS_HOME%` on Windows. In addition, the forward
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
  {TechnologyVersion} CI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<ANT_HOME>/bin`
2.  Copy <TS_HOME>/bin/ts.jte.jdk11 as <TS_HOME>/bin/ts.jte if JAVA_HOME is Java SE 11.
Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `webServerHost` property to the name of the host on which
  Jakarta EE {JakartaEEVersion} CI is running. +
  The default setting is `localhost`.
  b.  Set the `webServerPort` property to the port number of the host on
  which Jakarta EE {JakartaEEVersion} CI is running. +
  The default setting is `8080`.
  c. `impl.vi` to the Vendor implementation, which is "glassfish".
  d. `impl.vi.deploy.dir` to the Vendor implementation deployment directory,
  which for the Jakarta EE {JakartaEEVersion} CI would be
  +{TechnologyHomeEnv}/domains/domain1/autodeploy+.
  e. `jsf.classes` to the Jakarta Server Faces API and implementation classes
  that are under test
  f. `jspservlet.classes` to the path to the Servlet and JSP API classes. +
  The ts.jte file includes examples that show how to set the classes properties
  for Eclipse GlassFish servers.
  g. `report.dir` to the default directory in which JavaTest creates a test
  report for the most recent test run. +
  This property is a required property for the TCK Ant targets; it must
  be set. To disable reporting, set the `report.dir` property to "none".
  h. `work.dir` to the default directory in which JavaTest writes
  temporary files that are created during test execution. +
  This property is required for the TCK Ant targets.
3. Provide your own implementation of the porting package interface
provided with the Jakarta Server Faces TCK. +
The porting package interface, `TSURLInterface.java`, obtains URL
strings for web resources in an implementation-specific manner. API
documentation for the `TSURLInterface.java` porting package interface
is available in the Jakarta Server Faces TCK documentation bundle.


[[GCLHU]][[configuring-your-environment-to-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Run the TCK Against the Vendor Implementation (VI)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Follow the instructions above, customized as necessary for the
Vendor implementation.
A custom deployment handler will need to be created for the
Vendor implementation.


[[GHGDG]][[preparing-the-war-files-for-deployment]]

4.3 Preparing the WAR Files for Deployment
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Complete this step only if you intend to package a Jakarta Server Faces
implementation in the WAR files, thereby overriding the default Jakarta
Server Faces implementation for the application server under test. This
procedure adds to and rebuilds the Jakarta Server Faces TCK WAR files.

1. Copy one or more of the JAR files required by the Jakarta Server
Faces implementation that will replace the application server's default
Jakarta Server Faces implementation to the `<TS_HOME>/weblib` directory. +
This JAR file (or files) must contain all of the TLDs (located in the
`META-INF` directory of the archive) for the tag libraries, as defined
by the Jakarta Server Faces specification and any additional support
JAR files upon which the Jakarta Server Faces implementation is
dependent.
2.  Change to the `<TS_HOME>/src/com/sun/ts/tests` directory and run
`ant build` to package the web archives. +
This adds one or more JAR files from the `<TS_HOME>/weblib` directory
to each of the WAR files.

