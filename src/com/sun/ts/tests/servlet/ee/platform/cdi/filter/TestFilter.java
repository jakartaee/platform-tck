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

package com.sun.ts.tests.servlet.ee.platform.cdi.filter;

import com.sun.ts.tests.servlet.ee.platform.cdi.TCKTestBean;
import java.io.IOException;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/*")
public class TestFilter implements Filter {

  static FilterConfig config;

  @Inject
  TCKTestBean ttb;

  @Inject
  BeanManager bm;

  @Override
  public void destroy() {
    System.out.println("TestFilter:destroy()");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain fc) throws IOException, ServletException {
    if (ttb == null) {
      throw new ServletException(
          "Injection of TCKTestBean in TestFilter failed");
    }
    if (bm == null) {
      throw new ServletException(
          "Injection of BeanManager in TestFilter failed");
    }
    config.getServletContext().setAttribute("TEST_LOG",
        "Test PASSED from TestFilter");
    fc.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig config1) throws ServletException {
    System.out.println("TestFilter:init()");
    this.config = config1;
  }
}
