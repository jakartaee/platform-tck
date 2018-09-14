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

package com.sun.ts.tests.jpa.core.entitytest.persist.oneXmanyFetchEager;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;

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
    TestUtil.logTrace("Entity A no arg constructor");
  }

  public A(String id, String name, int value) {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  public A(String id, String name, int value, List<B> bCol) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.bCol = bCol;
  }

  // ===========================================================
  // relationship fields

  @OneToMany(targetEntity = com.sun.ts.tests.jpa.core.entitytest.persist.oneXmanyFetchEager.B.class, cascade = CascadeType.PERSIST, mappedBy = "a1", fetch = FetchType.EAGER)
  protected List<B> bCol = new java.util.ArrayList<B>();

  // =======================================================================
  // Business methods for test cases

  public List<B> getBCol() {
    TestUtil.logTrace("getBCol");
    return bCol;
  }

  public void setBCol(List<B> bCol) {
    TestUtil.logTrace("setBCol");
    this.bCol = bCol;
  }

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

  public List<B> getBInfoFromA() {
    TestUtil.logTrace("getBInfoFromA");
    List<B> v = new java.util.ArrayList<B>();
    if (getBCol().size() != 0) {
      List<B> bcol = getBCol();
      Iterator iterator = bcol.iterator();
      while (iterator.hasNext()) {
        B b = (B) iterator.next();
        v.add(b);
      }
    }
    return v;
  }

}
