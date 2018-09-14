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

package com.sun.ts.tests.ejb30.common.sessioncontext;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import static com.sun.ts.tests.ejb30.common.sessioncontext.Constants.FROM_BEAN;
import static com.sun.ts.tests.ejb30.common.sessioncontext.Constants.FROM_CLIENT;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import org.omg.CORBA.ORB;

abstract public class TestBeanBase implements TestIF {
  abstract protected SessionContext getSessionContext();

  abstract protected ThreeLocal1IF getLocal1();

  abstract protected ThreeLocal2IF getLocal2();

  abstract protected TwoLocalIF getTwoLocal() throws TestFailedException;

  abstract protected AcceptLocalIF getAcceptLocalBean();

  @Resource(name = "null")
  private ORB orb;

  public void getBusinessObjectLocal1() throws TestFailedException {
    ThreeLocal1IF local = getLocal1();
    try {
      ThreeLocal1IF bob = local.getBusinessObjectLocal1();
    } finally {
      // The bean local has REQUIRES_NEW, so it's safe to remove inside
      // the calling bean.
      try {
        local.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  public void getInvokedBusinessInterfaceLocal1() throws TestFailedException {
    ThreeLocal1IF local = getLocal1();
    String expected = ThreeLocal1IF.class.getName();
    String actual = null;
    try {
      Class intf = local.getInvokedBusinessInterfaceLocal1();
      actual = intf.getName();
      if (expected.equals(actual)) {
        // good
      } else {
        throw new TestFailedException("Expecting " + expected
            + " from calling getInvokedBusinessInterface(), but actual:"
            + actual + ". Bean instance: " + local);
      }
    } finally {
      // The bean local has REQUIRES_NEW, so it's safe to remove inside
      // the calling bean.
      try {
        local.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  public void getBusinessObjectLocal1Illegal() throws TestFailedException {
    ThreeLocal1IF local = getLocal1();
    try {
      local.getBusinessObjectLocal1Illegal();
    } finally {
      // The bean local has REQUIRES_NEW, so it's safe to remove inside
      // the calling bean.
      try {
        local.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  public void getBusinessObjectLocal2() throws TestFailedException {
    ThreeLocal2IF local = getLocal2();
    try {
      ThreeLocal2IF bob = local.getBusinessObjectLocal2();
    } finally {
      try {
        local.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  public void getInvokedBusinessInterfaceLocal2() throws TestFailedException {
    ThreeLocal2IF local = getLocal2();
    String expected = ThreeLocal2IF.class.getName();
    String actual = null;
    try {
      Class intf = local.getInvokedBusinessInterfaceLocal2();
      actual = intf.getName();
      if (expected.equals(actual)) {
        // good
      } else {
        throw new TestFailedException("Expecting " + expected
            + " from calling getInvokedBusinessInterface(), but actual:"
            + actual + ". bean instance: " + local);
      }
    } finally {
      // The bean local has REQUIRES_NEW, so it's safe to remove inside
      // the calling bean.
      try {
        local.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  public void passBusinessObjectLocal2() throws TestFailedException {
    ThreeLocal2IF local = getLocal2();
    AcceptLocalIF accept = getAcceptLocalBean();
    try {
      ThreeLocal2IF bob = local.getBusinessObjectLocal2();
      accept.accept(bob);
    } finally {
      try {
        local.remove();
      } catch (Exception e) {
        // ignore
      }
      try {
        accept.remove();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void passBusinessObjectLocal1() throws TestFailedException {
    ThreeLocal1IF local = getLocal1();
    AcceptLocalIF accept = getAcceptLocalBean();
    try {
      ThreeLocal1IF bob = local.getBusinessObjectLocal1();
      accept.accept(bob);
    } finally {
      try {
        local.remove();
      } catch (Exception e) {
        // ignore
      }
      try {
        accept.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  public void lookupIllegalArgumentException() throws TestFailedException {
    String lookupName = null;
    try {
      Object obj = getSessionContext().lookup(lookupName);
      throw new TestFailedException("Expecting IllegalArgumentException"
          + " when looking up non-existent resource: " + lookupName
          + ", but the lookup result is: " + obj);
    } catch (IllegalArgumentException e) {
      TLogger.log("Got expected IllegalArgumentException when looking up "
          + "a non-existent name: " + lookupName);
    }
    try {
      lookupName = "no" + System.currentTimeMillis();
      Object obj = getSessionContext().lookup(lookupName);
      throw new TestFailedException("Expecting IllegalArgumentException"
          + " when looking up non-existent resource:" + lookupName
          + ", but the lookup result is: " + obj);
    } catch (IllegalArgumentException e) {
      TLogger.log("Got expected IllegalArgumentException when looking up "
          + "a non-existent name: " + lookupName);
    }
  }

  public void getInvokedBusinessInterfaceLocalIllegal()
      throws TestFailedException {
    TwoLocalIF twoSessionContextBean = getTwoLocal();
    try {
      twoSessionContextBean.getInvokedBusinessInterfaceLocalIllegal();
    } catch (EJBException e) {
      throw new TestFailedException(e);
    }
  }
}
