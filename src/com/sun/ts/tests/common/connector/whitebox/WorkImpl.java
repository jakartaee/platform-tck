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

public class WorkImpl implements Work {
  protected WorkManager wm;

  protected String callingClassName = "WorkImpl";

  public WorkImpl(WorkManager wm) {
    this.wm = wm;

    String str = callingClassName + ".constructor";
    ConnectorStatus.getConnectorStatus().logAPI(str, "", "");
    debug(str);
  }

  public WorkImpl(WorkManager wm, String strCallingClassName) {
    this.wm = wm;
    callingClassName = strCallingClassName;

    String str = callingClassName + ".constructor";
    ConnectorStatus.getConnectorStatus().logAPI(str, "", "");
    debug(str);
  }

  public void release() {
    String str = callingClassName + ".release";
    ConnectorStatus.getConnectorStatus().logAPI(str, "", "");
    debug(str);
  }

  public void run() {
    try {
      String str = callingClassName + ".run";
      ConnectorStatus.getConnectorStatus().logAPI(str, "", "");
      debug(str);
      NestWork nw = new NestWork();
      wm.doWork(nw);
    } catch (WorkException we) {
      debug("got WorkException in  WorkImpl.run(): " + we.getMessage());
    }
  }

  /*
   * this sets the name of the calling class so that we can be sure proper
   * logging info is dumped out.
   *
   */
  public void setCallingClassName(String str) {
    this.callingClassName = str;
  }

  public String getCallingClassName() {
    return this.callingClassName;
  }

  private void debug(String str) {
    Debug.trace(str);
  }

}
