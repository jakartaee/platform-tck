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

package com.sun.ts.tests.jsp.spec.jspdocument.general;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_jspdocument_general_web";

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

    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: negativeWellFormednessTest
   * 
   * @assertion_ids: JSP:SPEC:173.4
   * 
   * @test_Strategy: access a jsp document that is not well-formed.
   */

  public void negativeWellFormednessTest() throws Fault {
    String testName = "negativeWellFormedness";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: identifyByJspRootTest
   * 
   * @assertion_ids: JSP:SPEC:173; JSP:SPEC:173.3
   * 
   * @test_Strategy: access a jsp page that has a jsp:root as top element
   */

  public void identifyByJspRootTest() throws Fault {
    String testName = "identifyByJspRoot";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: identifyByExtensionTest
   * 
   * @assertion_ids: JSP:SPEC:173; JSP:SPEC:173.2; JSP:SPEC:176
   * 
   * @test_Strategy: identify a jsp document by .jspx extension a jsp document
   * does not need to have jsp:root
   */

  public void identifyByExtensionTest() throws Fault {
    String testName = "identifyByExtension";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: identifyByConfigTest
   * 
   * @assertion_ids: JSP:SPEC:173; JSP:SPEC:173.1; JSP:SPEC:176
   * 
   * @test_Strategy: identify a jsp document by jsp-property-group via is-xml a
   * jsp document does not need to have jsp:root
   */

  public void identifyByConfigTest() throws Fault {
    String testName = "identifyByConfig";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativeDTDValidationTest
   * 
   * @assertion_ids: JSP:SPEC:260
   * 
   * @test_Strategy: access an invalid jsp document and expect translation
   * error.
   */

  public void negativeDTDValidationTest() throws Fault {
    String testName = "negativeDTDValidation";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: invalidPlainURITest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error must not be generated if the given
   * plain uri is not found in the taglib map.
   */

  public void invalidPlainURITest() throws Fault {
    String testName = "invalidPlainURI";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: tagDependentTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: validate that tagdependent body should be passed verbatim,
   * tag handles inside body must not be invoked.
   */

  public void tagDependentTest() throws Fault {
    String testName = "tagDependentTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "START ${pageScope.eval}|"
            + "jsp:scriptlet|out.println(\"scriptlet\");|jsp:scriptlet|"
            + "jsp:useBean|java.util.Date|" + "jsp:getProperty|property|" +

            "use jsp:body|" +

            "START ${pageScope.eval}|"
            + "jsp:scriptlet|out.println(\"scriptlet\");|jsp:scriptlet|"
            + "jsp:useBean|java.util.Date|" + "jsp:getProperty|property|"
            + "Expression from attribute: 18|" +

            "END 72");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "evaluated");
    invoke();
  }

}
