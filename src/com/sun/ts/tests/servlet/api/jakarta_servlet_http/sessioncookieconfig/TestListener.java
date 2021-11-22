/*
 * Copyright (c) 2009, 2021 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.jakarta_servlet_http.sessioncookieconfig;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.SessionCookieConfig;

public class TestListener implements ServletContextListener {

  /**
   * Test for SessionCookieConfig.SessionCookieConfig method
   */
  public void contextInitialized(ServletContextEvent sce) {
    StringBuffer testData = new StringBuffer("Testing_Session_Cookie_Config");
    String domain = "sun.com";
    String path = "/servlet_jsh_sessioncookieconfig_web/TestServlet";
    Boolean isSecure = true;
    Boolean httpOnly = false;
    int maxage = 50000;
    String name = "TCK_Cookie_Name";

    SessionCookieConfig scf = sce.getServletContext().getSessionCookieConfig();

    scf.setDomain(domain);
    scf.setHttpOnly(httpOnly);
    scf.setMaxAge(maxage);
    scf.setPath(path);
    scf.setSecure(isSecure);

    if (!scf.getPath().equals(path)) {
      testData.append("|getPathFAILED-expecting-" + path + "-got-" + scf.getPath());
    }

    if (!scf.isSecure()) {
      testData.append("|isSecureFAILED-expecting-" + isSecure + "-got-" + scf.isSecure());
    }

    if (scf.isHttpOnly()) {
      testData.append("|isHttpOnlyFAILED-expecting-" + httpOnly + "-got-" + scf.isHttpOnly());
    }

    if (!scf.getDomain().equals(domain.toString())) {
      testData.append("|getDomainFAILED-expecting-" + domain + "-got-" + scf.getDomain());
    }

    if (scf.getMaxAge() != maxage) {
      testData.append("|getMaxAgeFAILED-expecting-" + maxage + "-got-" + scf.getMaxAge());
    }

    if (scf.getName() != null) {
      testData.append("|getNameFAILED-expecting-null-got-" + scf.getName());
    }

    scf.setName(name);
    if (!scf.getName().equals(name)) {
      testData.append("|getNameFAILED-expecting-" + name + "-got-" + scf.getName());
    }
    
    sce.getServletContext().setAttribute(this.getClass().getName(), testData.toString());
  }
}
