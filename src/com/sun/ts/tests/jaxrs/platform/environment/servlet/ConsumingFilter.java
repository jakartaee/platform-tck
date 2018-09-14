/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.environment.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

public class ConsumingFilter implements Filter {

  public static final String CONTENTTYPE = MediaType.APPLICATION_FORM_URLENCODED;

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    String contentType = request.getContentType();
    if (contentType != null && contentType.equalsIgnoreCase(CONTENTTYPE)) {
      // read / cache form parameter from request entity
      String entity = request.getParameterMap().get("entity")[0];
      if (!"ENTITY".equals(entity)) {
        // The requested entity should be there
        ((HttpServletResponse) response).sendError(512, entity);
      }

      // make sure to consume the request entity stream
      ServletInputStream in = request.getInputStream();
      while (-1 != in.read())
        /* no-op */;
    }
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

}
