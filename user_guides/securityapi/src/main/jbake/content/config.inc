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
  a.  Set `securityapi.classes` to include all necessary JAR files that
  pertain to your implementation.
  b.  Set `web.home` to the location where the securityapi is implemented.
  For example, `web.home=<GLASSFISH_HOME_FOLDER`>.
  c.  Set `jdbc.lib.class.path` to the location where the JDBC drivers are
  installed.
  d.  Set `jdbc.db` to the name of the database under test. Valid values
  include:
+
--
[source,oac_no_warn]
----
derby
mysql
oracle
----
--
+
  e.  Set `sigTestClasspath` to include any additional classes not
  specified with the `securityapi.classes` property.
  f.  Set `work.dir` to the default directory in which JavaTest writes
  temporary files that are created during test execution. The default
  location is <TS_HOME>/tmp/JTwork. +
  This property is required for the TCK Ant targets.
  g.  Set `report.dir` to the default directory in which JavaTest creates
  a test report for the most recent test run. The default location is
  <TS_HOME>/tmp/JTreport. +
  This property is a required property for the TCK Ant targets; it must be
  set. To disable reporting, set the `report.dir` property to `"none"`.
  h.  If you are testing against the default UnboundID LDAP server that is
  bundled with the TCK (recommended), make sure the following properties
  are set: +
  `ldap.server=unboundid` +
  `ldap.install.server=true` +
  `ldap.ldif.file=${ts.home}/bin/ldap.ldif` +
  If you are testing against a different LDAP server, then set the
  following property: +
  `ldap.install.server=false` +
  For a non-default LDAP, you must manually install the server on the
  local machine, and configure it to listen on port 11389. You will also
  need to load the test data by importing the file
  `<TS_HOME>/bin/ldap.ldif`.
+
3.  Start the database under test.
4.  Ensure that no process is using port 11389. If you are not using the
default LDAP server, start the LDAP server under test.
5.  Initialize the Vendor Implementation (server, database, and LDAP). +
Change to the `<TS_HOME>/bin` directory and execute the following
command.
+
--
[source,oac_no_warn]
----
ant config.vi
ant init.ldap
----
--
+
6.  Deploy all the server packages for testing. To do so, execute the
following command:
+
--
[source,oac_no_warn]
----
ant deploy.all
----
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

Before You Begin

Decide against which {TechnologyShortName} implementation the tests
will be run and determine to which Servlet–compliant Web server the
{TechnologyShortName} TCK applications will be published.

Adapt the above instructions for the vendor implementation.


[[GHGDG]][[publishing-the-test-applications]]

4.3 Publishing the Test Applications
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Not needed for the {TechnologyShortName} TCK.
