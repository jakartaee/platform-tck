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

public class Animal {
  private int age;

  private String name;

  private float weight;

  private boolean furry;

  public Animal() {
  }

  public Animal(int age, String name, float weight, boolean furry) {
    this.age = age;
    this.name = name;
    this.weight = weight;
    this.furry = furry;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getWeight() {
    return weight;
  }

  public void setWeight(float weight) {
    this.weight = weight;
  }

  public boolean isFurry() {
    return furry;
  }

  public void setFurry(boolean furry) {
    this.furry = furry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Animal))
      return false;

    Animal animal = (Animal) o;

    if (age != animal.age)
      return false;
    if (Float.compare(animal.weight, weight) != 0)
      return false;
    if (furry != animal.furry)
      return false;
    return name != null ? name.equals(animal.name) : animal.name == null;
  }

  @Override
  public int hashCode() {
    int result = age;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (weight != +0.0f ? Float.floatToIntBits(weight) : 0);
    result = 31 * result + (furry ? 1 : 0);
    return result;
  }
}
