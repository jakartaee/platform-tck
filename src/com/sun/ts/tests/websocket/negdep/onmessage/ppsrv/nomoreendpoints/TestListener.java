/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.negdep.onmessage.ppsrv.nomoreendpoints;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.server.ServerContainer;

@WebListener
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
    ServletContext context = sce.getServletContext();
    try {
      final ServerContainer serverContainer = (ServerContainer) context
          .getAttribute("javax.websocket.server.ServerContainer");
      serverContainer.addEndpoint(new EchoServerConfig());
    } catch (Exception ex) {
      // The DeploymentException can be thrown here, since no
      // more endpoit can be deployed when there was invalid
      // endpoint in the app
      ex.printStackTrace();
    }
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
