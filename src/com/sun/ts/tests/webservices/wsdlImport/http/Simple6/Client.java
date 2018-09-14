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

package com.sun.ts.tests.webservices.wsdlImport.http.Simple6;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.*;
import javax.xml.rpc.handler.*;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  private String SERVICE_NAME_WITH_WSDL = "Simple6Http";

  private String SERVICE_NAME_WITH_WSDL_NO_PCREF = "Simple6Http_no_pcref";

  private Tests testsPort;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      InitialContext ctx = new InitialContext();
      Service svc;

      TestUtil.logMsg("JNDI lookup for Simple6 Service");
      svc = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      TestUtil.logMsg("Service found");
      TestUtil.logMsg("Get port from Service");
      testsPort = (Tests) svc.getPort(Tests.class);
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
   * @assertion_ids: WS4EE:SPEC:214; WS4EE:SPEC:65;
   *
   * @test_Strategy: Call a method in Tests.
   */
  public void InvokeMethod() throws Fault {
    TestUtil.logMsg("InvokeMethod");
    try {
      testsPort.invokeTest3();
      TestUtil.logMsg("InvokeMethod passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test InvokeMethod failed: got exception " + t.toString());
      throw new Fault("InvokeMethod failed");
    }

    return;
  }
}
