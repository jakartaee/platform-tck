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

package com.sun.ts.tests.jpa.core.inheritance.nonentity;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

/*
 * Project
 */

@Entity
public class Project implements java.io.Serializable {

  // Instance Variables
  private long projId;

  private String name;

  private BigDecimal budget;

  private Employee projectLead;

  public Project() {
    TestUtil.logTrace("Project no-arg constructor");
  }

  public Project(long projId, String name, BigDecimal budget) {
    this(projId, name, budget, (Employee) null);
  }

  public Project(long projId, String name, BigDecimal budget,
      Employee projectLead) {
    this.projId = projId;
    this.name = name;
    this.budget = budget;
    this.projectLead = projectLead;
  }

  // ===========================================================
  // getters and setters for the state fields

  @Id
  public long getProjId() {
    return projId;
  }

  public void setProjId(long projId) {
    this.projId = projId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getBudget() {
    return budget;
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  // ===========================================================
  // getters and setters for the association fields

  @OneToOne(mappedBy = "project")
  public Employee getProjectLead() {
    return projectLead;
  }

  public void setProjectLead(Employee projectLead) {
    this.projectLead = projectLead;
  }

}
