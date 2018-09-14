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

/**
 * Note: Only one of clientInterceptor, serverInterceptor, or server will be
 * non-null.
 */
public class ClientEntry extends Entry {
  public ClientEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("client")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("client-interceptor")) {
        clientInterceptor = new ClientInterceptorEntry((Element) node);
      } else if (node.getNodeName().equals("server-interceptor")) {
        serverInterceptor = new ServerInterceptorEntry((Element) node);
      }
      if (node.getNodeName().equals("server")) {
        server = new ServerEntry((Element) node);
      }
      if (node.getNodeName().equals("reply")) {
        reply = new ReplyEntry((Element) node);
      }
    }
  }

  public ClientInterceptorEntry getClientInterceptor() {
    return clientInterceptor;
  }

  public ServerInterceptorEntry getServerInterceptor() {
    return serverInterceptor;
  }

  public ServerEntry getServer() {
    return server;
  }

  public ReplyEntry getReply() {
    return reply;
  }

  public String toString() {
    String result = "<client>\n";
    if (clientInterceptor != null) {
      result += clientInterceptor.toString();
    }
    if (serverInterceptor != null) {
      result += serverInterceptor.toString();
    }
    if (server != null) {
      result += server.toString();
      result += reply.toString();
    }
    result += "</client>\n";
    return result;
  }

  /** @supplierCardinality 0..1 */
  private ClientInterceptorEntry clientInterceptor;

  /** @supplierCardinality 0..1 */
  private ServerInterceptorEntry serverInterceptor;

  /** @supplierCardinality 0..1 */
  private ServerEntry server;

  /** @supplierCardinality 1 */
  private ReplyEntry reply;
}
