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
import javax.naming.NamingException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public abstract class CMP20Wrapper implements EntityBean {

  /* Entity instance data */
  public abstract Integer getId();

  public abstract void setId(Integer i);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  public abstract float getPrice();

  public abstract void setPrice(float p);

  protected EntityContext ectx = null;

  protected TSNamingContext nctx = null;

  /*
   * Bean life cycle.
   */

  public Integer ejbCreate(Properties props, int id, String brandName,
      float price) throws CreateException {

    TestUtil.logTrace("[CMP20Wrapper] ejbCreate()");
    Integer pk = new Integer(id);

    try {
      TestUtil.logTrace("[CMP20Wrapper] initialize logging...");
      TestUtil.init(props);
      setId(pk);
      setBrandName(brandName);
      setPrice(price);
    } catch (Exception e) {
      TestUtil.logErr("[CMP20Wrapper] Caught exception: " + e);
      throw new CreateException("Exception occurred: " + e);
    }

    return null;
  }

  public void ejbPostCreate(Properties props, int id, String brandName,
      float price) {

    TestUtil.logTrace("[CMP20Wrapper] ejbPostCreate()");
  }

  public void setEntityContext(EntityContext c) {
    try {
      TestUtil.logTrace("[CMP20Wrapper] setEntityContext()");
      ectx = c;

      TestUtil.logMsg("[CMP20Wrapper] Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[CMP20Wrapper] Naming Exception : " + e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("[CMP20Wrapper] Caught exception: " + e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[CMP20Wrapper] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("[CMP20Wrapper] ejbRemove()");
  }

  public void ejbActivate() {
    TestUtil.logTrace("[CMP20Wrapper] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[CMP20Wrapper] ejbPassivate()");
  }

  public void ejbLoad() {
    TestUtil.logTrace("[CMP20Wrapper] ejbLoad()");
  }

  public void ejbStore() {
    TestUtil.logTrace("[CMP20Wrapper] ejbStore()");
  }

}
