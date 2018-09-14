/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.customizedmapping.adapters.model;

public class Dog extends Animal {
  private boolean barking;

  public Dog() {
  }

  public Dog(int age, String name, float weight, boolean furry,
      boolean barking) {
    super(age, name, weight, furry);
    this.barking = barking;
  }

  public boolean isBarking() {
    return barking;
  }

  public void setBarking(boolean barking) {
    this.barking = barking;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Dog))
      return false;
    if (!super.equals(o))
      return false;

    Dog dog = (Dog) o;

    return barking == dog.barking;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (barking ? 1 : 0);
    return result;
  }
}
