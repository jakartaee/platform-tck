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
  a.  `webServerHost` to the name of the host on which your Java API for
  WebSocket 1.0 implementation is running.
  b.  `webServerPort` to the port number of the host on which Java API for
  WebSocket 1.0 implementation is running.
  c.  `web.home` to the location where your Java API for WebSocket 1.0
  implementation is installed.
  d.  `websocket.api` to point to the location of the JAR files and
  directories that contain the APIs for Java API for WebSocket 1.0 and
  Java Servlet. +
  The default setting for this property is:
+
--
[source,oac_no_warn]
----
websocket.api=${web.home}/modules/javax.websocket-api.jar \
${pathsep}${web.home}/modules/javax.servlet-api.jar
----
--
+
  e.  `websocket.classes` property to point to the JAR files and
  directories that contain the Java API for WebSocket 1.0 implementation
  classes. +
  The default setting for this property is:
+
--
[source,oac_no_warn]
----
websocket.classes=${web.home}/modules/tyrus-websocket-core.jar \
${pathsep}${web.home}/modules/tyrus-client.jar \
${pathsep}${web.home}/modules/tyrus-core.jar \
${pathsep}${web.home}/modules/tyrus-container-grizzly.jar \
${pathsep}${web.home}/modules/glassfish-grizzly-extra-all.jar \
${pathsep}${web.home}/modules/nucleus-grizzly-all.jar \
${pathsep}${web.home}/modules/tyrus-server.jar \
${pathsep}${web.home}/modules/tyrus-container-servlet.jar \
${pathsep}${web.home}/modules/tyrus-spi.jar
----
--
+
  f.  `ws_wait` to the wait time, in seconds, for the socket to send or
  receive a message.
  g.  `report.dir` to the default directory in which JavaTest creates a
  test report for the most recent test run. +
  This property is a required property for the TCK Ant targets; it must be
  set. To disable reporting, set the `report.dir` property to `none`.
  h.  `work.dir` to the default directory in which JavaTest writes
  temporary files that are created during test execution. +
  This property is required for the TCK Ant targets.
  i.  `impl.vi` to the name of the Vendor implementation. +
  This property is used for identifying the deployment handler. +
  Set this property to `glassfish` for the Java EE 8 RI or the Java EE 8
  Web Profile RI.
  j. `impl.vi.deploy.dir` to the Vendor implementation deployment
  directory. +
  Set this property to `${web.home}/domains/domain1/autodeploy` for the
  Java EE 8 RI or the Java EE 8 Web Profile RI.
+
3.  Add permissions from the file `<TS_HOME>/bin/server_policy.append`
to the Java policy file or files on your Java EE 8 server. +
In case of Java EE 8 RI, add permissions that are not already defined to
the file $\{web.home}`/domains/domain1/config/server.policy`, where
$\{web.home} is the location of your installation of the Java EE 8 RI.


[[GCLHU]][[configuring-your-environment-to-repackage-and-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Repackage and Run the TCK Against the Vendor Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

After configuring your environment as described in this section,
continue with the instructions in link:#GBFUY[Section 4.4, "Using the
JavaTest Harness Software."]

Adapt the instructions above for the vendor implementation.


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


[[GCRWX]][[deploying-the-java-api-for-websocket-tck-tests]]

4.3 Deploying the Java API for WebSocket TCK Tests
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To deploy the Java API for WebSocket TCK tests to the Web/Application
server to which you will deploy the Java API for WebSocket TCK tests,
perform the following steps.

1.  Make sure that the Web/Application server to which you will deploy
the Java API for WebSocket TCK tests is running.
2.  Change to the `<TS_HOME>/bin` directory.
3.  Execute the `ant deploy.all` command. +
All of the WAR files containing the Java API for WebSocket TCK tests
have been deployed. Follow the instructions in
link:using.html#GBFUZ[Using the GUI for TCK Test Execution] or
link:using.html#GJCIW[Using the Command Line for TCK Test Execution] to
run the tests you just deployed.


