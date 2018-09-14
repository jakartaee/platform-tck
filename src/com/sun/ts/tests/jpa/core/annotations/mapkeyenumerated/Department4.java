/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.mapkeyenumerated;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "DEPARTMENT2")
public class Department4 implements java.io.Serializable {

  private static final long serialVersionUID = 22L;

  // Instance variables
  @Id
  private int id;

  private String name;

  @Transient
  private Map<Numbers, EmbeddedEmployee> lastNameEmployees;

  public Department4() {
  }

  public Department4(int id, String name) {
    this.id = id;
    this.name = name;
  }

  // ===========================================================
  // getters and setters for the state fields

  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // ===========================================================
  // getters and setters for the association fields

  @ElementCollection(targetClass = EmbeddedEmployee.class)
  @CollectionTable(name = "EMP_MAPKEYCOL2", joinColumns = @JoinColumn(name = "FK_DEPT5"))
  @AttributeOverrides({
      @AttributeOverride(name = "employeeId", column = @Column(name = "ID")),
      @AttributeOverride(name = "employeeName", column = @Column(name = "LASTNAME")) })
  @MapKeyEnumerated(EnumType.STRING)
  public Map<Numbers, EmbeddedEmployee> getLastNameEmployees() {
    return lastNameEmployees;
  }

  public void setLastNameEmployees(
      Map<Numbers, EmbeddedEmployee> lastNameEmployees) {
    this.lastNameEmployees = lastNameEmployees;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(this.getClass().getSimpleName() + "[");
    result.append("id: " + getId());
    if (getName() != null) {
      result.append(", name: " + getName());
    } else {
      result.append(", name: null");
    }
    result.append("]");
    return result.toString();
  }
}
