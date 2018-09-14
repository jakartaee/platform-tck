/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.servlet.api.javax_servlet.servletcontext40;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import java.util.List;

public class AddListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();
    List list = (List) context.getAttribute("arraylist");
    try {
      ServletRegistration registration = context.addJspFile("AddJspFile2",
          "/addJspFile.jsp");
      list.add("addJsp_addedListener");
    } catch (UnsupportedOperationException e) {
      list.add("UnsupportedOperationException_when_addJsp_addedListener");
    }

    try {
      context.setSessionTimeout(2);
      list.add("setSessionTimeout_addedListener");
    } catch (UnsupportedOperationException e) {
      list.add(
          "UnsupportedOperationException_when_setSessionTimeout_addedListener");
    }
    context.setAttribute("arraylist", list);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }
}
