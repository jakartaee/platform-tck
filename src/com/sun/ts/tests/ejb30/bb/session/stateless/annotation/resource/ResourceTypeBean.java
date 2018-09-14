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
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import java.net.URL;
import javax.annotation.Resource.AuthenticationType;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnectionFactory;
import javax.jms.ConnectionFactory;
import javax.mail.Session;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import javax.jms.Queue;
import javax.jms.Topic;
import org.omg.CORBA.ORB;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.ORB_JNDI_NAME;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.TIMER_SERVICE_JNDI_NAME;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.TRANSACTION_SYNCHRONIZATION_REGISTRY_JNDI_NAME;
import javax.ejb.TimerService;
import javax.jms.Destination;
import javax.transaction.TransactionSynchronizationRegistry;

@Stateless(name = "ResourceTypeBean")
@Remote({ ResourceIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
@Resources({
    @Resource(description = "user transaction", name = "myUserTransaction", type = UserTransaction.class),
    @Resource(name = "dataSource", type = DataSource.class, shareable = true, authenticationType = AuthenticationType.CONTAINER, description = "<resource-ref>"),
    @Resource(name = "myDataSource2", type = DataSource.class, authenticationType = AuthenticationType.CONTAINER),
    @Resource(name = "mailSession", type = Session.class),
    @Resource(name = "url", type = URL.class),
    @Resource(name = "queueConnectionFactory", type = QueueConnectionFactory.class),
    @Resource(name = "topicConnectionFactory", type = TopicConnectionFactory.class),
    @Resource(name = "connectionFactoryQ", type = ConnectionFactory.class),
    @Resource(name = "destinationQ", type = Queue.class),
    @Resource(name = "destinationT", type = Topic.class),
    @Resource(name = "connectionFactoryT", type = ConnectionFactory.class),
    @Resource(name = "queue", type = Queue.class),
    @Resource(name = "topic", type = Topic.class),
    @Resource(name = "myOrb", type = ORB.class, description = "corba orb", shareable = false),
    @Resource(name = "myTransactionSynchronizationRegistry", type = TransactionSynchronizationRegistry.class, description = "TransactionSynchronizationRegistry type-level injection"),
    @Resource(name = "myTimerService", type = TimerService.class, description = "TimerService type-level injection"),
    @Resource(name = "dog", type = Dog.class, description = "a custom resouce") })

public class ResourceTypeBean extends ResourceBeanBase implements ResourceIF {

  @Resource(name = "sessionContext", description = "session context", type = SessionContext.class)
  private SessionContext sessionContext;

  protected String getUserTransactionName() {
    return "myUserTransaction";
  }

  protected String getDataSourceName() {
    return "dataSource";
  }

  protected String getDataSource2Name() {
    return "myDataSource2";
  }

  protected String getMailSessionName() {
    return "mailSession";
  }

  protected String getUrlName() {
    return "url";
  }

  protected String getQueueConnectionFactoryName() {
    return "queueConnectionFactory";
  }

  protected String getTopicConnectionFactoryName() {
    return "topicConnectionFactory";
  }

  protected String getConnectionFactoryQName() {
    return "connectionFactoryQ";
  }

  protected String getConnectionFactoryTName() {
    return "connectionFactoryT";
  }

  protected String getDestinationQName() {
    return "destinationQ";
  }

  protected String getDestinationTName() {
    return "destinationT";
  }

  protected String getTopicName() {
    return "topic";
  }

  protected String getQueueName() {
    return "queue";
  }

  protected String getOrbName() {
    return "myOrb";
  }

  protected String getTransactionSynchronizationRegistryName() {
    return "myTransactionSynchronizationRegistry";
  }

  protected String getTimerServiceName() {
    return "myTimerService";
  }

  protected String getCustomeResourceName() {
    return "dog";
  } // as defined in type-level injection

  public ResourceTypeBean() {
  }

  public void remove() {
  }

  protected javax.ejb.EJBContext getEJBContext() {
    return sessionContext;
  }

  protected DataSource getDataSource() {
    return (DataSource) getEJBContext().lookup(getDataSourceName());
  }

  protected DataSource getDataSource2() {
    return (DataSource) getEJBContext().lookup(getDataSource2Name());
  }

  protected javax.mail.Session getMailSession() {
    return (Session) getEJBContext().lookup(getMailSessionName());
  }

  protected URL getUrl() {
    return (URL) getEJBContext().lookup(getUrlName());
  }

  protected QueueConnectionFactory getQueueConnectionFactory() {
    return (QueueConnectionFactory) getEJBContext()
        .lookup(getQueueConnectionFactoryName());
  }

  protected TopicConnectionFactory getTopicConnectionFactory() {
    return (TopicConnectionFactory) getEJBContext()
        .lookup(getTopicConnectionFactoryName());
  }

  protected ConnectionFactory getConnectionFactoryQ() {
    return (ConnectionFactory) getEJBContext()
        .lookup(getConnectionFactoryQName());
  }

  protected Destination getDestinationQ() {
    return (Destination) getEJBContext().lookup(getDestinationQName());
  }

  protected ConnectionFactory getConnectionFactoryT() {
    return (ConnectionFactory) getEJBContext()
        .lookup(getConnectionFactoryTName());
  }

  protected Destination getDestinationT() {
    return (Destination) getEJBContext().lookup(getDestinationTName());
  }

  protected Queue getQueue() {
    return (Queue) getEJBContext().lookup(getQueueName());
  }

  protected Topic getTopic() {
    return (Topic) getEJBContext().lookup(getTopicName());
  }

  protected javax.transaction.UserTransaction getUserTransaction() {
    return (UserTransaction) getEJBContext().lookup(getUserTransactionName());
  }

  protected ORB getOrb() {
    try {
      return (ORB) ServiceLocator.lookup(ORB_JNDI_NAME);
    } catch (NamingException e) {
      e.printStackTrace();
    }
    return null;
  }

  protected TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
    try {
      return (TransactionSynchronizationRegistry) ServiceLocator
          .lookup(TRANSACTION_SYNCHRONIZATION_REGISTRY_JNDI_NAME);
    } catch (NamingException e) {
      e.printStackTrace();
    }
    return null;
  }

  protected TimerService getTimerService() {
    return getEJBContext().getTimerService();
  }

}
