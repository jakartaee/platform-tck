/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.relationship.unionexone;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class Uni1X1Person implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private String name;

  // OnePerson OneProject
  @OneToOne(cascade = CascadeType.ALL)
  private Uni1X1Project project;

  public Uni1X1Person() {
  }

  public Uni1X1Project getProject() {
    return project;
  }

  public void setProject(Uni1X1Project project) {
    this.project = project;
  }

  public Uni1X1Person(Long i, String string) {
    this.id = i;
    this.name = string;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "unionexone.Uni1X1Person[id=" + id + "]";
  }

}
