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
  a.  Set the `webServerHost` property to the hostname where the web
  server is running that is configured with the JAX-WS RI. +
  The default setting is `localhost`.
  b.  Set the `webServerPort` property to the port number where the web
  server is running that is configured with the JAX-WS RI. +
  The default setting is `8000`.
  c.  Set the `jaxws.home` property to the location where the JAX-WS RI is
  installed. +
  The default setting is `${webcontainer.home}`. The value of this
  property must match the value of the `webcontainer.home` property that
  is set in step link:#BABBJEDF[g].
  d.  Set the `jaxws.classes` property to point to the JAX-WS RI
  classes/jars. +
  Note that this property is already set and should not require any
  changes.
  e.  Verify that the `tools.jar` property is set to the location of the
  `tools.jar` file that is distributed with Java SE.
  f.  Set the
  `impl.vi, impl.vi.deploy.dir, impl.vi.host, and impl.vi.port` properties
  to the supported web container, deploy directory, host and port for the
  JAX-WS RI. +
  The supported web container for the JAX-WS RI is the Java EE 8 RI. So
  the default settings for these properties are as follows:
+
--
[source,oac_no_warn]
----
impl.vi.deploy.dir=${webcontainer.home}/domains/domain1/autodeploy
impl.vi=glassfish.xml
impl.vi.host=${webServerHost}
impl.vi.port=${webServerPort}
----
--
+
  g.  Set the `webcontainer.home` property to the location where the web
  server is installed for the JAX-WS RI. This will be where the Java EE 8
  RI is installed.
  h.  Set the `porting.ts.url.class.1` property to the porting
  implementation class that is used for obtaining URLs. +
  The default setting for the JAX-WS RI porting implementation is:
+
--
[source,oac_no_warn]
----
com.sun.ts.lib.implementation.sun.common.SunRIURL
----
--
+
  i.  Set the `user` and `password` properties to the user name and
  password used for the basic authentication tests. +
  The default setting is `j2ee` for both.
  j. Set the `authuser` and `authpassword` properties to the user name
  and password used for the basic authentication tests. +
  The default setting for both is `javajoe`.
  k. Set the `http.server.supports.endpoint.publish` property based on
  whether Endpoint Publish APIs are supported on the container.
  l. If using Java SE 8 or above, verify that the property
  `endorsed.dirs` is set to the location of the VI API jars for those
  technologies you wish to override. Java SE 8 contains an implementation
  of JAX-WS 2.2 which will conflict with JAX-WS 2.3, therefore this
  property must be set so that JAX-WS 2.3 will be used during the building
  of tests and during test execution.
+
3.  Edit the catalog file
`<TS_HOME>/src/com/sun/ts/tests/jaxws/common/xml/catalog /META-INF/jax-ws-catalog.xml`,
replacing the host and port settings of `systemId` with the value of
your host and port setting where the WSDL is published.


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

Package the {TechnologyShortName} test applications for that
{TechnologyShortName} implementation and Servlet–compliant Web
server.

See link:rebuild.html#GCLIZ[Appendix B, "Packaging the Test
Applications in Servlet-Compliant WAR Files With VI-Specific
Information,"] for information about repackaging the
{TechnologyShortName} test application.

1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
  b.  `TS_HOME` to the directory in which the {TechnologyShortName} TCK
  {TechnologyVersion} software is installed
  c.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  +{TechnologyHomeEnv}/bin+, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `webServerHost` property to the hostname where the web
  server that is configured with the Vendor Implementation is running. +
  The default setting is `localhost`.
  b.  Set the `webServerPort` property to the port number where the web
  server that is configured with the Vendor Implementation is running. +
  The default setting is `8080`.
  c.  Set the `jaxws.home` property to the location where the Vendor
  Implementation is installed. +
  The default setting is `${webcontainer.home}`. The value of this
  property must match the value of the `webcontainer.home` property that
  is set in step link:#BABDJAHI[g].
  d.  Set the `jaxws.classes` property to point to the Vendor
  Implementation classes/JAR files. +
  As an example, the `ts.jte` file contains the property
  `jaxws.classes.ri`, which contains the classes/jar files that the Java
  EE 8 RI uses. The `jaxws.classes.ri` settings for the Java EE 8 RI web
  container are as follows:
