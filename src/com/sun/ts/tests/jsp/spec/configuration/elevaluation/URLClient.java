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

package com.sun.ts.tests.jsp.spec.configuration.elevaluation;

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

    setGeneralURI("/jsp/spec/configuration/elevaluation");
    setContextRoot("/jsp_config_eleval_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: elEvaluationUnspecifiedTest
   * 
   * @assertion_ids: JSP:SPEC:254
   * 
   * @test_Strategy: Validate that if the web application uses a 2.4 deployment
   * descriptor and the jsp-property-group element nor the JSP identified by the
   * jsp-property-group specifies no EL evaluation information, EL will be
   * evaluated by the container. This validates both JSPs in standard syntax and
   * JSP documents.
   */
  public void elEvaluationUnspecifiedTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elunspec/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elunspecx/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: elEvaluationConfigurationFalseTest
   * 
   * @assertion_ids: JSP:SPEC:142
   * 
   * @test_Strategy: Validate that if the web application uses a 2.4 deployment
   * descriptor and the jsp-property-group element sets the el-ignored element
   * to false, and the JSP page specifies no special EL handling, that EL
   * expressions will be evaluated. This validates both JSPs in standard syntax
   * and JSP documents.
   */
  public void elEvaluationConfigurationFalseTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elconffalse/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elconffalsex/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: elEvaluationConfigurationTrueTest
   * 
   * @assertion_ids: JSP:SPEC:141
   * 
   * @test_Strategy: Validate that if the web application uses a 2.4 deployment
   * descriptor and the jsp-property-group element sets the el-ignored element
   * to true, and the JSP page specifies no special EL handling, that EL
   * expressions will not be evaluated. This validates both JSPs in standard
   * syntax and JSP documents.
   */
  public void elEvaluationConfigurationTrueTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elconftrue/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elconftruex/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
  }

  /*
   * @testName: elEvaluationPageDirectiveOverrideTest
   * 
   * @assertion_ids: JSP:SPEC:255
   * 
   * @test_Strategy: Validate that if the web application uses a 2.4 deployment
   * descriptor, that the page directive attribute isELIgnored takes precedence
   * over the configuration of the JSP property group.
   */
  public void elEvaluationPageDirectiveOverrideTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elpagetrue/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elpagetruex/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elpagefalse/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elpagefalsex/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: elEvaluation23WebApplicationTest
   * 
   * @assertion_ids: JSP:SPEC:252
   * 
   * @test_Strategy: Validate a JSP 2.0 container when presented with a 2.3
   * based web application, and it encounters a JSP with an EL-like construct
   * (i.e. ${expr}), that EL Evaluation is not performed.
   */
  public void elEvaluation23WebApplicationTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval23_web/ElCompatTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
  }
}
