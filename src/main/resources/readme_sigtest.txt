Generating the signature file
-----------------------------

There are two ways of generating signature file, but most of the time, you should be fine using already provided signature files under ${TCK_REPO}/impl/src/main/resources.

1) You can use Maven profile:

There is an added profile in ${TCK_REPO}/impl called signature-generation.
Firstly make sure that you have the desired Java version set on your path.
You can then execute the build by running a command such as:

mvn clean install -Psignature-generation -Djdk.major.version=${JDK}


This will generate a file named "cdi-api-jdk${JDK}.sig" (e.g. cdi-api-jdk8.sig) and located in ${TCK_REPO}/impl/target.
Note that the JDK variable only affects file naming and nothing else.

2) You can use standard Java tools:

The general command for generating a signature file looks like this:

java -jar sigtestdev.jar Setup -classpath "%JAVA_HOME%\jre\lib\rt.jar:jakarta.enterprise.cdi-api.jar:jakarta.inject-api.jar:jakarta.el-api.jar:jakarta.interceptor-api.jar" -Package jakarta.decorator -Package jakarta.enterprise -FileName cdi-api.sig -static

Usually all the required libraries will be located in a local Maven repository, so here's a working example with the classpaths filled out:

Class path: "/Library/Java/JavaVirtualMachines/jdk1.8.0_251.jdk/Contents/Home/jre/lib/rt.jar:/Users/starksm/.m2/repository/jakarta/enterprise/jakarta.enterprise.cdi-api/3.0.0-M3/jakarta.enterprise.cdi-api-3.0.0-M3.jar:/Users/starksm/.m2/repository/jakarta/inject/jakarta.inject-api/2.0.0-RC4/jakarta.inject-api-2.0.0-RC4.jar:/Users/starksm/.m2/repository/jakarta/el/jakarta.el-api/4.0.0-RC1/jakarta.el-api-4.0.0-RC1.jar:/Users/starksm/.m2/repository/jakarta/interceptor/jakarta.interceptor-api/2.0.0-RC2/jakarta.interceptor-api-2.0.0-RC2.jar"
Constant checking: on
Found in total: 21010 classes
Selected by -Package: 148 classes
Written to sigfile: 144 classes(and 20 inner classes)
STATUS:Passed.

A shell script for this can be found in the run_sigtest.sh file.

Running the signature test
--------------------------
      
To run the signature test simply change the execution command from Setup to SignatureTest:
 
java -jar sigtestdev.jar SignatureTest -classpath "%JAVA_HOME%\jre\lib\rt.jar:cdi-api.jar:jakarta.inject.jar:el-api.jar:jboss-interceptor-api.jar" -Package jakarta.decorator -Package jakarta.enterprise -FileName cdi-api.sig -static

Once again, here's a working example:

java -jar  "/home/shane/java/sigtest-2.1/lib/sigtestdev.jar" SignatureTest -classpath "/usr/local/java/jre/lib/rt.jar:/home/shane/.m2/repository/javax/enterprise/cdi-api/1.0/cdi-api-1.0.jar:/home/shane/.m2/repository/javax/inject/jakarta.inject/1/jakarta.inject-1.jar:/home/shane/.m2/repository/javax/el/el-api/2.1.2-b04/el-api-2.1.2-b04.jar:/home/shane/.m2/repository/org/jboss/interceptor/jboss-interceptor-api/3.1.0-CR1/jboss-interceptor-api-3.1.0-CR1.jar" -Package jakarta.decorator -Package jakarta.enterprise -FileName cdi-api.sig -static


JDK 8 signature tests
---------------------------

You need a different signature file for each major JDK version.
Currently, there is only JDK 8 version included, as 9 and 10 are short-lived releases.

cdi-api-jdk8.sig is a signature file built with JDK 8 and can be found under ${TCK_REPO}/impl/src/main/resources/cdi-api-jdk8.sig
