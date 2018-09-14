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
 * @(#)ProductEJB.java	1.13 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class ProductEJB implements EntityBean {
  private EntityContext ectx = null;

  // ===========================================================
  // getters and setters for CMP fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract String getName();

  public abstract void setName(String v);

  public abstract double getPrice();

  public abstract void setPrice(double v);

  public abstract int getQuantity();

  public abstract void setQuantity(int v);

  public abstract long getPartNumber();

  public abstract void setPartNumber(long v);

  // ===========================================================
  // select methods

  public abstract Collection ejbSelectAllProducts() throws FinderException;

  public abstract Product ejbSelectProductByName(String name)
      throws FinderException;

  public abstract Product ejbSelectProductByType() throws FinderException;

  public abstract Collection ejbSelectProductsByPartNumber()
      throws FinderException;

  public abstract long ejbSelectCountSingle() throws FinderException;

  public abstract double ejbSelectSumSingle() throws FinderException;

  // ===========================================================

  public String ejbCreate(String id, String name, double price, int quantity,
      long partNumber) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setName(name);
      setPrice(price);
      setQuantity(quantity);
      setPartNumber(partNumber);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;

  }

  public void ejbPostCreate(String id, String name, double price, int quantity,
      long partNumber) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
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

  public Collection ejbHomeSelectAllProducts() throws FinderException {
    TestUtil.logTrace("ejbHomeSelectAllProducts");
    try {
      Collection ccol = ejbSelectAllProducts();
      return ccol;
    } catch (FinderException e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("ejbHomeSelectAllProducts: " + e);
    }
  }

  public Product ejbHomeSelectProductByName(String name)
      throws FinderException {
    TestUtil.logTrace("ejbHomeSelectProductByName");
    Product product = ejbSelectProductByName(name);
    return product;
  }

  public Product ejbHomeSelectProductByType() throws FinderException {
    TestUtil.logTrace("ejbHomeSelectProductByType");
    Product product = ejbSelectProductByType();
    return product;
  }

  public Collection ejbHomeSelectProductsByPartNumber() throws FinderException {
    TestUtil.logTrace("ejbHomeSelectProductsByPartNumber");
    Collection pNum = ejbSelectProductsByPartNumber();
    return pNum;
  }

  public long ejbHomeSelectCountSingle() {
    TestUtil.logTrace("ejbHomeSelectCountSingle");
    try {
      long l = ejbSelectCountSingle();
      return l;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectCountSingle: " + e);
    }
  }

  public double ejbHomeSelectSumSingle() {
    TestUtil.logTrace("ejbHomeSelectSumSingle");
    try {
      double d = ejbSelectSumSingle();
      return d;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectSumSingle: " + e);
    }
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }
}
