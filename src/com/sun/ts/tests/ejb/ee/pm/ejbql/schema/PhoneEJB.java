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
 * @(#)PhoneEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;

import javax.ejb.*;

// Lightweight Entity Bean

public abstract class PhoneEJB implements EntityBean {

  // JNDI Names for Address Local Home Interface

  private static final String AddressLocal = "java:comp/env/ejb/AddressLocal";

  private EntityContext context = null;

  private TSNamingContext nctx = null;

  // ===========================================================
  // getters and setters for the CMP fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract String getArea();

  public abstract void setArea(String v);

  public abstract String getNumber();

  public abstract void setNumber(String v);

  // ===========================================================
  // getters and setters for CMR fields

  // 1x1
  public abstract AddressLocal getAddress();

  public abstract void setAddress(AddressLocal v);

  // ===========================================================
  // methods to create instances of lightweight entity beans

  private AddressLocal createAddressLocal(String id, String street, String city,
      String state, String zip) throws Exception {
    TestUtil.logTrace("createAddressLocal");
    TSNamingContext nctx = new TSNamingContext();
    AddressLocalHome addressLocalHome = (AddressLocalHome) nctx
        .lookup(AddressLocal);
    AddressLocal addressLocal = addressLocalHome.create(id, street, city, state,
        zip);
    return addressLocal;
  }

  // ===========================================================

  public String ejbCreate(String id, String area, String number)
      throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setArea(area);
      setNumber(number);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String area, String number)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreatePhone");
  }

  public String ejbCreate(String id, String area, String number, AddressLocal a)
      throws CreateException {
    TestUtil.logTrace("ejbCreatePhone");
    try {
      setId(id);
      setArea(area);
      setNumber(number);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String area, String number,
      AddressLocal a) throws CreateException {
    TestUtil.logTrace("ejbPostCreatePhone");
    try {
      setAddress(a);
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
