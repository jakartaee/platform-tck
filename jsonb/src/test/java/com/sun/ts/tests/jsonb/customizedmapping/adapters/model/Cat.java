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

public class Cat extends Animal {
  public Cat() {
  }

  public Cat(int age, String name, float weight, boolean furry,
      boolean cuddly) {
    super(age, name, weight, furry);
    this.cuddly = cuddly;
  }

  private boolean cuddly;

  public boolean isCuddly() {
    return cuddly;
  }

  public void setCuddly(boolean cuddly) {
    this.cuddly = cuddly;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Cat))
      return false;
    if (!super.equals(o))
      return false;

    Cat cat = (Cat) o;

    return cuddly == cat.cuddly;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (cuddly ? 1 : 0);
    return result;
  }
}
