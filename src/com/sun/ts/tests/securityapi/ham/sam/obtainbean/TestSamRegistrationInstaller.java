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

package com.sun.ts.tests.securityapi.ham.sam.obtainbean;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TestSamRegistrationInstaller implements ServletContextListener {

  private String registrationId;

  private String getAppContextID(ServletContext context) {
    return context.getVirtualServerName() + " " + context.getContextPath();
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServerAuthModule sam = new TestServerAuthModule();
    registrationId = AccessController
        .doPrivileged(new PrivilegedAction<String>() {
          public String run() {
            return AuthConfigFactory.getFactory().registerConfigProvider(
                new TestAuthConfigProvider(sam), "HttpServlet",
                getAppContextID(sce.getServletContext()),
                "Test single SAM authentication config provider");
          }
        });
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    AuthConfigFactory.getFactory().removeRegistration(registrationId);
  }
}
