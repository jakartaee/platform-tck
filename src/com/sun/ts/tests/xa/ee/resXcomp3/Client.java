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
 * @(#)Client.java	1.5 03/05/16
 */

/*
 * @(#)Client.java	1.20 02/07/19
 */
package com.sun.ts.tests.xa.ee.resXcomp3;

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

  // Expected resultSet from JDBC and EIS
  private int expResultstest1ds[] = { 1, 2, 3 };

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
   * @testName: test9
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82;
   * JavaEE:SPEC:84
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
  public void test9() throws Fault {
    String testname = "test9";
    boolean testResult = false;
    String tName1 = this.tName1;

    try {
      TestUtil.logTrace(testname);
      TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
      TestUtil.logMsg("Insert/Delete followed by a commit to a single table");
      TestUtil
          .logMsg("Database access is performed from EJB1Test and EJB2Test");

      TestUtil.logMsg("Creating the table");
      beanRef.destroyData(tName1);

      TestUtil.logMsg("Insert rows");
      ut.begin();
      beanRef.dbConnect(tName1);
      TestUtil.logMsg("Calling insert in Ejb1");
      beanRef.insert(tName1);
      beanRef.dbUnConnect(tName1);
      ut.commit();

      TestUtil.logMsg("Get test results");
      ut.begin();
      beanRef.dbConnect(tName1);
      testResult = beanRef.verifyData(new String("commit"), tName1,
          expResultstest1ds);

      TestUtil.logTrace("Test results");
      if (!testResult) {
        TestUtil.logMsg(testResult + " - verification of data failed");
      } else {
        TestUtil.logMsg(testResult + " - verification of data successfull");
      }
      beanRef.dbUnConnect(tName1);
      ut.commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName1);
        beanRef.remove();
      } catch (Exception e) {
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test10
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82;
   * JavaEE:SPEC:84
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a rollback to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_rollback
   */
  public void test10() throws Fault {
    String testname = "test10";
    boolean testResult = false;
    String tName1 = this.tName1;

    try {
      TestUtil.logTrace(testname);
      TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
      TestUtil.logMsg("Insert/Delete followed by a rollback to a single table");
      TestUtil
          .logMsg("Database access is performed from EJB1Test and EJB2Test");

      TestUtil.logMsg("Creating the table");
      beanRef.destroyData(tName1);

      TestUtil.logMsg("Insert rows");
      ut.begin();
      beanRef.dbConnect(tName1);
      TestUtil.logMsg("Calling insert in Ejb1");
      beanRef.insert(tName1);
      beanRef.dbUnConnect(tName1);
      ut.rollback();

      TestUtil.logMsg("Get test results");
      ut.begin();
      beanRef.dbConnect(tName1);
      testResult = beanRef.verifyData(new String("rollback"), tName1,
          expResultstest1ds);

      TestUtil.logTrace("Test results");
      if (!testResult) {
        TestUtil.logMsg(testResult + " - verification of data failed");
      } else {
        TestUtil.logMsg(testResult + " - verification of data successfull");
      }
      beanRef.dbUnConnect(tName1);
      ut.commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName1);
        beanRef.remove();
      } catch (Exception e) {
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test11
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82;
   * JavaEE:SPEC:84
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
  public void test11() throws Fault {
    String testname = "test11";
    boolean testResult = false;

    try {
      TestUtil.logTrace(testname);
      TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
      TestUtil.logMsg("Insert/Delete followed by a commit to a single table");
      TestUtil
          .logMsg("Database access is performed from EJB1Test and EJB2Test");

      TestUtil.logMsg("Creating the table");
      beanRef.destroyData("EIS");

      TestUtil.logMsg("Insert rows");
      ut.begin();
      beanRef.dbConnect("EIS");
      TestUtil.logMsg("Calling insert in Ejb1");
      beanRef.insert("EIS");
      beanRef.dbUnConnect("EIS");
      ut.commit();

      TestUtil.logMsg("Get test results");
      testResult = beanRef.verifyData("commit", "EIS", expResultstest1ds);

      TestUtil.logTrace("Test results : " + testResult);
      if (!testResult) {
        TestUtil.logMsg(testResult + " - verification of data failed");
      } else {
        TestUtil.logMsg(testResult + " - verification of data successfull");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData("EIS");
        beanRef.remove();
      } catch (Exception e) {
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test12
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82;
   * JavaEE:SPEC:84
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a rollback to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_rollback
   */
  public void test12() throws Fault {
    String testname = "test12";
    boolean testResult = false;

    try {
      TestUtil.logTrace(testname);
      TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
      TestUtil.logMsg("Insert/Delete followed by a rollback to a single table");
      TestUtil
          .logMsg("Database access is performed from EJB1Test and EJB2Test");

      TestUtil.logMsg("Creating the table");
      beanRef.destroyData("EIS");

      TestUtil.logMsg("Insert rows");
      ut.begin();
      beanRef.dbConnect("EIS");
      TestUtil.logMsg("Calling insert in Ejb1");
      beanRef.insert("EIS");
      beanRef.dbUnConnect("EIS");
      ut.rollback();

      TestUtil.logMsg("Get test results");
      testResult = beanRef.verifyData("rollback", "EIS", expResultstest1ds);

      TestUtil.logTrace("Test results : " + testResult);
      if (!testResult) {
        TestUtil.logMsg(testResult + " - verification of data failed");
      } else {
        TestUtil.logMsg(testResult + " - verification of data successfull");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData("EIS");
        beanRef.remove();
      } catch (Exception e) {
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

  /*
   * @testName: test13
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:73; JavaEE:SPEC:82;
   * JavaEE:SPEC:84
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
  public void test13() throws Fault {
    String testname = "test13";
    boolean testResult = false;

    try {
      TestUtil.logTrace(testname);
      TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
      TestUtil.logMsg("Insert/Delete followed by a commit to a single table");
      TestUtil
          .logMsg("Database access is performed from EJB1Test and EJB2Test");

      TestUtil.logMsg("Creating the table");
      beanRef.destroyData("EIS");

      try {
        ut.begin();
        beanRef.dbConnect("EIS");
        TestUtil.logMsg("Calling insert in Ejb1");
        beanRef.insert("EIS");
        beanRef.dbUnConnect("EIS");

        TestUtil.logMsg("Insert rows using notx whitebox");
        TestUtil.logMsg("Calling insertDup in Ejb1");
        beanRef.insertDup("EIS");

        TestUtil.logMsg("before commit of insert");
        ut.commit();
      } catch (javax.transaction.RollbackException rex) {
        TestUtil.printStackTrace(rex);
        testResult = true;
        TestUtil
            .logMsg("Captured Rollback Exception : testResult : " + testResult);
      } catch (Exception ex) {
        TestUtil.printStackTrace(ex);
        testResult = false;
        TestUtil.logMsg("Captured Exception : testResult : " + testResult);
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname + " failed", e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData("EIS");
        beanRef.remove();
      } catch (Exception e) {
      }
      ;
      if (!testResult)
        throw new Fault(testname + " failed");
    }
  }

}
