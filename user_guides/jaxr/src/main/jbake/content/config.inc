///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
The following sections should be customized for the technology.
This text was originally from the JAX-RS TCK.  Most references
to JAX-RS have been parameterized to serve as a simple starting
point for customization.  There are still many details that will
need to be changed or removed.  The major sections 4.1, 4.2, and
4.3 should be preserved.  If their titles are changed, the links
at the top of config.adoc will need to be changed as well as well
as toc.adoc.
///////////////////////////////////////////////////////////////////////

[[GBFVU]][[configuring-your-environment-to-run-the-tck-against-the-reference-implementation]]

4.1 Configuring Your Environment to Run the TCK Against the Reference Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

After configuring your environment as described in this section,
continue with the instructions in link:#GBFUY[Section 4.4, "Using the
JavaTest Harness Software."]


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<TS_HOME>` becomes `$TS_HOME` on
Solaris/Linux and `%TS_HOME%` on Windows. In addition, the forward
slashes (`/`) used in all of the examples need to be replaced with
backslashes (`\`) for Windows. Finally, be sure to use the appropriate
separator for your operating system when specifying multiple path
entries (`;` on Windows, `:` on UNIX/Linux).

On Windows, you must escape any backslashes with an extra backslash in
path separators used in any of the following properties, or use forward
slashes as a path separator instead.

=======================================================================


Edit your ts_env or ts_env.bat file and set the following environment variables:

1.  Set the following environment variables in your shell environment:
    a.  `JAVA_HOME` to the directory in which Java SE 8 is installed
2.  `LOCAL_CLASSES` variable point to your {TechnologyShortName}
    {TechnologyVersion} implementation's classes/jars and to the
    {TechnologyShortName} TCK classes/jars (harness.jar, javatest.jar).  +
    `TS_HOME` to the directory in which the {TechnologyShortName} TCK
    {TechnologyVersion} software is installed. +
    Note that if you are using the JAXR RI, Version 1.0_01, the manner
    in which you set your classpath is dependent upon the location from
    which you downloaded the JAXR RI software. +
    For example, if you are running Solaris 8 and you downloaded the
    JAXR RI, Version 1.0_01 from the Java Software or Partner
    Engineering web sites, you could set the following classpath: +

    ----
    LOCAL_CLASSES=${JAXR_HOME}/lib/commons-logging.jar:
    ${JAXR_HOME}/lib/mail.jar:${JAXR_HOME}/lib/activation.jar:
    ${JAXR_HOME}/lib/xercesImpl.jar:${JAXR_HOME}/lib/xalan.jar:
    ${JAXR_HOME}/lib/dom.jar:${JAXR_HOME}/lib/dom4j.jar:
    ${JAXR_HOME}/lib/saaj-ri.jar:${JAXR_HOME}/lib/saaj-api.jar:
    ${JAXR_HOME}/lib/jaxr-ri.jar:${JAXR_HOME}/lib/jaxr-api.jar:
    ${JAXR_HOME}/lib/jaxp-api.jar:${JAXR_HOME}/lib/sax.jar:
    ${JAXR_HOME}/lib/jnet.jar:${JAXR_HOME}/lib/jcert.jar:
    ${JAXR_HOME}/lib/jaas.jar:${JAXR_HOME}/lib/jsse.jar:
    ${JAXR_HOME}/lib/castor-0.9.3.9-xml.jar:
    ${JAXR_HOME}/lib/soap.jar:${JAXR_HOME}/lib/fscontext.jar:
    ${JAXR_HOME}/lib/providerutil.jar:
    ${TS_HOME}/lib/javatest.jar:${TS_HOME}/lib/harness.jar
    ----

    Or, if you are running on Windows 2000/XP and you downloaded the
    JAXR RI, Version 1.0_01 from the Java Software or Partner
    Engineering web sites, you could set the following classpath:

    ----
    set LOCAL_CLASSES=%JAXR_HOME%\lib\commons-logging.jar;
    %JAXR_HOME%\lib\mail.jar;%JAXR_HOME%\lib\activation.jar;
    %JAXR_HOME%\lib\xercesimpl.jar;%JAXR_HOME%\lib\xalan.jar;
    %JAXR_HOME%\lib\dom.jar;%JAXR_HOME%\lib\dom4j.jar;
    %JAXR_HOME%\lib\saaj-ri.jar;%JAXR_HOME%\lib\saaj-api.jar;
    %JAXR_HOME%\lib\jaxr-ri.jar;%JAXR_HOME%\lib\jaxr-api.jar;
    %JAXR_HOME%\lib\jaxp-api.jar;%JAXR_HOME%\lib\sax.jar;
    %JAXR_HOME%\lib\jnet.jar;%JAXR_HOME%\lib\jcert.jar;
    %JAXR_HOME%\lib\jaas.jar;%JAXR_HOME%\lib\jsse.jar;
    %JAXR_HOME%\lib\castor-0.9.3.9-xml.jar;
    %JAXR_HOME%\lib\soap.jar;%JAXR_HOME%\lib\fscontext.jar;
    %JAXR_HOME%\lib\providerutil.jar;
    %TS_HOME%\lib\javatest.jar;%TS_HOME%\lib\harness.jar
    ----

    If you are running on Solaris 8 and you are using the JAXR RI,
    Version 1.0_01 from the Java Web Services Developer Pack, you would
    set the following classpath:

    ----
    set LOCAL_CLASSES=${JAXR_HOME}/common/lib/soap.jar;
    ${JAXR_HOME}/common/endorsed/xercesImpl.jar;
    ${JAXR_HOME}/common/lib/jaxr-api.jar;
    ${JAXR_HOME}/common/lib/mail.jar;
    ${JAXR_HOME}/common/lib/activation.jar;
    ${JAXR_HOME}/common/lib/castor-0.9.3.9-xml.jar;
    ${JAXR_HOME}/common/endorsed/xalan.jar;
    ${JAXR_HOME}/common/lib/jsse.jar;
    ${JAXR_HOME}/common/lib/jcert.jar;
    ${JAXR_HOME}/common/lib/jnet.jar;
    ${JAXR_HOME}/common/lib/jaas.jar;
    ${JAXR_HOME}/common/lib/jaxr-ri.jar;
    ${JAXR_HOME}/common/lib/saaj-api.jar;
    ${JAXR_HOME}/common/lib/saaj-ri.jar;
    ${JAXR_HOME}/common/lib/jaxp-api.jar;
    ${JAXR_HOME}/common/endorsed/dom.jar;
    ${JAXR_HOME}/common/endorsed/sax.jar;
    ${JAXR_HOME}/common/lib/commons-logging.jar;
    ${JAXR_HOME}/common/lib/dom4j.jar;
    ${JAXR_HOME}/common/lib/fscontext.jar;
    ${JAXR_HOME}/common/lib/providerutil.jar
    ${TS_HOME}/lib/javatest.jar;${TS_HOME}/lib/harness.jar
    ----

    If you are running on Windows 2000/XP and you are using the JAXR
    RI, Version 1.0_01 from the Java Web Services Developer Pack, you
    would set the following classpath:

    ----
    set LOCAL_CLASSES=%JAXR_HOME%\common\lib\soap.jar;
    %JAXR_HOME%\common\endorsed\xercesImpl.jar;
    %JAXR_HOME%\common\lib\jaxr-api.jar;
    %JAXR_HOME%\common\lib\mail.jar;
    %JAXR_HOME%\common\lib\activation.jar;
    %JAXR_HOME%\common\lib\castor-0.9.3.9-xml.jar;
    %JAXR_HOME%\common\endorsed\xalan.jar;
    %JAXR_HOME%\common\lib\jsse.jar;
    %JAXR_HOME%\common\lib\jcert.jar;
    %JAXR_HOME%\common\lib\jnet.jar;
    %JAXR_HOME%\common\lib\jaas.jar;
    %JAXR_HOME%\common\lib\jaxr-ri.jar;
    %JAXR_HOME%\common\lib\saaj-api.jar;
    %JAXR_HOME%\common\lib\saaj-ri.jar;
    %JAXR_HOME%\common\lib\jaxp-api.jar;
    %JAXR_HOME%\common\endorsed\dom.jar;
    %JAXR_HOME%\common\endorsed\sax.jar;
    %JAXR_HOME%\common\lib\commons-logging.jar;
    %JAXR_HOME%\common\lib\dom4j.jar;
    %JAXR_HOME%\common\lib\fscontext.jar;
    %JAXR_HOME%\common\lib\providerutil.jar
    %TS_HOME%\lib\javatest.jar;%TS_HOME%\lib\harness.jar
    ----


Edit your `ts.jte` file and modify the following environment settings:

1.  Set the `registryURL` property to the publishing registry URL.

2.  Set the `queryManagerURL` property to the query registry URL.

3.  Set the `httpProxyHost` property to the HTTP proxy host.

4.  Set the `httpProxyPort` property to the HTTP proxy port, usually 8080.
5.  Set the `httpsProxyHost` property to the secure HTTP proxy host.
6.  Set the `httpsProxyPort` property to the secure HTTP proxy port, usually 8080.
7.  Set the `proxyUserName` property to the username for the proxy host, if one is required.
8.  Set the `proxyPassword` property to the password for the proxy, if one is required.
9.  Set the `jaxrUser` property to the username for the registry.
10. Set the `jaxrPassword` property to the password for the registry.
11. Set the `jaxrUser2` property to a second username for the registry.
12. Set the `jaxrPassword2` property to a second password for the registry.
13. Set the `providerCapability` property to correspond to the capability level of your
    JAXR Provider. The default setting is level 0.
    The following example shows the properties that were set for use
    with the Java Web Services Developer Pack Registry Server 1.0.

    `registryURL = http://hostname:8080/registry-server/`
    `RegistryServerServlet`
    `queryManagerURL = http://hostname:8080/registry-server/`
    `RegistryServerServlet`
    `jaxrUser=testuser`
    `jaxrPassword=testuser`
    `jaxrUser2=testuser2`
    `jaxrPassword2=testuser2`
    `httpProxyHost=myproxy.mycompany.com`
    `httpProxyPort=8080`
    `httpsProxyHost=myproxy.mycompany.com`
    `httpsProxyPort=8080`
    `proxyUserName=“”`
    `proxyPassword=“”`
    `providerCapability=0`

    [NOTE]
    ===========================================================================
    The hostname must be fully qualified. You can specify localhost for a local
    registry. The default username and password for the Java WSDP registry is
    testuser. After the Java Web Services Developer Pack was installed, a second
    account was created for testuser2/testuser2. See the Java Web Services
    Developer Pack documentation for information about adding new users to the
    registry.
    ===========================================================================
    If you are using the Microsoft UDDI V2 registry, specify the
    following urls in the ts.jte file.

    `registryUrl = https://uddi.rte.microsoft.com/publish`
    `queryManagerUrl = http://uddi.rte.microsoft.com/inquire`
    `registryURL = https://www-3.ibm.com/services/uddi/v2beta`
    `/protect/publishapi`
    `queryManagerURL = http://www-3.ibm.com/services/uddi/v2beta`
    `/inquiryapi`

