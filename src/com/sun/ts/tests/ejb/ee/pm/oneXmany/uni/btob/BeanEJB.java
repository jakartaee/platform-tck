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
 * @(#)BeanEJB.java	1.12 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.oneXmany.uni.btob;

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

  // 1x1
  public abstract BLocal getB3();

  public abstract void setB3(BLocal v);

  // 1x1
  public abstract BLocal getB4();

  public abstract void setB4(BLocal v);

  // ===========================================================
  // Private methods

  private Collection copyCollection(Collection c) {
    Vector copy = new Vector();
    Iterator iterator = c.iterator();
    while (iterator.hasNext())
      copy.add(iterator.next());
    return copy;
  }

  private void dumpCollectionData(Collection c) {
    TestUtil.logMsg("Collection Data");
    TestUtil.logMsg("---------------");
    TestUtil.logMsg("- size=" + c.size());
    Iterator i = c.iterator();
    int elem = 1;
    while (i.hasNext()) {
      BLocal v = (BLocal) i.next();
      TestUtil.logMsg("- Element #" + elem++);
      TestUtil.logMsg("  id=" + v.getId() + ", name=" + v.getName() + ", value="
          + v.getValue());
    }
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

  public boolean setCmrFieldToNull() {
    TestUtil.logTrace("setCmrFieldToNull");
    try {
      ALocal a1 = getA1();
      return a1.setCmrFieldToNull();
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean test0() {
    TestUtil.logTrace("test0");
    ALocal aOne = getA1();
    Collection b1 = aOne.getB();

    if (b1.isEmpty())
      return true;
    else
      return false;
  }

  public Collection getBInfoFromA() {
    TestUtil.logTrace("getBInfoFromA");
    Vector v = new Vector();
    if (getA1() != null) {
      Collection c = getA1().getB();
      Iterator iterator = c.iterator();
      while (iterator.hasNext()) {
        BLocal b = (BLocal) iterator.next();
        BDVC bDVC = new BDVC(b.getId(), b.getName(), b.getValue());
        v.add(bDVC);
      }
    }
    return v;
  }

  public boolean doAssignmentTest1() {
    TestUtil.logMsg("doAssignmentTest1");

    ALocal aOne = getA1();
    ALocal aTwo = getA2();
    Collection bOne = aOne.getB();
    Collection bTwo = aTwo.getB();
    Collection b2Col = copyCollection(aTwo.getB());

    Collection b1, b2;

    try {
      TestUtil.logMsg("Dumping collection data for a1.getB() ...");
      dumpCollectionData(getA1().getB());
      TestUtil.logMsg("Dumping collection data for a2.getB() ...");
      dumpCollectionData(getA2().getB());

      TestUtil.logMsg("Performing assignment test ...");
      aOne.setB(aTwo.getB());

      TestUtil.logMsg("Getting assignment test results ...");
      b1 = aOne.getB();
      TestUtil.logMsg("Dumping collection data for a1.getB() ...");
      dumpCollectionData(b1);
      b2 = aTwo.getB();
      TestUtil.logMsg("Dumping collection data for a2.getB() ...");
      dumpCollectionData(b2);

      TestUtil.logMsg("Comparing assignment test results ...");
      if (b1.containsAll(b2Col) && b1.size() == b2Col.size() && b2.isEmpty()
          && b1 == bOne && b2 == bTwo) {
        TestUtil.logMsg("Relationship assignment passed");
        return true;
      } else {
        TestUtil.logMsg("Relationship assignment failed");
        if (!b1.containsAll(b2Col))
          TestUtil.logErr("b1 collection results unexpected");
        if (b1.size() != b2Col.size())
          TestUtil.logErr("collection size mismatch, expected: " + b2Col.size()
              + ", received: " + b1.size());
        if (!b2.isEmpty())
          TestUtil.logErr("b2 is not empty");
        if (b1 != bOne)
          TestUtil.logErr("b1 is not same collection reference");
        if (b2 != bTwo)
          TestUtil.logErr("b2 is not same collection reference");
        return false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean doAssignmentTest2() {
    TestUtil.logMsg("doAssignmentTest2");
    ALocal a1 = getA1();
    ALocal a2 = getA2();
    BLocal b22 = getB4();
    Collection b1 = a1.getB();
    Collection b2 = a2.getB();

    try {
      TestUtil.logMsg("Dumping collection data for a1.getB() ...");
      dumpCollectionData(a1.getB());
      TestUtil.logMsg("Dumping collection data for a2.getB() ...");
      dumpCollectionData(a2.getB());

      TestUtil.logMsg("Performing assignment test ...");
      a1.getB().add(b22);

      TestUtil.logMsg("Getting assignment test results ...");
      TestUtil.logMsg("Dumping collection data for a1.getB() ...");
      dumpCollectionData(b1);
      TestUtil.logMsg("Dumping collection data for a2.getB() ...");
      dumpCollectionData(b2);

      TestUtil.logMsg("Comparing assignment test results ...");
      if ((b1 == a1.getB()) && (b1.contains(b22))) {
        TestUtil.logMsg("Relationship assignment passed");
        return true;
      } else {
        TestUtil.logMsg("Relationship assignment failed");
        return false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

  public boolean doAssignmentTest3() {
    TestUtil.logMsg("doAssignmentTest3");

    ALocal a1 = getA1();
    BLocal b12 = getB2();

    Vector b = new Vector();
    b.add(getB1());

    try {
      TestUtil.logMsg("Performing assignment test ...");
      a1.getB().remove(b12);

      TestUtil.logMsg("Getting assignment test results ...");
      Collection b1 = a1.getB();
      TestUtil.logMsg("Dumping collection data for a1.getB() ...");
      dumpCollectionData(b1);

      TestUtil.logMsg("Comparing assignment test results ...");
      if (b1.containsAll(b) && b1.size() == b.size()) {
        TestUtil.logMsg("Relationship assignment passed");
        return true;
      } else {
        TestUtil.logMsg("Relationship assignment failed");
        if (!b1.containsAll(b))
          TestUtil.logErr("b1 collection results unexpected");
        if (b1.size() != b.size())
          TestUtil.logErr("collection size mismatch, expected: " + b.size()
              + ", received: " + b1.size());
        return false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
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
      BDVC bOne, BDVC bTwo, ADVC aTwo, BDVC bThree, BDVC bFour)
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

  public void ejbPostCreate(String id, String name, int value, ADVC aOne,
      BDVC bOne, BDVC bTwo, ADVC aTwo, BDVC bThree, BDVC bFour)
      throws CreateException {
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

      if (aTwo == null)
        return;

      ALocal a2 = createALocal(aTwo.getId(), aTwo.getName(), aTwo.getValue());
      setA2(a2);

      Collection bcol2 = a2.getB();

      BLocal b3 = createBLocal(bThree.getId(), bThree.getName(),
          bThree.getValue());
      setB3(b3);
      bcol2.add(b3);

      BLocal b4 = createBLocal(bFour.getId(), bFour.getName(),
          bFour.getValue());
      setB4(b4);
      bcol2.add(b4);

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
