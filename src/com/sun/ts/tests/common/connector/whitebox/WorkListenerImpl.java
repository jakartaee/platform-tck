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

import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;
import com.sun.ts.tests.common.connector.util.*;

public class WorkListenerImpl implements WorkListener {
  private String uidStr = null;

  public void workAccepted(WorkEvent e) {
    if (uidStr == null) {
      ConnectorStatus.getConnectorStatus()
          .logState("WorkListenerImpl.workAccepted");
    } else {
      ConnectorStatus.getConnectorStatus()
          .logState("WorkListenerImpl.workAccepted for:" + uidStr);
    }
    debug("WorkListenerImpl.workAccepted");
  }

  public void workRejected(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("WorkListenerImpl.workRejected");
    debug("WorkListenerImpl.workRejected");
  }

  public void workStarted(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("WorkListenerImpl.workStarted");
    debug("WorkListenerImpl.workStarted");
  }

  public void workCompleted(WorkEvent e) {
    ConnectorStatus.getConnectorStatus()
        .logState("WorkListenerImpl.workCompleted");
    debug("WorkListenerImpl.workCompleted");
  }

  public void setUidStr(String val) {
    this.uidStr = val;
  }

  public String getUidStr() {
    return this.uidStr;
  }

  private void debug(String str) {
    Debug.trace(str);
  }
}
