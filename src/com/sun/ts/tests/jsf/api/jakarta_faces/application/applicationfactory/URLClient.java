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

package com.sun.ts.tests.jsf.api.jakarta_faces.application.applicationfactory;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_applfactory_web";

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
   * @testName: applicationFactoryGetSetApplicationTest
   * @assertion_ids: JSF:JAVADOC:133; JSF:JAVADOC:134; JSF:JAVADOC:136
   * @test_Strategy: Validate Application instances can be obtained and set via
   *                 an ApplicationFactory instance.
   */
  public void applicationFactoryGetSetApplicationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationFactoryGetSetApplicationTest");
    invoke();
  }

  /**
   * @testName: applicationFactorySetApplicationNPETest
   * @assertion_ids: JSF:JAVADOC:133; JSF:JAVADOC:137
   * @test_Strategy: Validate ApplicationFactory.setApplication(Application)
   *                 throws NPE when argument is null.
   */
  public void applicationFactorySetApplicationNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationFactorySetApplicationNPETest");
    invoke();
  }

  /**
   * @testName: applicationFactoryGetWrappedTest
   * @assertion_ids: JSF:JAVADOC:133; JSF:JAVADOC:135
   * @test_Strategy: Validate ApplicationFactory.setApplication(Application)
   *                 throws NPE when argument is null.
   */
  public void applicationFactoryGetWrappedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "applicationFactoryGetWrappedTest");
    invoke();
  }
} // end of URLClient
