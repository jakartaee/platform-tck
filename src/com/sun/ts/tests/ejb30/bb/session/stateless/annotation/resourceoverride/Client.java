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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resourceoverride;

import com.sun.ts.tests.ejb30.common.annotation.resource.ClientBase;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceNoop;
import com.sun.ts.tests.ejb30.common.annotation.resource.UserTransactionNegativeIF;
import javax.ejb.EJB;
import com.sun.javatest.Status;

public class Client extends ClientBase {
  @EJB(beanName = "ResourceSetterBean")
  private static ResourceIF resourceSetterBean;

  @EJB(beanName = "ResourceFieldBean")
  private static ResourceIF resourceFieldBean;

  private ResourceIF resourceTypeBean = new ResourceNoop();

  // @EJB(beanName="ResourceTypeBean")
  // private static ResourceIF resourceTypeBean;

  // @EJB(beanName="UserTransactionNegativeBean")
  // private static UserTransactionNegativeIF userTransactionNegativeBean;

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
    return null;
  }

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
   * @todo how to declare it in dd? testName: userTransactionTest
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
   * @todo how to declare it in dd? testName: orbTest
   * 
   * @test_Strategy:
   * 
   */
}
