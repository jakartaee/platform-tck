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

package com.sun.ts.tests.common.connector.whitebox;

import java.util.Map;
import java.util.Vector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.work.WorkException;
import com.sun.ts.tests.common.connector.util.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.ActivationSpec;

/*
 * This class is used to assist with testing of work context notifications
 * and the calling order.  This makes use of the Counter class and since we
 * are counting invocations, this class should NOT be used by other tests
 * unless you are sure you know what you're doing.
 * We want to record the calling order of notifications so we must be sure
 * to increment each count
 *
 */
public class WorkListenerImpl2 implements WorkListener {
  private Counter count = new Counter();

  public void workAccepted(WorkEvent e) {
    debug("WorkListenerImpl2.workAccepted");
    String str = "notifications test: workAccepted(): count="
        + count.getCount(Counter.Action.INCREMENT);
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);
  }

  public void workRejected(WorkEvent e) {
    debug("WorkListenerImpl2.workRejected");
  }

  public void workStarted(WorkEvent e) {
    String str = "notifications test: workStarted(): count="
        + count.getCount(Counter.Action.INCREMENT);
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);
  }

  public void workCompleted(WorkEvent e) {
    String str = "notifications test: workCompleted(): count="
        + count.getCount(Counter.Action.INCREMENT);
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);
  }

  private void debug(String str) {
    Debug.trace(str);
  }
}
