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

package com.sun.ts.tests.concurrency.api.AbortedException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;

import javax.enterprise.concurrent.AbortedException;
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
   * @testName: AbortedExceptionNoArgTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:1
   * 
   * @test_Strategy: Constructs an AbortedException.
   */
  public void AbortedExceptionNoArgTest() throws Fault {
    boolean pass = false;
    try {
      throw new AbortedException();
    } catch (AbortedException ae) {
      TestUtil.logTrace("AbortedException Caught as Expected");
      if (ae.getMessage() == null) {
        TestUtil.logTrace("Received expected null message");
        pass = true;
      } else {
        TestUtil.logErr(
            "AbortedException should have had null message, actual message="
                + ae.getMessage());
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("AbortedExceptionNoArgTest failed");
  }

  /*
   * @testName: AbortedExceptionStringTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:3
   * 
   * @test_Strategy: Constructs an AbortedException.
   */
  public void AbortedExceptionStringTest() throws Fault {
    boolean pass = false;
    String expected = "thisisthedetailmessage";
    try {
      throw new AbortedException(expected);
    } catch (AbortedException ae) {
      TestUtil.logTrace("AbortedException Caught as Expected");
      if (ae.getMessage().equals(expected)) {
        TestUtil.logTrace("Received expected message");
        pass = true;
      } else {
        TestUtil.logErr(
            "Expected:" + expected + ", actual message=" + ae.getMessage());
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("AbortedExceptionStringTest failed");
  }

  /*
   * @testName: AbortedExceptionThrowableTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:4
   * 
   * @test_Strategy: Constructs an AbortedException.
   */
  public void AbortedExceptionThrowableTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    Throwable expected = new Throwable("thisisthethrowable");
    try {
      throw new AbortedException(expected);
    } catch (AbortedException ae) {
      TestUtil.logTrace("AbortedException Caught as Expected");
      Throwable cause = ae.getCause();
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
      throw new AbortedException(expected);
    } catch (AbortedException ae) {
      TestUtil.logTrace("AbortedException Caught as Expected");
      Throwable cause = ae.getCause();
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
      throw new Fault("AbortedExceptionThrowableTest failed");
  }

  /*
   * @testName: AbortedExceptionStringThrowableTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:2
   * 
   * @test_Strategy: Constructs an AbortedException.
   */
  public void AbortedExceptionStringThrowableTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    String sExpected = "thisisthedetailmessage";
    Throwable tExpected = new Throwable("thisisthethrowable");
    try {
      throw new AbortedException(sExpected, tExpected);
    } catch (AbortedException ae) {
      TestUtil.logTrace("AbortedException Caught as Expected");
      if (ae.getMessage().equals(sExpected)) {
        TestUtil.logTrace("Received expected message");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected:" + sExpected + ", actual message=" + ae.getMessage());
      }
      Throwable cause = ae.getCause();
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
      throw new AbortedException(sExpected, tExpected);
    } catch (AbortedException ae) {
      TestUtil.logTrace("AbortedException Caught as Expected");
      if (ae.getMessage().equals(sExpected)) {
        TestUtil.logTrace("Received expected message");
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Expected:" + sExpected + ", actual message=" + ae.getMessage());
      }
      Throwable cause = ae.getCause();
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
      throw new Fault("AbortedExceptionStringThrowableTest failed");
  }
}
