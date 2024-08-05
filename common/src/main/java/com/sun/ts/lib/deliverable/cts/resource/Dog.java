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

package com.sun.ts.lib.deliverable.cts.resource;

/**
 * A simple JavaBean class to be used as a custom resource type.
 */
public class Dog implements java.io.Serializable {
  public static final String DOG_NAME = "wangwang";

  public static final int DOG_AGE = 2;

  private static Dog instance = new Dog();

  private int age = DOG_AGE;

  private String name = DOG_NAME;

  public Dog() {
  }

  public static Dog getInstance() {
    return instance;
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

  @Override
  public String toString() {
    String retValue;
    retValue = super.toString() + ", name=" + name + ", age=" + age;
    return retValue;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ((name == null) ? 0 : name.hashCode());
    result = PRIME * result + age;
    return result;
  }

  @Override
  public boolean equals(Object anObject) {
    if (this == anObject) {
      return true;
    }
    if (anObject instanceof Dog) {
      Dog anotherDog = (Dog) anObject;
      return (this.age == anotherDog.age && this.name.equals(anotherDog.name));
    }
    return false;
  }

}
