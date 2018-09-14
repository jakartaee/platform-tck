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
 * @(#)ClientBase.java	1.3 05/12/04
 */

package com.sun.ts.tests.ejb30.common.equals;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.lib.harness.EETest;
import java.util.Properties;
import javax.ejb.EJB;

abstract public class ClientBase extends EETest {
  protected static final boolean CLIENT_LOG_IF_OK = true;

  protected Properties props;

  @EJB(name = "testBean")
  private static TestIF testBean;

  public void setup(String[] args, Properties p) {
    props = p;
  }

  public void cleanup() {
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: testBeanselfEquals
   * 
   * @test_Strategy:
   *
   */
  public void testBeanselfEquals() throws TestFailedException {
    testBean.selfEquals();
  }

  /*
   * testName: testBeanotherEquals
   * 
   * @test_Strategy: only for comparing stateless beans.
   *
   */
  public void testBeanotherEquals() throws TestFailedException {
    testBean.otherEquals();
  }

  /*
   * testName: testBeanotherNotEquals
   * 
   * @test_Strategy: only for comparing stateless beans.
   *
   */
  public void testBeanotherNotEquals() throws TestFailedException {
    testBean.otherNotEquals();
  }

  /*
   * testName: testBeandifferentInterfaceNotEqual
   * 
   * @test_Strategy:
   *
   */
  public void testBeandifferentInterfaceNotEqual() throws TestFailedException {
    testBean.differentInterfaceNotEqual();
  }

  /*
   * testName: testBeanselfEqualsLookup
   * 
   * @test_Strategy:
   *
   */
  public void testBeanselfEqualsLookup() throws TestFailedException {
    testBean.selfEqualsLookup();
  }

  /*
   * testName: testBeanotherEqualsLookup
   * 
   * @test_Strategy:only for comparing stateless beans.
   *
   */
  public void testBeanotherEqualsLookup() throws TestFailedException {
    testBean.otherEqualsLookup();
  }

  /*
   * testName: testBeanotherNotEqualsLookup
   * 
   * @test_Strategy:only for comparing stateful beans.
   *
   */
  public void testBeanotherNotEqualsLookup() throws TestFailedException {
    testBean.otherNotEqualsLookup();
  }

  /*
   * testName: testBeandifferentInterfaceNotEqualLookup
   * 
   * @test_Strategy:
   *
   */
  public void testBeandifferentInterfaceNotEqualLookup()
      throws TestFailedException {
    testBean.differentInterfaceNotEqualLookup();
  }
}
