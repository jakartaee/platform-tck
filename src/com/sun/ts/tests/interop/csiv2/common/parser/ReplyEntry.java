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

public class ReplyEntry extends Entry {

  /** Constants for possible exceptions */
  public static final String NO_EXCEPTION = "no-exception";

  public static final String CREATE_EXCEPTION = "create-exception";

  public static final String ACCESS_EXCEPTION = "access-exception";

  public static final String OTHER_EXCEPTION = "other-exception";

  public ReplyEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("reply")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals(NO_EXCEPTION)) {
        replyType = NO_EXCEPTION;
        replyMsg = null;
      } else if (node.getNodeName().equals(CREATE_EXCEPTION)) {
        replyType = CREATE_EXCEPTION;
        replyMsg = getText(node);
      } else if (node.getNodeName().equals(ACCESS_EXCEPTION)) {
        replyType = ACCESS_EXCEPTION;
        replyMsg = getText(node);
      } else if (node.getNodeName().equals(OTHER_EXCEPTION)) {
        replyType = OTHER_EXCEPTION;
        replyMsg = getText(node);
      }
    }
  }

  public String getReplyType() {
    return replyType;
  }

  public String getReplyMsg() {
    return replyMsg;
  }

  public String toString() {
    String result = "<reply>\n";
    if (replyMsg == null)
      result += "</" + replyType + ">";
    else
      result += "<" + replyType + ">" + replyMsg + "\n</" + replyType + ">\n";
    result += "</reply>\n";
    return result;
  }

  /** @supplierCardinality 0..1 */
  private String replyType;

  private String replyMsg;
}
