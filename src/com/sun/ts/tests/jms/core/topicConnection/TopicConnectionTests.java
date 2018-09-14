/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core.topicConnection;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.util.*;
import com.sun.javatest.Status;
import javax.jms.*;
import java.io.*;

public class TopicConnectionTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.topicConnection.TopicConnectionTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // Harness req's
  private Properties props = null;

  // JMS object
  private transient JmsTool tool = null;

  // properties read from ts.jte file
  long timeout;

  private String jmsUser;

  private String jmsPassword;

  private String mode;

  ArrayList connections = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    TopicConnectionTests theTests = new TopicConnectionTests();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Utility methods for tests */

  /*
   * Checks passed flag for negative tests and throws exception back to caller
   * which passes ot to harness.
   * 
   * @param boolean Pass/Fail flag
   */
  private void checkExceptionPass(boolean passed) throws Exception {
    if (passed == false) {
      logMsg("Didn't get expected exception");
      throw new Exception("Didn't catch expected exception");
    }
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
      logTrace("In setup");
      // get props
      jmsUser = p.getProperty("user");
      jmsPassword = p.getProperty("password");
      mode = p.getProperty("platform.mode");
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
      connections = new ArrayList(5);

      // get ready for new test
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * Closes the default connections that are created by setup(). Any separate
   * connections made by individual tests should be closed by that test.
   * 
   * @exception Fault
   */
  public void cleanup() throws Fault {
    try {
      if (tool != null) {
        logMsg("Cleanup: Closing Topic Connections");
        tool.closeAllConnections(connections);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: connNotStartedTopicTest
   *
   * @assertion_ids: JMS:SPEC:97; JMS:JAVADOC:522;
   *
   * @test_Strategy: Make certain that one can not receive a message on a
   * non-started connection. Setup to test assertion involves a best effort
   * attempt to ensure that subscriber has a message to deliver but can not
   * because the connection has never been started. Create two subscribers to
   * the same topic on different connections. Only start one of the connections.
   * Publish messages to the topic. Receive the messages on the started
   * connection. Make sure the message is not available to be received on the
   * non-started connection. Start the previously non-started connection and
   * make sure that the message are now received.
   *
   */
  public void connNotStartedTopicTest() throws Fault {
    boolean pass = true;
    String lookup = "MyTopicConnectionFactory";
    final int NUM_MSGS = 5;

    try {
      Topic topic = null;
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      TopicConnection nonStartedConn = null;
      TopicSession nonStartedSess = null;
      TopicSubscriber nonStartedSub = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
      topic = tool.getDefaultTopic();

      nonStartedConn = (TopicConnection) tool.getNewConnection(JmsTool.TOPIC,
          jmsUser, jmsPassword, lookup);
      connections.add(nonStartedConn);
      nonStartedSess = nonStartedConn.createTopicSession(false,
          Session.AUTO_ACKNOWLEDGE);
      nonStartedSub = nonStartedSess.createSubscriber(topic);

      tool.getDefaultTopicConnection().start();

      logMsg("Creating message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "connNotStartedTopicTest");

      logMsg("Publishing messages");
      for (int i = 0; i < NUM_MSGS; i++) {
        messageSent.setText("just a test. Msg num " + i);
        tool.getDefaultTopicPublisher().publish(messageSent);
      }

      // receive messages on started connection.
      logMsg("Receive  messages on started connection");
      for (int i = 0; i < NUM_MSGS; i++) {
        messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
            .receive(timeout);
        if (messageReceived == null) {
          logMsg("Fail: Did not receive expected message");
          pass = false;
        }
      }

      // ensure that can not receive messages on non-started connection.
      logMsg("Ensure messages not received on non-started connection");
      for (int i = 0; i < NUM_MSGS; i++) {
        messageReceived = (TextMessage) nonStartedSub.receiveNoWait();
        if (messageReceived != null) {
          logMsg("Fail: Received message on a non-started connection");
          pass = false;
        }
      }

      // Start connection and verify messages can be received.
      logMsg("Ensure messages received on now started connection");
      nonStartedConn.start();
      for (int i = 0; i < NUM_MSGS; i++) {
        messageReceived = (TextMessage) nonStartedSub.receive(timeout);
        if (messageReceived == null) {
          logMsg("Fail: Did not receive expected message");
          pass = false;
        }
      }
      nonStartedConn.close();

      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("connNotStartedTopicTest");
    }
  }

  /*
   * @testName: metaDataTests
   * 
   * @assertion_ids: JMS:JAVADOC:486; JMS:JAVADOC:488; JMS:JAVADOC:490;
   * JMS:JAVADOC:492; JMS:JAVADOC:494; JMS:JAVADOC:496; JMS:JAVADOC:498;
   * 
   * @test_Strategy: Create a TopicConnection to get ConnectionMetaData. Verify
   * that all Content of the ConnectionMetaData matches the type defined in API
   * and JMS API version is correct.
   */

  public void metaDataTests() throws Fault {
    boolean pass = true;
    ConnectionMetaData data = null;

    try {
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
      data = tool.getDefaultTopicConnection().getMetaData();

      if (!verifyMetaData(data))
        pass = false;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        tool.getDefaultTopicConnection().close();
      } catch (Exception e) {
        logErr("Error closing TopicConnection in metaDataTests : ", e);
      }
    }

    try {
      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
      data = tool.getDefaultConnection().getMetaData();

      if (!verifyMetaData(data))
        pass = false;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        tool.getDefaultConnection().close();
      } catch (Exception e) {
        logErr("Error closing Connection in metaDataTests : ", e);
      }
    }

    if (!pass) {
      throw new Fault("Error: metaDataTests failed");
    }
  }

  private boolean verifyMetaData(ConnectionMetaData data) {
    boolean pass = true;

    try {
      String tmp = data.getJMSVersion();
      TestUtil.logTrace("JMSVersion=" + tmp);

      if (!tmp.equals(JmsTool.JMS_VERSION)) {
        logErr("Error: incorrect JMSVersion=" + tmp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for JMSVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getJMSMajorVersion();
      TestUtil.logTrace("JMSMajorVersion=" + tmp);

      if (tmp != JmsTool.JMS_MAJOR_VERSION) {
        logErr("Error: incorrect JMSMajorVersion=" + tmp);
        pass = false;
      }
    } catch (Exception e) {
      logErr("Error: incorrect type returned for JMSMajorVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getJMSMinorVersion();
      TestUtil.logTrace("JMSMinorVersion=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for JMSMinorVersion: ", e);
      pass = false;
    }

    try {
      String tmp = data.getJMSProviderName();
      TestUtil.logTrace("JMSProviderName=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for JMSProviderName: ", e);
      pass = false;
    }

    try {
      String tmp = data.getProviderVersion();
      TestUtil.logTrace("JMSProviderVersion=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for ProviderVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getProviderMajorVersion();
      TestUtil.logTrace("ProviderMajorVersion=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for ProviderMajorVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getProviderMinorVersion();
      TestUtil.logTrace("ProviderMinorVersion=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for ProviderMinorVersion: ", e);
      pass = false;
    }
    return pass;
  }
}
