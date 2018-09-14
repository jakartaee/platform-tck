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

package com.sun.ts.tests.jms.ee20.cditests.mdb;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.Properties;
import java.util.ArrayList;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.SessionContext;
import javax.jms.*;
import javax.transaction.*;
import javax.naming.*;
import javax.inject.Inject;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;

import com.sun.ts.tests.jms.common.*;

@Stateful(name = "CDITestsMDBClntBean")
@Remote({ EjbClientIF.class })
public class EjbClient implements EjbClientIF {

  private static final boolean debug = false;

  private static final long serialVersionUID = 1L;

  long timeout;

  private static int testsExecuted = 0;

  // JMSContext CDI injection specifying ConnectionFactory
  @Inject
  @JMSConnectionFactory("jms/ConnectionFactory")
  transient JMSContext context;

  @Resource(name = "jms/MyConnectionFactory")
  private transient ConnectionFactory cfactory;

  @Resource(name = "jms/MDB_QUEUE")
  private transient Queue queueToMDB;

  @Resource(name = "jms/mdbReplyQueue")
  private transient Queue replyQueue;

  @Resource(name = "jms/mdbReplyTopic")
  private transient Topic replyTopic;

  private void cleanup() {
    TestUtil.logMsg("cleanup");
  }

  @PostConstruct
  public void postConstruct() {
    System.out.println("EjbClient:postConstruct()");
    System.out.println("cfactory=" + cfactory);
    System.out.println("queueToMDB=" + queueToMDB);
    System.out.println("replyQueue=" + replyQueue);
    System.out.println("replyTopic=" + replyTopic);
    if (context == null || cfactory == null || queueToMDB == null
        || replyQueue == null || replyTopic == null) {
      throw new EJBException("postConstruct failed: injection failure");
    }
  }

  public void init(Properties p) {
    try {
      TestUtil.init(p);
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    }
  }

  public boolean echo(String testName) {
    boolean pass = false;

    if (testName.equals("testCDIInjectionOfMDBWithQueueReplyFromEjb"))
      pass = testCDIInjectionOfMDBWithQueueReplyFromEjb();
    else if (testName.equals("testCDIInjectionOfMDBWithTopicReplyFromEjb"))
      pass = testCDIInjectionOfMDBWithTopicReplyFromEjb();
    cleanup();
    return pass;
  }

  public boolean testCDIInjectionOfMDBWithQueueReplyFromEjb() {
    boolean pass = true;
    TextMessage messageSent = null;
    TextMessage messageRecv = null;
    JMSConsumer consumer = null;
    try {
      TestUtil.logMsg("Creating TextMessage to send to MDB MsgBeanQ");
      messageSent = context.createTextMessage();
      messageSent.setText("Send message to MDB MsgBeanQ");
      messageSent.setStringProperty("TESTNAME",
          "testCDIInjectionOfMDBWithQueueReplyFromEjb");

      TestUtil.logMsg("Creating JMSConsumer for MDB MsgBeanQ Reply Queue");
      consumer = context.createConsumer(replyQueue);

      TestUtil.logMsg("Sending TextMessage to MDB MsgBeanQ");
      context.createProducer().send(queueToMDB, messageSent);

      for (int i = 1; i < 10; ++i) {
        TestUtil
            .logMsg("Try receiving reply message from MDB MsgBeanQ (loop count="
                + i + ")");
        messageRecv = (TextMessage) consumer.receive(timeout);
        if (messageRecv != null)
          break;
      }
      if (messageRecv != null) {
        String testname = messageRecv.getStringProperty("TESTNAME");
        String reason = messageRecv.getStringProperty("REASON");
        String jmscontext = messageRecv.getStringProperty("JMSCONTEXT");
        String status = messageRecv.getStringProperty("STATUS");
        if (status.equals("Pass")) {
          TestUtil.logMsg("TEST=" + testname + " PASSED for JMSCONTEXT="
              + jmscontext + " REASON=" + reason);
          pass = true;
        } else {
          TestUtil.logErr("TEST=" + testname + " FAILED for JMSCONTEXT="
              + jmscontext + " REASON=" + reason);
          pass = false;
        }
      } else {
        TestUtil.logErr("Did no receive a reply message from MDB MsgBeanQ");
        pass = false;
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

  public boolean testCDIInjectionOfMDBWithTopicReplyFromEjb() {
    boolean pass = true;
    TextMessage messageSent = null;
    TextMessage messageRecv = null;
    JMSConsumer consumer = null;
    try {
      TestUtil.logMsg("Creating TextMessage to send to MDB MsgBeanQ");
      messageSent = context.createTextMessage();
      messageSent.setText("Send message to MDB MsgBeanQ");
      messageSent.setStringProperty("TESTNAME",
          "testCDIInjectionOfMDBWithTopicReplyFromEjb");

      TestUtil.logMsg("Creating JMSConsumer for MDB MsgBeanQ Reply Topic");
      consumer = context.createConsumer(replyTopic);

      TestUtil.logMsg("Sending TextMessage to MDB MsgBeanQ");
      context.createProducer().send(queueToMDB, messageSent);

      for (int i = 1; i < 10; ++i) {
        TestUtil
            .logMsg("Try receiving reply message from MDB MsgBeanQ (loop count="
                + i + ")");
        messageRecv = (TextMessage) consumer.receive(timeout);
        if (messageRecv != null)
          break;
      }
      if (messageRecv != null) {
        String testname = messageRecv.getStringProperty("TESTNAME");
        String reason = messageRecv.getStringProperty("REASON");
        String jmscontext = messageRecv.getStringProperty("JMSCONTEXT");
        String status = messageRecv.getStringProperty("STATUS");
        if (status.equals("Pass")) {
          TestUtil.logMsg("TEST=" + testname + " PASSED for JMSCONTEXT="
              + jmscontext + " REASON=" + reason);
          pass = true;
        } else {
          TestUtil.logErr("TEST=" + testname + " FAILED for JMSCONTEXT="
              + jmscontext + " REASON=" + reason);
          pass = false;
        }
      } else {
        TestUtil.logErr("Did no receive a reply message from MDB MsgBeanQ");
        pass = false;
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
