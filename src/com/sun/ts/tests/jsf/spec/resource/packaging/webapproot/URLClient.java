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
package com.sun.ts.tests.jsf.spec.resource.packaging.webapproot;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_resource_webapproot_web";

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
   * @testName: resourceResPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'docroot'/resources/resourceName
   * @since 2.0
   */
  public void resourceResPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceResPkgTest");
    invoke();
  }

  /**
   * @testName: resourceLibResPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'docroot'/resources/libName/resourceName
   * 
   * @since 2.0
   */
  public void resourceLibResPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceLibResPkgTest");
    invoke();
  }

  /**
   * @testName: resourceLibVerResPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 -
   *                 'docroot'/resources/libraryName/libraryVersion/resourceNam
   *                 e
   * 
   * @since 2.0
   */
  public void resourceLibVerResPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceLibVerResPkgTest");
    invoke();
  }

  /**
   * @testName: resourceResVerPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'docroot'/resources/resourceName/resourceVersion
   * 
   * @since 2.0
   */
  public void resourceResVerPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceResVerPkgTest");
    invoke();
  }

  /**
   * @testName: resourceLibVerResVerPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 -
   *                 'docroot'/resources/libraryName/libraryVersion/resourceName/resourceVersio
   *                 n
   * 
   * @since 2.0
   */
  public void resourceLibVerResVerPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceLibVerResVerPkgTest");
    invoke();
  }

  /**
   * @testName: reourceNoFileExtPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that receive a resource back when requesting a
   *                 resource with no File Extension.
   * 
   *                 Example: 'docroot'/resources/negative_test_image
   * @since 2.0
   */
  public void reourceNoFileExtPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "reourceNoFileExtPkgTest");
    invoke();
  }

  /**
   * @testName: resourceLocaleDEPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'docroot'/resources/de/duke-de.gif
   * 
   * 
   * @since 2.0
   */
  public void resourceLocaleDEPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceLocaleDEPkgTest");
    super.setMyLocale("de");
    invoke();
  }

  /**
   * @testName: resourceLocaleENPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'docroot'/resources/en/locLib/1_0/duke-en.gif
   * 
   * @since 2.0
   */
  public void resourceLocaleENPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceLocaleENPkgTest");
    super.setMyLocale("en");
    invoke();
  }

  /**
   * @testName: resourceLocaleFRPkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that the resourceIdentifier is correct when the
   *                 resource is packaged under:
   * 
   *                 - 'docroot'/resources/fr/duke-fr.gif/10_01.gif
   * 
   * @since 2.0
   */
  public void resourceLocaleFRPkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceLocaleFRPkgTest");
    super.setMyLocale("fr");
    invoke();
  }

  // ------------------------------------------------------ negative test
  // cases
  /**
   * @testName: resourceLibVerResVerNegativePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource in a lower version level
   * 
   *                 -
   *                 'docroot'/resources/libraryName/libraryVersion/resourceName/resourceVersio
   *                 n
   * 
   * @since 2.0
   */
  public void resourceLibVerResVerNegativePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceLibVerResVerNegativePkgTest");
    invoke();
  }

  /**
   * @testName: reourceLibVerResNegativePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource in a lower library version level.
   * 
   *                 - 'docroot'/resources/styles/1_0/never-found.css
   * 
   * @since 2.0
   */
  public void reourceLibVerResNegativePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "reourceLibVerResNegativePkgTest");
    invoke();
  }

  /**
   * @testName: reourceTrailingUSNegativePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource with a trailing '_' in the name.
   * 
   *                 Example:
   *                 'docroot'/resources/styles/2_0/trailing.css/42_.css
   * 
   * @since 2.0
   */
  public void reourceTrailingUSNegativePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "reourceTrailingUSNegativePkgTest");
    invoke();
  }

  /**
   * @testName: reourceLeadingUSNegativePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource with a leading '_' in the name.
   * 
   *                 Example: 'docroot'/resources/styles/2_0/leading.css/_99.css
   * @since 2.0
   */
  public void reourceLeadingUSNegativePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "reourceLeadingUSNegativePkgTest");
    invoke();
  }

  /**
   * @testName: reourceNoUSNegetivePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource with an invalid version separator.
   * 
   *                 Example:
   *                 'docroot'/resources/styles/2_0/noUnderscore.css/99-100-101.cs
   *                 s
   * @since 2.0
   */
  public void reourceNoUSNegetivePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "reourceNoUSNegetivePkgTest");
    invoke();
  }

  /**
   * @testName: reourceNoFileExtVerNegetivePkgTest
   * @assertion_ids: PENDING: added assertions ID(s)
   * @test_Strategy: Validate that we do not receive a resource back when
   *                 requesting a resource with no File Extension.
   * 
   *                 Example: 'docroot'/resources/doug.css/1_1_1
   * @since 2.0
   */
  public void reourceNoFileExtVerNegetivePkgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "reourceNoFileExtVerNegetivePkgTest");
    invoke();
  }
} // end of URLClient
