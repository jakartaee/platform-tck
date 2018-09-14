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

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;

import java.util.*;

public final class HandlerTracker {

  // Messages for LogicalHandler and SOAPHandler tests
  private static Vector<String> messages1 = new Vector<String>();

  // Messages for MessageContext tests
  private static Vector<String> messages2 = new Vector<String>();

  // Messages for SOAPMessageContext tests
  private static Vector<String> messages3 = new Vector<String>();

  // Messages for LogicalMessageContext tests
  private static Vector<String> messages4 = new Vector<String>();

  // Message Handlers
  private static Vector<Handler> handlers = new Vector<Handler>();

  // Throwables
  private static Vector<String> throwables = new Vector<String>();

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

  public static String getIdNameOnly(Handler handler) {
    String name = getId(handler);
    int i = name.lastIndexOf(".");
    if (i >= 0) {
      name = name.substring(i + 1);
    }
    return name;
  }

  public static void reportInit(Handler handler, String methodName) {
    TestUtil.logTrace(
        "HandlerTracker: " + getIdNameOnly(handler) + "." + methodName + "()");
    if (!handlers.contains(handler))
      handlers.add(handler);
    messages1.add(getIdNameOnly(handler) + "." + methodName + "()");
  }

  public static void reportDestroy(Handler handler, String methodName) {
    TestUtil.logTrace(
        "HandlerTracker: " + getIdNameOnly(handler) + "." + methodName + "()");
    messages1.add(getIdNameOnly(handler) + "." + methodName + "()");
  }

  public static void reportGetHeaders(Handler handler) {
    TestUtil.logTrace(
        "HandlerTracker: " + getIdNameOnly(handler) + ".getHeaders()");
    messages1.add(getIdNameOnly(handler) + ".getHeaders()");
  }

  public static void reportHandleFault(Handler handler) {
    TestUtil.logTrace(
        "HandlerTracker: " + getIdNameOnly(handler) + ".handleFault()");
    messages1.add(getIdNameOnly(handler) + ".handleFault()");
  }

  public static void reportClose(Handler handler) {
    TestUtil.logTrace("HandlerTracker: " + getIdNameOnly(handler) + ".close()");
    messages1.add(getIdNameOnly(handler) + ".close()");
  }

  public static void reportComment(Handler handler, String comment) {
    TestUtil
        .logTrace("HandlerTracker: " + getIdNameOnly(handler) + " " + comment);
    messages1.add(getIdNameOnly(handler) + " " + comment);
  }

  public static void reportHandleMessage(Handler handler, String direction) {
    TestUtil.logTrace("HandlerTracker: " + getIdNameOnly(handler)
        + ".handleMessage().do" + direction + "()");
    messages1
        .add(getIdNameOnly(handler) + ".handleMessage().do" + direction + "()");
  }

  public static void reportPut(Handler h, String p, String v) {
    TestUtil.logTrace("HandlerTracker: reportPut()=" + getIdNameOnly(h)
        + ".MessageContext.put(" + p + "," + v + ")");
    messages2
        .add(getIdNameOnly(h) + ".MessageContext.put(" + p + "," + v + ")");
  }

  public static void reportSetScope(Handler h, String p, String s) {
    TestUtil.logTrace("HandlerTracker: reportSetScope()=" + getIdNameOnly(h)
        + ".MessageContext.setPropertyScope(" + p + "," + s + ")");
    messages2.add(getIdNameOnly(h) + ".MessageContext.setPropertyScope(" + p
        + "," + s + ")");
  }

  public static void reportProperties(Handler h, String d, String p, String v) {
    TestUtil.logTrace("HandlerTracker: reportProperties()=" + d
        + getIdNameOnly(h) + ".MessageContext.getProperty(" + p + ")=" + v);
    messages2.add(
        d + getIdNameOnly(h) + ".MessageContext.getProperty(" + p + ")=" + v);
  }

  public static void reportGet(Handler h, String p, String v) {
    TestUtil.logTrace("HandlerTracker: reportGet()=" + getIdNameOnly(h)
        + ".MessageContext.getProperty(" + p + ")=" + v);
    messages2
        .add(getIdNameOnly(h) + ".MessageContext.getProperty(" + p + ")=" + v);
  }

  public static void reportGetScope(Handler h, String p, String v) {
    TestUtil.logTrace("HandlerTracker: reportGetScope()=" + getIdNameOnly(h)
        + ".MessageContext.getPropertyScope(" + p + ")=" + v);
    messages2.add(
        getIdNameOnly(h) + ".MessageContext.getPropertyScope(" + p + ")=" + v);
  }

