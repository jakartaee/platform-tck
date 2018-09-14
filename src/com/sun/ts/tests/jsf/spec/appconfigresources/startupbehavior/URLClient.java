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
package com.sun.ts.tests.jsf.spec.appconfigresources.startupbehavior;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_appconfigresources_startupbehavior_web";

  private static final String SPAN = "span";

  private static final String NL = System.getProperty("line.separator", "\n");

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  @Override
  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /**
   * @testName: applicationConfigurationfilesTest1
   * @assertion_ids: PENDING
   * @test_Strategy: Test to make sure that all configuration files that match
   *                 either META-INF/faces-config.xml or the end with
   *                 .faces-config.xml directly in the META-INF directory. Each
   *                 resource that matches that expression must be considered an
   *                 application configuration resource.
   * 
   * @since 2.0
   */
  public void applicationConfigurationfilesTest1() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/appconfig_01.xhtml");

    // -------------------------------------------------------------- case 1
    HtmlSpan answer = (HtmlSpan) getElementOfTypeIncludingId(page, SPAN,
        "answer");

    if (!validateExistence("answer", SPAN, answer, formatter)) {
      handleTestStatus(messages);
      return;
    }

    HtmlSpan status = (HtmlSpan) getElementOfTypeIncludingId(page, SPAN,
        "status");

    if (!validateExistence("status", SPAN, status, formatter)) {
      handleTestStatus(messages);
      return;
    }

    HtmlSpan color = (HtmlSpan) getElementOfTypeIncludingId(page, SPAN,
        "color");

    if (!validateExistence("color", SPAN, color, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(answer, "YES", formatter);
    validateElementValue(status, "Ready!", formatter);
    validateElementValue(color, "red", formatter);

    if (messages.length() != 0) {
      formatter.format("Failure due to one of the following ManagedBeans were "
          + "not found! Using the applicationConfigurationResources." + NL
          + "AnswerYesBean.class - referenced in " + "META-INF/faces-config.xml"
          + NL + "StatBean.class - referenced in "
          + "META-INF/statBean.faces-config.xml" + NL
          + "ColorRedBean.class - referenced in "
          + "META-INF/colorBean.faces-config.xml");
    }

    handleTestStatus(messages);
  } // END applicationConfigurationfilesTest1

  /**
   * @testName: applicationConfigurationfilesTest2
   * @assertion_ids: PENDING
   * @test_Strategy: Test to make sure that javax.faces.CONFIG_FILES property
   *                 exists and treat it as a comma-delimited list of context
   *                 relative resource paths (starting with a /), and add each
   *                 of the specfied resources to the list.
   * @since 2.0
   */
  public void applicationConfigurationfilesTest2() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage pageTwo = getPage(CONTEXT_ROOT + "/faces/appconfig_02.xhtml");

    // -------------------------------------------------------------- case 2
    HtmlSpan question = (HtmlSpan) getElementOfTypeIncludingId(pageTwo, SPAN,
        "question");

    if (!validateExistence("question", SPAN, question, formatter)) {
      handleTestStatus(messages);
      return;
    }

    HtmlSpan ball = (HtmlSpan) getElementOfTypeIncludingId(pageTwo, SPAN,
        "ball");

    if (!validateExistence("ball", SPAN, ball, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(question, "NO", formatter);
    validateElementValue(ball, "blue", formatter);

    if (messages.length() != 0) {
      formatter.format("Failure due to one of the following ManagedBeans were "
          + "not found! Using the applicationConfigurationResources." + NL
          + "AnswerNoBean.class - referenced in webroot/"
          + "META-INF/testCase2-config.xml" + NL
          + "ColorBlueBean.class - referenced in webroot/"
          + "META-INF/testCase2-config_1.xml");
    }

    handleTestStatus(messages);
  } // END applicationConfigurationfilesTest2
} // END URLClient
