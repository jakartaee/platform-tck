Simple README file with instructions to quickly install, setup, configure,
and run the WebSocket TCK 2.0 and related software against the WebSocket 2.0 CI. For
more detailed instructions please refer to the WebSocket 2.0 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the WebSocket TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) WebSocket CI Version 2.0
3) WebSocket TCK Version 2.0

1. Download and install Java SE 8 software.

   mkdir /java
   cd /java
   unzip <java-zip-bundle>


2. Download and install the WebSocket 2.0 Compatible Implementation/jarfile.

   mkdir /ci
   cd /ci
   

3. Download and install the WebSocket TCK 2.0 software.

   mkdir /tck
   cd /tck
   unzip websockettck.zip

-----------------------------------------
Setup and Configuration of the WebSocket TCK
-----------------------------------------
1. Configure the WebSocket TCK to run against the WebSocket CI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /java
   setenv TS_HOME /tck/websockettck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

        web.home=/ci/glassfish6/glassfish
        webServerHost=localhost
        webServerPort=8080
        impl.vi=glassfish
        impl.vi.deploy.dir=${web.home}/domains/domain1/autodeploy

------------------------------
Executing the WebSocket TCK Tests
------------------------------
At this point we are ready to run the WebSocket TCK tests against the WebSocket 2.0
Reference Implementation.

1. Start WebSocket 2.0 Reference Implementation:

   cd /ci/glassfish6/glassfish/bin
   ./asadmin start-domain

2. Deploy all WebSocket TCK tests.

   cd $TS_HOME/bin
   ant deploy.all

   This will deploy all the WebSocket TCK tests including the signature tests.

3. Execute and run the WebSocket TCK tests.
  
   cd $TS_HOME/bin
   ant run.all

4. Another way to execute and run the WebSocket TCK tests.

   Run just one the websocket tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/websocket/api
   ant runclient

   Run just the websocket signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/websocket
   ant runclient
