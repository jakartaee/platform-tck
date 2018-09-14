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
 * @(#)UserGetStatusClient.java	1.19 03/05/16
 */

package com.sun.ts.tests.jta.ee.usertransaction.getstatus;

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
 * The UserGetStatusClient class tests getStatus() method of UserTransaction
 * interface using Sun's J2EE Reference Implementation.
 * 
 * @author P.Sandani Basha
 * @version 1.0.1, 09/17/99
 */

public class UserGetStatusClient extends ServiceEETest implements Serializable {
  private static final int SLEEPTIME = 2000;

  private static final String testName = "jta.ee.usertransaction.begin";

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
    UserGetStatusClient userGetStatusClientInst = new UserGetStatusClient();
    com.sun.javatest.Status s = userGetStatusClientInst.run(args, System.out,
        System.err);
    s.exit();

  }// End of main

  // Beginning of TestCases

  /**
   * @testName: testUserGetStatus001
   * @assertion_ids: JTA:JAVADOC:38
   * @test_Strategy: Without starting the User Transaction check the status of
   *                 User Transaction.
   */

  public void testUserGetStatus001() throws Fault {
    // TestCase id :- 4.3.1

    try {
      // Checks the Status of the transaction associated with
      // the current thread before calling begin.

      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
      } else {
        throw new Fault(
            "Failed to return the status" + "STATUS_NO_TRANSACTION");
      }
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          exception);
    }

  }// End of testUserGetStatus001

  /**
   * @testName: testUserGetStatus002
   * @assertion_ids: JTA:JAVADOC:38
   * @test_Strategy: Start the User Transaction and then check the status of
   *                 UserTransaction.
   */

  public void testUserGetStatus002() throws Fault {
    // TestCase id :- 4.3.2

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");
      } else {
        throw new Fault("Failed to return the status" + " STATUS_ACTIVE");
      }
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          exception);
    }

  }// End of testUserGetStatus002

  /**
   * @testName: testUserGetStatus003
   * @assertion_ids: JTA:JAVADOC:38
   * @test_Strategy: Start the UserTransaction.Call commit() on User
   *                 Transaction.Check the status of the User Transaction.
   */

  public void testUserGetStatus003() throws Fault {
    // TestCase id :- 4.3.3

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");

        // Commits the UserTransaction.
        userTransaction.commit();
      }

      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Committed");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
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
  }// End of testUserGetStatus003

  /**
   * @testName: testUserGetStatus004
   * @assertion_ids: JTA:JAVADOC:38
   * @test_Strategy: Start the UserTransaction.Call rollback() on User
   *                 Transaction.Check the status of the User Transaction.
   */

  public void testUserGetStatus004() throws Fault {
    // TestCase id :- 4.3.4

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the Status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");

        // Rollbacks the UserTransaction.
        userTransaction.rollback();
      }

      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Rolled back");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
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

  }// End of testUserGetStatus004

  /**
   * @testName: testUserGetStatus005
   * @assertion_ids: JTA:JAVADOC:38
   * @test_Strategy: Start the User Transaction.Call setRollbackOnly() on User
   *                 Transaction to mark the transaction for rollback only.Check
   *                 the status of the User Transaction.
   */

  public void testUserGetStatus005() throws Fault {
    // TestCase id :- 4.3.5

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Checks the status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
        logMsg("UserTransaction Status is" + " STATUS_ACTIVE");
        userTransaction.setRollbackOnly();
      }

      // Checks the status of transaction associated with
      // the current thread
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
        logMsg("UserTransaction marked for Rollback");
        logMsg("UserTransaction Status is" + " STATUS_MARKED_ROLLBACK");
      }

      // other acceptable status conditions
      else if (status == Status.STATUS_ROLLING_BACK
          || status == Status.STATUS_ROLLEDBACK
          || status == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction marked for Rollback");
        logMsg("UserTransaction is rolling/rolled" + " back");
      } else {
        throw new Fault("Failed to return valid" + " status");
      }
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          exception);
    }

  }// End of testUserGetStatus005

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

}// End of UserGetStatusTest
