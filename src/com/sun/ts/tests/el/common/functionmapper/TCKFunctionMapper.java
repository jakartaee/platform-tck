/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.common.functionmapper;

import com.sun.ts.lib.util.TestUtil;
import java.lang.reflect.Method;
import javax.el.FunctionMapper;

import java.util.HashMap;

/* A simple implementation of FunctionMapper that maps only
   a single function to Integer.valueOf(String).
*/

public class TCKFunctionMapper extends FunctionMapper {

  private static final String KEY = "Int:val";

  private final Class clazz = Integer.class;

  private final HashMap<String, Method> fMap;

  public TCKFunctionMapper() {

    fMap = new HashMap<String, Method>();
    try {
      fMap.put(KEY, clazz.getMethod("valueOf", String.class));
    } catch (NoSuchMethodException nsme) {
      TestUtil.logErr("CONSTRUCTOR: Can't find method!");
      TestUtil.printStackTrace(nsme);
    }
  }

  public Method resolveFunction(String prefix, String localName) {

    String key = prefix + ":" + localName;
    return fMap.get(key);
  }

  public void update() {

    fMap.remove(KEY);
    try {
      fMap.put(KEY, clazz.getMethod("toString", int.class));
    } catch (NoSuchMethodException nsme) {
      TestUtil.logErr("UPDATE: Can't find method!");
      TestUtil.printStackTrace(nsme);
    }
  }
}
