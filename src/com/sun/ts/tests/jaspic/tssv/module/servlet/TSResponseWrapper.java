/*
 * Copyright (c)  2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaspic.tssv.module.servlet;

import java.util.logging.Level;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.TSLogger;

public class TSResponseWrapper extends HttpServletResponseWrapper {
  private TSLogger logger = null;

  public TSResponseWrapper(HttpServletResponse response) {
    super(response);
    logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
    logMsg("TSResponseWrapper constructor called");
  }

  @Override
  public String getHeader(String name) {

    if ("isResponseWrapped".equals(name)) {
      return "true";
    }

    return super.getHeader(name);
  }

  public void logMsg(String str) {
    if (logger != null) {
      logger.log(Level.INFO, str);
    } else {
      System.out.println("*** TSLogger Not Initialized properly ***");
      System.out.println("*** TSSVLogMessage : ***" + str);
    }
  }

}
