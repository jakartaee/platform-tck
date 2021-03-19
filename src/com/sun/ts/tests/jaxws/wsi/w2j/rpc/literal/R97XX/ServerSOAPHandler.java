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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R97XX;

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceException;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

import org.w3c.dom.Element;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import jakarta.xml.soap.SOAPEnvelope;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  private String PASSED = "PASSED";

  private String FAILED = "FAILED";

  protected void processInboundMessage(SOAPMessageContext context) {
    System.out.println("in ServerSOAPHandler:processInboundMessage");
    String result = null;
    try {
      result = verifyXMLEnvSerialization(context);
    } catch (Exception e) {
      throw new RuntimeException(
          "Exception occurred in ServerSOAPHandler:verifyXMLEnvSerialization: "
              + e);
    }
    System.out.println("result=" + result);
    if (!result.equals(PASSED)) {
      throw new RuntimeException(
          "In ServerSOAPHandler:processInboundMessage: " + result);
    }
  }

  protected String verifyXMLEnvSerialization(SOAPMessageContext context)
      throws Exception {
    System.out.println("in ServerSOAPHandler:verifyXMLEnvSerialization");
    String result = FAILED;

    JAXWS_Util.dumpHTTPHeaders(context);
    Map<String, List<String>> map1 = (Map<String, List<String>>) context
        .get(MessageContext.HTTP_REQUEST_HEADERS);
    Map<String, List<String>> map2 = JAXWS_Util.convertKeysToLowerCase(map1);
    List<String> values = map2.get("content-type");
    System.out.println("HTTP header Content-Type=" + values);
    String sValues = values.toString().toLowerCase();
    if (sValues != null) {
      int index = sValues.indexOf("text/xml");
      if (index < 0) {
        result = FAILED + ": Content-Type header not text/xml [" + sValues
            + "]";
        return result;
      }
      index = sValues.indexOf("charset=");
      if (index > 0) {
        if ((sValues.indexOf("utf-8") >= 0)
            || (sValues.indexOf("utf-16") >= 0)) {
          result = PASSED;
        } else {
          result = FAILED + ":charset did not equal utf-8 or utf-16, it was |"
              + sValues + "|";
        }
      } else {
        result = FAILED + ": charset not found in HTTP Content-Type [" + sValues
            + "]";
      }
    } else {
      result = FAILED + ": the HTTP header Content-Type was not found";
    }
    if (!result.equals(PASSED)) {
      return result;
    }

    try {
      // Testing for serialization of envelope as XML 1.0

      System.out.println("Testing SOAPEnvelope");
      SOAPMessage sm = context.getMessage();
      SOAPPart sp = sm.getSOAPPart();
      SOAPEnvelope se = sp.getEnvelope();
      if (!isElement(se, "http://schemas.xmlsoap.org/soap/envelope/",
          "Envelope")) {
        result = FAILED + ": 'env:Envelope' element not received";
      } else {
        result = PASSED;
      }
    } catch (Exception e) {
      result = "FAILED: exception occurred:" + e;
    }
    return result;
  }

  private boolean isElement(Element element, String namespaceURI,
      String localName) {
    if (!namespaceURI.equals(element.getNamespaceURI())) {
      return false;
    }
    return localName.equals(element.getLocalName());
  }

}
