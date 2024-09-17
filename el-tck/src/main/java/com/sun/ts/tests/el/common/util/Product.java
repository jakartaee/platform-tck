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

public class Product {

  public int productID;

  public String name;

  public String category;

  public double unitPrice;

  public int unitsInStock;

  Product(int productID, String name, String category, double unitPrice,
      int unitsInStock) {

    this.productID = productID;
    this.name = name;
    this.category = category;
    this.unitPrice = unitPrice;
    this.unitsInStock = unitsInStock;
  }

  public String toString() {
    return "Product: " + productID + ", " + name + ", " + category + ", "
        + unitPrice + ", " + unitsInStock;
  }

  public int getProductID() {
    return productID;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public double getUnitPrice() {
    return unitPrice;
  }

  public int getUnitsInStock() {
    return unitsInStock;
  }

}
