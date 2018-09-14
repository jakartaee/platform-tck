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

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitycontexttest;

import com.sun.ts.lib.util.TestUtil;
import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FastPathBean implements SessionBean {
  private SessionContext sctx = null;

  public FastPathBean() {
  }

  public void ejbCreate() throws CreateException {
  }

  public void setSessionContext(SessionContext sc) {
    this.sctx = sc;
  }

  public void ejbRemove() {
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }

  public String getIt(String envEntryName) {
    String result = null;
    FastPathLocal fastPathBean2Local = null;
    try {
      fastPathBean2Local = getFastPathBean2Local();
      result = fastPathBean2Local.getIt(envEntryName);
    } catch (CreateException e) {
      result = TestUtil.printStackTraceToString(e);
    } catch (NamingException e) {
      result = TestUtil.printStackTraceToString(e);
    }
    return result;
  }

  public String setIt(String envEntryName) {
    String result = null;
    FastPathLocal fastPathBean2Local = null;
    try {
      fastPathBean2Local = getFastPathBean2Local();
      result = fastPathBean2Local.setIt(envEntryName);
    } catch (CreateException e) {
      result = TestUtil.printStackTraceToString(e);
    } catch (NamingException e) {
      result = TestUtil.printStackTraceToString(e);
    }
    return result;
  }

  public String getCoffeeId(String coffeeId) {
    String result = null;
    FastPathLocal fastPathBean2Local = null;
    try {
      fastPathBean2Local = getFastPathBean2Local();
      result = fastPathBean2Local.getCoffeeId(coffeeId);
    } catch (CreateException e) {
      result = TestUtil.printStackTraceToString(e);
    } catch (NamingException e) {
      result = TestUtil.printStackTraceToString(e);
    }
    return result;
  }

  public String setCoffeeBrand(String oldBrand, String newBrand) {
    String result = null;
    FastPathLocal fastPathBean2Local = null;
    try {
      fastPathBean2Local = getFastPathBean2Local();
      result = fastPathBean2Local.setCoffeeBrand(oldBrand, newBrand);
    } catch (CreateException e) {
      result = TestUtil.printStackTraceToString(e);
    } catch (NamingException e) {
      result = TestUtil.printStackTraceToString(e);
    }
    return result;
  }

  private FastPathLocal getFastPathBean2Local()
      throws CreateException, NamingException {
    InitialContext ic = new InitialContext();
    FastPathLocalHome fastPathLocalHome = (FastPathLocalHome) ic
        .lookup("java:comp/env/ejb/FastPathBean2");
    FastPathLocal fastPathLocal = fastPathLocalHome.create();
    return fastPathLocal;
  }
}
