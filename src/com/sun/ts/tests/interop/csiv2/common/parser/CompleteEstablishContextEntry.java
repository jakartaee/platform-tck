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

public class CompleteEstablishContextEntry extends Entry {
  public CompleteEstablishContextEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("complete-establish-context")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }

    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("client-context-id")) {
        clientContextID = Long.parseLong(getText(node));
      } else if (node.getNodeName().equals("context-stateful")) {
        contextStateful = getText(node).equals("true");
      } else if (node.getNodeName().equals("final-context-token")) {
        finalContextToken = parseBinHex(getText(node));
      }
    }
  }

  public long getClientContextID() {
    return clientContextID;
  }

  public boolean isContextStateful() {
    return contextStateful;
  }

  public byte[] getFinalContextToken() {
    return finalContextToken;
  }

  public String toString() {
    String result;
    result = "<complete-establish-context>\n";
    result += "<client-context-id>" + clientContextID
        + "</client-context-id>\n";
    result += "<context-stateful>" + contextStateful + "</context-stateful>\n";
    result += "<final-context-token>" + binHex(finalContextToken)
        + "</final-context-token>\n";
    result += "</complete-establish-context>\n";
    return result;
  }

  private long clientContextID;

  private boolean contextStateful;

  private byte[] finalContextToken;
}
