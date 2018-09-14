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
package com.sun.ts.tests.ejb30.misc.threebeans;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public class Client extends EETest {

  @EJB
  private static OneRemoteIF oneRemote;

  @EJB
  private static TwoRemoteIF twoRemote;

  @EJB(beanName = "ThreeBean")
  private static ThreeRemoteIF threeRemote;

  @EJB(beanName = "FourBean")
  private static ThreeRemoteIF fourRemote;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
  }

  public void cleanup() throws Fault {

  }

  /*
   * @testName: testOne
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void testOne() throws Fault {
    final String expected = "OneBean";
    String beanName = oneRemote.getShortName();
    if (expected.equals(beanName)) {
      TLogger.log("Got expected beanName: " + expected);
    } else {
      throw new Fault("Expecting " + expected + ", but got " + beanName);
    }
  }

  /*
   * @testName: testException
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void testException() throws TestFailedException {
    final String expected = "testException";
    String result = oneRemote.testException();
    if (expected.equals(result)) {
      TLogger.log("Got expected result: " + expected);
    } else {
      throw new TestFailedException(
          "Expecting " + expected + ", but got " + result);
    }
    result = twoRemote.testException();
    if (expected.equals(result)) {
      TLogger.log("Got expected result: " + expected);
    } else {
      throw new TestFailedException(
          "Expecting " + expected + ", but got " + result);
    }
  }

  /*
   * @testName: testNumber
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void testNumber() throws Fault {
    String expected = int.class.getName();
    String result = oneRemote.testNumber(2);
    if (expected.equals(result)) {
      TLogger.log(
          "Got expected return value from method testNumber(int):" + expected);
    } else {
      throw new Fault("Expecting " + expected + ", but got " + result);
    }
    expected = Integer.class.getName();
    result = oneRemote.testNumber(new Integer(2));
    if (expected.equals(result)) {
      TLogger.log("Got expected return value from method testNumber(Integer):"
          + expected);
    } else {
      throw new Fault("Expecting " + expected + ", but got " + result);
    }
    expected = double.class.getName();
    result = oneRemote.testNumber(2D);
    if (expected.equals(result)) {
      TLogger.log("Got expected return value from method testNumber(double):"
          + expected);
    } else {
      throw new Fault("Expecting " + expected + ", but got " + result);
    }
    try {
      result = oneRemote.testNumber(new Double(2D));
      throw new Fault(
          "Expecting EJBException, but got return value: " + result);
    } catch (EJBException e) {
      TLogger.log("Got expected EJBException", e.toString());
    }
  }

  /*
   * @testName: testTwo
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void testTwo() throws Fault {
    final String expected = "TwoBean";
    String beanName = twoRemote.getShortName();
    if (expected.equals(beanName)) {
      TLogger.log("Got expected beanName: " + expected);
    } else {
      throw new Fault("Expecting " + expected + ", but got " + beanName);
    }
  }

  /*
   * @testName: testThree
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void testThree() throws Fault {
    final String expected = "ThreeBean";
    String beanName = threeRemote.getShortName();
    if (expected.equals(beanName)) {
      TLogger.log("Got expected beanName: " + expected);
    } else {
      throw new Fault("Expecting " + expected + ", but got " + beanName);
    }
  }

  /*
   * @testName: testFour
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: FourBean and ThreeBean implement the same interfaces, and
   * inject each other.
   */
  public void testFour() throws Fault {
    final String expected = "FourBean";
    String beanName = fourRemote.getShortName();
    if (expected.equals(beanName)) {
      TLogger.log("Got expected beanName: " + expected);
    } else {
      throw new Fault("Expecting " + expected + ", but got " + beanName);
    }
  }
}
