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

package com.sun.ts.tests.ejb.ee.bb.entity.lrapitest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.rmi.PortableRemoteObject;

public class TestBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  // Entity Bean A -> Remote/Local Interfaces (CMP)
  // Entity Bean B -> Remote Interface Only (CMP)
  // Entity Bean C -> Local Interface Only (CMP)
  // Entity Bean D -> Local Interface Only (BMP)

  // JNDI Names for A and B Remote Home Interface
  private static final String ARemote = "java:comp/env/ejb/AEJB";

  private static final String BRemote = "java:comp/env/ejb/BEJB";

  // JNDI Names for A, C and D Local Home Interface
  private static final String ALocal = "java:comp/env/ejb/AEJBLocal";

  private static final String CLocal = "java:comp/env/ejb/CEJBLocal";

  private static final String DLocal = "java:comp/env/ejb/DEJBLocal";

  // References to Remote and Local Interfaces for Entity Bean A,B,C,D
  private A aRef = null;

  private AHome aHome = null;

  private ALocal aLocalRef = null;

  private ALocalHome aLocalHome = null;

  private B bRef = null;

  private BHome bHome = null;

  private CLocal cLocalRef = null;

  private CLocalHome cLocalHome = null;

  private DLocal dLocalRef = null;

  private DLocalHome dLocalHome = null;

  // ===========================================================
  // private methods

  private A createA(String id, String name, int value) throws Exception {
    TestUtil.logTrace("createA");
    nctx = new TSNamingContext();
    aHome = (AHome) nctx.lookup(ARemote, AHome.class);
    aLocalHome = (ALocalHome) nctx.lookup(ALocal);
    aRef = aHome.createA(id, name, value);
    aRef.init(harnessProps);
    return aRef;
  }

  private ALocal createALocal(String id, String name, int value)
      throws Exception {
    TestUtil.logTrace("createALocal");
    nctx = new TSNamingContext();
    aHome = (AHome) nctx.lookup(ARemote, AHome.class);
    aLocalHome = (ALocalHome) nctx.lookup(ALocal);
    return aLocalHome.createA(id, name, value);
  }

  private B createB(String id, String name, int value) throws Exception {
    TestUtil.logTrace("createB");
    nctx = new TSNamingContext();
    bHome = (BHome) nctx.lookup(BRemote, BHome.class);
    bRef = bHome.createB(id, name, value);
    bRef.init(harnessProps);
    return bRef;
  }

  private CLocal createCLocal(String id, String name, int value)
      throws Exception {
    TestUtil.logTrace("createCLocal");
    nctx = new TSNamingContext();
    cLocalHome = (CLocalHome) nctx.lookup(CLocal);
    return cLocalHome.createC(id, name, value);
  }

  private CLocalHome getCLocalHome() throws Exception {
    TestUtil.logTrace("getCLocalHome");
    nctx = new TSNamingContext();
    return (CLocalHome) nctx.lookup(CLocal);
  }

  private DLocal createDLocal(int id, String name, float price)
      throws Exception {
    TestUtil.logTrace("createDLocal");
    nctx = new TSNamingContext();
    dLocalHome = (DLocalHome) nctx.lookup(DLocal);
    return dLocalHome.createD(harnessProps, id, name, price);
  }

  private DLocalHome getDLocalHome() throws Exception {
    TestUtil.logTrace("getDLocalHome");
    nctx = new TSNamingContext();
    return (DLocalHome) nctx.lookup(DLocal);
  }

  private boolean createTest() {
    TestUtil.logTrace("createTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create local Entity Bean object");
      cLocalRef = createCLocal("1", "c1", 1);
      TestUtil.logMsg("Object was created successfully");
      TestUtil.logMsg(cLocalRef.whoAmILocal());
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean findTest() {
    TestUtil.logTrace("findTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create local Entity Bean object");
      cLocalRef = createCLocal("1", "c1", 1);
      TestUtil.logMsg("Find local Entity Bean object just created");
      CLocal findCLocalRef = cLocalHome.findByPrimaryKey("1");
      if (cLocalRef.isIdentical(findCLocalRef)) {
        TestUtil.logMsg("Object was found");
        pass = true;
      } else {
        TestUtil.logErr("Object was not found");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean removeLocalHomeTest() {
    TestUtil.logTrace("removeLocalHomeTest");
    boolean pass = true;
    boolean removeCalled = false;
    try {
      cLocalRef = createCLocal("1", "c1", 1);
      cLocalHome.remove("1");
      removeCalled = true;
      try {
        CLocal findCLocalRef = cLocalHome.findByPrimaryKey("1");
        TestUtil.logErr("object was found after removal");
        pass = false;
      } catch (FinderException e) {
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("object was not found after removal");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (!removeCalled)
          cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean homeTest() {
    TestUtil.logTrace("homeTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Execute local home method for local EntityBean");
      cLocalHome = getCLocalHome();
      String s = cLocalHome.addBar("foo");
      if (s.equals("foobar")) {
        TestUtil.logMsg("Received foobar as expected");
        pass = true;
      } else {
        TestUtil.logErr("Expected: foobar, Received: " + s);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean createBmpTest() {
    TestUtil.logTrace("createBmpTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create local Entity Bean object");
      dLocalRef = createDLocal(1, "d1", (float) 1.0);
      TestUtil.logMsg("Object was created successfully");
      TestUtil.logMsg(dLocalRef.whoAmILocal());
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        dLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean findBmpTest() {
    TestUtil.logTrace("findBmpTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create local Entity Bean object");
      dLocalRef = createDLocal(1, "d1", (float) 1.0);
      TestUtil.logMsg("Find local Entity Bean object just created");
      DLocal findDLocalRef = dLocalHome.findByPrimaryKey(new Integer(1));
      if (dLocalRef.isIdentical(findDLocalRef)) {
        TestUtil.logMsg("Object was found");
        pass = true;
      } else {
        TestUtil.logErr("Object was not found");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        dLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean removeLocalHomeBmpTest() {
    TestUtil.logTrace("removeLocalHomeBmpTest");
    boolean pass = true;
    boolean removeCalled = false;
    try {
      dLocalRef = createDLocal(1, "d1", (float) 1.0);
      dLocalHome.remove(new Integer(1));
      removeCalled = true;
      try {
        DLocal findDLocalRef = dLocalHome.findByPrimaryKey(new Integer(1));
        TestUtil.logErr("object was found after removal");
        pass = false;
      } catch (FinderException e) {
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("object was not found after removal");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (!removeCalled)
          dLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean homeBmpTest() {
    TestUtil.logTrace("homeBmpTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Execute local home method for local EntityBean");
      dLocalHome = getDLocalHome();
      String s = dLocalHome.addBar("foo");
      if (s.equals("foobar")) {
        TestUtil.logMsg("Received foobar as expected");
        pass = true;
      } else {
        TestUtil.logErr("Expected: foobar, Received: " + s);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean getPrimaryKeyTest() {
    TestUtil.logTrace("getPrimaryKeyTest");
    boolean pass = true;
    String pk = "1";
    try {
      cLocalRef = createCLocal(pk, "c1", 1);
      Object primaryKey = cLocalRef.getPrimaryKey();
      if (!(primaryKey instanceof String)) {
        TestUtil.logErr("primary key not a String object");
        pass = false;
      } else {
        String s = (String) primaryKey;
        if (!s.equals(pk)) {
          TestUtil.logErr("primaryKey expected: " + pk + ", received: " + s);
          pass = false;
        } else {
          TestUtil.logMsg("primaryKey value is as expected");
          pass = true;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean getEJBLocalHomeTest() {
    TestUtil.logTrace("getEJBLocalHomeTest");
    boolean pass = true;
    try {
      cLocalRef = createCLocal("1", "c1", 1);
      CLocalHome beanHome = (CLocalHome) cLocalRef.getEJBLocalHome();
      if (beanHome != null) {
        CLocal findCLocalRef = beanHome.findByPrimaryKey("1");
        if (cLocalRef.isIdentical(findCLocalRef))
          pass = true;
        else
          pass = false;
      } else {
        TestUtil.logErr("LocalHome returned was null");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean callBusinessMethodTest() {
    TestUtil.logTrace("callBusinessMethodTest");
    boolean pass = true;
    try {
      cLocalRef = createCLocal("1", "c1", 1);
      TestUtil.logMsg("calling business method: " + cLocalRef.whoAmILocal());
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean isIdenticalTest() {
    TestUtil.logTrace("isIdenticalTest");
    boolean pass = true;
    CLocal cLocalRef1 = null, cLocalRef2 = null;
    try {
      cLocalRef1 = createCLocal("1", "c1", 1);
      cLocalRef2 = createCLocal("2", "c2", 2);
      TestUtil.logMsg("Do isIdentical test with same object");
      if (cLocalRef1.isIdentical(cLocalRef1)) {
        TestUtil.logMsg("isIdentical same object test - okay");
      } else {
        TestUtil.logErr("isIdentical same object test - not okay");
        pass = false;
      }
      TestUtil.logMsg("Do isIdentical test with different objects");
      if (cLocalRef1.isIdentical(cLocalRef2)) {
        TestUtil.logErr("isIdentical diff object test - not okay");
        pass = false;
      } else {
        TestUtil.logMsg("isIdentical diff object test - okay");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        cLocalRef1.remove();
        cLocalRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean removeLocalObjectTest() {
    TestUtil.logTrace("removeLocalObjectTest");
    boolean pass = true;
    boolean removeCalled = false;
    try {
      cLocalRef = createCLocal("1", "c1", 1);
      cLocalRef.remove();
      removeCalled = true;
      try {
        CLocal findCLocalRef = cLocalHome.findByPrimaryKey("1");
        TestUtil.logErr("object was found after removal");
        pass = false;
      } catch (FinderException e) {
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("object was not found after removal");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (!removeCalled)
          cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean mapLocalToRemoteTest() {
    TestUtil.logTrace("mapLocalToRemoteTest");
    boolean pass = true;
    ALocal aRef = null;
    try {
      aRef = createALocal("1", "a1", 1);
      String pk = (String) aRef.getPrimaryKey();
      A a = (A) aHome.findByPrimaryKey(pk);
      TestUtil.logMsg("Now Call method on Remote Interface");
      String whoAmI = a.whoAmIRemote();
      if (whoAmI.equals("Remote-1-a1-1"))
        pass = true;
      else {
        TestUtil.logErr("Expected: (Remote-1-a1-1), Received (" + whoAmI + ")");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        aRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean mapRemoteToLocalTest() {
    TestUtil.logTrace("mapRemoteToLocalTest");
    boolean pass = true;
    A aRef = null;
    try {
      aRef = createA("1", "a1", 1);
      String pk = (String) aRef.getPrimaryKey();
      ALocal a = (ALocal) aLocalHome.findByPrimaryKey(pk);
      TestUtil.logMsg("Now Call method on Local Interface");
      String whoAmI = a.whoAmILocal();
      if (whoAmI.equals("Local-1-a1-1"))
        pass = true;
      else {
        TestUtil.logErr("Expected: (Local-1-a1-1), Received (" + whoAmI + ")");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        aRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  // ===========================================================
  // EJB Specification Required Methods

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("exception occurred: " + e);
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public boolean test1() {
    boolean pass = true;
    try {
      aRef = createA("1", "a1", 1);
      pass = aRef.test1();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    } finally {
      try {
        aRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (mapRemoteToLocalTest())
      TestUtil.logMsg("mapRemoteToLocalTest ... PASSED");
    else {
      TestUtil.logMsg("mapRemoteToLocalTest ... FAILED");
      pass = false;
    }
    if (mapLocalToRemoteTest())
      TestUtil.logMsg("mapLocalToRemoteTest ... PASSED");
    else {
      TestUtil.logMsg("mapLocalToRemoteTest ... FAILED");
      pass = false;
    }
    return pass;
  }

  public boolean test2() {
    boolean pass = true;
    try {
      bRef = createB("1", "b1", 1);
      pass = bRef.test2();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean test3() {
    boolean pass = true;
    try {
      cLocalRef = createCLocal("1", "c1", 1);
      pass = cLocalRef.test3();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    } finally {
      try {
        cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean test4() {
    boolean pass = true;
    if (createTest())
      TestUtil.logMsg("EJBLocalHome.create ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.create ... FAILED");
      pass = false;
    }
    if (findTest())
      TestUtil.logMsg("EJBLocalHome.find ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.find ... FAILED");
      pass = false;
    }
    if (removeLocalHomeTest())
      TestUtil.logMsg("EJBLocalHome.remove ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.remove ... FAILED");
      pass = false;
    }
    if (homeTest())
      TestUtil.logMsg("EJBLocalHome.home<METHOD> ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.home<METHOD> ... FAILED");
      pass = false;
    }
    return pass;
  }

  public boolean test5() {
    boolean pass = true;
    if (getPrimaryKeyTest())
      TestUtil.logMsg("EJBLocalObject.getPrimaryKey ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalObject.getPrimaryKey ... FAILED");
      pass = false;
    }
    if (isIdenticalTest())
      TestUtil.logMsg("EJBLocalObject.isIdentical ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalObject.isIdentical ... FAILED");
      pass = false;
    }
    if (getEJBLocalHomeTest())
      TestUtil.logMsg("EJBLocalObject.getEJBLocalHome ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalObject.getEJBLocalHome ... FAILED");
      pass = false;
    }
    if (callBusinessMethodTest())
      TestUtil.logMsg("businessMethod ... PASSED");
    else {
      TestUtil.logMsg("businessMethod ... FAILED");
      pass = false;
    }
    if (removeLocalObjectTest())
      TestUtil.logMsg("EJBLocalObject.remove ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalObject.remove ... FAILED");
      pass = false;
    }
    return pass;
  }

  public boolean test6() {
    boolean pass = true;
    try {
      aRef = createA("1", "a1", 1);
      aLocalRef = createALocal("2", "a2", 2);
      TestUtil.logMsg("call method to return a remote ref");
      A aRef1 = (A) PortableRemoteObject.narrow(aRef.getRemoteRef(), A.class);
      TestUtil.logMsg("call business method on remote ref returned");
      String whoami = aRef1.whoAmIRemote();
      if (!whoami.equals("Remote-1-a1-1")) {
        TestUtil
            .logErr("Expected: (Remote-1-a1-1), Received: (" + whoami + ")");
        pass = false;
      }
      TestUtil.logMsg("call method to return a local ref");
      ALocal aLocalRef1 = (ALocal) aLocalRef.getLocalRef();
      TestUtil.logMsg("call business method on local ref returned");
      whoami = aLocalRef1.whoAmILocal();
      if (!whoami.equals("Local-2-a2-2")) {
        TestUtil.logErr("Expected: (Local-2-a2-2), Received: (" + whoami + ")");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    } finally {
      try {
        aRef.remove();
        aLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean test7() {
    boolean pass = true;
    try {
      cLocalRef = createCLocal("1", "c1", 1);
      cLocalRef.remove();
      try {
        cLocalRef.whoAmILocal();
        TestUtil
            .logErr("NoSuchObjectLocalException did not occur - unexpected");
        pass = false;
      } catch (NoSuchObjectLocalException e) {
        TestUtil.logMsg("NoSuchObjectLocalException did occur - expected");
      } catch (Exception e) {
        TestUtil.logMsg("Exception: " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean test8() {
    boolean pass = true;
    if (createBmpTest())
      TestUtil.logMsg("EJBLocalHome.create ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.create ... FAILED");
      pass = false;
    }
    if (findBmpTest())
      TestUtil.logMsg("EJBLocalHome.find ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.find ... FAILED");
      pass = false;
    }
    if (removeLocalHomeBmpTest())
      TestUtil.logMsg("EJBLocalHome.remove ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.remove ... FAILED");
      pass = false;
    }
    if (homeBmpTest())
      TestUtil.logMsg("EJBLocalHome.home<METHOD> ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.home<METHOD> ... FAILED");
      pass = false;
    }
    return pass;
  }

  // ===========================================================
}
