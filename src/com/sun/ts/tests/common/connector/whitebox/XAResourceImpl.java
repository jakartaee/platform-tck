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

import java.util.Hashtable;
import javax.transaction.xa.*;
import com.sun.ts.tests.common.connector.util.*;

public class XAResourceImpl implements XAResource {

  private TSManagedConnection mc;

  private int seconds = 0;

  private Hashtable xidset = null;

  public XAResourceImpl(TSManagedConnection mc) {
    this.mc = mc;
    xidset = new Hashtable();
    if (mc == null) {
      System.out.println("TSManagedConnection is null in XAResourceImpl");
    }

  }

  private void handleResourceException(Exception ex) throws XAException {

    XAException xae = new XAException(ex.toString());
    xae.errorCode = XAException.XAER_RMERR;
    throw xae;
  }

  public void commit(Xid xid, boolean onePhase) throws XAException {
    try {
      System.out.println("XAResourceImpl.commit");
      // To detect two phase commit and see if the
      // prepare method was called or not.
      ConnectorStatus.getConnectorStatus().logAPI("XAResourceImpl.commit", "",
          "");
      TSeis.getTSeis().getResourceManager().commit(xid, onePhase);
    } catch (XAException xe) {
      throw xe;
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void start(Xid xid, int flags) throws XAException {
    try {
      System.out.println("XAResourceImpl.start");
      TSeis.getTSeis().getResourceManager().start(xid, flags,
          mc.getTSConnection());
      ConnectorStatus.getConnectorStatus().logAPI("XAResourceImpl.start", "",
          "");
    } catch (XAException xe) {
      throw xe;
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void end(Xid xid, int flags) throws XAException {
    try {
      System.out.println("XAResourceImpl.end");
      TSeis.getTSeis().getResourceManager().end(xid, flags);
      ConnectorStatus.getConnectorStatus().logAPI("XAResourceImpl.end", "", "");
    } catch (XAException xe) {
      throw xe;
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public void forget(Xid xid) throws XAException {
    System.out.println("XAResourceImpl.forget");
  }

  public int getTransactionTimeout() throws XAException {
    return this.seconds;
  }

  public boolean isSameRM(XAResource other) throws XAException {
    System.out.println("XAResourceImpl.isSameRM");
    if (this == other)
      return true;
    if (other == null)
      return false;
    if ((other instanceof XAResourceImpl) && (this.mc != null)) {
      XAResourceImpl obj = (XAResourceImpl) other;
      return (this.mc.equals(obj.mc));
    } else {
      return false;
    }
  }

  public int prepare(Xid xid) throws XAException {
    ConnectorStatus.getConnectorStatus().logAPI("XAResourceImpl.prepare", "",
        "");
    System.out.println("XAResourceImpl.prepare");
    try {
      return TSeis.getTSeis().getResourceManager().prepare(xid);
    } catch (XAException xe) {
      throw xe;
    } catch (Exception ex) {
      handleResourceException(ex);
      return XAException.XAER_RMERR;
    }
  }

  public Xid[] recover(int flag) throws XAException {
    System.out.println("XAResourceImpl.recover");
    return null;
  }

  public void rollback(Xid xid) throws XAException {
    try {
      System.out.println("XAResourceImpl.rollback");
      TSeis.getTSeis().getResourceManager().rollback(xid);
      ConnectorStatus.getConnectorStatus().logAPI("XAResourceImpl.rollback", "",
          "");
    } catch (XAException xe) {
      throw xe;
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  public boolean setTransactionTimeout(int seconds) throws XAException {
    this.seconds = seconds;
    return true;
  }

}
