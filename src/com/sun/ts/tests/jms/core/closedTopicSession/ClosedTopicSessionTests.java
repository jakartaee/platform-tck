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
package com.sun.ts.tests.jms.core.closedTopicSession;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.util.*;
import com.sun.javatest.Status;
import javax.jms.*;
import java.io.*;

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
        logMsg("Cleanup: Closing Queue and Topic Connections");
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
   * @testName: closedTopicSessionCommitTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:229;
   *
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */
  public void closedTopicSessionCommitTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TX_TOPIC);
      logMsg("Try to call commit with closed session.");
      try {
        tool.getDefaultTopicSession().commit();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCommitTest");
    }
  }

  /*
   * @testName: closedTopicSessionCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:SPEC:114; JMS:JAVADOC:233;
   * 
   * @test_Strategy: Close default session and call method on it.
   */

  public void closedTopicSessionCloseTest() throws Fault {
    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call close on closed session.");
      tool.getDefaultTopicSession().close();
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCloseTest");
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

  public void closedTopicSessionCreateDurableSubscriberTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create DurableSubscriber with closed session.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createDurableSubscriber(tool.getDefaultTopic(),
                "TestDurableSubscriber");
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateDurableSubscriberTest");
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
      throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg(
          "Try to create DurableSubscriber with message selector using closed session.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createDurableSubscriber(tool.getDefaultTopic(),
                "TestDurableSubscriberMsgSelector", "TEST = 'test'", false);
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(
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

  public void closedTopicSessionCreateTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create topic with closed session.");
      try {
        Topic t = tool.getDefaultTopicSession()
            .createTopic("closedTopicSessionCreateTopicTest");
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateTopicTest");
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
  public void closedTopicSessionCreateSubscriberTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create Subscriber with closed session.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createSubscriber(tool.getDefaultTopic());
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateSubscriberTest");
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

  public void closedTopicSessionCreateSubscriberMsgSelectorTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create Subscriber with closed session.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createSubscriber(tool.getDefaultTopic(), "TEST = 'test'", false);
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateSubscriberMsgSelectorTest");
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
  public void closedTopicSessionCreatePublisherTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create Publisher with closed session.");
      try {
        TopicPublisher tP = tool.getDefaultTopicSession()
            .createPublisher(tool.getDefaultTopic());
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreatePublisherTest");
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

  public void closedTopicSessionCreateTempTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create TemporaryTopic with closed session.");
      try {
        TemporaryTopic tT = tool.getDefaultTopicSession()
            .createTemporaryTopic();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateTempTopicTest");
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

  public void closedTopicSessionUnsubscribeTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call unsubscribe with closed session.");
      try {
        tool.getDefaultTopicSession().unsubscribe("TestDurableSubscription");
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionUnsubscribeTest");
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

  public void closedTopicSessionCreateMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create message with closed session.");
      try {
        Message m = tool.getDefaultTopicSession().createMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateMessageTest");
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

  public void closedTopicSessionCreateBytesMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create BytesMessage with closed session.");
      try {
        BytesMessage m = tool.getDefaultTopicSession().createBytesMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateBytesMessageTest");
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

  public void closedTopicSessionCreateMapMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create MapMessage with closed session.");
      try {
        MapMessage m = tool.getDefaultTopicSession().createMapMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateMapMessageTest");
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
  public void closedTopicSessionCreateObjectMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create ObjectMessage with closed session.");
      try {
        ObjectMessage m = tool.getDefaultTopicSession().createObjectMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateObjectMessageTest");
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

  public void closedTopicSessionCreateObject2MessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create ObjectMessage(object) with closed session.");
      try {
        String s = "Simple object";
        ObjectMessage m = tool.getDefaultTopicSession().createObjectMessage(s);
        if (m != null)
          logMsg("ObjectMessage=" + m);
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateObject2MessageTest");
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

  public void closedTopicSessionCreateStreamMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create StreamMessage with closed session.");
      try {
        StreamMessage m = tool.getDefaultTopicSession().createStreamMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateStreamMessageTest");
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

  public void closedTopicSessionCreateTextMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create TextMessage with closed session.");
      try {
        TextMessage m = tool.getDefaultTopicSession().createTextMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateTextMessageTest");
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

  public void closedTopicSessionCreateText2MessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to create TextMessage with closed session.");
      try {
        TextMessage m = tool.getDefaultTopicSession()
            .createTextMessage("test message");
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionCreateText2MessageTest");
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

  public void closedTopicSessionGetTransactedTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call getTransacted() with closed session.");
      try {
        boolean b = tool.getDefaultTopicSession().getTransacted();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionGetTransactedTest");
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

  public void closedTopicSessionRollbackTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call rollback() with closed session.");
      try {
        tool.getDefaultTopicSession().rollback();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionRollbackTest");
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

  public void closedTopicSessionRecoverTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logMsg("Try to call recover() with closed session.");
      try {
        tool.getDefaultTopicSession().recover();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        passed = true;
      }
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicSessionRecoverTest");
    }
  }

  /*
   * @testName: closedTopicSessionSubscriberCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:338;
   * 
   * @test_Strategy: Close default subscriber and call method on it.
   */

  public void closedTopicSessionSubscriberCloseTest() throws Fault {
    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call close again");
      tool.getDefaultTopicSubscriber().close();
    } catch (Exception e) {
      throw new Fault("closedTopicSessionSubscriberCloseTest", e);
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

  public void closedTopicSessionGetMessageSelectorTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getMessageSelector");
      try {
        String foo = tool.getDefaultTopicSubscriber().getMessageSelector();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionGetMessageSelectorTest", e);
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

  public void closedTopicSessionReceiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call receive");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receive();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionReceiveTest", e);
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
  public void closedTopicSessionReceiveTimeoutTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call receive(timeout)");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receive(timeout);

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionReceiveTimeoutTest", e);
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

  public void closedTopicSessionReceiveNoWaitTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call receiveNoWait");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receiveNoWait();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionReceiveNoWaitTest", e);
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
  public void closedTopicSessionGetNoLocalTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getNoLocal");
      try {
        boolean foo = tool.getDefaultTopicSubscriber().getNoLocal();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionGetNoLocalTest", e);
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

  public void closedTopicSessionSubscriberGetTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getTopic");
      try {
        Topic foo = tool.getDefaultTopicSubscriber().getTopic();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionSubscriberGetTopicTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionPublisherCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:315;
   * 
   * @test_Strategy: Close default publisher and call method on it.
   */

  public void closedTopicSessionPublisherCloseTest() throws Fault {
    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call close again");
      tool.getDefaultTopicPublisher().close();
    } catch (Exception e) {
      throw new Fault("closedTopicSessionPublisherCloseTest", e);
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

  public void closedTopicSessionGetDeliveryModeTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getDeliveryMode");
      try {
        int foo = tool.getDefaultTopicPublisher().getDeliveryMode();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionGetDeliveryModeTest", e);
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

  public void closedTopicSessionGetDisableMessageIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getDisableMessageID");
      try {
        boolean foo = tool.getDefaultTopicPublisher().getDisableMessageID();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionGetDisableMessageIDTest", e);
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
  public void closedTopicSessionGetDisableMessageTimestampTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getDisableMessageTimestamp");
      try {
        boolean foo = tool.getDefaultTopicPublisher()
            .getDisableMessageTimestamp();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionGetDisableMessageTimestampTest", e);
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

  public void closedTopicSessionGetPriorityTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getPriority");
      try {
        int foo = tool.getDefaultTopicPublisher().getPriority();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionGetPriorityTest", e);
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

  public void closedTopicSessionGetTimeToLiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getTimeToLive");
      try {
        long foo = tool.getDefaultTopicPublisher().getTimeToLive();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionGetTimeToLiveTest", e);
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

  public void closedTopicSessionSetDeliveryModeTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setDeliveryMode");
      try {
        tool.getDefaultTopicPublisher()
            .setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionSetDeliveryModeTest", e);
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

  public void closedTopicSessionSetDisableMessageIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setDisableMessageID");
      try {
        tool.getDefaultTopicPublisher().setDisableMessageID(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionSetDisableMessageIDTest", e);
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

  public void closedTopicSessionSetDisableMessageTimestampTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setDisableMessageTimestamp");
      try {
        tool.getDefaultTopicPublisher().setDisableMessageTimestamp(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionSetDisableMessageTimestampTest", e);
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

  public void closedTopicSessionSetPriorityTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setPriority");
      try {
        tool.getDefaultTopicPublisher().setPriority(Message.DEFAULT_PRIORITY);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionSetPriorityTest", e);
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

  public void closedTopicSessionSetTimeToLiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setTimeToLive");
      try {
        tool.getDefaultTopicPublisher()
            .setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionSetTimeToLiveTest", e);
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

  public void closedTopicSessionPublisherGetTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getTopic");
      try {
        Topic foo = tool.getDefaultTopicPublisher().getTopic();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionPublisherGetTopicTest", e);
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

  public void closedTopicSessionPublish1Test() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call publish(Message)");
      try {
        tool.getDefaultTopicPublisher().publish(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionPublish1Test", e);
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

  public void closedTopicSessionPublish2Test() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call publish(Message,int,int,long)");
      try {
        tool.getDefaultTopicPublisher().publish(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionPublish2Test", e);
    }
  }

}
