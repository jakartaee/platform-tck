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

package com.sun.ts.tests.jpa.core.metamodelapi.identifiabletype;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COLTAB")
@AttributeOverrides({
    @AttributeOverride(name = "name", column = @Column(name = "NAME")) })
public class A extends B {

  @Id
  protected String id;

  @Version
  protected Integer value;

  @ElementCollection
  @CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
  protected List<Address> lAddress = new ArrayList<Address>();

  @ElementCollection
  @CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
  protected Map<Address, String> mAddress = new HashMap<Address, String>();

  @ElementCollection
  @CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
  Collection<Address> cAddress = new ArrayList<Address>();

  @ElementCollection
  @CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
  Set<Address> sAddress = new HashSet<Address>();

  public A() {
  }

  public A(String id, String name, int value) {
    super(name);
    this.id = id;
    this.value = value;
  }

  public Set<Address> getAddressSet() {
    TestUtil.logTrace("getAddressSet");
    return sAddress;
  }

  public void setAddressSet(Set<Address> addr) {
    TestUtil.logTrace("setAddressSet");
    this.sAddress = addr;
  }

  public Collection<Address> getAddressCollection() {
    TestUtil.logTrace("getAddressCollection");
    return cAddress;
  }

  public void setAddressCollection(Collection<Address> addr) {
    TestUtil.logTrace("setAddressCollection");
    this.cAddress = addr;
  }

  public List<Address> getAddressList() {
    TestUtil.logTrace("getAddressList");
    return lAddress;
  }

  public void setAddressList(List<Address> addr) {
    TestUtil.logTrace("setAddressList");
    this.lAddress = addr;
  }

  public Map<Address, String> getAddressMap() {
    TestUtil.logTrace("getAddressMap");
    return mAddress;
  }

  public void setAddressMap(Map<Address, String> addr) {
    TestUtil.logTrace("setAddressMap");
    this.mAddress = addr;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer val) {
    this.value = val;
  }
}
