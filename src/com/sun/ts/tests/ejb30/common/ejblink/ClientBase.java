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

package com.sun.ts.tests.ejb30.common.ejblink;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.ejb.EJB;
import javax.naming.NamingException;

abstract public class ClientBase extends EETest implements Constants {

  private EjbLinkIF bean1;

  private EjbLinkIF bean2;

  protected Properties props;

  @EJB(name = "ejb/injected-EjbLink1Bean", beanName = "one_ejb.jar#EjbLink1Bean", description = "this one is not declared in descriptor")
  private static EjbLinkIF injectedBean1;

  // 2 remote beans directly accessed by client
  protected EjbLinkIF getBean1() {
    return bean1;
  }

  protected EjbLinkIF getBean2() {
    return bean2;
  }

  protected EjbLinkIF lookupBean1() throws Fault {
    Object obj = null;
    try {
      obj = ServiceLocator.lookup(BEAN1_REF_NAME);
    } catch (NamingException e) {
      throw new Fault("Failed to lookup " + BEAN1_REF_NAME, e);
    }
    return (EjbLinkIF) obj;
  }

  protected EjbLinkIF lookupBean2() throws Fault {
    Object obj = null;
    try {
      obj = ServiceLocator.lookup(BEAN2_REF_NAME);
    } catch (NamingException e) {
      throw new Fault("Failed to lookup " + BEAN2_REF_NAME, e);
    }
    return (EjbLinkIF) obj;
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    bean1 = lookupBean1();
    bean2 = lookupBean2();
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
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove bean1.");
      }
    }

    if (getBean2() != null) {
      try {
        getBean2().remove();
        TLogger.log("bean2 removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove bean2.");
      }
    }
  }

  public void cleanup() throws Fault {
  }

  /*
   * testName: callBean1
   * 
   * @test_Strategy:
   *
   */
  public void callBean1() throws Fault {
    try {
      getBean1().call();
    } catch (TestFailedException e) {
      throw new Fault("Test failed", e);
    }
  }

  /*
   * testName: callBean2
   * 
   * @test_Strategy:
   *
   */
  public void callBean2() throws Fault {
    try {
      getBean2().call();
    } catch (TestFailedException e) {
      throw new Fault("Test failed", e);
    }
  }

  /*
   * testName: callBean1Bean2
   * 
   * @test_Strategy:
   *
   */
  public void callBean1Bean2() throws Fault {
    try {
      getBean1().callTwo();
    } catch (TestFailedException e) {
      throw new Fault("Test failed", e);
    }
  }

  /*
   * testName: callBean2Bean1
   * 
   * @test_Strategy:
   *
   */
  public void callBean2Bean1() throws Fault {
    try {
      getBean2().callOne();
    } catch (TestFailedException e) {
      throw new Fault("Test failed", e);
    }
  }

  /*
   * testName: callBean2Bean3
   * 
   * @test_Strategy:
   *
   */
  public void callBean2Bean3() throws Fault {
    try {
      getBean2().callThree();
    } catch (TestFailedException e) {
      throw new Fault("Test failed", e);
    }
  }

  /*
   * testName: callBean2Bean1Local
   * 
   * @test_Strategy: bean1 packaged in jar1, bean2 and bean3 packaged in jar2.
   * Bean2 should be able to locally access bean1. <ejb-local-ref> is declared
   * in two_ejb.xml
   *
   */
  public void callBean2Bean1Local() throws Fault {
    try {
      getBean2().callOneLocal();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: callInjectedBean1
   * 
   * @test_Strategy: inject bean1, using fully qualified bean name
   * (<ejb-jar-name>#<bean-name>)
   *
   */
  public void callInjectedBean1() throws Fault {
    try {
      injectedBean1.call();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

}
