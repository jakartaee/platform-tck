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
 * @(#)Client.java	1.36 03/05/16
 */

package com.sun.ts.tests.jta.ee.txpropagationtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jta.ee.txpropagationtest.*;

import java.io.*;
import java.util.*;
import javax.transaction.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest implements Serializable {
  private TSNamingContext nctx = null;

  private Properties testProps = null;

  private static final String txRef = "java:comp/env/ejb/MyEjbReference";

  private TxBeanHome beanHome = null;

  private TxBean beanRef = null;

  private DBSupport db = null;

  private UserTransaction ut = null;

  private String tName1 = null;

  private String tName2 = null;

  private Integer tSize = null;

  private Integer fromKey1 = null;

  private Integer fromKey2 = null;

  private Integer toKey2 = null;

  public static void main(String[] args) {
    Client client = new Client();
    Status s = client.run(args, System.out, System.err);
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

      TestUtil.logMsg("Lookup TxBean: " + txRef);
      beanHome = (TxBeanHome) nctx.lookup(txRef, TxBeanHome.class);

      TestUtil.logMsg("Lookup java:comp/UserTransaction");
      ut = (UserTransaction) nctx.lookup("java:comp/UserTransaction");

      TestUtil.logMsg("Create DBSupport class");
      db = new DBSupport(testProps);

      // Get the table names
      TestUtil.logMsg("Lookup environment variables");
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("JTA_Tab1_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);

      this.tName2 = TestUtil
          .getTableName(TestUtil.getProperty("JTA_Tab2_Delete"));
      TestUtil.logTrace("tName2: " + this.tName2);

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
      beanRef = (TxBean) beanHome.create(p);

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

  /* Run test */

  /*
   * @testName: test1
   *
   * @assertion_ids: JavaEE:SPEC:39; JavaEE:SPEC:45; JavaEE:SPEC:46;
   * EJB:SPEC:543.1; EJB:SPEC:543.2
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the TxBean (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from TxBean EJB.
   *
   */
  public void test1() throws Fault {
    String testname = "test1";

    TestUtil.logTrace(testname);
    TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
    TestUtil.logMsg("Insert/Delete followed by a commit to a single table");
    TestUtil.logMsg("Database access is performed from TxBean EJB");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName1 = this.tName1;
    int tSize = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logMsg("Creating the table");
      ut.begin();
      beanRef.dbConnect(tName1);
      beanRef.createData(tName1);
      beanRef.dbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Insert and delete some rows");
      ut.begin();
      beanRef.dbConnect(tName1);
      TestUtil.logMsg("Inserting 2 new rows");
      if (beanRef.insert(tName1, tSize + 1))
        tSize++;
      if (beanRef.insert(tName1, tSize + 1))
        tSize++;
      TestUtil.logMsg("Deleting a row");
      beanRef.delete(tName1, tRng, tRng);
      beanRef.dbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Get test results");
      ut.begin();
      beanRef.dbConnect(tName1);
      dbResults = beanRef.getResults(tName1);

      TestUtil.logMsg("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= tSize; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }
      beanRef.dbUnConnect(tName1);
      ut.commit();

      if (b1 && b2)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        ut.begin();
        beanRef.dbConnect(tName1);
        beanRef.destroyData(tName1);
        beanRef.dbUnConnect(tName1);
        ut.commit();
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test2
   *
   * @assertion_ids: JavaEE:SPEC:39; JavaEE:SPEC:45; JavaEE:SPEC:46;
   * EJB:SPEC:543.1; EJB:SPEC:543.2
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the TxBean (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a rollback to a single table.
   *
   * Database Access is performed from TxBean EJB.
   *
   */
  public void test2() throws Fault {
    String testname = "test2";

    TestUtil.logTrace(testname);
    TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
    TestUtil.logMsg("Insert/Delete followed by a rollback to a single table");
    TestUtil.logMsg("Database access is performed from TxBean EJB");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName2 = this.tName2;
    int tSize = this.tSize.intValue();
    int tSizeOrig = this.tSize.intValue();
    int tRngFrom = this.fromKey2.intValue();
    int tRngTo = this.toKey2.intValue();

    try {
      TestUtil.logMsg("Creating the table");
      ut.begin();
      beanRef.dbConnect(tName2);
      beanRef.createData(tName2);
      beanRef.dbUnConnect(tName2);
      ut.commit();

      TestUtil.logMsg("Insert and delete some rows");
      ut.begin();
      beanRef.dbConnect(tName2);
      TestUtil.logMsg("Inserting 2 new rows");
      if (beanRef.insert(tName2, tSize + 1))
        tSize++;
      if (beanRef.insert(tName2, tSize + 1))
        tSize++;
      TestUtil.logMsg("Deleting a row");
      beanRef.delete(tName2, tRngFrom, tRngTo);
      beanRef.dbUnConnect(tName2);
      ut.rollback();

      TestUtil.logMsg("Get test results");
      ut.begin();
      beanRef.dbConnect(tName2);
      dbResults = beanRef.getResults(tName2);

      TestUtil.logMsg("Verifying the test results");
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
      beanRef.dbUnConnect(tName2);
      ut.commit();

      if (b1)
        TestUtil.logTrace("b1 true");
      if (b2)
        TestUtil.logTrace("b2 true");

      if (b1 && b2)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        ut.begin();
        beanRef.dbConnect(tName2);
        beanRef.destroyData(tName2);
        beanRef.dbUnConnect(tName2);
        ut.commit();
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: JavaEE:SPEC:39; JavaEE:SPEC:45; JavaEE:SPEC:46;
   * EJB:SPEC:543.1; EJB:SPEC:543.2
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the TxBean (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table and checking TxStatus.
   *
   * Database Access is performed from TxBean EJB.
   *
   */
  public void test3() throws Fault {
    String testname = "test3";

    TestUtil.logTrace(testname);
    TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
    TestUtil
        .logMsg("Insert/Delete followed by a commit, and checking TxStatus");
    TestUtil.logMsg("Database access is performed from TxBean EJB");

    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName2 = this.tName2;
    int tSize = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    int txStatus1, txStatus2, txStatus3;

    try {
      txStatus1 = ut.getStatus();
      TestUtil.logTrace("Tx Status: " + txStatus1);

      TestUtil.logMsg("BEGIN transaction");
      ut.begin();

      txStatus2 = ut.getStatus();
      TestUtil.logMsg("Tx Status: " + txStatus2);

      TestUtil.logTrace("Calling dbConnect method");
      beanRef.dbConnect(tName2);
      beanRef.createData(tName2);

      TestUtil.logMsg("Inserting 2 new rows");
      if (beanRef.insert(tName2, tSize + 1))
        tSize++;
      if (beanRef.insert(tName2, tSize + 1))
        tSize++;

      TestUtil.logMsg("Deleting a row");
      beanRef.delete(tName2, tRng, tRng);

      beanRef.dbUnConnect(tName2);
      TestUtil.logMsg("COMMIT transaction");
      ut.commit();

      txStatus3 = ut.getStatus();
      TestUtil.logMsg("Tx Status: " + txStatus3);

      // Verify the test results
      TestUtil.logMsg("Verifying the test results");
      if (txStatus1 == javax.transaction.Status.STATUS_NO_TRANSACTION)
        b1 = true;
      if (txStatus2 == javax.transaction.Status.STATUS_ACTIVE)
        b2 = true;
      if (txStatus3 == javax.transaction.Status.STATUS_NO_TRANSACTION)
        b3 = true;

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        ut.begin();
        beanRef.dbConnect(tName2);
        beanRef.destroyData(tName2);
        beanRef.dbUnConnect(tName2);
        ut.commit();
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test4
   *
   * @assertion_ids: JavaEE:SPEC:39; JavaEE:SPEC:45; JavaEE:SPEC:46;
   * EJB:SPEC:543.1; EJB:SPEC:543.2
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions directly to RDBMS.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from Servlet, EJB or JSP.
   *
   */
  public void test4() throws Fault {
    String testname = "test4";

    TestUtil.logTrace(testname);
    TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
    TestUtil.logMsg("Insert/Delete followed by a commit to a single table");
    TestUtil.logMsg("Database access is performed from Servlet, EJB or JSP");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName1 = this.tName1;
    int tSize = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logMsg("Creating the table");
      ut.begin();
      db.dbConnect(tName1);
      db.createData(tName1);
      db.dbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Insert and delete some rows");
      ut.begin();
      db.dbConnect(tName1);
      TestUtil.logMsg("Inserting 2 new rows");
      if (db.insert(tName1, tSize + 1))
        tSize++;
      if (db.insert(tName1, tSize + 1))
        tSize++;
      TestUtil.logMsg("Deleting a row");
      db.delete(tName1, tRng, tRng);
      db.dbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Get test results");
      ut.begin();
      db.dbConnect(tName1);
      dbResults = db.getResults(tName1);

      TestUtil.logMsg("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= tSize; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }
      db.dbUnConnect(tName1);
      ut.commit();

      if (b1 && b2)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        ut.begin();
        db.dbConnect(tName1);
        db.destroyData(tName1);
        db.dbUnConnect(tName1);
        ut.commit();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test5
   *
   * @assertion_ids: JavaEE:SPEC:39; JavaEE:SPEC:45; JavaEE:SPEC:46;
   * EJB:SPEC:543.1; EJB:SPEC:543.2
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions directly to RDBMS.
   * 
   * Insert/Delete followed by a rollback to a single table.
   *
   * Database Access is performed from Servlet, EJB or JSP.
   *
   */
  public void test5() throws Fault {
    String testname = "test5";

    TestUtil.logTrace(testname);
    TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
    TestUtil.logMsg("Insert/Delete followed by a rollback to a single table");
    TestUtil.logMsg("Database access is performed Servlet, EJB or JSP");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName2 = this.tName2;
    int tSize = this.tSize.intValue();
    int tSizeOrig = this.tSize.intValue();
    int tRngFrom = this.fromKey2.intValue();
    int tRngTo = this.toKey2.intValue();

    try {
      TestUtil.logMsg("Creating the table");
      ut.begin();
      db.dbConnect(tName2);
      db.createData(tName2);
      db.dbUnConnect(tName2);
      ut.commit();

      TestUtil.logMsg("Insert and delete some rows");
      ut.begin();
      db.dbConnect(tName2);
      TestUtil.logMsg("Inserting 2 new rows");
      if (db.insert(tName2, tSize + 1))
        tSize++;
      if (db.insert(tName2, tSize + 1))
        tSize++;
      TestUtil.logMsg("Deleting a row");
      db.delete(tName2, tRngFrom, tRngTo);
      db.dbUnConnect(tName2);
      ut.rollback();

      TestUtil.logMsg("Get test results");
      ut.begin();
      db.dbConnect(tName2);
      dbResults = db.getResults(tName2);

      TestUtil.logMsg("Verifying the test results");
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
      db.dbUnConnect(tName2);
      ut.commit();

      if (b1)
        TestUtil.logTrace("b1 true");
      if (b2)
        TestUtil.logTrace("b2 true");

      if (b1 && b2)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        ut.begin();
        db.dbConnect(tName2);
        db.destroyData(tName2);
        db.dbUnConnect(tName2);
        ut.commit();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test6
   *
   * @assertion_ids: JavaEE:SPEC:39; JavaEE:SPEC:45; JavaEE:SPEC:46;
   * EJB:SPEC:543.1; EJB:SPEC:543.2
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions directly to RDBMS.
   * 
   * Insert/Delete followed by a commit to a single table and checking TxStatus.
   *
   * Database Access is performed from Servlet, EJB or JSP.
   *
   */
  public void test6() throws Fault {
    String testname = "test6";

    TestUtil.logTrace(testname);
    TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
    TestUtil
        .logMsg("Insert/Delete followed by a commit, and checking TxStatus");
    TestUtil.logMsg("Database access is performed from Servlet, EJB or JSP");

    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName2 = this.tName2;
    int tSize = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    int txStatus1, txStatus2, txStatus3;

    try {
      txStatus1 = ut.getStatus();
      TestUtil.logTrace("Tx Status: " + txStatus1);

      TestUtil.logMsg("BEGIN transaction");
      ut.begin();

      txStatus2 = ut.getStatus();
      TestUtil.logMsg("Tx Status: " + txStatus2);

      TestUtil.logMsg("Calling dbConnect method");
      db.dbConnect(tName2);
      db.createData(tName2);

      TestUtil.logMsg("Inserting 2 new rows");
      if (db.insert(tName2, tSize + 1))
        tSize++;
      if (db.insert(tName2, tSize + 1))
        tSize++;

      TestUtil.logMsg("Deleting a row");
      db.delete(tName2, tRng, tRng);

      db.dbUnConnect(tName2);
      TestUtil.logMsg("COMMIT transaction");
      ut.commit();

      txStatus3 = ut.getStatus();
      TestUtil.logMsg("Tx Status: " + txStatus3);

      // Verify the test results
      TestUtil.logMsg("Verifying the test results");
      if (txStatus1 == javax.transaction.Status.STATUS_NO_TRANSACTION)
        b1 = true;
      if (txStatus2 == javax.transaction.Status.STATUS_ACTIVE)
        b2 = true;
      if (txStatus3 == javax.transaction.Status.STATUS_NO_TRANSACTION)
        b3 = true;

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        ut.begin();
        db.dbConnect(tName2);
        db.destroyData(tName2);
        db.dbUnConnect(tName2);
        ut.commit();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }
}
