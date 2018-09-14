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

package com.sun.ts.tests.ejb30.bb.session.stateless.busifacedd;

import com.sun.ts.tests.ejb30.common.busiface.BusinessIF1;
import com.sun.ts.tests.ejb30.common.busiface.BusinessIF2;
import com.sun.ts.tests.ejb30.common.busiface.TestIF;
import javax.ejb.EJB;
import com.sun.javatest.Status;

import com.sun.ts.tests.ejb30.common.busiface.ClientBase;

public class Client extends ClientBase {
  @EJB
  private static BusinessIF1 bean1;

  @EJB
  private static BusinessIF2 bean2;

  @EJB
  private static TestIF testBean;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  protected TestIF getTestBean() {
    return testBean;
  }

  protected BusinessIF2 getBean2() {
    return bean2;
  }

  protected BusinessIF1 getBean1() {
    return bean1;
  }

  /*
   * @testName: multipleInterfacesTest1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: multipleInterfacesTest2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: multipleInterfacesLocalTest1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: multipleInterfacesLocalTest2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: singleInterfaceLocalSerializableTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: singleInterfaceLocalExternalizableTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: singleInterfaceLocalTimedObjectTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: singleInterfaceLocalSessionBeanTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
}
