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
@Stateless(name = "CDIUseCasesBMBEAN2")
@Remote({ BMBean2IF.class })
public class BMBean2 implements BMBean2IF {

  private static final long serialVersionUID = 1L;

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

  @PostConstruct
  public void postConstruct() {
    System.out.println("BMBean2:postConstruct()");
    System.out.println("queue=" + queue);
    System.out.println("topic=" + topic);
    System.out.println("cfactory=" + cfactory);
    if (queue == null || topic == null || context == null || cfactory == null) {
      throw new EJBException("postConstruct failed: injection failure");
    }
  }

  public void init(Properties p) {
    TestUtil.logMsg("BMBean2.init()");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("BMBean2.init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("BMBean2.init: failed");
    }
  }

  public void method2() {
    TestUtil.logMsg("BMBean2.method2(): JMSContext context=" + context);
    TestUtil.logMsg("Sending message [Message 2]");
    context.createProducer().send(queue, "Message 2");
  }
}
