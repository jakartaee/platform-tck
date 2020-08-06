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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1011;

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import jakarta.xml.soap.SOAPEnvelope;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  final String PASSED = "PASSED";

  final String FAILED = "FAILED";

  protected void processInboundMessage(SOAPMessageContext context) {
    String result;
    System.out.println("in ServerSOAPHandler:processInboundMessage");
    JAXWS_Util.dumpHTTPHeaders(context);
    try {
      result = verifyChildren(context);
    } catch (Exception e) {
      result = "FAILED";
    }
    if (!result.equals(PASSED)) {
      throw new RuntimeException(
          "In ServerSOAPHandler:processInboundMessage: " + result);
    }
  }

  /**
   * Verifies the env:Envelope children.
   * 
   * @param request
   *          the HTTP servlet request.
   * 
   * @return "PASSED" if no invalid claims are present; "FAILED" otherwise.
   * 
   * @throws Exception
   */
  protected String verifyChildren(SOAPMessageContext context) throws Exception {
    SOAPMessage sm = context.getMessage();
    SOAPPart sp = sm.getSOAPPart();
    SOAPEnvelope se = sp.getEnvelope();

    Element envelope = se;
    if (!isElement(envelope, "http://schemas.xmlsoap.org/soap/envelope/",
        "Envelope")) {
      return "FAILED";
    }
    NodeList list = envelope.getChildNodes();
    boolean hasBody = false;
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() != Node.ELEMENT_NODE) {
        continue;
      }
      if (hasBody) {
        return "FAILED";
      } else {
        hasBody = isElement((Element) node,
            "http://schemas.xmlsoap.org/soap/envelope/", "Body");
      }
    }
    return "PASSED";
  }

  protected boolean isElement(Element element, String namespaceURI,
      String localName) {
    if (!namespaceURI.equals(element.getNamespaceURI())) {
      return false;
    }
    return localName.equals(element.getLocalName());
  }
}
