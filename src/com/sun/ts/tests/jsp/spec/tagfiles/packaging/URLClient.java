/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @(#)URLClient.java	1.1 12/09/02
 */

package com.sun.ts.tests.jsp.spec.tagfiles.packaging;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import java.rmi.UnexpectedException;

public class URLClient extends AbstractUrlClient {
  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jsp_tagfile_pkg_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspTagFilePackagedJarTest
   * 
   * @assertion_ids: JSP:SPEC:220.1
   * 
   * @test_Strategy: Validate that tag files packaged in a JAR file and
   * referenced in a TLD, can be recognized by the container and invoked within
   * a Page.
   */
  public void jspTagFilePackagedJarTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/JspTagFilePackagedJarTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspTagFilePackagedJarIgnoredTagTest
   * 
   * @assertion_ids: JSP:SPEC:220.2
   * 
   * @test_Strategy: Validate that if a Tag file is packaged in a JAR but not
   * referenced by a TLD, the container ignores the tag file. Since the Page
   * will refer to the ignored tag, a translation error should occur by its use.
   */
  public void jspTagFilePackagedJarIgnoredTagTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/JspTagFilePackagedJarIgnoredTagTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspTagFilePackagedWarTest
   * 
   * @assertion_ids: JSP:SPEC:221
   * 
   * @test_Strategy: Validate that tag files can be properly detected by the
   * container and that they can be used in a Page.
   */
  public void jspTagFilePackagedWarTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/JspTagFilePackagedWarTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED1|Test PASSED2");
    invoke();
  }

  /*
   * @testName: jspTagFilePackagedWarTldTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate tag files packaged in a web application can be
   * explicity referenced in a TLD to be used by a a Page.
   */
  public void jspTagFilePackagedWarTldTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/JspTagFilePackagedWarTldTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED1");
    invoke();
  }

  /*
   * @testName: implicitTldMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:311
   * 
   * @test_Strategy: [ImplicitTldMinimumJspVersion] Show that if the JSP version
   * specified in an implicit.tld file is less than 2.0 a translation error will
   * result.
   */
  public void implicitTldMinimumJspVersionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldMinimumJspVersion.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: implicitTldAdditionalElementsTest
   * 
   * @assertion_ids: JSP:SPEC:310
   * 
   * @test_Strategy: [ImplicitTldAdditionalElements] Show that if the JSP
   * version specified in an implicit.tld file is less than 2.0 a translation
   * error will result.
   */
  public void implicitTldAdditionalElementsTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldAdditionalElements.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: tldImplicitTldJspVersionNotMatchTest
   * 
   * @assertion_ids: JSP:SPEC:313
   * 
   * @test_Strategy: [TldImplicitTldJspVersionNotMatch] Show that if a tag file
   * is referenced by both a TLD and an implicit TLD, the JSP versions of the
   * TLD and implicit TLD do not need to match.
   */
  public void tldImplicitTldJspVersionNotMatchTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/TldImplicitTldJspVersionNotMatch.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitTldReservedName20Test
   * 
   * @assertion_ids: JSP:SPEC:308
   * 
   * @test_Strategy: [ImplicitTldReservedName] The JSP version of an implicit
   * tag library may be configured by placing a TLD with the reserved name
   * "implicit.tld" in the same directory as the implicit tag library's
   * constituent tag files. Verify this for version 2.0 by embedding '{#' in an
   * action without generating a translation error.
   */
  public void implicitTldReservedName20Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldReservedName20.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitTldReservedName21Test
   * 
   * @assertion_ids: JSP:SPEC:308
   * 
   * @test_Strategy: [ImplicitTldReservedName] The JSP version of an implicit
   * tag library may be configured by placing a TLD with the reserved name
   * "implicit.tld" in the same directory as the implicit tag library's
   * constituent tag files. Verify this for version 2.1 by embedding '{#' in an
   * action to cause a translation error.
   */
  public void implicitTldReservedName21Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldReservedName21.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: implicitTldDefaultJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:307
   * 
   * @test_Strategy: [ImplicitTldDefaultJspVersion] Show that the jsp version of
   * an implicit tag library defaults to 2.0 by embedding an unescaped '#{"
   * character sequence in template text.
   */
  public void implicitTldDefaultJspVersionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldDefaultJspVersion.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

}
