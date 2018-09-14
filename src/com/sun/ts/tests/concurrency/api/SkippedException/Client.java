/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.api.SkippedException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;

import javax.enterprise.concurrent.SkippedException;
import java.lang.String;
import java.lang.Throwable;
import java.util.Properties;

public class Client extends ServiceEETest implements java.io.Serializable {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      // do your setup if any here
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: SkippedExceptionNoArgTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:42
   * 
   * @test_Strategy: Constructs an SkippedException.
   */
  public void SkippedExceptionNoArgTest() throws Fault {
    boolean pass = false;
    try {
      throw new SkippedException();
    } catch (SkippedException se) {
      TestUtil.logTrace("SkippedException Caught as Expected");
      if (se.getMessage() == null) {
        TestUtil.logTrace("Received expected null message");
        pass = true;
      } else {
        TestUtil.logErr(
            "SkippedException should have had null message, actual message="
                + se.getMessage());
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("SkippedExceptionNoArgTest failed");
  }

  /*
   * @testName: SkippedExceptionStringTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:43
   * 
   * @test_Strategy: Constructs an SkippedException.
   */
  public void SkippedExceptionStringTest() throws Fault {
    boolean pass = false;
    String expected = "thisisthedetailmessage";
    try {
      throw new SkippedException(expected);
    } catch (SkippedException se) {
      TestUtil.logTrace("SkippedException Caught as Expected");
      if (se.getMessage().equals(expected)) {
        TestUtil.logTrace("Received expected message");
        pass = true;
      } else {
        TestUtil.logErr(
            "Expected:" + expected + ", actual message=" + se.getMessage());
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("SkippedExceptionStringTest failed");
  }

  /*
   * @testName: SkippedExceptionThrowableTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:45
   * 
   * @test_Strategy: Constructs an SkippedException.
   */
  public void SkippedExceptionThrowableTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    Throwable expected = new Throwable("thisisthethrowable");
    try {
      throw new SkippedException(expected);
    } catch (SkippedException se) {
      TestUtil.logTrace("SkippedException Caught as Expected");
      Throwable cause = se.getCause();
      if (cause.equals(expected)) {
        TestUtil.logTrace("Received expected cause");
        pass1 = true;
      } else {
        TestUtil.logErr("Expected:" + expected + ", actual message=" + cause);
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    expected = null;
    try {
      throw new SkippedException(expected);
    } catch (SkippedException se) {
      TestUtil.logTrace("SkippedException Caught as Expected");
      Throwable cause = se.getCause();
      if (cause == null) {
        TestUtil.logTrace("Received expected null cause");
        pass2 = true;
      } else {
        TestUtil.logErr("Expected:null, actual message=" + cause);
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass1 || !pass2)
      throw new Fault("SkippedExceptionThrowableTest failed");
  }

  /*
   * @testName: SkippedExceptionStringThrowableTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:44
   * 
   * @test_Strategy: Constructs an SkippedException.
   */
  public void SkippedExceptionStringThrowableTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    String sExpected = "thisisthedetailmessage";
    Throwable tExpected = new Throwable("thisisthethrowable");
    try {
      throw new SkippedException(sExpected, tExpected);
    } catch (SkippedException se) {
      TestUtil.logTrace("SkippedException Caught as Expected");
      if (se.getMessage().equals(sExpected)) {
        TestUtil.logTrace("Received expected message");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected:" + sExpected + ", actual message=" + se.getMessage());
      }
      Throwable cause = se.getCause();
      if (cause.equals(tExpected)) {
        TestUtil.logTrace("Received expected cause");
        pass2 = true;
      } else {
        TestUtil.logErr("Expected:" + tExpected + ", actual message=" + cause);
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }

    tExpected = null;
    try {
      throw new SkippedException(sExpected, tExpected);
    } catch (SkippedException se) {
      TestUtil.logTrace("SkippedException Caught as Expected");
      if (se.getMessage().equals(sExpected)) {
        TestUtil.logTrace("Received expected message");
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Expected:" + sExpected + ", actual message=" + se.getMessage());
      }
      Throwable cause = se.getCause();
      if (cause == null) {
        TestUtil.logTrace("Received expected null cause");
        pass4 = true;
      } else {
        TestUtil.logErr("Expected:null, actual message=" + cause);
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4)
      throw new Fault("SkippedExceptionStringThrowableTest failed");
  }
}
