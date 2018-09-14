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

package com.sun.ts.tests.ejb30.misc.nomethodbean;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.ejb.EJBs;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;

@EJBs({
    @EJB(name = "noMethodStatefulRemote", beanName = "NoMethodStatefulBean", beanInterface = NoMethodRemoteIF.class, description = "remote ejb3 stateful session bean with no business methods"),
    @EJB(name = "noMethodStatefulLocal", beanName = "NoMethodStatefulBean", beanInterface = NoMethodLocalIF.class, description = "local ejb3 stateful session bean with no business methods") })

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)
public class TestServlet extends HttpTCKServlet {
  @EJB(name = "noMethodStatelessRemote", beanName = "NoMethodStatelessBean")
  private NoMethodRemoteIF noMethodStatelessRemote;

  @EJB(name = "noMethodStatelessLocal", beanName = "NoMethodStatelessBean")
  private NoMethodLocalIF noMethodStatelessLocal;

  public void noMethodStateless(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    boolean pass2 = false;
    StringBuilder reason = new StringBuilder();
    if (noMethodStatelessRemote != null) {
      pass1 = true;
      reason.append(
          "A remote NoMethodStatelessBean has been injected correctly: ");
      reason.append(noMethodStatelessRemote.toString());
    } else {
      reason.append("A remote NoMethodStatelessBean has not been injected: ");
      // reason.append(noMethodStatelessRemote.toString());
    }
    if (noMethodStatelessLocal != null) {
      pass2 = true;
      reason.append(
          "A local NoMethodStatelessBean has been injected correctly: ");
      reason.append(noMethodStatelessLocal.toString());
    } else {
      reason.append("A local NoMethodStatelessBean has not been injected: ");
      // reason.append(noMethodStatelessLocal.toString());
    }
    pw.println((pass1 && pass2) ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void noMethodStateful(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    boolean pass2 = false;
    StringBuilder reason = new StringBuilder();
    NoMethodRemoteIF noMethodStatefulRemote = null;
    NoMethodLocalIF noMethodStatefulLocal = null;
    try {
      noMethodStatefulRemote = (NoMethodRemoteIF) ServiceLocator
          .lookupByShortName("noMethodStatefulRemote");
      pass1 = true;
      reason.append(
          "A remote NoMethodStatefulBean has been injected correctly: ");
      reason.append(noMethodStatefulRemote.toString());
    } catch (NamingException ex) {
      reason.append("A remote NoMethodStatefulBean has not been injected.");
      reason.append(" Exception during lookup: ").append(ex);
    }
    try {
      noMethodStatefulLocal = (NoMethodLocalIF) ServiceLocator
          .lookupByShortName("noMethodStatefulLocal");
      pass2 = true;
      reason
          .append("A local NoMethodStatefulBean has been injected correctly: ");
      reason.append(noMethodStatefulLocal.toString());
    } catch (NamingException ex) {
      reason.append("A local NoMethodStatefulBean has not been injected.");
      reason.append(" Exception during lookup: ").append(ex);
    }
    pw.println((pass1 && pass2) ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }
}
