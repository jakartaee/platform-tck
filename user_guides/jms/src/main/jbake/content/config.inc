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
continue with the instructions in link:#GBFUY[Section 4.6, "Using the
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


[[GBFWN]][[to-configure-your-environment-for-the-jms-tck]]

4.1.1 To Configure Your Environment for the JMS TCK
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1.  Set the following environment variables in your shell environment:
* `JAVA_HOME` to the directory in which Java SE 7 is installed
* `TS_HOME` to the directory in which the JMS TCK 2.0 software is
installed
* `JMS_HOME` to the directory in which the JMS 2.0 RI has been installed
* `PATH` to include the following directories: `JAVA_HOME/bin`,
`JMS_HOME/bin`, and `<TS_HOME>/tools/ant/bin`
2.  Edit your `<TS_HOME>/bin/ts.jte` file and set the following
properties:
* `jms.home` to the directory in which you installed your JMS 2.0
implementation
* `jms.classes` to the JAR file(s) that contain your JMS 2.0
implementation classes +
For example, if using the JMS 2.0 RI , the `jms.classes` property would
be set to `jms.classes=${ri.jars}`.
* `impl.vi` to the JMS 2.0 implementation being tested +
This property signals which configuration script to execute for the
`config.vi` and `clean.vi` Ant targets. These targets handle starting
and stopping the JMS implementation under test as well as creating and
deleting all of the JMS administered objects and JMS users needed by the
JMS TCK. +
The `ts.jte` file supports two JMS 2.0 Reference Implementations: JMS
2.0 RI and Oracle Open Message Queue Version 5.0 product. +
For example, if using the JMS 2.0 RI , the `impl.vi` property is set to
`impl.vi=ri`. With this setting, the config.vi and clean.vi ant targets
will invoke the script under `<TS_HOME>/bin/xml/impl/ri/config.vi.xml`.
* `user` to a valid user name in the JMS implementation
* `password` to the password for the user that was specifiedwith the
`user` property
* `jms_timeout` to the length of time used to receive messages
* `harness.log.port` to the port number that will be used for logging
* `porting.ts.jmsObjects.class.1` to point to your porting
implementation, if you implemented the TSJMSObjectInterface interface +
If you did not implement the interface, use the default setting.
3.  If your JMS 2.0 implementation does not support the `MY_QUEUE`,
`MY_TOPIC`, `MyQueueConnectionFactory`, or `MyTopicConnectionFactory`
JNDI names, you must provide your own implementation of the
`TSJMSObjectInterface` interface. See link:#GCFQK[Implementing the JMS
TCK Porting Package Interface] for additional infomation about the
TSJMSObjectInterface interface.
4.  If you are using the JMS 2.0 RI on the Windows platform, edit the
`<TS_HOME>/bin/ts.jte` file and add the drive letter to the
`admin.pass.file` and `jndi.fs.dir` properties. The property settings
for the Windows platform are as follows:
+
--
[source,oac_no_warn]
----
admin.pass.file=C:/tmp/ripassword
jndi.fs.dir=C:/tmp/ri_admin_objects
----
--
+
Edit the `<TS_HOME>/bin/xml/impl/ri/jndi.properties` file and add the
drive letter to the `java.naming.provider.url` property. The property
setting for the Windows platform is as follows:
+
--
[source,oac_no_warn]
----
java.naming.provider.url=file:///C:/tmp/ri_admin_objects
----
--


[[to-configure-and-start-your-jms-2.0-implementation]]
4.1.2 To Configure and Start Your JMS 2.0 Implementation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1.  Set `JMS_HOME` to the location where your JMS implementation has
been installed.
2.  Start the JNDI service.
+
--
[NOTE]
=======================================================================

A Vendor Implementation is not required to use a JNDI provider as the
object store for JMS administered objects. For more information, see the
`TSJMSObjectsInterface`, an interface that defines a porting layer for
looking up the JMS administered objects. Vendors must either implement
this porting package for their specific implementation or use the
provided porting package and JNDI file system provider.

