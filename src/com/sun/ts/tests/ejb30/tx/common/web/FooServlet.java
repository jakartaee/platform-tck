/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.tx.common.web;

import javax.annotation.Resource;
import javax.servlet.http.*;
import javax.servlet.*;
import jakarta.transaction.UserTransaction;

public class FooServlet extends HttpServlet {
  @Resource
  private UserTransaction ut;

  public FooServlet() {
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, java.io.IOException {
    try {
      // when the service() method exits, the tx should be terminated by
      // the container.
      ut.begin();
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
