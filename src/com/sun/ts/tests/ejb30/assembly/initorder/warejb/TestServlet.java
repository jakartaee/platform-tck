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

package com.sun.ts.tests.ejb30.assembly.initorder.warejb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.assembly.initorder.common.InitOrderRemoteIF;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)
public class TestServlet extends HttpTCKServlet {
  @Resource(lookup = "java:app/AppName")
  private String appNameInjected;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    Helper.getLogger().info("In postConstruct of " + this);
    getHelloBean().addRecord("TestServlet");
  }

  public void initOrder(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      List<String> expected = Arrays.asList("TestServlet", "InitOrderBean");
      List<String> records = getHelloBean().getAndClearRecords();
      Helper.assertEquals(null, expected, records, reason);
      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      reason.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(reason.toString());
  }

  public void appName(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      String expected = "renamed";
      String lookup = "java:app/AppName";
      String actual = (String) ServiceLocator.lookupNoTry(lookup);
      Helper.assertEquals("Check " + lookup, expected, actual, reason);
      Helper.assertEquals("Check appNameInjected ", expected, appNameInjected,
          reason);

      String s = "java:global/renamed/ejb3_assembly_initorder_warejb_ejb/InitOrderBean";
      InitOrderRemoteIF b = (InitOrderRemoteIF) ServiceLocator.lookupNoTry(s);
      reason.append("Looked up InitOrderBean by global jndi name: " + s);
      reason.append(b.toString());

      s = "java:global/ejb3_assembly_initorder_warejb/ejb3_assembly_initorder_warejb_ejb/InitOrderBean";
      try {
        b = (InitOrderRemoteIF) ServiceLocator.lookup(s);
        throw new RuntimeException("Expecting NamingException when looking up "
            + s + ", but got " + b);
      } catch (javax.naming.NamingException e) {
        reason.append(" Got expected ").append(e).append(" when looking up ")
            .append(s);
      }

      pw.println(Data.PASSED);
    } catch (Exception e) {
      pw.println(Data.FAILED);
      reason.append(TestUtil.printStackTraceToString(e));
    }
    pw.println(reason.toString());
  }

  private static HelloRemoteIF getHelloBean() {
    return (HelloRemoteIF) ServiceLocator
        .lookupNoTry(HelloRemoteIF.GLOBAL_JNDI_NAME);
  }

}
