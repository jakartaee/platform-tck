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
 * $Id$
 */

package com.sun.ts.tests.jstl.spec.sql.transaction;

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
    setGeneralURI("/jstl/spec/sql/transaction");
    setContextRoot("/jstl_sql_transaction_web");
    setGoldenFileDir("/jstl/spec/sql/transaction");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_sql_transaction_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_sql_transaction_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTxDataSourceAttributeEmptyTest.jsp")), "negativeTxDataSourceAttributeEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTxDataSourceAttributeTest.jsp")), "negativeTxDataSourceAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTxDataSourceNullAttributeTest.jsp")), "negativeTxDataSourceNullAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTxIsolationLevelAttributeTest.jsp")), "negativeTxIsolationLevelAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTxQueryDataSourceAttributeTest.jsp")), "negativeTxQueryDataSourceAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTxUpdateDataSourceAttributeTest.jsp")), "negativeTxUpdateDataSourceAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxCommitLifeCycleTest.jsp")), "positiveTxCommitLifeCycleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxDataSourceAttributeDataSourceTest.jsp")), "positiveTxDataSourceAttributeDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxDataSourceAttributeDriverManagerTest.jsp")), "positiveTxDataSourceAttributeDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxDataSourceConfigDataSourceTest.jsp")), "positiveTxDataSourceConfigDataSourceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxDataSourceConfigDriverManagerTest.jsp")), "positiveTxDataSourceConfigDriverManagerTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxDataSourceConfigPrecedenceTest.jsp")), "positiveTxDataSourceConfigPrecedenceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxIsolationAttributeSerializable.jsp")), "positiveTxIsolationAttributeSerializable.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxQueryCommitTest.jsp")), "positiveTxQueryCommitTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxQueryParamCommitTest.jsp")), "positiveTxQueryParamCommitTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxRollbackLifeCycleTest.jsp")), "positiveTxRollbackLifeCycleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxUpdateCommitTest.jsp")), "positiveTxUpdateCommitTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxUpdateParamCommitTest.jsp")), "positiveTxUpdateParamCommitTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxUpdateRollbackTest.jsp")), "positiveTxUpdateRollbackTest.jsp");

    archive.addAsWebInfResource(JSTLClientIT.class.getPackage(), "tssql.stmt", "jstl-sql.properties");    

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveTxDataSourceConfigDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.7; JSTL:SPEC:61.7.2
   * 
   * @testStrategy: Validate sql:transaction, sql:query actions utilizing the
   * configuration parameter jakarta.servlet.jsp.jstl.sql.dataSource. The query is
   * passed as body content. - that sql:transaction action uses the
   * configuration parameter - The datasource attribute takes precedence over
   * jakarta.servlet.jsp.jstl.sql.dataSource
   */
  @Test
  public void positiveTxDataSourceConfigDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxDataSourceConfigDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveTxDataSourceConfigDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveTxDataSourceConfigDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction action utilizing the configuration
   * parameter jakarta.servlet.jsp.jstl.sql.dataSource and setting it to a String
   * representing JDBC DriverManager parameters. The query is passed as body
   * content
   */
  @Test
  public void positiveTxDataSourceConfigDriverManagerTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxDataSourceConfigDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveTxDataSourceConfigDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveTxDataSourceConfigPrecedenceTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.7; JSTL:SPEC:61.7.2
   * 
   * @testStrategy: Validate sql:transaction action dataSource attribute takes
   * precedence over the configuration parameter
   * jakarta.servlet.jsp.jstl.sql.dataSource.
   */
  @Test
  public void positiveTxDataSourceConfigPrecedenceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxDataSourceConfigPrecedenceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveTxDataSourceConfigPrecedenceTest");
    invoke();
  }

  /*
   * @testName: positiveTxDataSourceAttributeDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction action specifying a DataSource
   * Object for the dataSource attribute. The query is passed as body content.
   */
  @Test
  public void positiveTxDataSourceAttributeDataSourceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxDataSourceAttributeDataSourceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveTxDataSourceAttributeDataSourceTest");
    invoke();
  }

  /*
   * @testName: positiveTxDataSourceAttributeDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.1; JSTL:SPEC:61.7;
   * JSTL:SPEC:61.7.1
   * 
   * @testStrategy: Validate sql:transaction action specifying a dataSource
   * attribute which contains JDBC DriverManager properties (URL, driver, user,
   * password) The query is passed as body content.
   */
  @Test
  public void positiveTxDataSourceAttributeDriverManagerTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxDataSourceAttributeDriverManagerTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveTxDataSourceAttributeDriverManagerTest");
    invoke();
  }

  /*
   * @testName: positiveTxQueryCommitTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction and sql:query actions allow a query
   * to be successfully executed.
   */
  @Test
  public void positiveTxQueryCommitTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxQueryCommitTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTxQueryCommitTest");
    invoke();
  }

  /*
   * @testName: positiveTxUpdateCommitTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction and sql:update actions allow a
   * query to be successfully committed.
   */
  @Test
  public void positiveTxUpdateCommitTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxUpdateCommitTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTxUpdateCommitTest");
    invoke();
  }

  /*
   * @testName: positiveTxUpdateRollbackTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction and sql:update actions allow a
   * query to be successfully rolled back when an SQLException occurs during the
   * transaction.
   */
  @Test
  public void positiveTxUpdateRollbackTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxUpdateRollbackTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTxUpdateRollbackTest");
    invoke();
  }

  /*
   * @testName: positiveTxIsolationAttributeSerializable
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction will set the isolation attribute to
   * serializable.
   */
  @Test
  public void positiveTxIsolationAttributeSerializable() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxIsolationAttributeSerializable.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveTxIsolationAttributeSerializable");
    invoke();
  }

  /*
   * @testName: positiveTxCommitLifeCycleTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction action lifecycle for a committed
   * transaction.
   */
  @Test
  public void positiveTxCommitLifeCycleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxCommitLifeCycleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTxCommitLifeCycleTest");
    invoke();
  }

  /*
   * @testName: positiveTxRollbackLifeCycleTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction action lifecyle for a transaction
   * that is rolled back
   */
  @Test
  public void positiveTxRollbackLifeCycleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxRollbackLifeCycleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTxRollbackLifeCycleTest");
    invoke();
  }

  /*
   * @testName: positiveTxQueryParamCommitTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction, sql:query and sql:param actions
   * allow for a query to be executed successfully.
   */
  @Test
  public void positiveTxQueryParamCommitTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxQueryParamCommitTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTxQueryParamCommitTest");
    invoke();
  }

  /*
   * @testName: positiveTxUpdateParamCommitTest
   * 
   * @assertion_ids: JSTL:SPEC:61
   * 
   * @testStrategy: Validate sql:transaction, sql:update and sql:param actions
   * allow for a query to be executed successfully.
   */
  @Test
  public void positiveTxUpdateParamCommitTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxUpdateParamCommitTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTxUpdateParamCommitTest");
    invoke();
  }

  /*
   * @testName: negativeTxDataSourceAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.2.6
   * 
   * @testStrategy: Validate the sql:transaction action which specifies an
   * invalid DataType for the dataSource attribute will generate a JspException.
   */
  @Test
  public void negativeTxDataSourceAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeTxDataSourceAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeTxDataSourceAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeTxDataSourceNullAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.4
   * 
   * @testStrategy: Validate the sql:transaction action which specifies an
   * DataSource Object which is null for the dataSource attribute will generate
   * a JspException.
   */
  @Test
  public void negativeTxDataSourceNullAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeTxDataSourceNullAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeTxDataSourceNullAttributeTest");
    invoke();
  }

  /*
   * @testName: negativeTxDataSourceAttributeEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.9
   * 
   * @testStrategy: Validate the sql:transaction action which specifies an
   * DataSource Object which is uninitialized for the dataSource attribute will
   * generate a JspException.
   */
  @Test
  public void negativeTxDataSourceAttributeEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeTxDataSourceAttributeEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeTxDataSourceAttributeEmptyTest");
    invoke();
  }

  /*
   * @testName: negativeTxIsolationLevelAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.2
   * 
   * @testStrategy: Validate that if a sql:transaction utilizes an invalid
   * isolationLevel attribute, that a translation error will occur.
   */
  @Test
  public void negativeTxIsolationLevelAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeTxIsolationLevelAttributeTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeTxIsolationLevelAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeTxQueryDataSourceAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.9
   * 
   * @testStrategy: Validate that if a sql:transaction contains a sql:query
   * action that specifies a dataSource attribute that a translation error
   * occurs.
   */
  @Test
  public void negativeTxQueryDataSourceAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeTxQueryDataSourceAttributeTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeTxQueryDataSourceAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeTxUpdateDataSourceAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.8
   * 
   * @testStrategy: Validate that if a sql:transaction contains a sql:update
   * action that specifies a dataSource attribute that a translation error
   * occurs.
   */
  @Test
  public void negativeTxUpdateDataSourceAttributeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME,
        "negativeTxUpdateDataSourceAttributeTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeTxUpdateDataSourceAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
