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

package com.sun.ts.tests.ejb30.lite.tx.cm.common;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "COFFEEEJBLITE")
public class CoffeeEJBLite implements java.io.Serializable {

  private Integer id;

  private String brandName;

  private float price;

  public CoffeeEJBLite() {
  }

  public CoffeeEJBLite(Integer id, String brandName, float price) {
    this.id = id;
    this.brandName = brandName;
    this.price = price;
  }

  @Id
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(name = "BRANDNAME")
  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String bName) {
    this.brandName = bName;
  }

  @Column(name = "PRICE")
  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "CoffeeEJBLite id=" + getId() + ", brandName=" + getBrandName()
        + ", price=" + getPrice();
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not
    // set
    if (!(object instanceof CoffeeEJBLite)) {
      return false;
    }
    CoffeeEJBLite other = (CoffeeEJBLite) object;
    if ((this.id == null && other.id != null)
        || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }
}
