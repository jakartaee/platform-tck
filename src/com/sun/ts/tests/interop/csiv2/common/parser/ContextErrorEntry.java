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

public class ContextErrorEntry extends Entry {
  public ContextErrorEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("context-error")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }

    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("client-context-id")) {
        clientContextID = Long.parseLong(getText(node));
      } else if (node.getNodeName().equals("major-status")) {
        majorStatus = Integer.parseInt(getText(node));
      } else if (node.getNodeName().equals("minor-status")) {
        minorStatus = Integer.parseInt(getText(node));
      } else if (node.getNodeName().equals("error-token")) {
        errorToken = parseBinHex(getText(node));
      }
    }
  }

  public long getClientContextID() {
    return clientContextID;
  }

  public int getMajorStatus() {
    return majorStatus;
  }

  public int getMinorStatus() {
    return minorStatus;
  }

  public byte[] getErrorToken() {
    return errorToken;
  }

  public String toString() {
    String result;
    result = "<context-error>";
    result += "<client-context-id>" + clientContextID
        + "</client-context-id>\n";
    result += "<major-status>" + majorStatus + "</major-status>\n";
    result += "<minor-status>" + majorStatus + "</minor-status>\n";
    result += "<error-token>" + binHex(errorToken) + "</error-token>\n";
    result += "</context-error>\n";
    return result;
  }

  private long clientContextID;

  private int majorStatus;

  private int minorStatus;

  private byte[] errorToken;
}
