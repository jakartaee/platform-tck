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

package com.sun.ts.tests.ejb30.bb.session.stateful.remove.complement;

import javax.ejb.EJB;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.ClientBase;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF;
import com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteHome;

public class Client extends ClientBase {
  // @EJB(name="removeBean")
  private static RemoveIF removeBean;

  // @EJB(name="removeBean2")
  private static Remove2IF removeBean2;

  // @EJB(name="removeNotRetainBean")
  private static RemoveNotRetainIF removeNotRetainBean;

  // @EJB(name="testBean")
  private static TestIF testBean;

  @EJB(name = "twoRemoteHome", lookup = "java:global/stateful_remove_complement/stateful_remove_complement_ejb/RemoveBean!com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteHome")
  private static TwoRemoteHome twoRemoteHome;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  @Override
  protected TestIF getTestBean() {
    return testBean;
  }

  @Override
  protected RemoveIF getRemoveBean() {
    return removeBean;
  }

  @Override
  protected Remove2IF getRemoveBean2() {
    return removeBean2;
  }

  @Override
  protected RemoveNotRetainIF getRemoveNotRetainBean() {
    return removeNotRetainBean;
  }

  @Override
  protected TwoRemoteHome getTwoRemoteHome() {
    return twoRemoteHome;
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
   * @testName: removeTwoRemoteHome
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: removeTwoRemoteHomeHandle
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
}
