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
 * @(#)JSTLClient.java	1.2 03/19/02
 */

package com.sun.ts.tests.jstl.spec.sql.query;

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

    setGeneralURI("/jstl/spec/sql/query");
    setContextRoot("/jstl_sql_query_web");
    setGoldenFileDir("/jstl/spec/sql/query");

    return super.run(args, out, err);
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
  public void positiveQueryBodyContentTest() throws Fault {
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
  public void positiveQueryDataSourceAttributeDataSourceTest() throws Fault {
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
  public void positiveQueryDataSourceAttributeDriverManagerTest() throws Fault {
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
  public void positiveQuerySQLAttributeTest() throws Fault {
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
  public void positiveQueryScopeAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveQueryScopeAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveQueryVarAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.5;JSTL:SPEC:59.5.1
   * 
   * @testStrategy: Validate that the var attribute within a <sql:query> action
   * is of type javax.servlet.jsp.jstl.sql.Result.
   */
  public void positiveQueryVarAttributeTest() throws Fault {
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
  public void positiveQueryEmptyResultTest() throws Fault {
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
  public void positiveQueryMaxRowsAttributeTest() throws Fault {
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
  public void positiveQueryStartRowAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveQueryStartRowAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveQueryMaxRowsConfigTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of the configuration parameter
   * javax.servlet.jsp.sql.maxRows passed as a String - That the number of rows
   * returned matches the value specified by the config param. - That all rows
   * are returned if the config param is '-1'. - That maxRows attribute takes
   * precedence over the config param.
   */
  public void positiveQueryMaxRowsConfigTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveQueryMaxRowsConfigTest");
    invoke();
  }

  /*
   * @testName: positiveQueryMaxRowsIntegerConfigTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of the configuration parameter
   * javax.servlet.jsp.sql.maxRows passed an Integer object - That the number of
   * rows returned matches the value specified by the config param. - That all
   * rows are returned if the config param is '-1'. - That maxRows attribute
   * takes precedence over the config param.
   */
  public void positiveQueryMaxRowsIntegerConfigTest() throws Fault {
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
   * javax.servlet.jsp.jstl.sql.dataSource.
   */
  public void positiveQueryDataSourceConfigPrecedenceTest() throws Fault {
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
   * parameter javax.servlet.jsp.jstl.sql.dataSource and setting it to a
   * DataSource Object. The query is passed as body content.
   */
  public void positiveQueryDataSourceConfigDataSourceTest() throws Fault {
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
   * parameter javax.servlet.jsp.jstl.sql.dataSource and setting it to a String
   * representing JDBC DriverManager parameters. The query is passed as body
   * content
   */
  public void positiveQueryDataSourceConfigDriverManagerTest() throws Fault {
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
  public void negativeQuerySQLAttributeTest() throws Fault {
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
  public void negativeQueryMaxRowsAttributeTest2() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeQueryMaxRowsAttributeTest2");
    invoke();
  }

  /*
   * @testName: negativeQueryMaxRowsConfigTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of
   * javax.servlet.jsp.sql.jstl.maxRows config parameter - That a JspException
   * is thrown when an invalid value is specified.
   */
  public void negativeQueryMaxRowsConfigTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeQueryMaxRowsConfigTest");
    invoke();
  }

  /*
   * @testName: negativeQueryMaxRowsConfigTest2
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the sql:query use of
   * javax.servlet.jsp.sql.jstl.maxRows config parameter - That a JspException
   * is thrown when an value < -1 is specified.
   */
  public void negativeQueryMaxRowsConfigTest2() throws Fault {
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
  public void negativeQueryScopeAttributeTest() throws Fault {
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
  public void negativeQueryNoVarAttributeTest() throws Fault {
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
  public void negativeQueryVarAttributeTest() throws Fault {
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
  public void negativeQueryBodyContentTest() throws Fault {
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
  public void negativeQueryDataSourceAttributeTest() throws Fault {
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
  public void negativeQueryDataSourceAttributeEmptyTest() throws Fault {
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
  public void negativeQueryDataSourceNullAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "negativeQueryDataSourceNullAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveResultSupportTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @test_Strategy: validates javax.servlet.jsp.jstl.sql.ResultSupport by using
   * cusotm tag resultSetQuery, which invokes ResultSupport.toResult() to
   * convert a java.sql.ResultSet to javax.servlet.jsp.jstl.sql.Result.
   */

  public void positiveResultSupportTest() throws Fault {
    String testName = "positiveResultSupportTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_sql_query_web/positiveResultSupportTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

}
