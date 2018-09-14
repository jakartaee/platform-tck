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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_handler_soap.SOAPMessageContext;

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
  private Service service = null;

  private HandlerRegistry hr = null;

  private HandlerChain hc = null;

  private Properties props = null;

  Hello port = null;

  Service svc = null;

  private void getStub() throws Exception {
    /* Lookup service then obtain port */
    try {
      InitialContext ic = new InitialContext();
      TestUtil.logMsg("Obtained InitialContext");
      TestUtil.logMsg("Lookup java:comp/env/service/soapmessagecontext");
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/soapmessagecontext");
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
   * @testName: DoSOAPMessageContextTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:144; JAXRPC:JAVADOC:145; JAXRPC:JAVADOC:147;
   * JAXRPC:JAVADOC:146; JAXRPC:SPEC:44; WS4EE:SPEC:35; WS4EE:SPEC:36;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side and
   * server-side soap message handler callbacks are called by the JAXRPC
   * RUNTIME. Verify usage of the SOAPMessageContext interface.
   *
   * Description Register 3 client-side and 3 server-side soap message handlers.
   */
  public void DoSOAPMessageContextTest() throws Fault {
    TestUtil.logTrace("DoSOAPMessageContextTest");
    boolean pass = true;
    boolean fault = true;
    try {
      TestUtil.logMsg("This is a test to verify the JAXRPC Soap Message"
          + " Handler Functionality");
      TestUtil.logMsg("Invoking RPC method port.doSOAPMessageContextTest()");
      HandlerTracker.purge();
      String serverSideMsgs = port.doSOAPMessageContextTest();
      String clientSideMsgs = HandlerTracker.getMessages3();
      TestUtil.logMsg("-----------------------------------------------");
      TestUtil.logMsg("Dumping Client-Side SOAPMessageContext messages");
      TestUtil.logMsg("-----------------------------------------------");
      TestUtil.logMsg(clientSideMsgs);
      TestUtil
          .logMsg("Verifying Client-Side SOAPMessageContext usage messages");
      if (!VerifySOAPMessageContext("client", clientSideMsgs)) {
        TestUtil.logErr("Client-Side SOAPMessageContext usage (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("Client-Side SOAPMessageContext usage (correct)");
      }

      TestUtil.logMsg("-----------------------------------------------");
      TestUtil.logMsg("Dumping Server-Side SOAPMessageContext messages");
      TestUtil.logMsg("-----------------------------------------------");
      TestUtil.logMsg(serverSideMsgs);
      TestUtil.logMsg("Verifying Server-Side SOAPMessageContext usage");
      if (!VerifySOAPMessageContext("server", serverSideMsgs)) {
        TestUtil.logErr("Server-Side SOAPMessageContext usage (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("Server-Side SOAPMessageContext usage (correct)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DoSOAPMessageContextTest failed", e);
    }

    if (!pass)
      throw new Fault("DoSOAPMessageContextTest failed");
  }

  private boolean VerifySOAPMessageContext(String who, String calls) {
    int startIdx = 0;
    int endIdx = 0;
    boolean pass = true;

    if (who.equals("client")) {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (calls.indexOf(
          "SOAPMessageContext.getMessage(ClientHandler1Request)") == -1) {
        TestUtil.logErr("SOAPMessageContext.getMessage("
            + "ClientHandler1Request) was not called");
        pass = false;
      }
      if (calls.indexOf(
          "SOAPMessageContext.setMessage(ClientHandler1Request)") == -1) {
        TestUtil.logErr("SOAPMessageContext.setMessage("
            + "ClientHandler1Request) was not called");
        pass = false;
      }
      if (calls.indexOf(
          "SOAPMessageContext.getMessage(ClientHandler1Response)") == -1) {
        TestUtil.logErr("SOAPMessageContext.getMessage("
            + "ClientHandler1Response) was not called");
        pass = false;
      }
      if (calls.indexOf(
          "SOAPMessageContext.setMessage(ClientHandler1Response)") == -1) {
        TestUtil.logErr("SOAPMessageContext.setMessage("
            + "ClientHandler1Response) was not called");
        pass = false;
      }
    } else {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (calls.indexOf(
          "SOAPMessageContext.getMessage(ServerHandler1Request)") == -1) {
        TestUtil.logErr("SOAPMessageContext.getMessage("
            + "ServerHandler1Request) was not called");
        pass = false;
      }
      if (calls.indexOf(
          "SOAPMessageContext.setMessage(ServerHandler1Request)") == -1) {
        TestUtil.logErr("SOAPMessageContext.setMessage("
            + "ServerHandler1Request) was not called");
        pass = false;
      }
    }
    return pass;
  }
}
