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

public class FloatBeanEJB implements EntityBean {

  /* CMP fields */
  public Float cmpID;

  public String cmpBrandName;

  public float cmpPrice;

  protected EntityContext ectx = null;

  protected TSNamingContext nctx = null;

  public void ping() {
    TestUtil.logTrace("FloatBean: ping()");
  }

  /*
   * Bean life cycle.
   */

  public Float ejbCreate(Properties props, Float id, String brandName,
      float price) throws CreateException {

    try {
      TestUtil.logTrace("CMP11Wrapper: ejbCreate()");

      TestUtil.init(props);

      this.cmpID = id;
      this.cmpBrandName = brandName;
      this.cmpPrice = price;

    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }

    return this.cmpID;
  }

  public void ejbPostCreate(Properties props, Float id, String brandName,
      float price) {

    TestUtil.logTrace("FloatBean: ejbPostCreate()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("FloatBean: ejbRemove()");
  }

  public void setEntityContext(EntityContext x) throws EJBException {

    TestUtil.logTrace("FloatBean: setEntityContext()");
    ectx = x;
    try {
      TestUtil.logMsg("FloatBean: Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("FloatBean: Naming Exception : " + e, e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("FloatBean: Caught exception: " + e, e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("FloatBean: unsetEntityContext()");
  }

  public void ejbStore() {
    TestUtil.logTrace("FloatBean: ejbStore()");
  }

  public void ejbLoad() {
    TestUtil.logTrace("FloatBean: ejbLoad()");
  }

  public void ejbActivate() {
    TestUtil.logTrace("FloatBean: ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("FloatBean: ejbPassivate()");
  }

}
