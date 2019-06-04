///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
The following sections should be customized for the technology.
This text was originally from the JAX-RS TCK.  Most references
to JAX-RS have been parameterized to serve as a simple starting
point for customization.  There are still many details that will
need to be changed or removed.
///////////////////////////////////////////////////////////////////////

[[GCLIZ]]

[[b-rebuilding-the-jax-ws-tck-using-the-vendors-toolset]]
B Rebuilding the JAX-WS TCK Using the Vendor's Toolset
------------------------------------------------------

The JAX-WS 2.3 specification requires that each implementation has a way
to generate WSDL from Java, and to generate Java from WSDL. To verify
that implementations do this in a compatible manner, half of the tests
in the JAX-WS TCK require that you first rebuild them using your
generation tools.

This appendix contains the following sections:

* link:#GCLIO[Overview]
* link:#GCLLW[Rebuilding the JAX-WS TCK Classes Using Ant]
* link:#GCLOO[Rebuilding the JAX-WS TCK Classes Manually]
* link:#GCQJY[wsgen Reference]
* link:#GCQMM[wsimport Reference]

[[GCLIO]][[b.1-overview]]

B.1 Overview
~~~~~~~~~~~~

The set of prebuilt archives and classes that ship with the JAX-WS TCK
were built using the JAX-WS Reference Implementation tools
(link:#GCQJY[wsgen Reference] and link:#GCQMM[wsimport Reference]), and
must be deployed and run against your implementation of JAX-WS. These
tests are referred to as `forward` tests.

You must also rebuild the archives and classes associated with these
tests using your generation tools, and then deploy and run them against
the JAX-WS Reference Implementation. These tests are known as `reverse`
tests. The test names of all the tests that will be run against the
Reference Implementation are identical to the `forward` test names found
in the client Java source files, with the added suffix of `_reverse`.
Essentially, for each `forward` test, there is an identical `reverse`
test. This ensures that the same behaviors are verified on each JAX-WS
implementation.


[NOTE]
=======================================================================

The same test client source file is used for each `forward` and
`reverse` test. Likewise, they also share the same test description,
which only appears under the `forward` test name in the client Java
source file.

=======================================================================


To be able to run the entire test suite in a single run, you must have
your implementation and the Reference Implementation configured
simultaneously. Please see link:config.html#GCLIK[Configuring Your
Environment to Simultaneously Run the JAX-WS TCK Against the VI and the
JAX-WS RI] for more information.

[[GCLLW]][[b.2-rebuilding-the-jax-ws-tck-classes-using-ant]]

B.2 Rebuilding the JAX-WS TCK Classes Using Ant
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Instead of rebuilding and overwriting the TCK prebuilt classes and
archives for each test directory, the JAX-WS TCK provides a way for you
to plug in your generation tools so that you may leverage the existing
build infrastructure that creates new classes and archives alongside
those that ship with the JAX-WS TCK.

1.  Create your own version of the Ant tasks `WsImport` and `WsGen`. +
Documentation for these tasks can be found later in this appendix:
* link:#GCQJY[wsgen Reference]
* link:#GCQMM[wsimport Reference]
2.  Set the `wsimport.ant.classname` and `wsgen.ant.classname`
properties in `<TS_HOME>/bin/ts.jte` to point to your implementations of
the above two tasks.
3.  If using Java SE 8 or above:
  a.  Verify that the property `endorsed.dirs` is set to the location of
  the VI API jars for those technologies you wish to override. Java SE 8
  contains an implementation of JAX-WS 2.2 which will conflict with JAX-WS
  2.3, therefore this property must be set so that JAX-WS 2.3 will be used
  during the building of tests and during test execution.
  b.  Set the `java.endorsed.dirs` property in the `ANT_OPTS` environment
  variable to point to the location in which only the JAX-WS 2.3 API jars
  exist; for example:
