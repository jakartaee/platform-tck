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

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.XATerminator;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import javax.transaction.xa.Xid;
import javax.transaction.xa.XAException;
import com.sun.ts.tests.common.connector.whitebox.XidImpl;

public class IBAnnoMessageListener implements WorkListener {

  private XidImpl xid;

  private BootstrapContext bsc;

  public IBAnnoMessageListener(XidImpl xid, BootstrapContext bsc) {
    this.xid = xid;
    this.bsc = bsc;
  }

  public void workAccepted(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("IBAnnoMessageListener.workAccepted");
    if (xid != null) {
      System.out.println("IBAnnoMessageListener.workAccepted() for XID = "
          + xid.getFormatId());
    } else {
      // should not get here but just in case...
      System.out.println("IBAnnoMessageListener.workAccepted() for XID = null");
    }
  }

  public void workRejected(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("IBAnnoMessageListener.workRejected");
    if (xid != null) {
      System.out.println("IBAnnoMessageListener.workRejected() for XID = "
          + xid.getFormatId());
    } else {
      // should not get here but just in case...
      System.out.println("IBAnnoMessageListener.workRejected() for XID = null");
    }
  }

  public void workStarted(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("IBAnnoMessageListener.workStarted");
    System.out.println("IBAnnoMessageListener.workStarted");
  }

  public void workCompleted(WorkEvent e) {
    try {
      XATerminator xt = bsc.getXATerminator();
      System.out.println(
          "IBAnnoMessageListener.workCompleted and about to call XATerminator.commit()");
      System.out.println(
          "XID getting used in XATerminator [ " + xid.getFormatId() + " ]");
      xt.commit(this.xid, true);
      ConnectorStatus.getConnectorStatus()
          .logState("IBAnnoMessageListener committed Xid");
    } catch (XAException ex) {
      Debug.trace("IBAnnoMessageListener.workCompleted() got XAException");
      Debug.trace("XAException.toString() = " + ex.toString());
      ex.printStackTrace();
    }
  }

}
