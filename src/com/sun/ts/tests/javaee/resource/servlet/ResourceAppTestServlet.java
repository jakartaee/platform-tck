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

package com.sun.ts.tests.javaee.resource.servlet;

import com.sun.ts.tests.servlet.common.util.ServletTestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.mail.Session;
import javax.mail.MailSessionDefinition;
import javax.annotation.Resource;
import java.io.IOException;

@MailSessionDefinition(name = "java:app/env/ResourceAppTestServlet_MailSession", properties = {
    "test=ResourceAppTestServlet_MailSession" })
@MailSessionDefinition(name = "java:app/env/ResourceAppTestServlet_MailSession_repeatable", properties = {
    "test=ResourceAppTestServlet_MailSession_repeatable" })
@WebServlet(urlPatterns = { "/resourceAppTest" })
public class ResourceAppTestServlet extends HttpServlet {

  // the value of the "test" property above
  private static final String EXPECTED = "ResourceAppTestServlet_MailSession";

  private static final String EXPECTED_REPEATABLE = "ResourceAppTestServlet_MailSession_repeatable";

  @Resource(lookup = "java:app/env/ResourceAppTestServlet_MailSession")
  Session session;

  @Resource(lookup = "java:app/env/ResourceAppTestServlet_MailSession_repeatable")
  Session session_repeatable;

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    ResourceUtil.test(response, session, EXPECTED);
    ResourceUtil.test(response, session_repeatable, EXPECTED_REPEATABLE);
  }
}
