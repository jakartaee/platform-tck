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
  a.  Set the `local.classes`
  property to point to the CAJ RI classes/jars that contain the
  annotations being supported in Step link:#BABJHHAJ[c], below.
  b.  Set the `endorsed.dirs`
  property to the location of the RI API jars for those technologies you
  wish to override that exist within the JDK.
  c.  [[BABJHHAJ]] +
  Depending on the annotations that are supported or unsupported in your
  CAJ implementation, the following properties
  should be set to `true` or `false` respectively. +
  By default, all of these annotations are supported in the CAJ RI. +
[source,oac_no_warn]
----
#javax.annotation.Generated
ca.sig.generated=true

#javax.annotation.ManagedBean
ca.sig.managedbean=true

#javax.annotation.PostConstruct
ca.sig.postconstruct=true

#javax.annotation.PreDestroy
ca.sig.predestroy=true

#javax.annotation.Priorityca.sig.priority=true

#javax.annotation.Resource
ca.sig.resource=true

#javax.annotation.Resources
ca.sig.resources=true

#javax.annotation.security.DeclareRoles
ca.sig.securitydeclareroles=true

#javax.annotation.security.DenyAll
ca.sig.securitydenyall=true

#javax.annotation.security.PermitAll
ca.sig.securitypermitall=true

#javax.annotation.security.RolesAllowed
ca.sig.securityrolesallowed=true

#javax.annotation.security.RunAs
ca.sig.securityrunas=true

#javax.annotation.sql.DataSourceDefinition
ca.sig.sqldatasourcedefinition=true

#javax.annotation.sql.DataSourceDefinitions
ca.sig.sqldatasourcedefinitions=true
----
  d.  Run the JavaTest harness in GUI or command-line mode, as described
in link:#GBFUY[Section 4.4, "Using the JavaTest Harness Software."]

[[GCLHU]][[configuring-your-environment-to-repackage-and-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Repackage and Run the TCK Against the Vendor Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This is not required for the CAJ TCK.

[[GHGDG]][[publishing-the-test-applications]]

4.3 Publishing the Test Applications
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This is not required for the CAJ TCK.
