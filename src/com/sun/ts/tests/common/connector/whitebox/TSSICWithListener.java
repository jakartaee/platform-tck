/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.resource.spi.work.WorkContextLifecycleListener;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;

/*
 * This class is used to assist with testing of work context notifications
 * and the calling order.  This makes use of the Counter class and since we
 * are counting invocations, this class should NOT be used by other tests
 * unless you are sure you know what you're doing.
 * We want to record the calling order of notifications so we must be sure
 * to increment each count.
 *
 */
public class TSSICWithListener extends TSSecurityContext
    implements WorkContextLifecycleListener {

  private Counter count = new Counter();

  public TSSICWithListener(String userName, String password,
      String principalName, boolean translationRequired) {
    super(userName, password, principalName, translationRequired);
    debug("TSSICWithListener:  constructor");
  }

  @Override
  public void contextSetupComplete() {
    debug("TSSICWithListener.contextSetupComplete() " + this.toString());

    String str = "notifications test: contextSetupComplete(): count="
        + count.getCount(Counter.Action.INCREMENT);
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);
  }

  @Override
  public void contextSetupFailed(String string) {
    debug("TSSICWithListener.contextSetupFailed with following: " + string);

    String str = "notifications test: contextSetupFailed(): count="
        + count.getCount(Counter.Action.INCREMENT);
    ConnectorStatus.getConnectorStatus().logState(str);

    str = "contextSetupFailed() due to errorCode=" + string;
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);
  }

  public void debug(String message) {
    Debug.trace(message);
  }

}
