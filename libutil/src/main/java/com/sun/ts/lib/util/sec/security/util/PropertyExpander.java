/*
 * Copyright (c) 1998, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.security.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

/**
 * A utility class to expand properties embedded in a string. Strings of the
 * form ${some.property.name} are expanded to be the value of the property.
 * Also, the special ${/} property is expanded to be the same as file.separator.
 * If a property is not set, a GeneralSecurityException will be thrown.
 *
 * @author Roland Schemers
 */
public class PropertyExpander {

  public static class ExpandException extends GeneralSecurityException {

    private static final long serialVersionUID = -7941948581406161702L;

    public ExpandException(String msg) {
      super(msg);
    }
  }

  public static String expand(String value) throws ExpandException {
    return expand(value, false);
  }

  public static String expand(String value, boolean encodeURL)
      throws ExpandException {
    if (value == null)
      return null;

    int p = value.indexOf("${", 0);

    // no special characters
    if (p == -1)
      return value;

    StringBuffer sb = new StringBuffer(value.length());
    int max = value.length();
    int i = 0; // index of last character we copied

    scanner: while (p < max) {
      if (p > i) {
        // copy in anything before the special stuff
        sb.append(value.substring(i, p));
        i = p;
      }
      int pe = p + 2;

      // do not expand ${{ ... }}
      if (pe < max && value.charAt(pe) == '{') {
        pe = value.indexOf("}}", pe);
        if (pe == -1 || pe + 2 == max) {
          // append remaining chars
          sb.append(value.substring(p));
          break scanner;
        } else {
          // append as normal text
          pe++;
          sb.append(value.substring(p, pe + 1));
        }
      } else {
        while ((pe < max) && (value.charAt(pe) != '}')) {
          pe++;
        }
        if (pe == max) {
          // no matching '}' found, just add in as normal text
          sb.append(value.substring(p, pe));
          break scanner;
        }
        String prop = value.substring(p + 2, pe);
        if (prop.equals("/")) {
          sb.append(java.io.File.separatorChar);
        } else {
          String val = System.getProperty(prop);
          if (val != null) {
            if (encodeURL) {
              // encode 'val' unless it's an absolute URI
              // at the beginning of the string buffer
              try {
                if (sb.length() > 0 || !(new URI(val)).isAbsolute()) {
                  val = com.sun.ts.lib.util.sec.net.www.ParseUtil
                      .encodePath(val);
                }
              } catch (URISyntaxException use) {
                val = com.sun.ts.lib.util.sec.net.www.ParseUtil.encodePath(val);
              }
            }
            sb.append(val);
          } else {
            throw new ExpandException("unable to expand property " + prop);
          }
        }
      }
      i = pe + 1;
      p = value.indexOf("${", i);
      if (p == -1) {
        // no more to expand. copy in any extra
        if (i < max) {
          sb.append(value.substring(i, max));
        }
        // break out of loop
        break scanner;
      }
    }
    return sb.toString();
  }
}
