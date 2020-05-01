/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.javax_faces.component.uimessage;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.UIMessage;
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
    setRendererType("jakarta.faces.Message");
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
    return new UIMessage();
  }

  // ----------------------------------------- UIMessage Specific Test Methods
  // UIMessage.{get,set}For
  public void uiMessageGetSetForTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String myto = "mytestobject";
    UIMessage message = (UIMessage) createComponent();
    message.setFor(myto);

    if (!myto.equals(message.getFor())) {
      out.println(JSFTestUtil.FAIL + " Expected result calling "
          + "UIMessage.setFor() or UIMessage.getFor()!" + JSFTestUtil.NL
          + "Expected: " + myto + JSFTestUtil.NL + "Received: "
          + message.getFor());

    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // end uiMessageGetSetForTest

  public void uiMessageIsSetRedisplayTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIMessage message = (UIMessage) createComponent();
    boolean tf;

    // Check redisplay for default value.
    tf = message.isRedisplay();
    if (!tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Default value returned from .isRedisplay()!"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + tf);
      return;
    }

    // Set value then check it again.
    message.setRedisplay(false);
    tf = message.isRedisplay();
    if (tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value returned from .isRedisplay() after "
          + "setting it with .setRedisplay()!" + JSFTestUtil.NL
          + "Expected: false" + JSFTestUtil.NL + "Received: " + tf);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // end uiMessageIsSetRedisplayTest

  public void uiMessageIsSetShowDetailTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIMessage message = (UIMessage) createComponent();
    boolean tf;

    // Check redisplay for default value.
    tf = message.isShowDetail();
    if (!tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Default value returned from .isShowDetail()!"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + tf);
      return;
    }

    // Set value then check it again.
    message.setShowDetail(false);
    tf = message.isShowDetail();
    if (tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value returned from .isShowDetail() after "
          + "setting it with .setShowDetail()!" + JSFTestUtil.NL
          + "Expected: false" + JSFTestUtil.NL + "Received: " + tf);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // end uiMessageIsSetShowDetailTest

  public void uiMessageIsSetShowSummaryTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIMessage message = (UIMessage) createComponent();
    boolean tf;

    // Check redisplay for default value.
    tf = message.isShowSummary();
    if (tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Default value returned from .isShowSummary()!"
          + JSFTestUtil.NL + "Expected: false" + JSFTestUtil.NL + "Received: "
          + tf);
      return;
    }

    // Set value then check it again.
    message.setShowSummary(true);
    tf = message.isShowSummary();
    if (!tf) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value returned from .isShowSummary() after "
          + "setting it with .setShowSummary()!" + JSFTestUtil.NL
          + "Expected: true" + JSFTestUtil.NL + "Received: " + tf);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // end uiMessageIsSetShowSummaryTest
}
