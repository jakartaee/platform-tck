/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.render.renderkitfactory;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_render_rkfactory_web";

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
   * @testName: renderKitFactoryAddGetRenderKitTest
   * @assertion_ids: JSF:JAVADOC:2093; JSF:JAVADOC:2095
   * @test_Strategy: Ensure RenderKits can be registered and then obtained after
   *                 registration.
   */
  public void renderKitFactoryAddGetRenderKitTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitFactoryAddGetRenderKitTest");
    invoke();
  }

  /**
   * @testName: renderKitFactoryGetRenderKitIdsTest
   * @assertion_ids: JSF:JAVADOC:2098
   * @test_Strategy: Ensure the Iterator returned by getRenderKitIds() contains
   *                 all of the expected IDs.
   */
  public void renderKitFactoryGetRenderKitIdsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitFactoryGetRenderKitIdsTest");
    invoke();
  }

} // end of URLClient
