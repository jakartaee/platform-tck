/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates and others.
 * All rights reserved.
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
 * @(#)URLClient.java	1.2 05/05/03
 */

package com.sun.ts.tests.jsp.spec.el.jsp;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.jsp.common.tags.tck.SetTag;
import com.sun.ts.tests.common.el.spec.Book;

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



  public URLClientIT() throws Exception {


    setGeneralURI("/jsp/spec/el/jsp/");
    setContextRoot("/jsp_el_jsp_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_el_jsp_web.war");
    archive.addClasses(JspTestUtil.class,
          SetTag.class,
          Book.class
    );
    archive.addPackages(true, Filters.exclude(URLClientIT.class),
            URLClientIT.class.getPackageName());
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_el_jsp_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/el_jsp.tld", "el_jsp.tld");    

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TemplateTextPoundTranslationError.jsp")), "TemplateTextPoundTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagLibraryPoundTranslationError.jsp")), "TagLibraryPoundTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveELDeferredValueValue.jsp")), "positiveELDeferredValueValue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveELDeferredMethodValue.jsp")), "positiveELDeferredMethodValue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeELDeferredValueValue.jsp")), "negativeELDeferredValueValue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeELDeferredMethodValue.jsp")), "negativeELDeferredMethodValue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ImplicitELImport.jsp")), "ImplicitELImport.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ELDeferredValueCoercion.jsp")), "ELDeferredValueCoercion.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ELDeferredMethodStringLiteralError2.jsp")), "ELDeferredMethodStringLiteralError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ELDeferredMethodStringLiteralError1.jsp")), "ELDeferredMethodStringLiteralError1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ELDeferredMethodStringLiteral.jsp")), "ELDeferredMethodStringLiteral.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DynamicAttributeSetterMethod.jsp")), "DynamicAttributeSetterMethod.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/AllowedDynamicAttributeValueTypes.jsp")), "AllowedDynamicAttributeValueTypes.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveELDeferredValueValueTest
   * 
   * @assertion_ids: JSP:SPEC:282
   * 
   * @test_Strategy: [ELDeferredValueValue] In a jsp page, pass a String literal
   * expression and an expression using the #{} syntax to a tag handler via a
   * deferred-value element in the tld file. Verify that the expressions are
   * accepted and that their values are transmitted correctly to the tag
   * handler.
   */
  @Test
  public void positiveELDeferredValueValueTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/positiveELDeferredValueValue.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeELDeferredValueValueTest
   * 
   * @assertion_ids: JSP:SPEC:282
   * 
   * @test_Strategy: [ELDeferredValueValue] In a jsp page, pass an expression
   * using the ${} syntax to a tag handler via a deferred-value element in the
   * tld file. Verify that a page translation error occurs.
   */
  @Test
  public void negativeELDeferredValueValueTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/negativeELDeferredValueValue.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: eLDeferredValueCoercionTest
   * 
   * @assertion_ids: JSP:SPEC:284
   * 
   * @test_Strategy: [ELDeferredValueCoercion] In a jsp page, pass a String
   * literal expression and an expression using the #{} syntax to a tag handler
   * via a deferred-value element in the tld file. Verify that the expressions
   * are accepted, that their values are coerced to the expected type and
   * evaluate as expected.
   */
  @Test
  public void eLDeferredValueCoercionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ELDeferredValueCoercion.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveELDeferredMethodValueTest
   * 
   * @assertion_ids: JSP:SPEC:287
   * 
   * @test_Strategy: [ELDeferredMethodValue] In a jsp page, pass a String
   * literal expression and an expression using the #{} syntax to a tag handler
   * via a deferred-method element in the tld file. Verify that the expressions
   * are accepted and that their values are transmitted correctly to the tag
   * handler.
   */
  @Test
  public void positiveELDeferredMethodValueTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/positiveELDeferredMethodValue.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeELDeferredMethodValueTest
   * 
   * @assertion_ids: JSP:SPEC:287
   * 
   * @test_Strategy: [ELDeferredMethodValue] In a jsp page, pass an expression
   * using the ${} syntax to a tag handler via a deferred-method element in the
   * tld file. Verify that a page translation error occurs.
   */
  @Test
  public void negativeELDeferredMethodValueTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/negativeELDeferredMethodValue.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: eLDeferredMethodStringLiteralTest
   * 
   * @assertion_ids: JSP:SPEC:288
   * 
   * @test_Strategy: [ELDeferredMethodStringLiteral] In a jsp page, pass a
   * literal expression representing a String and a literal expression
   * representing a Double to a tag handler via a deferred-method element in the
   * tld file. Verify that the expressions are accepted, that they are coerced
   * to the proper type and have the expected values.
   */
  @Test
  public void eLDeferredMethodStringLiteralTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ELDeferredMethodStringLiteral.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: eLDeferredMethodStringLiteralErrorTest1
   * 
   * @assertion_ids: JSP:SPEC:289
   * 
   * @test_Strategy: [ELDeferredMethodStringLiteralError] In a jsp page, pass a
   * string literal to a tag handler via a deferred-method element in the tld
   * file where the method signature -element has a return value of void. Verify
   * that a page translation error occurs.
   */
  @Test
  public void eLDeferredMethodStringLiteralErrorTest1() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ELDeferredMethodStringLiteralError1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: eLDeferredMethodStringLiteralErrorTest2
   * 
   * @assertion_ids: JSP:SPEC:289
   * 
   * @test_Strategy: [ELDeferredMethodStringLiteralError] In a jsp page, pass a
   * string literal to a tag handler via a deferred-method element in the tld
   * file where the string literal cannot be coerced to the return type of the
   * method signature. Verify that a page translation error occurs.
   */
  @Test
  public void eLDeferredMethodStringLiteralErrorTest2() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ELDeferredMethodStringLiteralError2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: templateTextPoundTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:272
   * 
   * @test_Strategy: [TemplateTextPoundTranslationError] Verify that the #{
   * character sequence, when embedded in template text, triggers a translation
   * error.
   */
  @Test
  public void templateTextPoundTranslationErrorTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/TemplateTextPoundTranslationError.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: tagLibraryPoundTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:273
   * 
   * @test_Strategy: [TagLibraryPoundTranslationError] Verify that the #{
   * character sequence triggers a translation error if used for a tag attribute
   * of a tag library where the jsp-version is greater than or equal to 2.1, and
   * for which the attribute is not marked as a deferred expression in the TLD.
   */
  @Test
  public void tagLibraryPoundTranslationErrorTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/TagLibraryPoundTranslationError.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: allowedDynamicAttributeValueTypesTest
   * 
   * @assertion_ids: JSP:SPEC:290
   * 
   * @test_Strategy: [AllowedDynamicAttributeValueTypes] In a jsp page, pass a
   * literal expression, a scriptlet expression, an expression using the ${}
   * syntax, and deferred-value and deferred-method expressions using the #{}
   * syntax as dynamic attributes to a tag handler. Verify that the expressions
   * are accepted.
   */
  @Test
  public void allowedDynamicAttributeValueTypesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/AllowedDynamicAttributeValueTypes.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: dynamicAttributeSetterMethodTest
   * 
   * @assertion_ids: JSP:SPEC:291
   * 
   * @test_Strategy: [DynamicAttributeSetterMethod] Verify that an argument to
   * the setter method of a dynamic attribute must be of type java.lang.Object.
   * Implement a tag handler with a setter method whose argument is a primitive
   * type and see that the container generates an internal server error.
   */
  @Test
  public void dynamicAttributeSetterMethodTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/DynamicAttributeSetterMethod.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }


  /*
   * @testName: implicitImportTest
   * 
   * @assertion_ids: JSP:SPEC:35
   * 
   * @test_Strategy: [ImplicitELImport] Verify that each of the implicit package
   * is available to the EL environment.
   */
  @Test
  public void implicitImportTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ImplicitELImport.jsp HTTP/1.1");
    invoke();
  }
}
