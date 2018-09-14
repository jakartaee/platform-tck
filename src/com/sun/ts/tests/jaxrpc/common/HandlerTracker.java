/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;

import java.util.*;

public final class HandlerTracker {
  // Messages for Handler tests
  private static Vector messages = new Vector();

  // Messages for MessageContext tests
  private static Vector messages2 = new Vector();

  // Messages for SOAPMessageContext tests
  private static Vector messages3 = new Vector();

  // SOAP Message Handlers
  private static Vector handlers = new Vector();

  // Store key <-> value information
  private static Map map = new Hashtable();

  public static Object get(String key) {
    return map.get(key);
  }

  public static void put(String key, Object value) {
    map.put(key, value);
  }

  public static boolean clearMap() {
    map = new Hashtable();
    return true;
  }

  public static String getId(Handler handler) {
    return handler.getClass().getName();
  }

  public static void reportInit(Handler handler) {
    System.out.println("HandlerTracker: " + getId(handler) + ".init()");
    if (!handlers.contains(handler))
      handlers.add(handler);
    messages.add(getId(handler) + ".init()");
  }

  public static void reportDestroy(Handler handler) {
    System.out.println("HandlerTracker: " + getId(handler) + ".destroy()");
    messages.add(getId(handler) + ".destroy()");
  }

  public static void reportGetHeaders(Handler handler) {
    System.out.println("HandlerTracker: " + getId(handler) + ".getHeaders()");
    messages.add(getId(handler) + ".getHeaders()");
  }

  public static void reportHandleFault(Handler handler) {
    System.out.println("HandlerTracker: " + getId(handler) + ".handleFault()");
    messages.add(getId(handler) + ".handleFault()");
  }

  public static void reportHandleRequest(Handler handler) {
    System.out
        .println("HandlerTracker: " + getId(handler) + ".handleRequest()");
    messages.add(getId(handler) + ".handleRequest()");
  }

  public static void reportHandleResponse(Handler handler) {
    System.out
        .println("HandlerTracker: " + getId(handler) + ".handleResponse()");
    messages.add(getId(handler) + ".handleResponse()");
  }

  public static void reportSetProperty(String p, String v) {
    messages2.add("MessageContext.setProperty(" + p + "," + v + ")");
  }

  public static void reportGetProperty(String p, String v) {
    messages2.add("MessageContext.getProperty(" + p + ")=" + v);
  }

  public static void reportContainsProperty(String p, boolean b) {
    messages2.add("MessageContext.containsProperty(" + p + ")=" + b);
  }

  public static void reportRemoveProperty(String p, boolean b) {
    messages2.add("MessageContext.removeProperty(" + p + ")=" + b);
  }

  public static void reportGetPropertyNames(Iterator i) {
    String s = null;
    while (i.hasNext()) {
      if (s == null)
        s = (String) i.next();
      else
        s = s + " " + (String) i.next();
    }
    messages2.add("MessageContext.getPropertyNames() = " + s);
  }

  public static void reportGetMessage(String s) {
    messages3.add("SOAPMessageContext.getMessage(" + s + ")");
  }

  public static void reportSetMessage(String s) {
    messages3.add("SOAPMessageContext.setMessage(" + s + ")");
  }

  public static void purge() {
    messages.clear();
    messages2.clear();
    messages3.clear();
  }

  // Return Handler test messages
  public static String[] getArray() {
    String[] result = new String[messages.size()];
    for (int i = 0; i < messages.size(); i++) {
      result[i] = (String) messages.elementAt(i);
    }
    return result;
  }

  // Return Handler test messages
  public static String get() {
    String result = "";
    for (int i = 0; i < messages.size(); i++) {
      if (i + 1 == messages.size())
        result = result + (String) messages.elementAt(i);
      else
        result = result + (String) messages.elementAt(i) + "\n";
    }
    return result;
  }

  // Return MessageContext test messages
  public static String[] getArrayMessages2() {
    String[] result = new String[messages2.size()];
    for (int i = 0; i < messages2.size(); i++) {
      result[i] = (String) messages2.elementAt(i);
    }
    return result;
  }

  // Return MessageContext test messages
  public static String getMessages2() {
    String result = "";
    for (int i = 0; i < messages2.size(); i++) {
      if (i + 1 == messages2.size())
        result = result + (String) messages2.elementAt(i);
      else
        result = result + (String) messages2.elementAt(i) + "\n";
    }
    return result;
  }

  // Return SOAPMessageContext test messages
  public static String[] getArrayMessages3() {
    String[] result = new String[messages3.size()];
    for (int i = 0; i < messages3.size(); i++) {
      result[i] = (String) messages3.elementAt(i);
    }
    return result;
  }

  // Return SOAPMessageContext test messages
  public static String getMessages3() {
    String result = "";
    for (int i = 0; i < messages3.size(); i++) {
      if (i + 1 == messages3.size())
        result = result + (String) messages3.elementAt(i);
      else
        result = result + (String) messages3.elementAt(i) + "\n";
    }
    return result;
  }

  // Return SOAP Message Handlers
  public static Vector getHandlers() {
    return handlers;
  }

  // Clear SOAP Message Handlers vector
  public static void clearHandlers() {
    handlers.clear();
  }

  // Remove either server-side or client-side SOAP Message Handlers from vector
  public static void removeHandlers(String which) {
    Vector v = new Vector();
    for (int i = 0; i < handlers.size(); i++) {
      if (handlers.elementAt(i).toString().indexOf(which) == -1)
        v.add(handlers.elementAt(i));
    }
    handlers = v;
  }
}
