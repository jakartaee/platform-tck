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
 * @(#)UserSetRollbackOnlyClient.java	1.19 03/05/16
 */

package com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly;

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
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.RollbackException;

/**
 * The UserSetRollbackOnlyClient class tests setRollbackOnly() method of
 * UserTransaction interface using Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class UserSetRollbackOnlyClient extends ServiceEETest
    implements Serializable {
  private static final int SLEEPTIME = 2000;

  private static final String testName = "jta.ee.usertransaction.setrollbackonly";

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
    UserSetRollbackOnlyClient userSetRollbackOnlyClientInst = new UserSetRollbackOnlyClient();
    com.sun.javatest.Status s = userSetRollbackOnlyClientInst.run(args,
        System.out, System.err);
    s.exit();

  }// End of main

  // Beginning of TestCases

  /**
   * @testName: testUserSetRollbackOnly001
   * @assertion_ids: JTA:JAVADOC:36
   * @test_Strategy: Without starting the User Transaction call
   *                 setRollbackOnly() on User Transaction.
   */

  public void testUserSetRollbackOnly001() throws Fault {
    // TestCase id :- 4.5.1

    try {
      // Checks the Status of transaction associated with
      // the current thread.
      logMsg("Getting the status of transaction");

      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
        logMsg("Trying to set the transaction for" + " Rollback operation");
        // Trying Mark the transaction for rollback
        // only.
        userTransaction.setRollbackOnly();// should
        // throw IllegalStateException
        throw new Fault("IllegalStateException not" + " thrown as Expected");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (IllegalStateException illegalState) {
      logMsg("IllegalStateException was caught as" + " expected!!");
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("IllegalStateException not thrown as" + " Expected",
          system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("IllegalStateException not thrown as" + " Expected",
          exception);
    }

  }// End of testUserSetRollbackOnly001

  /**
   * @testName: testUserSetRollbackOnly002
   * @assertion_ids: JTA:JAVADOC:35
   * @test_Strategy: Start the UserTransaction.Call setRollbackOnly() on User
   *                 Transaction to mark the transaction for rollback only.Then
   *                 check the status of User Transaction.
   */

  public void testUserSetRollbackOnly002() throws Fault {
    // TestCase id :- 4.5.2

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");
        // Sets the transacion for Rollback operation.
        userTransaction.setRollbackOnly();
      }

      // Checks the Status of transaction associated with
      // the current thread.
      int status = userTransaction.getStatus();

      // If unknown, try again
      if (status == Status.STATUS_UNKNOWN) {
        int count = 0;
        do {
          logTrace("Received STATUS_UNKNOWN." + " Checking status again.");
          count++;
          try {
            Thread.sleep(SLEEPTIME);
          } catch (Exception e) {
            throw new Fault(e.getCause());
          }
          status = userTransaction.getStatus();
        } while ((status == Status.STATUS_UNKNOWN) && (count < 5));
      }
      if (status == Status.STATUS_MARKED_ROLLBACK) {
        logMsg("UserTransaction set for Rollback" + " operation");
        logMsg("UserTransaction Status is" + " STATUS_MARKED_ROLLBACK");
      }

      // other acceptable status conditions
      else if (status == Status.STATUS_ROLLING_BACK
          || status == Status.STATUS_ROLLEDBACK
          || status == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction marked for Rollback");
        logMsg("UserTransaction is rolling/rolled" + " back");
      }

      else {
        throw new Fault(
            "Failed to return the status" + " STATUS_MARKED_ROLLBACK");
      }
    } catch (IllegalStateException illegalState) {
      logErr("Exception " + illegalState.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          illegalState);
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          exception);
    }

  }// End of testUserSetRollbackOnly002

  /**
   * @testName: testUserSetRollbackOnly003
   * @assertion_ids: JTA:JAVADOC:36
   * @test_Strategy: Start the User Transaction.Call commit() on User
   *                 Transaction.Check the status of the User Transaction.Call
   *                 setRollbackOnly() on User Transaction.
   */

  public void testUserSetRollbackOnly003() throws Fault {
    // TestCase id :- 4.5.3

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");
        // Commits the transaction.
        userTransaction.commit();
      }

      // Checks the status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Committed");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
        logMsg("Trying to set the transaction for" + " Rollback operation");

        // Trying to set the transaciton for Rollback
        // only
        userTransaction.setRollbackOnly();// should
        // throw IllegalStateException
        throw new Fault("IllegalStateException not" + " thrown as Expected");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (IllegalStateException illegalState) {
      logMsg("IllegalStateException was caught as" + " expected!!");
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("IllegalStateException not thrown as" + " Expected",
          system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("IllegalStateException not thrown as" + " Expected",
          exception);
    }

  }// End of testUserSetRollbackOnly003

  /**
   * @testName: testUserSetRollbackOnly004
   * @assertion_ids: JTA:JAVADOC:36
   * @test_Strategy: Start the User Transaction.Call rollback() on User
   *                 Transaction.Check the status of the User Transaction.Call
   *                 setRollbackOnly() on User Transaction.
   */

  public void testUserSetRollbackOnly004() throws Fault {
    // TestCase id :- 4.5.4

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");

        // Rolls back the transaction
        userTransaction.rollback();
      }

      // Checks the status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Rolled back");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
        logMsg("Trying to set the transaction for" + "Rollback operation");

        // Trying to set the transaciton for Rollback
        userTransaction.setRollbackOnly();// should
        // throw IllegalStateException
        throw new Fault("IllegalStateException not" + " thrown as Expected");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (IllegalStateException illegalState) {
      logMsg("IllegalStateException was caught as" + " expected!!");
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("IllegalStateException not thrown as" + " Expected",
          system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("IllegalStateException not thrown as" + " Expected",
          exception);
    }

  }// End of testUserSetRollbackOnly004

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

}// End of UserSetRollbackOnlyClient
