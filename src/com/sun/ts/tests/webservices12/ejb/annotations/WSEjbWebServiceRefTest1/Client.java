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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceRefTest1;

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

  // URL properties used by the test
  private TSURL ctsurl = new TSURL();

  private static final String ENDPOINT_URL = "wsejbwebservicereftest1.endpoint";

  private static final String WSDLLOC_URL = "wsejbwebservicereftest1.wsdlloc";

  private String url = null;

  private static URL wsdlurl = null;

  private Hello port;

  @WebServiceRef
  static WSEjbWebServiceRefTest1HelloService service;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPort() throws Exception {
    TestUtil.logMsg(
        "Get wsejbwebservicereftest1 Service via @WebServiceRef annotation");
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
   * @class.testArgs: -ap webservices-url-props.dat
   * 
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
        getTestURLs();
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
   * @testName: WSEjbWebServiceRefTest1CallHello
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
   * no field attributes so the service ref is accessed by its default service
   * ref name.
   */
  public void WSEjbWebServiceRefTest1CallHello() throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefTest1CallHello");
    try {
      String txt = port.hello("Hi there");
      if (txt.equals("Hi there to you too!"))
        TestUtil.logMsg("WSEjbWebServiceRefTest1CallHello passed");
      else
        throw new RuntimeException("Msg returned from hello() incorrect");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("WSEjbWebServiceRefTest1CallHello failed");
    }
    return;
  }

  /*
   * @testName: WSEjbWebServiceRefTest1CallBye
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
   * no field attributes so the service ref is accessed by its default service
   * ref name.
   */
  public void WSEjbWebServiceRefTest1CallBye() throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefTest1CallBye");
    try {
      String txt = port.bye("Bye-bye");
      if (txt.equals("Bye-bye and take care"))
        TestUtil.logMsg("WSEjbWebServiceRefTest1CallBye passed");
      else
        throw new RuntimeException("Msg returned from bye() incorrect");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("WSEjbWebServiceRefTest1CallBye failed");
    }
    return;
  }

  /*
   * @testName: WSEjbWebServiceRefTest1VerifyTargetEndpointAddress
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
   * also packaged in the ear file. The @WebServiceRef uses no field attributes
   * so the service ref is accessed by its default service ref name. The runtime
   * deployment descriptor does not specify an endpoint address uri so the
   * endpoint address uri is implementation specific. For the JavaEE 6 RI the
   * endpoint address uri is calculated as "service name/simple bean name" which
   * is "WSEjbWebServiceRefTest1HelloService/HelloBean". For another JavaEE 6
   * implementation this can be different.
   */
  public void WSEjbWebServiceRefTest1VerifyTargetEndpointAddress()
      throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefTest1VerifyTargetEndpointAddress");
    try {
      String endpointaddr = JAXWS_Util.getTargetEndpointAddress(port);
      TestUtil.logMsg("Verify the target endpoint address");
      if (endpointaddr.equals(url))
        TestUtil.logMsg(
            "WSEjbWebServiceRefTest1VerifyTargetEndpointAddress passed");
      else
        throw new RuntimeException(
            "Target Endpoint Address is incorrect: " + endpointaddr);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(
          "WSEjbWebServiceRefTest1VerifyTargetEndpointAddress failed");
    }
    return;
  }

  /*
   * @testName: WSEjbWebServiceRefTest1VerifyJNDILookupOfService
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
   * no field attributes so the service ref is accessed by its default service
   * ref name. The runtime deployment descriptor specifies the default service
   * ref. Verify that the container correctly publishes the web service
   * implementation under the default service ref via the JNDI name
   * "java:comp/env/default-service-ref-name".
   */
  public void WSEjbWebServiceRefTest1VerifyJNDILookupOfService() throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefTest1VerifyJNDILookupOfService");
    try {
      TestUtil.logMsg("Test JNDI lookup for wsejbwebservicereftest1 Service");
      InitialContext ctx = new InitialContext();
      WSEjbWebServiceRefTest1HelloService service = (WSEjbWebServiceRefTest1HelloService) ctx
          .lookup(
              "java:comp/env/com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceRefTest1.Client/service");
      TestUtil.logMsg("service=" + service);
      TestUtil
          .logMsg("WSEjbWebServiceRefTest1VerifyJNDILookupOfService passed");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(
          "WSEjbWebServiceRefTest1VerifyJNDILookupOfService failed");
    }
    return;
  }
}
