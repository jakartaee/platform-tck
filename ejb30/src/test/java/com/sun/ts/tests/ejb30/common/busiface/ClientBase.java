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

package com.sun.ts.tests.ejb30.common.busiface;

import java.rmi.RemoteException;
import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TLogger;

abstract public class ClientBase extends EETest {
  protected Properties props;

  // 2 remote beans directly accessed by client
  abstract protected BusinessIF1 getBean1();

  abstract protected BusinessIF2 getBean2();

  // 2 remote beans directly accessed by client
  // not to add new abstract methods since this class is extened by multiple
  // subclasses.
  protected AnnotatedBusinessInterface1 getAnnotatedInterfaceBean1() {
    return null;
  }

  protected AnnotatedBusinessInterface2 getAnnotatedInterfaceBean2() {
    return null;
  }

  // test bean colocated with other beans
  abstract protected TestIF getTestBean();

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  /**
   * Removes all beans used in this client. It should only be used by sfsb,
   * though other bean types may also have a remove business method.
   */
  protected void remove() {
    if (getBean1() != null) {
      try {
        getBean1().remove();
        TLogger.log("bean1 removed successfully.");
      } catch (final Exception e) {
        // ignore
        TLogger.log("failed to remove bean1.");
      }
    }

    if (getBean2() != null) {
      try {
        getBean2().remove2();
        TLogger.log("bean2 removed successfully.");
      } catch (final Exception e) {
        // ignore
        TLogger.log("failed to remove bean2.");
      }
    }
    if (getAnnotatedInterfaceBean1() != null) {
      try {
        getAnnotatedInterfaceBean1().remove();
        TLogger.log("annotatedInterfaceBean1 removed successfully.");
      } catch (final Exception e) {
        // ignore
        TLogger.log("annotatedInterfaceBean1 to remove bean1.");
      }
    }

    if (getAnnotatedInterfaceBean2() != null) {
      try {
        getAnnotatedInterfaceBean2().remove();
        TLogger.log("annotatedInterfaceBean2 removed successfully.");
      } catch (final Exception e) {
        // ignore
        TLogger.log("failed to remove annotatedInterfaceBean2.");
      }
    }
  }

  public void cleanup() throws Fault {
  }

  protected void verifyRemoteSemantics(String[] s) throws Fault {
    if (s == null) {
      throw new Fault("Unexpected: String[] value is null");
    }
    if (Constants.VALUE.equals(s[0])) {
      TLogger.log("Verified remote semantics");
    } else {
      throw new Fault(
          "Expected value: " + Constants.VALUE + ", but got " + s[0]);
    }
  }

  protected void verifyLocalSemantics(String[] s) throws Fault {
    if (s == null) {
      throw new Fault("Unexpected: String[] value is null");
    }
    if (Constants.VALUE_RESET.equals(s[0])) {
      TLogger.log("Verified local semantics");
    } else {
      throw new Fault(
          "Expected value: " + Constants.VALUE_RESET + ", but got " + s[0]);
    }
  }

  /*
   * testName: multipleInterfacesTest1
   * 
   * @test_Strategy:
   */
  public void multipleInterfacesTest1() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final boolean expected = true;
    boolean actual = false;
    try {
      actual = getBean1()._businessMethod1_(s);
    } catch (final RemoteException e) {
      throw new Fault(e);
    }
    if (expected != actual) {
      throw new Fault("Expected " + expected + ", actual " + actual);
    }
    verifyRemoteSemantics(s);
  }

  /*
   * testName: multipleInterfacesTest2
   * 
   * @test_Strategy:
   */
  public void multipleInterfacesTest2() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final boolean expected = true;
    final boolean actual = getBean2().businessMethod2(s);
    if (expected != actual) {
      throw new Fault("Expected " + expected + ", actual " + actual);
    }
    verifyRemoteSemantics(s);
  }

  /*
   * testName: multipleInterfacesLocalTest1
   * 
   * @test_Strategy:
   */
  public void multipleInterfacesLocalTest1() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean().multipleInterfacesLocalTest1(s);
    verifyLocalSemantics(result);
  }

  /*
   * testName: multipleInterfacesLocalTest2
   * 
   * @test_Strategy:
   */
  public void multipleInterfacesLocalTest2() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean().multipleInterfacesLocalTest2(s);
    verifyLocalSemantics(result);
  }

  /*
   * testName: singleInterfaceLocalSerializableTest
   * 
   * @test_Strategy:
   */
  public void singleInterfaceLocalSerializableTest() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean()
        .singleInterfaceLocalSerializableTest(s);
    verifyLocalSemantics(result);
  }

  /*
   * testName: singleInterfaceLocalExternalizableTest
   * 
   * @test_Strategy:
   */
  public void singleInterfaceLocalExternalizableTest() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean()
        .singleInterfaceLocalExternalizableTest(s);
    verifyLocalSemantics(result);
  }

  /*
   * testName: singleInterfaceLocalTimedObjectTest
   * 
   * @test_Strategy:
   */
  public void singleInterfaceLocalTimedObjectTest() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean()
        .singleInterfaceLocalTimedObjectTest(s);
    verifyLocalSemantics(result);
  }

  /*
   * testName: singleInterfaceLocalSessionSynchronizationTest
   * 
   * @test_Strategy:
   */
  public void singleInterfaceLocalSessionSynchronizationTest() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean()
        .singleInterfaceLocalSessionSynchronizationTest(s);
    verifyLocalSemantics(result);
  }

  /*
   * testName: singleInterfaceLocalSessionBeanTest
   * 
   * @test_Strategy:
   */
  public void singleInterfaceLocalSessionBeanTest() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean()
        .singleInterfaceLocalSessionBeanTest(s);
    verifyLocalSemantics(result);
  }

  /*
   * testName: multipleAnnotatedInterfacesTest1
   * 
   * @test_Strategy:
   */
  public void multipleAnnotatedInterfacesTest1() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final boolean expected = true;
    boolean actual = false;
    actual = getAnnotatedInterfaceBean1().annotatedBusinessInterfaceMethod(s);
    if (expected != actual) {
      throw new Fault("Expected " + expected + ", actual " + actual);
    }
    verifyRemoteSemantics(s);
  }

  /*
   * testName: multipleAnnotatedInterfacesTest2
   * 
   * @test_Strategy:
   */
  public void multipleAnnotatedInterfacesTest2() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final boolean expected = true;
    final boolean actual = getAnnotatedInterfaceBean2()
        .annotatedBusinessInterfaceMethod(s);
    if (expected != actual) {
      throw new Fault("Expected " + expected + ", actual " + actual);
    }
    verifyRemoteSemantics(s);
  }

  /*
   * testName: multipleAnnotatedInterfacesLocalTest1
   * 
   * @test_Strategy:
   */
  public void multipleAnnotatedInterfacesLocalTest1() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean()
        .multipleAnnotatedInterfacesLocalTest1(s);
    verifyLocalSemantics(result);
  }

  /*
   * testName: multipleAnnotatedInterfacesLocalTest2
   * 
   * @test_Strategy:
   */
  public void multipleAnnotatedInterfacesLocalTest2() throws Fault {
    final String[] s = new String[] { Constants.VALUE };
    final String[] result = getTestBean()
        .multipleAnnotatedInterfacesLocalTest2(s);
    verifyLocalSemantics(result);
  }

}
