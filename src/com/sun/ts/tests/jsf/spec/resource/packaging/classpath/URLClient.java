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
package com.sun.ts.tests.jsf.spec.resource.packaging.classpath;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_resource_classpath_web";

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
   * @testName: resourceClassPathResPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'META-INF'/resources/resourceName
   * @since 2.0
   */
  public void resourceClassPathResPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceClassPathResPkgTest");
    invoke();
  }

  /**
   * @testName: resourceClassPathNoFileExtPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that receive a resource back when requesting a
   *                 resource with no File Extension.
   * 
   *                 Example: 'META-INF'/resources/negative_test_image
   * @since 2.0
   */

  public void resourceClassPathNoFileExtPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceClassPathNoFileExtPkgTest");
    invoke();
  }

  /**
   * @testName: resourceClassPathLocaleDEPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'META-INF'/resources/de/duke-de.gif
   * 
   * 
   * @since 2.0
   */
  public void resourceClassPathLocaleDEPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceClassPathLocaleDEPkgTest");
    super.setMyLocale("de");
    invoke();
  }

  /**
   * @testName: resourceClassPathLocaleENPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'META-INF'/resources/en/locLib/1_0/duke-en-1.gif
   * 
   * @since 2.0
   */
  public void resourceClassPathLocaleENPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceClassPathLocaleENPkgTest");
    super.setMyLocale("en");
    invoke();
  }

  /**
   * @testName: resourceClassPathLocaleFRPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'META-INF'/resources/fr/duke-fr-1.gif/10_01.gif
   * 
   * @since 2.0
   */
  public void resourceClassPathLocaleFRPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceClassPathLocaleFRPkgTest");
    super.setMyLocale("fr");
    invoke();
  }

  // ------------------------------------------------------ negative test
  // cases

  /**
   * @testName: resourceClassPathTrailingUSNegativePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource with a trailing '_' in the name.
   * 
   *                 Example:
   *                 'META-INF'/resources/class-styles/2_0/trailing.css/42_.cs s
   * 
   * @since 2.0
   */
  public void resourceClassPathTrailingUSNegativePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceClassPathTrailingUSNegativePkgTest");
    invoke();
  }

  /**
   * @testName: resourceClassPathLeadingUSNegativePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource with a leading '_' in the name.
   * 
   *                 Example:
   *                 'META-INF'/resources/class-styles/2_0/leading.css/_99.css
   * @since 2.0
   */
  public void resourceClassPathLeadingUSNegativePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceClassPathLeadingUSNegativePkgTest");
    invoke();
  }

  /**
   * @testName: resourceClassPathNoFileExtVerNegetivePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource with no File Extension.
   * 
   *                 Example: 'META-INF'/resources/doug.css/1_1_1
   * @since 2.0
   */
  public void resourceClassPathNoFileExtVerNegetivePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceClassPathNoFileExtVerNegetivePkgTest");
    invoke();
  }

  /**
   * @testName: jsfJsDoesExistTest
   * @assertion_ids: JSF:SPEC:225; JSF:SPEC:226
   * @test_Strategy: Validate that we are able to get the Resource jsf.js from
   *                 the libarary javax.faces.
   * 
   * @since 2.2
   */
  public void jsfJsDoesExistTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jsfJsDoesExistTest");
    invoke();
  }
} // end of URLClient
