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
 * @(#)SecTestEJB.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.stateless.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class SecTestEJB implements SessionBean {

  private SessionContext sctx = null;

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
  }

  public void initLogging(java.util.Properties p) {
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("SecTestEJB initLogging failed.");
    }
  }

  public boolean IsCaller(String caller) {
    if (sctx.getCallerPrincipal().getName().indexOf(caller) < 0)
      return false;
    else
      return true;
  }

  public boolean EjbNotAuthz() {
    return true;
  }

  public boolean EjbIsAuthz() {
    return true;
  }

  public boolean EjbSecRoleRef(String role) {
    return sctx.isCallerInRole(role);
  }

  public boolean EjbOverloadedSecRoleRefs(String role1) {
    TestUtil.logMsg(
        "isCallerInRole(" + role1 + ") = " + sctx.isCallerInRole(role1));
    return sctx.isCallerInRole(role1);
  }

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2) {
    TestUtil
        .logMsg("isCallerInRole(" + role1 + ")= " + sctx.isCallerInRole(role1)
            + "isCallerInRole(" + role2 + ")= " + sctx.isCallerInRole(role2));
    return sctx.isCallerInRole(role1) && sctx.isCallerInRole(role2);
  }

  public boolean checktest1() {
    TestUtil.logTrace("In checktest1!");
    return true;
  }

  public boolean excludetest1() {
    return true;
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
