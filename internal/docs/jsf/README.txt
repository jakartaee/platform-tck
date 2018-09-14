Simple README file with instructions to quickly install, setup, configure,
and run the JSF TCK 2.3 and related software against the JSF 2.3 RI. For 
more detailed instructions please refer to the JSF 2.3 Users Guide.

-----------------------
Installing the Software
-----------------------
Before you can run the JSF TCK tests, you need to install
and set up the following software components:

1) Java SE 8
2) JSF RI Version 2.3
3) JSF TCK Version 2.3

1. Download and install Java SE 8 software.

   mkdir /javase8
   cd /javase8
   unzip <javase8-zip-bundle>

2. Download and install the JSF 2.3 Reference Implementation.

   mkdir /ri
   cd /ri
   

3. Download and install the JSF TCK 2.3 software.

   mkdir /tck
   cd /tck
   unzip jsf.zip

-----------------------------------------
Setup and Configuration of the JSF TCK
-----------------------------------------
1. Configure the JSF TCK to run against the JSF RI. Set the following
   variables in your shell environment.

   setenv JAVA_HOME /javase8
   setenv TS_HOME /tck/jsf
   setenv ANT_HOME /tck/apacheant
   setenv PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

2. Edit the $TS_HOME/bin/ts.jte file and set the following properties:

   	webServerHost=localhost
	webServerPort=8080
	webServerHome=/ri/glassfish3/glassfish
	impl.vi=glassfish
	impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy
	impl.deploy.timeout.multiplier=20
	jsf.classes=${webServerHome}/modules/javax.faces.jar${pathsep} /
	${webServerHome}/modules/weld-osgi-bundle.jar
	jspservlet.classes=${webServerHome}/modules/javax.servlet.jsp.jar${pathsep} /
	${webServerHome}/modules/javax.servlet.jsp.jstl.jar${pathsep} /
	${webServerHome}/modules/javax.servlet.jsp.jstl-api.jar${pathsep} /
	${webServerHome}/modules/javax.servlet-api.jar${pathsep} /
	${webServerHome}/modules/javax.el-api.jar${pathsep} /
	${webServerHome}/modules/javax.servlet.jsp-api.jar


------------------------------
Executing the JSF TCK Tests
------------------------------
At this point we are ready to run the JSF TCK tests against the JSF 2.3
Reference Implementation.

1. Execute and run the JSF TCK tests.

   cd $TS_HOME/bin
   ant deploy.all
   ant run.all
   ant undeploy.all

   This will run all the JSF TCK tests including the signature tests.

2. Another way to execute and run the JSF TCK tests.

   Run just the jsf tree of tests:

   cd $TS_HOME/src/com/sun/ts/tests/jsf/api
   ant runclient

   Run just the jsf signature tests:

   cd $TS_HOME/src/com/sun/ts/tests/signaturetests/jsf
   ant runclient

3. The third way to execute and run the JSF TCK tests is to use the
   JavaTest GUI.

   cd $TS_HOME/bin
   ant gui	// Follow the JSF TCK Users Guide and JavaTest Harness Guide
		// to configure the JSF TCK via the JavaTest GUI.
