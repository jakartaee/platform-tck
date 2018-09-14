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

package com.sun.ts.tests.ejb.ee.bb.entity.cmp.complexpktest;

public class ComplexPK implements java.io.Serializable {
  public Integer ID;

  public String BRAND_NAME;

  public ComplexPK() {
    this.ID = new Integer(0);
    this.BRAND_NAME = "default";
  }

  public ComplexPK(int id, String brandname) {
    Integer Id = new Integer(id);
    this.ID = Id;
    this.BRAND_NAME = brandname;
  }

  public boolean equals(Object o) {

    if (!(o instanceof ComplexPK))
      return false;
    ComplexPK other = (ComplexPK) o;
    if (ID.intValue() == other.ID.intValue()
        && BRAND_NAME.equals(other.BRAND_NAME))
      return true;
    else
      return false;

  }

  public int hashCode() {

    return ID.hashCode() / 2 + BRAND_NAME.hashCode() / 2;

  }
}
