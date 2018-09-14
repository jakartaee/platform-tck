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
 * @(#)connManagerClient1.java	1.14 03/05/16
 */

package com.sun.ts.tests.connector.connManager;

import java.io.*;
import java.util.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

public class connManagerClient1 extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con = null;

  private String whitebox_tx = null;

  private String whitebox_notx = null;

  private String whitebox_xa = null;

  private String whitebox_anno = null;

  private String whitebox_multianno = null;

  private TSDataSource dsource = null;

  private boolean flag = false;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    connManagerClient1 theTests = new connManagerClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-tx; whitebox-notx; whitebox-xa;
   * whitebox-anno_no_md; whitebox-multianno;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups whitebox_tx adapter. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_tx = p.getProperty("whitebox-tx");
    whitebox_notx = p.getProperty("whitebox-notx");
    whitebox_xa = p.getProperty("whitebox-xa");
    whitebox_anno = p.getProperty("whitebox-anno_no_md");
    whitebox_multianno = p.getProperty("whitebox-multianno");

    logMsg("Using: " + whitebox_tx);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    try {
      nctx = new TSNamingContext();
      dsource = (TSDataSource) nctx.lookup(whitebox_tx);
      TestUtil.logMsg("dsource JNDI lookup: " + dsource);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testcheckConnectionManager
   *
   * @assertion_ids: Connector:SPEC:20; Connector:SPEC:34; Connector:JAVADOC:6;
   * Connector:JAVADOC:27; Connector:JAVADOC:141; Connector:JAVADOC:187;
   *
   * @test_Strategy: The TSDataSource file has method checkConnectionManager
   * which returns true if ConnectionManager is Serializable and false if it is
   * not. If true is returned then test passes else test fails. Also, we assume
   * that since we can establish connections, we must successfully meet the
   * listed JAVADOC assertions.
   */
  public void testcheckConnectionManager() throws Fault {
    try {
      flag = dsource.checkConnectionManager();

      if (flag == true) {
        TestUtil.logMsg("Connection Manager is Serializable");
      } else {
        throw new Fault("Connection Manager is NOT Serializable");
      }
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(sqle.getMessage(), sqle);
    }
  }

  /*
   * @testName: testTransactionSupportLevels
   *
   * @assertion_ids: Connector:SPEC:52; Connector:SPEC:55; Connector:SPEC:287;
   * Connector:SPEC:300;
   * 
   *
   * @test_Strategy: Test that the Appserver supports the loading and execution
   * of RA's w/ all levels of transaction support. This includes: NoTransaction,
   * LocalTransaction, and XATransaction. For our test, we assume that if we can
   * connect to our RA's that test each of these areas, then the assertion is
   * passed. We also want to verify the support of transactions from annotation
   * based RA. For this latter test, we check we can connect to whitebox_anno
   * which tests LocalTransaction and multianno which has NoTransaction.
   *
   * Also, because we are able to establish a connection to rars that use diff
   * version of dtd's and schema, we can assume the testing of assertion
   * Connector:SPEC:287.
   * 
   */
  public void testTransactionSupportLevels() throws Fault {

    TSDataSource dsource1 = null;
    TSDataSource dsource2 = null;
    TSDataSource dsource3 = null;
    TSDataSource dsource4 = null;
    TSDataSource dsource5 = null;
    TSConnection con1 = null;
    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    try {
      nctx = new TSNamingContext();

      // assume that connections to dsource1, 2,3 means that
      // our whitebox rar files are all supported and working
      // thus verifying assertion 52 and part of 55.
      dsource1 = (TSDataSource) nctx.lookup(whitebox_tx);
      dsource2 = (TSDataSource) nctx.lookup(whitebox_notx);
      dsource3 = (TSDataSource) nctx.lookup(whitebox_xa);
      dsource4 = (TSDataSource) nctx.lookup(whitebox_anno);
      dsource5 = (TSDataSource) nctx.lookup(whitebox_multianno);

      // we need to verify an annotation based transactional RA
      // to verify the other part of assertion 55 so we do that
      // with dsource4. (Also, we want to make sure our
      // annotation based RA is working too.)
      dsource4.setLogFlag(true);
      con1 = dsource4.getConnection();
      log = dsource4.getStateLog();

      String toCheck1 = "anno based NestedWorkXid1 child context submitted";
      String toCheck2 = "anno based NestedWorkXid1 parent context submitted";

      // Turn tracing on if you want to see the log contents
      TestUtil.logTrace(log.toString());

      for (int i = 0; i < log.size(); i++) {
        String str = (String) log.elementAt(i);
        if (str.startsWith(toCheck1)) {
          b1 = true;
        }
        if (str.startsWith(toCheck2)) {
          b2 = true;
        }
      }

      if (b1 && b2) {
        TestUtil.logMsg("Methods called correctly");
      } else {
        throw new Fault(
            "Transactional support levels not processed correctly.");
      }

      // Verify connection object works by doing some end to end tests.
      TestUtil.logMsg("Performing end to end verification...");

      // Insert into table
      try {
        dbutil.insertIntoTable(con1);
        TestUtil.logMsg("Values inserted into table!");
      } catch (Exception sqle) {
        TestUtil.logMsg("Exception inserting into table.");
        throw new Fault(sqle.getMessage(), sqle);
      }

      // Drop the table
      try {
        dbutil.dropTable(con1);
        TestUtil.logMsg("Table has been dropped!");
      } catch (Exception sqle) {
        TestUtil.logMsg("Exception dropping table.");
        throw new Fault(sqle.getMessage(), sqle);
      }

    } catch (Exception e) {
      TestUtil.logMsg("Failed testTransactionSupportLevels()");
      throw new Fault(e.getMessage(), e);
    } finally {
      try {
        if (con1 != null) {
          con1.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

  }

  /* cleanup */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup");
    try {
      flag = false;
      if (con != null) {
        con.close();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception on cleanup: " + e.getMessage(), e);
    }
  }
}
