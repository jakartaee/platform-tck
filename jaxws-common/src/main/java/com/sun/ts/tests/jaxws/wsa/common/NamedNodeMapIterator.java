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
 * $Id: NamedNodeMapIterator.java 51109 2006-09-28 15:28:12Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.common;

import java.util.Iterator;

import org.w3c.dom.NamedNodeMap;

public class NamedNodeMapIterator implements Iterator {

  protected NamedNodeMap _map;

  protected int _index;

  public NamedNodeMapIterator(NamedNodeMap map) {
    _map = map;
    _index = 0;
  }

  public boolean hasNext() {
    if (_map == null)
      return false;
    return _index < _map.getLength();
  }

  public Object next() {
    Object obj = _map.item(_index);
    if (obj != null)
      ++_index;
    return obj;
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}
