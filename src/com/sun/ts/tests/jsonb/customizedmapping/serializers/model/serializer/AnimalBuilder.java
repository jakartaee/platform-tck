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

package com.sun.ts.tests.jsonb.customizedmapping.serializers.model.serializer;

import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.Animal;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.Cat;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.Dog;

public class AnimalBuilder {
  public enum TYPE {
    CAT, DOG, GENERIC
  }

  private TYPE type;

  private int age;

  private String name;

  private float weight;

  private boolean furry;

  private boolean cuddly;

  private boolean barking;

  public void setType(TYPE type) {
    this.type = type;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setWeight(float weight) {
    this.weight = weight;
  }

  public void setFurry(boolean furry) {
    this.furry = furry;
  }

  public void setCuddly(boolean cuddly) {
    this.cuddly = cuddly;
  }

  public void setBarking(boolean barking) {
    this.barking = barking;
  }

  public Animal build() {
    Animal animal;

    switch (type) {
    case CAT:
      animal = new Cat(age, name, weight, furry, cuddly);
      break;
    case DOG:
      animal = new Dog(age, name, weight, furry, barking);
      break;
    default:
      animal = new Animal(age, name, weight, furry);
    }

    return animal;
  }
}
