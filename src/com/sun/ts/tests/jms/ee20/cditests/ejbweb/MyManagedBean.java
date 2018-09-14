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
package com.sun.ts.tests.jms.ee20.cditests.ejbweb;

import java.io.Serializable;
import java.util.Properties;
import javax.naming.*;
import javax.jms.*;
import java.util.Enumeration;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.ManagedBean;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;
import javax.inject.*;
import javax.enterprise.inject.*;
import javax.enterprise.context.*;

@ManagedBean(value = "mybean")
public class MyManagedBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private Properties p = new Properties();

  private long timeout;

  // JMSContext CDI injection specifying ConnectionFactory
  @Inject
  @JMSConnectionFactory("jms/ConnectionFactory")
  private transient JMSContext context;

  @Resource(name = "jms/MY_QUEUE")
  private transient Queue queue;

  @Resource(name = "jms/MY_TOPIC")
  private transient Topic topic;

  public MyManagedBean() {
  }

  @PostConstruct
  public void postConstruct() {
    System.out.println("MyManageBean:postConstruct()");
    System.out.println("queue=" + queue);
    System.out.println("topic=" + topic);
    if (queue == null || topic == null || context == null) {
      throw new RuntimeException("postConstruct failed: injection failure");
    }
  }

  public void init(Properties p) {
    System.out.println("MyManageBean:init()");
    try {
      TestUtil.init(p);
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new RuntimeException("init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new RuntimeException("init: failed");
    }
  }

  public boolean sendRecvQueueTestUsingCDIFromManagedBean() {
    boolean pass = true;
    JMSConsumer consumer = null;
    TestUtil.logMsg("sendRecvQueueTestUsingCDIFromManagedBean");
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // Create JMSConsumer from JMSContext
      consumer = context.createConsumer(queue);

      TestUtil.logMsg("Creating TextMessage");
      messageSent = context.createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvQueueTestUsingCDIFromManagedBean");
      TestUtil.logMsg("Sending TextMessage");
      context.createProducer().send(queue, messageSent);
      TestUtil.logMsg("Receiving TextMessage");
      messageReceived = (TextMessage) consumer.receive(timeout);

      // Check to see if correct message received
      if (messageReceived == null) {
        TestUtil.logErr("No message was received");
        pass = false;
      } else {
        TestUtil.logMsg("Message Sent: \"" + messageSent.getText() + "\"");
        TestUtil
            .logMsg("Message Received: \"" + messageReceived.getText() + "\"");
        if (messageReceived.getText().equals(messageSent.getText())) {
          TestUtil.logMsg("Received correct message");
        } else {
          TestUtil.logErr("Received incorrect message");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    } finally {
      try {
        consumer.receive(timeout);
        while (consumer.receiveNoWait() != null)
          ;
        consumer.close();
      } catch (Exception e) {
      }
    }
    return pass;
  }

  public boolean sendRecvTopicTestUsingCDIFromManagedBean() {
    boolean pass = true;
    JMSConsumer consumer = null;
    TestUtil.logMsg("sendRecvTopicTestUsingCDIFromManagedBean");
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // Create JMSConsumer from JMSContext
      consumer = context.createConsumer(queue);

      TestUtil.logMsg("Creating TextMessage");
      messageSent = context.createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvTopicTestUsingCDIFromManagedBean");
      TestUtil.logMsg("Sending TextMessage");
      context.createProducer().send(queue, messageSent);
      TestUtil.logMsg("Receiving TextMessage");
      messageReceived = (TextMessage) consumer.receive(timeout);

      // Check to see if correct message received
      if (messageReceived == null) {
        TestUtil.logErr("No message was received");
        pass = false;
      } else {
        TestUtil.logMsg("Message Sent: \"" + messageSent.getText() + "\"");
        TestUtil
            .logMsg("Message Received: \"" + messageReceived.getText() + "\"");
        if (messageReceived.getText().equals(messageSent.getText())) {
          TestUtil.logMsg("Received correct message");
        } else {
          TestUtil.logErr("Received incorrect message");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    } finally {
      try {
        consumer.receive(timeout);
        while (consumer.receiveNoWait() != null)
          ;
        consumer.close();
      } catch (Exception e) {
      }
    }
    return pass;
  }
}