=======================================================================
--
+
3.  Start your JMS implementation.
4.  Add the following administered objects:
+
--
[width="80%",cols="50%,50%",options="header",]
|====================================================================
|Name |Type
|`MY_QUEUE` |`javax.jms.Queue`
|`MY_QUEUE2` |`javax.jms.Queue`
|`testQ0` |`javax.jms.Queue`
|`testQ1` |`javax.jms.Queue`
|`testQ2` |`javax.jms.Queue`
|`testQueue2` |`javax.jms.Queue`
|`Q2` |`javax.jms.Queue`
|`MY_TOPIC` |`javax.jms.Topic`
|`MY_TOPIC2` |j`avax.jms.Topic`
|`testT0` |`javax.jms.Topic`
|`testT1` |j`avax.jms.Topic`
|`testT2` |`javax.jms.Topic`
|`MyConnectionFactory` |`javax.jms.ConnectionFactory`
|`MyQueueConnectionFactory` |`javax.jms.QueueConnectionFactory`
|`MyTopicConnectionFactory` |`javax.jms.TopicConnectionFactory`
|`DURABLE_SUB_CONNECTION_FACTORY` |`javax.jms.TopicConnectionFactory`
|====================================================================
--


[NOTE]
=======================================================================

`jms/DURABLE_SUB_CONNECTION_FACTORY` must support durable subscriptions
and must be created with `clientID=cts`.

The sections that follow explain how to automatically and manually
create these administered objects using the JMS 2.0 RI.

=======================================================================


[[CEGBFJJB]]automatic-configuration-and-startup-of-the-jms-ri]]

4.1.3 Automatic Configuration and Startup of the JMS RI
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This section is optional. You do not have to start up and configure the
JMS RI as part of the certification process. You may, however, want to
do so to familiarize yourself with the testing process.

The steps in this section explain how to automatically handle the
configuration and startup procedures, something you may want to do with
your implementation, especially if you have want to test repeatedly. You
can examine the provided scripts, then modify them for use with the
implementaation under test. link:#GLQOJ[Section 4.1.4, "Manual
Configuration and Startup of the JMS RI,"] provides the manual steps for
doing what the provided scripts do automatically.

Complete the following steps to automatically configure and start up the
JMS RI:

1.  Set the following environment variables in your shell environment:
* `JAVA_HOME` to the directory in which the Java SE 7 software has been
installed
* `IMQ_JAVAHOME` to the value of `JAVA_HOME`. +
This is needed for JMS 2.0 RI commands and scripts.
* `TS_HOME` to the directory in which the JMS TCK software has been
installed
* `JMS_HOME` to the directory in which your JMS 2.0 implementation has
been installed
* `PATH` to include the following directories: `JAVA_HOME/bin`,
`JMS_HOME/bin`, and `/bin``<TS_HOME>/tools/ant`
2.  If you are using the JMS 2.0 RI on the Windows platform, edit the
`<TS_HOME>/bin/ts.jte` file and add the drive letter to the
`admin.pass.file` and `jndi.fs.dir` properties. The property settings
for the Windows platform are as follows: +
[source,oac_no_warn]
----
admin.pass.file=C:/tmp/ripassword
jndi.fs.dir=C:/tmp/ri_admin_objects
----
Edit the `<TS_HOME>/bin/xml/impl/ri/jndi.properties` file and add the
drive letter to the `java.naming.provider.url` property. The property
setting for the Windows platform is as follows: +
[source,oac_no_warn]
----
java.naming.provider.url=file:///C:/tmp/ri_admin_objects
----
3.  Invoke `config.vi`, the Ant configuration script, to start and
configure the JMS RI. +
The JMS TCK comes with a configuration XML file, which automates
starting and stopping the JMS service as well as creating and deleting
all of the JMS administered objects and JMS users needed by the JMS TCK.
Use the following command to invoke the configuration XML file to start
and configure the JJMS TCK. +
[source,oac_no_warn]
----
cd $TS_HOME/bin
ant config.vi
----
When used with the JMS 2.0 RI or the Oracle Open Message Queue 5.0
product, this target invokes the
<`TS_HOME>/bin/xml/impl/ri/config.vi.xml` script, which starts the JMS
service and creates the JMS administered objects and JMS users needed by
the JMS TCK. In the `ts.jte` file, the JMS 2.0 implementation property,
`impl.vi`, needs to be set to `ri` and the `jms.classes` property needs
to be set to `${ri.jars}` for the JMS RI or the Oracle Open Message
Queue product. +
To automate the process of creating the JMS administered obects and JMS
users for your JMS implementation, provide your own Ant-based
configuration file, name it `<TS_HOME>/bin/xml/impl/vi/config.vi.xml`,
and set the `impl.vi` property to vi in your `ts.jte` file. When you
execute the `ant config.vi` target, the script invokes and executes your
script `<TS_HOME>/bin/xml/impl/vi/config.vi.xml`.
4.  Invoke `clean.vi`, an Ant configuration script, to stop and
unconfigure the JMS RI. +
The JMS TCK comes with an XML configuration file that automates starting
and stopping the JMS service as well as creating and deleting all of the
JMS administered objects and JMS users needed by the JMS TCK. Use the
following command to invoke the configuration XML file to stop and
unconfigure the JMS RI. +
[source,oac_no_warn]
----
cd $TS_HOME/bin
ant clean.vi
----
If you are using the JMS RI or the Oracle Open Message Queue product,
this target invokes the `<TS_HOME>/bin/xml/impl/ri/config.vi.xml` script
which stops the JMS service and deletes all the JMS administered objects
and JMS users needed by the JMS TCK. In the `ts.jte` file, set the
`impl.vi` property to `ri` and the `jms.classes` property to
`${ri.jars}` for the JMS RI or the Oracle Open Message Queue product.

