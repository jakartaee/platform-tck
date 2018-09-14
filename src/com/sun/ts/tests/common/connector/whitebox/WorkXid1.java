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

import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import com.sun.ts.tests.common.connector.util.*;
import javax.transaction.xa.Xid;
import javax.resource.spi.work.ExecutionContext;

public class WorkXid1 implements Work {

  private WorkManager wm;

  private Xid xid;

  public WorkXid1(WorkManager wm, Xid xid) {
    this.wm = wm;
    this.xid = xid;
    ConnectorStatus.getConnectorStatus().logAPI("WorkXid.constructor", "", "");
    Debug.trace("WorkXid1.constructor");
  }

  public void release() {
    ConnectorStatus.getConnectorStatus().logAPI("WorkXid.release", "", "");
    Debug.trace("WorkXid1.release() called.");

    if ((xid != null) && (xid.getGlobalTransactionId() != null)) {
      Debug.trace("WorkXid1.release() xid was: "
          + new String(xid.getGlobalTransactionId()));
    } else if (xid != null) {
      Debug.trace(
          "WorkXid1.release() xid was NOT null but xid.getGlobalTransactionId() was null.");
    } else {
      Debug.trace("WorkXid1.release() xid was: null");
    }
    Debug.trace("WorkXid1.release() xid being set == null");

    this.xid = null;
  }

  public void run() {
    try {
      ConnectorStatus.getConnectorStatus().logAPI("WorkXid.run", "", "");
      Debug.trace("WorkXid1.run");
      ConnectorStatus.getConnectorStatus().logAPI("WorkXid1.run", "", "");
      NestedWorkXid workid = new NestedWorkXid();

      ExecutionContext ec = new ExecutionContext();
      ec.setXid(this.xid);
      Debug.trace("set the xid in ec");
      wm.doWork(workid, wm.INDEFINITE, ec, null);
      Debug.trace("submitted the nested xid work");
      ConnectorStatus.getConnectorStatus().logState("WorkXid Submitted");

    } catch (WorkException we) {
      if (we.TX_CONCURRENT_WORK_DISALLOWED == WorkException.TX_CONCURRENT_WORK_DISALLOWED) {
        Debug.trace("In the WorkException of Concurrent xid");
        ConnectorStatus.getConnectorStatus()
            .logState("WorkException.TX_CONCURRENT_WORK_DISALLOWED caught");
      }
    }
  }

}
