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
  c.  `connector.home` to the location where your {TechnologyShortName} runtime
  implementation is installed.
  d.  `orb.host` to the hostname of the system on which your
  {TechnologyShortName} runtime was installed.
  e.  `orb.port` to the port number of the system on which your
  {TechnologyShortName} runtime was installed.
  f.  `connector.classes` to the {TechnologyShortName} classes or JAR
  files that contain the {TechnologyShortName} classes.
  g.  `sigTestClasspath` to the to the classes or JAR file (or files) for
  the runtime implementation of the JSR 322 API and any additional
  required signature or {TechnologyShortName} API classes.
  h.  `impl.vi` to the directory where vendor-specific targets can best be
  stored and isolated. The default directory, `glassfish`, is where
  details about the installation and configuration of the GlassFish
  enterprise server can be found.
  i.  `report.dir` to the default directory in which JavaTest creates a
  test report for the most recent test run. +
  This property is a required property for the TCK Ant targets; it must be
  set. To disable reporting, set the `report.dir` property to `none`.
  j. `work.dir` to the default directory in which JavaTest writes
  temporary files that are created during test execution. +
  This property is required for the TCK Ant targets.
+
3.  Provide your own implementations of the porting package interfaces
provided with the {TechnologyShortName} TCK. +
`TSURLInterface.java` obtains URL strings for web resources in an
implementation-specific manner. API documentation for the
`TSURLInterface.java` porting package interface is available in the
documentation bundle in the `docs/api` directory.
4.  Execute the `config.vi` Ant task. +
This target performs the following tasks:
* Stops the application server running the {TechnologyShortName}
{TechnologyVersion} RI
* Copies the TCK-dependent files `${tslib.name}.jar` and `tsharness.jar`
into the application server's external library folder
* Starts the application server
* Sets the appropriate JVM options in the runtime
* Creates connection pools and connection resources
* Deploys the required RAs
* Creates users in the runtime, then maps RA users to these runtime
users +
Ensure that the users and passwords specified in the `ts.jte` file are
created in the {TechnologyShortName} server. These users and passwords
include the `ts.jte` properties of `rauser1`, `rapassword1`, `user`,
`password`, `authuser`, `authpassword`, `user_vi`, `password_vi`, and
so on.

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


Adapt the instructions above for your environment.


[[GCRWX]][[deploying-the-jca-tck-tests]]

4.3 Deploying the JCA TCK Tests
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

[[GJKVJ]][[extension-libraries]]

4.3.1 Extension Libraries
^^^^^^^^^^^^^^^^^^^^^^^^^

The JCA 1.7 TCK RAR files are deployed simultaneously.
The manifest file in each RAR file
includes a reference to the whitebox extension library. The
`whitebox.jar` file is a shared library that must be deployed as a
separate entity that all the standalone RAR files can access. This
extension library is needed to address classloading issues.

[[GJOUV]][[jca-1.7-tck-resource-adapter-files]]

4.3.1.1 JCA 1.7 TCK Resource Adapter Files
++++++++++++++++++++++++++++++++++++++++++

The Resource Adapter (RAR) files that are used with the JCA 1.7 TCK
differ from those that were used in earlier JCA TCK releases. The TCK no
longer includes the same common classes into every RAR file. Duplicate
common classes, such as `whitebox.jar`, have been removed from each RAR
file and are now handled as an Installed Library.

This was done to address the following compatibility issues:

* Portable use of Installed Libraries for specifying a resource
adapter's shared libraries +
See section EE.8.2.2 of the Java EE 7 platform specification and section
20.2.0.1 in the JCA 1.7 specification, which explicitly state that the
resource adapter server may employ the library mechanisms in Java EE 7.
* Support application-based standalone connector accessibility +
See section 20.2.0.4 of the JCA 1.7 specification, which uses the
classloading requirements listed in section 20.3 in the specification.

[[GJOUR]][[jca-1.7-tck-resource-adapters-and-classloading]]

