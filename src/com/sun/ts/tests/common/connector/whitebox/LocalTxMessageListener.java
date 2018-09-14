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

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.XATerminator;
import com.sun.ts.tests.common.connector.util.*;
import javax.transaction.xa.XAException;

public class LocalTxMessageListener implements WorkListener {

  private XidImpl xid;

  private BootstrapContext bsc;

  public LocalTxMessageListener(XidImpl xid, BootstrapContext bsc) {
    this.xid = xid;
    this.bsc = bsc;
  }

  @Override
  public void workAccepted(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("LocalTxMessageListener.workAccepted");
    System.out.println("LocalTxMessageListener.workAccepted");
  }

  @Override
  public void workRejected(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("LocalTxMessageListener.workRejected");
    System.out.println("LocalTxMessageListener.workRejected");
  }

  @Override
  public void workStarted(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("LocalTxMessageListener.workStarted");
    System.out.println("LocalTxMessageListener.workStarted");
  }

  @Override
  public void workCompleted(WorkEvent e) {
    try {
      XATerminator xt = bsc.getXATerminator();
      xt.commit(this.xid, true);
      System.out.println("LocalTxMessageListener.workCompleted");
      System.out.println(
          "XID getting used in XATerminator [ " + xid.getFormatId() + " ]");
      ConnectorStatus.getConnectorStatus()
          .logState("LocalTxMessageListener committed Xid");
    } catch (XAException ex) {
      ex.printStackTrace();
    }
  }

}
