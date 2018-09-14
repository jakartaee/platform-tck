/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.negdep;

/**
 * Use rather this user specific class than a java class such as stringbuilder
 * for sake of java class being allowed in future version by default
 * 
 * @since 1.11
 */
public class StringHolder implements CharSequence {
  public String inner = "";

  public StringHolder(String hold) {
    inner = hold;
  }

  public StringHolder() {
  }

  @Override
  public int length() {
    return inner.length();
  }

  @Override
  public char charAt(int index) {
    return inner.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return inner.subSequence(start, end);
  }

  @Override
  public String toString() {
    return inner;
  }
}