4.3.1.2 JCA 1.7 TCK Resource Adapters and Classloading
++++++++++++++++++++++++++++++++++++++++++++++++++++++

The JCA TCK 1.7 has scenarios in which multiple standalone RAR files
that use the same shared library (for example, `whitebox.jar`) are
referenced from a TCK application component.

Each standalone RAR file gets loaded in its own classloader. Since the
application component refers to more than one standalone RAR file, all
of the referenced standalone RAR files need to be made available in the
classpath of the application component. In previous versions of the TCK,
since each standalone RAR file contained a copy of the `whitebox.jar`
file, every time there was a reference to a class in the `whitebox.jar`
file from a standalone RAR, the reference was resolved by using the
private version of `whitebox.jar` (the `whitebox.jar` file was bundled
in each standalone RAR file). This approach can lead to class type
inconsistency issues.

[[GJOVA]][[use-case-problem-scenario]]

4.3.1.3 Use Case Problem Scenario
+++++++++++++++++++++++++++++++++

Assume that RAR1 and RAR2 are standalone RAR files that are referred to
by an application, where:

* RAR1's classloader has access to RAR1's classes and its copy of
`whitebox.jar`. (RAR1's classloader contains RAR1's classes and
`whitebox.jar`)
* RAR2's classloader has access to RAR2's classes and its copy of
`whitebox.jar`. (RAR2's classloader contains RAR2's classes and
`whitebox.jar`)

When the application refers to both of these RAR files, a classloader
that encompasses both of these classloaders (thereby creating a
classloader search order) is provided to the application. The
classloader search order could have the following sequence: , .

1.  RAR1's Classloader: RAR1's classes and `whitebox.jar`
2.  RAR2's Classloader: RAR2's classes and `whitebox.jar`

In this scenario, when an application loads a class (for example, class
`Foo`) in `whitebox.jar`, the application gets class `Foo` from RAR1's
classloader because that is first in the classloader search order.
However, when this is cast to a class (for example, `Foo` or a subclass
of `Foo` or even a class that references `Foo`) that is obtained from
RAR2's classloader (a sequence that is typically realized in a
`ConnectionFactory` lookup), this would result in a class-cast
exception.

The portable way of solving the issues raised by this use case problem
scenario is to use installed libraries, as described in section EE.8.2.2
in the Java EE 7 platform specification. If both RAR files (RAR1 and
RAR2) reference `whitebox.jar` as an installed library and the
application server can use a single classloader to load this common
dependency, there will be no type-related issues.

In the TCK's RI, `domain-dir/lib/applibs` is used as the Installed
Library directory and is the location to which the `whitebox.jar` file
gets copied.

[[GJOSS]][[required-porting-package]]

4.3.1.4 Required Porting Package
++++++++++++++++++++++++++++++++

The JCA 1.7 TCK treats the `whitebox.jar` dependency as an Installed
Library dependency instead of bundling the dependency (or dependencies)
with every RAR file. Each RAR file now contains a reference to the
`whitebox.jar` file through its Manifest files Extension-List attribute.

It is necessary to identify the `whitebox.jar` to the connector server
as an installed library. The mechanism used to identify the
`whitebox.jar` file to the connector server as an Installed Library must
allow the Installed Libraries to have dependencies on Java EE APIs. In
other words, because the `whitebox.jar` file depends on Java EE APIs,
one cannot simply put the `whitebox.jar` file into a `java.ext.dir`
directory , which gets loaded by the VM extension classloader, because
that mechanism does not allow the `whitebox.jar` file to support its
dependencies on the Java EE APIs. For this reason, the Installed Library
must support access to the Java EE APIs.

See section EE.8.2.2 in the Java EE 7 platform specification for
information about the reference implementation's support for Installed
libraries. However, note that this section does not recommend a
mechanism that a deployer can use to provide Installed Libraries in a
portable manner.

The JCA TCK tests should only be deployed after you properly configure
your connector runtime. This configuration includes creating users and
JVM options in the runtime, mapping RA users to existing runtime users,
deploying RAs, and creating connection pools and connection resources.

