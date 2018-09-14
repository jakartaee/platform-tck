/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.javax_servlet_http.writelistener;

import com.sun.javatest.Status;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

    setContextRoot("/servlet_jsh_writelistener_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */
  /*
   * @testName: nioOutputTest
   *
   * @assertion_ids: Servlet:JAVADOC:911; Servlet:JAVADOC:916;
   * Servlet:JAVADOC:917; Servlet:JAVADOC:582; Servlet:JAVADOC:609;
   *
   * @test_Strategy: Create a Servlet TestServlet which supports async; Create a
   * Writeistener; From Servlet, sends one batch of messages use stream; Verify
   * all message received by client; Verify WriteListener works accordingly
   */
  public void nioOutputTest() throws Fault {
    Boolean passed = true;
    String testName = "nioOutputTest";
    String EXPECTED_RESPONSE = "=onWritePossible";

    BufferedReader input = null;

    String requestUrl = getContextRoot() + "/" + getServletName() + "?testname="
        + testName;
    URL url = null;

    try {
      TSURL ctsURL = new TSURL();
      url = ctsURL.getURL("http", _hostname, _port, requestUrl);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      TestUtil.logTrace("======= Connecting " + url.toExternalForm());
      conn.setChunkedStreamingMode(5);
      conn.setDoOutput(true);
      TestUtil.logTrace("======= Header " + conn.toString());
      conn.connect();

      try {
        input = new BufferedReader(
            new InputStreamReader(conn.getInputStream()));
        String line = null;
        StringBuffer message_received = new StringBuffer();

        while ((line = input.readLine()) != null) {
          TestUtil.logTrace("======= message received: " + line);
          message_received.append(line);
        }
        passed = ServletTestUtil.compareString(EXPECTED_RESPONSE,
            message_received.toString());

      } catch (Exception ex) {
        passed = false;
        TestUtil.logErr("Exception: " + ex.getMessage());
      } finally {
        try {
          if (input != null) {
            input.close();
          }
        } catch (Exception ex) {
          TestUtil.logErr("Fail to close BufferedReader" + ex.getMessage());
        }
      }
    } catch (Exception ex3) {
      passed = false;
      TestUtil.logErr("Test" + ex3.getMessage());
    }

    if (!passed) {
      throw new Fault("Test Failed.");
    }
  }
}
