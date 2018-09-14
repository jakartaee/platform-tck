/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.common;

import java.util.HashSet;
import java.util.LinkedList;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class LinkedListHashSetTextDecoder
    implements Decoder.Text<LinkedList<HashSet<String>>> {

  @Override
  public void destroy() {
  }

  @Override
  public void init(EndpointConfig arg0) {
  }

  @Override
  public LinkedList<HashSet<String>> decode(String arg0)
      throws DecodeException {
    HashSet<String> set = new HashSet<String>();
    set.add(arg0);
    LinkedList<HashSet<String>> list = new LinkedList<HashSet<String>>();
    list.add(set);
    return list;
  }

  @Override
  public boolean willDecode(String arg0) {
    return true;
  }

}
