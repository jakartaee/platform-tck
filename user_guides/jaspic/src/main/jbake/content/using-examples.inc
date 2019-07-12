///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
These CLI examples can be customized as necessary.
///////////////////////////////////////////////////////////////////////

1.  Change to the directory `<TS_HOME>/src/com/sun/ts/tests/jaspic`.
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


Example 5-2 {TechnologyShortName} TCK Mandatory Tests

To run the {TechnologyShortName} TCK mandatory tests, enter the
following commands:

[source,subs="attributes"]
----
cd <TS_HOME>/src/com/sun/ts/tests/jaspic
ant -Dkeywords="jaspic_servlet | jaspic_baseline" runclient
----

[[GCMCA]]


Example 5-3 {TechnologyShortName} TCK  Optional Tests

To run the {TechnologyShortName} optional TCK tests, enter the
following commands:

[source,subs="attributes"]
----
cd <TS_HOME>/src/com/sun/ts/tests/jaspic
ant -Dkeywords="jaspic_soap_optional" clean build package runclient
----


A {TechnologyShortName} implementation can implement one or more of the profiles
specified in the specification. To accommodate this, the {TechnologyShortName} TCK
tests use the JavaTest `keyword` feature, which enables vendors to run
tests that are relevant to their implementation.

For example, a vendor who implements only a `servlet` profile can run
all tests specific to the `servlet` profile.

[source,oac_no_warn]
----
ant -Dkeywords="jaspic_core | jaspic_servlet" runclient 
----

As another example, for running tests relevant to a `soap` profile, you
could use the following command:

[source,oac_no_warn]
----
ant -Dkeywords="jaspic_core | jaspic_soap" runclient 
----

The keywords that can be used are:

* `jaspic_core`
* `jaspic_servlet`
* `jaspic_soap`
