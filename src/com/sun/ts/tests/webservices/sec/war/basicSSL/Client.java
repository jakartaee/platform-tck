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

package com.sun.ts.tests.webservices.sec.war.basicSSL;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.rmi.Remote;
import javax.xml.rpc.Service;
import javax.xml.rpc.Stub;
import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends ServiceEETest {

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String PROTOCOL = "https";

  private String urlString = null;

  private int portnum = 8000;

  private static final String ENDPOINTURL = "/SecWarBasicSSL_web/ws4ee/SecWarBasicSSL";

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

    TestUtil.logMsg("JNDI lookup for basicNoIdService");
    basicNoIdService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secWar/basicNoId");
    TestUtil.logMsg("Get basicNoIdPort");
    basicNoIdPort = (HelloBasic) basicNoIdService.getPort(HelloBasic.class);
    TestUtil.logMsg("basicNoIdPort obtained");
    TestUtil.logMsg("JNDI lookup for basicNoIdService");
    stub = (javax.xml.rpc.Stub) basicNoIdPort;
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicAuthorizedIdService");
    basicAuthorizedIdService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secWar/basicAuthorizedId");
    TestUtil.logMsg("Get basicAuthorizedIdPort");
    basicAuthorizedIdPort = (HelloBasic) basicAuthorizedIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicAuthorizedIdPort obtained");
    stub = (javax.xml.rpc.Stub) basicAuthorizedIdPort;
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicUnauthorizedIdService");
    basicUnauthorizedIdService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secWar/basicUnauthorizedId");
    TestUtil.logMsg("Get basicUnauthorizedIdPort");
    basicUnauthorizedIdPort = (HelloBasic) basicUnauthorizedIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicUnauthorizedIdPort obtained");
    stub = (javax.xml.rpc.Stub) basicUnauthorizedIdPort;
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicInvalidIdService");
    basicInvalidIdService = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/secWar/basicInvalidId");
    TestUtil.logMsg("Get basicInvalidIdPort");
    basicInvalidIdPort = (HelloBasic) basicInvalidIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicInvalidIdPort obtained");
    stub = (javax.xml.rpc.Stub) basicInvalidIdPort;
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
   * @class.setup_props: webServerHost; securedWebServicePort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("securedWebServicePort"));
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum, ENDPOINTURL);
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
   * @testName: secWarBasicNoId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195; WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB without any user id/password. Expect an
   * exception
   */
  public void secWarBasicNoId() throws Fault {
    TestUtil.logMsg("SecWarBasicNoId");
    try {
      if (vehicle.equals("appclient")) {
        TestUtil.logMsg("Skipping secEjbBasicNoId test for appclient vehicle");
        return;
      }
      String ret1 = basicNoIdPort.sayHelloBasic("secWarBasicNoId");
      TestUtil
          .logMsg("SecWarBasicNoId failed: unexpected return value " + ret1);
      throw new Fault("SecWarBasicNoId failed");
    } catch (RemoteException ex) {
      TestUtil.logMsg("SecWarBasicNoId success: got RemoteException");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test SecWarBasicNoId failed: got exception " + t.toString());
      throw new Fault("SecWarBasicNoId failed");
    }
    return;
  }

  /*
   * @testName: secWarBasicUnauthorizedId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195; WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB with a user id/password that's not
   * authorized for the port
   */
  public void secWarBasicUnauthorizedId() throws Fault {
    TestUtil.logMsg("SecWarBasicUnauthorizedId");
    try {
      String ret1 = basicUnauthorizedIdPort
          .sayHelloBasic("secWarBasicUnauthorizedId");
      TestUtil.logMsg(
          "SecWarBasicUnauthorizedId failed: unexpected return value " + ret1);
      throw new Fault("SecWarBasicUnauthorizedId failed");
    } catch (RemoteException ex) {
      TestUtil.logMsg("SecWarBasicUnauthorizedId success: got RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg("test SecWarBasicUnauthorizedId failed: got exception "
          + t.toString());
      throw new Fault("SecWarBasicUnauthorizedId failed");
    }
    return;
  }

  /*
   * @testName: secWarBasicInvalidId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195; WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB with an invalid id/password
   */
  public void secWarBasicInvalidId() throws Fault {
    TestUtil.logMsg("SecWarBasicInvalidId");
    try {
      if (vehicle.equals("appclient")) {
        TestUtil
            .logMsg("Skipping secEjbBasicInvalidId test for appclient vehicle");
        return;
      }
      String ret1 = basicInvalidIdPort.sayHelloBasic("secWarBasicInvalidId");
      TestUtil.logMsg(
          "SecWarBasicInvalidId failed: unexpected return value " + ret1);
      throw new Fault("SecWarBasicInvalidId failed");
    } catch (RemoteException ex) {
      TestUtil.logMsg("SecWarBasicInvalidId success: got RemoteException");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test SecWarBasicInvalidId failed: got exception " + t.toString());
      throw new Fault("SecWarBasicInvalidId failed");
    }
    return;
  }

  /*
   * #testName: secWarBasicAuthorizedId
   *
   * #assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195;
   * 
   * #test_Strategy: Call protected EJB with valid id/password.
   */
  public void secWarBasicAuthorizedId() throws Fault {
    TestUtil.logMsg("SecWarBasicAuthorizedId");
    try {
      String ret1 = basicAuthorizedIdPort
          .sayHelloBasic("secWarBasicAuthorizedId");
      if (!ret1.equals("'secWarBasicAuthorizedId' from HelloBasicImpl!")) {
        TestUtil
            .logMsg("test secWarBasicAuthorized failed: return value: " + ret1);
        throw new Fault("SecWarBasicAuthorized failed");
      }
      TestUtil.logMsg("SecWarBasicAuthorizedId passed");
    } catch (Throwable t) {
      t.printStackTrace();
      TestUtil.logMsg(
          "test SecWarBasicAuthorizedId failed: got exception " + t.toString());
      throw new Fault("SecWarBasicAuthorizedId failed");
    }
    return;
  }
}
