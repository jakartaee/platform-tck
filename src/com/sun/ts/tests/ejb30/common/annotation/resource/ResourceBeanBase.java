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

package com.sun.ts.tests.ejb30.common.annotation.resource;

import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.ORB_JNDI_NAME;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.PREFIX;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.TIMER_SERVICE_JNDI_NAME;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.TRANSACTION_SYNCHRONIZATION_REGISTRY_JNDI_NAME;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.USER_TRANSACTION_JNDI_NAME;

import java.net.URL;

import javax.ejb.EJBContext;
import javax.ejb.TimerService;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.mail.Session;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import org.omg.CORBA.ORB;

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

abstract public class ResourceBeanBase implements ResourceIF {
  abstract protected EJBContext getEJBContext();

  abstract protected UserTransaction getUserTransaction();

  abstract protected String getUserTransactionName();

  abstract protected DataSource getDataSource();

  abstract protected String getDataSourceName();

  abstract protected DataSource getDataSource2();

  abstract protected String getDataSource2Name();

  abstract protected javax.mail.Session getMailSession();

  abstract protected String getMailSessionName();

  abstract protected URL getUrl();

  abstract protected String getUrlName();

  abstract protected QueueConnectionFactory getQueueConnectionFactory();

  abstract protected String getQueueConnectionFactoryName();

  abstract protected TopicConnectionFactory getTopicConnectionFactory();

  abstract protected String getTopicConnectionFactoryName();

  abstract protected ConnectionFactory getConnectionFactoryQ();

  abstract protected String getConnectionFactoryQName();

  abstract protected ConnectionFactory getConnectionFactoryT();

  abstract protected String getConnectionFactoryTName();

  abstract protected Queue getQueue();

  abstract protected String getQueueName();

  abstract protected Topic getTopic();

  abstract protected String getTopicName();

  abstract protected ORB getOrb();

  abstract protected String getOrbName();

