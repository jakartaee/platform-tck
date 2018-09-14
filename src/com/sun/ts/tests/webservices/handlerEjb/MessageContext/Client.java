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

package com.sun.ts.tests.webservices.handlerEjb.MessageContext;

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
      TestUtil.logMsg("Got InitialContext");
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/messagecontext");
      TestUtil.logMsg("Obtained service");
      port = (Hello) svc.getPort(Hello.class);
      TestUtil.logMsg("Obtained port");
    } catch (Throwable t) {
      t.printStackTrace();
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
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: DoMessageContextTest
   *
   * @assertion_ids: WS4EE:SPEC:35; WS4EE:SPEC:36;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side and
   * server-side soap message handler callbacks are called by the JAXRPC
   * RUNTIME. Verify usage of the MessageContext interface.
   *
   * Description Register 3 client-side and 3 server-side soap message handlers.
   */
  public void DoMessageContextTest() throws Fault {
    TestUtil.logTrace("DoMessageContextTest");
    boolean pass = true;
    boolean fault = true;
    try {
      TestUtil.logMsg("This is a test to verify the JAXRPC Soap Message"
          + " Handler Functionality");
      TestUtil.logMsg("Invoking RPC method port.doMessageContextTest()");
      String serverSideMsgs = port.doMessageContextTest();
      String clientSideMsgs = HandlerTracker.getMessages2();
      HandlerTracker.purge();
      TestUtil.logMsg("Called HandlerTracker.purge()");
      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg("Dumping Client-Side MessageContext messages");
      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg(clientSideMsgs);
      TestUtil.logMsg("Verifying Client-Side MessageContext usage messages");
      if (!VerifyMessageContext("client", clientSideMsgs)) {
        TestUtil.logErr("Client-Side MessageContext usage (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("Client-Side MessageContext usage (correct)");
      }

      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg("Dumping Server-Side MessageContext messages");
      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg(serverSideMsgs);
      TestUtil.logMsg("Verifying Server-Side MessageContext usage");
      if (!VerifyMessageContext("server", serverSideMsgs)) {
        TestUtil.logErr("Server-Side MessageContext usage (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("Server-Side MessageContext usage (correct)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DoMessageContextTest failed", e);
    }

    if (!pass)
      throw new Fault("DoMessageContextTest failed");
  }

  private boolean VerifyMessageContext(String who, String calls) {
    int startIdx = 0;
    int endIdx = 0;
    boolean pass = true;

    if (who.equals("client")) {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (calls
          .indexOf("MessageContext.setProperty(ClientReqProp1,Value1)") == -1) {
        TestUtil.logErr("MessageContext.setProperty(ClientReqProp1,"
            + "Value1) was not called");
        pass = false;
      }
      if (calls
          .indexOf("MessageContext.setProperty(ClientReqProp2,Value2)") == -1) {
        TestUtil.logErr("MessageContext.setProperty(ClientReqProp2,"
            + "Value1) was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.setProperty(ClientRespProp1,Value1)") == -1) {
        TestUtil.logErr("MessageContext.setProperty(ClientRespProp2,"
            + "Value1) was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.setProperty(ClientRespProp2,Value2)") == -1) {

        TestUtil.logErr("MessageContext.setProperty(ClientRespProp2,"
            + "Value1) was not called");
        pass = false;
      }
      if (calls
          .indexOf("MessageContext.getProperty(ClientReqProp1)=Value1") == -1) {
        TestUtil.logErr("MessageContext.getProperty(ClientReqProp1)"
            + "=Value1 was not called");
        pass = false;
      }
      if (calls
          .indexOf("MessageContext.getProperty(ClientReqProp2)=Value2") == -1) {
        TestUtil.logErr("MessageContext.getProperty(ClientReqProp2)"
            + "=Value2 was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.getProperty(ClientRespProp1)=Value1") == -1) {
        TestUtil.logErr("MessageContext.getProperty(ClientRespProp1)"
            + "=Value1 was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.getProperty(ClientRespProp2)=Value2") == -1) {
        TestUtil.logErr("MessageContext.getProperty(ClientRespProp2)"
            + "=Value2 was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.removeProperty(ClientReqProp1)=true") == -1) {
        TestUtil.logErr("MessageContext.removeProperty(ClientReqProp1)"
            + "=true was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.removeProperty(ClientReqProp2)=true") == -1) {
        TestUtil.logErr("MessageContext.removeProperty(ClientReqProp2)"
            + "=true was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.removeProperty(ClientRespProp1)=true") == -1) {
        TestUtil.logErr("MessageContext.removeProperty(ClientRespProp1)"
            + "=true was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.removeProperty(ClientRespProp2)=true") == -1) {
        TestUtil.logErr("MessageContext.removeProperty(ClientRespProp2)"
            + "=true was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.containsProperty(ClientReqProp1)=true") == -1) {
        TestUtil.logErr("MessageContext.containsProperty(ClientReqProp1)=true"
            + " was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.containsProperty(ClientReqProp2)=true") == -1) {
        TestUtil.logErr("MessageContext.containsProperty(ClientReqProp2)=true"
            + " was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.containsProperty(ClientRespProp1)=true") == -1) {
        TestUtil.logErr("MessageContext.containsProperty(ClientRespProp1)=true"
            + " was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.containsProperty(ClientRespProp2)=true") == -1) {
        TestUtil.logErr("MessageContext.containsProperty(ClientRespProp2)=true"
            + " was not called");
        pass = false;
      }
      if (calls.indexOf("MessageContext.getPropertyNames()") == -1) {
        TestUtil
            .logErr("MessageContext.getPropertyNames()" + " was not called");
        pass = false;
      }
      if (calls.indexOf("MessageContext.getPropertyNames()") == -1) {
        TestUtil
            .logErr("MessageContext.getPropertyNames()" + " was not called");
        pass = false;
      }
      if (calls.indexOf("MessageContext.getPropertyNames()") == -1) {
        TestUtil
            .logErr("MessageContext.getPropertyNames()" + " was not called");
        pass = false;
      }
      if (calls.indexOf("MessageContext.getPropertyNames()") == -1) {
        TestUtil
            .logErr("MessageContext.getPropertyNames()" + " was not called");
        pass = false;
      }
    } else {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (calls
          .indexOf("MessageContext.setProperty(ServerReqProp1,Value1)") == -1) {
        TestUtil.logErr("MessageContext.setProperty(ServerReqProp1,"
            + "Value1) was not called");
        pass = false;
      }
      if (calls
          .indexOf("MessageContext.setProperty(ServerReqProp2,Value2)") == -1) {
        TestUtil.logErr("MessageContext.setProperty(ServerReqProp2,"
            + "Value1) was not called");
        pass = false;
      }
      if (calls
          .indexOf("MessageContext.getProperty(ServerReqProp1)=Value1") == -1) {
        TestUtil.logErr("MessageContext.getProperty(ServerReqProp1)"
            + "=Value1 was not called");
        pass = false;
      }
      if (calls
          .indexOf("MessageContext.getProperty(ServerReqProp2)=Value2") == -1) {
        TestUtil.logErr("MessageContext.getProperty(ServerReqProp2)"
            + "=Value2 was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.removeProperty(ServerReqProp1)=true") == -1) {
        TestUtil.logErr("MessageContext.removeProperty(ServerReqProp1)"
            + "=true was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.removeProperty(ServerReqProp2)=true") == -1) {
        TestUtil.logErr("MessageContext.removeProperty(ServerReqProp2)"
            + "=true was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.containsProperty(ServerReqProp1)=true") == -1) {
        TestUtil.logErr("MessageContext.containsProperty(ServerReqProp1)=true"
            + " was not called");
        pass = false;
      }
      if (calls.indexOf(
          "MessageContext.containsProperty(ServerReqProp2)=true") == -1) {
        TestUtil.logErr("MessageContext.containsProperty(ServerReqProp2)=true"
            + " was not called");
        pass = false;
      }
      if (calls.indexOf("MessageContext.getPropertyNames()") == -1) {
        TestUtil
            .logErr("MessageContext.getPropertyNames()" + " was not called");
        pass = false;
      }
      if (calls.indexOf("MessageContext.getPropertyNames()") == -1) {
        TestUtil
            .logErr("MessageContext.getPropertyNames()" + " was not called");
        pass = false;
      }
      return pass;
    }
    return pass;
  }
}
