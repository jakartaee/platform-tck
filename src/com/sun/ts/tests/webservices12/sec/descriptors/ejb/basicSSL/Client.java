/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.webservices12.sec.descriptors.ejb.basicSSL;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;
import java.util.Iterator;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.BindingProvider;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;
import java.util.Map;
import javax.net.ssl.*;

public class Client extends ServiceEETest {
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

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String PROTOCOL = "https";

  private String urlString = null;

  private int portnum = 8000;

  private void getPorts() throws Exception {
    ctx = new InitialContext();

    TestUtil.logMsg("JNDI lookup for basicNoIdService");
    basicNoIdService = (javax.xml.ws.Service) ctx
        .lookup("java:comp/env/service/wssecEjb/basicNoId");
    TestUtil.logMsg("Get basicNoIdPort");
    basicNoIdPort = (HelloBasic) basicNoIdService.getPort(HelloBasic.class);
    TestUtil.logMsg("basicNoIdPort obtained");
    BindingProvider bindingProvider = (BindingProvider) basicNoIdPort;
    Map<String, Object> map = bindingProvider.getRequestContext();
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicAuthorizedIdService");
    basicAuthorizedIdService = (javax.xml.ws.Service) ctx
        .lookup("java:comp/env/service/wssecEjb/basicAuthorizedId");
    TestUtil.logMsg("Get basicAuthorizedIdPort");
    basicAuthorizedIdPort = (HelloBasic) basicAuthorizedIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicAuthorizedIdPort obtained");
    bindingProvider = (BindingProvider) basicAuthorizedIdPort;
    map = bindingProvider.getRequestContext();
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicUnauthorizedIdService");
    basicUnauthorizedIdService = (javax.xml.ws.Service) ctx
        .lookup("java:comp/env/service/wssecEjb/basicUnauthorizedId");
    TestUtil.logMsg("Get basicUnauthorizedIdPort");
    basicUnauthorizedIdPort = (HelloBasic) basicUnauthorizedIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicUnauthorizedIdPort obtained");
    bindingProvider = (BindingProvider) basicUnauthorizedIdPort;
    map = bindingProvider.getRequestContext();
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicInvalidIdService");
    basicInvalidIdService = (javax.xml.ws.Service) ctx
        .lookup("java:comp/env/service/wssecEjb/basicInvalidId");
    TestUtil.logMsg("Get basicInvalidIdPort");
    basicInvalidIdPort = (HelloBasic) basicInvalidIdService
        .getPort(HelloBasic.class);
    TestUtil.logMsg("basicInvalidIdPort obtained");
    bindingProvider = (BindingProvider) basicInvalidIdPort;
    map = bindingProvider.getRequestContext();
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap webservices-url-props.dat
   * 
   * @class.setup_props: webServerHost; securedWebServicePort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("securedWebServicePort"));
      vehicle = p.getProperty("vehicle");
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum,
          "/WSSecEjbHelloBasicSSL/ejb");
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });
      getPorts();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: secEjbBasicNoId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195; WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB without any user id/password. Expect a
   * WebServiceException.
   */
  public void secEjbBasicNoId() throws Fault {
    TestUtil.logMsg("SecEjbBasicNoId");
    try {
      if (vehicle.equals("wsappclient")) {
        TestUtil.logMsg("Skipping secEjbBasicNoId test for appclient vehicle");
        return;
      }
      String ret1 = basicNoIdPort.sayHelloBasic("secEjbBasicNoId");
      TestUtil
          .logMsg("SecEjbBasicNoId failed: unexpected return value " + ret1);
      throw new Fault("SecEjbBasicNoId failed");
    } catch (WebServiceException ex) {
      TestUtil
          .logMsg("SecEjbBasicNoId success: got expected WebServiceException");
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
   * WS4EE:SPEC:195; WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB with a user id/password that's not
   * authorized for the port. Expect a WebServiceException.
   */
  public void secEjbBasicUnauthorizedId() throws Fault {
    TestUtil.logMsg("SecEjbBasicUnauthorizedId");
    try {
      String ret1 = basicUnauthorizedIdPort
          .sayHelloBasic("secEjbBasicUnauthorizedId");
      TestUtil.logMsg(
          "SecEjbBasicUnauthorizedId failed: unexpected return value " + ret1);
      throw new Fault("SecEjbBasicUnauthorizedId failed");
    } catch (WebServiceException ex) {
      TestUtil.logMsg(
          "SecEjbBasicUnauthorizedId success: got expected WebServiceException");
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
   * WS4EE:SPEC:195; WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB with an invalid id/password. Expect a
   * WebServiceException.
   */
  public void secEjbBasicInvalidId() throws Fault {
    TestUtil.logMsg("SecEjbBasicInvalidId");
    try {
      if (vehicle.equals("wsappclient")) {
        TestUtil.logMsg("Skipping secEjbBasicNoId test for appclient vehicle");
        return;
      }
      String ret1 = basicInvalidIdPort.sayHelloBasic("secEjbBasicInvalidId");
      TestUtil.logMsg(
          "SecEjbBasicInvalidId failed: unexpected return value " + ret1);
      throw new Fault("SecEjbBasicInvalidId failed");
    } catch (WebServiceException ex) {
      TestUtil.logMsg("SecEjbBasicInvalidId success: got WebServiceException");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test SecEjbBasicInvalidId failed: got exception " + t.toString());
      throw new Fault("SecEjbBasicInvalidId failed");
    }
    return;
  }

  /*
   * @testName: secEjbBasicAuthorizedId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195;
   * 
   * @test_Strategy: Call protected EJB with valid id/password.
   */
  public void secEjbBasicAuthorizedId() throws Fault {
    TestUtil.logMsg("SecEjbBasicAuthorizedId");
    try {
      String ret1 = basicAuthorizedIdPort
          .sayHelloBasic("secEjbBasicAuthorizedId");
      if (!ret1.equals("'secEjbBasicAuthorizedId' from HelloBasicBean!")) {
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
