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

package com.sun.ts.tests.ejb30.common.migration.threetwo;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBContext;

abstract public class ThreeTestBeanBase implements ThreeTestIF {

  abstract protected TwoLocalHome getTwoLocalHome();

  abstract protected TwoRemoteHome getTwoRemoteHome();

  abstract protected EJBContext getEJBContext();

  //////////////////////////////////////////////////////////////////////
  // business methods from ThreeTestIF
  //////////////////////////////////////////////////////////////////////

  public void callRemote() throws TestFailedException {
    TwoRemoteIF twoRemote = getTwoRemoteBean();
    try {
      String result = twoRemote.from2RemoteClient();
      if ("from2RemoteClient".equals(result)) {
        // good
      } else {
        throw new TestFailedException("Expected from2RemoteClient() to return"
            + "from2RemoteClient, but actual '" + result + "'");
      }
    } catch (RemoteException e) {
      throw new TestFailedException(e);
    } finally {
      try {
        twoRemote.remove();
      } catch (Exception e) {
        // ignore
      }
    }

  }

  public void callRemoteSameTxContext() throws TestFailedException {
    TwoRemoteIF twoRemote = getTwoRemoteBean();
    try {
      twoRemote.remoteSameTxContext();
      if (getEJBContext().getRollbackOnly()) {
        // expected
      } else {
        throw new TestFailedException(
            "Expected getRollbackOnly to return true," + " but got false.");
      }
    } catch (RemoteException e) {
      throw new TestFailedException(e);
    } finally {
      try {
        twoRemote.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  public void callLocal() throws TestFailedException {
    TwoLocalIF twoLocal = getTwoLocalBean();
    try {
      String result = twoLocal.from2LocalClient();
      if ("from2LocalClient".equals(result)) {
        // good
      } else {
        throw new TestFailedException("Expected from2LocalClient() to return"
            + "from2LocalClient, but actual '" + result + "'");
      }
    } finally {
      try {
        twoLocal.remove();
      } catch (Exception e) {
        // igore
      }
    }
  }

  public void callLocalSameTxContext() throws TestFailedException {
    TwoLocalIF twoLocal = getTwoLocalBean();
    try {
      twoLocal.localSameTxContext();
      if (getEJBContext().getRollbackOnly()) {
        // expected
      } else {
        throw new TestFailedException(
            "Expected getRollbackOnly to return true," + " but got false.");
      }
    } finally {
      try {
        twoLocal.remove();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  //////////////////////////////////////////////////////////////////////

  protected TwoRemoteIF getTwoRemoteBean() throws TestFailedException {
    TwoRemoteIF twoRemote = null;
    try {
      twoRemote = getTwoRemoteHome().create();
    } catch (CreateException e) {
      throw new TestFailedException(e);
    } catch (RemoteException e) {
      throw new TestFailedException(e);
    }
    return twoRemote;
  }

  protected TwoLocalIF getTwoLocalBean() throws TestFailedException {
    TwoLocalIF twoLocal = null;
    try {
      twoLocal = getTwoLocalHome().create();
    } catch (CreateException e) {
      throw new TestFailedException(e);
    }
    return twoLocal;
  }

}
