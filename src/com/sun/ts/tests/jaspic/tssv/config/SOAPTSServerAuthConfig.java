/*
 * Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaspic.tssv.config;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import javax.security.auth.callback.CallbackHandler;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.TSLogger;

import jakarta.security.auth.message.MessageInfo;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPTSServerAuthConfig extends TSServerAuthConfig {
  
  protected SOAPTSServerAuthConfig(String layer, String applicationCtxt,
      CallbackHandler cbkHandler, Map props) {
    super(layer, applicationCtxt, cbkHandler, props);
  }

  public SOAPTSServerAuthConfig(String layer, String applicationCtxt,
      CallbackHandler cbkHandler, Map props, TSLogger tsLogger) {

    super(layer, applicationCtxt, cbkHandler, props);

    if (tsLogger != null) {
      logger = tsLogger;
    }

    String str = "TSServerAuthConfig called for layer=" + layer
        + " : appContext=" + applicationCtxt;
    logger.log(Level.INFO, str);
  }

  @Override
  public String getAuthContextID(MessageInfo messageInfo) {
    logger.log(Level.INFO, "getAuthContextID called");
    String rval = null;

    if (messageLayer.equals(JASPICData.LAYER_SOAP)) {

      rval = getOpName((SOAPMessage) messageInfo.getRequestMessage());

      String logMsg = "getAuthContextID() called for layer=" + messageLayer;
      logMsg += " shows AuthContextId=" + rval;
      logger.log(Level.INFO, logMsg);

    } else if (messageLayer.equals(JASPICData.LAYER_SERVLET)) {
      
      super.getAuthContextID(messageInfo);

    } else {

      rval = null;

    }   

    return rval;
  }

  private String getOpName(SOAPMessage message) {
    if (message == null) {
      return null;
    }

    String rvalue = null;

    // first look for a SOAPAction header.
    // this is what .net uses to identify the operation

    MimeHeaders headers = message.getMimeHeaders();
    if (headers != null) {
      String[] actions = headers.getHeader("SOAPAction");
      if (actions != null && actions.length > 0) {
        rvalue = actions[0];
        if (rvalue != null && rvalue.equals("\"\"")) {
          rvalue = null;
        }
      }
    }

    // if that doesn't work then we default to trying the name
    // of the first child element of the SOAP envelope.

    if (rvalue == null) {
      Name name = getName(message);
      if (name != null) {
        rvalue = name.getLocalName();
      }
    }

    return rvalue;
  }

  private Name getName(SOAPMessage message) {
    Name rvalue = null;
    SOAPPart soap = message.getSOAPPart();
    if (soap != null) {
      try {
        SOAPEnvelope envelope = soap.getEnvelope();
        if (envelope != null) {
          SOAPBody body = envelope.getBody();
          if (body != null) {
            Iterator it = body.getChildElements();
            while (it.hasNext()) {
              Object o = it.next();
              if (o instanceof SOAPElement) {
                rvalue = ((SOAPElement) o).getElementName();
                break;
              }
            }
          }
        }
      } catch (SOAPException se) {
        if (logger.isLoggable(Level.FINE)) {
          logger.log(Level.FINE, "WSS: Unable to get SOAP envelope", se);
        }
      }
    }

    return rvalue;
  }

}
