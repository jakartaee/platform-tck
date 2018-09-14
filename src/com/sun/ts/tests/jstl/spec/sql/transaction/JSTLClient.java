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
 * $Id$
 */

package com.sun.ts.tests.jstl.spec.sql.transaction;

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

    setGeneralURI("/jstl/spec/sql/transaction");
    setContextRoot("/jstl_sql_transaction_web");
    setGoldenFileDir("/jstl/spec/sql/transaction");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveTxDataSourceConfigDataSourceTest
   * 
   * @assertion_ids: JSTL:SPEC:61; JSTL:SPEC:61.7; JSTL:SPEC:61.7.2
   * 
   * @testStrategy: Validate sql:transaction, sql:query actions utilizing the
   * configuration parameter javax.servlet.jsp.jstl.sql.dataSource. The query is
   * passed as body content. - that sql:transaction action uses the
   * configuration parameter - The datasource attribute takes precedence over
   * javax.servlet.jsp.jstl.sql.dataSource
   */
  public void positiveTxDataSourceConfigDataSourceTest() throws Fault {
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
   * parameter javax.servlet.jsp.jstl.sql.dataSource and setting it to a String
   * representing JDBC DriverManager parameters. The query is passed as body
   * content
   */
  public void positiveTxDataSourceConfigDriverManagerTest() throws Fault {
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
   * javax.servlet.jsp.jstl.sql.dataSource.
   */
  public void positiveTxDataSourceConfigPrecedenceTest() throws Fault {
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
  public void positiveTxDataSourceAttributeDataSourceTest() throws Fault {
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
  public void positiveTxDataSourceAttributeDriverManagerTest() throws Fault {
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
  public void positiveTxQueryCommitTest() throws Fault {
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
  public void positiveTxUpdateCommitTest() throws Fault {
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
  public void positiveTxUpdateRollbackTest() throws Fault {
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
  public void positiveTxIsolationAttributeSerializable() throws Fault {
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
  public void positiveTxCommitLifeCycleTest() throws Fault {
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
  public void positiveTxRollbackLifeCycleTest() throws Fault {
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
  public void positiveTxQueryParamCommitTest() throws Fault {
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
  public void positiveTxUpdateParamCommitTest() throws Fault {
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
  public void negativeTxDataSourceAttributeTest() throws Fault {
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
  public void negativeTxDataSourceNullAttributeTest() throws Fault {
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
  public void negativeTxDataSourceAttributeEmptyTest() throws Fault {
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
  public void negativeTxIsolationLevelAttributeTest() throws Fault {
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
  public void negativeTxQueryDataSourceAttributeTest() throws Fault {
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
  public void negativeTxUpdateDataSourceAttributeTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME,
        "negativeTxUpdateDataSourceAttributeTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeTxUpdateDataSourceAttributeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
