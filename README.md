# Platform TCK refactoring

## Build
From the root folder, try:
```
mvn clean install -Dmaven.compiler.failOnError=false > /tmp/build.txt
```

The ^ command will compile all sources that are included as per root pom.xml with many failures that we need to resolve.  

# JakartaEE TCK Jenkins Jobs
The Jenkins jobs required for certifying Eclipse GlassFish nightly builds using the latest Jakarta EE TCK bundles are hosted in the Eclipse CloudBees Infrastructure and are available under
https://jenkins.eclipse.org/jakartaee-tck/

For information regarding the various JakartaEE TCK related jobs, Please refer to the wiki page below
https://github.com/eclipse-ee4j/jakartaee-tck/wiki/Jakarta-EE-TCK-Jenkins-jobs

# Steps required to run CTS against Standalone RI changes

1. Build the individual project and release it to Eclipse Maven repositories.

2. Integrate the individual project to GlassFish.

    Sample steps done for JTA:

    ```
    git clone https://github.com/eclipse-ee4j/glassfish.git
    git checkout -b EE4J_8
    find . -name \pom.xml -exec sed -i.bak "s/javax.transaction</jakarta.transaction</g" {} ;
    find . -name \pom.xml -exec sed -i.bak "s/javax.transaction-api</jakarta.transaction-api</g" {} ;
    mvn clean install
    ```

3. Upload the glassfish bundle built in the previous step as attachment to the corresponding pull request.

4. Request CTS team to run the CTS suites by tagging @anajosep and @bhatpmk

5. Provide the names of the jars (under glassfish6/glassfish/modules) that were modified in this bundle.

6. If known, specify the test areas that needs to be run, if not leave it empty.

7. Wait for the confirmation and the results uploaded from the runs against the pull request.

8. If the results are clean, commit the changes to the individual project and the changes required for integration to GlassFish.

