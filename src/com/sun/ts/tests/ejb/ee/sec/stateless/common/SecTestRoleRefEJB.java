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

// The purpose of this EJB is to test scoping of security role references.

package com.sun.ts.tests.ejb.ee.sec.stateless.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class SecTestRoleRefEJB implements SessionBean {

  private SessionContext sctx = null;

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("SecTestRoleRefEJB ejbCreate OK!");
  }

  public void initLogging(java.util.Properties p) {
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.logErr("SecTestRoleRefEJB initLogging failed.", e);
    }
  }

  public boolean EjbSecRoleRefScope(String role) {
    return sctx.isCallerInRole(role);
  }

  public void setSessionContext(SessionContext sc) {
    sctx = sc;
  }

  public void ejbRemove() {
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }

}
