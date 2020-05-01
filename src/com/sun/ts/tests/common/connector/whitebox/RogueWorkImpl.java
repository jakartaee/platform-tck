/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.resource.spi.work.Work;

import com.sun.ts.tests.common.connector.util.*;

public class RogueWorkImpl implements Work {

  public RogueWorkImpl() {

    ConnectorStatus.getConnectorStatus().logAPI("RogueWorkImpl.constructor", "",
        "");
    System.out.println("RogueWorkImpl.constructor");
  }

  @Override
  public void release() {
    ConnectorStatus.getConnectorStatus().logAPI("RogueWorkImpl.release", "",
        "");
    System.out.println("RogueWorkImpl.release");
  }

  public void run() {
    final int i = 0;
    final int y = 10;

    int x = y / i;
    System.out.println("Answer is " + x);
  }

}