+
--
[source,oac_no_warn]
----
ANT_OPTS="-Djava.endorsed.dirs=PATH_TO_YOUR_ENDORSED_DIR"
----
--
+
4.  Change to the `jaxws` test directory and execute the following build
target:
+
--
[source,oac_no_warn]
----
<TS_HOME>/tools/ant/bin/ant -Dbuild.vi=true clean build
----
--
+
The clean and build targets may be executed in any subdirectory under
`<TS_HOME>/src/com/sun/ts/tests/jaxws` as long as the `-Dbuild.vi=true`
system property is also set. Failure to set this property while invoking
these targets will result in the prebuilt classes and archives being
deleted and/or overwritten. +
After completing the steps above, rebuilt class files will appear under
`<TS_HOME>/classes_vi_built` so as not to affect class files that were
generated and compiled with the JAX-WS Reference Implementation. Rebuilt
archives will be prefixed with the string, `vi_built`, and will be
created in the same directory (under `<TS_HOME>/dist`) as those built
using the Reference Implementation.

[NOTE]
=======================================================================

None of the JAX-WS test client source code or the service endpoint
implementation test source code is to be altered in any way by a Vendor
as part of the rebuild process.

=======================================================================


[[GCLMZ]]


Example B-1 Rebuilding a Single Test Directory

To further illustrate this process, the example below walks you through
the rebuilding of a single test directory.

1.  Change to the
`<TS_HOME>/src/com/sun/ts/tests/jaxws/ee/w2j/document/literal/httptest`
directory.
2.  Run `<TS_HOME>/tools/ant/bin/ant llc`. +
The following is a listing of classes built using the JAX-WS RI.
+
--
[source,oac_no_warn]
----
$TS_HOME/tools/ant/bin/ant llc
/var/tmp/jaxwstck/classes/com/sun/ts/tests/jaxws/ee/w2j/document/literal/httptest
-------------------------------------------------------------------------------
total 60
-rw-r--r--   1 root root   13825 Apr 12 08:32 Client.class
-rw-r--r--   1 root root    2104 Apr 12 08:32 HelloImpl.class
-rw-r--r--   1 root root    1153 Apr 12 08:32 Hello.class
-rw-r--r--   1 root root     793 Apr 12 08:32 HelloOneWay.class
-rw-r--r--   1 root root     796 Apr 12 08:32 HelloRequest.class
-rw-r--r--   1 root root     799 Apr 12 08:32 HelloResponse.class
-rw-r--r--   1 root root    1564 Apr 12 08:32 HttpTestService.class
-rw-r--r--   1 root root    2845 Apr 12 08:32 ObjectFactory.class
drwxr-xr-x   3 root root     512 Apr 12 08:32 generated_classes/
-rw-r--r--   1 root root     293 Apr 12 08:32 package-info.class
drwxr-xr-x   3 root root     512 Apr 12 08:31 generated_sources/
----
--
+
3.  Run `<TS_HOME>/tools/ant/bin/ant lld`. +
This shows you the listing of archives built using the JAX-WS RI.
+
--
[source,oac_no_warn]
----
$TS_HOME/tools/ant/bin/ant lld
/var/tmp/jaxwstck/dist/com/sun/ts/tests/jaxws/ee/w2j/document/literal/httptest
-------------------------------------------------------------------------------
total 286
-rw-r--r--   1 root root  113318 Apr 12 08:32 WSW2JDLHttpTest.war
----
--
+
4.  Once your `<TS_HOME>/bin/ts.jte` file is configured and your
implementations of the `wsgen` and `wsimport` tasks are specified, run
the following command:
+
--
[source,oac_no_warn]
----
<TS_HOME>/tools/ant/bin/ant -Dbuild.vi=true build
----
--
+
This builds the classes and archives using your implementation. Once
this has been done successfully, proceed to the next step.
5.  Run `<TS_HOME>/tools/ant/bin/ant -Dbuild.vi=true llc`. +
This shows you the listing of classes (under
`<TS_HOME>/classes_vi_built`) built using your Implementation.
+
--
[source,oac_no_warn]
----
$TS_HOME/tools/ant/bin/ant -Dbuild.vi=true llc
/var/tmp/jaxwstck/classes_vi_built/com/sun/ts/tests/jaxws/ee/w2j/document/literal/httptest
-------------------------------------------------------------------------------
total 60
-rw-r--r--   1 root root    1153 Apr 12 12:01 Hello.class
-rw-r--r--   1 root root     793 Apr 12 12:01 HelloOneWay.class
-rw-r--r--   1 root root     796 Apr 12 12:01 HelloRequest.class
-rw-r--r--   1 root root     799 Apr 12 12:01 HelloResponse.class
-rw-r--r--   1 root root    1564 Apr 12 12:01 HttpTestService.class
-rw-r--r--   1 root root    2845 Apr 12 12:01 ObjectFactory.class
drwxr-xr-x   3 root root     512 Apr 12 12:01 generated_classes/
-rw-r--r--   1 root root     293 Apr 12 12:01 package-info.class
drwxr-xr-x   3 root root     512 Apr 12 12:01 generated_sources/
-rw-r--r--   1 root root    2104 Apr 12 08:33 HelloImpl.class
-rw-r--r--   1 root root   13825 Apr 12 08:33 Client.class
----
--
+
6.  Run `<TS_HOME>/tools/ant/bin/ant lld`. +
This shows the listing of all archives and JAX-WS RI deployment plan
descriptors for this test directory. Those built using your
implementation are prepended with `vi_built_`.
+
--
[source,oac_no_warn]
----
$TS_HOME/tools/ant/bin/ant lld
/var/tmp/jaxwstck/dist/com/sun/ts/tests/jaxws/ee/w2j/document/literal/httptest
-------------------------------------------------------------------------------
total 286
-rw-r--r--   1 root root   22676 Apr 12 12:01 vi_built_WSW2JDLHttpTest.war
-rw-r--r--   1 root root  113318 Apr 12 08:32 WSW2JDLHttpTest.war
----
--
+
7.  Running the `clean` target while specifying the `build.vi` system
property will only clean the classes and archives that you rebuilt. To
clean them, run:
+
--
[source,oac_no_warn]
----
<TS_HOME>/tools/ant/bin/ant -Dbuild.vi=true clean
----
--
+
Notice that the `vi_built` classes and archives are deleted.

