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

package com.sun.ts.tests.webservices.handlerEjb.HandlerLifecycle;

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
   * @assertion_ids: WS4EE:SPEC:159
   *
   * @test_Strategy: If a JAXRPCException is thrown from a Handler instance
   * destroy must be called upon it. This checks JSR 109 specification section
   * 6.2.2.1.
   */
  public void DoHandlerTest1() throws Fault {
    String title = "DoHandlerTest1";
    TestUtil.logMsg(title);
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;

    try {
      InitialContext ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerlifecycletest");
      port = (Hello) svc.getPort(Hello.class);
      // Loop through all of the Handler calls (request/response on
      // client/server)
      // and cause failure... lastly, let it work.. and ensure unique instance
      // on
      // each call. We can't validate destroy() called but we can test to ensure
      // a new instance is used.
      String input = "clientRequestFail";
      String output = port.enventry(input);
      // Should never fail here... since the Client Handler is throwing an error
      pass = false;
      if (output.compareTo("reusedHandler") == 0)
        pass = false;
      System.out.println(title + "w/results of " + output);
    } catch (javax.naming.NamingException ne) {
      ne.printStackTrace();
      pass = false;
    } catch (javax.xml.rpc.ServiceException se) {
      se.printStackTrace();
      pass = false;
    } catch (RemoteException t) {
      pass = true;
    }

    if (!pass)
      throw new Fault(title + " failed");
  }

  /*
   * @testName: DoHandlerTest2
   *
   * @assertion_ids: WS4EE:SPEC:159
   *
   * @test_Strategy: If a JAXRPCException is thrown from a Handler instance
   * destroy must be called upon it. This checks JSR 109 specification section
   * 6.2.2.1.
   */
  public void DoHandlerTest2() throws Fault {
    String title = "DoHandlerTest2";
    TestUtil.logMsg(title);
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;

    try {
      InitialContext ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerlifecycletest");
      port = (Hello) svc.getPort(Hello.class);
      // Loop through all of the Handler calls (request/response on
      // client/server)
      // and cause failure... lastly, let it work.. and ensure unique instance
      // on
      // each call. We can't validate destroy() called but we can test to ensure
      // a new instance is used.
      String input = "clientResponseFail";
      String output = port.enventry(input);
      // Should never fail here.. Since the Client Handler is throwing an error
      pass = false;
      if (output.compareTo("reusedHandler") == 0)
        pass = false;
      System.out.println(title + "w/results of " + output);
    } catch (javax.naming.NamingException ne) {
      ne.printStackTrace();
      pass = false;
    } catch (javax.xml.rpc.ServiceException se) {
      se.printStackTrace();
      pass = false;
    } catch (RemoteException t) {
      pass = true;
    }

    if (!pass)
      throw new Fault(title + " failed");
  }

  /*
   * @testName: DoHandlerTest3
   *
   * @assertion_ids: WS4EE:SPEC:159
   *
   * @test_Strategy: If a JAXRPCException is thrown from a Handler instance
   * destroy must be called upon it. This checks JSR 109 specification section
   * 6.2.2.1.
   */
  public void DoHandlerTest3() throws Fault {
    String title = "DoHandlerTest3";
    TestUtil.logMsg(title);
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;

    try {
      InitialContext ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerlifecycletest");
      port = (Hello) svc.getPort(Hello.class);
      // Loop through all of the Handler calls (request/response on
      // client/server)
      // and cause failure... lastly, let it work.. and ensure unique instance
      // on
      // each call. We can't validate destroy() called but we can test to ensure
      // a new instance is used.
      String input = "serverRequestFail";
      String output = port.enventry(input);
      // Should never get here ... since the Server Handler is throwing an error
      pass = false;
      if (output.compareTo("reusedHandler") == 0)
        pass = false;
      System.out.println(title + "w/results of " + output);
    } catch (javax.naming.NamingException ne) {
      ne.printStackTrace();
      pass = false;
    } catch (javax.xml.rpc.ServiceException se) {
      se.printStackTrace();
      pass = false;
    } catch (RemoteException t) {
      pass = true;
    } catch (Throwable t) {
      t.printStackTrace();
    }

    if (!pass)
      throw new Fault(title + " failed");
  }

  /*
   * @testName: DoHandlerTest4
   *
   * @assertion_ids: WS4EE:SPEC:159
   *
   * @test_Strategy: If a JAXRPCException is thrown from a Handler instance
   * destroy must be called upon it. This checks JSR 109 specification section
   * 6.2.2.1.
   */
  public void DoHandlerTest4() throws Fault {
    String title = "DoHandlerTest4";
    TestUtil.logMsg(title);
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;

    try {
      InitialContext ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerlifecycletest");
      port = (Hello) svc.getPort(Hello.class);
      // Loop through all of the Handler calls (request/response on
      // client/server)
      // and cause failure... lastly, let it work.. and ensure unique instance
      // on
      // each call. We can't validate destroy() called but we can test to ensure
      // a new instance is used.
      String input = "serverResponseFail";
      String output = port.enventry(input);
      // Should never get here... since Server Handler is throwing an error
      pass = false;
      if (output.compareTo("reusedHandler") == 0)
        pass = false;
      System.out.println(title + "w/results of " + output);
    } catch (javax.naming.NamingException ne) {
      ne.printStackTrace();
      pass = false;
    } catch (javax.xml.rpc.ServiceException se) {
      se.printStackTrace();
      pass = false;
    } catch (RemoteException t) {
      pass = true;
    } catch (Throwable t) {
      t.printStackTrace();
    }

    if (!pass)
      throw new Fault(title + " failed");
  }

  /*
   * @testName: DoHandlerTest5
   *
   * @assertion_ids: WS4EE:SPEC:159
   *
   * @test_Strategy: If a JAXRPCException is thrown from a Handler instance
   * destroy must be called upon it. This checks JSR 109 specification section
   * 6.2.2.1.
   */
  public void DoHandlerTest5() throws Fault {
    String title = "DoHandlerTest5";
    TestUtil.logMsg(title);
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;

    try {
      InitialContext ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerlifecycletest");
      port = (Hello) svc.getPort(Hello.class);
      // Loop through all of the Handler calls (request/response on
      // client/server)
      // and cause failure... lastly, let it work.. and ensure unique instance
      // on
      // each call. We can't validate destroy() called but we can test to ensure
      // a new instance is used.
      String input = "passes";
      String output = port.enventry(input);
      if (output.compareTo("reusedHandler") == 0)
        pass = false;
      System.out.println(title + "w/results of " + output);
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault(title + " failed");
  }

}
