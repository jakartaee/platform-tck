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

package com.sun.ts.tests.jsf.api.javax_faces.application.facesmessage;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_facesmessage_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: facesMessageNoArgCtorTest
   * @assertion_ids: JSF:JAVADOC:215
   * @test_Strategy: Valdiate a FacesMessage instance can be created using a
   *                 no-arg constructor with no Exceptions thrown.
   */
  public void facesMessageNoArgCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageNoArgCtorTest");
    invoke();
  }

  /**
   * @testName: facesMessageCtor01Test
   * @assertion_ids: JSF:JAVADOC:218
   * @test_Strategy: Validate FacesMessage(int, String, String) creates a new
   *                 FacesMessage instance with no Exceptions thrown.
   */
  public void facesMessageCtor01Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageCtor01Test");
    invoke();
  }

  /**
   * @testName: facesMessageCtorSumTest
   * @assertion_ids: JSF:JAVADOC:216
   * @test_Strategy: Validate FacesMessage(String summary) creates a new
   *                 FacesMessage instance with no Exceptions thrown.
   */
  public void facesMessageCtorSumTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageCtorSumTest");
    invoke();
  }

  /**
   * @testName: facesMessageCtorSumDetTest
   * @assertion_ids: JSF:JAVADOC:217
   * @test_Strategy: Validate FacesMessage(String summary, String detail)
   *                 creates a new FacesMessage instance with no Exceptions
   *                 thrown.
   */
  public void facesMessageCtorSumDetTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageCtorSumDetTest");
    invoke();
  }

  /**
   * @testName: facesMessageGetSetSeverityTest
   * @assertion_ids: JSF:JAVADOC:220; JSF:JAVADOC:225; JSF:JAVADOC:229
   * @test_Strategy: Validate the proper severity is returned in the following
   *                 cases: - FacesMessage created passing severity information
   *                 via the constructor. - FacesMessage.setSeverity(int) is
   *                 called.
   */
  public void facesMessageGetSetSeverityTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageGetSetSeverityTest");
    invoke();
  }

  /**
   * @testName: facesMessageGetSetSummaryTest
   * @assertion_ids: JSF:JAVADOC:221; JSF:JAVADOC:227
   * @test_Strategy: Validate the proper summary is returned in the following
   *                 cases: - FacesMessage created passing summary information
   *                 via the constructor. - FacesMessage.setSummary(String) is
   *                 called.
   */
  public void facesMessageGetSetSummaryTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageGetSetSummaryTest");
    invoke();
  }

  /**
   * @testName: facesMessageGetSetDetailTest
   * @assertion_ids: JSF:JAVADOC:219; JSF:JAVADOC:224
   * @test_Strategy: Validate the proper detail is returned in the following
   *                 cases: - FacesMessage created passing detail information
   *                 via the constructor. - FacesMessage.setDetail(String) is
   *                 called.
   */
  public void facesMessageGetSetDetailTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageGetSetDetailTest");
    invoke();
  }

  /**
   * @testName: facesMessageIsRenderedTest
   * @assertion_ids: JSF:JAVADOC:222; JSF:JAVADOC:223
   * @test_Strategy: Validate that FacesMessage.isRendered() returns the correct
   *                 boolean values pre/post FacesMessage.rendered() being
   *                 called.
   */
  public void facesMessageIsRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageIsRenderedTest");
    invoke();
  }

  /**
   * @testName: facesMessageSeverityCompareToTest
   * @assertion_ids: JSF:JAVADOC:228
   * @test_Strategy: Validate that FacesMessage.Severity.compareTo returns the
   *                 correct Int value "0" based on the test conditions.
   */
  public void facesMessageSeverityCompareToTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageSeverityCompareToTest");
    invoke();
  }

  /**
   * @testName: facesMessageSeverityToStringTest
   * @assertion_ids: JSF:JAVADOC:230
   * @test_Strategy: Validate that FacesMessage.Severity.toString returns the
   *                 correct value.
   */
  public void facesMessageSeverityToStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesMessageSeverityToStringTest");
    invoke();
  }

} // end of URLClient