  protected TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
    return null;
  }

  protected String getTransactionSynchronizationRegistryName() {
    return null;
  }

  protected TimerService getTimerService() {
    return null;
  }

  protected String getTimerServiceName() {
    return null;
  }

  protected Object getCustomeResource() {
    return null;
  }

  protected String getCustomeResourceName() {
    return null;
  }

  protected Destination getDestinationQ() {
    return null;
  }

  protected String getDestinationQName() {
    return null;
  }

  protected Destination getDestinationT() {
    return null;
  }

  protected String getDestinationTName() {
    return null;
  }

  /////////////////////////////////////////////////////////////////////////
  // business methods
  /////////////////////////////////////////////////////////////////////////

  public void testUrl() throws TestFailedException {
    URL url1 = getUrl();
    verify(url1, "getUrl()");
    url1 = null;

    URL url2 = (URL) getEJBContext().lookup(getUrlName());
    verify(url2, "EJBContext.lookup " + getUrlName());
    url2 = null;

    try {
      URL url3 = (URL) ServiceLocator.lookup(PREFIX + getUrlName());
      verify(url3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testTopicConnectionFactory() throws TestFailedException {
    TopicConnectionFactory topic1 = getTopicConnectionFactory();
    verify(topic1, "getTopicConnectionFactory()");
    try {
      TopicConnection tConn = topic1.createTopicConnection();
      tConn.close();
      TLogger.log("Got TopicConnection from TopicConnectionFactory: " + tConn);
    } catch (JMSException e1) {
      throw new TestFailedException(e1);
    }
    topic1 = null;

    TopicConnectionFactory topic2 = (TopicConnectionFactory) getEJBContext()
        .lookup(getTopicConnectionFactoryName());
    verify(topic2, "EJBContext.lookup" + getTopicConnectionFactoryName());

    try {
      TopicConnectionFactory topic3 = (TopicConnectionFactory) ServiceLocator
          .lookup(PREFIX + getTopicConnectionFactoryName());
      verify(topic3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testQueueConnectionFactory() throws TestFailedException {
    QueueConnectionFactory queue1 = getQueueConnectionFactory();
    verify(queue1, "getQueueConnectionFactory()");
    try {
      QueueConnection qConn = queue1.createQueueConnection();
      qConn.close();
      TLogger.log("Got QueueConnection from QueueConnectionFactory: " + qConn);
    } catch (JMSException e1) {
      throw new TestFailedException(e1);
    }
    queue1 = null;

    QueueConnectionFactory queue2 = (QueueConnectionFactory) getEJBContext()
        .lookup(getQueueConnectionFactoryName());
    verify(queue2, "EJBContext.lookup" + getQueueConnectionFactoryName());

    try {
      QueueConnectionFactory queue3 = (QueueConnectionFactory) ServiceLocator
          .lookup(PREFIX + getQueueConnectionFactoryName());
      verify(queue3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testConnectionFactoryT() throws TestFailedException {
    ConnectionFactory topic1 = getConnectionFactoryT();
    verify(topic1, "getConnectionFactoryT()");
    topic1 = null;

    ConnectionFactory topic2 = (ConnectionFactory) getEJBContext()
        .lookup(getConnectionFactoryTName());
    verify(topic2, "EJBContext.lookup" + getConnectionFactoryTName());

    try {
      ConnectionFactory topic3 = (ConnectionFactory) ServiceLocator
          .lookup(PREFIX + getConnectionFactoryTName());
      verify(topic3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testConnectionFactoryQ() throws TestFailedException {
    ConnectionFactory queue1 = getConnectionFactoryQ();
    verify(queue1, "getConnectionFactoryQ()");
    queue1 = null;

    ConnectionFactory queue2 = (ConnectionFactory) getEJBContext()
        .lookup(getConnectionFactoryQName());
    verify(queue2, "EJBContext.lookup" + getConnectionFactoryQName());

    try {
      ConnectionFactory queue3 = (ConnectionFactory) ServiceLocator
          .lookup(PREFIX + getConnectionFactoryQName());
      verify(queue3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testDestinationQ() throws TestFailedException {
    Destination dest1 = getDestinationQ();
    verify(dest1, "getDestinationQ()");
    dest1 = null;

    Destination dest2 = (Destination) getEJBContext()
        .lookup(getDestinationQName());
    verify(dest2, "EJBContext.lookup" + getDestinationQName());

    try {
      Destination dest3 = (Destination) ServiceLocator
          .lookup(PREFIX + getDestinationQName());
      verify(dest3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testDestinationT() throws TestFailedException {
    Destination dest1 = getDestinationT();
    verify(dest1, "getDestinationT()");
    dest1 = null;

    Destination dest2 = (Destination) getEJBContext()
        .lookup(getDestinationTName());
    verify(dest2, "EJBContext.lookup" + getDestinationTName());

    try {
      Destination dest3 = (Destination) ServiceLocator
          .lookup(PREFIX + getDestinationTName());
      verify(dest3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testQueue() throws TestFailedException {
    Queue queue1 = getQueue();
    verify(queue1, "getQueue()");
    queue1 = null;

    Queue queue2 = (Queue) getEJBContext().lookup(getQueueName());
    verify(queue2, "EJBContext.lookup" + getQueueName());

    try {
      Queue queue3 = (Queue) ServiceLocator.lookup(PREFIX + getQueueName());
      verify(queue3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testTopic() throws TestFailedException {
    Topic topic1 = getTopic();
    verify(topic1, "getTopic()");
    topic1 = null;

    Topic topic2 = (Topic) getEJBContext().lookup(getTopicName());
    verify(topic2, "EJBContext.lookup" + getTopicName());

    try {
      Topic topic3 = (Topic) ServiceLocator.lookup(PREFIX + getTopicName());
      verify(topic3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testMailSession() throws TestFailedException {
    Session session1 = getMailSession();
    verify(session1, "getMailSession()");
    session1 = null;

    Session session2 = (Session) getEJBContext().lookup(getMailSessionName());
    verify(session2, "EJBContext.lookup" + getMailSessionName());

    try {
      Session session3 = (Session) ServiceLocator
          .lookup(PREFIX + getMailSessionName());
      verify(session3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testEJBContext() throws TestFailedException {
    if (getEJBContext() == null) {
      throw new TestFailedException("getEJBContext{} returned null");
    }
  }

  public void testDataSource2() throws TestFailedException {
    DataSource ds1 = getDataSource2();
    verify(ds1, "getDataSource2()");
    ds1 = null;

    DataSource ds2 = (DataSource) getEJBContext().lookup(getDataSource2Name());
    verify(ds2, "EJBContext.lookup" + getDataSource2Name());

    try {
      DataSource ds3 = (DataSource) ServiceLocator
          .lookup(PREFIX + getDataSource2Name());
      verify(ds3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testDataSource() throws TestFailedException {
    DataSource ds1 = getDataSource();
    verify(ds1, "getDataSource()");
    ds1 = null;

    DataSource ds2 = (DataSource) getEJBContext().lookup(getDataSourceName());
    verify(ds2, "EJBContext.lookup" + getDataSourceName());

    try {
      DataSource ds3 = (DataSource) ServiceLocator
          .lookup(PREFIX + getDataSourceName());
      verify(ds3, "Naming Context lookup");
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testUserTransaction() throws TestFailedException {
    UserTransaction ut1 = getUserTransaction();
    verify(ut1, "getUserTransaction()");
    ut1 = null;

    UserTransaction ut2 = getEJBContext().getUserTransaction();
    verify(ut2, "EJBContext.getUserTransaction()");
    ut2 = null;

    try {
      UserTransaction ut3 = (UserTransaction) ServiceLocator
          .lookup(USER_TRANSACTION_JNDI_NAME);
      verify(ut3, "Naming context lookup " + USER_TRANSACTION_JNDI_NAME);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }

    // try {
    // UserTransaction ut4 = (UserTransaction)
    // ServiceLocator.lookup(PREFIX + getUserTransactionName());
    // verify(ut4, "Naming context lookup "+ PREFIX + getUserTransactionName());
    // } catch (NamingException e) {
    // throw new TestFailedException(e);
    // }
  }

  public void testOrb() throws TestFailedException {
    ORB orb1 = getOrb();
    verify(orb1, "getOrb()");
    orb1 = null;

    try {
      ORB orb3 = (ORB) ServiceLocator.lookup(ORB_JNDI_NAME);
      verify(orb3, "Naming context lookup " + ORB_JNDI_NAME);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }

    // try {
    // ORB orb4 = (ORB)
    // ServiceLocator.lookup(PREFIX + getOrbName());
    // verify(orb4, "Naming context lookup "+ PREFIX + getOrbName());
    // } catch (NamingException e) {
    // throw new TestFailedException(e);
    // }
  }

  public void testTransactionSynchronizationRegistryInjected()
      throws TestFailedException {
    TransactionSynchronizationRegistry t = getTransactionSynchronizationRegistry();
    verify(t, "getTransactionSynchronizationRegistry()");
    Object key = t.getTransactionKey();
    try {
      t = (TransactionSynchronizationRegistry) ServiceLocator
          .lookup(TRANSACTION_SYNCHRONIZATION_REGISTRY_JNDI_NAME);
      verify(t, "Naming context lookup "
          + TRANSACTION_SYNCHRONIZATION_REGISTRY_JNDI_NAME);
      key = t.getTransactionKey();
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testTransactionSynchronizationRegistryLookup()
      throws TestFailedException {
    try {
      TransactionSynchronizationRegistry t = (TransactionSynchronizationRegistry) ServiceLocator
          .lookup(PREFIX + getTransactionSynchronizationRegistryName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testTimerServiceInjected() throws TestFailedException {
    TimerService t = getTimerService();
    verify(t, "getTimerService()");
    try {
      t = (TimerService) ServiceLocator.lookup(TIMER_SERVICE_JNDI_NAME);
      verify(t, "Naming context lookup " + TIMER_SERVICE_JNDI_NAME);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testTimerServiceLookup() throws TestFailedException {
    try {
      String lookupName = PREFIX + getTimerServiceName();
      TimerService t = (TimerService) ServiceLocator.lookup(lookupName);
      verify(t, "Naming context lookup " + lookupName);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void testCustomResourceLookup() throws TestFailedException {
    Dog dog = (Dog) getEJBContext().lookup(getCustomeResourceName());
    if (Dog.getInstance().equals(dog)) {
      TLogger.log("Got expected result: ", dog.toString());
    } else {
      throw new TestFailedException(
          "Failed to lookup the expected value.  Expected result: "
              + Dog.getInstance().toString() + ", actual result: " + dog);
    }
  }

  public void testCustomResourceInjected() throws TestFailedException {
    Object obj = getCustomeResource();
    if (Dog.getInstance().equals(obj)) {
      TLogger.log("Correctly injected ", obj.toString());
    } else {
      throw new TestFailedException(
          "The expected value has not been injected.  Expected result: "
              + Dog.getInstance().toString() + ", actual result: " + obj);
    }
  }

  //////////////////////////////////////////////////////////////////////////

  protected void verify(Object obj, String description)
      throws TestFailedException {
    if (obj == null) {
      if (description == null) {
        description = "Obtaining resource";
      }
      throw new TestFailedException(description + " returned null: " + this);
    }
  }

}
