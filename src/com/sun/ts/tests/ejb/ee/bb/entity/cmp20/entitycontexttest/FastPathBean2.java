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
import javax.rmi.PortableRemoteObject;

public class FastPathBean2 implements SessionBean {
  private SessionContext sctx = null;

  public FastPathBean2() {
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
    TestBeanLocal testBeanLocal = null;
    try {
      testBeanLocal = getTestBeanLocal("FastPathBean2-getIt");
      result = testBeanLocal.getIt(envEntryName);
    } catch (CreateException e) {
      result = TestUtil.printStackTraceToString(e);
    } catch (NamingException e) {
      result = TestUtil.printStackTraceToString(e);
    } finally {
      try {
        if (testBeanLocal != null) {
          testBeanLocal.remove();
        }
      } catch (Exception ignore) {
      }
    }
    return result;
  }

  public String setIt(String envEntryName) {
    String result = null;
    TestBeanLocal testBeanLocal = null;
    try {
      testBeanLocal = getTestBeanLocal("FastPathBean2-setIt");
      result = testBeanLocal.setIt(envEntryName);
    } catch (CreateException e) {
      result = TestUtil.printStackTraceToString(e);
    } catch (NamingException e) {
      result = TestUtil.printStackTraceToString(e);
    } finally {
      try {
        if (testBeanLocal != null) {
          testBeanLocal.remove();
        }
      } catch (Exception ignore) {
      }
    }
    return result;
  }

  public String getCoffeeId(String coffeeId) {
    String result = null;
    TestBeanLocal testBeanLocal = null;
    try {
      testBeanLocal = getTestBeanLocal(coffeeId);
      result = testBeanLocal.getId();
    } catch (CreateException e) {
      result = TestUtil.printStackTraceToString(e);
    } catch (NamingException e) {
      result = TestUtil.printStackTraceToString(e);
    } finally {
      try {
        if (testBeanLocal != null) {
          testBeanLocal.remove();
        }
      } catch (Exception ignore) {
      }
    }
    return result;
  }

  public String setCoffeeBrand(String oldBrand, String newBrand) {
    String result = null;
    TestBeanLocal testBeanLocal = null;
    try {
      testBeanLocal = getTestBeanLocal(oldBrand);
      testBeanLocal.setBrandName(newBrand);
      result = testBeanLocal.getBrandName();
    } catch (CreateException e) {
      result = TestUtil.printStackTraceToString(e);
    } catch (NamingException e) {
      result = TestUtil.printStackTraceToString(e);
    } finally {
      try {
        if (testBeanLocal != null) {
          testBeanLocal.remove();
        }
      } catch (Exception ignore) {
      }
    }
    return result;
  }

  private TestBeanLocal getTestBeanLocal(String coffeeId)
      throws NamingException, CreateException {
    TestBeanLocalHome testBeanLocalHome = null;
    TestBeanLocal testBeanLocal = null;
    InitialContext ic = new InitialContext();
    Object obj = ic.lookup("java:comp/env/ejb/TestBean");
    testBeanLocalHome = (TestBeanLocalHome) (obj);
    testBeanLocal = testBeanLocalHome.create(coffeeId, coffeeId);
    return testBeanLocal;
  }
}
