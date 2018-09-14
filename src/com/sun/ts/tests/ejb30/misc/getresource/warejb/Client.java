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

package com.sun.ts.tests.ejb30.misc.getresource.warejb;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import static com.sun.ts.tests.servlet.common.util.Data.FAILED;
import static com.sun.ts.tests.servlet.common.util.Data.PASSED;

public class Client extends AbstractUrlClient {
  public static final String CONTEXT_ROOT = "/misc_getresource_warejb_web";

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
   * @testName: getResourceNullParam
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceNullParam() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceNullParam");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamNullParam
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceAsStreamNullParam() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamNullParam");
    invoke();
  }

  /*
   * @testName: getResourceNonexisting
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceNonexisting() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceNonexisting");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamNonexisting
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceAsStreamNonexisting() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamNonexisting");
    invoke();
  }

  /*
   * @testName: getResourceSamePackage
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceSamePackage() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceSamePackage");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamSamePackage
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceAsStreamSamePackage() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamSamePackage");
    invoke();
  }

  /*
   * @testName: getResourceResolve
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceResolve() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceResolve");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamResolve
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceAsStreamResolve() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamResolve");
    invoke();
  }

  /*
   * @testName: getResourceResolveEarLib
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceResolveEarLib() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceResolveEarLib");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamResolveEarLib
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet
   */
  public void getResourceAsStreamResolveEarLib() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamResolveEarLib");
    invoke();
  }

  /////////////////////////////////////////////////
  // tests in EJB
  ////////////////////////////////////////////////
  /*
   * @testName: getResourceNullParamEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceNullParamEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceNullParamEJB");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamNullParamEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceAsStreamNullParamEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamNullParamEJB");
    invoke();
  }

  /*
   * @testName: getResourceNonexistingEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceNonexistingEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceNonexistingEJB");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamNonexistingEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceAsStreamNonexistingEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamNonexistingEJB");
    invoke();
  }

  /*
   * @testName: getResourceSamePackageEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceSamePackageEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceSamePackageEJB");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamSamePackageEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceAsStreamSamePackageEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamSamePackageEJB");
    invoke();
  }

  /*
   * @testName: getResourceResolveEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceResolveEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceResolveEJB");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamResolveEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceAsStreamResolveEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamResolveEJB");
    invoke();
  }

  /*
   * @testName: getResourceResolveEarLibEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceResolveEarLibEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceResolveEarLibEJB");
    invoke();
  }

  /*
   * @testName: getResourceAsStreamResolveEarLibEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:client -> TestServlet -> bean
   */
  public void getResourceAsStreamResolveEarLibEJB() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResourceAsStreamResolveEarLibEJB");
    invoke();
  }

}
