/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @(#)R2739Handler.java	1.3 03/05/20
 */

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2739;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import com.sun.ts.tests.jaxws.common.Constants;
import com.sun.ts.tests.jaxws.common.Handler_Util;

import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

public class R2739Handler implements SOAPHandler<SOAPMessageContext> {

  public Set<QName> getHeaders() {
    Set<QName> s = new HashSet<QName>();
    s.add(new QName("http://extra-header.org", "extra-header"));
    return s;
  }

  public void init(java.util.Map<String, Object> config) {
  };

  public boolean handleFault(SOAPMessageContext context) {
    return true;
  };

  public void destroy() {
  };

  public void close(MessageContext context) {
  };

  public boolean handleMessage(SOAPMessageContext context) {
    System.out.println("HANDLER: R2739Handler.handleMessage() BEGIN");
    if (Handler_Util.getDirection(context).equals(Constants.INBOUND)) {
      logSOAPMessage("Original", "Request", context);
      handle(context);
      logSOAPMessage("Modified", "Request", context);
    } else {
      logSOAPMessage("Original", "Response", context);
      handle(context);
      logSOAPMessage("Modified", "Response", context);
    }
    System.out.println("HANDLER: R2739Handler.handleMessage() END");
    return true;
  }

  private void handle(MessageContext context) {
    try {
      addExtraHeader((SOAPMessageContext) context);
    } catch (SOAPException se) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(se);
    }
  }

  private void addExtraHeader(SOAPMessageContext context) throws SOAPException {
    SOAPEnvelope env = context.getMessage().getSOAPPart().getEnvelope();
    if (env.getHeader() == null) {
      SOAPHeader header = env.addHeader();
      SOAPHeaderElement she = header.addHeaderElement(getExtraHeaderName(env));
      context.getMessage().saveChanges();
    }
  }

  private Name getExtraHeaderName(SOAPEnvelope env) throws SOAPException {
    return env.createName("extra-header", "ns1", "http://extra-header.org");
  }

  private void logSOAPMessage(String s1, String s2, MessageContext msgctx) {
    try {
      SOAPMessage msg = ((SOAPMessageContext) msgctx).getMessage();
      System.out.println("-----------------------------");
      System.out.println(s1 + " SOAP Message " + s2);
      System.out.println("-----------------------------");
      msg.writeTo(System.out);
      System.out.println("\n");
    } catch (Exception e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
    }
  }
}