+
--
[source,oac_no_warn]
----
jaxws.home.ri=${webcontainer.home.ri}
jaxws.lib.ri=${jaxws.home.ri}/modules
endorsed.dirs.ri=${jaxws.home.ri}/modules/endorsed

jaxws.classes.ri=${endorsed.dirs.ri}/webservices-api-osgi.jar:
${endorsed.dirs.ri}/jaxb-api-osgi.jar:
${jaxws.lib.ri}/webservices-osgi.jar:
${jaxws.lib.ri}/jaxb-osgi.jar:
${jaxws.lib.ri}/gmbal.jar:
${jaxws.lib.ri}/management-api.jar:
${jaxws.lib.ri}/mimepull.jar
----
--
+
The `jaxws.classes.ri` settings if using the Apache Tomcat web container
with the Standalone JAX-WS 2.3 RI would be as follows:
+
--
[source,oac_no_warn]
----
jaxws.home.ri=${webcontainer.home.ri}
jaxws.lib.ri=${jaxws.home.ri}/shared/lib
endorsed.dirs.ri=${jaxws.home.ri}/common/endorsed
jaxws.classes.ri=${endorsed.dirs.ri}/jaxws-api.jar:
${endorsed.dirs.ri}/jaxb-api.jar:
${jaxws.lib.ri}/FastInfoset.jar:${jaxws.lib.ri}/gmbal-api-only.jar:\
${jaxws.lib.ri}/ha-api.jar:${jaxws.lib.ri}/javax.annotation-api.jar:\
${jaxws.lib.ri}/javax.xml.soap-api.jar:${jaxws.lib.ri}/jaxb-core.jar:\
${jaxws.lib.ri}/jaxb-impl.jar:${jaxws.lib.ri}/jaxb-jxc.jar:\
${jaxws.lib.ri}/jaxb-xjc.jar:${jaxws.lib.ri}/jaxws-rt.jar:\
${jaxws.lib.ri}/jaxwstck.jar:${jaxws.lib.ri}/jaxws-tools.jar:\
${jaxws.lib.ri}/jsr181-api.jar:${jaxws.lib.ri}/mail.jar:\
${jaxws.lib.ri}/management-api.jar:${jaxws.lib.ri}/mimepull.jar:\
${jaxws.lib.ri}/policy.jar:${jaxws.lib.ri}/resolver.jar:\
${jaxws.lib.ri}/saaj-impl.jar:${jaxws.lib.ri}/sdo-eclipselink-plugin.jar:\
${jaxws.lib.ri}/stax2-api.jar:${jaxws.lib.ri}/stax-ex.jar:\
${jaxws.lib.ri}/streambuffer.jar:${jaxws.lib.ri}/woodstox-core-asl.jar:
----
--
+
  e.  Verify that the `tools.jar` property is set to the location of the
  `tools.jar` file that is distributed with Java SE.
  f.  Set the `impl.vi`, `impl.vi.deploy.dir`, `impl.vi.host`, and
  `impl.vi.port` properties to the supported web container, deploy
  directory, host and port for the Vendor Implementation. +
  As an example, the `ts.jte` file contains the settings for the Java EE 8
  RI, which are as follows:
+
--
[source,oac_no_warn]
----
webcontainer.home.ri=/sun/javaee6
impl.ri.deploy.dir=${webcontainer.home.ri}/domains/domain1/autodeploy
impl.ri=glassfish.xml
impl.ri.host=${webServerHost.2}
impl.ri.port=${webServerPort.2}
----
--
+
The RI settings using the Apache Tomcat web container with the
Standalone JAX-WS 2.3 RI would be as follows:
+
--
[source,oac_no_warn]
----
webcontainer.home.ri=/tomcat
impl.ri=tomcat
impl.ri.deploy.dir=${webcontainer.home.ri}/webapps
impl.ri.host=${webServerHost.2}
impl.ri.port=${webServerPort.2}
----
--
+
  g.  Set the `webcontainer.home` property to the location where the web
  container for the Vendor Implementation is installed.
  h.  Set the `porting.ts.url.class.1` property to the porting
  implementation class that is used for obtaining URLs. +
  The default setting for the JAX-WS RI porting implementation is:
