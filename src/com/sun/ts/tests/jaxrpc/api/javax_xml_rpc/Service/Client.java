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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.Service;

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
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.Service.";

  private final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private QName BAD_PORT_QNAME;

  private QName QNAME_TYPE_STRING;

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "helloservice.endpoint.1";

  private static final String WSDLLOC_URL = "helloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private String SERVICE_NAME_WITH_WSDL = "service";

  private String SERVICE_NAME_NO_WSDL = "service_no_wsdl";

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
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    // Initialize QNAMES used in the test
    SERVICE_QNAME = new QName(NAMESPACEURI, "HelloService");
    PORT_QNAME = new QName(NAMESPACEURI, "HelloPort");
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
        TestUtil.printStackTrace(e);
        pass = false;
      }
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
      } else {
        getTestDIIURLs();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
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
   * @testName: CreateCallConstructor1Test1NoWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:41; JAXRPC:SPEC:314; WS4EE:SPEC:138;
   * WS4EE:SPEC:173; WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191;
   * WS4EE:SPEC:192; WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208;
   * WS4EE:SPEC:2; WS4EE:SPEC:3; WS4EE:SPEC:4; WS4EE:SPEC:7; WS4EE:SPEC:8;
   * WS4EE:SPEC:9; WS4EE:SPEC:35; WS4EE:SPEC:38; WS4EE:SPEC:136; WS4EE:SPEC:117;
   * WS4EE:SPEC:124; WS4EE:SPEC:134;
   *
   * @test_Strategy: Create a Call object using the empty constructor via
   * Service.createCall(). Verify that the Call object was successfully created.
   */
  public void CreateCallConstructor1Test1NoWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor1Test1NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
      }
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
        TestUtil.logMsg("Set operation name to invokeTest2");
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

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"invokeTest2\")");
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
      throw new Fault("CreateCallConstructor1Test1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor1Test1NoWsdl failed");
  }

  /*
   * @testName: CreateCallConstructor1Test1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:41; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:173; WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191;
   * WS4EE:SPEC:192; WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208;
   * WS4EE:SPEC:117;
   *
   * @test_Strategy: Create a Call object using the empty constructor via
   * Service.createCall(). Verify that the Call object was successfully created.
   */
  public void CreateCallConstructor1Test1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor1Test1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
        return;
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
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
        TestUtil.logMsg("Set operation name to invokeTest2");
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

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"invokeTest2\")");
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

  /* The following commented out test is invalid/unspecified for JSR109 */
  /*
   * #testName: CreateCallConstructor2Test1NoWsdl
   *
   * #assertion_ids: JAXRPC:JAVADOC:35; JAXRPC:SPEC:314; WS4EE:SPEC:138;
   *
   * #test_Strategy: Create a Call object using the constructor
   * Service.createCall(QName). Verify that the that the correct exception is
   * thrown.
   */
  public void CreateCallConstructor2Test1NoWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor2Test1NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
      }
      TestUtil.logMsg("Create Call object via Service.createCall(QName)");
      TestUtil.logMsg("PORT_QNAME = " + PORT_QNAME);
      TestUtil.logMsg("Expect an (UnsupportedOperationException)");
      try {
        Call call = service.createCall(PORT_QNAME);
        TestUtil.logErr("Did not throw expected UnsupportedOperationException");
        pass = false;
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught UnsupportedOperationException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateCallConstructor2Test1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor2Test1NoWsdl failed");
  }

  /*
   * @testName: CreateCallConstructor2Test1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:41; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:173; WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191;
   * WS4EE:SPEC:192; WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208;
   * WS4EE:SPEC:117;
   *
   * @test_Strategy: Create a Call object using the empty constructor via
   * Service.createCall(). Verify that the Call object was successfully created.
   */
  public void CreateCallConstructor2Test1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor2Test1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Create Call object via Service.createCall(QName)");
      TestUtil.logMsg("PORT_QNAME = " + PORT_QNAME);
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
        TestUtil.logMsg("Set operation name to invokeTest2");
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

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"invokeTest2\")");
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

  /* The following commented out test is invalid/unspecified for JSR109 */
  /*
   * #testName: CreateCallConstructor3Test1NoWsdl
   *
   * #assertion_ids: JAXRPC:JAVADOC:37; JAXRPC:SPEC:314; WS4EE:SPEC:138;
   *
   * #test_Strategy: Create a Call object using the constructor
   * Service.createCall(QName, QName). Verify that the correct exception is
   * thrown.
   */
  public void CreateCallConstructor3Test1NoWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor3Test1NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
      }
      TestUtil
          .logMsg("Create Call object via Service.createCall(QName, QName)");
      QName OPERATION_QNAME = new QName(NAMESPACEURI, "invokeTest2");
      TestUtil.logMsg(
          "PORT_QNAME = " + PORT_QNAME + ", Operation = " + OPERATION_QNAME);
      TestUtil.logMsg("Expect an (UnsupportedOperationException)");
      try {
        Call call = service.createCall(PORT_QNAME, OPERATION_QNAME);
        TestUtil.logErr("Did not throw expected UnsupportedOperationException");
        pass = false;
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught UnsupportedOperationException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateCallConstructor3Test1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor3Test1NoWsdl failed");
  }

  /*
   * @testName: CreateCallConstructor3Test1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:41; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:173; WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191;
   * WS4EE:SPEC:192; WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208;
   * WS4EE:SPEC:117;
   *
   * @test_Strategy: Create a Call object using the empty constructor via
   * Service.createCall(). Verify that the Call object was successfully created.
   */
  public void CreateCallConstructor3Test1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor3Test1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil
          .logMsg("Create Call object via Service.createCall(QName, QName)");
      QName OPERATION_QNAME = new QName(NAMESPACEURI, "invokeTest2");
      TestUtil.logMsg(
          "PORT_QNAME = " + PORT_QNAME + ", Operation = " + OPERATION_QNAME);
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
        TestUtil.logMsg("Set operation name to invokeTest2");

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

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"invokeTest2\")");
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

  /* The following commented out test is invalid/unspecified for JSR109 */
  /*
   * #testName: CreateCallConstructor4Test1NoWsdl
   *
   * #assertion_ids: JAXRPC:JAVADOC:37; JAXRPC:SPEC:314; WS4EE:SPEC:138;
   *
   * #test_Strategy: Create a Call object using the constructor
   * Service.createCall(QName, String). Verify that the correct exception is
   * thrown.
   */
  public void CreateCallConstructor4Test1NoWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor4Test1NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
      }
      TestUtil
          .logMsg("Create Call object via Service.createCall(QName, String)");
      String operation = "invokeTest2";
      TestUtil
          .logMsg("PORT_QNAME = " + PORT_QNAME + ", Operation = " + operation);
      TestUtil.logMsg("Expect an (UnsupportedOperationException)");
      try {
        Call call = service.createCall(PORT_QNAME, operation);
        TestUtil.logErr("Did not throw expected UnsupportedOperationException");
        pass = false;
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught UnsupportedOperationException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateCallConstructor4Test1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor4Test1NoWsdl failed");
  }

  /*
   * @testName: CreateCallConstructor4Test1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:39; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:173; WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191;
   * WS4EE:SPEC:192; WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208;
   * WS4EE:SPEC:117;
   *
   * @test_Strategy: Create a Call object using the constructor
   * Service.createCall(QName, String). Verify that the Call object was
   * successfully created.
   */
  public void CreateCallConstructor4Test1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateCallConstructor4Test1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil
          .logMsg("Create Call object via Service.createCall(QName, String)");
      String operation = "invokeTest2";
      TestUtil
          .logMsg("PORT_QNAME = " + PORT_QNAME + ", Operation = " + operation);
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

        TestUtil.logMsg("Attempt to invoke RPC method Tests(\"invokeTest2\")");
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
      throw new Fault("CreateCallConstructor4Test1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateCallConstructor4Test1WithWsdl failed");
  }

  /*
   * @testName: GetCallsPosTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:33; JAXRPC:SPEC:314; WS4EE:SPEC:132;
   * WS4EE:SPEC:173; WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191;
   * WS4EE:SPEC:192; WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208;
   * WS4EE:SPEC:117;
   *
   * @test_Strategy: Get an array of Call objects by calling the api
   * Service.getCalls(). Access to WSDL metadata and valid port name. Verify the
   * behavior.
   */
  public void GetCallsPosTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetCallsPosTest1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getCalls (valid port name/WSDL " + "access) - positive test 1");
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
      throw new Fault("GetCallsPosTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetCallsPosTest1WithWsdl failed");
  }

  /*
   * @testName: GetCallsNegTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:34; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   *
   * @test_Strategy: Get an array of Call objects by calling the api
   * Service.getCalls(). Access to WSDL metadata and invalid port name. Verify
   * that an exception occurs due to port not found. Expect a ServiceException
   */
  public void GetCallsNegTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetCallsNegTest1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Test getCalls (invalid port name/"
          + "WSDL acesss) - negative test 1");
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
      throw new Fault("GetCallsNegTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetCallsNegTest1WithWsdl failed");
  }

  /*
   * @testName: GetCallsNegTest2WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:34; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   *
   * @test_Strategy: Get an array of Call objects by calling the api
   * Service.getCalls(). Access to WSDL metadata and a null port name. Verify
   * that an exception occurs due to port not found. Expect a ServiceException.
   */
  public void GetCallsNegTest2WithWsdl() throws Fault {
    TestUtil.logTrace("GetCallsNegTest2WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getCalls (null port name/" + "WSDL access) - negative test 2");
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
      throw new Fault("GetCallsNegTest2WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetCallsNegTest2WithWsdl failed");
  }

  /* The following commented out test is invalid/unspecified for JSR109 */
  /*
   * @testName: GetCallsNegTest1NoWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:34; JAXRPC:SPEC:314; WS4EE:SPEC:138;
   *
   * @test_Strategy: Get an array of Call objects by calling the api
   * Service.getCalls(). No access to WSDL metadata and valid port name. Verify
   * the behavior.
   */
  public void GetCallsNegTest1NoWsdl() throws Fault {
    TestUtil.logTrace("GetCallsNegTest1NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
        return;
      }
      TestUtil.logMsg("Test getCalls (valid port name/no "
          + "WSDL access) - negative test 1");
      TestUtil.logMsg("Get an array of Call objects via Service.getCalls()");
      TestUtil.logMsg("Expecting an (UnsupportedOperationException)");
      try {
        Call[] calls = service.getCalls(PORT_QNAME);
        TestUtil.logErr("Did not throw expected UnsupportedOperationException");
        pass = false;
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (ServiceException e) {
        TestUtil.logMsg("Caught expected ServiceException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetCallsNegTest1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetCallsNegTest1NoWsdl failed");
  }

  /*
   * @testName: GetPortConstructor1PosTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:29; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   * WS4EE:SPEC:119; WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort( QName, Class)
   * to return a dynamic proxy for the service port. Pass a valid port name with
   * WSDL access. Verify that the method returns a dynamic proxy.
   */
  public void GetPortConstructor1PosTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPortConstructor1PosTest1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getPort (valid port name/" + "WSDL access) - positive test 1");
      TestUtil.logMsg("Call Service.getPort(QName, Class)");
      TestUtil.logMsg("Should find port");
      Hello tests = (Hello) service.getPort(PORT_QNAME, Hello.class);
      if (tests == null) {
        TestUtil.logErr("getPort(QName, Class) returned null");
        pass = false;
      } else if (!(tests instanceof Hello)) {
        TestUtil.logErr(
            "getPort(QName, Class) did not" + " return instance of Hello");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor1PosTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor1PosTest1WithWsdl failed");
  }

  /*
   * @testName: GetPortConstructor1NegTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:30; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   * WS4EE:SPEC:119; WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort( QName, Class)
   * to return a dynamic proxy for the service port. Pass an invalid port name
   * with accesss to WSDL metadata. Verify that an exception occurs due to port
   * not found. Expect a ServiceException.
   */
  public void GetPortConstructor1NegTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPortConstructor1NegTest1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Test getPort (invalid port name/"
          + "WSDL access) - negative test 1");
      TestUtil.logMsg("Call Service.getPort(QName, Class)");
      TestUtil.logMsg("Should not find port (expect ServiceException)");
      Hello tests = (Hello) service.getPort(BAD_PORT_QNAME, Hello.class);
      TestUtil.logErr("Did not throw expected ServiceException");
      pass = false;
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor1NegTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor1NegTest1WithWsdl failed");
  }

  /* The following commented out test is invalid/unspecified for JSR109 */
  /*
   * @testName: GetPortConstructor1NegTest1NoWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:30; JAXRPC:SPEC:314; WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort( QName, Class)
   * to return a dynamic proxy for the service port. Pass a valid port name but
   * no access to WSDL metadata. Verify behaviourn
   */
  public void GetPortConstructor1NegTest1NoWsdl() throws Fault {
    TestUtil.logTrace("GetPortConstructor1NegTest1NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
        return;
      }
      TestUtil.logMsg("Test getPort (valid port name/no "
          + "WSDL access) - negative test 1");
      TestUtil.logMsg("Call Service.getPort(QName, Class)");
      TestUtil.logMsg("Expect an (UnsupportedOperationException)");
      Hello tests = (Hello) service.getPort(PORT_QNAME, Hello.class);
      TestUtil.logErr("Did not throw expected UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught expected UnsupportedOperationException");
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor1NegTest1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor1NegTest1NoWsdl failed");
  }

  /* The following commented out test is invalid/unspecified for JSR109 */
  /*
   * @testName: GetPortConstructor1NegTest2NoWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:30; JAXRPC:SPEC:314; WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort( QName, Class)
   * to return a dynamic proxy for the service port. Pass a null port name and
   * no WSDL access. Verify behaviour.
   */
  public void GetPortConstructor1NegTest2NoWsdl() throws Fault {
    TestUtil.logTrace("GetPortConstructor1NegTest2NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
        return;
      }
      TestUtil.logMsg(
          "Test getPort (null port name/" + "WSDL access) - negative test 2");
      TestUtil.logMsg("Call Service.getPort(QName, Class)");
      TestUtil.logMsg("Expect an (UnsupportedOperationException)");
      Hello tests = (Hello) service.getPort(PORT_QNAME, Hello.class);
      TestUtil.logErr("Did not throw expected UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught expected UnsupportedOperationException");
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor1NegTest2NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor1NegTest2NoWsdl failed");
  }

  /*
   * @testName: GetPortConstructor2PosTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:32; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   * WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort(Class) to return
   * a dynamic proxy for the service port. Pass a valid SEI class. Access to
   * WSDL metadata. Verify behavior.
   */
  public void GetPortConstructor2PosTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPortConstructor2PosTest1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getPort (valid SEI class/" + "WSDL access) - positive test 1");
      TestUtil.logMsg("Call Service.getPort(Class)");
      TestUtil.logMsg("Should find port");
      Hello tests = (Hello) service.getPort(Hello.class);
      if (tests == null) {
        TestUtil.logErr("getPort(Class) returned null");
        pass = false;
      } else if (!(tests instanceof Hello)) {
        TestUtil.logErr("getPort(Class) did not" + " return instance of Hello");
        pass = false;
      }
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor2PosTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor2PosTest1WithWsdl failed");
  }

  /*
   * @testName: GetPortConstructor2NegTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:32; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   * WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort(Class) to return
   * a dynamic proxy for the service port. Pass an invalid SEI class. WSDL
   * metadata access. Verify behavior. Expect ServiceException.
   */
  public void GetPortConstructor2NegTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPortConstructor2NegTest1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Test getPort (invalid SEI class/"
          + "WSDL access) - negative test 1");
      TestUtil.logMsg("Call Service.getPort(Class)");
      TestUtil.logMsg("Should not find port (expect ServiceException)");
      Hello tests = (Hello) service
          .getPort(java.rmi.activation.Activator.class);
      TestUtil.logErr("Did not throw expected ServiceException");
      pass = false;
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor2NegTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor2NegTest1WithWsdl failed");
  }

  /* The following commented out test is invalid/unspecified for JSR109 */
  /*
   * @testName: GetPortConstructor2NegTest1NoWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:32; JAXRPC:SPEC:314; WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPort(Class) to return
   * a dynamic proxy for the service port. Pass a valid SEI class. No WSDL
   * metadata access. Verify behavior.
   */
  public void GetPortConstructor2NegTest1NoWsdl() throws Fault {
    TestUtil.logTrace("GetPortConstructor2NegTest1NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
        return;
      }
      TestUtil.logMsg("Test getPort (valid SEI class/"
          + "no WSDL access) - negative test 1");
      TestUtil.logMsg("Call Service.getPort(Class)");
      TestUtil.logMsg("Expect an (UnsupportedOperationException)");
      Hello tests = (Hello) service.getPort(Hello.class);
      TestUtil.logErr("Did not throw expected UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught expected UnsupportedOperationException");
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortConstructor2NegTest1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortConstructor2NegTest1NoWsdl failed");
  }

  /*
   * @testName: GetPortsTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:44; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   * WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPorts() to return a
   * list of qualified names of the ports grouped by this service. Verify that
   * the method returns a list of qualified names of the ports grouped by this
   * service. Create a Service object with access to WSDL metadata.
   */
  public void GetPortsTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPortsTest1WithWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil
          .logMsg("Test getPorts with (WSDL access) - " + "positive test 1");
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
      throw new Fault("GetPortsTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortsTest1WithWsdl failed");
  }

  /* The following commented out test is invalid/unspecified for JSR109 */
  /*
   * @testName: GetPortsTest1NoWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:44; JAXRPC:SPEC:314; WS4EE:SPEC:118;
   *
   * @test_Strategy: Create a Call object. Call Service.getPorts() to return a
   * list of qualified names of the ports grouped by this service. Verify
   * behaviour.
   */
  public void GetPortsTest1NoWsdl() throws Fault {
    TestUtil.logTrace("GetPortsTest1NoWsdl");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
        return;
      }
      TestUtil
          .logMsg("Test getPorts with (no WSDL access) - " + "positive test 2");
      TestUtil.logMsg("Call Service.getPorts()");
      TestUtil.logMsg("Expect an (UnsupportedOperationException)");
      Iterator i = service.getPorts();
      TestUtil.logErr("Did not throw expected UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught expected UnsupportedOperationException");
    } catch (ServiceException e) {
      TestUtil.logMsg("Caught expected ServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortsTest1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortsTest1NoWsdl failed");
  }

  /*
   * @testName: GetServiceNameTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:43; JAXRPC:SPEC:314; WS4EE:SPEC:138;
   * WS4EE:SPEC:208
   *
   * @test_Strategy: Create a Call object. Call Service.getServiceName() to
   * return the name of this service.
   */
  public void GetServiceNameTest1() throws Fault {
    TestUtil.logTrace("GetServiceNameTest1");
    boolean pass = true;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Create Call object via Service.createCall()");
      Call call = service.createCall();
      TestUtil.logMsg("Get service name via Service.getServiceName()");
      QName sname = service.getServiceName();
      TestUtil.logMsg(
          "ServiceName = " + SERVICE_QNAME + "\ngetServiceName() = " + sname);
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

  /*
   * @testName: GetWSDLDocumentLocationTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:45; JAXRPC:SPEC:314; WS4EE:SPEC:208;
   * 
   * @test_Strategy: Call Service.getWSDLDocument- Location() to return the
   * location of the WSDL document for this service.
   */
  public void GetWSDLDocumentLocationTest1() throws Fault {
    TestUtil.logTrace("GetWSDLDocumentLocationTest1");
    boolean pass = false;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Call Service.getWSDLDocumentLocation() part1");
      URL url = service.getWSDLDocumentLocation();
      TestUtil.logMsg("WSDLURL = " + url);
      if (url != null) {
        TestUtil.logErr("WSDLURL is not null (expected)");
        pass = true;
      } else
        TestUtil.logMsg("WSDLURL is null (unexpected)");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetWSDLDocumentLocationTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetWSDLDocumentLocationTest1 failed");
  }

  /*
   * @testName: GetTypeMappingRegistryTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:46; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   *
   * @test_Strategy: Create a Call object. Call Service.getTypeMapping-
   * Registry(). Access to WDSL metadata. Verify behavior.
   */
  public void GetTypeMappingRegistryTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetTypeMappingRegistryTest1WithWsdl");
    boolean pass = false;
    TypeMappingRegistry tmrRegistry = null;
    Service service = null;
    try {
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Call Service.getTypeMappingRegistry()");
      tmrRegistry = service.getTypeMappingRegistry();
      if (tmrRegistry == null)
        TestUtil.logMsg("No TypeMappingRegistry defined on "
            + "Service object (returned null)");
      else
        TestUtil.logMsg("TypeMappingRegistry defined on Service "
            + "object (returned = " + tmrRegistry + ")");
      if (modeProperty.equals("standalone"))
        pass = true;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught UnsupportedOperationException");
      pass = true;
    } catch (JAXRPCException e) {
      TestUtil.logErr("Caught unexpected JAXRPCException", e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetTypeMappingRegistryTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetTypeMappingRegistryTest1WithWsdl failed");
  }

  /*
   * @testName: GetTypeMappingRegistryTest1NoWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:46; JAXRPC:SPEC:314; WS4EE:SPEC:173;
   * WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191; WS4EE:SPEC:192;
   * WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208; WS4EE:SPEC:117;
   *
   * @test_Strategy: Create a Call object. Call Service.getTypeMapping-
   * Registry(). No access to WSDL metadata. Verify behavior.
   */
  public void GetTypeMappingRegistryTest1NoWsdl() throws Fault {
    TestUtil.logTrace("GetTypeMappingRegistryTest1NoWsdl");
    boolean pass = false;
    TypeMappingRegistry tmrRegistry = null;
    Service service = null;
    try {
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
      }
      TestUtil.logMsg("Call Service.getTypeMappingRegistry()");
      tmrRegistry = service.getTypeMappingRegistry();
      if (tmrRegistry == null)
        TestUtil.logMsg("No TypeMappingRegistry defined on "
            + "Service object (returned null)");
      else
        TestUtil.logMsg("TypeMappingRegistry defined on Service "
            + "object (returned = " + tmrRegistry + ")");
      if (modeProperty.equals("standalone"))
        pass = true;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught UnsupportedOperationException");
      pass = true;
    } catch (JAXRPCException e) {
      TestUtil.logErr("Caught unexpected JAXRPCException", e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetTypeMappingRegistryTest1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetTypeMappingRegistryTest1NoWsdl failed");
  }

  /*
   * @testName: GetHandlerRegistryTest1WithWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:47; JAXRPC:SPEC:314; WS4EE:SPEC:28;
   * WS4EE:SPEC:173; WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191;
   * WS4EE:SPEC:192; WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208;
   * WS4EE:SPEC:117;
   *
   * @test_Strategy: Create a Call object. Call Service.getHandlerRegistry() to
   * get the configured HandlerRegistry. No access to WSDL metadata. Verify
   * behavior.
   */
  public void GetHandlerRegistryTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetHandlerRegistryTest1WithWsdl");
    boolean pass = false;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Call Service.getHandlerRegistry() - should throw UnsupportedOperationException");
      HandlerRegistry hr = service.getHandlerRegistry();
      TestUtil.logMsg(
          "service.getHandlerRegistry() did not throw UnsupportedOperationException");
      if (modeProperty.equals("standalone"))
        pass = true;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught UnsupportedOperationException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetHandlerRegistryTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetHandlerRegistryTest1WithWsdl failed");
  }

  /*
   * @testName: GetHandlerRegistryTest1NoWsdl
   *
   * @assertion_ids: JAXRPC:JAVADOC:47; JAXRPC:SPEC:314; WS4EE:SPEC:28;
   * WS4EE:SPEC:173; WS4EE:SPEC:176; WS4EE:SPEC:189; WS4EE:SPEC:191;
   * WS4EE:SPEC:192; WS4EE:SPEC:205; WS4EE:SPEC:206; WS4EE:SPEC:208;
   * WS4EE:SPEC:117;
   *
   * @test_Strategy: Create a Call object. Call Service.getHandlerRegistry() to
   * get the configured HandlerRegistry. No access to WSDL metadata. Verify
   * behavior.
   */
  public void GetHandlerRegistryTest1NoWsdl() throws Fault {
    TestUtil.logTrace("GetHandlerRegistryTest1NoWsdl");
    boolean pass = false;
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXRPC service instance without WSDL");
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_NO_WSDL);
      }
      TestUtil.logMsg(
          "Call Service.getHandlerRegistry() - should throw UnsupportedOperationException");
      HandlerRegistry hr = service.getHandlerRegistry();
      TestUtil.logMsg(
          "service.getHandlerRegistry() did not throw UnsupportedOperationException");
      if (modeProperty.equals("standalone"))
        pass = true;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught UnsupportedOperationException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetHandlerRegistryTest1NoWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetHandlerRegistryTest1NoWsdl failed");
  }
}
