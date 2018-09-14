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
 * @(#)CreditCardEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;
import javax.ejb.*;

public abstract class CreditCardEJB implements EntityBean {

  private static final String OrderLocal = "java:comp/env/ejb/OrderLocal";

  private static final String CustomerLocal = "java:comp/env/ejb/CustomerLocal";

  private EntityContext context = null;

  // ===========================================================
  // getters and setters for CMP fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract String getNumber();

  public abstract void setNumber(String v);

  public abstract String getType();

  public abstract void setType(String v);

  public abstract String getExpires();

  public abstract void setExpires(String v);

  public abstract boolean getApproved();

  public abstract void setApproved(boolean v);

  public abstract double getBalance();

  public abstract void setBalance(double v);

  // ===========================================================
  // getters and setters for CMR fields

  // 1x1
  public abstract OrderLocal getOrder();

  public abstract void setOrder(OrderLocal v);

  // 1x1
  public abstract CustomerLocal getCustomer();

  public abstract void setCustomer(CustomerLocal v);

  // ===========================================================

  public String ejbCreate(String id, String number, String type, String expires,
      boolean approved, double balance, OrderLocal orderLocal,
      CustomerLocal customerLocal) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setNumber(number);
      setType(type);
      setExpires(expires);
      setApproved(approved);
      setBalance(balance);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String number, String type,
      String expires, boolean approved, double balance, OrderLocal orderLocal,
      CustomerLocal customerLocal) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    try {
      setOrder(orderLocal);
      setCustomer(customerLocal);

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
