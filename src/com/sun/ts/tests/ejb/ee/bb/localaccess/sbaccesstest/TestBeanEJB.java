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
 * @(#)TestBeanEJB.java	1.9 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.localaccess.sbaccesstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class TestBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  // Entity Bean (CMP) A -> Local Interface Only
  // Session Bean (SF) B -> Local Interface Only
  // Entity Bean (BMP) C -> Local Interface Only
  // Session Bean (SL) D -> Local Interface Only

  // JNDI Names for A, B, C, D Local Home Interface
  private static final String ALocal = "java:comp/env/ejb/AEJBLocal";

  private static final String BLocal = "java:comp/env/ejb/BEJBLocal";

  private static final String CLocal = "java:comp/env/ejb/CEJBLocal";

  private static final String DLocal = "java:comp/env/ejb/DEJBLocal";

  // References to Local Interfaces for Session and Entity Bean A,B,C,D
  private ALocal aLocalRef = null;

  private ALocalHome aLocalHome = null;

  private BLocal bLocalRef = null;

  private BLocalHome bLocalHome = null;

  private CLocal cLocalRef = null;

  private CLocalHome cLocalHome = null;

  private DLocal dLocalRef = null;

  private DLocalHome dLocalHome = null;

  // ===========================================================
  // private methods

  private ALocal createA(int id, String name, int value) throws Exception {
    TestUtil.logTrace("createA");
    aLocalHome = (ALocalHome) nctx.lookup(ALocal);
    aLocalRef = aLocalHome.createA(id, name, value);
    return aLocalRef;
  }

  private BLocal createB() throws Exception {
    TestUtil.logTrace("createB");
    bLocalHome = (BLocalHome) nctx.lookup(BLocal);
    bLocalRef = bLocalHome.createB();
    return bLocalRef;
  }

  private CLocal createC(Properties p, int id, String name, int value)
      throws Exception {
    TestUtil.logTrace("createC");
    cLocalHome = (CLocalHome) nctx.lookup(CLocal);
    cLocalRef = cLocalHome.createC(p, id, name, value);
    return cLocalRef;
  }

  private DLocal createD() throws Exception {
    TestUtil.logTrace("createD");
    dLocalHome = (DLocalHome) nctx.lookup(DLocal);
    dLocalRef = dLocalHome.create();
    return dLocalRef;
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
      throw new CreateException("unable to obtain naming context");
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
    TestUtil.logTrace("test1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Lookup local home of Entity Bean (CMP) and do create");
      aLocalRef = createA(1, "a1", 1);
      String s = aLocalRef.whoAmI();
      TestUtil.logMsg("Calling local business method: " + s);
      if (!s.equals("entity-cmp")) {
        TestUtil.logErr(
            "Wrong string returned: got: " + s + ", expected: entity-cmp");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        aLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean test2() {
    TestUtil.logTrace("test2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Lookup local home of Session Bean (SF) and do create");
      bLocalRef = createB();
      String s = bLocalRef.whoAmI();
      TestUtil.logMsg("Calling local business method: " + s);
      if (!s.equals("session-stateful")) {
        TestUtil.logErr("Wrong string returned: got: " + s
            + ", expected: session-stateful");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean test3() {
    TestUtil.logTrace("test3");
    boolean pass = true;
    try {
      TestUtil.logMsg("Lookup local home of Entity Bean (BMP) and do create");
      cLocalRef = createC(harnessProps, 1, "c1", 1);
      String s = cLocalRef.whoAmI();
      TestUtil.logMsg("Calling local business method: " + s);
      if (!s.equals("entity-bmp")) {
        TestUtil.logErr(
            "Wrong string returned: got: " + s + ", expected: entity-bmp");
        pass = false;
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

  public boolean test4() {
    TestUtil.logTrace("test4");
    boolean pass = true;
    try {
      TestUtil.logMsg("Lookup local home of Session Bean (SL) and do create");
      dLocalRef = createD();
      String s = dLocalRef.whoAmI();
      TestUtil.logMsg("Calling local business method: " + s);
      if (!s.equals("session-stateless")) {
        TestUtil.logErr("Wrong string returned: got: " + s
            + ", expected: session-stateless");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
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

  public void cleanUpStatefulBean() {
    TestUtil.logTrace("cleanUpStatefulBean");
    try {
      if (bLocalRef != null) {
        bLocalRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logTrace("Exception caught trying to remove bLocalRef");
      throw new EJBException(e.getMessage());
    }
  }

  // ===========================================================
}
