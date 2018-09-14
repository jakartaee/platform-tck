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
package com.sun.ts.tests.servlet.api.javax_servlet_http.readlistener;

import com.sun.javatest.Status;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;
import java.io.*;
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

    setContextRoot("/servlet_jsh_readlistener_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   * servlet_async_wait;
   */
  /* Run test */
  /*
   * @testName: nioInputTest
   *
   * @assertion_ids: Servlet:SPEC:282; Servlet:SPEC:282.1; Servlet:SPEC:282.2;
   * Servlet:SPEC:282.6; Servlet:SPEC:282.7; Servlet:JAVADOC:904;
   * Servlet:JAVADOC:905; Servlet:JAVADOC:908;
   * Servlet:JAVADOC:909;Servlet:JAVADOC:577; Servlet:JAVADOC:609;
   * Servlet:JAVADOC:638; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet TestServlet which supports async; Create a
   * ReadListener; From Client, sends two batch of messages use stream; Verify
   * all message received; Verify ReadListener works accordingly
   */
  public void nioInputTest() throws Fault {
    int sleepInSeconds = Integer
        .parseInt(_props.getProperty("servlet_async_wait").trim());
    Boolean passed = true;

    String EXPECTED_RESPONSE = "=onDataAvailable|=Hello|=onDataAvailable|=World"
        + "|=onAllDataRead";

    BufferedReader input = null;
    BufferedWriter output = null;

    String requestUrl = getContextRoot() + "/" + getServletName();
    URL url = null;

    try {
      TSURL ctsURL = new TSURL();
      url = ctsURL.getURL("http", _hostname, _port, requestUrl);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      TestUtil.logTrace("======= Connecting " + url.toExternalForm());
      conn.setRequestProperty("Content-type", "text/plain; charset=utf-8");
      conn.setChunkedStreamingMode(5);
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      TestUtil.logTrace("======= Header " + conn.toString());
      conn.connect();

      try {
        output = new BufferedWriter(
            new OutputStreamWriter(conn.getOutputStream()));
        try {
          String data = "Hello";
          output.write(data);
          output.flush();
          Thread.sleep(sleepInSeconds * 1000);

          data = "World";
          output.write(data);
          output.flush();
          output.close();
        } catch (Exception ex) {
          passed = false;
          TestUtil
              .logErr("======= Exception sending message: " + ex.getMessage());
        }

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

        try {
          if (output != null) {
            output.close();
          }
        } catch (Exception ex) {
          TestUtil.logErr("Fail to close BufferedWriter" + ex.getMessage());
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
