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

package com.sun.ts.tests.jsf.api.javax_faces.event.abortprocessingexception;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.event.AbortProcessingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpTCKServlet {

  public void abortProcessingExceptionNoArgCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    // No-Arg constructor test
    try {
      throw new AbortProcessingException();

    } catch (AbortProcessingException ape) {
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected Exception thrownl");
      e.printStackTrace();
    }
  }

  public void abortProcessingExceptionCtor01Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    // constructor that takes a message as an argument test
    String message = "Test Message";
    AbortProcessingException ape = new AbortProcessingException(message);

    if (!message.equals(ape.getMessage())) {
      pw.println(JSFTestUtil.FAIL
          + ": AbortProcessingException() did not return the correct "
          + "message" + JSFTestUtil.NL + "Expected: " + message + JSFTestUtil.NL
          + "Recieved: " + ape.getMessage());
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  public void abortProcessingExceptionCtor02Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    // constructor that takes Throwable as an argument test
    String message = "Test Message";
    Throwable cause = new Throwable(message);
    AbortProcessingException ape = new AbortProcessingException(cause);

    if (!(cause.equals(ape.getCause()))) {
      pw.println(JSFTestUtil.FAIL
          + ": AbortProcessingException() did not return the correct cause");
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  public void abortProcessingExceptionCtor03Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    // constructor that takes Throwable and a message as an argument test
    String message = "Test Message";
    Throwable cause = new Throwable(message);
    AbortProcessingException ape = new AbortProcessingException(message, cause);

    if ((!(message.equals(ape.getMessage())))
        && (!(cause.equals(ape.getCause())))) {

      pw.println(
          JSFTestUtil.FAIL + ": AbortProcessingException() did not return the "
              + "correct cause or message");
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

}
