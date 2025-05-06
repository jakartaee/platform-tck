/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @(#)Client.java
 */

package com.sun.ts.tests.xa.ee.resXcomp1;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.transaction.UserTransaction;

import java.io.Serializable;
import java.util.Properties;
import java.util.Vector;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
@Tag("xa")
@Tag("platform")
public class Client extends ServiceEETest implements Serializable {

    private TSNamingContext tsNamingContext;
    protected String txRef = "java:comp/env/ejb/MyEjbReference";
    private TxBean beanRef;
    private UserTransaction userTransaction;
    private String tName1;
    private Integer tSize;
    private Integer fromKey1;
    private Integer fromKey2;
    private Integer toKey2;

    /* Test setup: */

    /*
     * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
     *
     * @class.testArgs: -ap tssql.stmt
     */
    public void setup(String args[], Properties p) throws Exception {
        try {
            TestUtil.init(p);
            TestUtil.logMsg("Setup tests");

            TestUtil.logMsg("Obtain naming context");
            tsNamingContext = new TSNamingContext();

            TestUtil.logMsg("Lookup TxBean: " + txRef);
            beanRef = (TxBean) tsNamingContext.lookup(txRef, TxBean.class);

            TestUtil.logMsg("Lookup java:comp/UserTransaction");
            userTransaction = (UserTransaction) tsNamingContext.lookup("java:comp/UserTransaction");

            // Get the table names
            TestUtil.logMsg("Lookup environment variables");
            this.tName1 = TestUtil.getTableName(TestUtil.getProperty("Xa_Tab1_Delete"));
            TestUtil.logTrace("tName1: " + this.tName1);

            // Get the table sizes
            this.tSize = (Integer) tsNamingContext.lookup("java:comp/env/size");
            TestUtil.logTrace("tSize: " + this.tSize);

            this.fromKey1 = (Integer) tsNamingContext.lookup("java:comp/env/fromKey1");
            TestUtil.logTrace("fromKey1: " + this.fromKey1);

            this.fromKey2 = (Integer) tsNamingContext.lookup("java:comp/env/fromKey2");
            TestUtil.logTrace("fromKey2: " + this.fromKey2);

            this.toKey2 = (Integer) tsNamingContext.lookup("java:comp/env/toKey2");
            TestUtil.logTrace("toKey2: " + this.toKey2);

            TestUtil.logMsg("Initialize " + txRef);
            beanRef.initialize();

            TestUtil.logMsg("Initialize logging data from server");
            beanRef.initLogging(p);

            TestUtil.logMsg("Setup ok");
        } catch (Exception e) {
            TestUtil.logErr("Exception in setup: ", e);
            throw new Exception("setup failed", e);
        }
    }

    /* Test cleanup */

    public void cleanup() throws Exception {
        TestUtil.logMsg("Cleanup ok");
    }

    /* Run test */

    /*
     * @testName: test1
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:68
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the TxBean (deployed as TX_REQUIRED) to a single RDBMS table.
     *
     * Insert/Delete followed by a commit to a single table.
     *
     * Database Access is performed from TxBean EJB.
     *
     */
    public void test1() throws Exception {
        String testname = "test1";
        Vector dbResults = new Vector();
        boolean testResult = false;
        boolean b1, b2;
        b1 = b2 = false;
        String tName1 = this.tName1;
        int tSize = this.tSize.intValue();
        int tRng = this.fromKey1.intValue();

        try {
            TestUtil.logTrace(testname);
            TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
            TestUtil.logMsg("Insert/Delete followed by a commit to a single table");
            TestUtil.logMsg("Database access is performed from TxBean EJB");

            TestUtil.logMsg("Creating the table");
            userTransaction.begin();
            beanRef.dbConnect(tName1);
            beanRef.createData(tName1);
            beanRef.dbUnConnect(tName1);
            userTransaction.commit();

            TestUtil.logMsg("Insert and delete some rows");
            userTransaction.begin();
            beanRef.dbConnect(tName1);
            TestUtil.logMsg("Inserting 2 new rows");
            if (beanRef.insert(tName1, tSize + 1))
                tSize++;
            if (beanRef.insert(tName1, tSize + 1))
                tSize++;
            TestUtil.logMsg("Deleting a row");
            beanRef.delete(tName1, tRng, tRng);
            beanRef.dbUnConnect(tName1);
            userTransaction.commit();

            TestUtil.logMsg("Get test results");
            userTransaction.begin();
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
            userTransaction.commit();

            if (b1 && b2)
                testResult = true;
            //
        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("Caught exception: " + e.getMessage());
            TestUtil.printStackTrace(e);
            throw new Exception(testname + " failed", e);
        } finally {
            // cleanup the bean
            try {
                userTransaction.begin();
                beanRef.dbConnect(tName1);
                beanRef.destroyData(tName1);
                beanRef.dbUnConnect(tName1);
                userTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ;
            if (!testResult)
                throw new Exception(testname + " failed");
        }
    }

    /*
     * @testName: test2
     *
     * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:68
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the TxBean (deployed as TX_REQUIRED) to a single RDBMS table.
     *
     * Insert/Delete followed by a rollback to a single table.
     *
     * Database Access is performed from TxBean EJB.
     *
     */
    public void test2() throws Exception {
        String testname = "test2";
        Vector dbResults = new Vector();
        boolean testResult = false;
        boolean b1, b2;
        b1 = b2 = false;
        String tName1 = this.tName1;
        int tSize = this.tSize.intValue();
        int tSizeOrig = this.tSize.intValue();
        int tRngFrom = this.fromKey2.intValue();
        int tRngTo = this.toKey2.intValue();

        try {
            TestUtil.logTrace(testname);
            TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
            TestUtil.logMsg("Insert/Delete followed by a rollback to a single table");
            TestUtil.logMsg("Database access is performed from TxBean EJB");

            TestUtil.logMsg("Creating the table");
            userTransaction.begin();
            beanRef.dbConnect(tName1);
            beanRef.createData(tName1);
            beanRef.dbUnConnect(tName1);
            userTransaction.commit();

            TestUtil.logMsg("Insert and delete some rows");
            userTransaction.begin();
            beanRef.dbConnect(tName1);
            TestUtil.logMsg("Inserting 2 new rows");
            if (beanRef.insert(tName1, tSize + 1))
                tSize++;
            if (beanRef.insert(tName1, tSize + 1))
                tSize++;
            TestUtil.logMsg("Deleting a row");
            beanRef.delete(tName1, tRngFrom, tRngTo);
            beanRef.dbUnConnect(tName1);
            userTransaction.rollback();

            TestUtil.logMsg("Get test results");
            userTransaction.begin();
            beanRef.dbConnect(tName1);
            dbResults = beanRef.getResults(tName1);

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
            beanRef.dbUnConnect(tName1);
            userTransaction.commit();

            if (b1)
                TestUtil.logTrace("b1 true");
            if (b2)
                TestUtil.logTrace("b2 true");

            if (b1 && b2)
                testResult = true;

        } catch (Exception e) {
            TestUtil.logErr("Caught exception: " + e.getMessage());
            TestUtil.printStackTrace(e);
            throw new Exception(testname + " failed", e);
        } finally {
            // cleanup the bean
            try {
                userTransaction.begin();
                beanRef.dbConnect(tName1);
                beanRef.destroyData(tName1);
                beanRef.dbUnConnect(tName1);
                userTransaction.commit();
            } catch (Exception e) {
            }
            ;
            if (!testResult)
                throw new Exception(testname + " failed");
        }
    }

    /*
     * @testName: test3
     *
     * @assertion_ids: JavaEE:SPEC:74
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the TxBean (deployed as TX_REQUIRED) to a single RDBMS table.
     *
     * Insert/Delete followed by a commit to a single table.
     *
     * Database Access is performed from TxBean EJB.
     *
     */
    public void test3() throws Exception {
        String testname = "test3";
        Vector dbResults = new Vector();
        boolean testResult = false;
        boolean b1, b2;
        b1 = b2 = false;
        String tName1 = this.tName1;
        int tSize = this.tSize.intValue();
        int tRng = this.fromKey1.intValue();

        try {
            TestUtil.logTrace(testname);
            TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
            TestUtil.logMsg("Insert/Delete followed by a commit to a single table");
            TestUtil.logMsg("Database access is performed from TxBean EJB");

            TestUtil.logMsg("Creating the data");
            userTransaction.begin();
            beanRef.dbConnect("EIS");
            beanRef.createData("EIS");
            beanRef.dbUnConnect("EIS");
            userTransaction.commit();

            TestUtil.logMsg("Insert and delete some rows");
            userTransaction.begin();
            beanRef.dbConnect("EIS");
            TestUtil.logMsg("Inserting 2 new rows");
            if (beanRef.insert("EIS", tSize + 1))
                tSize++;
            if (beanRef.insert("EIS", tSize + 1))
                tSize++;
            TestUtil.logMsg("Deleting a row");
            beanRef.delete("EIS", tRng, tRng);
            beanRef.dbUnConnect("EIS");
            userTransaction.commit();

            TestUtil.logMsg("Get test results");
            userTransaction.begin();
            beanRef.dbConnect("EIS");
            dbResults = beanRef.getResults("EIS");

            TestUtil.logMsg("Verifying the test results");
            if (!dbResults.contains((new Integer(tRng)).toString()))
                b1 = true;

            for (int i = 1; i <= tSize; i++) {
                if (i == tRng)
                    continue;
                else {
                    if (dbResults.contains((new Integer(i)).toString()))
                        b2 = true;
                    else {
                        b2 = false;
                        break;
                    }
                }
            }
            beanRef.dbUnConnect("EIS");
            userTransaction.commit();

            if (b1 && b2)
                testResult = true;
            //
        } catch (Exception e) {
            TestUtil.logErr("Caught exception: " + e.getMessage());
            TestUtil.printStackTrace(e);
            throw new Exception(testname + " failed", e);
        } finally {
            // cleanup the bean
            try {
                userTransaction.begin();
                beanRef.dbConnect("EIS");
                beanRef.destroyData("EIS");
                beanRef.dbUnConnect("EIS");
                userTransaction.commit();
            } catch (Exception e) {
            }
            ;
            if (!testResult)
                throw new Exception(testname + " failed");
        }
    }

    /*
     * @testName: test4
     *
     * @assertion_ids: JavaEE:SPEC:74
     *
     * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction interface. Perform global transactions
     * using the TxBean (deployed as TX_REQUIRED) to a single RDBMS table.
     *
     * Insert/Delete followed by a rollback to a single table.
     *
     * Database Access is performed from TxBean EJB.
     *
     */
    public void test4() throws Exception {
        String testname = "test4";
        Vector dbResults = new Vector();
        boolean testResult = false;
        boolean b1, b2;
        b1 = b2 = false;
        String tName1 = this.tName1;
        int tSize = this.tSize.intValue();
        int tSizeOrig = this.tSize.intValue();
        int tRngFrom = this.fromKey2.intValue();
        int tRngTo = this.toKey2.intValue();

        try {
            TestUtil.logTrace(testname);
            TestUtil.logMsg("Transaction propagation from Servlet, EJB or JSP");
            TestUtil.logMsg("Insert/Delete followed by a rollback to a single table");
            TestUtil.logMsg("Database access is performed from TxBean EJB");

            TestUtil.logMsg("Creating the table");
            userTransaction.begin();
            beanRef.dbConnect("EIS");
            beanRef.createData("EIS");
            beanRef.dbUnConnect("EIS");
            userTransaction.commit();

            TestUtil.logMsg("Insert and delete some rows");
            userTransaction.begin();
            beanRef.dbConnect("EIS");
            TestUtil.logMsg("Inserting 2 new rows");
            if (beanRef.insert("EIS", tSize + 1))
                tSize++;
            if (beanRef.insert("EIS", tSize + 1))
                tSize++;
            TestUtil.logMsg("Deleting a row");
            beanRef.delete("EIS", tRngFrom, tRngTo);
            beanRef.dbUnConnect("EIS");
            userTransaction.rollback();

            TestUtil.logMsg("Get test results");
            userTransaction.begin();
            beanRef.dbConnect("EIS");
            dbResults = beanRef.getResults("EIS");
            beanRef.dbUnConnect("EIS");
            userTransaction.commit();

            TestUtil.logMsg("Verifying the test results");
            for (int i = 1; i <= tSizeOrig; i++) {
                if (dbResults.contains((new Integer(i)).toString())) {
                    b1 = true;
                } else {
                    b1 = false;
                    break;
                }
            }
            for (int j = tSize; j > tSizeOrig; j--) {
                if (dbResults.contains((new Integer(j)).toString())) {
                    b2 = false;
                    break;
                } else {
                    b2 = true;
                }
            }

            if (b1)
                TestUtil.logTrace("b1 true");
            if (b2)
                TestUtil.logTrace("b2 true");

            if (b1 && b2)
                testResult = true;

        } catch (Exception e) {
            TestUtil.logErr("Caught exception: " + e.getMessage());
            TestUtil.printStackTrace(e);
            throw new Exception(testname + " failed", e);
        } finally {
            // cleanup the bean
            try {
                userTransaction.begin();
                beanRef.dbConnect("EIS");
                beanRef.destroyData("EIS");
                beanRef.dbUnConnect("EIS");
                userTransaction.commit();
            } catch (Exception e) {
            }
            ;
            if (!testResult)
                throw new Exception(testname + " failed");
        }
    }

}
