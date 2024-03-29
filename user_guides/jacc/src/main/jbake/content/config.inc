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
  a.  `JAVA_HOME` to the directory in which Java SE {SEversion} is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `ANT_HOME/bin`
2.  Copy <TS_HOME>/bin/ts.jte.jdk11 as <TS_HOME>/bin/ts.jte if JAVA_HOME is Java SE 11.
Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `jacc.home` property to the installation directory of Jakarta EE
  {JakartaEEVersion} CI.
  b.  Set the `jacc.host` property to the host name of the system where your
  {TechnologyShortName} runtime implementation is installed.
  c.  Set the `jacc.classes` property to point to the classes or JAR file
  that contains the {TechnologyShortName} classes.
  d. Set the `sigTestClasspath` property to point to the classes or JAR file
  for the runtime implementation of the {TechnologyShortName} {TechnologyVersion} API and any additional required
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
  a.  `JAVA_HOME` to the directory in which Java SE {SEversion} is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `ANT_HOME/bin`
2.  Copy <TS_HOME>/bin/ts.jte.jdk11 as <TS_HOME>/bin/ts.jte if JAVA_HOME is Java SE 11.
Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `jacc.home` property to the installation directory of Jakarta EE
  {JakartaEEVersion} CI.
  b.  Set the `jacc.host` property to the host name of the system where your
  {TechnologyShortName} runtime implementation is installed.
  c.  Set the `jacc.classes` property to point to the classes or JAR file
  that contains the {TechnologyShortName} classes.
  d. Set the `sigTestClasspath` property to point to the classes or JAR file
  for the runtime implementation of the Jakarta Authorization API and any additional required
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
* Sets the following Jakarta Authorization JVM options: +
[source,oac_no_warn]
----
-Djakarta.security.jacc.policy.provider=
        com.sun.ts.tests.jacc.provider.TSPolicy
-Dvendor.jakarta.security.jacc.policy.provider=
        com.sun.enterprise.security.jacc.provider.SimplePolicyProvider
-Djakarta.security.jacc.PolicyConfigurationFactory.provider=
        com.sun.ts.tests.jacc.provider.TSPolicyConfigurationFactoryImpl
-Dvendor.jakarta.security.jacc.PolicyConfigurationFactory.provider=
        com.sun.enterprise.security.jacc.provider.SimplePolicyConfigurationFactory
-Dlog.file.location=${log.file.location}
----
Note that the `log.file.location` comes from the property of the same
name in the `ts.jte` file.
+
* Deploys the {TechnologyShortName} Provider (from `<TS_HOME>/lib/tsprovider.jar`) to
your server's library directory (for example using {TechnologyRI},
`glassfish6/glassfish/lib`) where it can be picked up and loaded by the
server
* Enables the Security manager with the `-Djava.security.manager` JVM
option
* Creates users required by the TCK tests on the server under test
* Deploys `tsharness.jar` and `jacctck.jar` files to your {TechnologyShortName} server's
`/lib` directory +
4.  Enable the TCK {TechnologyShortName} provider: +
[source,oac_no_warn]
----
ant enable.jacc
----
After running the {TechnologyShortName} TCK tests, disable the {TechnologyShortName} provider by running
the disable.jacc Ant task: +
[source,oac_no_warn]
----
ant disable.jacc
----
5.  Change to the appropriate {TechnologyShortName} TCK test subdirectory
(`<TS_HOME>/src/com/sun/ts/tests/jacc/web` or
<TS_HOME>/src/com/sun/ts/tests/jacc/ejb) for the tests that you plan to
run and execute the `ant deploy` command to deploy the desired tests. +
To deploy the {TechnologyShortName} EJB tests: +
[source,oac_no_warn]
----
cd ${TS_HOME}/src/com/sun/ts/tests/jacc/ejb
ant deploy
----
Or, to deploy the {TechnologyShortName} Web tests: +
[source,oac_no_warn]
----
cd ${TS_HOME}/src/com/sun/ts/tests/jacc/web
ant deploy
----
Repeat this deployment step for each {TechnologyShortName} test directory after you run
the tests in the current directory, as described in
link:using.html#GBFWO[Chapter 5, "Executing Tests."] +
{TechnologyShortName} tests translate security configurations into corresponding {TechnologyShortName}
permissions. If multiple test directories are deployed simultaneously,
the result can be permissions that are stricter than what is expected,
which can lead to test failures. To avoid this potential problem, deploy
and run individual test directories separately, not simultaneously.
