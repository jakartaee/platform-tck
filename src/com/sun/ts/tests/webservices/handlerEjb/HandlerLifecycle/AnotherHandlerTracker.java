/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.handlerEjb.HandlerLifecycle;

import javax.xml.rpc.handler.*;
import java.util.*;

public class AnotherHandlerTracker {

  private static AnotherHandlerTracker _singleInstance = new AnotherHandlerTracker();

  private static HashSet _failedHandlerInstances = new HashSet();

  public static AnotherHandlerTracker getAHT() {
    return _singleInstance;
  }

  // Returns a true if the handler passed in had already failed
  public boolean checkForFailedInstance(Handler hand) {

    System.out.println("Checking for failed instance of " + hand);
    boolean result = _failedHandlerInstances.contains(hand);
    return result;
  }

  // Add a failed instance to the failed handler instance table
  public boolean addFailedInstance(Handler hand) {
    System.out.println("Adding failed instance to the table: " + hand);
    boolean result = _failedHandlerInstances.add(hand);
    return result;
  }

}
