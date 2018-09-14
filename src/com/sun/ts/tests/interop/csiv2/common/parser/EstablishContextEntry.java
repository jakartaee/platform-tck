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

public class EstablishContextEntry extends Entry {
  public EstablishContextEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("establish-context")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }

    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("client-context-id")) {
        clientContextID = Long.parseLong(getText(node));
      } else if (node.getNodeName().equals("identity-token")) {
        identityToken = new IdentityTokenEntry((Element) node);
      } else if (node.getNodeName().equals("client-auth-token")) {
        clientAuthToken = parseBinHex(getText(node));
      } else if (node.getNodeName().equals("authz-token-count")) {
        authzTokenCount = Integer.parseInt(getText(node));
      }
    }
  }

  public long getClientContextID() {
    return clientContextID;
  }

  public IdentityTokenEntry getIdentityToken() {
    return identityToken;
  }

  public byte[] getClientAuthToken() {
    return clientAuthToken;
  }

  public int getAuthzTokenCount() {
    return authzTokenCount;
  }

  public String toString() {
    String result;
    result = "<establish-context>\n";
    result += "<client-context-id>" + clientContextID
        + "</client-context-id>\n";
    result += identityToken.toString();
    result += "<client-auth-token>" + binHex(clientAuthToken)
        + "</client-auth-token>\n";
    result += "<authz-token-count>" + authzTokenCount
        + "</authz-token-count>\n";
    result += "</establish-context>\n";
    return result;
  }

  private long clientContextID;

  /**
   * @supplierCardinality 1
   */
  private IdentityTokenEntry identityToken;

  private byte[] clientAuthToken;

  private int authzTokenCount;
}
