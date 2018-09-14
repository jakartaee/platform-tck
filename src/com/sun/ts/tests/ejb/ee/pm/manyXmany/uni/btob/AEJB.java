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
 * @(#)AEJB.java	1.7 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.manyXmany.uni.btob;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class AEJB implements EntityBean {

  private EntityContext context = null;

  // ===========================================================
  // getters and setters for cmp fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract String getName();

  public abstract void setName(String v);

  public abstract int getValue();

  public abstract void setValue(int v);

  // ===========================================================
  // getters and setters for relationship fields

  // manyxmany
  public abstract Collection getB();

  public abstract void setB(Collection v);

  // ===========================================================
  // A interface business methods

  public void init(Properties p) {
    TestUtil.logTrace("init");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean isB() {
    TestUtil.logTrace("isB");
    if (getB().isEmpty() != true)
      TestUtil.logMsg("Relationship set for B ...");
    else
      TestUtil.logMsg("Relationship not set for B ...");
    return getB().isEmpty() != true;
  }

  public boolean setCmrFieldToNull() {
    TestUtil.logTrace("setCmrFieldToNull");
    try {
      setB(null);
      TestUtil.logErr("no exception when setting collection cmr field to null");
      return false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("IllegalArgumentException caught as expected");
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Expected IllegalArgumentException, received " + e);
      return false;
    }
  }

  public Collection getBInfo() {
    TestUtil.logTrace("getBInfo");
    try {
      Vector v = new Vector();
      if (isB()) {
        Collection bcol = getB();
        Iterator iterator = bcol.iterator();
        while (iterator.hasNext()) {
          BLocal b = (BLocal) iterator.next();
          BDVC bDVC = new BDVC(b.getId(), b.getName(), b.getValue());
          v.add(bDVC);
        }
      }
      return v;
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e, e);
      return null;
    }
  }

  // ===========================================================
  // EJB Specification Required Methods

  public String ejbCreate(String id, String name, int value)
      throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setName(name);
      setValue(value);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String name, int value)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
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

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }
}
