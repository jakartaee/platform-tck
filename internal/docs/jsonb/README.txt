Simple README file with instructions to quickly install, setup, configure,
and run the JSON-B TCK 1.0 and related software against the JSON-B RI 1.0. For 
more detailed instructions please refer to the JSON-B TCK 1.0 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the JSON-B TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) JSON-B RI Version 1.0
3) JSON-B TCK Version 1.0

1. Download and install Java SE 8 software.

   mkdir /javase8
   cd /javase8
   unzip <javase8-zip-bundle>

2. Download and install the JSON-B 1.0 Reference Implementation.

   mkdir /ri
   cd /ri
   unzip <jakarta.json.bind-ri-1.0.zip>

3. Download and install the JSON-B TCK 1.0 software.

   mkdir /tck
   cd /tck
   unzip jsonbtck-1.0_10-May-2017.zip

-----------------------------------------
Setup and Configuration of the JSON-B TCK
-----------------------------------------
1. Configure the JSON-B TCK to run against the JSON-B RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/jsonbtck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

    jsonb.classes=/ri/jakarta.json.bind-ri-1.0/lib/yasson-1.0.jar

    Add the path to the JSON-B 1.0 Reference Implementation jar

------------------------------
Executing the JSON-B TCK Tests
------------------------------
At this point we are ready to run the JSON-B TCK tests against the JSON-B 1.0
Reference Implementation.

1. Execute and run the JSON-B TCK tests.

   cd $TS_HOME/bin
   ant run.all | tee run.log

   This will run all the JSON-B TCK tests including the signature tests.

2. Another way to execute and run the JSON-B TCK tests.

   Run just the jsonb tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/jsonb/api
   ant runclient | tee run.log

   Run just the jsonb signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/jsonb
   ant runclient | tee run.log

3. The third way to execute and run the JSON-B TCK tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	// Follow the JSON-B TCK Users Guide and JavaTest Harness Guide
		// to configure the JSON-B TCK via the JavaTest GUI configurator
