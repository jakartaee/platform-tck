///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
This include file allows for additional instructions for running the TCK.
For example, the following is used for the JAX-WS TCK.
///////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////
[[GCLRR]][[running-the-jax-ws-tck-against-the-jax-ws-ri]]

5.6 Running the JAX-WS TCK Against the JAX-WS RI
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This test scenario is ensures that the configuration and deployment of
all the prebuilt JAX-WS TCK tests against the JAX-WS Reference
Implementation are successful, and that the TCK is ready for
compatibility testing against the Vendor and Reference Implementations.

1.  Verify that you have followed the configuration instructions in
link:config.html#GBFVU[Configuring Your Environment to Run the JAX-WS TCK
Against the JAX-WS Reference Implementation].
2.  Specify `forward` for the `keywords` option.
3.  Verify that you have completed the steps in
link:config.html#GCLIW[Deploying the JAX-WS TCK Prebuilt Archives].
4.  Run the tests, as described in link:#GBFUZ[Starting JavaTest] and,
if desired, link:#GBFWM[Running a Subset of the Tests].

[[GCLRZ]][[running-the-jax-ws-tck-against-a-vendors-implementation]]

5.7 Running the JAX-WS TCK Against a Vendor's Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This test scenario is one of the compatibility test phases that all
Vendors must pass. This ensures that the prebuilt JAX-WS TCK tests built
against the JAX-WS RI can be successfully run against the Vendor
Implementation (VI).

1.  Verify that you have followed the configuration instructions in
link:config.html#GCLHU[Configuring Your Environment to Run the JAX-WS TCK
Against the Vendor Implementation].
2.  Specify `forward` for the `keywords` option.
3.  Verify that you have completed the steps in
link:config.html#GCLIW[Deploying the JAX-WS TCK Prebuilt Archives]
4.  Run the tests, as described in link:#GBFUZ[Starting JavaTest] and,
if desired, link:#GBFWM[Running a Subset of the Tests].

[[GCLQV]][[running-the-rebuilt-jax-ws-tck-against-the-jax-ws-ri]]

5.8 Running the Rebuilt JAX-WS TCK Against the JAX-WS RI
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This test scenario is one of the compatibility test phases that all
Vendors must pass. This ensures that the JAX-WS TCK tests that are
rebuilt using the Vendor's toolset can be successfully run against the
Reference Implementation.

1.  Verify that you have followed the configuration instructions in
link:config.html#GCLHF[Configuring Your Environment to Rebuild and Run
the JAX-WS TCK Against the JAX-WS RI].
2.  Refer to link:rebuild.html#GCLIZ[Appendix B], to learn about
rebuilding the JAX-WS TCK tests.
3.  Specify `reverse` for the `keywords` option.
4.  Verify that you have completed the steps in
link:config.html#GCLIL[Deploying the Rebuilt JAX-WS TCK Tests Against the
JAX-WS Reference Implementation].
5.  Run the tests, as described in link:#GBFUZ[Starting JavaTest] and,
if desired, link:#GBFWM[Running a Subset of the Tests].

[[GBWUC]][[testing-interoperability-between-a-vendor-implementation-and-the-jax-ws-reference-implementation]]

5.9 Testing Interoperability Between a Vendor Implementation and the JAX-WS Reference Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1.  Specify `all` for the `keywords` option.
2.  Verify that you have completed the steps in
link:config.html#GCLIW[Deploying the JAX-WS TCK Prebuilt Archives].
3.  Verify that you have completed the steps in
link:config.html#GCLIL[Deploying the Rebuilt JAX-WS TCK Tests Against the
JAX-WS Reference Implementation]
4.  Run the tests, as described in link:#GBFUZ[Starting JavaTest] and,
if desired, link:#GBFWM[Running a Subset of the Tests].
///////////////////////////////////////////////////////////////////////
