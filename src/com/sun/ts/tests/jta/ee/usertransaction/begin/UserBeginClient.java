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

package com.sun.ts.tests.jta.ee.usertransaction.begin;

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
import javax.transaction.NotSupportedException;

/**
 * The UserBeginClient class tests begin() method of UserTransaction interface
 * using Sun's J2EE Reference Implementation.
 * 
 * @author P.Sandani Basha
 * @version 1.0.1, 09/17/99
 */

public class UserBeginClient extends ServiceEETest implements Serializable {
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
    UserBeginClient userBeginClientInst = new UserBeginClient();
    com.sun.javatest.Status s = userBeginClientInst.run(args, System.out,
        System.err);
    s.exit();

  }// End of main

  // Beginning of TestCases

  /**
   * @testName: testUserBegin001
   * @assertion_ids: JTA:JAVADOC:21
   * @test_Strategy: Check the status of UserTransaction before calling begin()
   *                 and check the status of UserTransaction after calling
   *                 begin()
   */

  public void testUserBegin001() throws Fault {
    // TestCase id :- 4.1.1

    try {
      // Gets the Status of the UserTransaction before
      // calling begin()
      int beforeBegin = userTransaction.getStatus();

      if (beforeBegin == Status.STATUS_NO_TRANSACTION) {
        logMsg(
            "The Status of the UserTransaction is" + " STATUS_NO_TRANSACTION");
      } else {
        throw new Fault(
            "Failed to return the status" + " STATUS_NO_TRANSACTION");
      }

      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      // Gets the Status of the UserTransaction after
      // calling begin()
      int afterBegin = userTransaction.getStatus();

      if (afterBegin == Status.STATUS_ACTIVE) {
        logMsg("The Status of the UserTransaction is" + " STATUS_ACTIVE");
      } else {
        throw new Fault("Failed to return the status" + " STATUS_ACTIVE");
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

  }// End of testUserBegin001

  /**
   * @testName: testUserBegin002
   * @assertion_ids: JTA:JAVADOC:22
   * @test_Strategy: Start the User Transaction and again call the begin()
   *                 method on User Transaction
   */

  public void testUserBegin002() throws Fault {
    // TestCase id :- 4.1.2

    try {
      // Starts a Global Transaction & associates with
      // Current Thread.
      userTransaction.begin();
      logMsg("UserTransaction Started");

      logMsg("Trying to start UserTransaction again");
      // Begins the UserTransaction again
      userTransaction.begin();// should throw
      // NotSupportedException

      throw new Fault("NotSupportedException was not thrown" + " as Expected");
    } catch (NotSupportedException notSupported) {
      logMsg("Release Doesn't Support NESTED TRANSACTIONS");
      logMsg("NotSupportedException was caught as" + " Expected !!");
    } catch (SystemException system) {
      logErr("Exception " + system.toString() + " was caught");
      throw new Fault("NotSupportedException was not thrown" + " as Expected",
          system);
    } catch (Exception exception) {
      logErr("Exception " + exception.toString() + " was caught");
      throw new Fault("NotSupportedException was not thrown" + " as Expected",
          exception);
    }

  }// End of testtestUserBegin002

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

}// End of UserBeginClient
