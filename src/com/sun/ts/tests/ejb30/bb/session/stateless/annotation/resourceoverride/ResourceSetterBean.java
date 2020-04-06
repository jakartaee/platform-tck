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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resourceoverride;

import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceBeanBase;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF;
import java.net.URL;
import javax.annotation.Resource.AuthenticationType;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.SessionContext;
import javax.annotation.Resource;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import javax.jms.QueueConnectionFactory;
import javax.jms.ConnectionFactory;
import javax.jms.TopicConnectionFactory;
import javax.sql.DataSource;
import jakarta.transaction.UserTransaction;
import javax.jms.Queue;
import javax.jms.Topic;
import org.omg.CORBA.ORB;

@Stateless(name = "ResourceSetterBean")
@Remote({ ResourceIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class ResourceSetterBean extends ResourceBeanBase implements ResourceIF {

  private SessionContext sessionContext;

  private UserTransaction userTransaction;

  private DataSource dataSource;

  private DataSource dataSource2;

  private javax.mail.Session mailSession;

  private URL url;

  private QueueConnectionFactory queueConnectionFactory;

  private TopicConnectionFactory topicConnectionFactory;

  private ConnectionFactory connectionFactoryQ;

  private ConnectionFactory connectionFactoryT;

  private Topic topic;

  private Queue queue;

  private ORB orb;

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

  protected DataSource getDataSource() {
    return dataSource;
  }

  @Resource(description = "<resource-ref>", shareable = false, authenticationType = AuthenticationType.APPLICATION, name = "dataSource", type = Object.class)
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

  @Resource(name = "myDataSource2", type = DataSource.class, shareable = false, authenticationType = AuthenticationType.APPLICATION)
  private void setDataSource2(DataSource dataSource2) {
    this.dataSource2 = dataSource2;
  }

  protected String getDataSource2Name() {
    return "myDataSource2";
  }

  //////////////////////////////////////////////////////////////////////

  protected javax.mail.Session getMailSession() {
    return mailSession;
  }

  @Resource(name = "mailSession", description = "<resource-ref>", shareable = false, authenticationType = AuthenticationType.APPLICATION)
  private void setMailSession(javax.mail.Session mailSession) {
    this.mailSession = mailSession;
  }

  protected String getMailSessionName() {
    return "mailSession";
  }

  //////////////////////////////////////////////////////////////////////

  protected URL getUrl() {
    return url;
  }

  @Resource(name = "url", description = "<resource-ref>", shareable = false, authenticationType = AuthenticationType.APPLICATION)
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

  @Resource(name = "queueConnectionFactory", description = "<resource-ref>", shareable = false, authenticationType = AuthenticationType.APPLICATION)
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

  @Resource(name = "topicConnectionFactory", description = "<resource-ref>", shareable = false, authenticationType = AuthenticationType.APPLICATION)
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

  @Resource(name = "connectionFactoryT", description = "<resource-ref>", shareable = false, authenticationType = AuthenticationType.APPLICATION)
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

  @Resource(name = "connectionFactoryQ", description = "<resource-ref>", shareable = false, authenticationType = AuthenticationType.APPLICATION)
  private void setConnectionFactoryQ(ConnectionFactory conn) {
    connectionFactoryQ = conn;
  }

  protected String getConnectionFactoryQName() {
    return "connectionFactoryQ";
  }

  //////////////////////////////////////////////////////////////////////

  protected Topic getTopic() {
    return topic;
  }

  @Resource(name = "topic", description = "<resource-env-ref>")
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

  @Resource(name = "queue", description = "<resource-env-ref>")
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

  protected String getOrbName() {
    return "myOrb";
  }

  @Resource(name = "myOrb", type = ORB.class, description = "corba orb", shareable = false)
  private void setOrb(ORB orb) {
    this.orb = orb;
  }

  protected ORB getOrb() {
    return this.orb;
  }

}
