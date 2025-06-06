/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core.closedTopicSession;

import java.util.ArrayList;
import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.common.MessageTestImpl;

import jakarta.jms.BytesMessage;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryTopic;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicPublisher;
import jakarta.jms.TopicSubscriber;

public class ClosedTopicSessionTests extends ServiceEETest {
  private static final String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

  private static final String testName = "com.sun.ts.tests.jms.core.closedTopicSession.ClosedTopicSessionTests";

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
    ClosedTopicSessionTests theTests = new ClosedTopicSessionTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /**
   * Used by tests that need a closed session for testing. Passes any exceptions
   * up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndCloseSession(int type) throws Exception {
    if ((type == JmsTool.TOPIC) || (type == JmsTool.TX_TOPIC)) {
      tool = new JmsTool(type, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      logMsg("Closing topic session");
      tool.getDefaultTopicSession().close();
    }
    logMsg("Session closed");
  }

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
  public void setup(String[] args, Properties p) throws Exception {
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

      // get ready for new test
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Setup failed!", e);
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
  public void cleanup() throws Exception {
    try {
      if (tool != null) {
        logMsg("Cleanup: Closing Queue and Topic Connections");
        tool.closeAllConnections(connections);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Exception("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: closedTopicSessionCommitTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:229;
   *
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */
  public void closedTopicSessionCommitTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TX_TOPIC);
      logMsg("Try to call commit with closed session.");
      try {
        tool.getDefaultTopicSession().commit();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCommitTest");
    }
  }

  /*
   * @testName: closedTopicSessionCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:SPEC:114; JMS:JAVADOC:233;
   * 
   * @test_Strategy: Close default session and call method on it.
   */

  public void closedTopicSessionCloseTest() throws Exception {
    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call close on closed session.");
      tool.getDefaultTopicSession().close();
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCloseTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateDurableSubscriberTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:87;
   *
   * @test_Strategy: Close default session and call method on Cannot call
   * CreateDurableSubscriber() on closed session it. Check for proper
   * JMSException.
   */

  public void closedTopicSessionCreateDurableSubscriberTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create DurableSubscriber with closed session.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createDurableSubscriber(tool.getDefaultTopic(),
                "TestDurableSubscriber");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateDurableSubscriberTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateDurableSubscriberMsgSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:89;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateDurableSubscriberMsgSelectorTest()
      throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg(
          "Try to create DurableSubscriber with message selector using closed session.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createDurableSubscriber(tool.getDefaultTopic(),
                "TestDurableSubscriberMsgSelector", "TEST = 'test'", false);
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception(
          "closedTopicSessionCreateDurableSubscriberMsgSelectorTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateTopicTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:81;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateTopicTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create topic with closed session.");
      try {
        Topic t = tool.getDefaultTopicSession()
            .createTopic("closedTopicSessionCreateTopicTest");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateTopicTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateSubscriberTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:83;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */
  public void closedTopicSessionCreateSubscriberTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create Subscriber with closed session.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createSubscriber(tool.getDefaultTopic());
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateSubscriberTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateSubscriberMsgSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:85;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateSubscriberMsgSelectorTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create Subscriber with closed session.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createSubscriber(tool.getDefaultTopic(), "TEST = 'test'", false);
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateSubscriberMsgSelectorTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreatePublisherTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:91;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  /**
   * Method Declaration.
   * 
   * 
   * @exception Fault
   *
   * @see
   */
  public void closedTopicSessionCreatePublisherTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create Publisher with closed session.");
      try {
        TopicPublisher tP = tool.getDefaultTopicSession()
            .createPublisher(tool.getDefaultTopic());
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreatePublisherTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateTempTopicTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:93;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateTempTopicTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create TemporaryTopic with closed session.");
      try {
        TemporaryTopic tT = tool.getDefaultTopicSession()
            .createTemporaryTopic();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateTempTopicTest");
    }
  }

  /*
   * @testName: closedTopicSessionUnsubscribeTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:95;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionUnsubscribeTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call unsubscribe with closed session.");
      try {
        tool.getDefaultTopicSession().unsubscribe("TestDurableSubscription");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionUnsubscribeTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:213;
   *
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create message with closed session.");
      try {
        Message m = tool.getDefaultTopicSession().createMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateMessageTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateBytesMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:209;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateBytesMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create BytesMessage with closed session.");
      try {
        BytesMessage m = tool.getDefaultTopicSession().createBytesMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateBytesMessageTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateMapMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:211;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateMapMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create MapMessage with closed session.");
      try {
        MapMessage m = tool.getDefaultTopicSession().createMapMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateMapMessageTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateObjectMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:215;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */
  public void closedTopicSessionCreateObjectMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create ObjectMessage with closed session.");
      try {
        ObjectMessage m = tool.getDefaultTopicSession().createObjectMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateObjectMessageTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateObject2MessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:217;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateObject2MessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create ObjectMessage(object) with closed session.");
      try {
        String s = "Simple object";
        ObjectMessage m = tool.getDefaultTopicSession().createObjectMessage(s);
        if (m != null)
          logMsg("ObjectMessage=" + m);
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateObject2MessageTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateStreamMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:219;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateStreamMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create StreamMessage with closed session.");
      try {
        StreamMessage m = tool.getDefaultTopicSession().createStreamMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateStreamMessageTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateTextMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:221;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateTextMessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create TextMessage with closed session.");
      try {
        TextMessage m = tool.getDefaultTopicSession().createTextMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateTextMessageTest");
    }
  }

  /*
   * @testName: closedTopicSessionCreateText2MessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:223;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionCreateText2MessageTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create TextMessage with closed session.");
      try {
        TextMessage m = tool.getDefaultTopicSession()
            .createTextMessage("test message");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionCreateText2MessageTest");
    }
  }

  /*
   * @testName: closedTopicSessionGetTransactedTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:225;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionGetTransactedTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call getTransacted() with closed session.");
      try {
        boolean b = tool.getDefaultTopicSession().getTransacted();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionGetTransactedTest");
    }
  }

  /*
   * @testName: closedTopicSessionRollbackTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:231;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionRollbackTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call rollback() with closed session.");
      try {
        tool.getDefaultTopicSession().rollback();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionRollbackTest");
    }
  }

  /*
   * @testName: closedTopicSessionRecoverTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:235;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedTopicSessionRecoverTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call recover() with closed session.");
      try {
        tool.getDefaultTopicSession().recover();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedTopicSessionRecoverTest");
    }
  }

  /*
   * @testName: closedTopicSessionSubscriberCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:338;
   * 
   * @test_Strategy: Close default subscriber and call method on it.
   */

  public void closedTopicSessionSubscriberCloseTest() throws Exception {
    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call close again");
      tool.getDefaultTopicSubscriber().close();
    } catch (Exception e) {
      throw new Exception("closedTopicSessionSubscriberCloseTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionGetMessageSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:326;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionGetMessageSelectorTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getMessageSelector");
      try {
        String foo = tool.getDefaultTopicSubscriber().getMessageSelector();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionGetMessageSelectorTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionReceiveTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:332;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionReceiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call receive");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receive();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionReceiveTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionReceiveTimeoutTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */
  public void closedTopicSessionReceiveTimeoutTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call receive(timeout)");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receive(timeout);

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionReceiveTimeoutTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionReceiveNoWaitTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:336;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionReceiveNoWaitTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call receiveNoWait");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receiveNoWait();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionReceiveNoWaitTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionGetNoLocalTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:79;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */
  public void closedTopicSessionGetNoLocalTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getNoLocal");
      try {
        boolean foo = tool.getDefaultTopicSubscriber().getNoLocal();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionGetNoLocalTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionSubscriberGetTopicTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:77;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionSubscriberGetTopicTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getTopic");
      try {
        Topic foo = tool.getDefaultTopicSubscriber().getTopic();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionSubscriberGetTopicTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionPublisherCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:315;
   * 
   * @test_Strategy: Close default publisher and call method on it.
   */

  public void closedTopicSessionPublisherCloseTest() throws Exception {
    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call close again");
      tool.getDefaultTopicPublisher().close();
    } catch (Exception e) {
      throw new Exception("closedTopicSessionPublisherCloseTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionGetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:303;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionGetDeliveryModeTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getDeliveryMode");
      try {
        int foo = tool.getDefaultTopicPublisher().getDeliveryMode();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionGetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionGetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:295;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionGetDisableMessageIDTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getDisableMessageID");
      try {
        boolean foo = tool.getDefaultTopicPublisher().getDisableMessageID();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionGetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionGetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:299;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */
  public void closedTopicSessionGetDisableMessageTimestampTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getDisableMessageTimestamp");
      try {
        boolean foo = tool.getDefaultTopicPublisher()
            .getDisableMessageTimestamp();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionGetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionGetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:307;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionGetPriorityTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getPriority");
      try {
        int foo = tool.getDefaultTopicPublisher().getPriority();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionGetPriorityTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionGetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:311;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionGetTimeToLiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getTimeToLive");
      try {
        long foo = tool.getDefaultTopicPublisher().getTimeToLive();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionGetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionSetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:301;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionSetDeliveryModeTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setDeliveryMode");
      try {
        tool.getDefaultTopicPublisher()
            .setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionSetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionSetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:293;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionSetDisableMessageIDTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setDisableMessageID");
      try {
        tool.getDefaultTopicPublisher().setDisableMessageID(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionSetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionSetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:297;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionSetDisableMessageTimestampTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setDisableMessageTimestamp");
      try {
        tool.getDefaultTopicPublisher().setDisableMessageTimestamp(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionSetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionSetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:305;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionSetPriorityTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setPriority");
      try {
        tool.getDefaultTopicPublisher().setPriority(Message.DEFAULT_PRIORITY);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionSetPriorityTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionSetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:309;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionSetTimeToLiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setTimeToLive");
      try {
        tool.getDefaultTopicPublisher()
            .setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionSetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionPublisherGetTopicTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:97;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionPublisherGetTopicTest() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getTopic");
      try {
        Topic foo = tool.getDefaultTopicPublisher().getTopic();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionPublisherGetTopicTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionPublish1Test
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:99;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionPublish1Test() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call publish(Message)");
      try {
        tool.getDefaultTopicPublisher().publish(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionPublish1Test", e);
    }
  }

  /*
   * @testName: closedTopicSessionPublish2Test
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:101;
   * 
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionPublish2Test() throws Exception {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call publish(Message,int,int,long)");
      try {
        tool.getDefaultTopicPublisher().publish(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicSessionPublish2Test", e);
    }
  }

}
