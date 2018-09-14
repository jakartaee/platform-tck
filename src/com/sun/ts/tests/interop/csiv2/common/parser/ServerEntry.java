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

public class ServerEntry extends Entry {
  public ServerEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("server")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("invocation-principal")) {
        invocationPrincipal = getText(node);
      } else if (node.getNodeName().equals("invocation")) {
        invocation = new InvocationEntry((Element) node);
      }
    }
  }

  public InvocationEntry getInvocation() {
    return invocation;
  }

  public String toString() {
    String result = "<server>\n";
    result += "<invocation-principal>" + invocationPrincipal
        + "</invocation-principal>\n";
    if (invocation != null) {
      result += invocation.toString();
    }
    result += "</server>\n";
    return result;
  }

  public String getInvocationPrincipal() {
    return invocationPrincipal;
  }

  private String invocationPrincipal;

  /** @supplierCardinality 0..1 */
  private InvocationEntry invocation;
}
