/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)JSTLClient.java	1.2 03/19/02
 */

package com.sun.ts.tests.jstl.spec.sql.param;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jstl.common.client.SqlUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@Tag("jstl")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends SqlUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");


  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setGeneralURI("/jstl/spec/sql/param");
    setContextRoot("/jstl_sql_param_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_sql_param_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_sql_param_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDateParamTypeAttributeTest.jsp")), "negativeDateParamTypeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeParamQueryBodyContentTest.jsp")), "negativeParamQueryBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeParamQuerySQLAttributeTest.jsp")), "negativeParamQuerySQLAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeParamUpdateBodyContentTest.jsp")), "negativeParamUpdateBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamQueryDateTest.jsp")), "positiveDateParamQueryDateTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamQueryNoTypeTest.jsp")), "positiveDateParamQueryNoTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamQueryTimestampTest.jsp")), "positiveDateParamQueryTimestampTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamQueryTimeTest.jsp")), "positiveDateParamQueryTimeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamUpdateDateTest.jsp")), "positiveDateParamUpdateDateTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamUpdateNoTypeTest.jsp")), "positiveDateParamUpdateNoTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamUpdateTimestampTest.jsp")), "positiveDateParamUpdateTimestampTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamUpdateTimeTest.jsp")), "positiveDateParamUpdateTimeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamBodyContentQueryTest.jsp")), "positiveParamBodyContentQueryTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamBodyContentUpdateTest.jsp")), "positiveParamBodyContentUpdateTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamQueryBodyContentTest.jsp")), "positiveParamQueryBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamQueryMultiBodyContentTest.jsp")), "positiveParamQueryMultiBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamQueryMultiSQLAttributeTest.jsp")), "positiveParamQueryMultiSQLAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamQuerySQLAttributeTest.jsp")), "positiveParamQuerySQLAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamUpdateBodyContentTest.jsp")), "positiveParamUpdateBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamUpdateMultiBodyContentTest.jsp")), "positiveParamUpdateMultiBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamUpdateMultiSQLAttributeTest.jsp")), "positiveParamUpdateMultiSQLAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamUpdateSQLAttributeTest.jsp")), "positiveParamUpdateSQLAttributeTest.jsp");

    archive.addAsWebInfResource(JSTLClientIT.class.getPackage(), "tssql.stmt", "jstl-sql.properties");    

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveParamQuerySQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:63
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql attribute and sql:param action - Validate that
   * you get the expected number of rows back.
   */
  @Test
  public void positiveParamQuerySQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamQuerySQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamQuerySQLAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveParamQueryBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:63.5
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql:param action and passing the query as body
   * content. - Validate that you get the expected number of rows back.
   */
  @Test
  public void positiveParamQueryBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamQueryBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamQueryBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveParamQueryMultiSQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:63; JSTL:SPEC:63.1.1
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql attribute and multiple sql:param actions -
   * Validate that you get the expected number of rows back. - That the order of
   * the sql:params action is the order the placeholders in the query are filled
   * in.
   */
  @Test
  public void positiveParamQueryMultiSQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamQueryMultiSQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamQueryMultiSQLAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveParamQueryMultiBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:63; JSTL:SPEC:63.2
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using mutiple sql:param actions and passing the query as
   * body content. - Validate that you get the expected number of rows back. -
   * That the order of the sql:params action is the order the placeholders in
   * the query are filled in.
   */
  @Test
  public void positiveParamQueryMultiBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamQueryMultiBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamQueryMultiBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveParamUpdateMultiSQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:63
   * 
   * @testStrategy: Validate the behavior of the sql:update action - That a
   * query can be executed using sql attribute and multiple sql:param actions -
   * Validate that you get the expected number of rows back.
   */
  @Test
  public void positiveParamUpdateMultiSQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamUpdateMultiSQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveParamUpdateMultiSQLAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveParamUpdateMultiBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:63
   * 
   * @testStrategy: Validate the behavior of the sql:update action - That a
   * query can be executed using mutiple sql:param actions and passing the query
   * as body content. - Validate that you get the expected number of rows back.
   */
  @Test
  public void positiveParamUpdateMultiBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamUpdateMultiBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamUpdateMultiBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamQueryDateTest
   * 
   * @assertion_ids: JSTL:SPEC:94.4
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql:dateParam action specifying a type of 'date' -
   * Validate that you get the expected number of rows back.
   */
  @Test
  public void positiveDateParamQueryDateTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamQueryDateTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDateParamQueryDateTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamQueryTimeTest
   * 
   * @assertion_ids: JSTL:SPEC:94.4
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql:dateParam action specifying a type of 'time' -
   * Validate that you get the expected number of rows back.
   */
  @Test
  public void positiveDateParamQueryTimeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamQueryTimeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDateParamQueryTimeTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamQueryTimestampTest
   * 
   * @assertion_ids: JSTL:SPEC:94.2
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql:dateParam action specifying a type of 'timestamp'
   * - Validate that you get the expected number of rows back.
   */
  @Test
  public void positiveDateParamQueryTimestampTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamQueryTimestampTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDateParamQueryTimestampTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamQueryNoTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:94.5; JSTL:SPEC:94.5.1; JSTL:SPEC:94.5.2
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql:dateParam action without specifying a type
   * defaults to 'timestamp' - Validate that you get the expected number of rows
   * back.
   */
  @Test
  public void positiveDateParamQueryNoTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamQueryNoTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDateParamQueryNoTypeTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamUpdateDateTest
   * 
   * @assertion_ids: JSTL:SPEC:63
   * 
   * @testStrategy: Validate the behavior of the sql:update action - That a
   * query can be executed using sql:dateParam action specifying a type of
   * 'date'.
   */
  @Test
  public void positiveDateParamUpdateDateTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamUpdateDateTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDateParamUpdateDateTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamUpdateTimeTest
   * 
   * @assertion_ids: JSTL:SPEC:63
   * 
   * @testStrategy: Validate the behavior of the sql:update action - That a
   * query can be executed using sql:dateParam action specifying a type of
   * 'time'.
   */
  @Test
  public void positiveDateParamUpdateTimeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamUpdateTimeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDateParamUpdateTimeTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamUpdateTimestampTest
   * 
   * @assertion_ids: JSTL:SPEC:63
   * 
   * @testStrategy: Validate the behavior of the sql:update action - That a
   * query can be executed using sql:dateParam action specifying a type of
   * 'timestamp'.
   */
  @Test
  public void positiveDateParamUpdateTimestampTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamUpdateTimestampTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDateParamUpdateTimestampTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamUpdateNoTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:94.1.2
   * 
   * @testStrategy: Validate the behavior of the sql:update action - That a
   * query can be executed using sql:dateParam action without specifying a type
   * defaults to 'timestamp' - Validate that you get the expected number of rows
   * back.
   */
  @Test
  public void positiveDateParamUpdateNoTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamUpdateNoTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDateParamUpdateNoTypeTest");
    invoke();
  }

  /*
   * @testName: positiveParamUpdateBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:63.5; JSTL:SPEC:63.6.1
   * 
   * @testStrategy: Validate the behavior of the sql:update action - That a
   * query can be executed by specifying a paramater via sql:param action
   */
  @Test
  public void positiveParamUpdateBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamUpdateBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamUpdateBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveParamUpdateSQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:63
   * 
   * @testStrategy: Validate the behavior of the sql:update action using the sql
   * attribute - That a query can be executed by specifying a paramater via
   * sql:param action
   */
  @Test
  public void positiveParamUpdateSQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamUpdateSQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamUpdateSQLAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveParamBodyContentQueryTest
   * 
   * @assertion_ids: JSTL:SPEC:94; JSTL:SPEC:94.1.1
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed by specifying a paramater via sql:param action which passes
   * the parameter value as a String.
   */
  @Test
  public void positiveParamBodyContentQueryTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamBodyContentQueryTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamBodyContentQueryTest");
    invoke();
  }

  /*
   * @testName: positiveParamBodyContentUpdateTest
   * 
   * @assertion_ids: JSTL:SPEC:63; JSTL:SPEC:63.5
   * 
   * @testStrategy: Validate the behavior of the sql:update action - That a
   * query can be executed by specifying a paramater via sql:param action which
   * passes the parameter value as a String.
   */
  @Test
  public void positiveParamBodyContentUpdateTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamBodyContentUpdateTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamBodyContentUpdateTest");
    invoke();
  }

  /*
   * @testName: negativeParamQuerySQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:94; JSTL:SPEC:94.2.2
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * which specifies place holders using sql attribute, requires a sql:param
   * action - Validate that a JspException is thrown when the sql:param action
   * is omitted
   */
  @Test
  public void negativeParamQuerySQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeParamQuerySQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeParamQuerySQLAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeParamQueryBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:94; JSTL:SPEC:94.3
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * which specifies place holders and specifies the query as body content,
   * requires a sql:param action - Validate that a JspException is thrown when
   * the sql:param action is omitted
   */
  @Test
  public void negativeParamQueryBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeParamQueryBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeParamQueryBodyContentTest");
    invoke();
  }

  /*
   * @testName: negativeDateParamTypeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:94.4
   * 
   * @testStrategy: Validate the behavior of the sql:dateParam action - Validate
   * that a JspException is thrown when an invalid value is provided for the
   * type attribute.
   */
  @Test
  public void negativeDateParamTypeAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeDateParamTypeAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeDateParamTypeAttributeTest");
    invoke();
  }
}
