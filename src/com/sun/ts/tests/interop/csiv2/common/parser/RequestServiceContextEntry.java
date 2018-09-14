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

public class RequestServiceContextEntry extends Entry {
  public boolean isPresent() {
    return present;
  }

  public InvalidMessageEntry getInvalidMessage() {
    return invalidMessage;
  }

  public RequestServiceContextEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("req-svc-context")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }

    this.present = element.getAttribute("present").equals("true");

    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("establish-context")) {
        establishContext = new EstablishContextEntry((Element) node);
      } else if (node.getNodeName().equals("invalid-message")) {
        invalidMessage = new InvalidMessageEntry((Element) node);
      }
    }
  }

  public EstablishContextEntry getEstablishContext() {
    return establishContext;
  }

  public String toString() {
    String result;
    if (present) {
      result = "<req-svc-context present=\"true\">\n";
      if (establishContext != null) {
        result += establishContext.toString();
      } else if (invalidMessage != null) {
        result += invalidMessage.toString();
      }
      result += "</req-svc-context>\n";
    } else {
      result = "<req-svc-context present=\"false\"/>\n";
    }
    return result;
  }

  private boolean present;

  /** @supplierCardinality 0..1 */
  private InvalidMessageEntry invalidMessage;

  /** @supplierCardinality 0..1 */
  private EstablishContextEntry establishContext;
}
