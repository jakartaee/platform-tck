/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.websocket.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MessageValidator {

  public static boolean checkSearchStrings(String expected, String actual)
      throws IOException {

    List<String> list = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(expected, "|");
    while (st.hasMoreTokens()) {
      list.add(st.nextToken());
    }

    boolean found = true;
    if (list != null && actual != null) {

      String search = null;

      for (int i = 0, n = list.size(), startIdx = 0, acLength = actual
          .length(); i < n; i++) {

        // set the startIdx to the same value as the actual message length
        // and let the test fail (prevents index based runtime exceptions).
        if (startIdx >= acLength) {
          startIdx = acLength;
        }

        search = (String) list.get(i);
        int searchIdx = actual.indexOf(search, startIdx);
        System.out
            .println("[MessageValidator] Scanning for " + "search string: '"
                + search + "' starting at index " + "location: " + startIdx);
        if (searchIdx < 0) {
          found = false;
          StringBuffer sb = new StringBuffer(1024);
          sb.append("[MessageValidator] Unable to find the following ");
          sb.append("search string");
          sb.append(search).append("' at index: ");
          sb.append(startIdx);
          System.err.println(sb.toString());
          break;
        }

        System.out.println("[MessageValidator] Found search string: '" + search
            + "' at index '" + searchIdx);
        // the new searchIdx is the old index plus the lenght of the
        // search string.
        startIdx = searchIdx + search.length();
      }
    }
    return found;
  }
}
