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

package com.sun.ts.tests.ejb30.assembly.metainfandlibdir;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyCommonIF;
import static com.sun.ts.tests.ejb30.assembly.common.AssemblyCommonIF.RESOURCE_NAME;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyRemoteIF;
import com.sun.ts.tests.ejb30.assembly.common.ConcurrentLookup;
import com.sun.ts.tests.ejb30.assembly.common.Util;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract public class TestServletBase extends HttpTCKServlet {
  @EJB(name = "remoteAssemblyBean", beanInterface = AssemblyRemoteIF.class)
  protected AssemblyCommonIF remoteAssemblyBean;

  // helloBean is deployed in a separate ejb module (see tests/ejb30/common/
  // helloejbjar/). This ejb-ref is resolved by sun-web.xml
  @EJB(name = "helloBean")
  protected HelloRemoteIF helloBean;

  public void ejbInjectionInFilterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    pw.println(
        "Do nothing for testname ejbInjectionInFilterTest.  The test logic is in filter class.");
  }

  public void remoteAdd(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int a = 1;
    int b = 2;
    int additionalByInterceptor = 100 * 2;
    int expected = a + b + additionalByInterceptor;
    int actual = remoteAssemblyBean.remoteAdd(a, b);
    if (actual == expected) {
      pw.println(Data.PASSED + " Got expected result: " + expected);
    } else {
      pw.println(Data.FAILED + " Expecting " + expected + ", but actual "
          + actual + ". The interceptor may not have been invoked.");
    }
  }

  public void remoteAddByHelloEJB(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int a = 1;
    int b = 1;
    int expected = a + b;
    int actual = helloBean.add(a, b);
    if (actual == expected) {
      pw.println(Data.PASSED + "Got expected result from calling helloBean.");
    } else {
      pw.println(Data.FAILED + "Expecting helloBean.add to return " + expected
          + ", but actual was " + actual);
    }
  }

  public void remoteAddByHelloEJBFromAssemblyBean(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    String result = remoteAssemblyBean.callHelloBean();
    if (result != null) {
      pw.println(Data.PASSED + "Got expected result: " + result);
    } else {
      pw.println(Data.FAILED
          + "Expecting a non-null result from remoteAssemblyBean.callHelloBean(), but got null.");
    }
  }

  public void libSubdirNotScanned(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      Util.verifyGetResource(getClass(), RESOURCE_NAME, null);
      pw.println(Data.PASSED);
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + TestUtil.printStackTraceToString(e));
    }
  }

}
