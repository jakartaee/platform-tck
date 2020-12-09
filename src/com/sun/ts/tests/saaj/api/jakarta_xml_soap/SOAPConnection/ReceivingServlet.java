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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;

import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;

public class ReceivingServlet extends HttpServlet {
  protected MessageFactory msgFactory = null;

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    try {
      // Initialize it to the default.
      SOAP_Util.setup();
      msgFactory = SOAP_Util.getMessageFactory();
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e.getMessage());
      e.printStackTrace();
      throw new ServletException("Exception occurred: " + e.getMessage());
    }
  }

  protected static MimeHeaders getHeaders(HttpServletRequest req) {
    Enumeration enumlist = req.getHeaderNames();
    MimeHeaders headers = new MimeHeaders();

    while (enumlist.hasMoreElements()) {
      String headerName = (String) enumlist.nextElement();
      String headerValue = req.getHeader(headerName);
      if (headerName.equalsIgnoreCase("Content-Type"))
        headers.addHeader(headerName, headerValue);
    }

    return headers;
  }

  protected static void putHeaders(MimeHeaders headers,
      HttpServletResponse res) {
    Iterator it = headers.getAllHeaders();
    while (it.hasNext()) {
      MimeHeader header = (MimeHeader) it.next();
      res.setHeader(header.getName(), header.getValue());
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      // Get all the headers from the HTTP request.
      MimeHeaders headers = getHeaders(req);

      // Get the body of the HTTP request.
      InputStream is = req.getInputStream();

      // Now internalize the contents of a HTTP request and
      // create a SOAPMessage
      SOAPMessage msg = msgFactory.createMessage(headers, is);

      // Now create a reply SOAPMessage to send back in the
      // HTTP response
      SOAPMessage reply = msgFactory.createMessage();

      SOAPEnvelope envelope = reply.getSOAPPart().getEnvelope();

      SOAPBody body = envelope.getBody();

      body.addBodyElement(envelope.createName("GetLastTradePriceResponse",
          "ztrade", "http://wombat.ztrade.com")).addChildElement("Price")
          .addTextNode("95.12");

      Iterator iterator = msg.getAttachments();
      while (iterator.hasNext()) {
        AttachmentPart ap = (AttachmentPart) iterator.next();
        reply.addAttachmentPart(ap);
      }

      reply.saveChanges();

      resp.setStatus(HttpServletResponse.SC_OK);

      putHeaders(reply.getMimeHeaders(), resp);

      // Write out the message on the response stream.
      OutputStream os = resp.getOutputStream();
      reply.writeTo(os);
      os.flush();
    } catch (Exception ex) {
      throw new ServletException(
          "RecievingServlet POST failed " + ex.getMessage());
    }
  }
}
