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

package com.sun.ts.tests.jpa.core.entitytest.detach.basic;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AEJB_1XM_BI_BTOB")
public class A implements java.io.Serializable {

  // ===========================================================
  // instance variables

  @Id
  protected String id;

  @Basic
  protected String name;

  @Basic
  protected int value;

  // ===========================================================
  // constructors

  public A() {
    TestUtil.logTrace("in entity A no arg constructor");
  }

  public A(String id, String name, int value) {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  // ===========================================================
  // methods used by test cases

  public String getAId() {
    return id;
  }

  public String getAName() {
    return name;
  }

  public void setAName(String name) {
    this.name = name;
  }

  public int getAValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    // check for self-comparison
    if (this == o)
      return true;
    if (!(o instanceof A))
      return false;

    A o1 = (A) o;

    boolean result = false;

    if (this.getAId() == o1.getAId() && this.getAName().equals(o1.getAName())
        && this.getAValue() == o1.getAValue()) {
      result = true;
    }

    return result;

  }

  @Override
  public int hashCode() {
    return this.getAId().hashCode() + this.getAName().hashCode()
        + this.getAValue();
  }

  @Override
  public String toString() {
    return "[" + this.getAId() + ":" + this.getAName() + ":" + this.getAValue()
        + "]";
  }
}
