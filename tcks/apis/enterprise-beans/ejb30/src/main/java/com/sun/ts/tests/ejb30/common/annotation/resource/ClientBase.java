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

package com.sun.ts.tests.ejb30.common.annotation.resource;

import java.util.Properties;

import javax.naming.NamingException;

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

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
  public void setup(String[] args, Properties p) throws Exception {
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

  public void cleanup() throws Exception {
  }

  /*
   * testName: ejbContextTest
   * 
   * @test_Strategy:
   *
   */
  public void ejbContextTest() throws Exception {
    try {
      getResourceFieldBean().testEJBContext();
      getResourceSetterBean().testEJBContext();
      getResourceTypeBean().testEJBContext();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: dataSourceTest
   * 
   * @test_Strategy:
   *
   */
  public void dataSourceTest() throws Exception {
    try {
      getResourceFieldBean().testDataSource();
      getResourceSetterBean().testDataSource();
      getResourceTypeBean().testDataSource();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: dataSource2Test
   * 
   * @test_Strategy:
   *
   */
  public void dataSource2Test() throws Exception {
    try {
      getResourceFieldBean().testDataSource2();
      getResourceSetterBean().testDataSource2();
      getResourceTypeBean().testDataSource2();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: urlTest
   * 
   * @test_Strategy:
   *
   */
  public void urlTest() throws Exception {
    try {
      getResourceFieldBean().testUrl();
      getResourceSetterBean().testUrl();
      getResourceTypeBean().testUrl();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: queueConnectionFactoryTest
   * 
   * @test_Strategy:
   *
   */
  public void queueConnectionFactoryTest() throws Exception {
    try {
      getResourceFieldBean().testQueueConnectionFactory();
      getResourceSetterBean().testQueueConnectionFactory();
      getResourceTypeBean().testQueueConnectionFactory();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: topicConnectionFactoryTest
   * 
   * @test_Strategy:
   *
   */
  public void topicConnectionFactoryTest() throws Exception {
    try {
      getResourceFieldBean().testTopicConnectionFactory();
      getResourceSetterBean().testTopicConnectionFactory();
      getResourceTypeBean().testTopicConnectionFactory();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: connectionFactoryQTest
   * 
   * @test_Strategy:
   *
   */
  public void connectionFactoryQTest() throws Exception {
    try {
      getResourceFieldBean().testConnectionFactoryQ();
      getResourceSetterBean().testConnectionFactoryQ();
      getResourceTypeBean().testConnectionFactoryQ();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: connectionFactoryTTest
   * 
   * @test_Strategy:
   *
   */
  public void connectionFactoryTTest() throws Exception {
    try {
      getResourceFieldBean().testConnectionFactoryT();
      getResourceSetterBean().testConnectionFactoryT();
      getResourceTypeBean().testConnectionFactoryT();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
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
  public void queueTest() throws Exception {
    try {
      getResourceFieldBean().testQueue();
      getResourceSetterBean().testQueue();
      getResourceTypeBean().testQueue();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: topicTest
   * 
   * @test_Strategy:
   *
   */
  public void topicTest() throws Exception {
    try {
      getResourceFieldBean().testTopic();
      getResourceSetterBean().testTopic();
      getResourceTypeBean().testTopic();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: userTransactionTest
   * 
   * @test_Strategy:
   *
   */
  public void userTransactionTest() throws Exception {
    try {
      getResourceFieldBean().testUserTransaction();
      getResourceSetterBean().testUserTransaction();
      getResourceTypeBean().testUserTransaction();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: userTransactionLookupNegativeTest
   * 
   * @test_Strategy:
   *
   */
  public void userTransactionLookupNegativeTest() throws Exception {
    try {
      getUserTransactionNegativeBean().testUserTransactionNegativeLookup();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: customResourceInjectionInAppclient
   * 
   * @test_Strategy:
   *
   */
  public void customResourceInjectionInAppclient() throws Exception {
    Object dog = getCustomeResource();
    if (dog == null) {
      throw new Exception(
          "The custom resource dog has not been injected into the appclient main class.");
    } else if (dog instanceof Dog) {
      TLogger.log(
          "Good. The custom resource dog has been injected into the appclient main class:"
              + dog);
    } else {
      throw new Exception(
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
  public void customResourceLookupInAppclient() throws Exception {
    if (getCustomeResourceName() == null) {
      throw new Exception(
          "getCustomeResourceName() returned null.  Make sure this method is implemented"
              + " in subclass.");
    }
    String lookupName = "java:comp/env/" + getCustomeResourceName();
    Object obj = null;
    try {
      obj = ServiceLocator.lookup(lookupName);
    } catch (NamingException ex) {
      throw new Exception(
          "Failed to lookup custom resource with name:" + lookupName, ex);
    }
    if (obj instanceof Dog) {
      TLogger.log("Good. successfully looked up custom resource:" + obj);
    } else {
      throw new Exception("Lookup succeeded, but the result is not instanceof Dog: "
          + obj + ".  Its type is " + obj.getClass());
    }
  }

  /*
   * testName: customResourceInjectionInEjb
   * 
   * @test_Strategy:
   *
   */
  public void customResourceInjectionInEjb() throws Exception {
    try {
      getResourceFieldBean().testCustomResourceInjected();
      getResourceSetterBean().testCustomResourceInjected();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: customResourceLookupInEjb
   * 
   * @test_Strategy:
   *
   */
  public void customResourceLookupInEjb() throws Exception {
    try {
      getResourceFieldBean().testCustomResourceLookup();
      getResourceSetterBean().testCustomResourceLookup();
      getResourceTypeBean().testCustomResourceLookup();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: TransactionSynchronizationRegistryInjection
   * 
   * @test_Strategy:
   *
   */
  public void TransactionSynchronizationRegistryInjection() throws Exception {
    try {
      getResourceFieldBean().testTransactionSynchronizationRegistryInjected();
      getResourceSetterBean().testTransactionSynchronizationRegistryInjected();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: TransactionSynchronizationRegistryLookup
   * 
   * @test_Strategy:
   *
   */
  public void TransactionSynchronizationRegistryLookup() throws Exception {
    try {
      getResourceFieldBean().testTransactionSynchronizationRegistryLookup();
      getResourceSetterBean().testTransactionSynchronizationRegistryLookup();
      getResourceTypeBean().testTransactionSynchronizationRegistryLookup();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: TimerServiceInjection
   * 
   * @test_Strategy:
   *
   */
  public void TimerServiceInjection() throws Exception {
    try {
      getResourceFieldBean().testTimerServiceInjected();
      getResourceSetterBean().testTimerServiceInjected();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: TimerServiceLookup
   * 
   * @test_Strategy:
   *
   */
  public void TimerServiceLookup() throws Exception {
    try {
      getResourceFieldBean().testTimerServiceLookup();
      getResourceSetterBean().testTimerServiceLookup();
      getResourceTypeBean().testTimerServiceLookup();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }
}
