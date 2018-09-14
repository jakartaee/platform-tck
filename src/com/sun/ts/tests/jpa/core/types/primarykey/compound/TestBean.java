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

package com.sun.ts.tests.jpa.core.types.primarykey.compound;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PKEY")
public class TestBean implements java.io.Serializable {

  private CompoundPK compoundPK;

  private String brandName;

  private float price;

  public TestBean() {
  }

  public TestBean(CompoundPK pk, String brandName, float price) {
    this.compoundPK = pk;
    this.brandName = brandName;
    this.price = price;
  }

  @EmbeddedId
  public CompoundPK getCompoundPK() {
    return compoundPK;
  }

  public void setCompoundPK(CompoundPK compoundPK) {
    this.compoundPK = compoundPK;
  }

  @Basic
  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String v) {
    this.brandName = v;
  }

  @Basic
  public float getPrice() {
    return price;
  }

  public void setPrice(float v) {
    this.price = v;
  }

  public void ping() {
    TestUtil.logTrace("[TestBean] ping()");
  }

}
