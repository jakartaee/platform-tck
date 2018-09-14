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
package com.sun.ts.tests.jsf.api.javax_faces.application.facesmessage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.application.FacesMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TestServlet extends HttpTCKServlet {

  // ------------------------------------------------ Test Declarations -----
  // No-arg constructor
  public void facesMessageNoArgCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    try {
      new FacesMessage();
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexected Exception creating new "
          + "FacesMessage instance.");
      e.printStackTrace();
    }
  }

  // arg constructor
  public void facesMessageCtor01Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    try {
      new FacesMessage(FacesMessage.SEVERITY_INFO, "summary", "detail");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown by "
          + "constructor.");
      e.printStackTrace();
    }
  }

  // FacesMessage(String summary)
  public void facesMessageCtorSumTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    try {
      new FacesMessage("summary");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown by "
          + "constructor.");
      e.printStackTrace();
    }
  }

  // FacesMessage(String summary, String detail)
  public void facesMessageCtorSumDetTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    try {
      new FacesMessage("summary", "detail");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown by "
          + "constructor.");
      e.printStackTrace();
    }
  }

  // FacesMessage.getServerity()
  // FacesMessage.setSeverity(int)
  public void facesMessageGetSetSeverityTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
        "summary", "detail");
    // first, call getSeverity on an instance that was constructed passing
    // the severity information in
    if (facesMessage.getSeverity().getOrdinal() != FacesMessage.SEVERITY_INFO
        .getOrdinal()) {
      out.println(JSFTestUtil.FAIL + "Expected FacesMesssage.getServerity() "
          + "to return Message.SEVERITY_INFO." + JSFTestUtil.NL
          + "Actual severity returned: " + JSFTestUtil
              .getSeverityAsString(FacesMessage.SEVERITY_INFO.getOrdinal()));
      return;
    }

    facesMessage.setSeverity(FacesMessage.SEVERITY_FATAL);

    if (facesMessage.getSeverity().getOrdinal() != FacesMessage.SEVERITY_FATAL
        .getOrdinal()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected FacesMessage.getServerity() to return "
          + "Message.SEVERITY_FATAL." + JSFTestUtil.NL
          + "Actual severity returned: " + JSFTestUtil
              .getSeverityAsString(FacesMessage.SEVERITY_FATAL.getOrdinal()));
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // FacesMessage.getSummary()
  // FacesMessage.setSummary(String)
  public void facesMessageGetSetSummaryTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
        "summary", "detail");

    if (!"summary".equals(facesMessage.getSummary())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected FacesMessage.getSummary() to "
          + "return the summary value passed to the FacesMessage "
          + "constructor." + JSFTestUtil.NL + "Expected: 'summary'"
          + JSFTestUtil.NL + "Received: '" + facesMessage.getSummary() + "'");
      return;
    }

    facesMessage.setSummary("newsummary");

    if (!"newsummary".equals(facesMessage.getSummary())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected FacesMessage.getSummary() to "
          + "return the summary value set by "
          + "FacesMessage.setSummary(String)." + JSFTestUtil.NL
          + "Expected: 'newsummary'" + JSFTestUtil.NL + "Received: '"
          + facesMessage.getSummary() + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // FacesMessage.getDetail()
  // FacesMessage.setDetail(String)
  public void facesMessageGetSetDetailTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
        "summary", "detail");

    if (!"detail".equals(facesMessage.getDetail())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected FacesMessage.getDetail() to "
          + "return the detail value passed to the FacesMessage"
          + " constructor." + JSFTestUtil.NL + "Expected: 'detail'"
          + JSFTestUtil.NL + "Received: '" + facesMessage.getDetail() + "'");
      return;
    }

    facesMessage.setDetail("newdetail");

    if (!"newdetail".equals(facesMessage.getDetail())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected FacesMessage.getDetail() to return the "
          + "detail value set by FacesMessage.setDetail(String)."
          + JSFTestUtil.NL + "Expected: 'newdetail'" + JSFTestUtil.NL
          + "Received: '" + facesMessage.getDetail() + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // FacesMessage.rendered()
  // FacesMessage.isRendered()
  public void facesMessageIsRenderedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
        "summary", "detail");

    if (facesMessage.isRendered()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected return from FacesMessage.isRendered() "
          + "prior to FacesMessage.rendered()" + "was called!" + JSFTestUtil.NL
          + "Expected: 'false'" + JSFTestUtil.NL + "Received: '"
          + facesMessage.isRendered() + "'");
      return;
    }

    facesMessage.rendered();

    if (!facesMessage.isRendered()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + " Unexpected return from FacesMessage.isRendered() "
          + "after FacesMessage.rendered() was called!" + JSFTestUtil.NL
          + "Expected: 'true'" + JSFTestUtil.NL + "Received: '"
          + facesMessage.isRendered() + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // FacesMessage.Severity.compareTo(Object)
  public void facesMessageSeverityCompareToTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    int z = 0;

    FacesMessage facesMessageOne = new FacesMessage(FacesMessage.SEVERITY_INFO,
        "summary", "detail");

    FacesMessage facesMessageTwo = new FacesMessage(FacesMessage.SEVERITY_FATAL,
        "summary", "detail");

    // compare to see that they are the same.
    if (z != facesMessageOne.getSeverity()
        .compareTo(facesMessageOne.getSeverity())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected test value to equal 0 when Severities "
          + "were compared");
      return;
    }

    // compare to see that they are the same.
    if (z == facesMessageOne.getSeverity()
        .compareTo(facesMessageTwo.getSeverity())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected test value to NOT equal 0 when Severities "
          + "were compared");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // FacesMessage.Severity.toString()
  public void facesMessageSeverityToStringTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesMessage facesMessageOne = new FacesMessage(FacesMessage.SEVERITY_INFO,
        "summary", "detail");

    if (!(facesMessageOne.getSeverity().toString().contains("INFO"))) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected FacesMessage.getSeverity().toString() "
          + "to contain 'INFO'.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

}
