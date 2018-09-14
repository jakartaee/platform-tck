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
package com.sun.ts.tests.jms.ee20.cditests.mdb;

import java.io.Serializable;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.EJBContext;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import javax.sql.*;
import javax.inject.Inject;
import javax.annotation.Resource;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

public class MsgBeanQ implements MessageDrivenBean, MessageListener {
  private static final long serialVersionUID = 1L;

  private Properties p = new Properties();

  private TSNamingContext context = null;

  private transient MessageDrivenContext mdc = null;

  // JMSContext CDI injection specifying QueueConnectionFactory
  @Inject
  @JMSConnectionFactory("jms/QueueConnectionFactory")
  transient JMSContext context1;

  // JMSContext CDI injection specifying TopicConnectionFactory
  @Inject
  @JMSConnectionFactory("jms/TopicConnectionFactory")
  transient JMSContext context2;

  @Resource(name = "jms/mdbReplyQueue")
  private transient Queue replyQueue;

  @Resource(name = "jms/mdbReplyTopic")
  private transient Topic replyTopic;

  public MsgBeanQ() {
  }

  public void ejbCreate() {
    System.out.println("MsgBeanQ.ejbCreate()!");
  }

  public void setMessageDrivenContext(MessageDrivenContext mdc) {
    System.out.println("MsgBeanQ.setMessageDrivenContext()!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    System.out.println("MsgBeanQ.ejbRemove()!");
  }

  public EJBContext getEJBContext() {
    return this.mdc;
  }

  // ================== business methods ====================================
  public void onMessage(javax.jms.Message msg) {
    System.out.println("MsgBeanQ.onMessage(): entering MDB");
    System.out.println("MsgBeanQ.onMessage(): JMSContext context1=" + context1);
    System.out.println("MsgBeanQ.onMessage(): JMSContext context2=" + context2);
    boolean status = false;
    String reason = null;
    String testname = null;
    try {
      testname = msg.getStringProperty("TESTNAME");
    } catch (JMSException e) {
      reason = "MsgBeanQ.onMessage(): failed to get TESTNAME from message";
      if (context1 == null || context2 == null)
        reason = reason + ", failed CDI injection of JMSContext";
      System.out.println(reason);
      return;
    }
    if (context1 == null || context2 == null) {
      reason = "MsgBeanQ.onMessage(): failed CDI injection of JMSContext";
      System.out.println(reason);
      if (testname.equals("testCDIInjectionOfMDBWithQueueReplyFromEjb"))
        sendReplyToQ(testname, status, reason, context1, replyQueue);
      else
        sendReplyToT(testname, status, reason, context2, replyTopic);
    }

    // reply to all messages that have a testname.
    status = true;
    reason = "MsgBeanQ.onMessage(): received message from TESTNAME=" + testname;
    System.out.println(reason);
    if (testname.equals("testCDIInjectionOfMDBWithQueueReplyFromEjb"))
      sendReplyToQ(testname, status, reason, context1, replyQueue);
    else
      sendReplyToT(testname, status, reason, context2, replyTopic);
    System.out.println("MsgBeanQ.onMessage(): leaving MDB");
  }

  private void sendReplyToQ(String testname, boolean status, String reason,
      JMSContext context, Queue qReply) {
    TextMessage msg = null;
    System.out.println("MsgBeanQ.sendReplyToQ(): entering");
    try {
      msg = context.createTextMessage();
      msg.setStringProperty("TESTNAME", testname);
      msg.setStringProperty("JMSCONTEXT", "context1");
      msg.setStringProperty("REASON", reason);
      msg.setText(testname);
      if (status) {
        msg.setStringProperty("STATUS", "Pass");
      } else {
        msg.setStringProperty("STATUS", "Fail");
      }
      System.out.println(
          "MsgBeanQ.sendReplyToQ(): sending reply with TESTNAME=" + testname
              + " STATUS=" + status + " JMSCONTEXT=context1 REASON=" + reason);
      context.createProducer().send(qReply, msg);
    } catch (Exception e) {
      throw new java.lang.IllegalStateException(
          "Failed to send reply to ReplyQueue");
    }
    System.out.println("MsgBeanQ.sendReplyToQ(): leaving");
  }

  private void sendReplyToT(String testname, boolean status, String reason,
      JMSContext context, Topic tReply) {
    TextMessage msg = null;
    System.out.println("MsgBeanQ.sendReplyToT(): entering");
    try {
      msg = context.createTextMessage();
      msg.setStringProperty("TESTNAME", testname);
      msg.setStringProperty("JMSCONTEXT", "context2");
      msg.setStringProperty("REASON", reason);
      msg.setText(testname);
      if (status) {
        msg.setStringProperty("STATUS", "Pass");
      } else {
        msg.setStringProperty("STATUS", "Fail");
      }
      System.out.println(
          "MsgBeanQ.sendReplyToT(): sending reply with TESTNAME=" + testname
              + " STATUS=" + status + " JMSCONTEXT=context2 REASON=" + reason);
      context.createProducer().send(tReply, msg);
    } catch (Exception e) {
      throw new java.lang.IllegalStateException(
          "Failed to send reply to ReplyTopic");
    }
    System.out.println("MsgBeanQ.sendReplyToT(): leaving");
  }
}
