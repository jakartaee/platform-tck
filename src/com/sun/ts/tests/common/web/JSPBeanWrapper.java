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

package com.sun.ts.tests.common.web;

import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import com.sun.ts.lib.util.TSNamingContext;

/**
 *
 * @see com.sun.ts.tests.common.web.WebServer
 */
public class JSPBeanWrapper implements java.io.Serializable {

  protected TSNamingContext nctx = null;

  /** No args constructor */
  public JSPBeanWrapper() {
    try {
      WebUtil.logMsg("[JSPBeanWrapper] Getting Naming Context");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      WebUtil.logErr("[JSPBeanWrapper] Unexpected exception", e);
    }
  }

  public String executeTest(HttpServletRequest request)
      throws ServletException {

    Properties resultProps;

    WebUtil.logTrace("[JSPBeanWrapper] executeTest()");
    resultProps = WebUtil.executeTest(this, nctx, request);

    return WebUtil.propsToString(resultProps);
  }

}
