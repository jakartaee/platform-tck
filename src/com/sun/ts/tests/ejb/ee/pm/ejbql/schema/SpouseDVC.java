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

public class SpouseDVC implements java.io.Serializable {

  // Instance variables
  private String id;

  private String first;

  private String maiden;

  private String last;

  private String sNumber;

  private InfoDVC info;

  public SpouseDVC(String v1, String v2, String v3, String v4, String v5,
      InfoDVC v6) {
    id = v1;
    first = v2;
    maiden = v3;
    last = v4;
    sNumber = v5;
    info = v6;
  }

  public String getId() {
    return id;
  }

  public void setId(String v) {
    id = v;
  }

  public String getFirstName() {
    return first;
  }

  public void setFirstName(String v) {
    first = v;
  }

  public String getMaidenName() {
    return maiden;
  }

  public void setMaidenName(String v) {
    maiden = v;
  }

  public String getLastName() {
    return last;
  }

  public void setLastName(String v) {
    last = v;
  }

  public String getSocialSecurityNumber() {
    return sNumber;
  }

  public void setSocialSecurityNumber(String v) {
    sNumber = v;
  }

  public InfoDVC getInfo() {
    return info;
  }

  public void setInfo(InfoDVC v) {
    info = v;
  }
}
