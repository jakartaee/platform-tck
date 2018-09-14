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

package com.sun.ts.tests.webservices.handlerEjb.HandlerFlow;

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
import javax.xml.rpc.server.ServiceLifecycle;
import javax.naming.InitialContext;

import com.sun.ts.tests.jaxrpc.common.*;

public class Client extends ServiceEETest {

  Hello port = null;

  Hello2 port2 = null;

  Service svc = null;
  // Get Port and Stub access via porting layer interface

  private void getStub() throws Exception {
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
   * @assertion_ids: WS4EE:SPEC:168; WS4EE:SPEC:162; WS4EE:SPEC:90;
   * WS4EE:SPEC:91; WS4EE:SPEC:174;
   *
   * @test_Strategy: A Handler chain is processed according to the JAX-RPC
   * specificaiton section 12.2.2. The process order defaults to the order the
   * handlers are defined in the deployment descriptor and follow the JAX-RPC
   * specification section 12.1.4 processing order.
   */
  public void DoHandlerTest1() throws Fault {
    TestUtil.logMsg("DoHandlerTest1");
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;

    try {
      InitialContext ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerflowtest");

      // Try Hello2 port
      HandlerTracker.clearHandlers();
      port2 = (Hello2) svc.getPort(Hello2.class);
      String output = port2.hello("You");
      Vector v = HandlerTracker.getHandlers();
      int vsize = v.size();
      if (vsize != 3) {
        TestUtil.logMsg("Hello2Port: Did not find required number of handlers");
        pass = false;
      } else {
        handlers = v.toArray();
        if (((handlers[0].toString()).indexOf("ClientHandler1") == -1)
            || ((handlers[1].toString()).indexOf("ClientHandler3") == -1)
            || ((handlers[2].toString()).indexOf("ClientHandler5") == -1)) {
          TestUtil.logMsg("Handlers are not in correct processing order");
          pass = false;
        }
      }
      HandlerTracker.clearHandlers();

      // Try Hello port
      port = (Hello) svc.getPort(Hello.class);
      output = port.hello("You");
      HandlerTracker.removeHandlers("ServerHandler");
      v = HandlerTracker.getHandlers();
      vsize = v.size();
      if (vsize != 3) {
        TestUtil.logMsg("HelloPort: Did not find required number of handlers");
        pass = false;
      } else {
        handlers = v.toArray();
        if (((handlers[0].toString()).indexOf("ClientHandler1") == -1)
            || ((handlers[1].toString()).indexOf("ClientHandler2") == -1)
            || ((handlers[2].toString()).indexOf("ClientHandler3") == -1)) {
          TestUtil.logMsg("Handlers are not in correct processing order");
          pass = false;
        }
      }
      v.clear();

    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("DoHandlerTest1 failed");
  }

  /*
   * @testName: DoHandlerTest2
   *
   * @assertion_ids: WS4EE:SPEC:105; WS4EE:SPEC:81; WS4EE:SPEC:198;
   * WS4EE:SPEC:90; WS4EE:SPEC:91; WS4EE:SPEC:162; WS4EE:SPEC:163;
   *
   * @test_Strategy: The container must call the init() method within the
   * context of a Port component's environment. The container must ensure the
   * Port component's env-entrys are setup for the init method to access
   *
   */
  public void DoHandlerTest2() throws Fault {
    TestUtil.logMsg("DoHandlerTest2");
    boolean pass = true;
    boolean fault = false;
    InitialContext ic = null;
    // String shadow = "shadow";

    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerflowtest");
      port = (Hello) svc.getPort(Hello.class);
      String input = "Padding";
      String output = port.enventry(input);
      TestUtil.logMsg("Return value = " + output);
      // if ((output.compareTo("badstring") == 0) |
      // (output.compareTo("badinteger") == 0) |
      // (output.compareTo("badstringbadinteger") == 0))
      if (output.indexOf("shadow:8:shadow:8") < 0)
        pass = false;
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("DoHandlerTest2 failed");
  }

  /*
   * @testName: DoHandlerTest3
   *
   * @assertion_ids: WS4EE:SPEC:80; WS4EE:SPEC:81; WS4EE:SPEC:162;
   * WS4EE:SPEC:90; WS4EE:SPEC:91;
   *
   * @test_Strategy: Handlers must be able to transform the SOAP header. One
   * example is the addition of a SOAP header for application specific
   * information.
   */
  public void DoHandlerTest3() throws Fault {
    TestUtil.logMsg("DoHandlerTest3");
    boolean pass = true;
    boolean fault = false;
    InitialContext ic = null;

    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerflowtest");
      port = (Hello) svc.getPort(Hello.class);
      String input = "Shadow";
      String output = port.hello(input);
      TestUtil.logMsg("Return value = " + output);
      // if (output.compareTo("HeaderAdded") != 0)
      if (output.indexOf("::SOAP header was added") < 0)
        pass = false;
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("DoHandlerTest3 failed");
  }

  /*
   * @testName: DoHandlerTest4
   *
   * @assertion_ids: WS4EE:SPEC:166; WS4EE:SPEC:81; WS4EE:SPEC:82;
   * WS4EE:SPEC:162; WS4EE:SPEC:90; WS4EE:SPEC:91;
   *
   * @test_Strategy: A SOAPMessageContext handler may add or remove headers form
   * the SOAP message. A SOAPMessage Context Handler may modify the header of a
   * SOAP message if it is not mapped to a parameter or if the modification does
   * not change value type of the parameter if it is mapped to a parameter. A
   * Handler may modify part values of a message if the modification does not
   * change the value type.
   *
   * 
   */

  public void DoHandlerTest4() throws Fault {
    TestUtil.logMsg("DoHandlerTest4");
    boolean pass = true;
    boolean fault = false;
    InitialContext ic = null;

    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerflowtest");
      port = (Hello) svc.getPort(Hello.class);
      String input = "Shadow";
      String output = port.howdy(input);
      TestUtil.logMsg("Return value = " + output);
      // if (output.compareTo("HeaderRemoved") != 0)
      if (output.indexOf("::SOAP header is removed - Indeed") < 0)
        pass = false;
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("DoHandlerTest4 failed");
  }

  /*
   * @testName: DoHandlerTest5
   *
   * @assertion_ids: WS4EE:SPEC:101; WS4EE:SPEC:85; WS4EE:SPEC:162;
   * WS4EE:SPEC:90; WS4EE:SPEC:91;
   *
   * @test_Strategy: A Handler implementation muts use the MessageContext to
   * pass information to other Handler implementations in the same Handler chain
   * and, in the case of the JAX-RPC service endpoint, to the service
   * implementation bean.
   *
   */

  public void DoHandlerTest5() throws Fault {
    TestUtil.logMsg("DoHandlerTest5");
    boolean pass = true;
    boolean yes = true;
    try {
      InitialContext ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerflowtest");
      port = (Hello) svc.getPort(Hello.class);
      yes = port.getMessageContextTest();
      if (!yes) {
        TestUtil.logMsg("did not get message context");
        pass = false;
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("DoHandlerTest5 failed");
  }

  /*
   * @testName: DoHandlerTest6
   *
   * @assertion_ids: WS4EE:SPEC:107; WS4EE:SPEC:162; WS4EE:SPEC:90;
   * WS4EE:SPEC:91;
   *
   * @test_Strategy: The container must share the same MessageContext instance
   * across all Handler instances and the target endpoint that are invoked
   * during a single request and resonse or fault processing on a specific node
   */

  public void DoHandlerTest6() throws Fault {
    TestUtil.logMsg("DoHandlerTest6");
    boolean pass = true;
    boolean fault = false;
    InitialContext ic = null;

    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerflowtest");
      port = (Hello) svc.getPort(Hello.class);
      String input = "Shadow";
      String output = port.hi(input);
      TestUtil.logMsg("Return value = " + output);
      if (output.indexOf(":Context Not Shared") >= 0)
        pass = false;
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("DoHandlerTest6 failed");
  }

}
