.  Install the Jakarta EE 9 Compatible Platform software (the servlet Web container used
for running the {TechnologyShortName} TCK with the
{TechnologyShortName} {TechnologyVersion} Compatible Implementation), if it is not already
installed. +
Download and install the Servlet Web container with the
{TechnologyShortName} {TechnologyVersion} Compatible Implementation used for running the
{TechnologyShortName} TCK {TechnologyVersion}, represented by the Jakarta EE 9 Compatible Platform.
This software can be obtained from the Java Licensee Engineering Web
site. Refer to any installation instructions that accompany the software
for additional information.

.  Install the Jakarta EE 9 Compatible Platform software which contains the JAX-WS 3.0
Compatible Implementation or install the Standalone JAX-WS 3.0 Compatible Implementation
software. +
You need to copy the TCK JAR files/classes to the correct location under the
Jakarta EE 9 Compatible Platform installation directory. Assuming
the installation directory is `${GLASSFISH_HOME}` and JAX-WS TCK 3.0 under `${TS_HOME}`,
you would copy the JAR files/classes as follows:
+
--
[source,oac_no_warn]
----
cp ${TS_HOME}/lib/tsharness.jar ${TS_HOME}/lib/jaxwstck.jar \
   ${GLASSFISH_HOME}/lib
----
--
+
The Jakarta EE 9 Compatible Platform software contains the JAX-WS 3.0 Compatible
Implementation and is used to validate your initial configuration and
setup of the JAX-WS TCK 3.0, as well as to run the reverse tests which
are explained further in link:config.html#GBFVV[Chapter 4, "Setup and
Configuration"] and link:rebuild.html#GCLIZ[Appendix B]. +
The Standalone JAX-WS 3.0 Compatible Implementation software contains the JAX-WS 3.0 Compatible
Implementation and can be used with any web container that meets the
minimum requirements for a container as defined in the JAX-WS 3.0
Specification such as the Apache Tomcat web container. +
If you use the Standalone JAX-WS 3.0 Compatible Implementation software with the Apache Tomcat
web container, you need to copy the JAR files/classes from the
standalone JAX-WS 3.0 Compatible Implementation software to the correct location under the
Apache Tomcat web container. Assuming the Apache Tomcat web container is
installed under `${CATALINA_HOME}`, JAX-WS 3.0 Compatible Implementation under `${JAXWS_HOME}`
and JAX-WS TCK 3.0 under `${TS_HOME}`, you would copy the JAR files/classes as follows:
+
--
[source,oac_no_warn]
----
mkdir -p ${CATALINA_HOME}/shared/lib

cp ${JAXWS_HOME}/lib/*.jar ${JAXWS_HOME}/lib/plugins/*.jar \
   ${JAXWS_HOME}/extra/*.jar ${CATALINA_HOME}/shared/lib

cp ${TS_HOME}/lib/tsharness.jar ${TS_HOME}/lib/jaxwstck.jar \
   ${CATALINA_HOME}/shared/lib
----
--
+
Edit `${CATALINA_HOME}/conf/catalina.properties` and set value of `shared.loader`
property to `${catalina.base}/shared/lib`:
+
--
[source,oac_no_warn]
----
shared.loader=${catalina.base}/shared/lib/*.jar
----
--
+
If, instead of using Tomcat, you are using the Jakarta EE 9 Compatible
Implementation, which already includes JAX-WS 3.0, you only need to copy
the `jaxwstck/lib/tsharness.jar` and `jaxwstck/lib/jaxwstck.jar` TCK JAR
files to the domain's `lib/ext` directory.
