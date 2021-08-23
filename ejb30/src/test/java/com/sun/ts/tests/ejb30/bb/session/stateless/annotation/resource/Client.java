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

import com.sun.javatest.Status;
import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.tests.ejb30.common.annotation.resource.ClientBase;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF;
import com.sun.ts.tests.ejb30.common.annotation.resource.UserTransactionNegativeIF;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;

public class Client extends ClientBase {

  @EJB(beanName = "ResourceSetterBean")
  private static ResourceIF resourceSetterBean;

  @EJB(beanName = "ResourceFieldBean")
  private static ResourceIF resourceFieldBean;

  @EJB(beanName = "ResourceTypeBean")
  private static ResourceIF resourceTypeBean;

  @EJB(beanName = "UserTransactionNegativeBean")
  private static UserTransactionNegativeIF userTransactionNegativeBean;

  @Resource(name = "dog", description = "inject a custom jndi resource")
  private static Dog dog;

  protected ResourceIF getResourceSetterBean() {
    return resourceSetterBean;
  }

  protected ResourceIF getResourceFieldBean() {
    return resourceFieldBean;
  }

  protected ResourceIF getResourceTypeBean() {
    return resourceTypeBean;
  }

  protected UserTransactionNegativeIF getUserTransactionNegativeBean() {
    return userTransactionNegativeBean;
  }

  protected Object getCustomeResource() {
    return dog;
  }

  protected String getCustomeResourceName() {
    return "dog2";
  } // as defined in application-client.xml

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: ejbContextTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: dataSourceTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: dataSource2Test
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: urlTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: mailSessionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: queueConnectionFactoryTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: topicConnectionFactoryTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: connectionFactoryQTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: connectionFactoryTTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: userTransactionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: userTransactionLookupNegativeTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: topicTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: queueTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: customResourceInjectionInAppclient
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: customResourceLookupInAppclient
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: customResourceInjectionInEjb
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: customResourceLookupInEjb
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: TransactionSynchronizationRegistryInjection
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: TransactionSynchronizationRegistryLookup
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: TimerServiceInjection
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: TimerServiceLookup
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: destinationQTest
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: destinationTTest
   * 
   * @test_Strategy:
   *
   */
}
