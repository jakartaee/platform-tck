/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class StringUtil {

  public static final String WEBSOCKET_SUBPROTOCOLS_0 = "MBWS.huawei.com";

  public static final String WEBSOCKET_SUBPROTOCOLS_1 = "MBLWS.huawei.com";

  public static final String WEBSOCKET_SUBPROTOCOLS_2 = "soap";

  public static final String WEBSOCKET_SUBPROTOCOLS_3 = "wamp";

  public static final String WEBSOCKET_SUBPROTOCOLS_4 = "v10.stomp";

  public static final String WEBSOCKET_SUBPROTOCOLS_5 = "v11.stomp";

  public static final String WEBSOCKET_SUBPROTOCOLS_6 = "v12.stomp";

  public static final String WEBSOCKET_SUBPROTOCOLS_7 = "ocpp1.2";

  public static final String WEBSOCKET_SUBPROTOCOLS_8 = "ocpp1.5";

  public static final String WEBSOCKET_SUBPROTOCOLS_9 = "ocpp2.0";

  /**
   * Registered subprotocols From IANA WebSocket Protocol Registries
   * http://www.iana.org/assignments/websocket/websocket.xml
   */
  public static final String[] WEBSOCKET_SUBPROTOCOLS = {
      WEBSOCKET_SUBPROTOCOLS_0, WEBSOCKET_SUBPROTOCOLS_1,
      WEBSOCKET_SUBPROTOCOLS_2, WEBSOCKET_SUBPROTOCOLS_3,
      WEBSOCKET_SUBPROTOCOLS_4, WEBSOCKET_SUBPROTOCOLS_5,
      WEBSOCKET_SUBPROTOCOLS_6, WEBSOCKET_SUBPROTOCOLS_7,
      WEBSOCKET_SUBPROTOCOLS_8, WEBSOCKET_SUBPROTOCOLS_9 };

  /**
   * @param objects
   *          to be put in a sentence
   * @return objects in a single string , each object separated by " "
   */
  public static String objectsToString(Object... objects) {
    return objectsToStringWithDelimiter(" ", objects);
  }

  public static String objectsToStringWithDelimiter(String delimiter,
      Object... objects) {
    StringBuilder sb = new StringBuilder();
    if (objects != null)
      for (Object o : objects) {
        if (o.getClass().isArray())
          sb.append(objectsToStringWithDelimiter(delimiter, (Object[]) o));
        else if (Collection.class.isInstance(o))
          sb.append(
              collectionToStringWithDelimiter(delimiter, (Collection<?>) o));
        else
          sb.append(o).append(delimiter);
      }
    return sb.toString().trim();
  }

  /**
   * @param objects
   *          to be put in a sentence
   * @return objects in a single string , each object separated by delimiter
   */
  public static String collectionToStringWithDelimiter(String delimiter,
      Collection<?> objects) {
    return objects == null ? ""
        : objectsToStringWithDelimiter(delimiter, objects.toArray());
  }

  /**
   * @param objects
   *          to be put in a sentence
   * @return objects in a single string , each object separated by " "
   */
  public static String collectionToString(Collection<?> objects) {
    return collectionToStringWithDelimiter(" ", objects);
  }

  /**
   * Check that every single String in one List is contained in other List
   * 
   * @param where
   *          The other list where String items are searched
   * @param what
   *          List of String items that are searched in the other List
   * @param caseSensitive
   *          define whether the occurrence of Strings is case sensitive
   * @return true iff every single item in {@link where} List is in {@link what}
   *         List
   */
  public static boolean //
      contains(List<String> where, List<String> what, boolean caseSensitive) {
    // do not use slow and memory consuming contains(List<T> where, List<T>
    // what)
    String whereInOne = objectsToStringWithDelimiter("", where);
    if (!caseSensitive)
      whereInOne = whereInOne.toLowerCase();
    for (String whatItem : what) {
      boolean found = false;
      if (caseSensitive)
        found = whereInOne.contains(whatItem);
      else
        found = whereInOne.contains(whatItem.toLowerCase());
      if (!found)
        return false;
    }
    return true;
  }

  /**
   * Check that every single T in one List is contained in other List
   * 
   * @param where
   *          The other list where T items are searched
   * @param what
   *          List of T items that are searched in the other List
   * @param comparator
   *          see {@link Comparator}
   * @return true iff every single item in {@link where} List is in {@link what}
   *         List
   */
  public static <T> boolean //
      contains(List<T> where, List<T> what, Comparator<? super T> comparator) {
    where = new ArrayList<T>(where); // new collection not to affect
    what = new ArrayList<T>(what); // the original by sorting
    Collections.sort(what, comparator);
    Collections.sort(where, comparator);
    Iterator<T> j = where.iterator();
    for (Iterator<T> i = what.iterator(); i.hasNext();) {
      T whatItem = i.next();
      T whereItem;
      do {
        if (!j.hasNext())
          return false;
        whereItem = j.next();
      } while (comparator.compare(whatItem, whereItem) != 0);
    }
    return true;
  }

  /**
   * Check that every single T in one List is contained in other List
   * 
   * @param where
   *          The other list where T items are searched
   * @param what
   *          List of T items that are searched in the other List
   * @return true iff every single item in {@link where} List is in {@link what}
   *         List
   */
  public static <T extends Comparable<? super T>> //
  boolean contains(List<T> where, List<T> what) {
    where = new ArrayList<T>(where); // new collection not to affect
    what = new ArrayList<T>(what); // the original by sorting
    Collections.sort(what);
    Collections.sort(where);
    return containsInOrder(where, what);
  }

  /**
   * Check that every single T in one List is contained in other List in given
   * order
   * 
   * @param where
   *          The other list where T items are searched
   * @param what
   *          List of T items that are searched in the other List
   * @return true iff every single item in {@link where} List is in {@link what}
   *         List
   */
  public static <T extends Comparable<? super T>> //
  boolean containsInOrder(List<T> where, List<T> what) {
    Iterator<T> j = where.iterator();
    for (Iterator<T> i = what.iterator(); i.hasNext();) {
      T whatItem = i.next();
      T whereItem;
      do {
        if (!j.hasNext())
          return false;
        whereItem = j.next();
      } while (whatItem.compareTo(whereItem) != 0);
    }
    return true;
  }
}
