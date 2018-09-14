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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.dii.webservice.WebService;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.client.dii.webservice.*;

public class Client extends EETest {
  private Properties props = null;

  private static final String WSDL_LOC = "http://localhost:8000/HS?WSDL";

  private static final String MODEL = "model.file";

  private WebService myWebSvc;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    myWebSvc = new WebService(WSDL_LOC, MODEL);
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: WebServiceConstructorTest1
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void WebServiceConstructorTest1() throws Fault {
    TestUtil.logTrace("WebServiceConstructorTest1");
    boolean pass = true;
    WebService ws;
    TestUtil.logMsg("Call WebService() constructor");
    ws = new WebService();
    if (ws == null)
      pass = false;
    if (!pass)
      throw new Fault("WebServiceConstructorTest1 failed");
  }

  /*
   * @testName: WebServiceConstructorTest2
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void WebServiceConstructorTest2() throws Fault {
    TestUtil.logTrace("WebServiceConstructorTest2");
    boolean pass = true;
    WebService ws;
    TestUtil.logMsg("Call WebService(String,String) constructor");
    ws = new WebService(WSDL_LOC, MODEL);
    if (ws == null)
      pass = false;
    if (!pass)
      throw new Fault("WebServiceConstructorTest2 failed");
  }

  /*
   * @testName: getWsdlLocation
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getWsdlLocation() throws Fault {
    TestUtil.logTrace("getWsdlLocation");
    boolean pass = true;
    TestUtil.logMsg("getWsdlLocation=" + myWebSvc.getWsdlLocation());
    if (!(myWebSvc.getWsdlLocation().equals(WSDL_LOC)))
      pass = false;
    if (!pass)
      throw new Fault("getWsdlLocation failed");
  }

  /*
   * @testName: setWsdlLocation
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void setWsdlLocation() throws Fault {
    TestUtil.logTrace("setWsdlLocation");
    String newWsdlLoc = "http://host:port/foobar";
    boolean pass = true;
    myWebSvc.setWsdlLocation(newWsdlLoc);
    TestUtil.logMsg("getWsdlLocation=" + myWebSvc.getWsdlLocation());
    if (!(myWebSvc.getWsdlLocation().equals(newWsdlLoc)))
      pass = false;
    if (!pass)
      throw new Fault("setWsdlLocation failed");
  }

  /*
   * @testName: getModel
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getModel() throws Fault {
    TestUtil.logTrace("getModel");
    boolean pass = true;
    TestUtil.logMsg("getModel=" + myWebSvc.getModel());
    if (!(myWebSvc.getModel().equals(MODEL)))
      pass = false;
    if (!pass)
      throw new Fault("getModel failed");
  }

  /*
   * @testName: setModel
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void setModel() throws Fault {
    TestUtil.logTrace("setModel");
    String newModel = "newmodel.file";
    boolean pass = true;
    myWebSvc.setModel(newModel);
    TestUtil.logMsg("getModel=" + myWebSvc.getModel());
    if (!(myWebSvc.getModel().equals(newModel)))
      pass = false;
    if (!pass)
      throw new Fault("setModel failed");
  }

  /*
   * @testName: toString
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void toStringTest() throws Fault {
    TestUtil.logTrace("toStringTest");
    boolean pass = true;
    TestUtil.logMsg("WebService=" + myWebSvc.toString());
    pass = false;
    if (!pass)
      throw new Fault("toStringTest failed");
  }
}
