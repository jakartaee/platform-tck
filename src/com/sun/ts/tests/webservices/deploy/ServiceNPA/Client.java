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

package com.sun.ts.tests.webservices.deploy.ServiceNPA;

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
  // These tests verify behaviour of the Service methods
  // when the development wsdl contains a port that
  // does not have a location attribute.

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private final String NAMESPACEURI = "http://testsservicenpa.org/wsdl";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private QName BAD_PORT_QNAME;

  private QName QNAME_TYPE_STRING;

  private String SERVICE_NAME = "service";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "servicenpa.endpoint.1";

  private String url = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get JAXRPC porting instance");
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = (String) ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
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
    props = p;
    boolean pass = true;

    // Initialize QNAMES used in the test
    SERVICE_QNAME = new QName(NAMESPACEURI, "TestsServiceNPA");
    PORT_QNAME = new QName(NAMESPACEURI, "TestsPort");
    BAD_PORT_QNAME = new QName(NAMESPACEURI, "BadPort");
    QNAME_TYPE_STRING = new QName(Constants.XSD, "string");

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
      getTestURLs();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: CreateCallConstructor1Test1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:41; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:134; WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object using the empty constructor via
   * Service.createCall(). Verify that the Call object was successfully created.
   */
  public void CreateCallConstructor1Test1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor1Test1WithWsdl");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Create Call object via Service.createCall()");
      Call call = service.createCall();
      if (call == null) {
        TestUtil.logErr("createCall() returned null");
        pass = false;
      } else if (!(call instanceof Call)) {
        TestUtil
            .logErr("createCall() did not return " + "instance of Call object");
        pass = false;
      } else {
        TestUtil.logMsg("Call object successfully created");
        TestUtil.logMsg("Fillin rest of Call object and invoke RPC method");
        call = JAXRPC_Util.setCallProperties(call, " ");
        call.setTargetEndpointAddress(url);
        TestUtil.logMsg("Set operation name to InvokeTest2");
        call.setOperationName(new QName(NAMESPACEURI, "invokeTest2"));

        boolean b = call.isParameterAndReturnSpecRequired(
            new QName(NAMESPACEURI, "invokeTest2"));
        if (b) {
          call.setReturnType(QNAME_TYPE_STRING);
          call.addParameter("BigInteger_1", new QName(Constants.XSD, "integer"),
              ParameterMode.IN);
          call.addParameter("BigDecimal_2", new QName(Constants.XSD, "decimal"),
              ParameterMode.IN);
          call.addParameter("QName_3", new QName(Constants.XSD, "QName"),
              ParameterMode.IN);
          call.addParameter("String_4", new QName(Constants.XSD, "string"),
              ParameterMode.IN);
        }
        Object[] params = { JAXRPC_Data.BigInteger_data[0],
            JAXRPC_Data.BigDecimal_data[0], JAXRPC_Data.QName_data[0],
            JAXRPC_Data.String_data[0] };

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"InvokeTest2\")");
        String expected = "passed";
        String response = (String) call.invoke(params);
        if (!response.equals(expected)) {
          TestUtil.logErr("RPC failed - expected \"" + expected
              + "\", received: " + response);
          pass = false;
        } else {
          TestUtil
              .logMsg("RPC passed - received expected response: " + response);
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateCallConstructor1Test1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor1Test1WithWsdl failed");
  }

  /*
   * @testName: CreateCallConstructor2Test1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:41; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:134; WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object using the constructor via
   * Service.createCall(QName). Verify that the Call object was successfully
   * created.
   */
  public void CreateCallConstructor2Test1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor2Test1WithWsdl");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Create Call object via Service.createCall(QName)");
      TestUtil.logMsg("QName = " + PORT_QNAME);
      Call call = service.createCall(PORT_QNAME);
      if (call == null) {
        TestUtil.logErr("createCall() returned null");
        pass = false;
      } else if (!(call instanceof Call)) {
        TestUtil
            .logErr("createCall() did not return " + "instance of Call object");
        pass = false;
      } else {
        TestUtil.logMsg("Call object successfully created");
        TestUtil.logMsg("Fillin rest of Call object and invoke RPC method");
        call = JAXRPC_Util.setCallProperties(call, " ");
        call.setTargetEndpointAddress(url);
        TestUtil.logMsg("Set operation name to InvokeTest2");
        call.setOperationName(new QName(NAMESPACEURI, "invokeTest2"));

        boolean b = call.isParameterAndReturnSpecRequired(
            new QName(NAMESPACEURI, "invokeTest2"));
        if (b) {
          call.setReturnType(QNAME_TYPE_STRING);
          call.addParameter("BigInteger_1", new QName(Constants.XSD, "integer"),
              ParameterMode.IN);
          call.addParameter("BigDecimal_2", new QName(Constants.XSD, "decimal"),
              ParameterMode.IN);
          call.addParameter("QName_3", new QName(Constants.XSD, "QName"),
              ParameterMode.IN);
          call.addParameter("String_4", new QName(Constants.XSD, "string"),
              ParameterMode.IN);
        }
        Object[] params = { JAXRPC_Data.BigInteger_data[0],
            JAXRPC_Data.BigDecimal_data[0], JAXRPC_Data.QName_data[0],
            JAXRPC_Data.String_data[0] };

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"InvokeTest2\")");
        String expected = "passed";
        String response = (String) call.invoke(params);
        if (!response.equals(expected)) {
          TestUtil.logErr("RPC failed - expected \"" + expected
              + "\", received: " + response);
          pass = false;
        } else {
          TestUtil
              .logMsg("RPC passed - received expected response: " + response);
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateCallConstructor2Test1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor2Test1WithWsdl failed");
  }

  /*
   * @testName: CreateCallConstructor3Test1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:41; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:134; WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object using the constructor via
   * Service.createCall(QName, Qname). Verify that the Call object was
   * successfully created.
   */
  public void CreateCallConstructor3Test1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor3Test1WithWsdl");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil
          .logMsg("Create Call object via Service.createCall(QName, QName)");
      QName OPERATION_QNAME = new QName(NAMESPACEURI, "invokeTest2");
      TestUtil
          .logMsg("QName = " + PORT_QNAME + ", Operation = " + OPERATION_QNAME);
      Call call = service.createCall(PORT_QNAME, OPERATION_QNAME);
      if (call == null) {
        TestUtil.logErr("createCall() returned null");
        pass = false;
      } else if (!(call instanceof Call)) {
        TestUtil
            .logErr("createCall() did not return " + "instance of Call object");
        pass = false;
      } else {
        TestUtil.logMsg("Call object successfully created");
        TestUtil.logMsg("Fillin rest of Call object and invoke RPC method");
        call = JAXRPC_Util.setCallProperties(call, " ");
        call.setTargetEndpointAddress(url);

        boolean b = call.isParameterAndReturnSpecRequired(
            new QName(NAMESPACEURI, "invokeTest2"));
        if (b) {
          call.setReturnType(QNAME_TYPE_STRING);
          call.addParameter("BigInteger_1", new QName(Constants.XSD, "integer"),
              ParameterMode.IN);
          call.addParameter("BigDecimal_2", new QName(Constants.XSD, "decimal"),
              ParameterMode.IN);
          call.addParameter("QName_3", new QName(Constants.XSD, "QName"),
              ParameterMode.IN);
          call.addParameter("String_4", new QName(Constants.XSD, "string"),
              ParameterMode.IN);
        }
        Object[] params = { JAXRPC_Data.BigInteger_data[0],
            JAXRPC_Data.BigDecimal_data[0], JAXRPC_Data.QName_data[0],
            JAXRPC_Data.String_data[0] };

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"InvokeTest2\")");
        String expected = "passed";
        String response = (String) call.invoke(params);
        if (!response.equals(expected)) {
          TestUtil.logErr("RPC failed - expected \"" + expected
              + "\", received: " + response);
          pass = false;
        } else {
          TestUtil
              .logMsg("RPC passed - received expected response: " + response);
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateCallConstructor3Test1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor3Test1WithWsdl failed");
  }

  /*
   * @testName: CreateCallConstructor4Test1
   *
   * @assertion_ids: JAXRPC:JAVADOC:39; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:134; WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object using the constructor
   * Service.createCall(QName, String). Verify that the Call object was
   * successfully created.
   */
  public void CreateCallConstructor4Test1() throws Fault {
    TestUtil.logTrace("CreateCallConstructor4Test1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil
          .logMsg("Create Call object via Service.createCall(QName, String)");
      String operation = "invokeTest2";
      TestUtil.logMsg("QName = " + PORT_QNAME + ", Operation = " + operation);
      Call call = service.createCall(PORT_QNAME, operation);
      if (call == null) {
        TestUtil.logErr("createCall(QName, String) returned null");
        pass = false;
      } else if (!(call instanceof Call)) {
        TestUtil.logErr("createCall(QName, String) did not"
            + " return instance of Call object");
        pass = false;
      } else {
        TestUtil.logMsg("Call object successfully created");
        TestUtil.logMsg("Fillin rest of Call object and invoke RPC method");
        call = JAXRPC_Util.setCallProperties(call, " ");
        call.setTargetEndpointAddress(url);

        boolean b = call.isParameterAndReturnSpecRequired(
            new QName(NAMESPACEURI, "invokeTest2"));
        if (b) {
          call.setReturnType(QNAME_TYPE_STRING);
          call.addParameter("BigInteger_1", new QName(Constants.XSD, "integer"),
              ParameterMode.IN);
          call.addParameter("BigDecimal_2", new QName(Constants.XSD, "decimal"),
              ParameterMode.IN);
          call.addParameter("QName_3", new QName(Constants.XSD, "QName"),
              ParameterMode.IN);
          call.addParameter("String_4", new QName(Constants.XSD, "string"),
              ParameterMode.IN);
        }
        Object[] params = { JAXRPC_Data.BigInteger_data[0],
            JAXRPC_Data.BigDecimal_data[0], JAXRPC_Data.QName_data[0],
            JAXRPC_Data.String_data[0] };

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"InvokeTest2\")");
        String expected = "passed";
        String response = (String) call.invoke(params);
        if (!response.equals(expected)) {
          TestUtil.logErr("RPC failed - expected \"" + expected
              + "\", received: " + response);
          pass = false;
        } else {
          TestUtil
              .logMsg("RPC passed - received expected response: " + response);
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateCallConstructor4Test1 failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor4Test1 failed");
  }

  /*
   * @testName: GetCallsPosTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:33; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:134; WS4EE:SPEC:186;
   *
   * @test_Strategy: Get an array of Call objects by calling the api
   * Service.getCalls(). Access to WSDL metadata and valid port name. Verify the
   * behavior.
   */
  public void GetCallsPosTest1() throws Fault {
    TestUtil.logTrace("GetCallsPosTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Test getCalls (valid port name/WSDL " + "access) - positive test 1");
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg(
          "Get an array of Call objects via Service.getCalls(PORT_QNAME)");
      Call[] calls = service.getCalls(PORT_QNAME);
      if (calls == null) {
        TestUtil.logErr("getCalls() returned null");
        pass = false;
      } else if (!(calls instanceof Call[])) {
        TestUtil.logErr(
            "getCalls() did not return " + "instance of Call[] objects");
        pass = false;
      } else {
        TestUtil.logMsg("getCalls() successfully returned Call[] array");
        TestUtil.logMsg("Array count = " + calls.length);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetCallsPosTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetCallsPosTest1 failed");
  }

  /*
   * @testName: GetCallsNegTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:34; JAXRPC:SPEC:314; WS4EE:SPEC:134;
   * WS4EE:SPEC:186;
   *
   * @test_Strategy: Get an array of Call objects by calling the api
   * Service.getCalls(). Access to WSDL metadata and invalid port name. Verify
   * that an exception occurs due to port not found. Expect a ServiceException
   */
  public void GetCallsNegTest2() throws Fault {
    TestUtil.logTrace("GetCallsNegTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Test getCalls (invalid port name/"
          + "WSDL acesss) - negative test 2");
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Get an array of Call objects via Service.getCalls()");
      TestUtil.logMsg("Expecting a (ServiceException)");
      try {
        Call[] calls = service.getCalls(BAD_PORT_QNAME);
        TestUtil.logErr("Did not throw expected ServiceException");
        pass = false;
      } catch (ServiceException e) {
        TestUtil.logMsg("Caught expected ServiceException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetCallsNegTest2 failed", e);
    }

    if (!pass)
      throw new Fault("GetCallsNegTest2 failed");
  }

  /*
   * @testName: GetCallsNegTest3
   *
   * @assertion_ids: JAXRPC:JAVADOC:34; JAXRPC:SPEC:314; WS4EE:SPEC:134;
   * WS4EE:SPEC:186;
   *
   * @test_Strategy: Get an array of Call objects by calling the api
   * Service.getCalls(). Access to WSDL metadata and a null port name. Verify
   * that an exception occurs due to port not found. Expect a ServiceException.
   */
  public void GetCallsNegTest3() throws Fault {
    TestUtil.logTrace("GetCallsNegTest3");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Test getCalls (null port name/" + "WSDL access) - negative test 3");
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Get an array of Call objects via Service.getCalls()");
      TestUtil.logMsg("Expecting a (ServiceException)");
      try {
        Call[] calls = service.getCalls(null);
        TestUtil.logErr("Did not throw expected ServiceException");
        pass = false;
      } catch (ServiceException e) {
        TestUtil.logMsg("Caught expected ServiceException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetCallsNegTest3 failed", e);
    }

    if (!pass)
      throw new Fault("GetCallsNegTest3 failed");
  }

  /*
   * @testName: GetPortConstructor1PosTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:29; JAXRPC:SPEC:314; WS4EE:SPEC:134;
   * WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort( QName, Class)
   * to return a dynamic proxy for the service port. Pass a valid port name with
   * WSDL access. Verify that the method returns a dynamic proxy.
   */
  public void GetPortConstructor1PosTest1() throws Fault {
    TestUtil.logTrace("GetPortConstructor1PosTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Test getPort (valid port name/" + "WSDL access) - positive test 1");
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Call Service.getPort(QName, Class)");
      TestUtil.logMsg("Should find port");
      Tests tests = (Tests) service.getPort(PORT_QNAME, Tests.class);
      if (tests == null) {
        TestUtil.logErr("getPort(QName, Class) returned null");
        pass = false;
      } else if (!(tests instanceof Tests)) {
        TestUtil.logErr(
            "getPort(QName, Class) did not" + " return instance of Tests");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor1PosTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor1PosTest1 failed");
  }

  /*
   * @testName: GetPortConstructor1NegTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:30; JAXRPC:SPEC:314; WS4EE:SPEC:134;
   * WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort( QName, Class)
   * to return a dynamic proxy for the service port. Pass an invalid port name
   * with accesss to WSDL metadata. Verify that an exception occurs due to port
   * not found. Expect a ServiceException.
   */
  public void GetPortConstructor1NegTest2() throws Fault {
    TestUtil.logTrace("GetPortConstructor1NegTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Test getPort (invalid port name/"
          + "WSDL access) - negative test 2");
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Call Service.getPort(QName, Class)");
      TestUtil.logMsg("Should not find port (expect ServiceException)");
      Tests tests = (Tests) service.getPort(BAD_PORT_QNAME, Tests.class);
      TestUtil.logErr("Did not throw expected ServiceException");
      pass = false;
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor1NegTest2 failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor1NegTest2 failed");
  }

  /*
   * @testName: GetPortConstructor2PosTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:32; JAXRPC:SPEC:314; WS4EE:SPEC:134;
   * WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort(Class) to return
   * a dynamic proxy for the service port. Pass a valid SEI class. Access to
   * WSDL metadata. Verify behavior.
   */
  public void GetPortConstructor2PosTest1() throws Fault {
    TestUtil.logTrace("GetPortConstructor2PosTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Test getPort (valid SEI class/" + "WSDL access) - positive test 1");
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Call Service.getPort(Class)");
      TestUtil.logMsg("Should find port");
      Tests tests = (Tests) service.getPort(Tests.class);
      if (tests == null) {
        TestUtil.logErr("getPort(Class) returned null");
        pass = false;
      } else if (!(tests instanceof Tests)) {
        TestUtil.logErr("getPort(Class) did not" + " return instance of Tests");
        pass = false;
      }
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor2PosTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor2PosTest1 failed");
  }

  /*
   * @testName: GetPortConstructor2NegTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:32; JAXRPC:SPEC:314; WS4EE:SPEC:134;
   * WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort(Class) to return
   * a dynamic proxy for the service port. Pass an invalid SEI class. WSDL
   * metadata access. Verify behavior. Expect ServiceException.
   */
  public void GetPortConstructor2NegTest1() throws Fault {
    TestUtil.logTrace("GetPortConstructor2NegTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Test getPort (invalid SEI class/"
          + "WSDL access) - negative test 1");
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Call Service.getPort(Class)");
      TestUtil.logMsg("Should not find port (expect ServiceException)");
      Tests tests = (Tests) service
          .getPort(java.rmi.activation.Activator.class);
      TestUtil.logErr("Did not throw expected ServiceException");
      pass = false;
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor2NegTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor2NegTest1 failed");
  }

  /*
   * @testName: GetPortsPosTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:44; JAXRPC:SPEC:314; WS4EE:SPEC:134;
   * WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object. Call Service.getPorts() to return a
   * list of qualified names of the ports grouped by this service. Verify that
   * the method returns a list of qualified names of the ports grouped by this
   * service. Create a Service object with access to WSDL metadata.
   */
  public void GetPortsPosTest1() throws Fault {
    TestUtil.logTrace("GetPortsPosTest1");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Test getPorts with (WSDL access) - " + "positive test 1");
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance with WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Call Service.getPorts()");
      TestUtil.logMsg("Expect a non empty iterator of ports");
      Iterator i = service.getPorts();
      if (!i.hasNext()) {
        TestUtil.logErr("getPorts() returned empty iterator (unexpected)");
        pass = false;
      } else {
        int count = 0;
        while (i.hasNext()) {
          i.next();
          count++;
        }
        if (count != 1) {
          TestUtil.logErr("getPorts() returned wrong " + "iterator count, got "
              + count + ", expected 1");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortsPosTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetPortsPosTest1 failed");
  }

  /*
   * @testName: GetServiceNameTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:43; JAXRPC:SPEC:314; WS4EE:SPEC:138;
   * WS4EE:SPEC:134; WS4EE:SPEC:186;
   *
   * @test_Strategy: Create a Call object. Call Service.getServiceName() to
   * return the name of this service.
   */
  public void GetServiceNameTest1() throws Fault {
    TestUtil.logTrace("GetServiceNameTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get Initial Context");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("Get JAXRPC service instance WSDL");
      javax.xml.rpc.Service service = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/" + SERVICE_NAME);
      TestUtil.logMsg("Create Call object via Service.createCall()");
      Call call = service.createCall();
      TestUtil.logMsg("Get service name via Service.getServiceName()");
      QName sname = service.getServiceName();
      if (!sname.equals(SERVICE_QNAME)) {
        TestUtil.logErr("getServiceName() returned wrong QName");
        TestUtil.logErr("Expected " + SERVICE_QNAME + "\nGot " + sname);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetServiceNameTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetServiceNameTest1 failed");
  }
}
