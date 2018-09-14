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
 * @(#)Client.java	1.3 03/05/16
 */

/*
 * @(#)Client.java	1.20 02/07/19
 */
package com.sun.ts.tests.xa.ee.xresXcomp1;

import java.io.*;
import java.util.*;
import javax.transaction.*;
import javax.transaction.xa.XAException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest implements Serializable {
  private TSNamingContext nctx = null;

  private Properties testProps = null;

  private static final String txRef = "java:comp/env/ejb/MyEjbReference";

  private Ejb1TestHome beanHome = null;

  private Ejb1Test beanRef = null;

  private UserTransaction ut = null;

  private String tName1 = null;

  private Integer tSize = null;

  private Integer fromKey1 = null;

  private Integer fromKey2 = null;

  private Integer toKey2 = null;

  // Expected resultSet from JDBC and EIS
  // private int expResultstest1ds1 [] = { 1,2,3,4,5 };
  // private int expResultstest1ds2 [] = { 1,2,3 };

  /* Run test in standalone mode */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * 
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String args[], Properties p) throws Fault {
    try {
      this.testProps = p;
      TestUtil.init(p);
      TestUtil.logMsg("Setup tests");

      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      TestUtil.logMsg("Lookup Ejb1Test: " + txRef);
      beanHome = (Ejb1TestHome) nctx.lookup(txRef, Ejb1TestHome.class);

      TestUtil.logMsg("Lookup java:comp/UserTransaction");
      ut = (UserTransaction) nctx.lookup("java:comp/UserTransaction");

      // Get the table names
      TestUtil.logMsg("Lookup environment variables");
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("Xa_Tab1_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);

      // Get the table sizes
      this.tSize = (Integer) nctx.lookup("java:comp/env/size");
      TestUtil.logTrace("tSize: " + this.tSize);

      this.fromKey1 = (Integer) nctx.lookup("java:comp/env/fromKey1");
      TestUtil.logTrace("fromKey1: " + this.fromKey1);

      this.fromKey2 = (Integer) nctx.lookup("java:comp/env/fromKey2");
      TestUtil.logTrace("fromKey2: " + this.fromKey2);

      this.toKey2 = (Integer) nctx.lookup("java:comp/env/toKey2");
      TestUtil.logTrace("toKey2: " + this.toKey2);

      TestUtil.logMsg("Create EJB instance of " + txRef);
      beanRef = (Ejb1Test) beanHome.create(testProps);

      TestUtil.logMsg("Initialize logging data from server in Client");
      beanRef.initLogging(p);

      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.logErr("Exception in setup: ", e);
      throw new Fault("setup failed", e);
    }
  }

  /* Test cleanup */

  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup ok");
  }

  /* Run tests */
  /*
   * @testName: test14
   *
   * @assertion_ids: JavaEE:SPEC:90; JavaEE:SPEC:92; JavaEE:SPEC:79;
   * JavaEE:SPEC:76; JavaEE:SPEC:74; JavaEE:SPEC:70
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_commit
   */
  public void test14() throws Fault {
    String testname = "test14";
    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName1 = this.tName1;
    int tSize = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace(testname);
      TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
      TestUtil.logMsg(
          "Insert/Delete followed by a commit to a single table in EIS and JDBC");
      TestUtil.logMsg("Database access is performed from EJB1Test");

      TestUtil.logMsg("Creating the data in JDBC table");
      ut.begin();
      beanRef.txDbConnect(tName1);
      beanRef.createData(tName1);
      beanRef.txDbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Creating the data in EIS table");
      ut.begin();
      beanRef.txDbConnect("EIS");
      beanRef.createData("EIS");
      beanRef.txDbUnConnect("EIS");
      ut.commit();

      TestUtil.logMsg("Insert and delete some rows");
      ut.begin();

      beanRef.dbConnect(tName1);
      TestUtil.logMsg("Inserting 2 new rows in JDBC");
      if (beanRef.insert(tName1, tSize + 1))
        tSize++;
      if (beanRef.insert(tName1, tSize + 1))
        tSize++;
      // TestUtil.logMsg("Deleting a row in JDBC");
      // beanRef.delete(tName1, tRng, tRng);
      beanRef.dbUnConnect(tName1);

      tSize = tSize - 2;

      beanRef.dbConnect("EIS");
      TestUtil.logMsg("Inserting 2 new rows in EIS");
      if (beanRef.insert("EIS", tSize + 1))
        tSize++;
      if (beanRef.insert("EIS", tSize + 1))
        tSize++;
      // TestUtil.logMsg("Deleting a row in EIS");
      // beanRef.delete("EIS", tRng, tRng);
      beanRef.dbUnConnect("EIS");

      ut.commit();

      TestUtil.logMsg("Get test results for JDBC");
      ut.begin();
      beanRef.txDbConnect(tName1);
      dbResults = beanRef.getResults(tName1);
      for (int i = 0; i < dbResults.size(); i++)
        TestUtil.logTrace(
            "JDBC dbResults.elementAt" + i + " :" + dbResults.elementAt(i));

      beanRef.txDbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Verifying the test results of JDBC");
      // if( ! dbResults.contains(new Integer(tRng)) )
      // b1 = true;

      for (int i = 1; i <= tSize; i++) {
        // if(i == tRng)
        // continue;
        // else
        // {
        if (dbResults.contains(new Integer(i)))
          b2 = true;
        else {
          b2 = false;
          break;
        }
        // }
      }

      dbResults = null;
      TestUtil.logMsg("Get test results for EIS");
      ut.begin();
      beanRef.txDbConnect("EIS");
      dbResults = beanRef.getResults("EIS");
      beanRef.txDbUnConnect("EIS");
      ut.commit();
      for (int i = 0; i < dbResults.size(); i++)
        TestUtil.logTrace(
            "EIS dbResults.elementAt" + i + " :" + dbResults.elementAt(i));

      TestUtil.logMsg("Verifying the test results of EIS");
      // if( ! dbResults.contains(new Integer(tRng)) )
      // b3 = true;

      for (int i = 1; i <= tSize; i++) {
        // if(i == tRng)
        // continue;
        // else
        // {
        if (dbResults.contains(new Integer(i).toString()))
          b4 = true;
        else {
          b4 = false;
          break;
        }
        // }
      }
      // TestUtil.logMsg("b1 : " + b1);
      TestUtil.logTrace("b2 : " + b2);
      // TestUtil.logMsg("b3 : " + b3);
      TestUtil.logTrace("b4 : " + b4);

      if (b2 && b4)
        // if( b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        TestUtil.logMsg("Finally cleaning the test data for JDBC");
        ut.begin();
        beanRef.txDbConnect(tName1);
        beanRef.destroyData(tName1);
        beanRef.txDbUnConnect(tName1);
        ut.commit();
        TestUtil.logMsg("Finally cleaning the test data for EIS");
        ut.begin();
        beanRef.txDbConnect("EIS");
        beanRef.destroyData("EIS");
        beanRef.txDbUnConnect("EIS");
        ut.commit();
        beanRef.remove();
      } catch (Exception e) {
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test15
   *
   * @assertion_ids: JavaEE:SPEC:90; JavaEE:SPEC:92; JavaEE:SPEC:79;
   * JavaEE:SPEC:76; JavaEE:SPEC:74; JavaEE:SPEC:70
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_commit
   */
  public void test15() throws Fault {
    String testname = "test15";
    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName1 = this.tName1;
    int tSize = this.tSize.intValue();
    int tSizeOrig = this.tSize.intValue();
    int tRngFrom = this.fromKey2.intValue();
    int tRngTo = this.toKey2.intValue();

    try {
      TestUtil.logTrace(testname);
      TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
      TestUtil.logMsg(
          "Insert/Delete followed by a commit to a single table in EIS and JDBC");
      TestUtil.logMsg("Database access is performed from EJB1Test");

      TestUtil.logMsg("Creating the data in JDBC table");
      ut.begin();
      beanRef.txDbConnect(tName1);
      beanRef.createData(tName1);
      beanRef.txDbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Creating the data in EIS table");
      ut.begin();
      beanRef.txDbConnect("EIS");
      beanRef.createData("EIS");
      beanRef.txDbUnConnect("EIS");
      ut.commit();

      TestUtil.logMsg("Insert and delete some rows");
      ut.begin();

      beanRef.dbConnect(tName1);
      TestUtil.logMsg("Inserting 2 new rows in JDBC");
      if (beanRef.insert(tName1, tSize + 1))
        tSize++;
      if (beanRef.insert(tName1, tSize + 1))
        tSize++;
      TestUtil.logMsg("Deleting a row in JDBC");
      beanRef.delete(tName1, tRngFrom, tRngTo);
      beanRef.dbUnConnect(tName1);

      tSize = tSize - 2;

      beanRef.dbConnect("EIS");
      TestUtil.logMsg("Inserting 2 new rows in EIS");
      if (beanRef.insert("EIS", tSize + 1))
        tSize++;
      if (beanRef.insert("EIS", tSize + 1))
        tSize++;
      TestUtil.logMsg("Deleting a row in EIS");
      beanRef.delete("EIS", tRngFrom, tRngTo);
      beanRef.dbUnConnect("EIS");

      ut.rollback();

      TestUtil.logMsg("Get test results for JDBC");
      ut.begin();
      beanRef.txDbConnect(tName1);
      dbResults = beanRef.getResults(tName1);
      for (int i = 0; i < dbResults.size(); i++)
        TestUtil.logTrace(
            "JDBC dbResults.elementAt" + i + " :" + dbResults.elementAt(i));

      beanRef.txDbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Verifying the test results of JDBC");
      for (int i = 1; i <= tSizeOrig; i++) {
        if (dbResults.contains(new Integer(i))) {
          b1 = true;
        } else {
          b1 = false;
          break;
        }
      }
      for (int j = tSize; j > tSizeOrig; j--) {
        if (dbResults.contains(new Integer(j))) {
          b2 = false;
          break;
        } else {
          b2 = true;
        }
      }

      dbResults = null;
      TestUtil.logMsg("Get test results for EIS");
      ut.begin();
      beanRef.txDbConnect("EIS");
      dbResults = beanRef.getResults("EIS");
      beanRef.txDbUnConnect("EIS");
      ut.commit();
      for (int i = 0; i < dbResults.size(); i++)
        TestUtil.logTrace(
            "EIS dbResults.elementAt" + i + " :" + dbResults.elementAt(i));

      TestUtil.logMsg("Verifying the test results of EIS");
      for (int i = 1; i <= tSizeOrig; i++) {
        if (dbResults.contains((new Integer(i)).toString())) {
          b3 = true;
        } else {
          b3 = false;
          break;
        }
      }
      for (int j = tSize; j > tSizeOrig; j--) {
        if (dbResults.contains((new Integer(j)).toString())) {
          b4 = false;
          break;
        } else {
          b4 = true;
        }
      }
      TestUtil.logTrace("b1 : " + b1);
      TestUtil.logTrace("b2 : " + b2);
      TestUtil.logTrace("b3 : " + b3);
      TestUtil.logTrace("b4 : " + b4);

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        TestUtil.logMsg("Finally cleaning the test data for JDBC");
        ut.begin();
        beanRef.txDbConnect(tName1);
        beanRef.destroyData(tName1);
        beanRef.txDbUnConnect(tName1);
        ut.commit();
        TestUtil.logMsg("Finally cleaning the test data for EIS");
        ut.begin();
        beanRef.txDbConnect("EIS");
        beanRef.destroyData("EIS");
        beanRef.txDbUnConnect("EIS");
        ut.commit();
        beanRef.remove();
      } catch (Exception e) {
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test16
   *
   * @assertion_ids: JavaEE:SPEC:90; JavaEE:SPEC:92; JavaEE:SPEC:79;
   * JavaEE:SPEC:76; JavaEE:SPEC:74; JavaEE:SPEC:70
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_commit
   */
  public void test16() throws Fault {
    String testname = "test16";
    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4, b5;
    b1 = b2 = b3 = b4 = b5 = false;
    String tName1 = this.tName1;
    int tSize = this.tSize.intValue();
    int tSizeOrig = this.tSize.intValue();
    int tSizeDuplicate = this.tSize.intValue() + 1;
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace(testname);
      TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
      TestUtil.logMsg(
          "Insert/Delete followed by a commit to a single table in EIS and JDBC");
      TestUtil.logMsg("Database access is performed from EJB1Test");

      TestUtil.logMsg("Creating the data in JDBC table");
      ut.begin();
      beanRef.txDbConnect(tName1);
      beanRef.createData(tName1);
      beanRef.txDbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Creating the data in EIS table");
      ut.begin();
      beanRef.txDbConnect("EIS");
      beanRef.createData("EIS");
      beanRef.txDbUnConnect("EIS");
      ut.commit();

      try {
        TestUtil.logMsg("Insert and delete some rows");
        ut.begin();

        beanRef.dbConnect(tName1);
        TestUtil.logMsg("Inserting 2 new rows in JDBC");
        if (beanRef.insert(tName1, tSize + 1))
          tSize++;
        if (beanRef.insert(tName1, tSize + 1))
          tSize++;
        TestUtil.logMsg("Deleting a row in JDBC");
        beanRef.delete(tName1, tRng, tRng);
        beanRef.dbUnConnect(tName1);

        tSize = tSize - 2;

        beanRef.dbConnect("EIS");
        TestUtil.logMsg("Inserting 2 new rows in EIS");
        if (beanRef.insert("EIS", tSize + 1))
          tSize++;
        if (beanRef.insert("EIS", tSize + 1))
          tSize++;
        // TestUtil.logMsg("Deleting a row in EIS");
        // beanRef.delete("EIS", tRng, tRng);
        beanRef.dbUnConnect("EIS");

        TestUtil.logMsg("Insert rows using notx whitebox");
        TestUtil
            .logMsg("Calling insertDup in Ejb1 with value = " + tSizeDuplicate);
        beanRef.insertDup("EIS", Integer.toString(tSizeDuplicate)); // 6
        // beanRef.insertDup("EIS");

        ut.commit();
      } catch (javax.transaction.RollbackException rex) {
        TestUtil.printStackTrace(rex);
        b5 = true;
        TestUtil.logMsg("Captured Rollback Exception : b5 : " + b5);
      } catch (Exception ex) {
        TestUtil.printStackTrace(ex);
        b5 = false;
        TestUtil.logMsg("Captured Exception : b5 : " + b5);
      }

      TestUtil.logMsg("Get test results for JDBC");
      ut.begin();
      beanRef.txDbConnect(tName1);
      dbResults = beanRef.getResults(tName1);
      for (int i = 0; i < dbResults.size(); i++)
        TestUtil.logTrace(
            "JDBC dbResults.elementAt" + i + " :" + dbResults.elementAt(i));

      beanRef.txDbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Verifying the test results of JDBC");
      for (int i = 1; i <= tSizeOrig; i++) {
        if (dbResults.contains(new Integer(i))) {
          b1 = true;
        } else {
          b1 = false;
          break;
        }
      }
      for (int j = tSize; j > tSizeOrig; j--) {
        if (dbResults.contains(new Integer(j))) {
          b2 = false;
          break;
        } else {
          b2 = true;
        }
      }

      dbResults = null;
      TestUtil.logMsg("Get test results for EIS");
      ut.begin();
      beanRef.txDbConnect("EIS");
      dbResults = beanRef.getResults("EIS");
      beanRef.txDbUnConnect("EIS");
      ut.commit();
      for (int i = 0; i < dbResults.size(); i++)
        TestUtil.logTrace(
            "EIS dbResults.elementAt" + i + " :" + dbResults.elementAt(i));

      TestUtil.logMsg("Verifying the test results of EIS");
      for (int i = 1; i <= (tSizeOrig + 1); i++) { // to include tSizeDuplicate
                                                   // also
        if (dbResults.contains((new Integer(i)).toString())) {
          b3 = true;
        } else {
          b3 = false;
          break;
        }
      }
      for (int j = tSize; j > tSizeOrig; j--) {
        if (j == tSizeDuplicate) {
          continue;
        } else {
          if (dbResults.contains((new Integer(j)).toString())) {
            b4 = false;
            break;
          } else {
            b4 = true;
          }
        }
      }
      TestUtil.logTrace("b1 : " + b1);
      TestUtil.logTrace("b2 : " + b2);
      TestUtil.logTrace("b3 : " + b3);
      TestUtil.logTrace("b4 : " + b4);
      TestUtil.logTrace("b5 : " + b5);

      if (b1 && b2 && b3 && b4 && b5)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        TestUtil.logMsg("Finally cleaning the test data for JDBC");
        ut.begin();
        beanRef.txDbConnect(tName1);
        beanRef.destroyData(tName1);
        beanRef.txDbUnConnect(tName1);
        ut.commit();
        TestUtil.logMsg("Finally cleaning the test data for EIS");
        ut.begin();
        beanRef.txDbConnect("EIS");
        beanRef.destroyData("EIS");
        beanRef.txDbUnConnect("EIS");
        ut.commit();
        beanRef.remove();
      } catch (Exception e) {
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

}
