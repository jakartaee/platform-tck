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

public class InfoDVC implements java.io.Serializable {

  // Instance variables
  private String id;

  private String street;

  private String city;

  private String state;

  private String zip;

  private SpouseDVC spouse;

  public InfoDVC(String v1, String v2, String v3, String v4, String v5) {
    id = v1;
    street = v2;
    city = v3;
    state = v4;
    zip = v5;
  }

  public InfoDVC(String v1, String v2, String v3, String v4, String v5,
      SpouseDVC v6) {
    id = v1;
    street = v2;
    city = v3;
    state = v4;
    zip = v5;
    spouse = v6;
  }

  public String getId() {
    return id;
  }

  public void setId(String v) {
    id = v;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String v) {
    street = v;
  }

  public String getState() {
    return state;
  }

  public void setState(String v) {
    state = v;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String v) {
    city = v;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String v) {
    zip = v;
  }

  public SpouseDVC getSpouse() {
    return spouse;
  }

  public void setSpouse(SpouseDVC v) {
    spouse = v;
  }

}
