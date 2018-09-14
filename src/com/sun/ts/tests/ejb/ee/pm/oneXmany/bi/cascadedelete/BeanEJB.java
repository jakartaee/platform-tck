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
 * @(#)BeanEJB.java	1.13 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.oneXmany.bi.cascadedelete;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class BeanEJB implements EntityBean {

  private EntityContext context = null;

  private TSNamingContext nctx = null;

  private static final int NO_RELATION_SET = 0;

  private static final int NULL_RELATION_SET = 1;

  private static final int RELATION_SET = 2;

  // JNDI Names for A and B Local Home Interface
  private static final String ALocal = "java:comp/env/ejb/ALocal";

  private static final String BLocal = "java:comp/env/ejb/BLocal";

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
    ALocal aOne = getA1();
    BLocal bOne = getB1();
    BLocal bTwo = getB2();

    TestUtil.logMsg("aOne = " + aOne);
    TestUtil.logMsg("bOne = " + bOne);
    TestUtil.logMsg("bTwo = " + bTwo);
    try {
      TestUtil.logMsg("Cascade delete entitybean object A1");
      aOne.remove();
      TestUtil.logMsg("Attempt to call a method on A1");
      aOne.getId();
      TestUtil.logErr(
          "Entitybean object A1 was not deleted, " + "expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Entitybean object A1 was deleted");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
    TestUtil
        .logMsg("Check that entitybean objects B1 and B2 were also deleted");
    try {
      TestUtil.logMsg("Attempt to call a method on B1");
      bOne.getId();
      TestUtil.logErr(
          "Entitybean object B1 was not " + "deleted, expected EJBException");
      TestUtil.logErr("Entitybean object B1 was not deleted");
      return false;
    } catch (EJBException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Entitybean object B1 was deleted");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
    try {
      TestUtil.logMsg("Attempt to call a method on B2");
      bTwo.getId();
      TestUtil.logErr(
          "Entitybean object B2 was not " + "deleted, expected EJBException");
      TestUtil.logErr("Entitybean object B2 was not deleted");
      return false;
    } catch (EJBException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Entitybean object B2 was deleted");
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean test2a() {
    ALocal aOne = getA1();
    BLocal bOne = getB1();
    BLocal bTwo = getB2();

    TestUtil.logMsg("aOne = " + aOne);
    TestUtil.logMsg("bOne = " + bOne);
    TestUtil.logMsg("bTwo = " + bTwo);
    try {
      TestUtil.logMsg("Cascade delete entitybean object A1");
      aOne.remove();
      TestUtil.logMsg("Check relationship accessor methods of BeanEJB");
      TestUtil.logMsg("Calling getA1(), getB1(), getB2()");
      TestUtil.logMsg("getA1() = " + getA1());
      TestUtil.logMsg("getB1() = " + getB1());
      TestUtil.logMsg("getB2() = " + getB2());
      if (getA1() != null) {
        TestUtil.logErr("Accessor method getA1() does not return null");
        return false;
      } else if (getB1() != null) {
        TestUtil.logErr("Accessor method getB1() does not return null");
        return false;
      } else if (getB2() != null) {
        TestUtil.logErr("Accessor method getB2() does not return null");
        return false;
      }
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean test2b() {
    ALocal aOne = getA1();
    BLocal bOne = getB1();
    BLocal bTwo = getB2();
    boolean part1 = true;
    boolean part2 = true;

    TestUtil.logMsg("aOne = " + aOne);
    TestUtil.logMsg("bOne = " + bOne);
    TestUtil.logMsg("bTwo = " + bTwo);
    try {
      TestUtil.logMsg("Delete entitybean object B1");
      bOne.remove();
      TestUtil.logMsg(
          "Check relationship accessor method of " + "entitybean object A1");
      TestUtil.logMsg("Calling Collection c = aOne.getB()");
      Collection c = aOne.getB();
      TestUtil.logMsg("Check size of collection returned");
      if (c.size() == 1) {
        TestUtil.logMsg("Collection size returned is 1");
        TestUtil.logMsg("Check contents of collection returned");
        Iterator iterator = c.iterator();
        BLocal b = (BLocal) iterator.next();
        if (b.getValue() == 12) {
          TestUtil.logMsg("Collection contents is correct");
        } else {
          TestUtil.logErr("Collection contents is incorrect B(" + b.getId()
              + ", " + b.getName() + ", " + b.getValue() + "), "
              + " expected B(12, b12, 12)");
          part1 = false;
        }
      } else {
        TestUtil
            .logErr("Collection size returned is " + c.size() + ", expected 1");
        TestUtil.logErr("Entitybean object B1 was not removed from collection");
        part1 = false;
      }
      TestUtil.logMsg("Check relationship accessor method of BeanEJB");
      TestUtil.logMsg("Calling c = getA1().getB()");
      c = getA1().getB();
      TestUtil.logMsg("Check size of collection returned");
      if (c.size() == 1) {
        TestUtil.logMsg("Collection size returned is 1");
        TestUtil.logMsg("Check contents of collection returned");
        Iterator iterator = c.iterator();
        BLocal b = (BLocal) iterator.next();
        if (b.getValue() == 12) {
          TestUtil.logMsg("Collection contents is correct");
        } else {
          TestUtil.logErr("Collection contents is incorrect B(" + b.getId()
              + ", " + b.getName() + ", " + b.getValue() + "), "
              + " expected B(12, b12, 12)");
          part2 = false;
        }
      } else {
        TestUtil
            .logErr("Collection size returned is " + c.size() + ", expected 1");
        TestUtil.logErr("Entitybean object B1 was not removed from collection");
        part2 = false;
      }
      if (part1 && part2)
        return true;
      else
        return false;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean test3() {
    ALocal aOne = getA1();
    BLocal bOne = getB1();
    BLocal bTwo = getB2();

    TestUtil.logMsg("aOne = " + aOne);
    TestUtil.logMsg("bOne = " + bOne);
    TestUtil.logMsg("bTwo = " + bTwo);
    try {
      TestUtil.logMsg("Cascade delete entitybean object A1");
      aOne.remove();
      TestUtil.logMsg("Accessor method of A1 must throw EJBException");
      aOne.getId();
      TestUtil.logErr("Did not get expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.logMsg("Caught expected EJBException");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
    try {
      TestUtil.logMsg("Accessor method of B1 must throw EJBException");
      bOne.getId();
      TestUtil.logErr("Did not get expected EJBException");
      return false;
    } catch (EJBException e) {
      TestUtil.logMsg("Caught expected EJBException");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
    try {
      TestUtil.logMsg("Accessor method of B2 must throw EJBException");
      bTwo.getId();
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
    ALocal aOne = getA1();
    BLocal bOne = getB1();
    BLocal bTwo = getB2();

    TestUtil.logMsg("aOne = " + aOne);
    TestUtil.logMsg("bOne = " + bOne);
    TestUtil.logMsg("bTwo = " + bTwo);
    try {
      TestUtil.logMsg("Cascade delete entitybean object A1");
      aOne.remove();
      TestUtil.logMsg(
          "Assigning A1 to BeanEJB must throw IllegalArgumentException");
      TestUtil.logMsg("Calling setA1(aOne)");
      setA1(aOne);
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
          "Assigning B1 to BeanEJB must throw IllegalArgumentException");
      TestUtil.logMsg("Calling setB1(bOne)");
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
          "Assigning B2 to BeanEJB must throw IllegalArgumentException");
      TestUtil.logMsg("Calling setB2(bTwo)");
      setB2(bTwo);
      TestUtil.logErr("Did not get expected IllegalArgumentException");
      return false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e, e);
      return false;
    }
    return true;
  }

  public Collection getBInfoFromA() {
    TestUtil.logTrace("getBInfoFromA");
    Vector v = new Vector();
    if (getA1() != null) {
      Collection bcol = getA1().getB();
      Iterator iterator = bcol.iterator();
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
    if (getA1() != null) {
      Collection bcol = getA1().getB();
      Iterator iterator = bcol.iterator();
      while (iterator.hasNext()) {
        BLocal b = (BLocal) iterator.next();
        ALocal a = b.getA();
        ADVC aDVC = new ADVC(a.getId(), a.getName(), a.getValue());
        v.add(aDVC);
      }
    }
    return v;
  }

  private Collection copyCollection(Collection c) {
    Vector copy = new Vector();
    Iterator iterator = c.iterator();
    while (iterator.hasNext())
      copy.add(iterator.next());
    return copy;
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
        Collection c = a1.getB();
        c.add(b1);
        break;
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
  }

  public String ejbCreate(String id, String name, int value, ADVC aOne,
      BDVC bOne, BDVC bTwo) throws CreateException {
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
      BDVC bOne, BDVC bTwo) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    try {
      ALocal a1 = createALocal(aOne.getId(), aOne.getName(), aOne.getValue());
      setA1(a1);

      Collection bcol1 = a1.getB();

      BLocal b1 = createBLocal(bOne.getId(), bOne.getName(), bOne.getValue());
      setB1(b1);
      bcol1.add(b1);

      BLocal b2 = createBLocal(bTwo.getId(), bTwo.getName(), bTwo.getValue());
      setB2(b2);
      bcol1.add(b2);

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
