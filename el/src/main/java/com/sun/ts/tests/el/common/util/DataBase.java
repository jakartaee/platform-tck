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

import java.util.ArrayList;
import java.util.List;

public class DataBase {

  private int curCustomer = 100;

  private int curProduct = 200;

  private int curOrder = 10;

  private boolean inited;

  private List<Customer> customers;

  private List<Product> products;

  private List<Order> orders;

  private List<Integer> ints;

  public List<Integer> getInts() {
    return this.ints;
  }

  public List<Customer> getCustomers() {
    return this.customers;
  }

  public List<Product> getProducts() {
    return this.products;
  }

  public List<Order> getOrders() {
    return this.orders;
  }

  public void init() {
    if (inited) {
      return;
    }

    inited = true;
    customers = new ArrayList<Customer>();
    orders = new ArrayList<Order>();
    products = new ArrayList<Product>();
    ints = new ArrayList<Integer>();

    initCustomer();
    initProduct();
    initOrder();
    initNums();
  }

  void initNums() {
    for (int i = 0; i < 10; i++) {
      ints.add(Integer.valueOf(i));
    }
  }

  void initCustomer() {
    c("John Doe", "123 Willow Road", "Menlo Park", "USA", "650-734-2187");
    c("Mary Lane", "75 State Street", "Atlanta", "USA", "302-145-8765");
    c("Charlie Yeh", "5 Nathan Road", "Kowlon", "Hong Kong", "11-7565-2323");
  }

  void initProduct() {
    p("Eagle", "book", 12.50, 100); // id: 200
    p("Coming Home", "dvd", 8.00, 50); // id: 201
    p("Greatest Hits", "cd", 6.5, 200); // id: 202
    p("History of Golf", "book", 11.0, 30); // id: 203
    p("Toy Story", "dvd", 10.00, 1000); // id: 204
    p("iSee", "book", 12.50, 150); // 205
  }

  void initOrder() {
    o(100, new Date(2010, 2, 18), 20.80);
    o(100, new Date(2011, 5, 3), 34.50);
    o(100, new Date(2011, 8, 2), 210.75);
    o(101, new Date(2011, 1, 15), 50.23);
    o(101, new Date(2012, 1, 3), 126.77);
    o(102, new Date(2011, 4, 15), 101.20);
  }

  void c(String name, String address, String city, String country,
      String phone) {
    customers
        .add(new Customer(curCustomer++, name, address, city, country, phone));
  }

  void o(int customerID, Date orderDate, double total) {
    Order order = new Order(curOrder++, customerID, orderDate, total);
    this.orders.add(order);
    findCustomer(customerID).getOrders().add(order);
  }

  void p(String name, String category, double unitPrice, int unitsInStock) {
    products.add(
        new Product(curProduct++, name, category, unitPrice, unitsInStock));
  }

  private Customer findCustomer(int id) {
    for (Customer customer : customers) {
      if (customer.customerID == id) {
        return customer;
      }
    }
    return null;
  }
}
