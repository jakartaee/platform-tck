Simple README file with instructions to quickly install, setup, configure,
and run the JAX-RS 2.1 TCK and related software against the JAX-RS 2.1 RI. For
more detailed instructions please refer to the JAX-RS 2.1 TCK Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the JPA TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) JPA RI Version 2.1
3) JPA TCK Version 2.1

1. Download and install Java SE 8 software.

   mkdir /javase8
   cd /javase8
   unzip <javase8-zip-bundle>

2. Download and install the JAX-RS 2.1 Reference Implementation.

   mkdir /ri
   cd /ri
   unzip <jaxrs2.1-zip-bundle>

3. Download and install the JAX-RS 2.1 TCK software.

   mkdir /tck
   cd /tck
   unzip jaxrstck-2.1_30-Jun-2017.zip

-----------------------------------------
Setup and Configuration of the JAX-RS TCK
-----------------------------------------
1. Configure the JAX-RS TCK to run against the JAX-RS RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/jaxrstck
   setenv ANT_HOME /tck/apacheant
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties. 
   The values will have to be those that are pertinent to your environment 
   and configuration.

   jaxrs_impl.classes=/ri/javax.ws.rs-ri-2.1/javax.ws.rs-api.jar:
                      /ri/javax.ws.rs-ri-2.1/jersey-client.jar:
                      /ri/javax.ws.rs-ri-2.1/jersey-common.jar:
                      /ri/javax.ws.rs-ri-2.1/jersey-server.jar:
                      /ri/javax.ws.rs-ri-2.1/jersey-container-servlet.jar:
                      /ri/javax.ws.rs-ri-2.1/jersey-container-servlet-core.jar:
                      /ri/javax.ws.rs-ri-2.1/jersey-media-jaxb.jar:
                      /ri/javax.ws.rs-ri-2.1/jersey-media-sse.jar:
                      /ri/javax.ws.rs-ri-2.1/jersey-hk2.jar:
                      /ri/javax.ws.rs-ri-2.1/osgi-resource-locator.jar:
                      /ri/javax.ws.rs-ri-2.1/jakarta.inject.jar:
                      /ri/javax.ws.rs-ri-2.1/guava.jar:
                      /ri/javax.ws.rs-ri-2.1/hk2-api.jar:
                      /ri/javax.ws.rs-ri-2.1/hk2-locator.jar:
                      /ri/javax.ws.rs-ri-2.1/hk2-utils.jar:
                      /ri/javax.ws.rs-ri-2.1/cglib.jar:
                      /ri/javax.ws.rs-ri-2.1/asm-all-repackaged.jar:
                      /ri/javax.ws.rs-ri-2.1/bean-validator.jar:
                      /ri/javax.ws.rs-ri-2.1/endorsed/javax.annotation-api.jar

------------------------------
Executing the JAX-RS TCK Tests
------------------------------
At this point we are ready to run the TCK tests against the
Reference Implementation.

1. Execute and run the tests.

   cd $TS_HOME/bin
   ant run.all | tee run.log

   This will run all the tests including the signature tests.

2. Another way to execute and run the tests.

   Run just the tests:

   cd $TS_HOME/src/com/sun/ts/tests/jaxrs
   ant runclient | tee run.log

   Run just the signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/jaxrs
   ant runclient | tee run.log

3. The third way to execute and run the tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	// Follow the TCK Users Guide and JavaTest Harness Guide
		    // to configure the TCK via the JavaTest GUI configurator
