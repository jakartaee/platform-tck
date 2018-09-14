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

package com.sun.ts.tests.jpa.common.schema30;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated("EclipseLink JPA 2.0 Canonical Model Generation")
@StaticMetamodel(Order.class)
public class Order_ {

  public static volatile SingularAttribute<Order, String> id;

  public static volatile SingularAttribute<Order, CreditCard> creditCard;

  public static volatile SingularAttribute<Order, LineItem> sampleLineItem;

  public static volatile SingularAttribute<Order, Customer> customer;

  public static volatile CollectionAttribute<Order, LineItem> lineItemsCollection;

  public static volatile SetAttribute<Order, LineItem> lineItemsSet;

  public static volatile ListAttribute<Order, LineItem> lineItemsList;

  public static volatile SingularAttribute<Order, Double> totalPrice;

}
