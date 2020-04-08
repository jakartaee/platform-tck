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

package com.sun.ts.tests.jpa.core.callback.listener;

import com.sun.ts.tests.jpa.core.callback.common.CallbackStatusIF;
import com.sun.ts.tests.jpa.core.callback.common.CallbackStatusImpl;
import com.sun.ts.tests.jpa.core.callback.common.ListenerB;
import com.sun.ts.tests.jpa.core.callback.common.ListenerC;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "ORDER_TABLE")
@EntityListeners({ ListenerB.class, ListenerC.class })
public class Order extends CallbackStatusImpl
    implements java.io.Serializable, CallbackStatusIF {
  private String id;

  private double totalPrice;

  private LineItem sampleLineItem;

  private Collection<LineItem> lineItemsCollection = new java.util.ArrayList<LineItem>();

  public Order() {
  }

  public Order(String id, double totalPrice) {
    this.id = id;
    this.totalPrice = totalPrice;
  }

  @Id
  @Column(name = "ID")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(name = "TOTALPRICE")
  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double price) {
    this.totalPrice = price;
  }

  // 1x1
  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "FK0_FOR_LINEITEM_TABLE")
  public LineItem getSampleLineItem() {
    return sampleLineItem;
  }

  public void setSampleLineItem(LineItem l) {
    this.sampleLineItem = l;
  }

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  public Collection<LineItem> getLineItemsCollection() {
    return lineItemsCollection;
  }

  public void setLineItemsCollection(Collection<LineItem> c) {
    this.lineItemsCollection = c;
  }

  public void addLineItem(LineItem p) {
    getLineItemsCollection().add(p);
  }
}
