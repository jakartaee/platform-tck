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

package com.sun.ts.tests.jsp.spec.tagfiles.directives.tag21;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static String CONTEXT_ROOT = "/jsp_tagfile_directives_tag21_web";

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

    setGeneralURI("/jsp/spec/tagfiles/directives/tag21");
    setContextRoot("/jsp_tagfile_directives_tag21_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */
  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:229.26
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralTagDirectiveAttribute]
   * Verify that when the DeferredSyntaxAllowedAsLiteral tag directive attribute
   * is set to false, a translation error occurs when the '{#' character
   * sequence is used in template text and the jsp version is 2.1 or greater.
   */
  public void deferredSyntaxAllowedAsLiteralFalseTemplateTextTest()
      throws Fault {
    String testName = "DeferredSyntaxAllowedAsLiteralFalseTemplateText";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:229.26
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralTagDirectiveAttribute]
   * Verify that when the DeferredSyntaxAllowedAsLiteral tag directive attribute
   * is set to true, the '{#' character sequence is allowed in template text
   * when the jsp version is 2.1 or greater.
   */
  public void deferredSyntaxAllowedAsLiteralTrueTemplateTextTest()
      throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "DeferredSyntaxAllowedAsLiteralTrueTemplateText");
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseActionTest
   * 
   * @assertion_ids: JSP:SPEC:229.26
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralTagDirectiveAttribute]
   * Verify that when the DeferredSyntaxAllowedAsLiteral tag directive attribute
   * is set to false, a translation error occurs when the '{#' character
   * sequence is used in actions and the jsp version is 2.1 or greater.
   */
  public void deferredSyntaxAllowedAsLiteralFalseActionTest() throws Fault {
    String testName = "DeferredSyntaxAllowedAsLiteralFalseAction";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueActionTest
   * 
   * @assertion_ids: JSP:SPEC:229.26
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralTagDirectiveAttribute]
   * Verify that when the DeferredSyntaxAllowedAsLiteral tag directive attribute
   * is set to true, the '{#' character sequence is allowed in actions when the
   * jsp version is 2.1 or greater.
   */
  public void deferredSyntaxAllowedAsLiteralTrueActionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "DeferredSyntaxAllowedAsLiteralTrueAction");
    invoke();
  }
}
