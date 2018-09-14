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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.common.filters;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import com.sun.ts.tests.jstl.common.wrappers.FormatResponseWrapper;
import com.sun.ts.tests.jstl.common.wrappers.FormatRequestWrapper;

/*
 * Simple Filter to wrap requests and responses for the fmt tests.
 */

public class FormatFilter implements Filter {

  /**
   * Filter configuration
   */
  private FilterConfig _config = null;

  /** Creates new FormatFilter */
  public FormatFilter() {
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Called by the container to initialize this filter.
   *
   * @param config
   *          filter configuration.
   */
  public void init(FilterConfig config) {
    _config = config;
  }

  /**
   * When called by the container, the current request and response are wrapped
   * by an instance of FormatRequestWrapper and FormatResponseWrapper
   * respectively. Doing this allows the "logging" of certain events that must
   * take place when using certain formatting actions.
   */
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    FormatResponseWrapper resWrapper = new FormatResponseWrapper(
        (HttpServletResponse) response);
    FormatRequestWrapper reqWrapper = new FormatRequestWrapper(
        (HttpServletRequest) request);
    chain.doFilter(reqWrapper, resWrapper);
  }

  /**
   * Called by the container to destroy this instance.
   */
  public void destroy() {
    _config = null;
  }
}
