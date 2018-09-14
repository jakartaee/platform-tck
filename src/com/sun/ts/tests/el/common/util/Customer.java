/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.common.util;

import java.util.List;
import java.util.ArrayList;

public class Customer {
  int customerID;

  String name;

  String address;

  String city;

  String country;

  String phone;

  List<Order> orders;

  public Customer(int customerID, String name, String address, String city,
      String country, String phone) {
    this.customerID = customerID;
    this.name = name;
    this.address = address;
    this.city = city;
    this.country = country;
    this.phone = phone;
    this.orders = new ArrayList<Order>();
  }

  public String toString() {
    return "Customer: " + customerID + ", " + name + ", " + city + ", "
        + country;
  }

  public int getCustomerID() {
    return customerID;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getPhone() {
    return phone;
  }

  public List<Order> getOrders() {
    return orders;
  }
}
