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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1016;

import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.common.Constants;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;

import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.soap.*;
import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class XMLLangHandler implements SOAPHandler<SOAPMessageContext> {

  public Set<QName> getHeaders() {
    return new HashSet<QName>();
  }

  public void init(java.util.Map<String, Object> config) {
  };

  public boolean handleFault(SOAPMessageContext context) {
    System.out.println("HANDLER: XMLLangHandler.handleFault() BEGIN");
    JAXWS_Util.dumpSOAPMessage(context.getMessage(), false);
    try {
      if (Handler_Util.getDirection(context).equals(Constants.OUTBOUND)) {
        System.out.println(
            "HANDLER: XMLLangHandler.handleFault() direction=outbound");
        addXMLLangAttribute((SOAPMessageContext) context);
      } else {
        System.out
            .println("HANDLER: XMLLangHandler.handleFault() direction=inbound");
      }
    } catch (SOAPException se) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(se);
    }
    System.out.println("HANDLER: XMLLangHandler.handleFault() END");
    return true;
  };

  public void destroy() {
  };

  public void close(MessageContext context) {
  };

  public boolean handleMessage(SOAPMessageContext context) {
    if (Handler_Util.getDirection(context).equals(Constants.OUTBOUND)) {
      System.out.println(
          "HANDLER: XMLLangHandler.handleMessage() direction=outbound");
    } else {
      System.out
          .println("HANDLER: XMLLangHandler.handleMessage() direction=inbound");
    }
    return true;
  }

  private void addXMLLangAttribute(SOAPMessageContext context)
      throws SOAPException {

    Iterator children;
    SOAPElement fault = context.getMessage().getSOAPPart().getEnvelope()
        .getBody().getFault();
    if (fault == null)
      return;
    children = fault.getChildElements();
    SOAPElement child;
    while (children.hasNext()) {
      child = (SOAPElement) children.next();
      if (child.getElementName().getLocalName().equals("faultstring")) {
        child.addAttribute(getXMLLangName(SOAPFactory.newInstance()), "en");
      }
    }
    context.getMessage().saveChanges();
  }

  private Name getXMLLangName(SOAPFactory factory) throws SOAPException {
    return factory.createName("lang", "xml", "");
  }
}
