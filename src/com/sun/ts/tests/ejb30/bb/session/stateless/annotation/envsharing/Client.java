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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing;

import com.sun.ts.tests.ejb30.common.annotation.resource.ClientBase;
import com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF;
import javax.ejb.EJB;
import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.annotation.resource.UserTransactionNegativeIF;

/**
 * The tested requirements are in Java EE Platform Specification section
 * EE.5.2.2 Sharing of Environment Entries This is about the sharing and
 * non-sharing of resource connection factory objects, not resource connections.
 * Although some annotations have shareable attribute, as
 * in @Resource(name="mailSession", shareable= false), it value (true or false)
 * should not affect the test result.
 */
public class Client extends ClientBase {
  @EJB(beanName = "ResourceSetterBean")
  private static ResourceIF resourceSetterBean;

  @EJB(beanName = "ResourceFieldBean")
  private static ResourceIF resourceFieldBean;

  @EJB(beanName = "ResourceTypeBean")
  private static ResourceIF resourceTypeBean;

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

}
