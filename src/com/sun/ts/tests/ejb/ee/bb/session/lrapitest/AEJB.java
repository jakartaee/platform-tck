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
 * @(#)AEJB.java	1.6 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.lrapitest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;

public class AEJB implements SessionBean {

  private SessionContext context = null;

  private Properties harnessProps;

  private String whoami;

  // ===========================================================
  // A interface business methods

  public void init(Properties p) {
    TestUtil.logTrace("init");
    harnessProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  // This method is only exposed through the Local Interface
  public String whoAmILocal() {
    return "Local-" + whoami;
  }

  // This method is only exposed through the Remote Interface
  public String whoAmIRemote() {
    return "Remote-" + whoami;
  }

  public boolean test1() {
    TestUtil.logTrace("test1");
    boolean pass = true;
    if (getObjectTest())
      TestUtil.logMsg("getEJBObject ... PASSED");
    else {
      TestUtil.logMsg("getEJBObject ... FAILED");
      pass = false;
    }
    if (getLocalObjectTest())
      TestUtil.logMsg("getEJBLocalObject ... PASSED");
    else {
      TestUtil.logMsg("getEJBLocalObject ... FAILED");
      pass = false;
    }
    return pass;
  }

  public EJBObject getRemoteRef() {
    TestUtil.logTrace("getRemoteRef");
    try {
      return context.getEJBObject();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return null;
    }
  }

  public EJBLocalObject getLocalRef() {
    TestUtil.logTrace("getLocalRef");
    try {
      return context.getEJBLocalObject();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return null;
    }
  }

  // ===========================================================
  // private methods

  private boolean getLocalObjectTest() {
    TestUtil.logTrace("getLocalObjectTest");
    boolean pass = true;
    try {
      EJBLocalObject object = context.getEJBLocalObject();
      if (object != null) {
        TestUtil
            .logMsg("getEJBLocalObject() returned EJBLocalObject reference");
        if (object instanceof ALocal)
          TestUtil.logMsg("An ALocal object");
        else {
          TestUtil.logErr("Not an ALocal object");
          pass = false;
        }
      } else {
        TestUtil.logErr("getEJBLocalObject() returned null reference");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean getObjectTest() {
    TestUtil.logTrace("getObjectTest");
    boolean pass = true;
    try {
      EJBObject object = context.getEJBObject();
      if (object != null) {
        TestUtil.logMsg("getEJBObject() returned EJBObject reference");
        if (object instanceof A)
          TestUtil.logMsg("An A object");
        else {
          TestUtil.logErr("Not an A object");
          pass = false;
        }
      } else {
        TestUtil.logErr("getEJBObject() returned null reference");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  // ===========================================================
  // EJB Specification Required Methods

  public void ejbCreateA(String whoAmI) throws CreateException {
    TestUtil.logTrace("ejbCreateA");
    whoami = whoAmI;
  }

  public void setSessionContext(SessionContext c) {
    TestUtil.logTrace("setSessionContext");
    context = c;
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
}
