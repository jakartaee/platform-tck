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

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.R2712;

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceException;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  private String PASSED = "PASSED";

  private String FAILED = "FAILED";

  private String GLOBAL_ELEMENT = "HelloRequestElement";

  protected void processInboundMessage(SOAPMessageContext context) {
    System.out.println("in ServerSOAPHandler:processInboundMessage");
    String result = null;
    try {
      result = verifyChildElement(context);
    } catch (Exception e) {
      throw new RuntimeException(
          "Exception occurred in ServerSOAPHandler:verifyChildElement: " + e);
    }
    if (!result.equals(PASSED)) {
      throw new RuntimeException(
          "In ServerSOAPHandler:processInboundMessage: " + result);
    }
  }

  protected String verifyChildElement(SOAPMessageContext context)
      throws Exception {
    System.out.println("in ServerSOAPHandler:verifyChildElement");
    String result = FAILED;
    SOAPMessage sm = context.getMessage();
    System.out.println("Getting children of body element ...");
    Iterator children = sm.getSOAPBody().getChildElements();
    SOAPElement child;
    String localName;
    int count = 0;
    while (children.hasNext()) {
      count++;
      System.out.println("Getting operation name ...");
      child = (SOAPElement) children.next();
      localName = child.getElementName().getLocalName();
      System.out.println("child localname: " + localName);
      if (localName.equals(GLOBAL_ELEMENT)) {
        if (count == 1) {
          result = PASSED;
        } else {
          result = FAILED + ": The element '" + GLOBAL_ELEMENT + "' was found "
              + count + " time(s) in the soap:body";
        }
      } else {
        result = FAILED + ": Expected element '" + GLOBAL_ELEMENT
            + "' in soap:body, instead got '" + localName + "'";
      }
    }
    if (count == 0) {
      result = "Error: no child elements were found in soap:body";
    }
    System.out.println("result=" + result);
    return result;
  }
}
