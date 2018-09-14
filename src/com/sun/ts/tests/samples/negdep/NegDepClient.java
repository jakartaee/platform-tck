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

package com.sun.ts.tests.samples.negdep;

import java.io.*;
import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

/**
 * The NegDepClient class will only be executed if deployment of an invalid war
 */
import com.sun.javatest.Status;

public class NegDepClient extends EETest {

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    NegDepClient theTests = new NegDepClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * 
   * @class.testArgs: -expectdeploymentfailure
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {

    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /* Run test */

  /*
   * @testName: negDepTest
   * 
   * @assertion: Negative Deployment test sample
   * 
   * @test_Strategy: If the invalid test war associated with this fails to
   * deploy, the test passes. However, if the war does deploy, we get into this
   * test code and we report failure.
   *
   */
  @CleanupMethod(name = "negDepTestCleanup")
  public void negDepTest() throws Fault {

    throw new Fault(
        "Test Failed - expected deployment to fail, but it succeeded!");

  }

  /* cleanup -- none in this case */
  public void negDepTestCleanup() throws Fault {
    logMsg(
        "Cleanup method, negDepTestCleanup, specified by @CleanupMethod annotation ok;");
  }
}
