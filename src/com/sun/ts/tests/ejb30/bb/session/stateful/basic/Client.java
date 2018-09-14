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

package com.sun.ts.tests.ejb30.bb.session.stateful.basic;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import java.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.ejb.NoSuchEJBException;
import javax.naming.NamingException;

public class Client extends EETest {
  private static final String refName = "java:comp/env/ejb/RemoteCalculatorBean";

  // not to inject here or in application-client.xml.
  // need to test that PostConstruct is always invoked even when there is no
  // injection
  // @EJB(beanName="RemoteCalculatorBean", name="RemoteCalculatorBean")
  // static private StatefulRemoteCalculator bean;

  private static int postConstructCallsCount;

  private Properties props;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  // declared as post-construct in application-client.xml
  // @PostConstruct
  private static void postConstruct() {
    postConstructCallsCount++;
  }

  /*
   * @testName: postConstructInvokedEvenNoResourceInjection
   * 
   * @assertion_ids: EJB:JAVADOC:185; EJB:JAVADOC:147
   * 
   * @test_Strategy: PostConstruct method must be invoked even when there is no
   * resource injection.
   */
  public void postConstructInvokedEvenNoResourceInjection() throws Fault {
    if (postConstructCallsCount > 0) {
      TLogger
          .log("Got expected result.  Client.PostConstruct method is invoked "
              + postConstructCallsCount + " times");
    } else {
      throw new Fault("Expecting the Client.PostConstruct to be invoked "
          + " at least once, but actual " + postConstructCallsCount);
    }
  }

  /*
   * @testName: testOperations
   * 
   * @assertion_ids: EJB:JAVADOC:185; EJB:JAVADOC:147
   * 
   * @test_Strategy: o using @Stateful annotation o use @Resource annotation o
   * use @Remote annotation o use @Remove annotation o business method may throw
   * arbitary application exceptions, which are propogated back to clients. o No
   * Init method is used here
   */
  public void testOperations() throws Fault {
    StatefulRemoteCalculator bean = null;
    boolean passed = false;
    try {
      bean = (StatefulRemoteCalculator) ServiceLocator.lookup(refName);
    } catch (NamingException e) {
      throw new Fault(e);
    }
    logMsg("About to call bean " + bean.toString());
    testRemoteAdd(bean);
    testRemoteThrowIt(bean);
    try {
      bean.remoteAdd(1, 2);
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
    }
  }

  private void testRemoteAdd(StatefulRemoteCalculator bean) throws Fault {
    int op1 = 2;
    int op2 = 3;
    int expected = op1 + op2;
    int result = 0;
    try {
      result = bean.remoteAdd(op1, op2);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
    if (result != expected) {
      throw new Fault("Expected " + expected + ", actual " + result);
    }
  }

  private void testRemoteThrowIt(StatefulRemoteCalculator bean) throws Fault {
    boolean passed = false;
    try {
      bean.remoteThrowIt();
    } catch (CalculatorException e) {
      passed = true;
    } catch (Exception e) {
      throw new Fault("Unexpected exception:", e);
    }
    if (!passed) {
      throw new Fault("Expected CalculatorException, actual no exception");
    }
  }

  public void cleanup() throws Fault {
    // bean already removed in test method. If not, need to remove it here.
  }
}
