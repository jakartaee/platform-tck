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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2745;

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceException;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  final String PASSED = "PASSED";

  final String FAILED = "FAILED";

  protected void processInboundMessage(SOAPMessageContext context) {
    System.out.println("in ServerSOAPHandler:processInboundMessage");
    JAXWS_Util.dumpHTTPHeaders(context);
    Map<String, List<String>> map = (Map<String, List<String>>) context
        .get(MessageContext.HTTP_REQUEST_HEADERS);
    String result = verifySOAPActionHeader(map);
    if (!result.equals(PASSED)) {
      throw new RuntimeException(
          "In ServerSOAPHandler:processInboundMessage: " + result);
    }
  }

  /**
   * Verifies the contents of the Content-Type HTTP header
   *
   * @param request
   *          the HTTP servlet request.
   */
  protected String verifySOAPActionHeader(Map<String, List<String>> m) {
    System.out.println("in ServerSOAPHandler:verifySOAPActionHeader");
    String result = FAILED;
    Map<String, List<String>> map = JAXWS_Util.convertKeysToLowerCase(m);
    List<String> values = map.get("soapaction");
    System.out.println("HTTP Header soapAction=" + values);
    int len = values.size();
    if (len == 1) {
      String r = values.get(0);
      if ((r == null) || (r.equals("\"\""))) {
        result = PASSED;
      } else {
        result = FAILED + ": unexpected value found in HTTP SOAPAction:["
            + values + "]";
      }
    } else if (len < 1) {
      result = FAILED + ": no values found in HTTP SOAPAction:[" + values + "]";
    } else if (len > 1) {
      result = FAILED + ": unexpected values found in HTTP SOAPAction:["
          + values + "]";
    }
    System.out.println("result=" + result);
    return result;
  }
}
