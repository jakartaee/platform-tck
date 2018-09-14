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

/**
 * Base class for all CSIv2 Log entries. Contains some utility methods used by
 * subclasses.
 * 
 * @author Mark Roth
 */
public class Entry {
  public static String binHex(byte[] bytes) {
    StringBuffer result = new StringBuffer("");
    char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
        'B', 'C', 'D', 'E', 'F' };
    for (int i = 0; i < bytes.length; i++) {
      int b = (int) bytes[i];
      result.append(digits[(b & 0xF0) >> 4]);
      result.append(digits[b & 0x0F]);
    }
    return result.toString();
  }

  public static byte[] parseBinHex(String data) {
    data = data.toUpperCase();
    byte[] result = new byte[data.length() / 2];
    for (int i = 0; i < data.length(); i += 2) {
      char d1 = data.charAt(i);
      char d2 = data.charAt(i + 1);
      int v1 = d1 - ((d1 >= 'A') ? ('A' - 10) : '0');
      int v2 = d2 - ((d2 >= 'A') ? ('A' - 10) : '0');
      result[i / 2] = (byte) (v1 * 16 + v2);
    }
    return result;
  }

  /**
   * Returns the text portion of the given node. Returns "" if there was no text
   * portion.
   */
  public String getText(Node textNode) {
    String result = "";
    NodeList nodes = textNode.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.TEXT_NODE) {
        result = node.getNodeValue();
        break;
      }
    }
    return result;
  }
}
