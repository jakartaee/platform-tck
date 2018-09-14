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

package com.sun.ts.tests.jstl.spec.etu.config;

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

    setContextRoot("/jstl_etu_config_web");
    setGoldenFileDir("/jstl/spec/etu/config");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveConfigStaticMembersTest
   * 
   * @assertion_ids: JSTL:SPEC:99.1; JSTL:SPEC:99.2; JSTL:SPEC:99.3;
   * JSTL:SPEC:99.4; JSTL:SPEC:99.5; JSTL:SPEC:99.6
   * 
   * @testStrategy: Validate that the public static member values of the
   * javax.servlet.jsp.jstl.core.Config class agree with the javadoc.
   */
  public void positiveConfigStaticMembersTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveConfigStaticMemebersTest");
    invoke();
  }

  /*
   * @testName: positiveConfigGetSetRemovePageContextTest
   * 
   * @assertion_ids: JSTL:SPEC:100; JSTL:SPEC:101; JSTL:SPEC:102
   *
   * @testStrategy: Validate the set(), get(), and remove() methods when passing
   * a PageContext object. Using the same variable, verify that the Config class
   * sets variables in the PageContext in such a way that even if the variable
   * names specified are all the same, they are unique in each scope. Validate
   * the the values returned by get() are the same as those set. Additionally
   * validate that once the variables are removed, that additional calls to get
   * for the same variables, will return null.
   */
  public void positiveConfigGetSetRemovePageContextTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "positiveConfigGetSetRemovePageContextTest");
    invoke();
  }

  /*
   * @testName: positiveConfigGetSetRemoveRequestTest
   * 
   * @assertion_ids: JSTL:SPEC:100; JSTL:SPEC:101; JSTL:SPEC:102
   *
   * @testStrategy: Validate the set(), get(), and remove() methods when passing
   * a ServletRequest object. Additionally validate that once the variable is
   * removed, that additional calls to get for the same variable, will return
   * null.
   */
  public void positiveConfigGetSetRemoveRequestTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveConfigGetSetRemoveRequestTest");
    invoke();
  }

  /*
   * @testName: positiveConfigGetSetRemoveSessionTest
   * 
   * @assertion_ids: JSTL:SPEC:100; JSTL:SPEC:101; JSTL:SPEC:102
   *
   * @testStrategy: Validate the set(), get(), and remove() methods when passing
   * an HttpSession object. Additionally validate that once the variable is
   * removed, that additional calls to get for the same variable, will return
   * null.
   */
  public void positiveConfigGetSetRemoveSessionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_config_web/positiveConfigGetSetRemoveSessionTest.jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveConfigGetSetRemoveApplicationTest
   * 
   * @assertion_ids: JSTL:SPEC:100; JSTL:SPEC:101; JSTL:SPEC:102
   * 
   * @testStrategy: Validate the set(), get(), and remove() methods when passing
   * a ServletContext object. Additionally validate that once the variable is
   * removed, that additional calls to get for the same variable, will return
   * null.
   */
  public void positiveConfigGetSetRemoveApplicationTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "positiveConfigGetSetRemoveApplicationTest");
    invoke();
  }

  /*
   * @testName: positiveConfigFindTest
   * 
   * @assertion_ids: JSTL:SPEC:103; JSTL:SPEC:103.1
   * 
   * @testStrategy: Validate that the find() method is able to find the and
   * return the specified variable from the PageContext without specifying a
   * scope. Also validate that if no variable is found in the PageContext, that
   * method will attempt to find a context initialization parameter by the name
   * provided.
   */
  public void positiveConfigFindTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveConfigFindTest");
    invoke();
  }
}
