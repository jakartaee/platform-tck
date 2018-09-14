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

package com.sun.ts.tests.ejb30.tx.common.session.inheritance;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

abstract public class ClientBase extends AbstractUrlClient {
  protected String SERVLET_NAME = "TestServlet";

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setServletName(SERVLET_NAME);
    setContextRoot(getContextRoot());
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * testName: aBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote ABean
   */
  public void aBeanRemote() throws Fault {
    TEST_PROPS.setProperty(APITEST, "aBeanRemote");
    invoke();
  }

  /*
   * testName: bBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote bBean
   */
  public void bBeanRemote() throws Fault {
    TEST_PROPS.setProperty(APITEST, "bBeanRemote");
    invoke();
  }

  /*
   * testName: cBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote cBean
   */
  public void cBeanRemote() throws Fault {
    TEST_PROPS.setProperty(APITEST, "cBeanRemote");
    invoke();
  }

  /*
   * testName: dBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote dBean
   */
  public void dBeanRemote() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dBeanRemote");
    invoke();
  }

  /*
   * testName: eBeanRemote
   * 
   * @test_Strategy: httpclient -> servlet -> remote eBean
   */
  public void eBeanRemote() throws Fault {
    TEST_PROPS.setProperty(APITEST, "eBeanRemote");
    invoke();
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: aBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local ABean
   */
  public void aBeanLocal() throws Fault {
    TEST_PROPS.setProperty(APITEST, "aBeanLocal");
    invoke();
  }

  /*
   * testName: bBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local bBean
   */
  public void bBeanLocal() throws Fault {
    TEST_PROPS.setProperty(APITEST, "bBeanLocal");
    invoke();
  }

  /*
   * testName: cBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local cBean
   */
  public void cBeanLocal() throws Fault {
    TEST_PROPS.setProperty(APITEST, "cBeanLocal");
    invoke();
  }

  /*
   * testName: dBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local dBean
   */
  public void dBeanLocal() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dBeanLocal");
    invoke();
  }

  /*
   * testName: eBeanLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local eBean
   */
  public void eBeanLocal() throws Fault {
    TEST_PROPS.setProperty(APITEST, "eBeanLocal");
    invoke();
  }

  /*
   * testName: overloadedMethodsTxLocal
   * 
   * @test_Strategy: httpclient -> servlet -> local fBean
   */
  public void overloadedMethodsTxLocal() throws Fault {
    TEST_PROPS.setProperty(APITEST, "overloadedMethodsTxLocal");
    invoke();
  }

  /*
   * testName: overloadedMethodsTxRemote
   * 
   * @test_Strategy: httpclient -> servlet -> local fBean
   */
  public void overloadedMethodsTxRemote() throws Fault {
    TEST_PROPS.setProperty(APITEST, "overloadedMethodsTxRemote");
    invoke();
  }

}
