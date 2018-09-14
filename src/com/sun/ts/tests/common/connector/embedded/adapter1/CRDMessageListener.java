/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.embedded.adapter1;

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

public class CRDMessageListener implements WorkListener {

  private XidImpl xid;

  private BootstrapContext bsc;

  public CRDMessageListener(XidImpl xid, BootstrapContext bsc) {
    this.xid = xid;
    this.bsc = bsc;
  }

  public void workAccepted(WorkEvent e) {
    System.out.println("CRDMessageListener.workAccepted");
  }

  public void workRejected(WorkEvent e) {
    System.out.println("CRDMessageListener.workRejected");
  }

  public void workStarted(WorkEvent e) {
    System.out.println("CRDMessageListener.workStarted");
  }

  public void workCompleted(WorkEvent e) {
    try {
      XATerminator xt = bsc.getXATerminator();
      xt.commit(this.xid, true);
      System.out.println("CRDMessageListener.workCompleted");
      System.out.println(
          "XID getting used in XATerminator [ " + xid.getFormatId() + " ]");

    } catch (XAException ex) {
      Debug.trace("CRDMessageListener.workCompleted() got XAException");
      ex.printStackTrace();
    }
  }

}
