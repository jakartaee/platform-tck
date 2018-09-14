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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1011;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

public final class ChildCheckFilter implements Filter, SOAPRequests {

  // The filter configuration object we are associated with. If this value
  // is null, this filter instance is not currently configured.
  private FilterConfig filterConfig = null;

  // remove the filter configuration object for this filter.
  public void destroy() {
  }

  // initialize the filter configuration object for this filter.

  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    String result;
    System.out.println("Here in ChildCheckFilter.doFilter()\n");
    if (filterConfig == null) {
      result = "EXCEPTION";
    } else {
      try {
        result = verifyChildren((HttpServletRequest) request);
      } catch (Exception e) {
        com.sun.ts.lib.util.TestUtil.printStackTrace(e);
        result = "EXCEPTION";
      }
    }
    String xml = MessageFormat.format(R1011_RESPONSE, result);
    response.setContentType("text/xml");
    OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
    osw.write(xml);
    osw.flush();
  }

  /**
   * Verifies the env:Envelope children.
   * 
   * @param request
   *          the HTTP servlet request.
   * 
   * @return "OK" if no invalid claims are present; "FAILED" otherwise.
   * 
   * @throws Exception
   */
  protected String verifyChildren(HttpServletRequest request) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(request.getInputStream());

    Element envelope = document.getDocumentElement();
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
    return "OK";
  }

  protected boolean isElement(Element element, String namespaceURI,
      String localName) {
    if (!namespaceURI.equals(element.getNamespaceURI())) {
      return false;
    }
    return localName.equals(element.getLocalName());
  }
}
