/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1109;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

public final class SoapActionFilter implements Filter, SOAPRequests {

  // The filter configuration object we are associated with. If this value
  // is null, this filter instance is not currently configured.
  private FilterConfig filterConfig = null;

  // remove the filter configuration object for this filter.
  public void destroy() {
  }

  // initialize the filter configuration object for this filter.

  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    String result;
    System.out.println("Here in SOAPActionFilter.doFilter()\n");
    if (filterConfig == null) {
      result = "EXCEPTION";
    } else {
      result = verifySoapActionHeader((HttpServletRequest) request);
    }
    String xml = MessageFormat.format(R1109_RESPONSE, result);
    response.setContentType("text/xml");
    OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
    osw.write(xml);
    osw.flush();
  }

  /**
   * Verifies the existance of the SOAPAction header
   *
   * @param request
   *          the HTTP servlet request.
   *
   * @return "OK" if no invalid claims are present; "FAILED" otherwise.
   *
   * @throws Exception
   */
  protected String verifySoapActionHeader(HttpServletRequest request) {
    String result = "FAILED";
    String headerValue = request.getHeader("SOAPAction");
    if (headerValue != null) {
      if ((headerValue.startsWith("\"") && headerValue.endsWith("\""))
          || (headerValue.startsWith("\'") && headerValue.endsWith("\'"))) {
        result = "OK";
      } else {
        result = "FAILED: the header SOAPAction was not a quoted value:"
            + headerValue;
      }
    } else {
      result = "FAILED: the header SOAPAction was not found";
    }
    return result;
  }
}
