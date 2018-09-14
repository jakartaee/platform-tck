/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.websocket.spec.servercontainer.addendpoint;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;

public class TestListener implements ServletContextListener {

  /**
   * Receives notification that the web application initialization process is
   * starting.
   *
   * @param sce
   *          The ServletContextEvent
   */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    boolean passed = true;
    ServletContext context = sce.getServletContext();
    StringBuilder log = new StringBuilder();

    final String servercontainer_name = "javax.websocket.server.ServerContainer";

    try {
      final ServerContainer serverContainer = (ServerContainer) sce
          .getServletContext()
          .getAttribute("javax.websocket.server.ServerContainer");
      serverContainer.addEndpoint(
          com.sun.ts.tests.websocket.spec.servercontainer.addendpoint.WSTestServer.class);
      serverContainer.addEndpoint(
          com.sun.ts.tests.websocket.spec.servercontainer.addendpoint.WSCloseTestServer.class);
      serverContainer.addEndpoint(
          com.sun.ts.tests.websocket.spec.servercontainer.addendpoint.WSCloseTestServer1.class);
      serverContainer.addEndpoint(
          com.sun.ts.tests.websocket.spec.servercontainer.addendpoint.WSCloseTestServer2.class);
      serverContainer.addEndpoint(
          com.sun.ts.tests.websocket.spec.servercontainer.addendpoint.WSTestServerByte.class);
      serverContainer.addEndpoint(
          com.sun.ts.tests.websocket.spec.servercontainer.addendpoint.WSTestServerPathParam.class);
      serverContainer.addEndpoint(
          com.sun.ts.tests.websocket.spec.servercontainer.addendpoint.WSTestServerString.class);
    } catch (DeploymentException ex) {
      passed = false;
      log.append("DeploymentException: " + ex.getMessage());
    } catch (UnsupportedOperationException ex1) {
      passed = false;
      log.append("UnsupportedOperationException:" + ex1.getMessage());
    }

    context.setAttribute("TCK_TEST_STATUS", log.toString());
    context.setAttribute("TCK_TEST_PASS_STATUS", passed);

    System.err.println("==========TestListener: " + log.toString());
  }

  /**
   * Receives notification that the servlet context is about to be shut down.
   *
   * @param sce
   *          The servlet context event
   */
  public void contextDestroyed(ServletContextEvent sce) {
    // Do nothing
  }
}
