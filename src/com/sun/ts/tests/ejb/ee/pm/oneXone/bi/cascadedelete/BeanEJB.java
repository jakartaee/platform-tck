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
 * @(#)BeanEJB.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.oneXone.bi.cascadedelete;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class BeanEJB implements EntityBean {

  private static final int NO_RELATION_SET = 0;

  private static final int NULL_RELATION_SET = 1;

  private static final int RELATION_SET = 2;

  // JNDI Names for A and B Local Home Interface
  private static final String ALocal = "java:comp/env/ejb/ALocal";

  private static final String BLocal = "java:comp/env/ejb/BLocal";

  private EntityContext context = null;

  private TSNamingContext nctx = null;

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

  // 1x1
  public abstract ALocal getA1();

  public abstract void setA1(ALocal v);

  // 1x1
  public abstract ALocal getA2();

  public abstract void setA2(ALocal v);

  // 1x1
  public abstract BLocal getB1();

  public abstract void setB1(BLocal v);

  // 1x1
  public abstract BLocal getB2();

  public abstract void setB2(BLocal v);

  // ===========================================================
  // private methods

  private ALocal createALocal(String id, String name, int value)
      throws Exception {
    TestUtil.logTrace("createALocal");
    TestUtil.logMsg("Obtain naming context");
    nctx = new TSNamingContext();
    ALocalHome aLocalHome = (ALocalHome) nctx.lookup(ALocal);
    ALocal aLocal = aLocalHome.create(id, name, value);
    return aLocal;
  }

  private BLocal createBLocal(String id, String name, int value)
      throws Exception {
    TestUtil.logTrace("createBLocal");
    TestUtil.logMsg("Obtain naming context");
    nctx = new TSNamingContext();
    BLocalHome bLocalHome = (BLocalHome) nctx.lookup(BLocal);
    BLocal bLocal = bLocalHome.create(id, name, value);
    return bLocal;
  }

  // ===========================================================
  // Bean interface business methods

  public void init(Properties p) {
    TestUtil.logTrace("init");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean test1() {
    ALocal aOne = null;
    BLocal bOne = null;
    BLocal b1 = null;
    ALocal a1 = null;

    try {
      aOne = getA1();
      bOne = getB1();
      b1 = aOne.getB();
      a1 = bOne.getA();
      TestUtil.logMsg("aOne = " + aOne);
      TestUtil.logMsg("bOne = " + bOne);
      TestUtil.logMsg("Cascade delete entitybean object B1");
      bOne.remove();
      TestUtil.logMsg("Attempt to call a method on B1");
      bOne.getId();
      TestUtil.logErr(
          "EntityBean object B1 was not deleted, " + "expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("EntityBean object B1 was deleted");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
    TestUtil.logMsg("Check that entitybean object A1 was also deleted");
    try {
      TestUtil.logMsg("Attempt to call a method on A1");
      aOne.getId();
      TestUtil.logErr(
          "EntityBean object A1 was not deleted, " + "expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("EntityBean object A1 was also deleted");
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
  }

  public boolean test2() {
    ALocal aOne = null;
    BLocal bOne = null;
    BLocal b1 = null;
    ALocal a1 = null;

    try {
      aOne = getA1();
      bOne = getB1();
      b1 = aOne.getB();
      a1 = bOne.getA();
      TestUtil.logMsg("aOne = " + aOne);
      TestUtil.logMsg("bOne = " + bOne);
      TestUtil.logMsg("Cascade delete entitybean object B1");
      bOne.remove();
      TestUtil.logMsg("Check relationship accessor methods of BeanEJB");
      TestUtil.logMsg("getA1() = " + getA1());
      TestUtil.logMsg("getB1() = " + getB1());
      if (getA1() == null && getB1() == null) {
        TestUtil
            .logMsg("Relationship accessor methods of BeanEJB returns null");
        return true;
      } else {
        TestUtil.logErr(
            "Relationship accessor methods of BeanEJB returns not null");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean test3() {
    ALocal aOne = null;
    BLocal bOne = null;
    BLocal b1 = null;
    ALocal a1 = null;

    try {
      aOne = getA1();
      bOne = getB1();
      b1 = aOne.getB();
      a1 = bOne.getA();
      TestUtil.logMsg("aOne = " + aOne);
      TestUtil.logMsg("bOne = " + bOne);
      TestUtil.logMsg("Cascade delete entitybean object B1");
      bOne.remove();
      TestUtil.logMsg("Accessor method of B1 must throw EJBException");
      TestUtil.logMsg("Calling bOne.getA(), expect EJBException");
      bOne.getA();
      TestUtil.logErr("Did not get expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.logMsg("Caught expected EJBException");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
    try {
      TestUtil.logMsg("Accessor method of A1 must also throw EJBException");
      TestUtil.logMsg("Calling aOne.getB(), expect EJBException");
      aOne.getB();
      TestUtil.logErr("Did not get expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.logMsg("Caught expected EJBException");
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
  }

  public boolean test4() {
    ALocal aOne = null;
    BLocal bOne = null;
    BLocal b1 = null;
    ALocal a1 = null;

    try {
      aOne = getA1();
      bOne = getB1();
      b1 = aOne.getB();
      a1 = bOne.getA();
      TestUtil.logMsg("aOne = " + aOne);
      TestUtil.logMsg("bOne = " + bOne);
      TestUtil.logMsg("Cascade delete entitybean object B1");
      bOne.remove();
      TestUtil.logMsg(
          "Assigning B1 to BeanEJB must throw IllegalArgumentException");
      TestUtil.logMsg("Calling setB1(bOne), expect IllegalArgumentException");
      setB1(bOne);
      TestUtil.logErr("Did not get expected IllegalArgumentException");
      return false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
    try {
      TestUtil.logMsg(
          "Assigning A1 to BeanEJB must throw IllegalArgumentException");
      TestUtil.logMsg("Calling setA1(aOne), expect IllegalArgumentException");
      setA1(aOne);
      TestUtil.logErr("Did not get expected IllegalArgumentException");
      return false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught expected IllegalArgumentException");
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
  }

  // ===========================================================
  // EJB Specification Required Methods

  public String ejbCreate(String id, String name, int value, ADVC a, BDVC b,
      int flag) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setName(name);
      setValue(value);
      setId(id);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String name, int value, ADVC a, BDVC b,
      int flag) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    try {
      ALocal a1 = null;
      BLocal b1 = null;
      switch (flag) {
      case NO_RELATION_SET:
        a1 = createALocal(a.getId(), a.getName(), a.getValue());
        b1 = createBLocal(b.getId(), b.getName(), b.getValue());
        break;
      case NULL_RELATION_SET:
        a1 = createALocal(a.getId(), a.getName(), a.getValue());
        b1 = createBLocal(b.getId(), b.getName(), b.getValue());
        break;
      case RELATION_SET:
        a1 = createALocal(a.getId(), a.getName(), a.getValue());
        b1 = createBLocal(b.getId(), b.getName(), b.getValue());
        a1.setB(b1);
        b1.setA(a1);
        break;
      }
      setA1(a1);
      setB1(b1);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
  }

  public String ejbCreate(String id, String name, int value, ADVC aOne,
      BDVC bOne, ADVC aTwo, BDVC bTwo) throws CreateException {
    TestUtil.logTrace("ejbCreateWith");
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

  public void ejbPostCreate(String id, String name, int value, ADVC aOne,
      BDVC bOne, ADVC aTwo, BDVC bTwo) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    ALocal a1, a2;
    BLocal b1, b2;

    try {
      b1 = createBLocal(bOne.getId(), bOne.getName(), bOne.getValue());
      a1 = createALocal(aOne.getId(), aOne.getName(), aOne.getValue());
      b2 = createBLocal(bTwo.getId(), bTwo.getName(), bTwo.getValue());
      a2 = createALocal(aTwo.getId(), aTwo.getName(), aTwo.getValue());
      setA1(a1);
      setA2(a2);
      setB1(b1);
      setB2(b2);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    context = c;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("NamingException ... " + e, e);
      throw new EJBException("unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.logErr("Exception ... " + e, e);
      throw new EJBException("Exception occurred: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
    TestUtil.logMsg("getA1() = " + getA1());
    TestUtil.logMsg("getA2() = " + getA2());
    TestUtil.logMsg("getB1() = " + getB1());
    TestUtil.logMsg("getB2() = " + getB2());
    if (getA1() != null) {
      TestUtil.logMsg("deleting entitybean object A1 ...");
      getA1().remove();
    }
    if (getA2() != null) {
      TestUtil.logMsg("deleting entitybean object A2 ...");
      getA2().remove();
    }
    if (getB1() != null) {
      TestUtil.logMsg("deleting entitybean object B1 ...");
      getB1().remove();
    }
    if (getB2() != null) {
      TestUtil.logMsg("deleting entitybean object B2 ...");
      getB2().remove();
    }
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
