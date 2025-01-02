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

package com.sun.ts.tests.jstl.spec.sql.query;

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
    setGeneralURI("/jstl/spec/sql/query");
    setContextRoot("/jstl_sql_query_web");
    setGoldenFileDir("/jstl/spec/sql/query");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_sql_query_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_sql_query_web.xml"));
    archive.addClasses(ResultSetQueryTag.class);
    archive.addAsWebInfResource(JSTLClientIT.class.getPackage(), "WEB-INF/resultSetQuery.tld", "resultSetQuery.tld");

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryBodyContentTest.jsp")), "negativeQueryBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryDataSourceAttributeEmptyTest.jsp")), "negativeQueryDataSourceAttributeEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryDataSourceAttributeTest.jsp")), "negativeQueryDataSourceAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryDataSourceNullAttributeTest.jsp")), "negativeQueryDataSourceNullAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryMaxRowsAttributeTest2.jsp")), "negativeQueryMaxRowsAttributeTest2.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryMaxRowsConfigTest.jsp")), "negativeQueryMaxRowsConfigTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryMaxRowsConfigTest2.jsp")), "negativeQueryMaxRowsConfigTest2.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryNoVarAttributeTest.jsp")), "negativeQueryNoVarAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryScopeAttributeTest.jsp")), "negativeQueryScopeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQuerySQLAttributeTest.jsp")), "negativeQuerySQLAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeQueryVarAttributeTest.jsp")), "negativeQueryVarAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryBodyContentTest.jsp")), "positiveQueryBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryDataSourceAttributeDataSourceTest.jsp")), "positiveQueryDataSourceAttributeDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryDataSourceAttributeDriverManagerTest.jsp")), "positiveQueryDataSourceAttributeDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryDataSourceConfigDataSourceTest.jsp")), "positiveQueryDataSourceConfigDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryDataSourceConfigDriverManagerTest.jsp")), "positiveQueryDataSourceConfigDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryDataSourceConfigPrecedenceTest.jsp")), "positiveQueryDataSourceConfigPrecedenceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryEmptyResultTest.jsp")), "positiveQueryEmptyResultTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryMaxRowsAttributeTest.jsp")), "positiveQueryMaxRowsAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryMaxRowsConfigTest.jsp")), "positiveQueryMaxRowsConfigTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryMaxRowsIntegerConfigTest.jsp")), "positiveQueryMaxRowsIntegerConfigTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryScopeAttributeTest.jsp")), "positiveQueryScopeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQuerySQLAttributeTest.jsp")), "positiveQuerySQLAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryStartRowAttributeTest.jsp")), "positiveQueryStartRowAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryVarAttributeTest.jsp")), "positiveQueryVarAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultSupportTest.jsp")), "positiveResultSupportTest.jsp");

    archive.addAsWebInfResource(JSTLClientIT.class.getPackage(), "tssql.stmt", "jstl-sql.properties");    

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveQueryBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:59; JSTL:SPEC:59.2; JSTL:SPEC:59.9;
   * JSTL:SPEC:59.2.2
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That
   * dataSource expression can be an DataSource - Specify a simple sql query
   * within the body of the sql:query acton and validate that you get the
   * expected number of rows back.
   */
  @Test
  public void positiveQueryBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQueryBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveQueryDataSourceAttributeDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:59.2.2
   * 
   * @testStrategy: Validate the sql:query use of the dataSource attribute -
   * That a query can be successfully executed when the dataSource attribute is
   * passed an instance of a DataSource.
   */
  @Test
  public void positiveQueryDataSourceAttributeDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryDataSourceAttributeDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveQueryDataSourceAttributeDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveQueryDataSourceAttributeDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of the dataSource attribute and
   * setting it to a String representing JDBC DriverManager parameters. The
   * query is passed as body content
   */
  @Test
  public void positiveQueryDataSourceAttributeDriverManagerTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryDataSourceAttributeDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveQueryDataSourceAttributeDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveQuerySQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.1; JSTL:SPEC:59.1.1
   * 
   * @testStrategy: Validate the behavior of the sql:query action utilizing a
   * sql attribute - create a sql:query action which utilizes a sql attribute
   * for defining the query and validate that you get the expected number of
   * rows back.
   */
  @Test
  public void positiveQuerySQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQuerySQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQuerySQLAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveQueryScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.6; JSTL:SPEC:59.6.1; JSTL:SPEC:59.6.2;
   * JSTL:SPEC:59.6.3; JSTL:SPEC:59.6.4; JSTL:SPEC:59.14
   * 
   * @testStrategy: Validate that the action exports var to the specified scope
   * as well as validating that if scope is not specified, var will be exported
   * to the page scope by default.
   */
  @Test
  public void positiveQueryScopeAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryScopeAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQueryScopeAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveQueryVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.5;JSTL:SPEC:59.5.1
   * 
   * @testStrategy: Validate that the var attribute within a <sql:query> action
   * is of type jakarta.servlet.jsp.jstl.sql.Result.
   */
  @Test
  public void positiveQueryVarAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryVarAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQueryVarAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveQueryEmptyResultTest
   * 
   * @assertion_ids: JSTL:SPEC:59.8
   * 
   * @testStrategy: Validate that if a sql query returns no rows, that an empty
   * Result object is returned
   */
  @Test
  public void positiveQueryEmptyResultTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryEmptyResultTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQueryEmptyResultTest");
    invoke();
  }

  /*
   * @testName: positiveQueryMaxRowsAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.3; JSTL:SPEC:59.3.1;JSTL:SPEC:59.3.2
   * 
   * @testStrategy: Validate the sql:query use of the maxRows attribute. - That
   * all rows returned if maxRows not specified. - That all rows returned if
   * maxRows is '-1'. - That 'maxRows'rows returned if maxRows specified. - That
   * 'maxRows' can be specified as an expression or specified directly.
   */
  @Test
  public void positiveQueryMaxRowsAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryMaxRowsAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQueryMaxRowsAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveQueryStartRowAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.4; JSTL:SPEC:59.4.1; JSTL:SPEC:59.4.2
   * 
   * @testStrategy: Validate the sql:query use of the startRow attribute - That
   * startRow expression must be an int type - That all rows returned if
   * startRow not specified. - That all rows returned if startRow equals 0. -
   * That the correct starting row is returned. This is validated by utilizing a
   * sorted query. - The correct number of rows is returned when startRow is
   * used with maxRows - That 'startRow' can be specified as an expression or
   * specified directly.
   */
  @Test
  public void positiveQueryStartRowAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryStartRowAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQueryStartRowAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveQueryMaxRowsConfigTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of the configuration parameter
   * jakarta.servlet.jsp.sql.maxRows passed as a String - That the number of rows
   * returned matches the value specified by the config param. - That all rows
   * are returned if the config param is '-1'. - That maxRows attribute takes
   * precedence over the config param.
   */
  @Test
  public void positiveQueryMaxRowsConfigTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryMaxRowsConfigTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQueryMaxRowsConfigTest");
    invoke();
  }

  /*
   * @testName: positiveQueryMaxRowsIntegerConfigTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of the configuration parameter
   * jakarta.servlet.jsp.sql.maxRows passed an Integer object - That the number of
   * rows returned matches the value specified by the config param. - That all
   * rows are returned if the config param is '-1'. - That maxRows attribute
   * takes precedence over the config param.
   */
  @Test
  public void positiveQueryMaxRowsIntegerConfigTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryMaxRowsIntegerConfigTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveQueryMaxRowsIntegerConfigTest");
    invoke();
  }

  /*
   * @testName: positiveQueryDataSourceConfigPrecedenceTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate sql:query action dataSource attribute takes
   * precedence over the configuration parameter
   * jakarta.servlet.jsp.jstl.sql.dataSource.
   */
  @Test
  public void positiveQueryDataSourceConfigPrecedenceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryDataSourceConfigPrecedenceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveQueryDataSourceConfigPrecedenceTest");
    invoke();
  }

  /*
   * @testName: positiveQueryDataSourceConfigDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate sql:query action utilizing the configuration
   * parameter jakarta.servlet.jsp.jstl.sql.dataSource and setting it to a
   * DataSource Object. The query is passed as body content.
   */
  @Test
  public void positiveQueryDataSourceConfigDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryDataSourceConfigDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveQueryDataSourceConfigDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveQueryDataSourceConfigDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate sql:query action utilizing the configuration
   * parameter jakarta.servlet.jsp.jstl.sql.dataSource and setting it to a String
   * representing JDBC DriverManager parameters. The query is passed as body
   * content
   */
  @Test
  public void positiveQueryDataSourceConfigDriverManagerTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryDataSourceConfigDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveQueryDataSourceConfigDriverManagerTest");
    invoke();
  }

  /*
   * @testName: negativeQuerySQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.1.2
   * 
   * @testStrategy: Validate the sql:query use of the sql attribute - That a
   * JspException is thrown when an invalid value is specified for sql.
   */
  @Test
  public void negativeQuerySQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeQuerySQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeQuerySQLAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeQueryMaxRowsAttributeTest2
   * 
   * @assertion_ids: JSTL:SPEC:59.3; JSTL:SPEC:59.3.3
   * 
   * @testStrategy: Validate the sql:query use of the maxRows attribute - That a
   * JspException is thrown when an value < -1 is specified for maxRows.
   */
  @Test
  public void negativeQueryMaxRowsAttributeTest2() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeQueryMaxRowsAttributeTest2.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeQueryMaxRowsAttributeTest2");
    invoke();
  }

  /*
   * @testName: negativeQueryMaxRowsConfigTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of
   * jakarta.servlet.jsp.sql.jstl.maxRows config parameter - That a JspException
   * is thrown when an invalid value is specified.
   */
  @Test
  public void negativeQueryMaxRowsConfigTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeQueryMaxRowsConfigTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeQueryMaxRowsConfigTest");
    invoke();
  }

  /*
   * @testName: negativeQueryMaxRowsConfigTest2
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of
   * jakarta.servlet.jsp.sql.jstl.maxRows config parameter - That a JspException
   * is thrown when an value < -1 is specified.
   */
  @Test
  public void negativeQueryMaxRowsConfigTest2() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeQueryMaxRowsConfigTest2.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeQueryMaxRowsConfigTest2");
    invoke();
  }

  /*
   * @testName: negativeQueryScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.6.5
   * 
   * @testStrategy: Validate that if a sql:query utilizes the scope attribute
   * that is invalid, that a translation error will occur.
   */
  @Test
  public void negativeQueryScopeAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeQueryScopeAttributeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeQueryScopeAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeQueryNoVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query action that when the var attribute is
   * not specified, a translation error occurs.
   */
  @Test
  public void negativeQueryNoVarAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeQueryNoVarAttributeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeQueryNoVarAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeQueryVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:69.2.1
   * 
   * @testStrategy: Validate the sql:query action that when the var attribute is
   * empty, a translation error occurs.
   */
  @Test
  public void negativeQueryVarAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeQueryVarAttributeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeQueryVarAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeQueryBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:69.1
   * 
   * @testStrategy: Validate the sql:query action use of the body content - That
   * a JspException is thrown when an invalid value is specified for sql. via
   * body content.
   */
  @Test
  public void negativeQueryBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeQueryBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeQueryBodyContentTest");
    invoke();
  }

  /*
   * @testName: negativeQueryDataSourceAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.2.3
   * 
   * @testStrategy: Validate the sql:query use of the dataSource attribute -
   * That a JspException is thrown when an invalid value is specified for
   * dataSource.
   */
  @Test
  public void negativeQueryDataSourceAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeQueryDataSourceAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeQueryDataSourceAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeQueryDataSourceAttributeEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query action which specifies an DataSource
   * Object which is uninitialized for the dataSource attribute will generate a
   * JspException.
   */
  @Test
  public void negativeQueryDataSourceAttributeEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeQueryDataSourceAttributeEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeQueryDataSourceAttributeEmptyTest");
    invoke();
  }

  /*
   * @testName: negativeQueryDataSourceNullAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query action which specifies null for the
   * dataSource attribute will generate a JspException.
   */
  @Test
  public void negativeQueryDataSourceNullAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeQueryDataSourceNullAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeQueryDataSourceNullAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveResultSupportTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @test_Strategy: validates jakarta.servlet.jsp.jstl.sql.ResultSupport by using
   * cusotm tag resultSetQuery, which invokes ResultSupport.toResult() to
   * convert a java.sql.ResultSet to jakarta.servlet.jsp.jstl.sql.Result.
   */
  @Test
  public void positiveResultSupportTest() throws Exception {
    String testName = "positiveResultSupportTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_sql_query_web/positiveResultSupportTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

}
