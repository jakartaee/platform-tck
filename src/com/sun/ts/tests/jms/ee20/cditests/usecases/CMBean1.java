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

package com.sun.ts.tests.jms.ee20.cditests.usecases;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.Properties;
import java.util.ArrayList;
import javax.jms.*;
import javax.ejb.*;
import javax.transaction.*;
import javax.naming.*;
import javax.inject.Inject;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;

@TransactionManagement(TransactionManagementType.CONTAINER)
@Stateless(name = "CDIUseCasesCMBEAN1")
@Remote({ CMBean1IF.class })
public class CMBean1 implements CMBean1IF {

  private static final long serialVersionUID = 1L;

  long timeout;

  // JMSContext CDI injection specifying ConnectionFactory
  @Inject
  @JMSConnectionFactory("jms/ConnectionFactory")
  JMSContext context;

  @Resource(name = "jms/MyConnectionFactory")
  ConnectionFactory cfactory;

  @Resource(name = "jms/MY_QUEUE")
  Queue queue;

  @Resource(name = "jms/MY_TOPIC")
  Topic topic;

  @EJB(name = "ejb/CDIUseCasesCMBEAN2")
  CMBean2IF cmbean2;

  @PostConstruct
  public void postConstruct() {
    System.out.println("CMBean1:postConstruct()");
    System.out.println("queue=" + queue);
    System.out.println("topic=" + topic);
    System.out.println("cfactory=" + cfactory);
    System.out.println("cmbean2=" + cmbean2);
    if (queue == null || topic == null || context == null || cfactory == null
        || cmbean2 == null) {
      throw new EJBException("postConstruct failed: injection failure");
    }
  }

  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public void init(Properties p) {
    TestUtil.logMsg("CMBean1.init()");
    try {
      TestUtil.init(p);
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("CMBean1.init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("CMBean1.init: failed");
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean cleanupQueue(int numOfMsgs) {
    int count = 0;
    String message = null;
    TestUtil.logMsg("CMBean1.cleanupQueue()");
    try {
      JMSConsumer consumer = context.createConsumer(queue);
      for (int i = 0; i < numOfMsgs; i++) {
        message = consumer.receiveBody(String.class, timeout);
        if (message != null) {
          TestUtil.logMsg("Cleanup message: [" + message + "]");
          count++;
        }
      }
      while ((message = consumer.receiveBody(String.class, timeout)) != null) {
        TestUtil.logMsg("Cleanup message: [" + message + "]");
        count++;
      }
      consumer.close();
      TestUtil.logMsg("Cleaned up " + count + " messages from Queue (numOfMsgs="
          + numOfMsgs + ")");
      if (count == numOfMsgs)
        return true;
      else
        return false;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("CMBean1.cleanupQueue: failed");
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void method1a() {
    TestUtil.logMsg("CMBean1.method1a(): JMSContext context=" + context);
    TestUtil.logMsg("Sending message [Message 1]");
    context.createProducer().send(queue, "Message 1");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void method1b() {
    TestUtil.logMsg("CMBean1.method1b(): JMSContext context=" + context);
    TestUtil.logMsg("Sending message [Message 2]");
    context.createProducer().send(queue, "Message 2");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void method2() {
    TestUtil.logMsg("CMBean1.method2(): JMSContext context=" + context);
    TestUtil.logMsg("Calling CMBean2.method2a() followed by Bean2.method2b()");
    cmbean2.method2a();
    cmbean2.method2b();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void method3() {
    TestUtil.logMsg("CMBean1.method3(): JMSContext context=" + context);
    TestUtil.logMsg("Sending message [Message 1]");
    context.createProducer().send(queue, "Message 1");
    TestUtil.logMsg("Calling CMBean2.method3()");
    cmbean2.method3();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void method4() {
    TestUtil.logMsg("CMBean1.method4(): JMSContext context=" + context);
    JMSProducer producer = context.createProducer();
    TestUtil.logMsg("Sending message [Message 1]");
    producer.send(queue, "Message 1");
    TestUtil.logMsg("Sending message [Message 2]");
    producer.send(queue, "Message 2");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void method5() {
    TestUtil.logMsg("CMBean1.method5(): JMSContext context=" + context);
    JMSProducer producer = context.createProducer();
    TestUtil.logMsg("Sending message [Message 1]");
    producer.send(queue, "Message 1");
    cmbean2.method5();
    TestUtil.logMsg("Sending message [Message 3]");
    producer.send(queue, "Message 3");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void method6() {
    TestUtil.logMsg("CMBean1.method6(): JMSContext context=" + context);
    TestUtil.logMsg("Sending message [Message 1]");
    context.createProducer().send(queue, "Message 1");
    cmbean2.method6();
  }
}
