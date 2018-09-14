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

package com.sun.ts.tests.jsf.api.javax_faces.event.facesevent;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_facesevent_web";

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
   * @testName: facesEventCtorTest
   * 
   * @assertion_ids: JSF:JAVADOC:1809
   * 
   * @test_Strategy: Verify constructor
   */
  public void facesEventCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesEventCtorTest");
    invoke();
  }

  /*
   * @testName: facesEventCtorIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JSF:JAVADOC:1809
   * 
   * @test_Strategy: Verify constructor
   */
  public void facesEventCtorIllegalArgumentExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "facesEventCtorIllegalArgumentExceptionTest");
    invoke();
  }

  /*
   * @testName: facesEventGetComponentTest
   * 
   * @assertion_ids: JSF:JAVADOC:1809; JSF:JAVADOC:1810
   * 
   * @test_Strategy: Verify source of event is returned
   */
  public void facesEventGetComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesEventGetComponentTest");
    invoke();
  }

  /**
   * @testName: facesEventQueueISETest
   * @assertion_ids: JSF:JAVADOC:1809; JSF:JAVADOC:1816
   * @test_Strategy: Ensure an IllegalStateException is thrown when calling the
   *                 queue() method on a FacesEvent when the source component of
   *                 the FacesEvent is not a child of a UIViewRoot instance.
   */
  public void facesEventQueueISETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesEventQueueISETest");
    invoke();
  }

  /**
   * @testName: facesEventGetSetPhaseIdTest
   * @assertion_ids: JSF:JAVADOC:1809; JSF:JAVADOC:1811; JSF:JAVADOC:1817
   * @test_Strategy: Verify we can set and get the Phaseid..
   */
  public void facesEventGetSetPhaseIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesEventGetSetPhaseIdTest");
    invoke();
  }

  /**
   * @testName: facesEventSetPhaseIdIAETest
   * @assertion_ids: JSF:JAVADOC:1809; JSF:JAVADOC:1818
   * @test_Strategy: Validate that we get an IllegalArgumentException when null
   *                 is passed in for PhaseId.
   */
  public void facesEventSetPhaseIdIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesEventSetPhaseIdIAETest");
    invoke();
  }
}
