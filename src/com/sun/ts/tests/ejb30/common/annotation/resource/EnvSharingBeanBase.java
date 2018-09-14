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

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBContext;
import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnectionFactory;
import javax.mail.Session;
import javax.naming.NamingException;
import javax.sql.DataSource;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.PREFIX;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.USER_TRANSACTION_JNDI_NAME;
import static com.sun.ts.tests.ejb30.common.annotation.resource.Constants.ORB_JNDI_NAME;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.transaction.UserTransaction;
import org.omg.CORBA.ORB;

abstract public class EnvSharingBeanBase extends ResourceBeanBase
    implements ResourceIF {

  public static final int LOOKUP_TIMES = 100;

  /////////////////////////////////////////////////////////////////////////
  // business methods
  /////////////////////////////////////////////////////////////////////////

  public void testUrl() throws TestFailedException {
    List list = new ArrayList();
    URL url1 = getUrl();
    verify(url1, "getUrl()", list);

    for (int i = 0; i < LOOKUP_TIMES; i++) {
      URL url2 = (URL) getEJBContext().lookup(getUrlName());
      verify(url2, "EJBContext.lookup " + getUrlName(), list);
    }

    for (int i = 0; i < LOOKUP_TIMES; i++) {
      try {
        URL url3 = (URL) ServiceLocator.lookup(PREFIX + getUrlName());
        verify(url3, "JNDI lookup " + getUrlName(), list);
      } catch (NamingException e) {
        throw new TestFailedException(e);
      }
    }
  }

  public void testMailSession() throws TestFailedException {
    List list = new ArrayList();
    Session session1 = getMailSession();
    verify(session1, "getMailSession()", list);
    session1 = null;

    for (int i = 0; i < LOOKUP_TIMES; i++) {
      Session session2 = (Session) getEJBContext().lookup(getMailSessionName());
      verify(session2, "EJBContext.lookup" + getMailSessionName(), list);
    }

    for (int i = 0; i < LOOKUP_TIMES; i++) {
      try {
        Session session3 = (Session) ServiceLocator
            .lookup(PREFIX + getMailSessionName());
        verify(session3, "Naming Context lookup" + getMailSessionName(), list);
      } catch (NamingException e) {
        throw new TestFailedException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////

  protected void verify(Object o, String description, List list)
      throws TestFailedException {
    if (o == null) {
      throw new TestFailedException("Lookup returned null");
    }
    if (description == null) {
      description = "Lookup";
    }
    int n = list.size();
    for (int i = 0; i < n; i++) {
      if (list.get(i) == o) {
        throw new TestFailedException(
            description + " returned the same instance as list[" + i + "]:" + o
                + " in class " + this);
      }
    }
    list.add(o);
  }

  //////////////////////////////////////////////////////////////////////

  public void remove() {
  }

  protected String getOrbName() {
    return null;
  }

  protected ORB getOrb() {
    return null;
  }

  protected String getDataSourceName() {
    return null;
  }

  protected String getDataSource2Name() {
    return null;
  }

  protected DataSource getDataSource2() {
    return null;
  }

  protected DataSource getDataSource() {
    return null;
  }

  protected String getConnectionFactoryTName() {
    return null;
  }

  protected ConnectionFactory getConnectionFactoryT() {
    return null;
  }

  protected String getConnectionFactoryQName() {
    return null;
  }

  protected ConnectionFactory getConnectionFactoryQ() {
    return null;
  }

  protected Queue getQueue() {
    return null;
  }

  protected QueueConnectionFactory getQueueConnectionFactory() {
    return null;
  }

  protected String getQueueConnectionFactoryName() {
    return null;
  }

  protected String getQueueName() {
    return null;
  }

  protected Topic getTopic() {
    return null;
  }

  protected TopicConnectionFactory getTopicConnectionFactory() {
    return null;
  }

  protected String getTopicConnectionFactoryName() {
    return null;
  }

  protected String getTopicName() {
    return null;
  }

  protected UserTransaction getUserTransaction() {
    return null;
  }

  protected String getUserTransactionName() {
    return null;
  }
}