[[sthref7]]

Next Steps

Once you have successfully built the archives using your implementation,
you can then proceed to the configuration section to learn how to deploy
these archives and how to run the `reverse` tests.

[[GCLOO]][[b.3-rebuilding-the-jax-ws-tck-classes-manually]]

B.3 Rebuilding the JAX-WS TCK Classes Manually
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

When rebuilding the JAX-WS TCK classes, it is strongly recommended that
you use the procedure described in the previous section,
link:#GCLLW[Rebuilding the JAX-WS TCK Classes Using Ant]. However, if
you choose not to use the existing Ant-based TCK infrastructure to
rebuild the tests, you can use the following procedure to rebuild the
classes manually.

1.  Run your tools in each of the JAX-WS test directories under
`<TS_HOME>/src/com/sun/ts/tests/jaxws`, being sure to place all newly
compiled classes under `<TS_HOME>/classes_vi_built`. +
Also be sure not to overwrite any of the compiled classes under
`<TS_HOME>/classes`.
2.  Use the existing customization files and/or any handler files that
exist in each of the test directories.
3.  Package the newly generated artifacts and all the other required
classes into new WAR files, prepeded with the string `vi_built_`. +
These WAR files should reside in the same directory with the prebuilt
WAR files under `<TS_HOME>/dist` directory.

[[sthref8]]

Next Steps

As part of the manual rebuild process, you may also need to modify some
of the following files. However, this is not recommended, since doing so
can result in the JAX-WS TCK not being able to be built or run the
prebuilt archives shipped with the TCK. The files you may need to modify
are:

* XML files in `<TS_HOME>/bin/xml`; these files are used to generate the
various WARs.
* Any `build.xml` file in `<TS_HOME>/src/com/sun/ts/tests/jaxws`.
* The `<TS_HOME>/src/com/sun/ts/tests/jaxws/common/common.xml` file,
which is the main build file used for the `jaxws` build process. This
`common.xml` file contains all the Ant tasks specific to invoking the
JAX-WS TCK `jaxws` tools.


[NOTE]
=======================================================================

