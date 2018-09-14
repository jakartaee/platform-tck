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
 * @(#)TestBeanEJB.java	1.7 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.handletest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;

public abstract class TestBeanEJB implements EntityBean {
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

  public Integer ejbCreate(int id, String brandName, float price)
      throws CreateException {
    TestUtil.logTrace("ejbCreate");
    Integer pk = new Integer(id);
    try {
      setId(pk);
      setBrandName(brandName);
      setPrice(price);

    } catch (Exception e) {
      TestUtil.logErr("Exception ...........");
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(int id, String brandName, float price)
      throws CreateException {
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

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public void ping() {
    TestUtil.logTrace("ping");
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
