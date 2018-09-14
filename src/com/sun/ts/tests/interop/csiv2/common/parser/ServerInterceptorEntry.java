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

public class ServerInterceptorEntry extends Entry {
  public ServerInterceptorEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("server-interceptor")) {
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
      } else if (node.getNodeName().equals("server")) {
        server = new ServerEntry((Element) node);
      } else if (node.getNodeName().equals("reply-svc-context")) {
        replyServiceContext = new ReplyServiceContextEntry((Element) node);
      } else if (node.getNodeName().equals("transport-client-principals")) {
        transportClientPrincipals = new TransportClientPrincipalsEntry(
            (Element) node);
      }
    }
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

  public ReplyServiceContextEntry getReplyServiceContext() {
    return replyServiceContext;
  }

  public TransportClientPrincipalsEntry getTransportClientPrincipals() {
    return transportClientPrincipals;
  }

  public ServerEntry getServer() {
    return server;
  }

  public String toString() {
    String result = "<server-interceptor>\n";
    result += "<operation>" + operation + "</operation>\n";
    result += requestServiceContext.toString();
    result += "<ssl-used>" + sslUsed + "</ssl-used>\n";
    if (transportClientPrincipals != null) {
      result += transportClientPrincipals.toString();
    }
    if (server != null) {
      result += server.toString();
    }
    if (server != null) {
      result += replyServiceContext.toString();
    }

    result += "</server-interceptor>\n";
    return result;
  }

  private String operation;

  private boolean sslUsed;

  /** @supplierCardinality 1 */
  private TransportClientPrincipalsEntry transportClientPrincipals;

  /** @supplierCardinality 0..1 */
  private ServerEntry server;

  /** @supplierCardinality 1 */
  private RequestServiceContextEntry requestServiceContext;

  /** @supplierCardinality 1 */
  private ReplyServiceContextEntry replyServiceContext;
}