[[GLQOJ]][[manual-configuration-and-startup-of-the-jms-ri]]

4.1.4 Manual Configuration and Startup of the JMS RI
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This section is optional. You do not have to start up and configure the
JMS RI as part of the certification process. You may, however, want to
do so to familiarize yourself with the testing process.

The steps in this section explain how to manually configure and start up
the JMS RI, something you will do with your implementation. If you have
to test repeatedly, you may want to examine the provided scripts that
automate the configuration and startup procedures, then modify them for
use with the implementaation under test. link:#CEGBFJJB[Section 4.1.3,
"Automatic Configuration and Startup of the JMS RI,"] provides the steps
for using the provided scripts do perform these steps automatically.

Complete the following steps to manually configure and start up the JMS
RI:

1.  Set the following environment variables in your shell environment:
* `JAVA_HOME` to the directory in which the Java SE 7 software has been
installed
* `IMQ_JAVAHOME` to the value of `JAVA_HOME`. +
This is needed for JMS 2.0 RI commands and scripts.
* `TS_HOME` to the directory in which the JMS TCK Version 2.0 software
has been installed
* `JMS_HOME` to the directory in which your JMS 2.0 implementation has
been installed
* `PATH` to include the following directories: `JAVA_HOME/bin`,
`JMS_HOME/bin`, and `<TS_HOME>/tools/ant/bin`
2.  Use the following command to start the JMS Message Queue Broker
Service:
+
--
[source,oac_no_warn]
----
(UNIX) $JMS_HOME/bin/imqbrokerd
----
--
+
3.  Use the following command to create the j2ee TCK user in the JMS
Message Queue user repository:
+
--
[source,oac_no_warn]
----
(UNIX) $JMS_HOME/bin/imqusermgr add -f -u j2ee -p j2ee -g admin
----
--
+
4.  Use the following command to create the file system directory as the
object store for the JMS Administered Objects:
+
--
[source,oac_no_warn]
----
(UNIX) mkdir /tmp/ri_admin_objects
----
--
+
This directory must match the `jndi.fs.dir`. property setting in the
`ts.jte` file and the `java.naming.provider.url` property in the
`<TS_HOME>/bin/xml/impl/ri/jndi.properties` file.
5.  Create the following JMS administered objects:
+
--
[width="80%",cols="50%,50%",options="header",]
|====================================================================
|Name |Type
|`MY_QUEUE` |`javax.jms.Queue`
|`MY_QUEUE2` |`javax.jms.Queue`
|`testQ0` |`javax.jms.Queue`
|`testQ1` |`javax.jms.Queue`
|`testQ2` |`javax.jms.Queue`
|`testQueue2` |`javax.jms.Queue`
|`Q2` |`javax.jms.Queue`
|`MY_TOPIC` |`javax.jms.Topic`
|`MY_TOPIC2` |j`avax.jms.Topic`
|`testT0` |`javax.jms.Topic`
|`testT1` |j`avax.jms.Topic`
|`testT2` |`javax.jms.Topic`
|`MyConnectionFactory` |`javax.jms.ConnectionFactory`
|`MyQueueConnectionFactory` |`javax.jms.QueueConnectionFactory`
|`MyTopicConnectionFactory` |`javax.jms.TopicConnectionFactory`
|`DURABLE_SUB_CONNECTION_FACTORY` |`javax.jms.TopicConnectionFactory`
|====================================================================
--

