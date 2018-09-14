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
 * @(#)TestBean2EJB.java	1.5 03/05/16
 */

package com.sun.ts.tests.interop.ejb.session.stateful.bean2beanmultijartest.bean2;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.interop.ejb.session.stateful.bean2beanmultijartest.bean1.*;
import java.io.*;
import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;

public class TestBean2EJB implements SessionBean {
  private static final String MyString = "message from bean2";

  private Handle MyHandle = null;

  private HomeHandle MyHomeHandle = null;

  private EJBMetaData MyEJBMetaData = null;

  private EJBObject myEJBObject = null;

  private EJBHome myEJBHome = null;

  private static final String ExpectedString = "message from bean1";

  private SessionContext sctx = null;

  private Properties harnessProps = null;

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
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
  // TestBean2 interface (our business methods)

  public String whoAmI() {
    return (MyString);
  }

  public boolean passBean1String(String s) {
    TestUtil.logTrace("passBean1String");
    TestUtil.logMsg("received string: " + s);
    if (s == null) {
      TestUtil.logErr("String passed in is null");
      return false;
    } else if (s.equals(ExpectedString)) {
      TestUtil.logMsg("String passed in is correct");
      return true;
    } else {
      TestUtil.logErr("String passed in is not correct");
      return false;
    }
  }

  public boolean passBean1Handle(Handle v) {
    boolean pass = false;
    EJBObject ejbObject;
    TestBean1 bean1Ref;

    try {
      TestUtil.logTrace("passBean1Handle");
      TestUtil.logMsg("received Handle: " + v);
      if (v == null) {
        TestUtil.logErr("Handle passed in is null");
      } else if (!(v instanceof Serializable)) {
        TestUtil.logErr("Handle passed in is not serializable");
      } else {
        TestUtil.logMsg("Convert Handle to appropriate bean reference");
        ejbObject = v.getEJBObject();
        bean1Ref = (TestBean1) PortableRemoteObject.narrow(ejbObject,
            TestBean1.class);
        TestUtil.logMsg("Check if we have correct bean reference");
        if ((bean1Ref instanceof TestBean1)) {
          pass = true;
          TestUtil.logMsg("Correct bean reference (thus correct Handle)");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return pass;
  }

  public boolean passBean1HomeHandle(HomeHandle v) {
    boolean pass = false;
    TestBean1Home bean1Home = null;
    EJBHome ejbHome;

    try {
      TestUtil.logTrace("passBean1HomeHandle");
      TestUtil.logMsg("received HomeHandle: " + v);
      if (v == null) {
        TestUtil.logErr("HomeHandle passed in is null");
      } else if (!(v instanceof Serializable)) {
        TestUtil.logErr("HomeHandle passed in is not serializable");
      } else {
        TestUtil.logMsg("Get HomeHandle & narrow to TestBean1Home");
        ejbHome = v.getEJBHome();
        bean1Home = (TestBean1Home) PortableRemoteObject.narrow(ejbHome,
            TestBean1Home.class);
        pass = true;
      }
    } catch (Exception e) {
      TestUtil
          .logErr("Caught exception in passBean1HomeHandle: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return pass;
  }

  public boolean passBean1EJBMetaData(EJBMetaData v) {
    boolean pass = false;
    TestBean1Home bean1Home = null;

    try {
      TestUtil.logTrace("passBean1EJBMetaData");
      if (v == null) {
        TestUtil.logErr("EJBMetaData returned is null");
      } else if (!(v instanceof Serializable)) {
        TestUtil.logErr("EJBMetaData returned is not serializable");
      } else {
        TestUtil.logMsg("Get the home interface class from EJBMetaData");
        Class cls = v.getHomeInterfaceClass();
        String clsName = cls.getName();
        TestUtil.logMsg("The class name = " + clsName);
        if (clsName.endsWith("TestBean1Home")) {
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

  public String returnBean2String() {
    TestUtil.logTrace("returnBean2String");
    return MyString;
  }

  public Handle returnBean2Handle() {
    TestUtil.logTrace("returnBean2Handle");
    getInfo();
    return MyHandle;
  }

  public HomeHandle returnBean2HomeHandle() {
    TestUtil.logTrace("returnBean2HomeHandle");
    getInfo();
    return MyHomeHandle;
  }

  public EJBMetaData returnBean2EJBMetaData() {
    TestUtil.logTrace("returnBean2EJBMetaData");
    getInfo();
    return MyEJBMetaData;
  }

  // ===========================================================
}
