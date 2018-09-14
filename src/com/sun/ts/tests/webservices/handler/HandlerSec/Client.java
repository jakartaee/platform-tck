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

package com.sun.ts.tests.webservices.handler.HandlerSec;

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

  Service svc = null;

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

  private boolean clearTracker(Service svc) throws Exception {
    TestQuery port = (TestQuery) svc.getPort(TestQuery.class);
    return port.clearTracker();
  }

  private ClassLoaderInfo getClassLoaderInfo(Service svc) throws Exception {
    TestQuery port = (TestQuery) svc.getPort(TestQuery.class);
    return port.getClassLoaderInfo();
  }

  /*
   * @testName: NoSecurityTest
   *
   * @assertion_ids: WS4EE:SPEC:157
   *
   * @test_Strategy: Ensure that ClassLoader in Handler and Implementation are
   * equivalent.
   */
  public void NoSecurityTest() throws Fault {
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "NoSecurityTest";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);

      // Clear tracker information
      pass = clearTracker(svc);

      // Get the port
      if (pass) {
        port = (TestNoSec) svc.getPort(TestNoSec.class);
        if (port == null) {
          TestUtil.logMsg("Failed to get port");
          pass = false;
        }
      } else {
        TestUtil.logMsg("Could not clear HandlerTracker information");
        pass = false;
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (pass) {
      try {
        // Run the test
        port.testingNoSec();

        ClassLoaderInfo info = getClassLoaderInfo(svc);

        // Expect Same ClassLoaders
        if (info == null) {
          pass = false;
          TestUtil.logMsg("No ClassLoaderInfo was returned");
        } else if (!info.isHasHandlerClassLoader()) {
          pass = false;
          TestUtil.logMsg("ServiceHandler was not called");
        } else if (!info.isHasBeanClassLoader()) {
          pass = false;
          TestUtil.logMsg("Implementation was not called");
        } else if (!info.isSameClassLoaders()) {
          pass = false;
          TestUtil.logMsg("ClassLoaders are not the same.");
        }
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        throw new Fault(test + " failed", e);
      }
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * @testName: AuthorizedTest
   *
   * @assertion_ids: WS4EE:SPEC:161
   *
   * @test_Strategy: Make sure handler is run after authorization.
   */
  public void AuthorizedTest() throws Fault {
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;
    TestAuth port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "AuthorizedTest";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);

      // Clear tracker information
      pass = clearTracker(svc);

      // Get the port
      if (pass) {
        port = (TestAuth) svc.getPort(TestAuth.class);
        if (port == null) {
          TestUtil.logMsg("Failed to get port");
          pass = false;
        }
      } else {
        TestUtil.logMsg("Could not clear HandlerTracker information");
        pass = false;
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (pass) {
      try {
        // Run the test
        port.testingAuth();

        ClassLoaderInfo info = getClassLoaderInfo(svc);

        // Expect Same ClassLoaders
        if (info == null) {
          pass = false;
          TestUtil.logMsg("No ClassLoaderInfo was returned");
        } else if (!info.isHasHandlerClassLoader()) {
          pass = false;
          TestUtil.logMsg("ServiceHandler was not called");
        } else if (!info.isHasBeanClassLoader()) {
          pass = false;
          TestUtil.logMsg("Implementation was not called");
        } else if (!info.isSameClassLoaders()) {
          pass = false;
          TestUtil.logMsg("ClassLoaders are not the same.");
        }
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        throw new Fault(test + " failed", e);
      }
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * @testName: UnAuthorizedTest
   *
   * @assertion_ids: WS4EE:SPEC:161
   *
   * @test_Strategy: Make sure handler is not run when authorization fails.
   */
  public void UnAuthorizedTest() throws Fault {
    boolean pass = true;
    boolean fault = false;
    Object[] handlers = null;
    TestUnAuth port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "UnAuthorizedTest";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);

      // Clear tracker information
      pass = clearTracker(svc);

      // Get the port
      if (pass) {
        port = (TestUnAuth) svc.getPort(TestUnAuth.class);
        if (port == null) {
          TestUtil.logMsg("Failed to get port");
          pass = false;
        }
      } else {
        TestUtil.logMsg("Could not clear HandlerTracker information");
        pass = false;
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (pass) {
      try {
        // Run the test
        port.testingUnAuth();

        TestUtil.logMsg(
            "Call to testingUnAuth completed " + "expected RemoteException");
        pass = false;
      } catch (RemoteException ex) {
        // Expected Remote Exception
        ClassLoaderInfo info;
        try {
          info = getClassLoaderInfo(svc);
        } catch (Exception e) {
          pass = false;
          TestUtil.logErr("Caught exception: " + e.getMessage());
          TestUtil.printStackTrace(e);
          throw new Fault(test + " failed", e);
        }

        // Expect No ClassLoaders
        if (info == null) {
          pass = false;
          TestUtil.logMsg("No ClassLoaderInfo was returned");
        } else if (info.isHasHandlerClassLoader()) {
          pass = false;
          TestUtil.logMsg("ServiceHandler was called");
        } else if (info.isHasBeanClassLoader()) {
          pass = false;
          TestUtil.logMsg("Implementation was called");
        }
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        throw new Fault(test + " failed", e);
      }
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * @testName: MessageChangeTest1
   *
   * @assertion_ids: WS4EE:SPEC:82;
   *
   * @test_Strategy: The message is not changed by the service handler. Verify
   * correct behavior.
   */
  public void MessageChangeTest1() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest1";
    String arg = "readonly";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        if (!result.equals(arg)) {
          TestUtil
              .logMsg("Result was (" + result + ") but expected (" + arg + ")");
          pass = false;
        }
      }
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * @testName: MessageChangeTest2
   *
   * @assertion_ids: WS4EE:SPEC:81;
   *
   * @test_Strategy: The value of the argument is changed by the server request
   * handler. Verify correct behavior.
   */
  public void MessageChangeTest2() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest2";
    String arg = "change_value_request";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        if (!result.equals(arg + "_PASS")) {
          TestUtil
              .logMsg("Result was (" + result + ") but expected (" + arg + ")");
          pass = false;
        }
      }
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * @testName: MessageChangeTest3
   *
   * @assertion_ids: WS4EE:SPEC:81;
   *
   * @test_Strategy: The value of the argument is changed by the server response
   * handler. Verify correct behavior.
   */
  public void MessageChangeTest3() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest3";
    String arg = "change_value_response";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        if (!result.equals(arg + "_PASS")) {
          TestUtil
              .logMsg("Result was (" + result + ") but expected (" + arg + ")");
          pass = false;
        }
      }
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * @testName: MessageChangeTest4
   *
   * @assertion_ids: WS4EE:SPEC:93; WS4EE:SPEC:94;
   * 
   * @test_Strategy: The message (operation) element name is changed by the
   * request handler. Verify MarshalException or UnmarshalException is thrown.
   */
  public void MessageChangeTest4() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest4";
    String arg = "change_message_request";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        TestUtil.logMsg("port.echoString(" + arg + ") returned " + result);
        pass = false;
      }
    } catch (java.rmi.ServerException e) {
      TestUtil.logMsg("Caught expected java.rmi.ServerException");
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg(
          "Caught expected, but more generic, java.rmi.RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * #testName: MessageChangeTest5
   *
   * #assertion_ids: WS4EE:SPEC:93; WS4EE:SPEC:95;
   *
   * #test_Strategy: The return message name is changed by the response handler.
   * Verify MarshalException or UnmarshalException is thrown
   */
  public void MessageChangeTest5() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest5";
    String arg = "change_message_response";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        TestUtil.logMsg("port.echoString(" + arg + ") returned " + result);
        pass = false;
      }
    } catch (java.rmi.ServerException e) {
      TestUtil.logMsg("Caught expected java.rmi.ServerException");
    } catch (javax.xml.rpc.soap.SOAPFaultException e) {
      TestUtil.logMsg("Caught expected javax.xml.rpc.soap.SOAPFaultException");
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg(
          "Caught expected, but more generic, java.rmi.RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * @testName: MessageChangeTest6
   *
   * @assertion_ids: WS4EE:SPEC:93;
   *
   * @test_Strategy: The argument element name is changed by the request
   * handler. Verify MarshalException or UnmarshalException is thrown.
   */
  public void MessageChangeTest6() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest6";
    String arg = "change_element_request";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        TestUtil.logMsg("port.echoString(" + arg + ") returned " + result);
        pass = false;
      }
    } catch (java.rmi.ServerException e) {
      TestUtil.logMsg("Caught expected java.rmi.ServerException");
    } catch (javax.xml.rpc.soap.SOAPFaultException e) {
      TestUtil.logMsg("Caught expected javax.xml.rpc.soap.SOAPFaultException");
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg(
          "Caught expected, but more generic, java.rmi.RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * #testName: MessageChangeTest7
   *
   * #assertion_ids: WS4EE:SPEC:93;
   *
   * #test_Strategy: The argument element name is changed by the response
   * handler. Verify MarshalException or UnmarshalException is thrown
   */
  public void MessageChangeTest7() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest7";
    String arg = "change_element_response";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        TestUtil.logMsg("port.echoString(" + arg + ") returned " + result);
        pass = false;
      }
    } catch (java.rmi.ServerException e) {
      TestUtil.logMsg("Caught expected java.rmi.ServerException");
    } catch (javax.xml.rpc.soap.SOAPFaultException e) {
      TestUtil.logMsg("Caught expected javax.xml.rpc.soap.SOAPFaultException");
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg(
          "Caught expected, but more generic, java.rmi.RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * @testName: MessageChangeTest8
   *
   * @assertion_ids: WS4EE:SPEC:93; WS4EE:SPEC:94;
   *
   * @test_Strategy: Another argument is added by the request handler. Verify
   * MarshalException or UnmarshalException is thrown
   */
  public void MessageChangeTest8() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest8";
    String arg = "add_element_request";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        TestUtil.logMsg("port.echoString(" + arg + ") returned " + result);
        pass = false;
      }
    } catch (java.rmi.ServerException e) {
      TestUtil.logMsg("Caught expected java.rmi.ServerException");
    } catch (javax.xml.rpc.soap.SOAPFaultException e) {
      TestUtil.logMsg("Caught expected javax.xml.rpc.soap.SOAPFaultException");
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg(
          "Caught expected, but more generic, java.rmi.RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }

  /*
   * #testName: MessageChangeTest9
   *
   * #assertion_ids: WS4EE:SPEC:93; WS4EE:SPEC:95;
   *
   * #test_Strategy: Another argument is added by the response handler. Verify
   * MarshalException or UnmarshalException is thrown
   */
  public void MessageChangeTest9() throws Fault {
    boolean pass = true;
    TestNoSec port = null;
    String lookup = "java:comp/env/service/handlersec";
    InitialContext ic = null;
    String test = "MessageChangeTest9";
    String arg = "add_element_response";

    TestUtil.logMsg(test);
    try {
      ic = new InitialContext();
      svc = (javax.xml.rpc.Service) ic.lookup(lookup);
      port = (TestNoSec) svc.getPort(TestNoSec.class);
      if (port == null) {
        TestUtil.logMsg("Failed to get port");
        pass = false;
      } else {
        String result = port.echoString(arg);
        TestUtil.logMsg("port.echoString(" + arg + ") returned " + result);
        pass = false;
      }
    } catch (java.rmi.ServerException e) {
      TestUtil.logMsg("Caught expected java.rmi.ServerException");
    } catch (javax.xml.rpc.soap.SOAPFaultException e) {
      TestUtil.logMsg("Caught expected javax.xml.rpc.soap.SOAPFaultException");
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg(
          "Caught expected, but more generic, java.rmi.RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg("Caught " + t.getClass() + " (" + t.getMessage() + "(");
      t.printStackTrace();
      throw new Fault(t.toString());
    }
    if (!pass)
      throw new Fault(test + " failed");
  }
}
