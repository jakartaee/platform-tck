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

package com.sun.ts.tests.jsf.spec.webapp.factoryfinder;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_webapp_ff_web";

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
   * @testName: factoryFinderConfig1Test
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure an ApplicatyionFactory can be specified via a
   *                 faces-config file located in WEB-INF. The test will make
   *                 sure the expected ApplicationFactory instance is returned
   *                 *and* that the JSF implementation property called the
   *                 single argument constructor passing in the default JSF
   *                 ApplicationFactory implementation.
   */
  public void factoryFinderConfig1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "factoryFinderConfig1Test");
    invoke();
  }

  /**
   * @testName: factoryFinderConfig2Test
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure an FacesConfigFactory can be specified via a
   *                 faces-config identified by the javax.faces.CONFIG_FILES
   *                 context initialization parameter in the web.xml. The test
   *                 will make sure the expected FacesContextFactory instance is
   *                 returned *and* that the JSF implementation property called
   *                 the single argument constructor passing in the default JSF
   *                 FacesContextFactory implementation.
   */
  public void factoryFinderConfig2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "factoryFinderConfig2Test");
    invoke();
  }

  /**
   * @testName: factoryFinderConfig3Test
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure an LifecycleFactory can be specified via a
   *                 faces-config file located in META-INF directory of a JAR
   *                 file. The test will make sure the expected LifecycleFactory
   *                 instance is returned *and* that the JSF implementation
   *                 property called the single argument constructor passing in
   *                 the default JSF LifecycleFactory implementation.
   */
  public void factoryFinderConfig3Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "factoryFinderConfig3Test");
    invoke();
  }

  /**
   * @testName: factoryFinderConfig4Test
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure an RenderKitFactory can be specified via
   *                 META-INF/services facility of a JAR file. The test will
   *                 make sure the expected RenderKitFactory instance is
   *                 returned *and* that the JSF implementation property called
   *                 the single argument constructor passing in the default JSF
   *                 RenderKItFactory implementation.
   */
  public void factoryFinderConfig4Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "factoryFinderConfig4Test");
    invoke();
  }

} // end of URLClient
