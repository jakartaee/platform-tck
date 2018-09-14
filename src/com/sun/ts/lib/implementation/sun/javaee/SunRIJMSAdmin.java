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

package com.sun.ts.lib.implementation.sun.javaee;

import java.util.*;
import java.io.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

/**
 * This is an implementation of the TSJMSAdminInterface. An implementation of
 * this class must be supplied by any Java EE implementation wishing to have
 * JavaTest (the harness which drives the TS tests) automatically create and
 * remove JMS queues and topics. This particular implementation uses the
 * create/remove semantics of Sun's Java EE reference implementation.
 * 
 * @author Kyle Grucci
 */
public class SunRIJMSAdmin implements TSJMSAdminInterface {
  PrintWriter log = null;

  public void init(PrintWriter writer) {
  }

  /**
   * This method creates queues in a Java EE implementation.
   *
   * @param queues
   *          queues to remove
   * 
   */
  public void createQueues(String[] queues) throws TSJMSAdminException {
  }

  /**
   * This method removes topics from a Java EE implementation.
   *
   * @param topics
   *          topics to remove
   * 
   */
  public void createTopics(String[] topics) throws TSJMSAdminException {
  }

  /**
   * This method removes queues from a Java EE implementation.
   *
   * @param queues
   *          queues to remove
   * 
   */
  public void removeQueues(String[] queues) throws TSJMSAdminException {
  }

  /**
   * This method removes topics from a Java EE implementation.
   *
   * @param topics
   *          topics to remove
   * 
   */
  public void removeTopics(String[] topics) throws TSJMSAdminException {
  }

  /*
   * Uses adminTool to create a Queue Destination object
   *
   * @param q JNDI name of the Queue Destination object
   * 
   */
  private void createQueue(String q) throws Exception {
  }

  /*
   * Uses adminTool to create a Topic Destination object
   *
   * @param q JNDI name of the Topic Destination object
   * 
   */
  private void createTopic(String topic) throws Exception {
  }

  private void deleteDestination(String s) throws Exception {
  }

  // ===========
  /**
   * This method creates queueConnectionFactories in a Java EE implementation.
   *
   * Arguments passed as queueConnectionFactories[n],prop[n] where n = offset
   * and the props[n] string is equal to the needed name=value pairs. If more
   * than one property they should be space separated in the props string.
   *
   * @param queueConnectionFactories
   *          queueConnectionFactories to create
   * @param props
   *          properties for the connection if any
   */
  public void createQueueConnectionFactories(String[] queueConnectionFactories,
      String[] props) throws TSJMSAdminException {
  }

  /*
   * Uses adminTool to create a QueueConnectionFactory object
   *
   * @param qConnectionFactory JNDI name of the QueueConnectionFactory object
   * 
   * @param props properties for the connection if any
   * 
   */
  private void createQueueConnectionFactory(String qConnectionFactory,
      String props) throws Exception {
  }

  // ===========
  /**
   * This method creates topicConnectionFactories in a Java EE implementation.
   *
   * Arguments passed as topicConnectionFactories[n],prop[n] where n = offset
   * and the props[n] string is equal to the needed name=value pairs. If more
   * than one property they should be space separated in the props string.
   *
   * @param topicConnectionFactories
   *          topicConnectionFactories to create
   * @param props
   *          properties if any
   */
  public void createTopicConnectionFactories(String[] topicConnectionFactories,
      String[] props) throws TSJMSAdminException {
  }

  /*
   * Uses adminTool to create a TopicConnectionFactory object
   *
   * @param tConnectionFactory JNDI name of the TopicConnectionFactory object
   * 
   * @param props properties, if any
   */
  private void createTopicConnectionFactory(String tConnectionFactory,
      String props) throws Exception {
  }

  // ===========
  /**
   * This method creates connectionFactories in a Java EE implementation.
   *
   * Arguments passed as connectionFactories[n],prop[n] where n = offset and the
   * props[n] string is equal to the needed name=value pairs. If more than one
   * property they should be space separated in the props string.
   *
   * @param connectionFactories
   *          connectionFactories to create
   * @param props
   *          properties if any
   */
  public void createConnectionFactories(String[] connectionFactories,
      String[] props) throws TSJMSAdminException {
  }

  /*
   * Uses adminTool to create a ConnectionFactory object
   *
   * @param connectionFactory JNDI name of the ConnectionFactory object
   * 
   * @param props properties, if any
   */
  private void createConnectionFactory(String connectionFactory, String props)
      throws Exception {
  }

  /**
   * This method removes ConnectionFactories from a Java EE implementation.
   *
   * @param topics
   *          JmsConnectionFactoryNames to remove
   * 
   */
  public void removeJmsConnectionFactories(String[] jmsConnectionFactoryNames)
      throws TSJMSAdminException {
  }

  /**
   * This method will delete a JmsConnectory factory from a Java EE
   * implementation.
   *
   * @param factoryName
   *          JmsConnectionFactoryName to remove
   * 
   */

  private void deleteJmsConnectionFactory(String factoryName) throws Exception {
  }

}
