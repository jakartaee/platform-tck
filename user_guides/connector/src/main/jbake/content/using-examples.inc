///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
These CLI examples can be customized as necessary.
///////////////////////////////////////////////////////////////////////

1.  Change to any subdirectory under `<TS_HOME>/src/com/sun/ts/tests`.
2.  Start JavaTest using the following command: +
[source,oac_no_warn]
----
ant runclient
----

[[GCMCU]]

Example 5-1 {TechnologyShortName} TCK Signature Tests

To run the {TechnologyShortName} TCK signature tests, enter the
following commands:

[source,subs="attributes"]
----
cd {sigTestDirectoryExample}
ant runclient
----

[[GCMBV]]


Example 5-2 Single Test Directory

To run a single test directory, enter the following commands:

[source,subs="attributes"]
----
cd {singleTestDirectoryExample}
ant runclient
----

[[GCMCA]]


Example 5-3 Subset of Test Directories

To run a subset of test directories, enter the following commands:

[source,subs="attributes"]
----
cd {subsetTestDirectoryExample}
ant runclient
----

Example 5-4 MDB-Specific JCA TCK Tests

To run the MDB-specific JCA TCK tests, enter the following commands:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/connector/mdb
ant runclient
----

Alternatively, from the <TS_HOME>/src/com/sun/ts/tests/connector
directory, you can also run the MDB-specific JCA TCK tests by using the
appropriate keyword:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/connector
ant -Dkeywords="connector_mdb_optional" runclient
----


Example 5-5 EJB and Servlet Resource Definition Annotation Tests

To run the JCA TCK EJB and Servlet Resource Definition Annotation Tests,
enter the following commands:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/connector/resourceDefs
ant runclient
----

Alternatively, from the <TS_HOME>/src/com/sun/ts/tests/connector
directory, you can also run the JCA TCK Web tests by using the
appropriate keyword:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/connector
ant -Dkeywords=  "(connector_resourcedef_ejb_optional | connector_resourcedef_servlet_optional)"
  runclient
----


Example 5-6 All JCA TCK Tests Except the EJB and Servlet Resource
Definition Annotation Tests

From the <TS_HOME>/src/com/sun/ts/tests/connector directory, you can run
all the JCA TCK tests except the EJB and Servlet Resource Definition
Annotation Tests by using the appropriate keyword:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/connector
ant -Dkeywords=
  "!(connector_resourcedef_ejb_optional | connector_resourcedef_servlet_optional)"
  runclient
----
