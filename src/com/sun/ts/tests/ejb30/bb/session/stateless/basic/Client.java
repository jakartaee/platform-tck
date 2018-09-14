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

package com.sun.ts.tests.ejb30.bb.session.stateless.basic;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import java.util.Properties;
import javax.annotation.PostConstruct;
import com.sun.ts.lib.harness.EETest;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;

import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.naming.NamingException;

public class Client extends EETest {
  private static int postConstructCallsCount;

  private static final String beanRefName = "java:comp/env/ejb/RemoteCalculatorBean";

  private static final String bean2RefName = "java:comp/env/ejb/RemoteCalculatorBean2";

  private static final String bean3RefName = "java:comp/env/ejb/RemoteCalculatorBean3";

  private static final String bean4RefName = "java:comp/env/ejb/RemoteCalculatorBean4";

  private static final String bean5RefName = "java:comp/env/ejb/RemoteCalculatorBean5";

  // not to inject here or in application-client.xml.
  // need to test that PostConstruct is always invoked even when there is no
  // injection
  // @EJB(name="bean", beanName="RemoteCalculatorBean")
  // private static RemoteCalculator bean;
  //
  // @EJB(name="bean2", beanName="RemoteCalculatorBean2")
  // private static RemoteCalculator bean2;

  private Properties props;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  // post-construct method may be final, except in ejb. See Common Annotation
  // Spec 2.5
  @PostConstruct
  final static void postConstruct() {
    postConstructCallsCount++;
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  /*
   * @testName: postConstructInvokedEvenNoResourceInjection
   * 
   * @assertion_ids: EJB:JAVADOC:186; EJB:JAVADOC:147
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
   * @testName: testRemoteAdd
   * 
   * @assertion_ids: EJB:JAVADOC:186; EJB:JAVADOC:147
   * 
   * @test_Strategy: o using @Stateless annotation o use @Resource annotation o
   * use @Remote annotation
   */
  public void testRemoteAdd() throws Fault {
    RemoteCalculator bean = null;
    int op1 = 2;
    int op2 = 3;
    int expected = op1 + op2;
    int result = 0;
    int result2 = 0;
    try {
      bean = (RemoteCalculator) ServiceLocator.lookup(beanRefName);
      result = bean.remoteAdd(op1, op2);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
    if (result == expected) {
      TLogger.log("Got expected result: " + expected);
    } else {
      throw new Fault("Expected " + expected + ", actual " + result);
    }

  }

  /*
   * @testName: postConstructCalledEvenNoResourceInjectionInBean
   * 
   * @assertion_ids: EJB:JAVADOC:186; EJB:JAVADOC:147
   * 
   * @test_Strategy: RemoteCalculatorBean2 contains no injection, but its
   * post-construct method must be called.
   * 
   */
  public void postConstructCalledEvenNoResourceInjectionInBean() throws Fault {
    RemoteCalculator bean2 = null;
    int op1 = 0;
    int op2 = 0;
    int expectedPostConstructCallsCount = 1;
    int expected = op1 + op2 + expectedPostConstructCallsCount;
    try {
      bean2 = (RemoteCalculator) ServiceLocator.lookup(bean2RefName);
    } catch (NamingException e) {
      throw new Fault(e);
    }
    int result2 = bean2.remoteAdd(op1, op2);
    if (result2 == expected) {
      TLogger.log("Got expected result: " + expected + ", which means "
          + "post-construct method is called even when the bean "
          + "contains no injection.");
    } else {
      throw new Fault("Expected " + expected + ", actual " + result2
          + ".  Likely reason is that post-construct in the target bean "
          + "may not have been called when the business method is invoked.");
    }
  }

  /*
   * @testName: postConstructInSuperCalledEvenNoAnnotationInBean
   * 
   * @assertion_ids: EJB:JAVADOC:186; EJB:JAVADOC:147
   * 
   * @test_Strategy: RemoteCalculatorBean3 contains no annotation, but its
   * post-construct defined in its superclass must be called.
   * 
   */
  public void postConstructInSuperCalledEvenNoAnnotationInBean() throws Fault {
    RemoteCalculator bean3 = null;
    int op1 = -10;
    int op2 = -20;
    int expectedPostConstructCallsCount = 1;
    int expected = op1 + op2 + expectedPostConstructCallsCount;
    try {
      bean3 = (RemoteCalculator) ServiceLocator.lookup(bean3RefName);
    } catch (NamingException e) {
      throw new Fault(e);
    }
    int result3 = bean3.remoteAdd(op1, op2);
    if (result3 == expected) {
      TLogger.log("Got expected result: " + expected + ", which means "
          + "post-construct method in superclass is called even when the bean "
          + "contains no annotation.");
    } else {
      throw new Fault("Expected " + expected + ", actual " + result3
          + ".  Likely reason is that post-construct in the target bean superclass "
          + "may not have been called when the business method is invoked.");
    }
  }

  /*
   * @testName: injectedIntoSuperCalledEvenNoAnnotationInBean
   * 
   * @assertion_ids: EJB:JAVADOC:186; EJB:JAVADOC:147
   * 
   * @test_Strategy: RemoteCalculatorBean4 contains no annotation, but a field
   * in superclass must be injected.
   * 
   */
  public void injectedIntoSuperCalledEvenNoAnnotationInBean() throws Fault {
    RemoteCalculator bean4 = null;
    int op1 = 1;
    int op2 = 2;
    int unexpected = op1 + op2;
    try {
      bean4 = (RemoteCalculator) ServiceLocator.lookup(bean4RefName);
    } catch (NamingException e) {
      throw new Fault(e);
    }
    int result4 = bean4.remoteAdd(op1, op2);
    if (result4 > unexpected) {
      TLogger.log("Did not get unexpected result.  Actual result " + result4
          + ", which means "
          + "injection in superclass is called even when the bean "
          + "contains no annotation.");
    } else {
      throw new Fault("Unexpected result " + result4
          + ".  Likely reason is that injection in the target bean superclass "
          + "may not have been performed when the business method is invoked.");
    }
  }

  /*
   * @testName: noComponentDefiningAnnotations
   * 
   * @assertion_ids: EJB:JAVADOC:186; EJB:JAVADOC:147
   * 
   * @test_Strategy: RemoteCalculatorBean5 contains no component-defining
   * annotations, but has @Resource and @PostConstruct annotations. Both must be
   * processed. If @Resource or @PostConstruct not processed, the client will
   * receive EJBException.
   */
  public void noComponentDefiningAnnotations() throws Fault {
    RemoteCalculator bean5 = null;
    int op1 = 1;
    int op2 = 2;
    try {
      bean5 = (RemoteCalculator) ServiceLocator.lookup(bean5RefName);
    } catch (NamingException e) {
      throw new Fault(e);
    }
    int result5 = bean5.remoteAdd(op1, op2);
    TLogger.log("remoteAdd returned " + result5);

  }

  /*
   * @testName: testRemoteThrowIt
   * 
   * @assertion_ids: EJB:JAVADOC:186; EJB:JAVADOC:147
   * 
   * @test_Strategy: o business method may throw arbitary application
   * exceptions, which are propogated back to clients.
   */
  public void testRemoteThrowIt() throws Fault {
    RemoteCalculator bean = null;
    boolean passed = false;
    try {
      bean = (RemoteCalculator) ServiceLocator.lookup(beanRefName);
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
  }
}
