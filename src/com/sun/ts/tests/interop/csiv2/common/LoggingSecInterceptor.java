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
 * @(#)LoggingSecInterceptor.java	1.12 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.common;

import org.omg.CORBA.*;
import org.omg.IOP.*;
import org.omg.IOP.CodecPackage.*;
import org.omg.PortableInterceptor.*;
import com.sun.enterprise.iiop.security.*;
import java.util.*;
import com.sun.corba.ee.org.omg.CSI.*;

/**
 * Common static code for the client and server LoggingSec interceptors.
 */
public class LoggingSecInterceptor {

  /**
   * A vector of Strings. Only operations with the given names will be logged.
   */
  private static Vector filters = new Vector();

  // Following padding spaces are necessary to format csiv2 log output

  // Padding space used for formatting request and reply service context and
  // its sub elements log output
  //
  private static String svc_cntx_pad = "             "; // 14 spaces(2*(8-1)

  private static String svc_cntx_sub_pad = "               "; // 16
                                                              // spaces(2*(9-1)

  /**
   * Static initializer for initial filter list
   */
  static {
    // For home interface testing:
    // addFilter( "createInvoke" );
    addFilter("create");

    // For remote interface testing:
    addFilter("invoke");
  }

  /**
   * Adds the given operation name to the list of filters (if not already
   * present). Only operations that match the names in the filter list return an
   * active status of true when isActive is called.
   */
  public static void addFilter(String operationName) {
    if (!filters.contains(operationName)) {
      filters.addElement(operationName);
    }
  }

  /**
   * Returns true if interceptors are currently enabled and the operation name
   * appears in the filter list. Note that this implicitly eliminates infinite
   * recursion unless "log" appears in the filters list, since we never invoke
   * any other remote method apart from log in these particular interceptors.
   */
  static boolean isActive(RequestInfo ri) {
    // true if the operation name appears in filter list.
    boolean filtered = filters.contains(ri.operation());
    boolean watchlist = CSIv2Log.watchList.containsKey(ri.operation());

    return (filtered || watchlist);
  }

  /**
   * Logs the request service context with the given ID to the given log,
   * getting the information from the given RequestInfo object.
   */
  public static void logRequestServiceContext(int id, CSIv2Log log,
      RequestInfo ri, Codec codec) {
    String logEntry;
    ServiceContext sc = null;
    try {
      sc = ri.get_request_service_context(id);
      if (sc != null) {
        Any scAny = codec.decode_value(sc.context_data,
            SASContextBodyHelper.type());
        SASContextBody sasctxbody = SASContextBodyHelper.extract(scAny);
        switch (sasctxbody.discriminator()) {
        case MTEstablishContext.value:
          EstablishContext establishMsg = sasctxbody.establish_msg();
          logEntry = svc_cntx_pad + "<establish-context>\n"
              + establishContextXML(establishMsg) + svc_cntx_pad
              + "</establish-context>";
          break;
        default:
          logEntry = svc_cntx_pad + "<invalid-message details=\"MT="
              + sasctxbody.discriminator() + "\"/>\n";
        }
      } else {
        // com.sun.ts.lib.util.TestUtil.printStackTrace(e);
        // ServiceContext is null, unexpected behavior!
        logEntry = null;

      }
    } catch (FormatMismatch e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      // Could not decode.
      logEntry = svc_cntx_pad
          + "<invalid-message details=\"Could not decode.\"/>\n";
    } catch (BAD_PARAM e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      // BAD_PARAM with minor code 26 is thrown if no entry
      logEntry = null;
    } catch (TypeMismatch e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      // Could not decode.
      logEntry = "<invalid-message details=\"Could not decode. caught TypeMismatch Exception\"/>\n";
    }

    log.logRequestServiceContext(logEntry);
  }

  /**
   * Returns an XML encoding of the given EstablishContext message
   */
  private static String establishContextXML(EstablishContext establishContext) {
    String result;

    // Log client context ID
    result = svc_cntx_sub_pad + "<client-context-id>"
        + establishContext.client_context_id + "</client-context-id>\n";

    // Log identity token
    result += svc_cntx_sub_pad + "<identity-token>\n";
    IdentityToken identityToken = establishContext.identity_token;
    switch (identityToken.discriminator()) {
    case ITTAbsent.value:
      result += svc_cntx_sub_pad + "  <absent/>\n";
      break;
    case ITTAnonymous.value:
      result += svc_cntx_sub_pad + "  <anonymous/>\n";
      break;
    case ITTPrincipalName.value:
      result += svc_cntx_sub_pad + "  <principal-name>"
          + CSIv2Log.binHex(identityToken.principal_name())
          + "</principal-name>\n";
      break;
    case ITTX509CertChain.value:
      result += svc_cntx_sub_pad + "  <certificate-chain>"
          + CSIv2Log.binHex(identityToken.certificate_chain())
          + "</certificate-chain>\n";
      break;
    case ITTDistinguishedName.value:
      result += svc_cntx_sub_pad + "  <distinguished-name>"
          + CSIv2Log.binHex(identityToken.dn()) + "</distinguished-name>\n";
      break;
    default:
      result += svc_cntx_sub_pad + "  <unknown-type details=\""
          + identityToken.discriminator() + "\"/>\n";
      break;
    }
    result += svc_cntx_sub_pad + "</identity-token>\n";

    // Log client authentication token
    result += svc_cntx_sub_pad + "<client-auth-token>"
        + CSIv2Log.binHex(establishContext.client_authentication_token)
        + "</client-auth-token>\n";

    // Log number of authorization tokens.
    result += svc_cntx_sub_pad + "<authz-token-count>"
        + establishContext.authorization_token.length
        + "</authz-token-count>\n";

    return result;
  }

  /**
   * Logs the reply service context with the given ID to the given log, getting
   * the information from the given RequestInfo object.
   */
  public static void logReplyServiceContext(int id, CSIv2Log log,
      RequestInfo ri, Codec codec) {
    String logEntry;
    ServiceContext sc = null;
    try {
      sc = ri.get_reply_service_context(id);
      if (sc != null) {

        Any scAny = codec.decode_value(sc.context_data,
            SASContextBodyHelper.type());
        SASContextBody sasctxbody = SASContextBodyHelper.extract(scAny);
        switch (sasctxbody.discriminator()) {
        case MTCompleteEstablishContext.value:
          CompleteEstablishContext cecMessage = sasctxbody.complete_msg();
          logEntry = svc_cntx_pad + "<complete-establish-context>\n"
              + completeEstablishContextXML(cecMessage) + svc_cntx_pad
              + "</complete-establish-context>";
          break;
        case MTContextError.value:
          ContextError errorMessage = sasctxbody.error_msg();
          logEntry = svc_cntx_pad + "<context-error>\n"
              + contextErrorXML(errorMessage) + svc_cntx_pad
              + "</context-error>";
          break;
        default:
          logEntry = svc_cntx_pad + "<invalid-message details=\"MT="
              + sasctxbody.discriminator() + "\"/>\n";
        }
      } else {
        // com.sun.ts.lib.util.TestUtil.printStackTrace(e);
        // ServiceContext is null, unexpected behavior!
        logEntry = null;

      }
    } catch (FormatMismatch e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      // Could not decode.
      logEntry = svc_cntx_pad
          + "<invalid-message details=\"Could not decode.\"/>\n";
    } catch (BAD_PARAM e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      // BAD_PARAM with minor code 26 is thrown if no entry
      logEntry = null;
    } catch (TypeMismatch e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      // Could not decode.
      logEntry = "<invalid-message details=\"Could not decode. caught TypeMismatch Exception\"/>\n";
    }

    log.logReplyServiceContext(logEntry);
  }

  /**
   * Returns an XML encoding of the given EstablishContext message
   */
  private static String completeEstablishContextXML(
      CompleteEstablishContext completeEstablishContext) {
    String result;

    // Log client context ID
    result = svc_cntx_sub_pad + "<client-context-id>"
        + completeEstablishContext.client_context_id + "</client-context-id>\n";

    // Log client stateful
    result += svc_cntx_sub_pad + "<context-stateful>"
        + completeEstablishContext.context_stateful + "</context-stateful>\n";

    // Log final context token
    result += svc_cntx_sub_pad + "<final-context-token>"
        + CSIv2Log.binHex(completeEstablishContext.final_context_token)
        + "</final-context-token>\n";

    return result;
  }

  /**
   * Returns an XML encoding of the given EstablishContext message
   */
  private static String contextErrorXML(ContextError contextError) {
    String result;

    // Log client context ID
    result = svc_cntx_sub_pad + "<client-context-id>"
        + contextError.client_context_id + "</client-context-id>\n";

    // Log major status, minor status
    result += svc_cntx_sub_pad + "<major-status>" + contextError.major_status
        + "</major-status>\n";
    result += svc_cntx_sub_pad + "<minor-status>" + contextError.minor_status
        + "</minor-status>\n";

    // Log error token:
    result += svc_cntx_sub_pad + "<error-token>"
        + CSIv2Log.binHex(contextError.error_token) + "</error-token>\n";

    return result;
  }

}
