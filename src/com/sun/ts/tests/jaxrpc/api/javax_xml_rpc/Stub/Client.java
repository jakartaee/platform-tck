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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.Stub;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.Stub.";

  // The webserver username and password property names (harness properties)
  private static final String USERNAME = "user";

  private static final String PASSWORD = "password";

  // Servlet URL's for negative test cases
  private static final String BADURL = Constants.BAD_RELATIVE_URL;

  // RPC service and port information
  private static final String NAMESPACE_URI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private static final Class PORT_CLASS = Hello.class;

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private String username = null;

  private String password = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "helloservice.endpoint.1";

  private static final String WSDLLOC_URL = "helloservice.wsdlloc.1";

  private static final String SERVLET_URL = "helloservice.servlet.1";

  private String url = null;

  private URL wsdlurl = null;

  private String testservleturl = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(SERVLET_URL);
    testservleturl = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
    TestUtil.logMsg("Test Servlet URL:    " + testservleturl);
  }

  private void getTestURLfromStub() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(SERVLET_URL);
    testservleturl = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("TestServletURL: " + testservleturl);
  }

  // Get Port and Stub access via porting layer interface
  Hello port = null;

  Stub stub = null;

  private void getStubJaxrpc() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (Hello) JAXRPC_Util.getStub(
        "com.sun.ts.tests.jaxrpc.api." + "javax_xml_rpc.Stub.HelloService",
        "getHelloPort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    /* Lookup service then obtain port */
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Obtained InitialContext");
    TestUtil.logMsg("Lookup java:comp/env/service/stub");
    javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/stub");
    TestUtil.logMsg("Obtained service");
    port = (Hello) svc.getPort(Hello.class);
    TestUtil.logMsg("Obtained port");
    TestUtil.logMsg("Cast port to base Stub class");
    stub = (javax.xml.rpc.Stub) port;
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
   * @class.setup_props: webServerHost; webServerPort; user; password;
   * platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    // Initialize QNames used by the test
    SERVICE_QNAME = new QName(NAMESPACE_URI, SERVICE_NAME);
    PORT_QNAME = new QName(NAMESPACE_URI, PORT_NAME);

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
      username = p.getProperty(USERNAME);
      password = p.getProperty(PASSWORD);
      TestUtil.logMsg("Creating stub instance ...");
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getStubJaxrpc();
      } else {
        getStub();
        getTestURLfromStub();
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
   * @testName: getPropertyNamesTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:28; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:122;
   * WS4EE:SPEC:192;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * get the configurable properties for the stub class.
   */
  public void getPropertyNamesTest1() throws Fault {
    TestUtil.logTrace("getPropertyNamesTest1");
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("Get of target endpoint ...");
      try {
        iterator = stub._getPropertyNames();
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Unsupported Operation ... no further testing");
        return;
      }
      if (!iterator.hasNext()) {
        TestUtil.logMsg("Stub has no configurable properties ...");
      } else {
        TestUtil.logMsg("Stub has the following configurable properties ...");
        int i = 1;
        while (iterator.hasNext()) {
          TestUtil.logMsg("Property" + i++ + " = " + iterator.next());
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getPropertyNamesTest1 failed", e);
    }

    if (!pass)
      throw new Fault("getPropertyNamesTest1 failed");
  }

  /*
   * @testName: setPropertyTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:24; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:122;
   * WS4EE:SPEC:192;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the endpoint address property for Stub, and verify endpoint address is
   * set correctly.
   */
  public void setPropertyTest1() throws Fault {
    TestUtil.logTrace("setPropertyTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Setting endpoint address to " + url + " ...");
      stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
      TestUtil.logMsg("Verify correct setting of endpoint address ...");
      String rec = (String) stub._getProperty(Stub.ENDPOINT_ADDRESS_PROPERTY);
      if (!url.equals(rec)) {
        TestUtil.logErr("endpoint address not set correctly: " + "expected "
            + url + ", received " + rec);
        pass = false;
      } else {
        TestUtil.logMsg("endpoint address set correctly to " + url);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("setPropertyTest1 failed", e);
    }

    if (!pass)
      throw new Fault("setPropertyTest1 failed");
  }

  /*
   * @testName: setPropertyTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:22; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:122;
   * WS4EE:SPEC:192;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the username for authentication for the Stub, and verify username is
   * set correctly.
   */
  public void setPropertyTest2() throws Fault {
    TestUtil.logTrace("setPropertyTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Setting username to " + username + " ...");
      TestUtil.logMsg("Setting username to " + username + " ...");
      if (modeProperty.equals("standalone")) {
        stub._setProperty(Stub.USERNAME_PROPERTY, username);
      } else {
        try {
          stub._setProperty(Stub.USERNAME_PROPERTY, username);
        } catch (UnsupportedOperationException e) {
          TestUtil.logMsg("Unsupported Operation ... no further testing");
          return;
        }
      }
      TestUtil.logMsg("Verify correct setting of username ...");
      String rec = (String) stub._getProperty(Stub.USERNAME_PROPERTY);
      if (!username.equals(rec)) {
        TestUtil.logErr("username not set correctly: " + "expected " + username
            + ", received " + rec);
        pass = false;
      } else {
        TestUtil.logMsg("username set correctly to " + username);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("setPropertyTest2 failed", e);
    }

    if (!pass)
      throw new Fault("setPropertyTest2 failed");
  }

  /*
   * @testName: setPropertyTest3
   *
   * @assertion_ids: JAXRPC:JAVADOC:23; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:122;
   * WS4EE:SPEC:192;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the password for authentication for the Stub, and verify password is
   * set correctly.
   */
  public void setPropertyTest3() throws Fault {
    TestUtil.logTrace("setPropertyTest3");
    boolean pass = true;
    try {
      TestUtil.logMsg("Setting password to " + password + " ...");
      if (modeProperty.equals("standalone")) {
        stub._setProperty(Stub.PASSWORD_PROPERTY, password);
      } else {
        try {
          stub._setProperty(Stub.PASSWORD_PROPERTY, password);
        } catch (UnsupportedOperationException e) {
          TestUtil.logMsg("Unsupported Operation ... no further testing");
          return;
        }
      }
      TestUtil.logMsg("Verify correct setting of property ...");
      String rec = (String) stub._getProperty(Stub.PASSWORD_PROPERTY);
      if (!password.equals(rec)) {
        TestUtil.logErr("password not set correctly: " + "expected " + password
            + ", received " + rec);
        pass = false;
      } else {
        TestUtil.logMsg("password set correctly to " + password);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("setPropertyTest3 failed", e);
    }

    if (!pass)
      throw new Fault("setPropertyTest3 failed");
  }

  /*
   * @testName: setPropertyTest4
   *
   * @assertion_ids: JAXRPC:JAVADOC:25; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:122;
   * WS4EE:SPEC:192;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the session maintain property for the Stub, and verify it is set
   * correctly.
   */
  public void setPropertyTest4() throws Fault {
    TestUtil.logTrace("setPropertyTest4");
    boolean pass = true;
    Boolean v = new Boolean("true");

    try {
      TestUtil.logMsg("Setting session maintain property to true ...");
      if (modeProperty.equals("standalone")) {
        stub._setProperty(Stub.SESSION_MAINTAIN_PROPERTY, v);
      } else {
        try {
          stub._setProperty(Stub.SESSION_MAINTAIN_PROPERTY, v);
        } catch (UnsupportedOperationException e) {
          TestUtil.logMsg("Unsupported Operation ... no further testing");
          return;
        }
      }
      TestUtil.logMsg("Verify correct setting of property ...");
      Boolean rec = (Boolean) stub._getProperty(Stub.SESSION_MAINTAIN_PROPERTY);
      if (!v.equals(rec)) {
        TestUtil.logErr("session maintain not set correctly: " + "expected " + v
            + ", received " + rec);
        pass = false;
      } else {
        TestUtil.logMsg("session maintain set correctly to " + v);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("setPropertyTest4 failed", e);
    }

    if (!pass)
      throw new Fault("setPropertyTest4 failed");
  }

  /*
   * @testName: setPropertyInvalidTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:26; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:122;
   * WS4EE:SPEC:192;
   *
   * @test_Strategy: Create a stub instance to our service definition interface.
   * Set invalid properties for the Stub. Verify invalid properties are not set.
   */
  public void setPropertyInvalidTest() throws Fault {
    TestUtil.logTrace("setPropertyInvalidTest");
    boolean pass = true;

    if (!invalidPropertyTest1())
      pass = false;
    if (!invalidPropertyTest2())
      pass = false;

    if (!pass)
      throw new Fault("setPropertyInvalidTest failed");
  }

  /*
   * @testName: setPropertyTest6a
   *
   * @assertion_ids: JAXRPC:JAVADOC:26; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; JAXRPC:SPEC:335; JAXRPC:SPEC:336; WS4EE:SPEC:139;
   *
   * @test_Strategy: Create a stub instance to our service definition interface.
   * Set properties for the Stub with valid and invalid stub configuration
   * errors. Verify stub configuration errors are detected at runtime. This test
   * sets a proper URL. This test also verifies that the JSR109-required call to
   * _setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY) works. It is not optional in
   * JSR-109.
   */
  public void setPropertyTest6a() throws Fault {
    TestUtil.logTrace("setPropertyTest6a");
    boolean pass = true;

    if (!invokeRPCMethodPositiveTest())
      pass = false;

    if (!pass)
      throw new Fault("setPropertyTest6a failed");
  }

  /*
   * @testName: setPropertyTest6b
   *
   * @assertion_ids: JAXRPC:JAVADOC:26; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; JAXRPC:SPEC:335; JAXRPC:SPEC:336; WS4EE:SPEC:139;
   *
   * @test_Strategy: Create a stub instance to our service definition interface.
   * Set properties for the Stub with valid and invalid stub configuration
   * errors. Verify stub configuration errors are detected at runtime. This test
   * sets a bad URL, a proper URL to an incorrect endpoint. This test also
   * verifies that the JSR109-required call to
   * _setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY) works. It is not optional in
   * JSR-109.
   */
  public void setPropertyTest6b() throws Fault {
    TestUtil.logTrace("setPropertyTest6b");
    boolean pass = true;

    if (!invokeRPCMethodNegativeTest1())
      pass = false;
    if (!invokeRPCMethodNegativeTest2())
      pass = false;

    if (!pass)
      throw new Fault("setPropertyTest6b failed");
  }

  /*
   * @testName: getPropertyTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:27; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:122;
   * WS4EE:SPEC:192;
   *
   * @test_Strategy: Create a stub instance to our service definition interface.
   * Get valid properties for the Stub. Verify only valid properties are
   * received.
   */
  public void getPropertyTest() throws Fault {
    TestUtil.logTrace("getPropertyTest");
    boolean pass = true;

    if (!getPropertyPositiveTest())
      pass = false;

    if (!pass)
      throw new Fault("getPropertyTest failed");
  }

  /*
   * @testName: getPropertyInvalidTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:27; JAXRPC:SPEC:312; JAXRPC:SPEC:314;
   * JAXRPC:SPEC:318; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:122;
   * WS4EE:SPEC:192;
   *
   * @test_Strategy: Create a stub instance to our service definition interface.
   * Get invalid properties for the Stub. Verify no invalid properties are
   * received.
   */
  public void getPropertyInvalidTest() throws Fault {
    TestUtil.logTrace("getPropertyInvalidTest");
    boolean pass = true;

    if (!getPropertyNegativeTest1())
      pass = false;
    if (!getPropertyNegativeTest2())
      pass = false;

    if (!pass)
      throw new Fault("getPropertyInvalidTest failed");
  }

  private boolean getPropertyPositiveTest() {
    TestUtil.logTrace("getPropertyPositiveTest");
    boolean pass = true;
    Object p = null;
    String s = null;
    try {
      TestUtil.logMsg("Get of endpoint address ...");
      try {
        p = stub._getProperty(Stub.ENDPOINT_ADDRESS_PROPERTY);
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Unsupported Operation ... no further testing");
        return true;
      }
      if (p != null) {
        TestUtil.logMsg("endpoint address received, value is: " + p);
      } else {
        TestUtil.logMsg("endpoint address received, value is: null");
      }
      TestUtil.logMsg("Set/Get of endpoint address ...");
      stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
      s = (String) stub._getProperty(Stub.ENDPOINT_ADDRESS_PROPERTY);
      if (!s.equals(url)) {
        TestUtil.logErr("endpoint address not received correctly: "
            + "expected " + url + ", received " + s);
        pass = false;
      } else {
        TestUtil.logMsg("endpoint address received correctly");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "getPropertyPositiveTest2");
    return pass;
  }

  private boolean getPropertyNegativeTest1() {
    TestUtil.logTrace("getPropertyNegativeTest1");
    boolean pass = true;
    Object p = null;
    try {
      TestUtil.logMsg("Get of invalid property ...");
      try {
        p = stub._getProperty(Constants.INVALID_PROPERTY);
        TestUtil.logErr("no exception occurred ...");
        pass = false;
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Unsupported Operation ... no further testing");
      } catch (JAXRPCException e) {
        TestUtil.logMsg("JAXRPCException as expected ...");
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception ... ", e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "getPropertyNegativeTest1");
    return pass;
  }

  private boolean getPropertyNegativeTest2() {
    TestUtil.logTrace("getPropertyNegativeTest2");
    boolean pass = true;
    Object p = null;
    try {
      TestUtil.logMsg("Get of invalid property ...");
      try {
        p = stub._getProperty(Constants.NULL_PROPERTY);
        TestUtil.logErr("no exception occurred ...");
        pass = false;
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Unsupported Operation ... no further testing");
      } catch (JAXRPCException e) {
        TestUtil.logMsg("JAXRPCException as expected ...");
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception ... ", e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "getPropertyNegativeTest2");
    return pass;
  }

  private boolean invokeRPCMethodPositiveTest() {
    TestUtil.logTrace("invokeRPCMethodPositiveTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Setting endpoint address to " + url + " ...");
      stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
      TestUtil.logMsg("Invoking RPC method hello(\"foo\") and expect "
          + "'Hello, foo!' ...");
      String response = port.hello("foo");
      if (!response.equals("Hello, foo!")) {
        TestUtil.logErr(
            "RPC failed - expected \"Hello, foo!\", received: " + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "invokeRPCMethodPositiveTest");
    return pass;
  }

  private boolean invokeRPCMethodNegativeTest1() {
    TestUtil.logTrace("invokeRPCMethodNegativeTest1");
    boolean pass = true;
    try {
      url = ctsurl.getURLString(PROTOCOL, hostname, portnum, BADURL);
      TestUtil.logMsg("Setting target endpoint to " + url + " ...");
      stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
      TestUtil.logMsg("Invoking RPC method on an invalid target endpoint");
      try {
        String response = port.hello("foo");
        TestUtil.logErr("Exception did not occur - unexpected");
        pass = false;
      } catch (Exception e) {
        TestUtil.logMsg("Exception did occur - expected");
        TestUtil.logMsg("Exception was: " + e);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "invokeRPCMethodNegativeTest1");
    return pass;
  }

  private boolean invokeRPCMethodNegativeTest2() {
    TestUtil.logTrace("invokeRPCMethodNegativeTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Setting target endpoint to " + testservleturl + " ...");
      stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, testservleturl);
      TestUtil.logMsg(
          "Invoking RPC method on an valid target endpoint that does not process SOAP messages");
      try {
        String response = port.hello("foo");
        TestUtil.logErr("Exception did not occur - unexpected");
        pass = false;
      } catch (Exception e) {
        TestUtil.logMsg("Exception did occur - expected");
        TestUtil.logMsg("Exception was: " + e);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "invokeRPCMethodNegativeTest2");
    return pass;
  }

  private boolean invalidPropertyTest1() {
    TestUtil.logTrace("invalidPropertyTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Setting invalid property " + Constants.INVALID_PROPERTY + " ...");
      try {
        stub._setProperty(Constants.INVALID_PROPERTY, new Object());
        TestUtil.logErr("No exception ... unexpected");
        pass = false;
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Unsupported Operation ... no further testing");
      } catch (JAXRPCException e) {
        TestUtil.logMsg("JAXRPCException caught as expected ...");
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception ... ", e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "invalidPropertyTest1");
    return pass;
  }

  private boolean invalidPropertyTest2() {
    TestUtil.logTrace("invalidPropertyTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Setting invalid property - null ...");
      try {
        stub._setProperty(Constants.NULL_PROPERTY, new Object());
        TestUtil.logErr("No exception ... unexpected");
        pass = false;
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Unsupported Operation ... no further testing");
      } catch (JAXRPCException e) {
        TestUtil.logMsg("JAXRPCException caught as expected ...");
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception ... ", e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "invalidPropertyTest2");
    return pass;
  }

  private boolean printTestStatus(boolean pass, String test) {
    if (pass)
      TestUtil.logMsg("" + test + " ... PASSED");
    else
      TestUtil.logErr("" + test + " ... FAILED");

    return pass;
  }
}
