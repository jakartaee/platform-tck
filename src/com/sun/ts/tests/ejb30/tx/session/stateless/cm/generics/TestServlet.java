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

package com.sun.ts.tests.ejb30.tx.session.stateless.cm.generics;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.ejb30.common.generics.GenericGreetingIF;
import com.sun.ts.tests.ejb30.common.generics.LocalIntGreetingIF;
import com.sun.ts.tests.ejb30.common.generics.LocalParameterizedIF;
import com.sun.ts.tests.ejb30.common.generics.RemoteIntGreetingIF;
import com.sun.ts.tests.ejb30.common.generics.RemoteParameterizedIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;

public class TestServlet extends HttpTCKServlet {
  @EJB(name = "remoteIntGreetingBean")
  private RemoteIntGreetingIF remoteIntGreetingBean;

  @EJB(name = "localIntGreetingBean")
  private LocalIntGreetingIF localIntGreetingBean;

  @EJB(name = "localDateGreetingBean", beanName = "DateGreetingBean")
  private GenericGreetingIF<java.util.Date> localDateGreetingBean;

  @EJB
  private LocalParameterizedIF localParameterizedBean;

  @EJB
  private RemoteParameterizedIF remoteParameterizedBean;

  public void genericsTxMandatoryRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    boolean pass2 = false;
    StringBuilder reason = new StringBuilder();
    try {
      Helper.getLogger().info(
          "TestServlet about to invoke remoteIntGreetingBean.greeting(0)");
      remoteIntGreetingBean.greet(0);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking ");
      reason.append(remoteIntGreetingBean).append(" greeting() method");
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking ");
      reason.append(remoteIntGreetingBean).append(" greeting() method");
    }
    try {
      Helper.getLogger()
          .info("TestServlet about to invoke remoteIntGreetingBean.reverse(1)");
      remoteIntGreetingBean.negate(1);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking ");
      reason.append(remoteIntGreetingBean).append(" negate() method");
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass2 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking ");
      reason.append(remoteIntGreetingBean).append(" reverse() method");
    }
    pw.println((pass1 && pass2) ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void rolesAllowedRemoteIntGreet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass = false;
    StringBuilder reason = new StringBuilder();
    try {
      Helper.getLogger().info(
          "TestServlet about to invoke remoteIntGreetingBean.rolesAllowed(0)");
      int result = remoteIntGreetingBean.rolesAllowed(0);
      reason.append("Failed to get expected EJBException when invoking ");
      reason.append(remoteIntGreetingBean)
          .append(" rolesAllowed() method.  Actually Got " + result);
    } catch (EJBException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass = true;
      reason.append(" Got expected EJBException when invoking ");
      reason.append(remoteIntGreetingBean).append(" rolesAllowed() method");
    }

    pw.println(pass ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void rolesAllowedLocalIntGreet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass = false;
    StringBuilder reason = new StringBuilder();
    try {
      Helper.getLogger().info(
          "TestServlet about to invoke localIntGreetingBean.rolesAllowed(0)");
      int result = localIntGreetingBean.rolesAllowed(0);
      reason.append("Failed to get expected EJBException when invoking ");
      reason.append(localIntGreetingBean)
          .append(" rolesAllowed() method.  Actually Got " + result);
    } catch (EJBException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass = true;
      reason.append(" Got expected EJBException when invoking ");
      reason.append(localIntGreetingBean).append(" rolesAllowed() method");
    }

    pw.println(pass ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void rolesAllowedNoArgRemoteIntGreet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass = false;
    StringBuilder reason = new StringBuilder();
    try {
      Helper.getLogger().info(
          "TestServlet about to invoke remoteIntGreetingBean.rolesAllowedNoArg()");
      int result = remoteIntGreetingBean.rolesAllowedNoArg();
      reason.append("Failed to get expected EJBException when invoking ");
      reason.append(remoteIntGreetingBean)
          .append(" rolesAllowedNoArg() method.  Actually Got " + result);
    } catch (EJBException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass = true;
      reason.append(" Got expected EJBException when invoking ");
      reason.append(remoteIntGreetingBean)
          .append(" rolesAllowedNoArg() method");
    }

    pw.println(pass ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void rolesAllowedNoArgLocalIntGreet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass = false;
    StringBuilder reason = new StringBuilder();
    try {
      Helper.getLogger().info(
          "TestServlet about to invoke localIntGreetingBean.rolesAllowedNoArg()");
      int result = localIntGreetingBean.rolesAllowedNoArg();
      reason.append("Failed to get expected EJBException when invoking ");
      reason.append(localIntGreetingBean)
          .append(" rolesAllowedNoArg() method.  Actually Got " + result);
    } catch (EJBException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass = true;
      reason.append(" Got expected EJBException when invoking ");
      reason.append(localIntGreetingBean).append(" rolesAllowedNoArg() method");
    }

    pw.println(pass ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void genericsTxMandatoryLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    boolean pass2 = false;
    StringBuilder reason = new StringBuilder();
    //
    // In ejb3, local client also get javax.ejb.EJBTransactionRequiredException,
    // not javax.ejb.TransactionRequiredLocalException.
    try {
      Helper.getLogger()
          .info("TestServlet about to invoke localIntGreetingBean.greeting(0)");
      localIntGreetingBean.greet(0);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(localIntGreetingBean).append(" greeting() method");
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(localIntGreetingBean).append(" greeting() method");
    } catch (javax.ejb.TransactionRequiredLocalException e) {
      Helper.getLogger().info("TestServlet got unexpected " + e);
      reason.append(" Got unexpected exception: ");
      reason.append(e.toString());
    }
    try {
      Helper.getLogger()
          .info("TestServlet about to invoke localIntGreetingBean.reverse(1)");
      localIntGreetingBean.negate(1);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(localIntGreetingBean).append(" negate() method");
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass2 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(localIntGreetingBean).append(" reverse() method");
    } catch (javax.ejb.TransactionRequiredLocalException e) {
      Helper.getLogger().info("TestServlet got unexpected " + e);
      reason.append(" Got unexpected exception: ");
      reason.append(e.toString());
    }
    pw.println((pass1 && pass2) ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void genericLocalBusinessInterfaceTxMandatory(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    StringBuilder reason = new StringBuilder();
    //
    // In ejb3, local client also get javax.ejb.EJBTransactionRequiredException,
    // not javax.ejb.TransactionRequiredLocalException.
    try {
      java.util.Date date = new java.util.Date();
      Helper.getLogger().info(
          "TestServlet about to invoke localDateGreetingBean.greeting(date): "
              + date.toString());
      java.util.Date greeting = localDateGreetingBean.greet(date);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(localDateGreetingBean).append(" greeting() method");
      reason.append(" It incorrectly returned ").append(greeting.toString());
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(localDateGreetingBean).append(" greeting() method");
    } catch (javax.ejb.TransactionRequiredLocalException e) {
      Helper.getLogger().info("TestServlet got unexpected " + e);
      reason.append(" Got unexpected exception: ");
      reason.append(e.toString());
    }
    pw.println(pass1 ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void rolesAllowedLocalDateGreeting(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    StringBuilder reason = new StringBuilder();
    try {
      java.util.Date date = new java.util.Date();
      Helper.getLogger().info(
          "TestServlet about to invoke localDateGreetingBean.rolesAllowed(date): "
              + date.toString());
      java.util.Date rolesAllowedResult = localDateGreetingBean
          .rolesAllowed(date);
      reason.append(
          "Failed to get expected javax.ejb.EJBException when invoking .");
      reason.append(localDateGreetingBean).append(" rolesAllowed() method");
      reason.append(" It incorrectly returned ")
          .append(rolesAllowedResult.toString());
    } catch (EJBException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(" Got expected EJBException when invoking .");
      reason.append(localDateGreetingBean)
          .append(" rolesAllowedResult() method");
    }
    pw.println(pass1 ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void rolesAllowedNoArgLocalDateGreeting(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    StringBuilder reason = new StringBuilder();
    try {
      Helper.getLogger().info(
          "TestServlet about to invoke localDateGreetingBean.rolesAllowedNoArg().");
      java.util.Date rolesAllowedNoArgResult = localDateGreetingBean
          .rolesAllowedNoArg();
      reason.append(
          "Failed to get expected javax.ejb.EJBException when invoking .");
      reason.append(localDateGreetingBean)
          .append(" rolesAllowedNoArg() method");
      reason.append(" It incorrectly returned ")
          .append(rolesAllowedNoArgResult.toString());
    } catch (EJBException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(" Got expected EJBException when invoking .");
      reason.append(localDateGreetingBean)
          .append(" rolesAllowedNoArgResult() method");
    }
    pw.println(pass1 ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void parameterizedParamLocalTxMandatory(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    StringBuilder reason = new StringBuilder();

    try {
      List<String> ls = Arrays.asList("parameterizedParamLocalTxMandatory");
      Helper.getLogger().info(
          "TestServlet about to invoke localParameterizedBean.parameterizedParam(List<String>): "
              + ls);
      localParameterizedBean.parameterizedParam(ls);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(localParameterizedBean)
          .append(" parameterizedParam(List<String>) method");
      reason.append(" It incorrectly returned void.");
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(localParameterizedBean)
          .append(" parameterizedParam(List<String>) method");
    } catch (javax.ejb.TransactionRequiredLocalException e) {
      Helper.getLogger().info("TestServlet got unexpected " + e);
      reason.append(" Got unexpected exception: ");
      reason.append(e.toString());
    }
    pw.println(pass1 ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void parameterizedReturnLocalTxMandatory(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    StringBuilder reason = new StringBuilder();

    try {
      int i = 2;
      Helper.getLogger().info(
          "TestServlet about to invoke localParameterizedBean.parameterizedReturn(int): "
              + i);
      List<String> result = localParameterizedBean.parameterizedReturn(i);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(localParameterizedBean)
          .append(" parameterizedReturn(i) method");
      reason.append(" It incorrectly returned: " + result);
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(localParameterizedBean)
          .append(" parameterizedReturn(int) method");
    } catch (javax.ejb.TransactionRequiredLocalException e) {
      Helper.getLogger().info("TestServlet got unexpected " + e);
      reason.append(" Got unexpected exception: ");
      reason.append(e.toString());
    }
    pw.println(pass1 ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void parameterizedParamRemoteTxMandatory(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    StringBuilder reason = new StringBuilder();

    try {
      List<String> ls = Arrays.asList("parameterizedParamRemoteTxMandatory");
      Helper.getLogger().info(
          "TestServlet about to invoke localParameterizedRemoteBean.parameterizedParam(List<String>): "
              + ls);
      remoteParameterizedBean.parameterizedParam(ls);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteParameterizedBean)
          .append(" parameterizedParam(List<String>) method");
      reason.append(" It incorrectly returned void.");
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteParameterizedBean)
          .append(" parameterizedParam(List<String>) method");
    }
    pw.println(pass1 ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void parameterizedReturnRemoteTxMandatory(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    StringBuilder reason = new StringBuilder();

    try {
      int i = 2;
      Helper.getLogger().info(
          "TestServlet about to invoke localParameterizedRemoteBean.parameterizedReturn(int): "
              + i);
      List<String> result = remoteParameterizedBean.parameterizedReturn(i);
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteParameterizedBean)
          .append(" parameterizedReturn(i) method");
      reason.append(" It incorrectly returned: " + result);
    } catch (EJBTransactionRequiredException e) {
      Helper.getLogger().info("TestServlet got expected " + e);
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteParameterizedBean)
          .append(" parameterizedReturn(int) method");
    }
    pw.println(pass1 ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }
}
