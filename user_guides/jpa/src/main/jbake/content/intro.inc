
[[GEQNJ]][[additional-jpa-notes]]

1.2.7 Additional {TechnologyShortName} Notes
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In addition to the usual signature, API, and end-to-end tests,
the {TechnologyShortName} TCK also includes pluggability tests verify that the
implementation under test can use third-party persistence providers
instead of the one provided by the implementation.

[NOTE]
=======================================================================

The following may be useful to users of previous TCK versions:

In the legacy Java EE {LegacyAcronym} (2.0) TCK, the tests were located in the
`src/com/sun/ts/tests/ejb30/persistence` directory. In the Java EE {LegacyAcronym} 2.1 TCK and
{TechnologyFullName} {TechnologyVersion} TCK, the tests have been reorganized and are now in a new
location: `src/com/sun/ts/tests/jpa`.

The TCKs for Jakarta {LegacyAcronym} 3.0 and higher follows the organization of legacy Java EE {LegacyAcronym} 2.1 TCK.

In the Java EE {LegacyAcronym} (2.0) TCK, the pluggability tests required special setup in
order to be run. This is no longer the case; the Java EE {LegacyAcronym} 2.1 and {TechnologyFullName} 2{TechnologyVersion} TCK
now executes the pluggability tests along with all the other {TechnologyShortName} TCK
tests without any special setup. The pluggability tests have also been
rewritten to use a stubbed-out {TechnologyShortName} implementation, which is located in
the `src/com/sun/ts/jpa/common/pluggibility/altprovider` directory.

The TCKs for Jakarta {LegacyAcronym} 3.0 and higher also follow the same simplified setup. 

=======================================================================

