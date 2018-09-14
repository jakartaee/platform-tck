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

package com.sun.ts.tests.jsp.spec.core_syntax.scripting.declaration;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

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

    setGeneralURI("/jsp/spec/core_syntax/scripting/declaration");
    setContextRoot("/jsp_coresyntx_script_declaration_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveDeclarationTest
   * 
   * @assertion_ids: JSP:SPEC:74;PENDING
   * 
   * @test_Strategy: Validate the scripting declarations are properly
   * recognized, by declaring and assigning a value to an int variable, and
   * displaying the value of the variable (validate in both standard and XML
   * syntax.
   */

  public void positiveDeclarationTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveDeclaration");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_declaration_web/positiveDeclaration.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<root>Test PASSED</root>");
    invoke();
  }

}
