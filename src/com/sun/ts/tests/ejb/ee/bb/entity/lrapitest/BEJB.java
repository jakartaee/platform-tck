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
 * @(#)BEJB.java	1.6 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.lrapitest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class BEJB implements EntityBean {

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
  // B interface business methods

  public void init(Properties p) {
    TestUtil.logTrace("init");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  // This method is only exposed through the Remote Interface
  public String whoAmIRemote() {
    return "Remote-" + getId() + "-" + getName() + "-" + getValue();
  }

  public boolean test2() {
    TestUtil.logTrace("test2");
    boolean pass = true;
    if (getLocalObjectTest())
      TestUtil.logMsg("getEJBLocalObject ... PASSED");
    else {
      TestUtil.logMsg("getEJBLocalObject ... FAILED");
      pass = false;
    }
    if (getLocalObjectTest())
      TestUtil.logMsg("getEJBLocalObject ... PASSED");
    else {
      TestUtil.logMsg("getEJBLocalObject ... FAILED");
      pass = false;
    }
    return pass;
  }

  // ===========================================================
  // private methods

  private boolean getLocalObjectTest() {
    TestUtil.logTrace("getLocalObjectTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("getEJBLocalObject() - IllegalStateException");
      EJBLocalObject object = context.getEJBLocalObject();
      TestUtil.logErr("getEJBLocalObject() - no IllegalStateException");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logErr("IllegalStateException caught as expected");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean getObjectTest() {
    TestUtil.logTrace("getObjectTest");
    boolean pass = true;
    try {
      EJBObject object = context.getEJBObject();
      if (object != null) {
        TestUtil.logMsg("getEJBObject() returned EJBObject reference");
        if (object instanceof B)
          TestUtil.logMsg("An B object");
        else {
          TestUtil.logErr("Not an B object");
          pass = false;
        }
      } else {
        TestUtil.logErr("getEJBObject() returned null reference");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  // ===========================================================
  // EJB Specification Required Methods

  public String ejbCreateB(String id, String name, int value)
      throws CreateException {
    TestUtil.logTrace("ejbCreateB");
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

  public void ejbPostCreateB(String id, String name, int value)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreateB");
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
