/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.javax_faces.application.navigationcase;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_navigationcase_web";

  private static final String VIEWID = "/faces/stop.xhtml";

  private static final String BUTTON_NAME = "Blue";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */
  /**
   * @testName: navigationCaseEqualsTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:231
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.equals().
   */
  public void navigationCaseEqualsTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationCaseEqualsTest");
    invoke();
  }

  /**
   * @testName: navigationCaseGetActionURLTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:232
   * @test_Strategy: Validate we get an absolute URL to this NavigationCase
   *                 instance.
   */
  public void navigationCaseGetActionURLTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationCaseGetActionURLTest");
    invoke();
  }

  /**
   * @testName: navigationCaseGetRedirectURLTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:242
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.getRedirect().
   */
  public void navigationCaseGetRedirectURLTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationCaseGetRedirectURLTest");
    invoke();
  }

  /**
   * @testName: navigationCaseGetBookmarkableURLTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:234
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.getBookmarkableURL().
   */
  public void navigationCaseGetBookmarkableURLTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationCaseGetBookmarkableURLTest");
    invoke();
  }

  /**
   * @testName: navigationCaseGetConditionTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:236
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.getCondition().
   */
  public void navigationCaseGetConditionTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationCaseGetConditionTest");
    invoke();
  }

  /**
   * @testName: navigationCaseHasConditionTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:247
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.hasCondition().
   */
  public void navigationCaseHasConditionTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationCaseHasConditionTest");
    invoke();
  }

  /**
   * @testName: navigationGetFromActionTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:238
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.getFromAction();
   */
  public void navigationGetFromActionTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationGetFromActionTest");
    invoke();
  }

  /**
   * @testName: navigationGetFromOutcomeTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:239
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.getFromOutcome();
   */
  public void navigationGetFromOutcomeTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationGetFromOutcomeTest");
    invoke();
  }

  /**
   * @testName: navigationGetFromViewIdTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:240
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.getFromViewId();
   */
  public void navigationGetFromViewIdTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationGetFromViewIdTest");
    invoke();
  }

  /**
   * @testName: navigationGetToViewIdTest
   * @assertion_ids: JSF:JAVADOC:246; JSF:JAVADOC:251; JSF:JAVADOC:240
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.getFromViewId();
   */
  public void navigationGetToViewIdTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationGetToViewIdTest");
    invoke();
  }

  /**
   * @testName: navigationIsRedirectTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:250
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.isRedirect();
   */
  public void navigationIsRedirectTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationIsRedirectTest");
    invoke();
  }

  /**
   * @testName: navigationIsIncludeViewParamsTest
   * @assertion_ids: JSF:JAVADOC:253; JSF:JAVADOC:251; JSF:JAVADOC:249
   * @test_Strategy: Validate we can add a NavigationCase to the
   *                 NavigationHandler's Map and retrieve the correct value back
   *                 from NavigationCase.isIncludeViewParams();
   */
  public void navigationIsIncludeViewParamsTest() throws Fault {
    this.preConfig(VIEWID, BUTTON_NAME);

    TEST_PROPS.setProperty(APITEST, "navigationIsIncludeViewParamsTest");
    invoke();
  }

  // --------------------- private/protected methods -------------

  private void preConfig(String viewRoot, String buttonId) throws Fault {
    BaseHtmlClientWrapper bw = new BaseHtmlClientWrapper();
    HtmlPage page;
    HtmlSubmitInput button;

    try {
      page = (HtmlPage) new WebClient().getPage(
          "http://" + _hostname + ":" + _port + CONTEXT_ROOT + viewRoot);

      button = (HtmlSubmitInput) bw.getInputIncludingId(page, buttonId);
      button.click();

    } catch (Exception e) {
      throw new Fault(e);
    }
  }

  // ----------------------------- Inner Classes ----------------------

  protected class BaseHtmlClientWrapper extends BaseHtmlUnitClient {

    private static final long serialVersionUID = 1L;

    protected HtmlInput getInputIncludingId(HtmlPage root, String id) {
      return super.getInputIncludingId(root, id);
    }
  }

} // end of URLClient
