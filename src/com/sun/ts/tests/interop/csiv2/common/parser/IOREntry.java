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

public class IOREntry extends Entry {
  public IOREntry(Element element) throws ParseException {
    if (!element.getTagName().equals("ior")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("stateful")) {
        stateful = getText(node).equals("true");
      } else if (node.getNodeName().equals("port")) {
        port = Integer.parseInt(getText(node));
      } else if (node.getNodeName().equals("compound-sec-mech")) {
        compoundSecMechs.addElement(new CompoundSecMechEntry((Element) node));
      }
    }
  }

  public String toString() {
    String result = "<ior>\n";
    result += "<port>" + port + "</port>\n";
    result += "<stateful>" + stateful + "</stateful>\n";
    for (int i = 0; i < compoundSecMechs.size(); i++) {
      result += compoundSecMechs.elementAt(i).toString();
    }
    result += "</ior>\n";
    return result;
  }

  public Vector getCompoundSecMechs() {
    return compoundSecMechs;
  }

  public boolean isStateful() {
    return stateful;
  }

  public int getPort() {
    return port;
  }

  /**
   * @link aggregation
   * @associates <{CompoundSecMechEntry}>
   * @supplierCardinality 1..*
   */
  private Vector compoundSecMechs = new Vector();

  private boolean stateful;

  private int port;
}
