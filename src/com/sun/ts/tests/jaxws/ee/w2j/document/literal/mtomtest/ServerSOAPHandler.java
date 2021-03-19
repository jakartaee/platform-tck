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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomtest;

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceException;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  private static final String PASSED = "PASSED";

  private static final String FAILED = "FAILED";

  protected void processInboundMessage(SOAPMessageContext context) {
    System.out.println("in processInboundMessage");
    if (Handler_Util.checkForMsg(context,
        "MTOMInOut2RequestCheckHttpHeadersTest")) {
      JAXWS_Util.dumpHTTPHeaders(context);
      Map<String, List<String>> map = (Map<String, List<String>>) context
          .get(MessageContext.HTTP_REQUEST_HEADERS);
      String result = verifyContentTypeHttpHeader(map);
      if (!result.equals(PASSED)) {
        throw new RuntimeException(
            "In ServerSOAPHandler:processInboundMessage: " + result);
      }
    }
  }

  /**
   * Verifies the contents of the Content-Type HTTP header
   *
   * @param request
   *          the HTTP servlet request.
   */
  protected String verifyContentTypeHttpHeader(Map<String, List<String>> m) {
    String result = FAILED;
    Map<String, List<String>> map = JAXWS_Util.convertKeysToLowerCase(m);
    List<String> values = map.get("content-type");
    System.out.println("DEBUG: HTTP Content-Type header=" + values);
    String sValues = values.toString().toLowerCase();
    if (sValues != null) {
      if ((sValues.indexOf("multipart/related") >= 0)
          && (sValues.indexOf("text/xml") >= 0)
          && (sValues.indexOf("application/xop+xml") >= 0)) {
        result = PASSED;
      } else {
        result = FAILED
            + ": HTTP Content-Type header does not contain expected [multipart/related, text/xml, application/xop+xml]";
      }
    } else {
      result = FAILED + ": HTTP Content-Type header not found";
    }
    System.out.println("result=" + result);
    return result;
  }

}
