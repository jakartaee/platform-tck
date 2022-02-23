Simple README file with instructions to quickly install, setup, configure,
and run the XML-WS 4.0 TCK and related software against the XML-WS 4.0 CI. For
more detailed instructions please refer to the XML-WS 4.0 TCK Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the XML-WS TCK tests, you need to install
and set up the following software components:

1) Java SE 11+
2) XML-WS CI Version 4.0.0
3) XML-WS TCK Version 4.0

1. Download and install Java SE 11 software.

   mkdir /javase11
   cd /javase11
   unzip <javase11-zip-bundle>

2. Download and install the XML-WS 4.0 Compatible Implementation.

   mkdir /ci
   cd /ci
   unzip <jakarta.xml.ws-ci-4.0.0.zip>

3. Download and install the XML-WS TCK 4.0 software.

   mkdir /tck
   cd /tck
   unzip xml-ws-tck-4.0.0_dd-Mmm-YYYY.zip

-----------------------------------------
Setup and Configuration of the XML-WS TCK
-----------------------------------------
1. Configure the XML-WS TCK to run against the XML-WS CI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase11
   setenv TS_HOME /tck/xml-ws-tck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and the following XML-WS 4.0 Compatible
   Implementation jars and used libraries to jaxws.classes property, including
   the full path:

   jakarta.xml.bind-api.jar, jakarta.xml.ws-api.jar

   and:

   FastInfoset.jar, jaxb-jxc.jar, policy.jar, gmbal-api-only.jar, jaxb-xjc.jar,
   ha-api.jar, jaxws-rt.jar, saaj-impl.jar, jakarta.annotation-api.jar,
   jaxws-tools.jar, stax-ex.jar, jakarta.xml.soap-api.jar,
   stax2-api.jar, jaxb-core.jar, management-api.jar, streambuffer.jar,
   jaxb-impl.jar, mimepull.jar, woodstox-core-asl.jar

------------------------------
Executing the XML-WS TCK Tests
------------------------------
At this point we are ready to run the XML-WS TCK tests against the XML-WS 4.0
Compatible Implementation.

1. Execute and run the XML-WS TCK tests.

   cd $TS_HOME/bin
   ant run.all | tee run.log

   This will run all the XML-WS TCK tests including the signature tests.

2. Another way to execute and run the XML-WS TCK tests.

   Run just the jaxws tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/jaxws/api
   ant runclient | tee run.log

   Run just the jaxws signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/jaxws
   ant runclient | tee run.log

3. The third way to execute and run the XML-WS TCK tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	// Follow the XML-WS TCK Users Guide and JavaTest Harness Guide
		        // to configure the XML-WS TCK via the JavaTest GUI configurator
