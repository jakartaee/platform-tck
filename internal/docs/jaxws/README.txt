Simple README file with instructions to quickly install, setup, configure,
and run the JAX-WS 3.0 TCK and related software against the JAX-WS 3.0 RI. For
more detailed instructions please refer to the JAX-WS 3.0 TCK Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the JAX-WS TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) JAX-WS RI Version 3.0.0
3) JAX-WS TCK Version 3.0

1. Download and install Java SE 8 software.

   mkdir /javase8
   cd /javase8
   unzip <javase8-zip-bundle>

2. Download and install the JAX-WS 3.0 Reference Implementation.

   mkdir /ri
   cd /ri
   unzip <jakarta.xml.ws-ri-3.0.0.zip>

3. Create endorsed dir to put JAX-WS 3.0 API (and JAX-B API) there.

   cd /ri
   mkdir endorsed
   mv jaxws-api.jar endorsed/jaxws-api.jar
   mv jaxb-api.jar endorsed/jaxb-api.jar

4. Download and install the JAX-WS TCK 3.0 software.

   mkdir /tck
   cd /tck
   unzip xml-ws-tck-3.0.0_dd-Mmm-YYYY.zip

-----------------------------------------
Setup and Configuration of the JAX-WS TCK
-----------------------------------------
1. Configure the JAX-WS TCK to run against the JAX-WS RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/xml-ws-tck
   setenv ANT_OPTS -Djava.endorsed.dirs=/ri/endorsed
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and the following JAX-WS 3.0 Reference
   Implementation jars and used libraries to jaxws.classes property, including
   the full path:

   jaxb-api.jar, jaxws-api.jar

   and:

   FastInfoset.jar, jaxb-jxc.jar, policy.jar, gmbal-api-only.jar, jaxb-xjc.jar,
   resolver.jar, ha-api.jar, jaxws-rt.jar, saaj-impl.jar, jakarta.annotation-api.jar,
   jaxws-tools.jar, stax-ex.jar, javax.xml.soap-api.jar, jsr181-api.jar,
   stax2-api.jar, jaxb-core.jar, management-api.jar, streambuffer.jar,
   jaxb-impl.jar, mimepull.jar, woodstox-core-asl.jar

------------------------------
Executing the JAX-WS TCK Tests
------------------------------
At this point we are ready to run the JAX-WS TCK tests against the JAX-WS 3.0
Reference Implementation.

1. Execute and run the JAX-WS TCK tests.

   cd $TS_HOME/bin
   ant run.all | tee run.log

   This will run all the JAX-WS TCK tests including the signature tests.

2. Another way to execute and run the JAX-WS TCK tests.

   Run just the jaxws tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/jaxws/api
   ant runclient | tee run.log

   Run just the jaxws signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/jaxws
   ant runclient | tee run.log

3. The third way to execute and run the JAX-WS TCK tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	// Follow the JAX-WS TCK Users Guide and JavaTest Harness Guide
		        // to configure the JAX-WS TCK via the JavaTest GUI configurator
