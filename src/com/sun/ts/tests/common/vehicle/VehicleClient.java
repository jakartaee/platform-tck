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

package com.sun.ts.tests.common.vehicle;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;

/**
 * Class used as a client of all vehicle tests.
 */
public class VehicleClient extends ServiceEETest {
  String[] sVehicles;

  private static Object theSharedObject = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    VehicleClient client = new VehicleClient();
    Status s = client.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * Set shared object
   */
  public static void setClientSharedObject(Object o) {
    theSharedObject = o;
  }

  /*
   * Get shared object
   */
  public static Object getClientSharedObject() {
    return theSharedObject;
  }

}
