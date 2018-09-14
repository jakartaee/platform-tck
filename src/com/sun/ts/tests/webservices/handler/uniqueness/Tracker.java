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

package com.sun.ts.tests.webservices.handler.uniqueness;

import java.util.*;

public class Tracker {
  private static Map handlerinfoMap;

  private static List configList;

  private static Map instanceMap;

  static {
    handlerinfoMap = new HashMap();
    configList = new ArrayList();
    instanceMap = new HashMap();
  }

  public static synchronized void checkHandlerInfo(Object hi) {
    Object value = handlerinfoMap.get(hi);
    if (value == null) {
      handlerinfoMap.put(hi, "in-use");
    } else {
      System.out.println(
          "DEBUG: checkHandlerInfo: SPEC Violation (HandlerInfo inst already in use) throwing RuntimeException");
      throw new RuntimeException("HandlerInfo inst already in use: " + hi);
    }
  }

  public static synchronized void checkConfig(Object cfg) {
    Iterator it = configList.iterator();
    while (it.hasNext()) {
      Object o = it.next();
      if (cfg == null) {
        System.out.println(
            "DEBUG: checkConfig: SPEC Violation (Config object is null) throwing RuntimeException");
        throw new RuntimeException("Config object is null");
      } else if (o == cfg) {
        System.out.println(
            "DEBUG: checkConfig: SPEC Violation (Config inst already in use) throwing RuntimeException");
        throw new RuntimeException("Config inst already in use: " + cfg);
      }
    }
    configList.add(cfg);
  }

  public static synchronized void checkInstance(Object inst) {
    Object value = instanceMap.get(inst);
    if (value == null) {
      instanceMap.put(inst, "in-use");
    } else {
      System.out.println(
          "DEBUG: checkInstance: SPEC Violation (Handler inst already in use) throwing RuntimeException");
      throw new RuntimeException("Handler inst already in use: " + inst);
    }
  }

  public static synchronized void removeHandlerInfo(Object hi) {
    System.out.println("DEBUG: removeHandlerInfo:");
    Object value = handlerinfoMap.remove(hi);
  }

  public static synchronized void removeConfig(Object cfg) {
    System.out.println("DEBUG: removeConfig:");
    Iterator it = configList.iterator();
    while (it.hasNext()) {
      Object o = it.next();
      if (o == cfg)
        it.remove();
    }
  }

  public static synchronized void removeInstance(Object inst) {
    System.out.println("DEBUG: removeInstance:");
    Object value = instanceMap.remove(inst);
  }
}
