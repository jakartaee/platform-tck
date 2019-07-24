Simple README file with instructions to quickly install, setup, configure,
and run the Jakarta Annotations TCK 1.3 and related software against the Jakarta Annotations Compatible Implementation (CI) 1.3. For 
more detailed instructions please refer to the Jakarta Annotations TCK 1.3 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the Jakarta Annotations TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) Jakarta Annotations CI Version 1.3
3) Jakarta Annotations TCK Version 1.3

1. Download and install Java SE 8 software.

   mkdir /javase8
   cd /javase8
   unzip <javase8-zip-bundle>

2. Download and install the Jakarta Annotations 1.3 Reference Implementation.

   mkdir /ri
   cd /ri
   unzip <cajri1.3-zip-bundle>

3. Download and install the Jakarta Annotations TCK 1.3 software.

   mkdir /tck
   cd /tck
   unzip cajtck-1.3_<date>.zip

-----------------------------------------
Setup and Configuration of the Jakarta Annotations TCK
-----------------------------------------
1. Configure the Jakarta Annotations TCK to run against the Jakarta Annotations CI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/cajtck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

    Add the path to the Jakarta Annotations 1.3 CI classes/jar that contain the
    annotations

    local.classes=/ri/lib/javax-annotation-api.jar

    Add the path to endorsed dirs to the location of the CI API jars for
    those technologies you wish to override that exist within the JDK

    endorsed.dirs=/ri/lib

------------------------------
Executing the Jakarta Annotations TCK Tests
------------------------------
At this point we are ready to run the Jakarta Annotations TCK tests against the Jakarta Annotations 1.3
Reference Implementation.

1. Execute and run the Jakarta Annotations TCK tests.

   cd $TS_HOME/bin
   ant run.all | tee run.log

   This will run all the Jakarta Annotations TCK tests including the signature tests.

2. Another way to execute and run the Jakarta Annotations TCK tests.

   Run just the Jakarta Annotations signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/caj
   ant runclient | tee run.log

3. The third way to execute and run the Jakarta Annotations TCK tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	// Follow the Jakarta Annotations TCK Users Guide and JavaTest Harness Guide
		// to configure the Jakarta Annotations TCK via the JavaTest GUI configurator
