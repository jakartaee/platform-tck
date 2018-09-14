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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_handler.GenericHandler;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;
import javax.xml.namespace.QName;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  private Properties props = null;

  Hello port = null;

  Service svc = null;

  private void getStub() throws Exception {
    /* Lookup service then obtain port */
    try {
      InitialContext ic = new InitialContext();
      TestUtil.logMsg("Obtained InitialContext");
      TestUtil.logMsg("Lookup java:comp/env/service/generichandler");
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/generichandler");
      TestUtil.logMsg("Obtained service");
      port = (Hello) svc.getPort(Hello.class);
      TestUtil.logMsg("Obtained port");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
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
    boolean pass = true;

    try {
      getStub();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: DoGenericHandlerTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:144; JAXRPC:JAVADOC:145; JAXRPC:JAVADOC:147;
   * JAXRPC:JAVADOC:146; WS4EE:SPEC:35; WS4EE:SPEC:36;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side and
   * server-side message handler callbacks are called by the JAXRPC RUNTIME.
   * Verify the usage of the GenericHandler interface.
   *
   * Description Register 3 client-side and 3 server-side soap message handlers.
   */

  public void DoGenericHandlerTest() throws Fault {
    TestUtil.logTrace("DoGenericHandlerTest");
    boolean pass = true;
    boolean fault = true;
    try {
      TestUtil.logMsg("This is a test to verify the JAXRPC Soap Message"
          + " Handler Functionality");
      TestUtil.logMsg("Register Client-Side handlers: "
          + "ClientHandler1,ClientHandler2,ClientHandler3");
      TestUtil.logMsg("Register Server-Side handlers: "
          + "ServeServerer1,ServerHandler2,ServerHandler3");

      // HandlerRegistry hr = svc.getHandlerRegistry();
      String serverSideMsgs = port.doGenericHandlerTest();
      String clientSideMsgs = HandlerTracker.get();
      HandlerTracker.purge();
      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg("Dumping Client-Side GenericHandler messages");
      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg(clientSideMsgs);
      TestUtil.logMsg("Verifying Client-Side GenericHandler usage messages");
      if (!VerifyGenericHandler("client", clientSideMsgs)) {
        TestUtil.logErr("Client-Side GenericHandler usage (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("Client-Side GenericHandler usage (correct)");
      }

      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg("Dumping Server-Side GenericHandler messages");
      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg(serverSideMsgs);
      TestUtil.logMsg("Verifying Server-Side GenericHandler usage");
      if (!VerifyGenericHandler("server", serverSideMsgs)) {
        TestUtil.logErr("Server-Side GenericHandler usage (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("Server-Side GenericHandler usage (correct)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DoGenericHandlerTest failed", e);
    }

    if (!pass)
      throw new Fault("DoGenericHandlerTest failed");
  }

  private boolean VerifyGenericHandler(String who, String calls) {
    int startIdx = 0;
    int endIdx = 0;
    boolean pass = true;

    if (who.equals("client")) {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (calls.indexOf("ClientHandler1.handleRequest()") == -1) {
        TestUtil.logErr("ClientHandler1.handleRequest() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.handleResponse()") == -1) {
        TestUtil.logErr("ClientHandler2.handleResponse() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler3.handleRequest()") == -1) {
        TestUtil.logErr("ClientHandler3.handleRequest() was not called");
        pass = false;
      }
    } else {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (calls.indexOf("ServerHandler1.handleRequest()") == -1) {
        TestUtil.logErr("ServerHandler1.handleRequest() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler3.handleRequest()") == -1) {
        TestUtil.logErr("ServerHandler3.handleRequest() was not called");
        pass = false;
      }
    }
    return pass;
  }
}