Deployment of the connector resource adapters involves the deployment of
17 RAR files:

* `whitebox-mixedmode.rar`
* `whitebox-tx-param.rar`
* `whitebox-multianno.rar`
* `whitebox-tx.rar`
* `whitebox-anno_no_md.rar`
* `whitebox-notx-param.rar`
* `whitebox-xa-param.rar`
* `whitebox-mdcomplete.rar`
* `whitebox-notx.rar`
* `whitebox-xa.rar`
* `whitebox-ibanno_no_md.rar`
* `old-dd-whitebox-notx-param.rar`
* `old-dd-whitebox-xa-param.rar`
* `old-dd-whitebox-tx.rar`
* `old-dd-whitebox-notx.rar`
* `old-dd-whitebox-xa.rar`
* `old-dd-whitebox-tx-param.rar`


[NOTE]
=======================================================================

RAR files with an "old" prefix are used to test the support of RAs that
are bundled with an older version of the `ra.xml` files.

=======================================================================


Deploying RAR files require the installation of a `whitebox.jar` file to
a directory into which the server can load. The `whitebox.jar` file is a
common library, which is referenced and used by all the RAR files. To
ensure proper class loading, the `whitebox.jar` file must be copied into
the application server's `domains/domain1/lib/applibs` directory, a
location where it can be loaded by the server and can be accessed by all
the RAR files.

To deploy the JCA TCK tests to the Java EE 7 platform, perform the
following steps.

[[GCRWU]][[to-configure-and-deploy-the-jca-tck-tests-on-the-java-ee-7-web-profile-ri]]

4.3.2 To Configure and Deploy the JCA TCK Tests on the Java EE 7 Web Profile RI
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The `config.vi` Ant task performs several configuration procedures on
your connector server. This target deploys some of the required Resource
Adapters (RAR files) and creates some connection resources and
connection pools. The test suite also provides a convenience Ant target,
which deploys only the RAR files and creates connection pools and
connection resources. This Ant target is in the
`<TS_HOME>/bin/xml/impl/glassfish/connector.xml` file. See the Ant
target `setup.all.rars.and.pools` for more information.

1.  Make sure that the server to which you will deploy the JCA TCK tests
is running.
2.  Change to the `<TS_HOME>/bin` directory and execute the Ant task to
configure the RI.
+
--
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant
----
--
+
3.  Execute the `ant deploy.all` Ant task to deploy the requisite RAR
files.
+
--
[source,oac_no_warn]
----
ant deploy.all
----
--
+
Follow the instructions in link:using.html#GBFUZ[Section 5.1, "Using the
GUI for TCK Test Execution,"] or link:using.html#GJCIW[Section 5.2,
"Using the Command-Line for TCK Test Execution,"] to run the tests you
just deployed.

[[GJKWJ]][[configuring-the-jca-tck-tests-on-the-vendor-implementation]]

4.3.3 Configuring the JCA TCK Tests on the Vendor Implementation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Vendors need to configure their application or connector servers to run
the TCK tests. This configuration needs to support the same features
that are currently performed against the RI through the execution of the
`config.vi` and `setup.all.rars.and.pools` Ant targets.

This section describes how to configure the Vendor Implementation (VI)
before running the JCA TCK tests.

Performing the tasks of these targets should minimally include the
following:

* link:#GJKVE[Creating Security Mappings for the RAR Files]
* link:#GJKVI[Creating Required Server-Side JVM Options]
* link:#GJKVW[Replacing the Default Vehicle with a Custom Vehicle]

[[GJKVE]][[creating-security-mappings-for-the-rar-files]]

4.3.3.1 Creating Security Mappings for the RAR Files
++++++++++++++++++++++++++++++++++++++++++++++++++++

See the Ant target `create.security.eis.mappings` in the
`<TS_HOME>/bin/xml/impl/glassfish/connector.xml` to see how this is done
with the RI. This task maps Resource Adapter user information to
existing user information in the connector runtime.

