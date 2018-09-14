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

package com.sun.ts.tests.jsp.spec.configuration.charsequence;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_config_charsequence_web";

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
    setGeneralURI("/jsp/spec/configuration/charsequence");
    setContextRoot(CONTEXT_ROOT);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:296
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which the deferred-syntax-allowed-as-literal element is set to true. Verify
   * that when the character sequence "#{" appears in template text, the
   * sequence is treated as literal characters.
   * [DeferredSyntaxAllowedAsLiteralElement]
   */
  public void deferredSyntaxAllowedAsLiteralTrueTemplateTextTest()
      throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "deferredSyntaxAllowedAsLiteralTrueTemplateTextTest");
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:295
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which the deferred-syntax-allowed-as-literal element is set to false.
   * Verify that when the character sequence "#{" appears in template text, an
   * internal server error results. [TranslationError#{Sequence]
   */
  public void deferredSyntaxAllowedAsLiteralFalseTemplateTextTest()
      throws Fault {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/deferredSyntaxAllowedAsLiteralFalseTemplateTextTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueActionTest
   * 
   * @assertion_ids: JSP:SPEC:296
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which the deferred-syntax-allowed-as-literal element is set to true. Verify
   * that when the character sequence "#{" is passed as an attribute to an
   * action, the sequence is treated as literal characters.
   * [DeferredSyntaxAllowedAsLiteralElement]
   */
  public void deferredSyntaxAllowedAsLiteralTrueActionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "deferredSyntaxAllowedAsLiteralTrueActionTest");
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseActionTest
   * 
   * @assertion_ids: JSP:SPEC:295
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which the deferred-syntax-allowed-as-literal element is set to false.
   * Verify that when the character sequence "#{" is passed as an attribute to
   * an action, and the tld does not specify a deferred-value element for the
   * attribute, an internal server error results. [TranslationError#{Sequence]
   */
  public void deferredSyntaxAllowedAsLiteralFalseActionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/deferredSyntaxAllowedAsLiteralFalseActionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: noDeferredSyntaxAllowedAsLiteralTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:295
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which there is no deferred-syntax-allowed-as-literal element. Verify that
   * when the character sequence "#{" appears in template text, an internal
   * server error results. [TranslationError#{Sequence]
   */
  public void noDeferredSyntaxAllowedAsLiteralTemplateTextTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/noDeferredSyntaxAllowedAsLiteralTemplateTextTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: noDeferredSyntaxAllowedAsLiteralActionTest
   * 
   * @assertion_ids: JSP:SPEC:295
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which there is no deferred-syntax-allowed-as-literal element. Verify that
   * when the character sequence "#{" is passed as an attribute to an action,
   * and the tld does not specify a deferred-value element for the attribute, an
   * internal server error results. [TranslationError#{Sequence]
   */
  public void noDeferredSyntaxAllowedAsLiteralActionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/noDeferredSyntaxAllowedAsLiteralActionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