+
--
[source,oac_no_warn]
----
com.sun.ts.lib.implementation.sun.common.SunRIURL
----
--
+
  i.  Set the `user` and `password` properties to the user name and
  password used for the basic authentication tests. +
  The default setting for both is `j2ee`.
  j. Set the `authuser` and `authpassword` properties to the user name
  and password used for the basic authentication tests. +
  The default setting for both is `javajoe`.
  k. Set the `http.server.supports.endpoint.publish` property based on
  whether Endpoint Publish APIs are supported on the container.
  l. If using Java SE 8 or above, verify that the property
  `endorsed.dirs` is set to the location of the RI API jars for those
  technologies you wish to override. Java SE 8 contains an implementation
  of JAX-WS 2.2 which will conflict with JAX-WS 2.3, therefore this
  property must be set so that JAX-WS 2.3 will be used during the building
  of tests and during test execution.
+
3.  Edit the catalog file
`<TS_HOME>/src/com/sun/ts/tests/jaxws/common/xml/catalog /META-INF/jax-ws-catalog.xml`,
replacing the host and port settings of `systemId` with the value of
your host and port setting where the WSDL is published.
4.  Provide your own implementation of the porting package interface
provided with the JAX-WS TCK. +
The porting package interface, `TSURLInterface.java`, obtains URL
strings for web resources in an implementation-specific manner. API
documentation for the `TSURLInterface.java` porting package interface is
available in the JAX-WS TCK documentation bundle. +
The `<TS_HOME>/bin/jaxws-url-props.dat` file contains the webservice
endpoint and WSDL URLs that the TCK tests use when running against the
JAX-WS RI. In the porting package that the TCK uses, the URLs are
returned as is since this is the form that the JAX-WS RI expects. You
may need an alternate form of these URLs in order to run the TCK tests
in your environment. However, you MUST NOT modify the
`jaxws-url-props.dat` file, but instead make any necessary changes in
your own porting implementation class to transform the URLs
appropriately for your environment.


[[additional-tck-instructions]]

4.3 Additional JAX-WS TCK Instructions
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

[[GCLHF]][[configuring-your-environment-to-rebuild-and-run-the-jax-ws-tck-against-the-jax-ws-ri]]

4.3.1 Configuring Your Environment to Rebuild and Run the JAX-WS TCK Against the JAX-WS RI
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This section describes the steps needed to configure the JAX-WS TCK so
that the tests can be rebuilt (using the Vendor Implementation toolset),
and then deployed and run against the JAX-WS Reference Implementation.

