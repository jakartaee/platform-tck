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
package com.sun.ts.tests.webservices12.sec.descriptors.servlet.basicSSL;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;
import java.util.Iterator;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;
import jakarta.xml.ws.BindingProvider;
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

  private static final String ENDPOINTURL = "/WSSecWarBasicSSL_web/jaxws/WSSecWarBasicSSL";

  private void getPorts() throws Exception {
    ctx = new InitialContext();

    TestUtil.logMsg("JNDI lookup for basicNoIdService");
    basicNoIdService = (jakarta.xml.ws.Service) ctx
        .lookup("java:comp/env/service/wssecWar/basicNoId");
    TestUtil.logMsg("Get basicNoIdPort");
    basicNoIdPort = (HelloBasic) basicNoIdService.getPort(HelloBasic.class);
    TestUtil.logMsg("basicNoIdPort obtained");
    BindingProvider bindingProvider = (BindingProvider) basicNoIdPort;
    Map<String, Object> map = bindingProvider.getRequestContext();
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

    TestUtil.logMsg("JNDI lookup for basicAuthorizedIdService");
    basicAuthorizedIdService = (jakarta.xml.ws.Service) ctx
        .lookup("java:comp/env/service/wssecWar/basicAuthorizedId");
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
    basicUnauthorizedIdService = (jakarta.xml.ws.Service) ctx
        .lookup("java:comp/env/service/wssecWar/basicUnauthorizedId");
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
    basicInvalidIdService = (jakarta.xml.ws.Service) ctx
        .lookup("java:comp/env/service/wssecWar/basicInvalidId");
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

  public void setup(String[] args, Properties p) throws Exception {
    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("securedWebServicePort"));
      vehicle = p.getProperty("vehicle");
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum, ENDPOINTURL);
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });
      getPorts();
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    TestUtil.logMsg("cleanup ok");
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
   * @test_Strategy: Call protected EJB without any user id/password. Expect a
   * WebServiceException.
   */
  public void secWarBasicNoId() throws Exception {
    TestUtil.logMsg("SecWarBasicNoId");
    try {
      if (vehicle.equals("wsappclient")) {
        TestUtil.logMsg("Skipping secWarBasicNoId test for appclient vehicle");
        return;
      }
      String ret1 = basicNoIdPort.sayHelloBasic("secWarBasicNoId");
      TestUtil
          .logMsg("SecWarBasicNoId failed: unexpected return value " + ret1);
      throw new Exception("SecWarBasicNoId failed");
    } catch (WebServiceException ex) {
      TestUtil
          .logMsg("SecWarBasicNoId success: got expected WebServiceException");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test SecWarBasicNoId failed: got exception " + t.toString());
      throw new Exception("SecWarBasicNoId failed");
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
   * authorized for the port. Expect a WebServiceException.
   */
  public void secWarBasicUnauthorizedId() throws Exception {
    TestUtil.logMsg("SecWarBasicUnauthorizedId");
    try {
      String ret1 = basicUnauthorizedIdPort
          .sayHelloBasic("secWarBasicUnauthorizedId");
      TestUtil.logMsg(
          "SecWarBasicUnauthorizedId failed: unexpected return value " + ret1);
      throw new Exception("SecWarBasicUnauthorizedId failed");
    } catch (WebServiceException ex) {
      TestUtil.logMsg(
          "SecWarBasicUnauthorizedId success: got expected WebServiceException");
    } catch (Throwable t) {
      TestUtil.logMsg("test SecWarBasicUnauthorizedId failed: got exception "
          + t.toString());
      throw new Exception("SecWarBasicUnauthorizedId failed");
    }
    return;
  }

  /*
   * @testName: secWarBasicInvalidId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195; WS4EE:SPEC:9000; WS4EE:SPEC:9001;
   * 
   * @test_Strategy: Call protected EJB with an invalid id/password. Expect a
   * WebServiceException.
   */
  public void secWarBasicInvalidId() throws Exception {
    TestUtil.logMsg("SecWarBasicInvalidId");
    try {
      if (vehicle.equals("wsappclient")) {
        TestUtil
            .logMsg("Skipping secWarBasicInvalidId test for appclient vehicle");
        return;
      }
      String ret1 = basicInvalidIdPort.sayHelloBasic("secWarBasicInvalidId");
      TestUtil.logMsg(
          "SecWarBasicInvalidId failed: unexpected return value " + ret1);
      throw new Exception("SecWarBasicInvalidId failed");
    } catch (WebServiceException ex) {
      TestUtil.logMsg("SecWarBasicInvalidId success: got WebServiceException");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test SecWarBasicInvalidId failed: got exception " + t.toString());
      throw new Exception("SecWarBasicInvalidId failed");
    }
    return;
  }

  /*
   * @testName: secWarBasicAuthorizedId
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195;
   * 
   * @test_Strategy: Call protected EJB with valid id/password.
   */
  public void secWarBasicAuthorizedId() throws Exception {
    TestUtil.logMsg("SecWarBasicAuthorizedId");
    try {
      String ret1 = basicAuthorizedIdPort
          .sayHelloBasic("secWarBasicAuthorizedId");
      if (!ret1.equals("'secWarBasicAuthorizedId' from HelloBasicImpl!")) {
        TestUtil
            .logMsg("test secWarBasicAuthorized failed: return value: " + ret1);
        throw new Exception("SecWarBasicAuthorized failed");
      }
      TestUtil.logMsg("SecWarBasicAuthorizedId passed");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test SecWarBasicAuthorizedId failed: got exception " + t.toString());
      throw new Exception("SecWarBasicAuthorizedId failed");
    }
    return;
  }
}
