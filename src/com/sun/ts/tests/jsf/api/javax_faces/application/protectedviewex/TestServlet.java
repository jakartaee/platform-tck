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

package com.sun.ts.tests.jsf.api.javax_faces.application.protectedviewex;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.application.ProtectedViewException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  // ------------------------------------------------------------------- Tests

  public void protectViewExceptionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Throwable tckException = new TCKException();

    // protectedViewException(java.lang.String message)
    ProtectedViewException exOne = new ProtectedViewException("Vocals");

    if (!(this.checkMessage(exOne, "Vocals", out))) {
      return;
    }

    // ProtectedViewException(java.lang.String message,
    // java.lang.Throwable cause)
    ProtectedViewException exTwo = new ProtectedViewException("Vocals",
        tckException);

    if (!(this.checkMessage(exOne, "Vocals", out)
        && this.checkCause(exTwo, "TCKException", out))) {
      return;
    }

    // ProtectedViewException(java.lang.Throwable rootCause)
    ProtectedViewException exThree = new ProtectedViewException(tckException);

    if (!(this.checkCause(exThree, "TCKException", out))) {
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ---------------------------------------------- private methods

  private Boolean checkMessage(ProtectedViewException vee, String expectedMess,
      PrintWriter out) {
    String resultMess = vee.getMessage();

    if (resultMess == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ViewExpiredException.getMessage() returned null when "
          + "not expected too!");

      return false;
    }

    if (!resultMess.contains(expectedMess)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Message does not contain initially set message!" + JSFTestUtil.NL
          + "Expected: " + expectedMess + JSFTestUtil.NL + "Received: "
          + resultMess);

      return false;
    }

    return true;
  }

  private Boolean checkCause(ProtectedViewException vee, String expectedCause,
      PrintWriter out) {
    String resultCause = vee.getCause().getClass().getSimpleName();

    if (resultCause == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ViewExpiredException.getCause() returned null when "
          + "not expected too!");
      return false;

    }

    if (!resultCause.contains(expectedCause)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Cause does not contain Initially set cause!" + JSFTestUtil.NL
          + "Expected: " + expectedCause + JSFTestUtil.NL + "Received: "
          + resultCause);
      return false;

    }

    return true;
  }

  // ----------------------------------------------------------- private
  // classes

  private class TCKException extends Throwable {
    // this class does not thing other then server as a none SE Exception
    // for this testcase.

  }

} // End TestServlet
