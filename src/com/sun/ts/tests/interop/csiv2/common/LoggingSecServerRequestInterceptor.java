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

import org.omg.CORBA.*;
import org.omg.IOP.*;
import org.omg.PortableInterceptor.*;

import com.sun.enterprise.iiop.*;
import com.sun.enterprise.iiop.security.*;
import com.sun.enterprise.util.*;

import org.omg.IOP.ServiceContext;

import javax.net.ssl.SSLSocket;

import com.sun.corba.ee.spi.ior.IOR;
import com.sun.corba.ee.spi.legacy.connection.Connection;
import com.sun.corba.ee.spi.legacy.interceptor.RequestInfoExt;

import java.net.Socket;

import com.sun.logging.LogDomains;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Replacement for SecServerInterceptor that also maintains a log. This class
 * was designed to be used for the CSIv2 TS tests.
 */
public class LoggingSecServerRequestInterceptor
    extends SecServerRequestInterceptor {

  private static Logger _logger = null;

  static {
    _logger = LogDomains.getLogger(LoggingSecServerRequestInterceptor.class,
        "com.sun.ts.tests.interop.csiv2.common.LoggingSecServerRequestInterceptor");
  }

  /** Here until this constant is more accessilble. */
  private static final int SECURITY_ATTRIBUTE_SERVICE_ID = 15;

  /** For unmarshalling service contexts */
  private Codec codec;

  public LoggingSecServerRequestInterceptor(String name, Codec codec) {
    super(name, codec);
    this.codec = codec;
    _logger.log(Level.FINE, " LoggingSecServerRequestInterceptor constructed.");

  }

  public String name() {
    return super.name();
  }

  public void destroy() {
    _logger.log(Level.FINE,
        "SI : LoggingSecServerRequestInterceptor destroyed.");
    super.destroy();
  }

  public void receive_request_service_contexts(ServerRequestInfo ri)
      throws ForwardRequest {
    _logger.log(Level.FINE, "SI : rrsc( " + ri.operation() + ").");

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.receive_request_service_contexts(ri);
    } else {
      // Increment watch counter for this method:
      CSIv2Log.incrementWatch(ri.operation());

      NO_PERMISSION np = null;
      StartingPointState startState = collectRelevantStartingState(ri);
      try {
        super.receive_request_service_contexts(ri);
      }

      // In PI, if a starting point throws an exception, the
      // ending point for that interceptor is not invoked. The
      // client and server side interceptor starting points
      // have to be modified to catch the NO_PERMISSION exception.
      // If caught, they need to log the appropriate information
      // and close the log element using </server-interceptor>.

      catch (NO_PERMISSION e) {
        com.sun.ts.lib.util.TestUtil.printStackTrace(e);
        np = e;
      } finally {
        CSIv2Log log = CSIv2Log.getLocalLog();
        logStartServerInterceptor(log, startState, ri);
        if (np != null) {
          log.logEndServerInterceptor();
          throw np;
        }
      }
    }

  }

  public void receive_request(ServerRequestInfo ri) throws ForwardRequest {
    _logger.log(Level.FINE, "SI : receive_request( " + ri.operation() + ").");

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.receive_request(ri);
    } else {
      super.receive_request(ri);
    }

  }

  public void send_reply(ServerRequestInfo ri) {
    _logger.log(Level.FINE, "SI : send_reply( " + ri.operation() + ").");

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.send_reply(ri);
    } else {
      // Decrement watch counter for this method:
      CSIv2Log.decrementWatch(ri.operation());
      try {
        super.send_reply(ri);
      } finally {
        CSIv2Log log = CSIv2Log.getLocalLog();
        logEndServerInterceptor(log, ri);
      }
    }

  }

  public void send_exception(ServerRequestInfo ri) throws ForwardRequest {
    _logger.log(Level.FINE, "SI : send_exception( " + ri.operation() + ").");

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.send_exception(ri);
    } else {
      // Decrement watch counter for this method:
      CSIv2Log.decrementWatch(ri.operation());

      try {
        super.send_exception(ri);
      } finally {
        CSIv2Log log = CSIv2Log.getLocalLog();
        logEndServerInterceptor(log, ri);
      }
    }

  }

  public void send_other(ServerRequestInfo ri) throws ForwardRequest {
    _logger.log(Level.FINE, "SI : send_other( " + ri.operation() + ").");

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.send_other(ri);
    } else {
      // Decrement watch counter for this method:
      CSIv2Log.decrementWatch(ri.operation());

      try {
        super.send_other(ri);
      } finally {
        CSIv2Log log = CSIv2Log.getLocalLog();
        logEndServerInterceptor(log, ri);
      }
    }

  }

  /**
   * Log all relevant starting interception information
   */
  private void logStartServerInterceptor(CSIv2Log log,
      StartingPointState startState, ServerRequestInfo ri) {
    // Log reply service context:
    log.logStartServerInterceptor();

    // Log operation name:
    log.logOperationName(ri.operation());

    // Now, analyze and log the service context:
    LoggingSecInterceptor.logRequestServiceContext(
        SECURITY_ATTRIBUTE_SERVICE_ID, log, ri, codec);

    // Analyze and log whether ssl is being used:
    log.logSSLUsed(startState.socket instanceof SSLSocket);

    // Analyze and log the transport client principals in the certificate
    // chain.
    log.logTransportClientPrincipals(startState.socket);
  }

  /**
   * Log all relevant ending interception information
   */
  private void logEndServerInterceptor(CSIv2Log log, ServerRequestInfo ri) {
    // Log the reply service context:
    LoggingSecInterceptor.logReplyServiceContext(SECURITY_ATTRIBUTE_SERVICE_ID,
        log, ri, codec);

    // Log reply service context:
    log.logEndServerInterceptor();
  }

  /**
   * Collect all relevant starting point state before calling super. This is
   * required because some information may be lost during the call to the
   * security interceptor. In particular, we are concerned about capturing the
   * socket information before it is lost when the interceptor calls the
   * authorization service locally.
   */
  private StartingPointState collectRelevantStartingState(
      ServerRequestInfo ri) {
    StartingPointState result = new StartingPointState();

    // Use the proprietary connection interceptor extension to determine
    // the connection object for this invocation:
    Connection c = ((RequestInfoExt) ri).connection();
    if (c != null) {
      // Retrieve the socket:
      result.socket = c.getSocket();
    }

    return result;
  }

  /**
   * This provides a convenient structure in which to organize any state that is
   * relevant, before we invoke the super interceptor. This structure is
   * appropriate for starting interception points.
   */
  private static class StartingPointState {
    /** The socket on which this invocation was made */
    public Socket socket;
  }

}
