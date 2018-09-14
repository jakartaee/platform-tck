/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.vehicle.customvehicle;

import java.io.*;
import java.util.*;
import java.net.*;

import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.common.vehicle.*;

/**
 * This class is to be modified and rebuilt by any vendor needing to build their
 * own custom vehicles. This should only be used as allowed on a case by case
 * basis and must be explicitly stated as a viable option in the TCK user guide.
 *
 * This class should be edited, compiled, and packaged as needed for the
 * environment it is to be used in. This vehicle will be recognized as the
 * "customvehicle" and it will be necessary to make appropriate changes to the
 * TS_HOME/src/vehicle.properties file to indicate the use of this vehicle.
 *
 * It is suggested that you use other vehicles as a model for implementing this.
 * Additional information for using this class should be referenced in the TCK
 * user guides for those technologies that support the definition and use of a
 * custom vehicle.
 *
 */
public class CustomVehicleRunner implements VehicleRunnable {

  protected final String sVehicle = "customvehicle";

  public Status run(String[] argv, Properties p) {

    // XXXX: implement your code to wrap and execute each test here
    // such that each test will run in a customvehicle and return
    // the result from each test.

    return Status.failed("not implemented yet.");
  }

}
