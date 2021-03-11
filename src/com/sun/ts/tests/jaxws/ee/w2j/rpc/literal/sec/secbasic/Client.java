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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.sec.secbasic;

import com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.sec.secbasic.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import jakarta.xml.ws.*;
import javax.xml.namespace.QName;
import com.sun.javatest.Status;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  private static final long serialVersionUID = 1L;

  private String hostname = null;

  private int portnum = 0;

  private String username = "";

  private String password = "";

  private String unauthUsername = "";

  private String unauthPassword = "";

  String vehicle = null;

  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private static final String WebHostProp = "webServerHost";

  private static final String WebPortProp = "webServerPort";

  private static final String UserNameProp = "user";

  private static final String PasswordProp = "password";

  private static final String unauthUserNameProp = "authuser";

  private static final String unauthPasswordProp = "authpassword";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.sec.secbasic.";

  // service and port information
  private static final String NAMESPACEURI = "http://BasicAuthServiceTestService.org/wsdl";

  private static final String SERVICE_NAME = "BasicAuthServiceTestService";

  private static final String PORT_NAME1 = "HelloUnprotectedPort";

  private static final String PORT_NAME2 = "HelloProtectedPort";

  private static final String PORT_NAME3 = "HelloGuestPort";

  private static final String PORT_NAME4 = "HelloProtectedPort1";

  private static final String PORT_NAME5 = "HelloProtectedPort2";

  private static final String PORT_NAME6 = "HelloProtectedPort3";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private QName PORT_QNAME2 = new QName(NAMESPACEURI, PORT_NAME2);

  private QName PORT_QNAME3 = new QName(NAMESPACEURI, PORT_NAME3);

  private QName PORT_QNAME4 = new QName(NAMESPACEURI, PORT_NAME4);

  private QName PORT_QNAME5 = new QName(NAMESPACEURI, PORT_NAME5);

  private QName PORT_QNAME6 = new QName(NAMESPACEURI, PORT_NAME6);

  // URL properties used by the test
  private static final String ENDPOINT1_URL = "secbasic.endpoint.1";

  private static final String ENDPOINT2_URL = "secbasic.endpoint.2";

  private static final String ENDPOINT3_URL = "secbasic.endpoint.3";

  private static final String ENDPOINT4_URL = "secbasic.endpoint.4";

  private static final String ENDPOINT5_URL = "secbasic.endpoint.5";

  private static final String ENDPOINT6_URL = "secbasic.endpoint.6";

  private static final String WSDLLOC1_URL = "secbasic.wsdlloc.1";

  private static final String WSDLLOC2_URL = "secbasic.wsdlloc.2";

  private static final String WSDLLOC3_URL = "secbasic.wsdlloc.3";

  private static final String WSDLLOC4_URL = "secbasic.wsdlloc.4";

  private static final String WSDLLOC5_URL = "secbasic.wsdlloc.5";

  private static final String WSDLLOC6_URL = "secbasic.wsdlloc.6";

  private Properties props = null;

  private String request = null;

  private String url1 = null;

  private String url2 = null;

  private String url3 = null;

  private String url4 = null;

  private String url5 = null;

  private String url6 = null;

  private TSURL ctsurl = new TSURL();

  private URL wsdlurl1 = null;

  private URL wsdlurl2 = null;

  private URL wsdlurl3 = null;

  private URL wsdlurl4 = null;

  private URL wsdlurl5 = null;

  private URL wsdlurl6 = null;

  transient HelloUnprotected port1 = null;

  transient HelloProtected port2noid = null;

  transient HelloProtected1 port2validid = null;

  transient HelloProtected2 port2invalidid = null;

  transient HelloProtected3 port2unauthid = null;

  transient HelloGuest port3 = null;

  transient javax.naming.InitialContext ic = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT1_URL);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT2_URL);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT3_URL);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT4_URL);
    url4 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT5_URL);
    url5 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT6_URL);
    url6 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC1_URL);
    wsdlurl1 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC2_URL);
    wsdlurl2 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC3_URL);
    wsdlurl3 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC4_URL);
    wsdlurl4 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC5_URL);
    wsdlurl5 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC6_URL);
    wsdlurl6 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint1 URL: " + url1);
    TestUtil.logMsg("Service Endpoint2 URL: " + url2);
    TestUtil.logMsg("Service Endpoint3 URL: " + url3);
    TestUtil.logMsg("Service Endpoint4 URL: " + url4);
    TestUtil.logMsg("Service Endpoint5 URL: " + url5);
    TestUtil.logMsg("Service Endpoint6 URL: " + url6);
    TestUtil.logMsg("WSDL Location URL1: " + wsdlurl1);
    TestUtil.logMsg("WSDL Location URL2: " + wsdlurl2);
    TestUtil.logMsg("WSDL Location URL3: " + wsdlurl3);
    TestUtil.logMsg("WSDL Location URL4: " + wsdlurl4);
    TestUtil.logMsg("WSDL Location URL5: " + wsdlurl5);
    TestUtil.logMsg("WSDL Location URL6: " + wsdlurl6);
  }

  private void getUnprotectedServiceStubStandalone() throws Exception {
    port1 = (HelloUnprotected) JAXWS_Util.getPort(wsdlurl1, SERVICE_QNAME,
        BasicAuthServiceTestService.class, PORT_QNAME1, HelloUnprotected.class);
    JAXWS_Util.setTargetEndpointAddress(port1, url1);
  }

  private void getProtectedNoIdServiceStubStandalone() throws Exception {
    port2noid = (HelloProtected) JAXWS_Util.getPort(wsdlurl2, SERVICE_QNAME,
        BasicAuthServiceTestService.class, PORT_QNAME2, HelloProtected.class);
    JAXWS_Util.setTargetEndpointAddress(port2noid, url2);
  }

  private void getProtectedValidIdServiceStubStandalone() throws Exception {
    port2validid = (HelloProtected1) JAXWS_Util.getPort(wsdlurl4, SERVICE_QNAME,
        BasicAuthServiceTestService.class, PORT_QNAME4, HelloProtected1.class);
    JAXWS_Util.setTargetEndpointAddress(port2validid, url4);
  }

  private void getProtectedInvalidIdServiceStubStandalone() throws Exception {
    port2invalidid = (HelloProtected2) JAXWS_Util.getPort(wsdlurl5,
        SERVICE_QNAME, BasicAuthServiceTestService.class, PORT_QNAME5,
        HelloProtected2.class);
    JAXWS_Util.setTargetEndpointAddress(port2invalidid, url5);
  }

  private void getProtectedUnauthIdServiceStubStandalone() throws Exception {
    port2unauthid = (HelloProtected3) JAXWS_Util.getPort(wsdlurl6,
        SERVICE_QNAME, BasicAuthServiceTestService.class, PORT_QNAME6,
        HelloProtected3.class);
    JAXWS_Util.setTargetEndpointAddress(port2unauthid, url6);
  }

  private void getGuestServiceStubStandalone() throws Exception {
    port3 = (HelloGuest) JAXWS_Util.getPort(wsdlurl3, SERVICE_QNAME,
        BasicAuthServiceTestService.class, PORT_QNAME3, HelloGuest.class);
    JAXWS_Util.setTargetEndpointAddress(port3, url3);
  }

  private void getUnprotectedServiceStub() throws Exception {
    try {
      BasicAuthServiceTestService service = (BasicAuthServiceTestService) ic
          .lookup("java:comp/env/service/unprotected");
      port1 = (HelloUnprotected) service.getPort(HelloUnprotected.class);
      JAXWS_Util.dumpTargetEndpointAddress(port1);
      JAXWS_Util.setSOAPLogging(port1);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  private void getProtectedNoIdServiceStub() throws Exception {
    try {
      BasicAuthServiceTestService service = (BasicAuthServiceTestService) ic
          .lookup("java:comp/env/service/protectednoid");
      port2noid = (HelloProtected) service.getPort(HelloProtected.class);
      JAXWS_Util.dumpTargetEndpointAddress(port2noid);
      JAXWS_Util.setSOAPLogging(port2noid);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  private void getProtectedValidIdServiceStub() throws Exception {
    try {
      BasicAuthServiceTestService service = (BasicAuthServiceTestService) ic
          .lookup("java:comp/env/service/protectedvalidid");
      port2validid = (HelloProtected1) service.getPort(HelloProtected1.class);
      JAXWS_Util.dumpTargetEndpointAddress(port2validid);
      JAXWS_Util.setSOAPLogging(port2validid);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  private void getProtectedInvalidIdServiceStub() throws Exception {
    try {
      BasicAuthServiceTestService service = (BasicAuthServiceTestService) ic
          .lookup("java:comp/env/service/protectedinvalidid");
      port2invalidid = (HelloProtected2) service.getPort(HelloProtected2.class);
      JAXWS_Util.dumpTargetEndpointAddress(port2invalidid);
      JAXWS_Util.setSOAPLogging(port2invalidid);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  private void getProtectedUnauthIdServiceStub() throws Exception {
    try {
      BasicAuthServiceTestService service = (BasicAuthServiceTestService) ic
          .lookup("java:comp/env/service/protectedunauthid");
      port2unauthid = (HelloProtected3) service.getPort(HelloProtected3.class);
      JAXWS_Util.dumpTargetEndpointAddress(port2unauthid);
      JAXWS_Util.setSOAPLogging(port2unauthid);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  private void getGuestServiceStub() throws Exception {
    try {
      BasicAuthServiceTestService service = (BasicAuthServiceTestService) ic
          .lookup("java:comp/env/service/guest");
      port3 = (HelloGuest) service.getPort(HelloGuest.class);
      JAXWS_Util.dumpTargetEndpointAddress(port3);
      JAXWS_Util.setSOAPLogging(port3);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; user; password; authuser;
   * authpassword; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      hostname = p.getProperty(WebHostProp);
      portnum = Integer.parseInt(p.getProperty(WebPortProp));
      username = p.getProperty(UserNameProp);
      password = p.getProperty(PasswordProp);
      unauthUsername = p.getProperty(unauthUserNameProp);
      unauthPassword = p.getProperty(unauthPasswordProp);
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
      } else {
        ic = new javax.naming.InitialContext();
        getTestURLs();
      }
      vehicle = p.getProperty("vehicle");
    } catch (Exception e) {
      TestUtil.logErr("setup failed:", e);
    }
  }

  /*
   * @testName: BasicAuthTest1
   *
   * @assertion_ids: JAXWS:SPEC:11006; JAXWS:SPEC:11007; JAXWS:SPEC:10017;
   * JAXWS:SPEC:10018; WS4EE:SPEC:113; WS4EE:SPEC:114; WS4EE:SPEC:115;
   * WS4EE:SPEC:117; WS4EE:SPEC:213; WS4EE:SPEC:219; WS4EE:SPEC:221;
   * WS4EE:SPEC:223; WS4EE:SPEC:224; WS4EE:SPEC:228; WS4EE:SPEC:248;
   * WS4EE:SPEC:249; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:5000; WS4EE:SPEC:5002; WS4EE:SPEC:32; WS4EE:SPEC:4011;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001; JAXWS:SPEC:129;
   *
   * @test_Strategy: 1. Invoke RPC on a protected JAXWS service definition
   * without authenticating. 2. The JAXWS runtime must deny access and throw a
   * WebServiceException (UnAuthorized).
   *
   * Description Test BASIC authentication as specified in the JAXWS
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a
   * protected JAXWS service definition, the JAXWS runtime must deny access and
   * throw a WebService- Exception (UnAuthorized).
   */

  public void BasicAuthTest1() throws Fault {
    TestUtil.logTrace("BasicAuthTest1");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      if (vehicle.equals("wsappclient")) {
        TestUtil.logMsg("Skipping BasicAuthTest1 test for appclient vehicle");
        return;
      }
      TestUtil.logMsg("Get stub for Protected Service Definition");
      if (modeProperty.equals("standalone"))
        getProtectedNoIdServiceStubStandalone();
      else
        getProtectedNoIdServiceStub();
      TestUtil.logMsg("Invoke RPC method without authenticating");
      TestUtil.logMsg("JAXWS runtime must throw a WebServiceException");
      try {
        String response = port2noid.helloProtected("foo");
        TestUtil.logErr("Authorization was allowed - failed");
        TestUtil.logErr("Did not get expected WebServiceException");
        pass = false;
      } catch (WebServiceException e) {
        TestUtil.logMsg("Got expected WebServiceException");
        TestUtil.logMsg("Detail exception message: " + e.getMessage());
        TestUtil.logMsg("Authorization was not allowed - passed");
        TestUtil.logMsg("RPC invocation was denied - passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BasicAuthTest1 failed: ", e);
    }
    if (!pass)
      throw new Fault("BasicAuthTest1 failed");
  }

  /*
   * @testName: BasicAuthTest2
   *
   * @assertion_ids: JAXWS:SPEC:11006; JAXWS:SPEC:11007; JAXWS:SPEC:10017;
   * JAXWS:SPEC:10018; WS4EE:SPEC:113; WS4EE:SPEC:114; WS4EE:SPEC:115;
   * WS4EE:SPEC:117; WS4EE:SPEC:213; WS4EE:SPEC:219; WS4EE:SPEC:221;
   * WS4EE:SPEC:223; WS4EE:SPEC:224; WS4EE:SPEC:228; WS4EE:SPEC:248;
   * WS4EE:SPEC:249; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:5000; WS4EE:SPEC:5002; WS4EE:SPEC:32; WS4EE:SPEC:4011;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001; JAXWS:SPEC:129;
   *
   * @test_Strategy: 1. Invoke RPC on a protected JAXWS service definition
   * authenticating with a valid username and password. 2. The JAXWS runtime
   * must allow access.
   *
   * Description Test BASIC authentication as specified in the JAXWS
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a
   * protected JAXWS service definition, and user enters a valid username and
   * password, then the JAXWS runtime must allow access.
   */

  public void BasicAuthTest2() throws Fault {
    TestUtil.logTrace("BasicAuthTest2");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      TestUtil.logMsg("Get stub for Protected Service Definition");
      if (modeProperty.equals("standalone")) {
        getProtectedValidIdServiceStubStandalone();
        JAXWS_Util.setUserNameAndPassword(port2validid, username, password);
      } else
        getProtectedValidIdServiceStub();
      TestUtil.logMsg("Invoke RPC method authenticating with a valid"
          + " username/password");
      TestUtil.logMsg(
          "User is in the required security role to access" + " the resource");
      TestUtil.logMsg("JAXWS runtime must allow access");
      TestUtil.logMsg("Username=" + username + ", Password=" + password);
      try {
        String response = port2validid.helloProtected1("foo");
        TestUtil.logMsg("Authorization was allowed - passed");
        TestUtil.logMsg("RPC invocation was allowed - passed");
        TestUtil.logMsg("Checking return response");
        if (!response.equals(expected)) {
          TestUtil.logErr("Received incorrect response - expected [" + expected
              + "], received: [" + response + "]");
          pass = false;
        } else {
          TestUtil.logMsg("Received expected response: [" + response + "]");
        }
      } catch (WebServiceException e) {
        TestUtil.logErr("Authorization was not allowed - failed", e);
        TestUtil.logErr("RPC invocation was denied - failed");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BasicAuthTest2 failed: ", e);
    }
    if (!pass)
      throw new Fault("BasicAuthTest2 failed");
  }

  /*
   * @testName: BasicAuthTest3
   *
   * @assertion_ids: JAXWS:SPEC:11006; JAXWS:SPEC:11007; JAXWS:SPEC:10017;
   * JAXWS:SPEC:10018; WS4EE:SPEC:113; WS4EE:SPEC:114; WS4EE:SPEC:115;
   * WS4EE:SPEC:117; WS4EE:SPEC:213; WS4EE:SPEC:219; WS4EE:SPEC:221;
   * WS4EE:SPEC:223; WS4EE:SPEC:224; WS4EE:SPEC:228; WS4EE:SPEC:248;
   * WS4EE:SPEC:249; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:5000; WS4EE:SPEC:5002; WS4EE:SPEC:32; WS4EE:SPEC:4011;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001; JAXWS:SPEC:129;
   *
   * @test_Strategy: 1. Invoke RPC on a protected JAXWS service definition
   * authenticating with invalid username and password. 2. The JAXWS runtime
   * must deny access and throw a WebServiceException (UnAuthorized).
   *
   * Description Test BASIC authentication as specified in the JAXWS
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a
   * protected JAXWS service definition, and user enters an invalid username and
   * password, then the JAXWS runtime must deny access and throw a WebService-
   * Exception (UnAuthorized).
   */

  public void BasicAuthTest3() throws Fault {
    TestUtil.logTrace("BasicAuthTest3");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      if (vehicle.equals("wsappclient")) {
        TestUtil.logMsg("Skipping BasicAuthTest3 test for appclient vehicle");
        return;
      }
      TestUtil.logMsg("Get stub for Protected Service Definition");
      if (modeProperty.equals("standalone")) {
        getProtectedInvalidIdServiceStubStandalone();
        JAXWS_Util.setUserNameAndPassword(port2invalidid, "invalid", "invalid");
      } else
        getProtectedInvalidIdServiceStub();
      TestUtil.logMsg("Invoke RPC method authenticating with an"
          + " invalid username/password");
      TestUtil.logMsg("Username=invalid, Password=invalid");
      TestUtil.logMsg("Username=invalid, Password=invalid");
      TestUtil.logMsg("JAXWS runtime must throw a WebServiceException");
      try {
        String response = port2invalidid.helloProtected2("foo");
        TestUtil.logErr("Did not get expected WebServiceException");
        TestUtil.logErr("Authorization was allowed - failed");
        TestUtil.logErr("RPC invocation was allowed - failed");
        pass = false;
      } catch (WebServiceException e) {
        TestUtil.logMsg("Got expected WebServiceException");
        TestUtil.logMsg("Detail exception message: " + e.getMessage());
        TestUtil.logMsg("Authorization was not allowed - passed");
        TestUtil.logMsg("RPC invocation was denied - passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BasicAuthTest3 failed: ", e);
    }
    if (!pass)
      throw new Fault("BasicAuthTest3 failed");
  }

  /*
   * @testName: BasicAuthTest4
   *
   * @assertion_ids: JAXWS:SPEC:11006; JAXWS:SPEC:11007; JAXWS:SPEC:10017;
   * JAXWS:SPEC:10018; WS4EE:SPEC:113; WS4EE:SPEC:114; WS4EE:SPEC:115;
   * WS4EE:SPEC:117; WS4EE:SPEC:213; WS4EE:SPEC:219; WS4EE:SPEC:221;
   * WS4EE:SPEC:223; WS4EE:SPEC:224; WS4EE:SPEC:228; WS4EE:SPEC:248;
   * WS4EE:SPEC:249; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:5000; WS4EE:SPEC:5002; WS4EE:SPEC:32; WS4EE:SPEC:4011;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001; JAXWS:SPEC:129;
   *
   * @test_Strategy: 1. Invoke RPC on a protected JAXWS service definition
   * authenticating with valid username and password but user is not in the
   * required secuirty role allowed by the JAXWS service definition. 2. The
   * JAXWS runtime must deny access and throw a WebServiceException
   * (UnAuthorized).
   *
   * Description Test BASIC authentication as specified in the JAXWS
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a
   * protected JAXWS service definition, and user enters a valid username and
   * password, but user is not in the required security role allowed by the
   * JAXWS service definition then the JAXWS runtime must deny access and throw
   * a WebServiceException (UnAuthorized).
   */

  public void BasicAuthTest4() throws Fault {
    TestUtil.logTrace("BasicAuthTest4");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      TestUtil.logMsg("Get stub for Protected Service Definition");
      if (modeProperty.equals("standalone")) {
        getProtectedUnauthIdServiceStubStandalone();
        JAXWS_Util.setUserNameAndPassword(port2unauthid, unauthUsername,
            unauthPassword);
      } else
        getProtectedUnauthIdServiceStub();
      TestUtil.logMsg("Invoke RPC method authenticating with a valid"
          + " username/password");
      TestUtil.logMsg("User is not in the required security role to"
          + " access the resource");
      TestUtil.logMsg(
          "Username=" + unauthUsername + ", Password=" + unauthPassword);
      TestUtil.logMsg("JAXWS runtime must throw a WebServiceException");
      try {
        String response = port2unauthid.helloProtected3("foo");
        TestUtil.logErr("Did not get expected WebServiceException");
        TestUtil.logErr("Authorization was allowed - failed");
        TestUtil.logErr("RPC invocation was allowed - failed");
        pass = false;
      } catch (WebServiceException e) {
        TestUtil.logMsg("Got expected WebServiceException");
        TestUtil.logMsg("Detail exception message: " + e.getMessage());
        TestUtil.logMsg("Authorization was not allowed - passed");
        TestUtil.logMsg("RPC invocation was denied - passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BasicAuthTest4 failed: ", e);
    }
    if (!pass)
      throw new Fault("BasicAuthTest4 failed");
  }

  /*
   * @testName: BasicAuthTest5
   *
   * @assertion_ids: JAXWS:SPEC:11006; JAXWS:SPEC:11007; JAXWS:SPEC:10017;
   * JAXWS:SPEC:10018; WS4EE:SPEC:113; WS4EE:SPEC:114; WS4EE:SPEC:115;
   * WS4EE:SPEC:117; WS4EE:SPEC:213; WS4EE:SPEC:219; WS4EE:SPEC:221;
   * WS4EE:SPEC:223; WS4EE:SPEC:224; WS4EE:SPEC:228; WS4EE:SPEC:248;
   * WS4EE:SPEC:249; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:5000; WS4EE:SPEC:5002; WS4EE:SPEC:32; WS4EE:SPEC:4011;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001; JAXWS:SPEC:129;
   *
   * @test_Strategy: 1. Invoke RPC on a unprotected JAXWS service definition. 2.
   * The JAXWS runtime must allow access without the need to authenticate.
   *
   * Description Test BASIC authentication as specified in the JAXWS
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access an
   * unprotected JAXWS service definition, then the JAXWS runtime must allow
   * access without the need to authenticate.
   */

  public void BasicAuthTest5() throws Fault {
    TestUtil.logTrace("BasicAuthTest5");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      TestUtil.logMsg("Get stub for Unprotected Service Definition");
      if (modeProperty.equals("standalone"))
        getUnprotectedServiceStubStandalone();
      else
        getUnprotectedServiceStub();
      TestUtil.logMsg("Invoke RPC method without authenticating");
      TestUtil.logMsg("JAXWS runtime must allow access without the"
          + " need to authenticate user");
      try {
        String response = port1.helloUnprotected("foo");
        TestUtil.logMsg("Authorization was allowed - passed");
        TestUtil.logMsg("RPC invocation was allowed - passed");
        TestUtil.logMsg("Checking return response");
        if (!response.equals(expected)) {
          TestUtil.logErr("Received incorrect response - expected [" + expected
              + "], received: [" + response + "]");
          pass = false;
        } else {
          TestUtil.logMsg("Received expected response: [" + response + "]");
        }
      } catch (WebServiceException e) {
        TestUtil.logErr("Authorization was not allowed - failed", e);
        TestUtil.logErr("RPC invocation was denied - failed");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BasicAuthTest5 failed: ", e);
    }
    if (!pass)
      throw new Fault("BasicAuthTest5 failed");
  }

  /*
   * @testName: BasicAuthTest6
   *
   * @assertion_ids: JAXWS:SPEC:11006; JAXWS:SPEC:11007; JAXWS:SPEC:10017;
   * JAXWS:SPEC:10018; WS4EE:SPEC:113; WS4EE:SPEC:114; WS4EE:SPEC:115;
   * WS4EE:SPEC:117; WS4EE:SPEC:213; WS4EE:SPEC:219; WS4EE:SPEC:221;
   * WS4EE:SPEC:223; WS4EE:SPEC:224; WS4EE:SPEC:228; WS4EE:SPEC:248;
   * WS4EE:SPEC:249; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:5000; WS4EE:SPEC:5002; WS4EE:SPEC:32; WS4EE:SPEC:4011;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001; JAXWS:SPEC:129;
   *
   * @test_Strategy: 1. Invoke RPC on a guest JAXWS service definition. 2. The
   * JAXWS runtime must allow access since all users have access to the guest
   * JAXWS service definition.
   *
   * Description Test BASIC authentication as specified in the JAXWS
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a guest
   * JAXWS service definition, and a user enters a valid username and password,
   * then the JAXWS runtime must allow access since all users have access to the
   * guest JAXWS service definition.
   */

  public void BasicAuthTest6() throws Fault {
    TestUtil.logTrace("BasicAuthTest6");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      TestUtil.logMsg("Get stub for Guest Service Definition");
      if (modeProperty.equals("standalone")) {
        getGuestServiceStubStandalone();
        JAXWS_Util.setUserNameAndPassword(port3, unauthUsername,
            unauthPassword);
      } else
        getGuestServiceStub();
      TestUtil.logMsg("Invoke RPC method authenticating with a"
          + " valid username/password");
      TestUtil.logMsg(
          "Username=" + unauthUsername + ", Password=" + unauthPassword);
      TestUtil.logMsg("JAXWS runtime must allow access since all"
          + " users have guest access");
      try {
        String response = port3.helloGuest("foo");
        TestUtil.logMsg("Authorization was allowed - passed");
        TestUtil.logMsg("RPC invocation was allowed - passed");
      } catch (WebServiceException e) {
        TestUtil.logErr("Authorization was not allowed - failed", e);
        TestUtil.logErr("RPC invocation was denied - failed");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BasicAuthTest6 failed: ", e);
    }
    if (!pass)
      throw new Fault("BasicAuthTest6 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup");
  }
}
