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

public class LineItemDVC implements java.io.Serializable {

  // Instance variables
  private String id;

  private int quantity;

  private Order order;

  private Product product;

  public LineItemDVC(String v1, int v2, Order v3, Product v4) {
    id = v1;
    quantity = v2;
    order = v3;
    product = v4;
  }

  public LineItemDVC(String v1, int v2) {
    id = v1;
    quantity = v2;
  }

  public String getId() {
    return id;
  }

  public void setId(String v) {
    id = v;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setgetQuantity(int v) {
    quantity = v;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order v) {
    order = v;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product v) {
    product = v;
  }
}
