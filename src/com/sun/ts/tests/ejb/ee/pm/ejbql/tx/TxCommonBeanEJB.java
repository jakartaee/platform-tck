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
 * @(#)TxCommonBeanEJB.java	1.10 03/05/16
 */
package com.sun.ts.tests.ejb.ee.pm.ejbql.tx;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.*;
import javax.ejb.*;

public abstract class TxCommonBeanEJB implements EntityBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  private EntityContext ectx = null;

  // ===========================================================
  // getters and setters for cmp fields

  public abstract Integer getId();

  public abstract void setId(Integer i);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  public abstract float getPrice();

  public abstract void setPrice(float p);

  // ===========================================================
  // Required Entity EJB methods
  // ===========================================================
  // public constructor which takes no arguments

  public TxCommonBeanEJB() {
    TestUtil.logTrace("TxCommonBeanEJB no arg constructor");
  }

  public Integer ejbCreate(Integer id, String brandName, float price,
      Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      initLogging(p);
      setId(id);
      setBrandName(brandName);
      setPrice(price);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught .....", e);
      throw new CreateException(e.getMessage());
    }
    return null;
  }

  public void ejbPostCreate(Integer id, String brandName, float price,
      Properties p) throws CreateException {
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

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // The TxCommonBean business methods

  public void updateBrandName(String newBrandName) {
    TestUtil.logMsg("updateBrandName");
    setBrandName(newBrandName);
  }

  public void updatePrice(float newPriceName) {
    TestUtil.logTrace("updatePrice");
    setPrice(newPriceName);
  }

  // ===========================================================

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }
}
