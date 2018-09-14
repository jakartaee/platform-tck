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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.spec.sql.setdatasource;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.SqlUrlClient;

public class JSTLClient extends SqlUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home; jstl.db.url;
   * jstl.db.user; jstl.db.password; jstl.db.driver;
   */

  /** Creates new JSTLClient */
  public JSTLClient() {
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JSTLClient theTests = new JSTLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setGeneralURI("/jstl/spec/sql/setdatasource");
    setContextRoot("/jstl_sql_setdatasource_web");
    setGoldenFileDir("/jstl/spec/sql/setdatasource");

    return super.run(args, out, err);
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
  public void positiveSetDataSourceQueryDriverManagerTest() throws Fault {
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
  public void positiveSetDataSourceQueryDataSourceTest() throws Fault {
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
  public void positiveSetDataSourceQueryURLTest() throws Fault {
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
  public void positiveSetDataSourceUpdateDriverManagerTest() throws Fault {
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
  public void positiveSetDataSourceUpdateDataSourceTest() throws Fault {
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
  public void positiveSetDataSourceUpdateURLTest() throws Fault {
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
  public void positiveSetDataSourceTxDriverManagerTest() throws Fault {
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
  public void positiveSetDataSourceTxDataSourceTest() throws Fault {
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
  public void positiveSetDataSourceTxURLTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetDataSourceTxURLTest");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceQueryNoVarAttributeDriverManagerTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.5
   * 
   * @testStrategy: Validate sql:setDataSource and sql:query actions by setting
   * javax.servlet.jsp.jstl.sql.dataSource to JDBC DriverManager properties
   * (URL, driver, user, password) The query is passed as body content.
   */
  public void positiveSetDataSourceQueryNoVarAttributeDriverManagerTest()
      throws Fault {
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
   * javax.servlet.jsp.jstl.sql.dataSource to a DataSource Object. The query is
   * passed as body content.
   */
  public void positiveSetDataSourceQueryNoVarAttributeDataSourceTest()
      throws Fault {
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
   * javax.servlet.jsp.jstl.sql.dataSource to JDBC DriverManager properties
   * (URL, driver, user, password) The query is passed as body content.
   */
  public void positiveSetDataSourceUpdateNoVarAttributeDriverManagerTest()
      throws Fault {
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
   * javax.servlet.jsp.jstl.sql.dataSource to a DataSource Object. The query is
   * passed as body content.
   */
  public void positiveSetDataSourceUpdateNoVarAttributeDataSourceTest()
      throws Fault {
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
   * setting javax.servlet.jsp.jstl.sql.dataSource to JDBC DriverManager
   * properties (URL, driver, user, password) The query is passed as body
   * content.
   */
  public void positiveSetDataSourceTxNoVarAttributeDriverManagerTest()
      throws Fault {
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
   * setting javax.servlet.jsp.jstl.sql.dataSource to a DataSource Object. The
   * query is passed as body content.
   */
  public void positiveSetDataSourceTxNoVarAttributeDataSourceTest()
      throws Fault {
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
  public void positiveSetDataSourceScopeVarAttributeTest() throws Fault {
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
  public void positiveSetDataSourceScopeNoVarAttributeTest() throws Fault {
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
  public void negativeSetDataSourceDataSourceAttributeDriverManagerTest()
      throws Fault {
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
  public void negativeSetDataSourceScopeAttributeTest() throws Fault {
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
  public void negativeSetDataSourceDataSourceAttributeDataSourceTest()
      throws Fault {
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
  public void negativeSetDataSourceDataSourceNullAttributeTest() throws Fault {
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
  public void negativeSetDataSourceDataSourceAttributeEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "negativeSetDataSourceDataSourceAttributeEmptyTest");
    invoke();
  }
}