14. You can specify proxy settings as properties in the `ts.jte` file or on the
    command line. The following example adds proxy settings to provide access to
    the registry through a firewall and adds debug logging level on the command
    line for the JAXR RI, Version 1.0_01 on UNIX.

    `env.ts-unix.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \`
    `CLASSPATH=${TS_HOME}/lib/harness.jar:${TS_HOME}/lib/javatest.jar:\`
    `${TS_HOME}/classes:${JAVA_HOME}/jre/lib/rt.jar:${LOCAL_CLASSES} \`
    `DISPLAY=:0.0 \`
    `HOME=${user.home} \`
    `${JAVA_HOME}/bin/java \`
    `-Dorg.apache.commons.logging.simplelog.log.com.sun.xml.registry=debug \`
    `-Dhttp.proxySet="true" \`
    `-Dhttp.proxyHost=myproxy.mycompany.com \`
    `-Dhttp.proxyPort=8080 \`
    `-Dhttps.proxyHost=myproxy.mycompany.com \`
    `-Dhttps.proxyPort=8080 \`
    `${JAVA_OPTIONS} \`
    `$testExecuteClass \`
    `$testExecuteArgs`


[[GCLHU]][[configuring-your-environment-to-repackage-and-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Repackage and Run the TCK Against the Vendor Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Update the configuration as described above.
