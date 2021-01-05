/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
 *
 */
package com.sun.ts.tests.common.vehicle.connectorservlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.util.TestUtil;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ConnectorServletVehicle extends HttpServlet {
  protected Properties properties = null;

  protected String[] arguments = null;

  protected EETest testObj = null;

  public void init(ServletConfig config) throws ServletException {
    TestUtil.logTrace("init " + this.getClass().getName() + " ...");
    super.init(config);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    try {
      // get the inputstream and read any objects passed from the
      // client, e.g. properties, args, etc.
      // wrap the Inputstream in an ObjectInputstream and read
      // the properties and args.
      TestUtil.logTrace("ConnectorServletVehicle - In doGet");

      ObjectInputStream objInStream = new ObjectInputStream(
          new BufferedInputStream(req.getInputStream()));
      System.out.println("ConnectorServletVehicle - got InputStream");
      TestUtil.logTrace("ConnectorServletVehicle - got InputStream");
      properties = (Properties) objInStream.readObject();
      System.out.println("read properties!!!");

      // create an instance of the test client and run here
      Class c = Class.forName(properties.getProperty("test_classname"));
      testObj = (EETest) c.newInstance();

      // Thread.currentThread().dumpStack();
      arguments = (String[]) objInStream.readObject();
      // arguments = new String[1];
      // arguments[0] = "";
      TestUtil.logTrace("ConnectorServletVehicle - read Objects");
      try {
        TestUtil.init(properties);
        TestUtil.logTrace("Remote logging set for Servlet Vehicle");
        TestUtil.logTrace("ConnectorServletVehicle - Here are the props");
        TestUtil.list(properties);
      } catch (Exception e) {
        throw new ServletException("unable to initialize remote logging");
      }
      ObjectOutputStream objOutStream = new ObjectOutputStream(
          res.getOutputStream());
      System.out.println("got outputstream");
      // now run the test and return the result
      RemoteStatus finalStatus = runTest();
      System.out.println("ran test");
      objOutStream.writeObject(finalStatus);
      objOutStream.flush();
      objOutStream.close();

    } catch (Throwable t) {
      System.out.println(t.getMessage());
      TestUtil.logTrace(t.getMessage());
      t.printStackTrace();
      throw new ServletException(
          "test failed to run within the Servlet Vehicle");
    }

  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    System.out.println("In doPost!");
    TestUtil.logTrace("In doPost");
    doGet(req, res);
  }

  protected RemoteStatus runTest() throws RemoteException {
    RemoteStatus sTestStatus = new RemoteStatus(Status.passed(""));

    try {
      // call EETest impl's run method
      sTestStatus = new RemoteStatus(testObj.run(arguments, properties));

      if (sTestStatus.getType() == Status.PASSED)
        TestUtil.logMsg("Test running in servlet vehicle passed");
      else
        TestUtil.logMsg("Test running in servlet vehicle failed");
    } catch (Throwable e) {
      TestUtil.logErr("Test running in servlet vehicle failed", e);
      sTestStatus = new RemoteStatus(
          Status.failed("Test running in servlet vehicle failed"));
    }
    return sTestStatus;
  }
}
