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

public class ReplyServiceContextEntry extends Entry {
  public ReplyServiceContextEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("reply-svc-context")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }

    this.present = element.getAttribute("present").equals("true");

    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("complete-establish-context")) {
        completeEstablishContext = new CompleteEstablishContextEntry(
            (Element) node);
      } else if (node.getNodeName().equals("context-error")) {
        contextError = new ContextErrorEntry((Element) node);
      } else if (node.getNodeName().equals("invalid-message")) {
        invalidMessage = new InvalidMessageEntry((Element) node);
      }
    }
  }

  public boolean isPresent() {
    return present;
  }

  public InvalidMessageEntry getInvalidMessage() {
    return invalidMessage;
  }

  public ContextErrorEntry getContextError() {
    return contextError;
  }

  public CompleteEstablishContextEntry getCompleteEstablishContext() {
    return completeEstablishContext;
  }

  public String toString() {
    String result;
    if (present) {
      result = "<reply-svc-context present=\"true\">\n";
      if (completeEstablishContext != null) {
        result += completeEstablishContext.toString();
      }
      if (contextError != null) {
        result += contextError.toString();
      }
      if (invalidMessage != null) {
        result += invalidMessage.toString();
      }
      result += "</reply-svc-context>\n";
    } else {
      result = "<reply-svc-context present=\"false\"/>\n";
    }
    return result;
  }

  private boolean present;

  /** @supplierCardinality 0..1 */
  private InvalidMessageEntry invalidMessage;

  /**
   * @supplierCardinality 0..1
   */
  private ContextErrorEntry contextError;

  /**
   * @supplierCardinality 0..1
   */
  private CompleteEstablishContextEntry completeEstablishContext;
}