  public static void reportSMCGetMessage(Handler h, String direction,
      String s) {
    TestUtil.logTrace("HandlerTracker: reportSMCGetMessage()=" + direction
        + getIdNameOnly(h) + ".SOAPMessageContext.getMessage()=" + s);
    messages3.add(
        direction + getIdNameOnly(h) + ".SOAPMessageContext.getMessage()=" + s);
  }

  public static void reportSMCSetMessage(Handler h, String direction) {
    messages3.add(direction + getIdNameOnly(h)
        + ".SOAPMessageContext.setMessage() was called");
  }

  public static void reportSMCGetRoles(Handler h, String direction,
      java.util.Set<String> set) {
    Object[] o = set.toArray();
    StringBuffer s = new StringBuffer("|");
    for (int i = 0; i <= o.length - 1; i++) {
      s.append(o[i]);
    }
    s.append("|");
    messages3.add(
        direction + getIdNameOnly(h) + ".SOAPMessageContext.getRoles()=" + s);
  }

  public static void reportSMCGetHeaders(Handler h, String direction,
      Object[] o) {
    StringBuffer s = new StringBuffer("|");
    for (int i = 0; i <= o.length - 1; i++) {
      s.append(o[i]);
    }
    s.append("|");
    messages3.add(
        direction + getIdNameOnly(h) + ".SOAPMessageContext.getHeaders()=" + s);
  }

  public static void reportLMCGetMessage(Handler h, String direction,
      String s) {
    TestUtil.logTrace("HandlerTracker: reportLMCGetMessage()=" + direction
        + getIdNameOnly(h) + ".LogicalMessageContext.getMessage()=" + s);
    messages4.add(direction + getIdNameOnly(h)
        + ".LogicalMessageContext.getMessage()=" + s);
  }

  public static void reportThrowable(Handler handler, Throwable t) {
    TestUtil.logTrace("HandlerTracker: reportThrowable(): "
        + getIdNameOnly(handler) + " " + t);
    throwables.add(getIdNameOnly(handler));
    throwables.add(t.toString());
    StackTraceElement[] ste = t.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
      throwables.add(ste[i].toString());
    }
  }

  public static void purge() {
    messages1.clear();
    messages2.clear();
    messages3.clear();
    messages4.clear();
    throwables.clear();
  }

  // Return handler test messages
  public static String[] getArrayMessages1() {
    String[] result = new String[messages1.size()];
    for (int i = 0; i < messages1.size(); i++) {
      result[i] = (String) messages1.elementAt(i);
    }
    return result;
  }

  // Return handler test messages
  public static List<String> getListMessages1() {
    return messages1;
  }

  // Return Handler test messages
  public static String getMessages1() {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < messages1.size(); i++) {
      if (i + 1 == messages1.size())
        result.append((String) messages1.elementAt(i));
      else
        result.append((String) messages1.elementAt(i) + "\n");
    }
    return result.toString();
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
  public static List<String> getListMessages2() {
    return messages2;
  }

  // Return MessageContext test messages
  public static String getMessages2() {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < messages2.size(); i++) {
      if (i + 1 == messages2.size())
        result.append((String) messages2.elementAt(i));
      else
        result.append((String) messages2.elementAt(i) + "\n");
    }
    return result.toString();
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
  public static List<String> getListMessages3() {
    return messages3;
  }

  // Return SOAPMessageContext test messages
  public static String getMessages3() {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < messages3.size(); i++) {
      if (i + 1 == messages3.size())
        result.append((String) messages3.elementAt(i));
      else
        result.append((String) messages3.elementAt(i) + "\n");
    }
    return result.toString();
  }

  // Return LogicalMessageContext test messages
  public static String[] getArrayMessages4() {
    String[] result = new String[messages4.size()];
    for (int i = 0; i < messages4.size(); i++) {
      result[i] = (String) messages4.elementAt(i);
    }
    return result;
  }

  // Return LogicalMessageContext test messages
  public static List<String> getListMessages4() {
    return messages4;
  }

  // Return LogicalMessageContext test messages
  public static String getMessages4() {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < messages4.size(); i++) {
      if (i + 1 == messages4.size())
        result.append((String) messages4.elementAt(i));
      else
        result.append((String) messages4.elementAt(i) + "\n");
    }
    return result.toString();
  }

  // Return Throwables
  public static String[] getArrayThrowables() {
    String[] result = new String[throwables.size()];
    for (int i = 0; i < throwables.size(); i++) {
      result[i] = (String) throwables.elementAt(i);
    }
    return result;
  }

  // Return Message Handlers
  public static List<Handler> getHandlerMessages() {
    return handlers;
  }

  // Clear Message Handlers vector
  public static void clearHandlers() {
    handlers.clear();
  }

}
