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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1012;

import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public final class EncodingCheckFilter implements Filter, SOAPRequests {

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
    System.out.println("Here in EncodingCheckFilter.doFilter()\n");
    if (filterConfig == null) {
      result = "EXCEPTION";
    } else {
      try {
        result = verifyEncoding((HttpServletRequest) request);
      } catch (Exception e) {
        com.sun.ts.lib.util.TestUtil.printStackTrace(e);
        result = "EXCEPTION";
      }
    }
    String xml = MessageFormat.format(R1012_RESPONSE, result);
    response.setContentType("text/xml");
    OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
    osw.write(xml);
    osw.flush();
  }

  /**
   * Verifies the encoding of the request.
   * 
   * @param request
   *          the HTTP servlet request.
   * 
   * @return "OK" if no invalid claims are present; "FAILED" otherwise.
   * 
   * @throws Exception
   */
  protected String verifyEncoding(HttpServletRequest request) throws Exception {
    Charset cs = Charset.forName("UTF-8");
    String contentType = request.getContentType();
    if (contentType != null) {
      int index = contentType.toLowerCase().indexOf("charset=");
      if (index > 0) {
        String name = contentType.substring(index + 8).trim();
        char c = name.charAt(0);
        if ((c == '\"') || (c == '\'')) {
          name = name.substring(1, name.length() - 1);
        }
        if ((name.equalsIgnoreCase("UTF-8"))
            || (name.equalsIgnoreCase("UTF-16"))) {
          cs = Charset.forName(name);
        } else {
          return "FAILED";
        }
      }
    }
    InputStreamReader isr = new InputStreamReader(request.getInputStream(), cs);
    char[] buffer = new char[1024];
    int length;
    try {
      do {
        length = isr.read(buffer);
      } while (length > 0);
    } catch (IOException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      return "FAILED";
    }
    return "OK";
  }
}
