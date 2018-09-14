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

package com.sun.ts.tests.ejb30.misc.metadataComplete.warejb;

import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.EJB;
import javax.ejb.EJBs;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.omg.CORBA.ORB;

@Resources({ @Resource(name = "typeLevelOrbNotInjected", type = ORB.class) })
@EJBs({
    @EJB(name = "typeLevelBeanNotInjected", beanInterface = RemoteCalculator.class, beanName = "StatelessRemoteCalculatorBean") })
public class TestServlet extends HttpTCKServlet {

  @Resource(name = "orb")
  private ORB orb;

  @EJB(name = "statelessBeanNotInjected", beanName = "StatelessRemoteCalculatorBean")
  private RemoteCalculator statelessBeanNotInjected;

  @EJB(name = "statefulBeanNotInjected", beanName = "StatefulRemoteCalculatorBean")
  private RemoteCalculator statefulBeanNotInjected;

  // beans used in tests. injected in web.xml
  @EJB(name = "statelessBean", beanName = "StatelessRemoteCalculatorBean")
  private RemoteCalculator statelessBean;

  @EJB(name = "statefulBean", beanName = "StatefulRemoteCalculatorBean")
  private RemoteCalculator statefulBean;

  private RemoteCalculator excludeDefaultIncludeClass;

  private RemoteCalculator excludeDefaultAndClassReinstateDefault;

  private RemoteCalculator sameInterceptor3Times;

  private void operation(RemoteCalculator bean, HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int op1 = 2;
    int op2 = 3;

    // default interceptor adds 100 to each param.
    int expected = op1 + op2 + 100 + 100;
    int result = 0;
    try {
      result = bean.remoteAdd(op1, op2);
    } catch (Exception e) {
      pw.println(Data.FAILED + e);
    }
    if (result == expected) {
      pw.println(Data.PASSED + "Got expected result: " + result);
    } else {
      pw.println(Data.FAILED + "Expected " + expected + ", actual " + result);
    }
  }

  public void annotationNotProcessedForStateless(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    operation(statelessBean, request, response);
  }

  public void annotationNotProcessedForStateful(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    operation(statefulBean, request, response);
  }

  public void annotationNotProcessedForWar(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    if (orb == null && statelessBeanNotInjected == null
        && statefulBeanNotInjected == null) {
      pw.println(Data.PASSED + ".  Annotated fields are not injected, since "
          + "this war has been marked as metadata-complete.");
    } else {
      pw.println(Data.FAILED + ".  Annotated fields should not be injected, "
          + "since this war has been marked as metadata-complete.  But "
          + "these fields were injected: " + orb + "\n"
          + statelessBeanNotInjected + "\n" + statefulBeanNotInjected);
    }
  }

  public void typeLevelAnnotationNotProcessedForWar(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    String lookupName = "java:comp/env/typeLevelBeanNotInjected";
    Object lookupValue = null;
    try {
      lookupValue = ServiceLocator.lookup(lookupName);
      pw.println(Data.FAILED + lookupName + " must not be injected "
          + "at the servlet type level, since a full "
          + "war descriptor is used. " + " The value looked up is "
          + lookupValue);
    } catch (NamingException e) {
      pw.println(Data.PASSED
          + "Got expected javax.naming.NamingException when looking up "
          + lookupName);
    }

    lookupName = "java:comp/env/typeLevelOrbNotInjected";
    try {
      lookupValue = ServiceLocator.lookup(lookupName);
      pw.println(Data.FAILED + lookupName + " must not be injected "
          + "at the servlet type level, since a full "
          + "war descriptor is used. " + " The value looked up is "
          + lookupValue);
    } catch (NamingException e) {
      pw.println(Data.PASSED
          + "Got expected javax.naming.NamingException when looking up "
          + lookupName);
    }
  }

  public void excludeDefaultIncludeClassInterceptor(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int param1 = 0;
    int param2 = 0;
    int additionByInterceptor = 100 + 100;
    int postConstructCallsCount = 1;
    int expected = param1 + param2 + additionByInterceptor
        + postConstructCallsCount;
    int actual = excludeDefaultIncludeClass.remoteAdd(param1, param2);
    if (actual == expected) {
      // pw.println(Data.PASSED + "Got expected result : " + actual);
      pw.println("Got expected result : " + actual);
    } else {
      pw.println(Data.FAILED + " Expecting result: " + expected
          + ", but actual is " + actual);
    }

    // invoke another business method, which should also be covered by the
    // class-level interceptor
    actual = excludeDefaultIncludeClass.remoteSubtract(param1, param2);
    if (actual == expected) {
      pw.println(Data.PASSED + " Got expected result : " + actual);
    } else {
      pw.println(Data.FAILED + " Expecting result: " + expected
          + ", but actual is " + actual);
    }
  }

  public void excludeDefaulAndClassReinstateDefault(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int param1 = 0;
    int param2 = 0;
    int additionByInterceptor = 100 + 100;
    int postConstructCallsCount = 1;
    int expected = param1 + param2 + additionByInterceptor
        + postConstructCallsCount;
    int actual = excludeDefaultAndClassReinstateDefault.remoteAdd(param1,
        param2);
    if (actual == expected) {
      pw.println(Data.PASSED + " Got expected result : " + actual);
    } else {
      pw.println(Data.FAILED + " Expecting result: " + expected
          + ", but actual is " + actual);
    }
  }

  public void defaultAndMethodInterceptor(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int param1 = 0;
    int param2 = 0;
    int additionByInterceptor = (100 + 100) * 2;
    int postConstructCallsCount = 1;
    int expected = param1 + param2 + additionByInterceptor
        + postConstructCallsCount;
    int actual = excludeDefaultAndClassReinstateDefault.remoteSubtract(param1,
        param2);
    if (actual == expected) {
      pw.println(Data.PASSED + " Got expected result : " + actual);
    } else {
      pw.println(Data.FAILED + " Expecting result: " + expected
          + ", but actual is " + actual);
    }
  }

  public void sameInterceptor3Times(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int param1 = 0;
    int param2 = 0;
    int additionByInterceptor = (100 + 100) * 3;
    int postConstructCallsCount = 1;
    int expected = param1 + param2 + additionByInterceptor
        + postConstructCallsCount;
    int actual = sameInterceptor3Times.remoteAdd(param1, param2);
    if (actual == expected) {
      pw.println(Data.PASSED + " Got expected result : " + actual);
    } else {
      pw.println(Data.FAILED + " Expecting result: " + expected
          + ", but actual is " + actual);
    }
  }

  public void sameInterceptorTwice(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int param1 = 0;
    int param2 = 0;
    int additionByInterceptor = (100 + 100) * 2;
    int postConstructCallsCount = 1;
    int expected = param1 + param2 + additionByInterceptor
        + postConstructCallsCount;
    int actual = sameInterceptor3Times.remoteSubtract(param1, param2);
    if (actual == expected) {
      pw.println(Data.PASSED + " Got expected result : " + actual);
    } else {
      pw.println(Data.FAILED + " Expecting result: " + expected
          + ", but actual is " + actual);
    }
  }

}
