/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.servlet.compat.LeadingSlash.WithoutLeadingSlash;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

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

    setGeneralURI("/servlet/compat/LeadingSlash/WithoutLeadingSlash");
    setContextRoot("/servlet_compat_LeadingSlash_Without_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: WithoutLeadingSlashTest
   * 
   * @assertion: A 2.2 web application deployment descriptor who's url mapping
   * does not begin with a "/" can be deployed in a 2.3 environment, specified
   * in the Java Servlet Pages Specification v2.3, Sec 11
   * 
   * @test_Strategy: The DD url-pattern has a "/" at the beginning of the
   * string. The web app should deploy and be able to be called by a client
   *
   */

  public void WithoutLeadingSlashTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "WithoutLeadingSlashTest");
    invoke();
  }
}
