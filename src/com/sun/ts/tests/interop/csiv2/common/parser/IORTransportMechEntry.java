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

public class IORTransportMechEntry extends Entry {
  public IORTransportMechEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("ior-transport-mech")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("tls-trans")) {
        tlsTrans = new TLSTransEntry((Element) node);
      } else if (node.getNodeName().equals("null-trans")) {
        nullTrans = new NullTransEntry((Element) node);
      } else if (node.getNodeName().equals("other-trans")) {
        otherTrans = new OtherTransEntry((Element) node);
      }
    }
  }

  public String toString() {
    String result = "<ior-transport-mech>\n";
    if (otherTrans != null) {
      result += otherTrans.toString();
    }
    if (tlsTrans != null) {
      result += tlsTrans.toString();
    }
    if (nullTrans != null) {
      result += nullTrans.toString();
    }
    result += "</ior-transport-mech>\n";
    return result;
  }

  public TLSTransEntry getTlsTrans() {
    return tlsTrans;
  }

  public NullTransEntry getNullTrans() {
    return nullTrans;
  }

  public OtherTransEntry getOtherTrans() {
    return otherTrans;
  }

  /**
   * @supplierCardinality 0..1
   */
  private TLSTransEntry tlsTrans;

  /**
   * @supplierCardinality 0..1
   */
  private NullTransEntry nullTrans;

  /**
   * @supplierCardinality 0..1
   */
  private OtherTransEntry otherTrans;
}
