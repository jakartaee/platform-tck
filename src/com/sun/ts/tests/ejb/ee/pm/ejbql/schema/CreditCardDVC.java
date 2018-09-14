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

public class CreditCardDVC implements java.io.Serializable {

  // Instance variables
  private String id;

  private String number;

  private String type;

  private String expires;

  private boolean approved;

  private double balance;

  private Order order;

  private Customer customer;

  public CreditCardDVC(String v1, String v2, String v3, String v4, boolean v5,
      double v6, Order v7, Customer v8) {
    id = v1;
    number = v2;
    type = v3;
    expires = v4;
    approved = v5;
    balance = v6;
    order = v7;
    customer = v8;
  }

  public CreditCardDVC(String v1, String v2, String v3, String v4, boolean v5,
      double v6) {
    id = v1;
    number = v2;
    type = v3;
    expires = v4;
    approved = v5;
    balance = v6;
  }

  public String getId() {
    return id;
  }

  public void setId(String v) {
    id = v;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String v) {
    number = v;
  }

  public String getType() {
    return type;
  }

  public void setType(String v) {
    type = v;
  }

  public String getExpires() {
    return expires;
  }

  public void setExpires(String v) {
    expires = v;
  }

  public boolean getApproved() {
    return approved;
  }

  public void setApproved(boolean v) {
    approved = v;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double v) {
    balance = v;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order v) {
    order = v;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer v) {
    customer = v;
  }
}
