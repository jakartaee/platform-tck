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

package com.sun.ts.tests.jaspic.tssv.util;

import java.security.Principal;
import javax.security.auth.Subject;

/**
 *
 * @author Raja Perumal
 */
public class SimplePrincipal implements Principal, java.io.Serializable {
  private String name; // username

  private String password; // password

  public SimplePrincipal(String val, String pwd) {
    name = val;
    password = pwd;
  }

  // required to satisfy Principal interface
  public boolean equals(Object another) {
    if ((another != null) && (another instanceof SimplePrincipal)
        && (((SimplePrincipal) another).getName().equals(name))) {
      return true;
    } else {
      return false;
    }
  }

  // required to satisfy Principal interface
  public String getName() {
    return name;
  }

  // required to satisfy Principal interface
  public String toString() {
    return name;
  }

  // required to satisfy Principal interface
  public int hashCode() {
    return name.hashCode();
  }

  // XXXX: may want to change this later if tests call for it
  // this is normally bad but for now we dont care
  public String getPassword() {
    return password;
  }
} // end of class SimulateRuntime
