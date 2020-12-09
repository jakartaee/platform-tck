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

package com.sun.ts.tests.saaj.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPMessage;

public class ReceivingSOAP11Servlet extends HttpServlet {
  private SOAPConnection con = null;

  private MessageFactory mf = null;

  private Properties harnessProps = null;

  private boolean debug = true;

  private String soapVersion = null;

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("ReceivingSOAP11Servlet:init (Entering)");
    try {
      SOAP_Util.setup();
      con = SOAP_Util.getSOAPConnection();
      mf = MessageFactory.newInstance();
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e.getMessage());
      e.printStackTrace();
      throw new ServletException("Exception occurred: " + e.getMessage());
    }
    System.out.println("ReceivingSOAP11Servlet:init (Leaving)");
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
      SOAPMessage reqMsg = mf.createMessage(headers, is);

      SOAPMessage respMsg = null;

      // There are no replies in case of an OnewayListener.
      respMsg = onMessage(reqMsg);

      if (respMsg != null) {
        respMsg.saveChanges();

        resp.setStatus(HttpServletResponse.SC_OK);
        putHeaders(respMsg.getMimeHeaders(), resp);

        // Write out the message on the response stream.
        OutputStream os = resp.getOutputStream();
        respMsg.writeTo(os);

        os.flush();

        if (debug) {
          System.out
              .println("---------------------------------------------------");
          System.out
              .println("Dumping SOAPMessage HTTP-RESPONSE minus attachments");
          System.out
              .println("---------------------------------------------------");
          dumpSOAPMessage(respMsg);
        }

      } else
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e.getMessage());
      e.printStackTrace();
      throw new ServletException(
          "ReceivingSOAP11Servlet:doPost failed " + e.getMessage());
    }
  }

  private MimeHeaders getHeaders(HttpServletRequest req) {

    Enumeration enumlist = req.getHeaderNames();
    MimeHeaders headers = new MimeHeaders();
    int k = 0;

    if (debug) {
      System.out.println("----------------------------------------");
      System.out.println("Dumping SOAPMessage HTTP-REQUEST headers");
      System.out.println("----------------------------------------");
    }

    while (enumlist.hasMoreElements()) {
      String headerName = (String) enumlist.nextElement();
      String headerValue = req.getHeader(headerName);
      if (debug) {
        System.out.println("HeaderName" + k + "=" + headerName + "\nHeaderValue"
            + k + "=" + headerValue);
        ++k;
      }

      StringTokenizer values = new StringTokenizer(headerValue, ",");
      if (headerName.equalsIgnoreCase("Content-Type")) {
        while (values.hasMoreTokens())
          headers.addHeader(headerName, values.nextToken().trim());
      }
    }
    return headers;
  }

  private void putHeaders(MimeHeaders headers, HttpServletResponse res) {

    Iterator it = headers.getAllHeaders();
    if (debug) {
      System.out.println("-----------------------------------------");
      System.out.println("Dumping SOAPMessage HTTP-RESPONSE headers");
      System.out.println("-----------------------------------------");
    }
    int k = 0;
    while (it.hasNext()) {
      MimeHeader header = (MimeHeader) it.next();

      String[] values = headers.getHeader(header.getName());
      if (values.length == 1)
        res.setHeader(header.getName(), header.getValue());
      if (debug) {
        System.out.println("HeaderName" + k + "=" + header.getName()
            + "\nHeaderValue" + k + "=" + header.getValue());
        ++k;
      } else {
        StringBuffer concat = new StringBuffer();
        int i = 0;
        while (i < values.length) {
          if (i != 0)
            concat.append(',');
          concat.append(values[i++]);
        }
        res.setHeader(header.getName(), concat.toString());
      }
    }
  }

  // This is the application code for handling the message. Once the
  // message is received the application can retrieve the soap part, the
  // attachment part if there are any, or any other information from the
  // message.

  private SOAPMessage onMessage(SOAPMessage reqMsg) {
    SOAPMessage respMsg = null;
    System.out.println("ReceivingSOAP11Servlet:onMessage");
    try {
      if (debug) {
        System.out
            .println("--------------------------------------------------");
        System.out
            .println("Dumping SOAPMessage HTTP-REQUEST minus attachments");
        System.out
            .println("--------------------------------------------------");
        dumpSOAPMessage(reqMsg);
      }
      // Create the soap response to be identical to the soap request
      respMsg = mf.createMessage();
      respMsg.getSOAPPart().setContent(reqMsg.getSOAPPart().getContent());
      Iterator i = reqMsg.getAttachments();
      while (i.hasNext())
        respMsg.addAttachmentPart((AttachmentPart) i.next());
      respMsg.saveChanges();
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e);
      e.printStackTrace();
    }
    return respMsg;
  }

  private void dumpSOAPMessage(SOAPMessage msg) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      msg.writeTo(baos);
      SOAPMessage tmpMsg = mf.createMessage(msg.getMimeHeaders(),
          new ByteArrayInputStream(baos.toByteArray()));
      tmpMsg.removeAllAttachments();
      tmpMsg.saveChanges();
      tmpMsg.writeTo(System.out);
      System.out.println("");
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e);
      e.printStackTrace();
    }
  }
}
