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

package com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated;

import com.sun.ts.tests.ejb30.tx.common.session.inheritance.ClientBase;
import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;

public class Client extends ClientBase {
  protected String CONTEXT_ROOT = "/tx_stateless_inheritance_annotated_web";

  @Override()
  public String getContextRoot() {
    return CONTEXT_ROOT;
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: aBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote ABean
   */

  /*
   * @testName: bBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote bBean
   */
  /*
   * @testName: cBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote cBean
   */
  /*
   * @testName: dBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote dBean
   */
  /*
   * @testName: eBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote eBean
   */

  /////////////////////////////////////////////////////////////

  /*
   * @testName: aBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local ABean
   */
  /*
   * @testName: bBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local bBean
   */
  /*
   * @testName: cBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local cBean
   */
  /*
   * @testName: dBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local dBean
   */
  /*
   * @testName: eBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local eBean
   */
  /*
   * @testName: overloadedMethodsTxLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local fBean
   */
  /*
   * @testName: overloadedMethodsTxRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote fBean
   */

}
