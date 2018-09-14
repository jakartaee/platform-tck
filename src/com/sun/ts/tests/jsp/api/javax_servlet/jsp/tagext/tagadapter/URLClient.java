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

/*
 * @(#)URLClient.java	1.2 10/09/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagadapter;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagAdapter. There isn't much that can be done to really test
 * the runtime functionality of TagAdapter as the container will provide the
 * code to cause the wrapping to occur. This set of tests will perform basic API
 * tests to make sure that methods that should not be called by the container
 * throw an UnsupportedOperationException. The last test will make sure that a
 * tag nested within a SimpleTag will be passed an instance of the TagAdapter.
 */
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

    setContextRoot("/jsp_tagadapter_web");
    setTestJsp("TagAdapterTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagAdapterCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:285
   * 
   * @test_Strategy: Validates the constructor of the TagAdapter class.
   */
  public void tagAdapterCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "tagAdapterCtorTest");
    invoke();
  }

  /*
   * @testName: tagAdapterSetPageContextTest
   * 
   * @assertion_ids: JSP:JAVADOC:287
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.setPageContext() is made.
   */
  public void tagAdapterSetPageContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "tagAdapterSetPageContextTest");
    invoke();
  }

  /*
   * @testName: tagAdapterSetParentTest
   * 
   * @assertion_ids: JSP:JAVADOC:289
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.setParent() is made.
   */
  public void tagAdapterSetParentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "tagAdapterSetParentTest");
    invoke();
  }

  /*
   * @testName: tagAdapterGetParentTest
   * 
   * @assertion_ids: JSP:JAVADOC:290
   * 
   * @test_Strategy: Validates that getParent always returns
   * getAdaptee().getParent()
   */
  public void tagAdapterGetParentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "tagAdapterGetParentTest");
    invoke();
  }

  /*
   * @testName: tagAdapterDoStartTagTest
   * 
   * @assertion_ids: JSP:JAVADOC:293;JSP:JAVADOC:294
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.doStartTag() is made.
   */
  public void tagAdapterDoStartTagTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "tagAdapterDoStartTagTest");
    invoke();
  }

  /*
   * @testName: tagAdapterDoEndTagTest
   * 
   * @assertion_ids: JSP:JAVADOC:296;JSP:JAVADOC:297
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.doEndTag() is made.
   */
  public void tagAdapterDoEndTagTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "tagAdapterDoEndTagTest");
    invoke();
  }

  /*
   * @testName: tagAdapterReleaseTest
   * 
   * @assertion_ids: JSP:JAVADOC:299
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.release() is made.
   */
  public void tagAdapterReleaseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "tagAdapterReleaseTest");
    invoke();
  }

  /*
   * @testName: tagAdapterValidationTest
   * 
   * @assertion_ids: JSP:JAVADOC:286;JSP:JAVADOC:288;JSP:JAVADOC:291;
   * JSP:JAVADOC:292;JSP:JAVADOC:295;JSP:JAVADOC:298; JSP:JAVADOC:290
   * 
   * @test_Strategy: This validates that the container properly wraps a
   * SimpleTag instance with a TagAdapter when a Classic tag handler is a child
   * of the SimpleTag handler within the JSP Page. This also makes the
   * assumption, that all of the previous tests passed as it expects an
   * UnsupportedOperationException to be thrown if an illegal method call is
   * made on the TagAdapter.
   */
  public void tagAdapterValidationTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagadapter_web/TagAdapterValidationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
