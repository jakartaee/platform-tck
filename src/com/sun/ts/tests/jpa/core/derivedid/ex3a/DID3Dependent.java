/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.derivedid.ex3a;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Raja Perumal
 */
@Entity
@IdClass(DID3DependentId.class)
public class DID3Dependent implements Serializable {

  @Id
  @Column(name = "NAME")
  String name2;

  @Id
  @JoinColumns({
      @JoinColumn(name = "FIRSTNAME", referencedColumnName = "firstname"),
      @JoinColumn(name = "LASTNAME", referencedColumnName = "lastname") })
  @ManyToOne
  DID3Employee emp;

  public DID3Dependent(String name2, DID3Employee emp) {
    this.name2 = name2;
    this.emp = emp;
  }

  public DID3Dependent(DID3DependentId dId, DID3Employee emp) {
    this.name2 = dId.getName();
    this.emp = emp;
  }

  public DID3Dependent() {
  }

  public DID3Employee getEmp() {
    return emp;
  }

  public void setEmp(DID3Employee emp) {
    this.emp = emp;
  }

  public String getName() {
    return this.name2;
  }

  public void setName(String name2) {
    this.name2 = name2;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DID3Dependent other = (DID3Dependent) obj;
    if ((this.name2 == null) ? (other.name2 != null)
        : !this.name2.equals(other.name2)) {
      return false;
    }
    if (this.emp != other.emp
        && (this.emp == null || !this.emp.equals(other.emp))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (this.name2 != null ? this.name2.hashCode() : 0);
    hash = 31 * hash + (this.emp != null ? this.emp.hashCode() : 0);
    return hash;
  }
}
