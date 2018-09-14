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

import com.sun.ts.lib.tests.jbi.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import com.sun.javatest.Status;
import javax.jbi.JBIException;
import javax.jbi.component.ComponentContext;

public class JBIVehicle extends RemoteComponent
    implements RemoteComponentInterface {

  private EETest testObj;

  private Properties p;

  private String[] arguments = new String[0];

  private String installRoot;

  public JBIVehicle() {
    NAME = getClass().getName();
  }

  // override init from BaseComponent to bind the JBIVehicle
  public void init(ComponentContext arg0) throws JBIException {
    super.init(arg0);
    installRoot = arg0.getInstallRoot();
  }

  public Result invoke(ServiceRequest request) throws RemoteException {
    try {
      p = request.getProperties();
      testInit(p);
      Class clazz = Class.forName(p.getProperty("test.class"));
      testObj = (EETest) clazz.newInstance();
      TestUtil.logTrace("JBIVehicle.invoke");
    } catch (Exception e) {
      throw new RemoteException("JBIVehicle.invoke() Failed", e);
    }
    return runTest();
  }

  public Remote getRemoteObject() {
    return this;
  }

  // the run method that we call here will either throw
  // an exception (failed), or return void (pass)
  private Result runTest() {
    Result result = null;
    Status stat = null;
    TestUtil.logTrace("in runTest()");
    try {
      // set component context props so that they are accessible by the test
      p.setProperty("component.install.root", installRoot);

      // call EETest impl's run method
      stat = testObj.run(arguments, p);

      if (stat.getType() == Status.PASSED) {
        TestUtil.logMsg("Test running in JBI vehicle passed");
      } else {
        TestUtil.logMsg("Test running in JBI vehicle failed");
      }
      result = new Result(stat);
    } catch (Throwable e) {
      e.printStackTrace();
      TestUtil.logErr("Test running in JBI vehicle failed", e);
      result = new Result(false,
          "Test running in JBI vehicle failed" + e.toString());
    }
    return result;
  }

}
