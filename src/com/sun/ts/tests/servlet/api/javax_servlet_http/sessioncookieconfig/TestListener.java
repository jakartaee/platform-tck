/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.javax_servlet_http.sessioncookieconfig;

import javax.servlet.SessionCookieConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TestListener implements ServletContextListener {

  /**
   * Test for SessionCookieConfig.SessionCookieConfig method
   */
  public void contextInitialized(ServletContextEvent sce) {
    StringBuffer comment = new StringBuffer("Testing_Session_Cookie_Config");
    String domain = "sun.com";
    String path = "/servlet_jsh_sessioncookieconfig_web/TestServlet";
    Boolean isSecure = true;
    Boolean httpOnly = false;
    int maxage = 50000;
    String name = "TCK_Cookie_Name";

    SessionCookieConfig scf = sce.getServletContext().getSessionCookieConfig();

    scf.setComment(comment.toString());
    scf.setDomain(domain);
    scf.setHttpOnly(httpOnly);
    scf.setMaxAge(maxage);
    scf.setPath(path);
    scf.setSecure(isSecure);

    if (!scf.getComment().equals(comment.toString())) {
      comment.append("|getCommentFAILED-expecting-" + comment + "-got-"
          + scf.getComment());
      scf.setComment(comment.toString());
    }

    if (!scf.getPath().equals(path)) {
      comment
          .append("|getPathFAILED-expecting-" + path + "-got-" + scf.getPath());
      scf.setComment(comment.toString());
    }

    if (!scf.isSecure()) {
      comment.append(
          "|isSecureFAILED-expecting-" + isSecure + "-got-" + scf.isSecure());
      scf.setComment(comment.toString());
    }
    if (scf.isHttpOnly()) {
      comment.append("|isHttpOnlyFAILED-expecting-" + httpOnly + "-got-"
          + scf.isHttpOnly());
      scf.setComment(comment.toString());
    }
    if (!scf.getDomain().equals(domain.toString())) {
      comment.append(
          "|getDomainFAILED-expecting-" + domain + "-got-" + scf.getDomain());
      scf.setComment(comment.toString());
    }

    if (scf.getMaxAge() != maxage) {
      comment.append(
          "|getMaxAgeFAILED-expecting-" + maxage + "-got-" + scf.getMaxAge());
      scf.setComment(comment.toString());
    }

    if (scf.getName() != null) {
      comment.append("|getNameFAILED-expecting-null-got-" + scf.getName());
      scf.setComment(comment.toString());
    }

    scf.setName(name);
    if (!scf.getName().equals(name)) {
      comment
          .append("|getNameFAILED-expecting-" + name + "-got-" + scf.getName());
      scf.setComment(comment.toString());
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {

  }
}
