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

package com.sun.ts.tests.jpa.core.entitytest.detach.manyXmany;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "AEJB_MXM_BI_BTOB")
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

  public A(String id, String name, int value, Collection bCol) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.bCol = bCol;
  }

  // ===========================================================
  // relationship fields

  @ManyToMany(targetEntity = com.sun.ts.tests.jpa.core.entitytest.detach.manyXmany.B.class, cascade = {
      CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE,
      CascadeType.REFRESH })
  @JoinTable(name = "FKEYS_MXM_BI_BTOB", joinColumns = @JoinColumn(name = "FK_FOR_AEJB_MXM_BI_BTOB", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "FK_FOR_BEJB_MXM_BI_BTOB", referencedColumnName = "ID"))
  protected Collection bCol = new java.util.ArrayList();

  // =======================================================================
  // Business methods for test cases

  public Collection getBCol() {
    TestUtil.logTrace("getBCol");
    return bCol;
  }

  public void setBCol(Collection bCol) {
    TestUtil.logTrace("setBCol");
    this.bCol = bCol;
  }

  public String getAId() {
    return id;
  }

  public String getAName() {
    return name;
  }

  public void setAName(String aName) {
    this.name = aName;
  }

  public int getAValue() {
    return value;
  }

}
