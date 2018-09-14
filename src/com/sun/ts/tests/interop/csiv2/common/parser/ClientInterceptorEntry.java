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

package com.sun.ts.tests.interop.csiv2.common.parser;

import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.util.*;

public class ClientInterceptorEntry extends Entry {
  public ClientInterceptorEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("client-interceptor")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("operation")) {
        operation = getText(node);
      } else if (node.getNodeName().equals("req-svc-context")) {
        requestServiceContext = new RequestServiceContextEntry((Element) node);
      } else if (node.getNodeName().equals("ssl-used")) {
        sslUsed = getText(node).equals("true");
      } else if (node.getNodeName().equals("ior")) {
        ior = new IOREntry((Element) node);
      } else if (node.getNodeName().equals("server-interceptor")) {
        serverInterceptor = new ServerInterceptorEntry((Element) node);
      } else if (node.getNodeName().equals("server")) {
        server = new ServerEntry((Element) node);
      } else if (node.getNodeName().equals("location-forward")) {
        locationForward = getText(node).equals("true");
      } else if (node.getNodeName().equals("reply-svc-context")) {
        replyServiceContext = new ReplyServiceContextEntry((Element) node);
      } else if (node.getNodeName().equals("client-interceptor")) {
        clientInterceptor = new ClientInterceptorEntry((Element) node);
      }
    }
  }

  public ServerInterceptorEntry getServerInterceptor() {
    return serverInterceptor;
  }

  public ServerEntry getServer() {
    return server;
  }

  public String getOperation() {
    return operation;
  }

  public RequestServiceContextEntry getRequestServiceContext() {
    return requestServiceContext;
  }

  public boolean isSslUsed() {
    return sslUsed;
  }

  public boolean isLocationForward() {
    return locationForward;
  }

  public ReplyServiceContextEntry getReplyServiceContext() {
    return replyServiceContext;
  }

  public ClientInterceptorEntry getClientInterceptor() {
    return clientInterceptor;
  }

  public String toString() {
    String result = "<client-interceptor>\n";
    result += "<operation>" + operation + "</operation>\n";
    result += requestServiceContext.toString();
    result += "<ssl-used>" + sslUsed + "</ssl-used>\n";
    result += ior.toString();
    if (serverInterceptor != null) {
      result += serverInterceptor.toString();
    }
    if (server != null) {
      result += server.toString();
    }
    result += "<location-forward>" + locationForward + "</location-forward>\n";
    result += replyServiceContext.toString();
    if (clientInterceptor != null) {
      result += "<client-interceptor>\n" + clientInterceptor.toString()
          + "</client-interceptor>\n";
    }
    result += "</client-interceptor>\n";
    return result;
  }

  public IOREntry getIor() {
    return ior;
  }

  /** @supplierCardinality 0..1 */
  private ServerEntry server;

  private String operation;

  private boolean sslUsed;

  /** @supplierCardinality 0..1 */
  private ServerInterceptorEntry serverInterceptor;

  private boolean locationForward;

  /** @supplierCardinality 1 */
  private RequestServiceContextEntry requestServiceContext;

  /** @supplierCardinality 1 */
  private ReplyServiceContextEntry replyServiceContext;

  /** @supplierCardinality 1 */
  private IOREntry ior;

  /** @supplierCardinality 0..1 */
  private ClientInterceptorEntry clientInterceptor;
}
