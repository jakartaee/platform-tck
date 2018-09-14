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

import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkCompletedException;
import javax.resource.spi.work.WorkRejectedException;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.TransactionContext;
import com.sun.ts.tests.common.connector.util.*;
import javax.transaction.xa.Xid;
import javax.resource.spi.work.ExecutionContext;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.NestedWorkXid;

public class NestedWorkXid1 implements Work {

  public enum ContextType {
    EXECUTION_CONTEXT, TRANSACTION_CONTEXT, SECURITY_CONTEXT
  };

  private WorkManager wm;

  private Xid xid;

  private ContextType contxtType;

  public NestedWorkXid1(WorkManager wm, Xid xid, ContextType contxt) {
    this.wm = wm;
    this.xid = xid;
    this.contxtType = contxt;

    ConnectorStatus.getConnectorStatus().logAPI("NestedWorkXid1.constructor",
        "", "");
    Debug.trace("NestedWorkXid1.constructor");
  }

  public void release() {
    ConnectorStatus.getConnectorStatus().logAPI("NestedWorkXid1.release", "",
        "");
    Debug.trace("NestedWorkXid1.release");
  }

  public void run() {
    String strPass = "anno based NestedWorkXid1 child context submitted";
    try {
      Debug.trace("NestedWorkXid1.run");
      ConnectorStatus.getConnectorStatus().logAPI("NestedWorkXid1.run", "", "");
      Debug.trace("Got the xid");
      NestedWorkXid workid = new NestedWorkXid();

      if (contxtType == ContextType.TRANSACTION_CONTEXT) {
        Debug.trace("Using TRANSACTION_CONTEXT to set the xid in tc");
        TransactionContext tc = new TransactionContext();
        tc.setXid(this.xid);
        wm.doWork(workid, wm.INDEFINITE, tc, null);
      } else {
        // assume ExecutionContext - no need for SecurityContext yet
        Debug.trace("Using EXECUTION_CONTEXT to set the xid in ec");
        ExecutionContext ec = new ExecutionContext();
        ec.setXid(this.xid);
        wm.doWork(workid, wm.INDEFINITE, ec, null);
      }

      // flow could make it here or could throw WorkCompletedException
      Debug.trace(strPass);
      ConnectorStatus.getConnectorStatus().logState(strPass);

    } catch (WorkCompletedException we) {
      // could make it here upon successful completion of work
      Debug.trace(strPass);
      ConnectorStatus.getConnectorStatus().logState(strPass);
    } catch (WorkRejectedException we) {
      // should not make it here
      Debug.trace("WorkRejectedException in NestedWorkXid1");
    } catch (WorkException we) {
      // should not make it here
      Debug.trace("WorkException in NestedWorkXid1");
    } catch (Exception ex) {
      // should not make it here
      Debug.trace("Exception in NestedWorkXid1");
    }
  }

}
