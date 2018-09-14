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

package com.sun.ts.tests.ejb.ee.pm.selfXself;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class DepartmentEJB implements EntityBean {
  private EntityContext ectx = null;

  // ===========================================================
  // getters and setters for CMP fields

  public abstract Integer getId();

  public abstract void setId(Integer v);

  public abstract String getName();

  public abstract void setName(String v);

  // ===========================================================
  // getters and setters for CMR fields

  // ===========================================================
  // select methods
  // ===========================================================

  // ===========================================================
  // life cycle methods
  // ===========================================================

  public Integer ejbCreate(Integer id, String name) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setName(name);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;

  }

  public void ejbPostCreate(Integer id, String name) throws CreateException {
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

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
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
