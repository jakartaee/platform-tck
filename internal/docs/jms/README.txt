Simple README file with instructions to quickly install, setup, configure,
and run the JMS TCK 2.0 and related software against the JMS RI 2.0. For 
more detailed instructions please refer to the JMS TCK 2.0 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the JMS TCK tests, you need to install and set up
the following software components:

1) Java SE 7
2) JMS RI Version 2.0
3) JMS TCK Version 2.0

1. Download and install the Java SE 7 software.

   mkdir /javase7
   cd /javase7
   unzip <javase7-zip-bundle>

2. Download and install the JMS Reference Implementation 2.0 software.

   mkdir /ri
   cd /ri
   unzip <jmsri2.0-zip-bundle>

3. Download and install the JMS TCK 2.0 software.

   mkdir /tck
   cd /tck
   unzip jmstck-2.0_15-May-2013.zip

--------------------------------------
Setup and Configuration of the JMS TCK
--------------------------------------
1. Configure the JMS TCK to run against the JMS RI. Set the following 
   variables in your shell environment.

   setenv JAVA_HOME /javase7
   setenv TS_HOME /tck/jmstck
   setenv JMS_HOME /ri/mq
   setenv ANT_HOME /tck/anthome
   setenv PATH $JAVA_HOME/bin:$JMS_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

    jms.home=/ri/mq
    jms.classes=${ri.jars}
    impl.vi=ri

    Leave the settings for user, password, jms_timeout, harness.log.port, 
    and porting.ts.jmsObjects.class.1 to their default settings.

    If you are on a WINDOWS platform you need to add the drive letter to the
    "admin.pass.file" and "jndi.fs.dir" properties in the $TS_HOME/bin/ts.jte
    file. The property setting for the WINDOWS platform is as follows:

       admin.pass.file=C:/tmp/ripassword
       jndi.fs.dir=C:/tmp/ri_admin_objects

       Also you need to edit the $TS_HOME/bin/xml/impl/ri/jndi.properties file 
       and add the drive letter to the "java.naming.provider.url" property. The 
       property setting for the WINDOWS platform is as follows:

       java.naming.provider.url=file:///C:/tmp/ri_admin_objects

--------------------------------------
Setup and Configuration of the JMS RI
--------------------------------------
1. Set the following environment variables in your shell environment.

   setenv JAVA_HOME /javase7
   setenv IMQ_JAVAHOME $JAVA_HOME
   setenv TS_HOME /tck/jmstck
   setenv JMS_HOME /ri/mq
   setenv PATH $JAVA_HOME/bin:$JMS_HOME/bin:$TS_HOME/tools/ant/bin:$PATH

2. Start and configure the JMS 2.0 Reference Implementation for the JMS TCK.

   cd $TS_HOME/bin
   ant config.vi

   This ant target invokes the ant script $TS_HOME/bin/xml/impl/ri/config.vi.xml
   which automatically starts the JMS Service, and creates the JMS administered 
   objects and JMS users needed by the JMS TCK. This creates the following JMS
   administered objects:

   -----------				----
   Object Name 				Type
   -----------				----
   MY_QUEUE 				javax.jms.Queue
   MY_QUEUE2 				javax.jms.Queue
   testQ0 				javax.jms.Queue
   testQ1 				javax.jms.Queue
   testQ2 				javax.jms.Queue
   testQueue2 				javax.jms.Queue
   Q2 					javax.jms.Queue
   MY_TOPIC 				javax.jms.Topic
   MY_TOPIC2 				javax.jms.Topic
   testT0 				javax.jms.Topic
   testT1 				javax.jms.Topic
   testT2 				javax.jms.Topic
   MyConnectionFactory 			javax.jms.ConnectionFactory
   MyQueueConnectionFactory 		javax.jms.QueueConnectionFactory
   MyTopicConnectionFactory 		javax.jms.TopicConnectionFactory
   DURABLE_SUB_CONNECTION_FACTORY	javax.jms.TopicConnectionFactory

---------------------------
Executing the JMS TCK Tests
---------------------------
At this point we are ready to run the JMS TCK tests against the JMS RI.

1. Execute and run the JMS TCK tests.

   cd $TS_HOME/bin
   ant run.all | tee run.log

   This will run all the JMS TCK tests including the signature tests.

2. Another way to execute and run the JMS TCK tests.

   Run just the jms tree of tests: 

   cd $TS_HOME/src/com/sun/ts/tests/jms
   ant runclient | tee run.log

   Run just the jms signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/jms
   ant runclient | tee run.log

3. The third way to execute and run the JMS TCK tests is to use the JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	   // Follow the JMS TCK Users Guide and JavaTest Harness Guide
		   // to configure the JMS TCK via the JavaTest GUI configurator.

----------------------------------------------------------------
Unconfiguring and shutting down the JMS RI after running JMS TCK
----------------------------------------------------------------
   cd $TS_HOME/bin
   ant clean.vi

   This ant target invokes the ant script $TS_HOME/bin/xml/impl/ri/config.vi.xml
   which automatically stops the JMS service and deletes all the JMS administered 
   objects and JMS users needed by the JMS TCK.
