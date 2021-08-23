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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resourcenoat;

import java.net.URL;

import javax.sql.DataSource;

import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceBeanBase;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF;

import jakarta.annotation.Resource;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnectionFactory;
import jakarta.transaction.UserTransaction;

@Stateless(name = "ResourceFieldBean")
@Remote({ ResourceIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class ResourceFieldBean extends ResourceBeanBase implements ResourceIF {

  @Resource(name = "sessionContext", description = "session context", type = SessionContext.class)
  private SessionContext sessionContext;

  // @Resource(description="user transaction", name="myUserTransaction",
  // type=UserTransaction.class)
  // already declared in ejb-jar.xml and sun-ejb-jar.xml. This is to test
  // UserTransaction can be declared in descriptor as a <resource-env-ref>
  // glassfish issue 1121
  private UserTransaction userTransaction;

  protected String getUserTransactionName() {
    return "myUserTransaction";
  }

  // @Resource
  private DataSource dataSource;

  protected String getDataSourceName() {
    return "dataSource";
  }

  // @Resource(name="myDataSource2", type=DataSource.class, shareable=true,
  // authenticationType=AuthenticationType.CONTAINER)
  private DataSource dataSource2;

  protected String getDataSource2Name() {
    return "myDataSource2";
  }

  // @Resource
  private jakarta.mail.Session mailSession;

  protected String getMailSessionName() {
    return "mailSession";
  }

  // @Resource
  private URL url;

  protected String getUrlName() {
    return "url";
  }

  // @Resource
  private QueueConnectionFactory queueConnectionFactory;

  protected String getQueueConnectionFactoryName() {
    return "queueConnectionFactory";
  }

  // @Resource
  private TopicConnectionFactory topicConnectionFactory;

  protected String getTopicConnectionFactoryName() {
    return "topicConnectionFactory";
  }

  // @Resource
  private ConnectionFactory connectionFactoryQ;

  protected String getConnectionFactoryQName() {
    return "connectionFactoryQ";
  }

  protected ConnectionFactory getConnectionFactoryQ() {
    return connectionFactoryQ;
  }

  // @Resource
  private ConnectionFactory connectionFactoryT;

  protected String getConnectionFactoryTName() {
    return "connectionFactoryT";
  }

  protected ConnectionFactory getConnectionFactoryT() {
    return connectionFactoryT;
  }

  // @Resource
  private Topic topic;

  protected String getTopicName() {
    return "topic";
  }

  // @Resource
  private Queue queue;

  protected String getQueueName() {
    return "queue";
  }

  public ResourceFieldBean() {
  }

  public void remove() {
  }

  protected jakarta.ejb.EJBContext getEJBContext() {
    return sessionContext;
  }

  protected DataSource getDataSource() {
    return dataSource;
  }

  protected DataSource getDataSource2() {
    return dataSource2;
  }

  protected jakarta.mail.Session getMailSession() {
    return mailSession;
  }

  protected URL getUrl() {
    return url;
  }

  protected QueueConnectionFactory getQueueConnectionFactory() {
    return queueConnectionFactory;
  }

  protected Queue getQueue() {
    return queue;
  }

  protected TopicConnectionFactory getTopicConnectionFactory() {
    return topicConnectionFactory;
  }

  protected Topic getTopic() {
    return topic;
  }

  protected UserTransaction getUserTransaction() {
    return userTransaction;
  }

}
