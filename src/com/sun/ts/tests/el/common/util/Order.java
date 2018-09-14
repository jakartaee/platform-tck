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

package com.sun.ts.tests.el.common.util;

public class Order {

  int orderID;

  int customerID;

  Date orderDate;

  double total;

  public Order(int orderID, int customerID, Date orderDate, double total) {
    this.orderID = orderID;
    this.customerID = customerID;
    this.orderDate = orderDate;
    this.total = total;
  }

  public String toString() {
    return "Order: " + orderID + ", " + customerID + ", " + orderDate.toString()
        + ", " + total;
  }

  public int getOrderID() {
    return orderID;
  }

  public int getCustomerID() {
    return customerID;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public double getTotal() {
    return total;
  }
}
