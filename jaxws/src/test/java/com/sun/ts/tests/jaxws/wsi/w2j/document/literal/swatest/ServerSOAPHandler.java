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

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest;

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceException;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MimeHeader;
import jakarta.activation.DataHandler;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  protected void processInboundMessage(SOAPMessageContext context) {
    String result = "FAILED";
    JAXWS_Util.dumpHTTPHeaders(context, false);
    try {
      if (!Handler_Util.checkForMsg(context, "putMultipleAttachments")
          && !Handler_Util.checkForMsg(context, "echoNoAttachments")) {
        System.out.println(
            "SoapMessage does not contain expected putMultipleAttachments");
        System.out.println("or echoNoAttachments value (exiting handler)");
        return;
      }
      if (Handler_Util.checkForMsg(context,
          "Check-Content-Transfer-Encoding")) {
        result = verifyContentTransferEncodingMimeHeader(context);
        System.out.println("result=" + result);
        throw new RuntimeException(result);
      } else {
        result = verifyContentTypeHttpHeader(context);
        System.out.println("result=" + result);
        throw new RuntimeException(result);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(
          "Exception occurred in ServerSOAPHandler: " + e);
    }
  }

  protected String verifyContentTypeHttpHeader(SOAPMessageContext context)
      throws Exception {
    System.out.println("in ServerSOAPHandler:verifyContentTypeHttpHeader");
    String result = "FAILED";

    Map<String, List<String>> map1 = (Map<String, List<String>>) context
        .get(MessageContext.HTTP_REQUEST_HEADERS);
    Map<String, List<String>> map2 = JAXWS_Util.convertKeysToLowerCase(map1);
    List<String> values = map2.get("content-type");
    System.out.println("HTTP header Content-Type=" + values);
    String sValues = values.toString().toLowerCase();
    if (sValues != null) {
      int index = sValues.toLowerCase().indexOf("text/xml");
      int index2 = sValues.toLowerCase().indexOf("multipart/related");
      if ((index >= 0) || (index2 >= 0)) {
        result = "PASSED: HTTP Content-Type header contains expected: text/xml, multipart/related";
      } else {
        result = "FAILED: HTTP Content-Type header does not contain expected text/xml, multipart/related";
      }
    } else {
      result = "FAILED: the HTTP Content-Type header was not found";
    }
    return result;
  }

  /**
   * Verifies the contents of the Content-Transfer-Encoding mime header
   */

  protected String verifyContentTransferEncodingMimeHeader(
      SOAPMessageContext context) throws Exception {
    String result = null;
    SOAPMessage sm = context.getMessage();
    try {
      JAXWS_Util.dumpSOAPMessage(sm, false);
      Iterator iterator = sm.getAttachments();
      int k = 0;
      while (iterator.hasNext()) {
        Object o = iterator.next();
        AttachmentPart ap = (AttachmentPart) o;
        String[] mimeHeaderValues = ap
            .getMimeHeader("Content-transfer-encoding");
        if (mimeHeaderValues != null && mimeHeaderValues.length > 0) {
          for (int i = 0; i < mimeHeaderValues.length; i++) {
            System.out.println(
                "Content-transfer-encoding[" + i + "]=" + mimeHeaderValues[i]);
            if ((mimeHeaderValues[i].indexOf("7bit") >= 0)
                || (mimeHeaderValues[i].indexOf("8bit") >= 0)
                || (mimeHeaderValues[i].indexOf("binary") >= 0)
                || (mimeHeaderValues[i].indexOf("quoted-printable") >= 0)
                || (mimeHeaderValues[i].indexOf("base64") >= 0)) {
              if (result == null)
                result = "PASSED: Attach[" + k + "]=";
              else
                result = result + "Attach[" + k + "]=";
              result = result + mimeHeaderValues[i] + " ";
            } else {
              return "FAILED: INVALID Content-Transfer-Encoding mime header value of ["
                  + mimeHeaderValues[i] + "]";
            }
            ++k;
          }
        }
      }
      if (result == null)
        result = "PASSED: No Content-Transfer-Encoding mime headers were found";
    } catch (Exception e) {
      result = "FAILED: exception occurred:" + e;
    }
    return result;
  }

}
