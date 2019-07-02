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
  a.  If you are testing an implementation within a Web container, set the
  `webServerHost` property to the name of the host on which your Web
  server is running that is configured with JTA 1.3 Reference
  Implementation. +
  The default setting is `localhost`.
  b.  If you are testing an implementation within a Web container, set the
  `webServerPort` property to the port number of the host on which the Web
  server is running that is configured with JTA 1.3 Reference
  Implementation. +
  The default setting is `8000`.
  c.  Set the webServerHome property to the directory where you installed
  JTA 1.3 Reference Implementation.
  d.  Set the jta.classes property to point to the classes or JAR file
  that contains the JTA classes.
  e.  If you are using a deployment handler XML file to deploy the tests
  to a Java Servlet or JavaServer Pages container, set the
  `web.deployment.handler` property to point to your deployment handler
  XML file in the `<TS_HOME>/bin/deploy` directory.Make sure that you
  implement the `—deploy` and `—deploy.all` targets in this file.
  f.  To copy the `tsharness.jar` and `jtatck.jar` files to the server's
  extension directory, change to the `<TS_HOME>/bin` directory and execute
  the following command: +
  `<TS_HOME>/tools/ant/bin/ant config.vi`
  g.  To deploy the JTATCKWAR files, change to the <TS_HOME>/bin directory
  and execute the following command: +
  `<TS_HOME>/tools/ant/bin/ant deploy.all`

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
  a.  If you are testing an implementation within a Web container, set the
  `webServerHost` property to the name of the host on which your Web
  server is running that is configured with your JTA 1.3 Vendor
  Implementation. +
  The default setting is `localhost`.
  b.  If you are testing an implementation within a Web container, set the
  `webServerPort` property to the port number of the host on which the Web
  server is running that is configured with your JTA 1.3 Vendor
  Implementation. +
  The default setting is `8000`.
  c.  Set the `webServerHome` property to the directory where you
  installed your JTA 1.3 Vendor Implementation.
  d.  Set the `jta.classes` property to point to the classes or JAR file
  that contains the JTA classes.
  e. Add Set `jta.JarClasspath` property to point to the path of the JTA API jar
  file under test.
  f.  If you are using a deployment handler XML file to deploy the tests
  to a Java Servlet or JavaServer Pages container, set the
  `web.deployment.handler` property to point to your deployment handler
  XML file in the `<TS_HOME>/bin/deploy` directory.Make sure that you
  implement the —deploy and —deploy.all targets in this file.
  g.  To copy the tsharness.jar and jtatck.jar files to the server's
  extension directory, change to the `<TS_HOME>/bin` directory and execute
  the following command: +
  `<TS_HOME>/tools/ant/bin/ant config.vi`
  h.  To deploy the JTATCKWAR files, change to the `<TS_HOME>/bin`
  directory and execute the following command: +
  `<TS_HOME>/tools/ant/bin/ant deploy.all`

[[GHGDG]][[publishing-the-test-applications]]

4.3 Publishing the Test Applications
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Not needed for the {TechnologyShortName} TCK.
