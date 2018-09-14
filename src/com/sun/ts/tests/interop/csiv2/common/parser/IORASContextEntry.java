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

public class IORASContextEntry extends Entry {
  public IORASContextEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("ior-as-context")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("target-supports")) {
        targetSupports = Integer.parseInt(getText(node));
      } else if (node.getNodeName().equals("target-requires")) {
        targetRequires = Integer.parseInt(getText(node));
      } else if (node.getNodeName().equals("client-authentication-mech")) {
        clientAuthenticationMech = parseBinHex(getText(node));
      } else if (node.getNodeName().equals("target-name")) {
        targetName = parseBinHex(getText(node));
      }
    }
  }

  public int getTargetSupports() {
    return targetSupports;
  }

  public int getTargetRequires() {
    return targetRequires;
  }

  public byte[] getTargetName() {
    return targetName;
  }

  public byte[] getClientAuthenticationMech() {
    return clientAuthenticationMech;
  }

  public String toString() {
    String result = "<ior-as-context>\n";
    result += "<target-supports>" + targetSupports + "</target-supports>\n";
    result += "<target-requires>" + targetRequires + "</target-requires>\n";
    result += "<client-authentication-mech>" + binHex(clientAuthenticationMech)
        + "</client-authentication-mech>\n";
    result += "<target-name>" + binHex(targetName) + "</target-name>\n";
    result += "</ior-as-context>\n";
    return result;
  }

  private int targetSupports;

  private int targetRequires;

  private byte[] clientAuthenticationMech;

  /** Elements of this vector are byte[] */
  private byte[] targetName;
}
