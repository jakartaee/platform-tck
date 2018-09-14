Simple README file with instructions to quickly install, setup, configure,
and run the CONCURRENCY TCK 1.0 and related software against the CONCURRENCY RI 1.0. For 
more detailed instructions please refer to the CONCURRENCY TCK 1.0 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the CONCURRENCY TCK tests, you need to install
and set up the following software components:

1) Java SE 7
2) CONCURRENCY RI Version 1.0 (we used Java EE 7 RI)
3) CONCURRENCY TCK Version 1.0

1. Download and install Java SE 7 software.

   mkdir /javase7
   cd /javase7
   unzip <javase7-zip-bundle>

2. Download and install the CONCURRENCY 1.0 Reference Implementation.
   We used Java EE 7 RI.

   mkdir /ri
   cd /ri
   unzip <javaee7-ri-zip-bundle>

3. Download and install the CONCURRENCY TCK 1.0 software.

   mkdir /tck
   cd /tck
   unzip concurrencytck-1.0_15-May-2013.zip

----------------------------------------------
Setup and Configuration of the CONCURRENCY TCK
----------------------------------------------
1. Configure the CONCURRENCY TCK to run against the CONCURRENCY RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase7
   setenv TS_HOME /tck/concurrencytck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

    Set the webserver host and port. Below uses default settings for Java EE 7 RI:

    webServerHost=localhost
    webServerPort=8080

    Set webcontainer home to point to path where Java EE 7 RI is installed:

    webcontainer.home=/path-to-javaee7-ri-installation

    Set concurrency classes property to point to implementaion classes needed
    for JavaEE7 4 RI:

    concurrency.classes=${glassfish.ri.classes}

-----------------------------------
Executing the CONCURRENCY TCK Tests
-----------------------------------
At this point we are ready to run the CONCURRENCY TCK tests against the CONCURRENCY 1.0
Reference Implementation.

1. Configure the server before running tests

   cd $TS_HOME/bin
   ant config.vi

2. Deploy all the WAR archives to the webserver before running tests

   cd $TS_HOME/bin
   ant deploy.all

3. Execute and run the CONCURRENCY TCK tests.

   cd $TS_HOME/bin
   ant run.all

   This will run all the CONCURRENCY TCK tests including the signature tests.

4. Another way to execute and run the CONCURRENCY TCK tests.

   Run just the concurrency tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/concurrency
   ant runclient

   Run just the concurrency signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/concurrency
   ant runclient

5. The third way to execute and run the CONCURRENCY TCK tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	// Follow the CONCURRENCY TCK Users Guide and JavaTest Harness Guide
		// to configure the CONCURRENCY TCK via the JavaTest GUI configurator
