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

package com.sun.ts.tests.ejb30.tx.common.session.cm;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.Properties;
import javax.ejb.EJB;

abstract public class ClientBase extends EETest {
  @EJB(name = "testBean")
  static protected TestIF testBean;

  protected Properties props;

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) {
    props = p;
  }

  public void cleanup() {
  }

  protected void removeBeans() {
    if (testBean != null) {
      try {
        testBean.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  /*
   * testName: mandatoryTest
   * 
   * @test_Strategy: client -> remote bm slsb -> remote cm slsb
   */
  public void mandatoryTest() throws TestFailedException {
    testBean.mandatoryTest();
  }

  /*
   * testName: localMandatoryTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */
  public void localMandatoryTest() throws TestFailedException {
    testBean.localMandatoryTest();
  }

  /*
   * testName: neverTest
   * 
   * @test_Strategy: client -> remote bm slsb -> remote cm slsb
   */
  public void neverTest() throws TestFailedException {
    testBean.neverTest();
  }

  /*
   * testName: localNeverTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */
  public void localNeverTest() throws TestFailedException {
    testBean.localNeverTest();
  }

  /*
   * testName: supportsTest
   * 
   * @test_Strategy: client -> remote bm slsb -> remote cm slsb 13.6.2.9
   * Handling of getRollbackOnly Method The container must throw the
   * java.lang.IllegalStateException if the EJBContext. getRollbackOnly method
   * is invoked from a business method executing with the SUPPORTS,
   * NOT_SUPPORTED, or NEVER transaction attribute.
   * 
   * 13.6.2.8 Handling of setRollbackOnly Method The container must throw the
   * java.lang.IllegalStateException if the EJBContext. setRollbackOnly method
   * is invoked from a business method executing with the SUPPORTS,
   * NOT_SUPPORTED, or NEVER transaction attribute.
   */
  public void supportsTest() throws TestFailedException {
    Helper.getLogger().info(testBean.supportsTest());
  }

  /*
   * testName: localSupportsTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */
  public void localSupportsTest() throws TestFailedException {
    Helper.getLogger().info(testBean.localSupportsTest());
    System.out.println(testBean.localSupportsTest());
  }

  /*
   * testName: illegalGetSetRollbackOnlyNeverTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */
  public void illegalGetSetRollbackOnlyNeverTest() throws TestFailedException {
    Helper.getLogger().info(testBean.illegalGetSetRollbackOnlyNeverTest());
  }

  /*
   * testName: localIllegalGetSetRollbackOnlyNeverTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */
  public void localIllegalGetSetRollbackOnlyNeverTest()
      throws TestFailedException {
    Helper.getLogger().info(testBean.localIllegalGetSetRollbackOnlyNeverTest());
  }

  /*
   * testName: illegalGetSetRollbackOnlyNotSupportedTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */
  public void illegalGetSetRollbackOnlyNotSupportedTest()
      throws TestFailedException {
    Helper.getLogger()
        .info(testBean.illegalGetSetRollbackOnlyNotSupportedTest());
  }

  /*
   * testName: localIllegalGetSetRollbackOnlyNotSupportedTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */
  public void localIllegalGetSetRollbackOnlyNotSupportedTest()
      throws TestFailedException {
    Helper.getLogger()
        .info(testBean.localIllegalGetSetRollbackOnlyNotSupportedTest());
  }

  /*
   * testName: systemExceptionTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */
  public void systemExceptionTest() throws TestFailedException {
    Helper.getLogger().info(testBean.systemExceptionTest());
  }

  /*
   * testName: localRequiresNewRemoveTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm sfsb
   */
  public void localRequiresNewRemoveTest() throws TestFailedException {
    testBean.localRequiresNewRemoveTest();
  }
}
