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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.spec.sql.setdatasource;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jstl.common.client.SqlUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends SqlUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");



  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setGeneralURI("/jstl/spec/sql/setdatasource");
    setContextRoot("/jstl_sql_setdatasource_web");
    setGoldenFileDir("/jstl/spec/sql/setdatasource");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_sql_setdatasource_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_sql_setdatasource_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetDataSourceDataSourceAttributeDataSourceTest.jsp")), "negativeSetDataSourceDataSourceAttributeDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetDataSourceDataSourceAttributeDriverManagerTest.jsp")), "negativeSetDataSourceDataSourceAttributeDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetDataSourceDataSourceAttributeEmptyTest.jsp")), "negativeSetDataSourceDataSourceAttributeEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetDataSourceDataSourceNullAttributeTest.jsp")), "negativeSetDataSourceDataSourceNullAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetDataSourceScopeAttributeTest.jsp")), "negativeSetDataSourceScopeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceQueryDataSourceTest.jsp")), "positiveSetDataSourceQueryDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceQueryDriverManagerTest.jsp")), "positiveSetDataSourceQueryDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceQueryNoVarAttributeDataSourceTest.jsp")), "positiveSetDataSourceQueryNoVarAttributeDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceQueryNoVarAttributeDriverManagerTest.jsp")), "positiveSetDataSourceQueryNoVarAttributeDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceQueryURLTest.jsp")), "positiveSetDataSourceQueryURLTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceScopeNoVarAttributeTest.jsp")), "positiveSetDataSourceScopeNoVarAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceScopeVarAttributeTest.jsp")), "positiveSetDataSourceScopeVarAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceTxDataSourceTest.jsp")), "positiveSetDataSourceTxDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceTxDriverManagerTest.jsp")), "positiveSetDataSourceTxDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceTxNoVarAttributeDataSourceTest.jsp")), "positiveSetDataSourceTxNoVarAttributeDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceTxNoVarAttributeDriverManagerTest.jsp")), "positiveSetDataSourceTxNoVarAttributeDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceTxURLTest.jsp")), "positiveSetDataSourceTxURLTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceUpdateDataSourceTest.jsp")), "positiveSetDataSourceUpdateDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceUpdateDriverManagerTest.jsp")), "positiveSetDataSourceUpdateDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceUpdateNoVarAttributeDataSourceTest.jsp")), "positiveSetDataSourceUpdateNoVarAttributeDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceUpdateNoVarAttributeDriverManagerTest.jsp")), "positiveSetDataSourceUpdateNoVarAttributeDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceUpdateURLTest.jsp")), "positiveSetDataSourceUpdateURLTest.jsp");

    archive.addAsWebInfResource(JSTLClientIT.class.getPackage(), "tssql.stmt", "jstl-sql.properties");    

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveSetDataSourceQueryDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.1; JSTL:SPEC:62.2;
   * JSTL:SPEC:62.2.1; JSTL:SPEC:62.3; JSTL:SPEC:62.3.1; JSTL:SPEC:62.4;
   * JSTL:SPEC:62.4.1
   * 
   * @testStrategy: Validate sql:setDataSource and sql:query actions specifying
   * a dataSource attribute to sql:setDataSource which contains JDBC
   * DriverManager properties (URL, driver, user, password) The query is passed
   * as body content.
   */
  @Test
  public void positiveSetDataSourceQueryDriverManagerTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceQueryDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceQueryDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceQueryDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.1; JSTL:SPEC:62.2;
   * JSTL:SPEC:62.2.1; JSTL:SPEC:62.3; JSTL:SPEC:62.3.1; JSTL:SPEC:62.4;
   * JSTL:SPEC:62.4.1
   * 
   * @testStrategy: Validate sql:setDataSource and sql:query actions specifying
   * a dataSource attribute to sql:setDataSource which contains a DataSource
   * Object. The query is passed as body content.
   */
  @Test
  public void positiveSetDataSourceQueryDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceQueryDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceQueryDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceQueryURLTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.1; JSTL:SPEC:62.2;
   * JSTL:SPEC:62.2.1; JSTL:SPEC:62.3; JSTL:SPEC:62.3.1; JSTL:SPEC:62.4;
   * JSTL:SPEC:62.4.1
   * 
   * @testStrategy: Validate sql:setDataSource and sql:query actions specifying
   * sql:setDataSource the attributes: url, driver, user and password. The query
   * is passed as body content.
   */
  @Test
  public void positiveSetDataSourceQueryURLTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceQueryURLTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetDataSourceQueryURLTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceUpdateDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.3
   * 
   * @testStrategy: Validate sql:setDataSource and sql:update actions specifying
   * a dataSource attribute to sql:setDataSource which contains JDBC
   * DriverManager properties (URL, driver, user, password) The query is passed
   * as body content.
   */
  @Test
  public void positiveSetDataSourceUpdateDriverManagerTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceUpdateDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceUpdateDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceUpdateDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.1; JSTL:SPEC:62.2;
   * JSTL:SPEC:62.2.1; JSTL:SPEC:62.3; JSTL:SPEC:62.3.1; JSTL:SPEC:62.4;
   * JSTL:SPEC:62.4.1
   * 
   * @testStrategy: Validate sql:setDataSource and sql:update actions specifying
   * a dataSource attribute to sql:setDataSource which contains a DataSource
   * Object. The query is passed as body content.
   */
  @Test
  public void positiveSetDataSourceUpdateDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceUpdateDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceUpdateDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceUpdateURLTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.1; JSTL:SPEC:62.2;
   * JSTL:SPEC:62.2.1; JSTL:SPEC:62.3; JSTL:SPEC:62.3.1; JSTL:SPEC:62.4;
   * JSTL:SPEC:62.4.1
   * 
   * @testStrategy: Validate sql:setDataSource and sql:query actions specifying
   * sql:setDataSource the attributes: url, driver, user and password. The query
   * is passed as body content.
   */
  @Test
  public void positiveSetDataSourceUpdateURLTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceUpdateURLTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetDataSourceUpdateURLTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceTxDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.1; JSTL:SPEC:62.2;
   * JSTL:SPEC:62.2.1; JSTL:SPEC:62.3; JSTL:SPEC:62.3.1; JSTL:SPEC:62.4;
   * JSTL:SPEC:62.4.1
   * 
   * @testStrategy: Validate sql:setDataSource and sql:transaction actions
   * specifying a dataSource attribute to sql:setDataSource which contains JDBC
   * DriverManager properties (URL, driver, user, password) The query is passed
   * as body content.
   */
  @Test
  public void positiveSetDataSourceTxDriverManagerTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceTxDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceTxDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceTxDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.1; JSTL:SPEC:62.2;
   * JSTL:SPEC:62.2.1; JSTL:SPEC:62.3; JSTL:SPEC:62.3.1; JSTL:SPEC:62.4;
   * JSTL:SPEC:62.4.1
   * 
   * @testStrategy: Validate sql:setDataSource and sql:transaction actions
   * specifying a dataSource attribute to sql:setDataSource which contains a
   * DataSource Object. The query is passed as body content.
   */
  @Test
  public void positiveSetDataSourceTxDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceTxDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetDataSourceTxDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceTxURLTest
   * 
   * @assertion_ids: JSTL:SPEC:62.13; JSTL:SPEC:62.13.1
   * 
   * @testStrategy: Validate sql:setDataSource and sql:transaction actions
   * specifying sql:setDataSource the attributes: url, driver, user and
   * password. The query is passed as body content.
   */
  @Test
  public void positiveSetDataSourceTxURLTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceTxURLTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetDataSourceTxURLTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceQueryNoVarAttributeDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.5
   * 
   * @testStrategy: Validate sql:setDataSource and sql:query actions by setting
   * jakarta.servlet.jsp.jstl.sql.dataSource to JDBC DriverManager properties
   * (URL, driver, user, password) The query is passed as body content.
   */
  @Test
  public void positiveSetDataSourceQueryNoVarAttributeDriverManagerTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceQueryNoVarAttributeDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceQueryNoVarAttributeDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceQueryNoVarAttributeDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.5
   * 
   * @testStrategy: Validate sql:setDataSource and sql:query actions by setting
   * jakarta.servlet.jsp.jstl.sql.dataSource to a DataSource Object. The query is
   * passed as body content.
   */
  @Test
  public void positiveSetDataSourceQueryNoVarAttributeDataSourceTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceQueryNoVarAttributeDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceQueryNoVarAttributeDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceUpdateNoVarAttributeDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.5
   * 
   * @testStrategy: Validate sql:setDataSource and sql:update actions by setting
   * jakarta.servlet.jsp.jstl.sql.dataSource to JDBC DriverManager properties
   * (URL, driver, user, password) The query is passed as body content.
   */
  @Test
  public void positiveSetDataSourceUpdateNoVarAttributeDriverManagerTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceUpdateNoVarAttributeDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceUpdateNoVarAttributeDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceUpdateNoVarAttributeDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.5
   * 
   * @testStrategy: Validate sql:setDataSource and sql:update actions by setting
   * jakarta.servlet.jsp.jstl.sql.dataSource to a DataSource Object. The query is
   * passed as body content.
   */
  @Test
  public void positiveSetDataSourceUpdateNoVarAttributeDataSourceTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceUpdateNoVarAttributeDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceUpdateNoVarAttributeDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceTxNoVarAttributeDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.5
   * 
   * @testStrategy: Validate sql:setDataSource and sql:transaction actions by
   * setting jakarta.servlet.jsp.jstl.sql.dataSource to JDBC DriverManager
   * properties (URL, driver, user, password) The query is passed as body
   * content.
   */
  @Test
  public void positiveSetDataSourceTxNoVarAttributeDriverManagerTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceTxNoVarAttributeDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceTxNoVarAttributeDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceTxNoVarAttributeDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.5
   * 
   * @testStrategy: Validate sql:setDataSource and sql:transaction actions by
   * setting jakarta.servlet.jsp.jstl.sql.dataSource to a DataSource Object. The
   * query is passed as body content.
   */
  @Test
  public void positiveSetDataSourceTxNoVarAttributeDataSourceTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceTxNoVarAttributeDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceTxNoVarAttributeDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceScopeVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:62.7.1; JSTL:SPEC:62.7.2; JSTL:SPEC:62.7.3;
   * JSTL:SPEC:62.7.4
   * 
   * @testStrategy: Validate sql:setDataSource exports the var attribute to the
   * correct scope.
   */
  @Test
  public void positiveSetDataSourceScopeVarAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceScopeVarAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveSetDataSourceScopeVarAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceScopeNoVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:62.7.1; JSTL:SPEC:62.7.2; JSTL:SPEC:62.7.3;
   * JSTL:SPEC:62.7.4
   * 
   * @testStrategy: Validate sql:setDataSource exports the var attribute to the
   * correct scope.
   */
  @Test
  public void positiveSetDataSourceScopeNoVarAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_sql_setdatasource_web/positiveSetDataSourceScopeNoVarAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: negativeSetDataSourceDataSourceAttributeDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.2.2
   * 
   * @testStrategy: Pass an invalid String as the dataSource attribute to
   * sql:setDataSource
   */
  @Test
  public void negativeSetDataSourceDataSourceAttributeDriverManagerTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeSetDataSourceDataSourceAttributeDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeSetDataSourceDataSourceAttributeDriverManagerTest");
    invoke();
  }

  /*
   * @testName: negativeSetDataSourceScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.2
   * 
   * @testStrategy: Validate that if a sql:setDataSource action utilizes the
   * scope attribute that is invalid, that a translation error will occur.
   */
  @Test
  public void negativeSetDataSourceScopeAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME,
        "negativeSetDataSourceScopeAttributeTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeSetDataSourceScopeAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeSetDataSourceDataSourceAttributeDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.2.2
   * 
   * @testStrategy: Pass an invalid Object as the dataSource attribute to
   * sql:setDataSource
   */
  @Test
  public void negativeSetDataSourceDataSourceAttributeDataSourceTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeSetDataSourceDataSourceAttributeDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeSetDataSourceDataSourceAttributeDataSourceTest");
    invoke();
  }

  /*
   * @testName: negativeSetDataSourceDataSourceNullAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.1.4
   * 
   * @testStrategy: Validate the sql:setDataSource action which specifies a null
   * for the dataSource attribute will generate a JspException.
   */
  @Test
  public void negativeSetDataSourceDataSourceNullAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeSetDataSourceDataSourceNullAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeSetDataSourceDataSourceNullAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeSetDataSourceDataSourceAttributeEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.3.2
   * 
   * @testStrategy: Validate the sql:setDataSource action which specifies an
   * DataSource Object which is uninitialized for the dataSource attribute will
   * generate a JspException the first attempt to use it.
   */
  @Test
  public void negativeSetDataSourceDataSourceAttributeEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeSetDataSourceDataSourceAttributeEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeSetDataSourceDataSourceAttributeEmptyTest");
    invoke();
  }
}
