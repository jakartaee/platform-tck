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
 * @(#)CompoundBeanEJB.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp11.pkey;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.io.Serializable;
import java.io.IOException;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.sql.*;
import com.sun.ts.tests.common.dao.coffee.variants.CompoundPK;

public class CompoundBeanEJB implements EntityBean {

  /*
   * CMP fields
   */
  public Integer pmIDInteger; /* 1st field of compound PK */

  public String pmIDString; /* 2nd field of compound PK */

  public Float pmIDFloat; /* 3rd field of compound PK */

  public String cmpBrandName;

  public float cmpPrice;

  protected EntityContext ectx = null;

  protected TSNamingContext nctx = null;

  public void ping() {
    TestUtil.logTrace("CompoundBean: ping()");
  }

  /*
   * Bean life cycle.
   */

  public CompoundPK ejbCreate(Properties props, CompoundPK id, String brandName,
      float price) throws CreateException {

    try {
      TestUtil.logTrace("CMP11Wrapper: ejbCreate()");

      TestUtil.init(props);

      this.pmIDInteger = id.pmIDInteger;
      this.pmIDString = id.pmIDString;
      this.pmIDFloat = id.pmIDFloat;
      this.cmpBrandName = brandName;
      this.cmpPrice = price;

    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }

    return null;
  }

  public void ejbPostCreate(Properties props, CompoundPK id, String brandName,
      float price) {

    TestUtil.logTrace("CompoundBean: ejbPostCreate()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("CompoundBean: ejbRemove()");
  }

  public void setEntityContext(EntityContext x) throws EJBException {

    TestUtil.logTrace("CompoundBean: setEntityContext()");
    ectx = x;
    try {
      TestUtil.logMsg("CompoundBean: Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("CompoundBean: Naming Exception : " + e, e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("CompoundBean: Caught exception: " + e, e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("CompoundBean: unsetEntityContext()");
  }

  public void ejbStore() {
    TestUtil.logTrace("CompoundBean: ejbStore()");
  }

  public void ejbLoad() {
    TestUtil.logTrace("CompoundBean: ejbLoad()");
  }

  public void ejbActivate() {
    TestUtil.logTrace("CompoundBean: ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("CompoundBean: ejbPassivate()");
  }

}
