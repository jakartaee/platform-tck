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

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.complexpktest;

public class ComplexPK implements java.io.Serializable {
  public Integer id;

  public String brandName;

  public ComplexPK() {
    this.id = new Integer(0);
    this.brandName = "default";
  }

  public ComplexPK(int id, String brandname) {
    Integer Id = new Integer(id);
    this.id = Id;
    this.brandName = brandname;
  }

  public boolean equals(Object o) {

    if (!(o instanceof ComplexPK))
      return false;
    ComplexPK other = (ComplexPK) o;
    if (id.intValue() == other.id.intValue()
        && brandName.equals(other.brandName))
      return true;
    else
      return false;

  }

  public int hashCode() {

    return id.hashCode() / 2 + brandName.hashCode() / 2;

  }
}