For the RI, these mappings add a line to the `domain.xml` file, similar
to the one shown below, and should include 6 of these mappings:

[source,oac_no_warn]
----
<jvm-options>-Dwhitebox-tx-map=cts1=j2ee</jvm-options>
<jvm-options>-Dwhitebox-tx-param-map=cts1=j2ee</jvm-options>
<jvm-options>-Dwhitebox-notx-map=cts1=j2ee</jvm-options>
<jvm-options>-Dwhitebox-notx-param-map=cts1=j2ee</jvm-options>
<jvm-options>-Dwhitebox-xa-map=cts1=j2ee</jvm-options>
<jvm-options>-Dwhitebox-xa-param-map=cts1=j2ee</jvm-options>
----

If the `rauser1` property has been set to `cts1` and the `user` property
has been set to `j2ee` in the `ts.jte` file, the following mappings
would be required in the connector runtime:

* For RA `whitebox-tx`, map `cts1` to `j2ee`
* For RA `whitebox-tx-param`, map `cts1` to `j2ee`
* For RA `whitebox-notx`, map `cts1` to `j2ee`
* For RA `whitebox-notx-param`, map `cts1` to `j2ee`
* For RA `whitebox-xa`, map `cts1` to `j2ee`
* For RA `whitebox-xa-param`, map `cts1` to `j2ee`

[[GJKVI]][[creating-required-server-side-jvm-options]]

4.3.3.2 Creating Required Server-Side JVM Options
+++++++++++++++++++++++++++++++++++++++++++++++++

Create the required JVM options to allow the passing and/or setting of
user information from the `ts.jte` file to the server. These properties
are set in the connector runtime for use by the RAR files, which are
deployed to the connector runtime. The RAR files need to know some of
the property settings used in the `ts.jte` file; this is the means used
to specify some of the user properties.

To see some of required JVM options for the RI, examine the
`ri.jvm.options` property in the `ts.jte` file. The following subset of
JVM option specified in the `ri.jvm.options` property must be set in the
connector runtime:

[source,oac_no_warn]
----
-Dj2eelogin.name=j2ee
-Dj2eelogin.password=j2ee
-Deislogin.name=cts1
-Deislogin.password=cts1
----

[[GJKVW]][[replacing-the-default-vehicle-with-a-custom-vehicle]]

4.3.3.3 Replacing the Default Vehicle with a Custom Vehicle
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

If your connector server does not have web support, you will need to
create your own vehicle. A vehicle is a wrapper that supports running
tests in different server-side containers, such as servlet, JSP, and so
on. The JCA TCK provides a default vehicle, `connectorservlet`, which
supports running the TCK tests in a connector runtime that has a Servlet
container. To support running tests in an environment other than a
Servlet container, you need to implement your own vehicle, effectively
replacing the default vehicle, `connectorservlet`.

This TCK was designed so you could use `connectorservlet` as a template
for creating your own vehicle. The `connectorservlet` vehicle is used to
contain and execute your client-side tests in the connector runtime.

The `connectorservlet` vehicle is located in the
`<TS_HOME>/src/com/sun/ts/tests/common/vehicle/connectorservlet`
directory.

To run the tests in a vehicle other than `connectorservlet`, you need to
create a custom vehicle named `customvehicle`. See link:#GJKYA[Section
4.2.2, "To Create a Custom Vehicle,"] for more information on this
topic.

[[sthref10]]


[[deploying-the-rar-files-to-the-vi]]
4.3.4 Deploying the RAR files to the VI
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This section describes how deploy the RAR files to the VI before running
the JCA TCK tests.

The deployment requirements for VI are similar to the requirements for
the RI in that the required Resource Adapter (RAR) files must be
deployed before any TCK tests can be run. Before you attempt to deploy
the RAR files, ensure your connector server has been properly
configured. For additional information about deploying the RAR files and
creating the corresponding connection pools and connection resources,
see the `setup.all.rars.and.pools` Ant target in the
`<TS_HOME>/bin/xml/impl/glassfish/connector.xml file`.


