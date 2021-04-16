///////////////////////////////////////////////////////////////////////////////

    Copyright (c) 2020, 2021 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

///////////////////////////////////////////////////////////////////////////////

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

4.1 Configuring Your Environment to Run the TCK Against the Compatible Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
  a.  `JAVA_HOME` to the directory in which Java SE {SEversion} is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c. `ANT_HOME` to the directory in which the Apache Ant {AntVersion} is installed
  d.  +{TechnologyHomeEnv}+ to the directory in which the {TechnologyShortName}
  {TechnologyVersion} CI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `ANT_HOME/bin`
2.  Copy <TS_HOME>/bin/ts.jte.jdk11 as <TS_HOME>/bin/ts.jte if JAVA_HOME is Java SE 11.
Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  `webServerHost` to the name of the host on which your {TechnologyFullName} 
  {TechnologyVersion} implementation is running.
  b.  `webServerPort` to the port number of the host on which {TechnologyFullName} 
  {TechnologyVersion} implementation is running.
  c.  `web.home` to the location where your {TechnologyFullName} {TechnologyVersion}
  implementation is installed.
  d.  `websocket.api` to point to the location of the JAR files and
  directories that contain the APIs for {TechnologyFullName} {TechnologyVersion} and
  Jakarta Servlet. +
  The default setting for this property is:
+
--
[source,oac_no_warn]
----
websocket.api=${web.home}/modules/jakarta.websocket-api.jar \
${pathsep}${web.home}/modules/jakarta.servlet-api.jar
----
--
+
  e.  `websocket.classes` property to point to the JAR files and
  directories that contain the {TechnologyFullName} {TechnologyVersion} implementation
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
${pathsep}${web.home}/modules/tyrus-container-grizzly-client.jar \
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
  Set this property to `glassfish` for the Jakarta EE 9 CI (Eclipse GlassFish 6.0) full-, or web-profile CI.
  j. `impl.vi.deploy.dir` to the Vendor implementation deployment
  directory. +
  Set this property to `${web.home}/domains/domain1/autodeploy` for the
   CI (Eclipse GlassFish 6.0) full-, or web-profile CI.
+
3.  Add permissions from the file `<TS_HOME>/bin/server_policy.append`
to the Java policy file or files on your Jakarta EE 9 server. +
In case of Jakarta EE 9 CI (Eclipse GlassFish 6.0), add permissions that are not already defined to
the file $\{web.home}`/domains/domain1/config/server.policy`, where
$\{web.home} is the location of your installation of the Jakarta EE 9 CI (Eclipse GlassFish 6.0).
4. When creating CI-specific application server settings, it is
recommended that you create a configuring file named `config.vi.xml`
under `$TS_HOME/bin/xml/${impl_vi}`. +
Use the `$TS_HOME/bin/xml/glassfish/config.vi.xml` file as a reference.
5. Run the `ant config.vi` target to configure the Compatible
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
* Copies the TCK-dependent files `${tslib.name}.jar` and `tsharness.jar` into the application server's external library folder
* Starts the application server
* Creates users, their password, and the appropriate roles as follows:
+
--
[width="100%",cols="34%,33%,33%",options="header",]
|==============================
|User |Password |Groups
|`javajoe` |`javajoe` |`guest`
|`j2ee` |`j2ee` |`staff`, `mgr`
|==============================
Also make sure the principal to role-mappings that are specified in the
runtime XML files are properly mapped in your environment. These
mappings may vary for each application.
--

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

==== Before You Begin

Decide against which {TechnologyShortName} implementation the tests
will be run and determine to which Servlet–compliant Web server the
{TechnologyShortName} TCK applications will be published.


1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE {SEversion} is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c. `ANT_HOME` to the directory in which the Apache Ant {AntVersion} is installed
  d.  +{TechnologyHomeEnv}+ to the directory in which the {TechnologyShortName}
  {TechnologyVersion} VI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `ANT_HOME/bin`
2.  Copy <TS_HOME>/bin/ts.jte.jdk11 as <TS_HOME>/bin/ts.jte if JAVA_HOME is Java SE 11.
Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  `webServerHost` to the name of the host on which your {TechnologyFullName} 
  {TechnologyVersion} implementation is running.
  b.  `webServerPort` to the port number of the host on which {TechnologyFullName} 
  {TechnologyVersion} implementation is running.
  c.  `web.home` to the location where your {TechnologyFullName} {TechnologyVersion}
  implementation is installed.
  d.  `websocket.api` to point to the location of the JAR files and
  directories that contain the APIs for {TechnologyFullName} {TechnologyVersion} and
  Jakarta Servlet. +
  The default setting for this property is:
+
--
[source,oac_no_warn]
----
websocket.api=${web.home}/modules/jakarta.websocket-api.jar \
${pathsep}${web.home}/modules/jakarta.servlet-api.jar
----
--
+
  e.  `websocket.classes` property to point to the JAR files and
  directories that contain the {TechnologyFullName} {TechnologyVersion} implementation
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
${pathsep}${web.home}/modules/tyrus-container-grizzly-client.jar \
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
  Set this property to `glassfish` for the Jakarta EE 9 VI (Eclipse GlassFish 6.0) full-, or web-profile VI.
  j. `impl.vi.deploy.dir` to the Vendor implementation deployment
  directory. +
  Set this property to `${web.home}/domains/domain1/autodeploy` for the
   VI (Eclipse GlassFish 6.0) full-, or web-profile VI.
+
3.  Add permissions from the file `<TS_HOME>/bin/server_policy.append`
to the Java policy file or files on your Jakarta EE 9 server.
4. When creating VI-specific application server settings, it is
recommended that you create a configuring file named `config.vi.xml`
under `$TS_HOME/bin/xml/${impl_vi}`. +
Use the `$TS_HOME/bin/xml/glassfish/config.vi.xml` file as a reference.
5. Run the `ant config.vi` target to configure the Vendor
Implementation that is defined in the `impl.vi` property.


[[GCRWX]][[deploying-the-java-api-for-websocket-tck-tests]]

4.3 Deploying the Java API for WebSocket TCK Tests
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To deploy the Java API for WebSocket TCK tests to the Web/Application
server to which you will deploy the Java API for WebSocket TCK tests,
perform the following steps.

1.  Make sure that the Web/Application server to which you will deploy
the {TechnologyFullName} {TechnologyVersion} TCK tests is running.
2.  Change to the `<TS_HOME>/bin` directory.
3.  Execute the `ant deploy.all` command. +
All of the WAR files containing the {TechnologyFullName} {TechnologyVersion} TCK tests
have been deployed. Follow the instructions in
link:using.html#GBFUZ[Using the GUI for TCK Test Execution] or
link:using.html#GJCIW[Using the Command Line for TCK Test Execution] to
run the tests you just deployed.


