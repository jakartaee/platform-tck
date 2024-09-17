/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.ClientBase;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

import jakarta.ejb.NoSuchEJBException;

public class Client extends ClientBase {
  // @EJB(name="removeBean")
  private static RemoveIF removeBean;

  // @EJB(name="removeBean2")
  private static Remove2IF removeBean2;

  // @EJB(name="removeNotRetainBean")
  private static RemoveNotRetainIF removeNotRetainBean;

  // @EJB(name="testBean")
  private static TestIF testBean;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  protected TestIF getTestBean() {
    return testBean;
  }

  protected RemoveIF getRemoveBean() {
    return removeBean;
  }

  protected Remove2IF getRemoveBean2() {
    return removeBean2;
  }

  protected RemoveNotRetainIF getRemoveNotRetainBean() {
    return removeNotRetainBean;
  }

  /*
   * @testName: removeBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: removeBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: testBeanremoveBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: testBeanremoveBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: retainBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: retainBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: testBeanretainBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: testBeanretainBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: removeNotRetainBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: removeNotRetainBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: alwaysRemoveAfterSystemException
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: a bean must always be removed after a system exception,
   * even though the remove method is retainIfException true.
   *
   */

  /*
   * @testName: testBeanremoveBeanRemote
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF.
   *
   */
  /*
   * @testName: testBeanremoveBean2Remote
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF2.
   *
   */
  /*
   * @testName: testBeanretainBeanRemote
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF.
   *
   */
  /*
   * @testName: testBeanretainBean2Remote
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF2.
   *
   */

  /*
   * @testName: removeNotRetainBeanOverloadedRemoveMethod
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  public void removeNotRetainBeanOverloadedRemoveMethod() throws Exception {
    try {
      removeNotRetainBean.remove(true);
    } catch (TestFailedException e) {
      TLogger.log("Got expected exception, and bean should not be removed"
          + " despite the exception" + e);
    }
    try {
      removeNotRetainBean.hi();
      TLogger.log("Ok, the bean is still there as expected.");
    } catch (NoSuchEJBException e) {
      throw new Exception("The bean was unexpectedly removed, and hi() results "
          + "in this exception: ", e);
    } finally {
      try {
        removeNotRetainBean.remove();
      } catch (Exception e) {
        // ignore the TestFailedException from remove().
      }
      removeNotRetainBean = null;
    }
  }

  /*
   * @testName: removeNotRetainBeanOverloadedRemove2Method
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  public void removeNotRetainBeanOverloadedRemove2Method() throws Exception {
    try {
      removeNotRetainBean.remove2(true, true);
    } catch (Exception e) {
      TLogger.log("Got expected exception, and bean should not be removed"
          + " despite the exception" + e);
    }
    try {
      removeNotRetainBean.hi();
      TLogger.log("Ok, the bean is still there as expected.");
    } catch (NoSuchEJBException e) {
      throw new Exception("The bean was unexpectedly removed, and hi() results "
          + "in this exception: ", e);
    } finally {
      try {
        removeNotRetainBean.remove();
      } catch (Exception e) {
        // ignore the TestFailedException from remove().
      }
      removeNotRetainBean = null;
    }
  }

  /*
   * @testName: retainBeanOverloaded
   * 
   * @test_Strategy:
   *
   */
  @Override
  public void retainBeanOverloaded() throws Exception {
    removeBean.remove("This is a remove-method.  The bean should be removed.");
    try {
      removeBean.remove();
      throw new Exception("Expecting NoSuchEJBException, but got no exception.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected NoSuchEJBException " + e);
    } finally {
      removeBean = null;
    }
  }

  /*
   * @testName: testBeanretainBeanOverloaded
   * 
   * @test_Strategy:
   *
   */
  @Override
  public void testBeanretainBeanOverloaded() throws Exception {
    try {
      testBean.retainBeanOverloaded();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

}
