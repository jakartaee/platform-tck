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
 * @(#)UserSetTransactionTimeoutClient.java	1.21 03/05/16
 */

package com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout;

// General Java Package Imports
import java.util.Properties;
import java.io.Serializable;

// Common Utilities
import com.sun.ts.tests.jta.ee.common.Transact;

// TS Specific Utils
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

// Test Specific Imports.
import javax.transaction.UserTransaction;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.RollbackException;

/**
 * The UserSetTransactionTimeoutClient class tests setTransactionTimeout()
 * method of UserTransaction interface using Sun's J2EE Reference
 * Implementation.
 * 
 * @author P.Sandani Basha
 * @version 1.0.1, 09/17/99
 */

public class UserSetTransactionTimeoutClient extends ServiceEETest
    implements Serializable {
  private static final String testName = "jta.ee.usertransaction.settransactiontimeout";

  private UserTransaction userTransaction = null;

  public void setup(String[] args, Properties p) throws Fault {
    try {
      // Initializes the Environment
      Transact.init();
      logTrace("Test environment initialized");

      // Gets the User Transaction
      userTransaction = (UserTransaction) Transact.nctx
          .lookup("java:comp/UserTransaction");
      logMsg("User Transaction object is Obtained");

      if (userTransaction == null) {
        logErr("Unable to get User Transaction"
            + " Instance : Could not proceed with" + " tests");
        throw new Exception("couldnt proceed further");
      } else if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        userTransaction.rollback();
      }
    } catch (Exception exception) {
      logErr("Setup Failed!");
      logTrace("Unable to get User Transaction Instance :"
          + " Could not proceed with tests");
      throw new Fault("Setup Failed", exception);
    }
  }// End of setup

  public static void main(String args[]) {
    UserSetTransactionTimeoutClient userSetTransTout = new UserSetTransactionTimeoutClient();
    com.sun.javatest.Status s = userSetTransTout.run(args, System.out,
        System.err);
    s.exit();

  } // End of main

  // Beginning of TestCases

  /**
   * @testName: testUserSetTransactionTimeout001
   * @assertion_ids: JTA:JAVADOC:40
   * @test_Strategy: Before starting the User Transaction set the transaction
   *                 time out as 10 seconds.Allow the thread to sleep for 30
   *                 seconds then call commit() User Transaction.
   */
  public void testUserSetTransactionTimeout001() throws Fault {
    // TestCase id :- 4.6.1
    boolean pass = false;

    try {
      // Sets the timeout value for the current transaction
      // as 10 seconds
      userTransaction.setTransactionTimeout(10);

      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");

        // sleeps the current thread for 30 seconds
        Thread.currentThread().sleep(30000);

        // Commits the transaction.
        try {
          userTransaction.commit(); // may get
          // rollback or illegal state exception
        } catch (IllegalStateException ise) {
          logMsg("Exception caught as expected");
          pass = true;
        } catch (RollbackException rbe) {
          logMsg("RollbackException caught as expected");
          pass = true;
        }

        if (pass == false) {
          throw new Fault("Exception was not" + " thrown as Expected");
        }
      }
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("Exception was not thrown as" + " Expected in commit()",
          exception);
    }

  }// End of testUserSetTransactionTimeout001

  /**
   * @testName: testUserSetTransactionTimeout002
   * @assertion_ids: JTA:JAVADOC:40
   * @test_Strategy: Before starting the User Transaction set the transaction
   *                 time out as 10 seconds.Allow the thread to sleep for 5
   *                 seconds then Call commit() User Transaction.Check the
   *                 status of the User Transaction.
   */

  public void testUserSetTransactionTimeout002() throws Fault {
    // TestCase id :- 4.6.2

    try {
      // Sets the timeout value for the current transaction
      // as 10 seconds
      userTransaction.setTransactionTimeout(10);

      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");

        // sleeps the current thread for 5 seconds
        Thread.currentThread().sleep(5000);

        // Commits the transaction
        userTransaction.commit();
      }

      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("status is STATUS_NO_TRANSACTION");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          exception);
    }

  }// End of testUserSetTransactionTimeout002

  public void cleanup() throws Fault {
    try {
      // Frees Current Thread, from Transaction
      Transact.free();
      try {
        userTransaction.rollback();
      } catch (Exception exception) {
        throw new Fault(exception.getCause());
      }
      int retries = 1;
      while ((userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION)
          && (retries <= 5)) {
        logMsg("cleanup(): retry # " + retries);
        try {
          Thread.sleep(1000);
        } catch (Exception e) {
          throw new Fault(e.getCause());
        }
        retries++;
      }
      logMsg("Cleanup ok;");
    } catch (Exception exception) {
      logErr("Cleanup Failed", exception);
      logTrace("Could not clean the environment");
    }

  }// End of cleanup

}// End of UserSetTransactionTimeoutClient
