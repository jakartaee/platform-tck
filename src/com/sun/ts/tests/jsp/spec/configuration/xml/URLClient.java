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

package com.sun.ts.tests.jsp.spec.configuration.xml;

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

    setContextRoot("/jsp_config_xml_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspConfigurationIsXmlUnspecTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if is-xml is not specified for a property
   * group, that the files matched by the url-pattern will not be considered JSP
   * documents.
   * 
   */
  public void jspConfigurationIsXmlUnspecTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_xml_web/unspec/NonXmlTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspConfigurationIsXmlFalseTest
   * 
   * @assertion_ids: JSP:SPEC:150.2
   * 
   * @test_Strategy: Validate that if is-xml is set to false for a property
   * group, that the files matched by the url-pattern will not be considered JSP
   * documents.
   */
  public void jspConfigurationIsXmlFalseTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_xml_web/xmlfalse/NonXmlTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspConfigurationIsXmlTrueTest
   * 
   * @assertion_ids: JSP:SPEC:150.1
   * 
   * @test_Strategy: Validate that if is-xml is set to true for a property
   * group, that the files matched by the url-pattern will be considered JSP
   * documents.
   */
  public void jspConfigurationIsXmlTrueTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_xml_web/xmltrue/XmlJspTest.xsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:page|directive");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
