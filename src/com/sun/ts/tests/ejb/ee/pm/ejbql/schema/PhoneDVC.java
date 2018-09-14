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

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import java.util.*;

// Dependent Value Class

public class PhoneDVC implements java.io.Serializable {

  // Instance variables
  private String id;

  private String area;

  private String number;

  public PhoneDVC(String v1, String v2, String v3) {
    id = v1;
    area = v2;
    number = v3;
  }

  public String getId() {
    return id;
  }

  public void setId(String v) {
    id = v;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String v) {
    area = v;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String v) {
    number = v;
  }
}
