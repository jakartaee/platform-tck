Simple README file with instructions to quickly install, setup, configure,
and run the JPA 2.2 TCK and related software against the JPA 2.2 RI. For
more detailed instructions please refer to the JPA 2.2 TCK Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the JPA TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) JPA RI Version 2.2
3) JPA TCK Version 2.2

1. Download and install Java SE 8 software.

   mkdir /javase8
   cd /javase8
   unzip <javase8-zip-bundle>

2. Download and install the JPA 2.2 Reference Implementation.

   mkdir /ri
   cd /ri
   unzip <jpa2.2-zip-bundle>

3. Download and install the JPA 2.2 TCK software.

   mkdir /tck
   cd /tck
   unzip jpatck-2.2_30-Jun-2017.zip

-----------------------------------------
Setup and Configuration of the JPA TCK
-----------------------------------------
1. Configure the JPA TCK to run against the JPA RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/jpatck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties. This
   example assumes the use of Derby as the Database. The values will have to
   be those that are pertinent to your environment and configuration.

    jpa.classes=/ri/jlib/jpa/javax.persistence_2.2.0.v201706121819.jar:/ri/jlib/eclipselink.jar

    jdbc.driver.classes=/derby/lib/derbyclient.jar
    jdbc.db=derby
    javax.persistence.provider=org.eclipse.persistence.jpa.PersistenceProvider
    javax.persistence.jdbc.driver=org.apache.derby.jdbc.ClientDriver
    javax.persistence.jdbc.url=jdbc:derby://localhost:1527/thedb;create=true
    javax.persistence.jdbc.user=usr1
    javax.persistence.jdbc.password=pass1
    db.supports.sequence=true
    persistence.second.level.caching.supported=true

------------------------------
Executing the JPA TCK Tests
------------------------------
At this point we are ready to run the TCK tests against the
Reference Implementation.

1. Execute and run the tests.

   cd $TS_HOME/bin
   ant run.all | tee run.log

   This will run all the tests including the signature tests.

2. Another way to execute and run the tests.

   Run just the tests:

   cd $TS_HOME/src/com/sun/ts/tests/jpa
   ant runclient | tee run.log

   Run just the signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/jpa
   ant runclient | tee run.log

3. The third way to execute and run the tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	// Follow the TCK Users Guide and JavaTest Harness Guide
		    // to configure the TCK via the JavaTest GUI configurator
