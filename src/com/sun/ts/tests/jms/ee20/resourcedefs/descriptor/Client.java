/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */
package com.sun.ts.tests.jms.ee20.resourcedefs.descriptor;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import javax.naming.*;
import javax.annotation.Resource;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSConnectionFactoryDefinition;
import javax.jms.JMSConnectionFactoryDefinitions;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;
import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS objects
  private transient ConnectionFactory cf = null;

  private transient ConnectionFactory cfra = null;

  private transient QueueConnectionFactory qcf = null;

  private transient TopicConnectionFactory tcf = null;

  private transient TopicConnectionFactory dtcf = null;

  private transient Topic topic = null;

  private transient Topic topica = null;

  private transient Queue queue = null;

  private transient JMSContext context = null;

  private transient JMSConsumer consumerQ = null;

  private transient JMSProducer producerQ = null;

  private transient JMSConsumer consumerT = null;

  private transient JMSProducer producerT = null;

  private boolean queueTest = false;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  String vehicle;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   * @exception Fault
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      // get props
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      user = p.getProperty("user");
      password = p.getProperty("password");
      mode = p.getProperty("platform.mode");
      vehicle = p.getProperty("vehicle");

      // check props for errors
      if (timeout < 1) {
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }

      TestUtil.logMsg("Lookup JMS factories defined in Deployment Descriptors");
      TestUtil
          .logMsg("Lookup JMS destinations defined in Deployment Descriptors");
      TestUtil.logMsg(
          "See <jms-connectionfactory> and <jms-destination> tags in DD's");
      TSNamingContext namingctx = new TSNamingContext();
      String prefix = null;
      if (vehicle.equals("appclient"))
        prefix = "AppClient";
      else if (vehicle.equals("ejb"))
        prefix = "EJB";
      else if (vehicle.equals("servlet"))
        prefix = "Servlet";
      else if (vehicle.equals("jsp"))
        prefix = "JSP";
      TestUtil
          .logMsg("Lookup java:global/" + prefix + "MyTestConnectionFactory");
      cf = (ConnectionFactory) namingctx
          .lookup("java:global/" + prefix + "MyTestConnectionFactory");
      TestUtil
          .logMsg("Lookup java:app/" + prefix + "MyTestQueueConnectionFactory");
      qcf = (QueueConnectionFactory) namingctx
          .lookup("java:app/" + prefix + "MyTestQueueConnectionFactory");
      TestUtil.logMsg(
          "Lookup java:module/" + prefix + "MyTestTopicConnectionFactory");
      tcf = (TopicConnectionFactory) namingctx
          .lookup("java:module/" + prefix + "MyTestTopicConnectionFactory");
      TestUtil.logMsg("Lookup java:comp/env/jms/" + prefix
          + "MyTestDurableTopicConnectionFactory");
      dtcf = (TopicConnectionFactory) namingctx.lookup("java:comp/env/jms/"
          + prefix + "MyTestDurableTopicConnectionFactory");
      TestUtil.logMsg("Lookup java:global/env/" + prefix + "MyTestQueue");
      queue = (Queue) namingctx
          .lookup("java:global/env/" + prefix + "MyTestQueue");
      TestUtil.logMsg("Lookup java:app/env/" + prefix + "MyTestTopic");
      topic = (Topic) namingctx
          .lookup("java:app/env/" + prefix + "MyTestTopic");

      TestUtil.logMsg("Create JMSContext, JMSProducer's and JMSConsumer's");
      context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      producerQ = context.createProducer();
      consumerQ = context.createConsumer(queue);
      producerT = context.createProducer();
      consumerT = context.createConsumer(topic);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("setup failed!", e);
    }
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * @exception Fault
   */
  public void cleanup() throws Fault {
    try {
      if (queueTest && consumerQ != null) {
        TestUtil.logMsg("Flush any messages left on Queue");
        Message rmsg = consumerQ.receive(timeout);
        while (rmsg != null) {
          rmsg = consumerQ.receiveNoWait();
          if (rmsg == null) {
            rmsg = consumerQ.receiveNoWait();
          }
        }
        consumerQ.close();
      }
      if (consumerT != null)
        consumerT.close();
      TestUtil.logMsg("Close JMSContext Objects");
      if (context != null)
        context.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("cleanup failed!", e);
    }
  }

  /*
   * @testName: sendAndRecvQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:1324; JMS:JAVADOC:1325; JMS:JAVADOC:1327;
   * JMS:JAVADOC:1330; JMS:JAVADOC:1331; JMS:JAVADOC:1332; JMS:JAVADOC:1333;
   * JMS:JAVADOC:1334; JMS:JAVADOC:1335; JMS:JAVADOC:1336; JMS:JAVADOC:1338;
   * JMS:JAVADOC:1339; JMS:JAVADOC:1342; JMS:JAVADOC:1343; JMS:JAVADOC:1344;
   * JMS:JAVADOC:1345; JMS:JAVADOC:1346; JMS:JAVADOC:1347; JMS:JAVADOC:1348;
   * JMS:JAVADOC:1451; JMS:JAVADOC:1452;
   *
   * @test_Strategy: Send and receive a message to/from a Queue.
   *
   */
  public void sendAndRecvQueueTest() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      queueTest = true;
      // send and receive TextMessage
      TestUtil.logMsg(
          "Creating TextMessage via JMSContext.createTextMessage(String)");
      TextMessage expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvQueueTest");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producerQ.send(queue, expTextMessage);
      TestUtil.logMsg("Receive TextMessage via JMSconsumer.receive(long)");
      TextMessage actTextMessage = (TextMessage) consumerQ.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      } else {
        TestUtil.logMsg("Check the value in TextMessage");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("sendAndRecvQueueTest", e);
    }

    if (!pass) {
      throw new Fault("sendAndRecvQueueTest failed");
    }
  }

  /*
   * @testName: sendAndRecvTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:1324; JMS:JAVADOC:1325; JMS:JAVADOC:1327;
   * JMS:JAVADOC:1330; JMS:JAVADOC:1331; JMS:JAVADOC:1332; JMS:JAVADOC:1333;
   * JMS:JAVADOC:1334; JMS:JAVADOC:1335; JMS:JAVADOC:1336; JMS:JAVADOC:1338;
   * JMS:JAVADOC:1339; JMS:JAVADOC:1342; JMS:JAVADOC:1343; JMS:JAVADOC:1344;
   * JMS:JAVADOC:1345; JMS:JAVADOC:1346; JMS:JAVADOC:1347; JMS:JAVADOC:1348;
   * JMS:JAVADOC:1451; JMS:JAVADOC:1452;
   *
   * @test_Strategy: Send and receive a message to/from a Topic.
   *
   */
  public void sendAndRecvTopicTest() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      queueTest = false;
      // send and receive TextMessage
      TestUtil.logMsg(
          "Creating TextMessage via JMSContext.createTextMessage(String)");
      TextMessage expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvTopicTest");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producerT.send(topic, expTextMessage);
      TestUtil.logMsg("Receive TextMessage via JMSconsumer.receive(long)");
      TextMessage actTextMessage = (TextMessage) consumerT.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      } else {
        TestUtil.logMsg("Check the value in TextMessage");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("sendAndRecvTopicTest", e);
    }

    if (!pass) {
      throw new Fault("sendAndRecvTopicTest failed");
    }
  }

  /*
   * @testName: checkClientIDOnDurableConnFactoryTest
   *
   * @assertion_ids: JMS:JAVADOC:1324; JMS:JAVADOC:1325; JMS:JAVADOC:1327;
   * JMS:JAVADOC:1330; JMS:JAVADOC:1331; JMS:JAVADOC:1332; JMS:JAVADOC:1333;
   * JMS:JAVADOC:1334; JMS:JAVADOC:1335; JMS:JAVADOC:1336; JMS:JAVADOC:1338;
   * JMS:JAVADOC:1339; JMS:JAVADOC:1342; JMS:JAVADOC:1343; JMS:JAVADOC:1344;
   * JMS:JAVADOC:1345; JMS:JAVADOC:1346; JMS:JAVADOC:1347; JMS:JAVADOC:1348;
   * JMS:JAVADOC:1451; JMS:JAVADOC:1452;
   *
   * @test_Strategy: Check client id setting on durable connection factory
   *
   */
  public void checkClientIDOnDurableConnFactoryTest() throws Fault {
    boolean pass = true;
    JMSContext context = null;
    try {
      queueTest = false;
      TestUtil
          .logMsg("Create JMSContext from durable topic connection factory");
      TestUtil.logMsg(
          "Check the client id which is configured as MyClientID in the deployment descriptors");
      context = dtcf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      String clientid = context.getClientID();
      if (clientid == null) {
        TestUtil.logErr("Client ID value is null (expected MyClientID)");
        pass = false;
      } else if (clientid.equals("MyClientID")) {
        TestUtil.logMsg("Client ID value is correct (MyClientID)");
      } else {
        TestUtil
            .logErr("Client ID value is incorrect (expected MyClientID, got "
                + clientid + ")");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("checkClientIDOnDurableConnFactoryTest", e);
    } finally {
      try {
        if (context != null)
          context.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("checkClientIDOnDurableConnFactoryTest failed");
    }
  }

}
