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
 * $Id: Toyota.java 51063 2006-08-11 19:56:36Z adf $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.typesubstitution;

public class Toyota extends Car {

  private String color;

  private final String make = "Toyota";

  public Toyota() {
    setMake("Toyota");
  }

  public String getMake() {
    return make;
  }

  public Toyota(String model, String year, String color) {
    setModel(model);
    setYear(year);
    this.color = color;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String toString() {
    return getMake() + ":" + getModel() + ":" + getYear() + ":" + color;
  }

}
