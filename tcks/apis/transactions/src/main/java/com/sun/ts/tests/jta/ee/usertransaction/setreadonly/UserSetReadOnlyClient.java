/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)UserSetTransactionTimeoutClient.java	1.21 03/05/16
 */

package com.sun.ts.tests.jta.ee.usertransaction.setreadonly;

import java.io.Serializable;
import java.util.Properties;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jta.ee.common.Transact;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

/**
 * The UserSetReadOnlyClient class tests setReadOnly() method of UserTransaction interface using
 * Sun's J2EE Reference Implementation.
 */

public class UserSetReadOnlyClient extends ServiceEETest implements Serializable {
    private static final String testName = "jta.ee.usertransaction.setreadonly";

    private UserTransaction userTransaction = null;

    public void setup(String[] args, Properties p) throws Exception {
        try {
            // Initializes the Environment
            Transact.init();
            logTrace("Test environment initialized");

            // Gets the User Transaction
            userTransaction = (UserTransaction) Transact.nctx.lookup("java:comp/UserTransaction");
            logMsg("User Transaction object is Obtained");

            if (userTransaction == null) {
                logErr("Unable to get User Transaction" + " Instance : Could not proceed with" + " tests");
                throw new Exception("couldnt proceed further");
            } else if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
                userTransaction.rollback();
            }
        } catch (Exception exception) {
            logErr("Setup Failed!");
            logTrace("Unable to get User Transaction Instance :" + " Could not proceed with tests");
            throw new Exception("Setup Failed", exception);
        }
    }// End of setup

    public static void main(String args[]) {
        UserSetReadOnlyClient userSetTransTout = new UserSetReadOnlyClient();
        com.sun.ts.lib.harness.Status s = userSetTransTout.run(args, System.out, System.err);
        s.exit();

    } // End of main

    // Beginning of TestCases

    /**
     * @testName: testUserSetReadOnly001
     * @test_Strategy: Before starting the User Transaction set the transaction read-only mode to true.
     */
    public void testUserSetReadOnly001() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;

        try {
            if (!userTransaction.isReadOnly()) {
                pass1 = true;
            }

            // Sets the readOnly value for the current transaction
            userTransaction.setReadOnly(true);

            // Starts a Global Transaction & associates with
            // Current Thread.
            userTransaction.begin();
            logMsg("UserTransaction Started");

            if (userTransaction.isReadOnly()) {
                logMsg("UserTransaction readOnly is true");
                pass2 = true;
            }

            // Commits the transaction.
            userTransaction.commit();

            if (!userTransaction.isReadOnly()) {
                pass3 = true;
            }

            if (pass1 && pass2 && pass3) {
                logMsg( "testUserSetReadOnly001 Passed" );
            } else if (!pass1) {
                throw new Exception("UserTransaction isReadOnly expected false on non active transaction");
            } else if (!pass2) {
                throw new Exception("UserTransaction isReadOnly expected true on new active transaction after setReadOnly");
            } else {
                throw new Exception("UserTransaction isReadOnly expected false on non active transaction");
            }
        } catch (Exception exception) {
            logErr("Exception " + exception.toString() + " was caught");
            throw new Exception("Exception was not thrown as" + " Expected in commit()", exception);
        }

    }// End of testUserSetReadOnly001

    /**
     * @testName: testUserSetReadOnly002
     * @test_Strategy: After starting the User Transaction set the transaction read-only mode to true.
     */

    public void testUserSetReadOnly002() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;

        try {
            if (!userTransaction.isReadOnly()) {
                pass1 = true;
            }
            // Starts a Global Transaction & associates with
            // Current Thread.
            userTransaction.begin();
            logMsg("UserTransaction Started");

            // Sets the readOnly value for the current transaction
            userTransaction.setReadOnly(true);

            if (!userTransaction.isReadOnly()) {
                pass2 = true;
            }

            // Commits the transaction.
            userTransaction.commit();

            if (!userTransaction.isReadOnly()) {
                pass3 = true;
            }

            if (pass1 && pass2 && pass3) {
                logMsg( "testUserSetReadOnly002 Passed" );
            } else if (!pass1) {
                throw new Exception("UserTransaction isReadOnly expected false on non active transaction");
            } else if (!pass2) {
                throw new Exception("UserTransaction isReadOnly expected false on existing active transaction after setReadOnly");
            } else {
                throw new Exception("UserTransaction isReadOnly expected false on non active transaction");
            }
        } catch (SystemException system) {
            logErr("Exception " + system.toString() + " was caught");
            throw new Exception("UnExpected Exception was caught:" + " Failed", system);
        } catch (Exception exception) {
            logErr("Exception " + exception.toString() + " was caught");
            throw new Exception("UnExpected Exception was caught:" + " Failed", exception);
        }

    }// End of testUserSetReadOnly002

    public void cleanup() throws Exception {
        try {
            // Removing noisy stack trace.
            if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
                // Frees Current Thread, from Transaction
                Transact.free();
                try {
                    userTransaction.rollback();
                } catch (Exception exception) {
                    throw new Exception(exception.getCause());
                }
                int retries = 1;
                while ((userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION) && (retries <= 5)) {
                    logMsg("cleanup(): retry # " + retries);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        throw new Exception(e.getCause());
                    }
                    retries++;
                }
                logMsg("Cleanup ok;");
            } else {
                logMsg("CleanUp not required as Transaction is not in Active state.");
            }
        } catch (Exception exception) {
            logErr("Cleanup Failed", exception);
            logTrace("Could not clean the environment");
        }

    }// End of cleanup

}// End of UserSetReadOnlyClient
