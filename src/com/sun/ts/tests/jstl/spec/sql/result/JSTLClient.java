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

package com.sun.ts.tests.jstl.spec.sql.result;

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

    setGeneralURI("/jstl/spec/sql/result");
    setContextRoot("/jstl_sql_result_web");
    setGoldenFileDir("/jstl/spec/sql/result");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveResultIsLimitedByMaxRowsTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * executed using maxRows attribute returns the correct value for
   * Result.isLimitedByMaxRows()
   */
  public void positiveResultIsLimitedByMaxRowsTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveResultIsLimitedByMaxRowsTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsLowerCaseTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the access to Result.getRows is case insensitive. -
   * For a row returned by getRows(), specify the column names in lower case.
   */
  public void positiveResultGetRowsLowerCaseTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsLowerCaseTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsUpperCaseTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the access to Result.getRows is case insensitive. -
   * For a row returned by getRows(), specify the column names in upper case.
   */
  public void positiveResultGetRowsUpperCaseTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsUpperCaseTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetColumnNamesTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate the values returned by Result.getColumns can be
   * used to access the column values returned by Result.getRows().
   */
  public void positiveResultGetColumnNamesTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetColumnNamesTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsByIndexTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that you can access each column for a given row
   * which is returned by Result.getRowsByIndex().
   */
  public void positiveResultGetRowsByIndexTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsByIndexTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsByIndexCountTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that the correct number of rows is returned by
   * Result.getRowsByIndex().
   */
  public void positiveResultGetRowsByIndexCountTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsByIndexCountTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsCountTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that the correct number of rows is returned by
   * Result.getRows().
   */
  public void positiveResultGetRowsCountTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetRowsCountTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetColumnNamesCountTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that the correct number of columns is returned by
   * Result.getColumnNames().
   */
  public void positiveResultGetColumnNamesCountTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveResultGetColumnNamesCountTest");
    invoke();
  }
}
