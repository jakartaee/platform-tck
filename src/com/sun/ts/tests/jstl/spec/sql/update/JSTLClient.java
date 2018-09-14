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

package com.sun.ts.tests.jstl.spec.sql.update;

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

    setGeneralURI("/jstl/spec/sql/update");
    setContextRoot("/jstl_sql_update_web");
    setGoldenFileDir("/jstl/spec/sql/update");

    return super.run(args, out, err);
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
  public void positiveUpdateDataSourceAttributeDataSourceTest() throws Fault {
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
  public void positiveUpdateDataSourceAttributeDriverManagerTest()
      throws Fault {
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
  public void positiveUpdateScopeAttributeTest() throws Fault {
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
  public void positiveUpdateVarAttributeTest() throws Fault {
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
  public void positiveUpdateNoVarAttributeTest() throws Fault {
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
  public void positiveUpdateNoRowsResultTest() throws Fault {
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
  public void positiveUpdateRowsResultTest() throws Fault {
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
  public void positiveUpdateBodyContentTest() throws Fault {
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
  public void positiveUpdateSQLAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveUpdateSQLAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateDataSourceConfigDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:60
   * 
   * @testStrategy: Validate sql:update action utilizing the configuration
   * parameter javax.servlet.jsp.jstl.sql.dataSource. The query is passed as
   * body content. - that javax.servlet.jsp.jstl.sql.dataSource can be provided
   * a DataSource.
   */
  public void positiveUpdateDataSourceConfigDataSourceTest() throws Fault {
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
   * parameter javax.servlet.jsp.jstl.sql.dataSource and setting it to a String
   * representing JDBC DriverManager parameters. The query is passed as body
   * content
   */
  public void positiveUpdateDataSourceConfigDriverManagerTest() throws Fault {
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
   * javax.servlet.jsp.jstl.sql.dataSource.
   */
  public void positiveUpdateDataSourceConfigPrecedenceTest() throws Fault {
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
  public void negativeUpdateSQLAttributeTest() throws Fault {
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
  public void negativeUpdateSQLAttributeTest2() throws Fault {
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
  public void negativeUpdateBodyContentTest() throws Fault {
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
  public void negativeUpdateVarAttributeTest() throws Fault {
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
  public void negativeUpdateScopeAttributeTest() throws Fault {
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
  public void negativeUpdateScopeVarAttributeTest() throws Fault {
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
  public void negativeUpdateDataSourceAttributeTest() throws Fault {
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
  public void negativeUpdateDataSourceNullAttributeTest() throws Fault {
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
  public void negativeUpdateDataSourceAttributeEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "negativeUpdateDataSourceAttributeEmptyTest");
    invoke();
  }

}
