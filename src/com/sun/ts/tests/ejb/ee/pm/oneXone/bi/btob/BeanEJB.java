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
 * @(#)BeanEJB.java	1.9 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.oneXone.bi.btob;

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
  private static final String ALocal = "java:comp/env/ejb/AEJBLocal";

  private static final String BLocal = "java:comp/env/ejb/BEJBLocal";

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

  private boolean nullTest() {
    TestUtil.logTrace("nullTest");
    ALocal aOne = getA1();
    BLocal bOne = getB1();

    BLocal b1 = aOne.getB();
    ALocal a1 = bOne.getA();

    if (a1 == null && b1 == null)
      return true;
    else
      return false;
  }

  public boolean test0() {
    TestUtil.logTrace("test0");
    return nullTest();
  }

  public boolean test1() {
    TestUtil.logTrace("test1");
    return nullTest();
  }

  public boolean test2() {
    TestUtil.logTrace("test2");
    ALocal aOne = getA1();
    ALocal aTwo = getA2();

    BLocal bOne = aOne.getB();
    BLocal bTwo = aTwo.getB();

    try {
      aOne.setB(aTwo.getB());

      ALocal a1 = bOne.getA();
      ALocal a2 = bTwo.getA();
      BLocal b1 = aOne.getB();
      BLocal b2 = aTwo.getB();

      if (b1.isIdentical(bTwo) && b2 == null && a1 == null
          && a2.isIdentical(aOne)) {
        TestUtil.logMsg("Relationship assignment passed");
        return true;
      } else {
        TestUtil.logMsg("Relationship assignment failed");
        if (!b1.isIdentical(bTwo))
          TestUtil.logErr("b1 not identical to b2");
        if (b2 != null)
          TestUtil.logErr("b2 not null");
        if (a1 != null)
          TestUtil.logErr("a1 not null");
        if (!a2.isIdentical(aOne))
          TestUtil.logErr("a2 not identical to a1");
        return false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public ADVC getAInfoFromB() {
    TestUtil.logTrace("getAInfoFromB");
    BLocal b = getB1();
    return b.getAInfo();
  }

  public BDVC getBInfoFromA() {
    TestUtil.logTrace("getBInfoFromA");
    ALocal a = getA1();
    return a.getBInfo();

  }

  private ALocal createALocal(String id, String name, int value)
      throws Exception {
    TestUtil.logTrace("createALocal");
    nctx = new TSNamingContext();
    ALocalHome aLocalHome = (ALocalHome) nctx.lookup(ALocal);
    ALocal aLocal = aLocalHome.create(id, name, value);
    return aLocal;
  }

  private BLocal createBLocal(String id, String name, int value)
      throws Exception {
    TestUtil.logTrace("createBLocal");
    nctx = new TSNamingContext();
    BLocalHome bLocalHome = (BLocalHome) nctx.lookup(BLocal);
    BLocal bLocal = bLocalHome.create(id, name, value);
    return bLocal;
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
        a1.setB(null);
        b1.setA(null);
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
      a1.setB(b1);
      b2 = createBLocal(bTwo.getId(), bTwo.getName(), bTwo.getValue());
      a2 = createALocal(aTwo.getId(), aTwo.getName(), aTwo.getValue());
      a2.setB(b2);
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
    if (getA1() != null) {
      TestUtil.logMsg("deleting entity object relationship ... A1");
      getA1().remove();
    }
    if (getB1() != null) {
      TestUtil.logMsg("deleting entity object relationship ... B1");
      getB1().remove();
    }
    if (getA2() != null) {
      TestUtil.logMsg("deleting entity object relationship ... A2");
      getA2().remove();
    }
    if (getB2() != null) {
      TestUtil.logMsg("deleting entity object relationship ... B2");
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
