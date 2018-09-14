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
package com.sun.ts.tests.jsf.api.javax_faces.component.annotation;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseUIComponentClient;

public class URLClient extends BaseUIComponentClient {

  private static final String CONTEXT_ROOT = "/jsf_comp_annotation_web";

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
   * @testName: uiOutputRDTypeTest
   * @assertion_ids: JSF:JAVADOC:273; JSF:JAVADOC:276
   * @test_Strategy: Validate ResourceDependecy ResourceType.
   */
  public void uiOutputRDTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiOutputRDTypeTest");
    invoke();
  }

  /**
   * @testName: uiOutputRDAttributeTest
   * @assertion_ids: JSF:JAVADOC:273; JSF:JAVADOC:274; JSF:JAVADOC:275;
   *                 JSF:JAVADOC:276
   * @test_Strategy: Validate Library & Target Attributes
   */
  public void uiOutputRDAttributeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiOutputRDAttributeTest");
    invoke();
  }
}
