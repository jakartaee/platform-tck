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
 * @(#)SpouseEJB.java	1.4 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;
import javax.ejb.*;
import java.util.*;

public abstract class SpouseEJB implements EntityBean {

  private static final String CustomerLocal = "java:comp/env/ejb/CustomerLocal";

  private static final String InfoLocal = "java:comp/env/ejb/InfoLocal";

  private EntityContext context = null;

  // ===========================================================
  // getters and setters for CMP fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract String getFirstName();

  public abstract void setFirstName(String v);

  public abstract String getMaidenName();

  public abstract void setMaidenName(String v);

  public abstract String getLastName();

  public abstract void setLastName(String v);

  public abstract String getSocialSecurityNumber();

  public abstract void setSocialSecurityNumber(String v);

  // ===========================================================
  // getters and setters for CMR fields

  // 1x1
  public abstract InfoLocal getInfo();

  public abstract void setInfo(InfoLocal v);

  // 1x1
  public abstract CustomerLocal getCustomer();

  public abstract void setCustomer(CustomerLocal v);

  // ===========================================================
  // select methods
  public abstract java.lang.String ejbSelectSpouseInfo() throws FinderException;

  // =============================================================

  public String ejbCreate(String id, String firstName, String maidenName,
      String lastName, String sNumber, InfoLocal infoLocal)
      throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setFirstName(firstName);
      setMaidenName(maidenName);
      setLastName(lastName);
      setSocialSecurityNumber(sNumber);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String firstName, String maidenName,
      String lastName, String sNumber, InfoLocal infoLocal)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    try {
      setInfo(infoLocal);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    context = c;
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

  public java.lang.String ejbHomeSelectSpouseInfo() throws SpouseException {
    TestUtil.logTrace("ejbHomeSelectSpouseInfo");
    try {
      String s = ejbSelectSpouseInfo();
      return s;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new SpouseException("ejbHomeSelectSpouseInfo: " + e);
    }
  }
}
