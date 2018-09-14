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

package com.sun.ts.tests.ejb30.common.sessioncontext;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.CreateException;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import java.rmi.RemoteException;
import java.util.Properties;
import static com.sun.ts.tests.ejb30.common.sessioncontext.Constants.FROM_BEAN;
import static com.sun.ts.tests.ejb30.common.sessioncontext.Constants.FROM_CLIENT;

abstract public class ClientBase extends EETest {
  protected Properties props;

  abstract protected Three1IF getSessionContextBean();

  abstract protected Three2IF getSessionContextBean2();

  abstract protected TwoRemoteIF getTwoSessionContextBean()
      throws TestFailedException;

  abstract protected TestIF getTestBean();

  abstract protected AcceptIF getAcceptBean();

  private Three1IF sessionContextBean;

  private Three2IF sessionContextBean2;

  private TestIF testBean;

  private AcceptIF acceptBean;

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    sessionContextBean = getSessionContextBean();
    sessionContextBean2 = getSessionContextBean2();
    testBean = getTestBean();
    acceptBean = getAcceptBean();
  }

  public void cleanup() throws Fault {
  }

  protected void removeBeans() {
    if (sessionContextBean != null) {
      try {
        sessionContextBean.remove();
        TLogger.log("Successfully removed sessionContextBean.");
      } catch (Exception e) {
        TLogger.log("Exception while removing sessionContextBean " + e);
      }
    }
    if (sessionContextBean2 != null) {
      try {
        sessionContextBean2.remove();
        TLogger.log("Successfully removed sessionContextBean2.");
      } catch (Exception e) {
        TLogger.log("Exception while removing sessionContextBean2 " + e);
      }
    }
    if (testBean != null) {
      try {
        testBean.remove();
        TLogger.log("Successfully removed testBean.");
      } catch (Exception e) {
        TLogger.log("Exception while removing testBean " + e);
      }
    }
    if (acceptBean != null) {
      try {
        acceptBean.remove();
        TLogger.log("Successfully removed acceptBean.");
      } catch (Exception e) {
        TLogger.log("Exception while removing acceptBean " + e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: getBusinessObjectRemote1
   * 
   * @test_Strategy:
   *
   */
  public void getBusinessObjectRemote1() throws Fault {
    try {
      Three1IF bob = sessionContextBean.getBusinessObjectRemote1();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getBusinessObjectRemote1Illegal
   * 
   * @test_Strategy:
   *
   */
  public void getBusinessObjectRemote1Illegal() throws Fault {
    try {
      sessionContextBean.getBusinessObjectRemote1Illegal();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getBusinessObjectRemote2
   * 
   * @test_Strategy:
   *
   */
  public void getBusinessObjectRemote2() throws Fault {
    try {
      Three2IF bob = sessionContextBean2.getBusinessObjectRemote2();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getBusinessObjectLocal1
   * 
   * @test_Strategy:
   *
   */
  public void getBusinessObjectLocal1() throws Fault {
    try {
      testBean.getBusinessObjectLocal1();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getBusinessObjectLocal1Illegal
   * 
   * @test_Strategy:
   *
   */
  public void getBusinessObjectLocal1Illegal() throws Fault {
    try {
      testBean.getBusinessObjectLocal1Illegal();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getBusinessObjectLocal2
   * 
   * @test_Strategy:
   *
   */
  public void getBusinessObjectLocal2() throws Fault {
    try {
      testBean.getBusinessObjectLocal2();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  //////////////////////////////////////////////////////////////////////
  /*
   * testName: passBusinessObjectRemote1
   * 
   * @test_Strategy:
   *
   */
  public void passBusinessObjectRemote1() throws Fault {
    try {
      Three1IF bob = sessionContextBean.getBusinessObjectRemote1();
      acceptBean.accept(bob);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: passBusinessObjectRemote2
   * 
   * @test_Strategy:
   *
   */
  public void passBusinessObjectRemote2() throws Fault {
    try {
      Three2IF bob = sessionContextBean2.getBusinessObjectRemote2();
      acceptBean.accept(bob);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: passBusinessObjectLocal1
   * 
   * @test_Strategy:
   *
   */
  public void passBusinessObjectLocal1() throws Fault {
    try {
      testBean.passBusinessObjectLocal1();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: passBusinessObjectLocal2
   * 
   * @test_Strategy:
   *
   */
  public void passBusinessObjectLocal2() throws Fault {
    try {
      testBean.passBusinessObjectLocal2();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: lookupIllegalArgumentException
   * 
   * @test_Strategy:
   *
   */
  public void lookupIllegalArgumentException() throws Fault {
    try {
      testBean.lookupIllegalArgumentException();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getInvokedBusinessInterfaceRemote1
   * 
   * @test_Strategy:
   *
   */
  public void getInvokedBusinessInterfaceRemote1() throws Fault {
    String expected = Three1IF.class.getName();
    String actual = null;
    try {
      Class intf = sessionContextBean.getInvokedBusinessInterfaceRemote1();
      actual = intf.getName();
      if (expected.equals(actual)) {
        TLogger.log("Got expected invoked business interface: " + expected);
      } else {
        throw new Fault("Expected class: " + expected + ", actual:" + actual);
      }
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getInvokedBusinessInterfaceRemote2
   * 
   * @test_Strategy:
   *
   */
  public void getInvokedBusinessInterfaceRemote2() throws Fault {
    String expected = Three2IF.class.getName();
    String actual = null;
    try {
      Class intf = sessionContextBean2.getInvokedBusinessInterfaceRemote2();
      actual = intf.getName();
      if (expected.equals(actual)) {
        TLogger.log("Got expected invoked business interface: " + expected);
      } else {
        throw new Fault("Expected class: " + expected + ", actual:" + actual);
      }
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getInvokedBusinessInterfaceLocal1
   * 
   * @test_Strategy:
   *
   */
  public void getInvokedBusinessInterfaceLocal1() throws Fault {
    try {
      testBean.getInvokedBusinessInterfaceLocal1();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getInvokedBusinessInterfaceLocal2
   * 
   * @test_Strategy:
   *
   */
  public void getInvokedBusinessInterfaceLocal2() throws Fault {
    try {
      testBean.getInvokedBusinessInterfaceLocal2();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getInvokedBusinessInterfaceRemoteIllegal
   * 
   * @test_Strategy:
   *
   */
  public void getInvokedBusinessInterfaceRemoteIllegal() throws Fault {
    try {
      TwoRemoteIF twoSessionContextBean = getTwoSessionContextBean();
      twoSessionContextBean.getInvokedBusinessInterfaceRemoteIllegal();
    } catch (TestFailedException e) {
      throw new Fault(e);
    } catch (RemoteException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: getInvokedBusinessInterfaceLocalIllegal
   * 
   * @test_Strategy:
   *
   */
  public void getInvokedBusinessInterfaceLocalIllegal() throws Fault {
    try {
      testBean.getInvokedBusinessInterfaceLocalIllegal();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }
}
