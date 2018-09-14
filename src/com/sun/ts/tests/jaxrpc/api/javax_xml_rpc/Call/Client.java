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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.Call;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxrpc.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.*;

import com.sun.javatest.Status;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.Call.";

  private final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private final String TYPESNAMESPACEURI = "http://helloservice.org/types";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private QName PORTTYPE_QNAME;

  private QName QNAME_TYPE_STRING;

  private Service service = null;

  private Call call = null;

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "helloservice.endpoint.1";

  private static final String WSDLLOC_URL = "helloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getTestDIIURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
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
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    // Initialize QNAMES used in the test
    SERVICE_QNAME = new QName(NAMESPACEURI, "HelloService");
    PORT_QNAME = new QName(NAMESPACEURI, "HelloPort");
    PORTTYPE_QNAME = new QName(NAMESPACEURI, "Hello");
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
        TestUtil.printStackTrace(e);
        pass = false;
      }
      TestUtil.logMsg("Create Service and Call objects");
      TestUtil.logMsg("SERVICE_QNAME=" + SERVICE_QNAME);
      TestUtil.logMsg("PORT_QNAME=" + PORT_QNAME);
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        getTestDIIURLs();
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/call");
      }
      call = service.createCall(PORT_QNAME);
      call = JAXRPC_Util.setCallProperties(call, " ");
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
   * @testName: InvokeTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:74; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invoke(Object[]) to invoke the RPC method. Verify RPC
   * method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a synchronous request-response interaction. Test using primitive wrapper
   * type parameters.
   */
  public void InvokeTest1() throws Fault {
    TestUtil.logTrace("InvokeTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("InvokeTest1: using primitive wrapper type parameters");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setOperationName(new QName(NAMESPACEURI, "invokeTest1"));
      call.setTargetEndpointAddress(url);
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest1"));
      if (b) {
        call.setReturnType(QNAME_TYPE_STRING);
        call.addParameter("Boolean_1", new QName(Constants.ENCODING, "boolean"),
            ParameterMode.IN);
        call.addParameter("Byte_2", new QName(Constants.ENCODING, "byte"),
            ParameterMode.IN);
        call.addParameter("Short_3", new QName(Constants.ENCODING, "short"),
            ParameterMode.IN);
        call.addParameter("Integer_4", new QName(Constants.ENCODING, "int"),
            ParameterMode.IN);
        call.addParameter("Long_5", new QName(Constants.ENCODING, "long"),
            ParameterMode.IN);
        call.addParameter("Float_6", new QName(Constants.ENCODING, "float"),
            ParameterMode.IN);
        call.addParameter("Double_7", new QName(Constants.ENCODING, "double"),
            ParameterMode.IN);
      }
      Object[] params = { JAXRPC_Data.Boolean_data[0], JAXRPC_Data.Byte_data[0],
          JAXRPC_Data.Short_data[0], JAXRPC_Data.Integer_data[0],
          JAXRPC_Data.Long_data[0], JAXRPC_Data.Float_data[0],
          JAXRPC_Data.Double_data[0] };

      TestUtil.logMsg("InvokeTest1 RPC method Tests(\"invokeTest1\")");
      String response = (String) call.invoke(params);
      if (!response.equals(expected)) {
        TestUtil.logErr("RPC failed - expected \"" + expected + "\", received: "
            + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeTest1 failed", e);
    }

    if (!pass)
      throw new Fault("InvokeTest1 failed");
  }

  /*
   * @testName: InvokeTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:74; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invoke(Object[]) to invoke the RPC method. Verify RPC
   * method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a synchronous request-response interaction. Test using supported value type
   * parameters.
   */
  public void InvokeTest2() throws Fault {
    TestUtil.logTrace("InvokeTest2");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("InvokeTest2: using supported value type parameters");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
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

      TestUtil.logMsg("InvokeTest2 RPC method Tests(\"invokeTest2\")");
      String response = (String) call.invoke(params);
      if (!response.equals(expected)) {
        TestUtil.logErr("RPC failed - expected \"" + expected + "\", received: "
            + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeTest2 failed", e);
    }

    if (!pass)
      throw new Fault("InvokeTest2 failed");
  }

  /*
   * @testName: InvokeTest3a
   *
   * @assertion_ids: JAXRPC:JAVADOC:74; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invoke(Object[]) to invoke the RPC method. Verify RPC
   * method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a synchronous request-response interaction. Test method passing no
   * parameters and returning a response.
   */
  public void InvokeTest3a() throws Fault {
    TestUtil.logTrace("InvokeTest3a");
    String expected = "Hello";
    boolean pass = true;
    try {
      TestUtil.logMsg("InvokeTest3a: with no parameters");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "invokeTest3"));
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest3"));
      if (b)
        call.setReturnType(QNAME_TYPE_STRING);

      TestUtil.logMsg("InvokeTest3a RPC method Tests(\"invokeTest3\")");
      String response = (String) call.invoke(new Object[0]);
      if (!response.equals(expected)) {
        TestUtil.logErr("RPC failed - expected \"" + expected + "\", received: "
            + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeTest3a failed", e);
    }

    if (!pass)
      throw new Fault("InvokeTest3a failed");
  }

  /*
   * @testName: InvokeTest3b
   *
   * @assertion_ids: JAXRPC:JAVADOC:76; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invoke(QName, Object[]) to invoke the RPC method. Verify
   * RPC method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a synchronous request-response interaction. Test method passing no
   * parameters and returning a response.
   */
  public void InvokeTest3b() throws Fault {
    TestUtil.logTrace("InvokeTest3b");
    String expected = "Hello";
    boolean pass = true;
    try {
      TestUtil.logMsg("InvokeTest3b: with no parameters");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest3"));
      if (b)
        call.setReturnType(QNAME_TYPE_STRING);

      TestUtil.logMsg("InvokeTest3b RPC method Tests(\"invokeTest3\")");
      String response = (String) call
          .invoke(new QName(NAMESPACEURI, "invokeTest3"), new Object[0]);
      if (!response.equals(expected)) {
        TestUtil.logErr("RPC failed - expected \"" + expected + "\", received: "
            + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeTest3b failed", e);
    }

    if (!pass)
      throw new Fault("InvokeTest3b failed");
  }

  /*
   * @testName: InvokeTest4
   *
   * @assertion_ids: JAXRPC:JAVADOC:75; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invoke(Object[]) to invoke the RPC method. Expect an
   * Exception.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a synchronous request-response interaction. Test method that throws a user
   * defined service exception.
   */
  public void InvokeTest4() throws Fault {
    TestUtil.logTrace("InvokeTest4");
    boolean pass = true;
    try {
      TestUtil.logMsg("InvokeTest4: throws a user defined service exception");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "invokeTest4"));

      TestUtil.logMsg("InvokeTest4 RPC method Tests(\"invokeTest4\")");
      try {
        call.invoke(new Object[0]);
        TestUtil.logErr("RPC method did not throw expected Exception");
        pass = false;
      } catch (Exception e) {
        TestUtil.logMsg("RPC method did throw expected Exception");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeTest4 failed", e);
    }

    if (!pass)
      throw new Fault("InvokeTest4 failed");
  }

  /*
   * @testName: InvokeOneWayTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:78; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invokeOneWay(Object[]) to invoke the RPC method. Verify
   * RPC method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a asynchronous one-way interaction. Test using primitive wrapper type
   * parameters.
   */
  public void InvokeOneWayTest1() throws Fault {
    TestUtil.logTrace("InvokeOneWayTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil
          .logMsg("InvokeOneWayTest1: using primitive wrapper type parameters");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "invokeOneWayTest1"));
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeOneWayTest1"));
      if (b) {
        call.addParameter("Boolean_1", new QName(Constants.ENCODING, "boolean"),
            ParameterMode.IN);
        call.addParameter("Byte_2", new QName(Constants.ENCODING, "byte"),
            ParameterMode.IN);
        call.addParameter("Short_3", new QName(Constants.ENCODING, "short"),
            ParameterMode.IN);
        call.addParameter("Integer_4", new QName(Constants.ENCODING, "int"),
            ParameterMode.IN);
        call.addParameter("Long_5", new QName(Constants.ENCODING, "long"),
            ParameterMode.IN);
        call.addParameter("Float_6", new QName(Constants.ENCODING, "float"),
            ParameterMode.IN);
        call.addParameter("Double_7", new QName(Constants.ENCODING, "double"),
            ParameterMode.IN);
      }
      Object[] params = { JAXRPC_Data.Boolean_data[0], JAXRPC_Data.Byte_data[0],
          JAXRPC_Data.Short_data[0], JAXRPC_Data.Integer_data[0],
          JAXRPC_Data.Long_data[0], JAXRPC_Data.Float_data[0],
          JAXRPC_Data.Double_data[0] };

      TestUtil.logMsg(
          "InvokeOneWayTest1 RPC method " + "Tests(\"invokeOneWayTest1\")");
      call.invokeOneWay(params);
      TestUtil.logMsg("RPC passed - invokeOneWay call successful");
    } catch (Exception e) {
      TestUtil.logErr("RPC failed - invokeOneWay call unsucessful");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeOneWayTest1 failed", e);
    }

    if (!pass)
      throw new Fault("InvokeOneWayTest1 failed");
  }

  /*
   * @testName: InvokeOneWayTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:78; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invokeOneWay(Object[]) to invoke the RPC method. Verify
   * RPC method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a asynchronous one-way interaction. Test using supported value type
   * parameters.
   */
  public void InvokeOneWayTest2() throws Fault {
    TestUtil.logTrace("InvokeOneWayTest2");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil
          .logMsg("InvokeOneWayTest2: using supported value type parameters");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "invokeOneWayTest2"));
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeOneWayTest2"));
      if (b) {
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

      TestUtil.logMsg(
          "InvokeOneWayTest2 RPC method " + "Tests(\"invokeOneWayTest2\")");
      call.invokeOneWay(params);
      TestUtil.logMsg("RPC passed - invokeOneWay call successful");
    } catch (Exception e) {
      TestUtil.logErr("RPC failed - invokeOneWay call unsuccessful");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeOneWayTest2 failed", e);
    }

    if (!pass)
      throw new Fault("InvokeOneWayTest2 failed");
  }

  /*
   * @testName: InvokeOneWayTest3
   *
   * @assertion_ids: JAXRPC:JAVADOC:78; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invokeOneWay(Object[]) to invoke the RPC method. Verify
   * RPC method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a asynchronous one-way interaction. Test method passing no parameters and
   * returning a response.
   */
  public void InvokeOneWayTest3() throws Fault {
    TestUtil.logTrace("InvokeOneWayTest3");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("InvokeOneWayTest3: with no parameters");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "invokeOneWayTest3"));
      TestUtil.logMsg(
          "InvokeOneWayTest3 RPC method " + "Tests(\"invokeOneWayTest3\")");
      call.invokeOneWay(new Object[0]);
      TestUtil.logMsg("RPC passed - invokeOneWay call successful");
    } catch (Exception e) {
      TestUtil.logErr("RPC failed - invokeOneWay call unsuccessful");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeOneWayTest3 failed", e);
    }

    if (!pass)
      throw new Fault("InvokeOneWayTest3 failed");
  }

  /*
   * @testName: InvokeNoSuchMethodTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:75; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invoke(Object[]) to invoke the RPC method. Verify RPC
   * method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a synchronous request-response interaction. Test invoking no such method.
   * Expect an Exception.
   */
  public void InvokeNoSuchMethodTest1() throws Fault {
    TestUtil.logTrace("InvokeNoSuchMethodTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("InvokeNoSuchMethodTest1: no such method exists");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "invokeNoSuchMethodTest1"));
      TestUtil.logMsg("InvokeNoSuchMethodTest1 RPC method "
          + "Tests(\"invokeNoSuchMethodTest1\")");
      call.invoke(new Object[0]);
      TestUtil.logErr("Test failed - did not receive expected Exception");
      pass = false;
    } catch (Exception e) {
      TestUtil.logMsg("Test passed  - did receive expected Exception");
    }

    if (!pass)
      throw new Fault("InvokeNoSuchMethodTest1 failed");
  }

  /*
   * @testName: InvokeOneWayNoSuchMethodTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:75; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and execute Call.invokeOneWay(Object[]) to invoke the RPC method. Verify
   * RPC method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation using
   * a asynchronous one-way interaction. Test invoking no such method. Expect an
   * Exception.
   */
  public void InvokeOneWayNoSuchMethodTest2() throws Fault {
    TestUtil.logTrace("InvokeOneWayNoSuchMethodTest2");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("InvokeOneWayNoSuchMethodTest2: no such method exists");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      call.setOperationName(
          new QName(NAMESPACEURI, "invokeOneWayNoSuchMethodTest2"));
      TestUtil.logMsg("InvokeOneWayNoSuchMethodTest2 RPC method "
          + "Tests(\"invokeOneWayNoSuchMethodTest2\")");
      call.invoke(new Object[0]);
      TestUtil.logErr("Test failed - did not receive expected Exception");
      pass = false;
    } catch (Exception e) {
      TestUtil.logMsg("Test passed  - did receive expected Exception");
    }

    if (!pass)
      throw new Fault("InvokeOneWayNoSuchMethodTest2 failed");
  }

  /*
   * @testName: SetGetOperationNameTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:64; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setOperationName(QName) followed by getOperationName().
   * Verify behavior.
   */
  public void SetGetOperationNameTest1() throws Fault {
    TestUtil.logTrace("SetGetOperationNameTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("Don't set any operation name");
      QName name = call.getOperationName();
      TestUtil.logMsg("Get operation name (expect null string)");
      if (name == null) {
        TestUtil
            .logMsg("getOperationName() passed - received " + "expected null");
      } else if (!name.getLocalPart().equals("")) {
        TestUtil.logErr("getOperationName() failed - expected: "
            + "\"\", received: " + name);
        pass = false;
      } else
        TestUtil.logMsg(
            "getOperationName() passed - received " + "expected name: \"\"");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetOperationNameTest1 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetOperationNameTest1 failed");
  }

  /*
   * @testName: SetGetOperationNameTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:64; JAXRPC:JAVADOC:65; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setOperationName(QName) followed by getOperationName().
   * Verify behavior.
   */
  public void SetGetOperationNameTest2() throws Fault {
    TestUtil.logTrace("SetGetOperationNameTest2");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("Set operation name to invokeTest3");
      call.setOperationName(new QName("invokeTest3"));
      TestUtil.logMsg("Get operation name (expect invokeTest3)");
      QName name = call.getOperationName();
      if (name == null) {
        TestUtil.logErr("getOperationName() returned null");
        pass = false;
      } else if (!name.getLocalPart().equals("invokeTest3")) {
        TestUtil.logErr("getOperationName() failed - expected: "
            + "invokeTest3, received: " + name);
        pass = false;
      } else
        TestUtil.logMsg(
            "getOperationName() passed - received " + "expected name: " + name);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetOperationNameTest2 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetOperationNameTest2 failed");
  }

  /*
   * @testName: SetInvalidOperationNameTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:64; JAXRPC:JAVADOC:65; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setOperationName(QName) with an invalid operantion name.
   * Verify behavior. Set an invalid operation name, expect exception.
   */
  public void SetInvalidOperationNameTest1() throws Fault {
    TestUtil.logTrace("SetInvalidOperationNameTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg(
          "Set operation name to InvalidOperationName " + "(expect Exception)");
      call.setOperationName(new QName("InvalidOperationName"));
      TestUtil.logMsg("Did not catch expected JAXRPCException");
    } catch (JAXRPCException e) {
      TestUtil.logMsg("Caught expected JAXRPCException");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetReturnTypeTest1 failed", e);
    }

    if (!pass)
      throw new Fault("SetInvalidOperationNameTest1 failed");
  }

  /*
   * @testName: SetGetReturnTypeTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:62; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setReturnType(QName) followed by getReturnType(). Verify
   * behavior.
   */
  public void SetGetReturnTypeTest1() throws Fault {
    TestUtil.logTrace("SetGetReturnTypeTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("Don't set any return type");
      QName type = call.getReturnType();
      TestUtil.logMsg("Get return type (expect null)");
      if (type == null) {
        TestUtil.logMsg("getReturnType() passed - received " + "expected null");
      } else if (!type.getLocalPart().equals("")) {
        TestUtil.logErr(
            "getReturnType() failed - expected: " + "\"\", received: " + type);
        pass = false;
      } else
        TestUtil.logMsg("getReturnType() passed - received null");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetReturnTypeTest1 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetReturnTypeTest1 failed");
  }

  /*
   * @testName: SetGetReturnTypeTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:60; JAXRPC:JAVADOC:62; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setReturnType(QName) followed by getReturnType(). Verify
   * behavior.
   */
  public void SetGetReturnTypeTest2() throws Fault {
    TestUtil.logTrace("SetGetReturnTypeTest2");
    boolean pass = true;
    String expected = "passed";
    try {
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest1"));
      if (b) {
        TestUtil.logMsg("Set return type to xml string type");
        call.setReturnType(QNAME_TYPE_STRING);
        TestUtil.logMsg("Get return type (expect xml string type)");
        QName type = call.getReturnType();
        if (type == null) {
          TestUtil.logErr("getReturnType() returned null");
          pass = false;
        } else if (!type.getLocalPart().equals("string")) {
          TestUtil.logErr("getReturnType() failed - expected: "
              + "string, received: " + type.getLocalPart());
          pass = false;
        } else
          TestUtil.logMsg("getReturnType() passed - received "
              + "expected type: " + type.getLocalPart());
      } else {
        TestUtil.logMsg("Call object does not support setting return type");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetReturnTypeTest2 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetReturnTypeTest2 failed");
  }

  /*
   * @testName: SetGetReturnTypeTest3
   *
   * @assertion_ids: JAXRPC:JAVADOC:61; JAXRPC:JAVADOC:62; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setReturnType(QName) followed by getReturnType(). Verify
   * behavior.
   */
  public void SetGetReturnTypeTest3() throws Fault {
    TestUtil.logTrace("SetGetReturnTypeTest3");
    boolean pass = true;
    String expected = "passed";

    try {
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest1"));
      if (b) {
        TestUtil.logMsg("Set return type to complex xml JavaBean type");
        call.setReturnType(new QName(TYPESNAMESPACEURI, "JavaBean"),
            JavaBean.class);
        TestUtil.logMsg("Get return type (expect complex xml JavaBean type)");
        QName type = call.getReturnType();
        if (type == null) {
          TestUtil.logErr("getReturnType() returned null");
          pass = false;
        } else if (!type.getLocalPart().equals("JavaBean")) {
          TestUtil.logErr("getReturnType() failed - expected: "
              + "string, received: " + type.getLocalPart());
          pass = false;
        } else
          TestUtil.logMsg("getReturnType() passed - received "
              + "expected type: " + type.getLocalPart());
      } else {
        TestUtil.logMsg("Call object does not support setting return type");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetReturnTypeTest3 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetReturnTypeTest3 failed");
  }

  /*
   * @testName: SetGetPropertyTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:70; JAXRPC:JAVADOC:71; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setProperty(String, Object) followed by getProperty(String).
   * Verify behavior.
   */
  public void SetGetPropertyTest1() throws Fault {
    TestUtil.logTrace("SetGetPropertyTest1");
    boolean pass = true;
    boolean skiprest = false;
    String expected = "passed";
    try {
      TestUtil.logMsg("Set a supported property");
      try {
        TestUtil.logMsg(
            "Setting property " + Constants.CALL_ENCODINGSTYLE_URI_PROPERTY);
        call.setProperty(Constants.CALL_ENCODINGSTYLE_URI_PROPERTY,
            Constants.URI_ENCODING);
      } catch (JAXRPCException e1) {
        skiprest = true;
        TestUtil.logMsg(
            "JAXRPC implementation does not support setting properties");
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: ", e2);
        pass = false;
      }
      if (!skiprest) {
        TestUtil.logMsg("Get a supported property");
        TestUtil.logMsg(
            "Getting property " + Constants.CALL_ENCODINGSTYLE_URI_PROPERTY);
        String pvalue = (String) call
            .getProperty(Constants.CALL_ENCODINGSTYLE_URI_PROPERTY);
        if (!pvalue.equals(Constants.URI_ENCODING)) {
          TestUtil.logErr("getProperty() failed - expected: "
              + Constants.URI_ENCODING + ", received: " + pvalue);
          pass = false;
        } else
          TestUtil.logMsg("getProperty() passed - received "
              + "expected property value: " + pvalue);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetPropertyTest1 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetPropertyTest1 failed");
  }

  /*
   * @testName: SetGetPropertyTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:70; JAXRPC:JAVADOC:71; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setProperty(String, Object) followed by getProperty(String).
   * Verify behavior.
   */
  public void SetGetPropertyTest2() throws Fault {
    TestUtil.logTrace("SetGetPropertyTest2");
    boolean pass = true;
    String expected = "passed";
    TestUtil.logMsg("Attempt to set invalid property");
    try {
      call.setProperty(Constants.INVALID_PROPERTY, new Object());
      TestUtil.logErr("setProperty() did not throw expected exception");
      pass = false;
    } catch (JAXRPCException e) {
      TestUtil.logMsg("setProperty() caught expected JAXRPCException");
    } catch (RuntimeException e) {
      TestUtil.logMsg("setProperty() caught expected RuntimeException");
    }
    TestUtil.logMsg("Attempt to get invalid property");
    try {
      call.getProperty(Constants.INVALID_PROPERTY);
      TestUtil.logErr("getProperty() did not throw expected exception");
      pass = false;
    } catch (JAXRPCException e) {
      TestUtil.logMsg("getProperty() caught expected JAXRPCException");
    } catch (RuntimeException e) {
      TestUtil.logMsg("getProperty() caught expected RuntimeException");
    }
    if (!pass)
      throw new Fault("SetGetPropertyTest2 failed");
  }

  /*
   * @testName: RemovePropertyTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:72; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setProperty(String, Object) followed by
   * removeProperty(String). Verify behavior.
   */
  public void RemovePropertyTest1() throws Fault {
    TestUtil.logTrace("RemovePropertyTest1");
    boolean pass = true;
    boolean skiprest = false;
    String expected = "passed";
    try {
      TestUtil.logMsg("Set some properties");
      try {
        TestUtil
            .logMsg("Set property " + Constants.CALL_SOAPACTION_USE_PROPERTY);
        call.setProperty(Constants.CALL_SOAPACTION_USE_PROPERTY,
            new Boolean(true));
      } catch (JAXRPCException e2) {
        skiprest = true;
        TestUtil.logMsg(
            "JAXRPC implementation does not support setting properties");
      }
      if (!skiprest) {
        TestUtil.logMsg(
            "Remove property " + Constants.CALL_SOAPACTION_USE_PROPERTY);
        call.removeProperty(Constants.CALL_SOAPACTION_USE_PROPERTY);
        TestUtil
            .logMsg("Get property " + Constants.CALL_SOAPACTION_USE_PROPERTY);
        String pvalue = (String) call
            .getProperty(Constants.CALL_SOAPACTION_USE_PROPERTY);
        if (pvalue != null) {
          TestUtil.logErr(
              "Property not removed " + Constants.CALL_SOAPACTION_USE_PROPERTY);
          pass = false;
        } else
          TestUtil.logMsg(
              "Property was removed " + Constants.CALL_SOAPACTION_USE_PROPERTY);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("RemovePropertyTest1 failed", e);
    }

    if (!pass)
      throw new Fault("RemovePropertyTest1 failed");
  }

  /*
   * @testName: RemovePropertyTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:72; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call removeProperty(String) of an invalid property. Verify
   * behavior.
   */
  public void RemovePropertyTest2() throws Fault {
    TestUtil.logTrace("RemovePropertyTest2");
    boolean pass = true;
    String expected = "passed";
    TestUtil.logMsg("Attempt to remove invalid property");
    try {
      call.removeProperty(Constants.INVALID_PROPERTY);
      TestUtil.logMsg("removeProperty() did not throw exception");
    } catch (JAXRPCException e) {
      TestUtil.logMsg("removeProperty() caught expected JAXRPCException");
    } catch (RuntimeException e) {
      TestUtil.logMsg("removeProperty() caught expected RuntimeException");
    }

    if (!pass)
      throw new Fault("RemovePropertyTest2 failed");
  }

  /*
   * @testName: SetGetPortTypeNameTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:66; JAXRPC:JAVADOC:67; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setPortTypeName(QName) followed by getPortTypeName(). Verify
   * behavior.
   */
  public void SetGetPortTypeNameTest1() throws Fault {
    TestUtil.logTrace("SetGetPortTypeNameTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      try {
        TestUtil.logMsg("Create Call object");
        call = service.createCall();
        TestUtil.logMsg("Set port type name");
        TestUtil.logMsg("portTypeName = " + PORTTYPE_QNAME);
        call.setPortTypeName(PORTTYPE_QNAME);
        TestUtil.logMsg("setPortTypeName() passed");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: ", e);
        TestUtil.logErr("setPortTypeName() failed");
        pass = false;
      }
      if (pass) {
        TestUtil.logMsg("Get port type name");
        QName value = call.getPortTypeName();
        if (!value.equals(PORTTYPE_QNAME)) {
          TestUtil.logErr("getPortTypeName() failed - expected: "
              + PORTTYPE_QNAME + ", received: " + value);
          pass = false;
        } else
          TestUtil.logMsg("getPortTypeName() passed - received "
              + "expected value: " + value);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetPortTypeNameTest1 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetPortTypeNameTest1 failed");
  }

  /*
   * @testName: SetGetPortTypeNameTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:66; JAXRPC:JAVADOC:67; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call setPortTypeName(QName) followed by getPortTypeName(). Verify
   * behavior.
   */
  public void SetGetPortTypeNameTest2() throws Fault {
    TestUtil.logTrace("SetGetPortTypeNameTest2");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("Create Call object");
      call = service.createCall();
      TestUtil.logMsg("Don't set port type name");
      TestUtil.logMsg("portTypeName =");
      TestUtil.logMsg("Get port type name");
      QName value = call.getPortTypeName();
      if (!value.equals(new QName(""))) {
        TestUtil.logErr("getPortTypeName() failed - expected: " + new QName("")
            + ", received: " + value);
        pass = false;
      } else
        TestUtil.logMsg("getPortTypeName() passed - received "
            + "expected value: " + value);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetPortTypeNameTest2 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetPortTypeNameTest2 failed");
  }

  /*
   * @testName: SetGetTargetEndpointAddressTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:68; JAXRPC:JAVADOC:69; JAXRPC:SPEC:313;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and call setTargetEndpointAddress(String) followed by
   * getTargetEndpointAddress(). Verify behavior.
   */
  public void SetGetTargetEndpointAddressTest1() throws Fault {
    TestUtil.logTrace("SetGetTargetEndpointAddressTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("Set target endpoint address");
      try {
        TestUtil.logMsg("url = " + url);
        call.setTargetEndpointAddress(url);
        TestUtil.logMsg("setTargetEndpointAddress() passed");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: ", e);
        TestUtil.logErr("setTargetEndpointAddress() failed");
        pass = false;
      }
      if (pass) {
        TestUtil.logMsg("Get target endpoint address");
        String value = call.getTargetEndpointAddress();
        if (!value.equals(url)) {
          TestUtil.logErr("getTargetEndpointAddress() failed" + "\nexpected: "
              + url + ", received: " + value);
          pass = false;
        } else
          TestUtil.logMsg("getPortTypeName() passed - received"
              + " expected value: " + value);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetTargetEndpointAddressTest1 failed", e);
    }

    if (!pass)
      throw new Fault("SetGetTargetEndpointAddressTest1 failed");
  }

  /*
   * @testName: AddGetRemoveAllParametersTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:57; JAXRPC:JAVADOC:58; JAXRPC:JAVADOC:59;
   * JAXRPC:SPEC:313; WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call addParameter(String, QName, ParameterMode) or
   * addParameter(String, QName, Class, ParameterMode) followed by
   * getParameterTypeByName( String) followed by removeAllParameters(). Verify
   * behavior.
   */
  public void AddGetRemoveAllParametersTest1() throws Fault {
    TestUtil.logTrace("AddGetRemoveAllParametersTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest1"));
      if (b) {
        QName name1 = new QName(Constants.ENCODING, "boolean");
        QName name2 = new QName(Constants.ENCODING, "byte");
        QName name3 = new QName(TYPESNAMESPACEURI, "JavaBean");
        call.addParameter("Boolean_1", name1, ParameterMode.IN);
        call.addParameter("Byte_2", name2, ParameterMode.IN);
        call.addParameter("JavaBean_3", name3, ParameterMode.IN);
        TestUtil.logMsg("QName1 = " + name1 + "\nQName2 = " + name2
            + "\nQName3 = " + name3);
        QName name1cmp = call.getParameterTypeByName("Boolean_1");
        QName name2cmp = call.getParameterTypeByName("Byte_2");
        QName name3cmp = call.getParameterTypeByName("JavaBean_3");
        TestUtil.logMsg("QName1cmp = " + name1cmp + "\nQName2cmp = " + name2cmp
            + "\nQName3cmp = " + name3cmp);
        if (!name1cmp.equals(name1)) {
          TestUtil.logErr("getParameterTypeByName returned wrong QName");
          TestUtil.logErr("Expected " + name1 + "\nGot " + name1cmp);
          pass = false;
        }
        if (!name2cmp.equals(name2)) {
          TestUtil.logErr("getParameterTypeByName returned wrong QName");
          TestUtil.logErr("Expected " + name2 + "\nGot " + name2cmp);
          pass = false;
        }
        if (!name3cmp.equals(name3)) {
          TestUtil.logErr("getParameterTypeByName returned wrong QName");
          TestUtil.logErr("Expected " + name3 + "\nGot " + name3cmp);
          pass = false;
        }
      } else {
        TestUtil.logMsg("Call object does not support setting paramters");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("AddGetRemoveAllParametersTest1 failed", e);
    }

    if (!pass)
      throw new Fault("AddGetRemoveAllParametersTest1 failed");
  }

  /*
   * @testName: AddGetRemoveAllParametersTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:57; JAXRPC:JAVADOC:58; JAXRPC:JAVADOC:59;
   * JAXRPC:SPEC:313; JAXRPC:JAVADOC:63; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and then call addParameter(String, QName, ParameterMode) or
   * addParameter(String, QName, Class, ParameterMode) followed by
   * getParameterTypeByName( String) followed by removeAllParameters(). Verify
   * behavior.
   */
  public void AddGetRemoveAllParametersTest2() throws Fault {
    TestUtil.logTrace("AddGetRemoveAllParametersTest2");
    boolean pass = true;
    String expected = "passed";
    try {
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest1"));
      if (b) {
        QName name1 = new QName(Constants.ENCODING, "boolean");
        QName name2 = new QName(Constants.ENCODING, "byte");
        QName name3 = new QName(TYPESNAMESPACEURI, "JavaBean");
        call.addParameter("Boolean_1", name1, ParameterMode.IN);
        call.addParameter("Byte_2", name2, ParameterMode.IN);
        call.addParameter("JavaBean_3", name3, ParameterMode.IN);
        TestUtil.logMsg("QName1 = " + name1 + "\nQName2 = " + name2
            + "\nQName3 = " + name3);
        call.removeAllParameters();
        QName name1cmp = call.getParameterTypeByName("Boolean_1");
        QName name2cmp = call.getParameterTypeByName("Byte_2");
        QName name3cmp = call.getParameterTypeByName("JavaBean_3");
        TestUtil.logMsg("QName1cmp = " + name1cmp + "\nQName2cmp = " + name2cmp
            + "\nQName3cmp = " + name3cmp);
        if (name1cmp != null) {
          TestUtil.logErr("getParameterTypeByName returned removed parameter");
          pass = false;
        }
        if (name2cmp != null) {
          TestUtil.logErr("getParameterTypeByName returned removed parameter");
          pass = false;
        }
        if (name3cmp != null) {
          TestUtil.logErr("getParameterTypeByName returned removed parameter");
          pass = false;
        }
      } else {
        TestUtil.logMsg("Call object does not support setting paramters");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("AddGetRemoveAllParametersTest2 failed", e);
    }

    if (!pass)
      throw new Fault("AddGetRemoveAllParametersTest2 failed");
  }

  /*
   * @testName: IsParameterAndReturnSpecRequiredTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:56; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition. Determine
   * types of parameters and return value from invocation of addParameter() and
   * setReturnType(). Verify that isParameterAndReturnSpecRequired() returns
   * true.
   */
  public void IsParameterAndReturnSpecRequiredTest1() throws Fault {
    TestUtil.logTrace("IsParameterAndReturnSpecRequiredTest1");
    boolean pass = true;
    String expected = "passed";

    try {
      TestUtil.logMsg("Call isParameterAndReturnSpecRequired() on "
          + "operation InvokeTest1");
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "invokeTest1"));
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest1"));
      if (b)
        TestUtil.logMsg("isParameterAndReturnSpecRequired() is required");
      else
        TestUtil.logMsg("isParameterAndReturnSpecRequired() is not required");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("IsParameterAndReturnSpecRequiredTest1 failed", e);
    }

    if (!pass)
      throw new Fault("IsParameterAndReturnSpecRequiredTest1 failed");
  }

  /*
   * @testName: IsParameterAndReturnSpecRequiredTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:56; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition. Determine
   * types of parameters and return value in an implementation specific way.
   * Verify that isParameterAndReturnSpecRequired() returns false.
   */
  public void IsParameterAndReturnSpecRequiredTest2() throws Fault {
    TestUtil.logTrace("IsParameterAndReturnSpecRequiredTest2");
    boolean pass = true;
    String expected = "passed";

    try {
      TestUtil.logMsg("Call isParameterAndReturnSpecRequired() on "
          + "operation InvokeTest4");
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "invokeTest4"));
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest4"));
      if (b)
        TestUtil.logMsg("isParameterAndReturnSpecRequired() is required");
      else
        TestUtil.logMsg("isParameterAndReturnSpecRequired() is not required");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("IsParameterAndReturnSpecRequiredTest2 failed", e);
    }

    if (!pass)
      throw new Fault("IsParameterAndReturnSpecRequiredTest2 failed");
  }

  /*
   * @testName: GetPropertyNamesTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:73; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition. Add some
   * properties by invoking setProperty(). Verify that getPropertyNames()
   * correctly returns all property names that were set.
   */
  public void GetPropertyNamesTest1() throws Fault {
    TestUtil.logTrace("GetPropertyNamesTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      Iterator i = call.getPropertyNames();
      int k = 0;
      while (i.hasNext())
        TestUtil.logMsg("Property #" + k++ + " = " + (String) i.next());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPropertyNamesTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetPropertyNamesTest1 failed");
  }

  /*
   * @testName: GetOutputParamsTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:79; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition. Populate a
   * Call object to invoke a method that has output parameters. Call
   * getOutputParams() and verify that the Map of {name, value} pairs returned
   * is correct.
   */
  public void GetOutputParamsTest1() throws Fault {
    TestUtil.logTrace("GetOutputParamsTest1");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest3"));
      if (b)
        call.setReturnType(QNAME_TYPE_STRING);
      call.setOperationName(new QName(NAMESPACEURI, "invokeTest3"));
      Map map = call.getOutputParams();
      TestUtil.logErr("Did not catch expected JAXRPCException");
      pass = false;
    } catch (JAXRPCException e) {
      TestUtil.logMsg("Caught expected JAXRPCException");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetOutputParamsTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetOutputParamsTest1 failed");
  }

  /*
   * @testName: GetOutputParamsTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:79; JAXRPC:SPEC:313; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:121;
   *
   * @test_Strategy: Create a Call object to our service definition. Populate a
   * Call object to invoke a method that has output parameters. Call
   * getOutputParams() and verify that the Map of {name, value} pairs returned
   * is correct.
   */
  public void GetOutputParamsTest2() throws Fault {
    TestUtil.logTrace("GetOutputParamsTest2");
    boolean pass = true;
    String expected = "passed";
    try {
      TestUtil.logMsg("Initialize rest of Call object");
      call.setTargetEndpointAddress(url);
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "invokeTest3"));
      if (b)
        call.setReturnType(QNAME_TYPE_STRING);
      call.setOperationName(new QName(NAMESPACEURI, "invokeTest3"));
      String response = (String) call.invoke(new Object[0]);
      Map map = call.getOutputParams();
      if (!map.isEmpty()) {
        TestUtil.logErr("getOutputParams() did not return empty Map");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetOutputParamsTest2 failed", e);
    }

    if (!pass)
      throw new Fault("GetOutputParamsTest2 failed");
  }
}
