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

package com.sun.ts.tests.ejb.ee.bb.session.lrapitest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;

public class CEJB implements SessionBean {

  private SessionContext context = null;

  private String whoAmI;

  // ===========================================================
  // C interface business methods

  // This method is only exposed through the Local Interface
  public String whoAmILocal() {
    return "Local-" + whoAmI;
  }

  public boolean test3() {
    TestUtil.logTrace("test3");
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

  // ===========================================================
  // private methods

  private boolean getObjectTest() {
    TestUtil.logTrace("getObjectTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("getEJBObject() - IllegalStateException");
      EJBObject object = context.getEJBObject();
      TestUtil.logErr("getEJBObject() - no IllegalStateException");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logErr("IllegalStateException caught as expected");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean getLocalObjectTest() {
    TestUtil.logTrace("getLocalObjectTest");
    boolean pass = true;
    try {
      EJBLocalObject object = context.getEJBLocalObject();
      if (object != null) {
        TestUtil
            .logMsg("getEJBLocalObject() returned EJBLocalObject reference");
        if (object instanceof CLocal)
          TestUtil.logMsg("An CLocal object");
        else {
          TestUtil.logErr("Not an CLocal object");
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

  // ===========================================================
  // EJB Specification Required Methods

  public void ejbCreateC(String whoami) throws CreateException {
    TestUtil.logTrace("ejbCreateC");
    whoAmI = whoami;
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
