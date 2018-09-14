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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceRefTest2;

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

  @WebServiceRef(name = "service/wsejbwebservicereftest2")
  static HelloService service;

  private void getPort() throws Exception {
    TestUtil.logMsg(
        "Get wsejbwebservicereftest2 Service via @WebServiceRef annotation");
    TestUtil.logMsg(
        "Uses name attribute @WebServiceRef(name=\"service/wsejbwebservicereftest2\")");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Get port from service");
    port = (Hello) service.getHello();
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
      if (pass)
        getPort();
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
   * @testName: WSEjbWebServiceRefTest2CallHello
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * Implementation class is packaged in the ear file. The Remote interface is
   * also packaged in the ear file. The @WebServiceRef uses the name attribute
   * field to access the service ref.
   */
  public void WSEjbWebServiceRefTest2CallHello() throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefTest2CallHello");
    try {
      String txt = port.hello("Hi there");
      if (txt.equals("Hi there to you too!"))
        TestUtil.logMsg("WSEjbWebServiceRefTest2CallHello passed");
      else
        throw new RuntimeException("Msg returned from hello() incorrect");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("WSEjbWebServiceRefTest2CallHello failed");
    }
    return;
  }

  /*
   * @testName: WSEjbWebServiceRefTest2CallBye
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * Implementation class is packaged in the ear file. The Remote interface is
   * also packaged in the ear file. The @WebServiceRef uses the name attribute
   * field to access the service ref.
   */
  public void WSEjbWebServiceRefTest2CallBye() throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefTest2CallBye");
    try {
      String txt = port.bye("Bye-bye");
      if (txt.equals("Bye-bye and take care"))
        TestUtil.logMsg("WSEjbWebServiceRefTest2CallBye passed");
      else
        throw new RuntimeException("Msg returned from bye() incorrect");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("WSEjbWebServiceRefTest2CallBye failed");
    }
    return;
  }

  /*
   * @testName: WSEjbWebServiceRefTest2VerifyTargetEndpointAddress
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * Implementation class is packaged in the ear file. The Remote interface is
   * also packaged in the ear file. The @WebServiceRef uses the name attribute
   * field to access the service ref. The runtime deployment descriptor
   * specifies the endpoint address uri of "WSEjbWebServiceRefTest2/ejb". So
   * verify that "WSEjbWebServiceRefTest2/ejb" is part of the target endpoint
   * address.
   */
  public void WSEjbWebServiceRefTest2VerifyTargetEndpointAddress()
      throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefTest2VerifyTargetEndpointAddress");
    try {
      String endpointaddr = JAXWS_Util.getTargetEndpointAddress(port);
      TestUtil.logMsg(
          "Verify that the target endpoint address ends with [WSEjbWebServiceRefTest2/ejb]");
      if (endpointaddr.endsWith("WSEjbWebServiceRefTest2/ejb"))
        TestUtil.logMsg(
            "WSEjbWebServiceRefTest2VerifyTargetEndpointAddress passed");
      else
        throw new RuntimeException(
            "Target Endpoint Address is incorrect: " + endpointaddr);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(
          "WSEjbWebServiceRefTest2VerifyTargetEndpointAddress failed");
    }
    return;
  }

  /*
   * @testName: WSEjbWebServiceRefTest2VerifyJNDILookupOfService
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * Implementation class is packaged in the ear file. The Remote interface is
   * also packaged in the ear file. The @WebServiceRef uses the name attribute
   * field to access the service ref. Verify that the container correctly
   * publishes the web service implementation under
   * "java:comp/env/service-ref-name".
   * 
   */
  public void WSEjbWebServiceRefTest2VerifyJNDILookupOfService() throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefTest2VerifyJNDILookupOfService");
    try {
      TestUtil.logMsg("Test JNDI lookup for wsejbwebservicereftest2 Service");
      InitialContext ctx = new InitialContext();
      HelloService service = (HelloService) ctx
          .lookup("java:comp/env/service/wsejbwebservicereftest2");
      TestUtil.logMsg("service=" + service);
      TestUtil
          .logMsg("WSEjbWebServiceRefTest2VerifyJNDILookupOfService passed");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(
          "WSEjbWebServiceRefTest2VerifyJNDILookupOfService failed");
    }
    return;
  }
}
