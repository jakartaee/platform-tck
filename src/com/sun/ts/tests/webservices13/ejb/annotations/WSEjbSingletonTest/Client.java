/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices13.ejb.annotations.WSEjbSingletonTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import javax.xml.ws.*;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.net.URL;
import java.util.Properties;
import java.util.Iterator;

public class Client extends EETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private Hello port;

  @WebServiceRef(name = "service/wsejbsingletontest")
  static WSEjbSingletonTestHelloService service;

  private void getPort() throws Exception {
    TestUtil
        .logMsg("Get wsejbsingletontest Service via @WebServiceRef annotation");
    TestUtil.logMsg("Uses no attribute @WebServiceRef");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Get port from service");
    port = (Hello) service.getHelloPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Port obtained");
    JAXWS_Util.dumpTargetEndpointAddress(port);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
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
      if (pass) {
        getPort();
      }
    } catch (Exception e) {
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

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: WSEjbSingletonTestCallHello
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187; WS4EE:SPEC:3000;
   * WS4EE:SPEC:3001; WS4EE:SPEC:3004; WS4EE:SPEC:3005;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * Implementation class is packaged in the ear file. The @WebServiceRef uses
   * the name attribute field to access the service ref.
   */
  public void WSEjbSingletonTestCallHello() throws Fault {
    TestUtil.logMsg("WSEjbSingletonTestCallHello");
    try {
      String txt = port.hello("Hi there");
      if (txt.equals("Hi there to you too!"))
        TestUtil.logMsg("WSEjbSingletonTestCallHello passed");
      else
        throw new RuntimeException("Msg returned from hello() incorrect");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("WSEjbSingletonTestCallHello failed");
    }
    return;
  }

  /*
   * @testName: WSEjbSingletonTestCallBye
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187; WS4EE:SPEC:3000;
   * WS4EE:SPEC:3001; WS4EE:SPEC:3004; WS4EE:SPEC:3005;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * Implementation class is packaged in the ear file. The @WebServiceRef uses
   * the name attribute field to access the service ref.
   */
  public void WSEjbSingletonTestCallBye() throws Fault {
    TestUtil.logMsg("WSEjbSingletonTestCallBye");
    try {
      String txt = port.bye("Bye-bye");
      if (txt.equals("Bye-bye and take care"))
        TestUtil.logMsg("WSEjbSingletonTestCallBye passed");
      else
        throw new RuntimeException("Msg returned from bye() incorrect");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("WSEjbSingletonTestCallBye failed");
    }
    return;
  }

  /*
   * @testName: WSEjbSingletonTestVerifyTargetEndpointAddress
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187; WS4EE:SPEC:3000;
   * WS4EE:SPEC:3001; WS4EE:SPEC:3004; WS4EE:SPEC:3005;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * Implementation class is packaged in the ear file. The @WebServiceRef uses
   * the name attribute field to access the service ref. The enpoint-address-uri
   * is specified in the runtime deployment descriptor. Verify that the target
   * endpoint ends with this enpoint-address-uri.
   */
  public void WSEjbSingletonTestVerifyTargetEndpointAddress() throws Fault {
    TestUtil.logMsg("WSEjbSingletonTestVerifyTargetEndpointAddress");
    try {
      String endpointaddr = JAXWS_Util.getTargetEndpointAddress(port);
      TestUtil.logMsg(
          "Verify that the target endpoint address ends with [WSEjbSingletonTestHelloService/HelloBean]");
      if (endpointaddr.endsWith("WSEjbSingletonTestHelloService/HelloBean"))
        TestUtil.logMsg("WSEjbSingletonTestVerifyTargetEndpointAddress passed");
      else
        throw new RuntimeException(
            "Target Endpoint Address is incorrect: " + endpointaddr);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("WSEjbSingletonTestVerifyTargetEndpointAddress failed");
    }
    return;
  }

  /*
   * @testName: WSEjbSingletonTestVerifyJNDILookupOfService
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * Implementation class is packaged in the ear file. The @WebServiceRef uses
   * the name attribute field to access the service ref. The runtime deployment
   * descriptor specifies the service ref. Verify that the container correctly
   * publishes the web service implementation under the service ref via the JNDI
   * name "java:comp/env/service/wsejbsingletontest".
   */
  public void WSEjbSingletonTestVerifyJNDILookupOfService() throws Fault {
    TestUtil.logMsg("WSEjbSingletonTestVerifyJNDILookupOfService");
    try {
      TestUtil.logMsg("Test JNDI lookup for wsejbsingletontest Service");
      InitialContext ctx = new InitialContext();
      WSEjbSingletonTestHelloService service = (WSEjbSingletonTestHelloService) ctx
          .lookup("java:comp/env/service/wsejbsingletontest");
      TestUtil.logMsg("service=" + service);
      TestUtil.logMsg("WSEjbSingletonTestVerifyJNDILookupOfService passed");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("WSEjbSingletonTestVerifyJNDILookupOfService failed");
    }
    return;
  }
}
