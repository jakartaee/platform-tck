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

package com.sun.ts.tests.jsf.api.javax_faces.event.valuechangeevent;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_valuechangeevent_web";

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

  /* Run test */

  /*
   * @testName: valueChangeEventCtorTest
   * 
   * @assertion_ids: JSF:JAVADOC:1889
   * 
   * @test_Strategy: Verify constructor
   *
   */
  public void valueChangeEventCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueChangeEventCtorTest");
    invoke();
  }

  /*
   * @testName: valueChangeEventCtorIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JSF:JAVADOC:1889
   * 
   * @test_Strategy: Verify constructor
   *
   */
  public void valueChangeEventCtorIllegalArgumentExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "valueChangeEventCtorIllegalArgumentExceptionTest");
    invoke();
  }

  /*
   * @testName: valueChangeEventGetOldValueTest
   * 
   * @assertion_ids: JSF:JAVADOC:1889
   * 
   * @test_Strategy: Verify source of event is returned
   *
   */
  public void valueChangeEventGetOldValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueChangeEventGetOldValueTest");
    invoke();
  }

  /*
   * @testName: valueChangeEventGetNewValueTest
   * 
   * @assertion_ids: JSF:JAVADOC:1889
   * 
   * @test_Strategy: Verify source of event is returned
   *
   */
  public void valueChangeEventGetNewValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueChangeEventGetNewValueTest");
    invoke();
  }

  /*
   * @testName: valueChangeEventGetComponentTest
   * 
   * @assertion_ids: JSF:JAVADOC:1889
   * 
   * @test_Strategy: Verify source of event is returned
   *
   */
  public void valueChangeEventGetComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueChangeEventGetComponentTest");
    invoke();
  }

}