None of the JAX-WS TCK test client source code or the service endpoint
implementation test source code is to be altered in any way by a Vendor
as part of the rebuild process.

=======================================================================


Once you have successfully built the archives, you can proceed to the
link:config.html#GBFVV[Chapter 4, "Setup and Configuration"] to learn how
to deploy these archives and how to run the reverse tests.

[[GCQJY]][[b.4-wsgen-reference]]

B.4 wsgen Reference
~~~~~~~~~~~~~~~~~~~

The `wsgen` tool generates JAX-WS portable artifacts used in JAX-WS Web
services. The tool reads a Web service endpoint class and generates all
the required artifacts for Web service deployment and invocation.

[[GCQKB]][[b.4.1-wsgen-syntax]]

B.4.1 wsgen Syntax
^^^^^^^^^^^^^^^^^^

[source,oac_no_warn]
----
wsgen [options] SEI
----

where SEI is the service endpoint interface implementation class.

[[sthref9]][[GCQJH]]

Table B-1 wsgen Command Syntax

[width="100%",cols="30%,70%",options="header",]
|=======================================================================
|Option |Description
|`-classpath` path |Specify where to find input class files.

|`-cp` path |Same as `-classpath` path.

|`-d` directory |Specify where to place generated output files.

|`-extension` |Allow vendor extensions (functionality not specified by
the specification). Use of extensions may result in applications that
are not portable or may not interoperate with other implementations.

|`-help` |Display help.

|`-keep` |Keep generated files.

|`-r` directory |Used only in conjunction with the `-wsdl` option.
Specify where to place generated resource files such as WSDLs.

|`-s` directory |Specify where to place generated source files.

|`-verbose` |Output messages about what the compiler is doing.

|`-version` |Print version information. Use of this option will ONLY
print version information; normal processing will not occur.

|`-wsdl[:`protocol`]` |By default `wsgen` does not generate a WSDL file.
This flag is optional and will cause `wsgen` to generate a WSDL file and
is usually only used so that the developer can look at the WSDL before
the endpoint is deployed. The protocol is optional and is used to
specify what protocol should be used in the `wsdl:binding`. Valid
protocols include: `soap1.1` and `Xsoap1.2`. The default is `soap1.1`.
`Xsoap1.2` is not standard and can only be used in conjunction with the
`-extension` option.

|`-servicename` name a|
Used only in conjunction with the `-wsdl` option. Used to specify a
particular `wsdl:service` name to be generated in the WSDL; for example:

[source,oac_no_warn]
----
-servicename "{http://mynamespace/}MyService"
----

|`-portname` name a|
Used only in conjunction with the `-wsdl` option. Used to specify a
particular `wsdl:port` name to be generated in the WSDL; for example:

[source,oac_no_warn]
----
 -portname "{http://mynamespace/}MyPort"
----

|=======================================================================


[[GCQJW]][[b.4.2-wsgen-ant-task]]

B.4.2 wsgen Ant Task
^^^^^^^^^^^^^^^^^^^^

An Ant task for the `wsgen` tool is provided along with the tool. The
attributes and elements supported by the Ant task are listed below.

[source,oac_no_warn]
----
<wsgen
   sei="..."
   destdir="directory for generated class files"
   classpath="classpath" | cp="classpath"
   resourcedestdir="directory for generated resource files such as WSDLs"
   sourcedestdir="directory for generated source files"
   keep="true|false"
   verbose="true|false"
   genwsdl="true|false"
   protocol="soap1.1|Xsoap1.2"
   servicename="..."
   portname="...">
   extension="true|false"
   <classpath refid="..."/>
</wsgen>
----

[[sthref10]][[GCQKN]]

Table B-2 wsgen Attributes and Elements

[width="100%",cols="20%,52%,28%",options="header",]
|=======================================================================
|Attribute |Description |Command Line
|`sei` |Name of the service endpoint interface implementation class.
|SEI

|`destdir` |Specify where to place output generated classes. |`-d`

|`classpath` |Specify where to find input class files. |`-classpath`

|`cp` |Same as `-classpath.` |`-cp`

