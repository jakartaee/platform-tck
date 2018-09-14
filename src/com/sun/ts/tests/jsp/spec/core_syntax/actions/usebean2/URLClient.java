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
 * %W 06/02/11
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean2;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_coresyntx_act_usebean2_web";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setGeneralURI("/jsp/spec/core_syntax/actions/usebean2");
    setContextRoot("/jsp_coresyntx_act_usebean2_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /*
   * @testName: inScriptlessTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: when used in scriptless context, an EL variable is created.
   */

  public void inScriptlessTest() throws Fault {
    String testName = "inScriptlessTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "[|]|invoke tag|[|]");
    // TEST_PROPS.setProperty(SEARCH_STRING, "invoke tag");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "${list");
    invoke();
  }

  /*
   * @testName: blockSCopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: The scope of the scripting language variable is dependent
   * upon the scoping rules and capabilities of the scripting language
   */

  public void blockSCopeTest() throws Fault {
    String testName = "blockSCopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "one");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "two");
    invoke();
  }

  /*
   * @testName: existingWithBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: If the jsp:useBean action had a non-empty body it is
   * ignored if this bean already exists.
   */

  public void existingWithBodyTest() throws Fault {
    String testName = "existingWithBodyTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "one");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "This body should be ignored");
    invoke();
  }

  /*
   * @testName: noClassNoBeanNameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: If the object is not found in the specified scope and
   * neither class nor beanName are given, a java.lang.InstantiationException
   * shall occur.
   */

  public void noClassNoBeanNameTest() throws Fault {
    String testName = "noClassNoBeanNameTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|InstantiationException");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: unrestrictedBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: The variable is initialized and available within the scope
   * of the body. Body content is not restricted.
   */

  public void unrestrictedBodyTest() throws Fault {
    String testName = "unrestrictedBodyTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "add to list|added to list|In unrestrictedBodyTestInclude.jsp|one");
    invoke();
  }
}
