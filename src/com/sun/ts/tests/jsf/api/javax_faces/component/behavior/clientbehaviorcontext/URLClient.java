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

package com.sun.ts.tests.jsf.api.javax_faces.component.behavior.clientbehaviorcontext;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_comp_behavior_clientbehaviorcontext_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    // don't reset the context root if already been set
    if (getContextRoot() == null) {
      setContextRoot(CONTEXT_ROOT);
    }
    // don't reset the Servlet name if already set
    if (getServletName() == null) {
      setServletName(DEFAULT_SERVLET_NAME);
    }
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run tests */

  // ------------------------------------------------------------- Test
  // methods

  /**
   * @testName: createClientBehaviorContextNPETest
   * @assertion_ids: JSF:JAVADOC:1025
   * @test_Strategy: Verify that a NullpointerException is thrown when any of
   *                 the following arguments are null
   * 
   *                 -context -component -eventName
   * 
   * @since 2.2
   */
  public void createClientBehaviorContextNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "createClientBehaviorContextNPETest");
    invoke();
  }

  /**
   * @testName: createClientBehaviorContextParamaterTest
   * @assertion_ids: JSF:JAVADOC:1031; JSF:JAVADOC:1032; JSF:JAVADOC:1033
   * @test_Strategy: Validate that when we create a
   *                 ClientBehaviorContext.Parameter that we can call the
   *                 following methods and get the correct information back.
   * 
   * @since 2.2
   */
  public void createClientBehaviorContextParamaterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "createClientBehaviorContextParamaterTest");
    invoke();
  }
}
