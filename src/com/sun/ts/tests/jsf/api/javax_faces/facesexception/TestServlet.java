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

/*
 * $Id$
 */
package com.sun.ts.tests.jsf.api.javax_faces.facesexception;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.FacesException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpTCKServlet {

  public void facesExceptionCtor1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesException fe = new FacesException();
    if (fe == null) {
      pw.println("Test FAILED: FacesException() returned null.");
    } else {
      pw.println(JSFTestUtil.PASS);
    }
  }

  public void facesExceptionCtor2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    String msg = "This is a test message";
    FacesException fe = new FacesException(msg);
    if (fe != null) {
      if (msg.equals(fe.getMessage().trim())) {
        pw.println(JSFTestUtil.PASS);
      } else {
        pw.println(
            JSFTestUtil.FAIL + " Exception message was not set" + " properly.");
        pw.println("Expected: " + msg);
        pw.println("Received: " + fe.getMessage());
      }
    } else {
      pw.println("Test FAILED: FacesException(String) returned null.");
    }
  }

  public void facesExceptionCtor3Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    ServletException se = new ServletException();

    FacesException fe = new FacesException(se);
    if (fe != null) {
      Throwable t = fe.getCause();
      if (t != null) {
        if (t instanceof ServletException) {
          pw.println(JSFTestUtil.PASS);
        } else {
          pw.println(JSFTestUtil.FAIL + " FacesException.getCause() "
              + "returned " + "unexpected exception type.");
          pw.println("Excpected: javax.servlet.ServletException");
          pw.println("Received: " + t.getClass().getName());
        }
      } else {
        pw.println(
            JSFTestUtil.FAIL + " FacesException.getCause() return " + "null.");
      }
    } else {
      pw.println(JSFTestUtil.FAIL + " FacesException(Throwable) return null.");
    }

  }

  public void facesExceptionCtor4Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    String msg = "This is a test message";
    ServletException se = new ServletException();
    FacesException fe = new FacesException(msg, se);
    if (fe != null) {
      Throwable t = fe.getCause();
      if (t != null) {
        if (t instanceof ServletException) {
          if (msg.equals(fe.getMessage().trim())) {
            pw.println(JSFTestUtil.PASS);
          } else {
            pw.println(JSFTestUtil.FAIL + " Exception message was not"
                + " set properly.");
            pw.println("Expected: " + msg);
            pw.println("Received: " + fe.getMessage());
          }
        } else {
          pw.println(JSFTestUtil.FAIL + " FacesException.getCause() "
              + "returned " + "unexpected exception type.");
          pw.println("Excpected: javax.servlet.ServletException");
          pw.println("Received: " + t.getClass().getName());
        }
      } else {
        pw.println(
            JSFTestUtil.FAIL + " FacesException.getCause() return " + "null.");
      }
    } else {
      pw.println(JSFTestUtil.FAIL + " FacesException(Throwable) return null.");
    }
  }

  public void facesExceptionGetCauseTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesException fe = new FacesException(new ServletException());
    if (fe != null) {
      Throwable t = fe.getCause();
      if (t != null) {
        if (t instanceof ServletException) {
          pw.println(JSFTestUtil.PASS);
        } else {
          pw.println(JSFTestUtil.FAIL + " FacesException.getCause() "
              + "returned unexepected Exception type.");
          pw.println("Expected: javax.servlet.ServletException");
          pw.println("Received: " + t.getClass().getName());
        }
      } else {
        pw.println(
            JSFTestUtil.FAIL + " FacesException.getCause() returned null.");
      }
    } else {
      pw.println(JSFTestUtil.FAIL + " FacesException(Throwable) return null.");
    }
  }
}
