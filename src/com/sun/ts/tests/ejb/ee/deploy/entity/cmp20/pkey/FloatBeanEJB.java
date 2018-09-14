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
 * @(#)FloatBeanEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp20.pkey;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;

public abstract class FloatBeanEJB implements EntityBean {

  /* Entity instance data */
  public abstract Float getId();

  public abstract void setId(Float id);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  public abstract float getPrice();

  public abstract void setPrice(float p);

  protected EntityContext ectx = null;

  protected TSNamingContext nctx = null;

  public void ping() {
    TestUtil.logTrace("[FloatBean] ping()");
  }

  /*
   * Bean life cycle.
   */

  public Float ejbCreate(Properties props, Float id, String brandName,
      float price) throws CreateException {

    TestUtil.logTrace("[FloatBean] ejbCreate()");

    try {
      TestUtil.logTrace("[FloatBean] initialize logging...");
      TestUtil.init(props);

      setId(id);
      setBrandName(brandName);
      setPrice(price);
    } catch (Exception e) {
      TestUtil.logErr("[FloatBean] Caught exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }

    return null;
  }

  public void ejbPostCreate(Properties props, Float id, String brandName,
      float price) {

    TestUtil.logTrace("[FloatBean] ejbPostCreate()");
  }

  public void setEntityContext(EntityContext c) {
    try {
      TestUtil.logTrace("[FloatBean] setEntityContext()");
      ectx = c;

      TestUtil.logMsg("[FloatBean] Getting Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[FloatBean] Naming Exception : " + e);
      throw new EJBException("Cannot obtain Naming Context", e);
    } catch (Exception e) {
      TestUtil.logErr("[FloatBean] Caught exception: " + e);
      throw new EJBException("Caught exception: ", e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[FloatBean] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("[FloatBean] ejbRemove()");
  }

  public void ejbActivate() {
    TestUtil.logTrace("[FloatBean] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[FloatBean] ejbPassivate()");
  }

  public void ejbLoad() {
    TestUtil.logTrace("[FloatBean] ejbLoad()");
  }

  public void ejbStore() {
    TestUtil.logTrace("[FloatBean] ejbStore()");
  }

}
