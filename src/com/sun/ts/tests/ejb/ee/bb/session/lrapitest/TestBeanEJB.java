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
 * @(#)TestBeanEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.lrapitest;

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

  // Session Bean A -> Remote/Local Interfaces (Stateful)
  // Session Bean B -> Remote Interface Only (Stateful)
  // Session Bean C -> Local Interface Only (Stateful)
  // Session Bean D -> Local Interface Only (Stateless)

  // JNDI Names for A and B Remote Home Interface
  private static final String ARemote = "java:comp/env/ejb/AEJB";

  private static final String BRemote = "java:comp/env/ejb/BEJB";

  // JNDI Names for A, C and D Local Home Interface
  private static final String ALocal = "java:comp/env/ejb/AEJBLocal";

  private static final String CLocal = "java:comp/env/ejb/CEJBLocal";

  private static final String DLocal = "java:comp/env/ejb/DEJBLocal";

  // References to Remote and Local Interfaces for Session Bean A,B,C,D
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

  private A createA(String s) throws Exception {
    TestUtil.logTrace("createA");
    aHome = (AHome) nctx.lookup(ARemote, AHome.class);
    aRef = aHome.createA(s);
    aRef.init(harnessProps);
    return aRef;
  }

  private ALocal createALocal(String s) throws Exception {
    TestUtil.logTrace("createALocal");
    aLocalHome = (ALocalHome) nctx.lookup(ALocal);
    return aLocalHome.createA(s);
  }

  private B createB(String s) throws Exception {
    TestUtil.logTrace("createB");
    bHome = (BHome) nctx.lookup(BRemote, BHome.class);
    bRef = bHome.createB(s);
    bRef.init(harnessProps);
    return bRef;
  }

  private CLocal createCLocal(String s) throws Exception {
    TestUtil.logTrace("createCLocal");
    cLocalHome = (CLocalHome) nctx.lookup(CLocal);
    return cLocalHome.createC(s);
  }

  private DLocal createDLocal() throws Exception {
    TestUtil.logTrace("createDLocal");
    dLocalHome = (DLocalHome) nctx.lookup(DLocal);
    return dLocalHome.create();
  }

  private boolean createTest() {
    TestUtil.logTrace("createTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create local Session Bean object");
      cLocalRef = createCLocal("C1");
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

  private boolean removeLocalHomeTest() {
    TestUtil.logTrace("removeLocalHomeTest");
    boolean pass = true;
    try {
      cLocalRef = createCLocal("C1");
      try {
        cLocalHome.remove("C1");
        TestUtil.logErr("RemoveException did not occur - unexpected");
        pass = false;
      } catch (RemoveException e) {
        TestUtil.logMsg("RemoveException did occur - expected");
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

  private boolean getPrimaryKeyTest() {
    TestUtil.logTrace("getPrimaryKeyTest");
    boolean pass = true;
    String pk = "1";
    try {
      cLocalRef = createCLocal("C1");
      Object primaryKey = cLocalRef.getPrimaryKey();
      TestUtil.logErr("EJBException did not occur - unexpected");
      pass = false;
    } catch (EJBException e) {
      TestUtil.logMsg("EJBException did occur - expected");
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

  private boolean getEJBLocalHomeTest() {
    TestUtil.logTrace("getEJBLocalHomeTest");
    boolean pass = true;
    try {
      cLocalRef = createCLocal("C1");
      CLocalHome beanHome = (CLocalHome) cLocalRef.getEJBLocalHome();
      if (beanHome != null) {
        pass = true;
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
      cLocalRef = createCLocal("C1");
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

  private boolean isIdenticalStatefulTest() {
    TestUtil.logTrace("isIdenticalStatefulTest");
    boolean pass = true;
    CLocal cLocalRef1 = null, cLocalRef2 = null;
    try {
      cLocalRef1 = createCLocal("C1");
      cLocalRef2 = createCLocal("C2");
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
        TestUtil.logErr("isIdentical diff object test - okay");
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

  private boolean isIdenticalStatelessTest() {
    TestUtil.logTrace("isIdenticalStatelessTest");
    boolean pass = true;
    DLocal dLocalRef1 = null, dLocalRef2 = null;
    try {
      dLocalRef1 = createDLocal();
      dLocalRef2 = createDLocal();
      TestUtil.logMsg("Do isIdentical test with same object");
      if (dLocalRef1.isIdentical(dLocalRef1)) {
        TestUtil.logMsg("isIdentical same object test - okay");
      } else {
        TestUtil.logErr("isIdentical same object test - not okay");
        pass = false;
      }
      TestUtil.logMsg("Do isIdentical test with different objects");
      if (dLocalRef1.isIdentical(dLocalRef2)) {
        TestUtil.logMsg("isIdentical same object test - okay");
      } else {
        TestUtil.logErr("isIdentical same object test - not okay");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        dLocalRef1.remove();
        dLocalRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  private boolean removeLocalObjectTest() {
    TestUtil.logTrace("removeLocalObjectTest");
    boolean pass = true;
    try {
      cLocalRef = createCLocal("C1");
      cLocalRef.remove();
      try {
        cLocalRef.whoAmILocal();
        TestUtil.logErr("object was found after removal");
        pass = false;
      } catch (EJBException e) {
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("object was not found after removal");
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
      aRef = createA("A1");
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
    return pass;
  }

  public boolean test2() {
    boolean pass = true;
    try {
      bRef = createB("B1");
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
      cLocalRef = createCLocal("C1");
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
    if (removeLocalHomeTest())
      TestUtil.logMsg("EJBLocalHome.remove ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalHome.remove ... FAILED");
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
    if (isIdenticalStatefulTest())
      TestUtil.logMsg("EJBLocalObject.isIdentical (Stateful) ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalObject.isIdentical (Stateful) ... FAILED");
      pass = false;
    }
    if (isIdenticalStatelessTest())
      TestUtil.logMsg("EJBLocalObject.isIdentical (Stateless) ... PASSED");
    else {
      TestUtil.logMsg("EJBLocalObject.isIdentical (Stateless) ... FAILED");
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
      aRef = createA("A1");
      aLocalRef = createALocal("A1");
      TestUtil.logMsg("call method to return a remote ref");
      A aRef1 = (A) PortableRemoteObject.narrow(aRef.getRemoteRef(), A.class);
      TestUtil.logMsg("call business method on remote ref returned");
      String whoami = aRef1.whoAmIRemote();
      if (!whoami.equals("Remote-A1")) {
        TestUtil.logErr("Expected: (Remote-A1), Received: (" + whoami + ")");
        pass = false;
      }
      TestUtil.logMsg("call method to return a local ref");
      ALocal aLocalRef1 = (ALocal) aLocalRef.getLocalRef();
      TestUtil.logMsg("call business method on local ref returned");
      whoami = aLocalRef1.whoAmILocal();
      if (!whoami.equals("Local-A1")) {
        TestUtil.logErr("Expected: (Local-A1), Received: (" + whoami + ")");
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
      cLocalRef = createCLocal("C1");
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
    TestUtil.logTrace("test8");
    boolean pass = false;
    try {
      TestUtil.logMsg("Lookup local home of Session Bean (SF) and do create");
      aLocalRef = createALocal("8");
      TestUtil.logMsg("Calling local business method: whoAmILocal");
      String s = aLocalRef.whoAmILocal();
    } catch (Exception e) {
      TestUtil
          .logErr("Caught Unexpected Exception in test8: " + e.getMessage());
    } finally {
      try {
        TestUtil.logMsg("Attempt Removal of bean while participating in TX");
        aLocalRef.remove();
      } catch (RemoveException re) {
        TestUtil.logMsg("RemoveException did occur - expected");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("RemoveException did not occur - unexpected", e);
      }
    }
    return pass;
  }

  public boolean test9() {
    TestUtil.logTrace("test9");
    boolean pass = false;
    try {
      TestUtil.logMsg("Lookup remote home of Session Bean (SF) and do create");
      aRef = createA("9");
      TestUtil.logMsg("Calling remote business method: whoAmIRemote");
      String s = aRef.whoAmIRemote();
    } catch (Exception e) {
      TestUtil
          .logErr("Caught Unexpected Exception in test9: " + e.getMessage());
    } finally {
      try {
        TestUtil.logMsg("Attempt Removal of bean while participating in TX");
        aRef.remove();
      } catch (RemoveException re) {
        TestUtil.logMsg("RemoveException did occur - expected");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("RemoveException did not occur - unexpected", e);
      }
    }
    return pass;
  }

  public boolean test10() {
    TestUtil.logTrace("test10");
    boolean pass = false;
    try {
      TestUtil.logMsg("Lookup local home of Session Bean (SF) and do create");
      aLocalRef = createALocal("10");
      TestUtil.logMsg("Calling local business method: whoAmILocal");
      String s = aLocalRef.whoAmILocal();
    } catch (Exception e) {
      TestUtil
          .logErr("Caught Unexpected Exception in test10: " + e.getMessage());
    } finally {
      try {
        TestUtil.logMsg("Attempt Removal of bean while participating in TX");
        aLocalHome.remove(aLocalRef);
      } catch (RemoveException re) {
        TestUtil.logMsg("RemoveException did occur - expected");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("RemoveException did not occur - unexpected", e);
      }
    }
    return pass;
  }

  public boolean test11() {
    TestUtil.logTrace("test11");
    boolean pass = false;
    try {
      TestUtil.logMsg("Lookup remote home of Session Bean (SF) and do create");
      aRef = createA("11");
      TestUtil.logMsg("Calling remote business method: whoAmIRemote");
      String s = aRef.whoAmIRemote();
    } catch (Exception e) {
      TestUtil
          .logErr("Caught Unexpected Exception in test11: " + e.getMessage());
    } finally {
      try {
        TestUtil.logMsg("Attempt Removal of bean while participating in TX");
        aHome.remove(aRef);
      } catch (RemoveException re) {
        TestUtil.logMsg("RemoveException did occur - expected");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("RemoveException did not occur - unexpected", e);
      }
    }
    return pass;
  }

  public boolean test12() {
    boolean pass;

    pass = test10();
    pass &= test11();

    return pass;
  }

  public void cleanUpStatefulBean() {
    TestUtil.logTrace("cleanUpStatefulBean");
    try {
      if (aRef != null) {
        TestUtil.logTrace("cleanUp Remote Session Stateful Bean");
        aRef.remove();
        aRef = null;
      }
      if (aLocalRef != null) {
        TestUtil.logTrace("cleanUp Local Session Stateful Bean");
        aLocalRef.remove();
        aLocalRef = null;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception caught trying to remove Stateful Session beans", e);
    }

  }

  // ===========================================================
}
