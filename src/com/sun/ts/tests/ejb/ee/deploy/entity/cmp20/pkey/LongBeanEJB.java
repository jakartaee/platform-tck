/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)LongBeanEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp20.pkey;

import java.util.Properties;

import javax.naming.NamingException;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.EntityBean;
import jakarta.ejb.EntityContext;
import jakarta.ejb.RemoveException;

public abstract class LongBeanEJB implements EntityBean {

  /* Entity instance data */
  public abstract Long getId();

  public abstract void setId(Long id);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  public abstract float getPrice();

  public abstract void setPrice(float p);

  protected EntityContext ectx = null;

  protected TSNamingContext nctx = null;

  public void ping() {
    TestUtil.logTrace("[LongBean] ping()");
  }

  /*
   * Bean life cycle.
   */

  public Long ejbCreate(Properties props, Long id, String brandName,
      float price) throws CreateException {

    TestUtil.logTrace("[LongBean] ejbCreate()");

    try {
      TestUtil.logTrace("[LongBean] initialize logging...");
      TestUtil.init(props);

      setId(id);
      setBrandName(brandName);
      setPrice(price);
    } catch (Exception e) {
      TestUtil.logErr("[LongBean] Caught exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }

    return null;
  }

  public void ejbPostCreate(Properties props, Long id, String brandName,
      float price) {

    TestUtil.logTrace("[LongBean] ejbPostCreate()");
  }

  public void setEntityContext(EntityContext c) {
    try {
      TestUtil.logTrace("[LongBean] setEntityContext()");
      ectx = c;

      TestUtil.logMsg("[LongBean] Getting Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[LongBean] Naming Exception : " + e);
      throw new EJBException("Cannot obtain Naming Context", e);
    } catch (Exception e) {
      TestUtil.logErr("[LongBean] Caught exception: " + e);
      throw new EJBException("Caught exception: ", e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[LongBean] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("[LongBean] ejbRemove()");
  }

  public void ejbActivate() {
    TestUtil.logTrace("[LongBean] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[LongBean] ejbPassivate()");
  }

  public void ejbLoad() {
    TestUtil.logTrace("[LongBean] ejbLoad()");
  }

  public void ejbStore() {
    TestUtil.logTrace("[LongBean] ejbStore()");
  }

}