Use the following commands to create the `MY_QUEUE` and `MY_TOPIC` JMS
administered objects. +
[source,oac_no_warn]
----
$JMS_HOME/bin/imqobjmgr -f add -l MY_QUEUE -o imqDestinationName=MY_QUEUE -t q \
-j java.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory \
-j java.naming.provider.url=file:////tmp/ri_admin_objects

$JMS_HOME/bin/imqobjmgr -f add -l MY_TOPIC -o imqDestinationName=MY_TOPIC -t t \
-j java.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory \
-j java.naming.provider.url=file:////tmp/ri_admin_objects
----
Use the following commands to create the `MyConnectionFactory`,
`MyQueueConnectionFactory`, and `MyTopicConnectionFactory` JMS
administered objects. +
[source,oac_no_warn]
----
$JMS_HOME/bin/imqobjmgr -f add -l MyConnectionFactory -t cf \
-j java.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory \
-j java.naming.provider.url=file:////tmp/ri_admin_objects

$JMS_HOME/bin/imqobjmgr -f add -l MyQueueConnectionFactory -t qf \
-j java.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory \
-j java.naming.provider.url=file:////tmp/ri_admin_objects

$JMS_HOME/bin/imqobjmgr -f add -l MyTopicConnectionFactory -t tf \
-j java.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory \
-j java.naming.provider.url=file:////tmp/ri_admin_objects
----
The `DURABLE_SUB_CONNECTION_FACTORY` administered object must support
durable subscriptions and must be created with `clientID=cts`. Use the
following command to create the `DURABLE_SUB_CONNECTION_FACTORY`
administered object. +
[source,oac_no_warn]
----
$JMS_HOME/bin/imqobjmgr -f add -l DURABLE_SUB_CONNECTION_FACTORY \
-o imqConfiguredClientID=cts -t tf \
-j java.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory \
-j java.naming.provider.url=file:////tmp/ri_admin_objects
----

[[GCFQK]][[implementing-the-jms-tck-porting-package-interface]]

4.2 Implementing the JMS TCK Porting Package Interface
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

You may need to provide your own implementation of the
`TSJMSObjectsInterface` porting package interface that is provided with
the JMS TCK.

Note that if you do implement this interface, you will also need to set
the `porting.ts.jmsObjects.class.1` property in the `ts.jte` file for
accessing your porting implementation class. The default for this
property value is set to the implementation used by the JMS Reference
Implementation. You may need to set this value to point to your
implementation of this interface. See link:#GLQOJ[Manual Configuration
and Startup of the JMS RI] for information about modifying the `ts.jte`
file.

The `TSJMSObjectsInterface` interface looks like this:

[source,oac_no_warn]
----
public interface TSJMSObjectsInterface { 
public Queue getQueue(java.lang.String name)
        throws java.lang.Exception;
public Topic getTopic(java.lang.String name)
        throws java.lang.Exception;
public TopicConnectionFactory
        getTopicConnectionFactory(java.lang.String name)
throws java.lang.Exception;
        public QueueConnectionFactory
getQueueConnectionFactory(java.lang.String name)
        throws java.lang.Exception;
        public ConnectionFactory
getConnectionFactory(java.lang.String name)
        throws java.lang.Exception;
}
----

where:

* The `getQueue` method, which is used to get a Queue, accepts the name
of the queue as an input parameter.
* The `getTopic` method, which is used to get a Topic, accepts the name
of the topic as an input parameter.
* The `getTopicConnectFactory` method, which is used to get a
`TopicConnectionFactory`, accepts the name of the
`TopicConnectionFactory` as an input parameter.
* The `getQueueConnectionFactory` method, which is used to get a
`QueueConnectionFactory`, accepts the name of the
`QueueConnectionFactory` as an input parameter.
* The `getConnectionFactory` method, which is used to get a
`ConnectionFactory`, accepts the name of the `ConnectionFactory` as an
input parameter.

Make sure that you set the value of the `porting.ts.jmsObjects.class.1`
property in the `ts.jte` file to point to your implementation of the
`TSJMSObjectsInterface`. Refer to link:#GLQOJ[Manual Configuration and
Startup of the JMS RI] for information about the list of administered
objects you may need to manually create.

A sample implementation of the `TSJMSObjectsInterface` porting package,
`SunRIJMSObjects.java`, is provided with the JMS TCK and can be found in
the `<TS_HOME>/src/com/sun/ts/lib/implementation/sun/jms`. directory.


[[GHGDG]][[publishing-the-test-applications]]

4.3 Publishing the Test Applications
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Not needed for the {TechnologyShortName} TCK.
