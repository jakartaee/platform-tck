///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
This include file allows for additional introductory material
specific to a TCK.  For example, the following is used for the
JASPIC TCK.
///////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////
[[GEQNJ]][[jaspic-technology-overview]]

1.2.7 JASPIC Technology Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The JASPIC 1.1 Specification (JSR 196) defines a service provider
interface (SPI) by which authentication providers implementing message
authentication mechanisms can be integrated in client and server message
processing runtimes or containers.

The JASPIC TCK uses a Test Suite SPI Verifier (TSSV) to verify whether
the vendor's message processing runtimes invoke the correct SPI in the
proper order.

TSSV includes test suite implementations of:

* `AuthConfigFactory`
* `AuthConfigProvider`
* `AuthConfigClient`, `AuthConfigServer`
* `AuthContextClient`, `AuthContextServer`
* `AuthenticationModulesClient`, `AuthenticationModules Server`

TSSV gets loaded into vendor's message processing runtime using one of
the following ways, as defined by the JASPIC 1.1 Specification:

* By defining a property in `JAVA_HOME/jre/lib/security/java.security`
as follows:
`authconfigprovider.factory=com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory`
* By calling the `registerConfigProvider()` method in a vendor's
`AuthConfigFactory` with the following values:

** Test Suite Provider ClassName

** Map of properties

** Message Layer (such as `SOAP` or `HttpServlet`)

** Application Context Identifier

** A description of the provider


[NOTE]
=======================================================================

For the JASPIC TCK, more than one provider is registered in the vendor's
message processing runtime.

=======================================================================


In a typical test scenario (for each profile of Servlet or SOAP), an
application is deployed into a vendor's runtime, and a client invokes
the service. The message policies required for the secure invocations
are built into TSSV implementations, and the runtime is analyzed to see
whether it invokes the correct SPIs at the proper time.

TSSV uses Java logging APIs to log the client and server invocation into
a log file (`TSSVLog.txt`), this log file is used by the TCK tests to
validate actual logged runtime information against expected results to
ensure that the runtime is compliant. The `jaspic_util_web.war` file
contains the JASPIC log file processor, which writes output to the
`TSSVLog.txt` file. The `TSSVLog.txt` file is put into the location
defined by the `log.file.location` property in the `ts.jte` file.

[[GKXQP]][[jaspic-tssv-files]]

1.2.8 JASPIC TSSV Files
^^^^^^^^^^^^^^^^^^^^^^^

The following sections describe the `tssv.jar`,
`ProviderConfiguration.xml`, and `provider-configuration.xsd` files that
are used by the JASPIC TCK tests.

[[GKXUX]][[tssv.jar-file]]

1.2.8.1 tssv.jar file
+++++++++++++++++++++

The `tssv.jar` file contains classes necessary for populating a vendor
implementation with a CTS AuthConfigFactory (ACF) as well as information
used to register CTS providers. The `tssv.jar` file contains the class
files for the Test Suite SPI Verifier. The `tssv.jar` file classes need
to be loaded by the vendor implementation runtime during startup.

[[GKXSG]][[providerconfiguration.xml-file]]

1.2.8.2 ProviderConfiguration.xml file
++++++++++++++++++++++++++++++++++++++

The format is a test suite-specific format.  The file was designed to
contain test provider information the test suite uses to populate the
ACF with a list of providers for testing. The file needs to be copied to
the location specified in the `ts.jte` file by the
`provider.configuration.file` property. An edit to the
`ProviderConfiguration.xml` file may be required for the vendor
implementation. The current application context Ids are generic and
should work as is, but there could be some scenarios in which the
application Context Ids may need to be modified.

The value of the `<app-context-id>` element in the
`ProviderConfiguration.xml` file should reflect what the vendor
implementation will use for its internal representation of the
application context identifier for a registered provider. Said
differently, the test suite registers its providers with information
from the `ProviderConfiguration.xml` file but every implementation is
not guaranteed to use the application context identifier that is used in
the call to register the configuration provider. This value of the
`<app-context-id>` element corresponds to the `appContext` argument in
the `AuthConfigFactory.registerConfigProvider()` API. The API
documentation for this method indicates that the `appContext` argument
may be used but is not guaranteed to be used.

The default `ProviderConfiguration.xml` file should work without
modification, but a vendor may need to alter the value of the
`<app-context-id>` element as previously described to accommodate the
implementation under test. Vendors need to find the correct application
context identifier for their implementation.

Vendors should enable two levels of logging output to get finer levels
of debugging and tracing information than is turned on by default. This
is done by setting the `traceflag` property in the `ts.jte` file to
"true" and setting the HARNESS_DEBUG environment variable to "true". If
both of these are set, the debug output should contain application
context identifier information.

