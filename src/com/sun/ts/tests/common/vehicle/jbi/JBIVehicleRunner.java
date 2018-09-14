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

package com.sun.ts.tests.common.vehicle.jbi;

import java.io.*;
import java.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.tests.jbi.*;
import com.sun.ts.tests.common.vehicle.*;

public class JBIVehicleRunner implements VehicleRunnable {

  public Status run(String[] argv, Properties p) {
    String sVehicle = p.getProperty("vehicle");
    Result res = null;
    try {
      String testClass = p.getProperty("test_classname");
      p.setProperty("test.class", testClass);
      ServiceRequest noRequest = new ServiceRequest("no-request", p);
      String rmiRegHost = p.getProperty("jbi.rmi.host", "localhost");
      int rmiRegPort = Integer.parseInt(p.getProperty("jbi.rmi.port", "1099"));
      String rmiBoundName = p.getProperty("jbi.vehicle.rmi.bound.name",
          "signaturetest.jbi.JBIVehicle");
      TestUtil.logMsg("rmiRegHost   = " + rmiRegHost);
      TestUtil.logMsg("rmiRegPort   = " + rmiRegPort);
      TestUtil.logMsg("rmiBoundName = " + rmiBoundName);
      TestUtil.logMsg("testClass = " + testClass);
      RemoteComponentInterface jbiVehicle = (RemoteComponentInterface) RegistryUtils
          .lookup(rmiRegHost, rmiRegPort, rmiBoundName);
      res = jbiVehicle.invoke(noRequest);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in JBIVehicleRunner.run()", e);
      res = new Result(false,
          "Exception caught in JBIVehicleRunner.run() " + e);
    }
    return res.asStatus();
  }

}
