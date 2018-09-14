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

package com.sun.ts.tests.common.connector.whitebox;

import javax.transaction.xa.*;
import com.sun.ts.tests.common.connector.util.*;

public class XAMessageXAResource implements XAResource {

  public XAMessageXAResource() {
    System.out.println("XAMessageXAResource constructor");
  }

  private void handleResourceException(Exception ex) throws XAException {

    XAException xae = new XAException(ex.toString());
    xae.errorCode = XAException.XAER_RMERR;
    throw xae;
  }

  public void commit(Xid xid, boolean onePhase) throws XAException {
    try {
      System.out.println("XAMessageXAResource.commit");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void start(Xid xid, int flags) throws XAException {
    try {
      System.out.println("XAMessageXAResource.start");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void end(Xid xid, int flags) throws XAException {
    try {
      System.out.println("XAMessageXAResource.end");
      // ConnectorStatus.getConnectorStatus().logAPI("MessageXAResource.end" ,
      // "", "");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void forget(Xid xid) throws XAException {
    System.out.println("XAMessageXAResource.forget");
  }

  public int getTransactionTimeout() throws XAException {
    return 1;
  }

  public boolean isSameRM(XAResource other) throws XAException {
    System.out.println("XAMessageXAResource.isSameRM");
    return false;
  }

  public int prepare(Xid xid) throws XAException {
    ConnectorStatus.getConnectorStatus().logAPI("XAMessageXAResource.prepare",
        "", "");
    System.out.println("XAMessageXAResource.prepare");
    try {
      return XAResource.XA_OK;
    } catch (Exception ex) {
      handleResourceException(ex);
      return XAException.XAER_RMERR;
    }
  }

  public Xid[] recover(int flag) throws XAException {
    System.out.println("XAMessageXAResource.recover");
    return null;
  }

  public void rollback(Xid xid) throws XAException {
    try {
      System.out.println("XAMessageXAResource.rollback");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public boolean setTransactionTimeout(int seconds) throws XAException {
    return true;
  }

}
