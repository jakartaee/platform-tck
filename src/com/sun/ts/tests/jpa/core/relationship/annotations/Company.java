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

package com.sun.ts.tests.jpa.core.relationship.annotations;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.*;
import java.util.Collection;

/*
 * Company
 */

@Entity
public class Company implements java.io.Serializable {

  private long companyId;

  private String name;

  private Address address;

  private Collection<Team> teams = new java.util.ArrayList<Team>();

  public Company() {
    TestUtil.logTrace("Company no arg constructor");
  }

  public Company(long companyId, String name) {
    this.companyId = companyId;
    this.name = name;
  }

  public Company(long companyId, String name, Address addr) {
    this.companyId = companyId;
    this.name = name;
    this.address = addr;
  }

  // ===========================================================
  // getters and setters for the state fields

  @Id
  public long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(long companyId) {
    this.companyId = companyId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // ===========================================================
  // getters and setters for the association fields

  /* Uni-directional Single-Valued One(Company)ToOne(Address) - Company Owner */
  @OneToOne
  @JoinColumn(name = "ADDRESS_ID")
  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  /* Bi-directional One(Company)ToMany(Teams) - Owner Teams */
  @OneToMany(mappedBy = "company")
  public Collection<Team> getTeams() {
    return teams;
  }

  public void setTeams(Collection<Team> teams) {
    this.teams = teams;
  }

}
