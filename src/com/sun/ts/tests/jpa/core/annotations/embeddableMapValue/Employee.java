/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.embeddableMapValue;

import javax.persistence.*;
import java.util.Map;

/*
 * Employee
 */

@Entity
@Table(name = "EMPLOYEE_EMBEDED_ADDRESS")
public class Employee implements java.io.Serializable {

  private int id;

  private String firstName;

  private String lastName;

  private Map<String, Address> locationAddress;

  public Employee() {
  }

  public Employee(int id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Column(name = "FIRSTNAME")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "LASTNAME")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @ElementCollection
  @CollectionTable(name = "COLTAB_EMP_EMBEDED_ADDRESS", joinColumns = @JoinColumn(name = "EMPEMBADDRID"))
  @MapKeyColumn(name = "ADDRESS_LOCATION")
  public Map<String, Address> getLocationAddress() {
    return locationAddress;
  }

  public void setLocationAddress(Map<String, Address> locationAddress) {
    this.locationAddress = locationAddress;
  }

}
