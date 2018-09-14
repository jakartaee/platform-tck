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

package com.sun.ts.tests.webservices.wsdlImport.http.nested4;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import javax.xml.rpc.Service;

import javax.naming.InitialContext;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

public class Client extends ServiceEETest {
  StockQuotePortType port = null;

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/nested4");
    TestUtil.logMsg("Get port from Service1");
    port = (StockQuotePortType) svc.getPort(
        com.sun.ts.tests.webservices.wsdlImport.http.nested4.StockQuotePortType.class);
    TestUtil.logMsg("Port obtained");
  }

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
      getStub();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: invokeNestedImportWsdl
   *
   * @assertion_ids: WS4EE:SPEC:214;WS4EE:SPEC:65
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke an RPC method. Verify
   * RPC method is invoked.
   *
   * Description A client can invoke an RPC method via generated stub.
   */
  public void invokeNestedImportWsdl() throws Fault {
    TestUtil.logTrace("invokeNestedImportWsdl");
    boolean pass = true;
    try {

      TestUtil.logMsg("Invoke getLastTradePrice(\"GTE\") and expect 24.25");
      GetLastTradePrice glt = new GetLastTradePrice();
      glt.setTickerSymbol("GTE");
      TradePrice response = port.getLastTradePrice(glt);
      if (response.getPrice() != 24.25f) {
        TestUtil.logErr("RPC failed - expected: \"24.25" + "\", received: "
            + response.getPrice());
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
      TestUtil.logMsg("Invoke getLastTradePrice(\"GE\") and expect 45.5");
      glt.setTickerSymbol("GE");
      response = port.getLastTradePrice(glt);
      if (response.getPrice() != 45.5f) {
        TestUtil.logErr("RPC failed - expected: \"45.5" + "\", received: "
            + response.getPrice());
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("invokeNestedImportWsdl");
    }

    if (!pass)
      throw new Fault("invokeNestedImportWsdl failed");
  }
}
