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
 * $Id$
 */

package com.sun.ts.tests.common.ejb.wrappers;

import java.util.Properties;
import java.io.Serializable;
import java.io.IOException;
import javax.naming.NamingException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.RemoteLoggingInitException;

/**
 * CMP 1.1 wrapper that provide the bean life cycle methods for a CMP 1.1 bean.
 * This class is intended to be subclassed by the final entity bean class that
 * will provide the test logic (business methods).
 */
public class CMP11Wrapper implements EntityBean {

  /* CMP fields */
  public String BRAND_NAME;

  public Integer KEY_ID;

  public float PRICE;

  protected EntityContext ectx = null;

  protected TSNamingContext nctx = null;

  public Integer ejbCreate(Properties props, int KEY_ID, String BRAND_NAME,
      float PRICE) throws CreateException {

    try {
      TestUtil.logTrace("[CMP11Wrapper] ejbCreate()");

      TestUtil.init(props);

      this.KEY_ID = new Integer(KEY_ID);
      this.BRAND_NAME = BRAND_NAME;
      this.PRICE = PRICE;

    } catch (RemoteLoggingInitException e) {
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      throw new CreateException("Exception occurred: " + e);
    }

    return this.KEY_ID;
  }

  public void ejbPostCreate(Properties props, int KEY_ID, String BRAND_NAME,
      float PRICE) {

    TestUtil.logTrace("[CMP11Wrapper] ejbPostCreate()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("[CMP11Wrapper] ejbRemove()");
  }

  public void setEntityContext(EntityContext x) throws EJBException {

    TestUtil.logTrace("[CMP11Wrapper] setEntityContext()");
    ectx = x;
    try {
      TestUtil.logMsg("[CMP11Wrapper] Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[CMP11Wrapper] Naming Exception : " + e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("[CMP11Wrapper] Caught exception: " + e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[CMP11Wrapper] unsetEntityContext()");
  }

  public void ejbStore() {
    TestUtil.logTrace("[CMP11Wrapper] ejbStore()");
  }

  public void ejbLoad() {
    TestUtil.logTrace("[CMP11Wrapper] ejbLoad()");
  }

  public void ejbActivate() {
    TestUtil.logTrace("[CMP11Wrapper] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[CMP11Wrapper] ejbPassivate()");
  }

}
