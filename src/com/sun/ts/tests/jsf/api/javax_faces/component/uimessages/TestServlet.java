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
package com.sun.ts.tests.jsf.api.javax_faces.component.uimessages;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIMessage;
import javax.faces.component.UIMessages;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends BaseComponentTestServlet {

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendererType("javax.faces.Messages");
  }

  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   * 
   * @return a new {@link UIComponent} instance.
   */
  @Override
  protected UIComponentBase createComponent() {
    return new UIMessages();
  }

  // ----------------------------------------- UIMessage Specific Test Methods
  // UIMessage.{get,set}For
  public void uiMessagesGetSetForTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String myto = "mytestobject";
    UIMessages messages = (UIMessages) createComponent();
    messages.setFor(myto);

    if (!myto.equals(messages.getFor())) {
      out.println(JSFTestUtil.FAIL + " Expected result calling "
          + "UIMessages.setFor() or UIMessages.getFor()!" + JSFTestUtil.NL
          + "Expected: " + myto + JSFTestUtil.NL + "Received: "
          + messages.getFor());

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  public void uiMessagesIsSetRedisplayTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIMessages messages = (UIMessages) createComponent();
    boolean tf;

    // Check redisplay for default value.
    tf = messages.isRedisplay();
    if (!tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Default value returned from .isRedisplay()!"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + tf);
      return;
    }

    // Set value then check it again.
    messages.setRedisplay(false);
    tf = messages.isRedisplay();
    if (tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value returned from .isRedisplay() after "
          + "setting it with .setRedisplay()!" + JSFTestUtil.NL
          + "Expected: false" + JSFTestUtil.NL + "Received: " + tf);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // end uiMessagesIsSetRedisplayTest

  public void uiMessagesIsSetShowDetailTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIMessages messages = (UIMessages) createComponent();
    boolean tf;

    // Check redisplay for default value.
    tf = messages.isShowDetail();
    if (tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Default value returned from .isShowDetail()!"
          + JSFTestUtil.NL + "Expected: false" + JSFTestUtil.NL + "Received: "
          + tf);
      return;
    }

    // Set value then check it again.
    messages.setShowDetail(true);
    tf = messages.isShowDetail();
    if (!tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value returned from .isShowDetail() after "
          + "setting it with .setShowDetail()!" + JSFTestUtil.NL
          + "Expected: tue" + JSFTestUtil.NL + "Received: " + tf);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // end uiMessagesIsSetShowDetailTest

  public void uiMessagesIsSetShowSummaryTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIMessages messages = (UIMessages) createComponent();
    boolean tf;

    // Check redisplay for default value.
    tf = messages.isShowSummary();
    if (!tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Default value returned from .isShowSummary()!"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + tf);
      return;
    }

    // Set value then check it again.
    messages.setShowSummary(false);
    tf = messages.isShowSummary();
    if (tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value returned from .isShowSummary() after "
          + "setting it with .setShowSummary()!" + JSFTestUtil.NL
          + "Expected: false" + JSFTestUtil.NL + "Received: " + tf);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // end uiMessagesIsSetShowSummaryTest

  public void uiMessagesIsSetGlobalOnlyTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIMessages messages = (UIMessages) createComponent();
    boolean tf;

    // Check redisplay for default value.
    tf = messages.isGlobalOnly();
    if (tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Default value returned from .isGlobalOnly()!"
          + JSFTestUtil.NL + "Expected: false" + JSFTestUtil.NL + "Received: "
          + tf);
      return;
    }

    // Set value then check it again.
    messages.setGlobalOnly(true);
    tf = messages.isGlobalOnly();
    if (!tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value returned from .isShowSummary() after "
          + "setting it with .setGlobalOnly()!" + JSFTestUtil.NL
          + "Expected: true" + JSFTestUtil.NL + "Received: " + tf);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // end uiMessagesIsSetGlobalOnlyTest
}
