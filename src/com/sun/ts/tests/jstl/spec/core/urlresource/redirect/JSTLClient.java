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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.spec.core.urlresource.redirect;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

public class JSTLClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /** Creates new JSTLClient */
  public JSTLClient() {
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JSTLClient theTests = new JSTLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jstl_core_url_redirect_web");
    setGoldenFileDir("/jstl/spec/core/urlresource/redirect");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveRedirectTest
   * 
   * @assertion_ids: JSTL:SPEC:43; JSTL:SPEC:43.1; JSTL:SPEC:43.1.1
   * 
   * @testStrategy: Validate that the action can properly redirect when the url
   * attribute is provided either a dynamic or static values.
   */
  public void positiveRedirectTest() throws Fault {

    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt1=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_core_url_redirect_web/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt2=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_core_url_redirect_web/urlresource/param/import.jsp");
    invoke();

    // rel
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt3=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_core_url_redirect_web/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt4=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_core_url_redirect_web/import.jsp");
    invoke();

    // foreign context
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt5=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_core_web/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt6=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_core_web/urlresource/param/import.jsp");
    invoke();

  }

  /*
   * @testName: positiveRedirectParamsBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:43; JSTL:SPEC:43.1
   * 
   * @testStrategy: Validate that the action can properly redirect when the the
   * body content consists of param subtags. The params should be added to the
   * redirect URI.
   */
  public void positiveRedirectParamsBodyTest() throws Fault {

    /* RT */
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectParamsBodyTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST,
        "positiveRedirectParamsBodyTest.jsp?rt1=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port
        + "/jstl_core_url_redirect_web/urlresource/param/import.jsp?testparm=testval");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectParamsTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST,
        "positiveRedirectParamsBodyTest.jsp?rt2=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_core_web/urlresource/param/import.jsp?testparm=testval");
    invoke();
  }

  /*
   * @testName: negativeRedirectExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:43.5
   * 
   * @testStrategy: Validate that if the body content of the action causes an
   * exception that it is properly propagated.
   */
  public void negativeRedirectExcBodyContentTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeRedirectExcBodyContentTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeRedirectExcBodyContentTest.jsp?el=true");
    TEST_PROPS.setProperty(GOLDENFILE, "negativeRedirectExcBodyContentTest.gf");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "negativeRedirectExcBodyContentTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeRedirectExcBodyContentTest.jsp?rt=true");
    TEST_PROPS.setProperty(GOLDENFILE, "negativeRedirectExcBodyContentTest.gf");
    invoke();
  }

  /*
   * @testName: negativeRedirectContextUrlInvalidValueTest
   * 
   * @assertion_ids: JSTL:SPEC:43.6.3; JSTL:SPEC:43.6.4
   * 
   * @testStrategy: Validate that if the context attribute is specified, and
   * either context or url are provided values that don't start with a leading
   * slash, an exception occurs.
   */
  public void negativeRedirectContextUrlInvalidValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "negativeRedirectContextUrlInvalidValueTest");
    invoke();
  }
}
