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

public class IdentityTokenEntry extends Entry {
  public IdentityTokenEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("identity-token")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }

    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("absent")) {
        identityToken = new ITAbsent();
      } else if (node.getNodeName().equals("anonymous")) {
        identityToken = new ITAnonymous();
      } else if (node.getNodeName().equals("principal-name")) {
        identityToken = new ITPrincipalName(parseBinHex(getText(node)));
      } else if (node.getNodeName().equals("certificate-chain")) {
        identityToken = new ITCertificateChain(parseBinHex(getText(node)));
      } else if (node.getNodeName().equals("distinguished-name")) {
        identityToken = new ITDistinguishedName(parseBinHex(getText(node)));
      } else if (node.getNodeName().equals("unknown-type")) {
        identityToken = new ITUnknownType(
            ((Element) node).getAttribute("details"));
      }
    }
  }

  public IdentityToken getIdentityToken() {
    return identityToken;
  }

  public String toString() {
    String result;
    result = "<identity-token>\n";
    result += identityToken.toString();
    result += "</identity-token>\n";
    return result;
  }

  /**
   * @supplierCardinality 1
   */
  private IdentityToken identityToken;
}
