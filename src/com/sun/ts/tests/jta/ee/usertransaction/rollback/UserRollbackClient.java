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
 * @(#)UserRollbackClient.java	1.19 03/05/16
 */

package com.sun.ts.tests.jta.ee.usertransaction.rollback;

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

/**
 * The UserRollbackClient class tests rollback() method of UserTransaction
 * interface using Sun's J2EE Reference Implementation.
 * 
 * @author P.Sandani Basha
 * @version 1.0.1, 09/17/99
 */

public class UserRollbackClient extends ServiceEETest implements Serializable {
  private static final int SLEEPTIME = 2000;

  private static final String testName = "jta.ee.usertransaction.rollback";

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
    UserRollbackClient userRollbackClientInst = new UserRollbackClient();
    com.sun.javatest.Status s = userRollbackClientInst.run(args, System.out,
        System.err);
    s.exit();

  }// End of main

  // Beginning of TestCases

  /**
   * @testName: testUserRollback001
   * @assertion_ids: JTA:JAVADOC:32
   * @test_Strategy: Without starting the User Transaction call rollback() on
   *                 User Transaction.
   */

  public void testUserRollback001() throws Fault {
    // TestCase id :- 4.4.1

    try {
      // Checks the Status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");

        logMsg("Rolling back the transaction");
        // Trying to Rollback the transaction.
        userTransaction.rollback(); // should throw
        // IllegalStateException.
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

  }// End of testUserRollback001

  /**
   * @testName: testUserRollback002
   * @assertion_ids: JTA:JAVADOC:31
   * @test_Strategy: Start the User Transaction.Call rollback() on User
   *                 Transaction.Check the status of the User Transaction
   */

  public void testUserRollback002() throws Fault {
    // TestCase id :- 4.4.2

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");
        // Rollbacks the UserTransaction.
        userTransaction.rollback();
      }

      // Checks the status of transaction associated with
      // the current thread.
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Rolledback");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (IllegalStateException illegal) {
      logErr("Exception " + illegal.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", illegal);
    } catch (SecurityException security) {
      logErr("Exception " + security.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", security);
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          exception);
    }

  }// End of testUserRollback002

  /**
   * @testName: testUserRollback003
   * @assertion_ids: JTA:JAVADOC:32
   * @test_Strategy: Start the User Transaction.Call commit() on User
   *                 Transaction.Call rollback() on User Transaction.
   */

  public void testUserRollback003() throws Fault {
    // TestCase id :- 4.4.3

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");
        // Commits the transaction.
        userTransaction.commit();
      }

      // Checks the Status of the transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Committed");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");

        logMsg("Trying to Rollback UserTransaction");
        // Tries to Rollback the UserTransaction.
        userTransaction.rollback(); // should throw
        // IllegalStateException
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

  }// End of testUserRollback003

  /**
   * @testName: testUserRollback004
   * @assertion_ids: JTA:JAVADOC:32
   * @test_Strategy: Start the User Transaction.Call rollback() on User
   *                 Transaction.Call rollback() again on User Transaction.
   */

  public void testUserRollback004() throws Fault {
    // TestCase id :- 4.4.4

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");
        // Rollbacks the transaction.
        userTransaction.rollback();
      }

      // Checks the Status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Rolled back");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");

        logMsg("Trying to Rollback UserTransaction" + " again");
        // Tries to Rollback the transaction.
        userTransaction.rollback(); // should throw
        // IllegalStateException.
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

  }// End of testUserRollback004

  /**
   * @testName: testUserRollback005
   * @assertion_ids: JTA:JAVADOC:31
   * @test_Strategy: Start the User Transaction.Call rollback() on User
   *                 Transaction.Check the status of the User Transaction.
   */

  public void testUserRollback005() throws Fault {
    // TestCase id :- 4.4.5

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");

        // Rollbacks the transaction.
        userTransaction.rollback();
      }

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

      // Checks the status of transaction associated with
      // the current thread
      if (status == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Rolledback");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (IllegalStateException illegalState) {
      logErr("Exception " + illegalState.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          illegalState);
    } catch (SecurityException security) {
      logErr("Exception " + security.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", security);
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          exception);
    }
  }// End of testUserRollback005

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

}// End of UserRollbackClient
