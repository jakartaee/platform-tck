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

package com.sun.ts.tests.jsf.api.javax_faces.lifecycle.lifecyclefactory;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_lifecycle_lifefactory_web";

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
   * @testName: lifecycleFactoryGetLifecycleTest
   * @assertion_ids: JSF:JAVADOC:1912; JSF:JAVADOC:1907
   * @test_Strategy: Ensure a reference to the default lifecycle object can be
   *                 obtained from he LifecycleFactory object.
   */
  public void lifecycleFactoryGetLifecycleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleFactoryGetLifecycleTest");
    invoke();
  }

  /**
   * @testName: lifecycleFactoryGetLifecycleIAETest
   * @assertion_ids: JSF:JAVADOC:1912; JSF:JAVADOC:1908
   * @test_Strategy: Ensure an IllegalArgumentException is thrown when passing
   *                 an invalid lifecycle ID (i.e. the ID was not registered).
   */
  public void lifecycleFactoryGetLifecycleIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleFactoryGetLifecycleIAETest");
    invoke();
  }

  /**
   * @testName: lifecycleFactoryAddLifecycleTest
   * @assertion_ids: JSF:JAVADOC:1912; JSF:JAVADOC:1904; JSF:JAVADOC:1908
   * @test_Strategy: Ensure a Lifecycle can be registered with the
   *                 LifecycleFactory and then obtained via getLifecycle using
   *                 the new ID.
   * 
   *                 Ensure an IllegalArgumentException is thrown when a
   *                 Lifecycle with the specified lifecycleId has already been
   *                 registered
   */
  public void lifecycleFactoryAddLifecycleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleFactoryAddLifecycleTest");
    invoke();
  }

  /**
   * @testName: lifecycleFactoryGetLifecycleIdsTest
   * @assertion_ids: JSF:JAVADOC:1912; JSF:JAVADOC:1910
   * @test_Strategy: Ensure getLifecycleIds() returns an Iterator with all of
   *                 the expected registered IDs.
   */
  public void lifecycleFactoryGetLifecycleIdsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleFactoryGetLifecycleIdsTest");
    invoke();
  }

  /**
   * @testName: lifecycleFactoryAddLifecycleNPETest
   * @assertion_ids: JSF:JAVADOC:1912; JSF:JAVADOC:1906
   * @test_Strategy: Ensure NullPointerException is thrown if lifecycleId or
   *                 lifecycle is null.
   */
  public void lifecycleFactoryAddLifecycleNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleFactoryAddLifecycleNPETest");
    invoke();
  }

  /**
   * @testName: lifecycleFactoryGetLifecycleNPETest
   * @assertion_ids: JSF:JAVADOC:1912; JSF:JAVADOC:1909
   * @test_Strategy: Ensure NullPointerException is thrown if lifecycleId is
   *                 null.
   */
  public void lifecycleFactoryGetLifecycleNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleFactoryGetLifecycleNPETest");
    invoke();
  }

  /**
   * @testName: lifecycleFactoryGetWrappedNullTest
   * @assertion_ids: JSF:JAVADOC:1912; JSF:JAVADOC:1911
   * @test_Strategy: Ensure A default implementation is provided that returns
   *                 null.
   */
  public void lifecycleFactoryGetWrappedNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleFactoryGetWrappedNullTest");
    invoke();
  }

} // end of URLClient
