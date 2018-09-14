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
 * @(#)TestBeanEJB.java	1.12 03/05/16
 */

package com.sun.ts.tests.rmiiiop.ee.objecttests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class TestBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("Initialize remote logging");
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

  // ===========================================================
  // TestBean interface (our business methods)

  public boolean pass_a_rmiiiop_object(HelloRMIIIOPObjectIntf p1) {
    String methodName = "pass_a_rmiiiop_object";
    String expStr = "Hello from HelloRMIIIOPObjectImpl";

    TestUtil.logTrace(methodName);
    boolean pass = true;
    if (p1 == null) {
      TestUtil.logErr("Could not receive a rmiiiop object (p1 is null)");
      pass = false;
    } else {
      try {
        TestUtil.logMsg("Invoke methods on the rmiiiop object reference");
        String hello = p1.hello();
        if (!hello.equals(expStr)) {
          TestUtil.logErr("Wrong message, got [" + hello + "]");
          TestUtil.logErr("Wrong message, expected [" + expStr + "]");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Unable to invoke the methods of the rmiiiop object");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        pass = false;
      }
    }
    return pass;
  }

  public HelloRMIIIOPObjectIntf return_a_rmiiiop_object(
      HelloRMIIIOPObjectIntf p1) {
    String methodName = "return_a_rmiiiop_object";
    TestUtil.logTrace(methodName);
    return p1;
  }

  public boolean pass_a_javaidl_object(HelloJAVAIDLObjectIntf p1) {
    boolean pass = false;
    String expStr = "Hello from HelloJAVAIDLObjectImpl";
    String hello;

    try {
      TestUtil.logTrace("[TestBeanEJB] pass_a_javaidl_object");

      if (null == p1) {
        TestUtil.logErr("Could not receive a javaidl object (p1 is null)");
        return false;
      }

      TestUtil.logMsg("Invoke methods on the javaidl object reference");
      hello = p1.hello();
      if (!hello.equals(expStr)) {
        TestUtil.logErr("Wrong message, got [" + hello + "], " + "expected ["
            + expStr + "]");
        return false;
      }
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unable to invoke the methods of the javaidl object", e);
    }

    return pass;
  }

  public HelloJAVAIDLObjectIntf return_a_javaidl_object(
      HelloJAVAIDLObjectIntf p1) {

    TestUtil.logTrace("[TestBeanEJB] return_a_javaidl_object()");
    return p1;
  }

  public String hello() {
    return "Hello from TestBeanEJB";
  }

  // ===========================================================
}
