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

package com.sun.ts.tests.jsf.spec.el.managedbean.ee;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_el_mgbean_ee_web";

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

  /* EE Specific Test Declarations */

  /**
   * @testName: managedBeanPostConstructTest
   * @assertion_ids: JSF:SPEC:82; JSF:SPEC:82.1
   * @test_Strategy: Ensure the managed bean facility can execute package
   *                 private, private, public, and protected methods annotated
   *                 with the PostConstruct annotation, and that execution
   *                 occurs prior to placing the bean in scope.
   */
  public void managedBeanPostConstructTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPostConstructTest");
    invoke();
  }

  /**
   * @testName: managedBeanPreDestroyTest
   * @assertion_ids: JSF:SPEC:82; JSF:SPEC:82.3
   * @test_Strategy: Ensure the managed bean facility can execute package
   *                 private, private, public, and protected methods annotated
   *                 with the PreDestroy annotation, and that execution occurs
   *                 upon removing the bean from scope.
   */
  public void managedBeanPreDestroyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPreDestroyTest");
    invoke();
  }

  /**
   * @testName: managedBeanPostConstructExceptionTest
   * @assertion_ids: JSF:SPEC:82; JSF:SPEC:82.2
   * @test_Strategy: Verify that a managed bean that throws a runtime exception
   *                 in a method annotated with @PostConstruct is not placed in
   *                 service.
   */
  public void managedBeanPostConstructExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPostConstructExceptionTest");
    invoke();
  }

  /**
   * @testName: managedBeanPreDestroyExceptionTest
   * @assertion_ids: JSF:SPEC:82; JSF:SPEC:82.4
   * @test_Strategy: Verify that if a managed bean throws a runtime exception in
   *                 a method annotated with @PreDestroy, the execution of the
   *                 JSF implementation is not altered.
   */
  public void managedBeanPreDestroyExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "managedBeanPreDestroyExceptionTest");
    invoke();
  }

} // end of URLClient
