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

package com.sun.ts.tests.interop.csiv2.common;

import com.sun.logging.LogDomains;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.omg.PortableInterceptor.ServerRequestInterceptor;
import com.sun.enterprise.iiop.security.AlternateSecurityInterceptorFactory;
import org.glassfish.internal.api.Globals;
import org.omg.IOP.Codec;

/**
 *
 * @author Stephen DiMilla
 */
public class LoggingSecRequestInterceptorFactory
    implements AlternateSecurityInterceptorFactory {

  private static Logger _logger = null;

  static {
    _logger = LogDomains.getLogger(LoggingSecRequestInterceptorFactory.class,
        "com.sun.ts.tests.interop.csiv2.common.LoggingSecRequestInterceptorFactory");
  }

  private LoggingSecClientRequestInterceptor altClient;

  private LoggingSecServerRequestInterceptor altServer;

  public ClientRequestInterceptor getClientRequestInterceptor(Codec codec) {
    _logger.log(Level.FINE,
        "CTS : in LoggingSecRequestInterceptorFactory:getClientRequestInterceptor()");

    return _getClientRequestInterceptor(codec);
  }

  public ServerRequestInterceptor getServerRequestInterceptor(Codec codec) {
    _logger.log(Level.FINE,
        "CTS : in LoggingSecRequestInterceptorFactory:getServerRequestInterceptor()");

    return _getServerRequestInterceptor(codec);
  }

  private synchronized ClientRequestInterceptor _getClientRequestInterceptor(
      Codec codec) {
    if (altClient == null) {
      altClient = new LoggingSecClientRequestInterceptor(
          "SecClientRequestInterceptor", codec);
    }
    return altClient;
  }

  private synchronized ServerRequestInterceptor _getServerRequestInterceptor(
      Codec codec) {
    if (altServer == null) {
      altServer = new LoggingSecServerRequestInterceptor(
          "SecServerRequestInterceptor", codec);
    }
    return altServer;
  }

}
