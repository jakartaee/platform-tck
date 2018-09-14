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

package com.sun.ts.tests.ejb.ee.timer.common;

import java.io.Serializable;

public final class TimerInfo implements Serializable {

  public String str;

  public Integer i;

  public Boolean bool;

  public Double doubl;

  public TimerInfo(String str, int i, boolean bool, double doubl) {

    this.str = str;
    this.i = new Integer(i);
    this.bool = new Boolean(bool);
    this.doubl = new Double(doubl);
  }

  public boolean equals(Object o) {

    if (o instanceof TimerInfo) {
      TimerInfo info = (TimerInfo) o;
      return (this.str.equals(info.str) && this.i.equals(info.i)
          && this.bool.equals(info.bool) && this.doubl.equals(info.doubl));
    }
    return false;
  }

  public String toString() {

    return (str + " " + i.toString() + " " + bool.toString() + " "
        + doubl.toString());
  }
}
