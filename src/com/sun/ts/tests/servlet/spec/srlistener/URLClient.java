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
 * $Id:$
 */
package com.sun.ts.tests.servlet.spec.srlistener;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

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

    setServletName("TestServlet");
    setContextRoot("/servlet_spec_srlistener_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: simpleinclude
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void simpleinclude() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "includes");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "IncludedServlet Invoked|simple method");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }
    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: multipleincludes
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void multipleincludes() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "multipleincludes");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "SecondIncludedServlet Invoked|simple method");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: includeforward
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void includeforward() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "includeforward");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "ForwardedServlet Invoked|simple method");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: includeerror
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void includeerror() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot()
          + "/TestServlet?testname=includeerror HTTP/1.1");
      TEST_PROPS.setProperty(STATUS_CODE, "403");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: simpleforward
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void simpleforward() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "forward");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "ForwardedServlet Invoked|simple method");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: multipleforwards
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void multipleforwards() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "multipleforwards");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "SecondForwardedServlet Invoked|simple method");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: forwardinclude
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void forwardinclude() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "forwardinclude");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "IncludedServlet Invoked|simple method");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: forwarderror
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void forwarderror() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot()
          + "/TestServlet?testname=forwarderror HTTP/1.1");
      TEST_PROPS.setProperty(STATUS_CODE, "403");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: simpleasync
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void simpleasync() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "async");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "TestServlet Invoked|method async|TestServlet_Async=STARTED");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: simpleasyncinclude
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void simpleasyncinclude() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "simpleasyncinclude");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "TestServlet Invoked|method simpleasyncinclude"
              + "|TestServlet_Async=STARTED"
              + "|IncludedServlet Invoked||simple method");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: simpleasyncforward
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void simpleasyncforward() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(APITEST, "simpleasyncforward");
      TEST_PROPS.setProperty(SEARCH_STRING,
          "ForwardedServlet Invoked||simple method");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: simpleasyncerror
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void simpleasyncerror() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot()
          + "/TestServlet?testname=simpleasyncerror HTTP/1.1");
      TEST_PROPS.setProperty(STATUS_CODE, "403");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }

  /*
   * @testName: error
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void error() throws Fault {
    Boolean pass = true;
    try {
      TEST_PROPS.setProperty(REQUEST,
          "GET " + getContextRoot() + "/TestServlet?testname=error HTTP/1.1");

      TEST_PROPS.setProperty(STATUS_CODE, "403");
      invoke();
    } catch (Fault flt) {
      pass = false;
      TestUtil.logErr("Test failed at the first invocation."
          + "catch it here so the cleanup can continue", flt);
    }

    TEST_PROPS.setProperty(APITEST, "checkLogSimple");
    invoke();

    if (!pass) {
      throw new Fault("Test failed at the first invocation."
          + "catch it here so the cleanup can continue");
    }
  }
}
