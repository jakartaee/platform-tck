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
 * $Id$
 */

package com.sun.ts.tests.jta.ee.usertransaction.commit;

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
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.transaction.SystemException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;

/**
 * The UserCommitClient class tests commit() method of UserTransaction interface
 * using Sun's J2EE Reference Implementation.
 * 
 * @author P.Sandani Basha.
 * @version 1.0.1, 09/17/99
 */

public class UserCommitClient extends ServiceEETest implements Serializable {
  private static final int SLEEPTIME = 2000;

  private static final String testName = "jta.ee.usertransaction.commit";

  private UserTransaction userTransaction = null;

  public void setup(String[] args, Properties p) throws Fault {
    try {
      // Initializes the Environment
      Transact.init();
      logTrace("Test environment initialized");

      // Gets the User Transaction
      userTransaction = (UserTransaction) Transact.nctx
          .lookup("java:comp/UserTransaction");
      logMsg("User Transaction is Obtained");

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
    UserCommitClient userCommitClientInst = new UserCommitClient();
    com.sun.javatest.Status s = userCommitClientInst.run(args, System.out,
        System.err);
    s.exit();

  }// End of main

  // Beginning of testCases

  /**
   * @testName: testUserCommit001
   * @assertion_ids: JTA:JAVADOC:29
   * @test_Strategy: Without starting the User Transaction Call commit() on User
   *                 Transaction.
   */

  public void testUserCommit001() throws Fault {
    // TestCase id :- 4.2.1

    try {
      // Checks the Status of the transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
        logMsg("Trying to Commit on inactive" + " UserTransaction");

        // Commits the UserTransaction.
        userTransaction.commit(); // should
        // throw IllegalStateException
        throw new Fault(
            "IllegalStateException was not" + " thrown as Expected");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (IllegalStateException illegalState) {
      logMsg("IllegalStateException was caught as" + " Expected !!");
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("IllegalStateException was not" + " thrown as Expected",
          system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("IllegalStateException was not" + " thrown as Expected",
          exception);
    }

  }// End of testUserCommit001

  /**
   * @testName: testUserCommit002
   * @assertion_ids: JTA:JAVADOC:24
   * @test_Strategy: Start the User Transaction.call commit() on User
   *                 Transaction.Check the status. .
   */

  public void testUserCommit002() throws Fault {
    // TestCase id :- 4.2.2

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

      // Checks the status of the transaction associated with
      // the current thread after commit
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Committed");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (RollbackException rollback) {
      logErr("Exception " + rollback.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", rollback);
    } catch (HeuristicMixedException heuristicMixed) {
      logErr("Exception " + heuristicMixed.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          heuristicMixed);
    } catch (HeuristicRollbackException heuristicRollback) {
      logErr("Exception " + heuristicRollback.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed",
          heuristicRollback);
    } catch (SecurityException security) {
      logErr("Exception " + security.toString() + " was caught");
      throw new Fault("UnExpected Exception was caught:" + " Failed", security);
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

  }// End of testUserCommit002

  /**
   * @testName: testUserCommit003
   * @assertion_ids: JTA:JAVADOC:29
   * @test_Strategy: Start the User Transaction.Call commit() on User
   *                 Transaction.Check status of the User Transaction. Call
   *                 commit() again on User Transaction.
   */

  public void testUserCommit003() throws Fault {
    // TestCase id :- 4.2.3

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

      // Check for the status of the transaction
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction Committed");
        logMsg("UserTransaction Status is" + " STATUS_NO_TRANSACTION");
        logMsg("Trying to Commit on UserTransaction" + " again");

        // Trying to Commit on UserTransaction.
        userTransaction.commit();// should
        // throw IllegalStateException
        throw new Fault(
            "IllegalStateException was not" + " thrown as Expected");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (IllegalStateException illegalState) {
      logMsg("IllegalStateException was caught as" + " Expected!!");
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("IllegalStateException was not" + " thrown as Expected",
          system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("IllegalStateException was not" + " thrown as Expected",
          exception);
    }

  }// End of testUserCommit003

  /**
   * @testName: testUserCommit004
   * @assertion_ids: JTA:JAVADOC:29
   * @test_Strategy: Start the User Transaction.Call rollback() on User
   *                 Transaction.Check status of the User Transaction.Call
   *                 commit() again on User Transaction.
   */

  public void testUserCommit004() throws Fault {
    // TestCase id :- 4.2.4

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

      // Checks the Status of transaction associated with
      // the current thread
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction rolled back");
        logMsg("Trying to Commit on UserTransaction");
        // Trying to Commit on UserTransaction.
        userTransaction.commit();// should throw
        // IllegalStateException
        throw new Fault(
            "IllegalStateException was not" + " thrown as Expected");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }
    } catch (IllegalStateException illegalState) {
      logMsg("IllegalStateException was caught as" + " Expected!!");
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("IllegalStateException was not" + " thrown as Expected",
          system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("IllegalStateException was not" + " thrown as Expected",
          exception);
    }

  }// End of testUserCommit004

  /**
   * @testName: testUserCommit005
   * @assertion_ids: JTA:JAVADOC:25
   * @test_Strategy: Start the User Transaction.Mark the User Transaction for
   *                 rollback only by calling setRollbackOnly().Check the
   *                 status.Call commit() on User Transaction.
   */

  public void testUserCommit005() throws Fault {
    // TestCase id :- 4.2.5

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

      // Checks the Status of transaction associated with
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
      if (status == Status.STATUS_MARKED_ROLLBACK
          || status == Status.STATUS_ROLLING_BACK
          || status == Status.STATUS_ROLLEDBACK
          || status == Status.STATUS_NO_TRANSACTION) {
        logMsg("UserTransaction set for Rollback");
        logMsg("Trying to Commit on UserTransaction");
        // Tries to Commit on UserTransaction.
        userTransaction.commit(); // should throw
        // RollbackException
        // or IllegalStateException
        throw new Fault("RollbackException was not" + " thrown as Expected");
      } else {
        throw new Fault("Failed to return a valid" + " status");
      }
    } catch (RollbackException rollback) {
      logMsg("RollbackException was caught as Expected!!");
    } catch (IllegalStateException state) // if rolled back
    {
      logMsg("IllegalStateException was caught. Exception" + " is acceptable.");
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("RollbackException was not" + " thrown as Expected",
          system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("RollbackException was not" + " thrown as Expected",
          exception);
    }

  }// End of testUserCommit005

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

}// End of UserCommitClient
