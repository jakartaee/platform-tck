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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_server.ServletEndpointContext;

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
import javax.xml.rpc.handler.*;

import com.sun.javatest.Status;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private String username = "";

  private String password = "";

  private String unauthUsername = "";

  private String unauthPassword = "";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String UserNameProp = "user";

  private static final String PasswordProp = "password";

  private static final String unauthUserNameProp = "authuser";

  private static final String unauthPasswordProp = "authpassword";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_server.ServletEndpointContext.";

  private final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "hellosecureservice.endpoint.1";

  private static final String WSDLLOC_URL = "hellosecureservice.wsdlloc.1";

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

  // Get Port and Stub access via porting layer interface
  Hello port = null;

  Stub stub = null;

  private void getStubJaxrpc() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (Hello) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_server."
            + "ServletEndpointContext.HelloService", "getHelloPort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    /* Lookup service then obtain port */
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Obtained InitialContext");
    TestUtil.logMsg("Lookup java:comp/env/service/servletendpointcontext");
    javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/servletendpointcontext");
    TestUtil.logMsg("Obtained service");
    port = (Hello) svc.getPort(Hello.class);
    TestUtil.logMsg("Obtained port");
    TestUtil.logMsg("Cast stub to base Stub class ...");
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
   * @class.setup_props: webServerHost; webServerPort; platform.mode; user;
   * password; authuser; authpassword; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    // Initialize QName's used in the test
    SERVICE_QNAME = new QName(NAMESPACEURI, "HelloService");
    PORT_QNAME = new QName(NAMESPACEURI, "HelloPort");

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
      username = p.getProperty(UserNameProp);
      password = p.getProperty(PasswordProp);
      unauthUsername = p.getProperty(unauthUserNameProp);
      unauthPassword = p.getProperty(unauthPasswordProp);
      TestUtil.logMsg("Username=" + username + ", Password=" + password);
      TestUtil.logMsg("unauthUsername=" + unauthUsername + ", unauthPassword="
          + unauthPassword);
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getStubJaxrpc();
        TestUtil.logMsg("Setting target endpoint to " + url + " ...");
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        stub._setProperty(Constants.STUB_USERNAME_PROPERTY, username);
        stub._setProperty(Constants.STUB_PASSWORD_PROPERTY, password);
      } else {
        getStub();
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
   * @testName: getHttpSessionTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:244; WS4EE:SPEC:150; WS4EE:SPEC:63;
   *
   * @test_Strategy: Deploy a service endpoint class and check for access to
   * HttpSession information via the method
   * ServletEndpointContext.getHttpSession().
   *
   * Description JAX-RPC runtime system is required to provide the appropriate
   * session, message context, servlet context and user principal information
   * per method invocation on the endpoint class.
   */
  public void getHttpSessionTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("getHttpSessionTest: test access to HttpSession information");
      TestUtil.logMsg(
          "Invoking RPC method getHttpSessionTest() and " + "expect true ...");
      boolean yes = port.getHttpSessionTest();
      if (!yes) {
        TestUtil.logErr("HttpSession information");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getHttpSessionTest failed", e);
    }

    if (!pass)
      throw new Fault("getHttpSessionTest failed");
  }

  /*
   * @testName: getMessageContextTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:242; WS4EE:SPEC:150; WS4EE:SPEC:63;
   *
   * @test_Strategy: Deploy a service endpoint class and check for access to
   * MessageContext information via the method
   * ServletEndpointContext.getMessageContext().
   *
   * Description JAX-RPC runtime system is required to provide the appropriate
   * session, message context, servlet context and user principal information
   * per method invocation on the endpoint class.
   */
  public void getMessageContextTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getMessageContextTest: test access to MessageContext information");
      TestUtil.logMsg("Invoking RPC method getMessageContextTest() and "
          + "expect true ...");
      boolean yes = port.getMessageContextTest();
      if (!yes) {
        TestUtil.logErr("MessageContext information");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getMessageContextTest failed", e);
    }

    if (!pass)
      throw new Fault("getMessageContextTest failed");
  }

  /*
   * @testName: getServletContextTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:245; WS4EE:SPEC:150; WS4EE:SPEC:63;
   *
   * @test_Strategy: Deploy a service endpoint class and check for access to
   * ServletContext information via the method
   * ServletEndpointContext.getServletContext().
   *
   * Description JAX-RPC runtime system is required to provide the appropriate
   * session, message context, servlet context and user principal information
   * per method invocation on the endpoint class.
   */
  public void getServletContextTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getServletContextTest: test access to ServletContext information");
      TestUtil.logMsg("Invoking RPC method getServletContextTest() and "
          + "expect true ...");
      boolean yes = port.getServletContextTest();
      if (!yes) {
        TestUtil.logErr("ServletContext information");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getServletContextTest failed", e);
    }

    if (!pass)
      throw new Fault("getServletContextTest failed");
  }

  /*
   * @testName: getUserPrincipalTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:243; WS4EE:SPEC:150; WS4EE:SPEC:63;
   *
   * @test_Strategy: Deploy a service endpoint class and check for access to
   * UserPrincipal information via the method
   * ServletEndpointContext.getUserPrincipal().
   *
   * Description JAX-RPC runtime system is required to provide the appropriate
   * session, message context, servlet context and user principal information
   * per method invocation on the endpoint class.
   */
  public void getUserPrincipalTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getUserPrincipalTest: test access to UserPrincipal information");
      TestUtil.logMsg("Invoking RPC method getUserPrincipalTest() and "
          + "expect true ...");
      boolean yes = port.getUserPrincipalTest();
      if (!yes) {
        TestUtil.logErr("UserPrincipal information");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getUserPrincipalTest failed", e);
    }

    if (!pass)
      throw new Fault("getUserPrincipalTest failed");
  }

  /*
   * @testName: isUserInRoleTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:247; WS4EE:SPEC:150; WS4EE:SPEC:63;
   *
   * @test_Strategy: Deploy a service endpoint class and check for access to
   * isUserInRole information via the method
   * ServletEndpointContext.isUserInRole().
   *
   * Description JAX-RPC runtime system is required to provide the appropriate
   * session, message context, servlet context and user principal information
   * per method invocation on the endpoint class.
   */
  public void isUserInRoleTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("isUserInRoleTest: test access to isUserInRole information");
      TestUtil.logMsg("Invoking RPC method isUserInRoleTest() and "
          + "expect true for Adminstrator role ...");
      boolean yes = port.isUserInRoleTest("Administrator");
      if (yes)
        TestUtil.logMsg("Administrator role - correct");
      else {
        TestUtil.logErr("Not Administrator role - incorrect");
        pass = false;
      }
      TestUtil.logMsg("Invoking RPC method isUserInRoleTest() and "
          + "expect false for Manager role ...");
      yes = port.isUserInRoleTest("Manager");
      if (!yes) {
        TestUtil.logMsg("Not Manager role - correct");
      } else {
        TestUtil.logErr("Manager role - incorrect");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("isUserInRoleTest failed", e);
    }

    if (!pass)
      throw new Fault("isUserInRoleTest failed");
  }
}
