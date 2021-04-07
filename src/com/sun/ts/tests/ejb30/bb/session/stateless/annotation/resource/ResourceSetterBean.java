/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource;

import java.net.URL;

import javax.sql.DataSource;

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceBeanBase;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF;

import jakarta.annotation.Resource;
import jakarta.annotation.Resource.AuthenticationType;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TimerService;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnectionFactory;
import jakarta.transaction.TransactionSynchronizationRegistry;
import jakarta.transaction.UserTransaction;

@Stateless(name = "ResourceSetterBean")
@Remote({ ResourceIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class ResourceSetterBean extends ResourceBeanBase implements ResourceIF {
  private Dog dog;

  private SessionContext sessionContext;

  private UserTransaction userTransaction;

  private DataSource dataSource;

  private DataSource dataSource2;

  private jakarta.mail.Session mailSession;

  private URL url;

  private QueueConnectionFactory queueConnectionFactory;

  private TopicConnectionFactory topicConnectionFactory;

  private ConnectionFactory connectionFactoryQ;

  private ConnectionFactory connectionFactoryT;

  private Destination destinationQ;

  private Destination destinationT;

  private Topic topic;

  private Queue queue;

  private Object orb;

  private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

  private TimerService timerService;

  public ResourceSetterBean() {
  }

  public void remove() {
  }

  //////////////////////////////////////////////////////////////////////

  protected jakarta.ejb.EJBContext getEJBContext() {
    return sessionContext;
  }

  @Resource(name = "sessionContext", description = "session context", type = SessionContext.class)
  private void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  //////////////////////////////////////////////////////////////////////
  @Resource(name = "dog", description = "inject a custom jndi resource")
  private void setDog(Dog dog) {
    this.dog = dog;
  }

  @Override
  protected Object getCustomeResource() {
    return dog;
  }

  @Override
  protected String getCustomeResourceName() {
    return "dog";
  } // as defined in setter injection

  //////////////////////////////////////////////////////////////////////

  protected DataSource getDataSource() {
    return dataSource;
  }

  @Resource(name = "dataSource")
  private void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  protected String getDataSourceName() {
    return "dataSource";
  }

  //////////////////////////////////////////////////////////////////////

  protected DataSource getDataSource2() {
    return dataSource2;
  }

  @Resource(name = "myDataSource2", type = DataSource.class, shareable = true, authenticationType = AuthenticationType.CONTAINER)
  private void setDataSource2(DataSource dataSource2) {
    this.dataSource2 = dataSource2;
  }

  protected String getDataSource2Name() {
    return "myDataSource2";
  }

  //////////////////////////////////////////////////////////////////////

  protected jakarta.mail.Session getMailSession() {
    return mailSession;
  }

  @Resource(name = "mailSession")
  private void setMailSession(jakarta.mail.Session mailSession) {
    this.mailSession = mailSession;
  }

  protected String getMailSessionName() {
    return "mailSession";
  }

  //////////////////////////////////////////////////////////////////////

  protected URL getUrl() {
    return url;
  }

  @Resource(name = "url")
  private void setUrl(URL url) {
    this.url = url;
  }

  protected String getUrlName() {
    return "url";
  }

  //////////////////////////////////////////////////////////////////////

  protected QueueConnectionFactory getQueueConnectionFactory() {
    return queueConnectionFactory;
  }

  @Resource(name = "queueConnectionFactory")
  private void setQueueConnectionFactory(
      QueueConnectionFactory queueConnectionFactory) {
    this.queueConnectionFactory = queueConnectionFactory;
  }

  protected String getQueueConnectionFactoryName() {
    return "queueConnectionFactory";
  }

  //////////////////////////////////////////////////////////////////////

  protected TopicConnectionFactory getTopicConnectionFactory() {
    return topicConnectionFactory;
  }

  @Resource(name = "topicConnectionFactory")
  private void setTopicConnectionFactory(
      TopicConnectionFactory topicConnectionFactory) {
    this.topicConnectionFactory = topicConnectionFactory;
  }

  protected String getTopicConnectionFactoryName() {
    return "topicConnectionFactory";
  }

  //////////////////////////////////////////////////////////////////////

  protected ConnectionFactory getConnectionFactoryT() {
    return connectionFactoryT;
  }

  @Resource(name = "connectionFactoryT")
  private void setConnectionFactoryT(ConnectionFactory conn) {
    connectionFactoryT = conn;
  }

  protected String getConnectionFactoryTName() {
    return "connectionFactoryT";
  }

  //////////////////////////////////////////////////////////////////////

  protected ConnectionFactory getConnectionFactoryQ() {
    return connectionFactoryQ;
  }

  @Resource(name = "connectionFactoryQ")
  private void setConnectionFactoryQ(ConnectionFactory conn) {
    connectionFactoryQ = conn;
  }

  protected String getConnectionFactoryQName() {
    return "connectionFactoryQ";
  }

  //////////////////////////////////////////////////////////////////////

  protected Destination getDestinationQ() {
    return destinationQ;
  }

  @Resource(name = "destinationQ", type = Queue.class)
  private void setDestinationQ(Destination dest) {
    destinationQ = dest;
  }

  protected String getDestinationQName() {
    return "destinationQ";
  }

  //////////////////////////////////////////////////////////////////////

  protected Destination getDestinationT() {
    return destinationT;
  }

  @Resource(name = "destinationT", type = Topic.class)
  private void setDestinationT(Destination dest) {
    destinationT = dest;
  }

  protected String getDestinationTName() {
    return "destinationT";
  }

  //////////////////////////////////////////////////////////////////////

  protected Topic getTopic() {
    return topic;
  }

  @Resource(name = "topic")
  private void setTopic(Topic topic) {
    this.topic = topic;
  }

  protected String getTopicName() {
    return "topic";
  }

  //////////////////////////////////////////////////////////////////////

  protected Queue getQueue() {
    return queue;
  }

  @Resource(name = "queue")
  private void setQueue(Queue queue) {
    this.queue = queue;
  }

  protected String getQueueName() {
    return "queue";
  }

  //////////////////////////////////////////////////////////////////////

  protected String getUserTransactionName() {
    return "myUserTransaction";
  }

  @Resource(description = "user transaction", name = "myUserTransaction", type = UserTransaction.class)
  private void setUserTransaction(UserTransaction ut) {
    userTransaction = ut;
  }

  protected jakarta.transaction.UserTransaction getUserTransaction() {
    return userTransaction;
  }

  //////////////////////////////////////////////////////////////////////

  protected String getTransactionSynchronizationRegistryName() {
    return "myTransactionSynchronizationRegistry";
  }

  @Resource(name = "myTransactionSynchronizationRegistry", description = "TransactionSynchronizationRegistry injected")
  private void setTransactionSynchronizationRegistry(
      TransactionSynchronizationRegistry t) {
    this.transactionSynchronizationRegistry = t;
  }

  protected TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
    return transactionSynchronizationRegistry;
  }

  protected String getTimerServiceName() {
    return "myTimerService";
  }

  @Resource(name = "myTimerService", description = "TimerService injected")
  private void setTimerService(TimerService t) {
    this.timerService = t;
  }

  protected TimerService getTimerService() {
    return timerService;
  }

}