|`resourcedestdir` |Used only in conjunction with the `-wsdl` option.
Specify where to place generated resource files such as WSDLs. |`-r`

|`sourcedestdir` |Specify where to place generated source files. |`-s`

|`keep` |Keep generated files. |`-keep`

|`verbose` |Output messages about what the compiler is doing.
|`-verbose`

|`genwsdl` |Specify that a WSDL file should be generated. |`-wsdl`

|`protocol` |Used in conjunction with `genwsdl` to specify the protocol
to use in the `wsdl:binding`. Value values are `soap1.1`or `Xsoap1.2`,
default is `soap1.1`. `Xsoap1.2`is not standard and can only be used in
conjunction with the `-extensions` option. |`-wsdl:soap1.1`

|`servicename` a|
Used in conjunction with the `genwsdl` option. Used to specify a
particular `wsdl:service` name for the generated WSDL; for example:

[source,oac_no_warn]
----
servicename="{http://mynamespace/}MyService"
----

 |`-servicename`

|`portname` a|
Used in conjunction with the `genwsdl` option. Used to specify a
particular `wsdl:portmame` name for the generated WSDL; for example:

[source,oac_no_warn]
----
portname="{http://mynamespace/}MyPort"
----

 |`-servicename`

|`extension` |Allow vendor extensions (functionality not specified by
the specification). Use of extensions may result in applications that
are not portable or may not interoperate with other implementations.
|`-extension`
|=======================================================================


The `classpath` attribute is a path-like structure
(`http://ant.apache.org/manual/using.html#path`) and can also be set by
using nested `<classpath>` elements. Before this task can be used, a
`<taskdef>` element needs to be added to the project as shown below.

[source,oac_no_warn]
----
<taskdef name="wsgen" classname="com.sun.tools.ws.ant.WsGen">
   <classpath path="jaxws.classpath"/>
</taskdef>
----

where `jaxws.classpath` is a reference to a path-like structure
(`http://ant.apache.org/manual/using.html#path`), defined elsewhere in
the build environment, and contains the list of classes required by the
JAX-WS tools.

[[GCQJI]][[b.4.3-wsgen-example]]

B.4.3 wsgen Example
^^^^^^^^^^^^^^^^^^^

[source,oac_no_warn]
----
<wsgen
   resourcedestdir=""
   sei="fromjava.server.AddNumbersImpl">
   <classpath refid="compile.classpath"/>
</wsgen>
----

[[GCQMM]][[b.5-wsimport-reference]]

B.5 wsimport Reference
~~~~~~~~~~~~~~~~~~~~~~

The `wsimport` tool generates JAX-WS portable artifacts, such as:

* Service Endpoint Interface (SEI)
* Service
* Exception class mapped from `wsdl:fault` (if any)
* Async Reponse Bean derived from response `wsdl:message` (if any)
* JAXB generated value types (mapped Java classes from schema types)

These artifacts can be packaged in a WAR file with the WSDL and schema
documents along with the endpoint implementation to be deployed.

The `wsimport` tool can be launched using the command line script
`wsimport.sh` (UNIX) or `wsimport.bat` (Windows). There is also an Ant
task to import and compile the WSDL. See the below for further details.

This section contains the following topics:

* link:#GCQLU[wsimport Syntax]
* link:#GCQLY[wsimport Ant Task]
* link:#GCQKP[wsimport Examples]

[[GCQLU]][[b.5.1-wsimport-syntax]]

B.5.1 wsimport Syntax
^^^^^^^^^^^^^^^^^^^^^

[source,oac_no_warn]
----
wsimport [options] wsdl
----

where wsdl is the WSDL file.

[[sthref11]][[GCQML]]

Table B-3 wsimport Command Syntax

[width="100%",cols="41%,59%",options="header",]
|=======================================================================
|Option |Description
|`-d` directory |Specify where to place generated output files.

|`-b` path |Specify external JAX-WS or JAXB binding files (Each `<file>`
must have its own `-b`).

|`-B` jaxbOption |Pass this option to JAXB schema compiler

