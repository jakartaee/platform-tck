/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.application.viewexpiredex;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.application.ViewExpiredException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    config.getServletContext();
  }

  public void destroy() {
    super.destroy();
  }

  // ------------------------------------------------------------------- Tests

  public void viewExpiredExceptionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Throwable tckException = new TCKException();

    // ViewExpiredException(java.lang.String viewId)
    ViewExpiredException vOne = new ViewExpiredException("Geddy");
    if (this.checkViewId(vOne, "Geddy", out)) {
      // do nothing test passed.

    } else {
      return;

    }

    // ViewExpiredException(java.lang.String message,
    // java.lang.String viewId)
    ViewExpiredException vTwo = new ViewExpiredException("Vocals", "Geddy");
    if (this.checkViewId(vTwo, "Geddy", out)
        && this.checkMessage(vTwo, "Vocals", out)) {
      // do nothing test passed.

    } else {
      return;
    }

    // ViewExpiredException(java.lang.Throwable cause,
    // java.lang.String viewId)
    ViewExpiredException vThree = new ViewExpiredException(tckException,
        "Geddy");

    if (this.checkViewId(vThree, "Geddy", out)
        && this.checkCause(vThree, "TCKException", out)) {
      // do nothing test passed.

    } else {
      return;
    }

    // ViewExpiredException(java.lang.String message,
    // java.lang.Throwable cause,
    // java.lang.String viewId)
    ViewExpiredException vFour = new ViewExpiredException("Vocals",
        tckException, "Geddy");

    if (this.checkViewId(vFour, "Geddy", out)
        && this.checkMessage(vFour, "Vocals", out)
        && this.checkCause(vFour, "TCKException", out)) {
      // do nothing test passed.

    } else {
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ----------------------------------------------------------- private
  // methods

  private Boolean checkViewId(ViewExpiredException vee, String expectedId,
      PrintWriter out) {
    String resultViewId = vee.getViewId();
    Boolean result = true;

    if (resultViewId == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ViewExpiredException.getViewId() returned null when "
          + "not expected too!");
      result = false;

    } else if (!resultViewId.contains(expectedId)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ViewId does not contain initially set viewId!" + JSFTestUtil.NL
          + "Expected: " + expectedId + JSFTestUtil.NL + "Received: "
          + resultViewId);
      result = false;
    }

    return result;
  }

  private Boolean checkMessage(ViewExpiredException vee, String expectedMess,
      PrintWriter out) {
    String resultMess = vee.getMessage();
    Boolean result = true;

    if (resultMess == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ViewExpiredException.getMessage() returned null when "
          + "not expected too!");
      result = false;

    } else if (!resultMess.contains(expectedMess)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Message does not contain initially set message!" + JSFTestUtil.NL
          + "Expected: " + expectedMess + JSFTestUtil.NL + "Received: "
          + resultMess);
      result = false;

    }

    return result;
  }

  private Boolean checkCause(ViewExpiredException vee, String expectedCause,
      PrintWriter out) {
    String resultCause = vee.getCause().getClass().getSimpleName();
    Boolean result = true;

    if (resultCause == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ViewExpiredException.getCause() returned null when "
          + "not expected too!");
      result = false;

    }
    if (!resultCause.contains(expectedCause)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Cause does not contain Initially set cause!" + JSFTestUtil.NL
          + "Expected: " + expectedCause + JSFTestUtil.NL + "Received: "
          + resultCause);
      result = false;

    }

    return result;
  }

  // ----------------------------------------------------------- private
  // classes

  private class TCKException extends Throwable {
    // this class does not thing other then server as a none SE Exception
    // for this testcase.

  }

} // End TestServlet
