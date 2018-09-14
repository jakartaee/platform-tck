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

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import org.omg.CORBA.ORB;

abstract public class ClientBase extends EETest {
  protected Properties props;

  // 2 remote beans directly accessed by client
  abstract protected ResourceIF getResourceSetterBean();

  abstract protected ResourceIF getResourceFieldBean();

  abstract protected ResourceIF getResourceTypeBean();

  abstract protected UserTransactionNegativeIF getUserTransactionNegativeBean();

  protected Object getCustomeResource() {
    return null;
  }

  protected String getCustomeResourceName() {
    return null;
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  /**
   * Removes all beans used in this client. It should only be used by sfsb,
   * though other bean types may also have a remove business method.
   */
  protected void remove() {
    if (getResourceSetterBean() != null) {
      try {
        getResourceSetterBean().remove();
        TLogger.log("ResourceSetterBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove ResourceSetterBean.");
      }
    }

    if (getResourceFieldBean() != null) {
      try {
        getResourceFieldBean().remove();
        TLogger.log("ResourceFieldBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove ResourceFieldBean.");
      }
    }
  }

  public void cleanup() throws Fault {
  }

  /*
   * testName: ejbContextTest
   * 
   * @test_Strategy:
   *
   */
  public void ejbContextTest() throws Fault {
    try {
      getResourceFieldBean().testEJBContext();
      getResourceSetterBean().testEJBContext();
      getResourceTypeBean().testEJBContext();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: dataSourceTest
   * 
   * @test_Strategy:
   *
   */
  public void dataSourceTest() throws Fault {
    try {
      getResourceFieldBean().testDataSource();
      getResourceSetterBean().testDataSource();
      getResourceTypeBean().testDataSource();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: dataSource2Test
   * 
   * @test_Strategy:
   *
   */
  public void dataSource2Test() throws Fault {
    try {
      getResourceFieldBean().testDataSource2();
      getResourceSetterBean().testDataSource2();
      getResourceTypeBean().testDataSource2();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: urlTest
   * 
   * @test_Strategy:
   *
   */
  public void urlTest() throws Fault {
    try {
      getResourceFieldBean().testUrl();
      getResourceSetterBean().testUrl();
      getResourceTypeBean().testUrl();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: mailSessionTest
   * 
   * @test_Strategy:
   *
   */
  public void mailSessionTest() throws Fault {
    try {
      getResourceFieldBean().testMailSession();
      getResourceSetterBean().testMailSession();
      getResourceTypeBean().testMailSession();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: queueConnectionFactoryTest
   * 
   * @test_Strategy:
   *
   */
  public void queueConnectionFactoryTest() throws Fault {
    try {
      getResourceFieldBean().testQueueConnectionFactory();
      getResourceSetterBean().testQueueConnectionFactory();
      getResourceTypeBean().testQueueConnectionFactory();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: topicConnectionFactoryTest
   * 
   * @test_Strategy:
   *
   */
  public void topicConnectionFactoryTest() throws Fault {
    try {
      getResourceFieldBean().testTopicConnectionFactory();
      getResourceSetterBean().testTopicConnectionFactory();
      getResourceTypeBean().testTopicConnectionFactory();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: connectionFactoryQTest
   * 
   * @test_Strategy:
   *
   */
  public void connectionFactoryQTest() throws Fault {
    try {
      getResourceFieldBean().testConnectionFactoryQ();
      getResourceSetterBean().testConnectionFactoryQ();
      getResourceTypeBean().testConnectionFactoryQ();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: connectionFactoryTTest
   * 
   * @test_Strategy:
   *
   */
  public void connectionFactoryTTest() throws Fault {
    try {
      getResourceFieldBean().testConnectionFactoryT();
      getResourceSetterBean().testConnectionFactoryT();
      getResourceTypeBean().testConnectionFactoryT();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: destinationQTest
   * 
   * @test_Strategy:
   *
   */
  public void destinationQTest() throws TestFailedException {
    getResourceFieldBean().testDestinationQ();
    getResourceSetterBean().testDestinationQ();
    getResourceTypeBean().testDestinationQ();
  }

  /*
   * testName: destinationTTest
   * 
   * @test_Strategy:
   *
   */
  public void destinationTTest() throws TestFailedException {
    getResourceFieldBean().testDestinationT();
    getResourceSetterBean().testDestinationT();
    getResourceTypeBean().testDestinationT();
  }

  /*
   * testName: queueTest
   * 
   * @test_Strategy:
   *
   */
  public void queueTest() throws Fault {
    try {
      getResourceFieldBean().testQueue();
      getResourceSetterBean().testQueue();
      getResourceTypeBean().testQueue();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: topicTest
   * 
   * @test_Strategy:
   *
   */
  public void topicTest() throws Fault {
    try {
      getResourceFieldBean().testTopic();
      getResourceSetterBean().testTopic();
      getResourceTypeBean().testTopic();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: userTransactionTest
   * 
   * @test_Strategy:
   *
   */
  public void userTransactionTest() throws Fault {
    try {
      getResourceFieldBean().testUserTransaction();
      getResourceSetterBean().testUserTransaction();
      getResourceTypeBean().testUserTransaction();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: userTransactionLookupNegativeTest
   * 
   * @test_Strategy:
   *
   */
  public void userTransactionLookupNegativeTest() throws Fault {
    try {
      getUserTransactionNegativeBean().testUserTransactionNegativeLookup();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: orbTest
   * 
   * @test_Strategy:
   *
   */
  public void orbTest() throws Fault {
    try {
      getResourceFieldBean().testOrb();
      getResourceSetterBean().testOrb();
      getResourceTypeBean().testOrb();
    } catch (TestFailedException e) {
      throw new Fault("Test Failed", e);
    }
  }

  /*
   * testName: customResourceInjectionInAppclient
   * 
   * @test_Strategy:
   *
   */
  public void customResourceInjectionInAppclient() throws Fault {
    Object dog = getCustomeResource();
    if (dog == null) {
      throw new Fault(
          "The custom resource dog has not been injected into the appclient main class.");
    } else if (dog instanceof Dog) {
      TLogger.log(
          "Good. The custom resource dog has been injected into the appclient main class:"
              + dog);
    } else {
      throw new Fault(
          "Injection succeeded, but the result is not instanceof Dog: " + dog
              + ".  Its type is " + dog.getClass());
    }
  }

  /*
   * testName: customResourceLookupInAppclient
   * 
   * @test_Strategy:
   *
   */
  public void customResourceLookupInAppclient() throws Fault {
    if (getCustomeResourceName() == null) {
      throw new Fault(
          "getCustomeResourceName() returned null.  Make sure this method is implemented"
              + " in subclass.");
    }
    String lookupName = "java:comp/env/" + getCustomeResourceName();
    Object obj = null;
    try {
      obj = ServiceLocator.lookup(lookupName);
    } catch (NamingException ex) {
      throw new Fault(
          "Failed to lookup custom resource with name:" + lookupName, ex);
    }
    if (obj instanceof Dog) {
      TLogger.log("Good. successfully looked up custom resource:" + obj);
    } else {
      throw new Fault("Lookup succeeded, but the result is not instanceof Dog: "
          + obj + ".  Its type is " + obj.getClass());
    }
  }

  /*
   * testName: customResourceInjectionInEjb
   * 
   * @test_Strategy:
   *
   */
  public void customResourceInjectionInEjb() throws Fault {
    try {
      getResourceFieldBean().testCustomResourceInjected();
      getResourceSetterBean().testCustomResourceInjected();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: customResourceLookupInEjb
   * 
   * @test_Strategy:
   *
   */
  public void customResourceLookupInEjb() throws Fault {
    try {
      getResourceFieldBean().testCustomResourceLookup();
      getResourceSetterBean().testCustomResourceLookup();
      getResourceTypeBean().testCustomResourceLookup();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: TransactionSynchronizationRegistryInjection
   * 
   * @test_Strategy:
   *
   */
  public void TransactionSynchronizationRegistryInjection() throws Fault {
    try {
      getResourceFieldBean().testTransactionSynchronizationRegistryInjected();
      getResourceSetterBean().testTransactionSynchronizationRegistryInjected();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: TransactionSynchronizationRegistryLookup
   * 
   * @test_Strategy:
   *
   */
  public void TransactionSynchronizationRegistryLookup() throws Fault {
    try {
      getResourceFieldBean().testTransactionSynchronizationRegistryLookup();
      getResourceSetterBean().testTransactionSynchronizationRegistryLookup();
      getResourceTypeBean().testTransactionSynchronizationRegistryLookup();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: TimerServiceInjection
   * 
   * @test_Strategy:
   *
   */
  public void TimerServiceInjection() throws Fault {
    try {
      getResourceFieldBean().testTimerServiceInjected();
      getResourceSetterBean().testTimerServiceInjected();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: TimerServiceLookup
   * 
   * @test_Strategy:
   *
   */
  public void TimerServiceLookup() throws Fault {
    try {
      getResourceFieldBean().testTimerServiceLookup();
      getResourceSetterBean().testTimerServiceLookup();
      getResourceTypeBean().testTimerServiceLookup();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }
}
