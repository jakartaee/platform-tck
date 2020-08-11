Simple README file with instructions to quickly install, setup, configure,
and run the Servlet TCK 5.0 and related software against the Servlet 5.0 Compatible Implementation. For 
more detailed instructions please refer to the Servlet 5.0 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the Servlet TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) Servlet Compatible Implementation Version 5.0
3) Servlet TCK Version 5.0

1. Download and install Java SE 8 software.

2. Download and install the Servlet 5.0 Compatible Implementation/jarfile.

   mkdir /ri
   cd /ri
   

3. Download and install the Servlet TCK 5.0 software.

   mkdir /tck
   cd /tck
   unzip servlettck.zip

-----------------------------------------
Setup and Configuration of the Servlet TCK
-----------------------------------------
1. Configure the Servlet TCK to run against the Servlet Compatible Implementation. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/servlettck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

        web.home=/ri/glassfish4/glassfish
        webServerHost=localhost
        webServerPort=8080
        webServerPort=8080
        securedWebServicePort=8181
        impl.vi=glassfish

------------------------------
Executing the Servlet TCK Tests
------------------------------
At this point we are ready to run the Servlet TCK tests against the Servlet 5.0
Reference Implementation.

1. Start Servlet 5.0 Reference Implementation:

   cd /ri/glassfish4/glassfish/bin
   ./asadmin start-domain

2. Config RI server for running security tests:

   cd $TS_HOME/bin
   ant config.security

3. Deploy all Servlet TCK tests.

   cd $TS_HOME/bin
   ant deploy.all

   This will deploy all the Servlet TCK tests including the signature tests.

4. Execute and run the Servlet TCK tests.
  
   cd $TS_HOME/bin
   ant run.all

5. Another way to execute and run the Servlet TCK tests.

   Run just one branch of the servlet tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/servlet/api
   ant runclient

   Run just the servlet signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/servlet
   ant runclient