If you are not ready to proceed with this portion of the testing
process, skip this section for now and proceed to
link:#GCFOI[Configuring and Starting Your Application Server or
Servers]. After configuring your environment, continue with the
instructions in link:#GBFUY[Using the JavaTest Harness Software].


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<TS_HOME>` becomes `$TS_HOME` on
Solaris/Linux and `%TS_HOME%` on Windows XP/Vista. In addition, the
forward slashes (`/`) used in all of the examples need to be replaced
with backslashes (`\`) for Windows XP/Vista. Finally, be sure to use the
appropriate separator for your operating system when specifying multiple
path entries (`;` on Windows, `:` on UNIX/Linux).

On Windows, you must escape any backslashes with an extra backslash in
path separators used in any of the following properties, or use forward
slashes as a path separator instead. For example, if the Java EE 8 RI
installation is `C:\JavaEE8`, you must specify it as `C:\\JavaEE8` or
`C:/JavaEE8`.

=======================================================================


1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE is installed
  b.  `TS_HOME` to the directory in which the JAX-WS TCK 2.3 software is
  installed
  c.  `ANT_HOME` should not be set in your environment. If it is set,
  unset it.
2.  Edit your `<TS_HOME>/bin/ts.jte` file and set the following
environment variables:
  a.  Set the `webServerHost` property to the hostname where the web
  server for the Vendor Implementation is running. +
  The default setting is `localhost`.
  b.  Set the `webServerPort` property to the port number where the web
  server for the Vendor Implementation is running. +
  The default setting is`8080`
  c.  Set the `webServerHost.2` property to the hostname where the web
  server for the JAX-WS RI is running.
  d.  Set the `webServerPort.2` property to the port number where the web
  server for the JAX-WS RI is running.
  e.  Set the `jaxws.home` property to the location where the Vendor
  Implementation is installed. +
  The default setting is `${webcontainer.home}`. The value of this
  property must match the value of the `webcontainer.home` property that
  is set in step link:#BABEGFEB[m].
  f.  Set the `jaxws.classes` property to point to the Vendor
  Implementation classes/JAR files.
  g.  Set the `jaxws.home.ri` property to the location where the JAX-WS RI
  is installed. +
  The default setting is `${webcontainer.home.ri}`. The value of this
  property must match the value of the `webcontainer.home.ri` property
  that is set in step link:#BABCAAFJ[n].
  h.  The `jaxws.classes.ri` property is already configured to point to
  the JAX-WS RI classes/JAR files. +
  No changes are necessary for this property.
  i.  Set the `wsgen.ant.classname` property to the Vendor Implementation
  class that mimics the JAX-WS RI Ant task, which in turn calls the
  `wsgen` Java-to-WSDL tool.
  j. Set the `wsimport.ant.classname` property to the Vendor
  Implementation class that mimics the JAX-WS RI Ant task, which in turn
  calls the `wsimport` WSDL-to-Java tool.
  k. Set the `impl.vi`, `impl.vi.deploy.dir`, `impl.vi.host`, and
  `impl.vi.port` properties to the supported web container, deploy
  directory, host and port used for the Vendor Implementation.
  l. Set the `impl.ri`, `impl.ri.deploy.dir`, `impl.ri.host`, and
  `impl.ri.port` properties to the supported web container, deploy
  directory, host and port used for the JAX-WS Reference Implementation. +
  The supported web container for standalone web applications is the Java
  EE 8 RI. The default settings for these properties are as follows:
+
--
[source,oac_no_warn]
----
impl.ri.deploy.dir=${webcontainer.home.ri}/domains/domain1/autodeploy
impl.ri=glassfish.xml
impl.ri.host=${webServerHost.2}
impl.ri.port=${webServerPort.2}
----
--
+
  m. Set the `webcontainer.home` property to the location where the web
  container for the Vendor Implementation is installed.
  n. Set the `webcontainer.home.ri` property to the location where the web
  container for the JAX-WS Reference Implementation is installed.
  o. Set the `porting.ts.url.class.1` property to your porting
  implementation class that is used for obtaining URLs. +
  The default setting points to the JAX-WS RI porting implementation which
  is:
+
--
[source,oac_no_warn]
----
com.sun.ts.lib.implementation.sun.common.SunRIURL
----
--
+
  p. Set the `porting.ts.url.class.2` property to the JAX-WS RI porting
  implementation class that is used for obtaining URLs. +
  No changes are necessary for this property.
  q. Set the `user` and `password` properties to the user name and
  password used for the basic authentication tests. +
  The default setting for both is `j2ee`.
  r. Set the `authuser` and `authpassword` properties to the user name
  and password used for the basic authentication tests. +
  The default setting for both is `javajoe`.
  s. Set the `http.server.supports.endpoint.publish` property based on
  whether Endpoint Publish APIs are supported on the container.
  t. If using Java SE 8 or above, verify that the property
  `endorsed.dirs` is set to the location of the VI API jars for those
  technologies you wish to override. Java SE 8 contains an implementation
  of JAX-WS 2.2 which will conflict with JAX-WS 2.3, therefore this
  property must be set so that JAX-WS 2.3 will be used during the building
  of tests and during test execution.
  u. If using Java SE 8 or above, verify that the property
  `endorsed.dirs.ri` is set to the location of the RI API jars for those
  technologies you wish to override. Java SE 8 contains an implementation
  of JAX-WS 2.2 which will conflict with JAX-WS 2.3, therefore this
  property must be set so that JAX-WS 2.3 will be used during the building
  of tests and during test execution.
+
3.  Edit the catalog file
`<TS_HOME>/src/com/sun/ts/tests/jaxws/common/xml/catalog /META-INF/jax-ws-catalog.xml`,
replacing the host and port settings of `systemId` with the value of
your host and port setting where the WSDL is published.



[[GCLIK]][[configuring-your-environment-to-simultaneously-run-the-jax-ws-tck-against-the-vi-and-the-jax-ws-ri]]

4.3.2 Configuring Your Environment to Simultaneously Run the JAX-WS TCK Against the VI and the JAX-WS RI
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This section describes the steps needed to configure the JAX-WS TCK so
that all tests can be run; forward tests against the Vendor
Implementation and reverse tests against the JAX-WS Reference
Implementation.

Since the JAX-WS TCK needs to be tested against both the JAX-WS
Reference Implementation and the Vendor Implementation, two separate Web
servers need to be configured. Two individual Web servers are required,
and the same steps, below, must be performed to configure each Web
server.

If you are not going to perform this kind of testing at this time, skip
this section and proceed to link:#GCFOI[Configuring and Starting Your
Application Server or Servers], otherwise perform the steps described in
the following sections:

* link:#GCLHU[Configuring Your Environment to Run the JAX-WS TCK Against
the Vendor Implementation]
* link:#GCLHF[Configuring Your Environment to Rebuild and Run the JAX-WS
TCK Against the JAX-WS RI]


[[GCFOI]][[configuring-and-starting-your-application-server-or-servers]]

4.3.3 Configuring and Starting Your Application Server or Servers
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Complete the following two procedures to configure your application
server environments for the RI and VI.

[[GCFOC]][[to-configure-the-vendor-implementation-as-your-vi-environment]]

==== 4.3.3.1 To Configure the Vendor Implementation as your VI Environment

1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE is installed
  b.  `TS_HOME` to the directory in which the JAX-WS TCK 2.3 software is
  installed
  c.  `ANT_HOME` should not be set in your environment. If it is set,
  unset it.
2.  Ensure that the `ts.jte` settings for Vendor specific properties
have been configured.
3.  Run the `ant config.vi` target to configure for the Vendor
Implementation.
+
--
[source,oac_no_warn]
----
cd <TS_HOME>/bin
<TS_HOME>/tools/ant/bin/ant config.vi
----
--

[[GCFFB]][[to-configure-the-jax-ws-reference-implementation-as-your-ri-environment]]

==== 4.3.3.2 To Configure the JAX-WS Reference Implementation as your RI Environment

1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE is installed
  b.  `TS_HOME` to the directory in which the JAX-WS TCK 2.3 software is
  installed
  c.  `ANT_HOME` should not be set in your environment. If it is set,
  unset it.
2.  Ensure that the `ts.jte` settings for RI specific properties have
been configured.
3.  Run the `ant config.ri` target to configure for RI implementation.
+
--
[source,oac_no_warn]
----
cd <TS_HOME>/bin
<TS_HOME>/tools/ant/bin/ant config.ri
----
--

[[GCLIN]][[deploying-the-jax-ws-tck-tests]]

4.3.4 Deploying the JAX-WS TCK Tests
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The JAX-WS TCK provides an automatic way of deploying both the prebuilt
and Vendor-built archives to the configured web container or container
by using deployment handlers.

The handler file (`<TS_HOME>/bin/xml/impl/glassfish/deploy.xml`) is
written to be used with the Java EE 8 RI. If the Vendor chooses not to
use Java EE 8 RI with their implementation, or chooses to rebuild the
JAX-WS TCK tests using some other method than the infrastructure
provided, they should create their own version handler file to provide
this functionality.

This section describes the various commands used for deploying the WAR
files to the configured web container.

* link:#GCLJG[Generic Deployment Command Scenarios]
* link:#GCLIW[Deploying the JAX-WS TCK Prebuilt Archives]
* link:#GCLIL[Deploying the Rebuilt JAX-WS TCK Tests Against the JAX-WS
Reference Implementation]

[[GCLJG]][[generic-deployment-command-scenarios]]

==== 4.3.4.1 Generic Deployment Command Scenarios

The `keywords` system property enables you to deploy a subset of the
tests that would normally be deployed in batch mode by means of
`<TS_HOME>/tools/ant/bin/ant deploy`. To specify it, add the option
`-Dkeywords=`value to the `ant` command, where value is either
`forward`, `reverse`, or `all`. The supported values control the
directions in which the rebuildable tests are deployed.

* Setting this property to `all` (the default) deploys both the prebuilt
and Vendor build tests.
* Setting the property to `forward` deploys the prebuilt tests in the
forward direction only.
* Setting the property to `reverse` deploys the Vendor rebuilt tests in
the reverse direction only.

[[GCLJK]][[to-deploy-all-the-war-files-from-the-ts_homedist-directory-to-both-web-servers]]

===== 4.3.4.1.1 To Deploy all the WAR Files From the `<TS_HOME>/dist` Directory to Both Web Servers

Enter the following command:

[source,oac_no_warn]
----
<TS_HOME>/tools/ant/bin/ant deploy.all
----

or

[source,oac_no_warn]
----
<TS_HOME>/tools/ant/bin/ant -Dkeywords=all deploy.all
----

[[GCLII]][[to-deploy-a-single-test-directory-in-the-forward-direction]]

===== 4.3.4.1.2 To Deploy a Single Test Directory in the Forward Direction

Enter the following commands:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/jaxws/api/javax_xml_ws/Dispatch 
<TS_HOME>/tools/ant/bin/ant -Dkeywords=forward deploy
----

[[GCLJC]][[to-deploy-a-subset-of-test-directories-in-the-reverse-direction]]

===== 4.3.4.1.3 To Deploy a Subset of Test Directories in the Reverse Direction

Enter the following commands:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/jaxws/api 
<TS_HOME>/tools/ant/bin/ant -Dkeywords=reverse deploy
----


[NOTE]
=======================================================================

The `-Dkeywords` option is supported by the `deploy`, `undeploy`,
`deploy.all`, and `undeploy.all` commands.

=======================================================================


[[GCLIW]][[deploying-the-jax-ws-tck-prebuilt-archives]]

==== 4.3.4.2 Deploying the JAX-WS TCK Prebuilt Archives

This section explains issues regarding the deployment of the JAX-WS TCK
prebuilt archives. Before conducting any deployment, ensure that your
environment has been configured by following the instructions in either
the link:#GBFVU[Configuring Your Environment to Run the JAX-WS TCK
Against the JAX-WS Reference Implementation] or the
link:#GCLHU[Configuring Your Environment to Run the JAX-WS TCK Against
the Vendor Implementation] sections.

The `<TS_HOME>/dist` directory contains all the WAR files for the JAX-WS
TCK web service endpoint tests that have been compiled and generated
using the JAX-WS Reference Implementation and packaged for deployment on
a Servlet-compliant web container using the standard Web Archive (WAR)
format.

These WAR files contain only portable artifacts for all the TCK web
service endpoint tests, and are tailored to run against the JAX-WS
Reference Implementation via the `web.xml` file in addition to a runtime
file, `sun-jaxws.xml`. These WAR files allow you to deploy (without any
additional setup or modification) against the JAX-WS Reference
Implementation to test the various features and functionality of this
implementation.

A Vendor is required to deploy the prebuilt WAR files as is on their
JAX-WS implementation without any changes to the WAR archives with the
exception of replacing and/or removing only the `web.xml` and the
`sun-jaxws.xml` files.

To deploy the tests, the Vendor should perform a deployment using either
the `deploy` or `deployall` batch command as described in
link:#GCLJG[Generic Deployment Command Scenarios], and specify the
`-Dkeywords=forward` option.

[[GCLIL]][[deploying-the-rebuilt-jax-ws-tck-tests-against-the-jax-ws-reference-implementation]]

==== 4.3.4.3 Deploying the Rebuilt JAX-WS TCK Tests Against the JAX-WS Reference Implementation

This section describes how to deploy the Vendor rebuilt JAX-WS TCK tests
against the Vendor Implementation. Before conducting the deployment,
ensure that you have followed the instructions in
link:#GBFVU[Configuring Your Environment to Run the JAX-WS TCK Against
the JAX-WS Reference Implementation].

This deployment scenario assumes that the Vendor has rebuilt all the
JAX-WS TCK tests using the existing infrastructure, and that the WAR
files exist alongside the prebuilt war files in the `<TS_HOME>/dist`
directory. The rebuilt WAR files will be prepended with the text
`vi_built_`.

If the Vendor chooses some other method of rebuilding the tests, they
may still be able to use this deployment method as long as the WAR files
are built correctly and are prepended with the text `vi_built_`. Refer
to the link:rebuild.html#GCLIZ[Appendix B, "Rebuilding the JAX-WS TCK
Using the Vendor's Toolset"] to learn about rebuilding the JAX-WS TCK
tests.

To deploy the tests, the Vendor should perform a deployment using either
the `deploy` or `deployall` batch command, as described in
link:#GCLJG[Generic Deployment Command Scenarios], and specify the
`-Dkeywords=reverse` option
