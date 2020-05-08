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
package com.sun.ts.tests.jms.core.foreignMsgTopic;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import jakarta.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class ForeignMsgTopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.foreignMsgTopic.ForeignMsgTopicTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS objects
  private transient JmsTool tool = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  ArrayList connections = null;

  // values to pass into/read from messages
  boolean testBoolean = true;

  byte testByte = 100;

  char testChar = 'a';

  int testInt = 10;

  Object testObject = new Double(3.141);

  String testString = "java";

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    ForeignMsgTopicTests theTests = new ForeignMsgTopicTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default Queue and/or
   * Topic Connection, as well as a default Queue and Topic. Tests that require
   * multiple Destinations create the extras within the test
   * 
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

      // check props for errors
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must be null");
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
   * @testName: sendReceiveBytesMsgTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
   * 
   * @test_Strategy: Send message with appropriate data Receive message and
   * check data
   */

  public void sendReceiveBytesMsgTopicTest() throws Fault {
    boolean pass = true;

    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = new BytesMessageTestImpl();
      logTrace("Setting test values in message");
      long bodyLength = 22L;
      BytesMessageTestImpl messageSentImpl = (BytesMessageTestImpl) messageSent;
      messageSentImpl.setBodyLength(bodyLength);
      messageSent.writeBoolean(testBoolean);
      messageSent.writeByte(testByte);
      messageSent.writeChar(testChar);
      messageSent.writeInt(testInt);
      messageSent.writeObject(testObject);
      messageSent.writeUTF(testString);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendReceiveBytesMsgTopicTest");
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("Did not receive message");
      }
      logTrace("Check received message");
      if (messageReceived.readBoolean() == testBoolean) {
        logTrace("Received correct boolean value");
      } else {
        logMsg("incorrect boolean value -- BAD");
        pass = false;
      }
      if (messageReceived.readByte() == testByte) {
        logTrace("Received correct byte value");
      } else {
        logMsg("incorrect byte value -- BAD");
        pass = false;
      }
      if (messageReceived.readChar() == testChar) {
        logTrace("Received correct char value");
      } else {
        logMsg("incorrect char value -- BAD");
        pass = false;
      }
      if (messageReceived.readInt() == testInt) {
        logTrace("Received correct int value");
      } else {
        logMsg("incorrect int value -- BAD");
        pass = false;
      }
      if (messageReceived.readDouble() == ((Double) testObject).doubleValue()) {
        logTrace("Received correct object");
      } else {
        logMsg("incorrect object -- BAD");
        pass = false;
      }
      if (messageReceived.readUTF().equals(testString)) {
        logTrace("Received correct String");
      } else {
        logMsg("incorrect string -- BAD");
        pass = false;
      }
      if (pass == false) {
        logMsg("Test failed -- see above");
        throw new Exception();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendReceiveBytesMsgTopicTest");
    }
  }

  /*
   * @testName: sendReceiveMsgTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
   * 
   * @test_Strategy: Send message with appropriate data Receive message and
   * check data
   */

  public void sendReceiveMsgTopicTest() throws Fault {
    try {
      Message messageSent = null;
      Message messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logMsg("Creating 1 message");
      messageSent = new MessageTestImpl();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendReceiveMsgTopicTest");
      logMsg("sending: " + messageSent);
      logMsg("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logMsg("Receiving message");
      messageReceived = tool.getDefaultTopicSubscriber().receive(timeout);
      logMsg("received: " + messageReceived);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendReceiveMsgTopicTest");
    }
  }

  /*
   * @testName: sendReceiveMapMsgTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
   * 
   * @test_Strategy: Send message with appropriate data Receive message and
   * check data
   */

  public void sendReceiveMapMsgTopicTest() throws Fault {
    boolean pass = true;

    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = new MapMessageTestImpl();
      logTrace("Setting test values in message");
      messageSent.setBoolean("TestBoolean", testBoolean);
      messageSent.setByte("TestByte", testByte);
      messageSent.setChar("TestChar", testChar);
      messageSent.setInt("TestInt", testInt);
      messageSent.setObject("TestDouble", testObject);
      messageSent.setString("TestString", testString);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendReceiveMapMsgTopicTest");
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("Did not receive message");
      }
      logTrace("Check received message");
      if (messageReceived.getBoolean("TestBoolean") == testBoolean) {
        logTrace("Received correct boolean value");
      } else {
        logMsg("incorrect boolean value -- BAD");
        pass = false;
      }
      if (messageReceived.getByte("TestByte") == testByte) {
        logTrace("Received correct byte value");
      } else {
        logMsg("incorrect byte value -- BAD");
        pass = false;
      }
      if (messageReceived.getChar("TestChar") == testChar) {
        logTrace("Received correct char value");
      } else {
        logMsg("incorrect char value -- BAD");
        pass = false;
      }
      if (messageReceived.getInt("TestInt") == testInt) {
        logTrace("Received correct int value");
      } else {
        logMsg("incorrect int value -- BAD");
        pass = false;
      }
      if (messageReceived.getDouble("TestDouble") == ((Double) testObject)
          .doubleValue()) {
        logTrace("Received correct object");
      } else {
        logMsg("incorrect object -- BAD");
        pass = false;
      }
      if (messageReceived.getString("TestString").equals(testString)) {
        logTrace("Received correct String");
      } else {
        logMsg("incorrect string -- BAD");
        pass = false;
      }
      if (pass == false) {
        logMsg("Test failed -- see above");
        throw new Exception();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendReceiveMapMsgTopicTest");
    }
  }

  /*
   * @testName: sendReceiveObjectMsgTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
   * 
   * @test_Strategy: Send message with appropriate data Receive message and
   * check data
   */

  public void sendReceiveObjectMsgTopicTest() throws Fault {
    try {
      ObjectMessage messageSent = null;
      ObjectMessage messageReceived = null;
      String text = "test";

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logMsg("Creating 1 message");
      messageSent = new ObjectMessageTestImpl();
      messageSent.setObject(text);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendReceiveObjectMsgTopicTest");
      logMsg("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logMsg("Receiving message");
      messageReceived = (ObjectMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("Did not receive message");
      }
      if (((String) messageReceived.getObject()).equals(text)) {
        logMsg("Received correct object");
      } else {
        throw new Exception("Did not receive correct message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendReceiveObjectMsgTopicTest");
    }
  }

  /*
   * @testName: sendReceiveStreamMsgTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
   * 
   * @test_Strategy: Send message with appropriate data Receive message and
   * check data
   */

  public void sendReceiveStreamMsgTopicTest() throws Fault {
    boolean pass = true;

    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logMsg("Creating 1 message");
      messageSent = new StreamMessageTestImpl();
      logTrace("Setting test values in message");
      messageSent.writeBoolean(testBoolean);
      messageSent.writeByte(testByte);
      messageSent.writeChar(testChar);
      messageSent.writeInt(testInt);
      messageSent.writeObject(testObject);
      messageSent.writeString(testString);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendReceiveStreamMsgTopicTest");
      logMsg("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logMsg("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("Did not receive message");
      }
      logTrace("Check received message");
      if (messageReceived.readBoolean() == testBoolean) {
        logTrace("Received correct boolean value");
      } else {
        logMsg("incorrect boolean value -- BAD");
        pass = false;
      }
      if (messageReceived.readByte() == testByte) {
        logTrace("Received correct byte value");
      } else {
        logMsg("incorrect byte value -- BAD");
        pass = false;
      }
      if (messageReceived.readChar() == testChar) {
        logTrace("Received correct char value");
      } else {
        logMsg("incorrect char value -- BAD");
        pass = false;
      }
      if (messageReceived.readInt() == testInt) {
        logTrace("Received correct int value");
      } else {
        logMsg("incorrect int value -- BAD");
        pass = false;
      }
      if (messageReceived.readDouble() == ((Double) testObject).doubleValue()) {
        logTrace("Received correct object");
      } else {
        logMsg("incorrect object -- BAD");
        pass = false;
      }
      if (messageReceived.readString().equals(testString)) {
        logTrace("Received correct String");
      } else {
        logMsg("incorrect string -- BAD");
        pass = false;
      }
      if (pass == false) {
        logMsg("Test failed -- see above");
        throw new Exception();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendReceiveStreamMsgTopicTest");
    }
  }

  /*
   * @testName: sendReceiveTextMsgTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
   * 
   * @test_Strategy: Send message with appropriate data Receive message and
   * check data
   */

  public void sendReceiveTextMsgTopicTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String text = "test";

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logMsg("Creating 1 message");
      messageSent = new TextMessageTestImpl();
      messageSent.setText(text);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendReceiveTextMsgTopicTest");
      logMsg("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("Did not receive message");
      }
      if (messageReceived.getText().equals(text)) {
        logMsg("Received correct text");
      } else {
        throw new Exception("Did not receive correct message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendReceiveTextMsgTopicTest");
    }
  }

  /*
   * @testName: sendSetsJMSDestinationTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.1; JMS:SPEC:246;
   * JMS:JAVADOC:365; JMS:JAVADOC:363;
   * 
   * @test_Strategy: Send message verify that JMSDestination was set
   */

  public void sendSetsJMSDestinationTopicTest() throws Fault {
    try {
      Message message;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      logMsg("Creating 1 message");
      message = new MessageTestImpl();
      message.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendSetsJMSDestinationTopicTest");

      // set header value
      message.setJMSDestination(null);
      logMsg("Publishing message");
      tool.getDefaultTopicPublisher().publish(message);

      // check again
      logTrace("Check header value");
      if (!((Topic) message.getJMSDestination()).getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        throw new Exception("Header not set correctly");
      } else {
        logTrace("Header set correctly");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendSetsJMSDestinationTopicTest");
    }
  }

  /*
   * @testName: sendSetsJMSExpirationTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.3; JMS:SPEC:246;
   * JMS:JAVADOC:381; JMS:JAVADOC:379;
   * 
   * @test_Strategy: Send message verify that JMSExpiration was set
   */

  public void sendSetsJMSExpirationTopicTest() throws Fault {
    try {
      Message message;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      logMsg("Creating 1 message");
      message = new MessageTestImpl();
      message.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendSetsJMSExpirationTopicTest");

      // set header value
      logTrace("Set JMSExpiration to 9999");
      message.setJMSExpiration(9999);
      logMsg("Publishing message");
      tool.getDefaultTopicPublisher().publish(message);

      // check header
      long mode = message.getJMSExpiration();

      logTrace("Check header value");
      if (mode == 9999) {
        logTrace("JMSExpiration for message is " + mode);
        throw new Exception("Header not set correctly");
      } else {
        logTrace("Header set correctly");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendSetsJMSExpirationTopicTest");
    }
  }

  /*
   * @testName: sendSetsJMSPriorityTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.4; JMS:SPEC:246;
   * JMS:JAVADOC:385; JMS:JAVADOC:383;
   * 
   * @test_Strategy: Send message verify that JMSPriority was set
   */

  public void sendSetsJMSPriorityTopicTest() throws Fault {
    try {
      Message message;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      logMsg("Creating 1 message");
      message = new MessageTestImpl();
      message.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendSetsJMSPriorityTopicTest");

      // set header value
      logTrace("Set JMSPriority to 9999");
      message.setJMSPriority(9999);
      logMsg("Publishing message");
      tool.getDefaultTopicPublisher().publish(message);

      // check header value
      int mode = message.getJMSPriority();

      logTrace("Check header value");
      if (mode == 9999) {
        logTrace("JMSPriority for message is " + mode);
        throw new Exception("Header not set correctly");
      } else {
        logTrace("Header set correctly: " + mode);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendSetsJMSPriorityTopicTest");
    }
  }

  /*
   * @testName: sendSetsJMSMessageIDTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.5; JMS:SPEC:246;
   * JMS:JAVADOC:345; JMS:JAVADOC:343;
   * 
   * @test_Strategy: Send message verify that JMSMessageID was set
   */

  public void sendSetsJMSMessageIDTopicTest() throws Fault {
    try {
      Message message;
      String id0 = "foo";

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      logMsg("Creating 1 message");
      message = new MessageTestImpl();
      message.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendSetsJMSMessageIDTopicTest");

      // set header value
      logTrace("Set JMSMessageID to \"" + id0 + "\"");
      message.setJMSMessageID(id0);
      logMsg("Publishing message");
      tool.getDefaultTopicPublisher().publish(message);

      // check header value
      String id1 = message.getJMSMessageID();

      logTrace("Check header value");
      if (id1.equals(id0)) {
        logTrace("JMSMessageID for message is " + id1);
        throw new Exception("Header not set correctly");
      } else {
        logTrace("Header set correctly: " + id1);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendSetsJMSMessageIDTopicTest");
    }
  }

  /*
   * @testName: sendSetsJMSTimestampTopicTest
   * 
   * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.6; JMS:SPEC:246;
   * JMS:JAVADOC:349; JMS:JAVADOC:347;
   * 
   * @test_Strategy: Send message verify that JMSTimestamp was set
   */

  public void sendSetsJMSTimestampTopicTest() throws Fault {
    try {
      Message message;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      logMsg("Creating 1 message");
      message = new MessageTestImpl();
      message.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendSetsJMSTimestampTopicTest");

      // set header value
      logTrace("Set JMSTimestamp to 9999");
      message.setJMSTimestamp(9999);
      logMsg("Publishing message");
      tool.getDefaultTopicPublisher().publish(message);

      // check header value
      long mode = message.getJMSTimestamp();

      logTrace("Check header value");
      if (mode == 9999) {
        logTrace("JMSTimestamp for message is " + mode);
        throw new Exception("Header not set correctly");
      } else {
        logTrace("Header set correctly: " + mode);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("sendSetsJMSTimestampTopicTest");
    }
  }

}
