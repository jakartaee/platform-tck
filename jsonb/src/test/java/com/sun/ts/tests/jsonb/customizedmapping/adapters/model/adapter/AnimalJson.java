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

package com.sun.ts.tests.jsonb.customizedmapping.adapters.model.adapter;

public class AnimalJson {
  public enum TYPE {
    CAT, DOG, GENERIC
  }

  private TYPE type;

  private int age;

  private String name;

  private float weight;

  private Boolean furry;

  private Boolean cuddly;

  private Boolean barking;

  public TYPE getType() {
    return type;
  }

  public void setType(TYPE type) {
    this.type = type;
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

  public Boolean isFurry() {
    return furry;
  }

  public void setFurry(Boolean furry) {
    this.furry = furry;
  }

  public Boolean isCuddly() {
    return cuddly;
  }

  public void setCuddly(Boolean cuddly) {
    this.cuddly = cuddly;
  }

  public Boolean isBarking() {
    return barking;
  }

  public void setBarking(Boolean barking) {
    this.barking = barking;
  }
}
