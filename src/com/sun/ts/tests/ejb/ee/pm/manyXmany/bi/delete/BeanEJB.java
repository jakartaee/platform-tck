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

package com.sun.ts.tests.ejb.ee.pm.manyXmany.bi.delete;

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
  // all uni-directional to various entitybeans for testing

  // 1x1
  public abstract ALocal getA1();

  public abstract void setA1(ALocal v);

  // 1x1
  public abstract ALocal getA2();

  public abstract void setA2(ALocal v);

  // 1x1
  public abstract ALocal getA3();

  public abstract void setA3(ALocal v);

  // 1x1
  public abstract ALocal getA4();

  public abstract void setA4(ALocal v);

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

  public Collection getBInfoFromA() {
    TestUtil.logTrace("getBInfoFromA");
    Vector v = new Vector();
    if (getA1() != null) {
      Collection col = getA1().getB();
      Iterator iterator = col.iterator();
      while (iterator.hasNext()) {
        BLocal b = (BLocal) iterator.next();
        BDVC bDVC = new BDVC(b.getId(), b.getName(), b.getValue());
        v.add(bDVC);
      }
    }
    return v;
  }

  public Collection getAInfoFromB() {
    TestUtil.logTrace("getAInfoFromB");
    Vector v = new Vector();
    if (getB1() != null) {
      Collection col = getB1().getA();
      Iterator iterator = col.iterator();
      while (iterator.hasNext()) {
        ALocal a = (ALocal) iterator.next();
        ADVC aDVC = new ADVC(a.getId(), a.getName(), a.getValue());
        v.add(aDVC);
      }
    }
    return v;
  }

  public boolean test1() {
    // ensure the entity object is deleted
    TestUtil.logMsg("starting test1");

    try {
      ALocal aTwo = getA2();
      TestUtil.logMsg("aTwo = " + aTwo);

      TestUtil.logMsg("Delete entitybean object A2");
      aTwo.remove();

      TestUtil.logMsg("Attempt to call a method on A2");
      aTwo.getId();

      TestUtil.logErr(
          "Entitybean object A2 was not " + "deleted, expected EJBException");

      return false;

    } catch (EJBException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Entitybean object A2 was deleted");
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean test2() {
    // ensure relationships are updated correctly
    TestUtil.logMsg("starting test2");

    ALocal aTwo = getA2();

    Collection c_A1 = new Vector(); // b1,b2
    c_A1.add(getB1());
    c_A1.add(getB2());

    Collection c_A3 = new Vector(); // b2,b3,b4
    c_A3.add(getB2());
    c_A3.add(getB3());
    c_A3.add(getB4());

    Collection c_A4 = new Vector(); // b3,b4
    c_A4.add(getB3());
    c_A4.add(getB4());

    Collection c_B1 = new Vector(); // a1
    c_B1.add(getA1());

    Collection c_B2 = new Vector(); // a1,a3
    c_B2.add(getA1());
    c_B2.add(getA3());

    Collection c_B3 = new Vector(); // a3,a4
    c_B3.add(getA3());
    c_B3.add(getA4());

    Collection c_B4 = new Vector(); // a3,a4
    c_B4.add(getA3());
    c_B4.add(getA4());

    Collection a1, a3, a4;
    Collection b1, b2, b3, b4;

    try {
      TestUtil.logMsg("Delete entitybean object A2");
      aTwo.remove();

      TestUtil.logMsg("Verifying relationships of As and Bs");
      a1 = getA1().getB();
      a3 = getA3().getB();
      a4 = getA4().getB();
      b1 = getB1().getA();
      b2 = getB2().getA();
      b3 = getB3().getA();
      b4 = getB4().getA();

      if (a1.containsAll(c_A1) && a1.size() == c_A1.size()
          && a3.containsAll(c_A3) && a3.size() == c_A3.size()
          && a4.containsAll(c_A4) && a4.size() == c_A4.size()
          && b1.containsAll(c_B1) && b1.size() == c_B1.size()
          && b2.containsAll(c_B2) && b2.size() == c_B2.size()
          && b3.containsAll(c_B3) && b3.size() == c_B3.size()
          && b4.containsAll(c_B4) && b4.size() == c_B4.size()) {

        TestUtil.logMsg("relationships of As and Bs are updated correctly");
      } else {
        TestUtil.logMsg("relationships of As and Bs are not updated correctly");
        return false;
      }

      TestUtil.logMsg("Check relationship accessor method of BeanEJB");
      TestUtil.logMsg("getA2() = " + getA2());
      if (getA2() == null) {
        TestUtil.logMsg("Relationship accessor method of BeanEJB returns null");
        return true;
      } else {
        TestUtil
            .logErr("Relationship accessor method of BeanEJB returns not null");
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

    ALocal aTwo = getA2();

    try {
      BLocal bOne = getB1();
      TestUtil.logMsg("aTwo = " + aTwo);

      TestUtil.logMsg("Delete entitybean object A2");
      aTwo.remove();

      TestUtil.logMsg("Assigning A2 to B1 must throw IllegalArgumentException");
      Collection c = bOne.getA();
      c.add(aTwo);

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
          "Assigning aTwo to BeanEJB must throw IllegalArgumentException");
      setA2(aTwo);
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

  public String ejbCreate(String id, String name, int value, ADVC aOne,
      BDVC bOne, int flag) throws CreateException {
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

  public void ejbPostCreate(String id, String name, int value, ADVC aOne,
      BDVC bOne, int flag) throws CreateException {
    Collection c;
    TestUtil.logTrace("ejbPostCreate");
    try {
      ALocal a1 = createALocal(aOne.getId(), aOne.getName(), aOne.getValue());
      setA1(a1);

      BLocal b1 = createBLocal(bOne.getId(), bOne.getName(), bOne.getValue());
      setB1(b1);

      switch (flag) {
      case NO_RELATION_SET:
        break;
      case NULL_RELATION_SET:
        a1.setB(null);
        b1.setA(null);
        break;
      case RELATION_SET:
        c = a1.getB();
        c.add(b1);
        break;
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
  }

  public String ejbCreate(String id, String name, int value, ADVC aOne,
      ADVC aTwo, ADVC aThree, ADVC aFour, BDVC bOne, BDVC bTwo, BDVC bThree,
      BDVC bFour) throws CreateException {
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

  public void ejbPostCreate(String id, String name, int value, ADVC aOne,
      ADVC aTwo, ADVC aThree, ADVC aFour, BDVC bOne, BDVC bTwo, BDVC bThree,
      BDVC bFour) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    Collection c;
    try {
      ALocal a1 = createALocal(aOne.getId(), aOne.getName(), aOne.getValue());
      setA1(a1);
      ALocal a2 = createALocal(aTwo.getId(), aTwo.getName(), aTwo.getValue());
      setA2(a2);
      ALocal a3 = createALocal(aThree.getId(), aThree.getName(),
          aThree.getValue());
      setA3(a3);
      ALocal a4 = createALocal(aFour.getId(), aFour.getName(),
          aFour.getValue());
      setA4(a4);

      BLocal b1 = createBLocal(bOne.getId(), bOne.getName(), bOne.getValue());
      setB1(b1);
      BLocal b2 = createBLocal(bTwo.getId(), bTwo.getName(), bTwo.getValue());
      setB2(b2);
      BLocal b3 = createBLocal(bThree.getId(), bThree.getName(),
          bThree.getValue());
      setB3(b3);
      BLocal b4 = createBLocal(bFour.getId(), bFour.getName(),
          bFour.getValue());
      setB4(b4);

      c = a1.getB();
      c.add(b1);
      c.add(b2);
      c = b1.getA();
      c.add(a2);

      c = a2.getB();
      c.add(b2);
      c.add(b3);
      c = b2.getA();
      c.add(a3);

      c = a3.getB();
      c.add(b3);
      c.add(b4);
      c = b3.getA();
      c.add(a4);

      c = a4.getB();
      c.add(b4);

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
      TestUtil.logMsg("deleting entitybean object a1 ...");
      getA1().remove();
    }
    if (getA2() != null) {
      TestUtil.logMsg("deleting entitybean object a2 ...");
      getA2().remove();
    }
    if (getA3() != null) {
      TestUtil.logMsg("deleting entitybean object a3 ...");
      getA3().remove();
    }
    if (getA4() != null) {
      TestUtil.logMsg("deleting entitybean object a4 ...");
      getA4().remove();
    }
    if (getB1() != null) {
      TestUtil.logMsg("deleting entitybean object b1 ...");
      getB1().remove();
    }
    if (getB2() != null) {
      TestUtil.logMsg("deleting entitybean object b2 ...");
      getB2().remove();
    }
    if (getB3() != null) {
      TestUtil.logMsg("deleting entitybean object b3 ...");
      getB3().remove();
    }
    if (getB4() != null) {
      TestUtil.logMsg("deleting entitybean object b4 ...");
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
