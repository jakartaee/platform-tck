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
package com.sun.ts.lib.implementation.sun.jms;

import java.util.*;
import java.io.*;
import javax.naming.*;
import javax.jms.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

/**
 * This is an implementation of the TSJMSObjectsInterface. An implementation of
 * this class must be supplied by any JMS implementation wishing to get JMS
 * adminsitered objects: ConnectionFactories, queues and topics.
 *
 * @author Dianne Jiao
 */

public class SunRIJMSObjects implements TSJMSObjectsInterface {
  private static Context jndiContext = null;

  private static QueueConnectionFactory qcf = null;

  private static TopicConnectionFactory tcf = null;

  private static ConnectionFactory cf = null;

  private javax.jms.Topic testTopic = null;

  private javax.jms.Queue testQueue = null;

  private void getJNDIContext() throws Exception {

    if (jndiContext == null) {
      try {
        TestUtil.logTrace("Getting initial context");
        jndiContext = new InitialContext();
      } catch (javax.naming.NamingException ne) {
        TestUtil.logErr("Could not create JNDI context because: ", ne);
        TestUtil.printStackTrace(ne);
        throw ne;
      }
    }
  }

  /**
   * This method allows individual implementation to get the Queue
   */

  public javax.jms.Queue getQueue(String name) throws Exception {
    getJNDIContext();

    try {
      testQueue = (javax.jms.Queue) jndiContext.lookup(name);
    } catch (Exception e) {
      TestUtil.logErr("Failed to lookup Queue");
      TestUtil.printStackTrace(e);
      throw e;
    }
    return testQueue;
  }

  /**
   * This method allows individual implementation to get the Topic
   */

  public Topic getTopic(String name) throws Exception {
    getJNDIContext();

    try {
      testTopic = (Topic) jndiContext.lookup(name);
    } catch (Exception e) {
      TestUtil.logErr("Failed to lookup Topic");
      TestUtil.printStackTrace(e);
      throw e;
    }
    return testTopic;
  }

  /**
   * This method allows individual implementation to get the
   * QueueConnectionFactory
   */

  public QueueConnectionFactory getQueueConnectionFactory(String name)
      throws Exception {
    getJNDIContext();

    try {
      qcf = (QueueConnectionFactory) jndiContext.lookup(name);
    } catch (Exception e) {
      TestUtil.logErr("Failed to lookup QueueConnectionFactory");
      TestUtil.printStackTrace(e);
      throw e;
    }
    return qcf;
  }

  /**
   * This method allows individual implementation to get the
   * TopicConnectionFactory
   */

  public TopicConnectionFactory getTopicConnectionFactory(String name)
      throws Exception {
    getJNDIContext();

    try {
      tcf = (TopicConnectionFactory) jndiContext.lookup(name);
    } catch (Exception e) {
      TestUtil.logErr("Failed to lookup TopicConnectionFactory");
      TestUtil.printStackTrace(e);
      throw e;
    }
    return tcf;
  }

  /**
   * This method allows individual implementation to get the ConnectionFactory
   */

  public ConnectionFactory getConnectionFactory(String name) throws Exception {
    getJNDIContext();

    try {
      cf = (ConnectionFactory) jndiContext.lookup(name);
    } catch (Exception e) {
      TestUtil.logErr("Failed to lookup ConnectionFactory");
      TestUtil.printStackTrace(e);
      throw e;
    }
    return cf;
  }
}
