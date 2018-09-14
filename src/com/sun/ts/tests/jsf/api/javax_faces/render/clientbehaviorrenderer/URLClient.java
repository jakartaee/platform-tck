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

/*
 * $Id$
 */

package com.sun.ts.tests.jsf.api.javax_faces.render.clientbehaviorrenderer;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_render_cbrender_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * 
   * XXX This test needs to be fixed/deleted for JSF 2.3. This test uses the
   * TestServlet class which imports and utilizes an RI specific class. See bug
   * ID: 20704066
   * 
   * @testName: clientBehaviorRendererDecodeNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2052
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if context,
   *                 component, or behavior is null.
   * 
   * @since: 2.2
   */
  public void clientBehaviorRendererDecodeNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "clientBehaviorRendererDecodeNPETest");
    invoke();
  }

} // end of URLClient
