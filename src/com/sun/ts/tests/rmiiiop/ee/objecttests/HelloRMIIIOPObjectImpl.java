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

package com.sun.ts.tests.rmiiiop.ee.objecttests;

import com.sun.ts.lib.util.*;

import java.rmi.*;
import javax.rmi.PortableRemoteObject;
import javax.ejb.*;
import java.util.*;
import java.io.*;

public class HelloRMIIIOPObjectImpl extends PortableRemoteObject
    implements HelloRMIIIOPObjectIntf, Serializable {

  private Properties saveProps = null;

  public HelloRMIIIOPObjectImpl() throws RemoteException {
    super();
  }

  public String hello() throws RemoteException {
    return "Hello from HelloRMIIIOPObjectImpl";
  }

  public void passProperties(Properties p) throws RemoteException {
    saveProps = p;
  }

  public boolean passEjbHome(TestBeanHome homeRef) throws RemoteException {
    String expStr = "Hello from TestBeanEJB";
    TestBean beanRef = null;

    TestUtil.logTrace("passEjbHome");
    boolean pass = true;
    try {
      if (homeRef == null) {
        TestUtil.logErr("Could not receive EJBHome reference (ref is null)");
        pass = false;
      } else {
        try {
          beanRef = homeRef.create(saveProps);
          String hello = beanRef.hello();
          if (!hello.equals(expStr)) {
            TestUtil.logErr("Wrong message, got [" + hello + "]");
            TestUtil.logErr("Wrong message, expected [" + expStr + "]");
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logErr("Unable to invoke the methods of the EJBObject");
          TestUtil.printStackTrace(e);
          TestUtil.logErr(e.toString());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean passEjbObject(TestBean remoteRef) throws RemoteException {
    String expStr = "Hello from TestBeanEJB";

    TestUtil.logTrace("passEjbObject");
    boolean pass = true;
    try {
      if (remoteRef == null) {
        TestUtil.logErr("Could not receive EJBObject reference (ref is null)");
        pass = false;
      } else {
        try {
          String hello = remoteRef.hello();
          if (!hello.equals(expStr)) {
            TestUtil.logErr("Wrong message, got [" + hello + "]");
            TestUtil.logErr("Wrong message, expected [" + expStr + "]");
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logErr("Unable to invoke the methods of the EJBObject");
          TestUtil.printStackTrace(e);
          TestUtil.logErr(e.toString());
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

  public TestBeanHome returnEjbHome(TestBeanHome homeRef)
      throws RemoteException {
    TestUtil.logTrace("returnEjbHome");
    return (homeRef);
  }

  public TestBean returnEjbObject(TestBean remoteRef) throws RemoteException {
    TestUtil.logTrace("returnEjbObject");
    return (remoteRef);
  }
}
