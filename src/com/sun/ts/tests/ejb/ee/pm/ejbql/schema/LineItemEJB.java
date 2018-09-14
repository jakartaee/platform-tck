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
 * @(#)LineItemEJB.java	1.6 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;
import javax.ejb.*;

// Lightweight Entity Bean

public abstract class LineItemEJB implements EntityBean {

  private EntityContext context = null;

  private static final String OrderLocal = "java:comp/env/ejb/OrderLocal";

  private static final String ProductLocal = "java:comp/env/ejb/ProductLocal";

  // ===========================================================
  // getters and setters for CMP fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract int getQuantity();

  public abstract void setQuantity(int v);

  // ===========================================================
  // getters and setters for CMR fields

  // 1x1
  public abstract OrderLocal getOrder();

  public abstract void setOrder(OrderLocal v);

  // 1x1
  public abstract ProductLocal getProduct();

  public abstract void setProduct(ProductLocal v);

  // ===========================================================
  public String ejbCreate(String id, int quantity, OrderLocal orderLocal,
      ProductLocal productLocal) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setQuantity(quantity);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, int quantity, OrderLocal orderLocal,
      ProductLocal productLocal) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    try {
      setOrder(orderLocal);
      setProduct(productLocal);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    context = c;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

}
