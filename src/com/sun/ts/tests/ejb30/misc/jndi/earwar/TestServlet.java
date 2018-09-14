/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.misc.jndi.earwar;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.basic.common.BasicBeanHelper;
import com.sun.ts.tests.ejb30.lite.basic.stateless.BasicBean;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)
public class TestServlet extends HttpTCKServlet {

  @Resource(lookup = "java:module/ModuleName")
  private String moduleNameInjected;

  @Resource(lookup = "java:app/AppName")
  private String appNameInjected;

  protected String getAppName(HttpServletRequest request) {
    String s = request.getHeader("appName");
    if (s == null || s.length() == 0) {
      throw new RuntimeException("appName in header is null or empty: " + s);
    }
    return s;
  }

  protected String getModuleName(HttpServletRequest request) {
    String s = request.getHeader("moduleName");
    if (s == null || s.length() == 0) {
      throw new RuntimeException("moduleName in header is null or empty: " + s);
    }
    return s;
  }

  @EJB
  private BasicBean basicBean;

  public void globalJNDI(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      BasicBeanHelper.globalJNDI(getAppName(request), getModuleName(request),
          basicBean.getBusinessInterface(), reason, (javax.ejb.EJBContext) null,
          (javax.naming.Context) null);
      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      reason.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(reason.toString());
  }

  public void globalJNDI2(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      reason.append(
          basicBean.globalJNDI(getAppName(request), getModuleName(request)));
      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      reason.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(reason.toString());
  }

  public void appJNDI(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      BasicBeanHelper.appJNDI(getModuleName(request),
          basicBean.getBusinessInterface(), reason);
      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      reason.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(reason.toString());
  }

  public void appJNDI2(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      reason.append(basicBean.appJNDI(getModuleName(request)));
      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      reason.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(reason.toString());
  }

  public void moduleJNDI(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      BasicBeanHelper.moduleJNDI(basicBean.getBusinessInterface(), reason);
      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      reason.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(reason.toString());
  }

  public void moduleJNDI2(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      reason.append((basicBean.moduleJNDI()));
      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      reason.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(reason.toString());
  }

  public void appNameModuleName(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder sb = new StringBuilder();

    try {
      String lookup = "java:module/ModuleName";
      String expected = getModuleName(request);
      String actual = (String) ServiceLocator.lookupNoTry(lookup);
      assertEquals("Check " + lookup, expected, actual, sb);
      assertEquals("Check injected value ", expected, moduleNameInjected, sb);

      lookup = "java:app/AppName";
      expected = getAppName(request);
      actual = (String) ServiceLocator.lookupNoTry(lookup);
      assertEquals("Check " + lookup, expected, actual, sb);
      assertEquals("Check injected value ", expected, appNameInjected, sb);

      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      sb.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(sb.toString());
  }
}
