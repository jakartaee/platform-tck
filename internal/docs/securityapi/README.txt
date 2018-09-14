Simple README file with instructions to quickly install, setup, configure,
and run the Security API TCK 1.0 and related software against the Security API 1.0 RI.
For more detailed instructions, refer to the Security API 1.0 User's Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the Security API TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) Security API RI Version 1.0
3) Security API TCK Version 1.0

1. Download and install Java SE 8 software.

2. Download and install the Security API 1.0 Reference Implementation (Glassfish 5).

   mkdir /ri
   cd /ri

3. Download and install the Security API TCK 1.0 software.

   mkdir /tck
   cd /tck
   unzip securityapitck.zip

-----------------------------------------------
Setup and Configuration of the Security API TCK
-----------------------------------------------
1. Configure the Security API TCK to run against the Security API RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/securityapitck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

        web.home=/ri/Glassfish5/Glassfish
        webServerHost=localhost
        webServerPort=8080
        securedWebServicePort=8181
        impl.vi=Glassfish
        work.dir=<work dir path>
        report.dir=<report dir path>

------------------------------------
Executing the Security API TCK Tests
------------------------------------
At this point we are ready to run the Security API TCK tests against the Security API 1.0
Reference Implementation.

1. Start Security API 1.0 Reference Implementation:

   cd /ri/glassfish5/glassfish/bin
   ./asadmin start-domain

2. Config RI server and init LDAP:

   cd $TS_HOME/bin
   ant config.vi
   ant init.ldap

3. Deploy all Security API TCK tests:

   cd $TS_HOME/bin
   ant deploy.all

   This will deploy all the Security API TCK tests.

4. Execute and run the Security API TCK tests:
  
   cd $TS_HOME/bin
   ant run.all

5. Execute and run a subset of the Security API TCK tests:

   Run just one branch of the Security API tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/securityapi/ham
   ant runclient

   Run just the Security API signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetest/securityapi
   ant runclient

Note: In the instructions in this document, the variables in angle brackets
need to be expanded for each platform. For example, <TS_HOME> becomes $TS_HOME
on Solaris/Linux, and %TS_HOME% on Windows. In addition, the forward slashes (/)
used in all of the examples need to be replaced with backslashes (\) on Windows.