# Jakarta EE TCK Build and Run Instructions 
Instructions for building and running JakartaEE TCK bundle from scratch is available in the following wiki page:
[JakartaEE TCK - Build and Run Instructions](https://github.com/eclipse-ee4j/jakartaee-tck/wiki/Instructions-for-building-and-running-JakartaEE-TCK-bundle)

# CTS User Guide

## Introduction

### CTS Overview

A Java EE 8 CTS is a set of tools and tests used to verify that a licensee's
implementation of Java EE 8 technology conforms to the applicable specification.

All tests in the CTS are based on the written specifications for the Java
platform. The CTS tests compatibility of a licensee's implementation of a
technology to the applicable specification of the technology. Compatibility
testing is a means of ensuring correctness, completeness, and consistency
across all implementations developed by technology licensees.
The set of tests included with the Java EE 8 CTS is called the test suite. All
tests in the CTS test suite are self-checking, but some tests may require tester
interaction. Most tests return either a Pass or Fail status. For a given platform
to be certified, all of the required tests must pass. The definition of required
tests may change from platform to platform.

The definition of required tests will change over time. Before your final
certification test pass, be sure to download the latest Exclude List for the
Java EE 8 CTS.

### About JavaEE8 CTS

Java EE 8 CTS is a portable, configurable, automated test suite for
verifying the compliance of a licensee's implementation of the Java EE 8 technologies.
Java EE 8 CTS includes version 5.0 of the JT harness.

For documentation on the test harness used for running the Java EE 8 CTS test
suite, see https://wiki.openjdk.java.net/display/CodeTools/Documentation.

### Hardware Requirements

The following section lists the hardware requirements for the Java EE 8 CTS
software, using the Java EE 8 RI or Java EE 8 Web Profile RI. Hardware requirements for
other reference implementations will vary.

All systems should meet the following recommended hardware requirements:

* CPU running at 2.0 GHz or higher

* 4 GB of RAM or more

* 2 GB of swap space , if required

* 6 GB of free disk space for writing data to log files, the Java EE 8 repository, and the database

* Network access to the Internet

### Software Requirements

You can run the Java EE 8 CTS software on platforms running the Solaris,
Linux, Windows, and Mac OS software that meet the following software requirements:

* Operating Systems:

  - Solaris 10 and newer

  - MAC OS X Mountain Lion (10.8.1+)

  - Windows XP SP3, Windows 2008 R2

  - Oracle Linux 6.4

  - Fedora 18

  - Ubuntu Linux 12.10

  - Suse Enterprise Linux 12.2

* Java SE 8 SDK

* Java EE 8 RI or Java EE 8 Web Profile RI

* Mail server that supports the IMAP and SMTP protocols

* One of the following databases:

  - Oracle

  - Sybase

  - DB2

  - Microsoft SQL Server

  - Postgres SQL

  - MySQL

  - Java DB

### Additional Java EE 8 CTS Requirements

In addition to the instructions and requirements described in this document,
all Java EE 8 and Java EE 8 Web Profile implementations must also pass the standalone
TCKs for the following technologies:

* Contexts and Dependency Injection for Java 2.0 (JSR 365)

* Dependency Injection for Java 1.0 (JSR 330)

* Bean Validation 2.0 (JSR 380)

## Installation

### Install CTS bundle
Complete the following procedure to install the Java EE 8 CTS on a system
running the Solaris, Linux, or Windows operating system.

1. Copy or download the CTS 8 software.

2. Change to the directory in which you want to install the Java EE 8 CTS
software and use the unzip command to extract the bundle:
    ```
    cd install_directory
    unzip jakartaeetck-nnn.zip
    ```
This creates the `jakartaeetck` directory. The `install_directory/jakartaeetck`
directory will be `TS_HOME`.

3. Set the `TS_HOME` environment variable to point to the `jakartaeetck` directory.

### Install Apache Ant
1. Download the latest version of Apache Ant from the below link
https://archive.apache.org/dist/ant/binaries/apache-ant-<version>-bin.zip

2. Change to the directory in which you want to install Apache Ant and
extract the bundle
    ```
    unzip apache-ant-<version>-bin.zip
    ```
or
    ```
    tar zxvf apache-ant-<version>-bin.tar.gz
    ```
3. Set `ANT_HOME` environment variable to point to the apache-ant-<version>
directory.

4. Set `PATH` environment variable to use the installed ant.


## Setup and Configuration
### Allowed Modifications

You can modify the following test suite components only:

* Your implementation of the porting package

* `ts.jte` environment file

* The vendor-specific SQL files in `<TS_HOME>/sql`

* Any files in `<TS_HOME>/bin` and `<TS_HOME>/bin/xml` (except for `ts.*` files)

### Configuring the Java EE 8 RI as the VI

To configure the Java EE 8 RI as the server under test (that is, to use the
Java EE 8 RI as the VI) follow the steps listed below.

In this scenario, the goal is simply to test the Java EE 8 RI against
the CTS for the purposes of familiarizing yourself with CTS test procedures.
You may also want to refer to the Quick Start guides included with
the Java EE 8 CTS for similar instructions.

1. Set server properties in your `<TS_HOME>/bin/ts.jte` file to suit your test
environment.
Be sure to set the following properties:
    a. Set the `webServerHost` property to the name of the host on which your Web
server is running that is configured with the RI.
The default setting is `localhost`.

    b. Set the `webServerPort` property to the port number of the host on which the
Web server is running and configured with the RI.
The default setting is `8001`.

    c. Set the `wsgen.ant.classname` property to the Vendor's implementation class
that mimics the RI Ant task that in turn calls the wsgen Java-to-WSDL tool.
The default setting is `com.sun.tools.ws.ant.WsGen`.

    d. Set the `wsimport.ant.classname` property to the Vendor's implementation
class that mimics the RI Ant task that in turn calls the `wsimport` WSDL-to-Java
tool.
The default setting is `com.sun.tools.ws.ant.WsImport`.

    e. Set the `porting.ts.url.class` property to your porting implementation class
that is used for obtaining URLs.
The default setting for the RI porting implementation is `com.sun.ts.lib.implementation.sun.common.SunRIURL`.

    f. Set the database-related properties in the `<TS_HOME>/bin/ts.jte` file.

    g. Add the following JVM option to the `command.testExecuteAppClient`
property to enable the Security Manager in the application client container:
`-Djava.security.manager`

Add this option to the list of other `-D` JVM options for this property.
As mentioned previously, these settings can vary, but must match whatever you
used when setting up the Java EE 8 RI server.

2. Install the Java EE 8 RI and configure basic settings, as described in

3. Start the Java EE 8 RI application server.
Refer to the application server documentation for complete instructions.

4. Enable the Security Manager.
If you are using the Java EE 8 RI, execute the following command from the
command line:
    ```
    asadmin create-jvm-options -Djava.security.manager
    ```
5. Stop and restart your application server so it is running with the Security
Manager enabled.

6. Change to the `<TS_HOME>/bin` directory.

7. Start your backend database.

If you are using Derby as your backend database, execute the start.javadb Ant
target:
    ```
    ant -f xml/impl/glassfish/s1as.xml start.javadb
    ```
Otherwise, refer to your backend database administration documentation for
information about starting your database server.

8. Initialize your backend database.
If you are using Derby as your backend database, execute the `init.derby` Ant
target:
    ```
    ant -f xml/init.xml init.derby
    ```

9. Run the configuration Ant target.
    ```
    ant config.vi
    ```

10. Build the special web services clients.

The special webservices tests under the `webservices12/specialcases` directory
have prebuilt endpoints, but the clients are not prebuilt. The clients will be
built after the endpoints are first predeployed to the application server under
test.  During the build, the clients import the WSDLs (by means of the Java EE
`wsimport` and `wsgen` tools) from the predeployed webservices endpoints. This process
verifies that importing a WSDL from a predeployed webservice endpoint works
properly.
To build the special webservices clients, the following command must be executed:
    ```
    ant build.special.webservices.clients
    ```
## Executing tests
### Running tests in CLI mode

1. Set the `TS_HOME` environment variable to the directory in which Java EE 8 CTS was
installed.

2. Set the `JAVA_HOME` environment variable to the latest version of JDK 8

3. Set the `ANT_HOME` environment variable to the latest version of Apache Ant installed.

4. Set the `PATH` environment to use the latest binaries.
    ```
    export PATH=$ANT_HOME/bin:$JAVA_HOME/bin:$PATH
    ```

5. Change to any subdirectory under <TS_HOME>/src/com/sun/ts/tests.

6. Ensure that the ts.jte file contains information relevant to your setup.

7. Execute the runclient Ant target to start the JavaTest:
    ```
    ant runclient
    ```
This runs all tests in the current directory and any subdirectories.

8. To run the Java EE 8 CTS signature tests, enter the following commands:
    ```
    cd <TS_HOME>/src/com/sun/ts/tests/signaturetest/javaee
    ant runclient
    ```

9. To run a single test directory in the forward direction, enter the following commands:
    ```
    cd <TS_HOME>/src/com/sun/ts/tests/jaxws/api/jakarta_xml_ws/Dispatch
    ant -Dkeywords=forward runclient
    ```

10. To run a subset of test directories in the reverse direction, enter the following
commands:
    ```
    cd <TS_HOME>/src/com/sun/ts/tests/jaxws/api
    ant -Dkeywords=reverse runclient
    ```
