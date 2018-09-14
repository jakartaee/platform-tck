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
 * @(#)StringBeanEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp20.pkey;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;

public abstract class StringBeanEJB implements EntityBean {

  /* Entity instance data */
  public abstract String getId();

  public abstract void setId(String i);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  public abstract float getPrice();

  public abstract void setPrice(float p);

  protected EntityContext ectx = null;

  protected TSNamingContext nctx = null;

  public void ping() {
    TestUtil.logTrace("[StringBean] ping()");
  }

  /*
   * Bean life cycle.
   */

  public String ejbCreate(Properties props, String id, String brandName,
      float price) throws CreateException {

    TestUtil.logTrace("[StringBean] ejbCreate()");

    try {
      TestUtil.logTrace("[StringBean] initialize logging...");
      TestUtil.init(props);

      setId(id);
      setBrandName(brandName);
      setPrice(price);
    } catch (Exception e) {
      TestUtil.logErr("[StringBean] Caught exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }

    return null;
  }

  public void ejbPostCreate(Properties props, String id, String brandName,
      float price) {

    TestUtil.logTrace("[StringBean] ejbPostCreate()");
  }

  public void setEntityContext(EntityContext c) {
    try {
      TestUtil.logTrace("[StringBean] setEntityContext()");
      ectx = c;

      TestUtil.logMsg("[StringBean] Getting Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[StringBean] Naming Exception : " + e);
      throw new EJBException("Cannot obtain Naming Context", e);
    } catch (Exception e) {
      TestUtil.logErr("[StringBean] Caught exception: " + e);
      throw new EJBException("Caught exception: ", e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[StringBean] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("[StringBean] ejbRemove()");
  }

  public void ejbActivate() {
    TestUtil.logTrace("[StringBean] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[StringBean] ejbPassivate()");
  }

  public void ejbLoad() {
    TestUtil.logTrace("[StringBean] ejbLoad()");
  }

  public void ejbStore() {
    TestUtil.logTrace("[StringBean] ejbStore()");
  }

}
