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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R11XX;

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
      result = verifySoapActionHeader(context);
    } catch (Exception e) {
      throw new RuntimeException(
          "Exception occurred in ServerSOAPHandler:verifySoapActionHeader: "
              + e);
    }
    if (!result.equals(PASSED)) {
      throw new RuntimeException(
          "In ServerSOAPHandler:processInboundMessage: " + result);
    }
  }

  protected String verifySoapActionHeader(SOAPMessageContext context)
      throws Exception {
    System.out.println("in ServerSOAPHandler:verifySoapActionHeader");
    String result = FAILED;

    JAXWS_Util.dumpHTTPHeaders(context);
    Map<String, List<String>> map1 = (Map<String, List<String>>) context
        .get(MessageContext.HTTP_REQUEST_HEADERS);
    Map<String, List<String>> map2 = JAXWS_Util.convertKeysToLowerCase(map1);
    List<String> values = map2.get("soapaction");
    if (values != null) {
      System.out.println("SoapAction=" + values);
      if (values.contains("\"helloWorld\"")
          || values.contains("\'helloWorld\'")) {
        result = "PASSED";
      } else {
        result = "FAILED: the header SOAPAction was not a quoted value:"
            + values;
      }
    } else {
      result = "FAILED: the header SOAPAction was not found";
    }
    return result;
  }

}
