/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.override.nocallbacklistener;

import com.sun.ts.tests.jpa.core.override.util.CallBackCounts;

import javax.persistence.*;

@Entity
@Table(name = "NOENTITYLISTENER_TABLE")
public class NoCallBackListener implements java.io.Serializable {

  private Long id;

  public NoCallBackListener() {
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void prePersistFromXML() {
    updateCallBackCount("prePersistFromXML");
  }

  public void preRemoveFromXML() {
    updateCallBackCount("preRemoveFromXML");
  }

  public void postRemoveFromXML() {
    updateCallBackCount("postRemoveFromXML");
  }

  @PostPersist
  public void postPersistFromXML() {
    updateCallBackCount("postPersist");
  }

  public void postLoadFromXML() {
    updateCallBackCount("postLoadFromXML");
  }

  protected void updateCallBackCount(String callBackKeyName) {
    CallBackCounts.updateCount(callBackKeyName);
  }

  public int hashCode() {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  public boolean equals(Object object) {
    if (!(object instanceof NoCallBackListener)) {
      return false;
    }
    NoCallBackListener other = (NoCallBackListener) object;
    if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  public String toString() {
    return "override.nocallbacklistener.NoCallBackListener id=" + id;
  }
}
