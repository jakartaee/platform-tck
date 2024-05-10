/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.pages.spec.core_syntax.actions.usebean;


import java.io.IOException;
import java.io.InputStream;

import ee.jakarta.tck.pages.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {


  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {


    setGeneralURI("/jsp/spec/core_syntax/actions/usebean");
    setContextRoot("/jsp_coresyntx_act_usebean_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_act_usebean_web.war");
    archive.addClasses(Counter.class, NewCounter.class, String_IntBean.class);
    
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_act_usebean_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/date.ser")), "WEB-INF/classes/date.ser");


    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/serBeanName.jsp")), "serBeanName.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ScopeResult.jsp")), "ScopeResult.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/requestTimeBeanName.jsp")), "requestTimeBeanName.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSessionScopedObject.jsp")), "positiveSessionScopedObject.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRequestScopedObject.jsp")), "positiveRequestScopedObject.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positivePageScopedObject.jsp")), "positivePageScopedObject.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveNoBody.jsp")), "positiveNoBody.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveClassTypeCast.jsp")), "positiveClassTypeCast.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBodyNew.jsp")), "positiveBodyNew.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBeanNameTypeCast.jsp")), "positiveBeanNameTypeCast.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBeanNameType.jsp")), "positiveBeanNameType.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveApplicationScopedObject.jsp")), "positiveApplicationScopedObject.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTypeAssignable.jsp")), "negativeTypeAssignable.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSessionScopeFatalTranslationError.jsp")), "negativeSessionScopeFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeInvalidScope.jsp")), "negativeInvalidScope.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateIDFatalTranslationError.jsp")), "negativeDuplicateIDFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeClassCastExceptionFwd.jsp")), "negativeClassCastExceptionFwd.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeClassCastException.jsp")), "negativeClassCastException.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/errorPage.jsp")), "errorPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultScope.jsp")), "defaultScope.jsp");
    
    return archive;
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

  @Test
  public void positiveBeanNameTypeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveBeanNameType.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void positiveBeanNameTypeCastTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveBeanNameTypeCast.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void positiveBodyNewTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveBodyNew.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void positivePageScopedObjectTest() throws Exception {
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

  @Test
  public void positiveRequestScopedObjectTest() throws Exception {
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

  @Test
  public void positiveSessionScopedObjectTest() throws Exception {
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

  @Test
  public void positiveApplicationScopedObjectTest() throws Exception {
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

  @Test
  public void positiveNoBodyTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveNoBody.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void positiveClassTypeCastTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveClassTypeCast.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void negativeDuplicateIDFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeSessionScopeFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeClassCastExceptionTest() throws Exception {
    String testName = "negativeClassCastException";
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeClassCastException.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void negativeTypeAssignableTest() throws Exception {
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

  @Test
  public void negativeInvalidScopeTest() throws Exception {
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

  @Test
  public void defaultScopeTest() throws Exception {
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

  @Test
  public void requestTimeBeanNameTest() throws Exception {
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

  @Test
  public void serBeanNameTest() throws Exception {
    String testName = "serBeanName";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_usebean_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "1062014879125|Test PASSED");
    invoke();
  }

}
