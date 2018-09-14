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

package com.sun.ts.tests.common.vehicle.ejb;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.*;
import java.io.*;
import java.net.*;
import java.rmi.Remote;
import javax.ejb.*;
import java.util.*;
import com.sun.javatest.Status;

public class EJBVehicle implements SessionBean {
  private EETest testObj;

  private Properties properties;

  private String[] arguments;

  public void ejbCreate(String[] args, Properties p) throws CreateException {
    // Initialize TestUtil Reporting
    try {
      TestUtil.init(p);
    } catch (Exception e) {
      TestUtil.logErr("initLogging failed in ejb vehicle.", e);
      throw new EJBException();
    }

    arguments = args;
    properties = p;

    // create an instance of the test client
    try {
      Class c = Class.forName(p.getProperty("test_classname"));
      testObj = (EETest) c.newInstance();
    } catch (Exception e) {
      TestUtil.logErr("Failed to create the EETest instance in the vehicle", e);
      throw new EJBException();
    }
    TestUtil.logTrace("ejbcreate");
  }

  // the run method that we call here will either throw
  // an exception (failed), or return void (pass)
  public RemoteStatus runTest() {
    RemoteStatus sTestStatus = new RemoteStatus(Status.passed(""));

    TestUtil.logTrace("in runTest()");

    try {
      // call EETest impl's run method
      sTestStatus = new RemoteStatus(testObj.run(arguments, properties));

      if (sTestStatus.getType() == Status.PASSED)
        TestUtil.logMsg("Test running in ejb vehicle passed");
      else
        TestUtil.logMsg("Test running in ejb vehicle failed");
    } catch (Throwable e) {
      e.printStackTrace();
      TestUtil.logErr("Test running in ejb vehicle failed", e);
      sTestStatus = new RemoteStatus(
          Status.failed("Test running in ejb vehicle failed"));
    }
    return sTestStatus;
  }

  public void setSessionContext(SessionContext sc) {
  }

  public void ejbRemove() {
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }
}
