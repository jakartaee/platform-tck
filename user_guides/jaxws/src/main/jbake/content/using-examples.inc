///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
These CLI examples can be customized as necessary.
///////////////////////////////////////////////////////////////////////

As explained in link:rebuild.html#GCLIZ[Appendix B],
the JAX-WS TCK introduces the
concept of rebuilding the TCK tests. To provide the user an ability to
run the TCK against the Vendor Implementation and the Reference
Implementation, the use of the `keywords` feature (in GUI mode) or
option `-Dkeywords=`value (in command line mode) is provided.

By default, the TCK is configured to run all tests in both directions
for all the tests except the signature tests. Setting `keywords` allows
the user to change which tests will be run.

* Setting the `keywords` property to `all` (the default) does not filter
out any tests, and results in the prebuilt tests to be run in the
forward direction, and the Vendor rebuilt tests to be run in the reverse
direction.
* Setting `keywords` to `forward` causes the prebuilt tests to be run in
the forward direction only.
* Setting `keywords` to `reverse` causes the Vendor rebuilt tests to be
run in the reverse direction only.

Refer to the JavaTest User's Guide and Reference in the documentation
bundle for information regarding how to configure the `keywords` feature
in GUI mode. For command line mode, add the following to your command
line `-Dkeywords=``value`, where value is either `forward`, `reverse`,
or `all`.

1.  Change to any subdirectory under `<TS_HOME>/src/com/sun/ts/tests`.
2.  Start JavaTest using the following command:
+
--
[source,oac_no_warn]
----
<TS_HOME>/tools/ant/bin/ant [-Dkeywords=forward|reverse|all] runclient 
----
--


[NOTE]
=======================================================================

The `-Dkeywords` option is supported by the `runclient` command in batch
mode only, not in GUI mode.

=======================================================================


[[TCJWS00012]][[GCMCU]]


Example 5-1 Running JAX-WS TCK Signature Tests

To run the JAX-WS TCK signature tests, enter the following commands:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/signaturetest/jaxws
<TS_HOME>/tools/ant/bin/ant [-Dkeywords=forward|reverse|all] runclient 
----

[[TCJWS00013]][[GCMBV]]


Example 5-2 Running a Single Test Directory

To run a single test directory in the `forward` direction, enter the
following commands:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/jaxws/api/javax_xml_ws/Dispatch 
<TS_HOME>/tools/ant/bin/ant -Dkeywords=forward runclient
----

[[TCJWS00014]][[GCMCA]]


Example 5-3 Running a Subset of Test Directories

To run a subset of test directories in the `reverse` direction, enter
the following commands:

[source,oac_no_warn]
----
cd <TS_HOME>/src/com/sun/ts/tests/jaxws/api 
<TS_HOME>/tools/ant/bin/ant -Dkeywords=reverse runclient
----
