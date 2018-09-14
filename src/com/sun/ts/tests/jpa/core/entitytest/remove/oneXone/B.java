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

package com.sun.ts.tests.jpa.core.entitytest.remove.oneXone;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.*;

@Entity
@Table(name = "BEJB_1X1_BI_BTOB")
public class B implements java.io.Serializable {

  // ===========================================================
  // instance variables
  @Id
  protected String id;

  @Basic
  protected String name;

  @Basic
  protected int value;

  // ===========================================================
  // relationship fields

  @OneToOne(targetEntity = com.sun.ts.tests.jpa.core.entitytest.remove.oneXone.A.class, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "FK_FOR_AEJB_1X1_BI_BTOB")
  protected A a1;

  // ===========================================================
  // constructors

  public B() {
    TestUtil.logTrace("Entity B no arg constructor");
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

  public boolean isA() {
    TestUtil.logTrace("isA");
    if (getA1() != null)
      TestUtil.logTrace("Relationship to A is not null...");
    else
      TestUtil.logTrace("Relationship to A is null...");
    return getA1() != null;
  }

  public A getA1Info() {
    TestUtil.logTrace("getA1Info");
    if (isA()) {
      A a1 = getA1();
      return a1;
    } else
      return null;
  }

  public String getBId() {
    return id;
  }

  public String getBName() {
    return name;
  }

  public int getBValue() {
    return value;
  }
}
