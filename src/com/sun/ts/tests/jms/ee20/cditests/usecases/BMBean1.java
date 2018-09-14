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

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless(name = "CDIUseCasesBMBEAN1")
@Remote({ BMBean1IF.class })
public class BMBean1 implements BMBean1IF {

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

  @EJB(name = "ejb/CDIUseCasesBMBEAN2")
  BMBean2IF bmbean2;

  @Inject
  UserTransaction ut;

  @PostConstruct
  public void postConstruct() {
    System.out.println("BMBean1:postConstruct()");
    System.out.println("queue=" + queue);
    System.out.println("topic=" + topic);
    System.out.println("cfactory=" + cfactory);
    System.out.println("bmbean2=" + bmbean2);
    System.out.println("ut=" + ut);
    if (queue == null || topic == null || context == null || cfactory == null
        || bmbean2 == null || ut == null) {
      throw new EJBException("postConstruct failed: injection failure");
    }
  }

  public void init(Properties p) {
    TestUtil.logMsg("BMBean1.init()");
    try {
      TestUtil.init(p);
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("BMBean1.init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("BMBean1.init: failed");
    }
  }

  public boolean cleanupQueue(int numOfMsgs) {
    int count = 0;
    String message = null;
    TestUtil.logMsg("BMBean1.cleanupQueue()");
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

  public void method1() {
    TestUtil.logMsg("BMBean1.method1(): JMSContext context=" + context);
    JMSProducer producer = context.createProducer();
    TestUtil.logMsg("Sending message [Message 1]");
    producer.send(queue, "Message 1");
    TestUtil.logMsg("Sending message [Message 2]");
    producer.send(queue, "Message 2");
  }

  public void method2() {
    TestUtil.logMsg("BMBean1.method2(): JMSContext context=" + context);
    TestUtil.logMsg("Sending message [Message 1]");
    context.createProducer().send(queue, "Message 1");
    TestUtil.logMsg("Calling BMBean2.method2()");
    bmbean2.method2();
  }

  public void method3() {
    try {
      TestUtil.logMsg("BMBean1.method3()");
      TestUtil.logMsg("Begin First User Transaction");
      ut.begin();
      TestUtil.logMsg("JMSContext context=" + context);
      JMSProducer producer = context.createProducer();
      TestUtil.logMsg("Sending message [Message 1]");
      producer.send(queue, "Message 1");
      TestUtil.logMsg("Sending message [Message 2]");
      producer.send(queue, "Message 2");
      TestUtil.logMsg("Commit First User Transaction");
      ut.commit();
      TestUtil.logMsg("Begin Second User Transaction");
      ut.begin();
      producer = context.createProducer();
      TestUtil.logMsg("Sending message [Message 3]");
      producer.send(queue, "Message 3");
      TestUtil.logMsg("Sending message [Message 4]");
      producer.send(queue, "Message 4");
      TestUtil.logMsg("Commit Second User Transaction");
      ut.commit();
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  public void method4() {
    TestUtil.logMsg("BMBean1.method4(): JMSContext context=" + context);
    try {
      JMSProducer producer = context.createProducer();
      TestUtil.logMsg("Sending message [Message 1]");
      producer.send(queue, "Message 1");
      TestUtil.logMsg("Sending message [Message 2]");
      producer.send(queue, "Message 2");
      TestUtil.logMsg("Begin User Transaction");
      ut.begin();
      producer = context.createProducer();
      TestUtil.logMsg("Sending message [Message 3]");
      producer.send(queue, "Message 3");
      TestUtil.logMsg("Commit User Transaction");
      ut.commit();
      producer = context.createProducer();
      TestUtil.logMsg("Sending message [Message 4]");
      producer.send(queue, "Message 4");
      TestUtil.logMsg("Sending message [Message 5]");
      producer.send(queue, "Message 5");
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }
}
