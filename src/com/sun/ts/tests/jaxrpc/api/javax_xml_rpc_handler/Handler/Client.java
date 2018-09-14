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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_handler.Handler;

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
import javax.xml.rpc.soap.*;
import javax.xml.namespace.QName;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {

  private Service service = null;

  private HandlerRegistry hr = null;

  private HandlerChain hc = null;

  private Properties props = null;

  Hello port1 = null;

  Hello2 port2 = null;

  private void getStub() throws Exception {
    /* Lookup service then obtain port's */
    try {
      InitialContext ic = new InitialContext();
      TestUtil.logMsg("Obtained InitialContext");
      TestUtil.logMsg("Lookup java:comp/env/service/handler1");
      javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handler1");
      TestUtil.logMsg("Obtained service 1");
      port1 = (Hello) svc.getPort(Hello.class);
      TestUtil.logMsg("Obtained port 1");
      TestUtil.logMsg("Lookup java:comp/env/service/handler2");
      javax.xml.rpc.Service svc2 = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handler2");
      TestUtil.logMsg("Obtained service 2");
      port2 = (Hello2) svc2.getPort(Hello2.class);
      TestUtil.logMsg("Obtained port 2");
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
   * @testName: DoHandlerTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:144; JAXRPC:JAVADOC:145; JAXRPC:JAVADOC:147;
   * WS4EE:SPEC:75; WS4EE:SPEC:86; WS4EE:SPEC:98; WS4EE:SPEC:99; WS4EE:SPEC:106;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side and
   * server-side soap message handler callbacks are called by the JAXRPC
   * RUNTIME. Verify handleRequest()/ handleResponse() callbacks.
   *
   * Description Register 3 client-side and 3 server-side soap message handlers.
   */
  public void DoHandlerTest1() throws Fault {
    TestUtil.logTrace("DoHandlerTest1");
    boolean pass = true;
    boolean fault = false;
    try {
      TestUtil.logMsg("This is a test to verify the JAXRPC Soap Message"
          + " Handler Functionality");
      TestUtil.logMsg("Register Client-Side handlers: "
          + "ClientHandler1,ClientHandler2,ClientHandler3");
      TestUtil.logMsg("Register Server-Side handlers: "
          + "ServeServerer1,ServerHandler2,ServerHandler3");

      TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
      String serverSideMsgs = port1.doHandlerTest1();
      String clientSideMsgs = HandlerTracker.get();
      HandlerTracker.purge();

      TestUtil.logMsg("Verify handleRequest()/handleResponse() callbacks");
      TestUtil.logMsg("Verifying Client-Side JAXRPC-RUNTIME Callbacks");
      if (!VerifyCallBacks("client", fault, clientSideMsgs)) {
        TestUtil.logErr("Client-Side Callbacks are (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("Client-Side Callbacks are (correct)");
      }

      TestUtil.logMsg("Verifying Server-Side JAXRPC-RUNTIME Callbacks");
      if (!VerifyCallBacks("server", fault, serverSideMsgs)) {
        TestUtil.logErr("Server-Side Callbacks are (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("Server-Side Callbacks are (correct)");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DoHandlerTest1 failed", e);
    }

    if (!pass)
      throw new Fault("DoHandlerTest1 failed");
  }

  /*
   * @testName: DoHandlerTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:144; JAXRPC:JAVADOC:145; JAXRPC:JAVADOC:147;
   * JAXRPC:JAVADOC:146; WS4EE:SPEC:75; WS4EE:SPEC:86; WS4EE:SPEC:88;
   * WS4EE:SPEC:98; WS4EE:SPEC:99; WS4EE:SPEC:106;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side and
   * server-side soap message handler callbacks are called by the JAXRPC
   * RUNTIME. Verify behavior when a JAXRPCException exception is thrown from
   * client-side handleRequest() callback method. Should get a Remote- Exception
   * with JAXRPCException wrapped as a nested exception.
   *
   * Description Register 2 client-side and 2 server-side soap message handlers.
   */
  public void DoHandlerTest2() throws Fault {
    TestUtil.logTrace("DoHandlerTest2");
    boolean pass = true;
    boolean fault = true;
    try {
      TestUtil.logMsg("This is a test to verify the JAXRPC Soap Message"
          + " Handler Functionality");
      TestUtil.logMsg("Now invoke RPC method and verify the JAXRPC "
          + "Soap Message Handler Functionality");
      TestUtil.logMsg(
          "Register Client-Side handlers: " + "ClientHandler4,ClientHandler5");
      TestUtil.logMsg(
          "Register Server-Side handlers: " + "ServeServerer4,ServerHandler5");

      TestUtil.logMsg("Invoking RPC method port2.doHandlerTest2()");
      TestUtil.logMsg(
          "Expecting RemoteException to be thrown which wraps a JAXRPCException");
      String serverSideMsgs = null;
      try {
        serverSideMsgs = port2.doHandlerTest2();
        TestUtil.logErr("Did not get expected RemoteException");
        pass = false;
      } catch (RemoteException e) {
        TestUtil.logMsg("Did get expected RemoteException: " + e.getMessage());
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DoHandlerTest2 failed", e);
    }

    if (!pass)
      throw new Fault("DoHandlerTest2 failed");
  }

  private boolean VerifyCallBacks(String who, boolean fault, String calls) {
    int startIdx = 0;
    int endIdx = 0;
    boolean pass = true;

    if (who.equals("client")) {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (fault) {
        if (calls.indexOf("ClientHandler4.handleFault()") == -1) {
          TestUtil.logErr("ClientHandler4.handleFault() was not called");
          pass = false;
        }
        return pass;
      }
      if (calls.indexOf("ClientHandler1.init()") == -1) {
        TestUtil.logErr("ClientHandler1.init() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.init()") == -1) {
        TestUtil.logErr("ClientHandler2.init() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler3.init()") == -1) {
        TestUtil.logErr("ClientHandler3.init() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler1.handleRequest()") == -1) {
        TestUtil.logErr("ClientHandler1.handleRequest() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.handleRequest()") == -1) {
        TestUtil.logErr("ClientHandler2.handleRequest() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler3.handleRequest()") == -1) {
        TestUtil.logErr("ClientHandler3.handleRequest() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler1.handleResponse()") == -1) {
        TestUtil.logErr("ClientHandler1.handleResponse() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.handleResponse()") == -1) {
        TestUtil.logErr("ClientHandler2.handleResponse() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler3.handleResponse()") == -1) {
        TestUtil.logErr("ClientHandler3.handleResponse() was not called");
        pass = false;
      }
    } else {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (fault) {
        if (calls.indexOf("ServerHandler4.handleFault()") == -1) {
          TestUtil.logErr("ServerHandler4.handleFault() was not called");
          pass = false;
        }
        return pass;
      }
      if (calls.indexOf("ServerHandler1.handleRequest()") == -1) {
        TestUtil.logErr("ServerHandler1.handleRequest() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler2.handleRequest()") == -1) {
        TestUtil.logErr("ServerHandler2.handleRequest() was not called");
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
