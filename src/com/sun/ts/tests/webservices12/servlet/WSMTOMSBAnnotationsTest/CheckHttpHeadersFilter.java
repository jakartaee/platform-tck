/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.webservices12.servlet.WSMTOMSBAnnotationsTest;

import java.io.*;
import java.util.*;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.soap.*;

import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;

public final class CheckHttpHeadersFilter implements Filter, SOAPRequests {

  // The filter configuration object we are associated with. If this value
  // is null, this filter instance is not currently configured.
  private FilterConfig filterConfig = null;

  private boolean testValidCase = false;

  private boolean testInvalidCase = false;

  // remove the filter configuration object for this filter.
  public void destroy() {
  }

  // initialize the filter configuration object for this filter.

  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    String result = "null";
    String input = null;
    String soapMsgAsString = null;
    String xml = null;
    System.out.println("Here in CheckHttpHeadersFilter.doFilter()\n");
    dumpHeaders((HttpServletRequest) request);
    byte buffer[] = new byte[65536];
    int count = 0;
    int tmpcount = 0;
    if (request.getInputStream().markSupported())
      request.getInputStream().mark(8192);
    while (tmpcount != -1) {
      tmpcount = request.getInputStream().read(buffer, count, 8192);
      if (tmpcount != -1)
        count += tmpcount;
    }
    ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, count);
    input = new String(buffer);
    if (filterConfig == null) {
      result = "EXCEPTION";
      xml = MessageFormat.format(MTOM_RESPONSE_DOCLIT, result);
    } else {
      if (input.indexOf("MTOMIn2") < 0) {
        System.out
            .println("HTTP request does not contain expected JAXWS request");
        System.out
            .println("Call next filter in chain which is the endpoint itself");
        if (request.getInputStream().markSupported())
          request.getInputStream().reset();
        chain.doFilter(request, response);
        return;
      }
      int index = input.indexOf("=_Part_");
      String partialInput = input;
      if (index >= 0) {
        index = input.indexOf("=_Part_", index + 1);
        partialInput = input.substring(0, index);
      }
      System.out.println("DEBUG: HTTP Request stream count=" + count);
      try {
        MimeHeaders headers = getHeaders((HttpServletRequest) request);
        SOAPMessage soapMsg = MessageFactory.newInstance()
            .createMessage(headers, bais);
        soapMsgAsString = returnSOAPMessageAsStringWOA(soapMsg);
        System.out.println(
            "DEBUG: HTTP Request stream soap message=" + soapMsgAsString);
      } catch (Exception e) {
        System.out
            .println("DEBUG: HTTP Request stream partialinput=" + partialInput);
      }
      testValidCase = false;
      testInvalidCase = false;
      if (input.indexOf("Check-Content-Transfer-Encoding") >= 0) {
        if (input.indexOf("TestValidCase") >= 0)
          testValidCase = true;
        else if (input.indexOf("TestInvalidCase") >= 0)
          testInvalidCase = true;
        try {
          result = verifyContentTransferEncodingHeader(
              (HttpServletRequest) request, bais);
        } catch (Exception e) {
          System.out.println("Caught exception: " + e.getMessage());
          e.printStackTrace();
          result = "Exception";
        }
      } else
        result = verifyContentTypeHttpHeader((HttpServletRequest) request);
      xml = MessageFormat.format(MTOM_RESPONSE_DOCLIT, result);
      response.setContentType("text/xml");
      System.out.println("DEBUG: HTTP Response output=" + xml);
    }
    OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
    osw.write(xml);
    osw.flush();
    osw.close();
  }

  /**
   * Verifies the contents of the Content-Type HTTP header
   *
   * @param request
   *          the HTTP servlet request.
   */
  protected String verifyContentTypeHttpHeader(HttpServletRequest request) {
    String result = null;
    String headerValue = request.getHeader("Content-Type");
    System.out.println("DEBUG: HTTP header Content-Type=" + headerValue);
    String headerValueLC = headerValue.toLowerCase();
    if (headerValueLC != null) {
      if ((headerValueLC.indexOf("multipart/related") >= 0)
          && (headerValueLC.indexOf("text/xml") >= 0)
          && (headerValueLC.indexOf("application/xop+xml") >= 0)) {
        result = "PASSED: HTTP Content-Type header contains expected: multipart/related, text/xml, application/xop+xml";
      } else {
        result = "FAILED: HTTP Content-Type header does not contain expected values";
      }
    } else {
      result = "FAILED: HTTP Content-Type header was not found";
    }
    System.out.println("result=" + result);
    return result;
  }

  /**
   * Verifies the contents of the Content-Transfer-Encoding mime header
   *
   * @param request
   *          the HTTP servlet request.
   *
   */
  protected String verifyContentTransferEncodingHeader(
      HttpServletRequest request, ByteArrayInputStream bais)
      throws ServletException {
    MimeHeaders headers = getHeaders(request);
    AttachmentPart ap = null;
    Iterator iterator = null;
    try {
      SOAPMessage reqMsg = MessageFactory.newInstance().createMessage(headers,
          bais);
      iterator = reqMsg.getAttachments();
    } catch (Exception e) {
      throw new ServletException(
          "verifyContentTransferEncodingMimeHeader failed: " + e.getMessage());
    }
    String[] headerValues = null;
    String result = null;
    int k = 0;
    while (iterator.hasNext()) {
      ap = (AttachmentPart) iterator.next();
      if (testValidCase)
        ap.addMimeHeader("Content-Transfer-Encoding", "base64");
      if (testInvalidCase)
        ap.addMimeHeader("Content-Transfer-Encoding", "invalid");
      headerValues = ap.getMimeHeader("Content-Transfer-Encoding");
      if (headerValues != null && headerValues.length > 0) {
        for (int i = 0; i < headerValues.length; i++) {
          headerValues[i] = headerValues[i].toLowerCase();
          if ((headerValues[i].indexOf("7bit") >= 0)
              || (headerValues[i].indexOf("8bit") >= 0)
              || (headerValues[i].indexOf("binary") >= 0)
              || (headerValues[i].indexOf("quoted-printable") >= 0)
              || (headerValues[i].indexOf("base64") >= 0)) {
            if (i == 0) {
              if (result == null)
                result = "Attach[" + k + "]=";
              else
                result = result + "Attach[" + k + "]=";
            }
            result = result + headerValues[i] + " ";
          } else {
            result = "FAILED: INVALID Content-Transfer-Encoding mime header value of ["
                + headerValues[i] + "]";
            break;
          }
        }
      }
      ++k;
    }
    if (result == null)
      result = "No Content-Transfer-Encoding mime headers were found";
    System.out.println("result=" + result);
    return result;
  }

  private MimeHeaders getHeaders(HttpServletRequest req) {

    Enumeration enumlist = req.getHeaderNames();
    MimeHeaders headers = new MimeHeaders();
    int k = 0;

    while (enumlist.hasMoreElements()) {
      String headerName = (String) enumlist.nextElement();
      String headerValue = req.getHeader(headerName);
      StringTokenizer values = new StringTokenizer(headerValue, ",");
      while (values.hasMoreTokens())
        headers.addHeader(headerName, values.nextToken().trim());
    }
    return headers;
  }

  private void dumpHeaders(HttpServletRequest req) {

    Enumeration enumlist = req.getHeaderNames();
    int k = 0;
    while (enumlist.hasMoreElements()) {
      String headerName = (String) enumlist.nextElement();
      String headerValue = req.getHeader(headerName);
      System.out.println(
          "DEBUG: HTTP request header=" + headerName + ",\t" + headerValue);
    }
  }

  public static String returnSOAPMessageAsStringWOA(SOAPMessage msg) {
    ByteArrayOutputStream baos = null;
    String s = null;
    try {

      baos = new ByteArrayOutputStream();
      msg.removeAllAttachments();
      msg.saveChanges();
      msg.removeAllAttachments();
      msg.writeTo(baos);
      s = baos.toString();
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e);
      e.printStackTrace();
    }
    return s;
  }
}
