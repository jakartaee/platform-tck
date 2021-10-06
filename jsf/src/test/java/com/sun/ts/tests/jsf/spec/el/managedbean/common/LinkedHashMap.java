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

package com.sun.ts.tests.jsf.spec.el.managedbean.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Simple {@link java.util.Map} to allow order validation of entries added by
 * the JSF implementation.
 */
public class LinkedHashMap extends HashMap {

  List linkedList;

  public LinkedHashMap() {
    super();
    linkedList = new LinkedList();
  }

  // ---------------------------------------------- Methods from java.util.Map

  /**
   * <p>
   * In addition to the standard behavior of the HashMap, this stores the keys
   * in a {@link LinkedList}.
   * </p>
   */
  public Object put(Object key, Object value) {

    linkedList.add(key);
    return super.put(key, value);

  } // END put

  // ---------------------------------------------------------- Public Methods

  /**
   * <p>
   * Return the backing {@link LinkedList}.
   * </p>
   */
  public List getKeyList() {

    return linkedList;

  } // END getKeyList

}
