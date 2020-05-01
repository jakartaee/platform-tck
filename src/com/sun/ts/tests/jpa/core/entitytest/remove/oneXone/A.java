/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.entitytest.remove.oneXone;

import com.sun.ts.lib.util.TestUtil;

import jakarta.persistence.*;

@Entity
@Table(name = "AEJB_1X1_BI_BTOB")
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
    TestUtil.logTrace("Entity A no arg constructor");
  }

  public A(String id, String name, int value) {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  public A(String id, String name, int value, B b1) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.b1 = b1;
  }

  // ===========================================================
  // relationship fields

  @OneToOne(targetEntity = com.sun.ts.tests.jpa.core.entitytest.remove.oneXone.B.class, mappedBy = "a1")
  protected B b1;

  // =======================================================================
  // Business methods for test cases

  public B getB1() {
    return b1;
  }

  public boolean isB1() {
    TestUtil.logTrace("isB");
    if (getB1() != null)
      TestUtil.logTrace("Relationship to B is not null...");
    else
      TestUtil.logTrace("Relationship to B is null...");
    return getB1() != null;
  }

  public B getB1Info() {
    TestUtil.logTrace("getBInfo");
    if (isB1()) {
      B b1 = getB1();
      return b1;
    } else
      return null;
  }

  public String getAId() {
    return id;
  }

  public String getAName() {
    return name;
  }

  public int getAValue() {
    return value;
  }

}
