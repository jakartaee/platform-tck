/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.ibanno;

import javax.transaction.xa.*;
import javax.resource.spi.*;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;

public class IBAnnoMessageXAResource1 implements XAResource {

  public IBAnnoMessageXAResource1() {
    Debug.trace("IBAnnoMessageXAResource1 constructor");
  }

  private void handleResourceException(Exception ex) throws XAException {

    XAException xae = new XAException(ex.toString());
    xae.errorCode = XAException.XAER_RMERR;
    throw xae;
  }

  public void commit(Xid xid, boolean onePhase) throws XAException {
    try {
      Debug.trace("IBAnnoMessageXAResource1.commit");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void start(Xid xid, int flags) throws XAException {
    try {
      Debug.trace("IBAnnoMessageXAResource1.start");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void end(Xid xid, int flags) throws XAException {
    try {
      Debug.trace("IBAnnoMessageXAResource1.end");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void forget(Xid xid) throws XAException {
    Debug.trace("IBAnnoMessageXAResource1.forget");
  }

  public int getTransactionTimeout() throws XAException {
    return 1;
  }

  public boolean isSameRM(XAResource other) throws XAException {
    Debug.trace("IBAnnoMessageXAResource1.isSameRM");
    return false;
  }

  public int prepare(Xid xid) throws XAException {
    ConnectorStatus.getConnectorStatus()
        .logAPI("IBAnnoMessageXAResource1.prepare", "", "");
    Debug.trace("IBAnnoMessageXAResource1.prepare");
    try {
      return XAResource.XA_OK;
    } catch (Exception ex) {
      handleResourceException(ex);
      return XAException.XAER_RMERR;
    }
  }

  public Xid[] recover(int flag) throws XAException {
    Debug.trace("IBAnnoMessageXAResource1.recover");
    return null;
  }

  public void rollback(Xid xid) throws XAException {
    try {
      Debug.trace("IBAnnoMessageXAResource1.rollback");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public boolean setTransactionTimeout(int seconds) throws XAException {
    return true;
  }

}
