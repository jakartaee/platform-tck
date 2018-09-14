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

package com.sun.ts.tests.jstl.spec.xml.xmlcore.bindings;

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

    setContextRoot("/jstl_xml_bindings_web");
    setGoldenFileDir("/jstl/spec/xml/xmlcore/bindings");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveXPathVariableBindingsTest
   * 
   * @assertion_ids: JSTL:SPEC:64; JSTL:SPEC:64.1; JSTL:SPEC:64.2;
   * JSTL:SPEC:64.3; JSTL:SPEC:64.4; JSTL:SPEC:64.5; JSTL:SPEC:64.6;
   * JSTL:SPEC:64.7; JSTL:SPEC:64.8; JSTL:SPEC:64.9
   * 
   * @testStrategy: Validate the following bindings are available:
   *
   * $foo - pageContext.findAttribute("foo") $param.foo -
   * request.getParameter("foo") $header:foo - request.getHeader("foo")
   * $initParam:foo - application.getInitParamter("foo") $cooke:foo - maps to
   * the cookies value for name foo $pageScope:foo -
   * pageContext.getAttribute("foo", PageContext.PAGE_SCOPE) $requestScope:foo -
   * pageContext.getAttribute("foo", PageContext.REQUEST_SCOPE)
   * $sessionScope:foo - pageContext.getAttribute("foo",
   * PageContext.SESSION_SCOPE) $applicationScope:foo -
   * pageContext.getAttribute("foo", PageContext.APPLICATION_SCOPE)
   */
  public void positiveXPathVariableBindingsTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveXPathVariableBindingsTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveXPathVariableBindingsTest.jsp?param1=RequestParameter1");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "reqheader:RequestHeader|Cookie: $Version=1; mycookie=CookieFound; $Domain="
            + _hostname + "; $Path=/jstl_xml_bindings_web");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveXPathVariableBindingsTest.gf");
    invoke();
  }
}
