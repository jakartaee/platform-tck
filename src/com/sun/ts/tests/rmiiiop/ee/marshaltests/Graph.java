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

package com.sun.ts.tests.rmiiiop.ee.marshaltests;

import java.io.*;
import java.util.*;

public class Graph implements Serializable {

  public final String CONSTANT_STRING = "This is a constant string.";

  public final int CONSTANT_INT = 10;

  public final BitSet CONSTANT_BITSET = new BitSet(32);

  private String _list = null;

  private Graph _next = null;

  private BitSet _bitset = null;

  transient String t_string = "This is a transient string.";

  transient int t_int = 1111;

  transient BitSet t_bitset = new BitSet(32);

  public Graph(String data, Graph next) {
    this._list = data;
    this._bitset = new BitSet(64);
    this._bitset.set(10);
    this._bitset.set(20);
    this._bitset.set(30);
    this._bitset.set(40);
    this._bitset.set(50);
    this._bitset.set(60);
    this._next = next;
  }

  public String data() {
    return this._list;
  }

  public Graph next() {
    return this._next;
  }

  public void next(Graph next) {
    this._next = next;
  }

  public String toString() {
    StringBuffer result = new StringBuffer("{ ");
    for (Graph list = this; list != null; list = list.next()) {
      result.append(list.data()).append(" ");
    }
    return result.append("}").toString();
  }
}
