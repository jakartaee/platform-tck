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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.dii.webservice.WebServicesClient;

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

  private static final String WSDL1 = "http://localhost:8000/WS1?WSDL";

  private static final String MODEL1 = "model1.file";

  private static final String WSDL2 = "http://localhost:8000/WS2?WSDL";

  private static final String MODEL2 = "model2.file";

  private WebServicesClient wsc = new WebServicesClient();

  private ArrayList webservices = new ArrayList();

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
    webservices.add(new WebService(WSDL1, MODEL1));
    webservices.add(new WebService(WSDL2, MODEL2));
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: WebServicesClientConstructorTest1
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void WebServicesClientConstructorTest1() throws Fault {
    TestUtil.logTrace("WebServicesClientConstructorTest1");
    boolean pass = true;
    WebServicesClient ws;
    TestUtil.logMsg("Call WebServicesClient() constructor");
    ws = new WebServicesClient();
    if (ws == null)
      pass = false;
    if (!pass)
      throw new Fault("WebServicesClientConstructorTest1 failed");
  }

  /*
   * @testName: setWebServices
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void setWebServices() throws Fault {
    TestUtil.logTrace("setWebServices");
    boolean pass = true;
    wsc.setWebServices(webservices);
    if (!pass)
      throw new Fault("setWebServices failed");
  }

  /*
   * @testName: getWebServices
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getWebServices() throws Fault {
    TestUtil.logTrace("getWebServices");
    boolean pass = true;
    wsc.setWebServices(webservices);
    List list = wsc.getWebServices();
    Iterator iter = list.iterator();
    int i = 0;
    while (iter.hasNext()) {
      ++i;
      WebService ws = (WebService) iter.next();
      TestUtil.logMsg("ws" + i + "=" + ws);
    }
    if (!pass)
      throw new Fault("getWebServices failed");
  }
}
