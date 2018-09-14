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
 * @(#)AddressEJB.java	1.9 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;

import java.util.*;
import javax.ejb.*;

// Lightweight Entity Bean

public abstract class AddressEJB implements EntityBean {

  // JNDI Names for Phone Local Home Interface

  private static final String PhoneLocal = "java:comp/env/ejb/PhoneLocal";

  private EntityContext context = null;

  private TSNamingContext nctx = null;

  // ===========================================================
  // getters and setters for the CMP fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract String getStreet();

  public abstract void setStreet(String v);

  public abstract String getCity();

  public abstract void setCity(String v);

  public abstract String getState();

  public abstract void setState(String v);

  public abstract String getZip();

  public abstract void setZip(String v);

  // ===========================================================
  // getters and setters for CMR fields

  // 1xMANY
  public abstract Collection getPhones();

  public abstract void setPhones(Collection v);

  // ===========================================================
  // methods to create instances of lightweight entity beans

  private PhoneLocal createPhoneLocal(String id, String area, String number)
      throws Exception {
    TestUtil.logTrace("createPhoneLocal");
    TSNamingContext nctx = new TSNamingContext();
    PhoneLocalHome phoneLocalHome = (PhoneLocalHome) nctx.lookup(PhoneLocal);
    PhoneLocal phoneLocal = phoneLocalHome.create(id, area, number);
    return phoneLocal;
  }

  private PhoneLocal createPhoneLocal(String id, String area, String number,
      AddressLocal addr) throws Exception {
    TestUtil.logTrace("createPhoneLocal");
    TSNamingContext nctx = new TSNamingContext();
    PhoneLocalHome phoneLocalHome = (PhoneLocalHome) nctx.lookup(PhoneLocal);
    PhoneLocal phoneLocal = phoneLocalHome.create(id, area, number, addr);
    return phoneLocal;
  }

  // ===========================================================

  public String ejbCreate(String id, String street, String city, String state,
      String zip) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setStreet(street);
      setCity(city);
      setState(state);
      setZip(zip);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String street, String city, String state,
      String zip) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public String ejbCreate(String id, String street, String city, String state,
      String zip, Collection phones) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setStreet(street);
      setCity(city);
      setState(state);
      setZip(zip);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String street, String city, String state,
      String zip, Collection phones) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    try {
      Collection pcol = getPhones();
      Iterator iterator = phones.iterator();
      while (iterator.hasNext()) {
        PhoneDVC pDVC = (PhoneDVC) iterator.next();
        PhoneLocal pLEB = createPhoneLocal(pDVC.getId(), pDVC.getArea(),
            pDVC.getNumber());
        pcol.add(pLEB);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred; " + e);
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
