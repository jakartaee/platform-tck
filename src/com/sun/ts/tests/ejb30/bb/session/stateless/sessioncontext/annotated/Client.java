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

package com.sun.ts.tests.ejb30.bb.session.stateless.sessioncontext.annotated;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.sessioncontext.AcceptIF;
import com.sun.ts.tests.ejb30.common.sessioncontext.ClientBase;
import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.sessioncontext.Three1IF;
import com.sun.ts.tests.ejb30.common.sessioncontext.Three2IF;
import com.sun.ts.tests.ejb30.common.sessioncontext.TestIF;
import com.sun.ts.tests.ejb30.common.sessioncontext.TwoRemoteIF;
import com.sun.ts.tests.ejb30.common.sessioncontext.TwoRemoteHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJB;

public class Client extends ClientBase {
  @EJB(name = "sessionContextBean")
  private static Three1IF sessionContextBean;

  @EJB(name = "sessionContextBean2")
  private static Three2IF sessionContextBean2;

  @EJB(name = "twoSessionContextBeanHome")
  private static TwoRemoteHome twoSessionContextBeanHome;

  @EJB(name = "testBean")
  private static TestIF testBean;

  @EJB(name = "acceptBean")
  private static AcceptIF acceptBean;

  protected Three2IF getSessionContextBean2() {
    return sessionContextBean2;
  }

  protected TwoRemoteIF getTwoSessionContextBean() throws TestFailedException {
    Object obj = null;
    try {
      obj = twoSessionContextBeanHome.create();
    } catch (RemoteException e) {
      throw new TestFailedException(e);
    } catch (CreateException e) {
      throw new TestFailedException(e);
    }
    return (TwoRemoteIF) obj;
  }

  protected Three1IF getSessionContextBean() {
    return sessionContextBean;
  }

  protected TestIF getTestBean() {
    return testBean;
  }

  protected AcceptIF getAcceptBean() {
    return acceptBean;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * @testName: getBusinessObjectRemote1
   * 
   * @assertion_ids: EJB:JAVADOC:164
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: getBusinessObjectRemote2
   * 
   * @assertion_ids: EJB:JAVADOC:164
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: getBusinessObjectLocal1
   * 
   * @assertion_ids: EJB:JAVADOC:164
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: getBusinessObjectLocal2
   * 
   * @assertion_ids: EJB:JAVADOC:164
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: passBusinessObjectRemote1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: passBusinessObjectRemote2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: passBusinessObjectLocal1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: passBusinessObjectLocal2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: lookupIllegalArgumentException
   * 
   * @assertion_ids: EJB:JAVADOC:33
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: getBusinessObjectRemote1Illegal
   * 
   * @assertion_ids: EJB:JAVADOC:165
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: getBusinessObjectLocal1Illegal
   * 
   * @assertion_ids: EJB:JAVADOC:165
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: getInvokedBusinessInterfaceRemote1
   * 
   * @assertion_ids: EJB:JAVADOC:170; EJB:JAVADOC:148
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: getInvokedBusinessInterfaceRemote2
   * 
   * @assertion_ids: EJB:JAVADOC:170
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: getInvokedBusinessInterfaceLocal1
   * 
   * @assertion_ids: EJB:JAVADOC:170
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: getInvokedBusinessInterfaceLocal2
   * 
   * @assertion_ids: EJB:JAVADOC:170
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: getInvokedBusinessInterfaceRemoteIllegal
   * 
   * @assertion_ids:EJB:JAVADOC:10; EJB:JAVADOC:11; EJB:JAVADOC:171
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: getInvokedBusinessInterfaceLocalIllegal
   * 
   * @assertion_ids: EJB:JAVADOC:171; EJB:JAVADOC:126
   * 
   * @test_Strategy:
   *
   */

}