[[GKXQN]][[provider-configuration.xsd-file]]

1.2.8.3 provider-configuration.xsd file
+++++++++++++++++++++++++++++++++++++++

The `provider-configuration.xsd` file is a schema file that resides in
the same directory as the `ProviderConfiguration.xml` file and describes
the `ProviderConfiguration.xml` file. This file should not be edited.

thref4]]


[[baseline-compatibility-requirements]]
1.2.9 Baseline Compatibility Requirements
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To obtain Baseline compliance, a vendor must meet the Baseline
Compatibility requirements.

[[GKXRJ]][[servlet-profile-tests]]

1.2.10 Servlet Profile Tests
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To obtain Servlet Profile compliance, a vendor must meet the Baseline
Compatibility requirements as well as the Servlet Profile requirements.

[[GEQME]][[soap-profile-tests]]

1.2.11 SOAP Profile Tests
^^^^^^^^^^^^^^^^^^^^^^^^^

Since various SOAP implementations are possible in a vendor's message
processing runtime, the JASPIC TCK considers the following SOAP
implementations:

* SOAP implementation in a Java EE environment
* SOAP implementation in standalone container (non-Java EE)
* Non-container based SOAP implementation

For SOAP profile tests, the client invocations of webservice have been
abstracted into two different types:

* Invocations of Service in a Java EE environment (for example, using
JAXWS annotations `@WebServiceRef` for looking up the service and
`@WebService` for service definition).
* Invocations of Service in a standalone (i.e. non-Java EE) environment
(this includes standalone container and non-container based
implementation). +
The following are used to get the service reference:

** WSDL

** Service `QName` (for example, `QNAME(NAMESPACEURI, SERVICENAME)`)

** Service Class (such as `HelloService.class`)

** PORT `QName` (for example, `QName(NAMESPACEURI, PORT_NAME)`)

** Service Endpoint Interface class (for example, `Hello.class`)

The deployment abstraction for handling various SOAP implementations are
handled in the following ways:

* A JSR 88–compliant deployment is used for Java EE-based
implementations. This is differentiated by using a different deliverable
class,
`deliverable.class=com.sun.ts.lib.deliverable.jaspic.JaspicJavaEEDeliverable`,
which is configurable in the `ts.jte` file. Vendors need to write their
own `Deliverable` class that can be used to deploy in their environment.
* For standalone implementations (this includes container and
non-container based implementations), a different deliverable class is
used,
`deliverable.class=com.sun.ts.lib.deliverable.jaspic.JaspicDeliverable`.
Along with this deliverable class an Ant file,
`TS_HOME/bin/xml/deploy.xml`, is used to deploy in GlassFish Server.
Vendors are expected to implement the `deploy` and `undeploy` targets in
`deploy.xml` to suite their environment. +

[NOTE]
=======================================================================

Two deliverable implementations are provided with the GlassFish server.
One implementation, for non-Java EE servers, turns off auto deployment
and leaves the deployment up to the licensee by way of an Ant target.

=======================================================================

* Along with the deliverable class, a configurable property in the
`ts.jte` file, `platform.mode`, is used to distinguish the different
SOAP implementations.

** `platform.mode=javaEE` (for Java EE based implementation)

** `platform.mode=standalone` +

[NOTE]
=======================================================================

A deployable EAR , WAR, or JAR file is created, based on the value
specified by the `platform.mode` property in the `ts.jte` file.

=======================================================================

* For non-container based standalone SOAP implementations, vendors are
expected to deploy the service and make it available for client
invocations. For this purpose, a no-op for `deploy` and `undeploy`
targets can be implemented in the `deploy.xml` file.

The JASPIC TCK uses Web Services (JSR 181) metadata-based annotations to
define web service applications. Although JSR 181 support is not
required in a vendor's SOAP implementation, using web services metadata
simplifies the definition of web services and the linking between
various artifacts of web services (the WSDL, `ServiceEndpoint`, and
implementation and their associations). Using other forms of web
services implementation will lead to separate binding files, web
services description files (`webservices.xml`) which are different for
different SOAP implementations, such as a Java EE-based SOAP
implementation, standalone implementation, and so on.

Since vendors are already expected to generate web service artifacts
using `wsgen` and `wsimport` tools, writing an annotation processor to
support JSR 181–based annotations is just a step further towards making
a better SOAP implementation. Also having annotated web services helps
vendors generate different artifacts that suit their SOAP
implementation.


[NOTE]
===================================================================

For Java EE-based SOAP implementations, JSR 181 support is required.

===================================================================

///////////////////////////////////////////////////////////////////////
