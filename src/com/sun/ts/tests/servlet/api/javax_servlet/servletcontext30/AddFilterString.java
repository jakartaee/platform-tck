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
package com.sun.ts.tests.servlet.api.javax_servlet.servletcontext30;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class AddFilterString implements Filter {

  // The filter configuration object we are associated with. If this value
  // is null, this filter instance is not currently configured.
  private FilterConfig filterConfig = null;

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    if (filterConfig == null)
      throw new ServletException(
          "doFilter of AddFilterString was called but this filter instance is not currently configured");

    ArrayList result = (ArrayList) filterConfig.getServletContext()
        .getAttribute("arraylist");
    result.add("ADD_FILTER_STRING_INVOKED");

    filterConfig.getServletContext().setAttribute("arraylist", result);
    chain.doFilter(request, response);
  }

  // Remove the filter configuration object for this filter.
  public void destroy() {
  }

  // initialize the filter configuration object for this filter.
  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }
}
