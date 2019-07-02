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
  c.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `jacc.home` property to the installation directory of Java EE
  8 RI.
  b.  Set the `jacc.host` property to the host name of the system where your
  JACC runtime implementation is installed.
  c.  Set the `jacc.classes` property to point to the classes or JAR file
  that contains the JACC classes.
  d. Set the `sigTestClasspath` property to point to the classes or JAR file
  for the runtime implementation of the JSR 115 API and any additional required
  signature classes.
3.  Copy the `tsharness.jar` and `jacctck.jar` files to the server’s extension
directory, change to the `<TS_HOME>/bin` directory and execute the following
commands:
+
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant config.vi
ant enable.jacc
----
+

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

1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `jacc.home` property to the installation directory of Java EE
  8 RI.
  b.  Set the `jacc.host` property to the host name of the system where your
  JACC runtime implementation is installed.
  c.  Set the `jacc.classes` property to point to the classes or JAR file
  that contains the JACC classes.
  d. Set the `sigTestClasspath` property to point to the classes or JAR file
  for the runtime implementation of the JSR 115 API and any additional required
  signature classes.
3.  Change to the `<TS_HOME>/bin` directory and execute the following
commands:
+
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant config.vi
----
+
The `config.vi` Ant task performs several actions, including:
+
* Sets the following JACC JVM options: +
[source,oac_no_warn]
----
-Djavax.security.jacc.policy.provider=
        com.sun.ts.tests.jacc.provider.TSPolicy
-Dvendor.javax.security.jacc.policy.provider=
        com.sun.enterprise.security.provider.PolicyWrapper
-Djavax.security.jacc.PolicyConfigurationFactory.provider=
        com.sun.ts.tests.jacc.provider.TSPolicyConfigurationFactoryImpl
-Dvendor.javax.security.jacc.PolicyConfigurationFactory.provider=
        com.sun.enterprise.security.provider.PolicyConfigurationFactoryImpl
-Dlog.file.location=${log.file.location}
----
Note that the `log.file.location` comes from the property of the same
name in the `ts.jte` file.
+
* Deploys the JACC Provider (from `<TS_HOME>/lib/tsprovider.jar`) to
your server's library directory (for example,
`glassfish5/glassfish/lib`) where it can be picked up and loaded by the
server
* Enables the Security manager with the `-Djava.security.manager` JVM
option
* Creates users required by the TCK tests on the server under test
* Deploys `tsharness.jar` and `jacctck.jar` files to your JACC server's
`/lib` directory +
4.  Enable the TCK JACC provider: +
[source,oac_no_warn]
----
ant enable.jacc
----
After running the JACC TCK tests, disable the JACC provider by running
the disable.jacc Ant task: +
[source,oac_no_warn]
----
ant disable.jacc
----
5.  Change to the appropriate JACC TCK test subdirectory
(`<TS_HOME>/src/com/sun/ts/tests/jacc/web` or
<TS_HOME>/src/com/sun/ts/tests/jacc/ejb) for the tests that you plan to
run and execute the `ant deploy` command to deploy the desired tests. +
To deploy the JACC EJB tests: +
[source,oac_no_warn]
----
cd ${TS_HOME}/src/com/sun/ts/tests/jacc/ejb
ant deploy
----
Or, to deploy the JACC Web tests: +
[source,oac_no_warn]
----
cd ${TS_HOME}/src/com/sun/ts/tests/jacc/web
ant deploy
----
Repeat this deployment step for each JACC test directory after you run
the tests in the current directory, as described in
link:using.html#GBFWO[Chapter 5, "Executing Tests."] +
JACC tests translate security configurations into corresponding JACC
permissions. If multiple test directories are deployed simultaneously,
the result can be permissions that are stricter than what is expected,
which can lead to test failures. To avoid this potential problem, deploy
and run individual test directories separately, not simultaneously.
