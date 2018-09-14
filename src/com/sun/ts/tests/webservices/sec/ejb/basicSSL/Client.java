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

package com.sun.ts.tests.webservices.sec.ejb.basicSSL;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.rmi.Remote;
import javax.xml.rpc.Stub;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends ServiceEETest {

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String PROTOCOLSECURE = "https";

  private String PROTOCOL = "http";

  private String urlString = null;

  private int portnumSecure = 1044;

  private int portnum = 8000;

  private static final String ENDPOINTURL = "HelloBasic/ejb";

  private static final String ENDPOINTURLUNPROTECTED = "HelloUnprotected/ejb";

  Service unprotectedService;

  HelloUnprotected unprotectedPort;

  Service basicNoIdService;

  HelloBasic basicNoIdPort;

  Service basicAuthorizedIdService;

  HelloBasic basicAuthorizedIdPort;

  Service basicUnauthorizedIdService;

  HelloBasic basicUnauthorizedIdPort;

  Service basicInvalidIdService;

  HelloBasic basicInvalidIdPort;

  InitialContext ctx;

  String vehicle = null;

  private void getStub() throws Exception {
    Stub stub;

    ctx = new InitialContext();
    TestUtil.logMsg("JNDI lookup for Unprotected Service");
    unprotectedService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secEjb/unprotected");
    TestUtil.logMsg("Get port from unprotected Service");
    unprotectedPort = (HelloUnprotected) unprotectedService
        .getPort(HelloUnprotected.class);
    TestUtil.logMsg("unprotected port obtained");
    stub = (javax.xml.rpc.Stub) unprotectedPort;
    urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum,
        ENDPOINTURLUNPROTECTED);
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicNoIdService");
    basicNoIdService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secEjb/basicNoId");
    TestUtil.logMsg("Get basicNoIdPort");
    basicNoIdPort = (HelloBasic) basicNoIdService.getPort(HelloBasic.class);
    TestUtil.logMsg("basicNoIdPort obtained");
    TestUtil.logMsg("JNDI lookup for basicNoIdService");
    stub = (javax.xml.rpc.Stub) basicNoIdPort;
    urlString = ctsurl.getURLString(PROTOCOLSECURE, hostname, portnumSecure,
        ENDPOINTURL);
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicAuthorizedIdService");
    basicAuthorizedIdService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secEjb/basicAuthorizedId");
    TestUtil.logMsg("Get basicAuthorizedIdPort");
    basicAuthorizedIdPort = (HelloBasic) basicAuthorizedIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicAuthorizedIdPort obtained");
    stub = (javax.xml.rpc.Stub) basicAuthorizedIdPort;
    urlString = ctsurl.getURLString(PROTOCOLSECURE, hostname, portnumSecure,
        ENDPOINTURL);
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicUnauthorizedIdService");
    basicUnauthorizedIdService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secEjb/basicUnauthorizedId");
    TestUtil.logMsg("Get basicUnauthorizedIdPort");
    basicUnauthorizedIdPort = (HelloBasic) basicUnauthorizedIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicUnauthorizedIdPort obtained");
    stub = (javax.xml.rpc.Stub) basicUnauthorizedIdPort;
    urlString = ctsurl.getURLString(PROTOCOLSECURE, hostname, portnumSecure,
        ENDPOINTURL);
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicInvalidIdService");
    basicInvalidIdService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secEjb/basicInvalidId");
    TestUtil.logMsg("Get basicInvalidIdPort");
    basicInvalidIdPort = (HelloBasic) basicInvalidIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicInvalidIdPort obtained");
    stub = (javax.xml.rpc.Stub) basicInvalidIdPort;
    urlString = ctsurl.getURLString(PROTOCOLSECURE, hostname, portnumSecure,
        ENDPOINTURL);
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);

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
   * @class.setup_props: webServerHost; webServerPort; securedWebServicePort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      hostname = p.getProperty("webServerHost");
      portnumSecure = Integer.parseInt(p.getProperty("securedWebServicePort"));
      portnum = Integer.parseInt(p.getProperty("webServerPort"));
      getStub();
      vehicle = p.getProperty("vehicle");
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: secEjbUnprotected
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call unprotected EJB, should succeed
   */
  public void secEjbUnprotected() throws Fault {
    TestUtil.logMsg("SecEjbUnprotected");
    try {
      String ret1 = unprotectedPort.sayHelloUnprotected("secEjbUnprotected");
      if (!ret1.equals("'secEjbUnprotected' from HelloUnprotected!")) {
        TestUtil.logMsg(
            "test SecEjbUnprotected failed: return value from first implementationis: "
                + ret1);
        throw new Fault("SecEjbUnprotected failed");
      }
      TestUtil.logMsg("SecEjbUnprotected passed");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test SecEjbUnprotected failed: got exception " + t.toString());
      throw new Fault("SecEjbUnprotected failed");
    }
    return;
  }

  /*
   * @testName: secEjbBasicNoId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB without any user id/password. Expect an
   * exception
   */
  public void secEjbBasicNoId() throws Fault {
    TestUtil.logMsg("SecEjbBasicNoId");
    try {
      if (vehicle.equals("appclient")) {
        TestUtil.logMsg("Skipping secEjbBasicNoId test for appclient vehicle");
        return;
      }
      String ret1 = basicNoIdPort.sayHelloBasic("secEjbBasicNoId");
      TestUtil
          .logMsg("SecEjbBasicNoId failed: unexpected return value " + ret1);
      throw new Fault("SecEjbBasicNoId failed");
    } catch (RemoteException ex) {
      TestUtil.logMsg("SecEjbBasicNoId success: got RemoteException");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test SecEjbBasicNoId failed: got exception " + t.toString());
      throw new Fault("SecEjbBasicNoId failed");
    }
    return;
  }

  /*
   * @testName: secEjbBasicUnauthorizedId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB with a user id/password that's not
   * authorized for the port
   */
  public void secEjbBasicUnauthorizedId() throws Fault {
    TestUtil.logMsg("SecEjbBasicUnauthorizedId");
    try {
      String ret1 = basicUnauthorizedIdPort
          .sayHelloBasic("secEjbBasicUnauthorizedId");
      TestUtil.logMsg(
          "SecEjbBasicUnauthorizedId failed: unexpected return value " + ret1);
      throw new Fault("SecEjbBasicUnauthorizedId failed");
    } catch (RemoteException ex) {
      TestUtil.logMsg("SecEjbBasicUnauthorizedId success: got RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg("test SecEjbBasicUnauthorizedId failed: got exception "
          + t.toString());
      throw new Fault("SecEjbBasicUnauthorizedId failed");
    }
    return;
  }

  /*
   * @testName: secEjbBasicInvalidId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB with an invalid id/password
   */
  public void secEjbBasicInvalidId() throws Fault {
    TestUtil.logMsg("SecEjbBasicInvalidId");
    try {
      if (vehicle.equals("appclient")) {
        TestUtil
            .logMsg("Skipping secEjbBasicInvalidId test for appclient vehicle");
        return;
      }
      String ret1 = basicInvalidIdPort.sayHelloBasic("secEjbBasicInvalidId");
      TestUtil.logMsg(
          "SecEjbBasicInvalidId failed: unexpected return value " + ret1);
      throw new Fault("SecEjbBasicInvalidId failed");
    } catch (RemoteException ex) {
      TestUtil.logMsg("SecEjbBasicInvalidId success: got RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test SecEjbBasicInvalidId failed: got exception " + t.toString());
      throw new Fault("SecEjbBasicInvalidId failed");
    }
    return;
  }

  /*
   * #testName: secEjbBasicAuthorizedId
   *
   * #assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * 
   * #test_Strategy: Call protected EJB with valid id/password.
   */
  public void secEjbBasicAuthorizedId() throws Fault {
    TestUtil.logMsg("SecEjbBasicAuthorizedId");
    try {
      String ret1 = basicAuthorizedIdPort
          .sayHelloBasic("secEjbBasicAuthorizedId");
      if (!ret1.equals("'secEjbBasicAuthorizedId' from HelloBasic!")) {
        TestUtil
            .logMsg("test secEjbBasicAuthorized failed: return value: " + ret1);
        throw new Fault("SecEjbBasicAuthorized failed");
      }
      TestUtil.logMsg("SecEjbBasicAuthorizedId passed");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test SecEjbBasicAuthorizedId failed: got exception " + t.toString());
      throw new Fault("SecEjbBasicAuthorizedId failed");
    }
    return;
  }

}
