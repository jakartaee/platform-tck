/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.nestedembedding;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

@Entity
@Table(name = "BNE_1XM_BI_BTOB")
public class B implements java.io.Serializable {
  // ===========================================================
  // instance variables
  @Id
  protected String id;

  @Basic
  protected String name;

  @Basic
  protected int value;

  @Embedded
  protected Address address;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  // ===========================================================
  // relationship fields
  @ManyToOne(targetEntity = com.sun.ts.tests.jpa.core.nestedembedding.A.class)
  @JoinColumn(name = "FK_FOR_ANE_1XM_BI_BTOB", nullable = true)
  protected A a1;

  // ===========================================================
  // constructors
  public B() {
    // TestUtil.logTrace("Entity B no arg constructor");
  }

  public B(String id, String name, int value) {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  public B(String id, String name, int value, A a1) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.a1 = a1;
  }

  // ==========================================================
  // Business Methods for Test Cases
  public A getA1() {
    return a1;
  }

  public void setA1(A a1) {
    this.a1 = a1;
  }

  public boolean isA() {
    TestUtil.logTrace("isA");
    if (getA1() != null) {
      TestUtil.logTrace("Relationship set for A ...");
    } else {
      TestUtil.logTrace("Relationship not set for A ...");
    }
    return getA1() != null;
  }

  public A getA1Info() {
    TestUtil.logTrace("getA1Info");
    if (isA()) {
      A a1 = getA1();
      return a1;
    } else {
      return null;
    }
  }

  public String getBId() {
    return id;
  }

  public String getBName() {
    return name;
  }

  public void setBName(String bName) {
    this.name = bName;
  }

  public int getBValue() {
    return value;
  }

  public Collection getAInfoFromB() {
    Vector v = new Vector();
    if (getA1() != null) {
      Collection bcol = getA1().getBCol();
      Iterator iterator = bcol.iterator();
      while (iterator.hasNext()) {
        B b = (B) iterator.next();
        A a = b.getA1();
        v.add(a);
      }
    }
    return v;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(this.getClass().getSimpleName() + "[");
    result.append("id: " + getBId());

    if (getBName() != null) {
      result.append("Name: " + getBName());
    } else {
      result.append("Name: null");
    }
    result.append("Value: " + getBValue());
    if (getAddress() != null) {
      result.append("Address: " + getAddress().toString());
    } else {
      result.append("Address: null");
    }
    result.append("]");
    return result.toString();
  }

  @Override
  public int hashCode() {
    return this.getBId().hashCode() + this.getBName().hashCode()
        + this.getBValue();
  }

  public boolean equals(Object o) {
    B other;
    boolean same = true;

    if (!(o instanceof B)) {
      return false;
    }
    other = (B) o;

    same &= this.id.equals(other.id) && this.name.equals(other.name)
        && this.value == other.value;

    return same;
  }

}
