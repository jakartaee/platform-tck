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

package com.sun.ts.tests.ejb.ee.pm.manyXone.uni.delete;

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
  // all uni-directional to various entity beans for testing

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

  // 1x1
  public abstract BLocal getB3();

  public abstract void setB3(BLocal v);

  // 1x1
  public abstract BLocal getB4();

  public abstract void setB4(BLocal v);

  // ===========================================================
  // Private methods

  private boolean isA(int i) {
    TestUtil.logTrace("isA");
    if (i == 1)
      return getB1().getA() != null;
    else if (i == 2)
      return getB2().getA() != null;
    else
      return false;
  }

  // ===========================================================
  // Bean interface business methods

  public void init(Properties p) {
    TestUtil.logTrace("init");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      throw new EJBException(e.getMessage());
    }
  }

  public boolean isA() {
    TestUtil.logTrace("isA");
    return getB1().getA() != null;
  }

  public ADVC getAInfo(int i) {
    TestUtil.logTrace("getAInfo");
    if (isA(i)) {
      ALocal a = null;
      switch (i) {
      case 1:
        a = getB1().getA();
        break;
      case 2:
        a = getB2().getA();
      }
      if (a != null)
        return new ADVC(a.getId(), a.getName(), a.getValue());
    }
    return null;
  }

  public boolean test1() {
    // ensure the entity object is deleted
    TestUtil.logMsg("starting test1");
    ALocal aOne = null;
    BLocal bOne = null;
    BLocal bTwo = null;

    try {
      aOne = getA1();
      bOne = getB1();
      bTwo = getB2();
      TestUtil.logMsg("aOne = " + aOne);
      TestUtil.logMsg("bOne = " + bOne);
      TestUtil.logMsg("bTwo = " + bTwo);
      TestUtil.logMsg("Delete entitybean object A1");
      aOne.remove();
      TestUtil.logMsg("Attempt to call a method on A1");
      aOne.getId();
      TestUtil.logErr(
          "Entitybean object A1 was not " + "deleted, expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.logMsg("Entitybean object A1 was deleted");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
    try {
      TestUtil.logMsg("Delete entitybean object B1");
      bOne.remove();
      TestUtil.logMsg("Attempt to call a method on B1");
      bOne.getId();
      TestUtil.logErr(
          "Entitybean object B1 was not " + "deleted, expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.logMsg("Entitybean object B1 was deleted");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
    try {
      TestUtil.logMsg("Delete entitybean object B2");
      bTwo.remove();
      TestUtil.logMsg("Attempt to call a method on second B2");
      bTwo.getId();
      TestUtil.logErr(
          "Entitybean object B2 was not " + "deleted, expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.logMsg("Entitybean object B2 was deleted");
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean test2() {
    // ensure relationships are updated correctly after remove aOne
    TestUtil.logMsg("starting test2");
    ALocal aOne = null;
    BLocal bOne = null;
    BLocal bTwo = null;

    try {
      aOne = getA1();
      bOne = getB1();
      bTwo = getB2();
      TestUtil.logMsg("aOne = " + aOne);
      TestUtil.logMsg("bOne = " + bOne);
      TestUtil.logMsg("bTwo = " + bTwo);
      TestUtil.logMsg("Delete entitybean object A1");
      aOne.remove();
      TestUtil.logMsg("Check relationship accessor method of "
          + "entitybean object bOne and bTwo");
      if (bOne.getA() == null && bTwo.getA() == null)
        TestUtil.logMsg("B1 and B2 relationship updated corrently ");
      else {
        TestUtil.logMsg("B1 and B2 relationship not updated corrently ");
        return false;
      }
      TestUtil.logMsg("Check relationship accessor method of BeanEJB");
      if (getA1() == null) {
        TestUtil.logMsg("BeanEJB relationship updated corrently ");
        return true;
      } else {
        TestUtil.logMsg("BeanEJB relationship not updated corrently ");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean test3() {
    // ensure entity object deleted can't be assigned to cmr field of another
    // bean
    TestUtil.logMsg("starting test3");
    ALocal aOne = null;
    BLocal bOne = null;
    BLocal bTwo = null;
    boolean pass = true;

    try {
      aOne = getA1();
      bOne = getB1();
      bTwo = getB2();
      TestUtil.logMsg("aOne = " + aOne);
      TestUtil.logMsg("bOne = " + bOne);
      TestUtil.logMsg("bTwo = " + bTwo);
      TestUtil.logMsg("Delete entitybean object A1");
      aOne.remove();
      TestUtil.logMsg(
          "Assigning aOne to BeanEJB must throw IllegalArgumentException");
      setA1(aOne);
      TestUtil.logErr("Did not get expected IllegalArgumentException");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
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
        b1.setA(null);
        break;
      case RELATION_SET:
        a1 = createALocal(a.getId(), a.getName(), a.getValue());
        b1 = createBLocal(b.getId(), b.getName(), b.getValue());
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
      BDVC bOne, BDVC bTwo, ADVC aTwo, BDVC bThree, BDVC bFour)
      throws CreateException {
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
      BDVC bOne, BDVC bTwo, ADVC aTwo, BDVC bThree, BDVC bFour)
      throws CreateException {
    ALocal a1, a2;
    BLocal b1, b2, b3, b4;

    TestUtil.logTrace("ejbPostCreate");
    try {
      a1 = createALocal(aOne.getId(), aOne.getName(), aOne.getValue());
      b1 = createBLocal(bOne.getId(), bOne.getName(), bOne.getValue());
      b1.setA(a1);
      b2 = createBLocal(bTwo.getId(), bTwo.getName(), bTwo.getValue());
      b2.setA(a1);

      setA1(a1);
      setB1(b1);
      setB2(b2);

      if (aTwo == null)
        return;

      a2 = createALocal(aTwo.getId(), aTwo.getName(), aTwo.getValue());
      b3 = createBLocal(bThree.getId(), bThree.getName(), bThree.getValue());
      b3.setA(a2);
      b4 = createBLocal(bFour.getId(), bFour.getName(), bFour.getValue());
      b4.setA(a2);

      setA2(a2);
      setB3(b3);
      setB4(b4);
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
      TestUtil.logMsg("deleting entity object a1 ...");
      getA1().remove();
    }
    if (getA2() != null) {
      TestUtil.logMsg("deleting entity object a2 ...");
      getA2().remove();
    }
    if (getB1() != null) {
      TestUtil.logMsg("deleting entity object b1 ...");
      getB1().remove();
    }
    if (getB2() != null) {
      TestUtil.logMsg("deleting entity object b2 ...");
      getB2().remove();
    }
    if (getB3() != null) {
      TestUtil.logMsg("deleting entity object b3 ...");
      getB3().remove();
    }
    if (getB4() != null) {
      TestUtil.logMsg("deleting entity object b4 ...");
      getB4().remove();
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