|`-catalog` |Specify catalog file to resolve external entity references,
it supports `TR9401`, `XCatalog`, and OASIS XML Catalog format. Please
read the Catalog Support document or see the `wsimport_catalog` sample.

|`-extension` |Allow vendor extensions (functionality not specified by
the specification). Use of extensions may result in applications that
are not portable or may not interoperate with other implementations.

|`-help` |Display help.

|`-httpproxy:`host`:`port |Specify an HTTP proxy server (port defaults
to `8080`).

|`-keep` |Keep generated files.

|`-p` |Specifying a target package with this command-line option
overrides any WSDL and schema binding customization for package name and
the default package name algorithm defined in the specification.

|`-s` directory |Specify where to place generated source files.

|`-verbose` |Output messages about what the compiler is doing.

|`-version` |Print version information.

|`-wsdllocation` location |`@WebService.wsdlLocation` and
`@WebServiceClient.wsdlLocation` value.

|`-target` |Generate code for the specified version of the JAX-WS
specification. For example, a value of 2.0 generates code that is
compliant with the JAX-WS 2.0 Specification. The default value is 2.3.

|`-quiet` |Suppress `wsimport` output.

|`-XadditionalHeaders` |Map the headers not bound to request or response
message to Java method parameters.

|`-Xauthfile` |File to carry authorization information in the format
`http://username:password@example.org/stock?wsdl`. Default value is
`$HOME/.metro/auth`.

|`-Xdebug` |Print debug information.

|`-Xno-addressing-databinding` |Enable binding of W3C
`EndpointReferenceType` to Java.

|`-Xnocompile` |Do not compile generated Java files.

|`-XdisableSSLHostnameVerification` |Disables the SSL Hostname
verification while fetching the wsdls.
|=======================================================================


Multiple JAX-WS and JAXB binding files can be specified using the `-b`
option, and they can be used to customize various things, such as
package names and bean names. More information on JAX-WS and JAXB
binding files can be found in the customization documentation.

[[GCQLY]][[b.5.2-wsimport-ant-task]]

B.5.2 wsimport Ant Task
^^^^^^^^^^^^^^^^^^^^^^^

An Ant task for the `wsimport` tool is provided along with the tool. The
attributes and elements supported by the Ant task are listed below.

[source,oac_no_warn]
----
<wsimport
   wsdl="..."
   destdir="directory for generated class files"
   sourcedestdir="directory for generated source files"
   keep="true|false"
   extension="true|false"
   verbose="true|false"
   version="true|false"
   wsdlLocation="..."
   catalog="catalog file"
   package="package name"
   target="target release"
   binding="..."
   quiet="true|false"
   xadditionalHeaders="true|false"
   xauthfile="authorization file"
   xdebug="true|false"
   xNoAddressingDatabinding="true|false"
   xnocompile="true|false"
   <binding dir="..." includes="..." />
   <arg value="..."/>
   <xjcarg value="..."/>
   <xmlcatalog refid="another catalog file"/>>
</wsimport>
----

[[sthref12]][[GCQKO]]

Table B-4 wsimport Attributes and Elements

[width="100%",cols="31%,35%,34%",options="header",]
|=======================================================================
|Attribute |Description |Command Line
|`wsdl` |WSDL file. |`WSDL`

|`destdir` |Specify where to place output generated classes |`-d`

|`sourcedestdir` |Specify where to place generated source files, keep is
turned on with this option |`-s`

|`keep` |Keep generated files, turned on with `sourcedestdir` option
|`-keep`

|`verbose` |Output messages about what the compiler is doing |`-verbose`

|`binding` |Specify external JAX-WS or JAXB binding files |`-b`

|`extension` |Allow vendor extensions (functionality not specified by
the specification). Use of extensions may result in applications that
are not portable or may not interoperate with other implementations
|`-extension`

|`wsdllocation` |The WSDL URI passed through this option is used to set
the value of `@WebService.wsdlLocation` and
`@WebServiceClient.wsdlLocation` annotation elements on the generated
SEI and Service interface |`-wsdllocation`

