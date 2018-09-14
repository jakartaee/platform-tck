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
 * @(#)TestBean2EJB.java	1.15 03/05/16
 */
package com.sun.ts.tests.samples.ejb.ee.twobean;

import com.sun.ts.lib.util.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class TestBean2EJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties props = null;

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
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

  // Logging method for TS

  public void initLogging(Properties p) {
    try {
      TestUtil.init(p);
    } catch (Exception e) {
      TestUtil.logErr("init failed", e);
    }
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public void bean2Test() {
    TestUtil.logTrace("bean2Test");
    try {
      TestUtil.logMsg("print this line out: bean2Test ok");
    } catch (Exception e) {
      TestUtil.logErr("bean2Test failed", e);
    }
  }

  // ===========================================================
}
