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

/**
 * Be careful..... This class will log a variety of message strings that will be
 * used by client side tests. If you change any strings in this class, it is
 * likely to result in test failures unless you are sure you know what tests you
 * are affecting.
 */
public class LocalTxMessageXAResource implements XAResource {
  String sHeader = "SomeDefault";

  public LocalTxMessageXAResource() {
    Debug.trace("LocalTxMessageXAResource constructor");
  }

  /*
   * This constructor takes a string val. The passed in string is a unique
   * string that will be used in client side test verifications. Making changes
   * to the passed in string val is likely to result in test failures - so don't
   * muck with the value unless you know exactly what tests you are affecting..
   */
  public LocalTxMessageXAResource(String val) {
    sHeader = val;
    Debug.trace(sHeader + " constructor");
  }

  private void handleResourceException(Exception ex) throws XAException {

    XAException xae = new XAException(ex.toString());
    xae.errorCode = XAException.XAER_RMERR;
    throw xae;
  }

  @Override
  public void commit(Xid xid, boolean onePhase) throws XAException {
    try {
      String str1 = sHeader + ".commit";
      ConnectorStatus.getConnectorStatus().logState(str1);
      Debug.trace(str1);
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  @Override
  public void start(Xid xid, int flags) throws XAException {
    try {
      String str1 = sHeader + ".start"; // e.g. "LocalTxMessageXAResource.start"
      Debug.trace(str1);
      ConnectorStatus.getConnectorStatus().logState(str1);
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  @Override
  public void end(Xid xid, int flags) throws XAException {
    try {
      String str1 = sHeader + ".end"; // e.g. "LocalTxMessageXAResource.end"
      Debug.trace(str1);
      ConnectorStatus.getConnectorStatus().logState(str1);
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  @Override
  public void forget(Xid xid) throws XAException {
    String str1 = sHeader + ".forget"; // e.g. "LocalTxMessageXAResource.forget"
    Debug.trace(str1);
  }

  @Override
  public int getTransactionTimeout() throws XAException {
    return 1;
  }

  @Override
  public boolean isSameRM(XAResource other) throws XAException {
    String str1 = sHeader + ".isSameRM"; // e.g.
                                         // "LocalTxMessageXAResource.isSameRM"
    Debug.trace(str1);
    return false;
  }

  @Override
  public int prepare(Xid xid) throws XAException {
    String str1 = sHeader + ".prepare"; // e.g.
                                        // "LocalTxMessageXAResource.prepare"
    ConnectorStatus.getConnectorStatus().logAPI(str1, "", "");
    Debug.trace(str1);
    try {
      return XAResource.XA_OK;
    } catch (Exception ex) {
      handleResourceException(ex);
      return XAException.XAER_RMERR;
    }
  }

  @Override
  public Xid[] recover(int flag) throws XAException {
    String str1 = sHeader + ".recover"; // e.g.
                                        // "LocalTxMessageXAResource.recover"
    Debug.trace(str1);
    return null;
  }

  @Override
  public void rollback(Xid xid) throws XAException {
    try {
      Debug.trace(sHeader + ".rollback");
    } catch (Exception ex) {
      handleResourceException(ex);
    }
  }

  @Override
  public boolean setTransactionTimeout(int seconds) throws XAException {
    return true;
  }

}
