/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.tests.security.permissions;

import java.security.BasicPermission;
import java.security.Permission;

/**
 * Java SecurityManager Permission class for CTS Test purposes. This permission
 * extends the Permission class and is used to perform validations of
 * permissions.xml.
 * 
 * Sample usage of this permssion: permission
 * com.sun.ts.tests.ejb30.sec.permsxml.CTSPermission1 "*";
 * 
 */

public final class CTSPermission1 extends Permission {

  private transient String methodName;

  private transient String actions;

  private transient int hashCodeValue = 0;

  /**
   * Create a new CTSPermission1 with no action
   *
   * @param name
   *          -
   */
  public CTSPermission1(String name) {
    super(name);
  }

  /**
   * Creates a new CTSPermission1 with action
   *
   * @param name
   *          JNDI resource path name
   * @param action
   *          JNDI action (none defined)
   */
  public CTSPermission1(String name, String actions) {
    super(name);
    this.actions = actions;
  }

  public boolean equals(Object o) {
    if (o == null || !(o instanceof CTSPermission1))
      return false;

    CTSPermission1 that = (CTSPermission1) o;

    if (!this.getName().equals(that.getName()))
      return false;

    if (this.methodName != null) {
      if (that.methodName == null || !this.methodName.equals(that.methodName)) {
        return false;
      }
    } else if (that.methodName != null) {
      return false;
    }

    if (this.actions != null) {
      if (that.actions == null || !this.actions.equals(that.actions)) {
        return false;
      }
    } else if (that.actions != null) {
      return false;
    }

    return true;
  }

  public String getActions() {
    return this.actions;
  }

  public void setActions(String val) {
    this.actions = val;
  }

  public String getMethodName() {
    return this.methodName;
  }

  public void setMethodName(String val) {
    this.methodName = val;
  }

  public int hashCode() {
    if (hashCodeValue == 0) {

      String hash = this.getName();
      if (this.actions != null) {
        hash += " " + this.actions;
      }
      hashCodeValue = hash.hashCode();
    }
    return this.hashCodeValue;
  }

  public boolean implies(Permission permission) {

    CTSPermission1 that = (CTSPermission1) permission;

    if ((permission == null) || !(permission instanceof CTSPermission1)) {
      return false;
    }

    if (!this.getName().equals(that.getName())) {
      return false;
    }

    if ((this.methodName != null) && (that.methodName == null
        || !this.methodName.equals(that.methodName))) {
      return false;
    }

    if ((this.actions != null)
        && (that.actions == null || !this.actions.equals(that.actions))) {
      return false;
    }

    return true;
  }

}
