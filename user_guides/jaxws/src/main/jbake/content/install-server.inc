.  Install the Java EE 8 RI software (the servlet Web container used
for running the {TechnologyShortName} TCK with the
{TechnologyShortName} {TechnologyVersion} RI), if it is not already
installed. +
Download and install the Servlet Web container with the
{TechnologyShortName} {TechnologyVersion} RI used for running the
{TechnologyShortName} TCK {TechnologyVersion}, represented by the Java
EE 8 RI.
This software can be obtained from the Java Licensee Engineering Web
site. Refer to any installation instructions that accompany the software
for additional information.

.  Install the Java EE 8 RI software which contains the JAX-WS 2.3
Reference Implementation or install the Standalone JAX-WS 2.3 RI
software. +
The Java EE 8 RI software contains the JAX-WS 2.3 Reference
Implementation and is used to validate your initial configuration and
setup of the JAX-WS TCK 2.3, as well as to run the reverse tests which
are explained further in link:config.html#GBFVV[Chapter 4, "Setup and
Configuration"] and link:rebuild.html#GCLIZ[Appendix B]. +
The Standalone JAX-WS 2.3 RI software contains the JAX-WS 2.3 Reference
Implementation and can be used with any web container that meets the
minimum requirements for a container as defined in the JAX-WS 2.3
Specification such as the Apache Tomcat web container. +
If you use the Standalone JAX-WS 2.3 RI software with the Apache Tomcat
web container, you need to copy the JAR files/classes from the
standalone JAX-WS 2.3 RI software to the correct location under the
Apache Tomcat web container. Assuming the Apache Tomcat web container is
installed under `${tomcat.home}`, you would copy the JAR files/classes
as follows:
+
--
[source,oac_no_warn]
----
cp jaxws-api.jar jaxb-api.jar ${tomcat.home}/common/endorsed

cp commonj.sdo.jar eclipselink.jar FastInfoset.jar gmbal-api-only.jar \
    ha-api.jar javax.annotation-api.jar javax.persistence.jar \
    javax.xml.soap-api.jar jaxb-core.jar jaxb-impl.jar jaxb-jxc.jar \
    jaxb-xjc.jar jaxws-eclipselink-plugin.jar jaxws-rt.jar jaxwstck.jar \
    jaxws-tools.jar jsr181-api.jar mail.jar management-api.jar mimepull.jar \
    policy.jar resolver.jar saaj-impl.jar sdo-eclipselink-plugin.jar \
    stax2-api.jar stax-ex.jar streambuffer.jar tsharness.jar \
    woodstox-core-asl.jar ${tomcat.home}/shared/lib

cp jaxwstck/lib/tsharness.jar jaxwstck/lib/jaxwstck.jar ${tomcat.home}/shared/lib 
----
--
+
If, instead of using Tomcat, you are using the Java EE 8 Reference
Implementation, which already includes JAX-WS 2.3, you only need to copy
the `jaxwstck/lib/tsharness.jar` and `jaxwstck/lib/jaxwstck.jar` TCK JAR
files to the domain's `lib/ext` directory.
