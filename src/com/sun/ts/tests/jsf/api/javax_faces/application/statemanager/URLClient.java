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
package com.sun.ts.tests.jsf.api.javax_faces.application.statemanager;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_statemgrs_web.xml";

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
   * @testName: stateManagerIsSavingStateInClientTest
   * @assertion_ids: JSF:JAVADOC:308
   * @test_Strategy: Ensure isStateSavingInClient() returns the expected value
   *                 based on the context initialization parameter.
   */
  public void stateManagerIsSavingStateInClientTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST, "GET /jsf_appl_statemgrs_web/TestServlet?"
        + "testname=stateManagerIsSavingStateInClientTest HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "false");
    invoke();
    TEST_PROPS.setProperty(REQUEST, "GET /jsf_appl_statemgrc_web/TestServlet?"
        + "testname=stateManagerIsSavingStateInClientTest HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "true");
    invoke();
  }

  /**
   * @testName: stateManagerIsSavingStateInClientNPETest
   * @assertion_ids: JSF:JAVADOC:309
   * @test_Strategy: Ensure isStateSavingInClient() throws a
   *                 NullPointerException when Context is null.
   */
  public void stateManagerIsSavingStateInClientNPETest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_appl_statemgrs_web/TestServlet?testname="
            + "stateManagerIsSavingStateInClientNPETest HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /**
   * @testName: stateManagerSaveSerializedViewTest
   * @assertion_ids: JSF:JAVADOC:312
   * @test_Strategy: Validate the following: - Saving state on the server: *
   *                 saveSerializedView returns null no matter if there is state
   *                 to save or not - Saving state on the client: *
   *                 saveSerializedView returns null only when there is no state
   *                 to save.
   */
  public void stateManagerSaveSerializedViewTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_appl_statemgrs_web/TestServlet?testname="
            + "stateManagerSaveSerializedViewTest HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "null|null");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_appl_statemgrc_web/TestServlet?testname="
            + "stateManagerSaveSerializedViewTest HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "null|not null");
    invoke();
  }

  /**
   * @testName: stateManagerSaveSerializedViewDupIdTest
   * @assertion_ids: JSF:JAVADOC:313
   * @test_Strategy: Validate an IllegalStateException is thrown if duplicate
   *                 IDs are found in the view.
   */
  public void stateManagerSaveSerializedViewDupIdTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_appl_statemgrs_web/TestServlet?testname="
            + "stateManagerSaveSerializedViewDupIdTest HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_appl_statemgrc_web/TestServlet?testname="
            + "stateManagerSaveSerializedViewDupIdTest HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

} // end of URLClient
