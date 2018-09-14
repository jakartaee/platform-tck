/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.rs.ext.runtimedelegate.setinstance;

import javax.ws.rs.ext.RuntimeDelegate;

import com.sun.ts.tests.jaxrs.api.rs.ext.runtimedelegate.JAXRSDelegateClient;
import com.sun.ts.tests.jaxrs.api.rs.ext.runtimedelegate.TckRuntimeDelegate;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSDelegateClient {

  private static final long serialVersionUID = -5586431064207012301L;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JAXRSClient theTests = new JAXRSClient();
    theTests.run(args);
  }

  /* Run test */
  /*
   * @testName: askForTckRuntimeDelegateGivenBySetInstanceTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:291;JAXRS:JAVADOC:292;
   * 
   * @test_Strategy: Set new RuntimeDelegate and check it is TckRuntimeDelegate
   * 
   */
  public void askForTckRuntimeDelegateGivenBySetInstanceTest() throws Fault {
    RuntimeDelegate original = RuntimeDelegate.getInstance();
    RuntimeDelegate.setInstance(new TckRuntimeDelegate());
    try {
      assertRuntimeDelegate();
    } finally {
      RuntimeDelegate.setInstance(original);
      assertRuntimeDelegate(false);
    }
  }

  /*
   * @testName: checkTckRuntimeDelegateIsNotDefaultTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:292;
   * 
   * @test_Strategy: Check by default, it is not our RuntimeDelegate
   */
  public void checkTckRuntimeDelegateIsNotDefaultTest() throws Fault {
    assertRuntimeDelegate(false);
  }

}
