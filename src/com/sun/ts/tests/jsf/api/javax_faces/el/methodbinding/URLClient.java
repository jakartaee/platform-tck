/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.el.methodbinding;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_el_methbinding_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: methBindingInvokeTest
   * @assertion_ids: JSF:JAVADOC:1714; JSF:JAVADOC:1710
   * @test_Strategy: Ensure a MethodBinding expression can be invoked with the
   *                 proper value returned by the invocation.
   */
  public void methBindingInvokeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methBindingInvokeTest");
    invoke();
  }

  /**
   * @testName: methBindingGetTypeNPETest
   * @assertion_ids: JSF:JAVADOC:1714; JSF:JAVADOC:1709
   * @test_Strategy: Ensure a NullPointerException is thrown if the FacesContext
   *                 argument go getType() is null.
   */
  public void methBindingGetTypeNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methBindingGetTypeNPETest");
    invoke();
  }

  /**
   * @testName: methBindingInvokeNPETest
   * @assertion_ids: JSF:JAVADOC:1714; JSF:JAVADOC:1713
   * @test_Strategy: Ensure a NullPointerException is thrown if the FacesContext
   *                 argument go invoke() is null.
   */
  public void methBindingInvokeNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methBindingInvokeNPETest");
    invoke();
  }

  /**
   * @testName: methBindingGetTypeMethNotFoundExcTest
   * @assertion_ids: JSF:JAVADOC:1714; JSF:JAVADOC:1708
   * @test_Strategy: Ensure MethodNotFoundException is thrown by getType() if
   *                 the method referenced by the MethodBinding expression
   *                 doesn't exist in the resolved Object.
   */
  public void methBindingGetTypeMethNotFoundExcTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methBindingGetTypeMethNotFoundExcTest");
    invoke();
  }

  /**
   * @testName: methBindingInvokeMethNotFoundExcTest
   * @assertion_ids: JSF:JAVADOC:1714; JSF:JAVADOC:1712
   * @test_Strategy: Ensure MethodNotFoundException is thrown by invoke() if the
   *                 method referenced by the MethodBinding expression doesn't
   *                 exist in the resolved Object.
   */
  public void methBindingInvokeMethNotFoundExcTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methBindingInvokeMethNotFoundExcTest");
    invoke();
  }

  /**
   * @testName: methBindingInvokeEvalExcTest
   * @assertion_ids: JSF:JAVADOC:1714; JSF:JAVADOC:1711
   * @test_Strategy: Ensure an EvaluationException is thrown if the resolved
   *                 method throws an exception during evaluation.
   */
  public void methBindingInvokeEvalExcTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methBindingInvokeEvalExcTest");
    invoke();
  }

} // end of URLClient
