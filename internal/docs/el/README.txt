Simple README file with instructions to quickly install, setup, configure,
and run the EL TCK 4.0 and related software against the EL 4.0 Compatible Implementation(CI). For 
more detailed instructions please refer to the EL 4.0 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the EL TCK tests, you need to install
and set up the following software components:

1) Java 8 
2) EL Compatible Implementation Version 4.0
3) EL TCK Version 4.0

1. Download and install Java 8 or Java 11 software.

   mkdir /java
   cd /java
   unzip <java-zip-bundle>

2. Download and install the EL 4.0 Compatible Implementation/jarfile.

   mkdir /ri
   cd /ri
   

3. Download and install the EL TCK 4.0 software.

   mkdir /tck
   cd /tck
   unzip el.zip

-----------------------------------------
Setup and Configuration of the EL TCK
-----------------------------------------
1. Configure the EL TCK to run against the EL Compatible Implementation. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /java
   setenv TS_HOME /tck/el
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

	el.classes=/ri/jakarta.el.jar
	sigTestClasspath=${el.classes}${pathsep}${java.home}/lib/rt.jar


------------------------------
Executing the EL TCK Tests
------------------------------
At this point we are ready to run the EL TCK tests against the EL 4.0
Compatible Implementation.

1. Execute and run the EL TCK tests.

   cd $TS_HOME/bin
   ant run.all

   This will run all the EL TCK tests including the signature tests.

2. Another way to execute and run the EL TCK tests.

   Run just the el tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/el/api
   ant runclient

   Run just the el signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/el
   ant runclient
