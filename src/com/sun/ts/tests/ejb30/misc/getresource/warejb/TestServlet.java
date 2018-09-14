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

package com.sun.ts.tests.ejb30.misc.getresource.warejb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceIF;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)
public class TestServlet extends HttpTCKServlet {
  // @EJB(name="getResourceBean", beanName="GetResourceBean")
  // There is only 1 bean implementing GetResourceIF
  @EJB(name = "getResourceBean")
  private GetResourceIF bean;

  private GetResourceIF tester = new GetResourceDelegateForWeb();

  public void getResourceSamePackage(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceSamePackage();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceAsStreamSamePackage(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceAsStreamSamePackage();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceResolve(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceResolve();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceAsStreamResolve(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceAsStreamResolve();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceResolveEarLib(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceResolveEarLib();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceAsStreamResolveEarLib(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceAsStreamResolveEarLib();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceNullParam(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceNullParam();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceNonexisting(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceNonexisting();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      throw new ServletException(ex);
    }
  }

  public void getResourceAsStreamNullParam(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceAsStreamNullParam();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      throw new ServletException(ex);
    }
  }

  public void getResourceAsStreamNonexisting(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      tester.getResourceAsStreamNonexisting();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      throw new ServletException(ex);
    }
  }

  /////////////////////////////////////////////////
  // tests in EJB
  ////////////////////////////////////////////////

  public void getResourceSamePackageEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceSamePackage();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceAsStreamSamePackageEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceAsStreamSamePackage();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceResolveEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceResolve();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceAsStreamResolveEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceAsStreamResolve();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceNullParamEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceNullParam();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceNonexistingEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceNonexisting();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      throw new ServletException(ex);
    }
  }

  public void getResourceAsStreamNullParamEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceAsStreamNullParam();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      throw new ServletException(ex);
    }
  }

  public void getResourceAsStreamNonexistingEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceAsStreamNonexisting();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      throw new ServletException(ex);
    }
  }

  public void getResourceResolveEarLibEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceResolveEarLib();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  public void getResourceAsStreamResolveEarLibEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      bean.getResourceAsStreamResolveEarLib();
      pw.println(Data.PASSED);
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(ex));
    }
  }

  // when creating an absolute resource name for Class.getResource, strarts with
  // /
  // when creating an absolute resource name for ClassLoader.getResource, no /
  protected String getAbsoluteName(String name, boolean leadingSlash) {
    return (leadingSlash ? "/" : "")
        + "com/sun/ts/tests/ejb30/misc/getresource/warejb/" + name;
  }
}
