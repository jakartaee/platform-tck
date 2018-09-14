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

import java.rmi.*;
import java.io.*;

public class PolarBear extends Bear implements Fuzzy, Color, Serializable {
  int size = 0;

  int weight = 0;

  boolean fuzzy = false;

  String color = "white";

  public PolarBear(int size, int weight, boolean fuzzy, String color) {
    this.size = size;
    this.weight = weight;
    this.fuzzy = fuzzy;
    this.color = color;
  }

  public int getSize() {
    return size;
  }

  public int getWeight() {
    return weight;
  }

  public boolean isFuzzy() {
    return fuzzy;
  }

  public String getColor() throws RemoteException {
    return color;
  }
}
