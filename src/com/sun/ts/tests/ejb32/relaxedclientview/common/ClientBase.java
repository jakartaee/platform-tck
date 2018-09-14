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

package com.sun.ts.tests.ejb32.relaxedclientview.common;

import java.util.*;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb32.relaxedclientview.common.HelperSingleton;
import com.sun.javatest.Status;

public class ClientBase extends EETest implements TestConstants {

  @EJB(beanName = "HelperSingletonBean", name = "helperSingleton")
  static HelperSingleton helperSingleton;

  @EJB(beanName = "RemoteAnnotationBean", name = "remoteAnnotationNormalInterface1")
  static NormalInterface1 remoteAnnotationNormalInterface1;

  @EJB(beanName = "RemoteAnnotationBean", name = "remoteAnnotationNormalInterface2")
  static NormalInterface2 remoteAnnotationNormalInterface2;

  @EJB(beanName = "OneRemoteAnnotationOnInterfaceBean", name = "oneRemoteAnnotationOnInterface1")
  static RemoteAnnotationInterface1 oneRemoteAnnotationOnInterface1;

  @EJB(beanName = "OneRemoteAnnotationOnEjbBean", name = "oneRemoteAnnotationOnEjb1")
  static NormalInterface1 oneRemoteAnnotationOnEjb1;

  public static void main(String[] args) {
    ClientBase theTests = new ClientBase();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void cleanup() throws Fault {
  }

  public void setup(String[] args, Properties p) throws Fault {
  }

  /*
   * testName: noAnnotationTest
   * 
   * @test_Strategy: 2 interfaces without any annotations
   */
  public void noAnnotationTest() throws TestFailedException {
    TLogger.log(helperSingleton.noAnnotationTest());
  }

  /*
   * testName: localAnnotationTest
   * 
   * @test_Strategy: 2 interfaces with local annotation on ejb
   */
  public void localAnnotationTest() throws TestFailedException {
    TLogger.log(helperSingleton.localAnnotationTest());
  }

  /*
   * testName: remoteAnnotationTest
   * 
   * @test_Strategy: 2 interfaces with remote annotation on ejb
   */
  public void remoteAnnotationTest() throws TestFailedException {

    try {
      int result = remoteAnnotationNormalInterface1.businessMethod1()
          + remoteAnnotationNormalInterface2.businessMethod2();

      if (result == EXPECTED_RESULT1) {
        TLogger.log(
            String.format(SUCCESSFULLY_RUN_RESULT, "remoteAnnotationTest"));
      } else {
        throw new TestFailedException(
            String.format(GOT_WRONG_RESULT, EXPECTED_RESULT1, result));
      }

    } catch (Exception e) {
      throw new TestFailedException(e);
    }

  }

  /*
   * testName: oneRemoteAnnotationOnInterfaceTest
   * 
   * @test_Strategy: a remote annotation on one interface and another interface
   * has nothing
   */
  public void oneRemoteAnnotationOnInterfaceTest() throws TestFailedException {
    int result = oneRemoteAnnotationOnInterface1.businessMethod1();

    if (result != EXPECTED_RESULT2) {
      throw new TestFailedException(
          String.format(GOT_WRONG_RESULT, EXPECTED_RESULT2, result));
    }

    TLogger.log(helperSingleton.oneRemoteAnnotationOnInterfaceTest());
  }

  /*
   * testName: oneRemoteAnnotationOnEjbTest
   * 
   * @test_Strategy: a remote annotation on ejb for one interface and another
   * interface has nothing
   */
  public void oneRemoteAnnotationOnEjbTest() throws TestFailedException {
    int result = oneRemoteAnnotationOnEjb1.businessMethod1();

    if (result != EXPECTED_RESULT2) {
      throw new TestFailedException(
          String.format(GOT_WRONG_RESULT, EXPECTED_RESULT2, result));
    }

    TLogger.log(helperSingleton.oneRemoteAnnotationOnEjbTest());
  }

  /*
   * testName: noInterfaceViewTest
   * 
   * @test_Strategy: an ejb has no interface view, so all business interfaces
   * must be explicitly designated
   */
  public void noInterfaceViewTest() throws TestFailedException {
    TLogger.log(helperSingleton.noInterfaceViewTest());
  }

  /*
   * testName: localDDTest
   * 
   * @test_Strategy: an ejb has dd local view
   */
  public void localDDTest() throws TestFailedException {
    TLogger.log(helperSingleton.localDDTest());
  }
}
