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

package com.sun.ts.tests.ejb30.bb.localaccess.common;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.ejb.EJB;

public abstract class ClientBase extends EETest implements Constants {
  protected Properties props;

  // test bean
  protected String testBeanRefName;

  abstract protected TestBeanIF getBean();

  // local stateless localStateless
  protected String localStatelessRefName = "ejb/localStatelessRefName";

  protected Class localStatelessClass = LocalIF.class;

  // default local stateless defaultLocalStateless
  protected String defaultLocalStatelessRefName = "ejb/defaultLocalStatelessRefName";

  protected Class defaultLocalStatelessClass = DefaultLocalIF.class;

  // local stateful localStateful
  protected String localStatefulRefName = "ejb/localStatefulRefName";

  protected Class localStatefulClass = StatefulLocalIF.class;

  // default local stateful defaultLocalStateful
  protected String defaultLocalStatefulRefName = "ejb/defaultLocalStatefulRefName";

  protected Class defaultLocalStatefulClass = StatefulDefaultLocalIF.class;

  // local stateless 2 localStateless2
  protected String localStateless2RefName = "ejb/localStateless2RefName";

  protected Class localStateless2Class = LocalIF.class;

  public abstract void cleanup() throws Fault;

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    // subclass should perform lookup
  }

  /*
   * nnName: passByValueTest
   * 
   * @test_Strategy: the (remote) test bean modifies the value of String array.
   * This change should not affect the value in Client.
   */
  public void passByValueTest() throws Fault {
    String expected = CLIENT_MSG;
    String actual = null;
    String[] args = new String[] { CLIENT_MSG };
    try {
      getBean().passByValueTest(args);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
    actual = args[0];
    if (expected.equals(actual)) {
      TLogger.log("Good, the value of the String[] was not modified.");
    } else {
      throw new Fault("Expected '" + expected + "', actual '" + actual + "'");
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // 1 localStateless
  /////////////////////////////////////////////////////////////////////////
  /*
   * nnName: passByReferenceTest1
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest1() throws Fault {
    try {
      getBean().passByReferenceTest(new String[] { CLIENT_MSG },
          localStatelessRefName, localStatelessClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: exceptionTest1
   * 
   * @test_Strategy:
   */
  public void exceptionTest1() throws Fault {
    try {
      getBean().exceptionTest(localStatelessRefName, localStatelessClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: runtimeExceptionTest1
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest1() throws Fault {
    try {
      getBean().runtimeExceptionTest(localStatelessRefName,
          localStatelessClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // 2 defaultdefaultLocalStateless
  /////////////////////////////////////////////////////////////////////////
  /*
   * nnName: passByReferenceTest2
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest2() throws Fault {
    try {
      getBean().passByReferenceTest(new String[] { CLIENT_MSG },
          defaultLocalStatelessRefName, defaultLocalStatelessClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: exceptionTest2
   * 
   * @test_Strategy:
   */
  public void exceptionTest2() throws Fault {
    try {
      getBean().exceptionTest(defaultLocalStatelessRefName,
          defaultLocalStatelessClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: runtimeExceptionTest2
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest2() throws Fault {
    try {
      getBean().runtimeExceptionTest(defaultLocalStatelessRefName,
          defaultLocalStatelessClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // 3 localStateful
  /////////////////////////////////////////////////////////////////////////
  /*
   * nnName: passByReferenceTest3
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest3() throws Fault {
    try {
      getBean().passByReferenceTest(new String[] { CLIENT_MSG },
          localStatefulRefName, localStatefulClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: exceptionTest3
   * 
   * @test_Strategy:
   */
  public void exceptionTest3() throws Fault {
    try {
      getBean().exceptionTest(localStatefulRefName, localStatefulClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: runtimeExceptionTest3
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest3() throws Fault {
    try {
      getBean().runtimeExceptionTest(localStatefulRefName, localStatefulClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // 4 defaultdefaultLocalStateful
  /////////////////////////////////////////////////////////////////////////
  /*
   * nnName: passByReferenceTest4
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest4() throws Fault {
    try {
      getBean().passByReferenceTest(new String[] { CLIENT_MSG },
          defaultLocalStatefulRefName, defaultLocalStatefulClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: exceptionTest4
   * 
   * @test_Strategy:
   */
  public void exceptionTest4() throws Fault {
    try {
      getBean().exceptionTest(defaultLocalStatefulRefName,
          defaultLocalStatefulClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: runtimeExceptionTest4
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest4() throws Fault {
    try {
      getBean().runtimeExceptionTest(defaultLocalStatefulRefName,
          defaultLocalStatefulClass);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  //////////////////////////////////////////////////////////////////////
  // localStateless2
  //////////////////////////////////////////////////////////////////////
  /*
   * nnName: passByReferenceTest5
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest5() throws Fault {
    try {
      getBean().passByReferenceTest(new String[] { CLIENT_MSG },
          localStateless2RefName, localStateless2Class);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: exceptionTest5
   * 
   * @test_Strategy:
   */
  public void exceptionTest5() throws Fault {
    try {
      getBean().exceptionTest(localStateless2RefName, localStateless2Class);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * nnName: runtimeExceptionTest5
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest5() throws Fault {
    try {
      getBean().runtimeExceptionTest(localStateless2RefName,
          localStateless2Class);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }
}
