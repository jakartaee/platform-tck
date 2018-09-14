/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox;

import java.security.Principal;

/**
 * @author Raja Perumal
 */
public class SimplePrincipal implements Principal, java.io.Serializable {
  private String name = null; // username

  private String password = null; // password

  public SimplePrincipal(String val) {
    name = val;
  }

  public SimplePrincipal(String val, String pwd) {
    name = val;
    password = pwd;
  }

  // required to satisfy Principal interface
  @Override
  public boolean equals(Object obj) {

    if ((obj == null) || !(obj instanceof SimplePrincipal)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    SimplePrincipal that = (SimplePrincipal) obj;

    if (!Util.isEqual(this.password, that.getPassword()))
      return false;

    if (!Util.isEqual(this.name, that.getName()))
      return false;

    return true;
  }

  // required to satisfy Principal interface
  @Override
  public String getName() {
    return name;
  }

  public void setName(String val) {
    name = val;
  }

  public void setPassword(String val) {
    password = val;
  }

  // required to satisfy Principal interface
  @Override
  public String toString() {
    return name;
  }

  // required to satisfy Principal interface
  @Override
  public int hashCode() {
    return this.getClass().getName().hashCode();
  }

  // may want to change this later if tests call for it
  // this is normally bad but for testing purposes we dont care
  public String getPassword() {
    return password;
  }
} // end of class SimulateRuntime
