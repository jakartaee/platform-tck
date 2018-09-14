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
package com.sun.ts.tests.jms.core.selectorQueue;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jms.common.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;
import javax.jms.*;

public class MsgSelectorQueueTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.ee.selectorQueue.MsgSelectorQueueTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS objects
  private transient Message msg = null;

  private transient QueueReceiver qReceiver = null;

  private transient TemporaryQueue tempQ = null;

  private transient QueueSender qSender = null;

  private transient QueueConnection qConnect;

  private transient QueueSession session;

  private transient QueueConnectionFactory qFactory;

  private boolean transacted = false;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  private long jms_timeout;

  private String jmsUser = null;

  private String jmsPassword = null;

  private String mode = null;

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    MsgSelectorQueueTests theTests = new MsgSelectorQueueTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /*
   * The beginning of the test creates receiver and message. This is broken
   * apart from the rest of the test so that the user can specify message
   * properties before sending the message.
   */
  private void startTest(String selector, String headerValue) throws Exception {
    logTrace("startTest(): Creating receiver with message selector");

    tempQ = session.createTemporaryQueue();
    qReceiver = session.createReceiver(tempQ, selector);

    logTrace("Creating message");
    msg = session.createMessage();
    msg.setStringProperty("COM_SUN_JMS_TESTNAME", "MsgSelectorQueueTests");
    msg.setJMSType(headerValue);
  }

  /*
   * Send the message and try to receive it. Check the result against the
   * expectation.
   */
  private void finishTestReceive() throws Exception {
    logTrace("finishTestReceive(): Sending test message");
    msg.setBooleanProperty("first_message", true);
    qSender = session.createSender(tempQ);

    qSender.send(msg);
    logTrace("Attempt to receive message");
    Message msg1 = qReceiver.receive(jms_timeout);
    logTrace("Received message: " + msg1);

    // check result
    if (msg1 == null) {
      throw new Exception("Did not receive message!");
    } else if (msg1.getBooleanProperty("first_message") == true) {
      logTrace("test passed");
    } else {
      logMsg("Received completely unexpected message.");
      throw new Exception("Received unexpected message -- not part of test");
    }
  }

  /*
   * Send the message. Used with finishTest() to send a second message and make
   * sure that the first is not received my the message consumer.
   */
  private void sendFirstMessage() throws JMSException {
    logTrace(
        "sendFirstMessage(): Sending message that does not match selector");
    msg.setBooleanProperty("second_message", false);
    // ii
    qSender = session.createSender(tempQ);
    qSender.send(msg);

    msg = session.createMessage();
    msg.setStringProperty("COM_SUN_JMS_TESTNAME", "MsgSelectorQueueTests_2");
  }

  /*
   * Send the second message which does match the selector. Receive() and verify
   * that only this second message is received.
   */
  private void finishTest() throws Exception {
    TestUtil.logTrace("time_out is " + jms_timeout);
    logTrace("finishTest: Sending message that should match selector");
    msg.setBooleanProperty("second_message", true);
    qSender = session.createSender(tempQ);
    qSender.send(msg);

    logTrace("Attempt to receive message. Should receive second message only.");
    Message msg1 = qReceiver.receive(jms_timeout);
    logTrace("Received message: " + msg1);

    // check result
    if (msg1 == null) {
      throw new Exception("Did not receive message!");
    } else if (msg1.getBooleanProperty("second_message") == true) {
      logTrace("test passed");
    } else if (msg1.getBooleanProperty("second_message") == false) {
      throw new Exception("Incorrectly received non-matching message!");
    }
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Individual tests create a temporary Queue
   * 
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   * @exception Fault
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      props = p;
      jms_timeout = Long.parseLong(props.getProperty("jms_timeout"));
      // check props for errors
      if (jms_timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
      jmsUser = props.getProperty("user");
      jmsPassword = props.getProperty("password");
      mode = p.getProperty("platform.mode");

      JmsTool tool = new JmsTool(JmsTool.QUEUE_FACTORY, jmsUser, jmsPassword,
          mode);
      qFactory = tool.getQueueConnectionFactory();
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      qConnect.start();

      session = qConnect.createQueueSession(transacted,
          Session.AUTO_ACKNOWLEDGE);
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
      logTrace(" closing connection");
      qConnect.close();
      tempQ = null;
      session.close();

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: selectorTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:49; JMS:SPEC:39;
   * 
   * @test_Strategy: create receiver with selector set msg header to include
   * string send message check receipt of message
   */
  public void selectorTest01() throws Fault {
    try {
      String selector = "JMSType='literal'";
      String value = "literal"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest02
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:49; JMS:SPEC:39;
   * 
   * @test_Strategy: create receiver with selector set msg header to include '
   * character send message check receipt of message
   */
  public void selectorTest02() throws Fault {
    try {
      String selector = "JMSType='literal''s'";
      String value = "literal's"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest03
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:63; JMS:SPEC:55; JMS:SPEC:49;
   * JMS:SPEC:40;
   * 
   * @test_Strategy: create receiver with selector set msg properties to include
   * values at ends of long range send message check receipt of message
   */
  public void selectorTest03() throws Fault {
    try {
      String selector = "myProp0=" + Long.MIN_VALUE + "L AND myProp1="
          + Long.MAX_VALUE + "L";
      String value = ""; // set for JMSType automatically
      TestUtil.logTrace(selector);

      startTest(selector, value);
      msg.setLongProperty("myProp0", Long.MIN_VALUE);
      msg.setLongProperty("myProp1", Long.MAX_VALUE);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest04
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:63; JMS:SPEC:55; JMS:SPEC:56;
   * JMS:SPEC:49; JMS:SPEC:40;
   * 
   * @test_Strategy: create receiver with selector set msg properties to include
   * numeric value send message check receipt of message
   */
  public void selectorTest04() throws Fault {
    try {
      String selector = "myProp0=-957 AND myProp1=+62";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setLongProperty("myProp0", -957);
      msg.setLongProperty("myProp1", 62);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest05
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:49; JMS:SPEC:40;
   * 
   * @test_Strategy: create receiver with selector containing octal set msg
   * properties to include numeric value send message check receipt of message
   */
  public void selectorTest05() throws Fault {
    try {
      String selector = "myProp=035"; // 29 in octal is 035
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setLongProperty("myProp", 29);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest06
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:49; JMS:SPEC:40;
   * 
   * @test_Strategy: create receiver with selector containing hexadecimal set
   * msg properties to include numeric value send message check receipt of
   * message
   */
  public void selectorTest06() throws Fault {
    try {
      String selector = "myProp=0x1d";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setLongProperty("myProp", 29); // 29 in hex is 0x1d
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest07
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:56; JMS:SPEC:49;
   * JMS:SPEC:41;
   * 
   * @test_Strategy: create receiver with selector set msg properties to include
   * numeric value send message check receipt of message
   */
  public void selectorTest07() throws Fault {
    try {
      String selector = "myProp0=7E3 AND myProp1=-57.9E3";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setDoubleProperty("myProp0", 7000.0);
      msg.setDoubleProperty("myProp1", -57900.0);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest08
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:49; JMS:SPEC:41;
   * 
   * @test_Strategy: create receiver with selector set msg properties to include
   * numeric value send message check receipt of message
   */
  public void selectorTest08() throws Fault {
    try {
      String selector = "myProp=7000.";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setDoubleProperty("myProp", 7000.0);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest09
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:49; JMS:SPEC:41;
   * 
   * @test_Strategy: create receiver with selector set msg properties to include
   * numeric value send message check receipt of message
   */
  public void selectorTest09() throws Fault {
    try {
      String selector = "myProp0=.7e4";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setDoubleProperty("myProp0", 7000.0);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest11
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:56; JMS:SPEC:49;
   * JMS:SPEC:42;
   * 
   * @test_Strategy: create receiver with selector send message with header
   * including TRUE/FALSE check receipt of message
   */
  public void selectorTest11() throws Fault {
    try {
      String selector = "myProp0=TRUE AND myProp1=FALSE";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myProp0", true);
      msg.setBooleanProperty("myProp1", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: selectorTest12
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:42;
   * 
   * @test_Strategy: create receiver with selector send message with header
   * including TRUE/FALSE check receipt of message
   */
  public void selectorTest12() throws Fault {
    try {
      String selector = "myProp0=false AND myProp1=true";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myProp0", false);
      msg.setBooleanProperty("myProp1", true);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:43;
   * 
   * @test_Strategy: create receiver with selector send message with identifiers
   * including '_' and '$' check receipt of message
   */
  public void identifierTest01() throws Fault {
    try {
      String selector = "$myProp=TRUE AND _myProp=FALSE";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("$myProp", true);
      msg.setBooleanProperty("_myProp", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest02
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:49; JMS:SPEC:44;
   * JMS:SPEC:175;
   * 
   * @test_Strategy: create receiver with selector containing 'NULL' check for
   * proper exception
   */
  public void identifierTest02() throws Fault {
    try {
      String selector = "NULL = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest03
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:44; JMS:SPEC:175;
   * 
   * @test_Strategy: create receiver with selector containing 'TRUE' check for
   * proper exception
   */
  public void identifierTest03() throws Fault {
    try {
      String selector = "TRUE = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest04
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:44; JMS:SPEC:175;
   * 
   * @test_Strategy: create receiver with selector containing 'FALSE' check for
   * proper exception
   */
  public void identifierTest04() throws Fault {
    try {
      String selector = "FALSE = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest05
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:45; JMS:SPEC:175;
   * 
   * @test_Strategy: create receiver with selector containing 'NOT' check for
   * proper exception
   */
  public void identifierTest05() throws Fault {
    try {
      String selector = "NOT = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest06
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:45;
   * 
   * @test_Strategy: create receiver with selector containing 'AND' check for
   * proper exception
   */
  public void identifierTest06() throws Fault {
    try {
      String selector = "AND = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest07
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:45;
   * 
   * @test_Strategy: create receiver with selector containing 'OR' check for
   * proper exception
   */
  public void identifierTest07() throws Fault {
    try {
      String selector = "OR = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest08
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:45;
   * 
   * @test_Strategy: create receiver with selector containing 'BETWEEN' check
   * for proper exception
   */
  public void identifierTest08() throws Fault {
    try {
      String selector = "BETWEEN = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest09
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:45;
   * 
   * @test_Strategy: create receiver with selector containing 'LIKE' check for
   * proper exception
   */
  public void identifierTest09() throws Fault {
    try {
      String selector = "LIKE = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest10
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:45;
   * 
   * @test_Strategy: create receiver with selector containing 'IN' check for
   * proper exception
   */
  public void identifierTest10() throws Fault {
    try {
      String selector = "IN = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest11
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:45;
   * 
   * @test_Strategy: create receiver with selector containing 'IS' check for
   * proper exception
   */
  public void identifierTest11() throws Fault {
    try {
      String selector = "IS = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest12
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:48;
   * 
   * @test_Strategy: create receiver with selector set two msg properties with
   * names differing in case send message check receipt of message
   */
  public void identifierTest12() throws Fault {
    try {
      String selector = "myProp=TRUE AND MYpROP=FALSE";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myProp", true);
      msg.setBooleanProperty("MYpROP", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest13
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:46;
   * 
   * @test_Strategy: create receiver with selector send message check that
   * message is not received
   */
  public void identifierTest13() throws Fault {
    try {
      String selector = "myProp ='foo'";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setStringProperty("myProp", "foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest14
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:45;
   * 
   * @test_Strategy: create receiver with selector containing 'ESCAPE' check for
   * proper exception
   */
  public void identifierTest14() throws Fault {
    try {
      String selector = "ESCAPE = 0";
      String value = "";

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: whitespaceTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:53;
   * 
   * @test_Strategy: create receiver with selector containing extra spaces send
   * message with header set check receipt of message
   */
  public void whitespaceTest1() throws Fault {
    try {
      String selector = "JMSType   =   'foo'";
      String value = "foo"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: whitespaceTest2
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:53;
   * 
   * @test_Strategy: create receiver with selector containing tabs send message
   * with header set check receipt of message
   */
  public void whitespaceTest2() throws Fault {
    try {
      String selector = "JMSType\t=\t'foo'";
      String value = "foo"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: whitespaceTest3
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:53;
   * 
   * @test_Strategy: create receiver with selector containing form feeds send
   * message with header set check receipt of message
   */
  public void whitespaceTest3() throws Fault {
    try {
      String selector = "JMSType\f=\f'foo'";
      String value = "foo"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: whitespaceTest4
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:53;
   * 
   * @test_Strategy: create receiver with selector containing line terminators
   * send message with header set check receipt of message
   */
  public void whitespaceTest4() throws Fault {
    try {
      String selector = "JMSType\n= \n'foo'";
      String value = "foo"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: expressionTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:54;
   * 
   * @test_Strategy: create receiver with selector send message with TRUE
   * boolean property check receipt of message
   */
  public void expressionTest1() throws Fault {
    try {
      String selector = "myProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myProp", true);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: expressionTest2
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:54;
   * 
   * @test_Strategy: create receiver with selector send message with FALSE
   * boolean property check that message is not received
   */
  public void expressionTest2() throws Fault {
    try {
      String selector = "myProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myProp", false);
      sendFirstMessage();
      msg.setBooleanProperty("myProp", true);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: expressionTest3
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:54;
   * 
   * @test_Strategy: create receiver with selector referenceing null property
   * send message check that message is not received
   */
  public void expressionTest3() throws Fault {
    try {
      String selector = "myNullProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setBooleanProperty("myNullProp", true);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: bracketingTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:57;
   * 
   * @test_Strategy: create receiver with selector that should evaluate to FALSE
   * send message check that message is not received
   */
  public void bracketingTest1() throws Fault {
    try {
      String selector = "(myTrueProp OR myFalseProp) AND myFalseProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", false);
      sendFirstMessage();
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", true);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: bracketingTest2
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:57;
   * 
   * @test_Strategy: create receiver with selector that should evaluate to TRUE
   * send message check receipt of message
   */
  public void bracketingTest2() throws Fault {
    try {
      String selector = "TRUE OR (FALSE AND FALSE)";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: bracketingTest3
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:57;
   * 
   * @test_Strategy: create receiver with selector that should evaluate to FALSE
   * send message check that message is not received
   */
  public void bracketingTest3() throws Fault {
    try {
      String selector = "(myProp0 = true OR myProp1 = true) AND JMSType = 'not_foo'";
      String value = "foo"; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myProp0", true);
      msg.setBooleanProperty("myProp1", false);
      sendFirstMessage();
      msg.setJMSType("not_foo");
      msg.setBooleanProperty("myProp0", true);
      msg.setBooleanProperty("myProp1", false);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: bracketingTest4
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:57;
   * 
   * @test_Strategy: create receiver with selector that should evaluate to TRUE
   * send message check receipt of message
   */
  public void bracketingTest4() throws Fault {
    try {
      String selector = "(myProp1 = true AND JMSType = 'not_foo') OR myProp0 = true";
      String value = "foo"; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myProp0", true);
      msg.setBooleanProperty("myProp1", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:59;
   * 
   * @test_Strategy: create receiver with selector containing operators send
   * message with properties matching selector check receipt of message
   */
  public void comparisonTest01() throws Fault {
    try {
      String selector = "myProp0 = 'foo' AND " + "myProp1 > 0 AND "
          + "myProp2 >= 2 AND " + "myProp3 >= 2 AND " + "myProp4 < 5 AND "
          + "myProp5 <= 6 AND " + "myProp6 <= 6 AND " + "myProp7 <> 7";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "foo");
      msg.setIntProperty("myProp1", 1);
      msg.setFloatProperty("myProp2", 2.0f);
      msg.setIntProperty("myProp3", 3);
      msg.setDoubleProperty("myProp4", 4.0);
      msg.setIntProperty("myProp5", 5);
      msg.setIntProperty("myProp6", 6);
      msg.setFloatProperty("myProp7", 0f);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest02
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:59;
   * 
   * @test_Strategy: create receiver with selector containing operators send
   * message with properties not matching selector check that message is not
   * received
   */
  public void comparisonTest02() throws Fault {
    try {
      String selector = "myProp0 = 'foo' OR " + "myProp1 > 0 OR "
          + "myProp2 >= 2 OR " + "myProp3 < 5 OR " + "myProp4 <= 6 OR "
          + "myProp5 <> 7";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "not_foo");
      msg.setIntProperty("myProp1", 0);
      msg.setFloatProperty("myProp2", 1.5f);
      msg.setDoubleProperty("myProp3", 5.0);
      msg.setIntProperty("myProp4", 7);
      msg.setFloatProperty("myProp5", 7f);
      sendFirstMessage();
      msg.setStringProperty("myProp0", "foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest03
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:60;
   * 
   * @test_Strategy: create receiver with selector send message with exact and
   * approximate numeric values check receipt of message
   */
  public void comparisonTest03() throws Fault {
    try {
      String selector = "myProp0 < myProp1";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", 0);
      msg.setDoubleProperty("myProp1", 1.5);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest04
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with selector containing > and string
   * should throw InvalidSelectorException
   */
  public void comparisonTest04() throws Fault {
    try {
      String selector = "myProp > 'foo'";
      String value = "";

      startTest(selector, value);

      // did not get exception
      throw new Exception(
          "Did not receive InvalidSelectorException " + "as expected.");
    } catch (InvalidSelectorException ise) {
      logTrace("Test passed. Received expected exception.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest05
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with selector containing >= and string
   * should throw InvalidSelectorException
   */
  public void comparisonTest05() throws Fault {
    try {
      String selector = "myProp >= 'foo'";
      String value = "";

      startTest(selector, value);

      // did not get exception
      throw new Exception(
          "Did not receive InvalidSelectorException " + "as expected.");
    } catch (InvalidSelectorException ise) {
      logTrace("Test passed. Received expected exception.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest06
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with selector containing < and string
   * should throw InvalidSelectorException
   */
  public void comparisonTest06() throws Fault {
    try {
      String selector = "myProp < 'foo'";
      String value = "";

      startTest(selector, value);

      // did not get exception
      throw new Exception(
          "Did not receive InvalidSelectorException " + "as expected.");
    } catch (InvalidSelectorException ise) {
      logTrace("Test passed. Received expected exception.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest07
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with selector containing <= and string
   * should throw InvalidSelectorException
   */
  public void comparisonTest07() throws Fault {
    try {
      String selector = "myProp <= 'foo'";
      String value = "";

      startTest(selector, value);

      // did not get exception
      throw new Exception(
          "Did not receive InvalidSelectorException " + "as expected.");
    } catch (InvalidSelectorException ise) {
      logTrace("Test passed. Received expected exception.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest08
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with valid selector send matching message
   * check for receipt of message
   */
  public void comparisonTest08() throws Fault {
    try {
      String selector = "JMSType = 'foo' AND myProp <> 'not_foo'";
      String value = "foo";

      startTest(selector, value);
      msg.setStringProperty("myProp", "foo");
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest09
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with selector comparing TRUE with > should
   * throw InvalidSelectorException
   */
  public void comparisonTest09() throws Fault {
    try {
      String selector = "'foo' > TRUE ";
      String value = "";

      startTest(selector, value);

      // did not get exception
      throw new Exception(
          "Did not receive InvalidSelectorException " + "as expected.");
    } catch (InvalidSelectorException ise) {
      logTrace("Test passed. Received expected exception.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest10
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with selector comparing TRUE with >= should
   * throw InvalidSelectorException
   */
  public void comparisonTest10() throws Fault {
    try {
      String selector = "'foo' >= TRUE ";
      String value = "";

      startTest(selector, value);

      // did not get exception
      throw new Exception(
          "Did not receive InvalidSelectorException " + "as expected.");
    } catch (InvalidSelectorException ise) {
      logTrace("Test passed. Received expected exception.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest11
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with selector comparing TRUE with < should
   * throw InvalidSelectorException
   */
  public void comparisonTest11() throws Fault {
    try {
      String selector = "'foo' < TRUE ";
      String value = "";

      startTest(selector, value);

      // did not get exception
      throw new Exception(
          "Did not receive InvalidSelectorException " + "as expected.");
    } catch (InvalidSelectorException ise) {
      logTrace("Test passed. Received expected exception.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest12
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with selector comparing TRUE with <= should
   * throw InvalidSelectorException
   */
  public void comparisonTest12() throws Fault {
    try {
      String selector = "'foo' <= TRUE ";
      String value = "";

      startTest(selector, value);

      // did not get exception
      throw new Exception(
          "Did not receive InvalidSelectorException " + "as expected.");
    } catch (InvalidSelectorException ise) {
      logTrace("Test passed. Received expected exception.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest13
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:61;
   * 
   * @test_Strategy: create receiver with valid selector send matching message
   * check for receipt of message
   */
  public void comparisonTest13() throws Fault {
    try {
      String selector = "myProp0 = TRUE AND myProp1 <> FALSE";
      String value = "";

      startTest(selector, value);
      msg.setBooleanProperty("myProp0", true);
      msg.setBooleanProperty("myProp1", true);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: operatorTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:62;
   * 
   * @test_Strategy: create receiver with selector containing +,- send message
   * with numeric properties check receipt of message
   */
  public void operatorTest1() throws Fault {
    try {
      String selector = "-myProp0 < 0 AND +myProp1 < 0";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", 5);
      msg.setIntProperty("myProp1", -5);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: operatorTest2
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:62;
   * 
   * @test_Strategy: create receiver with selector containing *,/ send message
   * with numeric properties check receipt of message
   */
  public void operatorTest2() throws Fault {
    try {
      String selector = "myProp0*2 = 10 AND myProp1/2 = -5";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", 5);
      msg.setIntProperty("myProp1", -10);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: operatorTest3
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:62;
   * 
   * @test_Strategy: create receiver with selector containing +,- send message
   * with numeric properties check receipt of message
   */
  public void operatorTest3() throws Fault {
    try {
      String selector = "myProp0+5 = 0 AND myProp1-5 = 5";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", -5);
      msg.setIntProperty("myProp1", 10);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: betweenTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:64;
   * 
   * @test_Strategy: create receiver with selector containing BETWEEN send
   * message matching selector check receipt of message
   */
  public void betweenTest1() throws Fault {
    try {
      String selector = "myProp0 BETWEEN 5 and 10 AND "
          + "myProp1 BETWEEN -1 and 1 AND " + "myProp2 BETWEEN 0 and 2";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", 7);
      msg.setIntProperty("myProp1", -1);
      msg.setIntProperty("myProp2", 2);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: betweenTest2
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:64;
   * 
   * @test_Strategy: create receiver with selector containing BETWEEN send
   * message not matching selector check that message is not received
   */
  public void betweenTest2() throws Fault {
    try {
      String selector = "myProp0 BETWEEN  -4 and 5 OR "
          + "myProp1 BETWEEN -5 and 4";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", -5);
      msg.setIntProperty("myProp1", 5);
      sendFirstMessage();
      msg.setIntProperty("myProp0", 0);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: betweenTest3
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:64;
   * 
   * @test_Strategy: create receiver with selector containing NOT BETWEEN send
   * message matching selector check receipt of message
   */
  public void betweenTest3() throws Fault {
    try {
      String selector = "myProp0 NOT BETWEEN  -4 and 5 AND "
          + "myProp1 NOT BETWEEN -5 and 4";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", -5);
      msg.setIntProperty("myProp1", 5);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: betweenTest4
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:64;
   * 
   * @test_Strategy: create receiver with selector containing NOT BETWEEN send
   * message not matching selector check that message is not received
   */
  public void betweenTest4() throws Fault {
    try {
      String selector = "myProp0 NOT BETWEEN 5 and 10 OR "
          + "myProp1 NOT BETWEEN -1 and 1 OR " + "myProp2 NOT BETWEEN 0 and 2";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", 7);
      msg.setIntProperty("myProp1", -1);
      msg.setIntProperty("myProp2", 2);
      sendFirstMessage();
      msg.setIntProperty("myProp0", 0);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: betweenTest5
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:64;
   * 
   * @test_Strategy: create receiver with selector containing BETWEEN send
   * message with property that is not arithmetic check that message is not
   * received
   */
  public void betweenTest5() throws Fault {
    try {
      String selector = "myProp BETWEEN 5 and 10";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);

      // not a number. should not be received
      msg.setStringProperty("myProp", "7");
      sendFirstMessage();
      msg.setIntProperty("myProp", 7);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: betweenTest6
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:64;
   * 
   * @test_Strategy: create receiver with selector containing BETWEEN send
   * message with property that is not arithmetic check that message is not
   * received
   */
  public void betweenTest6() throws Fault {
    try {
      String selector = "myProp BETWEEN 5 and 10";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);

      // not number, so message shouldn't be received
      msg.setStringProperty("myProp", "seven");
      sendFirstMessage();
      msg.setIntProperty("myProp", 7);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: betweenTest7
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:64;
   * 
   * @test_Strategy: create receiver with syntactically incorrect selector check
   * for proper excepion
   */
  public void betweenTest7() throws Fault {
    try {
      String selector = "myProp BETWEEN 'foo' and 'test'";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with selector containing IN send message
   * matching selector check receipt of message
   */
  public void inTest1() throws Fault {
    try {
      String selector = "JMSType IN ('foo','jms','test')";
      String value = "jms"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest2
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with selector containing IN send message
   * not matching selector check that message is not received
   */
  public void inTest2() throws Fault {
    try {
      String selector = "JMSType IN ('foo','jms','test')";
      String value = "JMS"; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setJMSType("jms");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest3
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with selector containing NOT IT send
   * message matching selector check receipt of message
   */
  public void inTest3() throws Fault {
    try {
      String selector = "JMSType NOT IN ('foo','jms','test')";
      String value = "FOO"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest4
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with selector containing NOT IN send
   * message not matching selector check that message is not received
   */
  public void inTest4() throws Fault {
    try {
      String selector = "JMSType NOT IN ('foo','jms','test')";
      String value = "foo"; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setJMSType("FOO");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest5
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with selector containing IN msg selector
   * references an unknown property send message check that message is not
   * received
   */
  public void inTest5() throws Fault {
    try {
      String selector = "myNullProp IN ('foo','jms','test')";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setStringProperty("myNullProp", "jms");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest6
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with selector containing NOT IN msg
   * selector references an unknown property send message check that message is
   * not received
   */
  public void inTest6() throws Fault {
    try {
      String selector = "myNullProp NOT IN ('foo','jms','test')";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setStringProperty("myNullProp", "JMS");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest7
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with selector containing NOT IN set msg
   * property of integer type send message check that message is not received
   */
  public void inTest7() throws Fault {
    try {
      String selector = "myProp NOT IN ('foo','jms','test')";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);

      // property is not a string
      msg.setIntProperty("myProp", 0);
      sendFirstMessage();
      msg.setStringProperty("myProp", "zero");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest8
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with syntactically incorrect selector check
   * for proper exception
   */
  public void inTest8() throws Fault {
    try {
      String selector = "myProp NOT IN (1,2,3)";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector containing LIKE send
   * message matching selector check receipt of message
   */
  public void likeTest01() throws Fault {
    try {
      String selector = "JMSType LIKE 'jms'";
      String value = "jms"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest02
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains
   * LIKE statement using '_' send message matching selector check receipt of
   * message
   */
  public void likeTest02() throws Fault {
    try {
      String selector = "JMSType LIKE 's_lector' AND "
          + "myProp0 LIKE '_bcd' AND " + "myProp1 LIKE '123_'";
      String value = "selector"; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "abcd");
      msg.setStringProperty("myProp1", "1234");
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest03
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains
   * LIKE statement using '%' send message matching selector check receipt of
   * message
   */
  public void likeTest03() throws Fault {
    try {
      String selector = "JMSType LIKE 's%tor' AND " + "myProp0 LIKE '%cd' AND "
          + "myProp1 LIKE '1%' AND " + "myProp2 LIKE 'AB%CD' AND "
          + "myProp3 LIKE '%'";
      String value = "selector"; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "abcd");
      msg.setStringProperty("myProp1", "1234");
      msg.setStringProperty("myProp2", "ABCD");
      msg.setStringProperty("myProp3", "foo");
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest04
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains
   * LIKE statement using ESCAPE character send message matching selector check
   * receipt of message
   */
  public void likeTest04() throws Fault {
    try {
      String selector = "JMSType LIKE 'A_fo_A%f%' " + "ESCAPE 'A'";
      String value = "_foo%foo"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest05
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector containing LIKE send
   * message not matching selector check that message is not received
   */
  public void likeTest05() throws Fault {
    try {
      String selector = "JMSType LIKE '123' OR " + "myProp LIKE 'jms0'";
      String value = "1234"; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp", "jms");
      sendFirstMessage();
      msg.setStringProperty("myProp", "jms0");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest06
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains
   * LIKE statement using '_' send message not matching selector check that
   * message is not received
   */
  public void likeTest06() throws Fault {
    try {
      String selector = "JMSType LIKE 'se_ector' OR " + "myProp0 LIKE '_cd' OR "
          + "myProp1 LIKE '12_'";
      String value = "seleector"; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "abcd");
      msg.setStringProperty("myProp1", "1234");
      sendFirstMessage();
      msg.setJMSType("selector");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest07
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains
   * LIKE statement using '%' send message not matching selector check that
   * message is not received
   */
  public void likeTest07() throws Fault {
    try {
      String selector = "JMSType LIKE '12%3'";
      String value = "1234";

      startTest(selector, value);
      sendFirstMessage();
      msg.setJMSType("12333");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest08
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains
   * LIKE statement using ESCAPE character send message not matching selector
   * check that message is not received
   */
  public void likeTest08() throws Fault {
    try {
      String selector = "JMSType LIKE 'A%fooA_' " + "ESCAPE 'A'";
      String value = "_foo%"; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setJMSType("%foo_");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest09
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector containing NOT LIKE
   * send message matching selector check receipt of message
   */
  public void likeTest09() throws Fault {
    try {
      String selector = "JMSType NOT LIKE 'jms_foo'";
      String value = "jms"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest10
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains NOT
   * LIKE statement using '_' send message matching selector check receipt of
   * message
   */
  public void likeTest10() throws Fault {
    try {
      String selector = "JMSType NOT LIKE 's_ector' AND "
          + "myProp0 NOT LIKE '_cd' AND " + "myProp1 NOT LIKE '12_'";
      String value = "selector"; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "abcd");
      msg.setStringProperty("myProp1", "1234");
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest11
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains NOT
   * LIKE statement using '%' send message matching selector check receipt of
   * message
   */
  public void likeTest11() throws Fault {
    try {
      String selector = "myProp0 NOT LIKE '%cde' AND "
          + "myProp1 NOT LIKE '01%' AND " + "myProp2 NOT LIKE 'A%x%D'";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "abcd");
      msg.setStringProperty("myProp1", "1234");
      msg.setStringProperty("myProp2", "ABCD");
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest12
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains NOT
   * LIKE statement using ESCAPE character send message matching selector check
   * receipt of message
   */
  public void likeTest12() throws Fault {
    try {
      String selector = "JMSType NOT LIKE 'A_A_' " + "ESCAPE 'A'";
      String value = "_A"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest13
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector containing NOT LIKE
   * send message not matching selector check that message is not received
   */
  public void likeTest13() throws Fault {
    try {
      String selector = "JMSType NOT LIKE '1234' OR " + "myProp NOT LIKE 'jms'";
      String value = "1234"; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp", "jms");
      sendFirstMessage();
      msg.setJMSType("foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest14
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains NOT
   * LIKE statement using '_' send message not matching selector check that
   * message is not received
   */
  public void likeTest14() throws Fault {
    try {
      String selector = "JMSType NOT LIKE 'se_ector' OR "
          + "myProp0 NOT LIKE '_bcd' OR " + "myProp1 NOT LIKE '123_'";
      String value = "selector"; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "abcd");
      msg.setStringProperty("myProp1", "1234");
      sendFirstMessage();
      msg.setJMSType("foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest15
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains NOT
   * LIKE statement using '%' send message not matching selector check that
   * message is not received
   */
  public void likeTest15() throws Fault {
    try {
      String selector = "JMSType NOT LIKE '12%'";
      String value = "1234";

      startTest(selector, value);
      sendFirstMessage();
      msg.setJMSType("foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest16
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains NOT
   * LIKE statement using ESCAPE character send message not matching selector
   * check that message is not received
   */
  public void likeTest16() throws Fault {
    try {
      String selector = "JMSType NOT LIKE 'A_fooA%' " + "ESCAPE 'A'";
      String value = "_foo%"; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setJMSType("jms");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest17
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector selector contains NOT
   * LIKE statement using ESCAPE character '\' send message not matching
   * selector check receipt of message
   */
  public void likeTest17() throws Fault {
    try {
      String selector = "JMSType LIKE '\\__\\%%' ESCAPE '\\'";
      String value = "_a%b"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest18
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:60; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector using LIKE selector
   * references unknown property send message check that message is not received
   */
  public void likeTest18() throws Fault {
    try {
      String selector = "myNullProp LIKE '1_3'";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setStringProperty("myNullProp", "123");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest19
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:60; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector using NOT LIKE
   * selector references unknown property send message check that message is not
   * received
   */
  public void likeTest19() throws Fault {
    try {
      String selector = "myNullProp NOT LIKE '1_3'";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setStringProperty("myNullProp", "foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest20
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector using LIKE send
   * message with non-string property check that message is not received
   */
  public void likeTest20() throws Fault {
    try {
      String selector = "myProp LIKE '1_3'";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);

      // not a string
      msg.setIntProperty("myProp", 123);
      sendFirstMessage();
      msg.setStringProperty("myProp", "123");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest21
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with invalid selector check for proper
   * exception
   */
  public void likeTest21() throws Fault {
    try {
      String selector = "myProp LIKE 7";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);

      // should have received exception
      throw new Exception("Did not receive InvalidSelectorException");
    } catch (InvalidSelectorException ise) {
      logTrace("Caught expected exception");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: isNullTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:67;
   * 
   * @test_Strategy: create receiver with message selector containing IS NULL
   * selector references unknown property send message check receipt of message
   */
  public void isNullTest1() throws Fault {
    try {
      String selector = "myNullProp IS NULL";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: isNullTest2
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:67;
   * 
   * @test_Strategy: create receiver with message selector containing IS NULL
   * selector references existing property send message check that message is
   * not received
   */
  public void isNullTest2() throws Fault {
    try {
      String selector = "myProp IS NULL";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp", "foo");
      sendFirstMessage();
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: isNullTest3
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:68;
   * 
   * @test_Strategy: create receiver with message selector containing IS NOT
   * NULL selector references existing property send message check receipt of
   * message
   */
  public void isNullTest3() throws Fault {
    try {
      String selector = "myProp IS NOT NULL";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setFloatProperty("myProp", 10f);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: isNullTest4
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:68;
   * 
   * @test_Strategy: create receiver with message selector containing IS NOT
   * NULL selector references unknown property send message check that message
   * is not received
   */
  public void isNullTest4() throws Fault {
    try {
      String selector = "myNullProp IS NOT NULL";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setStringProperty("myNullProp", "foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: caseTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:247;
   * 
   * @test_Strategy: create receiver with message selector selector contains
   * lower case versions of selector operators send message matching selector
   * check receipt of message
   */
  public void caseTest1() throws Fault {
    try {
      String selector = "myProp0 is null and "
          + "myProp1 like 'fooG_%' escape 'G' and "
          + "myProp2 in ('a', 'b') and " + "myProp3 not between 0 and 10 and "
          + "(myProp4 or false)";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp1", "foo_test");
      msg.setStringProperty("myProp2", "a");
      msg.setIntProperty("myProp3", 20);
      msg.setBooleanProperty("myProp4", true);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: precedenceTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:58;
   * 
   * @test_Strategy: create receiver with message selector containing AND and OR
   * send message not matching selector check that message is not received !F&F
   * = (!F)&F = T&F = F incorrect order would be !F&F -> !(F&F) = !F = T
   */
  public void precedenceTest1() throws Fault {
    try {
      String selector = "NOT myTrueProp AND myFalseProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", false);
      sendFirstMessage();
      msg.setBooleanProperty("myTrueProp", false);
      msg.setBooleanProperty("myFalseProp", true);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: precedenceTest2
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:58;
   * 
   * @test_Strategy: create receiver with message selector containing AND and
   * NOT send message matching selector check receipt of message
   */
  public void precedenceTest2() throws Fault {
    try {
      String selector = "myTrueProp AND NOT myFalseProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: precedenceTest3
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:58;
   * 
   * @test_Strategy: create receiver with message selector containing AND and OR
   * send message matching selector check receipt of message TvT&F = Tv(T&F) =
   * TvF = T incorrect order would be TvT&F -> (TvT)&F = T&F = F
   */
  public void precedenceTest3() throws Fault {
    try {
      String selector = "myTrueProp OR myTrueProp AND myFalseProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: precedenceTest4
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:62;
   * 
   * @test_Strategy: create receiver with message selector send message check
   * receipt of message
   */
  public void precedenceTest4() throws Fault {
    try {
      String selector = "- myProp0 + myProp1 = 0";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", 5);
      msg.setIntProperty("myProp1", 5);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: precedenceTest5
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:62;
   * 
   * @test_Strategy: create receiver with message selector send message matching
   * selector check receipt of message
   */
  public void precedenceTest5() throws Fault {
    try {
      String selector = "myProp0+myProp1*myProp2-myProp3/myProp4 = 6";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setFloatProperty("myProp0", 1f);
      msg.setFloatProperty("myProp1", 2f);
      msg.setFloatProperty("myProp2", 3f);
      msg.setFloatProperty("myProp3", 4f);
      msg.setFloatProperty("myProp4", 4f);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:60;
   * 
   * @test_Strategy: create receiver with selector referencing unknown property
   * send message check that message is not received
   */
  public void nullTest01() throws Fault {
    try {
      String selector = "myProp + 2 < 10";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setIntProperty("myProp", 0);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest02
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:60;
   * 
   * @test_Strategy: create receiver with selector referencing unknown property
   * send message check that message is not received
   */
  public void nullTest02() throws Fault {
    try {
      String selector = "myProp = 'foo'";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setStringProperty("myProp", "foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest03
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:60;
   * 
   * @test_Strategy: create receiver with selector referencing unknown property
   * send message check that message is not received
   */
  public void nullTest03() throws Fault {
    try {
      String selector = "myProp NOT IN ('a', 'b')";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setStringProperty("myProp", "foo");
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest04
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:248.1;
   * 
   * @test_Strategy: create receiver with selector containing AND send message
   * matching selector check receipt of message
   */
  public void nullTest04() throws Fault {
    try {
      String selector = "myTrueProp AND myTrueProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest05
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:248.2; JMS:SPEC:248.3;
   * JMS:SPEC:248.4; JMS:SPEC:248.5; JMS:SPEC:248.6; JMS:SPEC:248.7;
   * JMS:SPEC:248.8; JMS:SPEC:248.9;
   * 
   * @test_Strategy: create receiver with selector containing AND each
   * comparison should evaluate to FALSE send message check that message is not
   * received
   */
  public void nullTest05() throws Fault {
    try {
      String selector = "(myTrueProp AND myFalseProp) OR "
          + "(myTrueProp AND myNullProp) OR "
          + "(myFalseProp AND myTrueProp) OR "
          + "(myFalseProp AND myFalseProp) OR "
          + "(myFalseProp AND myNullProp) OR "
          + "(MyNullProp AND myTrueProp) OR "
          + "(MyNullProp AND myFalseProp) OR " + "(MyNullProp AND myNullProp)";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", false);
      sendFirstMessage();
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", true);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest06
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:249.1; JMS:SPEC:249.2;
   * JMS:SPEC:249.3; JMS:SPEC:249.4; JMS:SPEC:249.7;
   * 
   * @test_Strategy: create receiver with selector containing OR each comparison
   * should evaluate to TRUE send message check receipt of message
   */
  public void nullTest06() throws Fault {
    try {
      String selector = "(myTrueProp OR myTrueProp) AND "
          + "(myTrueProp OR myFalseProp) AND "
          + "(myTrueProp OR myNullProp) AND "
          + "(myFalseProp OR myTrueProp) AND " + "(myNullProp OR myTrueProp)";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest07
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:249.5; JMS:SPEC:249.6;
   * JMS:SPEC:249.8; JMS:SPEC:249.9;
   * 
   * @test_Strategy: create receiver with selector containing OR each comparison
   * should evaluate to FALSE send message check that message is not received
   */
  public void nullTest07() throws Fault {
    try {
      String selector = "(myFalseProp OR myFalseProp) OR "
          + "(myFalseProp OR myNullProp) OR "
          + "(myNullProp OR myFalseProp) OR " + "(myNullProp OR myNullProp)";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myFalseProp", false);
      sendFirstMessage();
      msg.setBooleanProperty("myFalseProp", true);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest08
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:250.2;
   * 
   * @test_Strategy: create receiver with selector containing NOT send message
   * matching selector check receipt of message
   */
  public void nullTest08() throws Fault {
    try {
      String selector = "NOT myFalseProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myFalseProp", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest09
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:250.1;
   * 
   * @test_Strategy: create receiver with selector containing NOT send message
   * not matching selector check that message is not received
   */
  public void nullTest09() throws Fault {
    try {
      String selector = "NOT myTrueProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      sendFirstMessage();
      msg.setBooleanProperty("myTrueProp", false);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest10
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:250.3;
   * 
   * @test_Strategy: create receiver with selector containing NOT selector
   * references an unknown property send message check receipt of message
   */
  public void nullTest10() throws Fault {
    try {
      String selector = "NOT myNullProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setBooleanProperty("myNullProp", false);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest11
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:37;
   * 
   * @test_Strategy: create receiver with null selector. check receipt of
   * message
   */
  public void nullTest11() throws Fault {
    try {
      String selector = null;
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myNullProp", false);
      finishTest();
    } catch (Exception e) {
      throw new Fault("test failed", e);
    }
  }
}
