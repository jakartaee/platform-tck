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

package com.sun.ts.tests.jstl.spec.sql.update;

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
    setGeneralURI("/jstl/spec/sql/update");
    setContextRoot("/jstl_sql_update_web");
    setGoldenFileDir("/jstl/spec/sql/update");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_sql_update_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_sql_update_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativesetDataSourceScopeAttributeTest.jsp")), "negativesetDataSourceScopeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateBodyContentTest.jsp")), "negativeUpdateBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateDataSourceAttributeEmptyTest.jsp")), "negativeUpdateDataSourceAttributeEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateDataSourceAttributeTest.jsp")), "negativeUpdateDataSourceAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateDataSourceNullAttributeTest.jsp")), "negativeUpdateDataSourceNullAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateScopeAttributeTest.jsp")), "negativeUpdateScopeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateScopeVarAttributeTest.jsp")), "negativeUpdateScopeVarAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateSQLAttributeTest.jsp")), "negativeUpdateSQLAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateSQLAttributeTest2.jsp")), "negativeUpdateSQLAttributeTest2.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUpdateVarAttributeTest.jsp")), "negativeUpdateVarAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateBodyContentTest.jsp")), "positiveUpdateBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateDataSourceAttributeDataSourceTest.jsp")), "positiveUpdateDataSourceAttributeDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateDataSourceAttributeDriverManagerTest.jsp")), "positiveUpdateDataSourceAttributeDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateDataSourceConfigDataSourceTest.jsp")), "positiveUpdateDataSourceConfigDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateDataSourceConfigDriverManagerTest.jsp")), "positiveUpdateDataSourceConfigDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateDataSourceConfigPrecedenceTest.jsp")), "positiveUpdateDataSourceConfigPrecedenceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateNoRowsResultTest.jsp")), "positiveUpdateNoRowsResultTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateNoVarAttributeTest.jsp")), "positiveUpdateNoVarAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateRowsResultTest.jsp")), "positiveUpdateRowsResultTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateScopeAttributeTest.jsp")), "positiveUpdateScopeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateSQLAttributeTest.jsp")), "positiveUpdateSQLAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateVarAttributeTest.jsp")), "positiveUpdateVarAttributeTest.jsp");
    
    archive.addAsWebInfResource(JSTLClientIT.class.getPackage(), "tssql.stmt", "jstl-sql.properties");    

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveUpdateDataSourceAttributeDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:60.2; JSTL:SPEC:60.2.2; JSTL:SPEC:60.9.1
   * 
   * @testStrategy: Validate the sql:update use of the dataSource attribute -
   * That a query can be successfully executed when the dataSource attribute is
   * passed an instance of a DataSource.
   */
  @Test
  public void positiveUpdateDataSourceAttributeDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateDataSourceAttributeDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveUpdateDataSourceAttributeDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateDataSourceAttributeDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:60
   * 
   * @testStrategy: Validate sql:update action specifying a dataSource attribute
   * which contains JDBC DriverManager properties (URL, driver, user, password)
   * The query is passed as body content.
   */
  @Test
  public void positiveUpdateDataSourceAttributeDriverManagerTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateDataSourceAttributeDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveUpdateDataSourceAttributeDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60.11; JSTL:SPEC:60.3; JSTL:SPEC:60.4;
   * JSTL:SPEC:60.4.1;JSTL:SPEC:60.4.2; JSTL:SPEC:60.4.3; JSTL:SPEC:60.4.4
   * 
   * @testStrategy: Validate that the action exports var to the specified scope
   * as well as validating that if scope is not specified, var will be exported
   * to the page scope by default.
   */
  @Test
  public void positiveUpdateScopeAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateScopeAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUpdateScopeAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60.3.1
   * 
   * @testStrategy: Validate the sql:update use of the var attribute - That
   * scoped var attribute returned is of type java.lang.Integer.
   */
  @Test
  public void positiveUpdateVarAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateVarAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUpdateVarAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateNoVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60
   * 
   * @testStrategy: Validate the sql:update can successfully execute a query
   * when the var attribute is not specified.
   */
  @Test
  public void positiveUpdateNoVarAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateNoVarAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUpdateNoVarAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateNoRowsResultTest
   * 
   * @assertion_ids: JSTL:SPEC:60.1; JSTL:SPEC:60.1.1; JSTL:SPEC:60.1.2
   * 
   * @testStrategy: Validate the sql:update use of the var attribute - That
   * scoped var attribute returned is equal to 0 if the SQL statement does not
   * affect any rows.
   */
  @Test
  public void positiveUpdateNoRowsResultTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateNoRowsResultTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUpdateNoRowsResultTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateRowsResultTest
   * 
   * @assertion_ids: JSTL:SPEC:60; JSTL:SPEC:60.10
   * 
   * @testStrategy: Validate the sql:update use of the var attribute - That
   * scoped var attribute returned is equal to the number of rows affected by
   * the SQL statement.
   */
  @Test
  public void positiveUpdateRowsResultTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateRowsResultTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUpdateRowsResultTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:60
   * 
   * @testStrategy: Validate the sql:update action - That a SQL query can be
   * passed as body content.
   */
  @Test
  public void positiveUpdateBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUpdateBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateSQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60.6; JSTL:SPEC:60.1; JSTL:SPEC:60.1.1
   * 
   * @testStrategy: Validate the sql:update use of the sql attribute - That sql
   * attribute can be specified in order to execute a SQL statement.
   */
  @Test
  public void positiveUpdateSQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateSQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUpdateSQLAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateDataSourceConfigDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:60
   * 
   * @testStrategy: Validate sql:update action utilizing the configuration
   * parameter jakarta.servlet.jsp.jstl.sql.dataSource. The query is passed as
   * body content. - that jakarta.servlet.jsp.jstl.sql.dataSource can be provided
   * a DataSource.
   */
  @Test
  public void positiveUpdateDataSourceConfigDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateDataSourceConfigDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveUpdateDataSourceConfigDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateDataSourceConfigDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:60
   * 
   * @testStrategy: Validate sql:update action utilizing the configuration
   * parameter jakarta.servlet.jsp.jstl.sql.dataSource and setting it to a String
   * representing JDBC DriverManager parameters. The query is passed as body
   * content
   */
  @Test
  public void positiveUpdateDataSourceConfigDriverManagerTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateDataSourceConfigDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveUpdateDataSourceConfigDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateDataSourceConfigPrecedenceTest
   * 
   * @assertion_ids: JSTL:SPEC:60
   * 
   * @testStrategy: Validate sql:update action dataSource attribute takes
   * precedence over the configuration parameter
   * jakarta.servlet.jsp.jstl.sql.dataSource.
   */
  @Test
  public void positiveUpdateDataSourceConfigPrecedenceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateDataSourceConfigPrecedenceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveUpdateDataSourceConfigPrecedenceTest");
    invoke();
  }

  /*
   * @testName: negativeUpdateSQLAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60.1.2
   * 
   * @testStrategy: Validate the sql:update action use of the sql attribute -
   * That a JspException is thrown when an invalid Datatype is specified for sql
   * attribute.
   */
  @Test
  public void negativeUpdateSQLAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeUpdateSQLAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeUpdateSQLAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeUpdateSQLAttributeTest2
   * 
   * @assertion_ids: JSTL:SPEC:60.1.2
   * 
   * @testStrategy: Validate the sql:update action use of the sql attribute -
   * That a JspException is thrown when an invalid SQL query is specified for
   * sql.
   */
  @Test
  public void negativeUpdateSQLAttributeTest2() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeUpdateSQLAttributeTest2.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeUpdateSQLAttributeTest2");
    invoke();
  }

  /*
   * @testName: negativeUpdateBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:60.13
   * 
   * @testStrategy: Validate the sql:update action use of the body content -
   * That a JspException is thrown when an invalid SQL query is specified via
   * body content.
   */
  @Test
  public void negativeUpdateBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeUpdateBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeUpdateBodyContentTest");
    invoke();
  }

  /*
   * @testName: negativeUpdateVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60.3.2
   * 
   * @testStrategy: Validate the sql:update var attribute - That a translation
   * error occurs when the var attribute is empty.
   */
  @Test
  public void negativeUpdateVarAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeUpdateVarAttributeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeUpdateVarAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeUpdateScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60; JSTL:SPEC:60.1.3
   * 
   * @testStrategy: Validate that if a sql:update action utilizes the scope
   * attribute that is invalid, that a translation error will occur.
   */
  @Test
  public void negativeUpdateScopeAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeUpdateScopeAttributeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeUpdateScopeAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeUpdateScopeVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60.12
   * 
   * @testStrategy: Validate the sql:update action var and scope attribute -
   * That a translation error occurs when the scope attribute is specified and
   * the var attribute is not.
   */
  @Test
  public void negativeUpdateScopeVarAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeUpdateScopeVarAttributeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeUpdateScopeVarAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeUpdateDataSourceAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60; JSTL:SPEC:60.1.3
   * 
   * @testStrategy: Validate the sql:update action which specifies an invalid
   * DataType for the dataSource attribute will generate a JspException.
   */
  @Test
  public void negativeUpdateDataSourceAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeUpdateDataSourceAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeUpdateDataSourceAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeUpdateDataSourceNullAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:60; JSTL:SPEC:60.1.3
   * 
   * @testStrategy: Validate the sql:update action which specifies an DataSource
   * Object which is null for the dataSource attribute will generate a
   * JspException.
   */
  @Test
  public void negativeUpdateDataSourceNullAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeUpdateDataSourceNullAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeUpdateDataSourceNullAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeUpdateDataSourceAttributeEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:60; JSTL:SPEC:61.4
   * 
   * @testStrategy: Validate the sql:update action which specifies an DataSource
   * Object which is uninitialized for the dataSource attribute will generate a
   * JspException.
   */
  @Test
  public void negativeUpdateDataSourceAttributeEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeUpdateDataSourceAttributeEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeUpdateDataSourceAttributeEmptyTest");
    invoke();
  }

}
