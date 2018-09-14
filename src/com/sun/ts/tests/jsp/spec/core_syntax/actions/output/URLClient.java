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
 * @(#)URLClient.java	1.1 12/09/02
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.output;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_core_act_output_web";

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

    setContextRoot("/jsp_core_act_output_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspOutputUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that jsp:output can be used within JSP documents
   * and Tag files in XML syntax and that a translation-time error is raised if
   * used within the context of a standard syntax JSP or Tag file.
   */
  public void jspOutputUsageContextTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputUsageContextTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputUsageContextTest3.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>|<root></root>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputUsageContextTest4.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>|<root></root>");
    invoke();
  }

  /*
   * @testName: jspOutputOmitDeclValidValuesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the valid values for the omit-xml-declaration are
   * 'true', 'false', 'yes', and 'no'. If the attribute values are 'false' or
   * 'no', then the xml declaration will be generated. If 'true' or 'yes' no
   * declaration will be generated.
   */
  public void jspOutputOmitDeclValidValuesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest3.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest4.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest5.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest6.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest7.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest8.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
  }

  /*
   * @testName: jspOutputJspRootOmitDeclDefaultTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if a JSP document contains jsp:root, and a
   * jsp:output action is present without the omit-xml-declaration attribute,
   * the xml declaration will not be generated.
   */
  public void jspOutputJspRootOmitDeclDefaultTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputJspRootOmitDeclDefaultTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
  }

  /*
   * @testName: jspOutputOmitDeclDefaultTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if jsp:output is present in a JSP document
   * without a jsp:root element, and the omit-xml-declaration attribute is not
   * present, the default behavior is that an xml declaration is generated.
   */
  public void jspOutputOmitDeclDefaultTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclDefaultTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
  }

  /*
   * @testName: jspOutputOmitDeclDefaultTagTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if jsp:output is present in a Tag file in XML
   * syntax, and the omit-xml-declaration attribute is not present, the default
   * behavior will be the generation of an XML declaration.
   */
  public void jspOutputOmitDeclDefaultTagTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclDefaultTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
  }

  /*
   * @testName: jspOutputBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a translation error occurs if the body of
   * jsp:output is not empty.
   */
  public void jspOutputBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputBodyTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputBodyTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: simpleDefaultTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use a jsp document without jsp:output. response encoding is
   * not set so use default for jsp document
   */

  public void simpleDefaultTest() throws Fault {
    String testName = "simpleDefaultTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
  }

  /*
   * @testName: doctypeSystemTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: A DOCTYPE must be automatically output if and only if the
   * doctype-system element appears in the translation unit as part of a
   * <jsp:output> action. The format of the DOCTYPE: <!DOCTYPE nameOfRootElement
   * SYSTEM "doctypeSystem">
   */

  public void doctypeSystemTest() throws Fault {
    String testName = "doctypeSystemTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<!DOCTYPE html SYSTEM |http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd|Example XHTML Document");
    invoke();
  }

  /*
   * @testName: doctypeSystemPublicTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: A DOCTYPE must be automatically output if and only if the
   * doctype-system element appears in the translation unit as part of a
   * <jsp:output> action. The format of the DOCTYPE: <!DOCTYPE nameOfRootElement
   * PUBLIC "doctypePublic" "doctypeSystem">
   */

  public void doctypeSystemPublicTest() throws Fault {
    String testName = "doctypeSystemPublicTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<!DOCTYPE html PUBLIC |-//W3C//DTD XHTML Basic 1.0//EN|http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd|Example XHTML Document");
    invoke();
  }

  /*
   * @testName: negativeDoctypeRootTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: The doctype- root-element must appear and must only appear
   * if the doctype-system property appears, or a translation error must occur.
   */

  public void negativeDoctypeRootTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/negativeDoctypeRootNoSystem.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/negativeDoctypeSystemNoRoot.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDoctypePublicNoSystemTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: The doctype-public property is optional, but must not
   * appear unless the doctype-system property appears, or a translation error
   * must occur.
   */

  public void negativeDoctypePublicNoSystemTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/negativeDoctypePublicNoSystemTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeMultipleDoctypeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Multiple occurrences of the doctype-root-element,
   * doctype-system or doctype-public properties will cause a translation error
   * if the values for the properties differ from the previous occurrence.
   */

  public void negativeMultipleDoctypeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/negativeMultipleDoctypeTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
