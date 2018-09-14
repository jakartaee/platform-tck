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

package com.sun.ts.tests.jdbc.ee.common;

import java.io.*;
import java.util.*;
import java.net.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.sql.*;
import javax.sql.*;

/**
 * JDBCTestMsg class is used to logging messages from the tests
 * 
 * @author Sudipto Ghosh
 * @version 1.0, 15/03/2002
 *
 */

public class JDBCTestMsg extends ServiceEETest {
  Collection msgList = new ArrayList();

  Collection outputMsgList = new ArrayList();

  boolean trace = Boolean.valueOf(TestUtil.getProperty("harness.log.traceflag"))
      .booleanValue();

  /**
   * Adds the messages to be printed to a List. If trace is true logs the
   * message directly.
   *
   * @param input
   *          a string to be logged out after the tests execution
   */

  public void setMsg(String input) {
    if (trace == true) {
      TestUtil.logMsg(input);
    } else {
      msgList.add(input);
    }
  }

  /**
   * This method is called from the error handling methods and also from the
   * tests to log all the messages contained in the List.
   * 
   */

  public void printTestMsg() {
    if (trace != true) {
      TestUtil.logMsg("***************************************");

      Iterator it = msgList.iterator();
      while (it.hasNext()) {
        TestUtil.logMsg(" " + it.next());
      }

      TestUtil.logMsg("***************************************");
    }

  }

  /**
   * Adds the expected and obtained output string to a List. If trace is true
   * logs the message directly.
   * 
   * @param expected
   *          the expected output string passed from the tests
   * @param obtained
   *          the obtained output string passed from the tests
   */
  public void addOutputMsg(String expected, String obtained) {
    if (trace == true) {
      TestUtil.logMsg("Expected Output..... " + expected
          + "  Obtained Output..... " + obtained);
    } else {
      outputMsgList.add(expected);
      outputMsgList.add(obtained);
    }
  }

  /**
   * This method is called from the error handling methods to print the expected
   * and obtained output if the tests fails.
   * 
   */

  public void printOutputMsg() {
    Iterator it = outputMsgList.iterator();
    while (it.hasNext()) {
      TestUtil.logMsg("Expected Output..... " + it.next()
          + "  Obtained Output..... " + it.next());
    }
    TestUtil.logMsg("***************************************");
  }

  /**
   * This method handles SQLException that is thrown by the tests in case of
   * failure due to database access error.
   *
   * @param sqle
   *          SQLException thrown from the tests
   * @param error
   *          error messages
   */
  public void printSQLError(SQLException sqle, String error) throws Fault {
    if (trace != true) {
      printTestMsg();
      printOutputMsg();
    }
    throw new Fault(error, sqle);

  }

  /**
   * This method handles Exception condition occurring due to some failures in
   * the tests execution.
   * 
   * @param e
   *          Exception thrown from the tests
   * @param error
   *          error message
   */
  public void printError(Exception e, String error) throws Fault {
    if (trace != true) {
      printTestMsg();
      printOutputMsg();
    }
    throw new Fault(error, e);
  }

  /**
   * This method logs any general message and error if the tests does not
   * passes.
   * 
   * @param message
   *          String message passed from the tests when it fails
   * @param error
   *          error message
   */

  public void printTestError(String message, String error) throws Fault {
    TestUtil.logErr(message);
    throw new Fault(error);

  }

}
