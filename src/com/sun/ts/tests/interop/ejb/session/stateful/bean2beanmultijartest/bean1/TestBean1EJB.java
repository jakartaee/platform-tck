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

package com.sun.ts.tests.interop.ejb.session.stateful.bean2beanmultijartest.bean1;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.interop.ejb.session.stateful.bean2beanmultijartest.bean2.*;
import java.util.*;
import java.rmi.*;
import java.io.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;

public class TestBean1EJB implements SessionBean {
  private static final String MyString = "message from bean1";

  private Handle MyHandle = null;

  private HomeHandle MyHomeHandle = null;

  private EJBMetaData MyEJBMetaData = null;

  private EJBObject myEJBObject = null;

  private EJBHome myEJBHome = null;

  private static final String ExpectedString = "message from bean2";

  private TestBean1 bean1Ref = null;

  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private TestBean2Home bean2Home = null;

  private TestBean2 bean2Ref = null;

  private TSNamingContext nctx = null;

  private static final String testBean2 = "java:comp/env/ejb/TestBean2";

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
      bean2Home = (TestBean2Home) nctx.lookup(testBean2, TestBean2Home.class);
      bean2Ref = (TestBean2) bean2Home.create(harnessProps);

    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception in ejbCreate", e);
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
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

  private void getInfo() throws EJBException {
    try {
      myEJBObject = sctx.getEJBObject();
      myEJBHome = myEJBObject.getEJBHome();
      MyHandle = myEJBObject.getHandle();
      MyHomeHandle = myEJBHome.getHomeHandle();
      MyEJBMetaData = myEJBHome.getEJBMetaData();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("EJBException: " + e);
    }
  }

  // ===========================================================
  // TestBean1 interface (our business methods)

  public String whoAmI() {
    return (MyString);
  }

  public boolean returnBean2String() {
    boolean pass = false;

    try {
      TestUtil.logTrace("returnBean2String");
      String s = bean2Ref.returnBean2String();
      if (s == null) {
        TestUtil.logErr("String returned is null");
      } else if (s.equals(ExpectedString)) {
        TestUtil.logMsg("String returned in returnBean2String is correct");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil
          .logErr("Caught exception in returnBean2String: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return pass;

  }

  public boolean returnBean2Handle() {
    EJBObject ejbObject;
    TestBean2 bRef2 = null;
    boolean pass = false;

    try {
      TestUtil.logTrace("returnBean2Handle");
      Handle v = bean2Ref.returnBean2Handle();
      if (v == null) {
        TestUtil.logErr("Handle returned is null");
      } else if (!(v instanceof Serializable)) {
        TestUtil.logErr("Handle returned is not serializable");
      } else {
        TestUtil.logMsg("Convert Handle to appropriate bean reference");
        ejbObject = v.getEJBObject();
        bRef2 = (TestBean2) PortableRemoteObject.narrow(ejbObject,
            TestBean2.class);
        TestUtil.logMsg("Check if we have correct bean reference");
        String whoami = bRef2.whoAmI();
        if (whoami.equals(ExpectedString)) {
          pass = true;
          TestUtil.logMsg("Correct bean reference (thus correct Handle)");
        }
      }
    } catch (Exception e) {
      TestUtil
          .logErr("Caught exception in returnBean2Handle: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (bRef2 != null) {
          bRef2.remove();
        }
      } catch (Exception e) {
        TestUtil
            .logTrace("Caught exception removing bRef2 in returnBean2Handle: "
                + e.getMessage());
      }
    }
    return pass;
  }

  public boolean returnBean2HomeHandle() {
    boolean pass = false;
    TestBean2Home bean2Home = null;
    EJBHome ejbHome;

    try {
      TestUtil.logTrace("returnBean2HomeHandle");
      HomeHandle v = bean2Ref.returnBean2HomeHandle();
      if (v == null) {
        TestUtil.logErr("HomeHandle returned is null");
      } else if (!(v instanceof Serializable)) {
        TestUtil.logErr("HomeHandle returned is not serializable");
      } else {
        TestUtil.logMsg(
            "Get HomeHandle and narrow to appropriate " + "beanhome reference");
        ejbHome = v.getEJBHome();
        bean2Home = (TestBean2Home) PortableRemoteObject.narrow(ejbHome,
            TestBean2Home.class);
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Caught exception in returnBean2HomeHandle: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return pass;
  }

  public boolean returnBean2EJBMetaData() {
    boolean pass = false;
    TestBean2Home bean2Home = null;

    try {
      TestUtil.logTrace("returnBean2EJBMetaData");
      EJBMetaData v = bean2Ref.returnBean2EJBMetaData();
      if (v == null) {
        TestUtil.logErr("EJBMetaData returned is null");
      } else if (!(v instanceof Serializable)) {
        TestUtil.logErr("EJBMetaData returned is not serializable");
      } else {
        TestUtil.logMsg("Get the home interface class from EJBMetaData");
        Class cls = v.getHomeInterfaceClass();
        String clsName = cls.getName();
        TestUtil.logMsg("The class name = " + clsName);
        if (clsName.endsWith("TestBean2Home")) {
          pass = true;
          TestUtil.logMsg("Correct beanhome (thus correct EJBMetaData)");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return pass;
  }

  public boolean passBean1String() {
    try {
      TestUtil.logTrace("passBean1String");
      return bean2Ref.passBean1String(MyString);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Caught Exception:" + e.getMessage());
    }
  }

  public boolean passBean1Handle() {
    try {
      TestUtil.logTrace("passBean1Handle");
      getInfo();
      return bean2Ref.passBean1Handle(MyHandle);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Caught Exception:" + e.getMessage());
    }
  }

  public boolean passBean1HomeHandle() {
    try {
      TestUtil.logTrace("passBean1HomeHandle");
      getInfo();
      return bean2Ref.passBean1HomeHandle(MyHomeHandle);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Caught Exception:" + e.getMessage());
    }
  }

  public boolean passBean1EJBMetaData() {
    try {
      TestUtil.logTrace("passBean1EJBMetaData");
      getInfo();
      return bean2Ref.passBean1EJBMetaData(MyEJBMetaData);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Caught Exception:" + e.getMessage());
    }
  }

  // ===========================================================
}
