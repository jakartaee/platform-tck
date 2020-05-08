/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.el.variableresolver;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_el_varresolver_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: varResolverResolveVariableTest
   * @assertion_ids: JSF:JAVADOC:1770
   * @test_Strategy: Ensure a non-null result for a variable that is known to
   *                 resolve on all implementations and null is returned for a
   *                 variable that can't be resolved.
   */
  public void varResolverResolveVariableTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "varResolverResolveVariableTest");
    invoke();
  }

  /**
   * @testName: varResolverResolveVariableNPETest
   * @assertion_ids: JSF:JAVADOC:1772
   * @test_Strategy: Ensure an NPE is thrown if the FacesContext or name
   *                 paramters are null.
   */
  public void varResolverResolveVariableNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "varResolverResolveVariableNPETest");
    invoke();
  }

} // end of URLClient
