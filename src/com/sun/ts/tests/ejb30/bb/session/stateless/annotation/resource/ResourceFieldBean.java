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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource;

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceBeanBase;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF;
import java.net.URL;
import javax.annotation.Resource.AuthenticationType;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.QueueConnectionFactory;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.TopicConnectionFactory;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.transaction.TransactionSynchronizationRegistry;
import org.omg.CORBA.ORB;

@Stateless(name = "ResourceFieldBean")
@Remote({ ResourceIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class ResourceFieldBean extends ResourceBeanBase implements ResourceIF {
  @Resource(name = "dog", description = "inject a custom jndi resource")
  private Dog dog;

  @Override
  protected Object getCustomeResource() {
    return dog;
  }

  @Override
  protected String getCustomeResourceName() {
    return "dog2";
  } // as defined in ejb-jar.xml

  @Resource(name = "session", description = "session context", type = SessionContext.class)
  private SessionContext sessionContext;

  @Resource(description = "user transaction", name = "myUserTransaction", type = UserTransaction.class)
  private UserTransaction ut;

  protected String getUserTransactionName() {
    return "myUserTransaction";
  }

  @Resource(name = "dataSource", description = "<resource-ref>")
  private DataSource dataSource;

  protected String getDataSourceName() {
    return "dataSource";
  }

  @Resource(name = "myDataSource2", type = DataSource.class, shareable = true, authenticationType = AuthenticationType.CONTAINER)
  private DataSource dataSource2;

  protected String getDataSource2Name() {
    return "myDataSource2";
  }

  @Resource(name = "mailSession")
  private javax.mail.Session mailSession;

  protected String getMailSessionName() {
    return "mailSession";
  }

  @Resource(name = "url")
  private URL url;

  protected String getUrlName() {
    return "url";
  }

  @Resource(name = "queueConnectionFactory")
  private QueueConnectionFactory queueConnectionFactory;

  protected String getQueueConnectionFactoryName() {
    return "queueConnectionFactory";
  }

  @Resource(name = "topicConnectionFactory")
  private TopicConnectionFactory topicConnectionFactory;

  protected String getTopicConnectionFactoryName() {
    return "topicConnectionFactory";
  }

  @Resource(name = "connectionFactoryQ")
  private ConnectionFactory connectionFactoryQ;

  protected String getConnectionFactoryQName() {
    return "connectionFactoryQ";
  }

  protected ConnectionFactory getConnectionFactoryQ() {
    return connectionFactoryQ;
  }

  @Resource(name = "connectionFactoryT")
  private ConnectionFactory connectionFactoryT;

  protected String getConnectionFactoryTName() {
    return "connectionFactoryT";
  }

  protected ConnectionFactory getConnectionFactoryT() {
    return connectionFactoryT;
  }

  @Resource(name = "destinationQ", type = Queue.class)
  private Destination destinationQ;

  protected String getDestinationQName() {
    return "destinationQ";
  }

  protected Destination getDestinationQ() {
    return destinationQ;
  }

  @Resource(name = "destinationT", type = Topic.class)
  private Destination destinationT;

  protected String getDestinationTName() {
    return "destinationT";
  }

  protected Destination getDestinationT() {
    return destinationT;
  }

  @Resource(name = "topic")
  private Topic topic;

  protected String getTopicName() {
    return "topic";
  }

  @Resource(name = "queue")
  private Queue queue;

  protected String getQueueName() {
    return "queue";
  }

  @Resource(name = "myOrb", type = ORB.class, description = "corba orb", shareable = false)
  private Object orb;

  protected String getOrbName() {
    return "myOrb";
  }

  @Resource(name = "myTransactionSynchronizationRegistry", description = "TransactionSynchronizationRegistry injected")
  private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

  protected String getTransactionSynchronizationRegistryName() {
    return "myTransactionSynchronizationRegistry";
  }

  @Resource(name = "myTimerService", description = "TimerService injected")
  private TimerService TimerService;

  protected String getTimerServiceName() {
    return "myTimerService";
  }

  public ResourceFieldBean() {
  }

  public void remove() {
  }

  protected javax.ejb.EJBContext getEJBContext() {
    return sessionContext;
  }

  protected DataSource getDataSource() {
    return dataSource;
  }

  protected DataSource getDataSource2() {
    return dataSource2;
  }

  protected javax.mail.Session getMailSession() {
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
    return ut;
  }

  protected ORB getOrb() {
    return (ORB) this.orb;
  }

  protected TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
    return transactionSynchronizationRegistry;
  }

  protected TimerService getTimerService() {
    return TimerService;
  }

}
