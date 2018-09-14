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
package com.sun.ts.tests.jsf.api.javax_faces.application.resourcehandlerEx;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.Formatter;

public final class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_resourcehandlerEx_web";

  private static final String NOT_FOUND = "404 Not Found";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();

    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /**
   * @testName: resourceHandlerExcludePropertiesTest
   * @assertion_ids: JSF:JAVADOC:284
   * @test_Strategy: Validate that a .properties(RESOURCE_EXCLUDES_DEFAULT_VALUE
   *                 constant) files when package as a resource returns a 404
   *                 when called.
   * 
   * @since 2.0
   */
  public void resourceHandlerExcludePropertiesTest() throws Fault {
    HtmlPage page = getPage(
        CONTEXT_ROOT + "/faces/resourceHandlerExcludeTest.jsp");
    HtmlAnchor outputLink = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
        "properties");
    this.testLink(outputLink);
  }

  /**
   * @testName: resourceHandlerExcludeClassTest
   * @assertion_ids: JSF:JAVADOC:284
   * @test_Strategy: Validate that a .class(RESOURCE_EXCLUDES_DEFAULT_VALUE
   *                 constant) files when package as a resource returns a 404
   *                 when called.
   * 
   * @since 2.0
   */
  public void resourceHandlerExcludeClassTest() throws Fault {
    HtmlPage page = getPage(
        CONTEXT_ROOT + "/faces/resourceHandlerExcludeTest.jsp");
    HtmlAnchor outputLink = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
        "class");
    this.testLink(outputLink);
  }

  /**
   * @testName: resourceHandlerExcludeJSPXTest
   * @assertion_ids: JSF:JAVADOC:284
   * @test_Strategy: Validate that a .jspx(RESOURCE_EXCLUDES_DEFAULT_VALUE
   *                 constant) files when package as a resource returns a 404
   *                 when called.
   * 
   * @since 2.0
   */
  public void resourceHandlerExcludeJSPXTest() throws Fault {
    HtmlPage page = getPage(
        CONTEXT_ROOT + "/faces/resourceHandlerExcludeTest.jsp");
    HtmlAnchor outputLink = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
        "jspx");
    this.testLink(outputLink);
  }

  /**
   * @testName: resourceHandlerExcludeJSPTest
   * @assertion_ids: JSF:JAVADOC:284
   * @test_Strategy: Validate that a .jsp(RESOURCE_EXCLUDES_DEFAULT_VALUE
   *                 constant) files when package as a resource returns a 404
   *                 when called.
   * 
   * @since 2.0
   */
  public void resourceHandlerExcludeJSPTest() throws Fault {
    HtmlPage page = getPage(
        CONTEXT_ROOT + "/faces/resourceHandlerExcludeTest.jsp");
    HtmlAnchor outputLink = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
        "jsp");
    this.testLink(outputLink);
  }

  /**
   * @testName: resourceHandlerExcludeXHTMLTest
   * @assertion_ids: JSF:JAVADOC:284
   * @test_Strategy: Validate that a .xhtml(RESOURCE_EXCLUDES_DEFAULT_VALUE
   *                 constant) files when package as a resource returns a 404
   *                 when called.
   * 
   * @since 2.0
   */
  public void resourceHandlerExcludeXHTMLTest() throws Fault {
    HtmlPage page = getPage(
        CONTEXT_ROOT + "/faces/resourceHandlerExcludeTest.jsp");
    HtmlAnchor outputLink = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
        "xhtml");
    this.testLink(outputLink);
  }

  // --------------------------------------------------------- private methods
  private void testLink(HtmlAnchor a) throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    try {
      a.click().getWebResponse().getStatusCode();

    } catch (Exception e) {
      if (e instanceof FailingHttpStatusCodeException) {
        String returned = e.getMessage();
        if (!returned.contains(NOT_FOUND)) {
          System.out.println("MESSAGE: " + e.getMessage());
          formatter.format("Unexpected return status code! %n"
              + "Expected: '%s'%n" + "Received: '%s'%n", NOT_FOUND, returned);
          handleTestStatus(messages);
          return;
        }

      } else {
        formatter.format("Unexpected Exception! %n" + e.toString());
        handleTestStatus(messages);
        return;
      }
    }
  }
} // end of URLClient
