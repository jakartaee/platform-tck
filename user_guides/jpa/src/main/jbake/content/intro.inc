
[[GEQNJ]][[additional-jpa-notes]]

1.2.7 Additional JPA Notes
^^^^^^^^^^^^^^^^^^^^^^^^^^

In addition to the usual signature, API, and end-to-end tests,
the JPA TCK also includes pluggability tests verify that the
implementation under test can use third-party persistence providers
instead of the one provided by the implementation.

[NOTE]
=======================================================================

In the JPA 2.0 TCK, the tests were located in the
`src/com/sun/ts/tests/ejb30/persistence` directory. In the JPA 2.1 and
JPA 2.2 TCK, the tests have been reorganized and are now in a new
location: `src/com/sun/ts/tests/jpa`.

In the JPA 2.0 TCK, the pluggability tests required special setup in
order to be run. This is no longer the case; the JPA 2.1 and JPA 2.2 TCK
now executes the pluggability tests along with all the other JPA TCK
tests without any special setup. The pluggability tests have also been
rewritten to use a stubbed-out JPA implementation, which is located in
the `src/com/sun/ts/jpa/common/pluggibility/altprovider` directory.

=======================================================================

