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

import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.WorkException;
import com.sun.ts.tests.common.connector.util.*;
import javax.resource.spi.work.DistributableWork;

public class DistributedWorkImpl implements DistributableWork {

  private WorkManager wm;

  public DistributedWorkImpl(WorkManager wm) {
    this.wm = wm;

    ConnectorStatus.getConnectorStatus()
        .logAPI("DistributedWorkImpl.constructor", "", "");
    System.out.println("DistributedWorkImpl.constructor");
  }

  @Override
  public void release() {
    ConnectorStatus.getConnectorStatus().logAPI("DistributedWorkImpl.release",
        "", "");
    System.out.println("DistributedWorkImpl.release");
  }

  public void run() {
    try {
      ConnectorStatus.getConnectorStatus().logAPI("DistributedWorkImpl.run", "",
          "");
      System.out.println("DistributedWorkImpl.run");
      NestWork nw = new NestWork();
      wm.doWork(nw);
    } catch (WorkException e) {
      // this could mean work completed or work rejected or something else.
      System.out.println("DistributedWorkImpl WorkException caught");
    }
  }

}
