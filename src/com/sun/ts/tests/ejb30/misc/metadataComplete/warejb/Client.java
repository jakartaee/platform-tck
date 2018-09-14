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

package com.sun.ts.tests.ejb30.misc.metadataComplete.warejb;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import static com.sun.ts.tests.servlet.common.util.Data.FAILED;
import static com.sun.ts.tests.servlet.common.util.Data.PASSED;

public class Client extends AbstractUrlClient {
  public static final String CONTEXT_ROOT = "/misc_metadataComplete_warejb";

  public static final String SERVLET_NAME = "TestServlet";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setServletName(SERVLET_NAME);
    setContextRoot(CONTEXT_ROOT);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: annotationNotProcessedForStateless
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void annotationNotProcessedForStateless() throws Fault {
    TEST_PROPS.setProperty(APITEST, "annotationNotProcessedForStateless");
    invoke();
  }

  /*
   * @testName: annotationNotProcessedForStateful
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void annotationNotProcessedForStateful() throws Fault {
    TEST_PROPS.setProperty(APITEST, "annotationNotProcessedForStateful");
    invoke();
  }

  /*
   * @testName: annotationNotProcessedForWar
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void annotationNotProcessedForWar() throws Fault {
    TEST_PROPS.setProperty(APITEST, "annotationNotProcessedForWar");
    invoke();
  }

  /*
   * @testName: typeLevelAnnotationNotProcessedForWar
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void typeLevelAnnotationNotProcessedForWar() throws Fault {
    TEST_PROPS.setProperty(APITEST, "typeLevelAnnotationNotProcessedForWar");
    invoke();
  }

  /*
   * @testName: excludeDefaultIncludeClassInterceptor
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void excludeDefaultIncludeClassInterceptor() throws Fault {
    TEST_PROPS.setProperty(APITEST, "excludeDefaultIncludeClassInterceptor");
    invoke();
  }

  /*
   * @testName: excludeDefaulAndClassReinstateDefault
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void excludeDefaulAndClassReinstateDefault() throws Fault {
    TEST_PROPS.setProperty(APITEST, "excludeDefaulAndClassReinstateDefault");
    invoke();
  }

  /*
   * @testName: defaultAndMethodInterceptor
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void defaultAndMethodInterceptor() throws Fault {
    TEST_PROPS.setProperty(APITEST, "defaultAndMethodInterceptor");
    invoke();
  }

  /*
   * @testName: sameInterceptor3Times
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void sameInterceptor3Times() throws Fault {
    TEST_PROPS.setProperty(APITEST, "sameInterceptor3Times");
    invoke();
  }

  /*
   * @testName: sameInterceptorTwice
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void sameInterceptorTwice() throws Fault {
    TEST_PROPS.setProperty(APITEST, "sameInterceptorTwice");
    invoke();
  }
}
