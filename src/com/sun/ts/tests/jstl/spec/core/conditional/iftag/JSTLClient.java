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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.spec.core.conditional.iftag;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

public class JSTLClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /** Creates new JSTLClient */
  public JSTLClient() {
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JSTLClient theTests = new JSTLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jstl_core_cond_if_web");
    setGoldenFileDir("/jstl/spec/core/conditional/iftag");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveIfTest
   * 
   * @assertion_ids: JSTL:SPEC:14.2
   * 
   * @testStrategy: Verify 'test' and 'var' attribute behavior of the 'if'
   * action with no content body
   */
  public void positiveIfTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIfTest");
    invoke();
  }

  /*
   * @testName: positiveIfBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:14.1.1; JSTL:SPEC:14.1.2
   * 
   * @testStrategy: Verify the behavior of the 'if' action with regards to the
   * result of it's test and it's body content
   */
  public void positiveIfBodyBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIfBodyBehaviorTest");
    invoke();
  }

  /*
   * @testName: positiveIfScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:14.3.1; JSTL:SPEC:14.3.2; JSTL:SPEC:14.3.3;
   * JSTL:SPEC:14.3.4; JSTL:SPEC:14.3.5
   * 
   * @testStrategy: Verify the behavior of the 'if' action when using the scope
   * attribute. If scope is not specified, the exported var should be in the
   * page scope, otherwise the exported var should be in the designated scope.
   */
  public void positiveIfScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIfScopeTest");
    invoke();
  }

  /*
   * @testName: positiveIfExportedVarTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:14.2.1
   * 
   * @testStrategy: Validate that the variable exported by the 'if' action is of
   * type 'java.lang.Boolean'
   */
  public void positiveIfExportedVarTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIfExportedVarTypeTest");
    invoke();
  }

  /*
   * @testName: negativeIfTestTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:14.1.3
   * 
   * @testStrategy: Validate that an instance of
   * javax.servlet.jsp.JspTagException is thrown if the resulting expression
   * passed ot the 'test' attribute is not of the expected type (boolean/Boolean
   * for EL, and boolean for RT).
   */
  public void negativeIfTestTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeIfTestTypeTest");
    invoke();
  }

  /*
   * @testName: negativeIfExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:14.7
   * 
   * @testStrategy: Validate that exceptions caused by the body content are
   * propagated.
   */
  public void negativeIfExcBodyContentTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeIfExcBodyContentTest");
    invoke();
  }
}
