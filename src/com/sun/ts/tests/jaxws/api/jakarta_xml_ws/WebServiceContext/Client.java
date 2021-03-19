/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.WebServiceContext;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.handler.*;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.hellosecureclient.*;

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

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.WebServiceContext.";

  // service and port information
  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "hellosecureservice.endpoint.1";

  private static final String WSDLLOC_URL = "hellosecureservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  Hello port = null;

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.hellosecureclient.HelloService.class;

  static HelloService service = null;

  private void getPorts() throws Exception {
    TestUtil.logMsg("Get port  = " + PORT_NAME);
    port = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port=" + port);
  }

  private void getPortsStandalone() throws Exception {
    getPorts();
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    port = (Hello) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        HelloService.class, PORT_QNAME, Hello.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortsJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    getPorts();
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode; user;
   * password; authuser; authpassword;
   */

  public void setup(String[] args, Properties p) throws Fault {
    boolean pass = true;
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
        TestUtil.logMsg("Create Service object");
        getTestURLs();
        service = (HelloService) JAXWS_Util.getService(wsdlurl, SERVICE_QNAME,
            SERVICE_CLASS);
        getPortsStandalone();
        JAXWS_Util.setUserNameAndPassword(port, username, password);
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HelloService) getSharedObject();
        getPortsJavaEE();
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
   * @testName: getMessageContextTest
   *
   * @assertion_ids: JAXWS:JAVADOC:69; WS4EE:SPEC:5004;
   *
   * @test_Strategy: Call WebServiceContext.getMessageContext() api.
   *
   * Description
   */
  public void getMessageContextTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getMessageContextTest: test access to MessageContext information");
      pass = port.getMessageContextTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getMessageContextTest failed", e);
    }

    if (!pass)
      throw new Fault("getMessageContextTest failed");
  }

  /*
   * @testName: getEndpointReferenceTest
   *
   * @assertion_ids: JAXWS:JAVADOC:158; JAXWS:SPEC:5028;
   *
   * @test_Strategy: Call
   * getEndpointReference(org.w3c.dom.Element...referenceParameters) and ensure
   * that EndpointReference was able to be retrieved.
   *
   * Description
   */
  public void getEndpointReferenceTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getEndpointReferenceTest: test access to EndpointReference information");
      pass = port.getEndpointReferenceTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getEndpointReferenceTest failed", e);
    }

    if (!pass) {
      throw new Fault("getEndpointReferenceTest failed");
    }
  }

  /*
   * @testName: getEndpointReference2Test
   *
   * @assertion_ids: JAXWS:JAVADOC:159; JAXWS:SPEC:5028;
   *
   * @test_Strategy: Call getEndpointReference(java.lang.Class class,
   * org.w3c.dom.Element...referenceParameters) and ensure that
   * EndpointReference was able to be retrieved.
   *
   * Description
   */
  public void getEndpointReference2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getEndpointReference2Test: test access to MessageContext information");
      pass = port.getEndpointReference2Test();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getEndpointReference2Test failed", e);
    }

    if (!pass)
      throw new Fault("getEndpointReference2Test failed");
  }

  /*
   * @testName: getUserPrincipalTest
   *
   * @assertion_ids: JAXWS:JAVADOC:70; WS4EE:SPEC:5004;
   *
   * @test_Strategy: Call WebServiceContext.getUserPrincipal() api.
   *
   * Description
   */
  public void getUserPrincipalTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getUserPrincipalTest: test access to UserPrincipal information");
      pass = port.getUserPrincipalTest();
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
   * @assertion_ids: JAXWS:JAVADOC:71; WS4EE:SPEC:5004;
   *
   * @test_Strategy: Call WebServiceContext.getisUserInRole() api.
   *
   * Description
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
