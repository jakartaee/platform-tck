Simple README file with instructions to quickly install, setup, configure,
and run the JSON-P TCK 2.0 and related software against the JSON-P RI 2.0.0. For
more detailed instructions please refer to the JSON-P TCK 2.0 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the JSON-P TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) JSON-P RI Version 2.0.0
3) JSON-P TCK Version 2.0

1. Download and install Java SE 8 software.

   mkdir /javase8
   cd /javase8
   unzip <javase8-zip-bundle>

2. Download and install the JSON-P 2.0.0 Reference Implementation.

   mkdir /ri
   cd /ri
   unzip <jakarta.json-ri-2.0.0.zip>

3. Download and install the JSON-P TCK 2.0 software.

   mkdir /tck
   cd /tck
   unzip jsonp_tck-2.0.zip

-----------------------------------------
Setup and Configuration of the JSON-P TCK
-----------------------------------------
1. Configure the JSON-P TCK to run against the JSON-P RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/jsonp-tck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

    jsonp.classes=/ri/jakarta.json-ri-2.0/lib/jakarta.json-2.0.jar

    Add the path to the JSON-P 2.0 Reference Implementation jar

------------------------------
Executing the JSON-P TCK Tests
------------------------------
At this point we are ready to run the JSON-P TCK tests against the JSON-P 2.0.0
Reference Implementation.

1. Execute and run the JSON-P TCK tests.

   cd $TS_HOME/bin
   ant run.all | tee run.log

   This will run all the JSON-P TCK tests including the signature tests.

2. Another way to execute and run the JSON-P TCK tests.

   Run just the jsonp tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/jsonp/api
   ant runclient | tee run.log

   Run just the jsonp signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/jsonp
   ant runclient | tee run.log

3. The third way to execute and run the JSON-P TCK tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui  // Follow the JSON-P TCK Users Guide and JavaTest Harness Guide
            // to configure the JSON-P TCK via the JavaTest GUI configurator
