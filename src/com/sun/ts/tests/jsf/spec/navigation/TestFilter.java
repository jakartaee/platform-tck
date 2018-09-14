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
package com.sun.ts.tests.jsf.spec.navigation;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TestFilter implements Filter {

  // --------------------------------------- Methods from javax.servlet.Filter

  public void init(FilterConfig filterConfig) throws ServletException {

    System.out.println("[TestFilter] init");

  } // END init

  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    TestServletResponseWrapper wrapper = new TestServletResponseWrapper(
        (HttpServletResponse) servletResponse);
    filterChain.doFilter(servletRequest, wrapper);

  } // END doFilter

  public void destroy() {

    System.out.println("[TestFilter] destroy");

  } // END destroy

}
