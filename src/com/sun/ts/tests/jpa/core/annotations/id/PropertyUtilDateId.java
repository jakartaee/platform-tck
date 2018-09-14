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

package com.sun.ts.tests.jpa.core.annotations.id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DATATYPES2")
public class PropertyUtilDateId implements java.io.Serializable {

  protected java.util.Date id;

  private java.util.Date date;

  public PropertyUtilDateId() {
  }

  public PropertyUtilDateId(java.util.Date id, java.util.Date date) {
    this.id = id;
    this.date = date;
  }

  @Id
  @Column(name = "DATATYPES2_ID")
  @Temporal(TemporalType.DATE)
  public java.util.Date getId() {
    return id;
  }

  public void setId(java.util.Date id) {
    this.id = id;
  }

  @Column(name = "DATEDATA")
  @Temporal(TemporalType.DATE)
  public java.util.Date getDate() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(this.getClass().getSimpleName() + "[");
    result.append("id: " + getId());
    if (getDate() != null) {
      result.append(", date: " + getDate());
    } else {
      result.append(", date: null");
    }
    result.append("]");
    return result.toString();
  }
}
