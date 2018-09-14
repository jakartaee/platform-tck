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
import java.util.Map;
import java.io.IOException;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.TSLogger;

public class TSRequestWrapper extends HttpServletRequestWrapper {
  private TSLogger logger = null;

  Map optionsMap = null;

  public TSRequestWrapper(HttpServletRequest request) {
    super(request);
    logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
    logMsg("TSRequestWrapper constructor called");
  }

  @Override
  public Object getAttribute(String name) {

    if ("isRequestWrapped".equals(name)) {
      return Boolean.TRUE;
    }

    return super.getAttribute(name);
  }

  @Override
  public boolean authenticate(HttpServletResponse response)
      throws IOException, ServletException {

    boolean bval = super.authenticate(response);

    debug("made it into TSRequestWrapper.authenticate()");

    //
    // NOTE:
    // It is not clear that flow will make it into this method. So we will
    // write out possible errors messages below and then check for occurances
    // of those error messages from within the tests in spi/servlet.
    //

    // do some checks and validation relates to JASPIC 1.1 spec
    // section 3.8.4 (para 1) per assertion JASPIC:SPEC:322
    if (bval) {
      String msg = "";
      // "Both cases, must also ensure that the value returned by calling
      // getAuthType on the HttpServletRequest is consistent in terms of
      // being null or non-null with the value returned by getUserPrincipal."
      if ((super.getAuthType() != null) && super.getRemoteUser() != null) {
        // This is good - both non-null so this is okay
        msg = "HttpServletRequest authentication results match with getAuthType() and getRemoteUser()";
      } else if ((super.getAuthType() == null)
          && super.getRemoteUser() == null) {
        // This is good - both null, so this is okay too
        msg = "HttpServletRequest authentication results match with getAuthType() and getRemoteUser()";
      } else {
        // This is bad - must be mismatch between getAuthType() and
        // getRemoteUser()
        msg = "ERROR - HttpServletRequest authentication result mis-match with getAuthType() and getRemoteUser()";
      }
      logger.log(Level.INFO, msg);
    }

    // test for assertion: JASPIC:SPEC:323 from spec section 3.8.4, para 2:
    // check if getAuthType() != null, and if not null, then check if
    // MessageInfo Map
    // sets/users key=javax.servlet.http.authType. If so, getAuthType should be
    // set
    // set to value of key. getAuthType should not be null on successful authN.
    if (bval) {
      String msg = "";

      if ((super.getAuthType() != null) && (optionsMap != null)) {
        // see if key=javax.servlet.http.authType exists and if so, make
        // sure it matches the getAuthType() value
        if (optionsMap.get("javax.servlet.http.authType") != null) {
          // if here, then we need to make sure the value specified for
          // getAuthType matches this value.
          String val = (String) optionsMap.get("javax.servlet.http.authType");
          if (val == null) {
            // spec violation - cant be null if key exists!!!
            msg = "ERROR - invalid setting for javax.servlet.http.authType = null";
          } else if (!val.equalsIgnoreCase(super.getAuthType())) {
            // spec violation - these have to match!!
            msg = "ERROR - mismatch value set for javax.servlet.http.authType and getAuthType()";
          } else {
            // we are good if here.
            msg = "getAuthType() matches value for javax.servlet.http.authType";
          }
          logger.log(Level.INFO, msg);
          debug(msg);
          debug("authenticate(): getAuthType() = " + super.getAuthType());
          debug("authenticate(): javax.servlet.http.authType  = " + val);
        }
      }

    }

    return bval;
  }

  public void setOptionsMap(Map options) {
    optionsMap = options;
  }

  public Map getOptionsMap() {
    return optionsMap;
  }

  public void logMsg(String str) {
    if (logger != null) {
      logger.log(Level.INFO, str);
    } else {
      System.out.println("*** TSLogger Not Initialized properly ***");
      System.out.println("*** TSSVLogMessage : ***" + str);
    }
  }

  public void debug(String str) {
    System.out.println("TSRequestWrapper:  " + str);
  }

}
