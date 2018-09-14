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

/**
 * $Id$
 *
 * @author Raja Perumal
 *         08/22/02
 */

package com.sun.ts.tests.jacc.util;

import java.lang.Long;
import java.lang.Integer;

public class JACCEntityKey implements java.io.Serializable {
  public String arg1;

  public Integer arg2;

  public Long arg3;

  public JACCEntityKey() {
  }

  public JACCEntityKey(String arg1, int arg2, long arg3) {
    this.arg1 = arg1;
    this.arg2 = new Integer(arg2);
    this.arg3 = new Long(arg3);
  }

  public boolean equals(Object newObject) {
    if (!(newObject instanceof JACCEntityKey))
      return false;

    JACCEntityKey newJACCEntityKey = (JACCEntityKey) newObject;

    if ((arg1.equals(newJACCEntityKey.arg1))
        && (arg2.intValue() == newJACCEntityKey.arg2.intValue())
        && (arg3.longValue() == newJACCEntityKey.arg3.longValue()))
      return true;
    else
      return false;
  }

  public int hashCode() {
    return arg1.hashCode() / 3 + arg2.hashCode() / 3 + arg3.hashCode() / 3;
  }
}
