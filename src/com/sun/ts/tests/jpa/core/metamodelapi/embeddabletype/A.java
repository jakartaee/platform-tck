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

package com.sun.ts.tests.jpa.core.metamodelapi.embeddabletype;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.*;

@Entity
@Table(name = "COLTAB")
public class A implements java.io.Serializable {

  @Id
  protected String id;

  protected String name;

  @Version
  protected Integer value;

  protected Address address;

  public A() {
  }

  public A(String id, String name, int value) {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  public A(String id, String name, int value, Address addr) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.address = addr;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer val) {
    this.value = val;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address addr) {
    this.address = addr;
  }
}