|`catalog` |Specify catalog file to resolve external entity references,
it supports `TR9401`, `XCatalog`, and OASIS XML Catalog format.
Additionally, the Ant `xmlcatalog` type can be used to resolve entities.
See the `wsimport_catalog` sample for more information. |`-catalog`

|`package` |Specifies the target package |`-p`

|`target` |Generate code for the specified version of the JAX-WS
specification. For example, a value of 2.0 generates code that is
compliant with the JAX-WS 2.0 Specification. The default value is 2.3.
|`-target`

|`quiet` |Suppress `wsimport` output. |`-quiet`

|`xadditionalHeaders` |Map headers not bound to request or response
message to Java method parameters. |`-XadditionalHeaders`

|`xauthfile` |File to carry authorization information in the format
`http://username:password@example.org/stock?wsdl`. |`-Xauthfile`

|`xdebug` |Print debug information. |`-Xdebug`

|`xNoAddressingDatabinding` |Enable binding of W3C EndpointReferenceType
to Java. |`-Xno-addressing-databinding`

|`xnocompile` |Do not compile generated Java files. |`-Xnocompile`
|=======================================================================


The `binding` attribute is like a path-like structure
(`http://ant.apache.org/manual/using.html#path`) and can also be set by
using nested `<binding>` elements, respectively. Before this task can be
used, a `<taskdef>` element needs to be added to the project as shown
below.

[source,oac_no_warn]
----
<taskdef name="wsimport" classname="com.sun.tools.ws.ant.WsImport">
   <classpath path="jaxws.classpath"/>
</taskdef>
----

where `jaxws.classpath` is a reference to a path-like structure
(`http://ant.apache.org/manual/using.html#path`), defined elsewhere in
the build environment, and contains the list of classes required by the
JAX-WS tools.

[[GJIKN]][[b.5.2.1-nested-elements]]

B.5.2.1 Nested Elements
+++++++++++++++++++++++

`wsimport` supports the following nested element parameters.

* `binding`: To specify more than one binding file at the same time, use
a nested `<binding>` element, which has the same syntax as `<fileset>`.
See `http://ant.apache.org/manual/Types/fileset.html` for more
information.
* `arg`: Additional command line arguments passed to the `wsimport` Ant
task. For details about the syntax, see the arg section
(`http://ant.apache.org/manual/using.html#arg`) in the Ant manual. This
nested element can be used to specify various options not natively
supported in the `wsimport` Ant task. For example, currently there is no
native support for the `-XdisableSSLHostnameVerification` command-line
option for `wsimport`. This nested element can be used to pass `–X`
command-line options directly, as done with `–XadditionalHeaders`. To
use any of these features from the `wsimport` Ant task, you must specify
the appropriate nested `<arg>` elements.
* `xjcarg`: The usage of `xjcarg` is similar to that of the `<arg>`
nested element, except that these arguments are passed directly to the
XJC tool (JAXB Schema Compiler), which compiles the schema that the WSDL
references. For details about the syntax, see the arg section
(`http://ant.apache.org/manual/using.html#arg`) in the Ant manual.
* `xmlcatalog`: The xmlcatalog
(`http://ant.apache.org/manual/Types/xmlcatalog.html`) element is used
to resolve entities when parsing schema documents.

[[GCQKP]][[b.5.3-wsimport-examples]]

B.5.3 wsimport Examples
^^^^^^^^^^^^^^^^^^^^^^^

[source,oac_no_warn]
----
<wsimport
   destdir=""
   debug="true"
   wsdl="AddNumbers.wsdl"
   binding="custom.xml"/>
----

The above example generates client-side artifacts for `AddNumbers.wsdl`
and stores `.class` files in the destination directory using the
`custom.xml` customization file. The `classpath` used is `xyz.jar` and
compiles with debug information on.

[source,oac_no_warn]
----
<wsimport
   keep="true"
   sourcedestdir=""
   destdir=""
   wsdl="AddNumbers.wsdl">
   <binding dir="${basedir}/etc" includes="custom.xml"/>
</wsimport>
----

The above example generates portable artifacts for `AddNumbers.wsdl`,
stores `.java` files in the destination directory, and stores `.class`
files in the same directory.


