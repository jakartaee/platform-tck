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

@MappedSuperclass()
public abstract class B implements java.io.Serializable {

  protected String name;

  @ElementCollection
  @CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
  protected List<Address> lAddress_inherited = new ArrayList<Address>();

  @ElementCollection
  @CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
  protected Map<Address, String> mAddress_inherited = new HashMap<Address, String>();

  @ElementCollection
  @CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
  Collection<Address> cAddress_inherited = new ArrayList<Address>();

  @ElementCollection
  @CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
  Set<Address> sAddress_inherited = new HashSet<Address>();

  public B() {
  }

  public B(String name) {
    this.name = name;
  }

  public Set<Address> getAddressSet() {
    TestUtil.logTrace("getAddressSet");
    return sAddress_inherited;
  }

  public void setAddressSet(Set<Address> addr) {
    TestUtil.logTrace("setAddressSet");
    this.sAddress_inherited = addr;
  }

  public Collection<Address> getAddressCollection() {
    TestUtil.logTrace("getAddressCollection");
    return cAddress_inherited;
  }

  public void setAddressCollection(Collection<Address> addr) {
    TestUtil.logTrace("setAddressCollection");
    this.cAddress_inherited = addr;
  }

  public List<Address> getAddressList() {
    TestUtil.logTrace("getAddressList");
    return lAddress_inherited;
  }

  public void setAddressList(List<Address> addr) {
    TestUtil.logTrace("setAddressList");
    this.lAddress_inherited = addr;
  }

  public Map<Address, String> getAddressMap() {
    TestUtil.logTrace("getAddressMap");
    return mAddress_inherited;
  }

  public void setAddressMap(Map<Address, String> addr) {
    TestUtil.logTrace("setAddressMap");
    this.mAddress_inherited = addr;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
