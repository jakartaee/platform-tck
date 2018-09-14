/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:
 */
package com.sun.ts.tests.jsf.api.javax_faces.event.exceptionqueuedeventcontext;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_exceptionqueuedeventcontext_web";

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
   * @testName: exceptionQueuedEventContextCtorTest
   * @assertion_ids: JSF:JAVADOC:1798
   * @test_Strategy: Ensure: Instantiate a new ExceptionQueuedEventContext that
   *                 indicates the argument Throwable just occurred.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "exceptionQueuedEventContextCtorTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextCtorOneTest
   * @assertion_ids: JSF:JAVADOC:1797
   * @test_Strategy: Instantiate a new ExceptionQueuedEventContext that
   *                 indicates the argument Throwable just occurred, relevant to
   *                 the argument component.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextCtorOneTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "exceptionQueuedEventContextCtorOneTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextCtorTwoTest
   * @assertion_ids: JSF:JAVADOC:1799
   * @test_Strategy: Instantiate a new ExceptionQueuedEventContext that
   *                 indicates the argument Throwable just occurred, relevant to
   *                 the argument component, during the lifecycle phase phaseId.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextCtorTwoTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "exceptionQueuedEventContextCtorTwoTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextgetContextTest
   * @assertion_ids: JSF:JAVADOC:1803
   * @test_Strategy: Make sure that getContext returns the FacesContext used to
   *                 create this ExceptionQueuedEventContext instance.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextgetContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventContextgetContextTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextgetAttributesTest
   * @assertion_ids: JSF:JAVADOC:1801
   * @test_Strategy: Make sure that getAttributes returns an instance of
   *                 java.util.Map
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextgetAttributesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventContextgetAttributesTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextgetExceptionTest
   * @assertion_ids: JSF:JAVADOC:1804
   * @test_Strategy: Make sure that getException returns the exception property.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextgetExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventContextgetExceptionTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextgetComponentTest
   * @assertion_ids: JSF:JAVADOC:1802
   * @test_Strategy: Make sure the returned UIComponent which was being
   *                 processed when the exception was thrown.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextgetComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventContextgetComponentTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextgetComponentNullTest
   * @assertion_ids: JSF:JAVADOC:1802
   * @test_Strategy: Make sure the returned UIComponent which was being
   *                 processed when the exception was thrown, If none or not
   *                 available, this will be null..
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextgetComponentNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventContextgetComponentNullTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextgetPhaseIdTest
   * @assertion_ids: JSF:JAVADOC:1806
   * @test_Strategy: Make sure that the PhaseId which was being processed when
   *                 the exception was thrown.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextgetPhaseIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventContextgetPhaseIdTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventContextgetPhaseIdNullTest
   * @assertion_ids: JSF:JAVADOC:1806
   * @test_Strategy: Make sure that the PhaseId which was being processed when
   *                 the exception was thrown. If none or not available, this
   *                 will be null.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventContextgetPhaseIdNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventContextgetPhaseIdNullTest");
    invoke();
  }
} // end of URLClient
