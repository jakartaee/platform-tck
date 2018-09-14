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

package com.sun.ts.tests.jaxrpc.ee.sec.secbasic;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.*;
import com.sun.javatest.Status;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
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

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.sec.secbasic.";

  // URL properties used by the test
  private static final String ENDPOINT1_URL = "secbasic.endpoint.1";

  private static final String ENDPOINT2_URL = "secbasic.endpoint.2";

  private static final String ENDPOINT3_URL = "secbasic.endpoint.3";

  private static final String WSDLLOC1_URL = "secbasic.wsdlloc.1";

  private static final String WSDLLOC2_URL = "secbasic.wsdlloc.2";

  private static final String WSDLLOC3_URL = "secbasic.wsdlloc.3";

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME1 = "BasicAuthServiceTestService1";

  private static final String PORT_NAME1 = "HelloUnprotectedPort";

  private static final String SERVICE_NAME2 = "BasicAuthServiceTestService2";

  private static final String PORT_NAME2 = "HelloProtectedPort";

  private static final String SERVICE_NAME3 = "BasicAuthServiceTestService3";

  private static final String PORT_NAME3 = "HelloGuestPort";

  private Properties props = null;

  private String request = null;

  private String url1 = null;

  private String url2 = null;

  private String url3 = null;

  private TSURL ctsurl = new TSURL();

  private URL wsdlurl1 = null;

  private URL wsdlurl2 = null;

  private URL wsdlurl3 = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT1_URL);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(ENDPOINT2_URL);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(ENDPOINT3_URL);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC1_URL);
    wsdlurl1 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC2_URL);
    wsdlurl2 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC3_URL);
    wsdlurl3 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint1 URL: " + url1);
    TestUtil.logMsg("Service Endpoint2 URL: " + url2);
    TestUtil.logMsg("Service Endpoint3 URL: " + url3);
    TestUtil.logMsg("WSDL Location URL1: " + wsdlurl1);
    TestUtil.logMsg("WSDL Location URL2: " + wsdlurl2);
    TestUtil.logMsg("WSDL Location URL3: " + wsdlurl3);
  }

  // Get Port and Stub access via porting layer interface
  HelloUnprotected port1 = null;

  HelloProtected port2noid = null;

  HelloProtected port2validid = null;

  HelloProtected port2invalidid = null;

  HelloProtected port2unauthid = null;

  HelloGuest port3 = null;

  Stub stub1 = null;

  Stub stub2noid = null;

  Stub stub2validid = null;

  Stub stub2invalidid = null;

  Stub stub2unauthid = null;

  Stub stub3 = null;

  private void getUnprotectedServiceStubStandalone() throws Exception {
    port1 = (HelloUnprotected) JAXRPC_Util.getStub(PKG_NAME + SERVICE_NAME1,
        "get" + PORT_NAME1);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub1 = (javax.xml.rpc.Stub) port1;
    TestUtil.logMsg("Setting target endpoint to:\n" + url1 + " ...");
    stub1._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url1);
  }

  private void getProtectedNoIdServiceStubStandalone() throws Exception {
    port2noid = (HelloProtected) JAXRPC_Util.getStub(PKG_NAME + SERVICE_NAME2,
        "get" + PORT_NAME2);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub2noid = (javax.xml.rpc.Stub) port2noid;
    TestUtil.logMsg("Setting target endpoint to:\n" + url2 + " ...");
    stub2noid._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url2);
  }

  private void getProtectedValidIdServiceStubStandalone() throws Exception {
    port2validid = (HelloProtected) JAXRPC_Util
        .getStub(PKG_NAME + SERVICE_NAME2, "get" + PORT_NAME2);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub2validid = (javax.xml.rpc.Stub) port2validid;
    TestUtil.logMsg("Setting target endpoint to:\n" + url2 + " ...");
    stub2validid._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url2);
  }

  private void getProtectedInvalidIdServiceStubStandalone() throws Exception {
    port2invalidid = (HelloProtected) JAXRPC_Util
        .getStub(PKG_NAME + SERVICE_NAME2, "get" + PORT_NAME2);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub2invalidid = (javax.xml.rpc.Stub) port2invalidid;
    TestUtil.logMsg("Setting target endpoint to:\n" + url2 + " ...");
    stub2invalidid._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url2);
  }

  private void getProtectedUnauthIdServiceStubStandalone() throws Exception {
    port2unauthid = (HelloProtected) JAXRPC_Util
        .getStub(PKG_NAME + SERVICE_NAME2, "get" + PORT_NAME2);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub2unauthid = (javax.xml.rpc.Stub) port2unauthid;
    TestUtil.logMsg("Setting target endpoint to:\n" + url2 + " ...");
    stub2unauthid._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url2);
  }

  private void getGuestServiceStubStandalone() throws Exception {
    port3 = (HelloGuest) JAXRPC_Util.getStub(PKG_NAME + SERVICE_NAME3,
        "get" + PORT_NAME3);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub3 = (javax.xml.rpc.Stub) port3;
    TestUtil.logMsg("Setting target endpoint to:\n" + url3 + " ...");
    stub3._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url3);
  }

  private void getUnprotectedServiceStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      javax.xml.rpc.Service service1 = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/unprotected");

      port1 = (HelloUnprotected) service1.getPort(HelloUnprotected.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
    stub1 = (javax.xml.rpc.Stub) port1;
  }

  private void getProtectedNoIdServiceStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      javax.xml.rpc.Service service2noid = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/protectednoid");

      port2noid = (HelloProtected) service2noid.getPort(HelloProtected.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
    stub2noid = (javax.xml.rpc.Stub) port2noid;
  }

  private void getProtectedValidIdServiceStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      javax.xml.rpc.Service service2validid = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/protectedvalidid");

      port2validid = (HelloProtected) service2validid
          .getPort(HelloProtected.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
    stub2validid = (javax.xml.rpc.Stub) port2validid;
  }

  private void getProtectedInvalidIdServiceStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      javax.xml.rpc.Service service2invalidid = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/protectedinvalidid");

      port2invalidid = (HelloProtected) service2invalidid
          .getPort(HelloProtected.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
    stub2invalidid = (javax.xml.rpc.Stub) port2invalidid;
  }

  private void getProtectedUnauthIdServiceStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      javax.xml.rpc.Service service2unauthid = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/protectedunauthid");

      port2unauthid = (HelloProtected) service2unauthid
          .getPort(HelloProtected.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
    stub2unauthid = (javax.xml.rpc.Stub) port2unauthid;
  }

  private void getGuestServiceStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      javax.xml.rpc.Service service3 = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/guest");

      port3 = (HelloGuest) service3.getPort(HelloGuest.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
    stub3 = (javax.xml.rpc.Stub) port3;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
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
      vehicle = p.getProperty("vehicle");
      if (modeProperty.equals("standalone"))
        getTestURLs();
    } catch (Exception e) {
      TestUtil.logErr("setup failed:", e);
    }
  }

  /*
   * @testName: BasicAuthTest1
   *
   * @assertion_ids: JAXRPC:SPEC:427; JAXRPC:SPEC:428;
   *
   * @test_Strategy: 1. Invoke RPC on a protected JAXRPC service definition
   * without authenticating. 2. The JAXRPC runtime must deny access and throw a
   * RemoteException (UnAuthorized).
   *
   * Description Test BASIC authentication as specified in the JAXRPC
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a
   * protected JAXRPC service definition, the JAXRPC runtime must deny access
   * and throw a Remote- Exception (UnAuthorized).
   */

  public void BasicAuthTest1() throws Fault {
    TestUtil.logTrace("BasicAuthTest1");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      if (vehicle.equals("appclient")) {
        TestUtil
            .logMsg("Skipping BasicAuthTest1 NoId test for appclient vehicle");
        return;
      }
      TestUtil.logMsg("Get stub for Protected Service Definition");
      if (modeProperty.equals("standalone"))
        getProtectedNoIdServiceStubStandalone();
      else
        getProtectedNoIdServiceStub();
      TestUtil.logMsg("Invoke RPC method without authenticating");
      TestUtil.logMsg("JAXRPC runtime must throw a RemoteException");
      try {
        String response = port2noid.helloProtected("foo");
        TestUtil.logErr("Authorization was allowed - failed");
        TestUtil.logErr("Did not get expected RemoteException");
        pass = false;
      } catch (RemoteException e) {
        TestUtil.logMsg("Got expected RemoteException");
        if (e.detail != null)
          TestUtil.logMsg(e.detail.getMessage());
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
   * @assertion_ids: JAXRPC:SPEC:427; JAXRPC:SPEC:428;
   *
   * @test_Strategy: 1. Invoke RPC on a protected JAXRPC service definition
   * authenticating with a valid username and password. 2. The JAXRPC runtime
   * must allow access.
   *
   * Description Test BASIC authentication as specified in the JAXRPC
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a
   * protected JAXRPC service definition, and user enters a valid username and
   * password, then the JAXRPC runtime must allow access.
   */

  public void BasicAuthTest2() throws Fault {
    TestUtil.logTrace("BasicAuthTest2");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      TestUtil.logMsg("Get stub for Protected Service Definition");
      if (modeProperty.equals("standalone"))
        getProtectedValidIdServiceStubStandalone();
      else
        getProtectedValidIdServiceStub();
      TestUtil.logMsg("Invoke RPC method authenticating with a valid"
          + " username/password");
      TestUtil.logMsg(
          "User is in the required security role to access" + " the resource");
      TestUtil.logMsg("JAXRPC runtime must allow access");
      TestUtil.logMsg("Username=" + username + ", Password=" + password);
      if (modeProperty.equals("standalone")) {
        stub2validid._setProperty(Constants.STUB_USERNAME_PROPERTY, username);
        stub2validid._setProperty(Constants.STUB_PASSWORD_PROPERTY, password);
      }
      try {
        String response = port2validid.helloProtected("foo");
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
      } catch (RemoteException e) {
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
   * @assertion_ids: JAXRPC:SPEC:427; JAXRPC:SPEC:428;
   *
   * @test_Strategy: 1. Invoke RPC on a protected JAXRPC service definition
   * authenticating with invalid username and password. 2. The JAXRPC runtime
   * must deny access and throw a RemoteException (UnAuthorized).
   *
   * Description Test BASIC authentication as specified in the JAXRPC
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a
   * protected JAXRPC service definition, and user enters an invalid username
   * and password, then the JAXRPC runtime must deny access and throw a Remote-
   * Exception (UnAuthorized).
   */

  public void BasicAuthTest3() throws Fault {
    TestUtil.logTrace("BasicAuthTest3");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      if (vehicle.equals("appclient")) {
        TestUtil.logMsg(
            "Skipping BasicAuthTest3 InvalidId test for appclient vehicle");
        return;
      }
      TestUtil.logMsg("Get stub for Protected Service Definition");
      if (modeProperty.equals("standalone"))
        getProtectedInvalidIdServiceStubStandalone();
      else
        getProtectedInvalidIdServiceStub();
      TestUtil.logMsg("Invoke RPC method authenticating with an"
          + " invalid username/password");
      TestUtil.logMsg("Username=invalid, Password=invalid");
      TestUtil.logMsg("Username=invalid, Password=invalid");
      if (modeProperty.equals("standalone")) {
        stub2invalidid._setProperty(Constants.STUB_USERNAME_PROPERTY,
            "invalid");
        stub2invalidid._setProperty(Constants.STUB_PASSWORD_PROPERTY,
            "invalid");
      }
      TestUtil.logMsg("JAXRPC runtime must throw a RemoteException");
      try {
        String response = port2invalidid.helloProtected("foo");
        TestUtil.logErr("Did not get expected RemoteException");
        TestUtil.logErr("Authorization was allowed - failed");
        TestUtil.logErr("RPC invocation was allowed - failed");
        pass = false;
      } catch (RemoteException e) {
        TestUtil.logMsg("Got expected RemoteException");
        if (e.detail != null)
          TestUtil.logMsg(e.detail.getMessage());
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
   * @assertion_ids: JAXRPC:SPEC:427; JAXRPC:SPEC:428;
   *
   * @test_Strategy: 1. Invoke RPC on a protected JAXRPC service definition
   * authenticating with valid username and password but user is not in the
   * required secuirty role allowed by the JAXRPC service definition. 2. The
   * JAXRPC runtime must deny access and throw a RemoteException (UnAuthorized).
   *
   * Description Test BASIC authentication as specified in the JAXRPC
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a
   * protected JAXRPC service definition, and user enters a valid username and
   * password, but user is not in the required security role allowed by the
   * JAXRPC service definition then the JAXRPC runtime must deny access and
   * throw a RemoteException (UnAuthorized).
   */

  public void BasicAuthTest4() throws Fault {
    TestUtil.logTrace("BasicAuthTest4");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      TestUtil.logMsg("Get stub for Protected Service Definition");
      if (modeProperty.equals("standalone"))
        getProtectedUnauthIdServiceStubStandalone();
      else
        getProtectedUnauthIdServiceStub();
      TestUtil.logMsg("Invoke RPC method authenticating with a valid"
          + " username/password");
      TestUtil.logMsg("User is not in the required security role to"
          + " access the resource");
      TestUtil.logMsg(
          "Username=" + unauthUsername + ", Password=" + unauthPassword);
      if (modeProperty.equals("standalone")) {
        stub2unauthid._setProperty(Constants.STUB_USERNAME_PROPERTY,
            unauthUsername);
        stub2unauthid._setProperty(Constants.STUB_PASSWORD_PROPERTY,
            unauthPassword);
      }
      TestUtil.logMsg("JAXRPC runtime must throw a RemoteException");
      try {
        String response = port2unauthid.helloProtected("foo");
        TestUtil.logErr("Did not get expected RemoteException");
        TestUtil.logErr("Authorization was allowed - failed");
        TestUtil.logErr("RPC invocation was allowed - failed");
        pass = false;
      } catch (RemoteException e) {
        TestUtil.logMsg("Got expected RemoteException");
        if (e.detail != null)
          TestUtil.logMsg(e.detail.getMessage());
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
   * @assertion_ids: JAXRPC:SPEC:427; JAXRPC:SPEC:428;
   *
   * @test_Strategy: 1. Invoke RPC on a unprotected JAXRPC service definition.
   * 2. The JAXRPC runtime must allow access without the need to authenticate.
   *
   * Description Test BASIC authentication as specified in the JAXRPC
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access an
   * unprotected JAXRPC service definition, then the JAXRPC runtime must allow
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
      TestUtil.logMsg("JAXRPC runtime must allow access without the"
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
      } catch (RemoteException e) {
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
   * @assertion_ids: JAXRPC:SPEC:427; JAXRPC:SPEC:428;
   *
   * @test_Strategy: 1. Invoke RPC on a guest JAXRPC service definition. 2. The
   * JAXRPC runtime must allow access since all users have access to the guest
   * JAXRPC service definition.
   *
   * Description Test BASIC authentication as specified in the JAXRPC
   * Specification.
   *
   * 1. If user has not been authenticated and user attempts to access a guest
   * JAXRPC service definition, and a user enters a valid username and password,
   * then the JAXRPC runtime must allow access since all users have access to
   * the guest JAXRPC service definition.
   */

  public void BasicAuthTest6() throws Fault {
    TestUtil.logTrace("BasicAuthTest6");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      TestUtil.logMsg("Get stub for Guest Service Definition");
      if (modeProperty.equals("standalone"))
        getGuestServiceStubStandalone();
      else
        getGuestServiceStub();
      TestUtil.logMsg("Invoke RPC method authenticating with a"
          + " valid username/password");
      TestUtil.logMsg(
          "Username=" + unauthUsername + ", Password=" + unauthPassword);
      if (modeProperty.equals("standalone")) {
        stub3._setProperty(Constants.STUB_USERNAME_PROPERTY, unauthUsername);
        stub3._setProperty(Constants.STUB_PASSWORD_PROPERTY, unauthPassword);
      }
      TestUtil.logMsg("JAXRPC runtime must allow access since all"
          + " users have guest access");
      try {
        String response = port3.helloGuest("foo");
        TestUtil.logMsg("Authorization was allowed - passed");
        TestUtil.logMsg("RPC invocation was allowed - passed");
      } catch (RemoteException e) {
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
