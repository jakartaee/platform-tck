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

package com.sun.ts.tests.ejb30.assembly.metainfandlibdir;

import com.sun.ts.tests.ejb30.assembly.common.AssemblyCommonIF;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyLocalIF;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.servlet.common.util.Data;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;

public class EJBInjectionFilterBase implements Filter {
  private boolean postConstructCalled;

  @EJB(name = "assemblyBeanInFilter", beanInterface = AssemblyLocalIF.class)
  private AssemblyCommonIF assemblyBean;

  // helloBean is deployed in a separate ejb module (see tests/ejb30/common/
  // helloejbjar/). This ejb-ref is resolved by sun-web.xml
  @EJB(name = "helloBeanInFilter")
  private HelloRemoteIF helloBean;

  @PostConstruct
  final void postConstruct() {
    postConstructCalled = true;
    if (helloBean != null && helloBean instanceof HelloRemoteIF) {
      // good
    } else {
      throw new IllegalStateException(
          "helloBean field has not been correctly injected when post-construct method is called."
              + helloBean);
    }
    if (assemblyBean != null && assemblyBean instanceof AssemblyLocalIF) {
      // good
    } else {
      throw new IllegalStateException(
          "assemblyBean field has not been correctly injected when post-construct method is called."
              + assemblyBean);
    }
  }

  private FilterConfig filterConfig = null;

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    PrintWriter pw = response.getWriter();
    String testname = request.getParameter("testname");
    StringBuilder sb = new StringBuilder();
    if ("ejbInjectionInFilterTest".equals(testname)) {
      // perform test logic for this test only. Otherwise, just pass thru.
      boolean pass1 = false;
      boolean pass2 = false;
      if (assemblyBean != null && assemblyBean instanceof AssemblyLocalIF) {
        pass1 = true;
        sb.append("assemblyBean field has been correctly injected:");
        sb.append(assemblyBean);
      } else {
        sb.append("assemblyBean field has not been correctly injected:");
        sb.append(assemblyBean);
      }
      if (helloBean != null && helloBean instanceof HelloRemoteIF) {
        pass2 = true;
        sb.append("helloBean field has been correctly injected:");
        sb.append(helloBean);
      } else {
        sb.append("helloBean field has not been correctly injected:");
        sb.append(helloBean);
      }
      if (postConstructCalled) {
        sb.append(" postConstruct method in servlet filter has been called.");
      } else {
        sb.append(
            " postConstruct method in servlet filter has not been called.");
      }
      sb.append(" ").append(
          (pass1 && pass2 && postConstructCalled) ? Data.PASSED : Data.FAILED);
      pw.println(sb.toString());
    }
    try {
      chain.doFilter(request, response);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public void destroy() {
  }

  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

}
