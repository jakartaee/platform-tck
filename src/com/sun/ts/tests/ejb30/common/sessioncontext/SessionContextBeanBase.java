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
import javax.ejb.EJBException;
import javax.ejb.SessionContext;

abstract public class SessionContextBeanBase
// implements Three1IF, Three2IF
{
  abstract protected SessionContext getSessionContext();

  //////////////////////////////////////////////////////////////////////
  // Three1IF, Three2IF methods
  //////////////////////////////////////////////////////////////////////
  public void hi(String[] m) {
    m[0] = FROM_BEAN;
  }

  public Three1IF getBusinessObjectRemote1() throws TestFailedException {
    Object obj = getSessionContext().getBusinessObject(Three1IF.class);
    Three1IF bob = (Three1IF) obj;
    return bob;
  }

  public Three2IF getBusinessObjectRemote2() throws TestFailedException {
    Object obj = getSessionContext().getBusinessObject(Three2IF.class);
    Three2IF bob = (Three2IF) obj;
    return bob;
  }

  public ThreeLocal1IF getBusinessObjectLocal1() throws TestFailedException {
    Object obj = getSessionContext().getBusinessObject(ThreeLocal1IF.class);
    ThreeLocal1IF bob = (ThreeLocal1IF) obj;
    return bob;
  }

  public ThreeLocal2IF getBusinessObjectLocal2() throws TestFailedException {
    Object obj = getSessionContext().getBusinessObject(ThreeLocal2IF.class);
    ThreeLocal2IF bob = (ThreeLocal2IF) obj;
    return bob;
  }

  public void getBusinessObjectRemote1Illegal() throws TestFailedException {
    getBusinessObject1Illegal();
  }

  //////////////////////////////////////////////////////////////////////
  // ThreeLocal1IF methods
  //////////////////////////////////////////////////////////////////////
  public void getBusinessObjectLocal1Illegal() throws TestFailedException {
    getBusinessObject1Illegal();
  }

  public Class getInvokedBusinessInterfaceRemote1() throws TestFailedException {
    return getInvokedBusinessInterface();
  }

  public Class getInvokedBusinessInterfaceRemote2() throws TestFailedException {
    return getInvokedBusinessInterface();
  }

  public Class getInvokedBusinessInterfaceLocal1() throws TestFailedException {
    return getInvokedBusinessInterface();
  }

  public Class getInvokedBusinessInterfaceLocal2() throws TestFailedException {
    return getInvokedBusinessInterface();
  }

  //////////////////////////////////////////////////////////////////////
  // TwoRemoteIF methods
  //////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////
  // TwoLocalIF methods
  //////////////////////////////////////////////////////////////////////
  public void noop() {
  }

  public void getInvokedBusinessInterfaceRemoteIllegal() {
    Class intf = null;
    try {
      intf = getSessionContext().getInvokedBusinessInterface();
      throw new EJBException("Expecting IllegalStateException when calling "
          + "SessionContext.getInvokedBusinessInterface() through "
          + "component interface, but the actual result is " + intf);
    } catch (IllegalStateException e) {
      // good
      TLogger.log("Got expected IllegalStateException when calling "
          + "SessionContext.getInvokedBusinessInterface() through "
          + "component interface.");
    }
  }

  public void getInvokedBusinessInterfaceLocalIllegal() {
    // same as the remote case
    getInvokedBusinessInterfaceRemoteIllegal();
  }

  //////////////////////////////////////////////////////////////////////
  private void getBusinessObject1Illegal() throws TestFailedException {
    try {
      getSessionContext().getBusinessObject(Object.class);
      throw new TestFailedException("Expecting IllegalStateException when "
          + "calling SessionContext.getBusinessObject(Object.class)");
    } catch (IllegalStateException e) {
      TLogger.log("Got expected IllegalStateException when calling "
          + "SessionContext.getBusinessObject(Object.class)");
      // good
    }
    try {
      getSessionContext().getBusinessObject(null);
      throw new TestFailedException("Expecting IllegalStateException when "
          + "calling SessionContext.getBusinessObject(null)");
    } catch (IllegalStateException e) {
      TLogger.log("Got expected IllegalStateException when calling "
          + "SessionContext.getBusinessObject(null)");
      // good
    }
    try {
      getSessionContext().getBusinessObject(java.io.Serializable.class);
      throw new TestFailedException("Expecting IllegalStateException when "
          + "calling SessionContext.getBusinessObject(Serializable.class)");
    } catch (IllegalStateException e) {
      TLogger.log("Got expected IllegalStateException when calling "
          + "SessionContext.getBusinessObject(Serializable.class)");
      // good
    }
  }

  private Class getInvokedBusinessInterface() throws TestFailedException {
    return getSessionContext().getInvokedBusinessInterface();
  }
}
