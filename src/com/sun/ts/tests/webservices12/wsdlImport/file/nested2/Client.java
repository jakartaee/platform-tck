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

package com.sun.ts.tests.webservices12.wsdlImport.file.nested2;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;

import java.util.*;

import javax.xml.ws.Service;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  private Tests port;

  Nested2FileSvc service = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap webservices-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      TestUtil.logMsg(
          "WebServiceRef is not set in Client (get it from specific vehicle)");
      service = (Nested2FileSvc) getSharedObject();
      TestUtil.logMsg("service=" + service);
      JAXWS_Util.dumpServiceName((Service) service);
      JAXWS_Util.dumpWSDLLocation((Service) service);
      JAXWS_Util.dumpPorts((Service) service);
      TestUtil.logMsg("Get port from Service");
      port = (Tests) service.getPort(Tests.class);
      TestUtil.logMsg("Port obtained");
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }

    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: InvokeMethod
   *
   * @assertion_ids: WS4EE:SPEC:214; WS4EE:SPEC:66;
   *
   * @test_Strategy: Call a method in Tests.
   */
  public void InvokeMethod() throws Fault {
    TestUtil.logMsg("InvokeMethod");
    try {
      port.invokeTest1();
      TestUtil.logMsg("InvokeMethod passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test InvokeMethod failed: got exception " + t.toString());
      throw new Fault("InvokeMethod failed");
    }

    return;
  }
}
