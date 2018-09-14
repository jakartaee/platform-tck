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

package com.sun.ts.tests.rmiiiop.ee.orbtests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.rmi.PortableRemoteObject;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;

public class TestBeanEJB implements SessionBean {
  private static final String orbLookup = "java:comp/ORB";

  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private HelloRMIIIOPObjectIntf rmiiiopRef = null;

  private org.omg.CORBA.ORB orb = null;

  private org.omg.CORBA.Object obj = null;

  private TSNamingContext nctx = null;

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      nctx = new TSNamingContext();
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
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

  public String hello() {
    return "Hello from TestBeanEJB";
  }

  public boolean test1(String ior) {
    boolean pass = true;
    String expStr = "Hello from HelloRMIIIOPObjectImpl";

    try {
      TestUtil.logTrace("test1");
      TestUtil.logMsg("Create an ORB instance using [ORB.init()]");
      orb = ORB.init(new String[0], null);
      TestUtil.logMsg("ORB = " + orb);
      if (orb == null) {
        TestUtil.logErr("Unable to create an ORB instance using [ORB.init()]");
        pass = false;
      } else {
        TestUtil.logMsg("Verify some basic ORB functionality");
        TestUtil.logMsg("IOR = " + ior);
        TestUtil.logMsg("Convert stringified IOR to a CORBA object");
        obj = orb.string_to_object(ior);
        TestUtil
            .logMsg("Narrow CORBA object to interface HelloRMIIIOPObjectIntf");
        rmiiiopRef = (HelloRMIIIOPObjectIntf) PortableRemoteObject.narrow(obj,
            HelloRMIIIOPObjectIntf.class);
        TestUtil
            .logMsg("Call hello method on interface HelloRMIIIOPObjectIntf");
        String hello = rmiiiopRef.hello();
        TestUtil.logMsg("Verify the method call");
        if (!hello.equals(expStr)) {
          TestUtil.logErr("Wrong message, got [" + hello + "]");
          TestUtil.logErr("Wrong message, expected [" + expStr + "]");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    return pass;
  }

  public boolean test2(String ior) {
    boolean pass = true;
    String expStr = "Hello from HelloRMIIIOPObjectImpl";

    try {
      TestUtil.logTrace("test2");
      TestUtil.logMsg(
          "Lookup ORB instance in JNDI namespace under [java:comp/ORB]");
      try {
        orb = (ORB) nctx.lookup(orbLookup);
        TestUtil.logMsg("ORB = " + orb);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        orb = null;
      }
      if (orb == null) {
        TestUtil.logErr("Unable to lookup ORB instance in JNDI namespace");
        pass = false;
      } else {
        TestUtil.logMsg("Verify some basic ORB functionality");
        TestUtil.logMsg("IOR = " + ior);
        TestUtil.logMsg("Convert stringified IOR to a CORBA object");
        obj = orb.string_to_object(ior);
        TestUtil
            .logMsg("Narrow CORBA object to interface HelloRMIIIOPObjectIntf");
        rmiiiopRef = (HelloRMIIIOPObjectIntf) PortableRemoteObject.narrow(obj,
            HelloRMIIIOPObjectIntf.class);
        TestUtil
            .logMsg("Call hello method on interface HelloRMIIIOPObjectIntf");
        String hello = rmiiiopRef.hello();
        TestUtil.logMsg("Verify the method call");
        if (!hello.equals(expStr)) {
          TestUtil.logErr("Wrong message, got [" + hello + "]");
          TestUtil.logErr("Wrong message, expected [" + expStr + "]");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    return pass;
  }

  // ===========================================================
}
