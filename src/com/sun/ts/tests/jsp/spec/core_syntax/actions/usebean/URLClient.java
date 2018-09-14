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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean;

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

    setGeneralURI("/jsp/spec/core_syntax/actions/usebean");
    setContextRoot("/jsp_coresyntx_act_usebean_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveBeanNameTypeTest
   * 
   * @assertion_ids: JSP:SPEC:155.1;JSP:SPEC:156;JSP:SPEC:162.2;JSP:SPEC:168.8
   * 
   * @test_Strategy: Use jsp:useBean to create a bean where the beanName and
   * type attributes have the same values. Verify that the bean can be used by
   * invoking a method on the bean inside a scriplet.
   */

  public void positiveBeanNameTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveBeanNameType");
    invoke();
  }

  /*
   * @testName: positiveBeanNameTypeCastTest
   * 
   * @assertion_ids: JSP:SPEC:162.1
   * 
   * @test_Strategy: Use jsp:useBean to create a bean where the beanName
   * specifies one particular type, and type specifies a superclass of the value
   * specified by beanName. Verify that the bean can be used by invoking a
   * method on the bean inside a scriplet.
   */

  public void positiveBeanNameTypeCastTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveBeanNameTypeCast");
    invoke();
  }

  /*
   * @testName: positiveBodyNewTest
   * 
   * @assertion_ids: JSP:SPEC:161.7.1
   * 
   * @test_Strategy: Using jsp:useBean, create a new instance. Within the body
   * of the jsp:useBean action, use jsp:setProperty to initialize a Bean
   * property. After closing the jsp:useBean action, use jsp:getProperty to
   * validate that the property was indeed set.
   */

  public void positiveBodyNewTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveBodyNew");
    invoke();
  }

  /*
   * @testName: positivePageScopedObjectTest
   * 
   * @assertion_ids: JSP:SPEC:158; JSP:SPEC:158.2;JSP:SPEC:8
   * 
   * @test_Strategy: In one JSP page, create a new bean object using jsp:useBean
   * with the scope set to "page". After the object has been created, forward
   * the request to a second JSP page to validate that an object associated with
   * the same ID used in the first JSP page is not available in the current
   * PageContext.
   */

  public void positivePageScopedObjectTest() throws Fault {
    String testName = "positivePageScopedObject";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "status:Test Status=PASSED");
    invoke();
  }

  /*
   * @testName: positiveRequestScopedObjectTest
   * 
   * @assertion_ids: JSP:SPEC:158; JSP:SPEC:158.3;JSP:SPEC:9
   * 
   * @test_Strategy: In one JSP page, create a new bean object using jsp:useBean
   * with the scope set to "request". After the object has been created, forward
   * the request to a second JSP page to validate that an object associated with
   * the same ID used in the first JSP page is available in the current
   * HttpServletRequest.
   */

  public void positiveRequestScopedObjectTest() throws Fault {
    String testName = "positiveRequestScopedObject";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "status:Test Status=PASSED");
    invoke();
  }

  /*
   * @testName: positiveSessionScopedObjectTest
   * 
   * @assertion_ids: JSP:SPEC:158; JSP:SPEC:158.4;JSP:SPEC:10
   * 
   * @test_Strategy: In one JSP page, create a new bean object using jsp:useBean
   * with the scope set to "session". After the object has been created, forward
   * the request to a second JSP page to validate that an object associated with
   * the same ID used in the first JSP page is available in the current
   * HttpSession.
   */

  public void positiveSessionScopedObjectTest() throws Fault {
    String testName = "positiveSessionScopedObject";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "status:Test Status=PASSED");
    invoke();
  }

  /*
   * @testName: positiveApplicationScopedObjectTest
   * 
   * @assertion_ids: JSP:SPEC:158; JSP:SPEC:158.5;JSP:SPEC:11
   * 
   * @test_Strategy: In one JSP page, create a new bean object using jsp:useBean
   * with the scope set to "application". After the object has been created,
   * forward the request to a second JSP page to validate that an object
   * associated with the same ID used in the first JSP page is available in the
   * current ServletContext.
   */

  public void positiveApplicationScopedObjectTest() throws Fault {
    String testName = "positiveApplicationScopedObject";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "status:Test Status=PASSED");
    invoke();
  }

  /*
   * @testName: positiveNoBodyTest
   * 
   * @assertion_ids: JSP:SPEC:161.5
   * 
   * @test_Strategy: Explicit test to ensure that the jsp:useBean action can be
   * used without a body.
   */

  public void positiveNoBodyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveNoBody");
    invoke();
  }

  /*
   * @testName: positiveClassTypeCastTest
   * 
   * @assertion_ids: JSP:SPEC:161.8
   * 
   * @test_Strategy: Create a new bean instance with a particular class set for
   * the class attribute, and a parent class for the type attribute. Validate
   * That the instance is cast without an Exception.
   */

  public void positiveClassTypeCastTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveClassTypeCast");
    invoke();
  }

  /*
   * @testName: negativeDuplicateIDFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:157
   * 
   * @test_Strategy: Create two beans with the same id attribute. Validate that
   * a Fatal Translation error occurs.
   */

  public void negativeDuplicateIDFatalTranslationErrorTest() throws Fault {
    String testName = "negativeDuplicateIDFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeSessionScopeFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:10;JSP:SPEC:159
   * 
   * @test_Strategy: Use the page directive to set the session attribute to
   * false and then declare a bean with session scope. Validate that a Fatal
   * Translation error occurs.
   */

  public void negativeSessionScopeFatalTranslationErrorTest() throws Fault {
    String testName = "negativeSessionScopeFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeClassCastExceptionTest
   * 
   * @assertion_ids: JSP:SPEC:161.4
   * 
   * @test_Strategy: In one JSP page, declare a bean of a particular type with
   * session scope. Once declared, this page will forward to a second JSP page
   * which will try to reference the previously declared bean in the session
   * scope, but will define the type attribute with an incompatible type.
   */

  public void negativeClassCastExceptionTest() throws Fault {
    String testName = "negativeClassCastException";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + "Fwd.jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativeTypeAssignableTest
   * 
   * @assertion_ids: JSP:SPEC:152
   * 
   * @test_Strategy: both type and class attributes are present and class is not
   * assignable to type
   */

  public void negativeTypeAssignableTest() throws Fault {
    String testName = "negativeTypeAssignable";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeInvalidScopeTest
   * 
   * @assertion_ids: JSP:SPEC:158.6
   * 
   * @test_Strategy: both type and class attributes are present and class is not
   * assignable to type
   */

  public void negativeInvalidScopeTest() throws Fault {
    String testName = "negativeInvalidScope";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultScopeTest
   * 
   * @assertion_ids: JSP:SPEC:158.1
   * 
   * @test_Strategy: check if the default scope is page
   */

  public void defaultScopeTest() throws Fault {
    String testName = "defaultScope";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: requestTimeBeanNameTest
   * 
   * @assertion_ids: JSP:SPEC:154; JSP:SPEC:155
   * 
   * @test_Strategy: use a request-time attribute expression for beanName
   */

  public void requestTimeBeanNameTest() throws Fault {
    String testName = "requestTimeBeanName";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: serBeanNameTest
   * 
   * @assertion_ids: JSP:SPEC:155; JSP:SPEC:152
   * 
   * @test_Strategy: use beanName of the form a.b.c.ser
   */

  public void serBeanNameTest() throws Fault {
    String testName = "serBeanName";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "1062014879125|Test PASSED");
    invoke();
  }

}
