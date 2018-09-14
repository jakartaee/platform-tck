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
 * @(#)URLClient.java	1.36 02/11/04
 */

package com.sun.ts.tests.jsp.spec.tldres;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

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

    setContextRoot("/jsp_tldres_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: tldResourcePathJsp11Test
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a taglibrary packaged as specified by the JSP
   * 1.1 specification will be properly added to the containers taglib map and
   * that the defined tag within can be used.
   */
  public void tldResourcePathJsp11Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPath11Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Tld11Tag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResourcePathMultiTldJarTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the container can properly process a JAR
   * containing multiple TLDs and adds the TLDs that are found containing a
   * <uri> element to the taglib map.
   */
  public void tldResourcePathMultiTldJarTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathMultiTldTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Multi1Tag: Test PASSED|Multi2Tag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResourcePathWebInfUriTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if a TLD exists in the WEB-INF directory or
   * some subdirectory thereof, and that TLD contains a <uri> element, the
   * container will add that taglibrary to the taglibrary map.
   */
  public void tldResourcePathWebInfUriTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathWebInfUriTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "UriTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResourcePathWebXmlTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Valdiate that the container adds any explicit taglibrary
   * entries found in the deployment descriptor to the taglib map and that the
   * tag or tags within can be properly used.
   */
  public void tldResourcePathWebXmlTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathExplicitWebXmlTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "WebXmlTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResourcePathDirectTldReference
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the TLD can be directly referenced in the taglib
   * directive and that the tag(s) declared in this TLD can be properly used.
   */
  public void tldResourcePathDirectTldReference() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathDirectTldReferenceTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "WebXmlTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldExplicitWebXmlPrecedenceTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that tag library entries explicity defined in the
   * web deployment descriptor (web.xml) have precedence over other tag
   * libraries defined with the same URI.
   */
  public void tldExplicitWebXmlPrecedenceTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldExplicitWebXmlPrecedenceTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "WebXmlTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResPathRelativeUriTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate a tag library can be resolved when referenced
   * though a relative path (no leading '/'). No translation error should occur
   * and the tag should be usable within the translation unit.
   */
  public void tldResPathRelativeUriTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/path/TldResPathRelativeUriTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "WebXmlTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResPathAbsUriNotFoundTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if the uri attribute of the taglib directive
   * is an absolute URI that is not present in the container's tag library map,
   * that a translation time error is raised.
   */
  public void tldResPathAbsUriNotFoundTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathAbsUriNotFoundTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: tld12DefaultBodyContentTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: omit body-content in a 1.2 style tld and a container is
   * required to apply default JSP.
   */

  public void tld12DefaultBodyContentTest() throws Fault {
    String testName = "tld12DefaultBodyContent";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeJSPPrefixTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use jsp prefix for an element that is not a standard action
   * and expect a translation error.
   */

  public void negativeJSPPrefixTest() throws Fault {
    String testName = "negativeJSPPrefix";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: listenerTldTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: A container is required to read listener element in all TLD
   * files and treat them as extension to web.xml.
   */

  public void listenerTldTest() throws Fault {
    String testName = "listenerTldTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "session created meta inf.|session created meta inf sub.|session created web inf.|session created web inf sub.");
    invoke();
  }

  /*
   * @testName: negativeTaglibAfterActionTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: It is a fatal translation error for the taglib directive to
   * appear after actions or functions using the prefix.
   */
  public void negativeTaglibAfterActionTest() throws Fault {
    String testName = "negativeTaglibAfterActionTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
