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

package com.sun.ts.tests.concurrency.spec.ContextService.contextPropagate;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class Client extends EETest {
  private String host = null;

  private int port;

  private Properties testProps; // Test properties passed via harness code.
                                // Contents of ts.jte file

  private static final String contextPath = "/ContextPropagate_web";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home; all.props; all
   * properties;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      // get props
      port = Integer.parseInt(p.getProperty("webServerPort"));
      host = p.getProperty("webServerHost");

      // check props for errors
      if (port < 1) {
        throw new Exception("'port' in ts.jte must be > 0");
      }
      if (host == null) {
        throw new Exception("'host' in ts.jte must not be null ");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }

    this.testProps = p;

  }

  /*
   * @testName: testJNDIContextAndCreateProxyInServlet
   *
   * @assertion_ids:
   * CONCURRENCY:SPEC:85;CONCURRENCY:SPEC:76;CONCURRENCY:SPEC:76.1;
   * CONCURRENCY:SPEC:76.2;CONCURRENCY:SPEC:76.3;CONCURRENCY:SPEC:77;
   * CONCURRENCY:SPEC:84;CONCURRENCY:SPEC:2;CONCURRENCY:SPEC:4.1;
   *
   * @test_Strategy: create proxy in servlet and pass it into ejb container,
   * then verify JNDI Context.
   *
   */

  public void testJNDIContextAndCreateProxyInServlet() throws Fault {
    URL url;
    String resp = null;
    try {
      url = new URL("http://" + host + ":" + port + contextPath
          + "/JNDIServlet?action=createProxyInServlet");
      resp = TestUtil.getResponse(url.openConnection());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("++ get response: " + resp);
    if (!"JNDIContextWeb".equals(resp.trim())) {
      throw new Fault(
          "testJNDIContextAndCreateProxyInServlet fail to get correct result.");
    }
  }

  /*
   * @testName: testJNDIContextAndCreateProxyInEJB
   *
   * @assertion_ids:
   * CONCURRENCY:SPEC:85;CONCURRENCY:SPEC:76;CONCURRENCY:SPEC:76.1;
   * CONCURRENCY:SPEC:76.2;CONCURRENCY:SPEC:76.3;CONCURRENCY:SPEC:77;
   * CONCURRENCY:SPEC:84;CONCURRENCY:SPEC:3;CONCURRENCY:SPEC:3.1;
   * CONCURRENCY:SPEC:3.2;CONCURRENCY:SPEC:3.3;CONCURRENCY:SPEC:3.4;
   * CONCURRENCY:SPEC:4;
   *
   * @test_Strategy: create proxy in servlet and pass it into ejb container,
   * then verify JNDI Context.
   *
   */
  public void testJNDIContextAndCreateProxyInEJB() throws Fault {
    URL url;
    String resp = null;
    try {
      url = new URL("http://" + host + ":" + port + contextPath
          + "/JNDIServlet?action=createProxyInEJB");
      resp = TestUtil.getResponse(url.openConnection());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("++ get response: " + resp);
    if (!"JNDIContextEJB".equals(resp.trim())) {
      throw new Fault(
          "testJNDIContextAndCreateProxyInEJB fail to get correct result.");
    }
  }

  /*
   * @testName: testClassloaderAndCreateProxyInServlet
   *
   * @assertion_ids:
   * CONCURRENCY:SPEC:85;CONCURRENCY:SPEC:76;CONCURRENCY:SPEC:76.1;
   * CONCURRENCY:SPEC:76.2;CONCURRENCY:SPEC:76.3;CONCURRENCY:SPEC:77;
   * CONCURRENCY:SPEC:84;CONCURRENCY:SPEC:4.2;CONCURRENCY:SPEC:4.4;
   *
   * @test_Strategy: create proxy in servlet and pass it into ejb container,
   * then verify classloader.
   *
   */
  public void testClassloaderAndCreateProxyInServlet() throws Fault {
    URL url;
    String resp = null;
    try {
      url = new URL(
          "http://" + host + ":" + port + contextPath + "/ClassloaderServlet");
      resp = TestUtil.getResponse(url.openConnection());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("++ get response: " + resp);
    if (!"success".equals(resp.trim())) {
      throw new Fault(
          "testClassloaderAndCreateProxyInServlet fail to get correct result.");
    }
  }

  /*
   * @testName: testSecurityAndCreateProxyInServlet
   *
   * @assertion_ids:
   * CONCURRENCY:SPEC:85;CONCURRENCY:SPEC:76;CONCURRENCY:SPEC:76.1;
   * CONCURRENCY:SPEC:76.2;CONCURRENCY:SPEC:76.3;CONCURRENCY:SPEC:77;
   * CONCURRENCY:SPEC:84;CONCURRENCY:SPEC:4.3;CONCURRENCY:SPEC:4.4;
   * CONCURRENCY:SPEC:4.4;
   *
   * @test_Strategy: create proxy in servlet and pass it into ejb container,
   * then verify permission.
   *
   */
  public void testSecurityAndCreateProxyInServlet() throws Fault {
    URL url;
    String resp = null;
    try {
      url = new URL(
          "http://" + host + ":" + port + contextPath + "/SecurityServlet");
      resp = TestUtil.getResponse(url.openConnection());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("++ get response: " + resp);
    if (!"success".equals(resp.trim())) {
      throw new Fault(
          "testSecurityAndCreateProxyInServlet fail to get correct result.");
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("test cleanup ok");
  }
}
