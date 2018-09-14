Simple README file with instructions to quickly install, setup, configure,
and run the WebSocket TCK 1.1 and related software against the WebSocket 1.1 RI. For 
more detailed instructions please refer to the WebSocket 1.1 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the WebSocket TCK tests, you need to install
and set up the following software components:

1) Java SE 7
2) WebSocket RI Version 1.1
3) WebSocket TCK Version 1.1

1. Download and install Java SE 7 software.

2. Download and install the WebSocket 1.1 Reference Implementation/jarfile.

   mkdir /ri
   cd /ri
   

3. Download and install the WebSocket TCK 1.1 software.

   mkdir /tck
   cd /tck
   unzip websockettck.zip

-----------------------------------------
Setup and Configuration of the WebSocket TCK
-----------------------------------------
1. Configure the WebSocket TCK to run against the WebSocket RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase7
   setenv TS_HOME /tck/websockettck
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

        web.home=/ri/glassfish4/glassfish
        webServerHost=localhost
        webServerPort=8080
        impl.vi=glassfish
        impl.vi.deploy.dir=${web.home}/domains/domain1/autodeploy

------------------------------
Executing the WebSocket TCK Tests
------------------------------
At this point we are ready to run the WebSocket TCK tests against the WebSocket 1.0
Reference Implementation.

1. Start WebSocket 1.1 Reference Implementation:

   cd /ri/glassfish4/glassfish/bin
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
